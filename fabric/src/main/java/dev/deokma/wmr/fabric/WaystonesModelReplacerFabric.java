package dev.deokma.wmr.fabric;

import dev.deokma.wmr.WaystonesModelReplacer;
import net.fabricmc.api.ModInitializer;

public class WaystonesModelReplacerFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        WaystonesModelReplacer.init();
    }
}
