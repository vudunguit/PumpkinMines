package com.aga.mine.pages2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.cocos2d.actions.instant.CCCallFuncN;
import org.cocos2d.actions.instant.CCCallFuncND;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.layers.CCLayer;
import org.cocos2d.nodes.CCLabel;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.nodes.CCTextureCache;
import org.cocos2d.sound.SoundEngine;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.ccColor3B;
import org.cocos2d.utils.CCFormatter;

import android.content.Context;
import android.util.Log;

import com.aga.mine.pumpkin.Config;
import com.aga.mine.pumpkin.NetworkController;
import com.aga.mine.pumpkin.R;
import com.aga.mine.util.UtilResource;

public class MineCell extends CCLayer{
	
	volatile public static int mCount;
	
	private boolean isMine;
	private boolean isOpened;
	private boolean isMarked = false;
	private boolean isSphere;
	private boolean isCollidable;
	
	private int Cell_ID;
	private int sphereCell_ID;
	
	private  CGPoint tilePosition;
	private  boolean isSphereBasePossible;
	private  int sphereType;
	
	private CGPoint tileCoord;
	
	Context mContext;

	ArrayList<MineCell> sphereCells;
	ArrayList<MineCell> roundCells  = new ArrayList<MineCell>();
	ArrayList<MineCell> sphereRoundCells;
	
	public int numberOfArroundMine; //자기 주변의 지뢰 숫자
	
	MineCell() {
		isMine = false;
		isOpened = false;
		//isMarked = false; // 뭔가 이유가 있을것 같은데 기억이 안남.
		isSphere = false;
		isCollidable = false;
		sphereCells =new ArrayList<MineCell>();
		sphereRoundCells = new ArrayList<MineCell>();	
		isSphereBasePossible = true;
		sphereType = -1; // none
	}
	
	private Game mGame;
	public MineCell(Game game) {
		this();
		mGame = game;
	}
	
	public void addRoundCell(MineCell cell) {
		roundCells.add(cell);
	}
	
	public void addSphereCell(MineCell cell) {
		sphereCells.add(cell);
	}
	
	public void addSphereRoundCell(MineCell cell) {
		sphereRoundCells.add(cell);
	}
	
	public ArrayList<MineCell> getRoundCells() {
		return roundCells;
	}
	
	public ArrayList<MineCell> getSphereCells() {
		return sphereCells;
	}
	
	public ArrayList<MineCell> getSphereRoundCells() {
		return sphereRoundCells;
	}

	// 선택된 지점의 주변 셀의 마크?? 된것의 갯수를 검사하여 검사된 값을 돌려준다.(Flag 지뢰 체크)
	public int getNumberOfMushroomAndPumpkinAround() {
		if (isMarked()) return -1; 		// 호박은 -1을 넣었다.
		int count = 0;
		if (getRoundCells() != null && getRoundCells().size() != 0) {
			CopyOnWriteArrayList<MineCell> cellArray = new CopyOnWriteArrayList<MineCell>();
			cellArray.addAll(getRoundCells());
			
			for (MineCell c : cellArray) {
				if (c != null && ( (c.isMarked() || (c.isMine() && c.isOpened())))) 
					count++;
			}	
		}
		return count;
	}
	
	// 숫자 주변의 마인 수
	public int getNumberOfMineAround() {
		if (isMine()) return -1;

		int count = 0;
		for (MineCell cell : getRoundCells()) {
			if (cell != null && cell.isMine())
				count++;
		}

		return count;
	}

	public int getNumberOfSphereAround() {
		if (isMine()) return -1;

		int count = 0;
		for (MineCell cell : getRoundCells()) {
			if (cell.isSphere() || cell.isOpened() || cell.isMine())
				count++;
		}
		return count;
	}

