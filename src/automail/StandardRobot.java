package automail;
/*
 * Standard Robot class extends from Robot Class.
 */

import strategies.IMailPool;

public class StandardRobot extends Robot {
    //the maximum number of items that the standard robot can carry is 4
    private static final int maxCapacity = 4;

    public StandardRobot(IMailDelivery delivery, IMailPool mailPool) {
        super(delivery, mailPool, maxCapacity);
    }

}
