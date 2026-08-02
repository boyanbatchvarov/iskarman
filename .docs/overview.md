# Overview

**iskarman.com** is a bilingual landing page for the Iskarman 10 km open-water swimming challenge. It shows a live countdown to the event (6 September 2026, Europe/Sofia) and sections for info, contacts, and media.

## Tech stack

| Layer | Choice |
|-------|--------|
| Language | Kotlin (JVM 21) |
| Server | [Ktor](https://ktor.io/) 3.x on Netty |
| HTML | Server-rendered via kotlinx-html DSL |
| Client JS | Vanilla `countdown.js` (tick updates in the browser) |
| Styling | Static CSS |
| i18n | Property files (`messages_en`, `messages_bg`) + `lang` cookie |
| Build | Gradle (Kotlin DSL) |
| Config | `application.yaml` (port 8080 locally) |

HTMX is noted in the README as available via CDN; the current page is mostly static HTML with a small client script for the countdown.

## Project structure

```
iskarman/
├── build.gradle.kts          # Dependencies, Ktor plugin, JVM toolchain
├── settings.gradle.kts
├── src/main/
│   ├── kotlin/com/iskarman/
│   │   ├── Application.kt    # Entry: static files + route wiring
│   │   ├── Routes.kt         # `/`, `/locale/{lang}`, page HTML
│   │   ├── Countdown.kt      # Target date and countdown math
│   │   └── Messages.kt       # Locale bundles and lookups
│   └── resources/
│       ├── application.yaml  # Ktor module and port
│       ├── messages_*.properties
│       ├── logback.xml
│       └── static/           # style.css, countdown.js
└── .docs/                    # Planning and project notes
```

## Runtime flow

1. `EngineMain` loads `Application.module`.
2. `/static/*` is served from `resources/static`.
3. `GET /` reads the `lang` cookie, computes the countdown server-side, and renders the landing page.
4. `GET /locale/{en|bg}` sets the cookie and redirects back.
5. `countdown.js` refreshes the display every second until the target time.

## Run locally

```bash
./gradlew run
```

Open [http://localhost:8080](http://localhost:8080).
