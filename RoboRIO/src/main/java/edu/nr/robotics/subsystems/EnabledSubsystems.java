package edu.nr.robotics.subsystems;

public class EnabledSubsystems {
	public static final boolean DRIVE_ENABLED 		= false,
								SHOOTER_ENABLED 	= true,
								HOOD_ENABLED 		= true,
								TURRET_ENABLED 		= false,
								INTAKE_ENABLED 		= false,
								INTAKE_ARM_ENABLED 	= false,
								INTAKE_SLIDE_ENABLED= false,
								GEAR_MOVER_ENABLED 	= false,
								LOADER_ENABLED 		= true,
								AGITATOR_ENABLED 	= false,
								COMPRESSOR_ENABLED 	= false;
	
	//These start the subsystem in dumb mode. Changing them later sometimes ignores this.
	public static final boolean DRIVE_DUMB_ENABLED	 	= false,
								SHOOTER_DUMB_ENABLED 	= false,
								HOOD_DUMB_ENABLED 		= false,
								TURRET_DUMB_ENABLED 	= false;
	
	
	public static final boolean AGITATOR_SMARTDASHBOARD_BASIC_ENABLED 	= false,
								DRIVE_SMARTDASHBOARD_BASIC_ENABLED 		= false,
								HOOD_SMARTDASHBOARD_BASIC_ENABLED 		= true,
								GEAR_MOVER_SMARTDASHBOARD_BASIC_ENABLED = false,
								INTAKE_SMARTDASHBOARD_BASIC_ENABLED 	= false,
								INTAKEARM_SMARTDASHBOARD_BASIC_ENABLED 	= false,
								INTAKESLIDE_SMARTDASHBOARD_BASIC_ENABLED= false,
								LOADER_SMARTDASHBOARD_BASIC_ENABLED 	= true,
								SHOOTER_SMARTDASHBOARD_BASIC_ENABLED 	= true,
								TURRET_SMARTDASHBOARD_BASIC_ENABLED 	= true;

	public static final boolean AGITATOR_SMARTDASHBOARD_COMPLEX_ENABLED		= false,
								DRIVE_SMARTDASHBOARD_COMPLEX_ENABLED 		= false,
								HOOD_SMARTDASHBOARD_COMPLEX_ENABLED 		= true,
								GEAR_MOVER_SMARTDASHBOARD_COMPLEX_ENABLED 	= false,
								INTAKE_SMARTDASHBOARD_COMPLEX_ENABLED 		= false,
								INTAKEARM_SMARTDASHBOARD_COMPLEX_ENABLED 	= false,
								INTAKESLIDE_SMARTDASHBOARD_COMPLEX_ENABLED 	= false,
								LOADER_SMARTDASHBOARD_COMPLEX_ENABLED 		= true,
								SHOOTER_SMARTDASHBOARD_COMPLEX_ENABLED 		= true,
								TURRET_SMARTDASHBOARD_COMPLEX_ENABLED 		= true;
}
