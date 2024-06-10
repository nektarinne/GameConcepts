package com.nektarinne.common;

import java.util.Comparator;
import java.util.Objects;

/**
 * Represents an Item.
 */
public class Item implements Comparable<Item> {

    static final int DEFAULT_STACK_SIZE = 64;
    private static final Comparator<Item> COMPARATOR = Comparator.comparing(Item::category).thenComparing(Item::name);
    private final String name;
    private final Category category;
    /**
     * Defaults to {@link #DEFAULT_STACK_SIZE}.
     */
    private final int stackSize;

    private Item(Builder builder) {
        this.name = Objects.requireNonNull(builder.name, "Unable to create an item using " + builder + ": the name cannot be null");
        this.category = Objects.requireNonNull(builder.category, "Unable to create an item using " + builder + ": the category cannot be null");
        this.stackSize = builder.stackSize;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static int compare(Item o1, Item o2) {
        return o1 == null ? 1 : o1.compareTo(o2);
    }

    public String name() {
        return name;
    }

    public Category category() {
        return category;
    }

    public int stackSize() {
        return stackSize;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return stackSize == item.stackSize && Objects.equals(name, item.name) && category == item.category;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, category, stackSize);
    }

    @Override
    public int compareTo(Item other) {
        if (other == null) {
            return -1;
        }
        return COMPARATOR.compare(this, other);
    }

    @Override
    public String toString() {
        return "%s{name='%s', category=%s, stackSize=%d}"
                .formatted(getClass().getSimpleName(), name, category, stackSize);
    }

    public enum Category {
        // values need to be declared in the sorting order
        EQUIPMENT,
        CONSUMABLE,
    }

    public static class Builder {
        private String name;
        private Category category;
        private int stackSize = DEFAULT_STACK_SIZE;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder category(Category category) {
            this.category = category;
            return this;
        }

        public Builder stackSize(int stackSize) {
            this.stackSize = stackSize;
            return this;
        }

        public Item build() {
            return new Item(this);
        }

        @Override
        public String toString() {
            return "%s{name='%s', category=%s, stackSize=%s}"
                    .formatted(getClass().getSimpleName(), name, category, stackSize);
        }
    }
}
