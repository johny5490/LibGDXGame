package jny.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * 可用中文字的按鈕
 */
public class CTextButton extends TextButton {
	
	private static int FONT_SIZE = 24;
	
	private static String FONT_PATH = "font/msjh.ttc";
	
	public CTextButton(String text) {
		super(text, genStyle(text));
	}

	private static TextButtonStyle genStyle(String text) {
		TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
		// 用 Pixmap 生成簡單灰色背景
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.GRAY);
        pixmap.fill();   
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
		 //按鍵彈起和按下圖都用一樣
        style.up = new TextureRegionDrawable(new TextureRegion(texture));
        style.down = new TextureRegionDrawable(new TextureRegion(texture));
        
        // --- FreeTypeFontGenerator 生成字型 ---
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(FONT_PATH));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = FONT_SIZE;
        parameter.characters = text;
        BitmapFont font = generator.generateFont(parameter);
        generator.dispose();
        
        style.font = font;
		return style;
	}
}
