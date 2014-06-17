package com.aga.mine.pumpkin;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;

import android.util.Log;

import com.aga.mine.pages2.GameData;
import com.sromku.simple.fb.entities.Profile;

public class DataFilter {
	
	static String[] keys = {
			"RequestMode",
			"FacebookId",
			"LevelCharacter", // 1
			"SphereNumber", // 2 
			"Exp", // 3
			"Gold", // 4
			"Point", // 5 사용 안함
			"HistoryWin", // 6 
			"HistoryLose", // 7
			"ReceivedBroomstick", // 8
			"LevelFire",  //9
			"LevelWind", // 10
			"LevelCloud", // 11 
			"LevelDivine", // 12
			"LevelEarth", // 13
			"LevelMirror", // 14
			"Emoticons", // 15
			"InviteNumber", // 16
			"Score",
			"SenderFacebookId",
			"Category",
			"Amount",
			"RequestId", // facebook 알림 글 번호로 받아오는 것 같다.
			"FacebookIdList",
			"DeviceType"
	};
		
	private static String headerEraser(String source, String filterStr) {
		if (source.indexOf(filterStr) < 0) {
			return source;
		}
		return source.substring(source.indexOf(filterStr) + filterStr.length(), source.length());
	}
	
	public static boolean readFilter(String RequestModeRead) {
		Log.e("DataFilter", "RequestModeRead response [" + RequestModeRead +"]");
		if (RequestModeRead == null || RequestModeRead.equals("") || RequestModeRead.indexOf("query error") >= 0) {
			Log.e("DataFilter", "false");
			return false;
		}
		Log.e("DataFilter", "true");
		return true;
	}
	
	public static String[] userDBFilter(String source) {
		return headerEraser(source, "***").split("-");
	}

	public static String gameScoreFilter(String source) {
		if (!headerEraser(source, "isQuerySuccess=YES").equals(source)) {
			return "complete";
		};
		return "false";
	}

	public static String[] mailFilter(String source) {
		return headerEraser(source, "data=").split(",");
	}
	
	public static boolean sendMailFilter(String source) {
		Log.e("DataFilter", "sendMailFilter : " + Integer.parseInt(headerEraser(source, "Rows=")));
		if (Integer.parseInt(headerEraser(source, "Rows=")) == 1)
			return true;
		return false;
	}

	public static void dailyFilter(CCDirector director, String facebookID) {
			String requestModeDailyCheck = getDailyData(facebookID);
//			String requestModeDailyCheck = "13"; // 테스트 코드
			CCScene scene = CCScene.node();
			if (requestModeDailyCheck.equals("not once a day")) {
				scene = Home.scene();
			} else if (0 < Integer.parseInt(requestModeDailyCheck) % 31) {
				scene.addChild(Daily.scene(Integer.parseInt(requestModeDailyCheck) % 31));
			} else {
	//			new Process();
	//			Process.killProcess(Process.myPid()); // 문의 넣기 메시지 출력 후 종료
				director.getActivity().finish(); // 문의 넣기 메시지 출력 후 종료
			}
			director.runWithScene(scene);
		}

	/***********************************************************/
	
