package com.nektarinne.inventory;

public class EmptyItemStackException extends RuntimeException {

    private EmptyItemStackException(String message) {
        super(message);
    }

    private EmptyItemStackException(String message, Throwable cause) {
        super(message, cause);
    }

    public static EmptyItemStackException create(ItemStack itemStack) {
        return new EmptyItemStackException("An %s can't have a quantity of 0: %s"
                .formatted(itemStack.getClass().getSimpleName(), itemStack));
    }
}
