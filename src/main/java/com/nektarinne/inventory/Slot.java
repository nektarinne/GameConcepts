package com.nektarinne.inventory;

import com.nektarinne.common.Item;

public class Slot {


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
        return this.item == null
                && (
                this.itemStack == null
                        || this.itemStack.item() == null // should not happen
                        || this.itemStack.quantity() == 0
        );
    }

    @Override
    public String toString() {
        return "%s{item=%s, itemStack=%s}"
                .formatted(getClass().getSimpleName(), item, itemStack);
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
