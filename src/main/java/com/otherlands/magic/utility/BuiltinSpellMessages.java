package com.otherlands.magic.utility;

import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public interface BuiltinSpellMessages {

    MutableText ACTIVATE = new LiteralText("AA").formatted(Formatting.DARK_PURPLE).formatted(Formatting.OBFUSCATED).append(new LiteralText(" Arcane magic wavers through your mind... ").formatted(Formatting.DARK_PURPLE)).formatted(Formatting.RESET).append(new LiteralText("AA").formatted(Formatting.DARK_PURPLE).formatted(Formatting.OBFUSCATED));
    MutableText DEACTIVATE = new LiteralText("AA").formatted(Formatting.DARK_PURPLE).formatted(Formatting.OBFUSCATED).append(new LiteralText(" The whispers of arcana cease. ").formatted(Formatting.DARK_PURPLE)).formatted(Formatting.RESET).append(new LiteralText("AA").formatted(Formatting.DARK_PURPLE).formatted(Formatting.OBFUSCATED));
    MutableText FIREBALL = new LiteralText("AA");
}
