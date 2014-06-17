package com.aga.mine.pumpkin;

import java.util.Timer;

import org.cocos2d.nodes.CCSprite;
import org.cocos2d.types.ccColor3B;

import android.util.Log;

public class Config {

	//
	// User answer
	String[] userAnswerArray = { "kAnswerLeft", "kAnswerRight", "kAnswerInvalid" };
	String[] questionItemArray = { "kQuestionCurrentItem", "kQuestionNextItem", "kQuestionThisFlag", "kQuestionOtherFlag" };
	String[] uiItemArray = { "kUIScoreItem", "kUIGoldItem" };
	String[] isoCodeArray = { "kIsoCodeKo", "kIsoCodeEn", "kIsoCodeZh" };
	int userAnswer = -1;
	int questionItem = -1;
	int uiItem = -1;
	int isoCode = -1;
	
	// MineGame
	String[] modeChoiseArray = {"kModeSingle", "kModeVersus", "kModeInvite"};
	String[] itemAttackArray = {"ItemAttackFire", "ItemAttackWind", "ItemAttackCloud"};
	String[] itemDefenseArray ={"ItemDefenseFire", "ItemDefenseWind", "ItemDefenseCloud"};
	int modeChoise = -1;
	int itemAttack = -1;
	int itemDefense = -1;
	
	boolean singleMulti ;
	boolean notificateDisableButton;
	boolean daily ;
	
	// 화면 전환
	final int kTagPrevious = 501;
	final int kTagHome = 502;

	// final int kMaxTime = 40; <-- 게임시간은 레벨별로 다르게 데이터파일에서 읽어오는것으로 변경되었다.
	final int kMaxLevel = 50;
	final int kMaxItemLevel = 20;
	final float kUtilityDimScreenOpacity = 0.7f;

	final int kTagQuestionCurrent = 990;
	final int kTagQuestionNext = 991;
	final int kTagThisFlag = 992;
	final int kTagOtherFlag = 993;

	final int kTagScoreLabel = 994;
	final int kTagGoldLabel = 995;

	//
	// 글꼴설정
	final String kFontNameDefault = "Futura-Medium";
	final String kFontNameMultiline = "Arial";
	final String kFontNameNumber = "ChalkboardSE-Bold";
	final ccColor3B kFontColorDefault = ccColor3B.ccc3(0xff, 0xff, 0xff);
	final String kFontNameThickNumber = "AvenirNext-Heavy";

	final String kFontNameStoreButtonLabel = kFontNameNumber;
	final int kFontSizeStoreButtonLabel = 14;
	final int kFontSizeScoreGold = 12;

	final ccColor3B kQuestionTextColor = ccColor3B.ccWHITE;

	//
	// App Store Url
	final String kAppStoreUrl = "https://itunes.apple.com/app/ox-flag/id661987513?l=en&mt=8";
	final String kAppStoreUrlShort = " http://goo.gl/0FgzW #Agatong";
	final String kHashTag = "#Agatong";
	final String kAppStoreDeveloperUrl = "https://itunes.apple.com/kr/artist/samwoo-space/id490586712?l=en";

	//
	// Game Center
	final String kGameCenterLeaderboard = "com.pixtation.OXFlag.Leaderboard";
	final int kFlagRefillTime = 1; // 임시
	final int kBroomstickRefillTime = (5*60); // 깃발 채워지는 시간
	final String kLastSavedDate = "kLastSavedDate";
	final String kTimeLeft = "kTimeLeft";
	final float kMinimumTouchTimeInterval = 0.5f; // 최소 터치 간격 시간

	//
	// BGM
	final String kSfxBgmHome = "bgmHome.mp3";
	final String kSfxBgmGame = "bgmGame.mp3";

