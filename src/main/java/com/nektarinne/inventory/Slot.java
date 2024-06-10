package com.nektarinne.inventory;

import com.nektarinne.common.Item;

public class Slot implements Comparable<Slot> {


    private Item item;
    private ItemStack itemStack;

    public Slot(Builder builder) {
        this.item = builder.item;
        this.itemStack = builder.itemStack;
    }

    public static Slot empty() {
        return new Builder().build();
    }

    static Builder builder() {
        return new Builder();
    }

    public Item item() {
        return item;
    }

    public Slot item(Item item) {
        this.item = item;
        return this;
    }

    public ItemStack itemStack() {
        return itemStack;
    }

    public Slot itemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
        return this;
    }

    public boolean isEmpty() {
        return this.item == null && this.itemStack == null;
    }

    @Override
    public String toString() {
        return "%s{item=%s, itemStack=%s}"
                .formatted(getClass().getSimpleName(), item, itemStack);
    }

    @Override
    public int compareTo(Slot o) {
        if (o == null) {
            return -1;
        }
        int result = Boolean.compare(this.isEmpty(), o.isEmpty());
        if (result != 0) {
            return result;
        }
        result = Item.compare(this.item, o.item);
        if (result != 0) {
            return result;
        }
        return ItemStack.compare(this.itemStack, o.itemStack);
    }

    public static class Builder {
        private Item item;
        private ItemStack itemStack;

        Builder item(Item item) {
            this.item = item;
            return this;
        }

        Builder itemStack(ItemStack itemStack) {
            this.itemStack = itemStack;
            return this;
        }

        public Slot build() {
            return new Slot(this);
        }

        @Override
        public String toString() {
            return "%s{item=%s, itemStack=%s}"
                    .formatted(getClass().getSimpleName(), item, itemStack);
        }
    }

}
