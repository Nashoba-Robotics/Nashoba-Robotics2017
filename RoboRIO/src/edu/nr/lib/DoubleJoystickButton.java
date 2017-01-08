package edu.nr.lib;

import edu.wpi.first.wpilibj.buttons.Button;

public class DoubleJoystickButton extends Button {

	Button m_buttonNumberOne;
	Button m_buttonNumberTwo;
	
	Type type;
	
	public enum Type {
		And, Or
	}
	
	public DoubleJoystickButton(Button buttonNumberOne, Button buttonNumberTwo, Type type) {
		m_buttonNumberOne = buttonNumberOne;
		m_buttonNumberTwo = buttonNumberTwo;
		this.type = type;
	}
	
	@Override
	public boolean get() {
		if(type == Type.And) {
			return m_buttonNumberOne.get() && m_buttonNumberTwo.get();
		} else {
			return m_buttonNumberOne.get() || m_buttonNumberTwo.get();
		}
	}
}
