package com.aga.mine.pumpkin;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.cocos2d.actions.instant.CCCallFuncN;
import org.cocos2d.actions.interval.CCAnimate;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.layers.CCLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.menus.CCMenu;
import org.cocos2d.menus.CCMenuItem;
import org.cocos2d.menus.CCMenuItemImage;
import org.cocos2d.menus.CCMenuItemSprite;
import org.cocos2d.nodes.CCAnimation;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCLabel;
import org.cocos2d.nodes.CCNode;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.nodes.CCSpriteFrameCache;
import org.cocos2d.nodes.CCSpriteSheet;
import org.cocos2d.nodes.CCTextureCache;
import org.cocos2d.sound.SoundEngine;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.ccColor3B;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.aga.mine.pages2.UserData;
import com.aga.mine.util.Popup;
import com.aga.mine.util.UtilResource;

// 전체적인 변수명 정리와 리팩토링이 필요함. (급하니 코드가 더 더러워짐.)
public class ShopItem2 extends CCLayer {

	private String tag = "ShopItem2";
	private final String commonfolder = "00common/";
	private final String folder = "22item/";
	private final String fileExtension = ".png";
	
	private CCSprite bg;
	private CCSprite bb;
	
	private CCSprite itemOver = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + folder + "item-buttoncover" + fileExtension));
	private Context mContext;
	private UserData userData;
	
	private int attackAttribute = 1;
	private int defenseAttribute = 1;
	private int price ;
	
	private CCLabel  sphereNumber1;
	private CCLabel  sphereNumber2;
	private CCLabel  offenseAttributeSelected1;
	private CCLabel  offenseAttributeSelected2;
	private CCLabel  defenseAttributeSelected1;
	private CCLabel  defenseAttributeSelected2;

	private CCLabel  levelFire;
	private CCLabel  levelWind;
	private CCLabel  levelCloud;
	private CCLabel  levelDivine;
	private CCLabel  levelEarth;
	private CCLabel  levelMirror;
	private CCLabel gold;
	
	private CCAnimate mItemLevelUp;
	private CCSpriteFrameCache cache;
	
	static CCScene scene() {
		CCScene scene = CCScene.node();
		CCLayer layer = new ShopItem2();
		scene.addChild(layer);
		return scene;
	}

	public ShopItem2() {
		mContext = CCDirector.sharedDirector().getActivity();
		userData = UserData.share(mContext);
		
		
		cache = CCSpriteFrameCache.sharedSpriteFrameCache();
		cache.addSpriteFrames("60game/fxMushroomItem.plist");
		CCSpriteSheet.spriteSheet("60game/fxMushroomItem.png"); 
		
		//호박폭발 애니메이션
		CCAnimation itemLevelUp = CCAnimation.animation("itemLevelUp");
		for( int i=1;i<=7;i++) {
			itemLevelUp.addFrame(cache.getSpriteFrame((String.format("item%02d.png", i))));
		}
		mItemLevelUp = CCAnimate.action(0.21f, itemLevelUp, false);
		
		
		bg = BackGround.setBackground(this, CGPoint.make(0.5f, 0.5f), commonfolder + "bg1" + fileExtension);
		setBackBoardMenu(folder + "item-backboard" + fileExtension);
		setBoardFrameMenu(commonfolder + "frameGeneral-hd" + fileExtension);
		TopMenu2.setSceneMenu(this);
		BottomImage.setBottomImage(this);
		
		itemOver.setAnchorPoint(0,0);
		this.setIsTouchEnabled(true);
	}

	private void setBackBoardMenu(String imageFullPath) {
		bb = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + imageFullPath));
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
		gold = FrameTitle6.setTitle(boardFrame, folder);
	}
	
	
	private void setMainMenu(CCSprite parentSprite){
			
			setText(parentSprite);
			
		CCMenuItem itemSphere = CCMenuItemImage.item(
				CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + folder + "item-buttonBuy-hd" + fileExtension)),
				CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + folder + "item-buttonBuy-select-hd" + fileExtension)),
				this, "buySphereCallback");
		itemSphere.setUserData("Sphere");
		
		CCMenuItem itemOffenseBuy = CCMenuItemImage.item(
				CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + folder + "item-buttonBuy-hd" + fileExtension)),
				CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + folder + "item-buttonBuy-select-hd" + fileExtension)),
				this, "buyMagicCallback");
		itemOffenseBuy.setTag(offense);
		
		CCMenuItem itemDefenseBuy = CCMenuItemImage.item(
				CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + folder + "item-buttonBuy-hd" + fileExtension)),
				CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + folder + "item-buttonBuy-select-hd" + fileExtension)),
				this, "buyMagicCallback");
		itemDefenseBuy.setTag(defense);
		
		CCMenuItemSprite o_fire = CCMenuItemImage.item(
				CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + folder + "item-buttonFire-hd" + fileExtension)),
				CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + folder + "item-buttonFire-select-hd" + fileExtension)),
				this, "selectMagicCallback");		
		o_fire.setTag(0);
		o_fire.addChild(itemOver);
		o_fire.setIsEnabled(false);
		
		CCMenuItemSprite o_wind = CCMenuItemImage.item(
				CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + folder + "item-buttonWind-hd" + fileExtension)),
				CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + folder + "item-buttonWind-select-hd" + fileExtension)),
				this, "selectMagicCallback");
		o_wind.setTag(1);
		
		CCMenuItemSprite o_cloud = CCMenuItemImage.item(
				CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + folder + "item-buttonCloud-hd" + fileExtension)),
				CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + folder + "item-buttonCloud-select-hd" + fileExtension)),
				this, "selectMagicCallback");
		o_cloud.setTag(2);
		
		CCMenuItemSprite d_fire = CCMenuItemImage.item(
				CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + folder + "item-buttonFire-hd" + fileExtension)),
				CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + folder + "item-buttonFire-select-hd" + fileExtension)),
				this, "selectMagicCallback");		
		d_fire.setTag(3);
		d_fire.addChild(itemOver);
		d_fire.setIsEnabled(false);
		
		CCMenuItemSprite d_wind = CCMenuItemImage.item(
				CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + folder + "item-buttonWind-hd" + fileExtension)),
				CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + folder + "item-buttonWind-select-hd" + fileExtension)),
				this, "selectMagicCallback");
		d_wind.setTag(4);
		
		CCMenuItemSprite d_cloud = CCMenuItemImage.item(
				CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + folder + "item-buttonCloud-hd" + fileExtension)),
				CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + folder + "item-buttonCloud-select-hd" + fileExtension)),
				this, "selectMagicCallback");
		d_cloud.setTag(5);
		
		CCMenu spherebuyMenu = CCMenu.menu(itemSphere);
		CCMenu offensebuyMenu = CCMenu.menu(itemOffenseBuy);
		CCMenu defensebuyMenu = CCMenu.menu(itemDefenseBuy);
		CCMenu offenseMenu = CCMenu.menu(o_fire, o_wind, o_cloud);
		CCMenu defenseMenu = CCMenu.menu(d_fire, d_wind, d_cloud);
		parentSprite.addChild(spherebuyMenu);
		parentSprite.addChild(offensebuyMenu);
		parentSprite.addChild(defensebuyMenu);
		parentSprite.addChild(offenseMenu);
		parentSprite.addChild(defenseMenu);

		offenseMenu.alignItemsHorizontally(10);
		defenseMenu.alignItemsHorizontally(10);
		
		spherebuyMenu.setPosition(
				465 - 44, parentSprite.getContentSize().height - 109); // 수정 구매
		
		offensebuyMenu.setPosition(
				465 - 44, parentSprite.getContentSize().height - 208); // 공격아이템 구매
		
		defensebuyMenu.setPosition(
				465 - 44, parentSprite.getContentSize().height - 404); // 방어아이템 구매
		
		offenseMenu.setAnchorPoint(0, 0);
		offenseMenu.setPosition(
				65 + o_fire.getContentSize().width * 1.5f,
				587 - 343 + o_fire.getContentSize().height / 2); // 공격 속성 메뉴
		
		defenseMenu.setAnchorPoint(0, 0);
		defenseMenu.setPosition(
				65 + o_fire.getContentSize().width * 1.5f,
				587 - 538 + o_fire.getContentSize().height / 2); // 방어 속성 메뉴
		
		CCSprite buyText = CCSprite.sprite(
				Utility.getInstance().getNameWithIsoCodeSuffix(folder + "item-textBuy" + fileExtension));
		buyText.setPosition(itemSphere.getContentSize().width/2, itemSphere.getContentSize().height / 2);
