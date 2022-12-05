/*
 * MIT License
 *
 * Copyright (c) 2017 Benjamin K
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.elytradev.architecture.common.helpers;

import com.elytradev.architecture.common.block.BlockShape;
import com.elytradev.architecture.common.tile.TileShape;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import static java.lang.Math.*;

public class Utils {

    public static Random random = new Random();

    public static int playerTurn(EntityLivingBase player) {
        return MathHelper.floor((player.rotationYaw * 4.0 / 360.0) + 0.5) & 3;
    }

    public static int lookTurn(Vector3 look) {
        double a = atan2(look.x, look.z);
        return (int) round(a * 2 / PI) & 3;
    }

    public static boolean playerIsInCreativeMode(EntityPlayer player) {
        return player != null && player.capabilities != null && player.capabilities.isCreativeMode;
    }

    @SideOnly(Side.CLIENT)
    public static TextureAtlasSprite getSpriteForBlockState(IBlockState state) {
        Block block = state.getBlock();
        @Nullable ResourceLocation blockResource = block.getRegistryName();

        if (blockResource != null && blockResource.getNamespace().equals("chisel")){
            // Attempt some trickery to force a 16x16 sprite.
            // Chisel's only larger sprites are used for randomization AFAIK, not intended to be rendered at that res.
            ItemStack blockStack = new ItemStack(block, 1, block.damageDropped(state));
            TextureAtlasSprite base = Minecraft.getMinecraft().getRenderItem()
                    .getItemModelWithOverrides(blockStack, null, null).getParticleTexture();
            if (base.getIconWidth() == 16 || base.getIconHeight() == 16)
                // No need!
                return base;

            // Just grab the top left 16x16 pixels.
            // TODO: Would love to randomize which sub-sprite we grab, but this API won't accommodate it.
            FakeAtlasSprite out = new FakeAtlasSprite(base);
            out.adjustIconSize(16, 16);
            return out;
        }

        else if (!Objects.equals(block, Blocks.AIR))
            return Minecraft.getMinecraft().getBlockRendererDispatcher()
                    .getBlockModelShapes().getTexture(state);
        else
            return null;
    }

    @SideOnly(Side.CLIENT)
    public static int getColourFromState(IBlockState state) {
        if (state == null)
            return -1;

        BlockColors blockColors = Minecraft.getMinecraft().getBlockColors();
        return blockColors.colorMultiplier(state, null, null, 0);
    }

    public static TextureAtlasSprite getSpriteForPos(IBlockAccess world, BlockPos pos, boolean renderPrimary) {
        IBlockState blockState = world.getBlockState(pos);

        if (blockState == null)
            return null;

        if (blockState.getBlock() instanceof BlockShape) {
            TileShape shape = TileShape.get(world, pos);

            if (renderPrimary) {
                return getSpriteForBlockState(shape.getBaseBlockState());
            } else {
                return getSpriteForBlockState(shape.getSecondaryBlockState());
            }
        } else if (!renderPrimary) {
            return null;
        }

        return getSpriteForBlockState(blockState);
    }

    public static String displayNameOfBlock(Block block, int meta) {
        String name = null;
        Item item = Item.getItemFromBlock(block);
        if (item != null) {
            ItemStack stack = new ItemStack(item, 1, meta);
            name = stack.getDisplayName();
        }
        if (name == null)
            name = block.getLocalizedName();
        return I18n.format("tooltip.architecturecraft.cutfrom", name);
    }

    public static String displayNameOnlyOfBlock(Block block, int meta) {
        String name = null;
        Item item = Item.getItemFromBlock(block);
        if (item != null) {
            ItemStack stack = new ItemStack(item, 1, meta);
            name = stack.getDisplayName();
        }
        if (name == null)
            name = block.getLocalizedName();
        return name;
    }

    public static AxisAlignedBB unionOfBoxes(List<AxisAlignedBB> list) {
        AxisAlignedBB box = list.get(0);
        int n = list.size();
        for (int i = 1; i < n; i++)
            box = box.union(list.get(i));
        return box;
    }
}
