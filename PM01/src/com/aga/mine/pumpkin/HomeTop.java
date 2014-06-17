package com.aga.mine.pumpkin;

import java.util.Calendar;
import java.util.Locale;

import org.cocos2d.actions.UpdateCallback;
import org.cocos2d.layers.CCLayer;
import org.cocos2d.menus.CCMenu;
import org.cocos2d.menus.CCMenuItem;
import org.cocos2d.menus.CCMenuItemImage;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCLabel;
import org.cocos2d.nodes.CCLabelAtlas;
import org.cocos2d.nodes.CCNode;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.nodes.CCTextureCache;
import org.cocos2d.types.CGSize;
import org.cocos2d.types.ccColor3B;

import com.aga.mine.util.Util;
import com.aga.mine.util.UtilResource;

import android.util.Log;

public class HomeTop extends CCLayer{

	public final int BROOMSTICK_REFRESH_TIME = 900; //900초(15분)
	final String commonfolder = "00common/";
	final String fileExtension = ".png";
	
	final int broomstickButton = 1001;
	final int goldButton = 1002;
	final int shopButton = 1003;
	final int enterButton = 1004;
	final int optionButton = 1005;
	final int inviteButton = 1006;
	
	private static CCLabel broomstickEA;
	private static CCLabel gold;
			
	//주간 순위 갱신 변수
	private float mSecondToRefreshWeek; //단위는 sec, 주간순위를 갱신하기까지 남은 시간
	private long mInitialSecond; //단위는 ms, 최초 시간
	private CCLabel periodText;
	
	//빗자루 갱신 변수
	private int mBroomstickCount; //빗자루 수량
	private long mMiliToRefreshBroom; //단위는 ms, 빗자루를 갱신하기까지 남은 시간
	private CCLabel broomstickTime;
	
	private CGSize winsize() {
		return CCDirector.sharedDirector().winSize();
	}
	
	public HomeTop(CCSprite parentSprite, String imageFolder, CCNode nodeThis) {
		//빗자루 수량 초기화
		mBroomstickCount = Integer.parseInt(FacebookData.getinstance().getDBData("ReceivedBroomstick"));
		
		// 빗자루 수량 최고값이 6으로 되어있어 풀어주는걸로 수정하였습니다.
		
		// 그리고 남은시간은 빗자루수가 6개 미만일시만 돌아야되고 6개가 될시 초기화 되야 합니다.
		// 현재 백그라운드에서 계속 도는건지 초기화가 안되는건지 6개에서 사용하고 돌아와보면 사용한 시간과 일치하지 않은 남은시간이 되어있습니다.
		if (mBroomstickCount < 6) {
			//mBroomstickCount = 2; //test
			long pastTime = Util.getBroomstickTime(); //경과 시간(ms), 
			int broomCount = (int)(pastTime/(BROOMSTICK_REFRESH_TIME * 1000)); //15분 경과시 1개로 빗자루 수량 계산
			if(mBroomstickCount + broomCount >= 6) {
				mBroomstickCount = 6;
			} else {
				mBroomstickCount += broomCount;
				mMiliToRefreshBroom = pastTime%(BROOMSTICK_REFRESH_TIME*1000); //빗자루 수량과 남은시간 계산후 다시 pref에 세팅
				Util.setBroomstickTime(mMiliToRefreshBroom);
			}
			FacebookData.getinstance().modDBData("ReceivedBroomstick", String.valueOf(mBroomstickCount)); //DB에 빗자루 수량 insert
		}
		
		
		setTopMenu(parentSprite, imageFolder, nodeThis);
		
		//주간순위 시간 초기화
		mSecondToRefreshWeek = DataFilter.getInitTime();
		mInitialSecond = System.currentTimeMillis();
		
		setTitle(parentSprite, imageFolder);
		setBottomMenu(parentSprite, imageFolder, nodeThis);
	}

