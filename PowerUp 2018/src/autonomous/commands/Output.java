package autonomous.commands;

import autonomous.builder.TimeRestrictedCommand;
import system.components.CubeManipulator;

public class Output extends TimeRestrictedCommand {
	
	public static final String NAME = "Output";

	CubeManipulator cubeManipulator;
	
	public Output(int argsLength, CubeManipulator cubeManipulator) {
		super(argsLength, NAME);
		this.cubeManipulator = cubeManipulator;
	}

	@Override
	protected boolean isFinished() {
		return super.isFinished();
	}
	
	@Override
	protected void execute () {
		
		cubeManipulator.slowOutput();
		
	}
	
	@Override
	protected void initialize () {
		super.initialize();
	}
	
	@Override
	protected void end() {
		
		cubeManipulator.stopSpeed();
		
	
	
	}

}