//		buyText.setAnchorPoint(0.5f, 0.5f);
		itemSphere.addChild(buyText);
		itemOffenseBuy.addChild(buyText);
		itemDefenseBuy.addChild(buyText);
		
		levelFire = CCLabel.makeLabel("Lv" + FacebookData.getinstance().getDBData("LevelFire"), "Arial", 20);
		levelFire.setColor(ccColor3B.ccYELLOW);
//		levelFire.setColor(ccColor3B.ccBLACK); // 스트록이 없어서 잘 안보임. 임시용
		levelFire.setPosition(CGPoint.make(o_fire.getContentSize().width - 12, o_fire.getContentSize().height - 7));
		levelFire.setAnchorPoint(1, 1);
		o_fire.addChild(levelFire);
		
		levelWind = CCLabel.makeLabel("Lv"+FacebookData.getinstance().getDBData("LevelWind"), "Arial", 20);
		levelWind.setColor(ccColor3B.ccYELLOW);
//		levelWind.setColor(ccColor3B.ccBLACK); // 스트록이 없어서 잘 안보임. 임시용
		levelWind.setPosition(CGPoint.make(o_fire.getContentSize().width - 12, o_fire.getContentSize().height - 7));
		levelWind.setAnchorPoint(1, 1);
		o_wind.addChild(levelWind);				
		
		levelCloud = CCLabel.makeLabel("Lv"+FacebookData.getinstance().getDBData("LevelCloud"), "Arial", 20);
		levelCloud.setColor(ccColor3B.ccYELLOW);
