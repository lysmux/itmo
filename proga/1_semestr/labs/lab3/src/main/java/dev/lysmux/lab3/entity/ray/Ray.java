package dev.lysmux.lab3.entity.ray;

import dev.lysmux.lab3.common.Case;
import dev.lysmux.lab3.common.HasCase;
import dev.lysmux.lab3.common.Pluralable;

import java.util.Objects;

public class Ray implements Pluralable, HasCase {


    protected VisibilityType visibility;
    protected RayKind kind;

    public Ray(RayKind kind, VisibilityType visibility) {
        this.visibility = visibility;
        this.kind = kind;
    }

    public Ray(RayKind kind) {
        this(kind, VisibilityType.NONE);
    }

    public VisibilityType getVisibility() {
        return visibility;
    }

    public void setVisibility(VisibilityType visibility) {
        this.visibility = visibility;
    }

    public RayKind getKind() {
        return kind;
    }

    public void setKind(RayKind kind) {
        this.kind = kind;
    }

    @Override
    public String getPluralName() {
        return "лучи";
    }

    @Override
    public String caseDeclension(Case to_case) {
        return switch (to_case) {
            case NOMINATIVE, ACCUSATIVE -> "луч";
            case GENITIVE -> "луча";
            case DATIVE -> "лучу";
            case CREATIVE -> "лучом";
            case PREPOSITIONAL -> "луче";
        };
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ray ray)) return false;
        if (!super.equals(o)) return false;
        return visibility == ray.visibility && kind == ray.kind;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), visibility, kind);
    }

    @Override
    public String toString() {
        return "луч";
    }
}
