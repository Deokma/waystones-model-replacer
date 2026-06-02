package dev.deokma.wmr.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Removes the {@code ParticleTypes.PORTAL} (and accompanying {@code ENCHANT}) particles that
 * Waystones spawns around an activated waystone.
 *
 * <p>Why a Mixin and not a config/resource override? Waystones spawns these particles directly in
 * {@code WaystoneBlock.animateTick} via {@code level.addParticle(...)}. There is no client config
 * option for them, and they are code-driven (a resource pack cannot suppress them without globally
 * re-texturing the vanilla portal/enchant particles, which would affect nether portals etc.).
 *
 * <p>Safety: the target class is referenced as a string so this file also compiles in the
 * loader-agnostic {@code common} module (no Waystones on its classpath); the Fabric/NeoForge builds
 * do have it, so the annotation processor resolves {@code animateTick} and produces a correct refmap.
 * {@code animateTick} is a client-only render hook, so this Mixin is registered under {@code "client"}
 * and never loads on dedicated servers. It only cancels particle spawning.
 */
@Mixin(targets = "net.blay09.mods.waystones.block.WaystoneBlock")
public class WaystoneBlockParticlesMixin {

    @Inject(method = "animateTick", at = @At("HEAD"), cancellable = true)
    private void wmr$suppressWaystoneParticles(BlockState state, Level level, BlockPos pos, RandomSource random, CallbackInfo ci) {
        ci.cancel();
    }
}
