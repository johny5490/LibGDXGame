package jny.game.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class CharacterTextureLoader {
	
	private List<TextureRegion[][]> allRegions = new ArrayList<>();
	
	/**
	 * 把多張圖指定幾欄切格出,固定每格64*64
	 * @param filePaths
	 * @param columnIndexes
	 */
	public CharacterTextureLoader(String[] filePaths, int[] columnIndexes) {
		for(String path:filePaths) {
			TextureRegion[][] regions = new SpriteSheetLoader(path, 64, 64).getAllRegions();
			//單個檔案的Regions
			TextureRegion[][] oneFileRegions = new TextureRegion[regions.length][columnIndexes.length];
			
			for(int row=0 ; row<regions.length ; row++) {
				List<TextureRegion> rowList = new ArrayList<>();
				for(int col=0; col < regions[row].length ; col++) {
					if(arrayContains(columnIndexes, col)) {
						rowList.add(regions[row][col]);
					}
				}
				
				if(rowList.size()>0) {
					addRow(row, oneFileRegions, rowList);
				}
			}
			
			allRegions.add(oneFileRegions);
		}
		
	}

	private boolean arrayContains(int[] intArray, int num) {		
		for(int i:intArray) {
			if(i==num) {
				return true;
			}
		}
		return false;
	}
	
	private void addRow(int row, TextureRegion[][] oneFileRegions, List<TextureRegion> rowList) {		
		for(int i=0;i<rowList.size();i++) {
			oneFileRegions[row][i] = rowList.get(i);
		}		
	}
	
	public List<TextureRegion[][]> getAllRegions(){
		return allRegions;
	}
	
	 
}
