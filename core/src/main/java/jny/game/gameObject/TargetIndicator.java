package jny.game.gameObject;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import jny.game.GameObjectStorage;

/**
 * 移動目的指示,以圓圈表示
 */
public class TargetIndicator extends GameObject{
	
	int radius = 15;

	Texture circleTexture;
	Pixmap pixmap = new Pixmap((int)width, (int)height, Pixmap.Format.RGBA8888);
	
	public TargetIndicator(float x, float y) {
		this.x=x;
		this.y=y;
		
		GameObjectStorage.INSTANCE.addTargetIndicator(this);
		//藍色
		pixmap.setColor(0, 0, 1, 1); 
		pixmap.drawCircle((int)width/2, (int)height/2, radius);
		//多畫一圈讓線條粗一點
		pixmap.drawCircle((int)width/2, (int)height/2, radius-1);
		circleTexture = new Texture(pixmap);
	}
	
	@Override
	public void update(float delta) {
		
	}

	@Override
	public void render(SpriteBatch batch) {		
		//batch.draw(circleTexture, x, y);
		batch.draw(circleTexture, x-width/2, y-height/2);
	}

	@Override
	public void dispose() {
		pixmap.dispose();		
	}

}
