package com.example.soulelytraboostfix.mixin;

import com.kyanite.deeperdarker.DeeperDarkerConfig;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Fixes Elytra Slot's Soul Elytra boost integration for current Deeper and Darker versions.
 *
 * Elytra Slot's SoulElytraBoostPayload was compiled against an older Deeper and Darker where
 * DeeperDarkerConfig.soulElytraCooldown was a plain static int field. In the installed version
 * (1.4.1) it's an instance field of type ModConfigSpec.IntValue on the DeeperDarkerConfig.CONFIG
 * instance, so that old field read throws at runtime the moment the boost key is pressed.
 *
 * This redirects that one field read to the correct modern accessor
 * (DeeperDarkerConfig.CONFIG.soulElytraCooldown.get().intValue()) without touching anything
 * else in Elytra Slot's compiled code. Targeted by string (not a compile-time class reference)
 * so this mod has no compile-time dependency on Elytra Slot at all.
 */
@Mixin(targets = "com.illusivesoulworks.elytraslot.common.integration.deeperdarker.SoulElytraBoostPayload")
public abstract class SoulElytraBoostPayloadMixin {

    @Redirect(
            method = "lambda$handle$0",
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
