package com.falsepattern.dynamicrendering.proxy;

import com.falsepattern.dynamicrendering.drawing.demo.BlockTeapot;
import com.falsepattern.dynamicrendering.drawing.demo.RotatingTeapotTileEntity;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.registry.GameRegistry;

public class CommonProxy {
    public void preInit(FMLPreInitializationEvent event) {
        GameRegistry.registerBlock(new BlockTeapot(), "teapot");
        GameRegistry.registerTileEntity(RotatingTeapotTileEntity.class, "tile_teapot");
    }

    public void init(FMLInitializationEvent event) {
    }

    public void postInit(FMLPostInitializationEvent event) {
    }

    public void serverAboutToStart(FMLServerAboutToStartEvent event) {
    }

    public void serverStarting(FMLServerStartingEvent event) {
    }

    public void serverStarted(FMLServerStartedEvent event) {
    }

    public void serverStopping(FMLServerStoppingEvent event) {
    }

    public void serverStopped(FMLServerStoppedEvent event) {
    }
}
