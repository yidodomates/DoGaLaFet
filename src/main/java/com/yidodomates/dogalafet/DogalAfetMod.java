package com.yidodomates.dogalafet;

import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
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

    // Özel Ok Eşyası
    public static Item LIGHTNING_ARROW = new ItemArrow() {
        @Override
        public EntityArrow createArrow(World worldIn, ItemStack stack, net.minecraft.entity.EntityLivingBase shooter) {
            EntityArrow arrow = super.createArrow(worldIn, stack, shooter);
            arrow.addTag("LightningArrow"); // Oku özel kılan etiket
            return arrow;
        }
    }.setUnlocalizedName("lightning_arrow").setRegistryName("lightning_arrow");

    private int timer = 0;
    private final Random random = new Random();

    @EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Mod.EventBusSubscriber
    public static class RegistrationHandler {
        @SubscribeEvent
        public static void registerItems(RegistryEvent.Register<Item> event) {
            event.getRegistry().register(LIGHTNING_ARROW);
        }
    }

    // 1. DÜZENLİ YILDIRIM VE SOHBET GERİ SAYIMI (10 Saniye)
    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        timer++;
        
        net.minecraft.server.MinecraftServer server = net.minecraftforge.fml.common.FMLCommonHandler.instance().getMinecraftServerInstance();
        if (server == null || server.getPlayerList().getPlayers().isEmpty()) return;

        // Her 1 saniyede bir (20 tick) sohbeti bilgilendir
        if (timer % 20 == 0) {
            int remainingSeconds = (200 - timer) / 20;
            if (remainingSeconds > 0) {
                for (EntityPlayer player : server.getPlayerList().getPlayers()) {
                    player.sendMessage(new TextComponentString("§e[Afet] §fSonraki felaket için kalan süre: §c" + remainingSeconds + " §fsaniye!"));
                }
            }
        }

        // 10 Saniye dolduğunda (200 tick)
        if (timer >= 200) {
            timer = 0;
            for (EntityPlayer player : server.getPlayerList().getPlayers()) {
                World world = player.getEntityWorld();
                
                double offsetX = (random.nextDouble() - 0.5) * 30;
                double offsetZ = (random.nextDouble() - 0.5) * 30;
                
                BlockPos targetPos = player.getPosition().add(offsetX, 0, offsetZ);
                BlockPos surfacePos = world.getTopSolidOrLiquidBlock(targetPos);

                EntityLightningBolt lightning = new EntityLightningBolt(world, surfacePos.getX(), surfacePos.getY(), surfacePos.getZ(), false);
                world.spawnEntity(lightning);
                
                player.sendMessage(new TextComponentString("§c⚡ YILDIRIM DÜŞTÜ! ⚡"));
            }
        }
    }

    // 2. ÖZEL OK YERE VEYA BİRİNE ÇARPTIĞINDA 5 YILDIRIM ÇAKTIRMA
    @SubscribeEvent
    public void onArrowHit(ProjectileImpactEvent.Arrow event) {
        EntityArrow arrow = event.getArrow();
        World world = arrow.getEntityWorld();

        if (!world.isRemote && arrow.getTags().contains("LightningArrow")) {
            RayTraceResult ray = event.getRayTraceResult();
            BlockPos hitPos = ray.getBlockPos();

            if (hitPos == null && ray.entityHit != null) {
                hitPos = ray.entityHit.getPosition();
            }

            if (hitPos != null) {
                // Çarptığı yere 5 adet yıldırım düşürür
                for (int i = 0; i < 5; i++) {
                    double offsetX = (random.nextDouble() - 0.5) * 6;
                    double offsetZ = (random.nextDouble() - 0.5) * 6;
                    
                    BlockPos strikePos = hitPos.add(offsetX, 0, offsetZ);
                    BlockPos surfacePos = world.getTopSolidOrLiquidBlock(strikePos);

                    EntityLightningBolt lightning = new EntityLightningBolt(world, surfacePos.getX(), surfacePos.getY(), surfacePos.getZ(), false);
                    world.spawnEntity(lightning);
                }
            }
        }
    }
}
