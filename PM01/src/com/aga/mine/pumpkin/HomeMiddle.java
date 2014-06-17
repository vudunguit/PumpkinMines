package com.aga.mine.pumpkin;

import java.util.List;
import java.util.Locale;

import org.cocos2d.menus.CCMenu;
import org.cocos2d.menus.CCMenuItem;
import org.cocos2d.menus.CCMenuItemImage;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCLabel;
import org.cocos2d.nodes.CCNode;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.nodes.CCTextureCache;
import org.cocos2d.types.ccColor3B;

import android.content.Context;
import android.util.Log;

import com.aga.mine.pages2.UserData;
import com.aga.mine.pumpkin.ImageDownloader.ImageLoaderListener;
import com.aga.mine.util.UtilResource;

public class HomeMiddle {

	final String commonfolder = "00common/";
	final String fileExtension = ".png";
	final CCNode nodeThis;

	final int mailButton = 1007;
	final int mailcloseButton = 1008;
	
	Context mContext;
	UserData userData;

	private ImageDownloader mDownloader;
	
	public HomeMiddle(CCSprite parentSprite, String imageFolder, CCNode nodeThis) {
		mContext = CCDirector.sharedDirector().getActivity();
		userData = UserData.share(mContext);
		
		userInfo(parentSprite, imageFolder, nodeThis);
		this.nodeThis = nodeThis;
	}
	
	private void userInfo(CCSprite parentSprite, String imageFolder, CCNode nodeThis) {
		String userIdStr = FacebookData.getinstance().getUserInfo().getId();
		String userNameStr = FacebookData.getinstance().getUserInfo().getName();
		List<GameScore> gameScores = FacebookData.getinstance().getGameScore();
		
		CCSprite profileBg = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + imageFolder + "home-profileBg-hd" + fileExtension)); // 프로필 백그
		parentSprite.addChild(profileBg, 10, 10);
		profileBg.setPosition(
				parentSprite.getContentSize().width / 2, 
				parentSprite.getContentSize().height - profileBg.getContentSize().height / 2 - 40.0f);
		
		CCSprite profilePicture= CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + commonfolder + "frame-pictureFrame-hd" + fileExtension)); // 프로필 사진 틀
		profilePicture.setPosition(
				profilePicture.getContentSize().width / 2 + 85, 
				profileBg.getContentSize().height - 60);
		
		CCMenuItem post = CCMenuItemImage.item(  // 우편
				CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + imageFolder + "home-mail-hd" + fileExtension)), 
				CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + imageFolder + "home-mail-hd" + fileExtension)), 				 
				nodeThis,"clicked2");
		post.setTag(mailButton);
		post.setAnchorPoint(1.0f, 0.5f);
		
		final CCSprite userImage = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + "noPicture" + fileExtension)); // 프로필 사진
		userImage.setAnchorPoint(0.5f, 0.5f);
		userImage.setPosition(profilePicture.getContentSize().width / 2, profilePicture.getContentSize().height / 2);
		profilePicture.addChild(userImage);
		
		// facebook 이미지 다운로드 및 교체
		String etUrl = "https://graph.facebook.com/" + userIdStr +"/picture";
		mDownloader = new ImageDownloader(etUrl, new ImageLoaderListener() {
			@Override
			public void onImageDownloaded(CCSprite profile) {
				userImage.setTexture(profile.getTexture());
			}
		});
		mDownloader.execute();
		
		
		CCSprite newIcon = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + imageFolder + "home-mailNew-hd01" + fileExtension)); // 새로운 우편
		newIcon.setPosition(
				post.getContentSize().width/2, 
				post.getContentSize().height);
		post.addChild(newIcon);

		CCMenu postMenu = CCMenu.menu(post);

		post.setPosition(0f,0f);
		postMenu.setContentSize(0f, 0f);
		postMenu.setPosition(
			profileBg.getContentSize().width - post.getContentSize().width/2, 
			profileBg.getContentSize().height/2);
		profileBg.addChild(postMenu);
		
		CCSprite levelProgressBar = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + imageFolder + "home-progressBar-hd" + fileExtension)); // 레벨 프로그레스 바
		levelProgressBar.setPosition(
				profileBg.getContentSize().width/2+10f,
				levelProgressBar.getContentSize().height/2+25.0f);
		
		int levelValue = Integer.parseInt(FacebookData.getinstance().getDBData("LevelCharacter"));
		float expValue = Float.parseFloat(FacebookData.getinstance().getDBData("Exp"));
		int levelLimit = userData.expPerLevel.length;
		int expLimit = 999999999;
		String expStr ;
		float scale;
		if (levelValue < levelLimit) {
			expLimit = userData.expPerLevel[levelValue - 1];
			expStr = (int)expValue + " / " + expLimit;
			scale = (levelProgressBar.getContentSize().width - 4) * (expValue / expLimit);
		} else {
			expStr = "MAX";
			scale = levelProgressBar.getContentSize().width - 4;
		}
		
		CCSprite gaugeBar = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + imageFolder + "Home-gaugeBar-hd" + fileExtension)); // 레벨 게이지 바
		levelProgressBar.addChild(gaugeBar);
		gaugeBar.setAnchorPoint(0, 0.5f);
		gaugeBar.setPosition(2, levelProgressBar.getContentSize().height / 2);
		gaugeBar.setScaleX(scale / gaugeBar.getContentSize().width);
		
