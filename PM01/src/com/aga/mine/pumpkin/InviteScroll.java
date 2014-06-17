package com.aga.mine.pumpkin;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.cocos2d.layers.CCColorLayer;
import org.cocos2d.layers.CCLayer;
import org.cocos2d.menus.CCMenu;
import org.cocos2d.menus.CCMenuItem;
import org.cocos2d.menus.CCMenuItemImage;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCLabel;
import org.cocos2d.nodes.CCNode;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.types.CGRect;
import org.cocos2d.types.CGSize;
import org.cocos2d.types.ccColor4B;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.aga.mine.pages2.UserData;
import com.aga.mine.pages2.GameData;
import com.sromku.simple.fb.entities.Profile;


public class InviteScroll extends CCColorLayer{
	
	final String commonfolder = "00common/";
	final String invitefolder = "30invite/";	
	
	CCMenuItem inviteButton;
	CCSprite inviteButtonOff;
	Context mContext;
	UserData userData;
	
	private static InviteScroll inviteScroll;
	
	public static InviteScroll getInstance() {
		if (inviteScroll == null) {
			inviteScroll = new InviteScroll(ccColor4B.ccc4(96, 66, 20, 255));
//			inviteScroll = new InviteScroll(ccColor4B.ccc4(244, 122, 122, 255));
		}
		return inviteScroll;
	}
	
	private static CGSize winsize() {
		return CCDirector.sharedDirector().winSize();
	}
	
	String friendBackBoardString = invitefolder + "invite-listPanel.png";
	final int width = 470; // 리스트 공간 폭 460
	final int height = (int) CCSprite.sprite(friendBackBoardString).getContentSize().height +3; // 리스트 높이 78
	int max_items = 4;
	final int base_tag = 100;
	int seperatorWeight = 3;
	
	protected InviteScroll(ccColor4B color) {
		super(color);
		mContext = CCDirector.sharedDirector().getActivity();
		userData = UserData.share(mContext);
//		List<GraphUser> friends = FacebookData.getinstance().getFriendsInfo();
		List<Profile> friends = FacebookData.getinstance().getFriendsInfo();
		if (friends.size() > 3) {
			max_items = friends.size();	
		}
//		max_items = friends.size();
		//
		CGRect rect = CGRect.make(0, -height, width, height * (max_items - 1) + 10);
		CGSize contentSize = CGSize.make(rect.size.width - rect.origin.x, rect.size.height - rect.origin.y);
		this.setContentSize(contentSize);
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		/*******************************************************/
		/** 문제지점 
		 * 
		 * 스크롤뷰 scroll view
		 * 
		 * 게임 설치된 facebook 친구들 중 초대거부 유저는 리스트에서 제외
		 * 이름순 정렬
		 * 
 		 *  **/
		int count = 0;
		for (Profile friend : friends) {
			// 친구 리스트
			CCSprite inviteBackBoard = CCSprite.sprite(friendBackBoardString);
			this.addChild(inviteBackBoard);
			inviteBackBoard.setPosition(
					this.getContentSize().width / 2, 
					this.getContentSize().height - 5 - (count * (inviteBackBoard.getContentSize().height + 3)));
			inviteBackBoard.setAnchorPoint(0.5f, 1);
			setFriendInvitionMenu(inviteBackBoard, friend.getName(), friend.getId(), count + 1);
			count ++;
			if (count > 9)  // 임시로 막음 10명까지만 받게 이사님 친구분들 많아서 오래걸림.
				break;
		}
		/*******************************************************/
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
	}
	
