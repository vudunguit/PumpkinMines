package com.aga.mine.pumpkin;

import java.util.List;
import java.util.Locale;

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
import org.cocos2d.opengl.CCTexture2D;
import org.cocos2d.types.CGPoint;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.aga.mine.pages2.UserData;
import com.aga.mine.pages2.GameData;
import com.aga.mine.util.UtilResource;
//
public class Option extends CCLayer {
	String tag = "Option";
	final String commonfolder = "00common/";
	final String folder = "40option/";
	final String fileExtension = ".png";

	boolean logCheck = false;
	
	CCSprite bg;
	CCSprite bb;
	CCSprite boardFrame;
	
	CCSprite itemOver = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + folder + "option-check" + fileExtension));

	private Context mContext;
	UserData userData ;
	
	public static CCScene scene() {
		CCScene scene = CCScene.node();
		CCLayer layer = new Option();
		scene.addChild(layer);
		return scene;
	}

	public Option() {
		mContext = CCDirector.sharedDirector().getActivity();
		userData = UserData.share(mContext);
		
		//배경 그림 설정
		bg = BackGround.setBackground(this, CGPoint.make(0.5f, 0.5f), commonfolder + "bg1" + fileExtension);
		
		setBackBoardMenu(
				Utility.getInstance().getNameWithIsoCodeSuffix(folder + "option-bb" + fileExtension));
		setBoardFrameMenu(commonfolder + "frameGeneral-hd" + fileExtension);
		
		// 상단 메뉴
		TopMenu2.setSceneMenu(this);
		// 하단 이미지
		BottomImage.setBottomImage(this);

		itemOver.setAnchorPoint(0,0);
		
		this.setIsTouchEnabled(true);
	}
	
	// 백 보드 설정
	private void setBackBoardMenu(String imageFullPath) {
		CCTexture2D texture = CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + imageFullPath);
		CCSprite bb = CCSprite.sprite(texture);
		bg.addChild(bb);
		bb.setPosition(bg.getContentSize().width / 2, bg.getContentSize().height * 0.5f);
		bb.setAnchorPoint(0.5f, 0.5f);
		radioButton(bb);
		setMainMenu(bb);
	}

	// 게시판 설정
	private void setBoardFrameMenu(String imageFullPath) {
		CCTexture2D texture = CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + imageFullPath);
		CCSprite boardFrame = CCSprite.sprite(texture);
		bg.addChild(boardFrame);
		boardFrame.setPosition(bg.getContentSize().width / 2, bg.getContentSize().height * 0.525f);
		boardFrame.setAnchorPoint(0.5f, 0.5f);
		FrameTitle2.setTitle(boardFrame, folder);
	}

	// 메인 메뉴
	private void setMainMenu(CCSprite parentSprite){
		MainActivity mMainActivity = MainApplication.getInstance().getActivity();
		
		CCMenuItem guide = CCMenuItemImage.item(
				SpriteSummery.imageSummary(
						folder + "guide-bg" + fileExtension,
						Utility.getInstance().getNameWithIsoCodeSuffix(
								folder + "guide" + fileExtension)),
				SpriteSummery.imageSummary(
					SpriteSummery.imageSummary(
							folder + "guide-bg" + fileExtension,
							Utility.getInstance().getNameWithIsoCodeSuffix(
							folder + "guide" + fileExtension)),
						folder + "option-buttonSelect" + fileExtension),
				this, "guideCallback");
		
		CCMenuItem facebook = CCMenuItemImage.item(
				SpriteSummery.imageSummary(
						folder + "facebook-bg" + fileExtension,
						Utility.getInstance().getNameWithIsoCodeSuffix(
						folder + 
						(mMainActivity.mSimpleFacebook.isLogin() ? "facebook_out" : "facebook_in") +
						fileExtension)),
				SpriteSummery.imageSummary(
					SpriteSummery.imageSummary(
							folder + "facebook-bg" + fileExtension,
							Utility.getInstance().getNameWithIsoCodeSuffix(
							folder + "facebook_out" + fileExtension)),
						folder + "option-buttonSelect" + fileExtension),
				this, "facebookCallback");
		
		CCMenuItem provision = CCMenuItemImage.item(
				SpriteSummery.imageSummary(
						folder + "provision-bg" + fileExtension,
						Utility.getInstance().getNameWithIsoCodeSuffix(
						folder + "provision" + fileExtension)),
				SpriteSummery.imageSummary(
					SpriteSummery.imageSummary(
							folder + "provision-bg" + fileExtension,
							Utility.getInstance().getNameWithIsoCodeSuffix(
							folder + "provision" + fileExtension)),
						folder + "option-buttonSelect" + fileExtension),
				this, "policyCallback");
		
		CCMenuItem more_apps = CCMenuItemImage.item(
				SpriteSummery.imageSummary(
						folder + "moreapps-bg" + fileExtension,
						"singleblackpixel" + fileExtension),
				SpriteSummery.imageSummary(
						folder + "moreapps-bg" + fileExtension,
						folder + "option-buttonSelect" + fileExtension),
				this, "more_appsCallback");
		
		CCMenu optionMenu = CCMenu.menu(guide, facebook, provision, more_apps);
		parentSprite.addChild(optionMenu);
		optionMenu.alignItemsVertically(-4);
		optionMenu.setPosition(parentSprite.getContentSize().width / 2, guide.getContentSize().height * 2.6f);
	}
	
	int[][] radioButtonUserdatas = {{1,2},{11,12},{21,22}};
	int[][] radioButtonMenuPositions = {{229,54},{229,106},{229,155},{229,205}};
	//
	private void radioButton(CCSprite parentSprite){
		
		int padding = 52;
		Log.e("userdatas", "length : " + radioButtonUserdatas.length);
		for (int i = 0; i < radioButtonUserdatas.length; i++) {
			
		CCMenuItemSprite onButton = CCMenuItemImage.item(
				CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH +folder + "option-check1" + fileExtension)),
				CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH +folder + "option-check1" + fileExtension)),
				this, "buttonCallback");		

		Log.e("userdatas[i][0]", "userdatas[i][0] : " + radioButtonUserdatas[i][0]);
		onButton.setUserData(radioButtonUserdatas[i][0]);
		onButton.addChild(itemOver);
		onButton.setIsEnabled(false);
		
		CCMenuItemSprite offButton = CCMenuItemImage.item(
				CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + folder + "option-check1" + fileExtension)),
				CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH +folder + "option-check1" + fileExtension)),
				this, "buttonCallback");
		Log.e("userdatas[i][1]", "userdatas[i][1] : " + radioButtonUserdatas[i][1]);
		offButton.setUserData(radioButtonUserdatas[i][1]);
		
		CCMenu music = CCMenu.menu(onButton, offButton);
		
		parentSprite.addChild(music);
		
		music.alignItemsHorizontally(padding);
		
		music.setAnchorPoint(0, 0);
		
		music.setPosition(
				radioButtonMenuPositions[i][0] + (padding / 2) + (onButton.getContentSize().width) + 29, // 이미지에서 글자 위치 변경 +29 
				544 + (onButton.getContentSize().height / 2) - radioButtonMenuPositions[i][1] - 4) ; // 이미지에서 글자 위치 변경 -4
		}
	}
	
	public void buttonCallback(Object sender) {
		MainApplication.getInstance().getActivity().click();
		CCMenuItemSprite button = (CCMenuItemSprite)sender;
//		button.getUserData();
//		Log.e(tag, "button : " + button.getUserData());
		List<CCNode> a = button.getParent().getChildren();
		for (CCNode ccNode : a) {
			final CCMenuItemSprite sprite = (CCMenuItemSprite)ccNode;
			sprite.setIsEnabled(true);
			sprite.removeChild(itemOver, true);
		}
		button.addChild(itemOver, 777);

		itemOver.setAnchorPoint(0,0);
		button.setIsEnabled(false);
		setElemental(((Integer)button.getUserData()));
	}
	
	private void setElemental(int type) {
		Log.e("tag", "selectButton : " + type);
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
				case previous :
				case home :
					if (GameData.share().isGuestMode) {
						scene = Home2.scene();
					} else {
						scene = Home.scene();
					}
					break;
			}
			CCDirector.sharedDirector().replaceScene(scene);
		}
	}

	public void guideCallback(Object sender) {
		MainApplication.getInstance().getActivity().click();
		CCScene scene = GameGuide1.scene();
		CCDirector.sharedDirector().replaceScene(scene);
	}

	public void facebookCallback(Object sender) {
		MainApplication.getInstance().getActivity().click();
		MainActivity mMainActivity = MainApplication.getInstance().getActivity();
		if (mMainActivity.mSimpleFacebook.isLogin()) {
			mMainActivity.logoutFaceBook();
		} else {
			GameData.share().isGuestMode = false;
			mMainActivity.loginFaceBook();
		}
	}

	public void policyCallback(Object sender) {
		MainApplication.getInstance().getActivity().click();
		String address = "http://agatong.co.kr/policy/policy_eng.html";
		if (Locale.getDefault().getLanguage().toString().equals("ko"))
			address = "http://agatong.co.kr/policy/policy_kor.html";
		webViewer(address);
	}

	public void more_appsCallback(Object sender) {
		MainApplication.getInstance().getActivity().click();
		String address = "http://agatong.co.kr/m/index.html";
		if (Locale.getDefault().getLanguage().toString().equals("ko"))
			address = "http://www.agatong.co.kr/m/index_kor.html";
		webViewer(address);
	}
	
	private void webViewer(String address) {
		Uri uri = Uri.parse(address);
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		CCDirector.sharedDirector().getActivity().startActivity(intent);
	}
}