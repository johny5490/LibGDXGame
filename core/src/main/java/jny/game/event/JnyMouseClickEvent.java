package jny.game.event;

import com.badlogic.gdx.Input;

public class JnyMouseClickEvent implements JnyInputEvent{
	
	public int x, y, button;
	
	public JnyMouseClickEvent(int x, int y, int button) {
		this.x=x;
		this.y=y;
		this.button = button;
	}
	
	public boolean isLeftPressed() {
		return Input.Buttons.LEFT == button;
	}
	
	public boolean isRightPressed() {
		return Input.Buttons.RIGHT == button;
	}
}