	//친구 초대 메뉴
	// facebook친구는 network를 통해 받을 것
	private void setFriendInvitionMenu(CCSprite parentSprite, String friendName, String friendID, int ranking){
		
		if (friendName.length() > 11) {
			friendName = friendName.substring(0, 10) + "..";
		}
		
		// 이미지틀
		CCSprite friendImage = CCSprite.sprite(commonfolder + "frame-pictureFrame-hd.png");
		parentSprite.addChild(friendImage);
		friendImage.setScale(0.8f);
		friendImage.setPosition(50.0f, parentSprite.getContentSize().height / 2);

		
		// 사용자 이미지 URL
//		String imageUrl = "https://graph.facebook.com/" + friendID + "/picture"; 
////		Log.e("Home", "facebookFriendsInfo : " + imageUrl);
//		CCSprite userImage= CCSprite.sprite(getBitmapFromURL(imageUrl)); // 프로필 사진
//		
		
		Bitmap userPhoto = getBitmapFromURL("https://graph.facebook.com/" + friendID +"/picture");  // 이사님 친구용 임시 주석
		Log.e("InviteScroll","userPhoto_Bytes : " + userPhoto.getRowBytes());  // 이사님 친구용 임시 주석
		//
		CCSprite userImage;
		if (userPhoto.getRowBytes() < 100) {  // 이사님 친구용 임시 주석
//		if (userPhoto == null) {
			userImage= CCSprite.sprite(commonfolder + "noPicture.png"); // 프로필 사진
		} else {  // 이사님 친구용 임시 주석
			userImage= CCSprite.sprite(userPhoto); // 프로필 사진  // 이사님 친구용 임시 주석
		}  // 이사님 친구용 임시 주석
		
		friendImage.addChild(userImage);
		userImage.setAnchorPoint(0.5f, 0.5f);
		userImage.setPosition(friendImage.getContentSize().width / 2, friendImage.getContentSize().height / 2);
		userImage.setScale(1 / 0.8f);
		
		CCLabel friendNameLabel = CCLabel.makeLabel(friendName, "Arial", 30.0f);
		parentSprite.addChild(friendNameLabel);
		friendNameLabel.setAnchorPoint(0,  0.5f);
		friendNameLabel.setPosition(
				friendImage.getPosition().x + ((1 - friendImage.getAnchorPoint().x) * friendImage.getContentSize().width) + 10, 
				parentSprite.getContentSize().height / 2);
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		/*******************************************************/
		/** 문제지점 
		 * 
		 * scroll view
		 * 
		 * 버튼을 눌렀을시 스크롤 전체 비활성 또는 버튼들만 비활성화
		 * (여러사람을 한번에 초대하는것을 막기위해)
		 * message가 들어오면 다시 활성화(초대에 거부시 다시 초대하기 위해)
		 * 
 		 *  **/
		// 초대 버튼
		inviteButton = CCMenuItemImage.item(
				Utility.getInstance().getNameWithIsoCodeSuffix(invitefolder + "invite-button1.png"),
				Utility.getInstance().getNameWithIsoCodeSuffix(invitefolder + "invite-button2.png"),
				this, "matchCallback");
		inviteButton.setUserData(friendID);
		inviteButton.setScale(0.9f);
//		inviteButton.setIsEnabled(false);
		/*******************************************************/
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		CCMenu inviteMenu = CCMenu.menu(inviteButton);
		inviteMenu.alignItemsVertically(0);
		parentSprite.addChild(inviteMenu);
		inviteMenu.setPosition(
				parentSprite.getContentSize().width - inviteButton.getContentSize().width / 2 - 5, 
				parentSprite.getContentSize().height / 2);
		
		inviteButtonOff = CCSprite.sprite(
				Utility.getInstance().getNameWithIsoCodeSuffix(invitefolder + "invite-button2.png"));
		inviteButtonOff.setVisible(false);
		int idTag = (int)Long.parseLong(friendID);
		parentSprite.addChild(inviteButtonOff, 1, idTag);
		inviteButtonOff.setPosition(inviteMenu.getPosition());
	}
	
	
//	
//	private void ranker(CCSprite parentSprite, String friendName, String friendID, int ranking) {
//		String medalColor = "home-circleNull";
//		ccColor3B medalNumberColor = ccColor3B.ccWHITE;
//		
//		
//		
//		switch (ranking) {
//		case 1:
//			medalColor = "home-circleGold-hd";
//			medalNumberColor = ccColor3B.ccBLACK;
//			break;
//		
//		case 2:
//			medalColor = "home-circleSilver-hd";
//			medalNumberColor = ccColor3B.ccBLACK;
//			break;
//			
//		case 3:
//			medalColor = "home-circleBronze-hd";
//			break;
//			
//		default:
//			
//			break;
//		}
//		
//		Log.e("inviteScroll", "friendName - before : " + friendName);
//		if (friendName.length() > 5) {
//			friendName = friendName.substring(0, 4) + "..";
//		}
//		Log.e("inviteScroll", "friendName - after : " + friendName);
//		
//		CCSprite rankingImg = CCSprite.sprite(folder + medalColor + .png"); // 메달 그림
//		parentSprite.addChild(rankingImg);
//		rankingImg.setAnchorPoint(0.5f, 0.5f);
//		rankingImg.setPosition(
//				10 + rankingImg.getContentSize().width / 2, 
//				(parentSprite.getContentSize().height - seperatorWeight) / 2.0f + seperatorWeight);
//		
//		CCLabel  rankingNumber = CCLabel.makeLabel("" + ranking, "Arial", 25.0f); // 순위 번호
//		rankingImg.addChild(rankingNumber);
//		rankingNumber.setColor(medalNumberColor);
//		rankingNumber.setPosition(
//				rankingImg.getContentSize().width / 2,rankingImg.getContentSize().height / 2);
//		
//		CCSprite profilePicture= CCSprite.sprite(commonfolder + "frame-pictureFrame-hd.png"); // 랭커 프로필 사진
//		parentSprite.addChild(profilePicture);
//		profilePicture.setPosition(
//				rankingImg.getPosition().x + rankingImg.getContentSize().width * rankingImg.getAnchorPoint().x + 10 +
//				profilePicture.getContentSize().width * profilePicture.getAnchorPoint().x,
//				rankingImg.getPosition().y);
//		
//		// 사용자 이미지 URL
//		String imageUrl = "https://graph.facebook.com/" + friendID + "/picture"; 
//		Log.e("Home", "facebookFriendsInfo : " + imageUrl);
//		CCSprite userImage= CCSprite.sprite(getBitmapFromURL(imageUrl)); // 프로필 사진
//		profilePicture.addChild(userImage);
//		userImage.setAnchorPoint(0.5f, 0.5f);
//		userImage.setPosition(profilePicture.getContentSize().width / 2, profilePicture.getContentSize().height / 2);
//		
//		// facebook friend에서 받아올것
//		CCLabel  rankersName = CCLabel.makeLabel(friendName + " ", "Arial", 30.0f); // 랭커 이름
//		parentSprite.addChild(rankersName);
//		rankersName.setAnchorPoint(0, 0.5f);
//		rankersName.setPosition(
//				profilePicture.getPosition().x + profilePicture.getContentSize().width * profilePicture.getAnchorPoint().x + 10,
//				parentSprite.getContentSize().height - rankersName.getContentSize().height / 2 -
//				(parentSprite.getContentSize().height - rankersName.getContentSize().height * 2 - seperatorWeight) / 3
//				);
//		
//		// db에서 받아올 것
//		CCLabel  rankersScore = CCLabel.makeLabel("760,000", "Arial", 30.0f); // 랭커 점수
//		parentSprite.addChild(rankersScore);
//		rankersScore.setAnchorPoint(0, 0.5f);
//		rankersScore.setPosition(
//				rankersName.getPosition().x,
//				parentSprite.getContentSize().height - rankersName.getPosition().y + seperatorWeight);
//		
//	}
//	
//	private void friendGiftButton(CCSprite parentSprite) {
//
//		CCMenuItem giftBoxPopup = CCMenuItemImage.item(  // 선물 상자 팝업
//				folder + "home-giftboxOn-hd.png",  
//				folder + "home-giftboxOff-hd.png",
//				this,"giftBoxCallback");
//	    
//		CCMenuItem broomstickPopup = CCMenuItemImage.item(  // 빗자루 팝업
//				folder + 	"home-broomStickBigOn-hd.png",
//				folder + 	"home-broomstickBigOff-hd.png", 				 
//				this,"broomStickGiftCallback");
//		
//		CCMenu giftMenu = CCMenu.menu(giftBoxPopup, broomstickPopup);
//		parentSprite.addChild(giftMenu);
//		giftMenu.setAnchorPoint(1, 0.5f);
//
//		giftMenu.setPosition(
//				parentSprite.getContentSize().width - 10, 
//				(parentSprite.getContentSize().height - seperatorWeight) / 2.0f + seperatorWeight);
//		
//		broomstickPopup.setAnchorPoint(1, 0.5f);
//		broomstickPopup.setPosition(0, 0);
//		
//		giftBoxPopup.setAnchorPoint(1, 0.5f);
//		giftBoxPopup.setPosition(
//				broomstickPopup.getPosition().x - 10 - (broomstickPopup.getContentSize().width * broomstickPopup.getAnchorPoint().x),
//				broomstickPopup.getPosition().y);	
//	}
//	
//	
//	// 백 보드 설정
//	private void setBackBoardMenu(String imageFullPath) {
//		bb = CCSprite.sprite(imageFullPath);
//		bg.addChild(bb);
//		bb.setPosition(bg.getContentSize().width / 2, bg.getContentSize().height * 0.525f);
//		bb.setAnchorPoint(0.5f, 0.5f);
//		userInfo(bb);
////		friends(bb);
//	}
//	
//
//	private void userInfo(CCSprite baseSprite) {
//		CCSprite profileBg = CCSprite.sprite(folder + "home-profileBg-hd.png"); // 프로필 백그
//		baseSprite.addChild(profileBg);
//		profileBg.setPosition(
//				baseSprite.getContentSize().width / 2, 
//				baseSprite.getContentSize().height - profileBg.getContentSize().height / 2 - 40.0f);
//		
//		Log.e("Home", "userInfo : " + imageUrl);
//		
//		CCSprite profilePicture= CCSprite.sprite(commonfolder + "frame-pictureFrame-hd.png"); // 프로필 사진 틀
//		profilePicture.setPosition(
//				profilePicture.getContentSize().width / 2 + 85, 
//				profileBg.getContentSize().height-60.0f);
//		
//		CCMenuItem post = CCMenuItemImage.item(  // 우편
//				folder + "home-mail-hd.png", 
//				folder + "home-mail-hd.png", 				 
//				this,"postCallback");
//		post.setAnchorPoint(1.0f, 0.5f);
//		
//		CCSprite userImage= CCSprite.sprite(getBitmapFromURL(imageUrl)); // 프로필 사진
//		profilePicture.addChild(userImage);
//		userImage.setAnchorPoint(0.5f, 0.5f);
//		userImage.setPosition(profilePicture.getContentSize().width / 2, profilePicture.getContentSize().height / 2);
//		
//		CCSprite newIcon = CCSprite.sprite(folder + "home-mailNew-hd01.png"); // 새로운 우편
//		newIcon.setPosition(
//				post.getContentSize().width/2, 
//				post.getContentSize().height);
//		post.addChild(newIcon);
//
//		CCMenu postMenu = CCMenu.menu(post);
//
//		post.setPosition(0f,0f);
//		postMenu.setContentSize(0f, 0f);
//		postMenu.setPosition(
//			profileBg.getContentSize().width - post.getContentSize().width/2, 
//			profileBg.getContentSize().height/2);
//		profileBg.addChild(postMenu);
//		
//		
//		
//		CCSprite levelProgressBar = CCSprite.sprite(folder + "home-progressBar-hd.png"); // 레벨 프로그레스 바
//		levelProgressBar.setPosition(
//				profileBg.getContentSize().width/2+10f,
//				levelProgressBar.getContentSize().height/2+25.0f);
//
//		
//		CCLabel  ranking = CCLabel.makeLabel("7", "Arial", 30.0f); // 순위
//		ranking.setPosition(
//				ranking.getContentSize().width/2+55.0f, 
//				profileBg.getContentSize().height-60.0f);
//		
//	
//
//		CCLabel  userName = CCLabel.makeLabel(userData.facebookUserInfo.getName(), "Arial", 30); // 이름
//		userName.setPosition(
//				profilePicture.getPosition().x + ((1 - profilePicture.getAnchorPoint().x) * profilePicture.getContentSize().width) + 12, 
//				profileBg.getContentSize().height - userName.getContentSize().height / 2 - 20);
//		userName.setAnchorPoint(0.0f, 0.5f);
//
//		CCLabel  score = CCLabel.makeLabel(userData.getPoint() + " ", "Arial", 30); // 최고 점수
//		score.setPosition(
////				levelProgressBar.getPosition().x + ((1 - levelProgressBar.getAnchorPoint().x) * levelProgressBar.getContentSize().width),
////				profileBg.getContentSize().height- score.getContentSize().height / 2 - 20.0f);
////				score.setAnchorPoint(1.0f, 0.5f);
//				userName.getPosition().x, 
//				profileBg.getContentSize().height- score.getContentSize().height / 2 - 60.0f);
//				score.setAnchorPoint(0.0f, 0.5f);
//
////		CCLabel  record = CCLabel.makeLabel("역대 전적", "Arial", 20.0f); // 전적
////		record.setPosition(
////				userName.getPosition().x, 
////				profileBg.getContentSize().height- record.getContentSize().height/2-60.0f);
////		record.setAnchorPoint(0.0f, 0.5f);
//		
//				String Ko = "전,승,패";
//				String Ch = "戰,勝,敗";
//				String En = "Played,Won,Lost";
//				
//		CCLabel  recordData = CCLabel.makeLabel(
//				"Played " + (userData.getHistory(0) +  userData.getHistory(1)) +
//				" : W" + userData.getHistory(1) + 
//				"/L" + userData.getHistory(0)
//				,"Arial", 20.0f); // 전적 값
//		recordData.setPosition(
//				userName.getPosition().x,
////				profileBg.getContentSize().height - recordData.getContentSize().height / 2 - 85
//				profilePicture.getPosition().y - (profilePicture.getAnchorPoint().y * profilePicture.getContentSize().height)
//				);
//		recordData.setAnchorPoint(0, 1);
//		
////		CCLabel  winningRate = CCLabel.makeLabel("승 률", "Arial", 20.0f); // 승률
////		winningRate.setPosition(
////				winningRate.getContentSize().width/2+330.0f, 
////				profileBg.getContentSize().height- winningRate.getContentSize().height/2-60.0f);
////		score.setAnchorPoint(1.0f, 0.5f);
//		
////		CCLabel  winningRateData = CCLabel.makeLabel((int)(100 * userData.getHistory(1) / (userData.getHistory(1) + userData.getHistory(0)))+"%", "Arial", 20.0f); // 승률 값
////		winningRateData.setPosition(
////				winningRateData.getContentSize().width/2+330.0f, 
////				profileBg.getContentSize().height- winningRateData.getContentSize().height/2-85.0f);
////		score.setAnchorPoint(1.0f, 0.5f);
//		
//		CCLabel  level = CCLabel.makeLabel("Lv"+""+" 1", "Arial", 40.0f); // 레벨
//		level.setPosition(
//				level.getContentSize().width/2+115.0f, 
//				profileBg.getContentSize().height- level.getContentSize().height/2-110.0f);
//		level.setAnchorPoint(1.0f, 0.5f);
//		level.setColor(ccColor3B.ccYELLOW);
//		
//		profileBg.addChild(profilePicture);
//		profileBg.addChild(levelProgressBar);
//		profileBg.addChild(ranking);
//		profileBg.addChild(userName);
//		profileBg.addChild(score);
////		profileBg.addChild(record);
//		profileBg.addChild(recordData);
////		profileBg.addChild(winningRate);
////		profileBg.addChild(winningRateData);
//		profileBg.addChild(level);
//
//	}
//	
//	
//	
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
//
	public CCLayer getLayer(){
		return this;
	}
//
//	public int getMax_items() {
//		return max_items;
//	}
//
//	public void setMax_items(int max_items) {
//		this.max_items = max_items;
//	}

	
	
