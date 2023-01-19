package com.mt1006.nbt_ac.mixin.client;

import com.mt1006.nbt_ac.autocomplete.loader.Loader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.main.GameConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin
{
	@Inject(method = "<init>", at = @At("RETURN"))
	private void atConstructor(GameConfig gameConfig, CallbackInfo callbackInfo)
	{
		new Thread(Loader::load).start();
	}
}
