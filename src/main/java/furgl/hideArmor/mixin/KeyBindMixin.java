package furgl.hideArmor.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import furgl.hideArmor.config.Config;
import furgl.hideArmor.gui.ToggleableButtonWidget;
import furgl.hideArmor.keybind.KeyBindOpenGui;
import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;

@Mixin(Keyboard.class)
public abstract class KeyBindMixin {

	@Inject(method = "onKey", at = @At("HEAD"))
	private void onKey(long window, int key, int scancode, int action, int modifiers, CallbackInfo ci) {
		MinecraftClient client = MinecraftClient.getInstance();
		if (window == client.getWindow().getHandle() &&
				action == 1 && KeyBindOpenGui.keyBinding.matchesKey(key, scancode)) {
			// open gui
			if (client.currentScreen == null) {
				Config.expandedGui = true;
				client.setScreen(new InventoryScreen(client.player));
			}
			// toggle expanded gui
			else if (client.currentScreen instanceof InventoryScreen) {
				ToggleableButtonWidget.toggleExpandedGui();
			}
		}
	}

}