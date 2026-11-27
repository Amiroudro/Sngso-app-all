# Sarangsho Plus — Screen map and interaction contract

## Navigation model
Four persistent destinations form the primary navigation: Today, Learn, Library and Explore. Profile, notifications and settings are reached from the top bar. Detail experiences are pushed onto the navigation stack and retain a clear back path. Today links to the edition archive, and Vocabulary Lab opens a detail view for the selected word. Search is reachable from primary surfaces. Do not place every feature in the bottom bar.

## Twenty primary screens
| ID | Screen | Entry | Primary action | Essential states |
|---|---|---|---|---|
| 01 | Welcome / onboarding | First launch | Continue / skip | First run, returning |
| 02 | Sign in | Profile / gated sync | Sign in / continue locally | Validation, loading, failure |
| 03 | Home briefing | Today tab | Open briefing | Fresh, quiet day, stale, offline |
| 04 | News feed | Explore tab | Filter and open story | Loading, empty filter, offline |
| 05 | Article reader | Feed item | Save, share, open summary | Full, metadata-only, unavailable |
| 06 | Three-level summary | Article action | Change depth | Available, partial, unavailable |
| 07 | Story timeline | Article action | Inspect event | Verified, incomplete chronology |
| 08 | Newspaper perspectives | Article action | Compare source records | Complete, partial, not run |
| 09 | Bangladesh affairs | Explore | Open subject | Topic list, no current update |
| 10 | World affairs explorer | Explore | Select region / issue | Region selected, no results |
| 11 | Knowledge graph | Article / topic | Follow relationship | Graph, no connected evidence |
| 12 | Data & fact explorer | Article / topic | Inspect indicator | Data available, revised, conflict |
| 13 | Vocabulary lab | Learn hub | Reveal meaning / save word | New, known, review |
| 14 | BCS & Bank challenge | Learn hub | Submit answer | In progress, submitted, expired |
| 15 | Answer explanation | After answer | Review evidence / next | Correct, incorrect, disputed |
| 16 | Written practice | Learn hub | Save draft | Empty, editing, saved |
| 17 | Revision deck | Learn hub | Reveal / rate recall | Due, complete, no cards |
| 18 | Search & sources | Top-bar search | Search / filter | Recent, results, no results |
| 19 | Saved items | Library tab | Open / remove | Saved, empty, offline |
| 20 | Profile | Top-bar profile action | Settings / review progress | New learner, populated |

## Shared interaction requirements
- Back returns to the previous meaningful context.
- Every tappable row has a full-row hit target and visible pressed state.
- Filters expose selected state and can be cleared.
- Empty states explain why the page is empty and offer a relevant next action.
- Errors explain whether retry is safe; preserve user-entered work.
- Offline reading distinguishes downloaded content from stale cached content.
- Missing publisher perspectives are shown as Not available or Not run, never synthesized.
- Charts include title, unit, time period, source and revision note.
- Quiz options are mutually exclusive; submission locks the response and reveals an explanation.
- Text scaling must not clip Bangla glyphs or truncate key metadata.
- TalkBack labels describe controls and chart summaries, not decorative shapes.

## Design system
- Brand: editorial, contemporary Bangladeshi, calm and evidence-led.
- Canvas: warm paper #F7F5EF; primary surface #FFFFFF.
- Ink: #172B32; secondary ink #53636A.
- Brand teal: #0D5960; deep teal: #123B43.
- Accent: muted ochre #C58A36; positive #287A59; warning #A65D25; danger #B54742.
- Borders: #E5E2D9; subtle surface #F0EEE7.
- Typography: platform sans for UI; Bengali fallback must be checked on real devices. Headlines use restrained weight and generous line height.
- Shape: 12–16dp cards, 10dp controls, avoid pill-shaped everything.
- Spacing: 4dp base scale; 16dp page gutter; 20–24dp section rhythm.
- Motion: short state transitions only; respect reduced-motion preferences.

## Editorial integrity
UI may display only fields supplied by validated edition data. Never invent source counts, publication times, score, user progress, or chart values. Keep reported claims, official statements, analysis, and background visibly distinct.
