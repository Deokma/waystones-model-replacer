package dev.deokma.wmr.neoforge;

import dev.deokma.wmr.WaystonesModelReplacer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(WaystonesModelReplacer.MOD_ID)
public class WaystonesModelReplacerNeoForge {

    public WaystonesModelReplacerNeoForge(IEventBus modBus) {
        WaystonesModelReplacer.init();
    }
}
