package io.github.toapuro.derendered.api.config;

import io.github.toapuro.derendered.lang.DerenderedLang;

public interface IOption {
    String getId();
    DerenderedLang getLang();
    boolean isDefaultEnabled();
}
