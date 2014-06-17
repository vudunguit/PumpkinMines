package com.aga.mine.pumpkin;

import java.util.Locale;

import org.cocos2d.actions.UpdateCallback;
import org.cocos2d.actions.base.CCRepeatForever;
import org.cocos2d.actions.instant.CCCallFunc;
import org.cocos2d.actions.interval.CCAnimate;
import org.cocos2d.actions.interval.CCScaleTo;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.events.CCTouchDispatcher;
import org.cocos2d.layers.CCLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.menus.CCMenu;
import org.cocos2d.menus.CCMenuItem;
import org.cocos2d.menus.CCMenuItemImage;
import org.cocos2d.menus.CCMenuItemToggle;
import org.cocos2d.nodes.CCAnimation;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCNode;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.nodes.CCSpriteFrameCache;
import org.cocos2d.nodes.CCSpriteSheet;
import org.cocos2d.nodes.CCTextureCache;
import org.cocos2d.types.CGPoint;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.aga.mine.pages2.GameData;
import com.aga.mine.util.UtilResource;

public class Login extends CCLayer{
	
    private static final String TAG = "Login";
	
	final String folder = "02appload/";
	final String fileExtension = ".png";
	
	boolean isTermsOK = false;
	
	final int kButtonOff = 0;
	final int kButtonOn = 1;
	final int aniTag = 100;
	final int termsTag = 200;
	final int disableButtonTag = 100;
	final int toggleButtonTag = 200;
	
	CCSprite bg;
	CCMenu loginMenu;
	CCMenuItem termsOK;
	
	public static CCScene scene() {
		CCScene scene = CCScene.node();
		Login layer = new Login();
		scene.addChild(layer);
		return scene;	
	}
	
	CCSpriteFrameCache cache;
	
	public Login() {
		bg = BackGround.setBackground(this, CGPoint.make(0.5f, 0), folder + "appload-bg" + fileExtension);
		cache = CCSpriteFrameCache.sharedSpriteFrameCache();
		cache.addSpriteFrames(folder + "loading-magician.plist");
		CCSpriteSheet.spriteSheet(folder + "loading-magician.png"); 
		
		//progress 애니메이션
		CCAnimation progress = CCAnimation.animation("progress");
		for( int i=1;i<=12;i++) {
			progress.addFrame(cache.getSpriteFrame((String.format("f%02d.png", i))));
		}
		
		//마법사 애니메이션
		CCAnimation wizard = CCAnimation.animation("wizard");
		for( int i=1;i<=2;i++) {
			wizard.addFrame(cache.getSpriteFrame((String.format("loading-magician%02d.png", i))));
		}
		
		mCircularProgress = CCAnimate.action(1.2f, progress, false);
		mWizard = CCAnimate.action(0.6f, wizard, false);
		
		setMain(bg);
		
	    if(MainApplication.getInstance().getActivity().mSimpleFacebook.isLogin()) {
		    MainApplication.getInstance().mHandler.post(new Runnable() {
	            @Override
	            public void run() {
	                nextSceneCallback();
	            }
	        });
	    } else {
	    	terms(bg);
			setForeground(bg);
	    }
	}
	
	private void terms(CCSprite bg) {
		//
		CCSprite termsBody = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + 
				Utility.getInstance().getNameWithIsoCodeSuffix(folder + "gameTermsBg" + fileExtension)));
		termsBody.setPosition(bg.getContentSize().width/2, bg.getContentSize().height/2);
		termsBody.setAnchorPoint(0.5f ,0.5f);
		bg.addChild(termsBody, termsTag , termsTag);
		
		//
		String termsStr = UtilResource.RESOURCE_PATH + Utility.getInstance().getNameWithIsoCodeSuffix(folder + "gameTermsTitle" + fileExtension);
		CCMenuItem termsUrl = CCMenuItemImage.item(
				CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(termsStr)), 
				CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(termsStr)),
				this, 
				"cilcked");
		termsUrl.setTag(0);
		CCMenu policyMenu = CCMenu.menu(termsUrl);
		policyMenu.setPosition(termsBody.getContentSize().width * 0.5f, termsBody.getContentSize().height * 0.55f);
		termsBody.addChild(policyMenu);
		
		// button
