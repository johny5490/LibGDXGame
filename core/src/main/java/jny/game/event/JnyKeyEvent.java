package jny.game.event;

import com.badlogic.gdx.Input;

public class JnyKeyEvent implements JnyInputEvent{
	
	public int keyCode;
	
	public JnyKeyEvent(int keyCode) {
		this.keyCode = keyCode;
	}
	
	public boolean isEnterKey() {
		return Input.Keys.ENTER == keyCode;
	}
	
	public boolean isEscKey() {
		return Input.Keys.ESCAPE == keyCode;
	}
}
