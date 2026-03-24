package io.github.toapuro.derendered.api.render.queryculling;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import io.github.toapuro.derendered.Derendered;
import io.github.toapuro.derendered.api.render.shader.ShaderHolder;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class QueryCullingShader {

    public static final ShaderHolder QUERY_BOX = ShaderHolder.of(provider -> new ShaderInstance(
            provider,
            Derendered.getResource("query_box"),
            DefaultVertexFormat.POSITION
    ));

    @SubscribeEvent
    public static void registerShader(RegisterShadersEvent event) {
        QUERY_BOX.register(event);
    }
}