	public boolean isSphereCellsClear() {
		for (MineCell mineCell : getSphereCells()) {
			if (mineCell != null) { // null 값이 나와서는 안된다. (임시)
				if (!mineCell.isMine() && !mineCell.isOpened() && !mineCell.isCollidable() && !mineCell.isSphere()) {
					// return true;
				} else {
					return false;
				}
			}
		}
		return true;
	}
	
	public void setToSphereCells(int sphereType) {
		int counter = 1;
		if (getSphereRoundCells() != null && getSphereRoundCells().size() != 0) {
			ArrayList<MineCell> cells = getSphereRoundCells();
			//Log.e("setToSphereCells ", " " + cells);
			int size = cells.size();
			for (int i = 0; i < size; i++) {
				// 없을시 그 값만 패스~
				if (cells.get(i) != null) {
				cells.get(i).setSphere(true); // 툭하면 에러나는 부분
				cells.get(i).setSphereCell_ID(sphereType * 10 + counter); // 1*10+1,1*10+2,1*10+3,1*10+4
				}
			}
		}
	}
	
	
	public int roundOpen() {
		int numberOfMine = this.numberOfArroundMine;
		int getNumberOfMushroomAround = this.getNumberOfMushroomAndPumpkinAround();
		//Log.e("폭탄 노출됨", "" + numberOfMine + "," + plusMine + ",");
		if (numberOfMine == getNumberOfMushroomAround) {
			// 더블탭을 했을때 더블 탭한곳의 숫자가 있으면 탐??
			CopyOnWriteArrayList<MineCell> cells = new CopyOnWriteArrayList<MineCell>();
			cells.addAll(getRoundCells());
			
			for(MineCell c : cells) {
				if(!c.isOpened && !c.isMarked) {
					c.open();
				}
			}

		}
		
		return numberOfMine;
	}
	
	public int open() {
		return open (1);
	}
	
