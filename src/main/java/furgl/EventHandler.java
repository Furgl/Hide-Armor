package furgl;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class EventHandler {

	private static final ResourceLocation BUTTONS = new ResourceLocation(HideArmor.MODID, "textures/gui/hide_armor_buttons.png");

	public static boolean hidePlayerArmor = true;
	public static boolean hideOtherPlayersArmor = false;

	private static NonNullList<ItemStack> armor;

	private static Widget recipeButton;
	private static ImageButton hideOtherPlayersArmorButton;
	private static ImageButton hidePlayerArmorButton;
	private static int space = 4;
	private static int recipeButtonAdjust = 5;
	private static int recipeButtonPositionX;

	/**Add buttons to inventory*/
	@SubscribeEvent
	public static void onInitGui(GuiScreenEvent.InitGuiEvent event) {
		if (event.getGui() instanceof InventoryScreen && !event.getWidgetList().isEmpty()) {
			recipeButton = event.getWidgetList().get(0);
			recipeButton.field_230690_l_ -= recipeButtonAdjust;
			recipeButtonPositionX = recipeButton.field_230690_l_;
			hidePlayerArmorButton = new ImageButton(recipeButton.field_230690_l_+20+space, recipeButton.field_230691_m_, 20, 18, 0, 0, 19, BUTTONS, 128, 128, (button) -> {
				hidePlayerArmor = !hidePlayerArmor;   
				button.field_230690_l_ = recipeButton.field_230690_l_+20+space;
				button.field_230691_m_ = recipeButton.field_230691_m_;
			}) {
				@Override
				public void func_230431_b_(MatrixStack matrix, int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
					Minecraft minecraft = Minecraft.getInstance();
					minecraft.getTextureManager().bindTexture(BUTTONS);
					RenderSystem.disableDepthTest();
					// background
					func_238463_a_(matrix, this.field_230690_l_, this.field_230691_m_, 0, this.func_230449_g_() ? 19 : 0, this.field_230688_j_, this.field_230689_k_, 128, 128);
					// icon
					func_238463_a_(matrix, this.field_230690_l_, this.field_230691_m_, 40, 0, this.field_230688_j_, this.field_230689_k_, 128, 128);
					// red cross
					if (hidePlayerArmor)
						func_238463_a_(matrix, this.field_230690_l_, this.field_230691_m_, 20, 0, this.field_230688_j_, this.field_230689_k_, 128, 128);
					RenderSystem.enableDepthTest();
				}
			};
			event.addWidget(hidePlayerArmorButton);
			hideOtherPlayersArmorButton = new ImageButton(recipeButton.field_230690_l_+40+space*2, recipeButton.field_230691_m_, 20, 18, 20, 0, 19, BUTTONS, 128, 128, (button) -> {
				hideOtherPlayersArmor = !hideOtherPlayersArmor;   
				button.field_230690_l_ = recipeButton.field_230690_l_+40+space*2;
				button.field_230691_m_ = recipeButton.field_230691_m_;
			}) {
				@Override
				public void func_230431_b_(MatrixStack matrix, int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
					Minecraft minecraft = Minecraft.getInstance();
					minecraft.getTextureManager().bindTexture(BUTTONS);
					RenderSystem.disableDepthTest();
					// background
					func_238463_a_(matrix, this.field_230690_l_, this.field_230691_m_, 0, this.func_230449_g_() ? 19 : 0, this.field_230688_j_, this.field_230689_k_, 128, 128);
					// icon
					func_238463_a_(matrix, this.field_230690_l_, this.field_230691_m_, 60, 0, this.field_230688_j_, this.field_230689_k_, 128, 128);
					// red cross
					if (hideOtherPlayersArmor)
						func_238463_a_(matrix, this.field_230690_l_, this.field_230691_m_, 20, 0, this.field_230688_j_, this.field_230689_k_, 128, 128);
					RenderSystem.enableDepthTest();
				}
			};
			event.addWidget(hideOtherPlayersArmorButton);
		}
	}

	/**Move buttons when recipe button is pressed*/
	@SuppressWarnings("resource")
	@SubscribeEvent
	public static void onActionPerformed(GuiScreenEvent.DrawScreenEvent event) {
		if (event.getGui() instanceof InventoryScreen && recipeButton != null) {
			// move recipe button
			if (recipeButton.field_230690_l_ != recipeButtonPositionX) {
				recipeButton.field_230690_l_ -= recipeButtonAdjust;
				recipeButtonPositionX = recipeButton.field_230690_l_;
			}
			// move my buttons
			hidePlayerArmorButton.field_230690_l_ = recipeButton.field_230690_l_+20+space;
			hidePlayerArmorButton.field_230691_m_ = recipeButton.field_230691_m_;
			hideOtherPlayersArmorButton.field_230690_l_ = recipeButton.field_230690_l_+40+space*2;
			hideOtherPlayersArmorButton.field_230691_m_ = recipeButton.field_230691_m_;
			// render tooltips
			if (hidePlayerArmorButton.func_231047_b_(event.getMouseX(), event.getMouseY()))
				Minecraft.getInstance().currentScreen.func_238652_a_(event.getMatrixStack(), new StringTextComponent("Hide your armor"), event.getMouseX(), event.getMouseY());
			else if (hideOtherPlayersArmorButton.func_231047_b_(event.getMouseX(), event.getMouseY()))
				Minecraft.getInstance().currentScreen.func_238652_a_(event.getMatrixStack(), new StringTextComponent("Hide other player's armor"), event.getMouseX(), event.getMouseY());
		}
	}

	/**Hide armor before rendering*/
	@SubscribeEvent
	public static void onRenderLivingPre(RenderLivingEvent.Pre<LivingEntity, EntityModel<LivingEntity>> event) {
		if (event.getEntity() instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity)event.getEntity();
			// hide armor
			if ((hidePlayerArmor && player == Minecraft.getInstance().player) ||
					(hideOtherPlayersArmor && player != Minecraft.getInstance().player)) {
				armor = NonNullList.create();
				for (int i=0; i<player.inventory.armorInventory.size(); ++i) {
					armor.add(player.inventory.armorInventory.get(i).copy());
					player.inventory.armorInventory.set(i, ItemStack.EMPTY);
				}
			}
		}
	}

	/**Add armor back after rendering*/
	@SubscribeEvent
	public static void onRenderLivingPost(RenderLivingEvent.Post<LivingEntity, EntityModel<LivingEntity>> event) {
		if (event.getEntity() instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity)event.getEntity();
			if (armor != null && (hidePlayerArmor && player == Minecraft.getInstance().player) ||
					(hideOtherPlayersArmor && player != Minecraft.getInstance().player)) {
				for (int i=0; i<player.inventory.armorInventory.size(); ++i)
					player.inventory.armorInventory.set(i, armor.get(i));
			}
		}
	}

}
