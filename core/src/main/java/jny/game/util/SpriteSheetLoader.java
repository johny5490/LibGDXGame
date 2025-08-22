package jny.game.util;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class SpriteSheetLoader {

    private Texture texture;
    private TextureRegion[][] regions;
    private int tileWidth, tileHeight;

    /**
     * 建立 SpriteSheetLoader
     * @param path 圖片路徑
     * @param tileWidth 每格寬
     * @param tileHeight 每格高
     */
    public SpriteSheetLoader(String path, int tileWidth, int tileHeight) {
        this.texture = new Texture(path);
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        this.regions = TextureRegion.split(texture, tileWidth, tileHeight);
    }

    /** 
     * 取得某一格 
     */
    public TextureRegion getRegion(int row, int col) {
        return regions[row][col];
    }

    /** 
     * 取得某一列（通常用來做動畫幀）
     */
    public Array<TextureRegion> getRow(int row) {
        Array<TextureRegion> list = new Array<>();
        for (int col = 0; col < regions[row].length; col++) {
            list.add(regions[row][col]);
        }
        return list;
    }

    public TextureRegion[][] getAllRegions() {       
        return regions;
    }

    /** 
     * 釋放資源 
     */
    public void dispose() {
        texture.dispose();
    }

}
