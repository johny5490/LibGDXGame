package jny.game;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import jny.game.gameObject.Character.Action;
import jny.game.ui.GameMenuStage;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class GameApplication extends ApplicationAdapter{
    private OrthographicCamera camera;
    private Viewport viewport;
    SpriteBatch batch;

    GameWorld gameWorld;
    GameMenuStage gameMenuStage;
    Label hpLabel;
    
    InputMultiplexer multiplexer;
    
    @Override
    public void create() {
    	batch = new SpriteBatch();
    	//camera = new OrthographicCamera(GV.VIRTUAL_WIDTH, GV.VIRTUAL_HEIGHT);
    	camera = new OrthographicCamera();
        camera.setToOrtho(false);
        //映射到螢幕
        viewport = new FitViewport(GV.VIRTUAL_WIDTH, GV.VIRTUAL_HEIGHT, camera);
        
        //viewport.apply();
        gameWorld = new GameWorld(batch, camera);
        
        
    	gameMenuStage = new GameMenuStage();
    	
        //利用InputMultiplexer才可以動態增減InputProcessor
        multiplexer = new InputMultiplexer();
        // 設置輸入
        //Gdx.input.setInputProcessor(uiStage);
        //multiplexer.addProcessor(uiStage);
        Gdx.input.setInputProcessor(multiplexer);
        
    }

    @Override
    public void render() {
    	float delta = Gdx.graphics.getDeltaTime();
    	//不受限於遊戲暫停,永遠都要處理的輸入事件
    	handleInput();
    	//更新邏輯
    	gameWorld.update(delta);
    	//清除畫面
    	ScreenUtils.clear(0, 0f, 0f, 1f);
    	// 更新攝影機
        camera.update();    	
    	batch.setProjectionMatrix(camera.combined);
    	batch.begin();
        gameWorld.render();
        batch.end();
        
        if(GV.PAUSE) {        	
        	gameMenuStage.act(delta);
            gameMenuStage.draw();
        }
        
    }
    
    private void handleInput() {
    	if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {    		
    		GV.PAUSE = !GV.PAUSE;
			if(GV.PAUSE) {
				//暫停才加入選單
				multiplexer.addProcessor(gameMenuStage);
			}else {
				multiplexer.removeProcessor(gameMenuStage);
			}
    	}
	}

	@Override    
    public void resize(int width, int height) {
    	// 保持虛擬解析度不變
        viewport.update(width, height, true);
        gameMenuStage.getViewport().update(width, height, true);
        
    }
    
    
    @Override
    public void dispose() {
    	batch.dispose();
        gameWorld.dispose();
        gameMenuStage.dispose();
    }

}
