package furgl.hideArmor.config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import furgl.hideArmor.utils.Utils;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.util.JsonHelper;

public class Config {
	
	public static final Gson GSON = new GsonBuilder()
			.setPrettyPrinting()
			.serializeNulls()
			.create();

	private static final String FILE = "./config/hideArmor.cfg";
	
	private static File file;

	public static HashMap<EquipmentSlot, Boolean> hideYourArmor = Maps.newHashMap();
	public static HashMap<EquipmentSlot, Boolean> hideOtherPlayerArmor = Maps.newHashMap();
	public static boolean expandedGui;

	static {
		for (EquipmentSlot slot : Utils.ARMOR_SLOTS) {
			hideYourArmor.put(slot, false);
			hideOtherPlayerArmor.put(slot, false);
		}
	}

	public static void init() {
		try {
			// create file if it doesn't already exist
			file = new File(FILE);
			if (!file.exists())
				file.createNewFile();
			else
				readFromFile();
			// write current values / defaults to file
			writeToFile();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void readFromFile() {
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			JsonObject parser = (JsonObject) JsonHelper.deserialize(reader);

			for (EquipmentSlot slot : Utils.ARMOR_SLOTS) {
				JsonElement element = parser.get("Hide your armor in slot: "+slot);
				boolean hide = element == null ? false : element.getAsBoolean();
				hideYourArmor.put(slot, hide);

				element = parser.get("Hide other players armor in slot: "+slot);
				hide = element == null ? false : element.getAsBoolean();
				hideOtherPlayerArmor.put(slot, hide);
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void writeToFile() {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
			JsonObject obj = new JsonObject();
			for (EquipmentSlot slot : Utils.ARMOR_SLOTS) {
				obj.addProperty("Hide your armor in slot: "+slot, hideYourArmor.get(slot));
				obj.addProperty("Hide other players armor in slot: "+slot, hideOtherPlayerArmor.get(slot));
			}

			writer.write(GSON.toJson(obj));
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}