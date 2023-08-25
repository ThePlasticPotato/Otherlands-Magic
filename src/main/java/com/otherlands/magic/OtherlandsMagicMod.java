package com.otherlands.magic;


import com.otherlands.magic.utility.Spell;
import com.otherlands.magic.utility.SpellRegistry;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OtherlandsMagicMod implements ModInitializer {

	public static final String MODID = "otherlands-magic";
	public static final Logger LOGGER = LoggerFactory.getLogger("otherlands-magic");

	@Override
	public void onInitialize() {

		LOGGER.info("Greetings, otherlander. Let's take a peek at your spellbook...");

		SpellRegistry.INSTANCE.init();


		if (SpellRegistry.INSTANCE.spells.size() > 0) {
			LOGGER.info("Your spellbook is added to the library!");
			LOGGER.info("There are " + SpellRegistry.INSTANCE.spells.size() + " spells currently initialized! They are: ");
			for (Spell s : SpellRegistry.INSTANCE.spells.values()) {
				LOGGER.info(s.getName());
			}
		} else {
			LOGGER.info("It seems your copy of the spellbook is empty... I'll have to consult the Librarian about this. If you're a client, do not fret; this may not be of consequence");
		}

		LOGGER.info("Magic at the ready!");
	}
}