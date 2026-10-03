package com.yidodomates.dogalafet;

import net.minecraft.entity.effect.LightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@Mod(modid = DogalAfetMod.MODID, name = DogalAfetMod.NAME, version = DogalAfetMod.VERSION)
public class DogalAfetMod {
    public static final String MODID = "dogalafet";
    public static final String NAME = "Dogal Afet Modu";
    public static final String VERSION = "1.0.0";

    private int tickCounter = 0;

    @EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            net.minecraft.server.MinecraftServer server = net.minecraftforge.fml.common.FMLCommonHandler.instance().getMinecraftServerInstance();
            if (server != null) {
                World world = server.getWorld(0);
                if (world != null && !world.isRemote && !world.playerEntities.isEmpty()) {
                    tickCounter++;
                    
                    if (tickCounter >= 400) {
                        tickCounter = 0;
                        triggerDisaster(world);
                    }
                }
            }
        }
    }

    private void triggerDisaster(World world) {
        EntityPlayer player = world.playerEntities.get(world.rand.nextInt(world.playerEntities.size()));
        BlockPos playerPos = player.getPosition();

        int offsetX = world.rand.nextInt(30) - 15;
        int offsetZ = world.rand.nextInt(30) - 15;
        BlockPos targetPos = playerPos.add(offsetX, 0, offsetZ);
        targetPos = world.getPrecipitationHeight(targetPos);

        int disasterType = world.rand.nextInt(3);

        if (disasterType == 0) {
            LightningBolt lightning = new LightningBolt(world, targetPos.getX(), targetPos.getY(), targetPos.getZ(), false);
            world.addWeatherEffect(lightning);
        } else if (disasterType == 1) {
            world.createExplosion(null, targetPos.getX(), targetPos.getY(), targetPos.getZ(), 3.0f, true);
        } else {
            world.createExplosion(null, playerPos.getX(), playerPos.getY(), playerPos.getZ(), 2.0f, false);
        }
    }
}
