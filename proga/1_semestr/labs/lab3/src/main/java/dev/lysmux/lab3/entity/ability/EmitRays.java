package dev.lysmux.lab3.entity.ability;

import dev.lysmux.lab3.entity.ray.Ray;

import java.util.List;
import java.util.Objects;

public class EmitRays extends Ability {
    protected Ray emmitedRay;
    protected List<Ray> influencingRays;

    public EmitRays(Ray emmitedRay, Ray... influencingRays) {;
        this.emmitedRay = emmitedRay;
        this.influencingRays = List.of(influencingRays);
    }

    public String influenced() {
        List<String> ray_kinds = influencingRays.stream().map(ray -> ray.getKind().getValue()).toList();

        return "под влиянием " + switch (ray_kinds.size()) {
            case 1 -> influencingRays.get(0).toString();
            case 2 -> String.join("или", ray_kinds.subList(0, 1));
            default -> String.join(", ", ray_kinds.subList(0, ray_kinds.size() - 1))
                    + " или " + ray_kinds.get(ray_kinds.size() - 1);
        } + " лучей";
    }

    @Override
    public String getDescription() {
        return String.join(
                " ",
                "испускать",
                emmitedRay.getVisibility().getValue(),
                emmitedRay.getPluralName(),
                emmitedRay.getKind().getValue(),
                "даже",
                influenced()
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmitRays emitRays = (EmitRays) o;
        return Objects.equals(emmitedRay, emitRays.emmitedRay)
                && Objects.equals(influencingRays, emitRays.influencingRays);
    }

    @Override
    public int hashCode() {
        return Objects.hash(emmitedRay, influencingRays);
    }

    @Override
    public String toString() {
        return "испускать лучи";
    }
}
