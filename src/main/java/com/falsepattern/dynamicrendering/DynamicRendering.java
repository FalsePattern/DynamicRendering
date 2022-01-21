package com.falsepattern.dynamicrendering;

import com.falsepattern.dynamicrendering.proxy.CommonProxy;
import com.falsepattern.lib.api.DependencyLoader;
import com.falsepattern.lib.api.SemanticVersion;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.locks.ReentrantLock;

@Mod(modid = ModInfo.MODID,
     version = ModInfo.VERSION,
     name = ModInfo.MODNAME,
     acceptedMinecraftVersions = "[1.7.10]",
     dependencies = "after:lightingoverhaul;" +
                    "required-after:falsepatternlib@[0.3.1,);" +
                    "required-after:triangulator@[1.1.1,);")
public class DynamicRendering {
    private static final Logger LOG = LogManager.getLogger(ModInfo.MODNAME);

    @Mod.Instance(ModInfo.MODID)
    public static DynamicRendering instance;

    @SidedProxy(clientSide = ModInfo.GROUPNAME + "." + ModInfo.MODID + ".proxy.ClientProxy", serverSide = ModInfo.GROUPNAME + "." + ModInfo.MODID + ".proxy.ServerProxy")
    public static CommonProxy proxy;

    public DynamicRendering() {
        DependencyLoader.addMavenRepo("https://repo1.maven.org/maven2/");
        DependencyLoader.addMavenRepo("https://maven.falsepattern.com/");
        DependencyLoader.builder()
                        .loadingModId(ModInfo.MODID)
                        .groupId("com.falsepattern")
                        .artifactId("json")
                        .minVersion(new SemanticVersion(0, 1, 2))
                        .maxVersion(new SemanticVersion(0, 1, Integer.MAX_VALUE))
                        .preferredVersion(new SemanticVersion(0, 1, 2))
                        .build();
        DependencyLoader.builder()
                        .loadingModId(ModInfo.MODID)
                        .groupId("de.javagl")
                        .artifactId("obj")
                        .minVersion(new SemanticVersion(0, 3, 0))
                        .maxVersion(new SemanticVersion(0, 3, Integer.MAX_VALUE))
                        .preferredVersion(new SemanticVersion(0, 3, 0))
                        .build();
        DependencyLoader.builder()
                        .loadingModId(ModInfo.MODID)
                        .groupId("org.joml")
                        .artifactId("joml")
                        .minVersion(new SemanticVersion(1, 10, 0))
                        .maxVersion(new SemanticVersion(1, 10, Integer.MAX_VALUE))
                        .preferredVersion(new SemanticVersion(1, 10, 2))
                        .build();
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }

    @Mod.EventHandler
    public void serverAboutToStart(FMLServerAboutToStartEvent event) {
        proxy.serverAboutToStart(event);
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        proxy.serverStarting(event);
    }

    @Mod.EventHandler
    public void serverStarted(FMLServerStartedEvent event) {
        proxy.serverStarted(event);
    }

    @Mod.EventHandler
    public void serverStopping(FMLServerStoppingEvent event) {
        proxy.serverStopping(event);
    }

    @Mod.EventHandler
    public void serverStopped(FMLServerStoppedEvent event) {
        proxy.serverStopped(event);
    }

    public static void debug(String message) {
        LOG.debug(message);
    }

    public static void info(String message) {
        LOG.info(message);
    }

    public static void warn(String message) {
        LOG.warn(message);
    }

    public static void error(String message) {
        LOG.error(message);
    }


}
