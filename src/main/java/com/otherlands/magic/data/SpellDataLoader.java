package com.otherlands.magic.data;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.otherlands.magic.OtherlandsMagicMod;
import com.otherlands.magic.utility.Spell;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.JsonDataLoader;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import static com.otherlands.magic.OtherlandsMagicMod.LOGGER;
public class SpellDataLoader extends JsonDataLoader implements SimpleSynchronousResourceReloadListener {
    public SpellDataLoader() {
        super(new Gson(), "spells");
    }
    public static final HashMap<Identifier, Spell> map = new HashMap<>();

    @Override
    protected void apply(Map<Identifier, JsonElement> prepared,
                         ResourceManager manager,
                         Profiler profiler
    ) {
        map.clear();
        if (prepared != null) {
            for (Map.Entry<Identifier, JsonElement> entry : prepared.entrySet()) {
                Identifier location = entry.getKey();
                JsonElement element = entry.getValue();
                try {
                    parse(element, location);
                } catch (Exception e) {
                    LOGGER.error("Your spellbook wasn't able to be deciphered: " + e);
                }
            }
        }
    }
    private void parse(JsonElement element, Identifier origin) {
        LOGGER.info("Flipping to a new page...");
        //check if element is missing something, error if it is
        // - name
        if (!element.getAsJsonObject().has("name")) throw new IllegalArgumentException("This page's name is torn off: " + origin);
        String name = element.getAsJsonObject().get("name").getAsString();
        LOGGER.info("Oh, " + name +"! I recall this one.");
        // - sequence
        if (!element.getAsJsonObject().has("sequence")) throw new IllegalArgumentException("This page's sequence is blotted out: " + origin);
        String sequence = element.getAsJsonObject().get("sequence").getAsString();
        LOGGER.info("Yes... sequence for casting it was " + sequence + ".");
        // - description
        if (!element.getAsJsonObject().has("description")) throw new IllegalArgumentException("This one is beyond description: " + origin);
        String description = element.getAsJsonObject().get("description").getAsString();
        LOGGER.info("This spell... a proper description would be " + description + ".");
        // - message
        if (!element.getAsJsonObject().has("message")) throw new IllegalArgumentException("This page's message is illegible: " + origin);
        String message = element.getAsJsonObject().get("message").getAsString();
        LOGGER.info("Upon casting, the arcane voices utter '" + message + "'.");
        // - effect
        String effect = "";
        if (!element.getAsJsonObject().has("effect")) {
            LOGGER.info("Hmm. It seems as though this spell lacks a Spell Effect. No worries; it isn't required.");
        } else {
            effect = element.getAsJsonObject().get("effect").getAsString();
            LOGGER.info("Aha, this spell causes : " + effect + ".");
        }
        // - command
        String command = "";
        if (!element.getAsJsonObject().has("command")) {
            LOGGER.info("Hmm. It seems as though this spell's authority is too low to have a Command. No worries; it isn't necessary.");
        } else {
            command = element.getAsJsonObject().get("command").getAsString();
            LOGGER.info("The authority of this spell commands : '" + command + "'.");
        }
        map.put(origin, new Spell(name, sequence, description, message, effect, command));
        LOGGER.info("Spell " + name + " has been added to your spellbook. Now, let's see...");
    }

    @Override
    protected Map<Identifier, JsonElement> prepare(ResourceManager resourceManager, Profiler profiler) {
        return super.prepare(resourceManager, profiler);
    }

    @Override
    public Identifier getFabricId() {
        return new Identifier(OtherlandsMagicMod.MODID, "spelldataloader");
    }

    @Override
    public void reload(ResourceManager manager) {

    }
}


///*
//- make your dataloader
//   - dataloader gets a list of resourcelocation & jsonobject (all the json inside the file)
//   - grabs every file that matches: data/*/<name>/*.json
//   - resource location becomes data/*:*
//- dataloader sticks that info into a map (VSMassDataLoader#apply)
// - store your map somewhere; you can now access it later
// - eg in my case: rig up a way to plug in block ID and get the relevant entry
// */
