# Product

<!-- impeccable:product-schema 1 -->

## Platform

android

## Stack

Kotlin, Jetpack Compose, Material 3, Navigation Compose. The editorial/news engine is maintained separately and will be integrated through a versioned content contract.

## Users

Primary users are Bangladeshi readers preparing for BCS, bank recruitment and other competitive examinations. They read Bangla-first explanations, retain useful English terms, and need to understand both current events and the underlying institutions, laws, economics and history. Secondary users are general readers who want verified, contextual news.

## Product Purpose

Sarangsho Plus turns current affairs into a structured learning experience. A reader should be able to understand an event quickly, inspect its chronology and sources, connect it to broader concepts, practise exam-style questions, and revisit what they learned.

## Positioning

The product connects a source-aware daily briefing to a reusable study system: three-depth summaries, event timelines, eligible multi-publication comparisons, topic knowledge maps, data explainers, vocabulary, practice questions and spaced revision. Editorial content is produced by a separate engine; the Android client presents approved editions and supports personal learning.

## Operating Context

- Primarily used on Android phones in Bangladesh, often in short reading sessions and sometimes with unreliable connectivity.
- Bangla is the primary explanatory language; standard English terms, abbreviations and official names remain visible with explanations.
- The home surface prioritizes the current daily edition and a clear next learning action.
- Reading, search, practice, saved items and profile/settings are the four primary navigation destinations (Today, Learn, Library, Explore), with Profile/settings available from the top bar.
- Deep links should open a specific edition, story, topic, word or practice set.

## Capabilities and Constraints

- The current branch contains an early Compose screen prototype, not a complete verified application.
- The repository has a working Gradle Android project scaffold. Routine CI runs one JVM smoke test, compiles the 36 authored Android instrumentation tests into a test APK and assembles a debug APK. Instrumentation compilation is not an emulator runtime test.
- The separate editorial engine and its rights/verification rules remain authoritative for news content.
- Sample content must be labelled as sample. Never fabricate live stories, source comparisons, user activity, progress, scores or data values.
- Authentication, cloud persistence/synchronization, offline editions, push delivery, analytics and engine APIs are not connected unless separately implemented and verified. Several prototype features persist locally with SharedPreferences; local storage must not be mistaken for account sync or a production data layer.
- The client must not publish or approve editorial content.
- Prefer a free/open-source implementation and avoid paid service dependencies by default.

## Topic study guides

Selecting a Bangladesh or World Affairs topic now opens a topic-specific local study guide. Each guide contains a concise overview, core concepts, exam-reading cautions and a written reflection prompt. These are learning scaffolds, not verified current-affairs content. Live topic feeds and topic-specific question banks remain future integrations.

## First-run preferences

The onboarding flow saves a learner's primary goal and topic interests locally on the device. The Today screen reflects those choices. They are not account-synced and do not yet filter editorial content.

## Brand Commitments

- Product name: Sarangsho Plus.
- The interface is Bangla-first, clear, serious, readable and locally grounded.
- It should feel like a carefully edited digital reading product, not a generic AI dashboard.
- Preserve source transparency, factual restraint and a calm editorial tone.

## Evidence on Hand

- app/src/main/java/com/sarangsho/plus/MainActivity.kt contains the first-pass Compose prototype.
- DESIGN.md records the existing paper, ink and teal visual language.
- The content engine is separate from this Android repository.
- No verified production API, account system, published Android build or user analytics are present in this branch.

## Product Principles

1. Evidence before decoration: never let visual polish imply that unverified data is verified.
2. Reading first: typography, line length, source context and navigation serve comprehension.
3. Learn from the news: connect current events to concepts, timelines, facts, vocabulary and practice.
4. Offline-resilient by design: cached reading and graceful network states are core product requirements.
5. Honest functionality: every control either works, is explicitly disabled with an explanation, or is visibly marked as a prototype.

## Accessibility & Inclusion

- Meet WCAG 2.1 AA contrast as a baseline for text and meaningful controls.
- Support Android font scaling and Bengali shaping without clipping.
- Minimum touch targets are 48dp.
- Use semantic Compose controls and meaningful content descriptions.
- Respect system reduced-motion preferences and support light/dark themes.
- Keep navigation and reading usable with TalkBack, keyboard and switch access.
