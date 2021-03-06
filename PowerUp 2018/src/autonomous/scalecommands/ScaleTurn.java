package autonomous.scalecommands;

import autonomous.builder.ChaosCommand;
import autonomous.builder.GameData;
import system.components.DriveBase;

public class ScaleTurn extends ChaosCommand {


	
	DriveBase drive;
	GameData data;
	public static final String NAME = "ScaleTurnRight";
	
	public ScaleTurn(int argsLength, DriveBase drive) {
		super(argsLength, NAME);
		this.drive = drive;
	}
	
	@Override
	protected void initialize () {
		super.initialize();
		drive.resetEncoders();
		data = new GameData ();
		//drive.setGains(0.0125, 0.1);
		
	}

	@Override
	protected boolean isFinished() {
		return doneDriving (drive);
	}
	
	@Override 
	protected void execute() {

		if (data.scaleIsLeft()) {
			
			drive.turnToAngleRight(Double.valueOf(args[0]));
			
		} else {
		
			drive.turnToAngleRight(Double.valueOf(args[1]));
			
		}
	}
	
	@Override
	protected void end () {
		drive.resetEncoders();
		drive.end();
		super.end();
	}

}
