package com.aga.mine.pumpkin;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.cocos2d.layers.CCLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.menus.CCMenu;
import org.cocos2d.menus.CCMenuItem;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCLabel;
import org.cocos2d.nodes.CCNode;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.nodes.CCTextureCache;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.ccColor3B;

import android.content.Context;
import android.util.Log;

import com.aga.mine.pages2.UserData;
import com.aga.mine.pages2.GameData;
import com.aga.mine.pumpkin.MainActivity.InviteCallback;
import com.aga.mine.util.Util;
import com.aga.mine.util.UtilResource;

public class Invite extends CCLayer {
	
//	public static int mode;
	
	final String commonfolder = "00common/";
	final String folder = "30invite/";
	final String fileExtension = ".png";

	CCSprite bg;
	
	int mode = 0;
	
	CCSprite backboardUpper;
	CCSprite boardFrame;
	ArrayList<CCSprite> rewards;
	CCSprite checkSprite;
	CCLabel  inviteQuantity;
	
	public static float displayLeft;
	public static float displayRight;
	public static float displayTop;
	public static float displayBottom;
	
	CCMenuItem inviteButton; 
	CCMenuItem timerBack;
	CCMenu menu111;
	
	private Context mContext;
	UserData userData;
	private static Invite inviteLayer;
	
	public static CCScene scene() {
		CCScene scene = CCScene.node();
		CCLayer layer = new Invite();
		scene.addChild(layer);
//		scene.addChild(InvitationReceiver.getInstance().getInvitationPopup());
		return scene;
	}
	
    public InviteCallback mInviteCallback = new InviteCallback() {

		@Override
		public void onInvited(List<String> invitedFriends, String requestId) {
			//To Do:
			Log.e("Invite", "Callback_7 - onInvited" );
//			Log.d("LDK", "requestId:" + requestId);
			int inviteCount = Integer.parseInt(FacebookData.getinstance().getDBData("InviteNumber"));
			for (String string : invitedFriends) {
//				Log.e("Invite", "invitedFriend : " + string);
				String requestID = requestId;
				String recipientID = FacebookData.getinstance().getUserInfo().getId();
				String senderID = "1";
				String sendMailData = 
						"0,RequestModeMailBoxAdd" +
						"*22," + requestID + 
						"*1," + recipientID + 
						"*19," + senderID + 
						"*20,Broomstick*21," + 1;
				DataFilter.sendMail(sendMailData);
				
				//save date to shared pref
				Util.setInvite(string);
				inviteCount++;
				
			}
			
			// 친구 초대인원이 30명인데 그다음부터 초대를 취소하면 보상을 계속 획득하는지 확인해봐야 됩니다.
			// (50, 70명도 동일합니다.)
			if (inviteCount == 30) { // 30번째 초대시 3천gold 보상
				inviteRewardGold(3000);
				rewards.get(0).addChild(checkSprite);
				checkSprite.setPosition(rewards.get(0).getContentSize().width*0.5f, rewards.get(0).getContentSize().height*0.5f);
			} else if (inviteCount == 50) { // 50번째 초대시 7천gold 보상
				inviteRewardGold(7000);
				rewards.get(1).addChild(checkSprite);
				checkSprite.setPosition(rewards.get(1).getContentSize().width*0.5f, rewards.get(1).getContentSize().height*0.5f);
			} else if (inviteCount == 70) { // 70명째 초대시 1만gold 보상
				inviteRewardGold(10000);
				rewards.get(2).addChild(checkSprite);
				checkSprite.setPosition(rewards.get(2).getContentSize().width*0.5f, rewards.get(2).getContentSize().height*0.5f);
			}
			
			FacebookData.getinstance().modDBData("InviteNumber", String.valueOf(inviteCount));
			inviteQuantity.setString(String.valueOf(inviteCount));
			//refresh invite scrollview
			MainApplication.getInstance().getActivity().mHandler.sendEmptyMessage(Constant.MSG_DISPLAY_INVITELIST);
		}
    };