//		levelCloud.setColor(ccColor3B.ccBLACK); // 스트록이 없어서 잘 안보임. 임시용
		levelCloud.setPosition(CGPoint.make(o_fire.getContentSize().width - 12, o_fire.getContentSize().height - 7));
		levelCloud.setAnchorPoint(1, 1);
		o_cloud.addChild(levelCloud);				
		
		levelDivine = CCLabel.makeLabel("Lv"+FacebookData.getinstance().getDBData("LevelDivine"), "Arial", 20);
		levelDivine.setColor(ccColor3B.ccYELLOW);
//		levelDivine.setColor(ccColor3B.ccBLACK); // 스트록이 없어서 잘 안보임. 임시용
		levelDivine.setPosition(CGPoint.make(o_fire.getContentSize().width - 12, o_fire.getContentSize().height - 7));
		levelDivine.setAnchorPoint(1, 1);
		d_fire.addChild(levelDivine);						
		
		levelEarth = CCLabel.makeLabel("Lv"+FacebookData.getinstance().getDBData("LevelEarth"), "Arial", 20);
		levelEarth.setColor(ccColor3B.ccYELLOW);
//		levelEarth.setColor(ccColor3B.ccBLACK); // 스트록이 없어서 잘 안보임. 임시용
		levelEarth.setPosition(CGPoint.make(o_fire.getContentSize().width - 12, o_fire.getContentSize().height - 7));
		levelEarth.setAnchorPoint(1, 1);
		d_wind.addChild(levelEarth);					
		
		levelMirror = CCLabel.makeLabel("Lv"+FacebookData.getinstance().getDBData("LevelMirror"), "Arial", 20);
		levelMirror.setColor(ccColor3B.ccYELLOW);
//		levelMirror.setColor(ccColor3B.ccBLACK); // 스트록이 없어서 잘 안보임. 임시용
		levelMirror.setPosition(CGPoint.make(o_fire.getContentSize().width - 12, o_fire.getContentSize().height - 7));
		levelMirror.setAnchorPoint(1, 1);
		d_cloud.addChild(levelMirror);						
	}
		
	
