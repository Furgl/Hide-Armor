package furgl.hideArmor;

import furgl.hideArmor.config.Config;
import net.fabricmc.api.ClientModInitializer;

public class HideArmor implements ClientModInitializer {

	public static final String MODNAME = "Hide Armor";
	public static final String MODID = "hidearmor";

	@Override
	public void onInitializeClient() {
		Config.init();
	}

}