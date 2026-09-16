# Soul Elytra Boost Fix

Fixes Elytra Slot's built-in Soul Elytra boost integration (Deeper and Darker compatibility)
so it works with current Deeper and Darker versions, without touching Elytra Slot's own jar.

## The bug

Elytra Slot's `SoulElytraBoostPayload` (the code that handles the boost keypress) was compiled
against an older Deeper and Darker where `DeeperDarkerConfig.soulElytraCooldown` was a plain
static `int` field. In current Deeper and Darker, it's an instance field of type
`ModConfigSpec.IntValue` on `DeeperDarkerConfig.CONFIG`. Elytra Slot's old bytecode still tries
to read it as a raw `int`, which throws the moment you press the boost key - nothing happens,
silently, unless you're watching the log.

## The fix

A single, narrow `@Redirect` mixin that swaps that one field read for the correct modern call
(`DeeperDarkerConfig.CONFIG.soulElytraCooldown.get().intValue()`). Nothing else in Elytra
Slot's compiled code is touched. The mixin targets Elytra Slot's class by name (a string, not
a compile-time reference), so this mod has no compile-time dependency on Elytra Slot's jar at
all - only on Deeper and Darker's, to compile against the real `DeeperDarkerConfig` class.

The mixin config is marked `"required": false`, so this mod does nothing (rather than crash)
if either Elytra Slot or Deeper and Darker isn't installed.

## Building

```bash
gradle build
```

Output: `build/libs/soulelytraboostfix-1.0.0.jar`.

## Installing

Drop the jar in `mods/` alongside Elytra Slot and Deeper and Darker, on both client and
server.

## Updating to a different Deeper and Darker version

If `DeeperDarkerConfig.soulElytraCooldown`'s type or Elytra Slot's internal method names ever
change, `SoulElytraBoostPayloadMixin.java` will need updating to match. Drop the new Deeper
and Darker jar in `libs/`, update `deeperdarker_jar_name` in `gradle.properties`, and rebuild.