//	//배경 그림 설정 (frameMatching)
	private void setText(CCSprite parentSprite){
		
		CCLabel gold1 = CCLabel.makeLabel("Gold", "Arial", 25);
		gold1.setColor(ccColor3B.ccYELLOW);
		gold1.setAnchorPoint(0, 0.5f);
		gold1.setPosition(190, parentSprite.getContentSize().height - 90);
		
		int sphereNumber = Integer.parseInt(FacebookData.getinstance().getDBData("SphereNumber"));
		if (sphereNumber < 3) {
			FacebookData.getinstance().modDBData("SphereNumber", "3");
			sphereNumber = 3;
		}
		if (sphereNumber < 9) {
			int spherePrice = userData.buySphere[sphereNumber - 3];	
			sphereNumber1 = CCLabel.makeLabel(
					new NumberComma().numberComma(spherePrice), "Arial", 30);
		} else {
			sphereNumber1 = CCLabel.makeLabel("MAX", "Arial", 30);
			sphereNumber1.setColor(ccColor3B.ccYELLOW);
		}
		sphereNumber1.setAnchorPoint(1, 1);
		sphereNumber1.setPosition(464 - 75 - 20, parentSprite.getContentSize().height - 63 - 8);
		
		String bottle  = "My Elemental bottle : ";
		float fontSize = 22;
		if (Locale.getDefault().getLanguage().toString().equals("ko")) {
			bottle  = "보유 정령병 : ";
			fontSize = 25; 
		}
		
		sphereNumber2 = CCLabel.makeLabel(
				bottle + new NumberComma().numberComma(sphereNumber), "Arial", fontSize);
		if (!Locale.getDefault().getLanguage().toString().equals("ko"))
			sphereNumber2.setScaleX(0.9f);
		sphereNumber2.setPosition(464 - 75 - 20, parentSprite.getContentSize().height - 152 + 8);
		sphereNumber2.setAnchorPoint(1, 0);
		
		parentSprite.addChild(gold1);	
		parentSprite.addChild(sphereNumber1);	
		parentSprite.addChild(sphereNumber2);	

		
		/********************* 공격 *************************/
		CCSprite attackItem = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + 
				Utility.getInstance().getNameWithIsoCodeSuffix(folder + "item-labelAttack" + fileExtension)));
		attackItem.setPosition(110, parentSprite.getContentSize().height - 190);
		parentSprite.addChild(attackItem);		
		
		CCLabel gold2 = CCLabel.makeLabel("Gold", "Arial", 25);
		gold2.setColor(ccColor3B.ccYELLOW);
		gold2.setAnchorPoint(0, 0.5f);
		gold2.setPosition(190, parentSprite.getContentSize().height - 190);
		
		int levelFire = Integer.parseInt(FacebookData.getinstance().getDBData("LevelFire"));
		if (levelFire < 1) {
			FacebookData.getinstance().modDBData("LevelFire", "1");
			levelFire = 1;
		}
		if (levelFire < 20) {
			int firePrice = userData.buyOffenseFire[levelFire];	
			userData.offenceMagic = 0;
			offenseAttributeSelected1 = CCLabel.makeLabel(
					new NumberComma().numberComma(firePrice), "Arial", 30);
		} else {
			offenseAttributeSelected1 = CCLabel.makeLabel("MAX", "Arial", 30);
			offenseAttributeSelected1.setColor(ccColor3B.ccYELLOW);
		}

		
//		offenseAttributeSelected1 = CCLabel.makeLabel(" " + firePrice, "Arial", 30);
		offenseAttributeSelected1.setUserData("LevelFire");
		offenseAttributeSelected1.setAnchorPoint(1, 1);
		offenseAttributeSelected1.setPosition(464 - 75 - 20, parentSprite.getContentSize().height - 163 - 8);
//		offenseAttributeSelected1.setColor(ccColor3B.ccYELLOW);
		
		String attack  = "1 sec increment of attacks";
		if (Locale.getDefault().getLanguage().toString().equals("ko"))
			attack  = "공격 시간 1초 증가";
		
		offenseAttributeSelected2 = CCLabel.makeLabel(attack, "Arial", 23);
		offenseAttributeSelected2.setUserData("LevelFire");
		offenseAttributeSelected2.setAnchorPoint(1, 0);
		offenseAttributeSelected2.setPosition(464 - 75 - 20, parentSprite.getContentSize().height - 248 + 8);
