package jny.game.gameObject;

import java.util.EnumMap;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import jny.game.GameObjectStorage;
import jny.game.gameObject.Character.Direction;
import jny.game.util.Util;

/**
 * 箭
 */
public class Arrow extends GameObject{    
    private float vx, vy;          // 速度向量
    /**
     * 飛行速度（像素/秒）
     */
    private float speed = 300f;  
    /**
     * 箭的圖
     */
    TextureRegion textureRegion;
   
    float targetX, targetY;
    /**
     * 隊伍,用來區分遊戲物件彼此是否敵對
     */
    public int team;
    
    public Arrow(TextureRegion textureRegion, float startX, float startY, float targetX, float targetY, int team) {
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
        this.team=team;
        GameObjectStorage.INSTANCE.addArrow(this);
    }
    
    @Override
    public void update(float delta) {
    	//往目標移動
        x += vx * delta;
        y += vy * delta;        
    }
    
    @Override
    public void render(SpriteBatch batch) {
        if (!isGone) {        	
            //batch.draw(textureRegion, x, y);
        	batch.draw(textureRegion, x-width/2, y-height/2);
        }
    }

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}

