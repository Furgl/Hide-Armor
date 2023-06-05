package furgl.hideArmor.keybind;

import org.lwjgl.glfw.GLFW;

import furgl.hideArmor.HideArmor;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

public class KeyBindOpenGui {

	public static KeyBinding keyBinding;

	public static void init() { 
		keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				"key.hidearmor.openInventoryMenu", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_O, HideArmor.MODNAME 
				));
	}

}