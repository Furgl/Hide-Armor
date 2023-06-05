package furgl.hideArmor.utils;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.minecraft.text.MutableText;
import net.minecraft.text.Text;


public class Utils {

	public static final ArrayList<HidingArmorSlot> ARMOR_SLOTS;
	public static final HashMap<HidingArmorSlot, Info> ARMOR_SLOT_INFO;
	
	static {
		ARMOR_SLOTS = Lists.newArrayList(HidingArmorSlot.HEAD, HidingArmorSlot.ELYTRA, HidingArmorSlot.CHEST, HidingArmorSlot.LEGS, HidingArmorSlot.FEET);
		ARMOR_SLOT_INFO = Maps.newHashMap();
		ARMOR_SLOT_INFO.put(HidingArmorSlot.HEAD, new Info("helmet", "helmets"));
		ARMOR_SLOT_INFO.put(HidingArmorSlot.ELYTRA, new Info("elytra", "elytras"));
		ARMOR_SLOT_INFO.put(HidingArmorSlot.CHEST, new Info("chestplate", "chestplates"));
		ARMOR_SLOT_INFO.put(HidingArmorSlot.LEGS, new Info("leggings", "leggings"));
		ARMOR_SLOT_INFO.put(HidingArmorSlot.FEET, new Info("boots", "boots"));
	}
	
	public static class Info {
		
		public MutableText nameSingular;
		public MutableText namePlural;

		private Info(String nameSingular, String namePlural) {
			this.nameSingular = Text.translatable("misc.hidearmor.singular."+nameSingular);
			this.namePlural = Text.translatable("misc.hidearmor.plural."+namePlural);
		}
		
	}
	
	public static enum HidingArmorSlot {
		HEAD, ELYTRA, CHEST, LEGS, FEET
	}

}