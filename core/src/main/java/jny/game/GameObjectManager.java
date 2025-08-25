package jny.game;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameObjectManager {

	public List<GameObject> gameObjList = new ArrayList<>();
	
	private static GameObjectManager gameObjManger;
	
	private List<GameObject> pendingAdd = new ArrayList<>();
	
	private GameObjectManager() {
		
	}
	
	public static GameObjectManager getInstance() {
		if(gameObjManger==null) {
			gameObjManger = new GameObjectManager();
		}
		return gameObjManger;
	}
	
	
	// 新增物件（不會馬上進入主清單）
	public void add(GameObject obj) {
		 pendingAdd.add(obj);
	}
	
    

    // 更新所有物件
    public void update(float delta) {
        // 先更新已存在的物件
        Iterator<GameObject> it = gameObjList.iterator();
        while (it.hasNext()) {
            GameObject obj = it.next();
            obj.update(delta);
            if (obj.isDead()) {
                it.remove(); // 安全刪除
            }
        }

        // 把新增的物件搬進來
        if (!pendingAdd.isEmpty()) {
        	gameObjList.addAll(pendingAdd);
            pendingAdd.clear();
        }
    }

    // 繪製所有物件
    public void render(SpriteBatch batch) {
        for (GameObject obj : gameObjList) {
            obj.render(batch);
        }
    }
}
