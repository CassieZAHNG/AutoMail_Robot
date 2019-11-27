package automail;
/*
 * the class used to describe the properties and behaviors of robot storage tube:
 * properties: the maximum capacity of a tube
 * behaviors:
 * 1. Add an item to the tube with conditions
 * 2. check the size, the state of top item
 * 3. get the first item of tube
 */

import exceptions.FragileItemBrokenException;
import exceptions.TubeFullException;

import java.util.Stack;

/**
 * The storage tube carried by the robot.
 */
class StorageTube {

    final int MAXIMUM_CAPACITY;
    Stack<MailItem> tube;

    // Initialize the StorageTube with maximum capacity and a new stack tube
    StorageTube(int maxCapacity){
        this.tube = new Stack<MailItem>();
        this.MAXIMUM_CAPACITY = maxCapacity;
    }

    // check if the tube is full
    boolean isFull(){
        return tube.size() == MAXIMUM_CAPACITY;
    }

    // check if tube is empty
    boolean isEmpty(){
        return tube.isEmpty();
    }

    // check the first mailItem of tube
    MailItem peek() {
    	return tube.peek();
    }

    // this function used to add item to robot tube.
    // this action happens when the tube size is less than maximum capacity
    void addItem(MailItem item) throws TubeFullException, FragileItemBrokenException {
        if(tube.size() < MAXIMUM_CAPACITY){
        	if (tube.isEmpty()) {
        		tube.add(item);
        	} else if (item.getFragile() || tube.peek().getFragile()) {
                // if the tube is not empty and the item(in robot hand) or the item
                // in the top of the tube is fragile, then throw the
                // FragileItemBrokenException
        		throw new FragileItemBrokenException();
        	} else {
        		tube.add(item);
        	}
        } else {
            // if items in tube more than tube's capacity, then throw the
            // TubeFullException
            throw new TubeFullException();
        }
    }

    //get the number of items in tube
    int getSize(){
    	return tube.size();
    }

    // get the first item out of the tube
    MailItem pop(){
        return tube.pop();
    }

}
