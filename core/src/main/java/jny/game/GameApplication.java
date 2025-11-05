package jny.game;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Queue;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import jny.game.event.JnyInputEvent;
import jny.game.event.JnyKeyEvent;
import jny.game.event.JnyMouseClickEvent;
import jny.game.ui.PauseMenuStage;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class GameApplication extends ApplicationAdapter{
    private OrthographicCamera camera;
    private Viewport viewport;
    SpriteBatch batch;

    GameWorld gameWorld;
    PauseMenuStage pauseMenuStage;
    
    
    private InputMultiplexer multiplexer;
    
    Queue<JnyInputEvent> inputEventQueue = new ArrayDeque<>();
    
    @Override
    public void create() {
    	batch = new SpriteBatch();
    	//camera = new OrthographicCamera(GV.VIRTUAL_WIDTH, GV.VIRTUAL_HEIGHT);
    	camera = new OrthographicCamera();
        camera.setToOrtho(false);
        //映射到螢幕
        viewport = new FitViewport(GV.VIRTUAL_WIDTH, GV.VIRTUAL_HEIGHT, camera);
        
        //利用InputMultiplexer才可以動態增減InputProcessor
        multiplexer = new InputMultiplexer();
        
        //viewport.apply();
        gameWorld = new GameWorld(batch, camera, inputEventQueue);
        
        
    	pauseMenuStage = new PauseMenuStage();
    
        multiplexer.addProcessor(new InputAdapter() {
        	@Override
        	public boolean keyDown(int keycode) {
        		if(Input.Keys.ESCAPE==keycode) {        			
        			inputEventQueue.add(new JnyKeyEvent(keycode));
        		}
        		return true;
        	}
        	/*
        	@Override
        	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        		inputEventQueue.add(new JnyMouseClickEvent(screenX, screenY, button));
        		return true;
        	}
        	*/
        });
        Gdx.input.setInputProcessor(multiplexer);
        
    }

    @Override
    public void render() {
    	float delta = Gdx.graphics.getDeltaTime();
    	//不受限於遊戲暫停,永遠都要處理的輸入事件
    	handleInputEvent();
    	
    	if(!GV.PAUSE) {			
			//更新邏輯
	    	gameWorld.update(delta);
		}
    	
    	
    	//清除畫面
    	ScreenUtils.clear(0, 0f, 0f, 1f);
    	// 更新攝影機
        camera.update();    	
    	batch.setProjectionMatrix(camera.combined);
    	batch.begin();
        gameWorld.render();
        batch.end();
        
        if(GV.PAUSE) {
        	//這個不知道用意,先不用
        	//pauseMenuStage.act(delta);
            pauseMenuStage.draw();
        }
        
    }
    
    private void handleInputEvent() {
    	
    	while(!inputEventQueue.isEmpty()) {
    		JnyInputEvent event = inputEventQueue.poll();
    		if(event instanceof JnyKeyEvent) {
    			JnyKeyEvent keyEvent = (JnyKeyEvent) event; 
    			if(keyEvent.isEscKey()) {
    				GV.PAUSE = !GV.PAUSE;
    				if(GV.PAUSE) {
    					//暫停才加入選單
    					multiplexer.addProcessor(pauseMenuStage);
    				}else {
    					multiplexer.removeProcessor(pauseMenuStage);
    				}
    			}
    		}
    	}
    	
	}

	@Override    
    public void resize(int width, int height) {
    	// 保持虛擬解析度不變
        viewport.update(width, height, true);
        pauseMenuStage.getViewport().update(width, height, true);
        
    }
    
    
    @Override
    public void dispose() {
    	batch.dispose();
        gameWorld.dispose();
        pauseMenuStage.dispose();
    }

}