	//
	// SFX	
	final int kTagOptionSFX = 1; // MineGame 효과음 설정
	final String kSfxTouch = "buttonselect.wav";
	final String kSfxTouchBuy = "storebuy.wav";
	final String kSfxGameStartBase = "gamestart.wav";
	final String kSfxTimeOutBase = "timeout.wav";
	final String kSfxClockNormal = "clocksound.wav";
	final String kSfxClockWarning = "clockwarning.wav";
	final String kSfxCombo = "combo.wav";
	final String kSfxRight = "correct.wav";
	final String kSfxWrong = "wrong2.wav";
	final String kSfxHint = "hitsound.wav";
	final String kSfxTakeItem = "itemFX.wav";
	final String kSfxLevelUp = "levelup.wav";

	final ccColor3B COLORTEXT = ccColor3B.ccc3(0x4c, 0x4c, 0x4c);
	final int kTagDimBackground = 100009;

	//
	// User Default Keys
	final String kLastExecutedDate = "kLastExecutedDate";
	final String kSharePoint = "kSharePoint";
	final String kCurrentBundleVersionMoreApps = "kCurrentBundleVersionMoreApps";
	final String kCurrentBundleVersionSpriteImages = "kCurrentBundleVersionSpriteImages";

	final int kTagMenuInApp = 2001;
	final int kTagMenuShare = 2002;

	//
	// Version Type
	final int kVersionTypePaid = 0;
	final int kVersionTypeLite = 1;
	final int kVersionTypeFree = 2;
	
	//
	// Version Define(kVersionTypeLite / kVersionTypePaid)
	final int VERSIONTYPE = kVersionTypePaid;
	
	String[] pathTypeArray = { "kPathTypeDocument", "kPathTypeBundle"};
	int pathType = -1;

	/***************************************************/
	
	private static Config config;
	
	boolean disableButton;
	boolean isNewGame;
	
	int thisGameScore;
	int thisGameGainedGold;
	int currentQuestionIndex;
	int flagTimeCountDown;	
	int continueGameCount;
	
	Timer flagTimer; // Date flagTimer; // int flagTimer;

	CCSprite currentFlagLeft = null;
	CCSprite currentFlagRight = null;
	CCSprite currentQuestion = null;
	CCSprite nextQuestion = null;

	// ----------------------------------------------------//

	boolean isAttack;
	boolean previousSecne;
	// ModeChoise modeChoise;
	// ItemAttck itemAttck;

	// 임시로 넣은 값(타입)
	final int kAnswerInvalid = 0;
	final int kModeSingle = 0;
	final int ItemAttackFire = 0;
	final int ItemDefenseFire = 0;
	
	public static synchronized Config getInstance() {
		if (config == null)
			config = new Config();
		return config;
	}
	
	/***************************************************/
	// self init 내용들 넣어주는곳
	public Config() {
		
		setDisableButton(false);
		setNewGame(true);
		setCurrentQuestionIndex(0);
		setFlagTimer(null);
		// thisGameScore = 0;

		// read file and calculate
		userAnswer =  arraySearch(userAnswerArray, "kAnswerInvalid");
		//this.answerArray = new ArrayList<Game>();  //  무슨용도???
		// self.flagTimeCountdown = kFlagRefillTime;

		// MineGame
		setModeChoise(arraySearch(modeChoiseArray, "kModeSingle"));
		setItemAttack(arraySearch(itemAttackArray, "ItemAttackFire"));
		 setItemDefense(arraySearch(itemDefenseArray, "ItemDefenseFire"));
		setAttack(true);
		setPreviousSecne(true);
		setSingleMulti(true);
		setNotificateDisableButton(true);
		setDaily(true);
	}
	// Config() end
	/***************************************************/
	private int arraySearch(String[] strArray, String str) {
		for (int k = 0; k < strArray.length; k++) {
			if (strArray[k].equals(str)) return k;
		}
		return 0;
	}
	
	/***************************************************/
	public boolean isNotificateDisableButton() {
		return notificateDisableButton;
	}

	public void setNotificateDisableButton(boolean notificateDisableButton) {
		this.notificateDisableButton = notificateDisableButton;
	}

	public boolean isDisableButton() {
		return disableButton;
	}

	public void setDisableButton(boolean disableButton) {
		this.disableButton = disableButton;
	}

	public boolean isNewGame() {
		return isNewGame;
	}

