# Proposed client edition projection v1

**Status: proposal for review. Not an implemented endpoint and not yet approved by the editorial-engine repository.**

This projection is an Android-facing read model. It deliberately does not expose the engine's persistence model or assume a hosting provider, URL, authentication method, or deployment.

## Why a separate projection

The engine's v1 Edition, Story, Article and LearningObject contracts describe editorial domain records. The Android app needs a compact read-only payload with the exact metadata its screens render. A projection keeps UI code independent from engine internals and can be versioned separately.

## Files

- `schema.json`: JSON Schema Draft 2020-12 for the proposed payload.
- `fixtures/published-edition.sample.json`: synthetic bilingual fixture. The publisher, story and text are fictional. It must never be displayed as live news.

## Rules

1. Only a published edition is accepted. Draft, in-review, approved-but-unpublished, rejected and superseded editions are not eligible for this client feed.
2. Only approved summary variants are included. The client must not silently substitute an unapproved translation.
3. Stable opaque IDs identify editions, stories and sources. Titles and array positions are never identifiers.
4. Every story includes publisher/source name, canonical source URL and source publication timestamp.
5. Dates are RFC 3339 timestamps with explicit timezone information.
6. Unknown major contract versions, invalid schema, missing required references and content-hash mismatches must fail closed.
7. The client may retain the last known-good cached edition if refresh fails, but must show its publication time and stale state.
8. Corrections and supersession are explicit. A new edition must not silently mutate a previously published snapshot.
9. The projection contains summaries and metadata only. It does not authorize storage or republication of publisher full text, images or excerpts.

## Content hash

Compute SHA-256 over the UTF-8 bytes of the JSON object after removing the top-level `content_hash` property, sorting object keys recursively, and serializing with no insignificant whitespace and unescaped Unicode. Prefix the lowercase hexadecimal digest with `sha256:`. Arrays retain their order. A consumer recomputes and compares this digest before displaying content.

The sample fixture's hash was computed using this rule.

## Mapping from engine domain

| Client projection | Engine source |
|---|---|
| `edition.id`, `edition.edition_key`, status and publication time | Published Edition |
| `stories[].id`, title and topic tags | Story |
| `stories[].source` | Article + Source, resolved through the edition's story/article relationships |
| `stories[].summaries[]` | Approved LearningObject summary variants or an explicitly approved summary projection |
| `correction`, `supersedes_edition_id` | Edition correction/supersession metadata |

The engine must define and validate the exact mapping before this proposal is adopted. In particular, the source relationship must be explicit and must not be guessed when a story has multiple articles or publishers.

## Decisions still required

- Whether the engine will emit this projection directly or an exporter will transform validated domain records.
- How the edition's ordered story list is represented and whether it needs an explicit rank.
- Whether multiple sources per story should be represented as a list rather than the single-source MVP shape.
- Where artifacts are hosted and how the client discovers the latest published edition.
- Cache retention limits and the user-visible freshness policy.

No Android network permission, HTTP dependency or production endpoint should be added until these decisions and an engine-owned fixture are agreed.
