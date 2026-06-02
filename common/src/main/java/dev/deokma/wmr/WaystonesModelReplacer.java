package dev.deokma.wmr;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Waystones Model Replacer - a cosmetic compatibility addon that replaces the Waystones model,
 * textures, runes and particles with a custom model.
 *
 * <p>This addon contains <b>no gameplay code</b>. The visual replacement is achieved entirely through:
 * <ul>
 *     <li>Resource-pack style overrides of Waystones' block/item models and textures (the custom model).</li>
 *     <li>A fully transparent override of the {@code minecraft:waystone_overlays/waystone_active}
 *         sprite (removes the floating side runes, which Waystones draws as an "active" overlay).</li>
 *     <li>A client-only Mixin that cancels {@code WaystoneBlock.animateTick} (removes the particles)
 *         and a both-sides Mixin that slims {@code WaystoneBlock.getShape} (hitbox). These are the
 *         only behaviours Waystones offers no config/resource hook for.</li>
 * </ul>
 *
 * No Waystones source code is shipped, forked or modified - Waystones remains a separate, required
 * mod dependency and all of its gameplay continues to run untouched.
 */
public final class WaystonesModelReplacer {

    public static final String MOD_ID = "waystones_model_replacer";
    public static final Logger LOGGER = LoggerFactory.getLogger("Waystones Model Replacer");

    private WaystonesModelReplacer() {}

    public static void init() {
        // Nothing to do on the common/server side - the addon is purely cosmetic.
    }

    public static void initClient() {
        LOGGER.info("Waystones Model Replacer active - Waystones model/runes/particles replaced (cosmetic only).");
    }
}
