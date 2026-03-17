package io.github.toapuro.derendered.api.render.shader;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.server.packs.resources.ResourceProvider;
import net.minecraftforge.client.event.RegisterShadersEvent;

import java.io.IOException;
import java.util.function.Supplier;

@RequiredArgsConstructor(staticName = "of")
public class ShaderHolder {

    private final ShaderInitializer shaderInitializer;
    private ShaderInstance shader = null;

    @SneakyThrows
    public void register(RegisterShadersEvent event) {
        event.registerShader(
                shaderInitializer.create(event.getResourceProvider()),
                this::setup
        );
    }

    public ShaderInstance get() {
        if(shader == null) {
            throw new RuntimeException("Not initialized!");
        }
        return shader;
    }

    public Supplier<ShaderInstance> asSupplier() {
        return this::get;
    }

    public void setup(ShaderInstance shaderInstance) {
        shader = shaderInstance;
    }

    @FunctionalInterface
    public interface ShaderInitializer {
        ShaderInstance create(ResourceProvider provider) throws IOException;
    }
}
