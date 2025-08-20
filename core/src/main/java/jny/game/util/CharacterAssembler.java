package jny.game.util;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.Gdx;

import java.util.List;

public class CharacterAssembler {

    /**
     * 將多個 TextureRegion 疊合成一張新的獨立 TextureRegion
     * @param textureRegions 各部位貼圖，依照疊加順序排列（底 → 上）
     * @param width 角色圖寬度
     * @param height 角色圖高度
     * @return 合成後的 TextureRegion（獨立貼圖，不受 FBO 影響）
     */
    public static TextureRegion assemble(List<TextureRegion> textureRegions, int width, int height) {

        // 建立 FBO
        FrameBuffer fbo = new FrameBuffer(Pixmap.Format.RGBA8888, width, height, false);
        fbo.begin();

        // 清空背景透明
        Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // 設定正交攝影機對齊 FBO
        OrthographicCamera camera = new OrthographicCamera(width, height);
        camera.setToOrtho(false, width, height);

        SpriteBatch batch = new SpriteBatch();
        batch.setProjectionMatrix(camera.combined);

        // 繪製各層
        batch.begin();
        for (TextureRegion region : textureRegions) {
            batch.draw(region, 0, 0, width, height);
        }
        batch.end();

        fbo.end();

        // 拷貝 FBO 的 ColorBufferTexture 到新的 Texture
        Texture fboTexture = fbo.getColorBufferTexture();

        TextureRegion tempRegion = new TextureRegion(fboTexture);

        // 創建新的 Texture：直接使用 SpriteBatch 畫到 Pixmap
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        FrameBuffer tmpFbo = new FrameBuffer(Pixmap.Format.RGBA8888, width, height, false);
        tmpFbo.begin();

        batch.begin();
        batch.draw(tempRegion, 0, 0, width, height);
        batch.end();

        Pixmap finalPixmap = com.badlogic.gdx.utils.ScreenUtils.getFrameBufferPixmap(0, 0, width, height);
        tmpFbo.end();

        Texture resultTexture = new Texture(finalPixmap);
        finalPixmap.dispose();
        pixmap.dispose();
        tmpFbo.dispose();
        batch.dispose();
        fbo.dispose();

        return new TextureRegion(resultTexture);
    }
}
