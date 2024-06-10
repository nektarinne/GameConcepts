package com.nektarinne.inventory;

import com.nektarinne.common.Item;

import java.util.Comparator;
import java.util.Objects;

/**
 * Represents a stack of {@link Item}.
 */
public class ItemStack implements Comparable<ItemStack> {

    private static final Comparator<ItemStack> COMPARATOR = createComparator();
    private final Item item;
    private int quantity;

    private ItemStack(Builder builder) {
        this(builder.item, builder.quantity);
    }

    private ItemStack(Item item, Integer quantity) {
        this.item = Objects.requireNonNull(item);
        this.quantity(Objects.requireNonNull(quantity));
    }

    public static Builder builder(Item item) {
        return new Builder(item);
    }

    static ItemStack from(ItemStack other) {
        return new ItemStack(other.item(), other.quantity());
    }

    private static Comparator<ItemStack> createComparator() {
        // reverse compare: larger quantity first
        return Comparator.comparing(ItemStack::item)
                .thenComparing(Comparator.<ItemStack, Integer>comparing(ItemStack::quantity)
                        .reversed());
    }

    public static int compare(ItemStack o1, ItemStack o2) {
        return o1 == null ? 1 : o1.compareTo(o2);
    }

    public Item item() {
        return item;
    }

    public int quantity() {
        return quantity;
    }

    public void quantity(int quantity) {
        if (quantity < 1) {
            throw new IllegalArgumentException("An %s should contain at least 1 item. %d is invalid"
                    .formatted(getClass().getSimpleName(), quantity));
        }
        if (quantity > this.item.stackSize()) {
            throw new IllegalArgumentException("A stack of %s is %d max. %d is invalid"
                    .formatted(item.name(), item.stackSize(), quantity));
        }
        this.quantity = quantity;
    }

    public boolean isMaxSize() {
        return this.quantity == this.item.stackSize();
    }

    /**
     * Merges the given {@link ItemStack} to this one, if possible.
     *
     * @param other the {@link ItemStack} to add.
     * @return Either
     * <li>an error;
     * <li>null if the stack to add has been fully absorbed;
     * <li>an {@link ItemStack} if the stack to add has not been fully absorbed.
     */
    public ItemStack add(ItemStack other) {
        if (!Objects.equals(this.item, other.item)) {
            throw UnableToStackTwoDifferentItems.create(this.item, other.item);
        }
        if (isMaxSize()) {
            return other;
        }
        if (this.quantity + other.quantity <= this.item.stackSize()) {
            this.quantity = this.quantity + other.quantity;
            return null;
        }
        int diff = this.item.stackSize() - this.quantity;
        this.quantity = this.item.stackSize();
        return ItemStack.builder(this.item).quantity(other.quantity - diff).build();
    }

    public String repr() {
        return "%dx%s".formatted(quantity, item);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemStack itemStack = (ItemStack) o;
        return quantity == itemStack.quantity && Objects.equals(item, itemStack.item);
    }

    @Override
    public int hashCode() {
        return Objects.hash(item, quantity);
    }

    @Override
    public int compareTo(ItemStack other) {
        if (other == null) {
            return -1;
        }
        return COMPARATOR.compare(this, other);
    }

    @Override
    public String toString() {
        return "%s{item=%s, quantity=%d}"
                .formatted(getClass().getSimpleName(), item, quantity);
    }

    public static class Builder {
        private final Item item;
        private Integer quantity;

        Builder(Item item) {
            this.item = Objects.requireNonNull(item);
        }

        public Builder quantity(int quantity) {
            this.quantity = quantity;
            return this;
        }

        public ItemStack build() {
            return new ItemStack(this);
        }

        @Override
        public String toString() {
            return "%s{item=%s, quantity=%d}"
                    .formatted(getClass().getSimpleName(), item, quantity);
        }
    }
}
