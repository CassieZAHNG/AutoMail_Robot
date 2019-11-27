package strategies;
/* This class prepares all the elements used for RMS company delivering.
 * First, it initializes mailPool and robots.
 * Besides, robots'tube filling, register and release robots also happens in this class.
 *
 */

import automail.*;
import exceptions.FragileItemBrokenException;

import java.util.LinkedList;

public class MyMailPool implements IMailPool {
	
	private LinkedList<Item> pool; //used to store mailItems
	private LinkedList<Robot> robots; //used to store robots available at mail room

    // this is the constructor of MyMailPool class, used to initialize the new mail pool
    // lightCount and robots list;
	public MyMailPool(){
		// Start empty
		pool = new LinkedList<>();
		robots = new LinkedList<>();
	}

    // this function is used to add mailItem to mailPool and sort
    // based on the priority level ranking
	public void addToPool(MailItem mailItem) {
		Item item = new Item(mailItem);
		pool.add(item);
		pool.sort(new ItemComparator());
	}

    // this loop function is used to fill every robot's storage tube.
	@Override
	public void step() throws FragileItemBrokenException {
		for (Robot robot: robots) { robot.fillStorageTube(pool); }
	}

	// registering waiting robots to the mail room as available
	@Override
	public void registerWaiting(Robot robot) { // assumes won't be there
		if (robot instanceof BigRobot || robot instanceof StandardRobot || robot instanceof CarefulRobot) {
			robots.add(robot); 
		} else {
			robots.addLast(robot); // weak robot last as want more efficient delivery with highest priorities
		}
	}

    // this function is used to remove robot from mail room as it goes out to deliver
	@Override
	public void deregisterWaiting(Robot robot) {
		robots.remove(robot);
	}

}
