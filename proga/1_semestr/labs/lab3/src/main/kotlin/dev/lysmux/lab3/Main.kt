package dev.lysmux.lab3

import dev.lysmux.lab3.common.Case
import dev.lysmux.lab3.entity.Glow
import dev.lysmux.lab3.entity.Substance
import dev.lysmux.lab3.entity.ability.EmitRays
import dev.lysmux.lab3.entity.ability.GlowsInDark
import dev.lysmux.lab3.entity.item.MoonStone
import dev.lysmux.lab3.entity.people.Person
import dev.lysmux.lab3.entity.people.ShortyGroup
import dev.lysmux.lab3.entity.place.Nature
import dev.lysmux.lab3.entity.place.Table
import dev.lysmux.lab3.entity.ray.Ray
import dev.lysmux.lab3.entity.ray.RayKind
import dev.lysmux.lab3.entity.ray.VisibilityType
import dev.lysmux.lab3.storage.IStoryStorage
import dev.lysmux.lab3.storage.StoryStorageImpl
import dev.lysmux.lab3.storage.exception.StoryExistsException
import dev.lysmux.lab3.story.SentenceFactory
import dev.lysmux.lab3.story.Story
import dev.lysmux.lab3.story.exception.EmptyStoryException
import dev.lysmux.lab3.util.Direction
import dev.lysmux.lab3.util.Util

fun main() {
    val story = Story()

    val person = Person()
    val shortyGroup = ShortyGroup("рассевшимися вокруг")

    val moonStone = MoonStone()
    val table = Table()
    val glow = Glow()

    val glowsSubstance = Substance(GlowsInDark())
    val raysSubstance = Substance(EmitRays(
        Ray(RayKind.LIGHT, VisibilityType.VISIBLE),
        Ray(RayKind.ULTRAVIOLET, VisibilityType.HIDDEN),
        Ray(RayKind.INFRARED, VisibilityType.HIDDEN),
        Ray(RayKind.COSMIC, VisibilityType.HIDDEN)
    ))

    story.addSentences(
        SentenceFactory.createSentence(
            Util.strCapitalize(person.toString()),
            person.put(
                moonStone,
                table,
                Direction(
                    Direction.Preposition.IN_FRONT_OF,
                    shortyGroup.getDescription()
                )
            )
        )
            .and(person.tell())
            .that(glowsSubstance.meet(Nature()))
            .which(glowsSubstance.acquireAbility())
            .after(glowsSubstance.influenced(Ray(RayKind.LIGHT))),
        SentenceFactory.createSentence(
            "Такое",
            glow.toString(),
            "называется",
            glow.getAlternativeName()
        ),
        SentenceFactory.createSentence(
            "Некоторые",
            raysSubstance.caseDeclension(Case.NOMINATIVE),
            raysSubstance.acquireAbility()
        )
    )

    try {
        println(story.makeStory())
    } catch (e: EmptyStoryException) {
        System.err.println(e.message)
    }

    println("Сохраняем рассказ...")
    val storage: IStoryStorage = StoryStorageImpl
    try {
        storage.save("Some рассказ", story)
    } catch (e: StoryExistsException) {
        System.err.println(e.message)
    }
}