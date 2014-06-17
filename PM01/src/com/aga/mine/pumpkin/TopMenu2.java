package com.aga.mine.pumpkin;

import org.cocos2d.layers.CCLayer;
import org.cocos2d.menus.CCMenu;
import org.cocos2d.menus.CCMenuItemImage;
import org.cocos2d.menus.CCMenuItemSprite;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCLabel;
import org.cocos2d.nodes.CCNode;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.nodes.CCTextureCache;
import org.cocos2d.types.CGSize;

import com.aga.mine.pages2.UserData;
import com.aga.mine.util.UtilResource;

import android.content.Context;

public class TopMenu2 extends CCLayer{

	private final static String commonfolder = "00common/";
	private final static String fileExtension = ".png";

	private static CCLabel broomstickEA;
	private static CCLabel broomstickTime;

	private Context mContext;
	private UserData userData;
	
	public TopMenu2() {
	}
	
	private static CGSize winsize() {
		return CCDirector.sharedDirector().winSize();
	}

	final static int previous = 501;
	final static int home= 502;
	
	// 상단 메뉴 (scene 이동)
	public static void setSceneMenu(CCNode nodeThis){
		CCSprite backNormal = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + commonfolder + "frame-buttonBack-normal-hd" + fileExtension));
		CCSprite backSelect = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + commonfolder + "frame-buttonBack-select-hd" + fileExtension));
		CCMenuItemSprite frameButtonBack = CCMenuItemImage.item(
				backNormal,
				backSelect,
				nodeThis, "clicked");
		frameButtonBack.setTag(previous);
		
		CCSprite homeNormal = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + commonfolder + "frame-buttonHome-normal-hd" + fileExtension));
		CCSprite homeSelect = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + commonfolder + "frame-buttonHome-select-hd" + fileExtension));
		CCMenuItemSprite frameButtonHome = CCMenuItemImage.item(
				homeNormal,
				homeSelect,
				nodeThis,"clicked");
		frameButtonHome.setTag(home);
		
		CCMenu menu = CCMenu.menu(frameButtonBack, frameButtonHome);
		nodeThis.addChild(menu);

		menu.setContentSize(winsize().width, winsize().height);
		menu.setAnchorPoint(0,0);
		menu.setPosition(0f, winsize().height - 200.0f);
		
		frameButtonBack.setAnchorPoint(0.5f, 0.5f);
		frameButtonBack.setPosition(frameButtonBack.getContentSize().width / 2 + 20, 0);
		
		frameButtonHome.setAnchorPoint(0.5f, 0.5f);
		frameButtonHome.setPosition(menu.getContentSize().width - frameButtonHome.getContentSize().width / 2 - 20, 0);

	}
	
}
