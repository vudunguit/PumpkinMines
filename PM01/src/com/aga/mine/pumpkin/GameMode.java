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
import org.cocos2d.opengl.CCTexture2D;
import org.cocos2d.types.CGPoint;

import android.util.Log;

import com.aga.mine.pages2.GameData;
import com.aga.mine.util.UtilResource;

public class GameMode extends CCLayer {
	
	final String commonfolder = "00common/";
	final String folder = "50mode/";
	final String fileExtension = ".png";

	final int singleMode = 1;
	final int randomMode = 2;
	final int inviteMode = 3;
	
	CCSprite bg;
	
	static CCScene scene() {
		CCScene scene = CCScene.node();
		CCLayer layer = new GameMode();
		scene.addChild(layer);
		return scene;
	}

	public GameMode() {
				
		//배경 그림 설정
		bg = BackGround.setBackground(this, CGPoint.make(0.5f, 0.5f), commonfolder + "bg1" + fileExtension);
		setBackBoardMenu(commonfolder + "bb1" + fileExtension);
		setBoardFrameMenu(commonfolder + "frameGeneral-hd" + fileExtension);
		
		// 상단 메뉴
		TopMenu2.setSceneMenu(this);
		// 하단 이미지
		BottomImage.setBottomImage(this);
		
		this.setIsTouchEnabled(true);
	}
	
	// 백 보드 설정
	private void setBackBoardMenu(String imageFullPath) {
		CCTexture2D texture = CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + imageFullPath);
		CCSprite bb = CCSprite.sprite(texture);
		bg.addChild(bb);
		bb.setPosition(bg.getContentSize().width / 2, bg.getContentSize().height * 0.525f);
		bb.setAnchorPoint(0.5f, 0.5f);
		setMainMenu(bb);
	}
	
	// 게시판 설정
	private void setBoardFrameMenu(String imageFullPath) {
		CCTexture2D texture = CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + imageFullPath);
		CCSprite boardFrame = CCSprite.sprite(texture);
		bg.addChild(boardFrame);
		boardFrame.setPosition(bg.getContentSize().width / 2, bg.getContentSize().height * 0.525f);
		boardFrame.setAnchorPoint(0.5f, 0.5f);

		// 게시판명
		FrameTitle2.setTitle(boardFrame, folder);
	}
	
	// 게임 메뉴
	private void setMainMenu(CCSprite parentSprite){
		// 싱글
		CCMenuItemSprite button1 = CCMenuItemImage.item(
				CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + folder + "mode-singlebutton1" + fileExtension)),
				CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + folder + "mode-singlebutton2" + fileExtension)),
				this, "nextCallback");
		button1.setTag(singleMode);
		// 대전
		CCMenuItemSprite button2 = CCMenuItemImage.item(
				CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + folder + 	"mode-randombutton1" + fileExtension)),
						CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + folder + 	"mode-randombutton2" + fileExtension)),
				this, "nextCallback");
		button2.setTag(randomMode);
		// 초대
		CCMenuItemSprite button3 = CCMenuItemImage.item(
				CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + folder + 	"mode-invitebutton1" + fileExtension)),
				CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + folder + 	"mode-invitebutton2" + fileExtension)),
				this, "nextCallback");
		button3.setTag(inviteMode);
		
		CCMenu gameMenu = CCMenu.menu(button1, button2, button3);
		gameMenu.alignItemsVertically(0);
		gameMenu.setPosition(parentSprite.getContentSize().width / 2 - 5, parentSprite.getContentSize().height / 2 - 12);
		parentSprite.addChild(gameMenu, 1);
		
		CCSprite	buttonText1 = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + 
				Utility.getInstance().getNameWithIsoCodeSuffix(folder + "mode-singletext" + fileExtension)));
		button1.addChild(buttonText1);
		buttonText1.setPosition(button1.getContentSize().width - buttonText1.getContentSize().width/2 - 30f, button1.getContentSize().height/2);

		CCSprite	buttonText2 = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH +
				Utility.getInstance().getNameWithIsoCodeSuffix(folder + "mode-randomtext" + fileExtension)));
		button2.addChild(buttonText2);
		buttonText2.setPosition(button2.getContentSize().width - buttonText2.getContentSize().width/2 - 30f, button2.getContentSize().height/2);

		CCSprite	buttonText3 = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH +
				Utility.getInstance().getNameWithIsoCodeSuffix(folder + "mode-invitetext" + fileExtension)));
		button3.addChild(buttonText3);
		buttonText3.setPosition(button3.getContentSize().width - buttonText3.getContentSize().width/2 - 30f, button3.getContentSize().height/2);
		
		if (GameData.share().isGuestMode) {
			//x표시 2
			CCSprite lock2 = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + "10home/" + "modeLock" + fileExtension));
			button2.addChild(lock2);
			lock2.setPosition(button2.getContentSize().width / 2, button2.getContentSize().height / 2);
			
			//x표시 3
			CCSprite lock3 = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + "10home/" + "modeLock" + fileExtension));
			button3.addChild(lock3);
			lock3.setPosition(button3.getContentSize().width / 2, button3.getContentSize().height / 2);
			
//			button1.setIsEnabled(false);
			button2.setIsEnabled(false);
			button3.setIsEnabled(false);
		}
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
					if (GameData.share().isGuestMode) {
						scene = Home2.scene();
					} else {
						scene = Home.scene();
					}
					break;
			}
			CCDirector.sharedDirector().replaceScene(scene);
		}
	}

	public void nextCallback(Object sender) {
		MainApplication.getInstance().getActivity().click();
		int tagNumber = ((CCNode)sender).getTag();
		Log.e("GameMode", "selectButton  : " + tagNumber);
		GameData.share().setGameMode(tagNumber); // gameData로 옮겨야됨. (기존에 있음.)
		CCScene scene = GameDifficulty.scene();
		CCDirector.sharedDirector().replaceScene(scene);
	}
}