package edu.nr.lib;

import edu.nr.lib.units.Time;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.buttons.Trigger;
import edu.wpi.first.wpilibj.buttons.Trigger.ButtonScheduler;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;

public class HeldJoystickButton extends JoystickButton {
	
	Time buttonPressTime;
	Time waitTime;
	
	public HeldJoystickButton(GenericHID joystick, int buttonNumber, Time waitTime) {
		super(joystick, buttonNumber);
		this.waitTime = waitTime;
	}
	
	public void initialThenRepeat(Command command) {
		Scheduler.getInstance().addButton(new ButtonScheduler() {

	      private boolean m_pressedLast = get();

	      @Override
	      public void execute() {
	        if (get()) {
	        	boolean firstTimeSincePress = false;
	        	if(!m_pressedLast) {
	        		buttonPressTime = Time.getCurrentTime();
	        		firstTimeSincePress = true;
	        	}
	        	m_pressedLast = true;
	        	if(firstTimeSincePress || Time.getCurrentTime().greaterThan(buttonPressTime.add(waitTime))) {
		  	          System.out.println("Starting command");
	  	          command.start();
	        	}
	        } else {
	          if (m_pressedLast) {
	            m_pressedLast = false;
	            command.cancel();
	          }
	        }
	      }
	    });
	}

}
