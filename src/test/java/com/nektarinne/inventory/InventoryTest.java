package com.nektarinne.inventory;

import com.nektarinne.common.Item;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatException;

class InventoryTest {

    private final Item oakLog = Item.builder()
            .name("oakLog")
            .category(Item.Category.EQUIPMENT)
            .build();
    private final Item oakPlank = Item.builder()
            .name("oakPlank")
            .category(Item.Category.EQUIPMENT)
            .build();
    private Inventory underTest;

    @Test
    void sort() {
        underTest = Inventory.builder().withNbOfSlots(3).build();
        underTest.add(0, ItemStack.builder(oakLog).quantity(5).build());
        underTest.add(2, ItemStack.builder(oakPlank).quantity(4).build());
        Slot slot0 = underTest.getSlot(0);
        Slot slot1 = underTest.getSlot(1);
        Slot slot2 = underTest.getSlot(2);
        underTest.sort();
        assertThat(underTest.getSlot(0)).isEqualTo(slot0);
        assertThat(underTest.getSlot(1)).isEqualTo(slot2);
        assertThat(underTest.getSlot(2)).isEqualTo(slot1);
    }

    @Test
    void add_newStackBecauseDifferentCategory() {
        underTest = Inventory.builder().withNbOfSlots(3).build();
        ItemStack result1 = underTest.add(ItemStack.builder(oakLog).quantity(5).build());
        ItemStack result2 = underTest.add(ItemStack.builder(oakPlank).quantity(4).build());
        assertThat(result1).isNull();
        assertThat(result2).isNull();

        assertThat(underTest.getItemStack(0)).isNotNull();
        assertThat(underTest.getItemStack(1)).isNotNull();
        assertThat(underTest.getItemStack(2)).isNull();

        assertThat(underTest.getItemStack(0).item()).isEqualTo(oakLog);
        assertThat(underTest.getItemStack(1).item()).isEqualTo(oakPlank);

        assertThat(underTest.getItemStack(0).quantity()).isEqualTo(5);
        assertThat(underTest.getItemStack(1).quantity()).isEqualTo(4);
    }

    @Test
    void add_specificIndex() {
        underTest = Inventory.builder().withNbOfSlots(3).build();
        ItemStack result1 = underTest.add(0, ItemStack.builder(oakLog).quantity(5).build());
        ItemStack result2 = underTest.add(2, ItemStack.builder(oakLog).quantity(4).build());
        assertThat(result1).isNull();
        assertThat(result2).isNull();

        assertThat(underTest.getItemStack(0)).isNotNull();
        assertThat(underTest.getItemStack(1)).isNull();
        assertThat(underTest.getItemStack(2)).isNotNull();

        assertThat(underTest.getItemStack(0).item()).isEqualTo(oakLog);
        assertThat(underTest.getItemStack(2).item()).isEqualTo(oakLog);

        assertThat(underTest.getItemStack(0).quantity()).isEqualTo(5);
        assertThat(underTest.getItemStack(2).quantity()).isEqualTo(4);
    }

    @Test
    void add_InventoryFull() {
        underTest = Inventory.builder().withNbOfSlots(1).build();
        ItemStack result1 = underTest.add(ItemStack.builder(oakLog).quantity(5).build());
        ItemStack result2 = underTest.add(ItemStack.builder(oakPlank).quantity(4).build());
        assertThat(result1).isNull();
        assertThat(result2).isNotNull();
    }

    @Test
    void remove() {
        underTest = Inventory.builder().withNbOfSlots(3).build();
        assertThat(underTest.remove(0)).isNull();
        assertThatException().isThrownBy(() -> underTest.remove(-1));
        assertThatException().isThrownBy(() -> underTest.remove(4));
        ItemStack itemStack1 = ItemStack.builder(oakLog).quantity(5).build();
        ItemStack itemStack2 = ItemStack.builder(oakPlank).quantity(4).build();
        ItemStack result1 = underTest.add(itemStack1);
        ItemStack result2 = underTest.add(itemStack2);
        assertThat(result1).isNull();
        assertThat(result2).isNull();
        assertThat(underTest.getItemStack(0)).isEqualTo(itemStack1);
        assertThat(underTest.getItemStack(1)).isEqualTo(itemStack2);

        assertThat(underTest.remove(0)).isEqualTo(itemStack1);

        assertThat(underTest.getItemStack(0)).isNull();
        assertThat(underTest.getItemStack(1)).isEqualTo(itemStack2);
    }
}