package com.aga.mine.pumpkin;

import java.io.IOException;

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
import android.widget.Toast;

import com.aga.mine.pages2.GameData;
import com.aga.mine.util.Util;
import com.aga.mine.util.UtilResource;

public class GameDifficulty extends CCLayer {
	
	final String commonfolder = "00common/";
	final String folder = "51difficulty/";
	final String fileExtension = ".png";

	final int singleMode = 1;
	final int randomMode = 2;
	final int inviteMode = 3;
	
//	final int easy = 1;
//	final int normal = 2;
//	final int expert = 3;
	
	CCSprite bg;
	
	public static int mode;
	
//	private Context mContext;
//	UserData userData;

	static CCScene scene() {
		CCScene scene = CCScene.node();
		CCLayer layer = new GameDifficulty();
		scene.addChild(layer);
		return scene;
	}

	public GameDifficulty() {
		
//		mContext = CCDirector.sharedDirector().getActivity();
//		userData = UserData.share(mContext);
		
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
		FrameTitle2.setTitle(boardFrame, folder);
	}

	// 게임 메뉴
	private void setMainMenu(CCSprite parentSprite){
		// 초급
		CCMenuItemSprite button1 = CCMenuItemImage.item(
				CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + folder + "difficulty-easybutton1" + fileExtension)),
				CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + folder + "difficulty-easybutton2" + fileExtension)),
				this, "nextCallback");
		button1.setTag(GameData.share().kGameDifficultyEasy);
		// 중급
		CCMenuItemSprite button2 = CCMenuItemImage.item(
				CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + folder + 	"difficulty-normalbutton1" + fileExtension)),
				CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + folder + 	"difficulty-normalbutton2" + fileExtension)),
				this, "nextCallback");
		button2.setTag(GameData.share().kGameDifficultyNormal);
		// 상급
		CCMenuItemSprite button3 = CCMenuItemImage.item(
				CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + folder + 	"difficulty-hardbutton1" + fileExtension)),
				CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + folder + 	"difficulty-hardbutton2" + fileExtension)),
				this, "nextCallback");
		button3.setTag(GameData.share().kGameDifficultyHard);
		
		CCMenu gameMenu = CCMenu.menu(button1, button2, button3);
		gameMenu.alignItemsVertically(0);
		gameMenu.setPosition(parentSprite.getContentSize().width / 2 - 5, parentSprite.getContentSize().height / 2 - 12);
		parentSprite.addChild(gameMenu, 1);
		
		CCSprite	buttonText1 = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + 
				Utility.getInstance().getNameWithIsoCodeSuffix(folder + "difficulty-easytext" + fileExtension)));
		button1.addChild(buttonText1);
		buttonText1.setPosition(button1.getContentSize().width - buttonText1.getContentSize().width/2 - 30f, button1.getContentSize().height/2);

		CCSprite	buttonText2 = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + 
				Utility.getInstance().getNameWithIsoCodeSuffix(folder + "difficulty-normaltext" + fileExtension)));
		button2.addChild(buttonText2);
		buttonText2.setPosition(button2.getContentSize().width - buttonText2.getContentSize().width/2 - 30f, button2.getContentSize().height/2);

		CCSprite	buttonText3 = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + 
				Utility.getInstance().getNameWithIsoCodeSuffix(folder + "difficulty-hardtext" + fileExtension)));
		button3.addChild(buttonText3);
		buttonText3.setPosition(button3.getContentSize().width - buttonText3.getContentSize().width/2 - 30f, button3.getContentSize().height/2);
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
			case previous:
				scene = GameMode.scene();
				break;

			case home:
				if (GameData.share().isGuestMode) {
					scene = Home2.scene();
				} else {
					scene = Home.scene();
				}
				GameData.share().setGameMode(0);
				break;
			}
			CCDirector.sharedDirector().replaceScene(scene);
		}
	}
	
	public void nextCallback(Object sender) {
		MainApplication.getInstance().getActivity().click();
		final int random = 1;
		final int invite = 2;
		
		int difficultyNumber = ((CCNode)sender).getTag();
		Log.e("GameMode", "tagNumber  : " + difficultyNumber);
		GameData.share().setGameDifficulty(difficultyNumber); // gameData로 옮겨야됨. (기존에 있음.)
//		GameData.share().setGameDifficulty((Integer)((CCNode)sender).getUserData());
//		switch (GameData.share().getGameMode()) {  // gameData로 옮겨야됨. (기존에 있음.)
		Log.e("GameDifficulty", "tagNumber  : " + GameData.share().getGameMode());
		
		CCScene scene = null;
		
		switch (GameData.share().getGameMode()) {  // gameData로 옮겨야됨. (기존에 있음.)
		case singleMode:
//			single(scene);
			scene = GameLoading.scene();
			Config.getInstance().setSingleOwner();
			break;
			
		case randomMode:
			scene = GameInvite.scene(random);
//			try {
//				NetworkController.getInstance().sendRequestMatch(GameData.share().getGameDifficulty()); // 난이도 주입
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
			break;
			
		case inviteMode:
			scene = GameInvite.scene(invite);
			try {
				NetworkController.getInstance().sendRoomOwner(NetworkController.getInstance().owner);
			} catch (IOException e) {
				e.printStackTrace();
			}
//			//display scroll view
//			MainApplication.getInstance().getActivity().mHandler.sendEmptyMessage(Constant.MSG_DISPLAY_MATCHLIST);
			break;
		}
		CCDirector.sharedDirector().replaceScene(scene);
	}

//	private void single(CCScene scene) {
//		// 게스트모드면 패스
//		if (!GameData.share().isGuestMode) {
//			
//			int mBroomstickCount = Integer.parseInt(FacebookData.getinstance().getDBData("ReceivedBroomstick"));
//			if (mBroomstickCount > 0) {
//				// 빗자루 수량이 1개 이상이면 게임 실행
//				// 게임 입장시 출입증격인 Broomstick을 1개 사용
//				// 사용후 남은 Broomstick이 6개 미만일시
//				// 기존에 빗자루 무료 제공하는 시간을 받아온 후
//				// setBroomstickTime(빗자루 무료 지급기) 실행
//				FacebookData.getinstance().modDBData("ReceivedBroomstick", String.valueOf(mBroomstickCount - 1)); // DB에 빗자루 수량 insert
//				if (mBroomstickCount >= 6)
//					Util.setBroomstickTime();
//			} else {
//				// 빗자루 수량이 0개면 메시지 출력 및 원래 화면으로
//				scene = GameDifficulty.scene();
//				CCDirector.sharedDirector().getActivity().runOnUiThread(new Runnable() {
//					public void run() {
//						Toast.makeText(MainApplication.getInstance().getApplicationContext(), "빗자루가 부족합니다.", Toast.LENGTH_SHORT).show();
//						}
//					});
//				}
//			}
//		}

}