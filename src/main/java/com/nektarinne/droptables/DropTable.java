package com.nektarinne.droptables;

import com.nektarinne.common.Item;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.random.RandomGenerator;

/**
 * Represents a drop table. A drop table yields a number of items. A drop table can link to another (often rarer) drop table.
 */
public class DropTable {

    private final Map<Item, Double> itemProbabilities;
    private final Map<DropTable, Double> dropTableProbabilities;
    private final RandomGenerator random = RandomGenerator.getDefault();

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

    public Map<Item, Integer> drop(long times) {
        Map<Item, Integer> result = new HashMap<>();
        itemProbabilities.forEach((item, proba) -> {
            int r = calc(times, proba);
            if (r > 0) {
                result.put(item, r);
            }
        });
        dropTableProbabilities.forEach((dropTable, proba) -> {
            int timesToCall = (int) Math.round(proba * times);
            dropTable.drop(timesToCall).forEach((item, numberOfItem) -> result.compute(item, (k, v) -> v == null ? numberOfItem : v + numberOfItem));
        });
        return result;
    }

    private int calc(long numberOfDrops, double probability) {
        long result;
        do {
            double rand = random.nextInt(1, 100) / 100.0;
            // random is more likely to be near 0.5, and much less 0 or 1
            double weightedR = rand < 0.5 ? (0.5 + (0.1 * Math.log(2 * rand))) : 0.5 + (0.1 * Math.exp(-9 + 10 * rand));
            result = Math.round(((probability / (1 - weightedR)) - probability) * numberOfDrops);
        } while (result > numberOfDrops);
        return (int) result;
    }
}
