package furgl.hideArmor.command;

import furgl.hideArmor.config.Config;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class ReloadCommand {

	public static void init() {
		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
			dispatcher.register(
					ClientCommandManager.literal("hidearmor")
					.then(ClientCommandManager.literal("reload")
					.executes(context -> {
						Config.init();
						MinecraftClient.getInstance().player.sendMessage(Text.translatable("command.reload.success").formatted(Formatting.GREEN), false);
						return 1;
					})));
		});
	}

}