package furgl.hideArmor.keybind;

import org.lwjgl.glfw.GLFW;

import furgl.hideArmor.HideArmor;
import furgl.hideArmor.config.Config;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

public class KeyBindOpenGui {

	private static KeyBinding keyBinding;

	public static void init() {
		keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
			    "Open Gui", // The translation key of the keybinding's name
			    InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
			    GLFW.GLFW_KEY_UNKNOWN, // The keycode of the key
			    HideArmor.MODNAME // The translation key of the keybinding's category.
			));

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
		    while (keyBinding.isPressed()) {
			Config.expandedGui = true;
			client.openScreen(new InventoryScreen(client.player));
		    }
		});
	}
	
	
	
}