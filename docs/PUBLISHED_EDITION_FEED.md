# Published edition feed integration

## Delivery source

The Android client reads the public, published daily-edition feed maintained in
[Amiroudro/sarangsho-plus-editions](https://github.com/Amiroudro/sarangsho-plus-editions):

- Index: `https://amiroudro.github.io/sarangsho-plus-editions/editions/index.json`
- English edition: `editions/data-YYYY-MM-DD.json`
- Bengali overlay: `editions/bn-YYYY-MM-DD.json`

The index is authoritative for which edition is current. The client selects the
latest record whose status is exactly `published`, verifies the date/file
binding, then requires the downloaded edition to carry the same date and
published status. A Bengali overlay is accepted only when its language, date,
and publication status match the English edition.

## Runtime behaviour

- HTTPS only, fixed GitHub Pages host, bounded connect/read timeouts.
- Only the expected `data-YYYY-MM-DD.json` filename is accepted from the index.
- The latest edition and matching Bengali overlay are cached in app-private
  SharedPreferences. The client never writes to the publisher repository.
- Cache entries younger than 15 minutes are reused; older entries are refreshed
  when Home or Feed is opened.
- Manual refresh is available on both Home and Feed.
- If refresh fails, the last valid cached published edition remains readable
  and is visibly marked as an offline copy. With no cache, the app displays an
  explicit connection/error state rather than fabricated sample news.
- Story source links are accepted only as HTTPS URLs and open in the system
  browser.

## Content mapping

The client maps the published edition's stories, Bangla overlay, key facts,
exam facts, summaries, and source citations into the Home, Feed, and Article
screens. Missing Bangla overlay fields fall back to the English edition text.
Source publication, headline, date, and canonical URL remain visible.

## Boundary with the new editorial engine contract

This feed is the existing public daily-edition publishing channel. It is not
the newer engine's client-edition v1 projection and does not imply that the
new engine's staging artifact has been promoted to production. The separate
contract parser remains a validation component; the new engine's PostgreSQL
cutover and authenticated publisher workflow are not connected by this change.

## Verification still required

CI validates the Android build and tests. Before broad client distribution,
verify the app against the live feed on a physical Android device, test a
network outage with an existing cache, and confirm the deployed Pages index
and latest edition remain reachable.
