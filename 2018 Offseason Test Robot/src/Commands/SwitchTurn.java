package Commands;

import org.usfirst.frc.team131.robot.DriveBase;

import NewAutoShell.ChaosCommand;
import NewAutoShell.GameData;

public class SwitchTurn extends ChaosCommand {

	// CounterClockwise is positive turn
	
	DriveBase drive;
	public static final String NAME = "SwitchTurn";
	
	public SwitchTurn(int argsLength, DriveBase drive) {
		super(argsLength);
		this.drive = drive;
	}
	
	@Override
	protected void initialize () {
		drive.resetEncoders();
		GameData data = new GameData ();
		if (data.closeSwitchIsLeft()) {
			drive.turnToAngle(Double.parseDouble(args[0]));
		} else {
		
			drive.turnToAngle(Double.parseDouble(args[1]));
			
		}
	}

	@Override
	protected boolean isFinished() {
		return doneDriving (drive);
	}
	
	@Override 
	protected void execute() {
		//driveBase.encoderData();
		drive.velocityData();
	}
	
	@Override
	protected void end () {
		drive.resetEncoders();
		drive.end();
	}

}