package com.nektarinne.inventory;

import com.nektarinne.common.Item;

public class UnableToStackTwoDifferentItems extends RuntimeException {

    private UnableToStackTwoDifferentItems(String message) {
        super(message);
    }

    private UnableToStackTwoDifferentItems(String message, Throwable cause) {
        super(message, cause);
    }

    public static UnableToStackTwoDifferentItems create(Item item1, Item item2) {
        return new UnableToStackTwoDifferentItems("Unable to stack %s on %s".formatted(item2, item1));
    }
}