	public int open(int depth) {
		// 이미 오픈되었거나 깃발꽂은 셀이면 그냥 빠져 나온다.
		if(isOpened() || isMarked())
			return numberOfArroundMine;
		
		// 오픈으로 데이터 설정
		setOpened(true);
		// TMX에서 현재 타일 제거
		removeTile(this.tileCoord, depth);
		
		if (GameData.share().isMultiGame) {
			try {
				NetworkController.getInstance().sendPlayDataCellOpen(getCell_ID());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		
		// 아래 if문에서 타일오픈 소리 또는 호박터지는 소리가 결정 되야 합니다.
		// showAroundMine(); or touchMine()
		
		// 지뢰는 -1로 지정했음
		int pumpkinMine = -1;
		if (numberOfArroundMine > pumpkinMine) { 
			startOpenTile(depth);
			// 프로그레스 하나 증가			
			mGame.mHud.updateProgress();
			
			// 현재셀 주변에 지뢰나 수정구가 없다면 , 주변 셀을 모두 연다.
			// 지뢰가 없는 곳만 열기 또는 지뢰와 아이템이 없는 곳만 열기
			// 지뢰 없는 곳만 열어주기
			if(numberOfArroundMine  == 0){
				//ArrayList 동기화 문제로 죽지 않도록 셀을 카피해서 사용
				CopyOnWriteArrayList<MineCell> cells = new CopyOnWriteArrayList<MineCell>();
				cells.addAll(getRoundCells());
				
				for (final MineCell cell : cells) {
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					cell.open(++depth);
				}
			}
		} else { // 지뢰 밟음
			touchMine();
		}
		
		Log.e("MineCell", "markedMine : " + GameData.share().getCurrentMine());
		Log.e("MineCell", "getClosedCell : " + mGame.getClosedCell());
		Log.e("MineCell", "getMineNumber() : " + GameData.share().getMineNumber());
		
		int closedTiles = mGame.getClosedCell();
		if (closedTiles <= GameData.share().getMineNumber()) {
			Log.e("MineCell", "new gameover : score 1000");
			if (GameData.share().isMultiGame) {
				gameOverType = continueGame;
				sendRequestGameOver(mGame.sumScore());
			} else {
				gameOverType = singleCompleted;
				gameOver(mGame.sumScore());
			}
		}
		
		return numberOfArroundMine;
	}
	
	public void removeTile(CGPoint tileCoord, int depth) {
		if(depth == 1) {
			mCount = 1;
		} else {
			++mCount;
		}

		// Global ID // Globally unique IDentifier
		int tileGid = mGame.tmxMeta.tileGIDAt(tileCoord);
		tileGid = CCFormatter.swapIntToLittleEndian(tileGid);
		// 0 : 일시 타일값 없음
		
		//메타레이어가 isCollidable, preOpened 일경우는 무시. isDontSetMine인 경우는 타일을 벗겨준다
		if (tileGid > 0) {
			HashMap<String, String> properties = mGame.tileMap.propertiesForGID(tileGid);

			// Log.e("Game / removeTile", "properties:" + properties);
			if (properties != null && properties.size() != 0) {
				String isDontSetMine = properties.get("isDontSetMine");
				
				if (isDontSetMine != null && isDontSetMine.equals("YES")) {
					mGame.getFg().removeTileAt(tileCoord);
					GameData.share().addOpenedCell(); // 오픈된 셀 수량 누적
					mGame.removeCell(); // 남은 총 cell 수량 감소
				}

			} 
		} else {
			mGame.getFg().removeTileAt(tileCoord);
			GameData.share().addOpenedCell(); // 오픈된 셀 수량 누적
			mGame.removeCell(); // 남은 총 cell 수량 감소
		}
	}
	
	synchronized private void startOpenTile(int depth) {
		//Log.w("LDK", "showAroundMine:"  + ", mcount:" + mCount);
		if(depth == 1) {
			SoundEngine.sharedEngine().playEffect(mContext, R.raw.landopen_01);
			//Log.e("LDK", "mcount:" + mCount);
		} else {
			if(mCount%2 == 0) {
				int effect = mCount/2;
				if ((effect/17)%2 == 0) {
					effect = effect%17;
				} else {
					effect = 16 - effect%17;
				}
				SoundEngine.sharedEngine().playEffect(mContext, R.raw.landopen_01 + effect);
				//Log.e("LDK", "effect:" + effect + ", mcount:" + mCount);
			}
		}
		
		//타일 오픈 애니메이션
		CCSprite tile = mGame.mAnimationTiles.get(getCell_ID()+2000);
		tile.setVisible(true);
		
		tile.runAction(CCSequence.actions(mGame.mOpenAction.copy(), CCCallFuncND.action(this, "removeTileAni", tileCoord)));
	}
	
	public void removeTileAni(Object sender, Object coord) {
		CCSprite obj = (CCSprite)sender;
		obj.setVisible(false);
		
		if(numberOfArroundMine>0) {
			displayMineNumber(numberOfArroundMine, tilePosition, Cell_ID);
			Log.e("MineCell / open", "주변 마인 갯수 : " + numberOfArroundMine);
		}
	}
	
	// 주변 지뢰 수
	public void displayMineNumber(int numberOfMine, CGPoint position, int tag) {
		if (mGame.isCollidable(mGame.tileCoordForPosition(position))) {
			return;
		}
		// 글자 크기 기존의 2/3로 감소(사유 : 수정구 바닦에 숫자가 겹쳐야 되서 수정구에 의해 숫자가 가려짐. - 박정렬 팀장님)
		CCLabel label = CCLabel.makeLabel("" + numberOfMine + " ",
				"Arial-Bold", (int) ((70 * (2 / 3.0) * mGame.tileSize.width) / 128));
		mGame.addChild(label, 5, tag);
		label.setAnchorPoint(0.5f, 0.5f);
		label.setPosition(position);
		label.setColor(ccColor3B.ccc3((int) (75 / 255f), (int) (51 / 255f),
				(int) (9 / 255f)));
	}
	
	private void touchMine() {
		//호박폭탄 애니메이션
		startPumpkinBomb();
		
		// 지뢰로 표시
		this.setMine(true);
		
		// 땅오픈
		this.setOpened(true);
		
		// 지뢰를 누르면 지뢰갯수 (화면좌측위 지뢰 숫자) 하나를 없애준다.
		int mineNumber = mGame.decreaseMineNumber();
		
		// 화면에 지뢰 갯수 갱신
		mGame.mHud.updateMineNumber(mineNumber);
		
		GameData.share().markMine();
		int currentMine = GameData.share().getCurrentMine(); // 로그용.
		Log.e("MineCell / open", "최대 지뢰 수 : " + GameData.share().getMineNumber());
		Log.e("MineCell / open", "open3 : currentMineNumber" + currentMine + ", decreaseMineNumber : " + mineNumber);

		//최대 지뢰 갯수와 currentMine의 수가 같으면 게임 종료 <<---- 밖으로 빼야 할 것 같음. 수상함. 좀 더 테스트
		Log.e("MineCell / open", "currentMine : " + currentMine + ", MaxMineNumber : " +  GameData.share().getMineNumber());
		
		if (GameData.share().getCurrentMine() == GameData.share().getMineNumber()) {
			Log.e("MineCell / open", "delegate - gameOver *** mission complete ***");
			int myScore = 0;
			
			float openedCell = GameData.share().getOpenedCell();
//			float foundMine = mGame.getFoundMine(); // 지우는건 일단 보류
			float foundMine = GameData.share().getCurrentMine(); // 올바르게 버섯이 심겨진 지뢰만(찾은 호박)
			float maxMine = GameData.share().getMineNumber(); // 테스트중
			float heart = GameData.share().getHeartNumber();
			float spentTime = 900 - GameData.share().getSeconds(); // 소요 시간

			if (heart > 0) {
				myScore = (int) ((((foundMine + heart) * maxMine) + spentTime) * maxMine * 0.006f);
			}
			
			Log.e("MineCell", "myScore : " + myScore + ", openedCell : " + openedCell + ", foundMine : " + foundMine + ", maxMine : " + maxMine + ", heart : " + heart + ", spentTime : " + spentTime);
			
			if (GameData.share().isMultiGame) {
				gameOverType = continueGame;
				sendRequestGameOver(myScore);
			} else if (GameData.share().isGuestMode) {
				gameOverType = singleCompleted;
				gameOver(myScore);
			}
			
		}
		
		// 지뢰를 밟을시 생명 하나 감소 시킨다.(현재셀은 이미 오픈되었음)
		// 지뢰를 밟을시 생명 감소 (-1)
		GameData.share().decreaseHeartNumber();
		
		// 생명수를 업데이트 시킨다.
		Log.e("MineCell / open", "updateHeart : " + updateHeart());
		updateHeart();

		// 생명수 다없어지면 게임오버
		if (GameData.share().isHeartOut()) {
			Log.e("MineCell / open", "delegate - gameOver *** mission failed ***");
			if (GameData.share().isMultiGame) {
				sendRequestGameOver(0); // 대전이므로 서버로 내점수 0점 보내기
			} else {
				gameOverType = singleFailed;
				gameOver(-1); // 대전이 아니므로 서버로 점수 안보내기
			}
			
		}
	}
	
	public void startPumpkinBomb() {
		CCSprite bomb = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + "60game/pumpkinbomb_01.png"));
		//붙이는 위치, 크기 조정해야 함.
		bomb.setPosition(tilePosition);
		mGame.addChild(bomb, 100);
		
		CCCallFuncN remove = CCCallFuncN.action(this, "cbRemovePumpkinBomb");
		bomb.runAction(CCSequence.actions(mGame.mPumpkinBomb, remove));
		
		SoundEngine.sharedEngine().playEffect(mContext, R.raw.pumpkin);
	}
	
