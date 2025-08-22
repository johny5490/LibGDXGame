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

import jny.game.util.CharacterAssembler;
import jny.game.util.SpriteSheetLoader;
import jny.game.util.Util;

public abstract class Character {
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
	
	public int nation;
	
	public TextureRegion currentFrame;
	
	public abstract EnumMap<Action, EnumMap<Direction, Animation<TextureRegion>>> createAnimations();
	public abstract void init();
	public abstract void update();
	
	public Character() {
		init();
		animations = createAnimations();
	}
	
    // 設定角色目前狀態
    public void setState(Action action, Direction dir) {
        if (currentAction != action || currentDirection != dir) {
            stateTime = 0f; // 動作切換時重置時間
        }
        currentAction = action;
        currentDirection = dir;
    }
    
	public void setLocation(float x, float y) {
		this.x=x;
		this.y=y;
	}
	
	public void draw(SpriteBatch spriteBatch) {
		/*
		stateTime += Gdx.graphics.getDeltaTime();
		if(animations != null) {
			Animation<TextureRegion> animation = animations.get(currentAction).get(currentDirection);
	        TextureRegion frame = animation.getKeyFrame(stateTime);
	        spriteBatch.draw(frame, x, y);
		}
		if (stateTime > 1000f) { // 超過 1000 秒就歸零
		    stateTime = 0f;
		}
		*/
		spriteBatch.draw(currentFrame, x, y);
	}
	
	protected SpriteSheetLoader genLoader(String path, int textureWidth, int textureHeight) {
		return new SpriteSheetLoader(path, textureWidth, textureHeight);
	}
}
