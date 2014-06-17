package com.aga.mine.pumpkin;

import java.util.Locale;

import org.cocos2d.layers.CCLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCLabel;
import org.cocos2d.nodes.CCNode;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.nodes.CCTextureCache;
import org.cocos2d.opengl.CCTexture2D;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.ccColor3B;

import com.aga.mine.util.UtilResource;

import android.content.Context;
import android.util.Log;

//import com.aga.mine.pages.UserData;

public class Home2 extends CCLayer {
	
	final String commonfolder = "00common/";
	final String folder = "10home/";
	final String fileExtension = ".png";
	
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
	
	TopMenu1 topMenu1 = new TopMenu1();
	CCLabel[] broomstickText;
	int currentTime = 0;
	
	private Context mContext;
//	UserData userData;
	
	public static CCScene scene() {
		CCScene scene = CCScene.node();
		CCLayer layer = new Home2();
        scene.addChild(layer, 2, 2);
        return scene;
	}
		
	public Home2() {
		mContext = CCDirector.sharedDirector().getActivity();
//		userData = UserData.share(mContext);
		
		//배경 그림 설정
		CCSprite bg = BackGround.setBackground(this, CGPoint.make(0.5f, 0.5f), commonfolder + "bg1" + fileExtension);

		setBackBoardMenu(bg, folder + "guestbg" + fileExtension);
		setBoardFrameMenu(bg, commonfolder + "frameGeneral-hd" + fileExtension);

		this.setIsTouchEnabled(true);
	}

	
	// 백 보드 설정
	private void setBackBoardMenu(CCSprite parentSprite, String imageFullPath) {
		CCTexture2D texture = CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + imageFullPath);
		CCSprite bb = CCSprite.sprite(texture);
		parentSprite.addChild(bb);
		bb.setPosition(parentSprite.getContentSize().width / 2, parentSprite.getContentSize().height * 0.525f);
		bb.setAnchorPoint(0.5f, 0.5f);
		

		String[] kor = {		
				"게스트 로그인은 아이템 구매, 레벨업,",
				"포인트, 골드 저장이 되지 않습니다.",
				"친구들과 게임을 하며 레벨업과 내 순",
				"위를 올리시려면 페이스북 로그인을",
				"하세요."
			};
		
		String[] eng = {		
				"Guest login is the item purchased,",
				"level up, points, gold does not save.",
				"Games with friends, and to raise the",
				"level of Rank-up and login to your",
				"Facebook."
			};
		
		if (Locale.getDefault().getLanguage().toString().equals("ko"))
			guestLogin(bb, kor, 43);
		else
			guestLogin(bb, eng, 40);
		
	}
	
	private void guestLogin(CCSprite parentSprite, String[] guestHelpString, int position) {
		float fontsize = 26;
		CCSprite help1 = CCLabel.makeLabel(guestHelpString[0], "Arial", fontsize);
		parentSprite.addChild(help1);
		help1.setColor(ccColor3B.ccc3(187, 187, 187));
		help1.setAnchorPoint(0, 1);
		help1.setPosition(position, parentSprite.getContentSize().height / 2 + 50);		
		
		CCSprite help2 = CCLabel.makeLabel(guestHelpString[1], "Arial", fontsize);
		parentSprite.addChild(help2);
		help2.setColor(ccColor3B.ccc3(187, 187, 187));
		help2.setAnchorPoint(0, 1);
		help2.setPosition(help1.getPosition().x, help1.getPosition().y - (help2.getContentSize().height * 1.1f));		
		
		CCSprite help3 = CCLabel.makeLabel(guestHelpString[2], "Arial", fontsize);
		parentSprite.addChild(help3);
		help3.setColor(ccColor3B.ccc3(187, 187, 187));
		help3.setAnchorPoint(0, 1);
		help3.setPosition(help2.getPosition().x, help2.getPosition().y - (help3.getContentSize().height * 1.8f));		
		
		CCSprite help4 = CCLabel.makeLabel(guestHelpString[3], "Arial", fontsize);
		parentSprite.addChild(help4);
		help4.setColor(ccColor3B.ccc3(187, 187, 187));
		help4.setAnchorPoint(0, 1);
		help4.setPosition(help3.getPosition().x, help3.getPosition().y - (help4.getContentSize().height * 1.1f));		
		
		CCSprite help5 = CCLabel.makeLabel(guestHelpString[4], "Arial", fontsize);
		parentSprite.addChild(help5);
		help5.setColor(ccColor3B.ccc3(187, 187, 187));
		help5.setAnchorPoint(0, 1);
		help5.setPosition(help4.getPosition().x, help4.getPosition().y - (help5.getContentSize().height * 1.1f));		
	}
	
	
	// 게시판 설정
	private void setBoardFrameMenu(CCSprite parentSprite, String imageFullPath) {
		CCTexture2D texture = CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + imageFullPath);
		CCSprite boardFrame = CCSprite.sprite(texture);
		parentSprite.addChild(boardFrame);
		boardFrame.setPosition(parentSprite.getContentSize().width / 2, parentSprite.getContentSize().height * 0.525f);
		boardFrame.setAnchorPoint(0.5f, 0.5f);
		
		// 게시판명
		FrameTitle2.setTitle(boardFrame, folder);
		
		// 상단 메뉴
		broomstickText = topMenu1.setTopMenu(boardFrame, folder, this);
		broomstickText[2].setString("0");
		
		// 하단 메뉴
		BottomMenu4.setBottomMenu(boardFrame, folder, this); // 주석
	}
	
	public void clicked2(Object sender) {
		MainApplication.getInstance().getActivity().click();
		int value = ((CCNode) sender).getTag();
		CCScene scene;
		
		switch (value) {
		case enterButton :
			Log.e("Home", "CallBack3 : enter" + value);
			scene = GameMode.scene();
			break;
		case optionButton :
			Log.e("Home", "CallBack3 : option" + value);
			scene = Option.scene();
			break;
		case inviteButton :
			Log.e("Home", "CallBack3 : invite" + value);
			scene = Invite.scene();
			break;
		default:
			Log.e("Home", "CallBack3 : Home" + value);
			scene = Home2.scene();
			break;
		}
		CCDirector.sharedDirector().replaceScene(scene);
	}
	
//	public void enterCallback(Object sender) {
////		CCScene scene = GameMode2.scene();
////		CCDirector.sharedDirector().replaceScene(scene);
//	}
//
//	public void optionCallback(Object sender) {
////		Log.e("Home2", "optionCallback");
////		CCScene scene = Option2.scene();
////		CCDirector.sharedDirector().replaceScene(scene);
//	}
	
}