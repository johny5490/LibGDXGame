package jny.game;

import java.util.ArrayList;
import java.util.List;

public class GameObjStorage {

	public List<Character> characterList = new ArrayList<>();
	
	private static GameObjStorage gameObj;
	
	private GameObjStorage() {
		
	}
	
	public static GameObjStorage getInstance() {
		if(gameObj==null) {
			gameObj = new GameObjStorage();
		}
		return gameObj;
	}
	
	
}
