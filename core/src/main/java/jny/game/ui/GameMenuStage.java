package jny.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import jny.game.GV;

public class GameMenuStage extends Stage{

	public GameMenuStage() {
		Table table = new Table();
        table.setFillParent(true);         
        table.center();
        
        CTextButton returnGameButton = new CTextButton("返回遊戲");
        returnGameButton.addListener(new ClickListener() {
        	@Override
        	public void clicked(InputEvent event, float x, float y) {
        		GV.PAUSE = false;
        	}
        });
        
        CTextButton closeButton = new CTextButton("關閉");
        closeButton.addListener(new ClickListener() {
			
			@Override
			public void clicked(InputEvent event, float x, float y) {							
				Gdx.app.exit();
			}
		});	
        
        
        
        table.add(returnGameButton).width(200).pad(10).row();
        table.add(closeButton).width(200).pad(10).row();
        
        
        //table.padBottom(25); // 距離底部 25px
        this.addActor(table);
	}
}
