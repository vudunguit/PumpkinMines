package com.aga.mine.pumpkin;

import java.util.List;

import org.cocos2d.actions.UpdateCallback;
import org.cocos2d.layers.CCLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.menus.CCMenu;
import org.cocos2d.menus.CCMenuItem;
import org.cocos2d.menus.CCMenuItemImage;
import org.cocos2d.menus.CCMenuItemSprite;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCNode;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.nodes.CCTextureCache;
import org.cocos2d.types.CGPoint;

import android.util.Log;
import android.widget.Toast;

import com.aga.mine.pages2.GameData;
import com.aga.mine.pumpkin.MainActivity.InviteCallback;
import com.aga.mine.util.Util;
import com.aga.mine.util.UtilResource;

public class Home extends CCLayer{
		
	final String commonfolder = "00common/";
	final String folder = "10home/";
	final String fileExtension = ".png";
	
	final static int scrollLayerTag = 1;
	final static int homeLayerTag = 2;
	final static int mailBoxLayerTag = 3;

	// 나중에 다른 클래스에서 정의한 것과 하나로 합치기
	final int broomstickButton = 1001;
	final int goldButton = 1002;
	final int shopButton = 1003;
	final int enterButton = 1004;
	final int optionButton = 1005;
	final int inviteButton = 1006;
	final int mailButton = 1007;
	final int mailcloseButton = 1008;
	final int mailReceiveAllButton = 1009;
	final int presentGoldButton = 1010;
	final int presentBroomstickButton = 1011;
	final int broomTab = 1012;
	final int goldTab = 1013;
	
	private HomeTop mHomeTop;
	private HomeMiddle mHomeMiddle;
	
//	static CCLayer scroll = CCLayer.node(); // 안드로이드 뷰로 인하여 제거됨.
	
	public static CCScene scene() {
		Log.e("Home", "scene");
		CCScene scene = CCScene.node();
		CCLayer home = new Home();
		scene.addChild(home, homeLayerTag, homeLayerTag);
		CCLayer mailBox = CCLayer.node();
		scene.addChild(mailBox,mailBoxLayerTag,mailBoxLayerTag);
        return scene;
	}
	
    public InviteCallback mInviteCallback = new InviteCallback() {
    	
		@Override
		public void onInvited(List<String> invitedFriends, String requestId) {
			//when sending broom is successful, this callback is called.
			Log.e("Invite", "Callback_7 - onInvited" );
//			Log.d("LDK", "requestId:" + requestId);
			for (String string : invitedFriends) {
//				Log.e("Invite", "invitedFriend : " + string);
				String requestID = requestId;
				String recipientID = string;
				String senderID = FacebookData.getinstance().getUserInfo().getId();
				String sendMailData = 
						"0,RequestModeMailBoxAdd" +
						"*22," + requestID + 
						"*1," + recipientID + 
						"*19," + senderID + 
						"*20,Broomstick*21," + 1;
				DataFilter.sendMail(sendMailData);
				
				//save date to shared pref
				Util.setBroom(string);
			}
			MainApplication.getInstance().getActivity().mHandler.sendEmptyMessage(Constant.MSG_DISPLAY_RANKLIST);
		}
    };
	
	public Home() {
//		CCSprite backGround = BackGround.setBackground(this, CGPoint.make(0.5f, 0.5f), commonfolder + "bg-open" + fileExtension);
//		setBackBoardMenu(backGround, folder + "home-backboard" + fileExtension);
		CCSprite backGround = BackGround.setBackground(this, CGPoint.make(0.5f, 0.5f), commonfolder + "bg1" + fileExtension);
		setBackBoardMenu(backGround, commonfolder + "bb1" + fileExtension);
		setBoardFrameMenu(backGround, commonfolder + "frameGeneral-hd" + fileExtension);
		this.setIsTouchEnabled(true);
		
		//register invite callback
		MainApplication.getInstance().getActivity().setInviteCallback(mInviteCallback);
		
		this.addChild(new RequestMatch());
		
		MainApplication.getInstance().getActivity().mHandler.sendEmptyMessage(Constant.MSG_HIDE_SCROLLVIEW);
		//display scroll view
		MainApplication.getInstance().getActivity().mHandler.sendEmptyMessage(Constant.MSG_DISPLAY_RANKLIST);
		GameData.share().isMultiGame = false;
	}
	
//	private CGSize winsize() {
//		return CCDirector.sharedDirector().winSize();
//	}

	// 백 보드 설정
	private CCSprite setBackBoardMenu(CCSprite parent, String imageFullPath) {
		CCSprite backBoard = setHomeMenu(parent, imageFullPath);
		mHomeMiddle = new HomeMiddle(backBoard, folder, this);
		return backBoard;
	}

