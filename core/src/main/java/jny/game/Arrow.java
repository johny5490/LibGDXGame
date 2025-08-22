package jny.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Arrow {
    private float x, y;            // 當前位置
    private float vx, vy;          // 速度向量
    private float speed = 400f;    // 飛行速度（像素/秒）
    private boolean active = true; // 是否還在飛行

    private TextureRegion texture; // 箭的圖

    public Arrow(float startX, float startY, float targetX, float targetY, TextureRegion texture) {
        this.x = startX;
        this.y = startY;
        this.texture = texture;

        // 計算單位方向向量
        float dx = targetX - startX;
        float dy = targetY - startY;
        float len = (float)Math.sqrt(dx*dx + dy*dy);
        this.vx = dx / len * speed;
        this.vy = dy / len * speed;
    }

    public void update(float delta) {
        if (!active) return;
        x += vx * delta;
        y += vy * delta;
    }

    public void draw(SpriteBatch batch) {
        if (active) {
            batch.draw(texture, x, y);
        }
    }

    public boolean checkHit(float targetX, float targetY, float radius) {
        float dx = targetX - x;
        float dy = targetY - y;
        if (dx*dx + dy*dy <= radius*radius) {
            active = false; // 命中 → 箭消失
            return true;
        }
        return false;
    }

    public boolean isActive() {
        return active;
    }
}