//		offenseAttributeSelected2.setColor(ccColor3B.ccYELLOW);
		
		parentSprite.addChild(gold2);				
		parentSprite.addChild(offenseAttributeSelected1);				
		parentSprite.addChild(offenseAttributeSelected2);				
	
		/********************* 방어 *************************/
		
		CCSprite defenseItem = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + 
				Utility.getInstance().getNameWithIsoCodeSuffix(folder + "item-labelDefense" + fileExtension)));
		defenseItem.setPosition(110, parentSprite.getContentSize().height - 385);
		parentSprite.addChild(defenseItem);
		
		
		CCLabel gold3 = CCLabel.makeLabel("Gold", "Arial", 25);
		gold3.setColor(ccColor3B.ccYELLOW);
		gold3.setAnchorPoint(0, 0.5f);
		gold3.setPosition(190, parentSprite.getContentSize().height - 385);
		

		int LevelDivine = Integer.parseInt(FacebookData.getinstance().getDBData("LevelDivine"));
		if (LevelDivine < 1) {
			FacebookData.getinstance().modDBData("LevelDivine", "1");
			LevelDivine = 1;
		}
		if (LevelDivine < 20) {
			int divinePrice = userData.buyOffenseFire[LevelDivine];	
			userData.defenceMagic = 3;
			defenseAttributeSelected1 = CCLabel.makeLabel(
					new NumberComma().numberComma(divinePrice), "Arial", 30);
		} else {
			defenseAttributeSelected1 = CCLabel.makeLabel("MAX", "Arial", 30);
			defenseAttributeSelected1.setColor(ccColor3B.ccYELLOW);
		}
		
		
		defenseAttributeSelected1.setUserData("LevelDivine");
		defenseAttributeSelected1.setAnchorPoint(1, 1);
		defenseAttributeSelected1.setPosition(464 - 75 - 20, parentSprite.getContentSize().height - 358 - 8);
//		defenseAttributeSelected1.setColor(ccColor3B.ccYELLOW);
		
		String defenses  = "1 sec increment of defenses";
		if (Locale.getDefault().getLanguage().toString().equals("ko"))
			defenses  = "방어 시간 1초 증가";
		
		defenseAttributeSelected2 = CCLabel.makeLabel(defenses, "Arial", 23);
		defenseAttributeSelected2.setUserData("LevelDivine");
		defenseAttributeSelected2.setAnchorPoint(1, 0);
		defenseAttributeSelected2.setPosition(464 - 75 - 20, parentSprite.getContentSize().height - 444 + 8);
//		defenseAttributeSelected2.setColor(ccColor3B.ccYELLOW);
		
		parentSprite.addChild(gold3);
		parentSprite.addChild(defenseAttributeSelected1);
		parentSprite.addChild(defenseAttributeSelected2);
}	

	// config 파일에 나중에 옮길것
	public static boolean buttonActive = true;
	final int previous = 501;
	final int home= 502;
	
	// sceneCallback들 전부 여기로 옮기기
	public void clicked(Object sender) {
		CCScene scene = null;
		int value = ((CCNode) sender).getTag();
		if (buttonActive) {
			switch (value) {
			case previous:
				MainApplication.getInstance().getActivity().click();
				scene = Shop.scene();
				CCDirector.sharedDirector().replaceScene(scene);
				break;

			case home:
				MainApplication.getInstance().getActivity().click();
				scene = Home.scene();
				CCDirector.sharedDirector().replaceScene(scene);
				break;
				
			case Constant.PURCHASING_OK:
				isPurchase = true;
				makeAPurchase();
				this.removeChildByTag(Constant.POPUP_LAYER, true);
				break;
				
			case Constant.PURCHASING_CANCEL:
				MainApplication.getInstance().getActivity().click();
				isPurchase = false;
				CCDirector.sharedDirector().getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(mContext, "구매 취소", Toast.LENGTH_SHORT).show();
					}
				});
				this.removeChildByTag(Constant.POPUP_LAYER, true);
				break;
			}

		}
	}

	private boolean  isPurchase = false;
	private long quantity;
