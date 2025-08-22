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

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    
    //public List<Character> characterList = new ArrayList<>();
    
    private Texture background;
    
    @Override
    public void create() {
    	
    	batch = new SpriteBatch();        
        background = new Texture("background.png");
        for(int i=0;i<10;i++) {
        	Character character = new ArcherCharacter();
            //初始位置
            character.setLocation(i*(2 + character.width) , Gdx.graphics.getHeight()/2);
            GameObjStorage.getInstance().characterList.add(character);
        }
        
    }

    @Override
    public void render() {
    	//更新邏輯
    	 for(Character character:GameObjStorage.getInstance().characterList) {
    		 character.update();
    	 }
    	
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
        for(Character character:GameObjStorage.getInstance().characterList ) {
        	character.draw(batch);
        }
        
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();        
        background.dispose();
    }
}
