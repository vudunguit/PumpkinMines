package com.aga.mine.pumpkin;

import org.cocos2d.menus.CCMenu;
import org.cocos2d.menus.CCMenuItem;
import org.cocos2d.menus.CCMenuItemImage;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCNode;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.types.CGSize;

public class HomeBottom {
	
	final String fileExtension = ".png";
	
	private CGSize winsize() {
		return CCDirector.sharedDirector().winSize();
	}
	
	public HomeBottom(CCSprite parentSprite, String imageFolder, CCNode nodeThis) {
		setBottomMenu(parentSprite, imageFolder, nodeThis);
	}

	// 하단 메뉴
	private void setBottomMenu(CCSprite parentSprite, String imageFolder, CCNode nodeThis) {
		
		// 좌측 버튼(상점)
		CCMenuItem button1 = CCMenuItemImage.item(
				SpriteSummery.imageSummary(
						imageFolder + "home-shopbutton1" + fileExtension,
						Utility.getInstance().getNameWithIsoCodeSuffix(imageFolder + "home-shoptext1" + fileExtension)),
				SpriteSummery.imageSummary(
						imageFolder + "home-shopbutton2" + fileExtension,
						Utility.getInstance().getNameWithIsoCodeSuffix(imageFolder + "home-shoptext2" + fileExtension)), 
						nodeThis, "clicked");
		button1.setUserData("shop");
		
		//중앙 버튼(입장)
		CCMenuItem button2 = CCMenuItemImage.item(
				SpriteSummery.imageSummary(
						imageFolder + "home-enterbutton1" + fileExtension,
						Utility.getInstance().getNameWithIsoCodeSuffix(imageFolder + "home-entertext1" + fileExtension)),
				SpriteSummery.imageSummary(
						imageFolder + "home-enterbutton2" + fileExtension,
						Utility.getInstance().getNameWithIsoCodeSuffix(imageFolder + "home-entertext2" + fileExtension)), 
						nodeThis, "clicked");
		button2.setUserData("enter");
		
		// 우측 버튼(설정)
		CCMenuItem button3 = CCMenuItemImage.item(
				SpriteSummery.imageSummary(
						imageFolder + "home-optionbutton1" + fileExtension,
						Utility.getInstance().getNameWithIsoCodeSuffix(imageFolder + "home-optiontext1" + fileExtension)),
				SpriteSummery.imageSummary(
						imageFolder + "home-optionbutton2" + fileExtension,
						Utility.getInstance().getNameWithIsoCodeSuffix(imageFolder + "home-optiontext2" + fileExtension)), 
						nodeThis, "clicked");
		button3.setUserData("option");
		
		CCMenu bottomMenu = CCMenu.menu(button1, button2, button3);
		nodeThis.addChild(bottomMenu);
		bottomMenu.setPosition(0.0f, 0.0f);
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
						nodeThis, "clicked");
		friendsInvite.setUserData("invite");
		
		CCMenu inviteMenu = CCMenu.menu(friendsInvite);
		parentSprite.addChild(inviteMenu);
		inviteMenu.setContentSize(parentSprite.getContentSize());
		inviteMenu.setPosition(0,0);
		inviteMenu.setAnchorPoint(0,0);
		
		friendsInvite.setPosition((595 * winsize().width) / parentSprite.getBoundingBox().size.width ,
		inviteMenu.getContentSize().height - 710);
		friendsInvite.setAnchorPoint(1, 1);
	}

}
