package com.yidodomates.dogalafet;

import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Random;

@Mod(modid = DogalAfetMod.MODID, name = DogalAfetMod.NAME, version = DogalAfetMod.VERSION)
public class DogalAfetMod {
    public static final String MODID = "dogalafet";
    public static final String NAME = "DoGaLaFet Mod";
    public static final String VERSION = "1.0";

    private int timer = 0;
    private final Random random = new Random();

    @EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            timer++;
            
            // Her 200 tick (yaklaşık 10 saniye) bir yıldırım düşer
            if (timer >= 200) {
                timer = 0;

                net.minecraft.server.MinecraftServer server = net.minecraftforge.fml.common.FMLCommonHandler.instance().getMinecraftServerInstance();
                if (server != null && !server.getPlayerList().getPlayers().isEmpty()) {
                    for (EntityPlayer player : server.getPlayerList().getPlayers()) {
                        World world = player.getEntityWorld();
                        
                        double offsetX = (random.nextDouble() - 0.5) * 30;
                        double offsetZ = (random.nextDouble() - 0.5) * 30;
                        
                        BlockPos targetPos = player.getPosition().add(offsetX, 0, offsetZ);
                        BlockPos surfacePos = world.getTopSolidOrLiquidBlock(targetPos);

                        EntityLightningBolt lightning = new EntityLightningBolt(world, surfacePos.getX(), surfacePos.getY(), surfacePos.getZ(), false);
                        world.spawnEntity(lightning);
                    }
                }
            }
        }
    }
}
