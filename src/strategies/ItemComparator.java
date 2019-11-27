package strategies;
/*
 * ItemComparator is separated from MyMailPool class.
 * ItemComparator acts as a comparator when comparing items.
 */

import java.util.Comparator;

public class ItemComparator implements Comparator<Item> {
    @Override
    public int compare(Item i1, Item i2) {
        int order = 0;
        if (i1.priority < i2.priority) {
            order = 1;
        } else if (i1.priority > i2.priority) {
            order = -1;
        } else if (i1.destination < i2.destination) {
            order = -1; // high floor last, so that they sink to bottom of tube stack
        } else if (i1.destination > i2.destination) {
            order = 1;
        }
        return order;
    }
}