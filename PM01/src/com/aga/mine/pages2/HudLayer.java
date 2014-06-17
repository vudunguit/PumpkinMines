package com.aga.mine.pages2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.cocos2d.actions.UpdateCallback;
import org.cocos2d.actions.base.CCAction;
import org.cocos2d.actions.base.CCRepeatForever;
import org.cocos2d.actions.instant.CCCallFuncN;
import org.cocos2d.actions.instant.CCCallFuncND;
import org.cocos2d.actions.interval.CCAnimate;
import org.cocos2d.actions.interval.CCBlink;
import org.cocos2d.actions.interval.CCDelayTime;
import org.cocos2d.actions.interval.CCFadeIn;
import org.cocos2d.actions.interval.CCFadeOut;
import org.cocos2d.actions.interval.CCMoveBy;
import org.cocos2d.actions.interval.CCMoveTo;
import org.cocos2d.actions.interval.CCRotateBy;
import org.cocos2d.actions.interval.CCRotateTo;
import org.cocos2d.actions.interval.CCScaleTo;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.layers.CCLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.layers.CCTMXTiledMap;
import org.cocos2d.menus.CCMenu;
import org.cocos2d.menus.CCMenuItem;
import org.cocos2d.menus.CCMenuItemImage;
import org.cocos2d.menus.CCMenuItemSprite;
import org.cocos2d.menus.CCMenuItemToggle;
import org.cocos2d.nodes.CCAnimation;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCLabel;
import org.cocos2d.nodes.CCNode;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.nodes.CCSpriteFrame;
import org.cocos2d.nodes.CCSpriteFrameCache;
import org.cocos2d.nodes.CCSpriteSheet;
import org.cocos2d.nodes.CCTextureCache;
import org.cocos2d.opengl.CCTexture2D;
import org.cocos2d.sound.SoundEngine;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGRect;
import org.cocos2d.types.CGSize;
import org.cocos2d.types.ccColor3B;

import android.R;
import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;

import com.aga.mine.pumpkin.Config;
import com.aga.mine.pumpkin.Home;
import com.aga.mine.pumpkin.Home2;
import com.aga.mine.pumpkin.MainApplication;
import com.aga.mine.pumpkin.NetworkController;
import com.aga.mine.pumpkin.Utility;
import com.aga.mine.util.UtilResource;

public class HudLayer extends CCLayer {

	String folder = "61hud/";
	int maxMineNumber;
	final float magicianPositionY = 0.217f;
	CGSize winSize;

	CCLabel label;
	public CCLabel testText;  // 출시때는 레이블과 들어오는 데이터 모두 지울 것
	// CCLabel debugLabel;
	// CCLabel debugLabelLine2;
	static CCLabel statusMine;

	CCSprite magician;

	CCMenu itemMenu;
	CCSprite itemBase;
	CCSequence magicianAction;

	Context mContext;

	float tilePixelSize = CCTMXTiledMap.tiledMap(GameData.share().gameMap)
			.getTileSize().width;

	// (생명) 태그 넘버
	static final int kTagHeart = 9999; // final x
	static final int kButtonOff = 0; // final x
	static final int kButtonOn = 1; // final x

	public GameEnding mGameEnding;
	HudLayer controlHudLayer;
	
	public Game mGame;
	public GameMinimap mGameMinimap;
	public GameProgressBar mGameProgressBar;
	
	private CCSprite fire;
	private CCAnimation fireAttack;
	private CCSprite wind;
	private CCAnimation windAttack;
	private CCSprite cloud;
	//private CCSprite rune;
	private CCAnimation runeAni;
	//private CCSprite divine;
	private CCAnimation divineAni;
	//private CCSprite earth;
	private CCAnimation earthAni;
	//private CCSprite mirror;
	private CCAnimation mirrorAni;
	private CCAnimation mShockAni; //마법사 감전 애니
	
	private CCSprite mBg;

	CCMenu minimap;
	
	float maxTiles;
	public HudLayer(Game game) {
		mGame = game;
		
		cache = CCSpriteFrameCache.sharedSpriteFrameCache();
		cache.addSpriteFrames(folder + "magician.plist");
		CCSpriteSheet.spriteSheet(folder + "magician.png"); 
		 
//		mGameEnding = new GameEnding();
		
//		addChild(mGameEnding, GameConfig.share().kDepthPopup, 1234);
//		mGameEnding.setVisible(false);
//		mGameEnding.setIsTouchEnabled(false);
		
		this.winSize = CCDirector.sharedDirector().winSize();
		this.mContext = CCDirector.sharedDirector().getActivity()
				.getApplicationContext();

		// 터치가 안될시 아래 명령을 비활성화 할 것
		// CCTouchDispatcher.sharedDispatcher().addTargetedDelegate(this, 0,
		// true);
		// this.setIsTouchEnabled(true);
		// this.isTouchEnabled_ = true; // cclayer 멤버

		int margin = 5;
		label = CCLabel.makeLabel("0", "verdana-Bold",
				(18 * tilePixelSize) / 128);
		label.setColor(ccColor3B.ccBLACK);
		label.setPosition(winSize.width
				- (label.getContentSize().width / 2) - margin,
				(label.getContentSize().height / 2) - margin);
		this.addChild(label);

		int testTextSize = 72;
		testText = CCLabel.makeLabel("시간이 초기화 되면 시작", "Arial",
				(testTextSize * tilePixelSize) / 128);
		testText.setColor(ccColor3B.ccWHITE);
		testText.setPosition(winSize.width / 2, winSize.height / 2);
//		this.addChild(testText); //
		//
		// 상단 중앙 게임진행시간 및 진행 막대기
		mGameProgressBar = new GameProgressBar(this);
		addChild(mGameProgressBar);
		// gameProgressBar.startTime(gameEnding);
		// Log.e("HudLayer / ", "minimap setting complete");adsa
		// gameProgressBar.startTime(controlHudLayer);
		mGameProgressBar.startTime();

		//
		// 좌상단 찾은 지뢰 갯수 및 생명 표시
		CCSprite statusBase = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + folder
				+ "game-statusBase-hd.png"));
		statusBase.setPosition(statusBase.getContentSize().width / 2
				+ margin, winSize.height
				- statusBase.getContentSize().height / 2 - margin);
		this.addChild(statusBase);

		// 현재 맵에서 찾아야되는 지뢰(호박)의 갯수 글자
		statusMine = CCLabel.makeLabel(
		// GameData.share().getMineNumber() + " ",
		// "AvenirNextCondensed-Bold", 11);
				String.valueOf(mGame.getMineNumber()), "Arial-Bold", 30);
		statusMine.setPosition(statusBase.getContentSize().width / 2,
				statusBase.getContentSize().height / 4);
		statusBase.addChild(statusMine);
		this.updateHeart(); // 생명 표시 초기화

		String MinimapImageNormal = "game-buttonMinimap-normal-hd.png";
		String MinimapImageSelect = "game-buttonMinimap-select-hd.png";
		if (!GameData.share().isMultiGame) {
			MinimapImageNormal = "pause1.png";
			MinimapImageSelect = "pause2.png";
		}
		//
		// 우상단 미니맵 버튼
		CCMenuItem item = CCMenuItemImage.item(
				CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + folder + MinimapImageNormal)), 
				CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + folder + MinimapImageSelect)), 
				this, "clicked");
		item.setTag(Game.kButtonMinimap);
		
		minimap = CCMenu.menu(item);
		minimap.setPosition(
				winSize.width - item.getContentSize().width / 2,
				winSize.height - item.getContentSize().height / 2);
		minimap.setPosition(CGPoint.ccpSub(minimap.getPosition(),
				CGPoint.ccp(4f, 4f)));
		// Log.e("HudLayer", "minimap setting complete");
		this.addChild(minimap);
		// Log.e("HudLayer", "minimap loaded");

		//
		// 하단 수정구 아이템
		itemBase = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + folder + "game-itemBase-hd.png"));
		this.addChild(itemBase);
		itemBase.setPosition(
				winSize.width / 2,
				itemBase.getContentSize().height / 2 + itemBase.getContentSize().height / 12);
		CCMenuItemSprite itemOn = null;
		CCMenuItemSprite itemOff = null;
		CCMenuItemToggle itemToggle;
		itemMenu = CCMenu.menu();
		String[] fileNames = { "Fire", "Wind", "Cloud", "Divine", "Earth", "Mirror" };
		int counter = 1;
		for (int i = 0; i < fileNames.length; i++) {
			String fOn = folder + "game-item" + fileNames[i] + "On-hd.png";
			String fOff = folder + "game-item" + fileNames[i] + "Off-hd.png";
			
			itemOn = CCMenuItemImage.item(
					CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + fOn)), 
					CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + fOn)));
			itemOff = CCMenuItemImage.item(
					CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + fOff)), 
					CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + fOff)));
			
			itemToggle = CCMenuItemToggle.item(this, "clicked", itemOff, itemOn);
			itemToggle.setSelectedIndex(kButtonOff);
			itemToggle.setTag(counter);
			itemMenu.addChild(itemToggle);
			counter++;
		}
		itemMenu.alignItemsHorizontally(0.0f);
		itemMenu.setPosition(
				itemBase.getContentSize().width / 2,
				itemOn.getContentSize().height / 2 + itemOn.getContentSize().height / 8);
		itemBase.addChild(itemMenu);

		// 추가하면 터치가 안됨. 확인할 것

		//
		// 이모티콘
		GameEmoticon gameEmoticon = new GameEmoticon();
		this.addChild(gameEmoticon);

		//
		// test
		// this.schedule("tick", 0.1f);
		this.updateSphereItemNumber();

		// test code
		// GameLoading loading = GameLoading.share(this.mContext);
		// this.addChild(loading, GameConfig.share().kDepthPopup);

		//
		// test code
		// game과 hudlayer에 mcontext둘다 있음.
		UserData userData = UserData.share(mContext);

		//
		// 마법사
		String magicianColor = "ra0.png";
		if (!Config.getInstance().getOwner())
			magicianColor = "br0.png";
		
