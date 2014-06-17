package com.aga.mine.pumpkin;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cocos2d.layers.CCLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.menus.CCMenu;
import org.cocos2d.menus.CCMenuItem;
import org.cocos2d.menus.CCMenuItemImage;
import org.cocos2d.menus.CCMenuItemSprite;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCLabel;
import org.cocos2d.nodes.CCNode;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.nodes.CCTextureCache;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.ccColor3B;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.aga.mine.pages2.UserData;
import com.aga.mine.pumpkin.MainActivity.InviteCallback;
import com.aga.mine.util.UtilResource;
import com.blundell.tutorial.simpleinappbillingv3.domain.items.Passport;
import com.blundell.tutorial.simpleinappbillingv3.ui.StartUpActivity;

public class ShopGold2 extends CCLayer {
	
	final String commonfolder = "00common/";
	final String folder = "21gold/";
	final String fileExtension = ".png";
	
	Map<String, String> basket;
	
	CCSprite bg;
	CCLabel goldLabel = null; 
	
	double[][] goldArray = { 
			{ 0.99, 5000, 0 }, { 4.99, 25000, 6250 },
			{ 9.99, 50000, 15000 }, { 24.99, 125000, 43750 },
			{ 49.99, 250000, 100000 }, { 99.99, 500000, 225000 } };
	
	private Context mContext;
	UserData userData;
	
	public static CCScene scene() {
		CCScene scene = CCScene.node();
		CCLayer layer = new ShopGold2();
		scene.addChild(layer);
//		scene.addChild(InvitationReceiver.getInstance().getInvitationPopup());
		return scene;
	}

	int gold;
	public InviteCallback mInviteCallback = new InviteCallback() {

		@Override
		public void onInvited(List<String> invitedFriends, String requestId) {
			//To Do:
			String senderID = FacebookData.getinstance().getUserInfo().getId();
			
			for (String recipientID : invitedFriends) {
				Log.e("ShopGold2", "recipientID : " + recipientID);
				String data = 
						"0,RequestModeMailBoxAdd*22," + requestId + "*1," + recipientID + "*19," + senderID + "*20,Gold*21," + gold;				
				DataFilter.sendMail(data);
			}
			
		}
    };
	
	public ShopGold2() {
		mContext = CCDirector.sharedDirector().getActivity();
		userData = UserData.share(mContext);
		basket = new HashMap<String, String>();
		
		MainApplication.getInstance().getActivity().setInviteCallback(mInviteCallback);
		
		// BackGround 파일
		bg = BackGround.setBackground(this, CGPoint.make(0.5f, 0.5f), commonfolder + "bg1" + fileExtension);
		setBackBoardMenu(commonfolder + "bb1" + fileExtension);
		setBoardFrameMenu(commonfolder + "frameGeneral-hd" + fileExtension);
		
		TopMenu2.setSceneMenu(this);
		BottomImage.setBottomImage(this);
		
		this.setIsTouchEnabled(true);
	}
	
	private void setBackBoardMenu(String imageFullPath) {
		CCSprite bb = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + imageFullPath));
		bg.addChild(bb);
		bb.setPosition(bg.getContentSize().width / 2, bg.getContentSize().height * 0.525f);
		bb.setAnchorPoint(0.5f, 0.5f);
		setMainMenu(bb);
	}
	
	private void setBoardFrameMenu(String imageFullPath) {
		CCSprite boardFrame = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + imageFullPath));
		bg.addChild(boardFrame);
		boardFrame.setPosition(bg.getContentSize().width / 2, bg.getContentSize().height * 0.525f);
		boardFrame.setAnchorPoint(0.5f, 0.5f);
		
//		FrameTitle6.setTitle(boardFrame, folder);
		goldLabel = FrameTitle6.setTitle(boardFrame, folder);
