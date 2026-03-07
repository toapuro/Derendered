package io.github.toapuro.derendered.api.mixin;

import io.github.toapuro.derendered.api.config.ModConfig;
import io.github.toapuro.derendered.api.config.PreloadOption;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.spongepowered.asm.service.MixinService;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

@Slf4j
public class MixinPlugin implements IMixinConfigPlugin {

    private final String PRELOAD_OPTION_DESC = PreloadOption.class.descriptorString();
    private final String REQUIRE_PRELOAD_DESC = RequirePreloadOption.class.descriptorString();
    private final String REQUIRE_PRELOADS_DESC = RequirePreloadOptions.class.descriptorString();

    @Override
    public void onLoad(String mixinPackage) {
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        try {
            ClassNode classNode = MixinService.getService().getBytecodeProvider().getClassNode(mixinClassName);
            if(classNode.visibleAnnotations == null) {
                return true;
            }
            List<PreloadOption> options = classNode.visibleAnnotations.stream()
                    // RequirePreloadOption, RequirePreloadOptions -> [RequirePreloadOption, ...]
                    .flatMap(this::expandAnnotations)
                    // RequirePreloadOption -> String[] (Enum)
                    .map(node -> (String[]) getAnnotationValue(node, "value"))
                    .filter(Objects::nonNull)
                    // String[] (Enum) -> String[] (PreloadOption)
                    .filter(info -> info[0].equals(PRELOAD_OPTION_DESC))
                    .map(info -> PreloadOption.valueOf(info[1]))
                    .toList();


            for (PreloadOption option : options) {
                ModConfig modConfig = ModConfig.get();
                if(!modConfig.isPreloadOptionEnabled(option)) {
                    return false;
                }
            }

            return true;
        } catch (ClassNotFoundException | IOException e) {
            log.debug("Failed to load mixin class", e);
            return false;
        }
    }

    private @NotNull Stream<AnnotationNode> expandAnnotations(AnnotationNode node) {
        if(node.desc.equals(REQUIRE_PRELOAD_DESC)) {
            return Stream.of(node);
        } else if(node.desc.equals(REQUIRE_PRELOADS_DESC)) {
            List<AnnotationNode> nodes = Objects.requireNonNullElse(MixinPlugin.getAnnotationValue(node, "value"), Collections.emptyList());
            return nodes.stream();
        }
        return Stream.empty();
    }

    @SuppressWarnings({"unchecked", "SameParameterValue"})
    static <T> T getAnnotationValue(AnnotationNode node, String key) {
        if (node.values == null) return null;
        for (int head = 0; head < node.values.size(); head += 2) {
            if (key.equals(node.values.get(head))) {
                return (T) node.values.get(head + 1);
            }
        }
        return null;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }
}
