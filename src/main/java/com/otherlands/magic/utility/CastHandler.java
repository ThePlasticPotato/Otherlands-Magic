package com.otherlands.magic.utility;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.otherlands.magic.OtherlandsMagicMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientCommandSource;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;

import java.util.Set;

import static com.otherlands.magic.OtherlandsMagicMod.LOGGER;
public class CastHandler {
    private static CommandDispatcher<ServerCommandSource> dispatcher = null;
    public static boolean castSpell(PlayerEntity player, Spell spell, boolean magicActive) {
        if (player instanceof ServerPlayerEntity sPlayer) {

            dispatcher = sPlayer.server.getCommandManager().getDispatcher();

            if (!magicActive) {
                if (spell.getName().equals("Activate")) {
                    sPlayer.sendMessage(BuiltinSpellMessages.ACTIVATE, true);
                    return true;
                } else if (spell.getName().equals("Deactivate")) {
                    sPlayer.sendMessage(Text.of("There is no whisper in your ear."), true);
                }
            } else {
                if (spell.getName().equals("Activate")) {
                    sPlayer.sendMessage(Text.of("The arcane voices already rumble within you."), true);
                    return false;
                } else if (spell.getName().equals("Deactivate")) {
                    sPlayer.sendMessage(BuiltinSpellMessages.DEACTIVATE, true);
                    return true;
                } else {
                    sPlayer.sendMessage(new LiteralText(spell.getMessage()), true);
                    try {
                        sendCommand(sPlayer, spell);
                    } catch (CommandSyntaxException e) {
                        sPlayer.sendMessage(Text.of("The spell's order fails: " + e), true);
                        LOGGER.error(e.getMessage() + e.getInput() + e.getContext());
                        LOGGER.error(spell.getCommand());
                    }
                    return false;
                }
            }
        }
        return false;
    }

    public static void sendCommand(ServerPlayerEntity player, Spell spell) throws CommandSyntaxException {
        if (dispatcher != null) {
            if (spell.getCommand() != null && !spell.getCommand().equals("")) {
                dispatcher.execute(spell.getCommand(), player.getCommandSource());
            }
        }
    }
}
