package com.aga.mine.pages2;

import java.io.IOException;

import org.cocos2d.layers.CCLayer;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCLabel;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.nodes.CCTextureCache;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGSize;

import android.content.Context;
import android.util.Log;

import com.aga.mine.pumpkin.Config;
import com.aga.mine.pumpkin.NetworkController;
import com.aga.mine.util.UtilResource;

//일단 완료(중간에 gameover game.release 빼고)
public class GameProgressBar extends CCLayer {
	/************** inner class *****************/
	public interface GameProgressBarDelegate{
		void gameOver2();
	}
	/***************************************/
	

	String hudLayerFolder =  "61hud/";
	
	Context mContext;

	public static final int kTagBase = 100;
	public static final int kTagIndicatorMe = 110;
	public static final int kTagIndicatorOther = 120;

	CCSprite base;
	CCSprite indicatorMe;
	CCSprite indicatorOther;
	//CCLabelTTFWithStroke progressedTime;
	CCLabel progressedTime;
	GameProgressBarDelegate delegate;
	
	private long mInitialTime;
	private long mPastTime;
	private long mGameTime;
	private long mLeftTime;

	HudLayer mHud;
	public GameProgressBar(HudLayer hudLayer) {
		mHud = hudLayer;
		mContext = CCDirector.theApp.getApplicationContext();
		CGSize winSize = CCDirector.sharedDirector().winSize();

		base = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + hudLayerFolder + "game-progressBase-hd.png"));
		base.setPosition(winSize.width / 2,
				winSize.height - base.getContentSize().height);
		this.addChild(base);

		if (Config.getInstance().getOwner()) {
			indicatorMe = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + hudLayerFolder + "game-progressIndicatorMe-hd.png"));
			indicatorOther = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + hudLayerFolder + "game-progressIndicatorOther-hd.png"));
		} else {
			indicatorMe = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + hudLayerFolder + "game-progressIndicatorOther-hd.png"));
			indicatorOther = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + hudLayerFolder + "game-progressIndicatorMe-hd.png"));
		}

		indicatorMe.setTag(kTagIndicatorMe);
		indicatorOther.setTag(kTagIndicatorOther);

		if (GameData.share().isMultiGame) {
			base.addChild(indicatorOther);
			this.progress(0.0f, kTagIndicatorOther);			
		}
		
		base.addChild(indicatorMe);
		this.progress(0.0f, kTagIndicatorMe);
		
		/*****************************************************/
		/** 문제지점 
		 * 게임 시계
		 * 
		 * @return
		 */
