package automail;
/*
 * Clock is used to trace the time during events.
 * method visibility is narrowed to package
 */

class Clock {
	
	/** Represents the current time **/
    private static int Time = 0;
    
    /** The threshold for the latest time for mail to arrive **/
    static int LAST_DELIVERY_TIME;

    static int Time() {
    	return Time;
    }
    
    static void Tick() {
    	Time++;
    }
}
