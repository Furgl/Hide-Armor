package furgl.hideArmor;

import furgl.hideArmor.command.ReloadCommand;
import furgl.hideArmor.config.Config;
import furgl.hideArmor.keybind.KeyBindOpenGui;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;

public class HideArmor implements ClientModInitializer {

	/*** Changelog
	 * TODO shift-click toggles all?
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
			if ((isClientPlayer && Config.hideYourArmor.get(slot).booleanValue()) ||
					(!isClientPlayer && Config.hideOtherPlayerArmor.get(slot).booleanValue()))
				return true;
		}
		return false;
	}

}