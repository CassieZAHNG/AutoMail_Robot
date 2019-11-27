package automail;
/*
 * WeakRobot class extends from Robot Class. This robot has their own properties.
 * 1. the robot can carry less than 4 items
 * 2. each item weight limited to 2000.
 *
 */

import exceptions.FragileItemBrokenException;
import exceptions.ItemTooHeavyException;
import exceptions.TubeFullException;
import strategies.IMailPool;
import strategies.Item;

import java.util.LinkedList;
import java.util.ListIterator;

public class WeakRobot extends Robot {
    public static final int itemWeightLimit = 2000;
    private static final int maxCapacity = 4;

    public WeakRobot(IMailDelivery delivery, IMailPool mailPool) {
        super(delivery, mailPool, maxCapacity);
    }

    @Override
    void setRoute() throws ItemTooHeavyException {
        // Pop the item from the StorageUnit
        deliveryItem = tube.pop();
        if (deliveryItem.weight > itemWeightLimit) throw new ItemTooHeavyException();
        // Set the destination floor
        destination_floor = deliveryItem.getDestFloor();
    }

    @Override
    public void fillStorageTube(LinkedList<Item> pool) throws FragileItemBrokenException {
        try { // Get as many items as available or as fit

            // get as many non-fragile and non-heavy items as possible from mail pool
            ListIterator<Item> i = pool.listIterator();
            while(!tube.isFull() && !pool.isEmpty()) {
                if (i.hasNext()){
                    Item item = i.next();
                    if(!item.getFragile() && !item.getHeavy()){
                        tube.addItem(item.getMailItem());
                        i.remove();
                    }
                } else {
                    break;
                }
            }

            // if there is at least 1 item, dispatch robot
            if (tube.getSize() > 0) {
                dispatch();
            }
        }
        catch(TubeFullException e) {
            e.printStackTrace();
        }
    }
}