//		magician.setDisplayFrame(frame);
		
		magician = CCSprite.sprite(cache.getSpriteFrame(magicianColor));
		magician.setScale(2);
		this.addChild(magician, -1);
		magician.setPosition(winSize.width * 0.57f, winSize.height * magicianPositionY);

		// this.addChild(point1);
		// point1.setPosition(winSize.width/2,winSize.height/2 + 100);
		// this.addChild(point2);
		// point2.setPosition(winSize.width/2,winSize.height/2 + 50);
		// this.addChild(point3);
		// point3.setPosition(winSize.width/2,winSize.height/2 - 50);
		// this.addChild(point4);
		// point4.setPosition(winSize.width/2,winSize.height/2 - 100);
		if (GameData.share().isMultiGame) {
			mGameMinimap = new GameMinimap(this);
			this.addChild(mGameMinimap, GameConfig.share().kDepthPopup);
			mGameMinimap.setVisible(false);
		}
		
		//불, 바람, 구름 애니메이션
		fire = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + "61hud/fire-01.png"));
		fireAttack = CCAnimation.animation("fireAttack");
		for(int i=1; i<=6; i++) {
    		CCSprite fireframe = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + String.format("61hud/fire-%02d.png", i)));
    		fireAttack.addFrame(fireframe.getTexture());
		}
		
		wind = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + "61hud/wind01_0.1.png"));
		windAttack = CCAnimation.animation("windAttack");
		for(int i=1; i<=8; i++) {
    		CCSprite windframe = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + String.format("61hud/wind%02d_0.1.png", i)));
    		//fire.flipY_ = true;
    		windAttack.addFrame(windframe.getTexture());
		}
		
		cloud = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + "61hud/fx-passingcloud1.png"));
		
