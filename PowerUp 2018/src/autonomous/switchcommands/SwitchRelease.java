package autonomous.switchcommands;

import autonomous.builder.ChaosCommand;
import autonomous.builder.GameData;
import autonomous.builder.TimeRestrictedCommand;
import system.components.CubeManipulator;

public class SwitchRelease extends TimeRestrictedCommand {
	
	public static final String NAME = "SwitchRelease";
	private CubeManipulator cubeManipulator;
	private boolean setOn;

	public SwitchRelease(int argsLength, CubeManipulator cubeManipulator) {
		super(argsLength, NAME);
		this.cubeManipulator = cubeManipulator;
		setOn = false;
	}

	@Override
	protected boolean isFinished() {
		
		return cubeManipulator.isReleased() || super.isFinished() || !setOn;
	}
	
	@Override
	protected void initialize () {
		super.initialize();
		GameData data = new GameData ();
		if (data.closeSwitchIsLeft()) {
			
			if (args[0].equals("1")) {
				
				cubeManipulator.release();
				setOn = true;
				
			}
			
			
		} else {
			
			if (args[1].equals("1")) {
				
				cubeManipulator.release();
				setOn = true;
				
			}
			
		}
		
	}

}
