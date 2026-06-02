package dev.deokma.wmr.neoforge;

import dev.deokma.wmr.WaystonesModelReplacer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;

@Mod(value = WaystonesModelReplacer.MOD_ID, dist = Dist.CLIENT)
public class WaystonesModelReplacerNeoForgeClient {

    public WaystonesModelReplacerNeoForgeClient() {
        WaystonesModelReplacer.initClient();
    }
}
