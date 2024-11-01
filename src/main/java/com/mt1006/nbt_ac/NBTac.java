package com.mt1006.nbt_ac;

import com.mojang.logging.LogUtils;
import com.mt1006.nbt_ac.autocomplete.loader.Loader;
import com.mt1006.nbt_ac.autocomplete.loader.resourceloader.ResourceLoader;
import com.mt1006.nbt_ac.autocomplete.suggestions.NbtSuggestion;
import com.mt1006.nbt_ac.config.ModConfig;
import com.mt1006.nbt_ac.config.gui.ConfigScreenFactory;
import com.mt1006.nbt_ac.utils.Fields;
import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.slf4j.Logger;

@Mod(NBTac.MOD_ID)
@Mod.EventBusSubscriber(modid = NBTac.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class NBTac
{
	public static final String MOD_ID = "nbt_ac";
	public static final String VERSION = "1.3.7";
	public static final String FOR_VERSION = "1.21.3";
	public static final String FOR_LOADER = "Forge";
	public static final Logger LOGGER = LogUtils.getLogger();
	public static final boolean isDedicatedServer = FMLEnvironment.dist.isDedicatedServer();

	public NBTac(FMLJavaModLoadingContext ctx)
	{
		if (isDedicatedServer) { return; }

		MinecraftForge.EVENT_BUS.register(this);
		ctx.registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, ConfigScreenFactory::create);

		((ReloadableResourceManager)Minecraft.getInstance().getResourceManager()).registerReloadListener(new ResourceLoader());
	}

	@SubscribeEvent
	public static void setup(final FMLCommonSetupEvent event)
	{
		if (isDedicatedServer)
		{
			LOGGER.info("Dedicated server detected - mod setup stopped!");
			return;
		}

		ModConfig.load();
		Fields.init();
		NbtSuggestion.Type.init();
	}

	@SubscribeEvent
	public static void loadComplete(FMLLoadCompleteEvent event)
	{
		if (isDedicatedServer) { return; }
		if (ModConfig.useNewThread.val) { new Thread(Loader::load).start(); }
		else { Loader.load(); }
	}
}
