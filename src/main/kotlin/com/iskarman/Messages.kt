package com.iskarman

import java.util.Properties

object Messages {
    private val bundles = mapOf(
        "en" to load("messages_en"),
        "bg" to load("messages_bg"),
    )

    fun locale(cookieLang: String?): String =
        if (cookieLang == "bg") "bg" else "en"

    fun get(lang: String, key: String): String {
        val bundle = bundles[lang] ?: bundles.getValue("en")
        return bundle.getProperty(key) ?: bundles.getValue("en").getProperty(key, key)
    }

    private fun load(name: String): Properties {
        val properties = Properties()
        val stream = Messages::class.java.classLoader.getResourceAsStream("$name.properties")
            ?: error("Missing resource: $name.properties")
        stream.use { properties.load(it) }
        return properties
    }
}
