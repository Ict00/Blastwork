package com.ist.blastwork.item.OverheatedExplosiveShell;

import com.ist.blastwork.Blastwork;
import com.ist.blastwork.entity.ModEntityTypes;
import com.ist.blastwork.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

import java.util.Random;


public class OverheatedExplosiveShellThrown extends ThrowableItemProjectile {
    public OverheatedExplosiveShellThrown(EntityType<? extends ThrowableItemProjectile> type, LivingEntity shooter, Level level) {
        super(type, shooter, level);
    }

    public OverheatedExplosiveShellThrown(LivingEntity shooter, Level level) {
        super(ModEntityTypes.OVERHEATED_EXPLOSIVE_SHELL_ET.get(), shooter, level);
    }
    public OverheatedExplosiveShellThrown(EntityType<? extends OverheatedExplosiveShellThrown> entityType, Level level) {
        super(entityType, level);
    }

    private void Explode(float rad) {
        if (level().isClientSide) return;

        var level = level();

        BlockPos center = new BlockPos((int) position().x, (int) position().y, (int) position().z);
        BlockPos min = center.offset((int) -rad, (int) -rad, (int) -rad);
        BlockPos max = center.offset((int) rad, (int) rad, (int) rad);
        RandomSource random1 = level.random;

        for (BlockPos cur : BlockPos.betweenClosed(min, max)) {
            if (center.distSqr(cur) <= rad * rad) {
                if (level.getBlockState(cur).is(BlockTags.AIR)) continue;
                if (level.getBlockState(cur).is(BlockTags.DRAGON_IMMUNE)) continue;
                if (level.getBlockState(cur).getBlock() instanceof LiquidBlock) continue;
                if (level.getBlockState(cur).is(BlockTags.WITHER_IMMUNE)) continue;

                Block block = level.getBlockState(cur).getBlock();
                ItemStack stack = new ItemStack(block.asItem());

                var recipe = level.getRecipeManager().getRecipeFor(RecipeType.SMELTING, new SingleRecipeInput(stack), level);
                if (recipe.isPresent()) {
                    if (random1.nextBoolean()) continue;
                    SmeltingRecipe smeltingRecipe = recipe.get().value();
                    var res = smeltingRecipe.getResultItem(null);

                    if (res.getItem() instanceof BlockItem blockItem) {
                        var defaultBlockState = blockItem.getBlock().defaultBlockState();

                        FallingBlockEntity falling = FallingBlockEntity.fall(level, cur, defaultBlockState);
                        level.addFreshEntity(falling);
                    }
                }
                else {
                    if (random1.nextBoolean()) continue;
                    if (random1.nextBoolean()) continue;
                    FallingBlockEntity fallingBlock = FallingBlockEntity.fall(level, cur, level.getBlockState(cur));
                    level.addFreshEntity(fallingBlock);
                }
            }
        }

        level().explode(
                null,
                Explosion.getDefaultDamageSource(level(), getOwner()),
                null,
                getBlockX(),
                getBlockY(),
                getBlockZ(),
                rad,
                false,
                Level.ExplosionInteraction.TNT);
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);

        Explode(3);

        this.discard();
    }

    @Override
    protected @NotNull Item getDefaultItem() {
        return ModItems.OVERHEATED_EXPLOSIVE_SHELL.get();
    }
}