//		rune = CCSprite.sprite("61hud/rune-01.png");
		runeAni = CCAnimation.animation("rune");
		for(int i=1; i<=10; i++) {
    		runeAni.addFrame(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + (String.format("61hud/rune-%02d.png", i))));
		}
		for(int i=1; i<=3; i++) {
			runeAni.addFrame(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + ("61hud/rune-10.png")));
		}
		
		//divine = CCSprite.sprite("61hud/divine-01.png");
		divineAni = CCAnimation.animation("divine");
		for(int i=1; i<=6; i++) {
    		divineAni.addFrame(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + (String.format("61hud/divine-%02d.png", i))));
		}
		
		//earth = CCSprite.sprite("61hud/earth-01.png");
		earthAni = CCAnimation.animation("earth");
		for(int i=1; i<=6; i++) {
    		CCSprite earthframe = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + String.format("61hud/earth-%02d.png", i)));
    		earthAni.addFrame(earthframe.getTexture());
		}
		
		//mirror = CCSprite.sprite("61hud/mirror-01.png");
		mirrorAni = CCAnimation.animation("mirror");
		for(int i=1; i<=6; i++) {
    		CCSprite mirrorframe = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH +  String.format("61hud/mirror-%02d.png", i)));
    		mirrorAni.addFrame(mirrorframe.getTexture());
		}
		
		//마법사 감전 애니
		mShockAni = CCAnimation.animation("shock");
		for(int i=1; i<=4; i++) {
    		CCSprite shockframe = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + String.format("61hud/Shock-%d.png", i)));
    		mShockAni.addFrame(shockframe.getTexture());
		}
		
		//test
		//GameEnding ending = new GameEnding();
		//addChild(ending, GameConfig.share().kDepthPopup, 1234);
		maxTiles = mGame.getClosedCell();
		otherProgress = (int) maxTiles;
		mine = mGame.getMineNumber();
	}

	//이모티콘 애니메이션 : NetworkController에서 데이터를 수신후 이 펑션을 호출
	public void startEmoticonAni(int emoticonId) {
		String emoticonPath = String.format("62game_emoticon/emoticons-hd/emoticon%02d-hd.png", emoticonId);
		CCSprite emoticon = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + emoticonPath));
		
		emoticon.setScale(0.5f);
		emoticon.setPosition(winSize.getWidth()/2, winSize.getHeight() * 3 / 4);
		addChild(emoticon, 10);
		
		CCScaleTo action1 = CCScaleTo.action(0.4f, 1.3f);
		CCScaleTo action2 = CCScaleTo.action(0.1f, 0.9f);
		CCScaleTo action3 = CCScaleTo.action(0.1f, 1.1f);
		CCScaleTo action4 = CCScaleTo.action(0.1f, 1.0f);
		CCDelayTime action5 = CCDelayTime.action(2.0f);
		CCFadeOut action6 = CCFadeOut.action(1.0f);
		CCCallFuncN action7 = CCCallFuncN.action(this, "cbRemoveSprite"); 
		
		emoticon.runAction(CCSequence.actions(action1, action2, action3, action4, action5, action6, action7));
	}
	
	public void cbRemoveSprite(Object sender) {
		CCSprite sprite = (CCSprite)sender;
		sprite.removeFromParentAndCleanup(true);
	}
	//이모티콘 애니메이션 끝-----------------------------------------------------------------

	//
	public boolean updateHeart() {
		// Log.e("HudLayer",
		// "HudLayer --------- updateHeart --------- HudLayer");
		// value는 3
		int value = GameData.share().getHeartNumber();

		for (int i = 0; i < GameData.share().kMaxHeartNumber * 2; i++) {
			this.removeChildByTag(kTagHeart, true);
		}

		// 하트 위치
		CGPoint position = CGPoint.make(winSize.width / 20.0f,
				winSize.height - winSize.width / 4.3f);
		int z = 0;

		// add flag
		for (int i = 0; i < GameData.share().kMaxHeartNumber; i++) {
			z = (i < value) ? 10 : 0;

			CCSprite heartOn = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + 
					folder
					+ "game-heartOn-hd.png"));
			CCSprite heartOff = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + 
					folder
					+ "game-heartOff-hd.png"));

			this.addChild(heartOff, z);
			heartOff.setPosition(position);
			heartOff.setTag(kTagHeart);

			if (i < value) {
				this.addChild(heartOn, z);
				heartOn.setPosition(position);
				heartOn.setTag(kTagHeart);

				// save for animation
				// lastFlag = flag;
			}
			position = CGPoint.make(position.x
					+ heartOff.getContentSize().width * 1.1f, position.y);
		}

		return true;
	}

	// updateHeart() end

	CCSpriteFrameCache cache;
	
	public void setMagicianTo(int direction) {
		String magicianColor = "ra";
		if (!Config.getInstance().getOwner())
			magicianColor = "br";
		CCSpriteFrame frame = cache.getSpriteFrame(magicianColor + direction + ".png");
		magician.setDisplayFrame(frame);
	}

	//
	// 수정구아이템 버튼 활성화 지정
	// @ sphereType : 수정구 유형
	// @ state : 활성화 여부, kBUttonOn, kButtonOff;

	// button callback
	public void clickEffect(int sphereType) {
		this.clickEffect(sphereType, 0.0f);
	}

	// 안될수도 있는 부분
	public void clickEffect(int sphereType, float startDelay) {
		//
		// 활성화된 아이템버튼의 클릭 효과 오버레이를 일정시간 켰다 끈다.
		CCSprite overlay = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + folder + "game-itemOver-hd.png"));

		for (CCNode itemNode : itemMenu.getChildren()) {
			CCMenuItemToggle item = (CCMenuItemToggle) itemNode;
			if (item.getTag() == sphereType && item.selectedIndex() == kButtonOn) {
				item.addChild(overlay);
				overlay.setPosition(
						item.getContentSize().width / 2,
						item.getContentSize().height / 2);
				// flashOut 메소드가 안되는 부분이 있어 막아뒀음.(remove node) 대신 다른것 넣어서
				// 테스트중...
				Utility.getInstance().flashOut(overlay, startDelay, 0.5f);
				break;
			}
		}
		// Log.e("Game / clickEffect", "end");
	}

	/*****************************************************/
	/**
	 * 문제지점
	 * 
	 * 애니매이션 아이템 발동 애니
	 * 
	 * 아이템 피해 애니는 GameMinimap.java 의 receivePlayData method 확인
	 * 
	 * 
	 * @return
	 */
	public void clicked(final Object button) {
		int tag = ((CCMenuItem) button).getTag();
		if (!Config.getInstance().isDisableButton()) {
			// 값이 false인데 도대체 어떻게 뚫고 들어가지...
			 Log.e("HudLayer", "clicked Button EnableButton");
			clickedNormal(tag);
		} else {
			 Log.e("HudLayer", "clicked Button DisableButton");
			clickedPause(tag);
		}
	}
	
	private void clickedNormal(final int tag) {
		 Log.e("HudLayer", "clicked1 : " + Config.getInstance().isDisableButton());
		String effectName = "";
		switch (tag) {
		case Game.kButtonMinimap:
//			// Log.e("button pressed", "kButtonMinimap");
			Config.getInstance().setDisableButton(true);
			if (GameData.share().isMultiGame) {
				mGameMinimap.setVisible(true);
			} else {
				pausePopup();
			}
			break;
		case Game.kButtonFire:
			Log.e("button pressed", "kButtonFire");
			effectName = "불마법";
			StartAniFireAttack();
			schedule(new UpdateCallback() {
				@Override
				public void update(float d) {
					startCoolTime(mGame.mFireAttackTime);
					unschedule(this);
				}
			}, 1.0f);
			break;
		case Game.kButtonWind:
			effectName = "바람마법";
			StartAniWindAttack();
			schedule(new UpdateCallback() {
				@Override
				public void update(float d) {
					startCoolTime(mGame.mWindAttackTime);
					unschedule(this);
				}
			}, 1.0f);
			break;
		case Game.kButtonCloud:
			effectName = "구름마법";
			StartAniCloudAttack();
			schedule(new UpdateCallback() {
				@Override
				public void update(float d) {
					startCoolTime(mGame.mCloudAttackTime);
					unschedule(this);
				}
			}, 1.0f);
			break;
		case Game.kButtonDivine:
			if(GameData.share().getItemNumberByType(Game.kButtonDivine) > 0) {
				StartAniRune(Game.kButtonDivine);
				GameData.share().decreaseItemByType(Game.kButtonDivine);
			}
			break;
		case Game.kButtonEarth:
			Log.d("LDK", "earth button is clicked");
			if (mGame.mIsClickedEarth == false) {
				mGame.mIsClickedEarth = true;
			}
			break;
		case Game.kButtonMirror:
			//공격이 올때 자동 발동
			//StartAniRune(6);
			break;

		default:
			// Log.e("button pressed", "default");
			effectName = "마법지정 오류";
			break;
		}
		final String alertText = effectName;
		//
		// 수정구아이템 버튼 클릭 공통
		if (tag >= 1 && tag <= 6) {
		
			//
			// 마법사 액션
			// - 애니메이션을 위에 올렸닥 끝나면 지워버린다.
			// - 본래 캐릭터는 가렸다가 애니메이션이 끝난 후에 (0.4초 이후) 다시 보이도록 한다.
			animationMagicianAction(this);
			magician.setVisible(false);
			final CCNode layer = this;
			
			schedule(new UpdateCallback() {
				@Override
				public void update(float d) {
					magician.setVisible(true);
					layer.removeChildByTag(888, true);
					//
					// 아이템수를 감소시키고
					// 라벨 디스플레이를 업데이트 시키고
					// 버튼 클릭 효과

					// 디펜스와 어택 구분 필요 이유????
					if (GameData.share().isMultiGame && tag < 4) { // 공격용 아이템(마법)만 보냄)
						try {
							NetworkController.getInstance().sendPlayDataMagicAttack((tag * 1000) + 23); // 23대신 각각의 공격 시간을 더해야됨.
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					GameData.share().decreaseItemByType(tag);
					updateSphereItemNumber();
					clickEffect(tag);
					
					unschedule(this);
				}
			}, 0.4f);

		}
	}
	
	private void startCoolTime(final long attackTime) {
		mGame.mStartTimeOfAttack = System.currentTimeMillis();
		
		final CCLabel label1 = CCLabel.makeLabel(" ", "verdana-Bold", 48);
		label1.setAnchorPoint(0.5f, 0.5f);
		final CCMenuItemToggle item1 = (CCMenuItemToggle) itemMenu.getChildByTag(Game.kButtonFire);
		item1.addChild(label1, 10, 201);
		label1.setPosition(item1.getContentSize().width/2, item1.getContentSize().height/2);
		
		final CCLabel label2 = CCLabel.makeLabel(" ", "verdana-Bold", 48);
		label2.setAnchorPoint(0.5f, 0.5f);
		final CCMenuItemToggle item2 = (CCMenuItemToggle) itemMenu.getChildByTag(Game.kButtonWind);
		item2.addChild(label2, 10, 202);
		label2.setPosition(item2.getContentSize().width/2, item2.getContentSize().height/2);
		
		final CCLabel label3 = CCLabel.makeLabel(" ", "verdana-Bold", 48);
		label3.setAnchorPoint(0.5f, 0.5f);
		final CCMenuItemToggle item3 = (CCMenuItemToggle) itemMenu.getChildByTag(Game.kButtonCloud);
		item3.addChild(label3, 10, 203);
		label3.setPosition(item3.getContentSize().width/2, item3.getContentSize().height/2);
		
		disableButton();
		
		schedule(new UpdateCallback() {
			@Override
			public void update(float d) {
				long pastTime = System.currentTimeMillis() - mGame.mStartTimeOfAttack; //경과시간
				long coolTime = attackTime - pastTime/1000;
				if(coolTime<=0) {
					//공격시간이 종료되면 공격시간을 다시 초기화
					unschedule(this);
					mGame.mStartTimeOfAttack = 0;
					item1.removeChildByTag(201, true);
					item2.removeChildByTag(202, true);
					item3.removeChildByTag(203, true);
					enableButton();
				} else {
					label1.setString(String.format("%02d", (int)coolTime));
					label2.setString(String.format("%02d", (int)coolTime));
					label3.setString(String.format("%02d", (int)coolTime));
				}
			}
		});
	}
	
	private void disableButton() {
		//공격 버튼을 비활성화 및 dim 처리
		for(int i = 1; i<=3; i++) {
			CCMenuItemToggle item = (CCMenuItemToggle) itemMenu.getChildByTag(i);
			item.setSelectedIndex(kButtonOff);
			item.setIsEnabled(false);
		}
	}
	
	private void enableButton() {
		for(int i = 1; i<=3; i++) {
			CCMenuItemToggle item = (CCMenuItemToggle) itemMenu.getChildByTag(i);
			item.setIsEnabled(true);
			if(GameData.share().getItemNumberByType(i) > 0) {
				item.setSelectedIndex(kButtonOn);
			}
		}
	}
	
	private void clickedPause(int tag) {
		Log.e("HudLayer", "clicked2 : " + Config.getInstance().isDisableButton());
		switch (tag) {
		case continueTag:
			Log.e("HudLayer", "continueTag");
			MainApplication.getInstance().getActivity().click();
			this.removeChildByTag(pausePopupTag, true);
			Config.getInstance().setDisableButton(false);
			minimap.setIsTouchEnabled(true);
			mGameProgressBar.pauseTime(false);
			SoundEngine.sharedEngine().resumeSound();
			break;
			
		case quitTag:
			Log.e("HudLayer", "quitTag");
			MainApplication.getInstance().getActivity().click();
			this.removeChildByTag(pausePopupTag, true);
			Config.getInstance().setDisableButton(false);
			mGameProgressBar.stopTime();
	        SoundEngine.sharedEngine().purgeSharedEngine();
			CCScene scene = null;
			if (GameData.share().isGuestMode) {
				scene = Home2.scene();
			} else {
				scene = Home.scene();
			}
			CCDirector.sharedDirector().replaceScene(scene);
			break;
			
		default:
			Log.e("HudLayer", "default");
			MainApplication.getInstance().getActivity().click();
			this.removeChildByTag(pausePopupTag, true);
			Config.getInstance().setDisableButton(false);
			minimap.setIsTouchEnabled(true);
			mGameProgressBar.pauseTime(false);
			SoundEngine.sharedEngine().resumeSound();
			break;
		}
	}

	final private int pausePopupTag = 550;
	final private int continueTag = 551;
	final private int quitTag = 552;
	
	private void pausePopup() {
		mGameProgressBar.pauseTime(true);
		SoundEngine.sharedEngine().pauseSound();
		Log.e("HudLayer", "pausePopup : " + Config.getInstance().isDisableButton());
		if (Config.getInstance().isDisableButton()) {
			minimap.setIsTouchEnabled(false);
			MainApplication.getInstance().getActivity().click();
			
			
			CCSprite opacitybg = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + "00common/opacitybg.png"));
			opacitybg.setPosition(this.getContentSize().width/2, this.getContentSize().height/2);
			this.addChild(opacitybg, pausePopupTag, pausePopupTag);
			
			CCSprite panel = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + folder + Utility.getInstance().getNameWithIsoCodeSuffix("pausebg.png")));
			panel.setPosition(opacitybg.getContentSize().width/2, opacitybg.getContentSize().height/2);
			opacitybg.addChild(panel);
			
			
			String continue1Str = Utility.getInstance().getNameWithIsoCodeSuffix("continue1.png"); 
			String continue2Str = Utility.getInstance().getNameWithIsoCodeSuffix("continue2.png"); 
			
			CCMenuItem continueButton = CCMenuItemImage.item(
					CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + folder + continue1Str)), 
					CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + folder + continue2Str)), this, "clicked");
			continueButton.setTag(continueTag);
			continueButton.setAnchorPoint(0.5f, 0.5f);
			
			CCMenu continueMenu = CCMenu.menu(continueButton);
			continueMenu.setAnchorPoint(0.5f, 0.5f);
			continueMenu.setPosition(
					panel.getContentSize().width * 0.28f,
//					0 + continueButton.getContentSize().width * 0.4f,
					panel.getContentSize().height * 0.37f);
			panel.addChild(continueMenu);
			
			
			String quit1Str = Utility.getInstance().getNameWithIsoCodeSuffix("quit1.png"); 
			String quit2Str = Utility.getInstance().getNameWithIsoCodeSuffix("quit2.png"); 
			
			CCMenuItem quitButton = CCMenuItemImage.item(
					CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + folder + quit1Str)), 
					CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + folder + quit2Str)), this, "clicked");
			quitButton.setTag(quitTag);
			quitButton.setAnchorPoint(0.5f, 0.5f);
			
			CCMenu quitMenu = CCMenu.menu(quitButton);
			quitMenu.setAnchorPoint(0.5f, 0.5f);
			quitMenu.setPosition(
					panel.getContentSize().width * (1 - 0.28f),
//					panel.getContentSize().width,
//					panel.getContentSize().width - quitButton.getContentSize().width,
					panel.getContentSize().height * 0.37f);
			panel.addChild(quitMenu);
		} else {
			Log.e("HudLayer", "pausePopup : " + Config.getInstance().isDisableButton());
			MainApplication.getInstance().getActivity().click();
			this.removeChildByTag(pausePopupTag, true);
			Config.getInstance().setDisableButton(false);
			minimap.setIsTouchEnabled(true);
			mGameProgressBar.pauseTime(false);
			SoundEngine.sharedEngine().resumeSound();
		}
	}

	/*****************************************************/

	public void showMagician() {
		magician.setVisible(true);
	}

	/*****************************************************/
	/**
	 * 문제지점
	 * 
	 * 애니매이션 게임 종료 애니 GameEnding.java
	 * 
	 * @return
	 */
	public boolean isGameOver = false; // 게임 종료 메시지를 서버로 무한 보내는것을 방지함.
	
	int myScore;
	int otherScore; 
