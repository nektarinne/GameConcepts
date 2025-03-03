package com.nektarinne.inventory;

import com.nektarinne.common.Item;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatException;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
    void constructor_invalidBuilder() {
        assertThatIllegalArgumentException().isThrownBy(() ->
                Inventory.builder()
                        .withNbOfSlots(-1)
                        .build());
        assertThatIllegalArgumentException().isThrownBy(() ->
                Inventory.builder()
                        .withNbOfSlots(0)
                        .withSlots(List.of(Slot.builder().build()))
                        .build());
    }

    @Test
    void constructor_copySlots() {
        Slot slot = Slot.builder().item(oakLog).build();
        underTest = Inventory.builder()
                .withNbOfSlots(2)
                .withSlots(List.of(slot))
                .build();
        assertThat(underTest.getSlot(0)).isEqualTo(slot);
        assertThat(underTest.getSlot(0).isEmpty()).isFalse();
        assertThat(underTest.getSlot(1).isEmpty()).isTrue();
    }

    @Test
    void getSlot() {
        underTest = Inventory.builder().withNbOfSlots(1).build();
        assertThatNoException().isThrownBy(() -> underTest.getSlot(0));
        assertThatIllegalArgumentException().isThrownBy(() -> underTest.getSlot(-1));
        assertThatIllegalArgumentException().isThrownBy(() -> underTest.getSlot(1));
    }

    @Test
    void getItem() {
        underTest = Inventory.builder().withNbOfSlots(1).build();
        assertThat(underTest.getItem(0)).isNull();
    }

    @Test
    void lock() {
        ItemStack itemStack = ItemStack.builder(oakPlank).quantity(5).build();
        underTest = Inventory.builder().withNbOfSlots(1).build();
        underTest.add(0, ItemStack.builder(oakLog).quantity(5).build());

        underTest.lock(0);
        underTest.remove(0);
        assertThat(underTest.getItem(0)).isEqualTo(oakLog);
        assertThat(underTest.getItemStack(0)).isNull();

        ItemStack result = underTest.add(0, itemStack);
        assertThat(itemStack).isEqualTo(result);
    }

    @Test
    void lock_empty() {
        underTest = Inventory.builder().withNbOfSlots(1).build();
        assertThatIllegalArgumentException().isThrownBy(() -> underTest.lock(0));
    }

    @Test
    void unlock() {
        ItemStack itemStack = ItemStack.builder(oakPlank).quantity(5).build();
        underTest = Inventory.builder().withNbOfSlots(1).build();
        underTest.add(0, ItemStack.builder(oakLog).quantity(5).build());
        underTest.lock(0);
        underTest.remove(0);
        ItemStack result = underTest.add(0, itemStack);
        assertThat(itemStack).isEqualTo(result);

        underTest.unlock(0);

        result = underTest.add(0, itemStack);
        assertThat(result).isNull();
    }

    @Test
    void unlock_empty() {
        underTest = Inventory.builder().withNbOfSlots(1).build();
        assertThatIllegalArgumentException().isThrownBy(() -> underTest.unlock(0));
    }

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
    void add_inventoryFull() {
        underTest = Inventory.builder().withNbOfSlots(1).build();
        ItemStack result1 = underTest.add(ItemStack.builder(oakLog).quantity(5).build());
        ItemStack result2 = underTest.add(ItemStack.builder(oakPlank).quantity(4).build());
        assertThat(result1).isNull();
        assertThat(result2).isNotNull();
    }

    @Test
    void add_invalidQuantity() {
        underTest = Inventory.builder().withNbOfSlots(1).build();
        // Cannot create invalid item stack so mock it
        ItemStack itemStack = mock(ItemStack.class);
        when(itemStack.quantity()).thenReturn(0);
        assertThatExceptionOfType(EmptyItemStackException.class)
                .isThrownBy(() -> underTest.add(itemStack));
    }

    @Test
    void add_oneSlotLockedWithThisItem() {
        underTest = Inventory.builder()
                .withNbOfSlots(1)
                .withSlots(List.of(Slot.builder().item(oakPlank).build()))
                .build();
        ItemStack result1 = underTest.add(ItemStack.builder(oakPlank).quantity(5).build());
        assertThat(result1).isNull();
    }

    @Test
    void add_oneSlotWithThisItem() {
        underTest = Inventory.builder()
                .withNbOfSlots(1)
                .withSlots(List.of(Slot.builder().itemStack(ItemStack.builder(oakPlank).quantity(5).build()).build()))
                .build();
        ItemStack result1 = underTest.add(ItemStack.builder(oakPlank).quantity(5).build());
        assertThat(result1).isNull();
    }

    @Test
    void add_oneSlotWithThisItem_full() {
        // Item with stackSize of 6
        Item item = Item.builder().name("test").category(Item.Category.EQUIPMENT).stackSize(6).build();
        // Quantity of 5
        underTest = Inventory.builder()
                .withNbOfSlots(1)
                .withSlots(List.of(Slot.builder()
                        .itemStack(ItemStack.builder(item)
                                .quantity(5)
                                .build())
                        .build()))
                .build();
        // Add another quantity of 5
        ItemStack result1 = underTest.add(ItemStack.builder(item).quantity(5).build());

        // 4 cannot be added
        assertThat(result1.item()).isEqualTo(item);
        assertThat(result1.quantity()).isEqualTo(4);
        assertThat(underTest.getSlot(0).itemStack().isMaxSize()).isTrue();
    }

    @Test
    void add_withSlot_invalidSlot() {
        underTest = Inventory.builder().withNbOfSlots(1).build();
        ItemStack itemStack = ItemStack.builder(oakLog).quantity(5).build();
        assertThatIllegalArgumentException()
                .isThrownBy(() -> underTest.add(-1, itemStack));
        assertThatIllegalArgumentException()
                .isThrownBy(() -> underTest.add(1, itemStack));
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

    @Test
    void toStringTest() {
        // For coverage completion
        assertThat(Inventory.builder().withNbOfSlots(1).toString()).isNotBlank();
    }
}