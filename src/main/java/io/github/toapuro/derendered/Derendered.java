package io.github.toapuro.derendered;

import io.github.toapuro.derendered.api.config.ModConfig;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Derendered.MODID)
public class Derendered {
    public static final String MODID = "derendered";

    public Derendered(FMLJavaModLoadingContext ctx) {
        ctx.registerExtensionPoint(
                ConfigScreenHandler.ConfigScreenFactory.class,
                () -> new ConfigScreenHandler.ConfigScreenFactory(
                        (minecraft, parent) ->
                                AutoConfig.getConfigScreen(ModConfig.class, parent).get()
                )
        );

        ModConfig.LOADER.ensureLoaded();
    }

    public static ResourceLocation getResource(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }
}