//	public void gameOver() {
	public void gameOver(int score, int other) {
        SoundEngine.sharedEngine().purgeSharedEngine();
		Log.e("HudLayer", "gameEnding - gogo");
		Config.getInstance().setDisableButton(true);
		myScore = score;
		otherScore = other; 
		Log.e("HudLayer", "myScore : " + myScore + ", otherScore : " + otherScore);
		
		// 애니메이션 이펙트 endingZoomOutAndBlastFX
		mGame.gameOverAnimation(); //2초동안 실행
		
		schedule(new UpdateCallback() {
			@Override
			public void update(float d) {
				unschedule(this);
				
				//To do : 대전게임에서 승리시 isVictory()로 승리 애니메이션 아니면 그냥 팝업
				boolean isVictory = Config.getInstance().getVs();
				if(isVictory) {
					startVictory();
				} else {
					popupEnding();
				}
			}
		}, 4.5f);  //2초후에 엔딩 팝업 
	}
	
	private void popupEnding() {
		mGameEnding = new GameEnding(myScore, otherScore, mGame.getClosedCell());
		addChild(mGameEnding, GameConfig.share().kDepthPopup, 1234);
		mGameEnding.setIsTouchEnabled(true);
	}

	/*****************************************************/

	// others

	// 위쪽 캐릭터 얼굴만있는 프로그레스바에 캐릭 얼굴 움직이게 해줌
	// mine에서 tiles로 변경해야됨.
	public void updateProgress() {
		float progress = 0;
		progress = getProgressPosition(mGame.getClosedCell(), false);
		Log.e("HudLayer", "p1 prog: " + ((int)(progress*10)) / 10 + ", cell: " + mGame.getClosedCell() + ", tile: " + maxTiles + ", mine: " + mine);		
		mGameProgressBar.progress(progress, GameProgressBar.kTagIndicatorMe);
	}
	
	int mine;
	int otherProgress;
	
	public void updateOtherPlayerProgress() {
		float progress = 0;
		progress = getProgressPosition(otherProgress, true);
		Log.e("HudLayer", "player2 progress : " + progress + ", ClosedCell : " + otherProgress + ", maxTiles : " + maxTiles + ", mine : " + mine);
		mGameProgressBar.progress(progress, GameProgressBar.kTagIndicatorOther);
	}
	
	private float getProgressPosition(int closedCell, boolean isOtherProgress) {
		float progress = 0;
		if (isOtherProgress)
			otherProgress--;
		if (closedCell != 0)
			progress = (closedCell - mine) / (maxTiles - mine);
		return progress = 1 - progress;
	}

	// 지뢰갯수 숫자로 표시하는 메서드
	public void updateMineNumber(int remainedMineNumber) {
		statusMine.setString("" + remainedMineNumber);
	}

	// 수정구를 활성화시키면 +1씩 라벨이 붙게함
	public void updateSphereItemNumber() {
		//
		// 전체 라벨 제거
		for (int i = 0; i < 6; i++) {
			this.removeChildByTag(999, true);
		}
		
		// 아이템 수량이 0개일시 버튼 클릭 비활성화
		// 아랫쪽 for문과 합쳐도 될 것 같음.
		int sphereTypes = 1;
		for (CCNode child : itemMenu.getChildren()) {
			CCMenuItemToggle button = (CCMenuItemToggle) child;
			Log(child);
			if (GameData.share().getItemNumberByType(sphereTypes) > 0) {
				button.setIsEnabled(true);
			} else {
				button.setIsEnabled(false);
			}
			sphereTypes ++;
		}
		
		for (int i = 0; i < 6; i++) {
			//
			// 새 라벨 추가
			int sphereType = i + 1;
			int itemNumber = GameData.share().getItemNumberByType(
					sphereType);
			String string = itemNumber > 0 ? "+" + itemNumber : "  ";
			// CCLabel l = CCLabel.makeLabel(string, "Arial", 12);
			// 아이템별 수량을 표시하는 글자
			CCLabel l = CCLabel.makeLabel(string, "Arial-Bold", 24);
			l.setTag(999);
			this.addChild(l);
			l.setPosition(i * 50f * 2 + 50f * 2,
					l.getContentSize().height * 1.2f);

			//
			// 버튼 상태 설정
			this.setSphereItemState(sphereType, itemNumber > 0 ? kButtonOn : kButtonOff);
		}
		// Log.e("Game / updateSphereItemNumber", "end");
	}

	private void Log(Object child) {
		String objStr = String.valueOf(child);
		Log.e("HudLayer", objStr + " : " + child);
	}

	// 원래 코드에서 자동이 하도 많아서
	// 지금 작성한게 돌아가도 잘 돌아가는건지 모르겠다.
	public void setSphereItemState(int sphereType, int state) {

		List<CCMenuItemToggle> item = new ArrayList<CCMenuItemToggle>();
		for (int k = 0; k < itemMenu.getChildren().size(); k++) {
			item.add((CCMenuItemToggle) itemMenu.getChildren().get(k));
		}
		if (item != null && item.size() != 0) {
			for (int k = 0; k < item.size(); k++) {
				if (item.get(k).getTag() == sphereType) {
					item.get(k).setSelectedIndex(state);
					break;
				}
			}

		}

	}

	public void showMessage(int cell_ID) {
		testText.setString("message type : " + cell_ID);
	}

	// public static void abc(int messageType){
	public void abc(byte[] messageType) {
		// final int aaa = messageType;
		byte[] aaa = messageType;
		Log.e("Game / abc", "in");
		String aaaa = "";
		for (byte b : aaa) {
			aaaa = aaaa + b + ",";
		}
		final String result = aaaa;
		
		schedule(new UpdateCallback() {
			@Override
			public void update(float d) {
				if (result != null) {
					testText.setString("message type : " + result);
				}
				unschedule(this);
			}
		});

		Log.e("Game / abc", "out");
	}

	@Override
	public boolean ccTouchesBegan(MotionEvent event) {
		return true;
	}

	@Override
	public boolean ccTouchesEnded(MotionEvent event) {
		return super.ccTouchesEnded(event);
	}

	//불 공격 애니메이션-------------------------------------------------------------
	public void StartAniFireAttack() {
		fire.setPosition(winSize.width * 0.57f, winSize.height * 0.26f);
		fire.setAnchorPoint(CGPoint.ccp(0.5f, 1.0f));
		fire.setScale(0);
		addChild(fire);
		
		CCRotateTo rot	= CCRotateTo.action(0.01f, 180);
		CCScaleTo scale = CCScaleTo.action(0.7f, 0.8f); //0.7초 동안 0.8스케일
		CCCallFuncN action2 = CCCallFuncN.action(this, "cbFireMove");
		
		CCAnimate action = CCAnimate.action(0.7f, fireAttack, false);
		CCRepeatForever repeat = CCRepeatForever.action(action);
		
		//프레임애니메이션 하면서 0.7초동안 0.8로 스케일 애니메이션
		fire.runAction(repeat);
		fire.runAction(CCSequence.actions(rot, scale, action2));
	}
	
	public void cbFireMove(Object sender) {
		CCSprite fire1 = (CCSprite) sender;

//		CCAnimate action = CCAnimate.action(0.5f, fireAttack, false);
//		CCRepeatForever repeat = CCRepeatForever.action(action);
		
		CCMoveBy move = CCMoveBy.action(0.5f, CGPoint.ccp(0, winSize.height));
		CCCallFuncN remove = CCCallFuncN.action(this, "cbRemoveSprite");
		
		//fire1.runAction(repeat);
		fire1.runAction(CCSequence.actions(move, remove));
	}
	
	//불 방어 애니메이션
	public void StartAniFireDefense(final int time) {
		startShockAni(); //마법사 감전 애니
		
		for(int k=0; k<10; k++) {
			CCSprite fire = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + "61hud/fire-01.png"));
			//x 위치 랜덤
			Random rand = new Random();
			float fireWidth = fire.getContentSize().width;
			fire.setPosition(fireWidth/2 +  rand.nextInt((int)(winSize.width - fireWidth)), winSize.height); //랜덤
			fire.setAnchorPoint(CGPoint.ccp(0.5f, 0));
			//fire.setScale(0.5f + new Random().nextFloat() * 0.5f); //크기 랜덤
			addChild(fire);
			
			CCAnimate action = CCAnimate.action(1f, fireAttack, false);
			CCAction repeat = CCRepeatForever.action(action);
			CCDelayTime delay = CCDelayTime.action(new Random().nextFloat() * 2.0f);
			CCMoveBy move = CCMoveBy.action(3.0f, CGPoint.ccp(0, - winSize.height-fire.getContentSize().height));
			CCCallFuncN remove = CCCallFuncN.action(this, "cbRemoveSprite");
	
			fire.runAction(repeat);
			fire.runAction(CCSequence.actions(delay, move, remove));
		}
		//1초(시간은  조정 필요) 후에 맵에 표시되는 부분 구현
		schedule(new UpdateCallback() {
			@Override
			public void update(float d) {
				// TODO
				unschedule(this);
				mGame.startFire(time);
			}
		}, 1.0f);
	}
	
	//바람공격 애니메이션------------------------------------------------------------
	public void StartAniWindAttack() {
		wind.setPosition(winSize.width * 0.57f, winSize.height * 0.26f);
		wind.setAnchorPoint(CGPoint.ccp(0.5f, 0));
		wind.setScale(0);
		addChild(wind);
		
		CCScaleTo scale = CCScaleTo.action(0.7f, 0.8f);
		CCCallFuncN action2 = CCCallFuncN.action(this, "cbWindMove");
		
		CCAnimate action = CCAnimate.action(0.7f, windAttack, false);
		CCRepeatForever repeat = CCRepeatForever.action(action);
		
		wind.runAction(repeat);
		wind.runAction(CCSequence.actions(scale, action2));
	}
	
	public void cbWindMove(Object sender) {
		CCSprite wind1 = (CCSprite) sender;

//		CCAnimate action = CCAnimate.action(1.4f, windAttack, false);
//		CCRepeatForever repeat = CCRepeatForever.action(action);
		
		CCMoveBy move = CCMoveBy.action(0.5f, CGPoint.ccp(0, winSize.height));
		CCCallFuncN remove = CCCallFuncN.action(this, "cbRemoveSprite");
		
		//wind1.runAction(repeat);
		wind1.runAction(CCSequence.actions(move, remove));
	}
	
	//바람 방어 애니메이션
	public void StartAniWindDefense(final int time) {
		startShockAni(); //마법사 감전 애니
		
		for(int k=0; k<10; k++) {
			CCSprite wind = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + "61hud/wind01_0.1.png"));
			//x 위치 랜덤
			Random rand = new Random();
			float windWidth = fire.getContentSize().width;
			wind.setPosition(windWidth/2 +  rand.nextInt((int)(winSize.width - windWidth)), winSize.height); //랜덤
			wind.setAnchorPoint(CGPoint.ccp(0.5f, 0));
			//wind.setScale(0.5f + new Random().nextFloat() * 0.5f); //크기 랜덤
			addChild(wind);
			
			CCAnimate action = CCAnimate.action(1f, windAttack, false);
			CCAction repeat = CCRepeatForever.action(action);
			CCDelayTime delay = CCDelayTime.action(new Random().nextFloat() * 3.0f);
			CCMoveBy move = CCMoveBy.action(2.0f, CGPoint.ccp(0, - winSize.height-fire.getContentSize().height));
			CCCallFuncN remove = CCCallFuncN.action(this, "cbRemoveSprite");
	
			wind.runAction(repeat);
			wind.runAction(CCSequence.actions(delay, move, remove));
		}
		//1초(시간은  조정 필요) 후에 맵에 표시되는 부분 구현
		schedule(new UpdateCallback() {
			@Override
			public void update(float d) {
				unschedule(this);
				mGame.startWind(time);
			}
		}, 1.0f);
	}
	
	//구름공격 애니메이션------------------------------------------------------------
	public void StartAniCloudAttack() {
		cloud.setPosition(winSize.width * 0.57f, winSize.height * 0.26f);
		cloud.setAnchorPoint(CGPoint.ccp(0.5f, 0));
		cloud.setScale(0);
		addChild(cloud);
		
		CCScaleTo scale = CCScaleTo.action(0.7f, 0.8f);
		CCCallFuncN action2 = CCCallFuncN.action(this, "cbCloudMove");
		cloud.runAction(CCSequence.actions(scale, action2));
	}
	
	public void cbCloudMove(Object sender) {
		CCSprite cloud1 = (CCSprite) sender;
		
		CCMoveBy move = CCMoveBy.action(0.5f, CGPoint.ccp(0, winSize.height));
		CCCallFuncN remove = CCCallFuncN.action(this, "cbRemoveSprite");
		
		cloud1.runAction(CCSequence.actions(move, remove));
	}
	
	//구름 방어 애니메이션---------------------------------------------------------
	public void StartAniCloudDefense(final int time) {
		startShockAni(); //마법사 감전 애니
		
		mGame.startCloud(time);
	}
	
	//룬(마법진) 애니메이션-----------------------------------------------------
	//parameter : 4:신성, 5:대지, 6:반사
	public void StartAniRune(int kind) {
		//룬애니
		CCSprite rune = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + "61hud/rune-01.png"));
		rune.setScale(0.55f);
		rune.setPosition(winSize.width * 0.5f, winSize.height * 0.5f);
		rune.setAnchorPoint(CGPoint.ccp(0.5f, 0.5f));
		addChild(rune);
		
		CCAnimate action = CCAnimate.action(1.04f, runeAni, false);
		CCFadeOut fadeout = CCFadeOut.action(0.32f);
		//CCCallFuncND call = CCCallFuncND.action(this, "cbRune", kind);
		rune.runAction(CCSequence.actions(action, fadeout));
		
		//정령 애니 ( 룬애니와 동시에 진행)
		CCFadeIn in = CCFadeIn.action(0.56f);
		CCFadeOut out = CCFadeOut.action(0.32f);
		switch(kind) {
		case 4:
			CCSprite divine = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + "61hud/divine-01.png"));
			divine.setPosition(rune.getContentSize().width * 0.5f, rune.getContentSize().height * 0.5f);
			divine.setAnchorPoint(CGPoint.ccp(0.5f, 0.5f));
			rune.addChild(divine, 2);
			divine.setOpacity(255);
			CCAnimate divineAction = CCAnimate.action(0.48f, divineAni, false);
			divine.runAction(CCSequence.actions(in, divineAction, out, CCCallFuncND.action(this, "cbRemoveSprite", kind)));
			break;
		case 5:
			CCSprite earth = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + "61hud/earth-01.png"));
			earth.setPosition(rune.getContentSize().width * 0.5f, rune.getContentSize().height * 0.5f);
			earth.setAnchorPoint(CGPoint.ccp(0.5f, 0.5f));
			rune.addChild(earth, 2);
			earth.setOpacity(255);
			CCAnimate earthAction = CCAnimate.action(0.48f, earthAni, false);
			earth.runAction(CCSequence.actions(in, earthAction, out, CCCallFuncND.action(this, "cbRemoveSprite", kind)));
			break;
		case 6:
			CCSprite mirror = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + "61hud/mirror-01.png"));
			mirror.setPosition(rune.getContentSize().width * 0.5f, rune.getContentSize().height * 0.5f);
			mirror.setAnchorPoint(CGPoint.ccp(0.5f, 0.5f));
			rune.addChild(mirror, 2);
			mirror.setOpacity(255);
			CCAnimate mirrorAction = CCAnimate.action(0.48f, mirrorAni, false);
			mirror.runAction(CCSequence.actions(in, mirrorAction, out, CCCallFuncND.action(this, "cbRemoveSprite", kind)));
			break;
		}
	}
	
	public void cbRemoveSprite(Object sender, Object k) {
		CCSprite sprite = (CCSprite) sender;
		sprite.getParent().removeFromParentAndCleanup(true);
		sprite.removeFromParentAndCleanup(true);
		
		int kind = (Integer) k;
		
		switch(kind) {
		case 4: //신성 마법
			mGame.stopAttack();
			break;
		case 5:
			//마법진 사라진 후 타일 벗기기
			//mGame.startEarth(MotionEvent event)
			break;
		case 6: //반사 마법, 마법 자동 소멸
			mGame.stopAttack();
			break;
		}
		
	}
	
	// Victory 애니메이션
	private void startVictory() {
		CCSprite backLight = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + "70game_ending/back-light.png"));
		CCSprite backSearch = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + "70game_ending/back-search.png"));
		CCSprite rybon = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + "70game_ending/rybon.png"));
		
		mBg = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + "00common/" + "opacitybg.png"));
		mBg.setPosition(winSize.width * 0.5f, winSize.height * 0.5f);
		//mBg.setScale(0.8f);
		addChild(mBg);

		// 백라이트 배치
		mBg.addChild(backLight);
		backLight.setPosition(winSize.width * 0.5f, winSize.height * 0.5f);

		// 회전 섬광 배치
		mBg.addChild(backSearch);
		backSearch.setPosition(winSize.width * 0.5f, winSize.height * 0.5f);
		backSearch.setScale(2.0f);

		// 리본 배치
		rybon.setPosition(winSize.width * 0.5f, winSize.height * 0.5f);
		mBg.addChild(rybon);

		// 별배치
		for (int i = 0; i < 30; i++) {
			CCSprite star = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + "70game_ending/star.png"));
			star.setPosition(winSize.width * new Random().nextFloat(), winSize.height * new Random().nextFloat());
			star.setScale(0.5f + 0.5f * new Random().nextFloat());
			mBg.addChild(star);

			CCBlink blink = CCBlink.action(0.5f + 2f * new Random().nextFloat(), 1);
			CCRepeatForever repeat = CCRepeatForever.action(blink);
			star.runAction(repeat);
		}

		// 배경 회전
		CCRotateBy rot = CCRotateBy.action(4, 360);
		backSearch.runAction(rot);

		// 리본 확대->축소
		rybon.setScale(2.0f);
		CCScaleTo scale = CCScaleTo.action(0.5f, 1.0f);
		CCCallFuncN call = CCCallFuncN.action(this, "cbCallHeartLeft");
		rybon.runAction(CCSequence.actions(scale, call));
	}

	public void cbEndVictory(Object sender) {
		mBg.removeAllChildren(true);
		popupEnding();
	}

	public void cbCallHeartLeft(Object sender) {
		CCSprite heartLeft = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + "70game_ending/heart-left.png"));
		mBg.addChild(heartLeft);
		heartLeft.setPosition(winSize.width * 0.5f, winSize.height * 0.5f);

		heartLeft.setScale(2.0f);
		CCScaleTo scale = CCScaleTo.action(0.5f, 1.0f);
		
		if(GameData.share().getHeartNumber() == 1) {
			CCDelayTime delay = CCDelayTime.action(1.5f);
			CCCallFuncN call = CCCallFuncN.action(this, "cbEndVictory");
			heartLeft.runAction(CCSequence.actions(scale, delay, call));
		} else {
			CCCallFuncN call = CCCallFuncN.action(this, "cbCallHeartRight");
			heartLeft.runAction(CCSequence.actions(scale, call));
		}
	}

	public void cbCallHeartRight(final Object sender) {
		CCSprite heartRight = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + "70game_ending/heart-right.png"));
		mBg.addChild(heartRight);
		heartRight.setPosition(winSize.width * 0.5f, winSize.height * 0.5f);

		heartRight.setScale(2.0f);
		CCScaleTo scale = CCScaleTo.action(0.5f, 1.0f);

		if(GameData.share().getHeartNumber() == 2) {
			CCDelayTime delay = CCDelayTime.action(1.5f);
			CCCallFuncN call = CCCallFuncN.action(this, "cbEndVictory");
			heartRight.runAction(CCSequence.actions(scale, delay, call));
		} else {
			CCCallFuncN call = CCCallFuncN.action(this, "cbCallHeartCenter");
			heartRight.runAction(CCSequence.actions(scale, call));
		}

	}

	public void cbCallHeartCenter(final Object sender) {
		CCSprite heartCenter = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + "70game_ending/heart-center.png"));
		mBg.addChild(heartCenter);
		heartCenter.setPosition(winSize.width * 0.5f, winSize.height * 0.5f);

		heartCenter.setScale(2.0f);
		CCScaleTo scale = CCScaleTo.action(0.5f, 1.0f);
		CCDelayTime delay = CCDelayTime.action(1.5f);
		CCCallFuncN call = CCCallFuncN.action(this, "cbEndVictory");
		heartCenter.runAction(CCSequence.actions(scale, delay, call));
	}
	
	//정령석 아래 영역으로 이동하는 애니 :
	//CCSprite spirit : 정령석 종류
	//CGPoint pos: HudLayer에서의 현재 좌표.  Game 레이어에서 HudLayer의 좌표로 변환해서 넘겨줘야함.
	//CGPoint pos2 : 이동해야 할 좌표
	//int kind : 정령석 종류
	public void startMoveSpirit(int kind, CGPoint pos) {
		Log.d("LDK", "itembase: " + itemBase.getPosition().x + "," + itemBase.getPosition().y);
		Log.d("LDK", "itemmenu: " + itemMenu.getPosition().x + "," + itemMenu.getPosition().y);
		Log.d("LDK", "item1: " +  itemMenu.getChildByTag(Game.kButtonFire).getPosition().x + "," +  itemMenu.getChildByTag(Game.kButtonFire).getPosition().y);
		
		CCSprite spirit = null;
		CCNode item = null;
		CCAnimation ani = CCAnimation.animation("spirit");
		
		switch(kind) {
		case Game.kButtonFire:
			spirit = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + "60game/sprit1_01.png"));
			item = itemMenu.getChildByTag(Game.kButtonFire);
			for(int i=1; i<=5; i++) {
	    		CCSprite frame = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + String.format("60game/sprit1_%02d.png", i)));
	    		ani.addFrame(frame.getTexture());
			}
			break;
		case Game.kButtonWind:
			spirit = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + "60game/sprit2_01.png"));
			item = itemMenu.getChildByTag(Game.kButtonWind);
			for(int i=1; i<=5; i++) {
	    		CCSprite frame = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + String.format("60game/sprit2_%02d.png", i)));
	    		ani.addFrame(frame.getTexture());
			}
			break;
		case Game.kButtonCloud:
			spirit = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + "60game/sprit3_01.png"));
			item = itemMenu.getChildByTag(Game.kButtonCloud);
			for(int i=1; i<=5; i++) {
	    		CCSprite frame = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + String.format("60game/sprit3_%02d.png", i)));
	    		ani.addFrame(frame.getTexture());
			}
			break;
		case Game.kButtonDivine:
			spirit = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + "60game/sprit4_01.png"));
			item = itemMenu.getChildByTag(Game.kButtonDivine);
			for(int i=1; i<=5; i++) {
	    		CCSprite frame = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + String.format("60game/sprit4_%02d.png", i)));
	    		ani.addFrame(frame.getTexture());
			}
			break;
		case Game.kButtonEarth:
			spirit = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + "60game/sprit5_01.png"));
			item = itemMenu.getChildByTag(Game.kButtonEarth);
			for(int i=1; i<=5; i++) {
	    		CCSprite frame = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + String.format("60game/sprit5_%02d.png", i)));
	    		ani.addFrame(frame.getTexture());
			}
			break;
		case Game.kButtonMirror:
			spirit = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + "60game/sprit6_01.png"));
			item = itemMenu.getChildByTag(Game.kButtonMirror);
			for(int i=1; i<=5; i++) {
	    		CCSprite frame = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + String.format("60game/sprit6_%02d.png", i)));
	    		ani.addFrame(frame.getTexture());
			}
			break;
		}
		
		CGPoint pos2 = CGPoint.ccpAdd(itemBase.getPosition(), item.getPosition());

		//정령석 애니메이션, 점점 커지면서 위로 잠깐 솟았다가 아래 아이콘 영역으로 이동
		spirit.setPosition(pos);
		spirit.setScale(0.3f);
		addChild(spirit, 200);
		
		CCScaleTo scale = CCScaleTo.action(1.5f, 1.0f);
		spirit.runAction(scale);
		
		CCMoveBy moveby = CCMoveBy.action(0.35f, CGPoint.ccp(0, 150));
		CCMoveTo moveto = CCMoveTo.action(1.15f, pos2);
		CCAnimate action = CCAnimate.action(0.3f, ani, false);
		CCCallFuncND increase = CCCallFuncND.action(this, "cbIncreaseNumber", kind);
		spirit.runAction(CCSequence.actions(moveby, moveto, action, increase));
	}

	public void cbIncreaseNumber(Object sender, Object k) {
		CCSprite sprite = (CCSprite) sender;
		removeChild(sprite, true);
		
		int kind = (Integer) k;
		
		// 수정구 획득수를 타입별로 하나 증가시킨다.
		GameData.share().increaseItemByType(kind);

		// UI 업데이트를 한다.
		// 수정구를 클릭해서 활성화시키면 +1 씩 라벨에 넣어준다.
		updateSphereItemNumber();
	}
	
	//마법사 감전 애니
	public void startShockAni() {
		CCAnimate shock = CCAnimate.action(0.4f, mShockAni, false);
		CCRepeatForever shockAni = CCRepeatForever.action(shock);
		
		CCSprite sprite = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + "61hud/Shock-1.png"));
		this.addChild(sprite, 11, 2001);
		sprite.setPosition(winSize.width * 0.5f, winSize.height * 0.25f);
		sprite.runAction(shockAni);
		
		//기존 마법사는 안보이게 한다.
		magician.setVisible(false);
	}
	
	public void stopShockAni() {
		this.removeChildByTag(2001, true);
		magician.setVisible(true);
	}
	
	// 이동 하였습니다.
	public void animationMagicianAction(CCNode parentNode) {
		String aniTime = "1,2,3,4,5,6,6,6,6,7,8,8";
		String[] array = aniTime.split(",");
		
		CCSprite sprite = CCSprite.sprite(cache.getSpriteFrame("ra01.png"));
		sprite.setScale(2);
		sprite.setPosition(parentNode.getContentSize().width * 0.57f, parentNode.getContentSize().height * magicianPositionY );
		parentNode.addChild(sprite);
		
		ArrayList<CCSpriteFrame> frames = new ArrayList<CCSpriteFrame>();
		for (int i = 0; i < array.length; i++) {
			frames.add(cache.getSpriteFrame((String.format("ra%02d.png", i+1))));
		}
		CCAnimation animation = CCAnimation.animation("magicAction", 0.05f, frames);
		CCAnimate animate = CCAnimate.action(animation);
		CCSequence sequence = CCSequence.actions(animate);
//		sprite.runAction(sequence);
		CCCallFuncN _removeAction = CCCallFuncN.action(this, "removeAction");
		sprite.runAction(CCSequence.actions(sequence, _removeAction));
	}

	public void removeAction(Object sender) {
		((CCSprite)sender).removeFromParentAndCleanup(true);
	}
	
//	public void animationMagicianAction(CCNode parentNode) {
//		ArrayList<CCSpriteFrame> frames = new ArrayList<CCSpriteFrame>();
//		frames.add(cache.getSpriteFrame("loading-magician01.png"));
//		frames.add(cache.getSpriteFrame("loading-magician02.png"));
//		
//		CCAnimation animation = CCAnimation.animation("magic", 0.3f, frames); 
//		CCAnimate animate = CCAnimate.action(animation);
//		CCSprite sprite = CCSprite.sprite(cache.getSpriteFrame("loading-magician01.png"));
//		CCSequence sequence = CCSequence.actions(animate);
//		CCRepeatForever repeat = CCRepeatForever.action(sequence);
//		sprite.runAction(repeat);
//		parentNode.addChild(sprite);
//		sprite.setPosition(parentNode.getContentSize().width/2,parentNode.getContentSize().height * 0.6f); 
//	}
}
