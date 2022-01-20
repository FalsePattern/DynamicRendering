package com.falsepattern.dynamicrendering;

import lombok.SneakyThrows;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;

public class ResourceUtil {
    @SneakyThrows
    public static InputStream toInputStream(ResourceLocation location) {
        return Minecraft.getMinecraft().getResourceManager().getResource(location).getInputStream();
    }

    @SneakyThrows
    public static String toString(ResourceLocation location) {
        return IOUtils.toString(toInputStream(location));
    }
}
