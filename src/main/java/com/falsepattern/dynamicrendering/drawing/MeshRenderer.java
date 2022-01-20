package com.falsepattern.dynamicrendering.drawing;

import lombok.val;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class MeshRenderer extends TileEntitySpecialRenderer {
    public MeshRenderer() {
    }

    @Override
    public void bindTexture(ResourceLocation texture) {
        super.bindTexture(texture);
    }

    @Override
    public void renderTileEntityAt(TileEntity entity, double x, double y, double z, float partialTickTime) {
        ModelTileEntity te = (ModelTileEntity) entity;
        if (te.model == null) {
            val paths = new ModelResourcePaths();
            te.getModelResources(paths);
            te.model = new Model(paths.modID, paths.meshDir, paths.texture);
            te.invalidateCallback = (self) -> self.model.close();
        }
        te.updateModel(partialTickTime);
        GL11.glPushMatrix();
        GL11.glTranslated(x, y, z);
        te.model.draw(this, x, y, z);
        GL11.glPopMatrix();
    }
}
