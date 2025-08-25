package jny.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface GameObject {
	public abstract void update(float delta);
	public void render(SpriteBatch batch);
	public abstract boolean isDead();
}
