package furgl.hideArmor;

import furgl.hideArmor.command.ReloadCommand;
import furgl.hideArmor.config.Config;
import furgl.hideArmor.keybind.KeyBindOpenGui;
import net.fabricmc.api.ClientModInitializer;

public class HideArmor implements ClientModInitializer {
	
	/*** Changelog
	 * Added Open Gui keybind
	 * Added /hidearmor reload to reload config
	 * Added config options to move or hide the inventory button
	 */

	public static final String MODNAME = "Hide Armor";
	public static final String MODID = "hidearmor";

	@Override
	public void onInitializeClient() {
		Config.init();
		KeyBindOpenGui.init();
		ReloadCommand.init();
	}

}