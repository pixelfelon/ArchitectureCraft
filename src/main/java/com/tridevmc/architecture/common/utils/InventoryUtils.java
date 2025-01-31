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

package com.tridevmc.architecture.common.utils;

import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.item.ItemStack;

public class InventoryUtils {

    public static InventorySide inventorySide(Container base, Direction side) {
        if (base instanceof WorldlyContainer)
            return new SidedInventorySide((WorldlyContainer) base, side);
        else
            return new UnsidedInventorySide(base);
    }

    public static abstract class InventorySide {
        public int size;

        public abstract ItemStack get(int slot);

        public abstract boolean set(int slot, ItemStack stack);

        public abstract ItemStack extract(int slot);
    }

    public static class UnsidedInventorySide extends InventorySide {

        Container base;

        public UnsidedInventorySide(Container base) {
            this.base = base;
            this.size = base.getContainerSize();
        }

        @Override
        public ItemStack get(int slot) {
            return this.base.getItem(slot);
        }

        @Override
        public boolean set(int slot, ItemStack stack) {
            this.base.setItem(slot, stack);
            return true;
        }

        @Override
        public ItemStack extract(int slot) {
            return this.get(slot);
        }

    }

    public static class SidedInventorySide extends InventorySide {

        WorldlyContainer base;
        Direction side;
        int[] slots;

        public SidedInventorySide(WorldlyContainer base, Direction side) {
            this.base = base;
            this.side = side;
            this.slots = base.getSlotsForFace(side);
            this.size = this.slots.length;
        }

        @Override
        public ItemStack get(int i) {
            return this.base.getItem(this.slots[i]);
        }

        @Override
        public boolean set(int i, ItemStack stack) {
            int slot = this.slots[i];
            if (this.base.canPlaceItemThroughFace(slot, stack, this.side)) {
                this.base.setItem(slot, stack);
                return true;
            } else
                return false;
        }

        @Override
        public ItemStack extract(int i) {
            int slot = this.slots[i];
            ItemStack stack = this.base.getItem(slot);
            if (this.base.canTakeItemThroughFace(slot, stack, this.side))
                return stack;
            else
                return ItemStack.EMPTY;
        }

    }

}
