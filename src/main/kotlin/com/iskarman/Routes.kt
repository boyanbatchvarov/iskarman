package com.iskarman

import io.ktor.http.Cookie
import io.ktor.server.application.call
import io.ktor.server.html.respondHtml
import io.ktor.server.response.respondRedirect
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import kotlinx.html.HTML
import kotlinx.html.a
import kotlinx.html.body
import kotlinx.html.details
import kotlinx.html.div
import kotlinx.html.h1
import kotlinx.html.h2
import kotlinx.html.h3
import kotlinx.html.li
import kotlinx.html.head
import kotlinx.html.id
import kotlinx.html.link
import kotlinx.html.main
import kotlinx.html.meta
import kotlinx.html.nav
import kotlinx.html.p
import kotlinx.html.script
import kotlinx.html.section
import kotlinx.html.source
import kotlinx.html.summary
import kotlinx.html.title
import kotlinx.html.ul
import kotlinx.html.unsafe
import kotlinx.html.video

fun Route.landingRoutes() {
    get("/locale/{lang}") {
        val lang = call.parameters["lang"]
        if (lang == "en" || lang == "bg") {
            call.response.cookies.append(
                Cookie(
                    name = "lang",
                    value = lang,
                    maxAge = 60 * 60 * 24 * 365,
                    path = "/",
                ),
            )
        }
        val redirectTo = call.request.queryParameters["redirect"] ?: "/"
        call.respondRedirect(redirectTo)
    }

    get("/") {
        val lang = Messages.locale(call.request.cookies["lang"])
        val countdown = CountdownCalculator.calculate()
        val targetEpochMs = CountdownCalculator.target.toInstant().toEpochMilli()

        call.respondHtml {
            renderPage(lang, countdown, targetEpochMs)
        }
    }
}

