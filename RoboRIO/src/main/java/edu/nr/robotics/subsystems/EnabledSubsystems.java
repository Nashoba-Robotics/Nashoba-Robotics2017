package edu.nr.robotics.subsystems;

public class EnabledSubsystems {
	public static final boolean DRIVE_ENABLED 		= false,
								SHOOTER_ENABLED 	= false,
								HOOD_ENABLED 		= false,
								TURRET_ENABLED 		= true,
								INTAKE_ENABLED 		= false,
								INTAKE_ARM_ENABLED 	= false,
								INTAKE_SLIDE_ENABLED= false,
								GEAR_MOVER_ENABLED 	= false,
								LOADER_ENABLED 		= false,
								AGITATOR_ENABLED 	= false,
								COMPRESSOR_ENABLED 	= false;
	
	//These start the subsystem in dumb mode. Changing them later sometimes ignores this.
	public static final boolean DRIVE_DUMB_ENABLED	 	= false,
								SHOOTER_DUMB_ENABLED 	= false,
								HOOD_DUMB_ENABLED 		= false,
								TURRET_DUMB_ENABLED 	= false;
	
	
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

	public static final boolean AGITATOR_SMARTDASHBOARD_COMPLEX_ENABLED		= false,
								DRIVE_SMARTDASHBOARD_COMPLEX_ENABLED 		= false,
								HOOD_SMARTDASHBOARD_COMPLEX_ENABLED 		= false,
								GEAR_MOVER_SMARTDASHBOARD_COMPLEX_ENABLED 	= false,
								INTAKE_SMARTDASHBOARD_COMPLEX_ENABLED 		= false,
								INTAKEARM_SMARTDASHBOARD_COMPLEX_ENABLED 	= false,
								INTAKESLIDE_SMARTDASHBOARD_COMPLEX_ENABLED 	= false,
								LOADER_SMARTDASHBOARD_COMPLEX_ENABLED 		= false,
								SHOOTER_SMARTDASHBOARD_COMPLEX_ENABLED 		= false,
								TURRET_SMARTDASHBOARD_COMPLEX_ENABLED 		= true;
}
