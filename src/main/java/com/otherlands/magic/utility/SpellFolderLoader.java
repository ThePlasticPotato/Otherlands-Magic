package com.otherlands.magic.utility;

import com.otherlands.magic.OtherlandsMagicMod;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.util.Identifier;
import net.minecraft.util.InvalidIdentifierException;

import java.io.File;
import java.io.FileInputStream;

public class SpellFolderLoader {

	public static File loadDir;

	private static void setup() {
		loadDir = new File(SpellRegistry.SPELL_LOCATION);
		if (!loadDir.exists()) {
			loadDir.mkdir();
		} else if (!loadDir.isDirectory()) {
			throw new RuntimeException(loadDir.getAbsolutePath() + " is a file, not a folder, aborting. Please delete this file or move it elsewhere if it has important contents.");
		}
	}

	public static void findSpells() {
		if (loadDir == null) {
			setup();
		}

		ModContainer self = FabricLoader.getInstance().getModContainer(OtherlandsMagicMod.MODID).get();
		File[] subdirs = loadDir.listFiles(File::isDirectory);
		if (subdirs == null) {
			OtherlandsMagicMod.LOGGER.warn("Failed to list external spells in {}, not loading external spells",
					loadDir.getAbsolutePath());
			return;
		}

		for (File dir : subdirs) {
			Identifier res;
			try {
				res = new Identifier(OtherlandsMagicMod.MODID, dir.getName());
			} catch (InvalidIdentifierException ex) {
				OtherlandsMagicMod.LOGGER.error("Invalid external spell folder name {}, skipping", dir.getName(), ex);
				continue;
			}

			File bookJson = new File(dir, "book.json");
			try (FileInputStream stream = new FileInputStream(bookJson)) {
				SpellRegistry.INSTANCE.loadSpell(res, stream);
			} catch (Exception e) {
				OtherlandsMagicMod.LOGGER.error("Failed to load external spell json from {}, skipping", bookJson, e);
			}
		}
	}

}
