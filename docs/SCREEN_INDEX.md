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
| 9 | learn | Learn hub | Learn |
| 9 | archive | Edition archive | Read |
| 10 | edition | Edition overview | Read |
| 11 | feed | Discover / news feed | Discover |
| 12 | search | Search | Discover |
| 13 | article | Story reader | Read |
| 14 | summary | Three-depth summary | Read |
| 15 | timeline | Story timeline | Read |
| 16 | perspectives | Publication perspectives | Read |
| 17 | sources | Sources and verification | Trust |
| 18 | source-detail | Source detail | Trust |
| 19 | report-story | Report a correction | Trust |
| 20 | bangladesh | Bangladesh affairs | Topics |
| 21 | world | World affairs | Topics |
| 22 | topic-detail | Topic lesson | Topics |
| 23 | graph | Knowledge map | Learn |
| 24 | data | Data explorer | Learn |
| 25 | indicator-detail | Indicator detail | Learn |
| 26 | vocabulary | Vocabulary lab | Learn |
| 27 | word-detail | Word detail | Learn |
| 28 | quiz-setup | Practice setup | Practice |
| 29 | challenge | Question session | Practice |
| 30 | answer | Answer explanation | Practice |
| 31 | quiz-results | Session result shell | Practice |
| 32 | practice-history | Practice history | Practice |
| 33 | writing | Written practice | Practice |
| 34 | revision | Revision deck | Practice |
| 35 | study-plan | Study plan | Practice |
| 36 | saved | Saved overview | Library |
| 37 | saved-collections | Saved collections | Library |
| 38 | reading-history | Reading history | Library |
| 39 | downloads | Offline library | Library |
| 40 | notifications | Notification centre | Account |
| 41 | profile | Profile | Account |
| 42 | settings | Settings | Account |
| 43 | reading-settings | Reading preferences | Account |
| 44 | language | Language preferences | Account |
| 45 | privacy | Privacy and data | Account |
| 46 | help | Help centre | Support |
| 47 | feedback | Feedback | Support |
| 48 | about | About Sarangsho Plus | Support |


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
