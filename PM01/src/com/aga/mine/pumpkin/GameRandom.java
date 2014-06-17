package com.aga.mine.pumpkin;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.cocos2d.events.CCTouchDispatcher;
import org.cocos2d.layers.CCLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCLabel;
import org.cocos2d.nodes.CCNode;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.types.CGPoint;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.aga.mine.pages2.GameData;
import com.sromku.simple.fb.entities.Profile;

public class GameRandom extends CCLayer {
	
	public boolean isOwner = false;

	static CCSprite bg;
	static CCLabel player1Label;
	static CCLabel player2Label;
	
	final String commonfolder = "00common/";
	final String folder = "52random/";
	
	CCSprite sprite1;
	CCSprite sprite2;
	CCSprite illust;
	
	private static GameRandom gameRandom;
	
	public static CCScene scene() {
		CCScene scene = CCScene.node();
		CCLayer layer = new GameRandom();
		scene.addChild(layer);
		scene.addChild(InvitationReceiver.getInstance().getInvitationPopup());
		return scene;
	}
	
	 public static synchronized GameRandom getInstance() {
		if (gameRandom == null)
			gameRandom = new GameRandom();
		return gameRandom;
	} // 불필요 할듯

	private GameRandom() {
		
		//배경 그림 설정
		bg = BackGround.setBackground(this, CGPoint.make(0.5f, 0.5f), commonfolder + "bg1.png");
		
		setBackBoardMenu(commonfolder + "bb1.png");
		setBoardFrameMenu(commonfolder + "frameMatching-hd.png");
		// 상단 메뉴
		TopMenu2.setSceneMenu(this);
		 // 잠시 주석처리
		// 하단 이미지
		BottomImage.setBottomImage(this);
//		BottomMenu2.setBottomMenu(null, folder, this);

		CCTouchDispatcher.sharedDispatcher().addTargetedDelegate(this, 0, true);
		this.setIsTouchEnabled(true);
		try {
			NetworkController.getInstance().sendRequestMatch(GameData.share().getGameDifficulty()); // 난이도 주입
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 백 보드 설정
	private void setBackBoardMenu(String imageFullPath) {
		CCSprite bb = CCSprite.sprite(imageFullPath);
		bg.addChild(bb, 0, 0);
		bb.setPosition(bg.getContentSize().width / 2, bg.getContentSize().height * 0.525f);
		bb.setAnchorPoint(0.5f, 0.5f);
		setMainMenu(bb);
		countdown(bb);
	}
	
	// 게시판 설정
	private void setBoardFrameMenu(String imageFullPath) {
		CCSprite boardFrame = CCSprite.sprite(imageFullPath);
		bg.addChild(boardFrame);
		boardFrame.setPosition(bg.getContentSize().width / 2, bg.getContentSize().height * 0.525f);
		boardFrame.setAnchorPoint(0.5f, 0.5f);
		FrameTitle2.setTitle(boardFrame, folder);
	}

	// 메인 메뉴
	private void setMainMenu(CCSprite parentSprite){
		int matchingPanelPosition = -20;
		
		List<CCSprite> matchingPanel = new ArrayList<CCSprite>();
		matchingPanel.add(CCSprite.sprite(commonfolder + "matchPanelMe.png")); 
		matchingPanel.add(CCSprite.sprite(commonfolder + "matchPanelOther.png")); 
		
		CCSprite pictureFrame = CCSprite.sprite(commonfolder + "frame-pictureFrame-hd.png");
		
		for (int i = 0; i < matchingPanel.size(); i++) {
			parentSprite.addChild(matchingPanel.get(i), 0, i + 10);
			matchingPanel.get(i).setPosition(
					parentSprite.getContentSize().width / 2, 
					parentSprite.getContentSize().height - matchingPanel.get(0).getContentSize().height * (i + 0.5f) - (i * 5) - 35  + matchingPanelPosition);

			 // facebook 이미지
			pictureFrame = CCSprite.sprite(commonfolder + "frame-pictureFrame-hd.png");
			matchingPanel.get(i).addChild(pictureFrame, 0, i + 100);
			pictureFrame.setScale(0.8f);		
			pictureFrame.setPosition(pictureFrame.getContentSize().width * 2, matchingPanel.get(0).getContentSize().height / 2);
		}
		
//		CCSprite player1Photo = CCSprite.sprite(FacebookData.getinstance().getUserPhoto()); // 프로필 사진
		CCSprite player1Photo = CCSprite.sprite(commonfolder + "noPicture.png"); // 프로필 사진
		matchingPanel.get(0).getChildByTag(100).addChild(player1Photo, 0, 1000);
		player1Photo.setScale(1 / 0.8f);
		player1Photo.setAnchorPoint(0.5f, 0.5f);
		player1Photo.setPosition(pictureFrame.getContentSize().width / 2, pictureFrame.getContentSize().height / 2);
		

		// facebook이름 (network에서 받을 것)
//		player1Label = CCLabel.makeLabel(userData.facebookUserInfo.getName(), "Arial", 30.0f);
		player1Label = CCLabel.makeLabel(FacebookData.getinstance().getUserInfo().getName(), "Arial", 30.0f);
		player2Label = CCLabel.makeLabel("상 대", "Arial", 30.0f);
		Log.e("GameRandom", "kMessageMatchCompleted : " + "yourName");
		
		matchingPanel.get(0).addChild(player1Label);
		matchingPanel.get(1).addChild(player2Label);
		
		player1Label.setAnchorPoint(0,  0.5f);
		player1Label.setPosition(
				pictureFrame.getPosition().x + ((1 - pictureFrame.getAnchorPoint().x) * pictureFrame.getContentSize().width) + 10, 
				matchingPanel.get(0).getContentSize().height / 2);
		player2Label.setAnchorPoint(0,  0.5f);
		player2Label.setPosition(player1Label.getPosition().x, matchingPanel.get(1).getContentSize().height / 2);
	}
	
	private void countdown(CCSprite parentSprite){

		illust = CCSprite.sprite(folder + "randomMatchingBottomIllust.png"); // 카운트 다운 배경
		parentSprite.addChild(illust);
		illust.setPosition(parentSprite.getContentSize().width / 2, illust.getContentSize().height / 2);

	}
	
//	//	배경 그림 설정 (frameMatching)
//	private void setBackground(){
		
//		bg = CCSprite.sprite("bg.png"); //백그라운드(풀)
//		bg.setPosition(CGPoint.make(winsize().width/2, winsize().height/2));
//		
//		CCSprite bb = CCSprite.sprite("mode-backboard.png"); //게시판 배경
//		bb.setPosition(CGPoint.make(winsize().width/2-4f, winsize().height/2-10f));
//		
		
//		// ----  대전 나 & 상대 --------//
//		CCSprite mm = CCSprite.sprite("matchPanelMe.png"); // 매칭 나
//		mm.setPosition(bb.getContentSize().width/2-10.0f,bb.getContentSize().height - mm.getContentSize().height/2-35f);
//		//mm.setPosition(CGPoint.make(winsize().width/2-3f,winsize().height-303.0f)); // 정사이즈 이미지 나왔을시 최종 위치 이동 필요
//		//mm.setAnchorPoint(0.5f,0.5f);
//		bb.addChild(mm);
//		
//		CCSprite mo = CCSprite.sprite("matchPanelOther.png"); // 매칭 상대
//		mo.setPosition(bb.getContentSize().width/2-10.0f,bb.getContentSize().height - (mo.getContentSize().height*1.5f)-40f);
//		bb.addChild(mo);
//		//mo.setPosition(CGPoint.make(winsize().width/2-3f, winsize().height-349.0f)); // 정사이즈 이미지 나왔을시 최종 위치 이동 필요
//		//mo.setAnchorPoint(0.5f,0.5f);
//		//this.addChild(mo);
//		
//		CCSprite pictureFrame = CCSprite.sprite("frame-pictureFrame-hd.png"); // 매칭 이미지
//		//pf.setPosition(CGPoint.make(75.0f, mm.getAnchorPoint().y+1f)); // 정사이즈 이미지 나왔을시 최종 위치 이동 필요
//		pictureFrame.setPosition(pictureFrame.getContentSize().width*2, mm.getContentSize().height/2); // CGpoint랑 무슨 차인지....
//		//pf.setAnchorPoint(0.5f, 0f);
//		pictureFrame.setScale(0.8f);
//		mm.addChild(pictureFrame);
//		mo.addChild(pictureFrame);
//		
//		// 이름은 스트링으로 db에서 받을것
//		CCLabel  myName = CCLabel.makeLabel("나길동", "Arial", 30.0f);
//		myName.setPosition(CGPoint.make(250.0f, mm.getContentSize().height/2));
//		mm.addChild(myName);
//		
//		myName = CCLabel.makeLabel("너길동", "Arial", 30.0f);
//		myName.setPosition(CGPoint.make(250.0f, mo.getContentSize().height/2));
//		mo.addChild(myName);
//		// ----  대전 나 & 상대 --------//
//		
//		illust = CCSprite.sprite("randomMatchingBottomIllust.png"); // 카운트 다운 배경
//		illust.setPosition(
//				bb.getContentSize().width/2 - 4.0f, 
//				illust.getContentSize().height/2 + 20.0f);
//		bb.addChild(illust);
//
//		countdownNumber1 = CCSprite.sprite("n01.png"); // 카운트 1
//		countdownNumber1.setPosition(
//				illust.getContentSize().width/2, 
//				illust.getContentSize().height/2);
//		illust.addChild(countdownNumber1);
//		countdownNumber1.setVisible(false);
//
//		countdownNumber2 = CCSprite.sprite("n02.png"); // 카운트 2
//		countdownNumber2.setPosition(
//				illust.getContentSize().width/2, 
//				illust.getContentSize().height/2);
//		illust.addChild(countdownNumber2);
//		countdownNumber2.setVisible(false);
//		
//		countdownNumber3 = CCSprite.sprite("n03.png"); // 카운트 3
//		countdownNumber3.setPosition(
//				illust.getContentSize().width/2, 
//				illust.getContentSize().height/2);
//		illust.addChild(countdownNumber3);
//		countdownNumber3.setVisible(false);
//		
//		countdownNumber4 = CCSprite.sprite("n04.png"); // 카운트 4
//		countdownNumber4.setPosition(
//				illust.getContentSize().width/2, 
//				illust.getContentSize().height/2);
//		illust.addChild(countdownNumber4);
//		countdownNumber4.setVisible(false);
//		
//		countdownNumber5 = CCSprite.sprite("n05.png"); // 카운트 5
//		countdownNumber5.setPosition(
//				illust.getContentSize().width/2, 
//				illust.getContentSize().height/2);
//		illust.addChild(countdownNumber5);
//		countdownNumber5.setVisible(false);
//		
//		
//
//		
//		
//		
//		// ----  게시판 틀 --------//
//		CCSprite boardFrame = CCSprite.sprite("frameMatching.png"); // 게시판 
//		boardFrame.setPosition(bb.getContentSize().width/2+5f, bb.getContentSize().height/2+20f);
//
//		CCSprite titlePanel = CCSprite.sprite("frame-titlePanel.png"); // 게시판 명패 
//		titlePanel.setPosition(boardFrame.getContentSize().width/2, boardFrame.getContentSize().height-100.0f);
//		
//		CCSprite frameTitle = CCSprite.sprite("random-titleKo.png"); // 게시판 명(랜덤매칭 선택)
//		frameTitle.setPosition(titlePanel.getContentSize().width/2,titlePanel.getContentSize().height/2+2f);
//		
//		this.addChild(bg); // 백그라운드
//		
//		titlePanel.addChild(frameTitle); // 명패에 게시판명 붙이기
//		boardFrame.addChild(titlePanel); // 게시판에 명패 붙이기
//		bb.addChild(boardFrame, 2); // 백보드에 게시판(틀) 붙이기
//		this.addChild(bb); // layer에 백보드붙붙
//		
//		}
	
	
//	
//	// scene 이동 메뉴
//	private void setSceneMenu(){
//		
//		CCMenuItemImage frameButtonBack = CCMenuItemImage.item(
//				"frame-buttonBack-normal.png",
//				"frame-buttonBack-select.png",
//				this, "PreviousCallback");
//		
//		CCMenuItemImage frameButtonHome = CCMenuItemImage.item(
//				"frame-buttonHome-normal.png",
//				"frame-buttonHome-select.png",
//				this,"homeCallback");
//		
//		CCMenuItem[] items = { frameButtonBack, frameButtonHome };
//		
//		CCMenu menu = CCMenu.menu(items);
//
//		frameButtonBack.setPosition(frameButtonBack.getContentSize().width/2+0.0f, 0f);
//		frameButtonHome.setPosition(winsize().width - frameButtonHome.getContentSize().width/2-0.0f, 0f);
//		menu.setPosition(CGPoint.ccp(0f, winsize().height - 200.0f));
//		addChild(menu);
//	}

/*
	// 게임 메뉴
	private void setGameMenu(){
		
		CCMenuItemImage button1 = CCMenuItemImage.item(
				"uiImages/05 Mode/mode-buttonSingle.png",
				"uiImages/05 Mode/mode-buttonSingle-select.png",
				this, "randomMatching");
		
		CCMenuItemImage button2 = CCMenuItemImage.item(
				"uiImages/05 Mode/mode-buttonVersus.png",
				"uiImages/05 Mode/mode-buttonVersus-select.png",
				this, "gameStart");
				
		CCSprite	buttonText1 = CCSprite.sprite("uiImages/05 Mode/mode-textSingleKo.png");
		buttonText1.setPosition(button1.getContentSize().width-buttonText1.getContentSize().width/2-30f, button1.getContentSize().height/2);
		button1.addChild(buttonText1);
		CCSprite	buttonText2 = CCSprite.sprite("uiImages/05 Mode/mode-textVersusKo.png");
		buttonText2.setPosition(button2.getContentSize().width-buttonText2.getContentSize().width/2-30f, button2.getContentSize().height/2);
		button2.addChild(buttonText2);
		
		CCMenuItem[] gameItems = { button1, button2 };
		CCMenu gameMenu = CCMenu.menu(gameItems);

		button1.setPosition(0f,-button1.getContentSize().height*0.5f); // 랜덤매칭
		button2.setPosition(0f,-button1.getContentSize().height*1.5f); // 게임시작
		gameMenu.setPosition(CGPoint.ccp(bb.getContentSize().width/2,bb.getContentSize().height/2-33f));
		
		bb.addChild(gameMenu, 1);
		this.addChild(bb); // 백보드
	}

	*/
	
//	
//	public CCSprite imageSummary(String image1, String image2) {
//		sprite1 = CCSprite.sprite(image1);
//		sprite2 = CCSprite.sprite(image2);
//		//sprite2.setPosition(sprite1.getContentSize().width/2, sprite1.getContentSize().height/2);
//		sprite2.setPosition(0f,0f);
//		sprite1.addChild(sprite2);
//		return sprite1;
//	}
	
//	
//	// 게임 메뉴
//	public void setMatchingMenu(){
//	/*	
//		CCMenuItemSprite button1 = CCMenuItemSprite.item(
//				imageSummary("uiImages/07-01 Random/random-buttonReady-normal.png","uiImages/07-01 Random/random-textReadyKo-normal.png"),
//				imageSummary("uiImages/07-01 Random/random-buttonReady-select.png","uiImages/07-01 Random/random-textReadyKo-select.png"),
//				this, "gameStart");
//		*/
//		CCMenuItemImage button1 = CCMenuItemImage.item(
//				"random-buttonReadyKo.png",
//				"random-buttonReadyKo-select.png",
//				this, "gameStart");
//
////		CCMenuItem[] gameItems = { button1};
////		CCMenu gameMenu = CCMenu.menu(gameItems);
//		CCMenu gameMenu = CCMenu.menu(button1);
//
//		button1.setPosition(button1.getContentSize().width/2,button1.getContentSize().height/2); // 랜덤매칭
//		gameMenu.setPosition(CGPoint.ccp(0.0f, button1.getContentSize().height/2));
//		addChild(gameMenu);
//	}

	public static Bitmap getBitmapFromURL(String src) {
	    try {
	        URL url = new URL(src);
	        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	        connection.setDoInput(true);
	        connection.connect();
	        InputStream input = connection.getInputStream();
	        Bitmap myBitmap = BitmapFactory.decodeStream(input);
	        return myBitmap;
	    } catch (IOException e) {
	        e.printStackTrace();
	        return null;
	    }
	}
	
	String userName = "_유저";
	String oppenentName = "_상대";
	CCSprite player1Photo = null;
	CCSprite player2Photo = null;
	
	public void matchNameReceiver(final String hostName, final String guestName) {
		player1Photo = (CCSprite) bg.getChildByTag(0).getChildByTag(10).getChildByTag(100).getChildByTag(1000);
		
		if (isOwner) {
//			for (GraphUser friend : userData.facebookFriendsInfo) {
			for (Profile friend : FacebookData.getinstance().getFriendsInfo()) {
				Log.e("Random", "friend.getId() : " + friend.getId());
				if (guestName.equals(friend.getId())) {
					userName = hostName;
					oppenentName = friend.getName();
					Bitmap userPhoto = getBitmapFromURL("https://graph.facebook.com/" + friend.getId() +"/picture");
					if (userPhoto.getRowBytes() < 100) {
						player2Photo= CCSprite.sprite(commonfolder + "noPicture.png"); // 프로필 사진
					} else {
						player2Photo= CCSprite.sprite(userPhoto); // 프로필 사진
					}
					break;
				}
			}
		} else {
			for (Profile friend : FacebookData.getinstance().getFriendsInfo()) {
				Log.e("Random", "friend.getId() : " + friend.getId());
				if (hostName.equals(friend.getId())) {
					userName = friend.getName();
					oppenentName = guestName;
					player2Photo = player1Photo;
					Bitmap userPhoto = getBitmapFromURL("https://graph.facebook.com/" + friend.getId() +"/picture");
					if (userPhoto.getRowBytes() < 100) {
						player1Photo= CCSprite.sprite(commonfolder + "noPicture.png"); // 프로필 사진
					} else {
						player1Photo= CCSprite.sprite(userPhoto); // 프로필 사진
					}
					break;
				}
			}
		}

		CCDirector.sharedDirector().getActivity().runOnUiThread(new Runnable() {
			public void run() {
					player1Label.setString(userName);
					player2Label.setString(oppenentName);
					
//					player1Photo // 이미지가 안들어감. 수정 요함 invite도 작업할것.
					
					//임시로 막음.
//					CCSprite parent1 = (CCSprite) bg.getChildByTag(0).getChildByTag(10).getChildByTag(100);
//					parent1.removeAllChildren(true);
//					parent1.addChild(player1Photo, 0, 1000);
//					CCSprite parent = (CCSprite) bg.getChildByTag(0).getChildByTag(11).getChildByTag(101);
//					parent.addChild(player2Photo, 0, 1001);
//					player2Photo.setScale(1 / 0.8f);
//					player2Photo.setAnchorPoint(0.5f, 0.5f);
//					player2Photo.setPosition(parent.getContentSize().width / 2, parent.getContentSize().height / 2);		
			}
		});
	}
	
	// config 파일에 나중에 옮길것
	public static boolean buttonActive = true;
	final int previous = 501;
	final int home= 502;
	
	
	int empty = 0;
	// sceneCallback들 전부 여기로 옮기기
	public void clicked(Object sender) {
		MainApplication.getInstance().getActivity().click();
		CCScene scene = null;
		int value = ((CCNode) sender).getTag();
		if (buttonActive) {
			GameData.share().setGameDifficulty(empty);
			try {
				NetworkController.getInstance().sendRoomOwner(NetworkController.getInstance().guest); // 방장 권한 제거 (random match에서만 있음)
				NetworkController.getInstance().sendRequestMatch(GameData.share().getGameDifficulty()); // 난이도 주입
			} catch (IOException e) {
				// 게임서버와 연결이 끊김.
				// 서버 다운인지 네트워크 문제인지. 발생시 처리방법?
				e.printStackTrace();
			}

			switch (value) {
			case previous:
				scene = GameDifficulty.scene();
				break;

			case home:
				scene = Home.scene();
				break;
			}
			CCDirector.sharedDirector().replaceScene(scene);
		}
	}
	
	public void gameStart(Object sender) {
	}
	
	public void count(float dt) {
	}
	
}