	public void matchCallback(Object sender) {
		CCMenuItemImage a= (CCMenuItemImage)sender;
		String facebookID = (String)a.getUserData();
		Log.e("inviteScroll", "a : " + a.toString() + ", facebookId : " + facebookID);
//		a.setIsEnabled(false);
		
		
//		NetworkController.getInstance().sendRequestMatchInvite(GameData.share().getGameDifficulty(), facebookID);
		
//		a.getParent().addChild(timerBack);
//		a.removeFromParentAndCleanup(true);
//		userData.addBroomstick(1);
//		String facebookId = (String)inviteButton.getUserData();

//		a.setVisible(false);
//		int idTag = (int)(Long.parseLong(facebookId));
//		a.getParent().getChildByTag(idTag).setVisible(true);
		
		// 난이도는 1(쉬움), 2(보통), 3(어려움)
		try {
		NetworkController.getInstance().sendRequestMatchInvite(GameData.share().getGameDifficulty(), facebookID);
	} catch (IOException e) {
		e.printStackTrace();
	}
		
		
//		a.getParent().addChild(timerBack);
//		a.removeFromParentAndCleanup(true);
//		userData.addBroomstick(1);
		//menu111.addChild(timerBack);
		
		//inviteButton.removeFromParentAndCleanup(true);
		//menu111.removeChild((CCNode) sender, true);
	}

	CCNode nodeThis = CCNode.node(); 
	public CCLayer getLayer(CCNode nodeThis){
		this.nodeThis = nodeThis;
		return this;
	}
	
}
