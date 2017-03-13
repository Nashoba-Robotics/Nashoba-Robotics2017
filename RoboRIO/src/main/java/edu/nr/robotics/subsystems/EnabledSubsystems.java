package edu.nr.robotics.subsystems;

public class EnabledSubsystems {
	public static final boolean DRIVE_ENABLED 		= true,
								SHOOTER_ENABLED 	= true,
								HOOD_ENABLED 		= true,
								TURRET_ENABLED 		= true,
								INTAKE_ENABLED 		= false,
								INTAKE_ARM_ENABLED 	= true,
								INTAKE_SLIDE_ENABLED= true,
								GEAR_MOVER_ENABLED 	= true,
								LOADER_ENABLED 		= true,
								AGITATOR_ENABLED 	= true;
	
	//These start the subsystem in dumb mode. Changing them later sometimes ignores this.
	public static final boolean DRIVE_DUMB_ENABLED	 	= false,
								SHOOTER_DUMB_ENABLED 	= true,
								HOOD_DUMB_ENABLED 		= false,
								TURRET_DUMB_ENABLED 	= true;
	
	
	public static final boolean AGITATOR_SMARTDASHBOARD_BASIC_ENABLED 	= true,
								DRIVE_SMARTDASHBOARD_BASIC_ENABLED 		= true,
								HOOD_SMARTDASHBOARD_BASIC_ENABLED 		= true,
								GEAR_MOVER_SMARTDASHBOARD_BASIC_ENABLED = true,
								INTAKE_SMARTDASHBOARD_BASIC_ENABLED 	= true,
								INTAKEARM_SMARTDASHBOARD_BASIC_ENABLED 	= true,
								INTAKESLIDE_SMARTDASHBOARD_BASIC_ENABLED= true,
								LOADER_SMARTDASHBOARD_BASIC_ENABLED 	= true,
								SHOOTER_SMARTDASHBOARD_BASIC_ENABLED 	= true,
								TURRET_SMARTDASHBOARD_BASIC_ENABLED 	= true;


	public static final boolean AGITATOR_SMARTDASHBOARD_COMPLEX_ENABLED		= true,
								DRIVE_SMARTDASHBOARD_COMPLEX_ENABLED 		= true,
								HOOD_SMARTDASHBOARD_COMPLEX_ENABLED 		= true,
								GEAR_MOVER_SMARTDASHBOARD_COMPLEX_ENABLED 	= true,
								INTAKE_SMARTDASHBOARD_COMPLEX_ENABLED 		= true,
								INTAKEARM_SMARTDASHBOARD_COMPLEX_ENABLED 	= true,
								INTAKESLIDE_SMARTDASHBOARD_COMPLEX_ENABLED 	= true,
								LOADER_SMARTDASHBOARD_COMPLEX_ENABLED 		= true,
								SHOOTER_SMARTDASHBOARD_COMPLEX_ENABLED 		= true,
								TURRET_SMARTDASHBOARD_COMPLEX_ENABLED 		= true;

}