	// 상단 메뉴
	private void setTopMenu(CCSprite parentSprite, String imageFolder, CCNode nodeThis) {
		
		// 지팡이 상점 바로가기 배경(버튼)
		CCMenuItem broomstickBg = CCMenuItemImage.item(
				CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + imageFolder	+ "home-broomstickBg-hd" + fileExtension)), 
				CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + imageFolder	+ "home-broomstickBg-hd" + fileExtension)), 
				nodeThis, "clicked2");
		broomstickBg.setTag(broomstickButton);
		
		// 골드 상점 바로가기 배경(버튼)
		CCMenuItem goldBg = CCMenuItemImage.item(
				CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + imageFolder + "home-goldBg-hd" + fileExtension)),
				CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + imageFolder + "home-goldBg-hd" + fileExtension)), 
				nodeThis, "clicked2");
		goldBg.setTag(goldButton);
		
		CCMenu topMenu = CCMenu.menu(broomstickBg, goldBg);
		parentSprite.addChild(topMenu);
		
		topMenu.setColor(ccColor3B.ccc3(255, 255, 255));
		topMenu.setContentSize(CGSize.make(
				parentSprite.getContentSize().width - 80.0f,
				broomstickBg.getContentSize().height));
		topMenu.setPosition(
				35.0f, parentSprite.getContentSize().height - broomstickBg.getContentSize().height / 2 - 65.0f);

		broomstickBg.setPosition(broomstickBg.getContentSize().width / 2, 0f);
		goldBg.setPosition(topMenu.getContentSize().width - goldBg.getContentSize().width/ 2, 0f);

		 // 지팡이 그림
		CCSprite broomstickImg = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + imageFolder + "home-broomstickOn-hd" + fileExtension));
		broomstickBg.addChild(broomstickImg);
		broomstickImg.setPosition(
				broomstickImg.getContentSize().width / 2 + 10.0f,
				broomstickBg.getContentSize().height / 2);
		

		// 지팡이 수량
		broomstickEA = CCLabel.makeLabel("+" + mBroomstickCount, "Arial", 24.0f); // 지팡이 수량
		broomstickBg.addChild(broomstickEA);
		broomstickEA.setAnchorPoint(0, 0.5f);
		broomstickEA.setPosition(
				broomstickImg.getPosition().x + ((1 - broomstickImg.getAnchorPoint().x) * broomstickImg.getContentSize().width) - 2, 
				broomstickBg.getContentSize().height - broomstickEA.getContentSize().height / 2 - 5.0f);

		 // 지팡이 수량 증가 (카운트 다운)
		broomstickTime = CCLabel.makeLabel("Loading...", "Arial", 20.0f);
		broomstickBg.addChild(broomstickTime, 0, 2);
		broomstickTime.setPosition(
				broomstickBg.getContentSize().width - 10.0f,
				broomstickTime.getContentSize().height / 2 + 5.0f);
		broomstickTime.setAnchorPoint(1.0f, 0.5f);

		// 골드
		gold = CCLabel.makeLabel(
				new NumberComma().numberComma(FacebookData.getinstance().getDBData("Gold")), "Arial", 22.0f);
		goldBg.addChild(gold);
		gold.setPosition(goldBg.getContentSize().width - 10.0f, gold.getContentSize().height / 2 + 5.0f);
		gold.setAnchorPoint(1.0f, 0.5f);
	}

	private void setTitle(CCSprite parentSprite, String imageFolder) {
		
		// 타이틀 판넬
		CCSprite titlePanel = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + commonfolder + "frame-titlePanel" + fileExtension));
		parentSprite.addChild(titlePanel);
		titlePanel.setAnchorPoint(0.5f, 0.5f);
		titlePanel.setPosition(
			parentSprite.getContentSize().width / 2,
			parentSprite.getContentSize().height - 98);
		
		// 타이틀
		CCSprite frameTitle = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + 
				Utility.getInstance().getNameWithIsoCodeSuffix(
						imageFolder + imageFolder.substring(2, imageFolder.length()-1) + "-title" + fileExtension)));
		titlePanel.addChild(frameTitle);
		frameTitle.setPosition(titlePanel.getContentSize().width / 2, titlePanel.getContentSize().height / 2);
			
		// 경기 기간 배경
		CCSprite banner = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + commonfolder + "titlebanner" + fileExtension));
		titlePanel.addChild(banner);
		banner.setAnchorPoint(0.5f, 1);
		banner.setPosition(titlePanel.getContentSize().width / 2, 10);

		// 주간순위 마감 남은 시간
		periodText = CCLabel.makeLabel(displayLeftTime(mSecondToRefreshWeek), "Arial", 21);
		banner.addChild(periodText);
		periodText.setColor(ccColor3B.ccYELLOW);
		periodText.setAnchorPoint(0.5f, 0.5f);
		periodText.setPosition(
				banner.getContentSize().width / 2, 
				banner.getContentSize().height / 2);
	}
	
	public void getDBData() {
		mBroomstickCount = Integer.parseInt(FacebookData.getinstance().getDBData("ReceivedBroomstick"));
		broomstickEA.setString(String.valueOf(mBroomstickCount));
		gold.setString(new NumberComma().numberComma(FacebookData.getinstance().getDBData("Gold")));
	}
	
	public void setLeftTime(float dt) {
		//주간 순위 마감 시간 갱신
		long pastTimeMilli = System.currentTimeMillis() - mInitialSecond; //경과 시간 계산
		periodText.setString(String.valueOf(displayLeftTime(mSecondToRefreshWeek - pastTimeMilli/1000f)));
		
		//빗자루 갱신 시간 갱신 : 수량이 6이하일때만 계산
		if(mBroomstickCount < 6) {
			long broomTime = BROOMSTICK_REFRESH_TIME * 1000 - (mMiliToRefreshBroom + pastTimeMilli); //15분까지 남은 시간 계산
			broomstickTime.setString(String.valueOf(displayBroomTime(broomTime/1000f)));
		} else {
			broomstickTime.setString("00:00");
		}
	}
	
	private String displayLeftTime(float secondOfFloat) {
		int secondOfInt;
		
		if(secondOfFloat < 0) {
			mSecondToRefreshWeek = DataFilter.getInitTime();
			mInitialSecond = System.currentTimeMillis();
			secondOfInt = (int) mSecondToRefreshWeek;
		} else {
			secondOfInt = (int) secondOfFloat;
		}
		
		long day = secondOfInt / (60 * 60 * 24);
		long hour = (secondOfInt % (60 * 60 * 24)) / (60 * 60);
		long min = (secondOfInt % (60 * 60)) / 60;
		long sec = secondOfInt % 60;
		
		String deadlineText = "";
		String[] timeStr = {"day ","hour ","min"};
		if (isKorean()) {
			timeStr = new String[]{"일 ","시 ","분 후 마감"};
		}
		
//		if (day > 0) 
			deadlineText = String.format("%01d",day) + timeStr[0];
//		if (hour > 0)
			deadlineText += String.format("%02d",hour) + timeStr[1];
//		if (min > 0) 
			deadlineText += String.format("%02d",min) + timeStr[2];
//		deadlineText += sec + "초 후 마감";
		return deadlineText;
	}
	
	private String displayBroomTime(float secondOfFloat) {
		int secondOfInt;
		
		if(secondOfFloat < 0) {
			secondOfInt = 0;
			mMiliToRefreshBroom = 0;
			mBroomstickCount++;
			Util.setBroomstickTime();
			FacebookData.getinstance().modDBData("ReceivedBroomstick", String.valueOf(mBroomstickCount));
		} else {
			secondOfInt = (int) secondOfFloat;
		}
		
		long min = (secondOfInt % (60 * 60)) / 60;
		long sec = secondOfInt % 60;
		
		String deadlineText = String.format("%02d:%02d", (int)min, (int)sec);

		return deadlineText;
	}
	
	// 하단 메뉴
	private void setBottomMenu(CCSprite parentSprite, String imageFolder, CCNode nodeThis) {
		
		// 좌측 버튼(상점)
		CCMenuItem button1 = SpriteSummery.menuItemBuilder(
						imageFolder + "home-shopbutton1" + fileExtension,
						Utility.getInstance().getNameWithIsoCodeSuffix(imageFolder + "home-shoptext1" + fileExtension),
						imageFolder + "home-shopbutton2" + fileExtension,
						Utility.getInstance().getNameWithIsoCodeSuffix(imageFolder + "home-shoptext2" + fileExtension),
						nodeThis, "clicked2");
		button1.setTag(shopButton);
		
		//중앙 버튼(입장)
		CCMenuItem button2 = SpriteSummery.menuItemBuilder(
				imageFolder + "home-enterbutton1" + fileExtension,
				Utility.getInstance().getNameWithIsoCodeSuffix(imageFolder + "home-entertext1" + fileExtension),
				imageFolder + "home-enterbutton2" + fileExtension,
				Utility.getInstance().getNameWithIsoCodeSuffix(imageFolder + "home-entertext2" + fileExtension),
				nodeThis, "clicked2");
		button2.setTag(enterButton);
		
		// 우측 버튼(설정)
		CCMenuItem button3 = SpriteSummery.menuItemBuilder(
				imageFolder + "home-optionbutton1" + fileExtension,
				Utility.getInstance().getNameWithIsoCodeSuffix(imageFolder + "home-optiontext1" + fileExtension),
				imageFolder + "home-optionbutton2" + fileExtension,
				Utility.getInstance().getNameWithIsoCodeSuffix(imageFolder + "home-optiontext2" + fileExtension),
				nodeThis, "clicked2");
		button3.setTag(optionButton);
		
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
		CCMenuItem friendsInvite = SpriteSummery.menuItemBuilder(
				imageFolder + "home-invitebutton1" + fileExtension,
				Utility.getInstance().getNameWithIsoCodeSuffix(imageFolder + "home-invitetext1" + fileExtension),
				imageFolder + "home-invitebutton2" + fileExtension,
				Utility.getInstance().getNameWithIsoCodeSuffix(imageFolder + "home-invitetext2" + fileExtension),
				nodeThis, "clicked2");
		friendsInvite.setTag(inviteButton);
		
		CCMenu inviteMenu = CCMenu.menu(friendsInvite);
		parentSprite.addChild(inviteMenu);
		inviteMenu.setContentSize(parentSprite.getContentSize());
		inviteMenu.setPosition(0,0);
		inviteMenu.setAnchorPoint(0,0);
		
		friendsInvite.setPosition((595 * winsize().width) / parentSprite.getBoundingBox().size.width ,
		inviteMenu.getContentSize().height - 710);
		friendsInvite.setAnchorPoint(1, 1);
	}
	
	public static void getDB() {
		broomstickEA.setString("+" + FacebookData.getinstance().getDBData("ReceivedBroomstick"));
		gold.setString(new NumberComma().numberComma(FacebookData.getinstance().getDBData("Gold")));
	}
	
	private boolean isKorean() {
		if (Locale.getDefault().getLanguage().toString().equals("ko"))
			return true;
		else
			return false; 
	}

	
}
