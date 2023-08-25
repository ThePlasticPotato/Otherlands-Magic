package com.otherlands.magic.utility;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.otherlands.magic.OtherlandsMagicMod;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Predicate;

public class SpellRegistry {
    private static final Logger LOGGER = LoggerFactory.getLogger("Librarian");
    public static final SpellRegistry INSTANCE = new SpellRegistry();
    public static final String SPELL_LOCATION = "spells";

    public final Map<Identifier, Spell> spells = new HashMap<>();

    public static final Gson GSON = new GsonBuilder().registerTypeAdapter(Identifier.class, new Identifier.Serializer()).create();
    private boolean loaded = false;
    private SpellRegistry() {
    }

    public void init() {

        registerDefaultSpells();

        Map<Identifier, String> foundSpells = new HashMap<>();

        SpellFolderLoader.findSpells();

        findFiles(String.format("data/%s/%s", OtherlandsMagicMod.MODID, SPELL_LOCATION), Files::exists,
                (path, file) -> {
                    if (Files.isRegularFile(file)
                            && file.getFileName().toString().equals("spell.json")) {
                        String fileStr = file.toString().replaceAll("\\\\", "/");
                        String relPath = fileStr
                                .substring(fileStr.indexOf(SPELL_LOCATION) + SPELL_LOCATION.length() + 1);
                        String spellName = relPath.substring(0, relPath.indexOf("/"));
                        OtherlandsMagicMod.LOGGER.info("Found a new page @ {}", file);
                        if (spellName.contains("/")) {
                            OtherlandsMagicMod.LOGGER.warn("Flipping past a page @ {}", file);
                            return true;
                        }

                        String assetPath = fileStr.substring(fileStr.indexOf("data/"));
                        Identifier spellId = new Identifier(OtherlandsMagicMod.MODID, spellName);
                        foundSpells.put(spellId, assetPath);
                    }

                    return true;
                }, true, 2);

        foundSpells.forEach((id, file) -> {
            try (InputStream stream = Files.newInputStream(FabricLoader.getInstance().getModContainer(OtherlandsMagicMod.MODID).get().getPath(file))) {
                loadSpell(id, stream);
            } catch (IOException e) {
                OtherlandsMagicMod.LOGGER.error("This spell's page was torn {}", id, e);
            }
        });


    }
    public void loadSpell(Identifier res, InputStream stream) {
        Reader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
        Spell spell = GSON.fromJson(reader, Spell.class);
        spell.build(res);
        describePage(spell);
        spells.put(res, spell);
    }

    public boolean isLoaded() {
        return loaded;
    }

    public void describePage(Spell spell) {
        LOGGER.info("Flipping to a new page...");
        // - name
        if (spell.getName() == null) throw new IllegalArgumentException("This page's name is torn off...");
        LOGGER.info("Oh, " + spell.getName() +"! I recall this one.");
        // - sequence
        if (spell.getSequence() == null) throw new IllegalArgumentException("This page's sequence is blotted out..");
        LOGGER.info("Yes... sequence for casting it was " + spell.getSequence() + ".");
        // - description
        if (spell.getDescription() == null) throw new IllegalArgumentException("This one is beyond description...");
        LOGGER.info("This spell... a proper description would be '" + spell.getDescription() + "'.");
        // - message
        if (spell.getMessage() == null) throw new IllegalArgumentException("This page's message is illegible...");
        LOGGER.info("Upon casting, the arcane voices utter '" + spell.getMessage() + "'.");
        // - effect
        if (spell.getEffectList() == null || spell.getEffectList().equals("")) {
            LOGGER.info("Hmm. It seems as though this spell lacks a Spell Effect. No worries; it isn't required.");
        } else {
            LOGGER.info("Aha, this spell causes : " + spell.getEffectList() + ".");
        }
        // - command
        if (spell.getCommand() == null || spell.getCommand().equals("")) {
            LOGGER.info("Hmm. It seems as though this spell's authority is too low to have a Command. No worries; it isn't necessary.");
        } else {
            LOGGER.info("The authority of this spell commands : '" + spell.getCommand() + "'.");
        }
        LOGGER.info("Spell " + spell.getName() + " has passed the requirements. Now, let's see what else your book has in store...");
    }

    public void registerDefaultSpells() {
        Spell Activate = new Spell("Activate", "7162534", "Opens your ears to the arcane forces.", "You shouldn't see this.", "", null);
        Identifier aIden = new Identifier(OtherlandsMagicMod.MODID, "activate");
        Spell Deactivate = new Spell("Deactivate", "012345678", "Shuts out the incessant voices.", "You shouldn't see this.", "", null);
        Identifier dIden = new Identifier(OtherlandsMagicMod.MODID, "deactivate");

        LOGGER.info("It seems your standard spells are all in good order.");

        spells.put(aIden, Activate);
        spells.put(dIden, Deactivate);
    }

    // HELPER

    public static void findFiles(String base, Predicate<Path> rootFilter,
                                 BiFunction<Path, Path, Boolean> processor, boolean visitAllFiles, int maxDepth) {
        ModContainer mod = FabricLoader.getInstance().getModContainer(OtherlandsMagicMod.MODID).orElseThrow(() -> new IllegalStateException("ModContainer not found!"));

        try {
            walk(mod.getRootPath().resolve(base), rootFilter, processor, visitAllFiles, maxDepth);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    private static void walk(Path root, Predicate<Path> rootFilter, BiFunction<Path, Path, Boolean> processor,
                             boolean visitAllFiles, int maxDepth) throws IOException {
        if (root == null || !Files.exists(root) || !rootFilter.test(root)) {
            return;
        }

        if (processor != null) {
            Iterator<Path> itr = Files.walk(root, maxDepth).iterator();

            while (itr.hasNext()) {
                boolean cont = processor.apply(root, itr.next());

                if (!visitAllFiles && !cont) {
                    return;
                }
            }
        }
    }

}
