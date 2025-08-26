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
public class Arrow extends GameObject{    
    private float vx, vy;          // 速度向量
    private float speed = 200f;    // 飛行速度（像素/秒）
  
    TextureRegion textureRegion;
   
    float targetX, targetY;
    int team;
    
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
        GV.gameObjectStorage.addArrow(this);
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
            batch.draw(textureRegion, x, y);
        }
    }
    

}

