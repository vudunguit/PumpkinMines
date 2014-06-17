package com.aga.mine.pumpkin;

import java.io.IOException;
import java.util.Locale;

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
import org.cocos2d.types.ccColor3B;

import android.util.Log;

import com.aga.mine.pages2.GameData;
import com.aga.mine.util.UtilResource;
import com.sromku.simple.fb.entities.Profile;

public class RequestMatch extends CCLayer{
	
	static String oppenentPlayerID = ""; // networkController에서만 저장해둬도 될듯
	
	static CCSprite base;
	static CCLabel message1;
	static CCLabel message2;
	
	static CGPoint basePositionOn;
	CGPoint basePositionOff;

	boolean isLocaleKo = false;
	
	public RequestMatch() {
		if (Locale.getDefault().getLanguage().toString().equals("ko"))
			isLocaleKo = true;
		layout();
	}

	private void layout() {
		CGSize winSize = CCDirector.sharedDirector().winSize();
		
		String commonfolder = "00common/";
		String folder = "11mailbox/";
		String fileExtension = ".png";
		
		// 초대 창
		base = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + commonfolder + "popup-bg" + fileExtension));
		this.addChild(base);
		
		// 초대 메시지
		message1 = CCLabel.makeLabel("이름", "Arial", 26);
		message1.setColor(ccColor3B.ccBLACK);
		message1.setPosition(base.getContentSize().width / 2 , base.getContentSize().height * 0.82f);
		base.addChild(message1);
		message2 = CCLabel.makeLabel("난이도", "Arial", 26);
		message2.setColor(ccColor3B.ccBLACK);
		message2.setPosition(message1.getPosition().x , message1.getPosition().y - (message2.getContentSize().height * 1.1f));
		base.addChild(message2);
		
		// 초대 응답 버튼
		CCMenuItem accept = CCMenuItemImage.item(
				CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + folder + "receiveAllButtonNormal" + fileExtension)),
				CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + folder + "receiveAllButtonPress" + fileExtension)), 
				this, "clicked");
		accept.setTag(1);
		
		String acceptStr = "Accept";
		if (isLocaleKo)
			acceptStr = "승락";
		CCLabel textAccept = CCLabel.makeLabel(acceptStr, "Arial", 24);
		textAccept.setColor(ccColor3B.ccBLACK);
		textAccept.setPosition(accept.getContentSize().width / 2, accept.getContentSize().height / 2);
		accept.addChild(textAccept);
		CCMenu acceptMenu = CCMenu.menu(accept);
		acceptMenu.setContentSize(accept.getContentSize());
		acceptMenu.setPosition(base.getContentSize().width * 0.28f, base.getContentSize().height / 3);
		base.addChild(acceptMenu);
		
		// 초대 거부 버튼
		CCMenuItem reject = CCMenuItemImage.item(
				CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + folder + "receiveAllButtonNormal" + fileExtension)),
				CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + folder + "receiveAllButtonPress" + fileExtension)), 
				this, "clicked");
		reject.setTag(2);
		
		String rejectStr = "Reject";
		if (isLocaleKo)
			rejectStr = "거절";
		CCLabel textReject = CCLabel.makeLabel(rejectStr, "Arial", 24);
		textReject.setColor(ccColor3B.ccBLACK);
		textReject.setPosition(accept.getContentSize().width / 2, accept.getContentSize().height / 2);
		reject.addChild(textReject);
		CCMenu rejectMenu = CCMenu.menu(reject);
		rejectMenu.setContentSize(reject.getContentSize());
		rejectMenu.setPosition(base.getContentSize().width * 0.72f, base.getContentSize().height / 3);
		base.addChild(rejectMenu);
		
		
		basePositionOff = CGPoint.ccp(
				winSize.width / 2 + ((base.getAnchorPoint().x - 0.5f) * base.getContentSize().width), 
				winSize.height + (base.getAnchorPoint().y * base.getContentSize().height));
		basePositionOn = CGPoint.ccp(basePositionOff.x, basePositionOff.y - base.getContentSize().height);
		
		base.setPosition(GameConfig.share().isEmoticonPanelOn ? basePositionOn : basePositionOff);
	}

	static int difficulty;
	
	public static void inviteMatch(String id, int result) {
		difficulty = result;
		String name = null;
		String gameLevel = null;
		final int easy = 1;
		final int normal = 2;
		final int hard = 3;
	
		for (Profile friend : FacebookData.getinstance().getFriendsInfo()) {
			if (friend.getId().equals(id)) {
				name = friend.getName();
				oppenentPlayerID = id;
				break;
			}
		}
		
		switch (result) {
		case easy:
			gameLevel = "초급자";
			break;
		case normal:
			gameLevel = "중급자";
			break;
		case hard:
			gameLevel = "상급자";
			break;
		}
	
		message1.setString(name + "님이 ");
		message2.setString(gameLevel + " 난이도의 대전을 신청하였습니다.");
		
		if (base.getContentSize().width * 0.8f < message2.getContentSize().width) {
			message2.setScaleX((base.getContentSize().width * 0.8f)  / message2.getContentSize().width);
		}
		
		// 초대창 애니메이션 온
		CCFiniteTimeAction moveOn = CCMoveTo.action(GameConfig.share().kEmoticonPanelMoveTime, basePositionOn);
		base.runAction(CCSequence.actions(moveOn));
	}

	public void clicked(Object sender) {
		Log.e("RequestMatch", "clicked");
		MainApplication.getInstance().getActivity().click();
		if (Integer.valueOf(FacebookData.getinstance().getDBData("ReceivedBroomstick")) > 0) {
			// 초대창 애니메이션 오프
			CCFiniteTimeAction moveOff = CCMoveTo.action(GameConfig.share().kEmoticonPanelMoveTime, basePositionOff);
			base.runAction(CCSequence.actions(moveOff));
			
			switch (((CCMenuItem)sender).getTag()) {
			case 1:
				try {
					GameData.share().setGameDifficulty(difficulty);
					NetworkController.getInstance().sendRoomOwner(NetworkController.getInstance().guest);
					NetworkController.getInstance().sendWillYouAcceptInviteOk(oppenentPlayerID);
					
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
				
			case 2:
				Log.e("RequestMatch", "거절");
				break;
			}
		}
	}
	
}
