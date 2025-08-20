package jny.game.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.Pixmap;

public class Util {
	
	public static TextureRegion genTextureRegion(String fileName) {
		return new TextureRegion(new Texture(fileName));
	}
	
    /**
     * 從一張 TextureRegion 預先產生「已旋轉」的 TextureRegion（不再需要在 draw 時旋轉）
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

}
