package automail;
/*
 * Abstract class for all different types of robots.
 * Robot class represents the prototype of general robot. Distinct
 * features of robot would be customized within different robot classes.
 * Apart from features and necessary information about robots,this class
 * also defines the general pattern of robot state change, expected behaviors,
 * and hashCode as robot identifier.
 */

import exceptions.ExcessiveDeliveryException;
import exceptions.ItemTooHeavyException;
import exceptions.FragileItemBrokenException;
import exceptions.TubeFullException;
import strategies.IMailPool;
import strategies.Item;

import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * The robot delivers mail!
 */
public abstract class Robot {

	StorageTube tube;
    private IMailDelivery delivery;
    private final String id;
    /** Possible states the robot can be in */
    public enum RobotState { DELIVERING, WAITING, RETURNING }
    private RobotState current_state;
    int current_floor;
    int destination_floor;
    private IMailPool mailPool;
    private boolean receivedDispatch;

    MailItem deliveryItem;

    private int deliveryCounter;
    

    /**
     * Initiates the robot's location at the start to be at the mailroom
     * also set it to be waiting for mail.
     * @param delivery governs the final delivery
     * @param mailPool is the source of mail items
     */
    public Robot(IMailDelivery delivery, IMailPool mailPool, int maxCapacity){
    	id = "R" + hashCode();//Assign identifier using hashCode.
    	current_state = RobotState.RETURNING;
        current_floor = Building.MAILROOM_LOCATION;
        // Create a tube for a robot based on maxCapacity argument while
        // creating a robot.
        tube = new StorageTube(maxCapacity);
        this.delivery = delivery;
        this.mailPool = mailPool;
        this.receivedDispatch = false;
        this.deliveryCounter = 0;
    }
    
    void dispatch() {
    	receivedDispatch = true;
    }

    // Generic logic for a robot when it tries to fill its tube with mailItems.
    public void fillStorageTube(LinkedList<Item> pool) throws FragileItemBrokenException {
        // default behavior does not pick up fragile items
        // and picks up as much as tube capacity allows
        try { // Get as many items as available or as fit

            ListIterator<Item> i = pool.listIterator();
            while(!tube.isFull() && !pool.isEmpty()) {
                if (i.hasNext()){
                    Item item = i.next();
                    if(!item.getFragile()){
                        tube.addItem(item.getMailItem());
                        i.remove();
                    }
                } else {
                    break;
                }
            }

            //A robot would be dispatched if its tube was filled with one or
            //more mailItems.
            if (tube.getSize() > 0) {
                dispatch();
            }
        }
        catch(TubeFullException e) {
            e.printStackTrace();
        }
    }
    /**
     * This is called on every time step
     * @throws ExcessiveDeliveryException if robot delivers more than the capacity of the tube without refilling
     */

    // Defines robot behaviors for each RobotState
    // Returning, Waiting and delivering.
    void step() throws ExcessiveDeliveryException, ItemTooHeavyException, FragileItemBrokenException {
    	switch(current_state) {
    		/** This state is triggered when the robot is returning to the mailroom after a delivery */
    		case RETURNING:
    			/** If its current position is at the mailroom, then the robot should change state */
                if(current_floor == Building.MAILROOM_LOCATION){
                	while(!tube.isEmpty()) { //return items back to pool
                		MailItem mailItem = tube.pop();
                		mailPool.addToPool(mailItem);
                        System.out.printf("T: %3d > old addToPool [%s]%n", Clock.Time(), mailItem.toString());
                	}
        			/** Tell the sorter the robot is ready */
        			mailPool.registerWaiting(this);
                	changeState(RobotState.WAITING);
                } else { // case robot on its way back to mailroom. Move one step.
                	/** If the robot is not at the mailroom floor yet, then move towards it! */
                    moveTowards(Building.MAILROOM_LOCATION);
                	break;
                }
    		case WAITING:
                /** If the StorageTube is ready and the Robot is waiting in the mailroom then start the delivery */
                if(!tube.isEmpty() && receivedDispatch){
                	receivedDispatch = false;
                	deliveryCounter = 0; // reset delivery counter
        			setRoute();
        			mailPool.deregisterWaiting(this);
                	changeState(RobotState.DELIVERING);
                }
                break;
    		case DELIVERING:
                //case robot arrives at target floor during one delivery.
    			if(current_floor == destination_floor){ // If already here drop off either way
                    /** Delivery complete, report this to the simulator! */
                    delivery.deliver(deliveryItem);
                    deliveryCounter++;
                    //The number of deliveries cannot be larger than the capacity of
                    //robot's tube.
                    if(deliveryCounter > tube.MAXIMUM_CAPACITY){  // Implies a simulation bug
                    	throw new ExcessiveDeliveryException();
                    }
                    /** Check if want to return, i.e. if there are no more items in the tube*/
                    if(tube.isEmpty()){
                    	changeState(RobotState.RETURNING);
                    }
                    else{
                        /** If there are more items, set the robot's route to the location to deliver the item */
                        setRoute();
                        changeState(RobotState.DELIVERING);
                    }
    			} else {
                    // Move forward during delivery.
	                moveTowards(destination_floor);
    			}
                break;
    	}
    }

    // Get the top item in tube as deliveryItem and determine target floor based
    // on the address of this item.
    void setRoute() throws ItemTooHeavyException{
        // Pop the item from the StorageUnit
        deliveryItem = tube.pop();
        // Set the destination floor
        destination_floor = deliveryItem.getDestFloor();
    }

    // generic robot movement behavior
    // note that slower robot needs to implement their own version
    void moveTowards(int destination) throws FragileItemBrokenException {
        if (deliveryItem != null && deliveryItem.getFragile() || !tube.isEmpty() && tube.peek().getFragile()) throw new FragileItemBrokenException();
        if(current_floor < destination){
            current_floor++;
        }
        else{
            current_floor--;
        }
    }

    // Show robot id, current items in tube and total tube size.
    private String getIdTube() {
    	return String.format("%s(%1d/%1d)", id, tube.getSize(), tube.MAXIMUM_CAPACITY);
    }

    // Convert robot state based on nextState argument, during which
    // show the details of the transition.
    private void changeState(RobotState nextState){
    	if (current_state != nextState) {
            System.out.printf("T: %3d > %7s changed from %s to %s%n", Clock.Time(), getIdTube(), current_state, nextState);
    	}
    	current_state = nextState;
    	if(nextState == RobotState.DELIVERING){
            System.out.printf("T: %3d > %7s-> [%s]%n", Clock.Time(), getIdTube(), deliveryItem.toString());
    	}
    }

//    we don't need  to getTube after implementing various robots based on principle of polymorphism
//	public StorageTube getTube() {
//		return tube;
//	}

    //hashCode is used to assign identifiers for different robots.
	static private int count = 0;
	static private Map<Integer, Integer> hashMap = new TreeMap<Integer, Integer>();

	@Override
	public int hashCode() {
		Integer hash0 = super.hashCode();
		Integer hash = hashMap.get(hash0);
		if (hash == null) { hash = count++; hashMap.put(hash0, hash); }
		return hash;
	}
}