//	private long price;

	private void makeAPurchase() {
		Log.e("ShopItem2", "isPurchase : " + isPurchase);
		if (isPurchase) {
			FacebookData.getinstance().modDBData(basket);
			gold.setString(new NumberComma().numberComma(FacebookData.getinstance().getDBData("Gold")));
			
			Collection k = basket .keySet();
			Iterator itr = k.iterator();
			boolean isSphere = false;
				while(itr.hasNext()){
					if (((String) itr.next()).equals("SphereNumber")) {
						isSphere = true;
						break;
					}
			}
			if (isSphere) {
				refreshSphere();
			} else {
				if (buttonTag == offense) {
					refreshMagic(offenseTag);
					actionWizard(offenseTag);
				} else {
					refreshMagic(defenseTag);
					actionWizard(defenseTag);
				}
			}
				
//				 for (int i = 0; i < itemType.length; i++) {
					 //  맞는것만 돌리기
//					 if (itemType[i].equals(object)) {
//							refreshMagic(i);
//					}
//				}
			
			basket.clear();
			SoundEngine.sharedEngine().playEffect(mContext, R.raw.buy);
			CCDirector.sharedDirector().getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(mContext, "구매 완료", Toast.LENGTH_SHORT).show();
				}
			});
			
		} else {
			MainApplication.getInstance().getActivity().click();
			Log.e("ShopItem2", "isPurchase : " + isPurchase);
		}

	}
	
	private void refreshSphere() {
		int sphereNumber = Integer.parseInt(FacebookData.getinstance().getDBData("SphereNumber"));
		
		if (sphereNumber < 9) {
			int spherePrice = userData.buySphere[sphereNumber - 3];	
			sphereNumber1.setString(new NumberComma().numberComma(spherePrice));
		} else {
			sphereNumber1.setString("MAX");
			sphereNumber1.setColor(ccColor3B.ccYELLOW);
		}
		
		if (Locale.getDefault().getLanguage().toString().equals("ko")) { // 나중에 정리해서 공용으로 넣기
			sphereNumber2.setString("보유 정령병 :  " + new NumberComma().numberComma(sphereNumber));
		} else {
			sphereNumber2.setString("My Elemental bottle : " + new NumberComma().numberComma(sphereNumber));
		}
		
		
	}
	
	
	final int offense = 0; 
	final int defense = 1; 
	Map<String, String> basket = new HashMap<String, String>();
	
	
	public void buySphereCallback(Object sender) {
		MainApplication.getInstance().getActivity().click();
		int sphereQuantity = Integer.parseInt(FacebookData.getinstance().getDBData("SphereNumber"));
		
		// 보유 정령병 MAX (종료) 
		if (sphereQuantity >= 9) {
			CCDirector.sharedDirector().getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(mContext, "더 이상 구매가 불가능 합니다.", Toast.LENGTH_SHORT).show();
				}
			});
			return;
		}
		
		int spherePrice = userData.buySphere[sphereQuantity - 3];
		long gold = Integer.parseInt(FacebookData.getinstance().getDBData("Gold"));
		
		// 골드 부족 (종료) 
		if (gold < spherePrice) {
			CCDirector.sharedDirector().getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(mContext, "골드가 부족합니다.", Toast.LENGTH_SHORT).show();
				}
			});
			return;
		}
		
		Popup.popupOfPurchase(this);
		basket.put("SphereNumber", String.valueOf(sphereQuantity + 1));
		basket.put("Gold", String.valueOf(gold - spherePrice));				
	}
	
	int buttonTag; 
	// 구매 버튼(공격or방어 마법)
	public void buyMagicCallback(Object sender) {
		MainApplication.getInstance().getActivity().click();
		buttonTag = ((CCNode)sender).getTag();
		int _tag;
		
		if (buttonTag == offense) {
			_tag = offenseTag;
		} else {
			_tag = defenseTag;
		}
		
		String magicType = itemType[_tag];
		
		int level = Integer.parseInt(FacebookData.getinstance().getDBData(magicType));

		// 지정 마법 MAX (종료) 
		if (level >= 20) {
			CCDirector.sharedDirector().getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(mContext, "더 이상 구매가 불가능 합니다.", Toast.LENGTH_SHORT).show();
				}
			});
			return;
		}
		
		int price = userData.magicPrice[_tag][level];
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
		basket.put(magicType, String.valueOf(level + 1));
		basket.put("Gold", String.valueOf(gold - price));			
	}


	public void selectMagicCallback(Object sender) {
		MainApplication.getInstance().getActivity().click();
		int buttonTag = ((CCNode) sender).getTag();
		if (buttonTag < 3) {
			offenseTag = buttonTag;
		} else {
			defenseTag  = buttonTag;
		}
//		Log.e("ShopItem2", "selectMagicCallback _ tag : " + temp);
		
		List<CCNode> a = ((CCNode) sender).getParent().getChildren();
		CCMenuItemSprite button = (CCMenuItemSprite)sender;
		
		// 부모에 속한 자식들 모두 선택되지 않은상태로 변경
		for (CCNode ccNode : a) {
			final CCMenuItemSprite ccButton = (CCMenuItemSprite)ccNode;
			ccButton.setIsEnabled(true);
			ccButton.removeChild(itemOver, true);
		}
		
		// 현재 선택된 버튼만 선택된상태로 변경
		button.addChild(itemOver, 777);
		itemOver.setAnchorPoint(0,0);
		button.setIsEnabled(false);
		Log.e("ShopItem2", "button _ tag : " + button.getTag());
		refreshMagic(buttonTag);
//		actionWizard((CCNode) sender);
	}
	
