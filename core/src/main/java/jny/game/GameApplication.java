package jny.game;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
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

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class GameApplication extends ApplicationAdapter{
    private OrthographicCamera camera;
    private Viewport viewport;
    SpriteBatch batch;

    GameWorld gameWorld;
    Stage uiStage;
    Label hpLabel;
    TextButton button;
    int uiHeight=100;
    Viewport uiViewport;
    OrthographicCamera uiCamera;
    
    @Override
    public void create() {
    	batch = new SpriteBatch();
    	//camera = new OrthographicCamera(GV.VIRTUAL_WIDTH, GV.VIRTUAL_HEIGHT);
    	camera = new OrthographicCamera();
        camera.setToOrtho(false);
        //映射到螢幕
        viewport = new FitViewport(GV.VIRTUAL_WIDTH, GV.VIRTUAL_HEIGHT-uiHeight, camera);
        
        //viewport.apply();
        gameWorld = new GameWorld(batch, camera);
        
        uiCamera = new OrthographicCamera();
        uiViewport = new ScreenViewport(uiCamera);
        
    	
    	
    	uiStage = new Stage(uiViewport);
    	// --- FreeTypeFontGenerator 生成字型 ---
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font/msjh.ttc"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 24;
        parameter.characters = "點擊";
        BitmapFont font = generator.generateFont(parameter);
        generator.dispose();

        // --- 按鈕樣式 ---
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();

        // 用 Pixmap 生成簡單灰色背景
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.GRAY);
        pixmap.fill();
        Texture texture = new Texture(pixmap);
        pixmap.dispose();

        style.up = new TextureRegionDrawable(new TextureRegion(texture));
        style.down = new TextureRegionDrawable(new TextureRegion(texture));
        style.font = font;

        button = new TextButton("點擊", style);
        button.setSize(150, 50);
        button.addListener(new ClickListener() {
			
			@Override
			public void clicked(InputEvent event, float x, float y) {
				System.out.println("click");
			}
		});
        
        Table table = new Table();
        table.setFillParent(true);
        table.bottom();           // Table內容放在底部
        table.add(button).padBottom(25); // 距離底部 25px

        uiStage.addActor(table);

        // 設置輸入
        Gdx.input.setInputProcessor(uiStage);
        
    }

    @Override
    public void render() {
    	float delta = Gdx.graphics.getDeltaTime();
    	//處理輸入（滑鼠/鍵盤)
    	gameWorld.handleInput();
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
        
        // --- UI 畫面 (固定在螢幕下方) ---
        uiStage.act(delta);
        uiStage.draw();
    }
    
    @Override    
    public void resize(int width, int height) {
    	// 保持虛擬解析度不變
        viewport.update(width, height, true);
        uiStage.getViewport().update(width, height, true);
        
        uiViewport.update(width, uiHeight, true);      // 下面 100
        uiViewport.setScreenY(0);                 // UI 固定在最下方
        uiViewport.setScreenHeight(uiHeight);          // 高度固定 100
    }
    
    
    @Override
    public void dispose() {
    	batch.dispose();
        gameWorld.dispose();
        uiStage.dispose();
    }

}
