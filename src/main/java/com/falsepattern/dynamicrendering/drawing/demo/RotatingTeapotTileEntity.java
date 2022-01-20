package com.falsepattern.dynamicrendering.drawing.demo;

import com.falsepattern.dynamicrendering.ModInfo;
import com.falsepattern.dynamicrendering.drawing.ModelResourcePaths;
import com.falsepattern.dynamicrendering.drawing.ModelTileEntity;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import lombok.val;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;

public class RotatingTeapotTileEntity extends ModelTileEntity {
    private static final float RADIANS_PER_SECOND = (float) (Math.PI / 4f);
    private static final float RADIANS_PER_TICK = RADIANS_PER_SECOND / 20;
    private int ticks = 0;
    @Override
    @SideOnly(Side.CLIENT)
    public void getModelResources(ModelResourcePaths output) {
        output.modID = ModInfo.MODID;
        output.meshDir = "meshes/teapot";
        output.texture = "textures/jade.png";
    }

    @Override
    public void updateEntity() {
        ticks++;
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        if (!worldObj.isRemote) {
            markDirty();
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        ticks = nbt.getInteger("ticks");
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setInteger("ticks", ticks);
    }

    @Override
    public Packet getDescriptionPacket() {
        val nbt = new NBTTagCompound();
        nbt.setInteger("ticks", ticks);
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 1, nbt);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        ticks = pkt.func_148857_g().getInteger("ticks");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void updateModel(float partialTickTime) {
        model.rotation.setAngleAxis((ticks + partialTickTime) * RADIANS_PER_TICK, 0, 1, 0);
    }
}
