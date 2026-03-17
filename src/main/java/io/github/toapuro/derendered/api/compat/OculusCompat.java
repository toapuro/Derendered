package io.github.toapuro.derendered.api.compat;

import net.irisshaders.iris.api.v0.IrisApi;
import net.minecraftforge.fml.ModList;

public class OculusCompat {

    public static String MODID = "oculus";

    public static boolean isEnabled() {
        return ModList.get().isLoaded(MODID);
    }

    public static boolean isShaderEnabledSafe() {
        return isEnabled() && IrisApi.getInstance().isShaderPackInUse();
    }
}