	public void setNewGame(boolean isNewGame) {
		this.isNewGame = isNewGame;
	}

	public int getThisGameScore() {
		return thisGameScore;
	}

	public void setThisGameScore(int thisGameScore) {
		this.thisGameScore = thisGameScore;
	}

	public int getThisGameGainedGold() {
		return thisGameGainedGold;
	}

	public void setThisGameGainedGold(int thisGameGainedGold) {
		this.thisGameGainedGold = thisGameGainedGold;
	}

	public int getCurrentQuestionIndex() {
		return currentQuestionIndex;
	}

	public void setCurrentQuestionIndex(int currentQuestionIndex) {
		this.currentQuestionIndex = currentQuestionIndex;
	}

	public int getFlagTimeCountDown() {
		return flagTimeCountDown;
	}

	public void setFlagTimeCountDown(int flagTimeCountDown) {
		this.flagTimeCountDown = flagTimeCountDown;
	}

	public int getContinueGameCount() {
		return continueGameCount;
	}

	public void setContinueGameCount(int continueGameCount) {
		this.continueGameCount = continueGameCount;
	}

	public Timer getFlagTimer() {
		return flagTimer;
	}

	public void setFlagTimer(Timer flagTimer) {
		this.flagTimer = flagTimer;
	}

	
	public CCSprite getCurrentFlagLeft() {
		return currentFlagLeft;
	}

	public void setCurrentFlagLeft(CCSprite currentFlagLeft) {
		this.currentFlagLeft = currentFlagLeft;
	}

	public CCSprite getCurrentFlagRight() {
		return currentFlagRight;
	}

	public void setCurrentFlagRight(CCSprite currentFlagRight) {
		this.currentFlagRight = currentFlagRight;
	}

	public CCSprite getCurrentQuestion() {
		return currentQuestion;
	}

	public void setCurrentQuestion(CCSprite currentQuestion) {
		this.currentQuestion = currentQuestion;
	}

	public CCSprite getNextQuestion() {
		return nextQuestion;
	}

	public void setNextQuestion(CCSprite nextQuestion) {
		this.nextQuestion = nextQuestion;
	}

	public boolean isAttack() {
		return isAttack;
	}

	public void setAttack(boolean isAttack) {
		this.isAttack = isAttack;
	}

	public boolean isPreviousSecne() {
		return previousSecne;
	}

	public void setPreviousSecne(boolean previousSecne) {
		this.previousSecne = previousSecne;
	}

	public int getModeChoise() {
		return modeChoise;
	}

	public void setModeChoise(int modeChoise) {
		this.modeChoise = modeChoise;
	}

	public int getItemAttack() {
		return itemAttack;
	}

	public void setItemAttack(int itemAttack) {
		this.itemAttack = itemAttack;
	}

	public int getItemDefense() {
		return itemDefense;
	}

	public void setItemDefense(int itemDefense) {
		this.itemDefense = itemDefense;
	}

	public boolean isSingleMulti() {
		return singleMulti;
	}

	public void setSingleMulti(boolean singleMulti) {
		this.singleMulti = singleMulti;
	}

	public boolean isDaily() {
		return daily;
	}

	public void setDaily(boolean daily) {
		this.daily = daily;
	}

	// currentUser Selection
	// answerArray
	
	
	/***************************************************/
	/**************************** 추가 or 변경 ****************************/
	boolean guest = false;
	public boolean isGuest() {
		return this.guest;
	}
	
	public final boolean vsLose = false;
	public final boolean vsWin = true;
	public boolean vsResult = true;
	
	public void setVs(boolean vsResult) {
		this.vsResult = vsResult; 
	}
	
	public boolean getVs() {
		return vsResult; 
	}

	boolean owner = false;
	
	public void setOwner() {
		Log.e("Config", "setOwner : " + owner);
		owner =  NetworkController.getInstance().getOwner();
	}
	
	public boolean getOwner() {
		return owner;
	}
	
	public void setSingleOwner() {
		Log.e("Config", "setSingleOwner : " + owner);
		owner = true;
	}
}
