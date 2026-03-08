package io.github.toapuro.derendered.api.config.registry;

import lombok.SneakyThrows;
import me.shedaniel.autoconfig.gui.registry.api.GuiProvider;
import me.shedaniel.autoconfig.gui.registry.api.GuiRegistryAccess;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.impl.builders.BooleanToggleBuilder;
import net.minecraft.network.chat.Component;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EnableConfigGuiProvider implements GuiProvider {
    private static final Component resetButtonKey = Component.translatable("text.cloth-config.reset_value");

    @SuppressWarnings({"rawtypes", "unchecked"})
    @SneakyThrows
    @Override
    public List<AbstractConfigListEntry> get(String i18n, Field field, Object config, Object defaults, GuiRegistryAccess access) {
        ArrayList<AbstractConfigListEntry> entries = new ArrayList<>();

        field.setAccessible(true);
        Object fieldObject = field.get(config);
        if (!(fieldObject instanceof Map map)) {
            throw new IllegalStateException("Field type" + field + " does not match");
        }

        map.forEach((key, value) -> {
            if (!(value instanceof Boolean bool)) {
                throw new IllegalStateException("Value " + value.toString() + " is not boolean");
            }

            entries.add(new BooleanToggleBuilder(resetButtonKey, Component.translatable(i18n), bool)
                    .setSaveConsumer(newValue -> map.put(key, newValue))
                    .build());
        });

        return entries;
    }
}