//		gold.setString("" + UserData.share(CCDirector.sharedDirector().getActivity()).getGold());
	}
	
	private void setMainMenu(CCSprite parentSprite){
		CCMenu gameMenu = CCMenu.menu(buttons(goldArray));
		gameMenu.alignItemsVertically(-3);
		gameMenu.setPosition(CGPoint.ccp(parentSprite.getContentSize().width / 2 - 5, parentSprite.getContentSize().height / 2 - 12));
		parentSprite.addChild(gameMenu);
	}
	
	private CCMenuItem[] buttons(double[][] valuesArray) {
		List<CCMenuItem> listMenus = new ArrayList<CCMenuItem>();
		
		for (double[] ds : valuesArray) {
			CCMenuItemSprite button = CCMenuItemImage.item(
					CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + folder + "buttonnormal" + fileExtension)),
					CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + folder + "buttonpressed" + fileExtension)),
					this, "buttonCallback");
			button.setUserData(ds[0]);
			
			// 골드 이미지
			CCSprite goldimage = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + folder + (int)ds[1] + fileExtension));
			goldimage.setPosition(goldimage.getContentSize().width / 2, button.getContentSize().height / 2);
			button.addChild(goldimage);
			
			// 인앱 가격
			CCLabel  usd = CCLabel.makeLabel("$ " + ds[0], "Arial", 26);
			usd.setColor(ccColor3B.ccYELLOW);
			usd.setAnchorPoint(1, 1);
			usd.setPosition(button.getContentSize().width - 15, button.getContentSize().height - 5);
			button.addChild(usd);
			
			// 골드&추가골드
			CCLabel  value = CCLabel.makeLabel(plus((int)ds[1], (int)ds[2]), "Arial", 30);
			value.setAnchorPoint(1, 0);
			value.setPosition(button.getContentSize().width - 15, 10);
			button.addChild(value);
			
			//gold Text
			CCLabel  gold = CCLabel.makeLabel("Gold", "Arial", 18);
			gold.setColor(ccColor3B.ccYELLOW);
			gold.setAnchorPoint(value.getAnchorPoint());
			gold.setPosition(value.getPosition().x - value.getContentSize().width - 10, value.getPosition().y + 4);
			button.addChild(gold);
			
			listMenus.add(button);
		}
		
		CCMenuItem[] menusArray = listMenus.toArray(new CCMenuItem[listMenus.size()]);
		return menusArray;
	}
	
	private String plus	(int value1, int value2) {
		String values = numberComma(value1);
		if (value2 > 0)
			values = values + " + " + numberComma(value2);
		return values;
	}
	
	// int 값을 String으로 변환하여 comma 삽입
	private String Decimal(int value) {
		DecimalFormat format = new DecimalFormat("###,###,###,###");
		String ret = format.format(value);
		return ret;
	}
	
	// int 값을 String으로 변환하여 comma 삽입
	private String numberComma(int value) {
		String s = Integer.toString(value);
		return numberComma(s);
	}
	
	// String에 comma 삽입
	private String numberComma(String string) {
		if (string.length() < 4) 
			return string;
		
		String value = string;
		int len = value.length();
		int position = 3;
		for (int i = 0; i < (len -1) / 3; i++) {
			value = value.substring(0, (value.length() - position)) + "," + value.substring((value.length() - position), value.length());
			position += 4;
		}
		return value;
	}

	// config 파일에 나중에 옮길것
	public static boolean buttonActive = true;
	final int previous = 501;
	final int home= 502;
	
	// sceneCallback들 전부 여기로 옮기기
	public void clicked(Object sender) {
		MainApplication.getInstance().getActivity().click();
		CCScene scene = null;
		int value = ((CCNode) sender).getTag();
		if (buttonActive) {
			switch (value) {
			case previous:
				scene = Shop.scene();
				break;

			case home:
				scene = Home.scene();
				break;
			}
			CCDirector.sharedDirector().replaceScene(scene);
		}
	}
	
	public void buttonCallback(Object sender) {
		MainApplication.getInstance().getActivity().click();
		String Product = "gold";
		float bonus = 1.2f;
		double usd = (Double)((CCNode)sender).getUserData();
		gold = 5000;
		
		for (int i = 0; i < goldArray.length; i++) {
			if (usd == goldArray[i][0]) {
				Product = Product + i;
				break;
			}
		}
		
		if (usd == goldArray[0][0]) {
			gold = (int)(gold * (usd +  0.01));
		} else {
			for (int i = 0; i < goldArray.length; i++) {
				if (usd == goldArray[i][0] ) {
					gold = (int) (gold * (usd +  0.01) * (bonus + (i * 0.05f)))   ;
					break;
				}
			}
		}
		
		final String recipientID = FacebookData.getinstance().getRecipientID(); // 상점 이동 방식에 따른 ID 변경
		final String user = FacebookData.getinstance().getUserInfo().getId();
		long myGold = Integer.parseInt(FacebookData.getinstance().getDBData("Gold"));
		
		// 받는사람이 본인이면 DB로 바로 저장
		if (recipientID.equals(user)) {
			_mygold = (int) myGold;
			_price = gold;
			pps = _price / 2;
			basket.put("Gold", String.valueOf(myGold + gold));	
			FacebookData.getinstance().modDBData(basket);
		} else {
			double requestID = Math.random() * 9223372036854775807L;  //facebook 알림글번호로 대체할 것
			String senderID = FacebookData.getinstance().getUserInfo().getId();
			MainApplication.getInstance().mHandler.post(new Runnable() {
				@Override
				public void run() {
					MainApplication.getInstance().getActivity().sendInvite(recipientID, user + "님이 선물을 보냈습니다.", null);
				}
			});
			
		}
		
		//경험치 및 레벨업 애니메이션
		//경험치 1000당 1초
		schedule("goldAni");
		
//		goldLabel.setString(new NumberComma().numberComma(myGold));

				
				

//				FacebookData.getinstance().getRequestID(recipientID);  //test용 

//		
//		String data = 
//				"0,RequestModeMailBoxAdd*22," + requestID + 
//				"*1," + recipientID + "*19," + senderID + "*20,Gold*21," + gold;
//		
//		DataFilter.sendMail(data);
		
		
		Passport.SKU = Product;
		Log.e("ShopGold2", "buttonCallback(usd) : " + usd);
		
		inAppBilling();
	}
		
	private void inAppBilling() {
		Intent task1 = new Intent(CCDirector.sharedDirector().getActivity(), StartUpActivity.class);
    	CCDirector.sharedDirector().getActivity().startActivity(task1);
	}
	
	int _mygold;
	int _price;
	int pps;
	int gamso = 0;
	
	public void goldAni(float dt) {
//		Log.e("ShopGold", "dt : " + dt + ", pps : " + pps + ", gamso : " + gamso + ", _price : " + _price);
		gamso += dt * pps;
		goldLabel.setString(new NumberComma().numberComma(_mygold + gamso));
		if (gamso > _price) {
			unschedule("goldAni");
			goldLabel.setString(new NumberComma().numberComma(FacebookData.getinstance().getDBData("Gold")));
			gamso = 0;
		}
	}	
	
}
