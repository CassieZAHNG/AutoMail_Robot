package automail;
/*
 * Class for robot type: Big.
 */

import strategies.IMailPool;

public class BigRobot extends Robot {
    //MaxCapacity of BigRobot tube is 6.
    private static final int maxCapacity = 6;

    public BigRobot(IMailDelivery delivery, IMailPool mailPool) {
        super(delivery, mailPool, maxCapacity);
    }

}
