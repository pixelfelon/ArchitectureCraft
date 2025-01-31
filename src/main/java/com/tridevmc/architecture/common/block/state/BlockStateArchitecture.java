package com.tridevmc.architecture.common.block.state;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.MapCodec;
import com.tridevmc.architecture.common.block.BlockArchitecture;
import com.tridevmc.architecture.common.helpers.Trans3;
import com.tridevmc.architecture.common.helpers.Vector3;
import com.tridevmc.architecture.common.render.ModelSpec;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderSet;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

import java.util.function.Predicate;


@MethodsReturnNonnullByDefault
public class BlockStateArchitecture extends BlockState {
    public BlockStateArchitecture(BlockArchitecture block, ImmutableMap<Property<?>, Comparable<?>> propertyComparableImmutableMap, MapCodec<BlockState> blockStateMapCodec) {
        super(block, propertyComparableImmutableMap, blockStateMapCodec);
    }

    @Override
    public BlockArchitecture getBlock() {
        return (BlockArchitecture) super.getBlock();
    }

    @Override
    public boolean hasBlockEntity() {
        return this.getBlock().hasBlockEntity(this);
    }

    @Override
    public boolean is(TagKey<Block> tag) {
        return this.getBlock().is(this, tag);
    }

    @Override
    public boolean is(TagKey<Block> tag, Predicate<BlockBehaviour.BlockStateBase> predicate) {
        return this.getBlock().is(this, tag, predicate);
    }

    @Override
    public boolean is(HolderSet<Block> holderSet) {
        return this.getBlock().is(this, holderSet);
    }

    public BlockArchitecture.IOrientationHandler getOrientationHandler() {
        return getBlock().getOrientationHandler();
    }

    public RenderShape getRenderShape() {
        return getBlock().getRenderShape(this);
    }

    public ModelSpec getModelSpec() {
        return getBlock().getModelSpec(this);
    }

    public Trans3 localToGlobalRotation(BlockAndTintGetter level, BlockPos pos) {
        return getBlock().localToGlobalRotation(level, pos, this);
    }

    public Trans3 localToGlobalTransformation(BlockGetter level, BlockPos pos) {
        return getBlock().localToGlobalTransformation(level, pos, this);
    }

    public Trans3 localToGlobalTransformation(BlockGetter level, BlockPos pos, Vector3 origin) {
        return getBlock().localToGlobalTransformation(level, pos, this, origin);
    }

    public BlockState getParticleState(BlockAndTintGetter level, BlockPos pos) {
        return getBlock().getParticleState(level, pos);
    }
}
