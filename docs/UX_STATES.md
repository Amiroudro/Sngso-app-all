# UX state contract

Every data-backed screen must define the states below before it is considered complete.

| State | Required behaviour |
|---|---|
| Initial | Reserve layout space; show a meaningful skeleton for content loading |
| Success | Show real data with provenance and timestamps where relevant |
| Empty | Explain why there is no content and offer one useful next action |
| Offline + cached | Keep reading available and label the last sync time |
| Offline + uncached | Explain that the content needs a connection and offer retry |
| Error | Describe the problem in user language and provide a recovery action |
| Partial | Show what loaded and identify what is unavailable |
| Stale | Keep content visible, show last updated time and offer refresh |
| Disabled | Explain why the feature is unavailable; do not present a dead control |

## Editorial trust states

- Verified: the claim passed the content pipeline's verification gate.
- Primary source: directly supported by an official or original source.
- Reported: attributed to a publisher or named source.
- Allegation: explicitly attributed and not presented as established fact.
- Analysis: interpretation, clearly separated from reported facts.
- Pending: not yet cleared for publication.
- Unavailable: source could not be accessed or use is not permitted.

These labels must be driven by editorial metadata, not manually inferred by the UI.

## Interaction contract

- All controls respond within the same interaction cycle with pressed/selected feedback.
- Network actions show loading and failure states.
- Save, download, submit and delete actions confirm the resulting state.
- Destructive actions require a clear confirmation and a reversible path where possible.
- Forms use persistent labels, inline validation and preserve entered data after errors.
- Every icon-only action has a localized accessibility label.
- A disabled action is visibly disabled and has a reason available to TalkBack.
- No fake progress, fake scores, fake notifications or fake source counts.

## Reading contract

- Body text defaults to a comfortable reading measure and 1.5 line height.
- Font scaling must not clip Bengali conjuncts, vowel marks or punctuation.
- The source, publication time, update time and verification status remain available without interrupting reading.
- Summary depth changes content in place; it should not unexpectedly reset scroll or lose reading position.
- Timeline dates distinguish event date, publication date and update date.
- Comparison views show only eligible, accessible source records.
