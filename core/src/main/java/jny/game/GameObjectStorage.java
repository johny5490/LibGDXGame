package jny.game;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * 存放所有遊戲物件
 */
public class GameObjectStorage {

	private List<Character> characterList = new ArrayList<>();
	
	private List<GameObject> pendingCharAdd = new ArrayList<>();
	
	private List<Arrow> arrowList = new ArrayList<>();
	private List<Arrow> pendingArrowAdd = new ArrayList<>();
	
	/**
	 * 新增物件（不會馬上進入主清單）,
	 * 新的物件先加入pendingAdd,再適合階段再加入gameObjList,
	 * 以避免ConcurrentModificationException
	 * @param obj
	 */
	public void addCharacter(Character obj) {
		 pendingCharAdd.add(obj);
	}
	
	public void addArrow(Arrow arrow) {
		pendingArrowAdd.add(arrow);
	}
	
	public List<Character> getCharacterList() {
		return characterList;
	}
	
	public List<Arrow> getArrowList(){
		return arrowList;
	}
	
    /**
     * 更新所有物件
     * @param delta
     */
    public void update(float delta) {        
    	update(delta, characterList, pendingCharAdd);
    	update(delta, arrowList, pendingArrowAdd);
    }

    private void update(float delta, List objectList, List peddingList) {
    	// 先更新已存在的物件
        Iterator<GameObject> it = objectList.iterator();
        while (it.hasNext()) {
            GameObject obj = it.next();
            obj.update(delta);
            if (obj.isGone) {
                it.remove();
            }
        }

        // 把新增的物件搬進來
        if (!peddingList.isEmpty()) {
        	objectList.addAll(peddingList);
        	peddingList.clear();
        }
    }
    
    public void render(SpriteBatch batch) {    	
    	renderList(batch, characterList);
    	renderList(batch, arrowList);
    }
    
    private void renderList(SpriteBatch batch, List list) {
    	for(GameObject obj : (List<GameObject>)list) {
    		obj.render(batch);
    	}
    }
}
