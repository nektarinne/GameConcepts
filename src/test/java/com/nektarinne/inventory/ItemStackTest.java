package com.nektarinne.inventory;

import com.nektarinne.common.Item;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class ItemStackTest {

    private static final Item ITEM_1 = Item.builder().name("item 1").category(Item.Category.EQUIPMENT).build();

    @Test
    void itemStack_invalidItem() {
        assertThatNullPointerException().isThrownBy(() -> ItemStack.builder(null).quantity(1).build());
    }

    @Test
    void itemStack_invalidQuantity() {
        assertThatNullPointerException().isThrownBy(() -> ItemStack.builder(ITEM_1).build());

        assertThatIllegalArgumentException().isThrownBy(() -> ItemStack.builder(ITEM_1).quantity(0).build());
        assertThatIllegalArgumentException().isThrownBy(() -> ItemStack.builder(ITEM_1).quantity(-1).build());
        assertThatIllegalArgumentException().isThrownBy(() -> ItemStack.builder(ITEM_1).quantity(ITEM_1.stackSize() + 1).build());
    }

    @Test
    void isMaxSize() {
        assertThat(ItemStack.builder(ITEM_1).quantity(1).build().isMaxSize()).isFalse();
        assertThat(ItemStack.builder(ITEM_1).quantity(ITEM_1.stackSize()).build().isMaxSize()).isTrue();
        assertThat(ItemStack.builder(ITEM_1).quantity(ITEM_1.stackSize() - 1).build().isMaxSize()).isFalse();
    }

    @Test
    void add() {
        ItemStack underTest = ItemStack.builder(ITEM_1).quantity(1).build();
        ItemStack other = ItemStack.builder(ITEM_1).quantity(1).build();

        ItemStack result = underTest.add(other);

        // underTest's quantity is the sum of the two. Result is null because other has been fully absorbed.
        assertThat(result).isNull();
        assertThat(underTest.quantity()).isEqualTo(2);
    }

    @Test
    void add_alreadyAtStackSize() {
        ItemStack underTest = ItemStack.builder(ITEM_1).quantity(ITEM_1.stackSize()).build();
        ItemStack other = ItemStack.builder(ITEM_1).quantity(1).build();

        ItemStack result = underTest.add(other);

        // Nothing have changed
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(other);
        assertThat(underTest.quantity()).isEqualTo(ITEM_1.stackSize());
    }

    @Test
    void add_sumIsStackSize() {
        ItemStack underTest = ItemStack.builder(ITEM_1).quantity(ITEM_1.stackSize() - 3).build();
        ItemStack other = ItemStack.builder(ITEM_1).quantity(3).build();

        ItemStack result = underTest.add(other);

        // underTest's quantity is the sum of the two. Result is null because other has been fully absorbed.
        assertThat(result).isNull();
        assertThat(underTest.quantity()).isEqualTo(ITEM_1.stackSize());
    }

    @Test
    void add_sumIsHigherThanStackSize() {
        int missingQuantityForStackSize = 3;
        ItemStack underTest = ItemStack.builder(ITEM_1).quantity(ITEM_1.stackSize() - missingQuantityForStackSize).build();
        ItemStack other = ItemStack.builder(ITEM_1).quantity(missingQuantityForStackSize + 2).build();

        ItemStack result = underTest.add(other);

        // Result is the diff of ITEM_1.stackSize() - other.quantity
        assertThat(result).isNotNull();
        assertThat(result.quantity()).isEqualTo(other.quantity() - missingQuantityForStackSize);
        assertThat(underTest.quantity()).isEqualTo(ITEM_1.stackSize());
    }

    @Test
    void repr() {
        ItemStack underTest;
        underTest = ItemStack.builder(ITEM_1).quantity(1).build();
        assertThat(underTest.repr()).isEqualTo(1 + "x" + ITEM_1);

        underTest = ItemStack.builder(ITEM_1).quantity(ITEM_1.stackSize()).build();
        assertThat(underTest.repr()).isEqualTo(ITEM_1.stackSize() + "x" + ITEM_1);
    }

    @Test
    void compare() {
        ItemStack itemStack = ItemStack.builder(ITEM_1).quantity(1).build();
        assertThat(ItemStack.compare(itemStack, itemStack)).isZero();
        assertThat(ItemStack.compare(null, itemStack)).isEqualTo(1);
        assertThat(ItemStack.compare(itemStack, null)).isEqualTo(-1);
    }

    @Test
    void toStringTest() {
        // For coverage completion
        String result = ItemStack.builder(ITEM_1).toString();
        assertThat(result).isNotBlank();
        assertThat(result).doesNotContain("@");
    }

    @Test
    void builder_toString() {
        // For coverage completion
        String result = ItemStack.builder(ITEM_1).quantity(1).build().toString();
        assertThat(result).isNotBlank();
        assertThat(result).doesNotContain("@");

    }
}