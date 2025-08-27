package jny.game.gameObject;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import jny.game.GameObjectStorage;
import jny.game.gameObject.Character.Action;
import jny.game.gameObject.Character.Direction;
import jny.game.util.CharacterAssembler;
import jny.game.util.CharacterTextureLoader;
import jny.game.util.SpriteSheetLoader;
import jny.game.util.Util;

/**
 * 弓箭手角色
 */
public class ArcherCharacter extends Character{
	
	private static int TEXTURE_WIDTH = 64;
	private static int TEXTURE_HEIGHT = 64;
	
	private SpriteSheetLoader behindSheetLoader;
	private SpriteSheetLoader walkBodySheetLoader;
	private SpriteSheetLoader feetSheetLoader;
	private SpriteSheetLoader legsSheetLoader;
	private SpriteSheetLoader torsoSheetLoader;
	private SpriteSheetLoader beltSheetLoader;
	private SpriteSheetLoader headSheetLoader;
	//private SpriteSheetLoader handsSheetLoader;
	private SpriteSheetLoader weaponBowSheetLoader;
	private SpriteSheetLoader weaponArrowSheetLoader;
	
	/**
	 * 走路frame速度,值越小越快
	 */
	private float walkDuration;
	
	private float attackDuration;
	
	private float attachRange = 300f;
	//弓箭射擊的方位
	float targetX;
	float targetY;
	
	@Override
	public void init() {
		behindSheetLoader = genLoader("character/walkcycle/BEHIND_quiver.png");
		walkBodySheetLoader = genLoader("character/walkcycle/BODY_male.png");
		feetSheetLoader = genLoader("character/walkcycle/FEET_shoes_brown.png");
		legsSheetLoader = genLoader("character/walkcycle/LEGS_pants_greenish.png");
		torsoSheetLoader = genLoader("character/walkcycle/TORSO_leather_armor_torso.png");
		beltSheetLoader = genLoader("character/walkcycle/BELT_leather.png");
		headSheetLoader = genLoader("character/walkcycle/HEAD_leather_armor_hat.png");
		//handsSheetLoader = genLoader("character/bow/HANDS_plate_armor_gloves.png");
		weaponBowSheetLoader = genLoader("character/bow/WEAPON_bow.png");
		weaponArrowSheetLoader = genLoader("character/bow/WEAPON_arrow.png");
		
		walkDuration = 0.15f;
		attackDuration = 0.13f;
	}
	
	@Override
	public EnumMap<Action, EnumMap<Direction, Animation<TextureRegion>>> createAnimations() {
		EnumMap<Action, EnumMap<Direction, Animation<TextureRegion>>> animations = new EnumMap<>(Action.class);
		animations.put(Action.IDLE, loadIdleBowCharacterTexture());
		animations.put(Action.WALK, loadWalkBowCharacterTexture());
		animations.put(Action.ATTACK, loadAttckBowCharacterTexture());
		return animations;
	}

	@Override
	public void update(float delta) {
		stateTime += delta;
		currentFrame = animations.get(currentAction).get(currentDirection).getKeyFrame(stateTime);
		
		switch (currentAction) {
		case ATTACK:
			//動畫播完
			if(animations.get(currentAction).get(currentDirection).isAnimationFinished(stateTime)) {
				createArrow();				
				setAction(Action.IDLE);
			}
			
			break;
		case IDLE:
			attackEnemyInRange();
			break;
		case WALK:
			moving(delta);
		default:
			break;
		}		
	}
	
	private void createArrow() {
		//向右箭
		TextureRegion rightArrow = weaponArrowSheetLoader.getRegion(3, 12);
		float dx = targetX - x;
		float dy = targetY - y;
		//算出箭頭的角度 (弧度)
		float radians = (float)Math.atan2(dy, dx);
		//弧度換算角度
		new Arrow(Util.createRotatedRegion(rightArrow, (float)Math.toDegrees(radians)), x, y, targetX, targetY, team);
	}

	private void attackEnemyInRange() {
		Character enemyInRange = enemyInNearestAttackRange();
		if(enemyInRange!=null) {
			attack(enemyInRange.x, enemyInRange.y);
		}
	}
	
    public void attack(float targetX, float targetY) {
        this.targetX = targetX;
        this.targetY = targetY;
    	//面向目標
        faceTo(targetX, targetY);    	
    	setAction(Action.ATTACK);
    }
    
	/**
	 * 在攻擊範圍內最近的敵人
	 * @return
	 */
	private Character enemyInNearestAttackRange() {
		//有敵人的最近距離
		float nearestDisatance = 0f;
		//最近距離的敵人
		Character nearestChar = null;
		for(Character c : GameObjectStorage.INSTANCE.getCharacterList()) {			
			if (c != this && c.team != team && inRange(c)) {
				float dx = c.x - x;
				float dy = c.y - y;
				float distance = dx * dx + dy * dy;
				if (distance <= attachRange * attachRange) {
					// 目標在攻擊範圍內
					if (nearestChar == null || distance < nearestDisatance) {
						nearestChar = c;
						nearestDisatance = distance;
					}

				}
			}
		}
		
		return nearestChar;
	}
	
	private boolean inRange(Character c) {
		float dx = c.x - x;
		float dy = c.y-y;
		float distance = dx * dx + dy * dy;
		return distance <= attachRange * attachRange;
	}

	private SpriteSheetLoader genLoader(String path) {
		return super.genLoader(path, TEXTURE_WIDTH, TEXTURE_HEIGHT);		
	}
	
