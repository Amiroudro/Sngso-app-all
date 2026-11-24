# Screen index

Current prototype route inventory: **48 destinations**. This count is descriptive, not a product requirement. Screens are grouped by user task; a new route is added only when it gives the user a distinct place to complete a task or understand a state.

| # | Route | Screen | Group |
|---:|---|---|---|
| 1 | welcome | Welcome | First run |
| 2 | onboarding-goals | Learning goals | First run |
| 3 | onboarding-interests | Topic interests | First run |
| 4 | signin | Sign in | Account |
| 5 | signup | Create account | Account |
| 6 | password-reset | Password recovery | Account |
| 7 | home | Daily briefing | Read |
| 8 | archive | Edition archive | Read |
| 9 | edition | Edition overview | Read |
| 10 | feed | Discover / news feed | Discover |
| 11 | search | Search | Discover |
| 12 | article | Story reader | Read |
| 13 | summary | Three-depth summary | Read |
| 14 | timeline | Story timeline | Read |
| 15 | perspectives | Publication perspectives | Read |
| 16 | sources | Sources and verification | Trust |
| 17 | source-detail | Source detail | Trust |
| 18 | report-story | Report a correction | Trust |
| 19 | bangladesh | Bangladesh affairs | Topics |
| 20 | world | World affairs | Topics |
| 21 | topic-detail | Topic lesson | Topics |
| 22 | graph | Knowledge map | Learn |
| 23 | data | Data explorer | Learn |
| 24 | indicator-detail | Indicator detail | Learn |
| 25 | vocabulary | Vocabulary lab | Learn |
| 26 | word-detail | Word detail | Learn |
| 27 | quiz-setup | Practice setup | Practice |
| 28 | challenge | Question session | Practice |
| 29 | answer | Answer explanation | Practice |
| 30 | quiz-results | Session result shell | Practice |
| 31 | practice-history | Practice history | Practice |
| 32 | writing | Written practice | Practice |
| 33 | revision | Revision deck | Practice |
| 34 | study-plan | Study plan | Practice |
| 35 | saved | Saved overview | Library |
| 36 | saved-collections | Saved collections | Library |
| 37 | reading-history | Reading history | Library |
| 38 | downloads | Offline library | Library |
| 39 | notifications | Notification centre | Account |
| 40 | profile | Profile | Account |
| 41 | settings | Settings | Account |
| 42 | reading-settings | Reading preferences | Account |
| 43 | language | Language preferences | Account |
| 44 | privacy | Privacy and data | Account |
| 45 | help | Help centre | Support |
| 46 | feedback | Feedback | Support |
| 47 | about | About Sarangsho Plus | Support |
| 48 | learn | Learn hub | Learn |

## Primary navigation

The four persistent destinations are Today, Learn, Library and Explore. Profile, notifications and settings are reached from the top bar. Other routes are opened from a relevant context and return to their parent.

## Implementation status

The route shell and first-pass layouts exist. This does not mean every feature is connected:
- Some routes are honest empty/unavailable states pending engine, account or download services.
- Authentication, cloud sync, real editorial search, edition API integration, publication comparisons and production analytics are not yet implemented.
- Saved items, reading preferences, onboarding choices, notification preferences, drafts, reading history, practice history and the daily study checklist use device-local SharedPreferences.
- The quiz and study interactions remain prototypes with sample content; local practice history is not a verified measure of exam readiness.
- The source and correction screens are UI shells; they do not send data.
- Routine CI has passed build and test-source compilation. Emulator execution and real-device visual verification are still pending.
