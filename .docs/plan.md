# iskarman.com — Plan

Action items to get the site from a local landing page to a published, content-rich presence.

## Hosting

- [ ] Choose a hosting target (e.g. VPS, PaaS, or container platform)
- [ ] Point `iskarman.com` DNS at the deployment
- [ ] Configure TLS/HTTPS (Let's Encrypt or provider-managed certs)
- [ ] Set up a production run command or fat JAR deploy (`./gradlew shadowJar` or `./gradlew run`)
- [ ] Define environment config (port, logging) for production
- [ ] Add health checks and basic uptime monitoring

## Repository

- [ ] Confirm remote origin and default branch
- [ ] Add CI to build and test on push (Gradle)
- [ ] Automate deploy from main (or tagged releases)
- [ ] Document local dev setup in README (already started)
- [ ] Decide on branching / release workflow

## Media

- [ ] Collect and organize photos from past events
- [ ] Add video embeds or self-hosted clips (YouTube/Vimeo vs. static assets)
- [ ] Create or source a Google Earth flyover of the swim course (Iskar reservoir / Pancharevo)
- [ ] Optimize images (sizes, formats, lazy loading)
- [ ] Wire the **Media** section (`#media`) with real content instead of placeholder copy
- [ ] Consider a CDN or object storage for large assets if not served from the app

## Content

- [ ] Finalize English and Bulgarian copy in `messages_en.properties` / `messages_bg.properties`
- [ ] Expand **Info**: course, date, rules, registration, safety
- [ ] Flesh out **Contacts** beyond email if needed (social, org links)
- [ ] Review external links (Windy, Bulsailing, maps) and keep URLs current
- [ ] Add meta tags / Open Graph for sharing
- [ ] Proofread both locales before launch
