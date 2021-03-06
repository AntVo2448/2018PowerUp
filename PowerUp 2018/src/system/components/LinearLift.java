package system.components;

import org.usfirst.frc.team131.robot.Controller;
import org.usfirst.frc.team131.robot.PortConstants;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class LinearLift {
	
	private static final double MAX_UP_SPEED = 1.0;
	private static final double MIN_UP_SPEED = 0.2;
	
	private static final double MAX_DOWN_SPEED = 0.47;
	private static final double MIN_DOWN_SPEED = 0.01;

	
	
	private static final double PROPORTIONAL_DISTANCE = 12;
	
	private final long changeRate = 10;
	private final double maxAcceleration = 0.03;
	

	double currentSet;
	
	long lastTimeUpdate;
	
	public static final double DEADBAND_INCHES = 2.0; // the acceptable error 
	
	public static final double FLOOR_POSITION_INCHES = 1.5;
	public static final double EXTEND_POSITION_INCHES = 8.0;
	public static final double PORTAL_POSITION_INCHES = 20.0;
	public static final double VAULT_POSITION_INCHES = 15.0;//10.0;
	public static final double SWITCH_POSITION_INCHES = 36.0;
	public static final double HIGH_POSITION_INCHES = 96.0; // position for scale and climb
	

	
	private static final double HOLD_SPEED = 0.04;
	
	private final double chaosRange = 105.375;
	private final double potRange = (0.28 / 1.2) / 0.821917;
	private final double potOffset = 0.0048;

	
	Victor lift1;
	Victor lift2;
	Victor lift3;
	Victor lift4;
	
	SpeedControllerGroup lifts;
	
	String setPosition;
	
	AnalogPotentiometer pot;
	
	double targetPosition;
	
	public LinearLift() {
		pot = new AnalogPotentiometer (new AnalogInput (PortConstants.CHOAS_POT_PORT));
		lift1 = new Victor (PortConstants.LINEAR_LIFT_1);
		lift2 = new Victor (PortConstants.LINEAR_LIFT_2);
		lift3 = new Victor (PortConstants.LINEAR_LIFT_3);
		lift4 = new Victor (PortConstants.LINEAR_LIFT_4);
		lifts = new SpeedControllerGroup(lift1, lift2, lift3, lift4);
		lifts.setInverted(false);
		targetPosition = FLOOR_POSITION_INCHES;
		setPosition = "current position";
		currentSet = 0D;
		lastTimeUpdate = 0;
	}
	
	/**
	 * 
	 * @param speed = min = -1.0, max = 1.0
	 */
	public void setSpeed (final double speed) {
//		if ((chaosPotGet() >= HIGH_POSITION_INCHES && speed > 0.0) || (chaosPotGet() <= FLOOR_POSITION_INCHES && speed < 0.0)) {
//			lifts.set(0.0);
//		} else {
			lifts.set(speed);
//		}
			
			
			
			//System.out.println (chaosPotGet());
	}
	/**
	 * 
	 * @param direction - Up = High (climb/scale), Left = Vault, Right = Switch, Down = Floor
	 */
	public void setToPosition (final Controller.DPadDirection direction) {
		
		switch (direction) {
		case DOWN:
			targetPosition = FLOOR_POSITION_INCHES;
			setPosition = "floor position";
			break;
		case RIGHT:
			targetPosition = SWITCH_POSITION_INCHES;
			setPosition = "switch position";
			break;
		case LEFT:
			targetPosition = VAULT_POSITION_INCHES;
			setPosition = "scale position";
			break;
		case UP:
			targetPosition = HIGH_POSITION_INCHES;
			setPosition = "high position";
			break;
		case NONE:
		default:
			targetPosition = getChaosPot();
			setPosition = "current position";
			break;
		}
		
	}
	
	public void setToIntakePosition () {
		targetPosition = EXTEND_POSITION_INCHES;
	}
	
	public void setToFloorPosition () {
		targetPosition = FLOOR_POSITION_INCHES;
	}
	
	
	
	public boolean liftIsStopped () {
		return isAtTargetPosition();
	}
	
	public boolean isAtTargetPosition () {
		return Math.abs(Math.abs(targetPosition) - Math.abs(getChaosPot())) <= DEADBAND_INCHES ;
	}
	
	public void setTargetPosition (double target) {
		targetPosition = target;
	}
	
	public double liftPosition () {
		return getChaosPot();
	}
	
	public void moveToPosition () {
		
		if (isAtTargetPosition ()) {
			setSpeed(HOLD_SPEED);
		} else {
			setSpeed(getProportionalSet());
		}
		
	}
	/**
	 * 
	 * @return - Takes %distance to target position and sets a speed based on that % of a set Max speed. Cannot go above 1 or below -1
	 */
	private double getProportionalSet () {
		
		return getProportionalSet(targetPosition, getChaosPot());
		
	}
	
	/**
	 * Sets the speed of the lift to go to position
	 * Limits acceleration so that it has to ramp up before getting to maximum speed
	 * Begins to decelerate once it reaches set proportional distance, with no limit on deceleration rate
	 * 
	 * @param targetPosition - height to go to
	 * @param currentPosition - where the lift is at the start
	 * @return - speed to get to position
	 */
	public double getProportionalSet (double targetPosition, double currentPosition) {
		

		// if at the correct position, return 0 to void NaN errors
			if (targetPosition == currentPosition) {
				currentSet = 0;
				return 0;
			}
			
			// the slope of x value distance away from target distance
			double proportionalSet = (targetPosition- currentPosition) / PROPORTIONAL_DISTANCE;
			
			double maxSpeed;
			double minSpeed;
			double signModifier;
			
			// keeps proportions within 1 and -1
			if (proportionalSet > 1.0) {
				proportionalSet = 1.0;
			} else if (proportionalSet < -1.0) {
				proportionalSet = -1.0;
			}
			
			boolean proportionalSetIsPositive = proportionalSet > 0.0;
			
			if (proportionalSetIsPositive) {
				maxSpeed = MAX_UP_SPEED;
				minSpeed = MIN_UP_SPEED;
				signModifier = 1.0;
			} else {
				maxSpeed = MAX_DOWN_SPEED;
				minSpeed = MIN_DOWN_SPEED;
				signModifier = -1.0;
			}
			
			// sets the value of proportional set
			proportionalSet = maxSpeed * proportionalSet;
			
			//makes sure value is within min speed
			if (Math.abs(proportionalSet) < minSpeed) 
				proportionalSet = signModifier * minSpeed;

			
			double velocityChange = (proportionalSetIsPositive) ? proportionalSet - currentSet : Math.abs(currentSet - proportionalSet);
			
			if (currentSet == 0.0) {
				velocityChange = Math.abs(currentSet - proportionalSet); 
			} else if (proportionalSet == 0.0) {
				velocityChange = -Math.abs(currentSet - proportionalSet); 
			}
			
			// if ΔV > max acceleration
			if (velocityChange > maxAcceleration) {
				
				if ((proportionalSet > 0 && currentSet < 0) || (proportionalSet < 0 && currentSet > 0)) {
					
					proportionalSet = 0.0;
					
				} else if (lastTimeUpdate + changeRate < System.currentTimeMillis()) {
					
					double addAcceleration = (proportionalSetIsPositive) ? maxAcceleration : -maxAcceleration;
					
					proportionalSet = currentSet + addAcceleration;
					
					lastTimeUpdate = System.currentTimeMillis();
				
				} else {
					
					proportionalSet = currentSet;
					
				}
				
			}
				
			//System.out.printf("p-set: %.3f,\tposition: %.3f\n", proportionalSet, getChaosPot());
			
			currentSet = proportionalSet;
			
			return proportionalSet;
		
	 
			
	}
	
	public double getChaosPot () {
		
		double slope = (chaosRange / potRange);
		
		return slope * (pot.get() - potOffset);
	}
	
	public void putInfo () {
		
		SmartDashboard.putNumber("Lift position (in inches): ", getChaosPot());
		SmartDashboard.putNumber("Lift position (raw): ", pot.get());
		SmartDashboard.putString("Set Position: ", setPosition);
		SmartDashboard.putNumber("Speed set: ", getProportionalSet());
		
		
		
		
	}
	
}
