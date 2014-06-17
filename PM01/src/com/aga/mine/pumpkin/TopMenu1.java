package com.aga.mine.pumpkin;

import org.cocos2d.layers.CCLayer;
import org.cocos2d.menus.CCMenu;
import org.cocos2d.menus.CCMenuItem;
import org.cocos2d.menus.CCMenuItemImage;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCLabel;
import org.cocos2d.nodes.CCNode;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.nodes.CCTextureCache;
import org.cocos2d.types.CGSize;
import org.cocos2d.types.ccColor3B;

import android.content.Context;

import com.aga.mine.pages2.UserData;
import com.aga.mine.util.UtilResource;

public class TopMenu1 extends CCLayer{

	final static String commonfolder = "00common/";
	final static String fileExtension = ".png";

	Context mContext;
	UserData userData;
	
//	Clock clock1 = new Clock();
	
	int currentTime = 0;

	public TopMenu1() {
		mContext = CCDirector.sharedDirector().getActivity();
		userData = UserData.share(mContext);
	}

	// 상단 메뉴
	CCLabel[] setTopMenu(CCSprite parentSprite, String imageFolder, CCNode nodeThis) {
		
		// 지팡이 상점 바로가기 배경(버튼)
		CCMenuItem broomstickBg = CCMenuItemImage.item(
				CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + imageFolder	+ "home-broomstickBg-hd" + fileExtension)), 
				CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH +imageFolder	+ "home-broomstickBg-hd" + fileExtension)), 
				nodeThis, "broomstickCallback");

		// 골드 상점 바로가기 배경(버튼)
		CCMenuItem goldBg = CCMenuItemImage.item(
				CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH +imageFolder + "home-goldBg-hd" + fileExtension)),
				CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH +imageFolder + "home-goldBg-hd" + fileExtension)), 
				nodeThis, "goldCallback");

		CCMenu topMenu = CCMenu.menu(broomstickBg, goldBg);
		parentSprite.addChild(topMenu);
		
		topMenu.setColor(ccColor3B.ccc3(255, 255, 255));
		topMenu.setContentSize(CGSize.make(
				parentSprite.getContentSize().width - 80.0f,
				broomstickBg.getContentSize().height));
		topMenu.setPosition(
				35.0f, parentSprite.getContentSize().height - broomstickBg.getContentSize().height / 2 - 65.0f);

		broomstickBg.setPosition(broomstickBg.getContentSize().width / 2, 0f);
		goldBg.setPosition(topMenu.getContentSize().width - goldBg.getContentSize().width/ 2, 0f);

		 // 지팡이 그림
		CCSprite broomstickImg = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH +imageFolder + "home-broomstickOn-hd" + fileExtension));
		broomstickBg.addChild(broomstickImg);
		broomstickImg.setPosition(
				broomstickImg.getContentSize().width / 2 + 10.0f,
				broomstickBg.getContentSize().height / 2);

		// 지팡이 수량
		CCLabel broomstickEA = CCLabel.makeLabel("+" + userData.getBroomstick(), "Arial", 24.0f); // 지팡이 수량
		broomstickBg.addChild(broomstickEA);
		broomstickEA.setAnchorPoint(0, 0.5f);
		broomstickEA.setPosition(
				broomstickImg.getPosition().x + ((1 - broomstickImg.getAnchorPoint().x) * broomstickImg.getContentSize().width) - 2, 
				broomstickBg.getContentSize().height - broomstickEA.getContentSize().height / 2 - 5.0f);

		 // 지팡이 수량 증가 (카운트 다운)
		CCLabel broomstickTime = CCLabel.makeLabel("Loading...", "Arial", 20.0f);
//		broomstickTime = CCLabel.makeLabel("00:00", "Arial", 20.0f);  // schedule 때문에 생성자로 보냄
		broomstickBg.addChild(broomstickTime, 0, 2);
		broomstickTime.setPosition(
				broomstickBg.getContentSize().width - 10.0f,
				broomstickTime.getContentSize().height / 2 + 5.0f);
		broomstickTime.setAnchorPoint(1.0f, 0.5f);

		// 골드
		CCLabel gold = CCLabel.makeLabel("" + userData.getGold(), "Arial", 22.0f);
		goldBg.addChild(gold);
		gold.setPosition(goldBg.getContentSize().width - 10.0f, gold.getContentSize().height / 2 + 5.0f);
		gold.setAnchorPoint(1.0f, 0.5f);
		
		CCLabel[] aa = {broomstickEA, broomstickTime, gold};
		return aa;
	}
}
