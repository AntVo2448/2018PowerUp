package autonomous.switchcommands;

import autonomous.builder.ChaosCommand;
import autonomous.builder.GameData;
import system.components.DriveBase;

public class SwitchDrive extends ChaosCommand {

	DriveBase drive;
	public static final String NAME = "SwitchDrive";
	GameData data;
	
	public SwitchDrive(int argsLength, DriveBase drive) {
		super(argsLength, NAME);
		this.drive = drive;
	}
	
	@Override
	protected void initialize () {
		super.initialize();
		drive.resetEncoders();
		data = new GameData ();
		//drive.setGains(0.015, 0.1);
		
	}

	@Override
	protected boolean isFinished() {
		return doneDriving (drive);
	}
	

	@Override 
	protected void execute() {
		
		if (data.closeSwitchIsLeft()) {
			
			drive.tankCorrectedDrive(Double.parseDouble(args[0]), Double.parseDouble(args[0]));
			
		} else {
		
			drive.tankCorrectedDrive(Double.parseDouble(args[1]), Double.parseDouble(args[1]));
			
		}
		
	}
	
	@Override
	protected void end () {
		drive.resetEncoders();
		drive.end();
		super.end();
	}


}
