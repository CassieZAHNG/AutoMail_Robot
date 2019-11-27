package strategies;
/*
 * Item class is separated from MyMailPool class.
 * Item class defines item and its components.
 */

import automail.MailItem;
import automail.PriorityMailItem;
import automail.WeakRobot;

public class Item {
    int priority;
    int destination;
    private boolean heavy;
    private boolean fragile;
    private MailItem mailItem;
    // Use stable sort to keep arrival time relative positions

    Item(MailItem mailItem) {
        this.priority = (mailItem instanceof PriorityMailItem) ? ((PriorityMailItem) mailItem).getPriorityLevel() : 1;
        this.heavy = mailItem.getWeight() >= WeakRobot.itemWeightLimit;
        this.fragile = mailItem.getFragile();
        this.destination = mailItem.getDestFloor();
        this.mailItem = mailItem;
    }

    public MailItem getMailItem(){
        return mailItem;
    }

    public boolean getFragile (){
        return fragile;
    }

    public boolean getHeavy(){
        return heavy;
    }

}