	private EnumMap<Direction, Animation<TextureRegion>> loadWalkBowCharacterTexture() {
		EnumMap<Direction, Animation<TextureRegion>> walkMap = new EnumMap<>(Direction.class);
		walkMap.put(Direction.UP, new Animation<>(walkDuration, createWalkAnimation(0), Animation.PlayMode.LOOP));
		walkMap.put(Direction.LEFT, new Animation<>(walkDuration, createWalkAnimation(1), Animation.PlayMode.LOOP));
		walkMap.put(Direction.DOWN, new Animation<>(walkDuration, createWalkAnimation(2), Animation.PlayMode.LOOP));
		walkMap.put(Direction.RIGHT, new Animation<>(walkDuration, createWalkAnimation(3), Animation.PlayMode.LOOP));
		return walkMap;
	}

	private EnumMap<Direction, Animation<TextureRegion>> loadAttckBowCharacterTexture() {
		EnumMap<Direction, Animation<TextureRegion>> attackMap = new EnumMap<>(Direction.class);
		String[] filePaths = new String[] {"character/bow/BODY_animation.png", 
											"character/bow/FEET_shoes_brown.png",
											"character/bow/LEGS_robe_skirt.png", 
											"character/bow/TORSO_leather_armor_torso.png", 
											"character/bow/BELT_leather.png", 
											"character/bow/HEAD_leather_armor_hat.png",
											"character/bow/WEAPON_arrow.png", 
											"character/bow/WEAPON_bow.png"};
		
		CharacterTextureLoader loader = new CharacterTextureLoader(filePaths, new int[] {1,2,3,4,5,6,7,8,9});
		
		attackMap.put(Direction.UP, createAttackAnimation(loader.getAllRegions(), 0));
		attackMap.put(Direction.LEFT, createAttackAnimation(loader.getAllRegions(), 1));
		attackMap.put(Direction.DOWN, createAttackAnimation(loader.getAllRegions(), 2));
		attackMap.put(Direction.RIGHT, createAttackAnimation(loader.getAllRegions(), 3));
		return attackMap;
	}
		
	private Animation<TextureRegion> createAttackAnimation(List<TextureRegion[][]> regionList, int row) {
		//特定面向的全身連續圖
		Array<TextureRegion> frames = new Array<>();		
		for(int frameIdx=0;frameIdx<regionList.get(0)[row].length;frameIdx++) {
			List<TextureRegion> allPartTextureList = new ArrayList<>();
			for(TextureRegion[][] onePart:regionList) {				
				allPartTextureList.add(onePart[row][frameIdx]);
			}
			frames.add(CharacterAssembler.assemble(allPartTextureList, TEXTURE_WIDTH, TEXTURE_HEIGHT));
		}
		
		return new Animation<>(attackDuration, frames, Animation.PlayMode.NORMAL);		
	}

	private Array<TextureRegion> createWalkAnimation(int row) {
		Array<TextureRegion> textRegions = new Array<>();
		for(int i=1;i<9;i++) {
			textRegions.add(createTexture(row, i));
		}
		return textRegions;
	}

	private EnumMap<Direction, Animation<TextureRegion>> loadIdleBowCharacterTexture() {	
		//IDLE 動畫（可用單幀)
		EnumMap<Direction, Animation<TextureRegion>> idleMap = new EnumMap<>(Direction.class);
		idleMap.put(Direction.UP, makeAnimation(0.1f, createTexture(0, 0)));
		idleMap.put(Direction.LEFT, makeAnimation(0.1f, createTexture(1, 0)));
		idleMap.put(Direction.DOWN, makeAnimation(0.1f, createTexture(2, 0)));
		idleMap.put(Direction.RIGHT, makeAnimation(0.1f, createTexture(3, 0)));
		return idleMap;
	}
	
	/**
	 * 組出特定面向的角色全圖,專門走路用，沒拿武器，因為沒有拿弓箭走路的圖
	 * 組合骨架、武器、裝備,繪圖需要一定順序
	 *	背後物件 (BEHIND)
	 *  全身 (BODY)
	 *  腳部 (FEET)
	 *  腿部 (LEGS)
	 *  胸甲 (TORSO)
	 *  腰帶 (BELT)
	 *  頭部 (HEAD)
	 *  手套 (HANDS)
	 *  武器 (WEAPON)
	 * @return
	 */
	private TextureRegion createTexture(int row, int column) {
		List<TextureRegion> allPartTextureList = new ArrayList<>();		
		allPartTextureList.add(behindSheetLoader.getRegion(row, column));
		allPartTextureList.add(walkBodySheetLoader.getRegion(row, column));
		allPartTextureList.add(feetSheetLoader.getRegion(row, column));
		allPartTextureList.add(legsSheetLoader.getRegion(row, column));
		allPartTextureList.add(torsoSheetLoader.getRegion(row, column));
		allPartTextureList.add(beltSheetLoader.getRegion(row, column));
		allPartTextureList.add(headSheetLoader.getRegion(row, column));		
		//allPartTextureList.add(weaponArrowSheetLoader.getRegion(row, column));
		//allPartTextureList.add(weaponBowSheetLoader.getRegion(row, column));
		//allPartTextureList.add(handsSheetLoader.getRegion(row, column));
				
		TextureRegion assembledTexture = CharacterAssembler.assemble(allPartTextureList, TEXTURE_WIDTH, TEXTURE_HEIGHT);
		return assembledTexture;
	}
	
    private Animation<TextureRegion> makeAnimation(float frameDuration, TextureRegion textRegion) {
    	Array<TextureRegion> textRegions = new Array<>();
    	textRegions.add(textRegion);
    	return new Animation<>(frameDuration, textRegions, Animation.PlayMode.LOOP);
    }

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
