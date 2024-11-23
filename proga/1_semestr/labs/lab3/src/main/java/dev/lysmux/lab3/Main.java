package dev.lysmux.lab3;

import dev.lysmux.lab3.common.Case;
import dev.lysmux.lab3.entity.Glow;
import dev.lysmux.lab3.entity.Substance;
import dev.lysmux.lab3.entity.ability.EmitRays;
import dev.lysmux.lab3.entity.ability.GlowsInDark;
import dev.lysmux.lab3.entity.item.MoonStone;
import dev.lysmux.lab3.entity.people.Person;
import dev.lysmux.lab3.entity.people.ShortyGroup;
import dev.lysmux.lab3.entity.place.Nature;
import dev.lysmux.lab3.entity.place.Table;
import dev.lysmux.lab3.entity.ray.Ray;
import dev.lysmux.lab3.entity.ray.RayKind;
import dev.lysmux.lab3.entity.ray.VisibilityType;
import dev.lysmux.lab3.storage.IStoryStorage;
import dev.lysmux.lab3.storage.StoryStorageImpl;
import dev.lysmux.lab3.storage.exception.StoryExistsException;
import dev.lysmux.lab3.story.SentenceFactory;
import dev.lysmux.lab3.story.Story;
import dev.lysmux.lab3.story.exception.EmptyStoryException;
import dev.lysmux.lab3.util.Direction;
import dev.lysmux.lab3.util.Util;

public class Main {
    public static void main(String[] args) {
        Story story = new Story();

        Person person = new Person();
        ShortyGroup shortyGroup = new ShortyGroup("рассевшимися вокруг");

        MoonStone moonStone = new MoonStone();
        Table table = new Table();
        Glow glow = new Glow();

        Substance glows_substance = new Substance(new GlowsInDark());
        Substance rays_substance = new Substance(new EmitRays(
                new Ray(RayKind.LIGHT, VisibilityType.VISIBLE),
                new Ray(RayKind.ULTRAVIOLET, VisibilityType.HIDDEN),
                new Ray(RayKind.INFRARED, VisibilityType.HIDDEN),
                new Ray(RayKind.COSMIC, VisibilityType.HIDDEN)
        ));

        story.addSentences(
                SentenceFactory.createSentence(
                                Util.capitalize(person.toString()),
                                person.put(
                                        moonStone,
                                        table,
                                        new Direction(
                                                Direction.Preposition.IN_FRONT_OF,
                                                shortyGroup.getDescription()
                                        )
                                )
                        )
                        .and(person.tell())
                        .that(glows_substance.meet(new Nature()))
                        .which(glows_substance.acquireAbility())
                        .after(glows_substance.influenced(new Ray(RayKind.LIGHT))),
                SentenceFactory.createSentence(
                        "Такое",
                        glow.toString(),
                        "называется",
                        glow.getAlternativeName()
                ),
                SentenceFactory.createSentence(
                        "Некоторые",
                        rays_substance.caseDeclension(Case.NOMINATIVE),
                        rays_substance.acquireAbility()
                )
        );

        try {
            System.out.println(story.makeStory());
        } catch (EmptyStoryException e) {
            System.err.println(e.getMessage());
        }

        System.out.println("Сохраняем рассказ...");
        IStoryStorage storage = StoryStorageImpl.getInstance();
        try {
            storage.save("Some рассказ", story);
        } catch (StoryExistsException e) {
            System.err.println(e.getMessage());
        }
    }
}

