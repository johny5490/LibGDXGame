package jny.game.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;

import jny.game.gameObject.Character.Direction;

import com.badlogic.gdx.graphics.Pixmap;

public class Util {
	
	public static TextureRegion genTextureRegion(String fileName) {
		return new TextureRegion(new Texture(fileName));
	}
	
    /**
     * 從一張 TextureRegion 預先產生「已旋轉」的 TextureRegion
     * @param src
     * @param angleDeg
     * @return
     */
    public static TextureRegion createRotatedRegion(TextureRegion src, float angleDeg) {
        int w = src.getRegionWidth();
        int h = src.getRegionHeight();

        // 計算旋轉後外接框尺寸，避免被裁切
        float rad = angleDeg * MathUtils.degreesToRadians;
        int outW = MathUtils.ceil(Math.abs(w * MathUtils.cos(rad)) + Math.abs(h * MathUtils.sin(rad)));
        int outH = MathUtils.ceil(Math.abs(w * MathUtils.sin(rad)) + Math.abs(h * MathUtils.cos(rad)));

        //if (offscreenBatch == null) offscreenBatch = new SpriteBatch();
        SpriteBatch offscreenBatch = new SpriteBatch();
        FrameBuffer fbo = new FrameBuffer(Pixmap.Format.RGBA8888, outW, outH, false);

        // 綁定 FBO 並清成透明
        fbo.begin();
        Gdx.gl.glViewport(0, 0, outW, outH); // 保險起見，確保視口正確
        Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // 暫存與設定投影矩陣（用 FBO 尺寸作正交投影）
        Matrix4 oldProj = offscreenBatch.getProjectionMatrix().cpy();
        offscreenBatch.setProjectionMatrix(new Matrix4().setToOrtho2D(0, 0, outW, outH));

        // 在 FBO 中把原圖放到中心，然後旋轉
        float drawX = (outW - w) * 0.5f;
        float drawY = (outH - h) * 0.5f;

        offscreenBatch.begin();
        offscreenBatch.draw(
                src,
                drawX, drawY,
                w * 0.5f, h * 0.5f, // 旋轉中心設定在該貼圖中心
                w, h,
                1f, 1f,
                angleDeg
        );
        offscreenBatch.end();

        // ⚠ 一定要在 fbo.end() 之前讀像素，否則會讀到螢幕背板
        Pixmap pm = ScreenUtils.getFrameBufferPixmap(0, 0, outW, outH);

        // 還原投影、解除綁定
        offscreenBatch.setProjectionMatrix(oldProj);
        fbo.end();

        // 垂直翻轉 Pixmap（OpenGL 讀出來是從左下角開始）
        Pixmap flipped = new Pixmap(outW, outH, pm.getFormat());
        for (int y = 0; y < outH; y++) {
            flipped.drawPixmap(pm, 0, y, 0, outH - 1 - y, outW, 1);
        }
        pm.dispose();

        // 建立最終 Texture（與 FBO 壽命無關）
        Texture result = new Texture(flipped);
        result.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear); // 抗鋸齒較順眼
        flipped.dispose();

        // FBO 不再需要可以釋放
        fbo.dispose();

        return new TextureRegion(result);
    }

    public static Direction getFaceTo(float x, float y, float targetX, float targetY) {
    	float dx = targetX - x;
    	float dy = targetY - y;
    	if (Math.abs(dx) > Math.abs(dy)) {
    		return dx > 0 ? Direction.RIGHT : Direction.LEFT;
        } else {
        	return dy > 0 ? Direction.UP : Direction.DOWN;
        }  
    }
    
    /**
     * 矩形碰撞 
     */
    public static boolean isCollided(Rectangle a, Rectangle b) {
        return a.overlaps(b);
    }

    /** 圓形 vs 圓形碰撞 */
    public static boolean circleVsCircle(Circle a, Circle b) {
        float dx = a.x - b.x;
        float dy = a.y - b.y;
        float distance2 = dx * dx + dy * dy;
        float radiusSum = a.radius + b.radius;
        return distance2 <= radiusSum * radiusSum;
    }

    /** 圓形 vs 矩形碰撞 */
    public static boolean circleVsRect(Circle c, Rectangle r) {
        // 找出圓心到矩形最近點
        float closestX = clamp(c.x, r.x, r.x + r.width);
        float closestY = clamp(c.y, r.y, r.y + r.height);

        float dx = c.x - closestX;
        float dy = c.y - closestY;
        return dx * dx + dy * dy <= c.radius * c.radius;
    }

    /** 線段 vs 矩形碰撞 */
    public static boolean lineVsRect(Vector2 start, Vector2 end, Rectangle r) {
        // 檢查線段是否與矩形四條邊相交
        Vector2 topLeft = new Vector2(r.x, r.y + r.height);
        Vector2 topRight = new Vector2(r.x + r.width, r.y + r.height);
        Vector2 bottomLeft = new Vector2(r.x, r.y);
        Vector2 bottomRight = new Vector2(r.x + r.width, r.y);

        return Intersector.intersectSegments(start, end, bottomLeft, bottomRight, null)
                || Intersector.intersectSegments(start, end, bottomLeft, topLeft, null)
                || Intersector.intersectSegments(start, end, topLeft, topRight, null)
                || Intersector.intersectSegments(start, end, topRight, bottomRight, null);
    }
    
    /** 小工具：夾住數值在 min~max */
    private static float clamp(float val, float min, float max) {
        return Math.max(min, Math.min(max, val));
    }
}
