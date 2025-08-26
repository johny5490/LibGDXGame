package jny.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import jny.game.util.Util;

public abstract class GameObject {
	public float x, y; // 當前位置
    public float width = 64f;
    public float height = 64f;  
    /**
     * 遊戲物件是否要銷毀,是的話要移出遊戲物件清單
     */
    public boolean isGone;
    
	public abstract void update(float delta);
	public abstract void render(SpriteBatch batch);	
	
	public Rectangle getRectangle() {
		return new Rectangle(x, y, width, height);
	}
	
	/**
	 * 是碰撞
	 * @param obj
	 * @return
	 */
	public boolean isCollided(GameObject obj) {
		return Util.isCollided(getRectangle(), obj.getRectangle());
	}
}