//		CCLabel exp = CCLabel.makeLabel(expStr, "Arial", 11); // 경험치 (게이지로 변경필요)
//		levelProgressBar.addChild(exp);
////		exp.setColor(ccColor3B.ccc3(194, 216, 233));
////		exp.setColor(ccColor3B.ccc3(0, 35, 102));
//		exp.setColor(ccColor3B.ccc3(50, 50, 0x8c));
//		exp.setPosition(
//				levelProgressBar.getContentSize().width/2,
//				levelProgressBar.getContentSize().height/2);
		
		CCLabel userRanking = CCLabel.makeLabel("0", "Arial", 30); // 순위
		userRanking.setPosition(
				userRanking.getContentSize().width/2+55.0f, 
				profileBg.getContentSize().height-60.0f);
		
		CCLabel userName = CCLabel.makeLabel(userNameStr, "Arial", 30); // 이름
		userName.setPosition(
				profilePicture.getPosition().x
						+ ((1 - profilePicture.getAnchorPoint().x) * profilePicture
								.getContentSize().width) + 12,
				profileBg.getContentSize().height
						- userName.getContentSize().height / 2 - 20);
		userName.setAnchorPoint(0.0f, 0.5f);

		CCLabel score = CCLabel.makeLabel(new NumberComma()
				.numberComma(FacebookData.getinstance().getDBData("Point")),
				"Arial", 30); // 최고 점수
		score.setPosition(userName.getPosition().x,
				profileBg.getContentSize().height
						- score.getContentSize().height / 2 - 60.0f);
		score.setAnchorPoint(0.0f, 0.5f);
		
		int myRank = 1;
		int myScore = 1;
		
		for (GameScore gameScore : gameScores) {
			if (gameScore.getId().equals(userIdStr)) {

				myScore = gameScore.score;
				score.setString(String.valueOf(myScore));
			}
		}
		
		for (GameScore gameScore : gameScores) {
			if (gameScore.score > myScore) {
				myRank++;
			}
		}
		
		Log.e("HomeMiddle", "rank : " + myRank + ", score : " + myScore);
		userRanking.setString(String.valueOf(myRank));
		
		
			userRanking.setString(String.valueOf(myRank));
				String[] Ko = {"전 : ","승 / ","패"," 18"};
				String[] En = {"P "," : W "," / L "," 18"};
							
				String record = null;
				float recordSize;
				if (Locale.getDefault().getLanguage().toString().equals("en")) {
					record =  
							En[0] + 	(Integer.parseInt(FacebookData.getinstance().getDBData("HistoryWin")) +
											Integer.parseInt(FacebookData.getinstance().getDBData("HistoryLose"))) +
							En[1] + 	FacebookData.getinstance().getDBData("HistoryWin") + 
							En[2] + 	FacebookData.getinstance().getDBData("HistoryLose");
					recordSize = Float.parseFloat(En[3]);
				} else {
					record = 
							(Integer.parseInt(FacebookData.getinstance().getDBData("HistoryWin")) +
							Integer.parseInt(FacebookData.getinstance().getDBData("HistoryLose"))) + Ko[0] +
							FacebookData.getinstance().getDBData("HistoryWin") + Ko[1] + 
							FacebookData.getinstance().getDBData("HistoryLose") + Ko[2];
					recordSize = Float.parseFloat(Ko[3]);
				}
			
		CCLabel  recordData = CCLabel.makeLabel(record, "Arial", recordSize); // 전적 값
		recordData.setPosition(
				userName.getPosition().x,
				profilePicture.getPosition().y - (profilePicture.getAnchorPoint().y * profilePicture.getContentSize().height)
				);
		recordData.setAnchorPoint(0, 1);

		
		CCLabel  level = CCLabel.makeLabel("Lv "+ FacebookData.getinstance().getDBData("LevelCharacter"), "Arial", 40.0f); // 레벨
		level.setPosition(
				level.getContentSize().width/2+115.0f, 
				profileBg.getContentSize().height- level.getContentSize().height/2-110.0f);
		level.setAnchorPoint(1.0f, 0.5f);
		level.setColor(ccColor3B.ccYELLOW);
		
		profileBg.addChild(profilePicture);
		profileBg.addChild(levelProgressBar);
		profileBg.addChild(userRanking);
		profileBg.addChild(userName);
		profileBg.addChild(score);
		profileBg.addChild(recordData);
		profileBg.addChild(level);
	}
	
}
