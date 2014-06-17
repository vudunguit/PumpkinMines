package com.aga.mine.pumpkin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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
import android.util.Log;
import android.widget.Toast;

import com.aga.mine.pages2.UserData;
import com.aga.mine.pumpkin.MainActivity.InviteCallback;
import com.aga.mine.util.Popup;
import com.aga.mine.util.UtilResource;

public class ShopBroomstick2 extends CCLayer {
	
	final String commonfolder = "00common/";
	final String folder = "23broomstick/";
	final String fileExtension = ".png";
	
	Map<String, String> basket;
	
	CCSprite bg;
	CCLabel goldLabel;
	
	long [][] stickArray = { 
			{ 1000, 5, 0 }, { 1800, 10, 0 }, { 3400, 20, 0 },
			{ 6400, 40, 0 }, { 12000, 80, 0 }, { 22400, 160, 0 } };
	
	private Context mContext;
	UserData userData;
	
	public static CCScene scene() {
		CCScene scene = CCScene.node();
		CCLayer layer = new ShopBroomstick2();
		scene.addChild(layer);
		return scene;
	}
	
	public InviteCallback mInviteCallback = new InviteCallback() {

		@Override
		public void onInvited(List<String> invitedFriends, String requestId) {
			//To Do:
			String senderID = FacebookData.getinstance().getUserInfo().getId();
			
			for (String recipientID : invitedFriends) {
				Log.e("ShopBroomstick2", "recipientID : " + recipientID);
				String data = 
						"0,RequestModeMailBoxAdd*22," + requestId + "*1," + recipientID + "*19," + senderID + "*20,Broomstick*21," + quantity;				
				DataFilter.sendMail(data);
			}
			
		}
    };

	public ShopBroomstick2() {
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
		
		goldLabel = FrameTitle6.setTitle(boardFrame, folder);
	}
	
	private void setMainMenu(CCSprite parentSprite){
		CCMenu gameMenu = CCMenu.menu(buttons(stickArray));
		gameMenu.alignItemsVertically(-3);
		gameMenu.setPosition(CGPoint.ccp(parentSprite.getContentSize().width / 2 - 5, parentSprite.getContentSize().height / 2 - 12));
		parentSprite.addChild(gameMenu);
	}
	
	private CCMenuItem[] buttons(long[][] valuesArray) {
		List<CCMenuItem> listMenus = new ArrayList<CCMenuItem>();
		
		for (long[] ds : valuesArray) {
			CCMenuItemSprite button = CCMenuItemImage.item(
					CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + folder + "buttonnormal" + fileExtension)),
					CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + folder + "buttonpressed" + fileExtension)),
					this, "buttonCallback");
			button.setUserData(ds);
			listMenus.add(button);
			
			// 빗자루 이미지
			CCSprite goldimage = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + folder + (int)ds[1] + fileExtension));
			goldimage.setPosition(goldimage.getContentSize().width / 2, button.getContentSize().height / 2);
			button.addChild(goldimage);
			
			// 가격
			CCLabel  gold = CCLabel.makeLabel(new NumberComma().numberComma(ds[0]), "Arial", 28);
			gold.setAnchorPoint(1, 0);
			gold.setPosition(button.getContentSize().width - 15, 14);
			button.addChild(gold);
			
			//gold Text
			CCLabel  goldText = CCLabel.makeLabel("Gold", "Arial", 20);
			goldText.setColor(ccColor3B.ccYELLOW);
			goldText.setAnchorPoint(0, 1);
			goldText.setPosition(
					gold.getPosition().x - (gold.getContentSize().width * gold.getAnchorPoint().x), 
					button.getContentSize().height - 9);
			button.addChild(goldText);
			
			// 빗자루
			String broomstick = plus((int)ds[1], (int)ds[2]) + " BROOMSTICK";
			float positionX = button.getContentSize().width - 117;
			if (Locale.getDefault().getLanguage().toString().equals("ko")) {
				broomstick = "빗자루 " + plus((int)ds[1], (int)ds[2]) + "개";
				positionX = button.getContentSize().width - 132;
			}
			CCLabel stick = CCLabel.makeLabel(broomstick, "Arial", 26);
			if (!Locale.getDefault().getLanguage().toString().equals("ko"))
				stick.setScaleX(0.85f);
			stick.setAnchorPoint(1, 0);
			stick.setPosition(positionX, 15);
			button.addChild(stick);
		}
		
		CCMenuItem[] menusArray = listMenus.toArray(new CCMenuItem[listMenus.size()]);
		return menusArray;
	}
	
	private String plus	(int value1, int value2) {
		String values = new NumberComma().numberComma(value1);
		if (value2 > 0)
			values = values + "+" + new NumberComma().numberComma(value2);
		return values;
	}

	// config 파일에 나중에 옮길것
	public static boolean buttonActive = true;
	final int previous = 501;
	final int home= 502;
	
	// sceneCallback들 전부 여기로 옮기기
	public void clicked(Object sender) {
		int value = ((CCNode) sender).getTag();
		if (buttonActive) {
			MainApplication.getInstance().getActivity().click();
			switch (value) {
			case previous:
				CCDirector.sharedDirector().replaceScene(Shop.scene());
				break;

			case home:
				CCDirector.sharedDirector().replaceScene(Home.scene());
				break;
				
			case Constant.PURCHASING_OK:
				makeAPurchase();
				this.removeChildByTag(Constant.POPUP_LAYER, true);
				break;
				
			case Constant.PURCHASING_CANCEL:
				this.removeChildByTag(Constant.POPUP_LAYER, true);
				break;
			}

		}
	}

	private long quantity;
	private long price;
	
	private void makeAPurchase() {
		long myGold = Integer.parseInt(FacebookData.getinstance().getDBData("Gold"));
		int myBroomstick = Integer.parseInt(FacebookData.getinstance().getDBData("ReceivedBroomstick"));
		basket.put("Gold", String.valueOf(myGold - price));	
		basket.put("ReceivedBroomstick", String.valueOf(myBroomstick + quantity));	
		FacebookData.getinstance().modDBData(basket);
		_mygold = (int) myGold;
		_price = (int) -price;
		pps = _price / 2;
		// 골드 차감 애니
		schedule("goldAni");
	}
	
	int _mygold;
	int _price;
	int pps;
	int gamso = 0;
	
	public void goldAni(float dt) {
		gamso += dt * pps;
		goldLabel.setString(new NumberComma().numberComma(_mygold + gamso));
		// 골드 차감은 gamso가 작아져야함.
		if (gamso < _price) {
			unschedule("goldAni");
			goldLabel.setString(new NumberComma().numberComma(FacebookData.getinstance().getDBData("Gold")));
			gamso = 0;
		}
	}	
	
	
	public void buttonCallback(Object sender) {
		MainApplication.getInstance().getActivity().click();
		long[] value = (long[]) ((CCNode)sender).getUserData();
		price = value[0];
		quantity = value[1];
		
		long gold = Integer.parseInt(FacebookData.getinstance().getDBData("Gold"));
		
		// 골드 부족 (종료) 
		if (gold < price) {
			CCDirector.sharedDirector().getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(mContext, "골드가 부족합니다.", Toast.LENGTH_SHORT).show();
				}
			});
			return;
		}
		Popup.popupOfPurchase(this);
	}
	
}
//end