package com.otherlands.magic.utility;

import it.unimi.dsi.fastutil.Pair;
import org.apache.commons.lang3.tuple.Triple;

import java.util.HashSet;
import java.util.Set;

public class PatternChecker {

    public static Pair<Boolean, Set<Spell>> checkForPattern(Integer currentSlot) {
        boolean found = false;
        Set<Spell> pattern = new HashSet<>();
        for (int x = 0; x < SpellRegistry.INSTANCE.spells.size(); x++) {
            if (SpellRegistry.INSTANCE.spells.values().stream().toList().get(x).getSequence().startsWith(currentSlot.toString())) {
                pattern.add(SpellRegistry.INSTANCE.spells.values().stream().toList().get(x));
                found = true;
            }
        }

        return Pair.of(found, pattern);
    }

    public static Triple<Boolean, Boolean, Set<Spell>> checkOnTrack(String active, Set<Spell> pattern) {
        boolean found = false;
        Set<Spell> done = new HashSet<>();
        for (Spell p : pattern) {
            if (p.getSequence().equals(active)) {
                done.add(p);
                return Triple.of(true, true, done);
            }

            if (p.getSequence().contains(active)) {
                found = true;
            } else {
                pattern.remove(p);
            }
        }
        return Triple.of(found, false, pattern);
    }

}
