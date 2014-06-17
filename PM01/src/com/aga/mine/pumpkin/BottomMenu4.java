package com.aga.mine.pumpkin;

import org.cocos2d.menus.CCMenu;
import org.cocos2d.menus.CCMenuItem;
import org.cocos2d.menus.CCMenuItemImage;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCNode;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.nodes.CCTextureCache;
import org.cocos2d.types.CGSize;

import com.aga.mine.util.UtilResource;

public class BottomMenu4 {
	
	final static String fileExtension = ".png";
	
	// 나중에 다른 클래스에서 정의한 것과 하나로 합치기
	final int broomstickButton = 1001;
	final int goldButton = 1002;
	final int shopButton = 1003;
	final static int enterButton = 1004;
	final static int optionButton = 1005;
	final int inviteButton = 1006;
	final int mailButton = 1007;
	final int mailcloseButton = 1008;
	final int mailReceiveAllButton = 1009;
	final int presentGoldButton = 1010;
	final int presentBroomstickButton = 1011;
	
	private static CGSize winsize() {
		return CCDirector.sharedDirector().winSize();
	}
	
	// 하단 메뉴
	public static void setBottomMenu(CCSprite parentSprite, String imageFolder, CCNode nodeThis) {
		
		
		// 좌측 버튼(상점)
		CCMenuItem button1 = CCMenuItemImage.item(
				SpriteSummery.imageSummary(
						imageFolder + "home-shopbutton1" + fileExtension,
						Utility.getInstance().getNameWithIsoCodeSuffix(imageFolder + "home-shoptext1" + fileExtension)),
				SpriteSummery.imageSummary(
						imageFolder + "home-shopbutton2" + fileExtension,
						Utility.getInstance().getNameWithIsoCodeSuffix(imageFolder + "home-shoptext2" + fileExtension)), 
						null, null);
		
		//상점 x표시
		CCSprite lock1 = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH +imageFolder + "shopLock" + fileExtension));
		button1.addChild(lock1);
		lock1.setPosition(button1.getContentSize().width / 2, button1.getContentSize().height / 2);
		button1.setIsEnabled(false);
		
		
		//중앙 버튼(입장)
		CCMenuItem button2 = CCMenuItemImage.item(
				SpriteSummery.imageSummary(
						imageFolder + "home-enterbutton1" + fileExtension,
						Utility.getInstance().getNameWithIsoCodeSuffix(imageFolder + "home-entertext1" + fileExtension)),
				SpriteSummery.imageSummary(
						imageFolder + "home-enterbutton2" + fileExtension,
						Utility.getInstance().getNameWithIsoCodeSuffix(imageFolder + "home-entertext2" + fileExtension)), 
						nodeThis, "clicked2");
		button2.setTag(enterButton);
		
		// 우측 버튼(설정)
		CCMenuItem button3 = CCMenuItemImage.item(
				SpriteSummery.imageSummary(
						imageFolder + "home-optionbutton1" + fileExtension,
						Utility.getInstance().getNameWithIsoCodeSuffix(imageFolder + "home-optiontext1" + fileExtension)),
				SpriteSummery.imageSummary(
						imageFolder + "home-optionbutton2" + fileExtension,
						Utility.getInstance().getNameWithIsoCodeSuffix(imageFolder + "home-optiontext2" + fileExtension)), 
						nodeThis, "clicked2");
		button3.setTag(optionButton);
		
		CCMenu bottomMenu = CCMenu.menu(button1, button2, button3);
		nodeThis.addChild(bottomMenu);
		bottomMenu.setPosition(0.0f, 0.0f);
//		bottomMenu.setIsTouchEnabled(false);
		
		button1.setPosition(5, 30);
		button1.setAnchorPoint(0, 0);

		button2.setPosition(winsize().width / 2, 30);
		button2.setAnchorPoint(0.5f, 0);

		button3.setPosition(winsize().width - 5, 30);
		button3.setAnchorPoint(1, 0);
		
		// 친구 초대 버튼
		CCMenuItem friendsInvite = CCMenuItemImage.item(
				SpriteSummery.imageSummary(
						imageFolder + "home-invitebutton1" + fileExtension,
						Utility.getInstance().getNameWithIsoCodeSuffix(imageFolder + "home-invitetext1" + fileExtension)),
				SpriteSummery.imageSummary(
						imageFolder + "home-invitebutton2" + fileExtension,
						Utility.getInstance().getNameWithIsoCodeSuffix(imageFolder + "home-invitetext2" + fileExtension)), 
						null, null);

		CCMenu inviteMenu = CCMenu.menu(friendsInvite);
		parentSprite.addChild(inviteMenu);
		inviteMenu.setContentSize(parentSprite.getContentSize());
		inviteMenu.setPosition(0,0);
		inviteMenu.setAnchorPoint(0,0);
		
		friendsInvite.setPosition((595 * winsize().width) / parentSprite.getBoundingBox().size.width ,
		inviteMenu.getContentSize().height - 710);
		friendsInvite.setAnchorPoint(1f, 1f);
		
		//
		//x표시
		CCSprite lock2 = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH +imageFolder + "inviteLock" + fileExtension));
		friendsInvite.addChild(lock2);
		lock2.setPosition(friendsInvite.getContentSize().width / 2, friendsInvite.getContentSize().height / 2);
		friendsInvite.setIsEnabled(false);
//		inviteMenu.setIsTouchEnabled(false);
	}

}
