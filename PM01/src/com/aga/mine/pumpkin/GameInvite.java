package com.aga.mine.pumpkin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.cocos2d.layers.CCLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCLabel;
import org.cocos2d.nodes.CCNode;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.types.CGPoint;

import android.util.Log;

import com.aga.mine.pages2.GameData;
import com.aga.mine.pumpkin.ImageDownloader.ImageLoaderListener;
import com.aga.mine.pumpkin.NetworkController.MatchCallback;
import com.aga.mine.util.Util;

public class GameInvite extends CCLayer {
	
	// config 파일에 나중에 옮길것
	public static boolean buttonActive = true;
	final int previous = 501;
	final int home= 502;
	
	final String commonfolder = "00common/";
	final String folder = "53invite/";
	final int singleMode = 1;
	final int randomMode = 2;
	final int inviteMode = 3;
	
	final int titleTag = 345;
	final int bottomItemTag = 346;
	final int tornadoTag = 347;
	
	String matchedOppenentFacebookId;
	String matchedOppenentName;
	public boolean isOwner = false;
	
	private CCSprite bg;
	static CCSprite backboard;

    private static CCScene scene;
//	private static GameInvite gameInvite;
	
	final int random = 1;
	final int invite = 2;
    
	public static CCScene scene(int gameMode) {
		scene = CCScene.node();
		CCLayer layer = new GameInvite(gameMode);
		scene.addChild(layer);
		return scene;
	}
	
	public static CCScene scene(String id, String name, boolean owner, int matchMode) {
		scene = CCScene.node();
		CCLayer layer = new GameInvite(id, name, owner, matchMode);
		scene.addChild(layer);
		return scene;
	}
	
//	public static synchronized GameInvite getInstance() {
//		if (gameInvite == null)
//			gameInvite = new GameInvite();
//		return gameInvite;
//	} // 불필요 할듯

