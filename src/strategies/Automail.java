package strategies;
/*
 * The class is used to initialize robots according the inputs of automail.properties.
 *
 */

import automail.*;

import java.util.ArrayList;
import java.util.List;

public class Automail {
	      
    public List<Robot> robot = new ArrayList<>();
    public IMailPool mailPool;
    
    public Automail(IMailPool mailPool, IMailDelivery delivery, List<Simulation.RobotType> robotTypes) {
    	// Swap between simple provided strategies and your strategies here
    	// Initialize the MailPool
    	
    	this.mailPool = mailPool;

        // This function is responsible for creating a new robot by the types of robots
        // specified in automail.properties; There are only four types of robots: Big, Standard
        // Weak, and Careful;
		for(Simulation.RobotType robotType : robotTypes ){
			switch (robotType){
				case Big:
					robot.add(new BigRobot(delivery, mailPool));
					break;
				case Standard:
					robot.add(new StandardRobot(delivery, mailPool));
					break;
				case Weak:
					robot.add(new WeakRobot(delivery, mailPool));
					break;
				case Careful:
					robot.add(new CarefulRobot(delivery, mailPool));
					break;
                default:
                    System.out.println("Robot type "+robotType.toString()+" not supported");
                    break;
			}
		}
    }
    
}