//		CCMenuItem termsCheck = CCMenuItemImage.item(
//				folder + "gameTermsCheck1" + fileExtension,
//				folder + "gameTermsCheck2" + fileExtension,
//				this, "cilcked");
//		termsCheck.setTag(1);
//		CCMenu checkMenu = CCMenu.menu(termsCheck);
		CCMenu checkMenu = CCMenu.menu();
		checkMenu.setPosition(termsBody.getContentSize().width * 0.92f, termsBody.getContentSize().height *0.32f);
		termsBody.addChild(checkMenu);

		// toggle
		String fOn = folder + "gameTermsCheck1" + fileExtension;
		String fOff = folder + "gameTermsCheck2" + fileExtension;
		CCMenuItemImage itemOn = CCMenuItemImage.item(fOn, fOn);
		CCMenuItemImage itemOff = CCMenuItemImage.item(fOff, fOff);
		
		CCMenuItemToggle itemToggle = CCMenuItemToggle.item(this, "cilcked", itemOn, itemOff);
		itemToggle.setSelectedIndex(kButtonOff);
		itemToggle.setTag(toggleButtonTag);
		checkMenu.addChild(itemToggle);
		
		//
		termsOK = CCMenuItemImage.item(
				Utility.getInstance().getNameWithIsoCodeSuffix(folder + "gameTermsOkImage1" + fileExtension),
						Utility.getInstance().getNameWithIsoCodeSuffix(folder + "gameTermsOkImage2" + fileExtension),
				this, "cilcked");
		termsOK.setTag(2);
		
		CCMenu okMenu = CCMenu.menu(termsOK);
		okMenu.setIsTouchEnabled(false);
		okMenu.setPosition(termsBody.getContentSize().width *0.5f, termsBody.getContentSize().height *0.14f);
		termsBody.addChild(okMenu);
		
		// termsOK disable image
		CCSprite disableButton = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH +
				Utility.getInstance().getNameWithIsoCodeSuffix(folder + "gameTermsOkImage3" + fileExtension)));
		disableButton.setPosition(termsOK.getContentSize().width/2, termsOK.getContentSize().height/2);
		termsOK.addChild(disableButton, disableButtonTag, disableButtonTag);
	}
	


	private void setMain(CCSprite bg) {
		//게임이름
		CCSprite title = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + 
				Utility.getInstance().getNameWithIsoCodeSuffix(folder + "game-Woodtitle" + fileExtension)));
		title.setPosition(bg.getContentSize().width/2, bg.getContentSize().height/2);
		title.setAnchorPoint(0.5f ,0.5f);
		bg.addChild(title, aniTag, aniTag);
		
		//로딩바 배경
		CCSprite loadingMap = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + folder + "countbg" + fileExtension));
		loadingMap.setPosition(bg.getContentSize().width/2, bg.getContentSize().height/2);
		loadingMap.setAnchorPoint(0.5f ,0.5f);
		bg.addChild(loadingMap, aniTag + 1,  aniTag + 1);
		
//		// 로딩바
//		CCSprite loadingBar = CCSprite.sprite(folder + "c01" + fileExtension);
//		loadingBar.setPosition(bg.getContentSize().width/2, bg.getContentSize().height/2 + 0);
//		loadingBar.setAnchorPoint(0.5f ,0.5f);
//		bg.addChild(loadingBar, aniTag + 2,  aniTag + 2);
		
		// 냄비
		CCSprite pot = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + folder + "pot" + fileExtension));
		pot.setPosition(bg.getContentSize().width/2, bg.getContentSize().height/2 + 116);
		pot.setAnchorPoint(0.5f ,0.5f);
		bg.addChild(pot, aniTag + 3,  aniTag + 3);

//		// 마법사
//		CCSprite wizard = CCSprite.sprite(folder + "wizard01" + fileExtension);
//		bg.addChild(wizard);
//		wizard.setPosition(bg.getContentSize().width/2, bg.getContentSize().height/2 + 116);
//		wizard.setAnchorPoint(0.5f ,0.5f);
		
		CCDirector.sharedDirector().getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				actionWizard();
				actionProgress();
			}
		});
	}
	
	private void setForeground(CCSprite bg) {
		//페이스북
		CCMenuItem facebook = CCMenuItemImage.item(
				CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + folder + "Facebook-normal" + fileExtension)),
				CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + folder + "Facebook-select" + fileExtension)),
				this, "facebookCallback");
//		facebook.setAnchorPoint(0.5f, 0.5f);
		
		//게스트
		CCMenuItem guest = CCMenuItemImage.item(
				CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + folder + "Guest-normal" + fileExtension)),
				CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + folder + "Guest-select" + fileExtension)),
				this, "guestCallback");
//		guest.setAnchorPoint(0.5f, 0.5f);
		
		loginMenu = CCMenu.menu(facebook, guest);
		loginMenu.setContentSize(facebook.getContentSize());
		loginMenu.setAnchorPoint(0,0); // menu는 앙카포인트가 레알 이상함.. 도저히 모르겠음..
//		loginMenu.setAnchorPoint(0.5f ,0.5f);
		loginMenu.setPosition(
				bg.getContentSize().width/2,
				bg.getContentSize().height * 0.12f + loginMenu.getContentSize().height/2);
		loginMenu.setScale(0.2f);
		loginMenu.alignItemsVertically(10);		
		loginMenu.setIsTouchEnabled(false);
		loginMenu.setVisible(false);
		bg.addChild(loginMenu);
	}
	
	
	public CCAnimate mWizard;
	public CCAnimate mCircularProgress;
	
	public void actionProgress() {
		CCSprite loadingBar = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + folder + "c01" + fileExtension));