	// 게시판 설정
	private CCSprite setBoardFrameMenu(CCSprite parent, String imageFullPath) {
		CCSprite boardFrame = setHomeMenu(parent, imageFullPath);	
		mHomeTop = new HomeTop(boardFrame, folder, this);
		schedule(new UpdateCallback() {
			@Override
			public void update(float d) {
				refreshLeftTime(d);
			}
		});
//		new HomeBottom(boardFrame, folder, this);
		return boardFrame;
	}

	 public void refreshLeftTime(float dt) {
		mHomeTop.setLeftTime(dt);
	 }
	
	private CCSprite setHomeMenu(CCSprite parent, String imageFullPath) {
		
		CCSprite sprite = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + imageFullPath));
		parent.addChild(sprite);
		sprite.setPosition(parent.getContentSize().width / 2, parent.getContentSize().height * 0.525f);
		sprite.setAnchorPoint(0.5f, 0.5f);
		return sprite;
	}
	
	public void clicked(Object sender) {
		
//		final int broomstick = 0;
//		final int gold = 1;
//		final int shop = 2;
//		final int enter = 3;
//		final int option = 4;
//		final int invite = 5;
		final int mail = 6;
		final int mailclose = 7;
//		final int getItem = 8;
		final int mailReceiveAll = 9;
		final int presentGold = 10;
		final int presentBroomstick = 11;
		
		int value = 99;
		
		String data = (String) ((CCMenuItemImage)sender).getUserData();
		String[] callback = {"broomstick","gold","shop","enter","option",
				"invite","mailopen","mailclose","getItem","mailReceiveAll",
				"presentGold","presentBroomstick"};
		Log.e("Home", "CallBack1 : " + data);
		for (int i = 0; i < callback.length; i++) {
			if (data.indexOf(callback[i]) >= 0) {
				value = i;
				break;
			}
		}
		Log.e("Home", "CallBack2 : " + value);
		
		CCScene homeScene = (CCScene) this.getParent();
		CCLayer mailBoxLayer = (CCLayer) homeScene.getChildByTag(3);
		
		if (value == mail) {
			Log.e("Home", "CallBack2 : " + value);
			new MailBox(mailBoxLayer, "11mailbox/", this);
			
			List<CCNode> layers = homeScene.getChildren();
			for (CCNode ccNode : layers) {
				CCLayer tempLayer = (CCLayer) ccNode;
				if (tempLayer != mailBoxLayer) {
//					tempLayer.setIsTouchEnabled(false);
					List<CCNode> sprites = tempLayer.getChildren();
					for (CCNode ccNode2 : sprites) {
						Log.e("Home", "CCNode " + ccNode2);
						if (ccNode2 instanceof CCMenu) {
							Log.e("Home", "ccNode2 " + ccNode2);
							List<CCNode> tempMenuItems = ccNode2.getChildren();
							for (CCNode ccNode3 : tempMenuItems) {
								((CCMenuItem)ccNode3).setIsEnabled(false);
							}
//							CCMenuItem tempMenuItem = (CCMenuItem)ccNode2;	
						}
					}
				}
			}
//			((CCLayer)homeScene.getChildByTag(1)).setTouchEnabled(false);
//			((CCLayer)homeScene.getChildByTag(2)).setIsTouchEnabled(false);
//			((CCLayer)homeScene.getChildByTag(3)).setIsTouchEnabled(false);
////			((CCLayer)homeScene.getChildByTag(1)).removeAllChildren(true);
////			((CCLayer)homeScene.getChildByTag(2)).removeAllChildren(true);
//			CCLayer layer = CCLayer.node();
//			scene.addChild(homeLayer, 2, 2);
		} else if (value == mailclose) {
			mailBoxLayer.removeChildByTag(999, true);
			List<CCNode> layers = homeScene.getChildren();
			for (CCNode ccNode : layers) {
				if ((CCLayer)ccNode != mailBoxLayer) {
//					((CCLayer)ccNode).setIsTouchEnabled(false);
					((CCLayer)ccNode).setVisible(true);
				}
			}
//			((CCLayer)homeScene.getChildByTag(1)).setIsTouchEnabled(true);
//			((CCLayer)homeScene.getChildByTag(2)).setIsTouchEnabled(true);
		} else if (value == mailReceiveAll) {
			String[] items = data.split(",");
			for (String item : items) {
				Log.e("Home", "mailReceiveAll : " + item);
				DataFilter.deleteMail(item);
			}
			// child sprite 제거 방식으로 수정 요함.
			mailBoxLayer.removeChildByTag(999, true);
			new MailBox(mailBoxLayer, "11mailbox/", this);
		} else if (value == presentGold) {
			Log.e("Home", "presentGold : " + value);
			String[] items = data.split(",");
			for (String item : items) {
				if (!item.equals("presentGold")) {
					FacebookData.getinstance().setRecipientID(item);
					CCScene scene = ShopGold2.scene();
					CCDirector.sharedDirector().replaceScene(scene);
				}
			}
		} else if (value == presentBroomstick) {
//			Log.e("Home", "presentBroomstick : " + value);
//			String[] items = data.split(",");
//			for (String item : items) {
//				if (!item.equals("presentBroomstick")) {
//					String senderID = FacebookData.getinstance().getUserInfo().getId();
//					String sendMailData = 
//							"0,RequestModeMailBoxAdd*22," + FacebookData.getinstance().getRequestID() + 
//							"*1," + item + "*19," + senderID + "*20,Broomstick*21," + 1;
//					DataFilter.sendMail(sendMailData);
//				}
//			}
		}

	}
	


	public void clicked2(Object sender) {
		MainApplication.getInstance().getActivity().click();
		// hide scroll view
		MainApplication.getInstance().getActivity().mHandler
				.sendEmptyMessage(Constant.MSG_HIDE_SCROLLVIEW);

		int value = ((CCNode) sender).getTag();
		CCScene scene = null;

		switch (value) {
		case broomstickButton:
			Log.e("Home", "CallBack3 : broomstick " + value);
			FacebookData.getinstance().setRecipientID(
					FacebookData.getinstance().getUserInfo().getId());
			scene = ShopBroomstick2.scene();
			break;
		case goldButton:
			Log.e("Home", "CallBack3 : gold " + value);
			FacebookData.getinstance().setRecipientID(
					FacebookData.getinstance().getUserInfo().getId());
			scene = ShopGold2.scene();
			break;
		case shopButton:
			Log.e("Home", "CallBack3 : shop " + value);
			scene = Shop.scene();
			break;
		case enterButton:
			Log.e("Home", "CallBack3 : enter " + value);
			if (Integer.valueOf(FacebookData.getinstance().getDBData("ReceivedBroomstick")) > 0) {
				scene = GameMode.scene();	
			} else {
				CCDirector.sharedDirector().getActivity().runOnUiThread(new Runnable() {
					public void run() {
						Toast.makeText(MainApplication.getInstance().getApplicationContext(), "빗자루가 부족합니다.", Toast.LENGTH_SHORT).show();
						}
					});
				scene = Home.scene();	
			}
			break;
		case optionButton:
			Log.e("Home", "CallBack3 : option " + value);
			scene = Option.scene();
			break;
		case inviteButton:
			Log.e("Home", "CallBack3 : invite " + value);
			scene = Invite.scene();
			MainApplication.getInstance().getActivity().mHandler.sendEmptyMessage(Constant.MSG_DISPLAY_INVITELIST);
			break;
		// default:
		// Log.e("Home", "CallBack3 : Home " + value +
		// " scene에 임시로 저장 (불필요 할 것 같다.)");
		// scene = Home.scene();
		// break;
		}

		if (value <= inviteButton) {
			Log.e("Home", "clicked2() if (value <= inviteButton) 탔음");
			CCDirector.sharedDirector().replaceScene(scene);
			return;
		}

		CCScene homeScene = (CCScene) this.getParent();
		CCLayer mailBoxLayer = (CCLayer) homeScene.getChildByTag(mailBoxLayerTag);
		String data = (String) ((CCMenuItemSprite) sender).getUserData();

		if (value == mailButton) {
			// 안드로이드 스크롤뷰로 교체해야됩니다.
			new MailBox(mailBoxLayer, "11mailbox/", this);

			List<CCNode> layers = homeScene.getChildren();
			for (CCNode ccNode : layers) { // home scene에 붙은 layer
				CCLayer tempLayer = (CCLayer) ccNode;
				if (tempLayer != mailBoxLayer) { // layer중에 mailboxlayer만 제외
					tempLayer.setIsTouchEnabled(false);

					// List<CCNode> sprites = tempLayer.getChildren();
					// for (CCNode ccNode2 : sprites) {
					// Log.e("Home", "CCNode " + ccNode2);
					// if (ccNode2 instanceof CCMenu) { // layer중에 menu만 고르고
					// Log.e("Home", "ccNode2 " + ccNode2);
					// List<CCNode> tempMenuItems = ccNode2.getChildren(); //
					// menu에서 버튼을 골라서
					// for (CCNode ccNode3 : tempMenuItems) {
					// ((CCMenuItem)ccNode3).setIsEnabled(false); // 터치 잠금
					// }
					// } else if (ccNode2 instanceof CCMenuItem) {
					// ((CCMenuItem)ccNode2).setIsEnabled(false); // 바로
					// menuitem이 나오면 터치 잠금
					//
				}
			}
		} else if (value == mailcloseButton) {
			MainApplication.getInstance().getActivity().mHandler
					.sendEmptyMessage(Constant.MSG_DISPLAY_RANKLIST);
			mailBoxLayer.removeChildByTag(999, true);
			List<CCNode> layers = homeScene.getChildren();
			for (CCNode ccNode : layers) { // home scene에 붙은 layer
				CCLayer tempLayer = (CCLayer) ccNode;
				tempLayer.setIsTouchEnabled(true);
			}
		} else if (value == mailReceiveAllButton) {
			String[] items = data.split(",");
			for (String item : items) {
				Log.e("Home", "mailReceiveAll : " + item);
				if (!item.equals("")) {
					DataFilter.deleteMail(item);
				}
			}
			// child sprite 제거 방식으로 수정 요함.
			mailBoxLayer.removeChildByTag(999, true);
			//우편물 탭 상태에 따른 MAIL_TAB 새로 고침 
			if (isBroomTab)
				new MailBox(mailBoxLayer, "11mailbox/", this, Constant.MAIL_TAB_BROOM);
			else
				new MailBox(mailBoxLayer, "11mailbox/", this, Constant.MAIL_TAB_GOLD);
		} else if (value == presentGoldButton) {
			Log.e("Home", "presentGold : " + value);
			String[] items = data.split(",");
			for (String item : items) {
				FacebookData.getinstance().setRecipientID(item);
				scene = ShopGold2.scene();
				CCDirector.sharedDirector().replaceScene(scene);
			}
		} else if (value == presentBroomstickButton) {
//			Log.e("Home", "presentBroomstick : " + value);
//			String[] items = data.split(",");
//			for (String item : items) {
//				String senderID = FacebookData.getinstance().getUserInfo()
//						.getId();
//				String sendMailData = "0,RequestModeMailBoxAdd*22,"
//						+ FacebookData.getinstance().getRequestID() + "*1,"
//						+ item + "*19," + senderID + "*20,Broomstick*21," + 1;
//				DataFilter.sendMail(sendMailData);
//			}
		} else if (value == broomTab) {
			isBroomTab = true;
			mailBoxLayer.removeChildByTag(999, true);
			new MailBox(mailBoxLayer, "11mailbox/", this, Constant.MAIL_TAB_BROOM);
			int broomstickBg2Tag = 301;
			int presentBg2Tag = 302;
			int broomstickBg1Tag = 303;
			int presentBg1Tag = 304;
			mailBoxLayer.getChildByTag(999).getChildByTag(888).getChildByTag(broomstickBg2Tag).setVisible(false);
			mailBoxLayer.getChildByTag(999).getChildByTag(888).getChildByTag(presentBg2Tag).setVisible(true);
			mailBoxLayer.getChildByTag(999).getChildByTag(888).getChildByTag(broomstickBg1Tag).setVisible(true);
			mailBoxLayer.getChildByTag(999).getChildByTag(888).getChildByTag(presentBg1Tag).setVisible(false);
		} else if (value == goldTab) {
			isBroomTab = false;
			mailBoxLayer.removeChildByTag(999, true);
			new MailBox(mailBoxLayer, "11mailbox/", this, Constant.MAIL_TAB_GOLD);
			int broomstickBg2Tag = 301;
			int presentBg2Tag = 302;
			int broomstickBg1Tag = 303;
			int presentBg1Tag = 304;
			mailBoxLayer.getChildByTag(999).getChildByTag(888).getChildByTag(broomstickBg2Tag).setVisible(true);
			mailBoxLayer.getChildByTag(999).getChildByTag(888).getChildByTag(presentBg2Tag).setVisible(false);
			mailBoxLayer.getChildByTag(999).getChildByTag(888).getChildByTag(broomstickBg1Tag).setVisible(false);
			mailBoxLayer.getChildByTag(999).getChildByTag(888).getChildByTag(presentBg1Tag).setVisible(true);
		}
	}
	
	//우편물 탭 상태 저장 (임시용 나중에 모으기)
	boolean isBroomTab = true;
	
//	private void modifyMenu(CCMenu menu, boolean enabled) {
//		for (CCNode menuItem : menu.getChildren()) {
//			((CCMenuItem) menuItem).setIsEnabled(enabled);
//		}
//	}
}