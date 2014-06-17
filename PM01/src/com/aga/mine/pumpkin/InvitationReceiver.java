package com.aga.mine.pumpkin;

import java.io.IOException;

import org.cocos2d.layers.CCLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.menus.CCMenu;
import org.cocos2d.menus.CCMenuItem;
import org.cocos2d.menus.CCMenuItemImage;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCLabel;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.types.CGSize;
import org.cocos2d.types.ccColor3B;

import android.content.Context;
import android.util.Log;

import com.aga.mine.pages2.UserData;
import com.aga.mine.pumpkin.NetworkController;
import com.facebook.model.GraphUser;
import com.sromku.simple.fb.entities.Profile;

public class InvitationReceiver extends CCLayer{

	CCLayer Popup = CCLayer.node();
	CCLabel  text1 = null;
	CCLabel text2 = null;
	Context mContext;
	UserData userData;
	
	private static InvitationReceiver invitationReceiver;
	
	public static InvitationReceiver getInstance() {
		if (invitationReceiver == null) {
			invitationReceiver = new InvitationReceiver();
		}
		return invitationReceiver;
	}
	
	private InvitationReceiver() {
		mContext = CCDirector.sharedDirector().getActivity();
		userData = UserData.share(mContext);
		
		CCSprite bg = CCSprite.sprite("00common/opacitybg.png");
		Popup.addChild(bg);
		bg.setPosition(winsize().width / 2, winsize().height / 2);
		
		CCSprite backBoard = CCSprite.sprite("mirroritem/ref-popup.png");
		bg.addChild(backBoard);
		backBoard.setPosition(bg.getContentSize().width / 2, bg.getContentSize().height / 2);
		
		text1 = CCLabel.makeLabel("blank ", "Arial", 30.0f);
		backBoard.addChild(text1);
		text1.setColor(ccColor3B.ccc3(94, 62, 65));
		text1.setPosition(backBoard.getContentSize().width / 2, backBoard.getContentSize().height / 4 * 3);
		
		text2 = CCLabel.makeLabel("blank", "Arial", 30.0f);
		backBoard.addChild(text2);
		text2.setColor(text1.getColor());
		text2.setPosition(text1.getPosition().x, text1.getPosition().y - text1.getContentSize().height);
		
		CCLabel text3 = CCLabel.makeLabel("대전을 신청 하였습니다. ", "Arial", 30.0f);
		backBoard.addChild(text3);
		text3.setColor(text2.getColor());
		text3.setPosition(text2.getPosition().x, text2.getPosition().y - text2.getContentSize().height);
		
		CCLabel text4 = CCLabel.makeLabel("싸울까~?", "Arial", 40.0f);
		backBoard.addChild(text4);
		text4.setColor(text3.getColor());
		text4.setPosition(text3.getPosition().x, text3.getPosition().y - text3.getContentSize().height * 1.5f);
		
		
		
		a1 = CCMenuItemImage.item("mirroritem/mirrorbutton1.png",
				"mirroritem/ref-popup-ok-select.png", this, "aCallback");

		b1 = CCMenuItemImage.item("mirroritem/mirrorbutton2.png",
				"mirroritem/ref-popup-Cancel-select.png", this, "bCallback");
		
		inviteMenu = CCMenu.menu(a1, b1);
		backBoard.addChild(inviteMenu);
		inviteMenu.setAnchorPoint(0, 0);
		inviteMenu.setPosition(0, 0);
		
//		a1.setAnchorPoint(0.15f, 0.2f);
		a1.setAnchorPoint(0, 0);
		a1.setPosition(a1.getAnchorPoint().x * backBoard.getContentSize().width, a1.getAnchorPoint().y * backBoard.getContentSize().height);
//		b1.setAnchorPoint(0.85f, 0.2f);
		b1.setAnchorPoint(1, 0);
		b1.setPosition(b1.getAnchorPoint().x * backBoard.getContentSize().width, b1.getAnchorPoint().y * backBoard.getContentSize().height);
		
		hidePopup();
	}
	CCMenu inviteMenu;
	CCMenuItem a1 = null;
	CCMenuItem b1 = null;
	
//	public void setbutton(CCNode nodeThis) {
//		a1.item(nodeThis, "aCallback");
////		b1.item(nodeThis, "bCallback");
//		CCMenuItem.item(this, "bCallback");
//	}
	
	String oppenentPlayer = "";
	
	public void setUserName(final int diffculty, final String matchedOppenentID) {
		
		for (Profile friend : userData.facebookFriendsInfo) {
			if (matchedOppenentID.equals(friend.getId())) {
				Log.e("invite", "friend.getId() : " + friend.getId());
				oppenentPlayer = friend.getName();
				break;
			}
		}
		
		final String[] diffcultyArray = {"초급", "중급", "상급"};
		CCDirector.sharedDirector().getActivity().runOnUiThread(new Runnable() {
			public void run() {
				text1.setString(oppenentPlayer + " 님이");
				text2.setString(diffcultyArray[diffculty - 1] + " 난이도의 ");
			}
		});
		oppenentPlayer = matchedOppenentID;
	}
	
	private static CGSize winsize() {
		return CCDirector.sharedDirector().winSize();
	}
	
	public CCLayer getInvitationPopup(){
		return Popup;
	}
	
	public void exposePopup(){
		Popup.setVisible(true);
		a1.setIsEnabled(true);
		b1.setIsEnabled(true);
	}
	

	public void hidePopup(){
		Popup.setVisible(false);
		a1.setIsEnabled(false);
		b1.setIsEnabled(false);
	}
	
	public void aCallback(Object sender) {
		try {
			NetworkController.getInstance().sendRoomOwner(NetworkController.getInstance().guest);
			NetworkController.getInstance().sendResponseMatchInvite(1, oppenentPlayer);
//			CCScene scene = GameInvite.getInstance().scene();
//			CCDirector.sharedDirector().replaceScene(scene);
//			GameInvite.getInstance().matchNameReceiver(oppenentPlayer, NetworkController.getInstance().kTempName); // 이건 될랑가 몰라
			invitationReceiver = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void bCallback(Object sender) {
		try {
			NetworkController.getInstance().sendResponseMatchInvite(0, oppenentPlayer);
			hidePopup();
			invitationReceiver = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
}
