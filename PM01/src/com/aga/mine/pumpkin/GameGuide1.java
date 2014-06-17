package com.aga.mine.pumpkin;

import org.cocos2d.layers.CCColorLayer;
import org.cocos2d.layers.CCLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.ccColor4B;

import com.aga.mine.pumpkin.Utility;

public class GameGuide1 extends CCLayer {

	final String folder = "41tutorial/";
	final String fileExtension = ".png";
	
//	CCScene scene = null;

	public static CCScene scene() {
		CCScene scene = CCScene.node();
		CCLayer layer = CCColorLayer.node(new ccColor4B(255, 255, 0, 0));
		CCLayer layer2 = new GameGuide1();
		scene.addChild(layer);
		layer.addChild(layer2);
		return scene;
	}

	public GameGuide1() {
		CCSprite bg = BackGround.setBackground(this, CGPoint.make(0.5f, 0.5f), 
				Utility.getInstance().getNameWithIsoCodeSuffix3(folder + "guide-tutorial1-i4-") + fileExtension);

		// 하단 메뉴
		BottomMenu5.setBottomMenu(bg, folder, this, 1);
		
//		this.setIsTouchEnabled(true);
	}
	
	public void nextCallback(Object sender) {
		MainApplication.getInstance().getActivity().click();
		CCScene scene = GameGuide2.scene();
		CCDirector.sharedDirector().replaceScene(scene);
		}
	
}
