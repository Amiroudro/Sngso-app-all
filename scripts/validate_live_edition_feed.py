#!/usr/bin/env python3
"""Smoke-test the public published-edition feed consumed by Android."""

from __future__ import annotations

import json
import re
from urllib.parse import urljoin, urlsplit
from urllib.request import Request, urlopen

BASE = "https://amiroudro.github.io/sarangsho-plus-editions/"
MAX_BYTES = 8 * 1024 * 1024


def fetch_json(path: str) -> dict:
    url = urljoin(BASE, path)
    request = Request(
        url,
        headers={"Accept": "application/json", "User-Agent": "SarangshoPlus-CI/1"},
    )
    with urlopen(request, timeout=20) as response:
        if response.status != 200:
            raise RuntimeError(f"{path}: unexpected HTTP status {response.status}")
        if urlsplit(response.geturl()).hostname != "amiroudro.github.io":
            raise RuntimeError(f"{path}: unexpected redirect host")
        payload = response.read(MAX_BYTES + 1)
    if len(payload) > MAX_BYTES:
        raise RuntimeError(f"{path}: response exceeds {MAX_BYTES} bytes")
    value = json.loads(payload.decode("utf-8"))
    if not isinstance(value, dict):
        raise RuntimeError(f"{path}: expected a JSON object")
    return value


def main() -> None:
    index = fetch_json("editions/index.json")
    editions = index.get("editions")
    if not isinstance(editions, list) or not editions:
        raise RuntimeError("published index has no editions")
    published = [
        item for item in editions
        if isinstance(item, dict)
        and item.get("status") == "published"
        and isinstance(item.get("edition_date"), str)
        and re.fullmatch(r"\d{4}-\d{2}-\d{2}", item["edition_date"])
    ]
    if not published:
        raise RuntimeError("published index has no valid published edition")
    latest = max(published, key=lambda item: item["edition_date"])
    date = latest["edition_date"]
    expected_file = f"data-{date}.json"
    if latest.get("file") != expected_file:
        raise RuntimeError("latest index record has an unexpected edition filename")

    edition = fetch_json(f"editions/{expected_file}")
    overlay = fetch_json(f"editions/bn-{date}.json")
    if edition.get("status") != "published" or edition.get("edition_date") != date:
        raise RuntimeError("latest edition file does not match the published index")
    if overlay.get("status") != "published" or overlay.get("edition_date") != date:
        raise RuntimeError("Bengali overlay is not published for the latest edition")
    if overlay.get("language") != "bn":
        raise RuntimeError("Bengali overlay has an unexpected language tag")

    stories = edition.get("stories")
    translated = overlay.get("stories")
    if not isinstance(stories, list) or not stories:
        raise RuntimeError("latest published edition has no stories")
    if not isinstance(translated, dict):
        raise RuntimeError("Bengali overlay has no story map")
    ids = [story.get("id") for story in stories if isinstance(story, dict)]
    if len(ids) != len(stories) or any(not isinstance(item, str) or not item for item in ids):
        raise RuntimeError("latest edition contains a story without an ID")
    if len(ids) != len(set(ids)):
        raise RuntimeError("latest edition contains duplicate story IDs")
    if set(ids) != set(translated):
        raise RuntimeError("Bengali overlay story IDs do not exactly match the edition")

    source_count = 0
    for story in stories:
        sources = story.get("sources", [])
        if not isinstance(sources, list):
            raise RuntimeError(f"{story['id']}: sources must be an array")
        for source in sources:
            if not isinstance(source, dict):
                raise RuntimeError(f"{story['id']}: source entry must be an object")
            parts = urlsplit(source.get("url", ""))
            if parts.scheme != "https" or not parts.hostname or parts.username or parts.password or parts.fragment:
                raise RuntimeError(f"{story['id']}: source URL must be a safe HTTPS URL")
            if not source.get("publication") or not source.get("headline"):
                raise RuntimeError(f"{story['id']}: source attribution is incomplete")
            source_count += 1

    print(
        f"PASS: {date} is published; {len(stories)} stories, "
        f"{source_count} HTTPS source citations, and a complete Bengali overlay."
    )


if __name__ == "__main__":
    main()
