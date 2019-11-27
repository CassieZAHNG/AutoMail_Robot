package automail;

import exceptions.FragileItemBrokenException;
import exceptions.TubeFullException;
import strategies.IMailPool;
import strategies.Item;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

public class CarefulRobot extends Robot {
    private boolean movedLastStep;
    private static final int maxCapacity = 3;

    public CarefulRobot(IMailDelivery delivery, IMailPool mailPool) {
        super(delivery, mailPool, maxCapacity);
        this.movedLastStep = false;
    }

    private boolean hasFragile(){
        Iterator<MailItem> i = tube.tube.iterator();
        boolean fragile = false;
        while(i.hasNext()){
            MailItem m = i.next();
            if(m.getFragile()){
                fragile = true;
            }
        }
        return fragile;
    }

    void moveTowards(int destination) {
//        if (deliveryItem != null && deliveryItem.getFragile() || !tube.isEmpty() && tube.peek().getFragile()) throw new FragileItemBrokenException();
            if(!movedLastStep){
                if(current_floor < destination){
                    current_floor++;
                }
                else{
                    current_floor--;
                }
                movedLastStep = true;
            } else {
                movedLastStep = false;
            }
    }

    @Override
    public void fillStorageTube(LinkedList<Item> pool) throws FragileItemBrokenException {
        try { // Get as many items as available or as fit

            // load a fragile item if exists, if not, load as many items as possible and go
            ListIterator<Item> i = pool.listIterator();
            while(i.hasNext() && !pool.isEmpty()) {
                Item item = i.next();
                if(item.getFragile()){
                    tube.addItem(item.getMailItem());
                    i.remove();
                    break;
                }
            }

            if(!hasFragile()){
                ListIterator<Item> it = pool.listIterator();
                while(it.hasNext() && !pool.isEmpty() && !tube.isFull()) {
                    Item item = it.next();
                    tube.addItem(item.getMailItem());
                    it.remove();
                }
            }

            if (tube.getSize() > 0) {
                dispatch();
            }
        }
        catch(TubeFullException e){
            e.printStackTrace();
        }
    }
}
