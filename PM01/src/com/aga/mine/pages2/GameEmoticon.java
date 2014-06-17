package com.aga.mine.pages2;

import java.io.IOException;

import org.cocos2d.actions.base.CCFiniteTimeAction;
import org.cocos2d.actions.interval.CCMoveTo;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.layers.CCLayer;
import org.cocos2d.menus.CCMenu;
import org.cocos2d.menus.CCMenuItem;
import org.cocos2d.menus.CCMenuItemImage;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCLabel;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.nodes.CCTextureCache;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGSize;

import android.util.Log;
import android.view.MotionEvent;

import com.aga.mine.pumpkin.Config;
import com.aga.mine.pumpkin.NetworkController;
import com.aga.mine.pumpkin.Utility;
import com.aga.mine.util.UtilResource;


// 이미지 파일명의 -hd를 제거 하여서 오류 발생 함.
// 완료후 파일명 변경할것
public class GameEmoticon extends CCLayer{

	String folder = "62game_emoticon/";
	static final int kButtonBase = 0;
	static final int kButtonSend = 1;
	CGPoint basePositionOn;
	CGPoint basePositionOff;
	CCSprite base;
	boolean isEmoticonPanelOn;
	
	public GameEmoticon() {
		layout();
	}

	private void layout() {
//		CCTouchDispatcher.sharedDispatcher().addTargetedDelegate(this, 0, true);
//		this.setIsTouchEnabled(true); 

		CGSize winSize = CCDirector.sharedDirector().winSize();
		isEmoticonPanelOn = false;
		
		//
		// 베이스
		base = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + folder + "emoticon-base.png"));
		this.addChild(base);
		CGSize baseActive = CGSize.make(
				base.getContentSize().width - base.getContentSize().width / 7.7f, 
				base.getContentSize().height);
		
		//
		// 탭 버튼
		CCMenuItem item = CCMenuItemImage.item(
				CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + folder + "emoticon-baseButton-hd.png")),
				CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + folder + "emoticon-baseButton-hd.png")),
				this, "clicked");
		item.setTag(kButtonBase);
		CCMenu baseButton = CCMenu.menu(item);
		base.addChild(baseButton);
		baseButton.setPosition(
				base.getContentSize().width - item.getContentSize().width /2,
				base.getContentSize().height /2 + 5f);
		basePositionOn = CGPoint.ccp(winSize.width / 2, winSize.height /2);
		basePositionOff = CGPoint.ccp(
				basePositionOn.x - base.getContentSize().width + item.getContentSize().width + 5, 
				basePositionOn.y);
		
		base.setPosition(GameConfig.share().isEmoticonPanelOn ? basePositionOn : basePositionOff);
		
		//
		// 제목
		String titleFile = Utility.getInstance().getNameWithIsoCodeSuffix(folder + "emoticon-title.png");
		CCSprite title = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + titleFile));
		base.addChild(title);
		title.setPosition(baseActive.width / 2, baseActive.height - title.getContentSize().height * 1.2f);
		
		//
		// 이모티콘들
		CCSprite emoticonBase = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + folder + "emoticon-tiles.png"));
		base.addChild(emoticonBase);
		emoticonBase.setPosition(baseActive.width / 2, baseActive.height / 2 + baseActive.height / 50);
		
		// 이모티콘 셀 위치값 확인
		CGPoint basePosition = CGPoint.ccp(baseActive.width / 11f, baseActive.height / 3.5f);
		float cellSize = 38f * 2; // 128pixel 기준(현재 64pixel 사용중) 
		for (int i = 0; i < 7; i++) {
			for (int k = 0; k < 4; k++) {
				CCLabel mark = CCLabel.makeLabel("+", "Arial", 15);
				base.addChild(mark);
				mark.setPosition(basePosition.x + cellSize * i, basePosition.y + cellSize * k);
				mark.setTag(i*10+k);
			}
		}
		
		//
		// 입력창
		CCSprite inputBase = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + folder + "emoticon-inputBase-hd.png"));
		base.addChild(inputBase);
		inputBase.setPosition(baseActive.width/2 , inputBase.getContentSize().height * 0.9f);
		
		//
		// 보내기 버튼
		CCMenuItem itemSend = CCMenuItemImage.item(
				CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + folder + "emoticon-buttonSend-hd.png")), 
				CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + folder + "emoticon-buttonSendOver-hd.png")),
				this, "clicked");
		itemSend.setTag(kButtonSend);
		CCMenu buttonSend = CCMenu.menu(itemSend);
		inputBase.addChild(buttonSend);
		buttonSend.setPosition(
				inputBase.getContentSize().width - itemSend.getContentSize().width * 0.6f, 
				itemSend.getContentSize().height * 0.6f);
		
		//
		// 버튼 글자
		// 언어 파악후 파일명에 붙여서 저장하기
		//String textFile = Utility.getInstance().getNameWithIsoCodeSuffix("emoticon-textSendKo-hd.png");
		String textFile = folder + Utility.getInstance().getNameWithIsoCodeSuffix("emoticon-textSend.png");
		CCSprite textSend = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + textFile));
		itemSend.addChild(textSend);
		textSend.setPosition(
				itemSend.getContentSize().width / 2, 
				itemSend.getContentSize().height / 2);
	}
	// layout() end

	@Override
	public boolean ccTouchesBegan(MotionEvent event) {
		return true;
	}

	@Override
	public boolean ccTouchesEnded(MotionEvent event) {
		return super.ccTouchesEnded(event);
	}
	
	
	//public void clicked(CCMenuItem button) {
	public void clicked(Object sender) {
		//button clicked
		Log.e("GameEmoticon / clicked", "clicked");
		
		switch (((CCMenuItem)sender).getTag()) {
		case kButtonBase:
			Log.e("clicked", "kButtonBase clicked");

			if (GameConfig.share().isMinimapPanelOn == false) {
				
				// 이모티콘 패널 온/오프
				isEmoticonPanelOn = !isEmoticonPanelOn;
				GameConfig.share().setEmoticonPanelOn(isEmoticonPanelOn);
				
				// 온/오프 애니메이션
				CCFiniteTimeAction moveOn = CCMoveTo.action(GameConfig.share().kEmoticonPanelMoveTime, basePositionOn);
				CCFiniteTimeAction moveOff = CCMoveTo.action(GameConfig.share().kEmoticonPanelMoveTime, basePositionOff);
				
				if(isEmoticonPanelOn) {
					Config.getInstance().setDisableButton(true);
					base.runAction(CCSequence.actions(moveOn));
				} else {
					Config.getInstance().setDisableButton(false);
					base.runAction(CCSequence.actions(moveOff));
				}
			}
			break;
		case kButtonSend:
			Log.e("clicked", "kButtonSend clicked");
			if (GameData.share().isMultiGame) {
				int EmoticonID = 1; // 인자값은 이모티콘 ID값을 넣는다.
				try {
					NetworkController.getInstance().sendPlayDataEmoticon(EmoticonID);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			break;

		default:
			break;
		}
		Log.e("Method", "clicked end");
	}
	// clicked() end

}
// class end
