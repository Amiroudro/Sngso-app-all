#!/usr/bin/env python3
"""Validate the proposed client-edition fixture and its integrity hash.

This checks structural compatibility only. It does not establish editorial
accuracy, publication rights, or approval of the proposed contract.
"""
from __future__ import annotations

import copy
import hashlib
import json
from pathlib import Path

from jsonschema import Draft202012Validator, FormatChecker

ROOT = Path(__file__).resolve().parents[1]
SCHEMA_PATH = ROOT / "contracts/client-edition/v1/schema.json"
FIXTURE_PATH = ROOT / "contracts/client-edition/v1/fixtures/published-edition.sample.json"


def canonical_payload(document: dict) -> bytes:
    payload = copy.deepcopy(document)
    payload.pop("content_hash", None)
    return json.dumps(
        payload,
        ensure_ascii=False,
        sort_keys=True,
        separators=(",", ":"),
    ).encode("utf-8")


def has_valid_hash(document: dict) -> bool:
    expected = "sha256:" + hashlib.sha256(canonical_payload(document)).hexdigest()
    return document.get("content_hash") == expected


def validate_document(validator: Draft202012Validator, document: dict) -> list[str]:
    return [
        f"{'.'.join(str(part) for part in error.absolute_path) or '<root>'}: {error.message}"
        for error in validator.iter_errors(document)
    ]


def main() -> None:
    schema = json.loads(SCHEMA_PATH.read_text(encoding="utf-8"))
    fixture = json.loads(FIXTURE_PATH.read_text(encoding="utf-8"))
    Draft202012Validator.check_schema(schema)
    validator = Draft202012Validator(schema, format_checker=FormatChecker())

    errors = validate_document(validator, fixture)
    if errors:
        raise SystemExit("Fixture does not match schema:\n- " + "\n- ".join(errors))

    expected_hash = "sha256:" + hashlib.sha256(canonical_payload(fixture)).hexdigest()
    if not has_valid_hash(fixture):
        raise SystemExit(
            "Fixture content_hash mismatch: "
            f"expected {expected_hash}, got {fixture.get('content_hash')!r}"
        )

    # Regression checks: each unsafe or unsupported payload must be rejected.
    invalid_cases = {
        "unknown contract version": lambda d: d.update(contract_version="2.0.0"),
        "unpublished edition": lambda d: d["edition"].update(status="draft"),
        "unapproved summary": lambda d: d["stories"][0]["summaries"][0].update(status="draft"),
        "missing source attribution": lambda d: d["stories"][0].pop("source"),
        "missing story identifier": lambda d: d["stories"][0].pop("id"),
    }
    for label, mutate in invalid_cases.items():
        candidate = copy.deepcopy(fixture)
        mutate(candidate)
        if not validate_document(validator, candidate):
            raise SystemExit(f"Regression check failed: accepted {label}")

    bad_hash = copy.deepcopy(fixture)
    bad_hash["content_hash"] = "sha256:" + ("0" * 64)
    if validate_document(validator, bad_hash):
        raise SystemExit("Regression check failed: bad hash test payload should remain schema-valid")
    if has_valid_hash(bad_hash):
        raise SystemExit("Regression check failed: accepted content-hash mismatch")

    print("Client-edition v1 schema: valid")
    print(f"Published-edition fixture: valid ({len(fixture['stories'])} story)")
    print(f"Canonical SHA-256: {expected_hash}")
    print(f"Rejected invalid regression cases: {len(invalid_cases)}")


if __name__ == "__main__":
    main()
