package furgl.hideArmor.config;

import java.lang.reflect.Field;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

import furgl.hideArmor.keybind.KeyBindOpenGui;
import furgl.hideArmor.utils.Utils;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.Modifier;
import me.shedaniel.clothconfig2.api.ModifierKeyCode;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil.Key;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.text.Text;

public class ModMenuIntegration implements ModMenuApi {

	private static Field keybind;

	static {
		try {
			keybind = KeyBinding.class.getDeclaredField("field_1655");
			keybind.setAccessible(true);
		} catch (Exception e) {
			try {
				keybind = KeyBinding.class.getDeclaredField("boundKey");
				keybind.setAccessible(true);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}

	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return parent -> {
			ConfigBuilder builder = ConfigBuilder.create()
					.setParentScreen(parent)
					.setTitle(Text.translatable("config.hidearmor.name"))
					.setSavingRunnable(() -> Config.writeToFile());
			ConfigCategory category = builder.getOrCreateCategory(Text.translatable("category.hidearmor.general"));
			category.addEntry(builder.entryBuilder()
					.startBooleanToggle(Text.translatable("option.hidearmor.showInventoryButton"), Config.showGuiButton)
					.setTooltip(Text.translatable("option.hidearmor.showInventoryButton.tooltip"))
					.setDefaultValue(true)
					.setSaveConsumer(value -> Config.showGuiButton = value)
					.build());
			category.addEntry(builder.entryBuilder()
					.startIntField(Text.translatable("option.hidearmor.buttonXOffset"), Config.guiButtonXOffset)
					.setTooltip(Text.translatable("option.hidearmor.buttonXOffset.tooltip"))
					.setDefaultValue(0)
					.setSaveConsumer(value -> Config.guiButtonXOffset = value)
					.build());
			category.addEntry(builder.entryBuilder()
					.startIntField(Text.translatable("option.hidearmor.buttonYOffset"), Config.guiButtonYOffset)
					.setTooltip(Text.translatable("option.hidearmor.buttonYOffset.tooltip"))
					.setDefaultValue(0)
					.setSaveConsumer(value -> Config.guiButtonYOffset = value)
					.build());
			try {
				category.addEntry(builder.entryBuilder()
						.startModifierKeyCodeField(Text.translatable("option.hidearmor.openInventoryMenu"), ModifierKeyCode.of((Key) keybind.get(KeyBindOpenGui.keyBinding), Modifier.none()))
						.setDefaultValue(() -> KeyBindOpenGui.keyBinding.getDefaultKey())
						.setSaveConsumer(value -> {
							KeyBindOpenGui.keyBinding.setBoundKey(value);
							MinecraftClient.getInstance().options.write();
						})
						.build());
			} catch (Exception e) {
				e.printStackTrace();
			}
			category = builder.getOrCreateCategory(Text.translatable("category.hidearmor.hiddenArmor"));
			for (EquipmentSlot slot : Utils.ARMOR_SLOTS)
				category.addEntry(builder.entryBuilder()
						.startBooleanToggle(Text.translatable("option.hidearmor.hiddenArmor", slot), Config.hideYourArmor.get(slot))
						.setDefaultValue(false)
						.setSaveConsumer(value -> Config.hideYourArmor.put(slot, value))
						.build());
			for (EquipmentSlot slot : Utils.ARMOR_SLOTS)
				category.addEntry(builder.entryBuilder()
						.startBooleanToggle(Text.translatable("option.hidearmor.otherHiddenArmor", slot), Config.hideOtherPlayerArmor.get(slot))
						.setDefaultValue(false)
						.setSaveConsumer(value -> Config.hideOtherPlayerArmor.put(slot, value))
						.build());
			return builder.build();
		};
	}

}