//		loadingBar.setPosition(bg.getContentSize().width/2, bg.getContentSize().height/2 + 0);
		// plist이미지(f01~12)와 낱개(c01) 이미지의 위치가 다름
		// plist를 만지는 툴이 없으므로 위치를 변경함.
		loadingBar.setPosition(bg.getContentSize().width/2, bg.getContentSize().height/2 + 114);
		loadingBar.setAnchorPoint(0.5f ,0.5f);
		bg.addChild(loadingBar, aniTag + 2,  aniTag + 2);
		CCRepeatForever repeat = CCRepeatForever.action(CCSequence.actions(mCircularProgress));
		loadingBar.runAction(repeat);
	}
	
	public void actionWizard() {
		CCSprite wizard = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + folder + "wizard01" + fileExtension));
		// 마법사 위치는 맞기때문에 그냥 놔둠.(신기신기)
		wizard.setPosition(bg.getContentSize().width/2, bg.getContentSize().height/2 + 116);
		wizard.setAnchorPoint(0.5f ,0.5f);
		bg.addChild(wizard, aniTag + 4, aniTag + 4);
//		CCCallFuncN _removeAction = CCCallFuncN.action(this, "removeAction");
//		wizard.runAction(CCSequence.actions(mPumpkinBomb, _removeAction));
		CCRepeatForever repeat = CCRepeatForever.action(CCSequence.actions(mWizard));
		wizard.runAction(repeat);
	}
	
	public void removeAction(Object sender) {
		CCSprite sprite = (CCSprite)sender;
		sprite.removeFromParentAndCleanup(true);
	}	
	
	public void facebookCallback(Object sender){
		MainApplication.getInstance().mHandler.post(new Runnable() {
			@Override
			public void run() {
				MainApplication.getInstance().getActivity().click();
				MainApplication.getInstance().getActivity().loginFaceBook();
			}
		});
	}

	public void guestCallback(Object sender) {
		MainApplication.getInstance().getActivity().click();
		GameData.share().isGuestMode = true;
		CCScene scene = Home2.scene();
		CCDirector.sharedDirector().replaceScene(scene);
	}
	
	public void cilcked(Object sender) {
		MainApplication.getInstance().getActivity().click();
		int tag = ((CCNode)sender).getTag();
		Log.e(TAG, "tag : " + tag);
		switch (tag) {
		case 0:
			String address = "http://agatong.co.kr/policy/policy_eng.html";
			if (Locale.getDefault().getLanguage().toString().equals("ko"))
				address = "http://agatong.co.kr/policy/policy_kor.html";
			webViewer(address);
			break;
			
//		case 1:	
//			isTermsOK = !isTermsOK;
//			termsOK.getChildByTag(disableButtonTag).setVisible(!isTermsOK);
//			((CCMenu)termsOK.getParent()).setIsTouchEnabled(isTermsOK);
//			break;
			
		case 2:
			bg.removeChildByTag(termsTag, true);
			loginMenu.setVisible(true);
			aniEffect();
			break;
			
		case toggleButtonTag:
			CCMenuItemToggle _Toggle = (CCMenuItemToggle)sender;
			if (_Toggle.selectedIndex() == kButtonOn)
				_Toggle.setSelectedIndex(kButtonOn);
			else
				_Toggle.setSelectedIndex(kButtonOff);
			isTermsOK = !isTermsOK;
			termsOK.getChildByTag(disableButtonTag).setVisible(!isTermsOK);
			((CCMenu)termsOK.getParent()).setIsTouchEnabled(isTermsOK);
			
			break;
		}
	}
	
	private void webViewer(String address) {
		Uri uri = Uri.parse(address);
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		CCDirector.sharedDirector().getActivity().startActivity(intent);
	}
	
	private void aniEffect() {
		CCScaleTo action1 = CCScaleTo.action(0.6f, 1);
		CCCallFunc action2 = CCCallFunc.action(this, "buttonTouch");		
		loginMenu.runAction(CCSequence.actions(action1, action2));
	}
	
	public void buttonTouch() {
		loginMenu.setIsTouchEnabled(true);
	}
	
	public void nextSceneCallback() {
	    MainActivity mMainActivity = MainApplication.getInstance().getActivity();	    
	        //get myProfile and get Friends info
	        //go to daily scene
	        mMainActivity.getMyProfile();
	        mMainActivity.getFriendsInfo();
	}
	
	@Override
	public void onExit() {
		Log.e(TAG, "onExit");
		// 뭔지 대부분 모름.
		cache.removeAllSpriteFrames();
		
//		CCDirector.sharedDirector().purgeCachedData(); // 전부 다 날아감.
		
//		this.removeAllChildren(true); //removeAllChildrenWithCleanup 이게 없어서 대신 씀.
		this.removeSelf(); // 이걸로 해야될 것 같음.
		
		this.unscheduleAllSelectors();
		
//		this.unschedule(timeOutCheck()); // 없음
//		this.unschedule(checkFacebookLoginFinish()); // 없음

		CCTouchDispatcher.sharedDispatcher().removeAllDelegates();
		super.onExit();
	}

}