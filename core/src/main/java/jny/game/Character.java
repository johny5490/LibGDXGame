package jny.game;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import jny.game.util.Util;

public class Character {
	/**
	 * 位置
	 */
	public float x, y;
	public float width = 150, height = 100;
	/**
	 * 速度,每秒移動 200 像素
	 */
	public float speed = 200;	
	/**
	 * 目前的面向
	 */
	FaceTo faceTo = FaceTo.DOWN;	
	/**
	 * 所有面向的圖
	 */
	Map<FaceTo, TextureRegion> faceToTextureMap = new HashMap<>();
	
	Animation<TextureRegion> attackAnimation;
	TextureRegion region;
	
	public Character() {
		Texture faceUpTexture = new Texture("face_up.png");
		faceToTextureMap.put(FaceTo.UP, new TextureRegion(faceUpTexture));
		
		faceToTextureMap.put(FaceTo.UP_RIGHT, Util.createRotatedRegion(new TextureRegion(faceUpTexture), 45));
		
		region = Util.createRotatedRegion(new TextureRegion(new Texture("face_up.png")), -45);
		
	}
	
	public void update() {
		/*
		//上一幀（frame）到現在經過的時間(秒)
		float delta = Gdx.graphics.getDeltaTime();
		x += speed*delta;
		if(x+width > Gdx.graphics.getWidth()) {
			x=0;
		}
		*/
	}
	
	public void render(SpriteBatch spriteBatch) {
		
		spriteBatch.draw(region, x, y);
		
		//TextureRegion region = new TextureRegion(texture);
		// 水平翻轉，讓角色面向左
		//region.flip(true, false);
		//spriteBatch.draw(region, x, y, width , height);
		/*
		spriteBatch.draw(
			    region,       // 圖片
			    x, y,         // 左下角座標
			    width/2, height/2, 		// 旋轉中心，通常是角色中心
			    width, height,
			    1, 1,
			    45            // 旋轉角度（順時針，單位：度）
			);
		*/
		
		
	}
	
	
	
	public void dispoise() {
		//Texture要dispose ... not yet
	}
}
