package com.nektarinne.common;

import java.util.Comparator;

/**
 * Represents an Item.
 */
public class Item implements Comparable<Item> {

    private static final Comparator<Item> COMPARATOR = Comparator.comparing(Item::id);
    /**
     * Item's id. Should be unique.
     */
    private final String id;
    /**
     * Item's name.
     */
    private final String name;

    public Item(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String id() {
        return id;
    }

    public String name() {
        return name;
    }

    @Override
    public int compareTo(Item other) {
        return COMPARATOR.compare(this, other);
    }

    @Override
    public String toString() {
        return "%s{id='%s', name='%s'}"
                .formatted(getClass().getSimpleName(), id, name);
    }
}
