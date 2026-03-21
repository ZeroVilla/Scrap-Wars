package net.zerostudios.scrapwars.common.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

public class ScrapPileBlock extends Block {

    public ScrapPileBlock() {
        super(BlockBehaviour.Properties.of()
                .mapColor(MapColor.METAL)
                .strength(1.2F, 3.0F)
                .sound(SoundType.METAL)
                .requiresCorrectToolForDrops());
    }
}