/*		int seconds = GameData.share().getSeconds();
		int min = seconds / 60;
		int sec = (int) seconds % 60;
		String timerString = min + ":" + sec;*/
		//progressedTime = new CCLabelTTFWithStroke(timerString,"AvenirNextCondensed-Bold", 14);
		progressedTime = CCLabel.makeLabel("00:00","AvenirNextCondensed-Bold", 28);
		progressedTime.setPosition(base.getContentSize().width / 2, base.getContentSize().height * 0.98f);
		//progressedTime.strokeSize = 1.5f;
		//progressedTime.strokeColor = ccColor3B.ccBLACK;
		base.addChild(progressedTime);
		/*****************************************************/
		
	}
	
	
	
	//GameEnding controlGameEnding; 
	//CCLayer thelayer;
	HudLayer hudLayer = null;
	//void startTime(GameEnding gameEnding) {
	//void startTime(HudLayer hudLayer) {
	
	
	/***********************************************************/
	/** 문제지점 
	 * 
	 * Thread
	 * 
	 * 게임 시계
	 * 
	 * 대전 중 기기별 오차 발생
	 * 
	 * @return
	 */
	void startTime() {
		Log.e("progressBar / startTime", "progressBar in");
		Log.e("progressBar / startTime", "hudLayer : " + hudLayer);
		Log.e("progressBar / startTime", "this.hudLayer : " + this.hudLayer);

		mInitialTime = System.currentTimeMillis();
		mGameTime = GameData.share().getSeconds();
		this.schedule("tick");
	}

	void stopTime() {
		this.unschedule("tick");
	}
	
	public void pauseTime(boolean isPause) {
		_isPause = isPause;
		if (isPause) {
			mPauseTime = mPastTime;
		} else {
			mInitialTime = System.currentTimeMillis() - mPauseTime;
		}
	}
	
	boolean _isPause = false;
	long mPauseTime;
	
	
	//private void tick(ccTime dt) {
	public void tick(float dt) {
		mPastTime = System.currentTimeMillis() - mInitialTime;
		mLeftTime = mGameTime * 1000 - mPastTime;
		//Log.e("progressBar / tick", "progressBar in");
		//int seconds = GameData.share().getSeconds();
		//seconds --;
		int seconds = (int)(mLeftTime/1000f);
		seconds = seconds < 0 ? 0 : seconds; // 음수값 방지
		GameData.share().setSeconds(seconds);
		String[] time = {"" + (seconds / 60), "" + (seconds % 60)};
		//int min = seconds / 60;
		//int sec = seconds % 60;
		//String secStr = "" + sec;
		for (int i = 0; i < time.length; i++) {
			if (time[i].length() < 2) {
				time[i] = "0" + time[i];
			}
			
		}
		//String timerString = min + ":" + secStr;
		String timerString = time[0] + ":" + time[1];
		if (!_isPause) {
			progressedTime.setString(timerString);	
		}
		

		//
		// 게임시간이 종료하면 타이머를 멈추고 게임종료 메서드 호출한다.
		if (seconds <= 0) {
			this.stopTime();
			gameover();
			
			
			
			//Log.e("Game / HudLayer / gameOver", "gameEnding - gogo");
//			GameEnding ending = GameEnding.share(this.mContext);
//			thelayer.addChild(ending, GameConfig.share().kDepthPopup);
			
			/*
			Game game = new Game();
			delegate = (GameProgressBarDelegate) new Game();
			Log.e("progressBar /  tick", "this.delegate : " + this.delegate);
			//Log.e("progressBar /  tick", "this.delegate.gameOver() : " + this.delegate.gameOver());
			//this.delegate.displayMineNumber(1, CGPoint.ccp(10, 20), 3);
			
			delegate.gameOver2();
			//this.delegate.gameOver2();  //뭐래
			//game = null; // 이게 뭐더라??? alloc init하고 같은거라고 했던가?? <-- 그냥 지우는거
			*/
			
		}
	}
	/***********************************************************/
	
	
	public void gameover() {
		Config.getInstance().setDisableButton(true);
		if (GameData.share().isMultiGame) {
			Log.e("GameProgressBar", "대전 - 시간 초과시 상대방과 점수 비교");
			try {
				NetworkController.getInstance().sendRequestGameOver(mHud.mGame.sumScore());
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			Log.e("GameProgressBar", "싱글 - 시간 초과시 무조건 승리");
			Config.getInstance().setVs(Config.getInstance().vsWin);
			mHud.gameOver(mHud.mGame.sumScore(), 0);
		}
//		mHud.gameOver(mHud.mGame.sumScore(), -1);

		((com.aga.mine.pages2.HudLayer)getParent()).setVisible(true);
		//hudLayer.getChildByTag(1234).setVisible(false);
		
		/*
		Log.e("GameProgressBar / gameover", "controlGameEnding : " + controlGameEnding);
		CCDirector.sharedDirector().getActivity().runOnUiThread(new Runnable() {
			public void run() {
				controlGameEnding.setVisible(false);
			}
		});
		*/
		
	}
	
	// CGFloat 없어서 float 대체
	public void progress(float value, int tag) {
		value = CGPoint.clampf(value, 0, 1);
		float height = base.getContentSize().height * 0.3f;
		float positionX = base.getContentSize().width * value;
		
		base.getChildByTag(tag).setPosition(positionX, height);
	}

}
