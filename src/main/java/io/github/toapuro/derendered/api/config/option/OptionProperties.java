package io.github.toapuro.derendered.api.config.option;

import lombok.Builder;
import lombok.Getter;

import java.util.function.Function;
import java.util.function.UnaryOperator;

@Builder
@Getter
public class OptionProperties {

    private Function<Boolean, ErrorResult> errorChecker;
    private UnaryOperator<Boolean> flagModifier;
    private boolean validateOnLoad;
}
