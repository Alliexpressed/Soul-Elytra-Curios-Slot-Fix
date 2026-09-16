package com.example.soulelytraboostfix.mixin;

import com.kyanite.deeperdarker.DeeperDarkerConfig;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Same underlying bug as SoulElytraBoostPayloadMixin, in a second location: Elytra Slot's
 * DeeperDarkerPlugin.tick() (which runs every tick while the Soul Elytra is equipped, to show
 * the boost cooldown percentage in the action bar) also reads
 * DeeperDarkerConfig.soulElytraCooldown as an old-style static int field, which no longer
 * exists in current Deeper and Darker versions.
 */
@Mixin(targets = "com.illusivesoulworks.elytraslot.common.integration.deeperdarker.DeeperDarkerPlugin")
public abstract class DeeperDarkerPluginMixin {

    @Redirect(
            method = "tick(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/Entity;)V",
            at = @At(
                    value = "FIELD",
                    opcode = Opcodes.GETSTATIC,
                    target = "Lcom/kyanite/deeperdarker/DeeperDarkerConfig;soulElytraCooldown:I",
                    remap = false
            ),
            remap = false
    )
    private static int soulelytraboostfix$readCooldown() {
        return DeeperDarkerConfig.CONFIG.soulElytraCooldown.get().intValue();
    }
}
