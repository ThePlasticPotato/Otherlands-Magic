package com.otherlands.magic.utility;

import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public class Spell {

    private String name;
    private String sequence;
    private String description;
    private String message;
    @Nullable
    private String effect;
    @Nullable
    private String command;

    public Identifier ID;

    public Spell(String name, String sequence, String description, String message, @Nullable String effect, @Nullable String command) {
        this.name = name;
        this.sequence = sequence;
        this.description = description;
        this.message = message;
        this.effect = effect;
        this.command = command;
    }

//    public Spell(String name, String sequence, String description, Text message, String effect, String command) {
//        this.name = name;
//        this.sequence = sequence;
//        this.description = description;
//        this.message = message;
//        this.effect = effect;
//        this.command = command;
//    }

    public void build(Identifier resource) {
        this.ID = resource;
    }

    public String getName() {
        return name;
    }

    public String getSequence() {
        return sequence;
    }

    public String getDescription() {
        return description;
    }

    public String getMessage() {
        return message;
    }

    public String getEffectList() {
        return effect;
    }
    public String getCommand() {
        return command;
    }
}
