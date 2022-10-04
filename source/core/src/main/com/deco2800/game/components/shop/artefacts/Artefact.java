package com.deco2800.game.components.shop.artefacts;

import java.util.Arrays;
import java.util.List;

public enum Artefact {
    HEALTH_POTION,
    CLOCK,
    BED;
    // SKULL;

    public static String getFilepath(Artefact artefact) {
        return switch (artefact) {
            case HEALTH_POTION -> "configs/artefacts/healthPotion.json";
            case CLOCK -> "configs/artefacts/clock.json";
            case BED -> "configs/artefacts/bed.json";
            // case SKULL -> "configs/artefacts/skull.json";
        };
    }

    public static List<Artefact> getAllartefactTypes() {
        List<Artefact> all = Arrays.asList(Artefact.class.getEnumConstants());
        return all;
    }

}