	public void cbRemovePumpkinBomb(Object sender) {
		CCSprite sprite = (CCSprite)sender;
		sprite.removeFromParentAndCleanup(true);
	}
	
	public boolean updateHeart() {
		return mGame.mHud.updateHeart();
	}
	
	// 대전했을시 게임오버 점수
	private void sendRequestGameOver(int myScore) {
		Log.e("MineCell", "myScore : " + myScore);
		try {
			NetworkController.getInstance().sendRequestGameOver(myScore);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	int gameOverType = 0; //
	final int singleFailed = 1;
	final int singleCompleted = 2;
	final int continueGame = 3;
	
	// 싱글 및 게스트일때 게임오버 점수
	private void gameOver(int myScore) {
		Log.e("MineCell", "myScore : " + myScore);
		if (gameOverType == singleFailed) {
			Log.e("MineCell", "싱글 - 생명 전부 소모로 종료 (type) : " + gameOverType);
			Config.getInstance().setVs(Config.getInstance().vsLose);
			mGame.mHud.gameOver(myScore, 0);
		} else if (gameOverType == singleCompleted){
			Log.e("MineCell", "싱글 - 전부 찾고 종료 (type) : " + gameOverType);
			Config.getInstance().setVs(Config.getInstance().vsWin);
			myScore = (int) (myScore*0.2);
			mGame.mHud.gameOver(myScore, 0);
		} else if (gameOverType == continueGame){
			Log.e("MineCell", "멀티 - 계속 하기 (type) : " + gameOverType);
		}
	}

	
	public int getSphereItem() {
		CopyOnWriteArrayList<MineCell> copiedSphereCells = new CopyOnWriteArrayList<MineCell>();
		copiedSphereCells.addAll(getSphereCells());
		for (MineCell cell : copiedSphereCells) {
			if(!cell.isOpened()) return -1;
		}
		
		CopyOnWriteArrayList<MineCell> copiedSphereRoundCells = new CopyOnWriteArrayList<MineCell>();
		copiedSphereRoundCells.addAll(getSphereRoundCells());
		for (MineCell cell : copiedSphereRoundCells) {
				if (cell.isOpened() || cell.isMarked())
					continue;
				else
					return -1;
		}
		
		return getSphereType();
	}

	public boolean isMine() {
		return isMine;
	}

	public void setMine(boolean isMine) {
		this.isMine = isMine;
	}

	public boolean isOpened() {
		return isOpened;
	}

	public void setOpened(boolean isOpened) {
		this.isOpened = isOpened;
	}

	public boolean isMarked() {
		return isMarked;
	}

	public void setMarked(boolean isMarked) {
		this.isMarked = isMarked;
	}

	public boolean isCollidable() {
		return isCollidable;
	}

	public void setCollidable(boolean isCollidable) {
		this.isCollidable = isCollidable;
	}

	public int getCell_ID() {
		return Cell_ID;
	}

	public void setCell_ID(int cell_ID) {
		this.Cell_ID = cell_ID;
	}

	public CGPoint getTilePosition() {
		return tilePosition;
	}

	public void setTilePosition(CGPoint tilePosition) {
		this.tilePosition = tilePosition;
	}

	public boolean isSphere() {
		return isSphere;
	}

	public void setSphere(boolean isSphere) {
		this.isSphere = isSphere;
	}

	public int getSphereCell_ID() {
		return sphereCell_ID;
	}

	public void setSphereCell_ID(int sphereCell_ID) {
		this.sphereCell_ID = sphereCell_ID;
	}

	public boolean isSphereBasePossible() {
		return isSphereBasePossible;
	}

	public void setSphereBasePossible(boolean isSphereBasePossible) {
		this.isSphereBasePossible = isSphereBasePossible;
	}

	public int getSphereType() {
		return sphereType;
	}

	public void setSphereType(int sphereType) {
		this.sphereType = sphereType;
	}

	public CGPoint getTileCoord() {
		return tileCoord;
	}

	public void setTileCoord(CGPoint tileCoord) {
		this.tileCoord = tileCoord;
	}
	
}
