package jny.game;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

import jny.game.gameObject.ArcherCharacter;
import jny.game.gameObject.Arrow;
import jny.game.gameObject.Character;
import jny.game.util.Util;

public class GameWorld {	
	//背景
	Texture background;
	SpriteBatch batch;
	OrthographicCamera camera;	
	/**
	 * 玩家操控的角色
	 */
	Character playerChar;
	int mapWidth = 1000, mapHeight = 1000;
	
	public GameWorld(SpriteBatch batch, OrthographicCamera camera) {
		this.batch = batch;
		this.camera = camera;
		
		background = new Texture("background.png");
		Character char1 = new ArcherCharacter();
        char1.setLocation(50, Gdx.graphics.getHeight()/2);
        
        playerChar = new ArcherCharacter();
        playerChar.setLocation(340 , Gdx.graphics.getHeight()/2);
        playerChar.team=1;
        
        Character char3 = new ArcherCharacter();
        char3.setLocation(150 , Gdx.graphics.getHeight()/2 - 120);
     
       
	}
	
	public void handleInput() {
		if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
			
			Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
			//轉換成遊戲世界座標
			camera.unproject(touchPos);
			playerChar.moveTo(touchPos.x, touchPos.y);
		}
	}
	
	public void update(float delta) {
		camera.position.set(playerChar.x, playerChar.y, 0);
		// 限制攝影機不要超出地圖邊界
        camera.position.x = MathUtils.clamp(camera.position.x, camera.viewportWidth / 2f, mapWidth - camera.viewportWidth / 2f);
        camera.position.y = MathUtils.clamp(camera.position.y, camera.viewportHeight / 2f, mapHeight - camera.viewportHeight / 2f);
        
		GameObjectStorage.INSTANCE.update(delta);
		
		//箭碰到敵人,箭消失,敵人扣血,超出範圍箭消失(not yet)
		for(Arrow arrow : GameObjectStorage.INSTANCE.getArrowList()){
			for(Character character : GameObjectStorage.INSTANCE.getCharacterList()) {
				if((arrow.team!=character.team && arrow.isCollided(character)) || 
						isOutsideMap(arrow)) {
					arrow.isGone = true;
				}				
			}
		}
		
	}
	
	private boolean isOutsideMap(Arrow arrow) {
		return arrow.x>mapWidth || arrow.y<0;		
	}

	/**
	 * 繪製全部物件
	 * @param batch
	 */
	public void render() {
		//畫背景
		/*
        batch.draw(
                background,
                0, 0,
                Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight()
            );
		*/
		batch.draw(background, 0, 0);
        GameObjectStorage.INSTANCE.render(batch);
        
	}
	
	public void dispose() {       
        background.dispose();
	}
}
