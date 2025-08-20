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

public class Character {
    // 動作種類
    public enum Action { IDLE, WALK, ATTACK }
    // 方向（這裡先用4方向，左邊用翻轉）
    public enum Direction { UP, DOWN, LEFT, RIGHT }
	
    float x, y;
	private float stateTime = 0f;
    private Action currentAction = Action.IDLE;
	/**
	 * 目前的面向
	 */
	Direction currentDirection = Direction.DOWN;			
	/**
	 * 動畫
	 */
	EnumMap<Action, EnumMap<Direction, Animation<TextureRegion>>> animations = new EnumMap<>(Action.class);
	int width = 64; 
	int height = 64;
	
	public Character() {
		/*
		// === WALK 動畫 ===
        EnumMap<Direction, Animation<TextureRegion>> walkMap = new EnumMap<>(Direction.class);
        walkMap.put(Direction.DOWN,  makeAnimation(atlas, "walk_down", 4, 0.15f));
        walkMap.put(Direction.UP,    makeAnimation(atlas, "walk_up",   4, 0.15f));
        walkMap.put(Direction.RIGHT, makeAnimation(atlas, "walk_right",4, 0.15f));
        // LEFT 用 RIGHT 翻轉
        walkMap.put(Direction.LEFT,  flipAnimation(walkMap.get(Direction.RIGHT)));

        animations.put(Action.WALK, walkMap);

        // === ATTACK 動畫 ===
        EnumMap<Direction, Animation<TextureRegion>> atkMap = new EnumMap<>(Direction.class);
        atkMap.put(Direction.DOWN,  makeAnimation(atlas, "atk_down", 3, 0.1f));
        atkMap.put(Direction.UP,    makeAnimation(atlas, "atk_up",   3, 0.1f));
        atkMap.put(Direction.RIGHT, makeAnimation(atlas, "atk_right",3, 0.1f));
        atkMap.put(Direction.LEFT,  flipAnimation(atkMap.get(Direction.RIGHT)));

        animations.put(Action.ATTACK, atkMap);
		
        */
		
		animations.put(Action.IDLE, loadIdleBowCharacterTexture());
	}
	
	private EnumMap<Direction, Animation<TextureRegion>>  loadIdleBowCharacterTexture() {
		SpriteSheetLoader bodySheetLoader = new SpriteSheetLoader("character/walkcycle/BODY_male.png", width, height);
		SpriteSheetLoader weaponBowSheetLoader = new SpriteSheetLoader("character/bow/WEAPON_bow.png", width, height);
		SpriteSheetLoader weaponArrowSheetLoader = new SpriteSheetLoader("character/bow/WEAPON_arrow.png", width, height);
		
		/* 組合骨架、武器、裝備,取決於圖需要一定順序
		* 	武器 (WEAPON)	
		*   雙手 (HANDS)
		*   頭部 (HEAD)
		*   腰帶 (BELT)
			身體 (TORSO)
			腿部 (LEGS)
			腳部 (FEET)
			身體 (BODY)
			背後物件 (BEHIND)
		*/		
		List<TextureRegion> charUpRegions = new ArrayList<>();
		charUpRegions.add(weaponArrowSheetLoader.getRegion(0, 0));
		charUpRegions.add(weaponBowSheetLoader.getRegion(0, 0));
		charUpRegions.add(bodySheetLoader.getRegion(0, 0));
		
		TextureRegion charUp = CharacterAssembler.assemble(charUpRegions, width, height);
		
		List<TextureRegion> charDownRegions = new ArrayList<>();
		charUpRegions.add(weaponArrowSheetLoader.getRegion(2, 0));
		charDownRegions.add(weaponBowSheetLoader.getRegion(2, 0));
		charDownRegions.add(bodySheetLoader.getRegion(2, 0));
		TextureRegion charDown = CharacterAssembler.assemble(charDownRegions, width, height);
		
		// === IDLE 動畫（可用單幀） ===
		EnumMap<Direction, Animation<TextureRegion>> idleMap = new EnumMap<>(Direction.class);
		idleMap.put(Direction.UP, makeAnimation(0.1f, charUp));
		idleMap.put(Direction.LEFT, makeAnimation(0.1f, bodySheetLoader.getRegion(1, 0)));
		idleMap.put(Direction.DOWN, makeAnimation(0.1f, charDown));
		idleMap.put(Direction.RIGHT, makeAnimation(0.1f, bodySheetLoader.getRegion(3, 0)));
		return idleMap;
	}
	
    // 設定角色目前狀態
    public void setState(Action action, Direction dir) {
        if (currentAction != action || currentDirection != dir) {
            stateTime = 0f; // 動作切換時重置時間
        }
        currentAction = action;
        currentDirection = dir;
    }
    
    private Animation<TextureRegion> makeAnimation(float frameDuration, TextureRegion textRegion) {
    	Array<TextureRegion> frames = new Array<>();
    	frames.add(textRegion);
    	return new Animation<>(frameDuration, frames, Animation.PlayMode.LOOP);
    }
    
    // 產生一個 Animation
    private Animation<TextureRegion> makeAnimation(TextureAtlas atlas, String prefix, int frameCount, float frameDuration) {
        Array<TextureRegion> frames = new Array<>();
        for (int i = 1; i <= frameCount; i++) {
            frames.add(atlas.findRegion(prefix + "_" + i)); // e.g. walk_down_1, walk_down_2...
        }
        return new Animation<>(frameDuration, frames, Animation.PlayMode.LOOP);
    }
    
	public void update() {
		
	}
	
	public void setLocation(float x, float y) {
		this.x=x;
		this.y=y;
	}
	
	public void draw(SpriteBatch spriteBatch) {
		stateTime += Gdx.graphics.getDeltaTime();
        Animation<TextureRegion> animation = animations.get(currentAction).get(currentDirection);
        TextureRegion frame = animation.getKeyFrame(stateTime);
        spriteBatch.draw(frame, x, y);
    
	}
	
	
}
