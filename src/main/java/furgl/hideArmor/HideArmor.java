package furgl.hideArmor;

import java.util.HashMap;

import furgl.hideArmor.command.ReloadCommand;
import furgl.hideArmor.config.Config;
import furgl.hideArmor.keybind.KeyBindOpenGui;
import furgl.hideArmor.utils.Utils.HidingArmorSlot;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ElytraItem;

public class HideArmor implements ClientModInitializer {

	/*** Changelog
	 * TODO shift-click toggles all?
	 * 
	 * Ported to 1.19.4
	 * Added support for hiding Elytra
	 * Potion effects now are pushed to the right to prevent overlapping when the Hide Armor gui is open
	 * Added Simplified Chinese translation - credit to https://github.com/CJYKK
	 * Added Brazilian Portuguese translation - credit to https://github.com/Sanadriell
	 * Added support for Fabric API's armor renderer - credit to https://github.com/CleverNucleus
	 */

	public static final String MODNAME = "Hide Armor";
	public static final String MODID = "hidearmor";

	@Override
	public void onInitializeClient() {
		Config.init();
		KeyBindOpenGui.init();
		ReloadCommand.init();
	}

	/**Should this armor be hidden*/
	public static boolean shouldHide(Entity entity, EquipmentSlot slot) {
		// if rendering player's armor
		if (entity instanceof PlayerEntity) {
			boolean isClientPlayer = entity.getUuid().equals(MinecraftClient.getInstance().player.getUuid());
			// cancel if rendering client player's armor and in hideYourArmor
			// or if rendering other player's armor and in hideOtherPlayerArmor
			HashMap<HidingArmorSlot, Boolean> map = isClientPlayer ? Config.hideYourArmor : Config.hideOtherPlayerArmor;
			if (
					(slot == EquipmentSlot.HEAD && map.get(HidingArmorSlot.HEAD).booleanValue()) ||
					(slot == EquipmentSlot.CHEST && map.get(HidingArmorSlot.ELYTRA).booleanValue() && ((PlayerEntity) entity).getEquippedStack(EquipmentSlot.CHEST).getItem() instanceof ElytraItem) ||
					(slot == EquipmentSlot.CHEST && map.get(HidingArmorSlot.CHEST).booleanValue() && !(((PlayerEntity) entity).getEquippedStack(EquipmentSlot.CHEST).getItem() instanceof ElytraItem)) ||
					(slot == EquipmentSlot.LEGS && map.get(HidingArmorSlot.LEGS).booleanValue()) ||
					(slot == EquipmentSlot.FEET && map.get(HidingArmorSlot.FEET).booleanValue()))
				return true;
		}
		return false;
	}

}