# Sarangsho Plus screen architecture

This is a product inventory, not a target screen count. The current prototype exposes 47 navigation destinations, while several capabilities remain honest empty or disabled shells until their services exist. A route should exist only when it supports a distinct user task, a meaningful decision, or a necessary system state. Related content belongs in a detail section or bottom sheet when that is clearer than another page.

## Primary navigation

The app keeps four top-level destinations, matching the most frequent jobs:

1. **Today** — today's edition, reading continuation and the next useful learning action.
2. **Learn** — vocabulary, exam practice, written practice, revision and study planning.
3. **Library** — saved stories, vocabulary, revision cards and downloaded editions.
4. **Explore** — news feed, topics, publications and search.

Profile, notifications and settings are reached from the top bar.

## First-run preference behavior

- The learning goal and selected topic interests are stored on-device using the local learning preference store.
- Returning to onboarding preselects the saved values; completing the flow returns to Today and the briefing surfaces the chosen goal and topics.
- These preferences are device-local. They are not attached to an account or synchronized.
- The prototype does not yet use these preferences to filter live editorial content.

## Screen families

### First run and identity
- Welcome
- Learning goal selection
- Topic interests
- Sign in
- Password recovery
- Account creation
- Permissions explanation (only when a permission is actually needed)
- First-run completion

### Daily reading
- Home briefing
- Edition archive
- Edition overview
- Story feed
- Story detail
- 30-second summary
- 2-minute summary
- Full explainer
- Story timeline
- Newspaper perspective comparison
- Source list
- Publisher/source detail
- Bangladesh topics
- World affairs topics
- Topic detail
- Knowledge graph
- Data explorer
- Indicator detail
- Related stories

### Exam preparation
- Practice hub
- Exam/subject selection
- Quiz setup
- Question session
- Answer feedback
- Quiz result
- Attempt review
- Practice history
- Written prompt
- Writing draft
- Vocabulary list
- Vocabulary detail
- Flashcard review
- Revision schedule
- Study plan
- Progress overview

### Personal library
- Saved overview
- Saved stories
- Saved words
- Saved questions
- Collections/folders
- Downloaded editions
- Download manager
- Reading history
- Continue reading

### Account and support
- Profile
- Notification centre
- Notification preferences
- App settings
- Reading preferences (font, size, line spacing, theme)
- Language preference
- Privacy and data controls
- About Sarangsho Plus
- Help and FAQ
- Feedback/report a problem

### System and recovery states
- Initial loading
- Content skeleton
- Empty edition
- Empty search
- No saved items
- Offline with cached content
- Offline without cached content
- Network retry
- Source unavailable / metadata only
- Content pending verification
- Content updated since last read
- Download queued / downloading / paused / failed / complete
- Sign-in validation and authentication failure
- Permission denied
- Storage unavailable
- Generic recoverable error

## Route rules

- A top-level navigation destination must remain stable and have a clear selected state.
- Detail pages retain their parent context and return to the originating list.
- Search filters should be progressive: common filters first, advanced filters in a sheet.
- Do not make a page for every summary depth; use a single story reading surface with a depth selector.
- Do not make separate pages for every category when one topic template can render the category data.
- Do not show a screen for a feature that has no implemented data or interaction. Use an honest disabled state or keep it out of navigation.
- Deep links should be stable and shareable for editions, stories, topics, words and practice sets.
