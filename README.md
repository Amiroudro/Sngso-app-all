# Sarangsho Plus — Android Client

A Bengali-first editorial intelligence and exam-preparation app for Bangladesh.

This repository contains the Android client. The editorial engine lives separately in [Amiroudro/new-sngso](https://github.com/Amiroudro/new-sngso). The client consumes versioned, validated edition artifacts; it does not scrape publishers or generate editorial claims itself.

## Product experience

Sarangsho Plus helps readers move from a verified news event to understanding, context, and exam practice.

### Core destinations

1. Home briefing
2. News feed and topic filters
3. Article reader
4. Three-level summary (30 seconds / 2 minutes / detailed)
5. Story timeline
6. Newspaper perspectives (only when eligible source records exist)
7. Bangladesh affairs
8. World affairs explorer
9. Knowledge graph
10. Data and fact explorer
11. Vocabulary lab
12. BCS and Bank challenge
13. Answer explanation
14. Written practice
15. Revision deck
16. Search
17. Saved items
18. Notifications
19. Learning analytics
20. Profile and settings

Each destination must also handle loading, empty, offline, stale-data, error, and content-unavailable states where relevant.

## Technical direction

- Kotlin + Jetpack Compose
- Material 3 components, with a custom editorial design system
- Navigation Compose
- Coroutines and Flow
- Room for local saved items and learning progress
- DataStore for preferences
- Retrofit/OkHttp only when a versioned API is introduced
- Offline-first reading of downloaded edition artifacts
- No paid service required for the first client milestone

## Design principles

- Bengali-first typography with explicit font fallback and tested line height.
- A restrained ink / warm-paper / deep-teal palette; no generic gradient-heavy dashboard.
- Strong editorial hierarchy: headlines, metadata, evidence, context, and learning actions must be visually distinct.
- Reading surfaces should prioritize line length, contrast, spacing, and adjustable text size.
- Charts must show units, periods, source, and whether values are preliminary or revised.
- Never display invented progress, source comparisons, ratings, or learning analytics.
- Every interactive control must have a real action and visible feedback.
- Respect system font scaling, reduced motion, TalkBack semantics, and touch target sizes.

## Repository status

The repository was empty when initialized. The first implementation milestone is the design system, navigation shell, and screen inventory. Build and device verification must be recorded separately; source code being committed is not proof that an Android build has passed.

## Related project

- Editorial engine: [Amiroudro/new-sngso](https://github.com/Amiroudro/new-sngso)
- UI/UX guidance: [UI UX Pro Max](https://github.com/nextlevelbuilder/ui-ux-pro-max-skill)
- Design critique and quality guidance: [Impeccable](https://github.com/pbakaus/impeccable)
