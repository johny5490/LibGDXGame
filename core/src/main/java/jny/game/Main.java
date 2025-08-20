package jny.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    
    private Character character;
    
    private Texture background;
    
    @Override
    public void create() {
        batch = new SpriteBatch();        
        background = new Texture("background.png");
        character = new Character();        
        
    }

    @Override
    public void render() {
    	//更新邏輯
    	character.update();
    	
        ScreenUtils.clear(0, 0f, 0f, 1f);                
        batch.begin();
        //畫背景
        batch.draw(
                background,
                0, 0,
                Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight()
            );
        
        //繪製角色
        character.draw(batch);
        
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        
        background.dispose();
    }
}