    private void inviteRewardGold(int gold) {
		long requestID = (long) (Math.random() * 72036854775807L);
		String recipientID = FacebookData.getinstance().getUserInfo().getId();
		String senderID = "1";
		String data = 
				"0,RequestModeMailBoxAdd*22," + requestID + "*1," + recipientID + "*19," + senderID + "*20,Gold*21," + gold;				
		DataFilter.sendMail(data);
	}
    
	public static synchronized Invite getInstance() {
		if (inviteLayer == null) {
			inviteLayer = new Invite();
		}
		return inviteLayer;
	}

	private Invite() {
		mContext = CCDirector.sharedDirector().getActivity();
		userData = UserData.share(mContext);
		rewards = new ArrayList<CCSprite>();
		checkSprite = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + "24emoticon/emoticonchecked.png"));
		//when invitation is successful, this callback is called.
    	Log.e("Invite", "Callback_1 - setInviteCallback()");
		
		//배경 그림 설정
		bg = BackGround.setBackground(this, CGPoint.make(0.5f, 0.5f), commonfolder + "bg1" + fileExtension);		
		setBackBoardMenu(folder + "invite-bb2" + fileExtension);
		setBoardFrameMenu(commonfolder + "frameGeneral-hd" + fileExtension);
		
		// 상단 메뉴
		TopMenu2.setSceneMenu(this);
		// 하단 이미지
		BottomImage.setBottomImage(this);
		
		MainApplication.getInstance().getActivity().setInviteCallback(mInviteCallback);
		this.setIsTouchEnabled(true);
	}

	// 백 보드 설정
	private void setBackBoardMenu(String imageFullPath) {
		CCSprite backBoard = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + imageFullPath));
		bg.addChild(backBoard);
		backBoard.setPosition(bg.getContentSize().width / 2, bg.getContentSize().height * 0.525f);
		backBoard.setAnchorPoint(0.5f, 0.5f);
		setMainMenu(backBoard);
	}
	
	// 게시판 설정
	private void setBoardFrameMenu(String imageFullPath) {
		CCSprite boardFrame = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + imageFullPath));
		bg.addChild(boardFrame);
		boardFrame.setPosition(bg.getContentSize().width / 2, bg.getContentSize().height * 0.525f);
		boardFrame.setAnchorPoint(0.5f, 0.5f);
		FrameTitle2.setTitle(boardFrame, folder);
	}

	// 메인 메뉴
	private void setMainMenu(CCSprite parentSprite){
		String invite1str = "persons you invited";
		float invite1PositionX = parentSprite.getContentSize().width * 0.43f;
		if (Locale.getDefault().getLanguage().toString().equals("ko")) {
			invite1str  = "현재초대인원             명";
			invite1PositionX = parentSprite.getContentSize().width * 0.5f;
		}
		
		CCLabel  inviteCountText = CCLabel.makeLabel(invite1str, "Arial", 25); // 현재 초대 인원 (노란색)
		inviteCountText.setColor(ccColor3B.ccYELLOW);
		inviteCountText.setPosition(invite1PositionX, 187);
		parentSprite.addChild(inviteCountText);
		
		float invite2PositionX = parentSprite.getContentSize().width * 0.78f;
		if (Locale.getDefault().getLanguage().toString().equals("ko"))
			invite2PositionX = parentSprite.getContentSize().width * 0.68f;
		
		String app_Invite = FacebookData.getinstance().getDBData("InviteNumber");
//		String app_Invite = "888"; // 테스트 코드
		
		inviteQuantity = CCLabel.makeLabel(
				new NumberComma().numberComma(app_Invite), "Arial", 35); // 현재 초대 인원 (노란색)
		inviteQuantity.setAnchorPoint(1, 0.5f);
		inviteQuantity.setColor(ccColor3B.ccYELLOW);
		inviteQuantity.setPosition(invite2PositionX, 187);
		parentSprite.addChild(inviteQuantity);
		
		String invit2str = "up to 20 persons can be invited a day";
		if (Locale.getDefault().getLanguage().toString().equals("ko"))
			invit2str  = "초대는 하루 20명까지 가능 합니다.";
		
		CCLabel inviteText = CCLabel.makeLabel(invit2str, "Arial", 20); // 흰색
		parentSprite.addChild(inviteText);
		inviteText.setPosition(parentSprite.getContentSize().width/2, 160.0f);		

		rewards.add(jewelButton(parentSprite, -1.1f, 3000, 30));
		rewards.add(jewelButton(parentSprite, 0, 7000, 50));
		rewards.add(jewelButton(parentSprite, 1.1f, 10000, 70));
		
		// 일단 막코딩 합니다. ㅠㅠ (리팩토링이 필요합니다.)
		int inviteCount = Integer.parseInt(FacebookData.getinstance().getDBData("InviteNumber"));
		int count = 0;
		for (int i = 30; i < inviteCount; i+=20) {
			rewards.get(count).addChild(checkSprite);
			checkSprite.setPosition(rewards.get(count).getContentSize().width*0.5f, rewards.get(count).getContentSize().height*0.5f);
			count ++;
		}
		count = 0;
	}
		
	private CCSprite jewelButton(CCSprite parentSprite, float position, int gold, int friends) {

		CCSprite statusPanel = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + folder + "invite-statusPanel" + fileExtension)); //보석 버튼
		statusPanel.setAnchorPoint(0.5f, 0.0f);
		statusPanel.setPosition(
				parentSprite.getContentSize().width/2 + statusPanel.getContentSize().width * position, 
				statusPanel.getContentSize().height/2-10.0f);
		
		CCLabel  statusPanelText1 = CCLabel.makeLabel(
				"Gold " + new NumberComma().numberComma(gold), "Arial", 22); // 보석 보상 갯수 (노란색)
		statusPanelText1.setColor(ccColor3B.ccYELLOW);
		statusPanelText1.setAnchorPoint(0.5f, 1.0f);
		statusPanelText1.setPosition(
				statusPanel.getContentSize().width / 2,
				statusPanel.getContentSize().height - 3);
		
		String friendsInvitation1Str = "Invitation";
		if (Locale.getDefault().getLanguage().toString().equals("ko"))
			friendsInvitation1Str  = "친구 초대";
		
		CCLabel  statusPanelText2 = CCLabel.makeLabel(friendsInvitation1Str, "Arial", 22); // 현재 초대 인원 (노란색)
		statusPanelText2.setColor(ccColor3B.ccYELLOW);
		statusPanelText2.setAnchorPoint(0.5f, 1.0f);
		statusPanelText2.setPosition(
				statusPanel.getContentSize().width / 2,
				statusPanel.getContentSize().height - 39f);

		String friendsInvitation2Str = String.valueOf(friends);
		if (Locale.getDefault().getLanguage().toString().equals("ko"))
			friendsInvitation2Str  = friends + "명";
		
		CCLabel  statusPanelText3 = CCLabel.makeLabel(friendsInvitation2Str, "Arial", 22); // 현재 초대 인원 (노란색)
		statusPanelText3.setColor(ccColor3B.ccYELLOW);
		statusPanelText3.setAnchorPoint(0.5f, 1.0f);
		statusPanelText3.setPosition(
				statusPanel.getContentSize().width / 2,
				statusPanel.getContentSize().height - 66f);
		

		parentSprite.addChild(statusPanel);
		statusPanel.addChild(statusPanelText1);
		statusPanel.addChild(statusPanelText2);
		statusPanel.addChild(statusPanelText3);
		
		checkSprite.setContentSize(statusPanel.getContentSize()); 
		return statusPanel;
	}

	// config 파일에 나중에 옮길것
	public static boolean buttonActive = true;
	final int previous = 501;
	final int home= 502;
	
	// sceneCallback들 전부 여기로 옮기기
	public void clicked(Object sender) {
		MainApplication.getInstance().getActivity().click();
		// hide scroll view
		MainApplication.getInstance().getActivity().mHandler.sendEmptyMessage(Constant.MSG_HIDE_SCROLLVIEW);
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
	
}