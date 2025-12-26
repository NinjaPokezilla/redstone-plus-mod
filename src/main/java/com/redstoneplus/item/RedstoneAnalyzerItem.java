package com.redstoneplus.item;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RedstoneAnalyzerItem extends Item {
    public RedstoneAnalyzerItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();
        PlayerEntity player = context.getPlayer();
        
        if (!world.isClient && player != null) {
            BlockState state = world.getBlockState(pos);
            
            StringBuilder info = new StringBuilder();
            info.append("§6=== Análise de Redstone ===§r\n");
            info.append("§7Bloco: §f").append(state.getBlock().getName().getString()).append("\n");
            
            // Check if block emits redstone
            if (state.emitsRedstonePower()) {
                info.append("§7Emite sinal: §aSIM\n");
            } else {
                info.append("§7Emite sinal: §cNÃO\n");
            }
            
            // Get redstone power at position
            int receivedPower = world.getReceivedRedstonePower(pos);
            info.append("§7Força recebida: §e").append(receivedPower).append("\n");
            
            // Check for POWER property
            if (state.contains(Properties.POWER)) {
                int power = state.get(Properties.POWER);
                info.append("§7Força do sinal: §e").append(power).append("\n");
            }
            
            // Check for POWERED property
            if (state.contains(Properties.POWERED)) {
                boolean powered = state.get(Properties.POWERED);
                info.append("§7Estado: ").append(powered ? "§aLIGADO" : "§cDESLIGADO").append("\n");
            }
            
            // Check for FACING property
            if (state.contains(Properties.HORIZONTAL_FACING)) {
                info.append("§7Direção: §f").append(state.get(Properties.HORIZONTAL_FACING).getName()).append("\n");
            }
            
            player.sendMessage(Text.literal(info.toString()), false);
        }
        
        return ActionResult.SUCCESS;
    }
}