//	final int  levelFire = 0;
//	final int  levelWind = 1;
//	final int  levelCloud = 2;
//	final int  levelDivine = 3;
//	final int  levelEarth = 4;
//	final int  levelMirror = 5;
	
	int offenseTag = 0;
	int defenseTag = 3;
	String[] itemType = { "LevelFire", "LevelWind", "LevelCloud", "LevelDivine", "LevelEarth", "LevelMirror" };
	String[][] magicText2 = { {"공격 시간 ","초 증가"}, {"피해 시간 ","초 감소"}};
	
	private void refreshMagic(int type) {
		Log.e("ShopItem2", "setElemental _ tag : " + type);
		String price = "MAX";
		String magicType = itemType[type];
		
		int level = Integer.parseInt(FacebookData.getinstance().getDBData(magicType));
		
		if (level < 20)
			price = "" + userData.magicPrice[type][level];
		
		CCLabel level_Label = null;
		
		switch (type) {
		case 0:
			level_Label = levelFire;
			break;
		case 1:
			level_Label = levelWind;
			break;
		case 2:
			level_Label = levelCloud;
			break;
		case 3:
			level_Label = levelDivine;
			break;
		case 4:
			level_Label = levelEarth;
			break;
		case 5:
			level_Label = levelMirror;
			break;
		}
		
		level_Label.setString("Lv" + FacebookData.getinstance().getDBData(itemType[type]));
		
		if (type < 3) {
			offenseAttributeSelected1.setString(
					new NumberComma().numberComma(price));				
//			offenseAttributeSelected2.setString(magicText2[0][0] + level + magicText2[0][1]);
			if (Locale.getDefault().getLanguage().toString().equals("ko")) { // 나중에 정리해서 공용으로 넣기
				offenseAttributeSelected2.setString("공격 시간 1초 증가");
			} else {
				offenseAttributeSelected2.setString("1 sec increment of attacks");
			}
			userData.offenceMagic =  type;
			
			if (price.equals("MAX"))
				offenseAttributeSelected1.setColor(ccColor3B.ccYELLOW);
			else
				offenseAttributeSelected1.setColor(ccColor3B.ccWHITE);
			
		} else {
			defenseAttributeSelected1.setString(
					new NumberComma().numberComma(price));
			if (Locale.getDefault().getLanguage().toString().equals("ko")) { // 나중에 정리해서 공용으로 넣기
				defenseAttributeSelected2.setString("방어 시간 1초 증가");
			} else {
				defenseAttributeSelected2.setString("1 sec increment of defenses");
			}
			userData.defenceMagic =  type;
			
			if (price.equals("MAX"))
				defenseAttributeSelected1.setColor(ccColor3B.ccYELLOW);
			else
				defenseAttributeSelected1.setColor(ccColor3B.ccWHITE);
			
		}
		
	}
	
	public void actionWizard(int labelTag) {
		CCMenuItem button = null;
		switch (labelTag) {
		case 0:
			button = (CCMenuItemSprite) levelFire.getParent();
			break;
		case 1:
			button = (CCMenuItemSprite) levelWind.getParent();
			break;
		case 2:
			button = (CCMenuItemSprite) levelCloud.getParent();
			break;
		case 3:
			button = (CCMenuItemSprite) levelDivine.getParent();
			break;
		case 4:
			button = (CCMenuItemSprite) levelEarth.getParent();
			break;
		case 5:
			button = (CCMenuItemSprite) levelMirror.getParent();
			break;
		}
		Log.e(tag, "button : " + button);
		actionBuy(button);
	}
	
	public void actionBuy(CCNode parent) {
		Log.e(tag, "parent : " + parent);
		Log.e(tag, "parent.getAnchorPoint() : " + parent.getAnchorPoint());
		CCSprite glittering = CCSprite.sprite(cache.getSpriteFrame("item01.png"));
//		CCSprite bomb = CCSprite.sprite("60game/pumpkinbomb_01.png");
//		bomb.setPosition(0,0);
		glittering.setPosition(parent.getContentSize().width/2,parent.getContentSize().height/2);
		glittering.setScale(2);
		parent.addChild(glittering, 100);
		
		CCCallFuncN _removeAction = CCCallFuncN.action(this, "removeAction");
		glittering.runAction(CCSequence.actions(mItemLevelUp, _removeAction));
	}
	
	public void removeAction(Object sender) {
		((CCSprite)sender).removeFromParentAndCleanup(true);
	}
	

}