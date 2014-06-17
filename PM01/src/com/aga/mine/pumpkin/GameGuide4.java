package com.aga.mine.pumpkin;

import org.cocos2d.layers.CCColorLayer;
import org.cocos2d.layers.CCLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.ccColor4B;

import com.aga.mine.pumpkin.Utility;

public class GameGuide4 extends CCLayer {

	final String folder = "41tutorial/";
	final String fileExtension = ".png";
	
//	CCScene scene = null;

	public static CCScene scene() {
		CCScene scene = CCScene.node();
		CCLayer layer = CCColorLayer.node(new ccColor4B(255, 255, 0, 0));
		CCLayer layer2 = new GameGuide4();
		scene.addChild(layer);
		layer.addChild(layer2);
		return scene;
	}

	public GameGuide4() {
		CCSprite bg = BackGround.setBackground(this, CGPoint.make(0.5f, 0.5f), 
				Utility.getInstance().getNameWithIsoCodeSuffix3(folder + "guide-tutorial4-i4-") + fileExtension);

		// 하단 메뉴
		BottomMenu5.setBottomMenu(bg, folder, this, 2);
		
//		this.setIsTouchEnabled(true);
	}
	
	public void backCallback(Object sender) {
		MainApplication.getInstance().getActivity().click();
		CCScene scene = GameGuide3.scene();
		CCDirector.sharedDirector().replaceScene(scene);
	}
	
	public void nextCallback(Object sender) {
		MainApplication.getInstance().getActivity().click();
		CCScene scene = GameGuide5.scene();
		CCDirector.sharedDirector().replaceScene(scene);
		}
	
}
