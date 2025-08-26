package jny.game;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import jny.game.util.Util;

public class GameWorld {	
	
	Texture background;
	SpriteBatch batch;
	
	public GameWorld(SpriteBatch batch) {
		this.batch = batch;
		//背景
		background = new Texture("background.png");
		Character char1 = new ArcherCharacter();
        char1.setLocation(50, Gdx.graphics.getHeight()/2);
        
        Character char2 = new ArcherCharacter();
        char2.setLocation(340 , Gdx.graphics.getHeight()/2);
        char2.team=1;
        
        Character char3 = new ArcherCharacter();
        char3.setLocation(150 , Gdx.graphics.getHeight()/2 - 120);
     
       
	}
	
	public void update(float delta) {
		
		GV.gameObjectStorage.update(delta);
		
		//擊中目標或超出視窗要消除(從GameObjStorage移除)...not yet
        //沒考慮目標的移動? 目標的寬高先寫死
		
		for(Arrow arrow : GV.gameObjectStorage.getArrowList()){
			for(Character character : GV.gameObjectStorage.getCharacterList()) {
				if(arrow.team!=character.team && arrow.isCollided(character)) {
					arrow.isGone = true;
				}				
			}
		}
		
	}
	
	/**
	 * 繪製全部物件
	 * @param batch
	 */
	public void render() {
		//畫背景
        batch.draw(
                background,
                0, 0,
                Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight()
            );
		
        GV.gameObjectStorage.render(batch);
        
	}
	
	public void dispose() {       
        background.dispose();
	}
}
