package io.github.toapuro.derendered.api.render.instancing.particle;

import io.github.toapuro.derendered.Derendered;
import io.github.toapuro.derendered.api.render.shader.ShaderHolder;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ParticleInstancingShader {

    public static final ShaderHolder PARTICLE_INSTANCING = ShaderHolder.of(provider -> new ShaderInstance(
            provider,
            Derendered.getResource("particle_instanced"),
            ParticleVertexFormat.PARTICLE_FULL
    ));

    @SubscribeEvent
    public static void registerShader(RegisterShadersEvent event) {
        PARTICLE_INSTANCING.register(event);
    }
}
