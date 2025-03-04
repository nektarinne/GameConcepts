package com.nektarinne.inventory;

import com.nektarinne.common.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class Inventory {

    private static final int DEFAULT_NB_OF_SLOTS = 27;
    private final int nbOfSlots;
    private final List<Slot> slots;

    private Inventory(Builder builder) {
        if (builder.nbOfSlots < 0) {
            throw new IllegalArgumentException("nbOfSlots '%d' is invalid"
                    .formatted(builder.nbOfSlots));
        }
        if (builder.nbOfSlots < builder.slots.size()) {
            throw new IllegalArgumentException("nbOfSlots '%d' is less than slots size '%d'"
                    .formatted(builder.nbOfSlots, builder.slots.size()));
        }
        this.slots = new ArrayList<>(builder.slots);
        // Ensure the list has the correct number of slots by adding empty slots if needed.
        while (this.slots.size() < builder.nbOfSlots) {
            this.slots.add(Slot.empty());
        }
        this.nbOfSlots = builder.nbOfSlots;
    }

    static Builder builder() {
        return new Builder();
    }

    public Slot getSlot(int slotIndex) {
        if (slotIndex < 0 || slotIndex >= nbOfSlots) {
            throw new IllegalArgumentException("SlotIndex '" + slotIndex + "' is outside [0, " + (nbOfSlots - 1) + "]");
        }
        return slots.get(slotIndex);
    }

    public void sort() {
        this.slots.sort(null);  // assuming Slot implements Comparable
    }

    public ItemStack getItemStack(int slotIndex) {
        return this.getSlot(slotIndex).itemStack();
    }

    public Item getItem(int slotIndex) {
        return this.getSlot(slotIndex).item();
    }

    public void lock(int slotIndex) {
        Slot slot = this.getSlot(slotIndex);
        if (slot.isEmpty()) {
            throw new IllegalArgumentException("Unable to lock an empty slot");
        }
        slot.item(slot.itemStack().item());
    }

    public void unlock(int slotIndex) {
        Slot slot = this.getSlot(slotIndex);
        if (slot.isEmpty()) {
            throw new IllegalArgumentException("Unable to unlock an empty slot");
        }
        slot.item(null);
    }

    public ItemStack add(ItemStack itemStack) {
        Objects.requireNonNull(itemStack);
        if (itemStack.quantity() == 0) {
            throw EmptyItemStackException.create(itemStack);
        }
        List<Slot> candidates = slots.stream()
                .filter(slot -> !slot.isEmpty())
                .filter(slot -> Objects.equals(slot.item(), itemStack.item()) || Objects.equals(slot.itemStack().item(), itemStack.item()))
                .toList();
        ItemStack result = ItemStack.from(itemStack);
        for (Slot slot : candidates) {
            if (slot.itemStack() == null) {
                slot.itemStack(ItemStack.from(result));
                return null;
            }
            result = slot.itemStack().add(result);
            if (result == null) {
                return null;
            }
        }
        Optional<Slot> emptySlot = slots.stream()
                .filter(Slot::isEmpty)
                .findFirst();
        if (emptySlot.isPresent()) {
            emptySlot.get().itemStack(ItemStack.from(result));
            return null;
        }
        return result;
    }

    public ItemStack add(int slotIndex, ItemStack itemStack) {
        if (slotIndex < 0 || slotIndex >= nbOfSlots) {
            throw new IllegalArgumentException("SlotIndex '%d' is outside [0, %d]".formatted(slotIndex, nbOfSlots - 1));
        }
        Slot dst = slots.get(slotIndex);
        if (dst.item() == null || Objects.equals(dst.item(), itemStack.item())) {
            if (dst.itemStack() == null) {
                dst.itemStack(ItemStack.from(itemStack));
                return null;
            }
            if (!Objects.equals(dst.itemStack().item(), itemStack.item())) {
                throw new IllegalStateException("Unable to add " + itemStack + " to " + dst);
            }
            return dst.itemStack().add(itemStack);
        }
        return itemStack;
    }

    public ItemStack remove(int slotIndex) {
        if (slotIndex < 0 || slotIndex >= nbOfSlots) {
            throw new IllegalArgumentException("SlotIndex '%d' is outside [0, %d]".formatted(slotIndex, nbOfSlots - 1));
        }
        if (slots.get(slotIndex).isEmpty()) {
            return null;
        }
        ItemStack itemStack = slots.get(slotIndex).itemStack();
        slots.get(slotIndex).itemStack(null);
        return itemStack;
    }

    public static class Builder {
        private List<Slot> slots;
        private int nbOfSlots;

        private Builder() {
            this.slots = List.of();
            this.nbOfSlots = DEFAULT_NB_OF_SLOTS;
        }

        Builder withSlots(List<Slot> slots) {
            this.slots = slots;
            return this;
        }

        Builder withNbOfSlots(int nbOfSlots) {
            this.nbOfSlots = nbOfSlots;
            return this;
        }

        public Inventory build() {
            return new Inventory(this);
        }

        @Override
        public String toString() {
            return "%s{nbOfSlots=%d, slots=%s}"
                    .formatted(getClass().getSimpleName(), nbOfSlots, slots);
        }
    }
}
