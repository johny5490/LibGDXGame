package jny.game.gameObject;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import jny.game.util.Util;

public abstract class GameObject {	
	/**
	 * 當前位置,圖中心在地圖的座標
	 */
	public float x, y;
    public float width = 64f;
    public float height = 64f;  
    /**
     * 遊戲物件是否要銷毀,是的話要移出遊戲物件清單
     */
    public boolean isGone;
    
	public abstract void update(float delta);
	public abstract void render(SpriteBatch batch);	
	public abstract void dispose();
	
	public Rectangle getRectangle() {
		//return new Rectangle(x, y, width, height);
		return new Rectangle(x-width/2, y-height/2, width, height);
	}
	
	/**
	 * 是碰撞
	 * @param obj
	 * @return
	 */
	public boolean isCollided(GameObject obj) {
		return Util.isCollided(getRectangle(), obj.getRectangle());
	}
	
	public void gone() {
		isGone = true;
	}
	
	public void setPosition(float x, float y) {
		this.x=x;
		this.y=y;
	}
}
