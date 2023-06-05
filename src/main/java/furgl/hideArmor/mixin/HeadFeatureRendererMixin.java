package furgl.hideArmor.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import furgl.hideArmor.HideArmor;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.HeadFeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.ModelWithHead;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;

@Mixin(HeadFeatureRenderer.class)
public class HeadFeatureRendererMixin<T extends LivingEntity, M extends EntityModel<T> & ModelWithHead> {

	@Inject(method = "render", at = @At("HEAD"), cancellable = true)
	private void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, 
			T entity, float f, float g, float h, float j, float k, float l, CallbackInfo ci) {
		if (HideArmor.shouldHide(entity, EquipmentSlot.HEAD))
			ci.cancel();
	}
	
}