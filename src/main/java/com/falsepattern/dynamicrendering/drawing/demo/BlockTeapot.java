package com.falsepattern.dynamicrendering.drawing.demo;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockTeapot extends Block {
    public BlockTeapot() {
        super(Material.rock);
    }

    @Override
    public int getRenderType() {
        return -1;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z) {
        world.setTileEntity(x, y, z, this.createTileEntity(world, world.getBlockMetadata(x, y, z)));
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public boolean hasTileEntity(int meta) {
        return true;
    }

    @Override
    public int tickRate(World p_149738_1_) {
        return super.tickRate(p_149738_1_);
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata) {
        return new RotatingTeapotTileEntity();
    }
}
