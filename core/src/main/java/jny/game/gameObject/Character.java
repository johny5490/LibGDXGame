package jny.game.gameObject;


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

import jny.game.GameObjectStorage;
import jny.game.gameObject.Character.Direction;
import jny.game.util.CharacterAssembler;
import jny.game.util.SpriteSheetLoader;
import jny.game.util.Util;

/**
 * 角色
 */
public abstract class Character extends GameObject{
    // 動作種類
    public enum Action { IDLE, WALK, ATTACK }
    // 方向（這裡先用4方向）
    public enum Direction { UP, DOWN, LEFT, RIGHT }
	
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

	public int team;
	
	public TextureRegion currentFrame;
	//移動的目的地
	public float moveTargetX;
	public float moveTargetY;
	//移動速度
	public float speed = 100f;
	//移動目的指示
	TargetIndicator indicator;
	
	public abstract EnumMap<Action, EnumMap<Direction, Animation<TextureRegion>>> createAnimations();
	public abstract void init();
	
	
	public Character() {
		init();
		animations = createAnimations();
		GameObjectStorage.INSTANCE.addCharacter(this);
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
	
	public void render(SpriteBatch batch) {
		if(currentFrame!=null) {
			//batch.draw(currentFrame, x, y);
			batch.draw(currentFrame, x-width/2, y-height/2);
		}		
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
    
    /**
     * 往某地點移動
     * @param targetX
     * @param targetY
     */
    public void moveTo(float targetX, float targetY) {
    	faceTo(targetX, targetY);
    	setAction(Action.WALK);
    	moveTargetX = targetX;
    	moveTargetY = targetY;
    	if(indicator != null) {
    		indicator.setPosition(moveTargetX, moveTargetY);
    	}else {
    		indicator = new TargetIndicator(moveTargetX, moveTargetY);
    	}
    	
    }
    
    /**
     * 移動,到目的地就發呆
     * @param delta
     */
    public void moving(float delta) {
    	float dx = moveTargetX - x;
        float dy = moveTargetY - y;
        float distance = (float)Math.sqrt(dx * dx + dy * dy);

        if (distance > 1f) { // 避免抖動
            float vx = dx / distance * speed;
            float vy = dy / distance * speed;
            x += vx * delta;
            y += vy * delta;
        }else {
        	//到目的地
        	setAction(Action.IDLE);
        	moveTargetX = x;
        	moveTargetY = y;
        	if(indicator!=null) {
        		indicator.gone();
        		indicator=null;
        	}        	
        }
    }
}
