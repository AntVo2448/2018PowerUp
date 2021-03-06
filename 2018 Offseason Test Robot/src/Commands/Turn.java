package Commands;

import org.usfirst.frc.team131.robot.DriveBase;

import NewAutoShell.ChaosCommand;

public class Turn extends ChaosCommand {

	// CounterClockwise is positive turn
	
	DriveBase drive;
	public static final String NAME = "TurnRight";
	
	public Turn(int argsLength, DriveBase drive) {
		super(argsLength, NAME);
		this.drive = drive;
	}
	
	@Override
	protected void initialize () {
		super.initialize();
	}
	
	@Override
	protected boolean isFinished() {
		return doneDriving (drive);
	}
	

	@Override 
	protected void execute() {
		//driveBase.encoderData();
		drive.velocityData();
		drive.turnToAngleRight(Double.parseDouble(args[0]));
	}
	
	@Override
	protected void end () {
		drive.resetEncoders();
		drive.end();
		super.end();
	}


}
