package com.falsepattern.dynamicrendering.drawing;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;

import java.util.function.Consumer;

public abstract class ModelTileEntity extends TileEntity {
    @SideOnly(Side.CLIENT)
    public Model model;

    Consumer<ModelTileEntity> invalidateCallback;

    @Override
    public void invalidate() {
        super.invalidate();
        if (invalidateCallback != null) invalidateCallback.accept(this);
        invalidateCallback = null;
    }

    @SideOnly(Side.CLIENT)
    public abstract void getModelResources(ModelResourcePaths output);

    @SideOnly(Side.CLIENT)
    public void updateModel(float partialTickTime){}

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return INFINITE_EXTENT_AABB;
    }
}