private fun HTML.renderPage(lang: String, countdown: Countdown, targetEpochMs: Long) {
    val m: (String) -> String = { key -> Messages.get(lang, key) }

    attributes["lang"] = if (lang == "bg") "bg" else "en"
    head {
        meta(charset = "UTF-8")
        meta(name = "viewport", content = "width=device-width, initial-scale=1.0")
        title { +m("site.title") }
        link(rel = "stylesheet", href = "/static/style.css")
    }
    body {
        div(classes = "path-bg") {}
        div(classes = "path-marker") {
            attributes["aria-hidden"] = "true"
        }

        div(classes = "locale") {
            a(
                href = "/locale/en?redirect=${encodeRedirect("/")}",
                classes = "locale-flag" + if (lang == "en") " active" else "",
            ) {
                attributes["aria-label"] = m("locale.en")
                +"🇬🇧"
            }
            a(
                href = "/locale/bg?redirect=${encodeRedirect("/")}",
                classes = "locale-flag" + if (lang == "bg") " active" else "",
            ) {
                attributes["aria-label"] = m("locale.bg")
                +"🇧🇬"
            }
        }

        details(classes = "menu") {
            summary { +m("menu.label") }
            nav {
                a(href = "#info") { +m("menu.info") }
                a(href = "#track-rules") { +m("menu.trackRules") }
                a(href = "#contacts") { +m("menu.contacts") }
                a(href = "#media") { +m("menu.media") }
                a(href = "#results") { +m("menu.results") }
            }
        }

        main(classes = "content") {
            div(classes = "hero") {
                div(classes = "hero-inner") {
                    h1 { +m("site.title") }
                    div(classes = "countdown") {
                        id = "countdown"
                        attributes["data-target"] = targetEpochMs.toString()
                        attributes["data-label-months"] = m("countdown.months")
                        attributes["data-label-days"] = m("countdown.days")
                        attributes["data-label-hours"] = m("countdown.hours")
                        attributes["data-label-minutes"] = m("countdown.minutes")
                        attributes["data-label-seconds"] = m("countdown.seconds")
                        unsafe {
                            +renderCountdownHtml(countdown, m)
                        }
                    }
                    p(classes = "tagline") { +m("site.tagline") }
                }
            }

            section {
                id = "info"
                h2 { +m("section.info.title") }
                p { +m("section.info.body1") }
                p { +m("section.info.body2") }
                p {
                    +"${m("section.info.facebook")} "
                    externalLink(
                        href = "https://www.facebook.com/events/1255118769960613",
                        label = m("link.facebookEvent"),
                    )
                }
                p(classes = "links") {
                    externalLink(
                        href = "https://www.windy.com/42.441/23.622?waves,42.439,23.622,16",
                        label = m("link.windy"),
                    )
                    +" · "
                    externalLink(
                        href = "https://www.google.com/maps/dir//%D0%92%D0%B5%D1%82%D1%80%D0%BE%D1%85%D0%BE%D0%B4%D0%BD%D0%B0+%D0%B1%D0%B0%D0%B7%D0%B0+%D0%98%D1%81%D0%BA%D1%8A%D1%80,+%D0%A0%D0%B0%D0%B9%D0%BE%D0%BD,+1137+Pancharevo/data=!4m6!4m5!1m1!4e2!1m2!1m1!1s0x14ab330f222d8221:0x9ea127539ef3ce95?sa=X&ved=1t:57443&ictx=111",
                        label = m("link.bulsailing"),
                    )
                    +" · "
                    externalLink(
                        href = "https://www.facebook.com/events/1255118769960613",
                        label = m("link.facebookEvent"),
                    )
                    +" · "
                    externalLink(
                        href = "https://earth.google.com/earth/d/1eNhSkv5ELvCFUorgYsnxWjl4ziWdRt9U?usp=sharing",
                        label = m("link.googleEarth"),
                    )
                }
            }
            section {
                id = "track-rules"
                h2 { +m("section.trackRules.title") }
                h3 { +m("section.track.title") }
                p { +m("section.track.intro") }
                p { +m("section.track.5km") }
                p { +m("section.track.10km") }
                h3 { +m("section.rules.title") }
                ul {
                    li { +m("section.rules.1") }
                    li { +m("section.rules.2") }
                    li { +m("section.rules.3") }
                    li { +m("section.rules.4") }
                    li { +m("section.rules.5") }
                }
                h3 { +m("section.baseCamp.title") }
                p { +m("section.baseCamp.body") }
            }
            section {
                id = "contacts"
                h2 { +m("section.contacts.title") }
                p {
                    externalLink(
                        href = "https://www.facebook.com/boyan.batchvarov/",
                        label = m("link.boyan"),
                    )
                }
                p {
                    externalLink(
                        href = "https://www.facebook.com/profile.php?id=61592439134555",
                        label = m("link.facebookPage"),
                    )
                }
                p {
                    externalLink(
                        href = "https://www.facebook.com/events/1255118769960613",
                        label = m("link.facebookEvent"),
                    )
                }
            }
            section {
                id = "media"
                h2 { +m("section.media.title") }
                video(classes = "media-video") {
                    attributes["controls"] = ""
                    attributes["playsinline"] = ""
                    attributes["preload"] = "metadata"
                    source {
                        src = "/static/swim_video.mp4"
                        type = "video/mp4"
                    }
                }
            }
            section {
                id = "results"
                h2 { +m("section.results.title") }
                h3 { +m("section.results.2026") }
                p { +m("section.results.2026.body") }
            }
        }

        script(src = "/static/countdown.js") {}
        script(src = "/static/path-follow.js") {}
    }
}

private fun renderCountdownHtml(countdown: Countdown, m: (String) -> String): String =
    """
    <span class="countdown-value">${countdown.months}</span>
    <span class="countdown-unit">${m("countdown.months")}</span>
    <span class="countdown-sep">:</span>
    <span class="countdown-value">${countdown.days}</span>
    <span class="countdown-unit">${m("countdown.days")}</span>
    <span class="countdown-sep">:</span>
    <span class="countdown-value">${countdown.hours}</span>
    <span class="countdown-unit">${m("countdown.hours")}</span>
    <span class="countdown-sep">:</span>
    <span class="countdown-value">${countdown.minutes}</span>
    <span class="countdown-unit">${m("countdown.minutes")}</span>
    <span class="countdown-sep">:</span>
    <span class="countdown-value">${countdown.seconds}</span>
    <span class="countdown-unit">${m("countdown.seconds")}</span>
    """.trimIndent()

private fun encodeRedirect(path: String): String =
    java.net.URLEncoder.encode(path, Charsets.UTF_8)

private fun kotlinx.html.FlowContent.externalLink(href: String, label: String) {
    a(href = href) {
        attributes["target"] = "_blank"
        attributes["rel"] = "noopener noreferrer"
        +label
    }
}
