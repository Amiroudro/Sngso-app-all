# Sarangsho Plus visual system

## Visual premise
A working editorial desk for curious readers and serious exam candidates. The interface should feel composed, literate and locally grounded: warm paper, dark ink, disciplined teal, and a small amount of ochre for emphasis. It should not resemble a generic AI dashboard.

## Design decisions
- Use typography and whitespace to establish hierarchy before adding containers.
- Reserve filled teal surfaces for primary actions and a small number of high-value editorial moments.
- Use warm neutral surfaces for reading and comparison; keep borders quiet.
- Prefer square-ish editorial cards with 12–16dp corners over a screen full of pills.
- Keep category, publisher, timestamp, reading time and verification status in a stable metadata order.
- Use Bangla for primary explanation and retain English terms where they are standard exam vocabulary.
- Do not use emoji as interface icons. Use Material Symbols / vector icons with content descriptions.
- Use real, licensed editorial imagery only when a verified image asset is available. Never use decorative stock photos to fill empty space.
- Avoid fabricated charts, progress, source counts, ratings, or activity streaks.

## Tokens
| Token | Value | Use |
|---|---|---|
| Paper | #F7F5EF | App canvas |
| Surface | #FFFFFF | Reading surfaces |
| Ink | #172B32 | Primary text |
| Muted | #637279 | Supporting text |
| Teal | #0D5960 | Primary action |
| Deep teal | #123B43 | Brand moments |
| Ochre | #C58A36 | Restrained emphasis |
| Line | #E5E2D9 | Dividers and borders |
| Soft | #F0EEE7 | Quiet supporting surfaces |
| Positive | #287A59 | Confirmed positive state |

## Typography and reading
- Use system sans-serif with native Bengali fallback; test Noto Sans Bengali glyph coverage on physical devices.
- Headlines should wrap naturally and must not be forced into a single line.
- Article body defaults should prioritize comfortable line length and 1.45–1.6 line-height.
- Support Android font scaling. Never clip Bangla vowel marks or conjuncts.
- Keep English abbreviations and official titles legible inside Bangla paragraphs.

## Interaction and accessibility
- Minimum touch target: 48dp.
- Every icon-only control needs a localized content description.
- Provide clear selected, pressed, disabled, loading and error states.
- Maintain 4.5:1 contrast for normal text.
- Respect system reduced-motion and font-size settings.
- Keyboard and switch navigation must follow visual order.
- Empty states should explain what is missing and provide one useful next action.

## Quality review
Review each screen at 360dp, 393dp and 600dp widths, with font scale 1.0 and 1.3. Inspect Bengali line wrapping, TalkBack labels, dark/system theme behavior, offline state, and long content before marking a screen production-ready.
