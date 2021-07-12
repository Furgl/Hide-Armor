package furgl.hideArmor.mixin;

import java.util.ArrayList;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;

import furgl.hideArmor.HideArmor;
import furgl.hideArmor.config.Config;
import furgl.hideArmor.gui.ToggleableButtonWidget;
import furgl.hideArmor.utils.Utils;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.screen.recipebook.RecipeBookProvider;
import net.minecraft.client.gui.screen.recipebook.RecipeBookWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

@Mixin(InventoryScreen.class)
public abstract class RenderGuiMixin extends AbstractInventoryScreen<PlayerScreenHandler> implements RecipeBookProvider {

	@Shadow
	RecipeBookWidget recipeBook;

	Identifier BUTTONS = new Identifier(HideArmor.MODID, "textures/gui/buttons.png");
	Identifier BACKGROUND = new Identifier(HideArmor.MODID, "textures/gui/background.png");
	TexturedButtonWidget button;
	ArrayList<ToggleableButtonWidget> hideYourArmorButtons;
	ArrayList<ToggleableButtonWidget> hideOtherPlayersArmorButtons;

	public RenderGuiMixin(PlayerScreenHandler screenHandler, PlayerInventory playerInventory, Text text) {
		super(screenHandler, playerInventory, text);
	}

	@Inject(method = "init", at = @At("RETURN"))
	private void onInit(CallbackInfo ci) {
		// buttons
		hideYourArmorButtons = Lists.newArrayList();
		hideOtherPlayersArmorButtons = Lists.newArrayList();
		for (int i=0; i<Utils.ARMOR_SLOTS.size(); ++i) {
			EquipmentSlot slot = Utils.ARMOR_SLOTS.get(i);
			// hide your armor
			ToggleableButtonWidget toggle = new ToggleableButtonWidget(0, 0, 18, 18, i*18, 0, 18, BUTTONS, 128, 128, (button) -> {
				((ToggleableButtonWidget) button).toggle();
				Config.hideYourArmor.put(slot, ((ToggleableButtonWidget) button).isToggled());
				Config.writeToFile();
			}, (button, matrices, mouseX, mouseY) -> {
				this.renderTooltip(matrices, Text.of(
						(((ToggleableButtonWidget) button).isToggled() ? Formatting.RED+"Hiding" : Formatting.GREEN+"Showing")+
						Formatting.WHITE+" your "+Utils.ARMOR_SLOT_INFO.get(slot).nameSingular), mouseX, mouseY);
			}, LiteralText.EMPTY);
			hideYourArmorButtons.add(toggle);
			toggle.visible = Config.expandedGui;
			if (Config.hideYourArmor.get(slot).booleanValue())
				toggle.toggle();
			this.addDrawableChild(toggle);
			// hide other player's armor
			toggle = new ToggleableButtonWidget(0, 0, 18, 18, i*18, 36, 18, BUTTONS, 128, 128, (button) -> {
				((ToggleableButtonWidget) button).toggle();
				Config.hideOtherPlayerArmor.put(slot, ((ToggleableButtonWidget) button).isToggled());
				Config.writeToFile();
			}, (button, matrices, mouseX, mouseY) -> {
				this.renderTooltip(matrices, Text.of(
						(((ToggleableButtonWidget) button).isToggled() ? Formatting.RED+"Hiding" : Formatting.GREEN+"Showing")+
						Formatting.WHITE+" other player's "+Utils.ARMOR_SLOT_INFO.get(slot).namePlural), mouseX, mouseY);
			}, LiteralText.EMPTY);
			hideOtherPlayersArmorButtons.add(toggle);
			toggle.visible = Config.expandedGui;
			if (Config.hideOtherPlayerArmor.get(slot).booleanValue())
				toggle.toggle();
			this.addDrawableChild(toggle);
		}
		// button to expand
		this.button = new TexturedButtonWidget(0, 0, 18, 18, 72, 0, 18, BUTTONS, 128, 128, (button) -> {
			Config.expandedGui = !Config.expandedGui;
			// hide/show buttons
			if (Config.expandedGui) {
				for (ToggleableButtonWidget button2 : hideYourArmorButtons)
					button2.visible = true;
				for (ToggleableButtonWidget button2 : hideOtherPlayersArmorButtons)
					button2.visible = true;
			}
			else {
				for (ToggleableButtonWidget button2 : hideYourArmorButtons)
					button2.visible = false;
				for (ToggleableButtonWidget button2 : hideOtherPlayersArmorButtons)
					button2.visible = false;
			}
		});
		this.addDrawableChild(this.button);
		// reset button positions
		this.resetButtonPositions();
	}

	@Inject(method = "drawForeground", at = @At("HEAD"))
	private void onDrawForeground(MatrixStack matrices, int mouseX, int mouseY, CallbackInfo ci) {
		if (Config.expandedGui) 
			// draw text
			drawCenteredText(matrices, textRenderer, Formatting.WHITE+"Hide Armor", this.backgroundWidth+40, 12, 16777215);
	}

	@Inject(method = "drawBackground", at = @At("HEAD"))
	private void onDrawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY, CallbackInfo ci) {
		if (Config.expandedGui) {
			// draw background
			RenderSystem.setShader(GameRenderer::getPositionTexShader);
			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
			RenderSystem.setShaderTexture(0, this.BACKGROUND);
			this.drawTexture(matrices, x+this.backgroundWidth+2, this.y, 0, 0, 75, 117);
		}
	}

	@Inject(method = "mouseClicked", at = @At("RETURN"))
	private void onMouseClicked(double mouseX, double mouseY, int button, CallbackInfoReturnable ci) {
		resetButtonPositions();
	}

	private void resetButtonPositions() {
		this.button.setPos(this.x + 143, this.height / 2 - 22);
		for (int i=0; i<this.hideYourArmorButtons.size(); ++i) {
			int x = this.x+this.backgroundWidth+17;
			int y = this.y+i*21+24;
			this.hideYourArmorButtons.get(i).setPos(x, y);
			this.hideOtherPlayersArmorButtons.get(i).setPos(x+25, y);
		}
	}

}