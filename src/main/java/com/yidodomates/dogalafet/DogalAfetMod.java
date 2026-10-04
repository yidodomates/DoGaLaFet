package com.yidodomates.dogalafet;

import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = DogalAfetMod.MODID, name = DogalAfetMod.NAME, version = DogalAfetMod.VERSION)
public class DogalAfetMod {
    public static final String MODID = "dogalafet";
    public static final String NAME = "DoGaLaFet Mod";
    public static final String VERSION = "1.0";

    private static Logger logger;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        logger.info("DoGaLaFet Mod baslatiliyor!");
    }

    // Örnek bir metod içerisinde yıldırım çağırma mantığı:
    public void spawnLightning(World world, BlockPos targetPos) {
        if (!world.isRemote) {
            // Doğru sınıf ismi olan EntityLightningBolt kullanıldı
            EntityLightningBolt lightning = new EntityLightningBolt(world, targetPos.getX(), targetPos.getY(), targetPos.getZ(), false);
            world.spawnEntity(lightning);
        }
    }
}
