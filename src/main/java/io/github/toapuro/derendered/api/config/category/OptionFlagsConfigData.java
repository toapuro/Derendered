package io.github.toapuro.derendered.api.config.category;

import io.github.toapuro.derendered.api.config.IConfigOption;
import io.github.toapuro.derendered.api.config.option.ErrorResult;
import io.github.toapuro.derendered.api.config.registry.IOptionFlagsValidator;
import lombok.extern.slf4j.Slf4j;
import me.shedaniel.autoconfig.ConfigData;
import net.minecraft.network.chat.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
public abstract class OptionFlagsConfigData<T extends IConfigOption> implements ConfigData, IOptionFlagsValidator {

    protected abstract Map<String, Boolean> mutableOptionMap();

    protected abstract T[] options();

    public void setEnabled(T option, boolean enabled) {
        mutableOptionMap().put(option.getId(), enabled);
    }

    public boolean isEnabled(T option) {
        boolean enabled = mutableOptionMap().getOrDefault(option.getId(), option.isDefaultEnabled());
        return option.properties().getFlagModifier().apply(enabled);
    }

    @Override
    public Optional<Component> checkError(String name, boolean flag) {
        List<T> options = Arrays.stream(options())
                .filter(option -> option.getId().equals(name))
                .toList();

        for (T option : options) {
            ErrorResult result = option.properties().getErrorChecker().apply(flag);
            Component component = result.errorComponent();
            if(component != null) {
                return Optional.of(component);
            }
        }
        return Optional.empty();
    }

    @Override
    public void validatePostLoad() throws ValidationException {
        Map<String, Boolean> optionMap = mutableOptionMap();
        for (T option : options()) {
            if(!option.properties().isValidateOnLoad()) {
                continue;
            }

            boolean flag = optionMap.getOrDefault(option.getId(), option.isDefaultEnabled());
            ErrorResult result = option.properties().getErrorChecker().apply(flag);

            if(result.errorComponent() != null) {
                log.error("Validation failed {}=={}: {}", option.getId(), flag, result.errorComponent().getString());
            }

            Optional<Boolean> modification = result.flagModification();
            modification.ifPresent(aBoolean -> setEnabled(option, aBoolean));
        }
    }
}
