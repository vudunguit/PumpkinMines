package com.aga.mine.pumpkin;

import org.cocos2d.layers.CCLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.types.CGPoint;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Process;

public class Logo extends CCLayer {
	String tag = "Logo";
	final String folder = "01trademark/";
	final String fileExtension = ".png";
	
	String Android = "0";
	String Iphone = "1";
	String clientVersion = "0.8.0";
	
	public static CCScene scene() {
		CCScene scene = CCScene.node();
		CCLayer layer = new Logo();
		scene.addChild(layer);
		return scene;
	}
	
	public Logo() {		
		versionCheck(BackGround.setBackground(this, CGPoint.make(0.5f, 0.5f), folder + "trademark" + fileExtension));
	}
	
	private void versionCheck(CCSprite parentSprite) {
		final String serverVersion = DataFilter.getGameVersionData();
//		CCLabel ccWebVersion = CCLabel.makeLabel("webVersion  " + webVersion, "Arial", 20);
//		ccWebVersion.setPosition(parentSprite.getContentSize().width - 80, 140);
//		parentSprite.addChild(ccWebVersion);
//		Log.e(tag, "webVersion : " + webVersion);
//		
//		CCLabel ccVersion = CCLabel.makeLabel("version  " + version, "Arial", 20);
//		ccVersion.setPosition(parentSprite.getContentSize().width - 80, 70);
//		parentSprite.addChild(ccVersion);
//		Log.e(tag, "Version : " + version);
		

		if (serverVersion == null) { // 이미지를 찾을수 없어서 일단 다이얼로그로 띄웠음.
			CCDirector.sharedDirector().getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					   AlertDialog.Builder builder = new AlertDialog.Builder(CCDirector.sharedDirector().getActivity());
		                builder.setMessage("무선 네트워크가 꺼져있거나\n서버로부터 데이터를 받을 수 없습니다.\n\n다시 확인해 주세요.")
		                       .setCancelable(false)
		                       .setPositiveButton("OK", new DialogInterface.OnClickListener() {
		                           public void onClick(DialogInterface dialog, int id) {
		                        	   CCDirector.sharedDirector().getActivity().moveTaskToBack(true); // 본Activity finish후 다른 Activity가 뜨는 걸 방지.
		                        	   new Process().killProcess(Process.myPid());
//		                        	   CCDirector.sharedDirector().getActivity().finish(); // 종료는되는데 크래쉬남.
		                           }
		                       });
		                AlertDialog alert = builder.create();
		                alert.show();
				}
			});
		} else if (!clientVersion.equals(serverVersion)) { // 서버 버전과 다를때 (아이폰도 현재 같은 버전까지만 체크 합니다.)
			CCDirector.sharedDirector().getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					   AlertDialog.Builder builder = new AlertDialog.Builder(CCDirector.sharedDirector().getActivity());
		                builder.setMessage("현재 버전 " +clientVersion+ "입니다.\n새버전 " +serverVersion+ "로 업데이트 해주세요.")
		                       .setCancelable(false)
		                       .setPositiveButton("OK", new DialogInterface.OnClickListener() {
		                           public void onClick(DialogInterface dialog, int id) {
		                        	   Uri uri = Uri.parse("https://play.google.com/store/apps");
		                        	   Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//		                        	   CCDirector.sharedDirector().getActivity().moveTaskToBack(true); // 본Activity finish후 다른 Activity가 뜨는 걸 방지.
		                        	   CCDirector.sharedDirector().getActivity().startActivity(intent);
//		                        	   new Process().killProcess(Process.myPid());
		                           }
		                       });
		                AlertDialog alert = builder.create();
		                alert.show();
				}
			});
		// 서버에 접속이 안될때
		} else {
		    MainApplication.getInstance().mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    nextSceneCallback();
                }
            }, 2200);
		}
		
	}

	
	public void nextSceneCallback() {
//	    MainActivity mMainActivity = MainApplication.getInstance().getActivity();
//	    
//	    if(mMainActivity.mSimpleFacebook.isLogin()) {
//	        //get myProfile and get Friends info
//	        //go to daily scene
//	        mMainActivity.getMyProfile();
//	        mMainActivity.getFriendsInfo();
//	    } else {
    	    CCScene scene = Login.scene();
    	    CCDirector.sharedDirector().replaceScene(scene);
//	    }
	}
	
	
}
