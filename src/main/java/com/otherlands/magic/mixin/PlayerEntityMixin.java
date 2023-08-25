package com.otherlands.magic.mixin;

import com.otherlands.magic.OtherlandsMagicMod;
import com.otherlands.magic.utility.CastHandler;
import com.otherlands.magic.utility.PatternChecker;
import com.otherlands.magic.utility.Spell;
import com.otherlands.magic.utility.SpellRegistry;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import org.apache.commons.lang3.tuple.Triple;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashSet;
import java.util.Set;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {

    // SECTION : SLOT SELECTION

    @Shadow public abstract PlayerInventory getInventory();

    @Unique
    private int lastSlotSelected = 0;

    @Unique
    private String activeSequence = "";

    @Unique
    private boolean isPatternActive = false;

    @Unique
    private int decay = 0;

    @Unique
    private Set<Spell> activePatterns = new HashSet<>();

    private boolean slotChanged(int slot) {
        if (slot != lastSlotSelected) {
            return true;
        }
        return false;
    }

    //SECTION : CASTING

    @Unique
    private boolean magicActive = false;

    @Unique
    private int magicDuration = 0;

    @Unique
    private int particleCountdown = 0;


    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void tickEdit(CallbackInfo info) {
        int slot = getInventory().selectedSlot;
        boolean slotChanged = slotChanged(slot);
        lastSlotSelected = slot;

//        OtherlandsMagicMod.LOGGER.info("Currenty loaded spells: ");
//        for (Spell s : SpellRegistry.INSTANCE.spells.values()) {
//            OtherlandsMagicMod.LOGGER.info(s.getName());
//        }

        if (decay > 0) {
            decay--;
        } else {
            isPatternActive = false;
        }

        if (magicDuration > 0) {
            magicDuration--;
        } else {
            magicActive = false;
        }

        if (slotChanged) {
            if (isPatternActive) {
                activeSequence = activeSequence + "" + slot;
                Triple<Boolean, Boolean, Set<Spell>> result = PatternChecker.checkOnTrack(activeSequence, activePatterns);
                boolean onTrack = result.getLeft();
                boolean success = result.getMiddle();
                if (success) {
                    boolean toggleSuccess = CastHandler.castSpell(getInventory().player, (Spell) result.getRight().toArray()[0], magicActive);
                    isPatternActive = false;
                    decay = 0;
                    activePatterns.clear();
                    activeSequence = "";
                    if (toggleSuccess && !magicActive) {
                        magicActive = true;
                        magicDuration = 4000;
                    } else if (toggleSuccess && magicActive) {
                        magicActive = false;
                    }
                } else if (onTrack) {
                    decay = 1000;
                    activePatterns = result.getRight();
                } else {
                    isPatternActive = false;
                    decay = 0;
                    activePatterns.clear();
                    activeSequence = "";
                }
            } else {
                Pair<Boolean, Set<Spell>> result = PatternChecker.checkForPattern(slot);
                boolean checked = result.left();
                if (checked) {
                    decay = 1000;
                    isPatternActive = checked;
                    activePatterns = result.right();
                    activeSequence = slot + "";
                }
            }
        }
    }
}
