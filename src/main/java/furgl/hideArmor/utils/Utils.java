package furgl.hideArmor.utils;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;


public class Utils {

	public static final ArrayList<EquipmentSlot> ARMOR_SLOTS;
	public static final HashMap<EquipmentSlot, Info> ARMOR_SLOT_INFO;
	
	static {
		ARMOR_SLOTS = Lists.newArrayList(EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET);
		ARMOR_SLOT_INFO = Maps.newHashMap();
		ARMOR_SLOT_INFO.put(EquipmentSlot.HEAD, new Info("helmet", "helmets"));
		ARMOR_SLOT_INFO.put(EquipmentSlot.CHEST, new Info("chestplate", "chestplates"));
		ARMOR_SLOT_INFO.put(EquipmentSlot.LEGS, new Info("leggings", "leggings"));
		ARMOR_SLOT_INFO.put(EquipmentSlot.FEET, new Info("boots", "boots"));
	}
	
	public static class Info {
		
		public MutableText nameSingular;
		public MutableText namePlural;

		private Info(String nameSingular, String namePlural) {
			this.nameSingular = Text.translatable("misc.hidearmor.singular."+nameSingular);
			this.namePlural = Text.translatable("misc.hidearmor.plural."+namePlural);
		}
		
	}

}