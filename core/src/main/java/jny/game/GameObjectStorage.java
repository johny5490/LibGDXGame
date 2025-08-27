package jny.game;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import jny.game.gameObject.Arrow;
import jny.game.gameObject.Character;
import jny.game.gameObject.GameObject;
import jny.game.gameObject.TargetIndicator;

/**
 * 存放所有遊戲物件
 */
public class GameObjectStorage {

	public static GameObjectStorage INSTANCE = new GameObjectStorage();
	
	private List<Character> characterList = new ArrayList<>();
	private List<Arrow> arrowList = new ArrayList<>();
	private List<TargetIndicator> targetIndicatorList = new ArrayList<>();
	
	private List<GameObject> pendingCharAdd = new ArrayList<>();	
	private List<Arrow> pendingArrowAdd = new ArrayList<>();
	private List<TargetIndicator> pendingtargetIndicatorAdd = new ArrayList<>();
	
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
	
	public void addTargetIndicator(TargetIndicator indicator) {
		pendingtargetIndicatorAdd.add(indicator);
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
    	update(delta, targetIndicatorList, pendingtargetIndicatorAdd);
    }

    private void update(float delta, List objectList, List peddingList) {
    	// 先更新已存在的物件
        Iterator<GameObject> it = objectList.iterator();
        while (it.hasNext()) {
            GameObject obj = it.next();
            obj.update(delta);
            if (obj.isGone) {
                it.remove(); 
                obj.dispose();
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
    	renderList(batch, targetIndicatorList);
    }
    
    private void renderList(SpriteBatch batch, List list) {
    	for(GameObject obj : (List<GameObject>)list) {
    		obj.render(batch);
    	}
    }
}