	private GameInvite(int gameMode) {
		NetworkController.getInstance().setMatchCallback(mMatchCallback);
		bg = BackGround.setBackground(this, CGPoint.make(0.5f, 0.5f), commonfolder + "bg1.png");	
		setBackBoardMenu(commonfolder + "bb1.png");
		setBoardFrameMenu(commonfolder + "frameMatching-hd.png");

		if (gameMode == random) {
			FrameTitle2.setTitle(boardFrame, "52random/", titleTag);
			TopMenu2.setSceneMenu(this);
			BottomImage.setBottomImage(this, bottomItemTag);
			Util.tornado(backboard, tornadoTag);
			try {
				NetworkController.getInstance().sendRequestMatch(GameData.share().getGameDifficulty()); // 난이도 주입
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (gameMode == invite) {
			FrameTitle2.setTitle(boardFrame, "53invite/", titleTag);
			TopMenu2.setSceneMenu(this);
			BottomMenu3.setBottomMenu(null, folder, this, bottomItemTag);
			mMatchCallback.setEntry(null, null, true);
			MainApplication.getInstance().getActivity().mHandler.sendEmptyMessage(Constant.MSG_DISPLAY_MATCHLIST);
		}
		
		isOwner = NetworkController.getInstance().getOwner();
//		NetworkController.getInstance().matchMode;
		
	}
	
	
	private GameInvite() {
		isOwner = NetworkController.getInstance().getOwner();
		
		//when invitation is successful, this callback is called.
    	Log.e("Invite", "Callback_1 - setInviteCallback()");
//		MainApplication.getInstance().getActivity().setInviteCallback(mInviteCallback);
		NetworkController.getInstance().setMatchCallback(mMatchCallback);
		//배경 그림 설정
		bg = BackGround.setBackground(this, CGPoint.make(0.5f, 0.5f), commonfolder + "bg1.png");	
		setBackBoardMenu(commonfolder + "bb1.png");
		setBoardFrameMenu(commonfolder + "frameMatching-hd.png");
//		Util.setEntry(null, null, true, backboard);
		// 상단 메뉴
		TopMenu2.setSceneMenu(this);
		// 하단 이미지
		BottomMenu3.setBottomMenu(null, folder, this, bottomItemTag); 
		
////		MainApplication.getInstance().getActivity().mHandler.sendEmptyMessage(Constant.MSG_HIDE_SCROLLVIEW);
//		//display scroll view
//		MainApplication.getInstance().getActivity().mHandler.sendEmptyMessage(Constant.MSG_DISPLAY_MATCHLIST);
	}
	
	String oppenentId;
	String oppenentName;
	
	private GameInvite(String id, String name, boolean owner, int matchMode) {
		oppenentId = id;
		oppenentName = name;
		isOwner = owner;
		final int randomGuest = 2;
		final int inviteGuest= 4;
		//배경 그림 설정
		bg = BackGround.setBackground(this, CGPoint.make(0.5f, 0.5f), commonfolder + "bg1.png");	
		setBackBoardMenu(commonfolder + "bb1.png");
		setBoardFrameMenu(commonfolder + "frameMatching-hd.png");
		if (matchMode == randomGuest) {
			FrameTitle2.setTitle(boardFrame, "52random/", titleTag);
		} else if (matchMode == inviteGuest) {
			FrameTitle2.setTitle(boardFrame, "53invite/", titleTag);
		}  
//		TopMenu2.setSceneMenu(this); // 불필요한데, 다시 넣으라고 할 것 같다. 까라면 깜.
		BottomImage.setBottomImage(this);
		mMatchCallback.setEntry(id, name, owner);
	}
	
	// 백 보드 설정
	private void setBackBoardMenu(String imageFullPath) {
		backboard = CCSprite.sprite(imageFullPath);
		bg.addChild(backboard, 0, 0);
		backboard.setPosition(bg.getContentSize().width / 2, bg.getContentSize().height * 0.525f);
		backboard.setAnchorPoint(0.5f, 0.5f);
		setMainMenu(backboard);
	}
	
	static CCSprite boardFrame;
	// 게시판 설정
	private void setBoardFrameMenu(String imageFullPath) {
		boardFrame = CCSprite.sprite(imageFullPath);
		bg.addChild(boardFrame);
		boardFrame.setPosition(bg.getContentSize().width / 2, bg.getContentSize().height * 0.525f);
		boardFrame.setAnchorPoint(0.5f, 0.5f);
//		FrameTitle2.setTitle(boardFrame, folder);
	}
	
	// 메인 메뉴
	private void setMainMenu(CCSprite parentSprite){
		
		List<CCNode> matchingPanel = new ArrayList<CCNode>();
		matchingPanel.add(CCSprite.sprite(commonfolder + "matchPanelMe.png")); 
		matchingPanel.add(CCSprite.sprite(commonfolder + "matchPanelOther.png")); 
				
		for (int k = 0; k < matchingPanel.size(); k++) {
			int value = k + 1;
			CCNode _node = matchingPanel.get(k);
			parentSprite.addChild(_node, 0, value);
			_node.setPosition(
					parentSprite.getContentSize().width / 2, 
					parentSprite.getContentSize().height - _node.getContentSize().height * (value * 1.05f + 0.1f));
			
			// 이미지
			final CCSprite _picture = CCSprite.sprite("noPicture.png");		
			_picture.setAnchorPoint(0.5f, 0.5f);
			_picture.setPosition(_picture.getContentSize().width * 2.7f, _node.getContentSize().height / 2);
			_node.addChild(_picture, 0, 102);

			// 이름
			CCLabel _name = CCLabel.makeLabel("Player" + value, "Arial", 30.0f);
			_name.setAnchorPoint(0, 0.5f);
			_name.setPosition(
					_picture.getPosition().x + ((1.3f - _picture.getAnchorPoint().x) * _picture.getContentSize().width), 
					_picture.getPosition().y);
			_node.addChild(_name, 0, 103);
		}
	}
	
	// sceneCallback들 전부 여기로 옮기기
	public void clicked(Object sender) {
		MainApplication.getInstance().getActivity().click();
		// hide scroll view
		MainApplication.getInstance().getActivity().mHandler.sendEmptyMessage(Constant.MSG_HIDE_SCROLLVIEW);
		CCScene scene = null;
		int value = ((CCNode) sender).getTag();
		if (buttonActive) {
			GameData.share().setGameDifficulty(0);

			switch (value) {
			case previous:
				scene = GameDifficulty.scene();
				break;

			case home:
				scene = Home.scene();
				GameData.share().setGameMode(0);
				break;
			}
			try {
				NetworkController.getInstance().sendRoomOwner(NetworkController.getInstance().guest);
			} catch (IOException e) {
				e.printStackTrace();
			}
			CCDirector.sharedDirector().replaceScene(scene);
		}
	}

	// BottomMenu3.setBottomMenu
	public void randomMatch(Object sender) {
		MainApplication.getInstance().getActivity().click();
		this.removeChildByTag(titleTag, true);
		this.removeChildByTag(bottomItemTag, true);
		FrameTitle2.setTitle(boardFrame, "52random/", titleTag);
		BottomImage.setBottomImage(this, bottomItemTag);
		Util.tornado(backboard, tornadoTag);
		// hide scroll view
		MainApplication.getInstance().getActivity().mHandler.sendEmptyMessage(Constant.MSG_HIDE_SCROLLVIEW);
		try {
			NetworkController.getInstance().sendRequestMatch(GameData.share().getGameDifficulty());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// ok!
	private static ImageDownloader mDownloader;
	public MatchCallback mMatchCallback = new MatchCallback() {
		
		@Override
		public void setEntry(String matchedOppenentFacebookId, String matchedOppenentName, boolean owner) {

			int[] location = {0,1};
			String[] urls = {FacebookData.getinstance().getUserInfo().getId(), matchedOppenentFacebookId}; 
			String[] names = {FacebookData.getinstance().getUserInfo().getName(), matchedOppenentName};
			int loop = 2;
			
			if (!owner)
				location = new int[] {1,0};
			if (matchedOppenentFacebookId == null || matchedOppenentName == null)
				loop = 1;
			
			for (int i = 0; i < loop ; i++) {
				List<CCNode> entry = backboard.getChildren();
				final CCSprite tempPicture = (CCSprite) entry.get(location[i]).getChildByTag(102);
				CCLabel tempName = (CCLabel) entry.get(location[i]).getChildByTag(103);
				
				String etUrl = "https://graph.facebook.com/" + urls[i] + "/picture";
				mDownloader = new ImageDownloader(etUrl, new ImageLoaderListener() {
					@Override
					public void onImageDownloaded(CCSprite profile) {
						tempPicture.setTexture(profile.getTexture());
					}
				});
				mDownloader.execute();
				tempName.setString(names[i]);
				
				if (location[i] == 1) {
					Config.getInstance().setDisableButton(true); // 대전이 되는 순간 버튼 잠그기(도망 못 감)
					backboard.removeChildByTag(tornadoTag, true); // 기존 애니 제거
					Util.count(backboard); // 
				}
			}
		}

		
	};
	
	
}