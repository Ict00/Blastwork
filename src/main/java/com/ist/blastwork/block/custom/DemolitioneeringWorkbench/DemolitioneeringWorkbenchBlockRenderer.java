package com.ist.blastwork.block.custom.DemolitioneeringWorkbench;

import com.ist.blastwork.Blastwork;
import com.ist.blastwork.block.custom.FireExplosiveBarrel.FireExplosiveBarrelBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;

public class DemolitioneeringWorkbenchBlockRenderer implements BlockEntityRenderer<DemolitioneeringWorkbenchBlockEntity> {
    public DemolitioneeringWorkbenchBlockRenderer(BlockEntityRendererProvider.Context ctx) {

    }

    @Override
    public void render(DemolitioneeringWorkbenchBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        int angle = 0;
        int add = blockEntity.getSlots() == 0 ? 0 : 360/blockEntity.getSlots();


        for (int i = 0; i < blockEntity.getSlots(); i++) {
            ItemStack stack = blockEntity.getStackInSlot(i);

            poseStack.pushPose();
            var off = FireExplosiveBarrelBlockEntity.getPointsOnCircle(0.3f, angle);
            if (add == 360)
                poseStack.translate(0.5f, 1.35f, 0.5f);
            else
                poseStack.translate(0.5f+off.x, 1.35f, 0.5f+off.y);
            poseStack.scale(0.5f, 0.5f, 0.5f);
            poseStack.mulPose(Axis.YP.rotationDegrees(DemolitioneeringWorkbenchBlockEntity.getRot()));
            itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED, 200, OverlayTexture.NO_OVERLAY,
                    poseStack, bufferSource, blockEntity.getLevel(), 1);

            poseStack.popPose();

            angle += add;
        }
    }

    public int light(Level level, BlockPos pos) {
        int b = level.getBrightness(LightLayer.BLOCK, pos);
        int s = level.getBrightness(LightLayer.SKY, pos);

        return LightTexture.pack(b, s);
    }
}