	// 1
	public static String getGameVersionData() {
			try {
				return new DataController().execute("0,RequestModeIsServerOk*24,0").get();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		return null;
	}
	
	// 2
	public static String getUserDBData(String facebookID) {
		try {
			return new DataController().execute("0,RequestModeRead*1," + facebookID).get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String checkUserDBData(String facebookID) {
		String getData = getUserDBData(facebookID);
		if (!readFilter(getData)) { // 정상작동은 false ,해당 ID 데이터 초기화시 true
//		if (true) { // true (test용)
			Log.e("DataFilter", "getUserDBData() 새로운 아이디 생성 또는 기존 아이디 초기화");
			GameData.share().setInitialConnection(true);
			setUserDBData(facebookID);
			getData = getUserDBData(facebookID);
		}
		Log.e("DataFilter", "getUserDBData : " + getData);
		return getData;
	}

	// 3
	public static void setUserDBData(String facebookID) {
		Log.e("DataFilter", "setUserDBData");
		Log.e("DataFilter", "아이디 최초 생성시 우편으로 3500 Gold 보내야됨.");
					new DataController().execute(
							"0,RequestModeUpdate" +
							"*1,"  + facebookID + 
							"*2," +DefaultUserData.level +
							"*3," +DefaultUserData.sphere +
							"*4," +DefaultUserData.exp +
							"*5," +DefaultUserData.gold +
							"*6," +DefaultUserData.gameScore +
							"*7," +DefaultUserData.win +
							"*8," +DefaultUserData.lose +
							"*9," +DefaultUserData.broomstick +
							"*10," +DefaultUserData.fireLevel +
							"*11," +DefaultUserData.windLevel +
							"*12," +DefaultUserData.cloudLevel +
							"*13," +DefaultUserData.divineLevel +
							"*14," +DefaultUserData.earthLevel +
							"*15," +DefaultUserData.mirrorLevel +
							"*16," +DefaultUserData.emoticons +
							"*17," +DefaultUserData.invite);
	}
	
	public static void setUserDBData(Map<String, String> mapData) {
	    Iterator<String> iterator = mapData.keySet().iterator();
	    String sendData = "0,RequestModeUpdate";
//	    String sendData = null;
		while (iterator.hasNext()) {
			String key = (String) iterator.next();
			for (int i = 0; i < keys.length; i++) {
				if (key.equals(keys[i])) {
					sendData += "*" + i;
					break;
				}
			}
			// sendData += "*" + key;
			sendData += "," + mapData.get(key);
		}
		Log.e("DataFilter", "sendData [" + sendData + "]");
	   new DataController().execute(sendData);
	}

	// 4
	public static String addGameScore(String score) {
		try {
			return new DataController().execute(
					"0,RequestModeAddScore" +
					"*1," + FacebookData.getinstance().getUserInfo().getId() +
					"*18,"+ score).get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	// 5
	public static boolean sendMail(String data) {
			try {
				return DataFilter.sendMailFilter(new DataController().execute(data).get());
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
			return false;
	}
	
	// 6
	public static String[] readMail() {
		try {
			// 정렬후 String[]로 리턴
			return DataFilter.mailFilter(
					// 메일을 읽어 
					new DataController().execute(
							"0,RequestModeMailBoxRead*1," + FacebookData.getinstance().getUserInfo().getId()).get());
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	// 7
	public static String deleteMail(String requestIDs) {
		try {
			return new DataController().execute("0,RequestModeMailBoxDelete*22,"+ requestIDs).get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return null;
	}

	// 8
	public static ArrayList<GameScore> getGameRank() {
		// 페이스북 아이디 받기
		Profile user = FacebookData.getinstance().getUserInfo();		
		List<Profile> friends = FacebookData.getinstance().getFriendsInfo();
	
		//DB조회를 위해 아이디들 합치기 (나, 친구들)
		String facebookIDs = "(" + user.getId();		
		for (Profile profile : friends) {
			facebookIDs += "," + profile.getId();
		}
		facebookIDs += ")";
		Log.e("DataFilter","facebookIDs : " + facebookIDs );
	
		try {
			// DB에서 게임 스코어 조회
			String rank = new DataController().execute("0,RequestModeGetInDBUserList*23," + facebookIDs).get();
			Log.e("DataFilter", "getRanking : " + rank);

			// 결과값 유저별 데이터 자르기
			String[] array = headerEraser(rank, "***").split("\\&");
			
			ArrayList<GameScore> sourceList = new ArrayList<GameScore>(); 
			
			GameScore gameScoreModel;
			
			// 각 유저별 데이터 상세 자르기
			int count = 1;
			for (String strings : array) {
//				Log.e("DataFilter", "strings : " + strings);
				String[] userScoreData = strings.split("-");
				
//				for (String string : userScoreData) { // 테스트용
//					if (string != null)
//						Log.e("DataFilter", "userScoreData : " + string);						
//				}
				
				gameScoreModel = new GameScore();
				gameScoreModel.id = userScoreData[0];

				if (user.getId().equals(gameScoreModel.id))
					gameScoreModel.name = user.getName();
				else
					for (Profile facebook : friends) {
						if (facebook.getId().equals(gameScoreModel.id)) {
							gameScoreModel.name = facebook.getName();
							break;
						}
					}
				gameScoreModel.level = 	checkValue(userScoreData[1]);
				gameScoreModel.score = 	checkValue(userScoreData[2]);
				sourceList.add(gameScoreModel);
				count ++;
			}
			// 스코어 내림차순 정렬
			Collections.sort(sourceList, new ClothesComparator());
			
			// 데이터 확인
			for (GameScore strings : sourceList) {
				Log.e("DataFilter", "score : " + strings.name + "," + strings.id + "," + strings.level + "," + strings.score);
			}

			return sourceList;
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	private static int checkValue(String data) {
		if (data == null || data.equals("") )
			return 0;
		return Integer.parseInt(data);
	}
	
	// 남은 시간을 초로 리턴
	public static float getInitTime() {
		try {
			return Float.parseFloat(new DataController().execute("0,RequestModeGetWeeklyLeftTime").get());
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return -10;
	}
	
	// 10
	public static String getDailyData(String facebookID) {
		try {
			return new DataController().execute(
					"0,RequestModeDailyCheck*1," + facebookID).get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return "error";
	}
	
	class DefaultUserData {
		final static int level = 1;
		final static int sphere = 3;
		final static int exp = 0;
		final static int gold = 0; // 우편으로 3500Gold 보내야됨.
		final static int gameScore = 0;
		final static int win = 0;
		final static int lose = 0;
		final static int broomstick = 6;
		final static int fireLevel = 1;
		final static int windLevel = 1;
		final static int cloudLevel = 1;
		final static int divineLevel = 0;
		final static int earthLevel = 0;
		final static int mirrorLevel = 0;
		final static String emoticons = "1,2,3";
		final static int invite = 0; 
	}
}
