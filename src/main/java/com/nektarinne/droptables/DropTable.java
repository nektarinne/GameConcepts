package com.nektarinne.droptables;

import com.nektarinne.common.Item;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

/**
 * Represents a drop table. A drop table yields a number of items. A drop table can link to another (often rarer) drop table.
 */
public class DropTable {

    private final Map<Item, Double> itemProbabilities;
    private final Map<DropTable, Double> dropTableProbabilities;
    private final Random random = new Random();

    public DropTable(Map<Item, Double> itemProbabilities, Map<DropTable, Double> dropTableProbabilities) {
        Objects.requireNonNull(itemProbabilities);
        Objects.requireNonNull(dropTableProbabilities);
        this.itemProbabilities = Map.copyOf(itemProbabilities);
        this.dropTableProbabilities = Map.copyOf(dropTableProbabilities);
    }

    public Map<Item, Integer> drop() {
        Map<Item, Integer> result = new HashMap<>();
        itemProbabilities.forEach((item, proba) -> {
            if (random.nextDouble() <= proba) {
                result.put(item, 1);
            }
        });
        dropTableProbabilities.forEach((dropTable, proba) -> {
            if (random.nextDouble() <= proba) {
                dropTable.drop().forEach((item, numberOfItem) -> result.compute(item, (k, v) -> v == null ? numberOfItem : v + numberOfItem));
            }
        });
        return result;
    }

    public Map<Item, Integer> drop(int times) {
        if (times <= 100) {
            Map<Item, Integer> result = new HashMap<>();
            for (int counter = 0; counter < times; counter++) {
                drop().forEach((item, numberOfItem) -> result.compute(item, (k, v) -> v == null ? numberOfItem : v + numberOfItem));
            }
            return result;
        }
        Map<Item, Integer> result = new HashMap<>();
        itemProbabilities.forEach((item, proba) -> {
            if (proba <= 0.01) {
                double newProba = proba * times;
                if (random.nextDouble() <= newProba) {
                    result.put(item, 1);
                }
            } else {
                result.put(item, (int) Math.round(proba * times));
            }
        });
        dropTableProbabilities.forEach((dropTable, proba) -> {
            int timesToCall = (int) Math.round(proba * times);
            dropTable.drop(timesToCall).forEach((item, numberOfItem) -> result.compute(item, (k, v) -> v == null ? numberOfItem : v + numberOfItem));
        });
        return result;
    }
}
