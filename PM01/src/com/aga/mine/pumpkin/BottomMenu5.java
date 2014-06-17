package com.aga.mine.pumpkin;

import org.cocos2d.menus.CCMenu;
import org.cocos2d.menus.CCMenuItem;
import org.cocos2d.menus.CCMenuItemImage;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCNode;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.types.CGSize;

import com.aga.mine.pumpkin.Utility;

public class BottomMenu5 {
	
	final static String fileExtension = ".png";
	
	public BottomMenu5() {
	}

	private static CGSize winsize() {
		return CCDirector.sharedDirector().winSize();
	}
	
	// 하단 메뉴
	public static void setBottomMenu(CCSprite parentSprite, String imageFolder, CCNode nodeThis, int type) {
		
	
		// 좌측 버튼(상점)
		CCMenuItem button1 = CCMenuItemImage.item(
						imageFolder + "guide-back-normal" + fileExtension,
						imageFolder + "guide-back-select" + fileExtension,
						nodeThis, "backCallback");
		
		CCMenuItem button3;
		
		if (type == 3) {
		// 우측 버튼(설정)
			button3 = CCMenuItemImage.item(
						imageFolder + "guide-done-normal" + fileExtension,
						imageFolder + "guide-done-select" + fileExtension,
						nodeThis, "nextCallback");
		} else {
			button3 = CCMenuItemImage.item(
					imageFolder + "guide-next-normal" + fileExtension,
					imageFolder + "guide-next-select" + fileExtension,
					nodeThis, "nextCallback");
		}
		
		CCMenu bottomMenu = CCMenu.menu(button1, button3);
		nodeThis.addChild(bottomMenu);
		bottomMenu.setPosition(0.0f, 0.0f);
		
		button1.setPosition(15, 30);
		button1.setAnchorPoint(0, 0);

		button3.setPosition(winsize().width - 15, 30);
		button3.setAnchorPoint(1, 0);
		
		if (type == 1) {
			button1.setVisible(false);
			button1.setIsEnabled(false);
		}
	}

}
