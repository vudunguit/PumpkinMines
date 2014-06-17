package com.aga.mine.pumpkin;

import org.cocos2d.layers.CCLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.menus.CCMenu;
import org.cocos2d.menus.CCMenuItemImage;
import org.cocos2d.menus.CCMenuItemSprite;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCNode;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.nodes.CCTextureCache;
import org.cocos2d.types.CGPoint;

import android.view.MotionEvent;

import com.aga.mine.pages2.GameData;
import com.aga.mine.util.UtilResource;

public class Shop extends CCLayer {
	
	final String commonfolder = "00common/";
	final String folder = "20shop/";
	final String fileExtension = ".png";

	CCSprite bg;
	
	static CCScene scene() {
		CCScene scene = CCScene.node();
		CCLayer layer = new Shop();
		scene.addChild(layer);
//		scene.addChild(InvitationReceiver.getInstance().getInvitationPopup());
		return scene;
	}

	public Shop() {
		bg = BackGround.setBackground(this, CGPoint.make(0.5f, 0.5f), commonfolder + "bg1" + fileExtension);
		setBackBoardMenu(commonfolder + "bb1" + fileExtension);
		setBoardFrameMenu(commonfolder + "frameGeneral-hd" + fileExtension);
		TopMenu2.setSceneMenu(this);
		BottomImage.setBottomImage(this);
		
		this.setIsTouchEnabled(true);
	}
	
	private void setBackBoardMenu(String imageFullPath) {
		CCSprite bb = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + imageFullPath));
		bg.addChild(bb);
		bb.setPosition(bg.getContentSize().width / 2, bg.getContentSize().height * 0.525f);
		bb.setAnchorPoint(0.5f, 0.5f);
		setMainMenu(bb);
	}
	
	private void setBoardFrameMenu(String imageFullPath) {
		CCSprite boardFrame = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + imageFullPath));
		bg.addChild(boardFrame);
		boardFrame.setPosition(bg.getContentSize().width / 2, bg.getContentSize().height * 0.525f);
		boardFrame.setAnchorPoint(0.5f, 0.5f);
		FrameTitle2.setTitle(boardFrame, folder);
//		FrameTitle6.setTitle(boardFrame, folder);
	}
	
	private void setMainMenu(CCSprite parentSprite){
		CCMenuItemSprite button1 = CCMenuItemImage.item(
				CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + Utility.getInstance().getNameWithIsoCodeSuffix(
						folder + "shop-gold1" + fileExtension))),
				CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + Utility.getInstance().getNameWithIsoCodeSuffix(
						folder + "shop-gold2" + fileExtension))),
				this, "button1Callback");
		
		CCMenuItemSprite button2 = CCMenuItemImage.item(
				CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + Utility.getInstance().getNameWithIsoCodeSuffix(
						folder + "shop-item1" + fileExtension))),
				CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + Utility.getInstance().getNameWithIsoCodeSuffix(
						folder + "shop-item2" + fileExtension))),
				this, "button2Callback");
		
		CCMenuItemSprite button3 = CCMenuItemImage.item(
				CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + Utility.getInstance().getNameWithIsoCodeSuffix(
						folder + "shop-broomstick1" + fileExtension))),
				CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + Utility.getInstance().getNameWithIsoCodeSuffix(
						folder + "shop-broomstick2" + fileExtension))),
				this, "button3Callback");
		
		CCMenuItemSprite button4 = CCMenuItemImage.item(
				CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + Utility.getInstance().getNameWithIsoCodeSuffix(
						folder + "shop-emoticon1" + fileExtension))),
				CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + Utility.getInstance().getNameWithIsoCodeSuffix(
						folder + "shop-emoticon2" + fileExtension))),
				this, "button4Callback");
		
		CCMenu gameMenu = CCMenu.menu(button1, button2, button3, button4);
		gameMenu.alignItemsVertically(0);
		gameMenu.setPosition(parentSprite.getContentSize().width / 2 - 5, parentSprite.getContentSize().height / 2 - 12);
		parentSprite.addChild(gameMenu, 1);
//		gameMenu.setPosition(parentSprite.getContentSize().width / 2 - 5, parentSprite.getContentSize().height - 58f);
//		parentSprite.addChild(gameMenu);
//		
//		button1.setPosition(0, -button1.getContentSize().height * 0.5f); // 골드
//		button2.setPosition(0, -button1.getContentSize().height * 1.5f); // 아이템
//		button3.setPosition(0, -button1.getContentSize().height * 2.5f); // 빗자루
//		button4.setPosition(0, -button1.getContentSize().height * 3.5f); // 이모티콘
	}
	
	@Override
	public boolean ccTouchesEnded(MotionEvent event) {
		return super.ccTouchesEnded(event);
	}
	
	// config 파일에 나중에 옮길것
	public static boolean buttonActive = true;
	final int previous = 501;
	final int home= 502;
	
	// sceneCallback들 전부 여기로 옮기기
	public void clicked(Object sender) {
		MainApplication.getInstance().getActivity().click();
		CCScene scene = null;
		int value = ((CCNode) sender).getTag();
		if (buttonActive) {
			switch (value) {
				case previous :
				case home :
					if (GameData.share().isGuestMode) { // 게스트모드는 들어올수 없기때문에 불필요한 코드지만 만약에 오류상황시 넘길수 있는 상태로 놔둠.
						scene = Home2.scene();
					} else {
						scene = Home.scene();
					}
					break;
			}
			CCDirector.sharedDirector().replaceScene(scene);
		}
	}
	
	public void button1Callback(Object sender) {
		MainApplication.getInstance().getActivity().click();
		FacebookData.getinstance().setRecipientID(FacebookData.getinstance().getUserInfo().getId());
		CCScene scene = ShopGold2.scene();
		CCDirector.sharedDirector().replaceScene(scene);
	}
	
	public void button2Callback(Object sender) {
		MainApplication.getInstance().getActivity().click();
		CCScene scene = ShopItem2.scene();
		CCDirector.sharedDirector().replaceScene(scene);
	}
	
	public void button3Callback(Object sender) {
		MainApplication.getInstance().getActivity().click();
		FacebookData.getinstance().setRecipientID(FacebookData.getinstance().getUserInfo().getId());
		CCScene scene = ShopBroomstick2.scene();
		CCDirector.sharedDirector().replaceScene(scene);
	}
	
	public void button4Callback(Object sender) {
		MainApplication.getInstance().getActivity().click();
		CCScene scene = ShopEmoticon.scene();
		CCDirector.sharedDirector().replaceScene(scene);
	}

}