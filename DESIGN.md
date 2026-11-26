# Sarangsho Plus design system

## Product mode and visual direction

**Mode:** Operate for navigation, settings, practice and personal library; Read for story, timeline, source, data and topic surfaces.

**Creative direction: The Bengali reading desk.** The app belongs to the world of daily newspapers, exam notebooks and carefully annotated reference books. It uses a quiet paper field, ink-like typography and a restrained teal signal. It should feel like a useful publication, not a generic AI dashboard.

The existing Compose prototype already establishes this visual world. Extend it; do not replace it with a trend-driven style.

## Design principles

- Reading surfaces are editorial and quiet. Let headlines, source metadata and the story lead.
- Functional screens use familiar Android patterns and predictable navigation.
- Use one strong visual focus per screen. Supporting actions stay secondary.
- Prefer typography, alignment and whitespace to repeated cards.
- A surface or border must communicate grouping or state. Do not wrap every paragraph in a card.
- Do not build pages from identical icon-plus-heading cards.
- Do not put an eyebrow/kicker above every section heading.
- Use icons from the Material icon family, never emoji as structural controls.
- Use real, rights-cleared editorial images only when the story has a verified asset. Never fill space with decorative stock imagery.
- Never display invented metrics, activity streaks, source counts, charts or quiz scores.
- Show the source and verification state as editorial metadata, not as decorative badges.

## Semantic color tokens

| Token | Light | Dark | Use |
|---|---|---|---|
| Background | #F7F5EF | #11191C | App canvas |
| Surface | #FFFFFF | #1A2529 | Reading surface |
| Primary text | #172B32 | #E9EFED | Headlines and body |
| Secondary text | #637279 | #B3C0BC | Supporting copy |
| Primary action | #0D5960 | #0D5960 | Main action and selection |
| Deep brand | #123B43 | #123B43 | Brand moments |
| Accent | #C58A36 | #C58A36 | Restrained emphasis |
| Divider | #E5E2D9 | #354348 | Rules and separators |
| Quiet surface | #F0EEE7 | #263438 | Subtle grouping |
| Success | #287A59 | #287A59 | Confirmed success |
| Error | #B3261E | #FFB4AB | Failure only |

All UI colors are centralized in SarangshoTheme.kt. Screens should use MaterialTheme.colorScheme or SarangshoPalette. Do not add per-screen hex values.

## Typography

- Native system sans-serif for interface controls and Bengali body text.
- Test Bengali glyph shaping with Noto Sans Bengali on physical Android devices.
- Keep display headlines distinct through size and weight, not decorative font pairing.
- Body text should target 16–18sp and 1.45–1.6 line height, with user font scaling respected.
- Keep long-form reading measure comfortable on tablets; do not stretch paragraphs edge to edge.
- Do not clip conjuncts, vowel marks, punctuation or mixed Bangla-English terms.

## Shape, spacing and depth

- Use a 4dp/8dp spacing rhythm.
- Standard content gutters: 16–20dp on phones; expand on wider layouts.
- Use 8–14dp corner radii where grouping needs a surface. Prefer flat editorial rules for article content.
- Shadows are rare and reserved for elevated controls or sheets. No hard-offset shadows, glow halos or decorative glass.
- Touch targets are at least 48dp even when the visible icon is smaller.

## Navigation and interaction

- Four primary destinations: Today, Learn, Library, Explore. Profile and settings remain available from the top bar.
- Detail screens retain a predictable back path to their parent.
- Use in-place segmented controls for summary depth; do not create redundant pages for each depth.
- Use a filter sheet for advanced discovery filters rather than crowding the feed.
- Give every asynchronous action loading, success and failure feedback.
- Disabled controls must be visibly disabled and explain why they are unavailable.
- Most state transitions should complete in 150–250ms and respect reduced-motion settings.
- Use state hoisting and unidirectional data flow; business state belongs in ViewModels, not screen composables.

## Accessibility

- Minimum 48dp touch target.
- Text contrast at least 4.5:1 for normal text; meaningful icons and boundaries at least 3:1.
- Every icon-only control has a localized content description.
- Use semantic Compose controls and expose selected/expanded/disabled states.
- Support TalkBack, keyboard, switch access and system font scaling.
- Respect system dark mode and reduced motion.
- Keep focus order aligned with visual reading order.

## Quality gates

A screen is not production-ready until it has:
- Real or explicitly sample content with provenance.
- Loading, empty, error, offline and partial states as applicable.
- Working primary actions and honest disabled states for unimplemented services.
- Small-phone, large-phone, tablet and landscape layout checks.
- Light and dark theme checks.
- Bengali text checks at default and enlarged font scale.
- Accessibility semantics and a testable UI contract.
- A visual critique pass that removes generic card grids, repeated eyebrow labels, placeholder controls and decorative noise.
