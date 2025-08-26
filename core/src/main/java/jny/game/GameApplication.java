package jny.game;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import jny.game.Character.Action;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class GameApplication extends ApplicationAdapter {
    private OrthographicCamera camera;
    private Viewport viewport;
    SpriteBatch batch;

    GameWorld gameWorld;

  
    @Override
    public void create() {
    	batch = new SpriteBatch();
    	gameWorld = new GameWorld(batch);
    	
        // 建立攝影機與 viewport
        camera = new OrthographicCamera();
        viewport = new FillViewport(GV.VIRTUAL_WIDTH, GV.VIRTUAL_HEIGHT, camera);
        viewport.apply();
        
    }

    @Override
    public void render() {
    	float delta = Gdx.graphics.getDeltaTime();
    	
    	// 更新攝影機
        camera.update();
    	ScreenUtils.clear(0, 0f, 0f, 1f);
        
    	//更新邏輯
    	gameWorld.update(delta);
    	batch.setProjectionMatrix(camera.combined);
    	
    	batch.begin();
        gameWorld.render();
        batch.end();
    }
    
    @Override    
    public void resize(int width, int height) {
    	// 保持虛擬解析度不變
        viewport.update(width, height, true);
    }
    
    
    @Override
    public void dispose() {
    	batch.dispose();
        gameWorld.dispose();
    }
}
