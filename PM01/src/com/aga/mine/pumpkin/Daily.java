package com.aga.mine.pumpkin;

import org.cocos2d.actions.instant.CCCallFunc;
import org.cocos2d.actions.interval.CCScaleTo;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.layers.CCLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCLabel;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.nodes.CCTextureCache;
import org.cocos2d.sound.SoundEngine;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.ccColor3B;

import com.aga.mine.util.UtilResource;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;

public class Daily extends CCLayer {

	final String tag = "Daily";
	final String folder = "03daily/";
	final String fileExtension = ".png";

	CCSprite bg;
	int dailyCount = 0;

	private CCSprite stamp;
	Context mContext;

	static CCScene scene(int dailyCount) {
		CCScene scene = CCScene.node();
		CCLayer layer = new Daily(dailyCount);
		scene.addChild(layer);
		return scene;
	}

	public Daily(int dailyCount) {
		mContext = CCDirector.sharedDirector().getActivity();
		SoundEngine.sharedEngine().preloadEffect(mContext, R.raw.stamp);
		this.dailyCount = dailyCount;
		bg = BackGround.setBackground(this, CGPoint.make(0.5f, 0.5f), folder
				+ "daily-bg" + fileExtension);
		setMain();
		schedule("nextCallback", 20);
		this.setIsTouchEnabled(true);
	}

	private void setMain() {
		setTitle();
		closeImage();
		gold(bg, dailyCount);
		goldtext(bg, dailyCount);
	}

	// 출석부 타이틀
	private void setTitle() {
		CCSprite title = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + 
				Utility.getInstance().getNameWithIsoCodeSuffix(
						folder + "daily-title" + fileExtension)));
		bg.addChild(title);
		title.setPosition(bg.getContentSize().width / 2,
				bg.getContentSize().height - title.getContentSize().height
						* 1.85f);
		title.setAnchorPoint(0.5f, 0.5f);
	}

	private void closeImage() {
		CCSprite close = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + folder + "daily-close3"
				+ fileExtension));
		bg.addChild(close, 1, 777);
		close.setPosition(525, bg.getContentSize().height - 178);
		close.setAnchorPoint(0, 0);
	}

	// 출석부 gold 이미지
	private void gold(CCSprite parent, int dailyCount) {
		int[] goldX = { 100, 204, 308, 410, 517 };
		int[] goldY = { 1136 - 370, 1136 - 479, 1136 - 590, 1136 - 707,
				1136 - 820, 1136 - 940 };
		int count = 1;

		for (int k = 0; k < 6; k++) {
			for (int i = 0; i < 5; i++) {
				CCSprite gold = null;
				if ((i + 1) % 5 != 0) {
					if (dailyCount < count) {
						gold = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + folder + "daily-coin1.png"));
					} else {
						gold = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + folder + "daily-coin2.png"));
					}
				} else {
					if (dailyCount < count) {
						gold = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + folder + "daily-pack1.png"));
					} else {
						gold = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + folder + "daily-pack2.png"));
					}
				}
				parent.addChild(gold, 1);
				gold.setPosition(goldX[i] + 10, goldY[k]);

				// 도장 찍기
				if (dailyCount > count) {
					CCSprite stamp = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + 
							Utility.getInstance().getNameWithIsoCodeSuffix(
									folder + "stamp" + fileExtension)));
					parent.addChild(stamp, 100);
					stamp.setPosition(goldX[i], goldY[k]);
					stamp.setAnchorPoint(0.5f, 0.5f);
					stamp.setScale(0.23f);
				} else if (dailyCount == count) {
					Log.e("Daily", "도장찍기 애니메이션 넣을 곳 입니다.");
					Log.e("Daily", "stamp animation");

					stamp = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + 
							Utility.getInstance().getNameWithIsoCodeSuffix(folder + "stamp" + fileExtension)));
					parent.addChild(stamp, 100);
					stamp.setPosition(goldX[i], goldY[k]);
					stamp.setAnchorPoint(0.5f, 0.5f);
					//stamp.setScale(0.23f);
					
					//스탬프 애니메이션
					CCScaleTo action1 = CCScaleTo.action(0.25f, 0.23f);
					CCCallFunc action2 = CCCallFunc.action(this, "soundStamp");
					stamp.runAction(CCSequence.actions(action1, action2));
				}

				count++;
			}
		}
	}
	
	public void soundStamp() {
		SoundEngine.sharedEngine().playEffect(mContext, R.raw.stamp);
	}

	// 출석부 gold 및 보상 가격 text
	private void goldtext(CCSprite parent, int dailyCount) {
		int[] textX = { 100, 204, 308, 410, 517 };
		int[] textY = { 1136 - 338, 1136 - 445, 1136 - 555, 1136 - 670,
				1136 - 787, 1136 - 894 };
		int[] reward = { 100, 200, 300, 400, 1000, 500, 600, 700, 800, 2000,
				900, 1000, 1100, 1200, 3000, 1300, 1400, 1500, 1600, 4000,
				1700, 1800, 1900, 2000, 5000, 2100, 2200, 2300, 2400, 10000 };

		int count = 0;

		for (int k = 0; k < 6; k++) {
			for (int i = 0; i < 5; i++) {
				CCLabel goldText = CCLabel.makeLabel("Gold", "Arial", 16.0f);
				parent.addChild(goldText, 10);
				goldText.setColor(ccColor3B.ccBLACK);
				goldText.setAnchorPoint(0, 1);
				goldText.setPosition(textX[i] - 45, textY[k]);

				CCLabel goldValue = CCLabel.makeLabel("" + reward[count],
						"Arial", 22.0f);
				parent.addChild(goldValue, 20);
				goldValue.setColor(ccColor3B.ccBLACK);
				goldValue.setAnchorPoint(1, 1);
				goldValue.setPosition(textX[i] + 50, textY[k]);

				count++;
			}
		}

		Log.e("Daily", "우편 보내기 / Gold " + reward[dailyCount - 1]);
		// 우편 보내기
		String gold = String.valueOf(reward[dailyCount - 1]);
		long requestID = (long) (Math.random() * 72036854775807L);  //facebook 알림글번호로 대체할 것
		String recipientID = FacebookData.getinstance().getUserInfo().getId(); // 상점 이동 방식에 따른 ID 변경
		String senderID = "1";
		String data = 
				"0,RequestModeMailBoxAdd*22," + requestID + "*1," + recipientID + "*19," + senderID + "*20,Gold*21," + gold;				
		DataFilter.sendMail(data);
	}

	@Override
	public boolean ccTouchesBegan(MotionEvent event) {
		bg.removeChildByTag(777, true);
		CCSprite close = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + folder + "daily-close4"
				+ fileExtension));
		bg.addChild(close, 2);
		close.setPosition(525, bg.getContentSize().height - 178);
		close.setAnchorPoint(0, 0);
		return super.ccTouchesBegan(event);
	}

	@Override
	public boolean ccTouchesEnded(MotionEvent event) {
		nextCallback(10f);
		MainApplication.getInstance().getActivity().click();
		this.setIsTouchEnabled(false);
		return super.ccTouchesEnded(event);
	}

	public void nextCallback(float dt) {
		CCScene scene = Home.scene();
		CCDirector.sharedDirector().replaceScene(scene);
	}

}
