package com.nektarinne.droptables;

import com.nektarinne.common.Item;
import org.assertj.core.data.Percentage;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class DropTableTest {

    private static final Item ITEM_1 = Item.builder().name("item 1").category(Item.Category.EQUIPMENT).build();
    private static final Item ITEM_2 = Item.builder().name("item 2").category(Item.Category.CONSUMABLE).build();
    private static final double ITEM_1_PROBA = 0.33;
    private static final double ITEM_2_PROBA = 0.05;

    @Test
    void drop() {
        Map<Item, Double> map1 = Map.of(ITEM_1, ITEM_1_PROBA, ITEM_2, ITEM_2_PROBA);
        Map<DropTable, Double> map2 = Map.of();
        Map<String, Integer> stats = new HashMap<>();
        int numberOfRuns = 10_000;

        DropTable underTest = new DropTable(map1, map2);
        for (int i = 0; i < numberOfRuns; i++) {
            compileStats(underTest.drop(), stats);
        }

        assertThat(stats.get("item1_minValue")).isGreaterThanOrEqualTo(0);
        assertThat(stats.get("item2_minValue")).isGreaterThanOrEqualTo(0);

        assertThat(stats.get("item1_maxValue")).isLessThanOrEqualTo(numberOfRuns);
        assertThat(stats.get("item2_maxValue")).isLessThanOrEqualTo(numberOfRuns);

        assertThat(stats.get("item1_sum")).isCloseTo((int) (ITEM_1_PROBA * numberOfRuns), Percentage.withPercentage(5));
        assertThat(stats.get("item2_sum")).isCloseTo((int) (ITEM_2_PROBA * numberOfRuns), Percentage.withPercentage(5));
    }

    @Test
    void drop_10Times() {
        Map<Item, Double> map1 = Map.of(ITEM_1, ITEM_1_PROBA, ITEM_2, ITEM_2_PROBA);
        Map<DropTable, Double> map2 = Map.of();
        Map<String, Integer> stats = new HashMap<>();
        int numberOfRuns = 10_000;
        int dropTimes = 10;
        int expected = numberOfRuns * dropTimes;

        DropTable underTest = new DropTable(map1, map2);
        for (int i = 0; i < numberOfRuns; i++) {
            compileStats(underTest.drop(dropTimes), stats);
        }

        assertThat(stats.get("item1_minValue")).isGreaterThanOrEqualTo(0);
        assertThat(stats.get("item2_minValue")).isGreaterThanOrEqualTo(0);

        assertThat(stats.get("item1_maxValue")).isLessThanOrEqualTo(expected);
        assertThat(stats.get("item2_maxValue")).isLessThanOrEqualTo(expected);

        assertThat(stats.get("item1_sum")).isCloseTo((int) (ITEM_1_PROBA * expected), Percentage.withPercentage(5));
        assertThat(stats.get("item2_sum")).isCloseTo((int) (ITEM_2_PROBA * expected), Percentage.withPercentage(5));
    }

    @Test
    void drop_100Times() {
        Map<Item, Double> map1 = Map.of(ITEM_1, ITEM_1_PROBA, ITEM_2, ITEM_2_PROBA);
        Map<DropTable, Double> map2 = Map.of();
        Map<String, Integer> stats = new HashMap<>();
        int numberOfRuns = 10_000;
        int dropTimes = 100;
        int expected = numberOfRuns * dropTimes;

        DropTable underTest = new DropTable(map1, map2);
        for (int i = 0; i < numberOfRuns; i++) {
            compileStats(underTest.drop(dropTimes), stats);
        }

        assertThat(stats.get("item1_minValue")).isGreaterThanOrEqualTo(0);
        assertThat(stats.get("item2_minValue")).isGreaterThanOrEqualTo(0);

        assertThat(stats.get("item1_maxValue")).isLessThanOrEqualTo(expected);
        assertThat(stats.get("item2_maxValue")).isLessThanOrEqualTo(expected);

        assertThat(stats.get("item1_sum")).isCloseTo((int) (ITEM_1_PROBA * expected), Percentage.withPercentage(5));
        assertThat(stats.get("item2_sum")).isCloseTo((int) (ITEM_2_PROBA * expected), Percentage.withPercentage(5));
    }

    @Test
    void drop_1000Times() {
        Map<Item, Double> map1 = Map.of(ITEM_1, ITEM_1_PROBA, ITEM_2, ITEM_2_PROBA);
        Map<DropTable, Double> map2 = Map.of();
        Map<String, Integer> stats = new HashMap<>();
        int numberOfRuns = 10_000;
        int dropTimes = 1000;
        int expected = numberOfRuns * dropTimes;

        DropTable underTest = new DropTable(map1, map2);
        for (int i = 0; i < numberOfRuns; i++) {
            compileStats(underTest.drop(dropTimes), stats);
        }

        assertThat(stats.get("item1_minValue")).isGreaterThanOrEqualTo(0);
        assertThat(stats.get("item2_minValue")).isGreaterThanOrEqualTo(0);

        assertThat(stats.get("item1_maxValue")).isLessThanOrEqualTo(expected);
        assertThat(stats.get("item2_maxValue")).isLessThanOrEqualTo(expected);

        assertThat(stats.get("item1_sum")).isCloseTo((int) (ITEM_1_PROBA * expected), Percentage.withPercentage(5));
        assertThat(stats.get("item2_sum")).isCloseTo((int) (ITEM_2_PROBA * expected), Percentage.withPercentage(5));
    }

    private void compileStats(Map<Item, Integer> drop, Map<String, Integer> stats) {
        int item1Quantity = drop.getOrDefault(ITEM_1, 0);
        int item2Quantity = drop.getOrDefault(ITEM_2, 0);
        stats.compute("item1_minValue", (k, v) -> (v == null || item1Quantity < v) ? item1Quantity : v);
        stats.compute("item1_maxValue", (k, v) -> (v == null || item1Quantity > v) ? item1Quantity : v);

        stats.compute("item2_minValue", (k, v) -> (v == null || item2Quantity < v) ? item2Quantity : v);
        stats.compute("item2_maxValue", (k, v) -> (v == null || item2Quantity > v) ? item2Quantity : v);

        stats.compute("item1_sum", (k, v) -> (v == null) ? item1Quantity : v + item1Quantity);
        stats.compute("item2_sum", (k, v) -> (v == null) ? item2Quantity : v + item2Quantity);
    }
}