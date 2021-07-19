package furgl.hideArmor.command;

import furgl.hideArmor.config.Config;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class ReloadCommand {

	public static void init() {
		CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
			ClientCommandManager.DISPATCHER.register(
					ClientCommandManager.literal("hidearmor")
					.then(ClientCommandManager.literal("reload")
					.executes(context -> {
						Config.init();	
						MinecraftClient.getInstance().player.sendMessage(Text.of(Formatting.GREEN+"Reloaded Hide Armor"), false);
						return 1;
					})));
		});
	}

}