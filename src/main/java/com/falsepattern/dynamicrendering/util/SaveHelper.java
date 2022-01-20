package com.falsepattern.dynamicrendering.util;

import lombok.val;
import net.minecraft.nbt.NBTTagCompound;
import org.joml.Quaterniond;
import org.joml.Vector3d;
import org.joml.Vector3i;

public class SaveHelper {
    public static void serializeVector3d(Vector3d vector, NBTTagCompound nbt, String name) {
        val subCompound = new NBTTagCompound();
        subCompound.setDouble("x", vector.x);
        subCompound.setDouble("y", vector.y);
        subCompound.setDouble("z", vector.z);
        nbt.setTag(name, subCompound);
    }

    public static void serializeVector3i(Vector3i vector, NBTTagCompound nbt, String name) {
        nbt.setIntArray(name, new int[]{vector.x, vector.y, vector.z});
    }

    public static void serializeQuaterniond(Quaterniond quaternion, NBTTagCompound nbt, String name) {
        val subCompound = new NBTTagCompound();
        subCompound.setDouble("x", quaternion.x);
        subCompound.setDouble("y", quaternion.y);
        subCompound.setDouble("z", quaternion.z);
        subCompound.setDouble("w", quaternion.w);
        nbt.setTag(name, subCompound);
    }

    public static void deserializeVector3d(Vector3d output, NBTTagCompound nbt, String name) {
        val subCompound = nbt.getCompoundTag(name);
        output.set(subCompound.getDouble("x"), subCompound.getDouble("y"), subCompound.getDouble("z"));
    }

    public static void deserializeVector3i(Vector3i output, NBTTagCompound nbt, String name) {
        val arr = nbt.getIntArray(name);
        if (arr.length == 0) {
            output.set(0);
        } else {
            output.set(arr[0], arr[1], arr[2]);
        }
    }

    public static void deserializeQuaterniond(Quaterniond output, NBTTagCompound nbt, String name) {
        val subCompound = nbt.getCompoundTag(name);
        output.set(subCompound.getDouble("x"), subCompound.getDouble("y"), subCompound.getDouble("z"), subCompound.getDouble("w"));
    }
}
