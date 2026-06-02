package dev.deokma.wmr.fabric;

import dev.deokma.wmr.WaystonesModelReplacer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class WaystonesModelReplacerFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        WaystonesModelReplacer.initClient();
    }
}
