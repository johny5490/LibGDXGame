package jny.game;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;

import jny.game.Character.Action;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    
    //public List<Character> characterList = new ArrayList<>();
    
    private Texture background;
    
    @Override
    public void create() {
    	
    	batch = new SpriteBatch();        
        background = new Texture("background.png");
        
        Character char1 = new ArcherCharacter();
        char1.setLocation(50, Gdx.graphics.getHeight()/2);
        
        Character char2 = new ArcherCharacter();
        char2.setLocation(340 , Gdx.graphics.getHeight()/2);
        char2.team=1;
        
        Character char3 = new ArcherCharacter();
        char3.setLocation(150 , Gdx.graphics.getHeight()/2 - 120);
        
        /*
        Character char4 = new ArcherCharacter();
        char4.setLocation(180 , Gdx.graphics.getHeight()/2 - 60);
        char4.team=1;
        */
    }

    private Character genCharacter() {
    	Character character = new ArcherCharacter();
    	
    	return character;
    }
    
    @Override
    public void render() {
    	float delta = Gdx.graphics.getDeltaTime();
    	//更新邏輯
    	GameObjectManager.getInstance().update(delta);
    	
        ScreenUtils.clear(0, 0f, 0f, 1f);                
        batch.begin();
        //畫背景
        batch.draw(
                background,
                0, 0,
                Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight()
            );
        
        //繪製       
        GameObjectManager.getInstance().render(batch);
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();        
        background.dispose();
    }
}
