package jny.game;

import java.util.EnumMap;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import jny.game.Character.Direction;
import jny.game.util.Util;

/**
 * 箭
 */
public class Arrow implements GameObject{
    private float x, y;            // 當前位置
    private float vx, vy;          // 速度向量
    private float speed = 200f;    // 飛行速度（像素/秒）
    private float width = 64f;
    private float height = 64f;    
    /**
     * 箭面對不同方向的圖
     */
    //EnumMap<Direction, TextureRegion> directionTexture;
    TextureRegion textureRegion;
   
    boolean isDead;
    
    float targetX, targetY;
    
    public Arrow(TextureRegion textureRegion, float startX, float startY, float targetX, float targetY) {
        this.x = startX;
        this.y = startY;
        this.targetX = targetX;
        this.targetY = targetY;
        // 計算單位方向向量
        float dx = targetX - startX;
        float dy = targetY - startY;
        float len = (float)Math.sqrt(dx*dx + dy*dy);
        this.vx = dx / len * speed;
        this.vy = dy / len * speed;
        this.textureRegion=textureRegion;
        
        GameObjectManager.getInstance().add(this);
    }
    
    @Override
    public void update(float delta) {        
        //擊中目標或超出視窗要消除(從GameObjStorage移除)...not yet
        //沒考慮目標的移動? 目標的寬高先寫死
        if(Util.rectVsRect(new Rectangle(x, y, width, height), new Rectangle(targetX, targetY, 64, 64))) {
        	isDead = true;        	
        }else {
        	//往目標移動
            x += vx * delta;
            y += vy * delta;
        }
        
    }
    
    @Override
    public void render(SpriteBatch batch) {
        if (!isDead) {        	
            batch.draw(textureRegion, x, y);
        }
    }
    
	@Override
	public boolean isDead() {		
		return isDead;
	}

}

