package jny.game;


import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import jny.game.Character.Direction;
import jny.game.util.CharacterAssembler;
import jny.game.util.SpriteSheetLoader;
import jny.game.util.Util;

/**
 * 角色
 */
public abstract class Character implements GameObject{
    // 動作種類
    public enum Action { IDLE, WALK, ATTACK }
    // 方向（這裡先用4方向）
    public enum Direction { UP, DOWN, LEFT, RIGHT }
	
    public float x, y;
	public float stateTime = 0f;
    public Action currentAction = Action.IDLE;
	/**
	 * 目前的面向
	 */
    public Direction currentDirection = Direction.DOWN;			
	/**
	 * 動畫
	 */
    public EnumMap<Action, EnumMap<Direction, Animation<TextureRegion>>> animations;
	int width = 64; 
	int height = 64;
	
	public int team;
	
	public TextureRegion currentFrame;
	
	public abstract EnumMap<Action, EnumMap<Direction, Animation<TextureRegion>>> createAnimations();
	public abstract void init();
	
	
	public Character() {
		init();
		animations = createAnimations();
		GameObjectManager.getInstance().gameObjList.add(this);
	}
	
    /**
     * 設定角色目前動作
     * @param action
     */
    public void setAction(Action action) {
        if (currentAction != action) {
            stateTime = 0f; // 動作切換時重置時間
        }
        currentAction = action;        
    }
    
	public void setLocation(float x, float y) {
		this.x=x;
		this.y=y;
	}
	
	public void render(SpriteBatch spriteBatch) {		
		spriteBatch.draw(currentFrame, x, y);
	}
	
	protected SpriteSheetLoader genLoader(String path, int textureWidth, int textureHeight) {
		return new SpriteSheetLoader(path, textureWidth, textureHeight);
	}
    
    /**
     * 面向目標
     * @param targetX
     * @param targetY
     */
    public void faceTo(float targetX, float targetY) {
    	currentDirection = Util.getFaceTo(x, y, targetX, targetY);    	   
    }
}
