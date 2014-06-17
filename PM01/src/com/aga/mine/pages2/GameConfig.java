package com.aga.mine.pages2;

// 일단 물어볼것 빼고 완료
public class GameConfig {

	//
	// 게임 설정 일반
	final float kLongPressTime = 0.8f; // 롱프레스 발동시간
	final float kDefaultScale = 0.71f; // 처음 맵 스케일, .23f for full screen

	// 줌인 . 아웃 관련
	final float kPinchZoomMultiplier = 0.005f;
	//final float kPinchZoomMultiplier = 0.0015f;
	final float kMinScale = 0.23f;
	final float kMaxScale = 1.0f;

	// 이모티콘 패널 이동 시간
	final float kEmoticonPanelMoveTime = 0.2f;

	//
	// 레이어 깊이, 메뉴 허드
	final int kDepthUI = 0;
	final int kDepthPopup = 20;
	int kDepthMagician = 10;

	boolean isEmoticonPanelOn;
	boolean isMinimapPanelOn;
	boolean isEndingOn;

	//---------------------------------------------------------------//
	private static GameConfig gameConfig;
	
	private GameConfig() {
	}

	// #pragma mark - Singleton Methods
	public static synchronized GameConfig share() {
		if (gameConfig == null) {
			gameConfig = new GameConfig();
		}
		return gameConfig;
	}
	//---------------------------------------------------------------//

	public boolean isEmoticonPanelOn() {
		return isEmoticonPanelOn;
	}

	public void setEmoticonPanelOn(boolean isEmoticonPanelOn) {
		this.isEmoticonPanelOn = isEmoticonPanelOn;
	}

	public boolean isMinimapPanelOn() {
		return isMinimapPanelOn;
	}

	public void setMinimapPanelOn(boolean isMinimapPanelOn) {
		this.isMinimapPanelOn = isMinimapPanelOn;
	}

	public boolean isEndingOn() {
		return isEndingOn;
	}

	public void setEndingOn(boolean isEndingOn) {
		this.isEndingOn = isEndingOn;
	}
	
	
}
// end