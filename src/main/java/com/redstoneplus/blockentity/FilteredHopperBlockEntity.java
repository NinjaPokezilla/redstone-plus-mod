package com.redstoneplus.blockentity;

import com.redstoneplus.registry.ModBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class FilteredHopperBlockEntity extends BlockEntity implements SidedInventory, NamedScreenHandlerFactory {
    private DefaultedList<ItemStack> inventory = DefaultedList.ofSize(6, ItemStack.EMPTY); // 5 slots + 1 filter
    private ItemStack filterItem = ItemStack.EMPTY;
    private boolean whitelistMode = true;

    public FilteredHopperBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.FILTERED_HOPPER_BE, pos, state);
    }

    public static void tick(World world, BlockPos pos, BlockState state, FilteredHopperBlockEntity be) {
        // Hopper functionality would go here
    }

    public ItemStack getFilterItem() {
        return filterItem;
    }

    public void setFilterItem(ItemStack stack) {
        this.filterItem = stack.copy();
        markDirty();
    }

    public boolean isWhitelistMode() {
        return whitelistMode;
    }

    public void toggleMode() {
        whitelistMode = !whitelistMode;
        markDirty();
    }

    public boolean canAcceptItem(ItemStack stack) {
        if (filterItem.isEmpty()) return true;
        
        boolean matches = ItemStack.areItemsEqual(filterItem, stack);
        return whitelistMode ? matches : !matches;
    }

    @Override
    public int[] getAvailableSlots(Direction side) {
        return new int[]{0, 1, 2, 3, 4};
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
        return canAcceptItem(stack);
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction dir) {
        return true;
    }

    @Override
    public int size() {
        return 5;
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : inventory) {
            if (!stack.isEmpty()) return false;
        }
        return true;
    }

    @Override
    public ItemStack getStack(int slot) {
        return inventory.get(slot);
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        return Inventories.splitStack(inventory, slot, amount);
    }

    @Override
    public ItemStack removeStack(int slot) {
        return Inventories.removeStack(inventory, slot);
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        inventory.set(slot, stack);
        if (stack.getCount() > getMaxCountPerStack()) {
            stack.setCount(getMaxCountPerStack());
        }
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return true;
    }

    @Override
    public void clear() {
        inventory.clear();
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, inventory);
        nbt.putBoolean("whitelistMode", whitelistMode);
        NbtCompound filterNbt = new NbtCompound();
        filterItem.writeNbt(filterNbt);
        nbt.put("filterItem", filterNbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt, inventory);
        whitelistMode = nbt.getBoolean("whitelistMode");
        filterItem = ItemStack.fromNbt(nbt.getCompound("filterItem"));
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable("block.redstone_plus.filtered_hopper");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, net.minecraft.entity.player.PlayerInventory playerInventory, PlayerEntity player) {
        return null; // Would need a custom screen handler
    }
}
