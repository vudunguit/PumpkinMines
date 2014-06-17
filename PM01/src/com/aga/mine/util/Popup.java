package com.aga.mine.util;

import org.cocos2d.layers.CCLayer;
import org.cocos2d.menus.CCMenu;
import org.cocos2d.menus.CCMenuItem;
import org.cocos2d.menus.CCMenuItemImage;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCNode;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.nodes.CCTextureCache;
import org.cocos2d.types.CGSize;

import android.util.Log;

import com.aga.mine.pumpkin.Constant;
import com.aga.mine.pumpkin.Utility;

public class Popup extends CCLayer {
	
	public static void popupOfPurchase(CCNode parent) {
		CGSize winsize = CCDirector.sharedDirector().winSize();
		String commonFolder = "00common/";
		String folder = "20shop/";
		
		//  반투명
		CCSprite opacity = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + commonFolder + "opacitybg.png"));
		opacity.setPosition(winsize.width/2, winsize.height/2);
		parent.addChild(opacity, Constant.POPUP_LAYER, Constant.POPUP_LAYER);
		
		// 배경
		CCSprite background = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + commonFolder + "popup-bg.png"));
		background.setPosition(opacity.getContentSize().width/2, opacity.getContentSize().height/2);
		opacity.addChild(background);
		
		// 구매 메시지
		CCSprite message = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + Utility.getInstance().getNameWithIsoCodeSuffix(folder + "popup-msg.png")));
		message.setPosition(background.getContentSize().width/2, background.getContentSize().height/2);
		background.addChild(message);
		
		// OK 버튼
		CCSprite ok1 = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + Utility.getInstance().getNameWithIsoCodeSuffix(folder + "popup-ok1.png")));
		CCSprite ok2 = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + Utility.getInstance().getNameWithIsoCodeSuffix(folder + "popup-ok2.png")));
		CCMenuItem ok = CCMenuItemImage.item(ok1, ok2, parent, "clicked");
		ok.setTag(Constant.PURCHASING_OK);
		
		// Cancel 버튼
		CCSprite cancel1 = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + Utility.getInstance().getNameWithIsoCodeSuffix(folder + "popup-cancel1.png")));
		CCSprite cancel2 = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + Utility.getInstance().getNameWithIsoCodeSuffix(folder + "popup-cancel2.png")));
		CCMenuItem cancel = CCMenuItemImage.item(cancel1, cancel2, parent, "clicked");
		cancel.setTag(Constant.PURCHASING_CANCEL);
		
		// 버튼 메뉴 구성
		CCMenu buttons = CCMenu.menu(ok, cancel);
		buttons.alignItemsHorizontally(50);
		buttons.setPosition(background.getContentSize().width/2, background.getContentSize().height * 0.37f);
		background.addChild(buttons);
	}
	
}
