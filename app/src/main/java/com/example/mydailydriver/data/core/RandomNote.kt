package com.example.mydailydriver.data.core

import io.github.serpro69.kfaker.faker
import kotlin.random.Random

class Note {

    // ✅ Korrekte Syntax für 1.16.0 mit deutschem Locale
    private val faker = faker {
        fakerConfig {
            locale = "de"
        }
    }
    private val rnd = Random.Default

    fun randomName(): String =
        faker.name.name()

    fun randomAddress(): String =
        listOf(
            faker.address.streetAddress(),
            "${faker.address.postcode()} ${faker.address.city()}",
            faker.address.country()
        ).joinToString(", ")

    // ✅ lorem.words() gibt einzelne Wörter zurück – daraus Sätze bauen
    fun randomLoremSentences(sentenceCount: Int = 3): String =
        (1..sentenceCount).joinToString(" ") {
            faker.lorem.words().replaceFirstChar { it.uppercase() } + "."
        }

    fun randomParagraph(sentenceCount: Int = 5): String =
        randomLoremSentences(sentenceCount)

    fun randomCharacterDescription(): String {
        val name = randomName()
        val age = (16..30).random()
        val job = faker.job.title()
        val trait1 = faker.hobby.activity()
        val trait2 = faker.book.title()
        // ✅ quote.famous() existiert nicht – stattdessen z.B. movie.quote()
        val habit = faker.movie.quote()
        return buildString {
            appendLine("$name, $age Jahre, $job.")
            appendLine("Eigenschaften: $trait1, $trait2.")
            appendLine("Kleine Eigenheit: $habit")
            append("Kurzbeschreibung: ${randomLoremSentences(2)}")
        }
    }

    fun randomNote(): String {
        val generators: List<() -> String> = listOf(
            ::randomName,
            ::randomAddress,
            { randomLoremSentences(3) },
            { randomParagraph(4) },
            ::randomCharacterDescription
        )
        return generators[rnd.nextInt(generators.size)].invoke()
    }
}