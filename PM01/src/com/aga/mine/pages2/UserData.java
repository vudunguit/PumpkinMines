package com.aga.mine.pages2;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.cocos2d.nodes.CCDirector;
import org.cocos2d.types.CGPoint;

import android.content.Context;
import android.util.Log;

import com.sromku.simple.fb.entities.Profile;

public class UserData{

	public static String userID = "";
	public static String userName = "Guest";
//	public static  Profile facebookUserInfo = null;
//	public static String accessToken = "";
	
//	public static ArrayList<String> facebookFriends = new ArrayList<String>();
	public static  List<Profile> facebookFriendsInfo = new ArrayList<Profile>();
	private int kResultWin = 1;
	private int kResultLose = 0;
	
	int kOptionBGM = 0;
	int kOptionSFX = 1;
	int kOptionRefuseInvitation = 2;
	int kOptionReceiveBloomstick = 3;
	int kOptionShowMyPicture = 4;
	int kOptionNoviceGuide = 5;
	public int broomtime = 0;
	
	public static int difficulty = 0; // 게임 난이도
	
	// 최대 기본 지급 빗자루 수
	final int timeMaxBroomstick = 6; // 6개
	// 홈에 저장할 수 있는 최대 빗자루 수
	final int homeMaxBroomstick = 20; // 20개
	// 최대 우편물 수
	final int postboxMax = 30; 
	
	// 기본 지급 빗자루 누적 시간(15분)
	public final long broomstickRenewalTime = 900000L; // 기본 900000L
//	public final long broomstickRenewalTime = 20000L;
	// 빗자루 보내기 활성 시간(3시간) (상대에게 보상 빗자루1 to우편함)
	public final long broomstickRepresentTime = 10800000L;
	// 친구 초대 활성 시간 (24시간) (보상은 빗자루1)
	public final long friendReinviteTime = 86400000L;
	
	// 초대 누적당 보상 (단위 : gold)
	public final int invite30 = 5000;
	public final int invite60 = 10000;
	public final int invite90 = 30000;
	
	// 연속 출석 보상 (최대 30일)
	final public int[] rollBook = {
			100,200,300,400,1000,
			500,600,700,800,2000,
			900,1000,1100,1200,3000,
			1300,1400,1500,1600,4000,
			1700,1800,1900,2000,5000,
			2100,2200,2300,2400,10000};
	
	// 빗자루 구입 비용
	private HashMap<Integer, Integer> buyBroomstick = new HashMap<Integer, Integer>();
	private void bBroomstick() {
		buyBroomstick.put(5, 1000);
		buyBroomstick.put(10, 1800);
		buyBroomstick.put(20, 3400);
		buyBroomstick.put(40, 6400);
		buyBroomstick.put(80, 12000);
		buyBroomstick.put(160, 22400);
	}
	
	// 골드 구입 비용
	private HashMap<Integer, String> buyGold = new HashMap<Integer, String>();
	private void bGold() {
		buyGold.put(5000, "$0.99");
		buyGold.put((int)(25000*1.25f), "$4.99");
		buyGold.put((int)(50000*1.30f), "$9.99");
		buyGold.put((int)(150000*1.35f), "$24.99");
		buyGold.put((int)(250000*1.40f), "$49.99");
		buyGold.put((int)(500000*1.45f), "$99.99");
	}
	
	// 수정구 구입 비용
	final public int[] buySphere = {
			2000, 4600, 10500, 24100, 55400, 127400};
	
	final public int[][] magicPrice = {
			{ 800, 1040, 1350, 1750, 2270, 2950, 3830, 4970, 6460, 8390, 10900,
					14170, 18420, 23940, 31120, 40450, 52580, 68350, 88850, 115500 },
			{ 560, 720, 930, 1200, 1560, 2020, 2620, 3400, 4420, 5740, 7460,
					9690, 12590, 16360, 21260, 27630, 35910, 46680, 60680, 78880 },
			{ 400, 520, 670, 870, 1130, 1460, 1890, 2450, 3180, 4130, 5360,
					6960, 9040, 11750, 15270, 19850, 25800, 33540, 43600, 56680 },
			{ 800, 1040, 1350, 1750, 2270, 2950, 3830, 4970, 6460, 8390, 10900,
					14170, 18420, 23940, 31120, 40450, 52580, 68350, 88850, 115500 },
			{ 560, 720, 930, 1200, 1560, 2020, 2620, 3400, 4420, 5740, 7460,
					9690, 12590, 16360, 21260, 27630, 35910, 46680, 60680, 78880 },
			{ 400, 520, 670, 870, 1130, 1460, 1890, 2450, 3180, 4130, 5360,
					6960, 9040, 11750, 15270, 19850, 25800, 33540, 43600, 56680 }
	};
	
	public int offenceMagic = 0;
	public int defenceMagic = 3;
	
	// 불마법 구입 비용(공격)
	final public int[] buyOffenseFire = {
			800,1040,1350,1750,2270,
			2950,3830,4970,6460,8390,
			10900,14170,18420,23940,31120,
			40450,52580,68350,88850,115500	};
	
	// 바람마법 구입 비용(공격)
	final public int[] buyOffenseWind = {
			560,720,930,1200,1560,
			2020,2620,3400,4420,5740,
			7460,9690,12590,16360,21260,
			27630,35910,46680,60680,78880};
	
	// 구름마법 구입 비용(공격)
	final public int[] buyOffenseCloud = {
			400,520,670,870,1130,
			1460,1890,2450,3180,4130,
			5360,6960,9040,11750,15270,
			19850,25800,33540,43600,56680};
	

	// 불마법 구입 비용(방어)
	final public int[] buyDefenseFire = {
			800,1040,1350,1750,2270,
			2950,3830,4970,6460,8390,
			10900,14170,18420,23940,31120,
			40450,52580,68350,88850,115500	};
	
	// 바람마법 구입 비용(방어)
	final public int[] buyDefenseWind = {
			560,720,930,1200,1560,
			2020,2620,3400,4420,5740,
			7460,9690,12590,16360,21260,
			27630,35910,46680,60680,78880};
	
	// 구름마법 구입 비용(방어)
	final public int[] buyDefenseCloud = {
			400,520,670,870,1130,
			1460,1890,2450,3180,4130,
			5360,6960,9040,11750,15270,
			19850,25800,33540,43600,56680};

	// 싱글모드 기본조건 (초,중,상) {호박수, 생명수, 게임시간, 정력석}
	final public int[][] singleDefalutData = {
			{30, 3, 15, 0},
			{60, 3, 15, 0},
			{90, 3, 15, 0},
	} ;
	
	// 대전모드 기본조건 (초,중,상) {호박수, 생명수, 게임시간, 정력석}
	final public int[][] versusDefalutData = {
			{30, 3, 15, 3},
			{60, 3, 15, 3},
			{90, 3, 15, 3},
	} ;
	
	// 정령석 확률(아이템획득??? 공격/방어성공률??)
	int offenseProbability = 70;
	int duringTheOffenseFire = 20; 
	int duringTheOffenseWind = 30; 
	int duringTheOffenseCloud = 50; 
	
	int defenseProbability = 30;
	int duringTheDefenseFire = 20; 
	int duringTheDefenseWind = 30; 
	int duringTheDefenseCloud = 50; 
	
	//
	// ** 승패 팝업
	// * 보상조건
	// 찾은 호박수
	int searchPumpkin = 90; // 예제
	// 생명수
	int watersOfLife = 3; // 예제
	// 소요시간
	int LeadTime = 1000; // 예제
	
	// 포인트
	//싱글모드 : (((찾은 지뢰수 * 남은 생명수*1000) / 소요시간) * 2)			
	//대전모드 : ((찾은 지뢰수 * 남은 생명수*1000) / 소요시간) * 10
	// 싱글
	int point = searchPumpkin * watersOfLife * 1000 / LeadTime * 2; 
	// 대전
	//int point = searchPumpkin * watersOfLife * 1000 / LeadTime * 10;
	
	// 경험치 (획득 포인트 * 1.5)
	int EXP = (int)(point * 1.5f);
	// 골드 (획득 포인트 / 8)
	int gainGold = (int)(point /8);

	// ** 포인트 차감 또는 포인트 방어 둘중에 하나를 선택 **
	int winnersPoint = 200;
	
	// 포인트 차감 (패배시 승리자 포인트의 / 3)
	int pointSubtraction = (int)(winnersPoint / 3);
	
	// 포인트 방어 골드 (포인트 차감 / 10)
	int pointDefense = pointSubtraction / 10;
	
	// 아이템 총 구매비/1회 획득골드	4426 ????
	
	
	// 레벨당 경험치 및 레벨업 보상(골드)
	public static final int[] expPerLevel = {
			7000,			7350,			7710,			8090,			8490,
			8910,			9350,			9810,			10300,		10810,
			11350,		11910,		12500,		13120,		13770,
			14450,		15170,		15920,		16710,		17540,
			18410,		19330,		20290,		21300,		22360,
			23470,		24640,		25870,		27160,		28510,
			29930,		31420,		32990,		34630,		36360,
			38170,		40070,		42070,		44170,		46370,
			48680,		51110,		53660,		56340,		59150,
			62100,		65200,		68460,		71880,		75470
	};
	
	final int[] compensationPerLevel = {
			1000,			1050,			1100,			1150,			1200,
			1260,			1320,			1380,			1440,			1510,
			1580,			1650,			1730,			1810,			1900,
			1990,			2080,			2180,			2280,			2390,
			2500,			2620,			2750,			2880,			3020,
			3170,			3320,			3480,			3650,			3830,
			4020,			4220,			4430,			4650,			4880,
			5120,			5370,			5630,			5910,			6200,
			6510,			6830,			7170,			7520,			7890,
			8280,			8690,			9120,			9570,			10040
	};
	
	
	private String kPath = "UserData.plist";

	private Context mContext;
	private HashMap<String, Object> data;
	
	private static UserData userData;

	// #pragma mark - Singleton Methods
	public static synchronized UserData share(Context context) {
		if (userData == null) {
			userData = new UserData(CCDirector.sharedDirector().getActivity().getApplicationContext());
			Log.e("** UserData **", "make Single Instance");
		}
		return userData;
	}

	// 파일이 존재시 데이터를 가지고 오고, 없을시 데이터 생성(File생성x)
	private UserData(Context context) {
		mContext = context;

		
		// 파일 유무 체크(파일이 존재하면 불러오기)
		try {
			
			// 파일이 이미 있으면 불러오고,
			InputStream fis = mContext.openFileInput(kPath);
			InputStreamReader isr = new InputStreamReader(fis, "utf-8");
			BufferedReader br = new BufferedReader(isr);
			//StringBuffer sb = new StringBuffer();
			this.data = new HashMap<String, Object>();
			while (true) {
				String readLine = br.readLine();
				if (readLine == null)
					break;
					if ( !readLine.equals("")) {
						String[] key = readLine.split("=");
						/*
						Log.e("key", key[1]);
						String[] typeValue = key[1].split(")");
						mapType = mapType.substring(1);
						Log.e("mapType", mapType);
						*/
						String mapType = key[1].substring(1, key[1].indexOf(")"));
						Log.e("mapType", mapType);
						String typeValue = key[1].substring(key[1].indexOf(")")+1);
						Log.e("typeValue", typeValue);
						
						int i = key[1].indexOf(")"); // 시작값은 -1
						
						if (mapType.equals("Integer")) {
							int value = Integer.parseInt(typeValue);
							data.put(key[0], value);
							
							//long형 변환
						} else if (mapType.equals("Long")) {
							long value = Long.parseLong(typeValue);
							data.put(key[0], value);
							
						} else if (mapType.equals("Boolean")) {
							boolean value ;
							if (typeValue.equals("true")) {
								 value = true;
							} else {
								 value = false;
							}
							data.put(key[0], value);
	
						} else if (mapType.equals("String")) {
							String value = typeValue;
							data.put(key[0], value);
	
						} else if (mapType.equals("ArrayList")) {
							String temp = typeValue.substring(1, typeValue.length()-1);
							String[] ArrayData = temp.split(",");
							ArrayList<Integer> value = new ArrayList<Integer>();
							
							for (int j = 0; j < ArrayData.length; j++) {
								if (!ArrayData[j].equals("") && ArrayData[j] != null) {
									 value.add( Integer.parseInt(ArrayData[j].replace(" ", "")));									
								}
							}
							data.put(key[0], value);
						}
					}
				//sb.append(readLine + "\n");
			}
			br.close();
			fis.close();
			//String hashmapData = sb.toString();

			//Toast.makeText(mContext, "파일에서 데이터 불러오기 성공", Toast.LENGTH_SHORT).show();
			
		} catch (FileNotFoundException e) {
			
			//Toast.makeText(mContext, "최초 데이터 만들기", Toast.LENGTH_SHORT).show();
			// e.printStackTrace();
			Log.e("Exception", "FileNotFoundException");
			// 없으면, 초기값으로 지정한다.
			this.data = new HashMap<String, Object>();
			data.put("LevelCharacter", 1); // 값은 임시 넣음. Default값은 0임
			data.put("LevelFire", 1);
			data.put("LevelWind", 1);
			data.put("LevelCloud", 1);
			data.put("LevelDivine", 0);
			data.put("LevelEarth", 0);
			data.put("LevelMirror", 0);
			data.put("SphereNumber", 3); // 기본3개(추가 구매 시작값은 0부터) + 구매갯수
			data.put("Exp", 7777L); // 값은 임시 넣음. Default값은 0임
			data.put("Gold", 0L);
			data.put("Point", 654897L); // 값은 임시 넣음. Default값은 0임
			data.put("HistoryWin", 77); // 값은 임시 넣음. Default값은 0임
			data.put("HistoryLose", 44); // 값은 임시 넣음. Default값은 0임
			data.put("ReceivedBroomstick", 0); // Default값은 6임
			data.put("broomstickRenewalTimeData", 1999999999999L); // 빗자루 소모된 시점(시간)
			
			// 최종적으로는 1일 1회만 가능하게 해야됨.
			// 원래 DB로 넣어야됨.
			data.put("daily", 1); // 출석 일수

			ArrayList<Integer> emoticonArray = new ArrayList<Integer>();
			emoticonArray.add(101);
			emoticonArray.add(102);
			emoticonArray.add(103);
			data.put("Emoticon", emoticonArray);

			//
			// 쿨타임

			// 친구목록을 불러온다
			// 친구아이디별 1)초대, 2)빗자루 쿨타임(최근 발송 시간)
			data.put("Pester", true);
			data.put("SendBroomstick", true);

			//
			// 설정
			data.put("OptionBGM", true);
			data.put("OptionSFX", true);
			data.put("OptionRefuseInvitation", false);
			data.put("OptionReceiveBloomstick", true);
			data.put("OptionShowMyPicture", true);
			data.put("OptionNoviceGuide", false);
			

			

			updateUserData(data);
	
		} catch (UnsupportedEncodingException e) {
			Log.e("Exception", "UnsupportedEncodingException");
		} catch (IOException e) {
			Log.e("Exception", "IOException");
		}
	}

	//
	// kPath 파일명으로 파일저장
	private void updateUserData(final HashMap<String, Object> userData) {

		final Set<Entry<String, Object>> set = userData.entrySet();
		if (set.equals("")) {
			Log.e("updateUserData", "UserData 내용이 없습니다.");
		}

		CCDirector.sharedDirector().getActivity().runOnUiThread(new Runnable() {
			public void run() {
				android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
				try {
					String xml = "";
					Set mapKey = userData.keySet();
					String[] splitLine = mapKey.toString().substring(1,mapKey.toString().length()-1).split(", ");
					//Log.e("mapKey", mapKey.toString().substring(1,mapKey.toString().length()-1));
					for (int i = 0; i < userData.size(); i++) {

						// Log.e("containsKey(splitLine[i])"+i,splitLine[i]+":"+userData.containsKey(splitLine[i]));
						if (userData.containsKey(splitLine[i])) {
							String type = "";
							if (userData.get(splitLine[i].toString()) instanceof String) {
								type = "String";
							} else if (userData.get(splitLine[i].toString()) instanceof ArrayList) {
								type = "ArrayList";
							} else if (userData.get(splitLine[i].toString()) instanceof Integer) {
								type = "Integer";
							} else if (userData.get(splitLine[i].toString()) instanceof Long) {
								type = "Long";
							} else if (userData.get(splitLine[i].toString()) instanceof Boolean) {
								type = "Boolean";
							}
							xml += splitLine[i] + "=(" + type + ")"
									+ userData.get(splitLine[i]).toString()
									+ "\r\n";
							// xml += "\r\n" + splitLine[i] + "="	+ userData.get(splitLine[i]).toString();
						}
					}
					xml += "\r\n";
					xml = xml.replace("\r\n\r\n","");
					//xml = xml.replace (" ","");
					//Toast.makeText(mContext, "** xml **\r\n"+xml, Toast.LENGTH_LONG).show();
					
					// in memory
					FileOutputStream fos = mContext.openFileOutput(kPath,Context.MODE_PRIVATE);
					OutputStreamWriter osw = new OutputStreamWriter(fos);
					//osw.write(set.toString());
					osw.write(xml);
					osw.flush();
					osw.close();
					fos.close();
					//Toast.makeText(mContext, "write success!!",Toast.LENGTH_SHORT).show();
				} catch (Exception e) {
					//Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
				}
			}
		});
		//Toast.makeText(mContext, "UserData 파일로 저장 완료 \r\n" + set.toString(),Toast.LENGTH_LONG).show();
	}
	// updateUserData() end

	// #pragma mark - data methods general
	// 이모티콘 쓸때는 한번더 형변환 해야됨. ArrayList --> int로
	private int getByKey(String key) {
		return (Integer) this.data.get(key);
	}

	private void setByKey(String key, int value) {
		this.data.put(key, value);
		this.updateUserData(this.data);
	}
	
	private long getLongByKey(String key) {
		return (Long)this.data.get(key);
	}

	private void setLongByKey(String key, long value) {
		this.data.put(key, value);
		this.updateUserData(this.data);
	}

	private boolean getBoolByKey(String key) {
		return (Boolean) this.data.get(key);
	}

	private void setBoolByKey(String key, boolean value) {
		//Log.e(key + "-before", "" + data.put(key, (Boolean)value));
		this.data.put(key, (Boolean)value);
		//Log.e(key+ "-after", "" + data.put(key, (Boolean)value));
		this.updateUserData(this.data);
	}
	
	// #pragma mark - data methods
	public int getLevelByCategory(String keyString) {
		return (Integer) this.getByKey(keyString);
	}

	private void setLevelByCategory(String keyString, int level) {
		this.setByKey(keyString, level);
	}

	public void levelUpByCategory(String keyString) {
		int level = this.getLevelByCategory(keyString) + 1;
		this.setLevelByCategory(keyString, level);
	}

	public int getSphereNumber() {
		return this.getByKey("SphereNumber");
	}

	public void setSphereNumber(int number) {
		this.setByKey("SphereNumber", number);
	}

	public void increaseSphere() {
		int sphereNumber = this.getSphereNumber() + 1;
		this.setSphereNumber(sphereNumber);
	}

	public long getExp() {
		return (Long) this.getLongByKey("Exp");
	}

	public void setExp(long number) {
		this.setLongByKey("Exp", number);
	}

	long nextLevelExp = 5000; // temporary

	public void addExp(long number) {
		// 할일 : 레벨업경험치에 따른 레벨업
		long exp = this.getExp() + number;
		exp %= nextLevelExp;
		this.setExp(exp);
	}

	public long getGold() {
		return this.getLongByKey("Gold");
	}

	public void setGold(long gold) {
		this.setLongByKey("Gold", gold);
	}

	public void addGold(long deltaGold) {
		long gold = this.getGold() + deltaGold;
		this.setGold(gold);
	}

	//
	// false : 골드부족
	// true : 정상처리
	public boolean reduceGold(long deltaGold) {
		long gold = this.getGold() - deltaGold;
		if (gold >= 0) {
			this.setGold(gold);
			return true;
		} else {
			return false;
		}
	}

	public long getPoint() {
		return this.getLongByKey("Point");
	}

	public void setPoint(long point) {
		this.setLongByKey("Point", point);
	}

	public void addPoint(long deltaPoint) {
		long point = this.getPoint() + deltaPoint;
		this.setPoint(point);
	}

	public void reducePoint(long deltaPoint) {
		long point = this.getPoint() - deltaPoint;
		point = (long) CGPoint.clampf(point, 0, point);
		this.setPoint(point);
	}

	//
	// return -1 : 오류
	// 양의값 : 정상처리
	public int getHistory(int result) {
		if (result < 0 || result > 1)
			return -1;

		String keyString = result == kResultWin ? "HistoryWin" : "HistoryLose";
		return this.getByKey(keyString);
	}

	public int increaseHistory(int result) {
		if (result < 0 || result > 1)
			return -1;

		String keyString = result == kResultWin ? "HistoryWin" : "HistoryLose";
		this.setByKey(keyString, 1);
		return 0;
	}

	public int getBroomstick() {
		return this.getByKey("ReceivedBroomstick");
	}

	public void setBroomstick(int number) {
		if (number <= homeMaxBroomstick && number >= 0) {
			this.setByKey("ReceivedBroomstick", number);
		} else {
			CCDirector.sharedDirector().getActivity().runOnUiThread(new Runnable() {
						public void run() {
							android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
						}
					});
		}
	}

	public void addBroomstick(int deltaNumber) {
		int broomstick = this.getBroomstick() + deltaNumber;
			this.setBroomstick(broomstick);
	}
	
	public void myBroomstick() {
		int broomstick = this.getBroomstick() -1;
		this.setBroomstick(broomstick);
	}
	
	public ArrayList<Integer> getEmoticons() {
		return (ArrayList<Integer>) this.data.get("Emoticon");
	}

	public void addEmoticon(int emoticonId) {
		ArrayList<Integer> emoticons = getEmoticons();
		emoticons.add(emoticonId);
		this.data.put("Emoticon", emoticons);
		this.updateUserData(data);
	}
	
	// 친구에게 조르기
	public boolean getPester() {
		return this.getBoolByKey("Pester");
	}
	
	public void setPester() {
		boolean state = this.getBoolByKey("Pester");
		this.setBoolByKey("Pester", !state);
	}
	
	// 친구에게 지팡이 보내기
	public boolean getSendBroomstick() {
		return this.getBoolByKey("SendBroomstick");
	}

	public void setSendBroomstick() {
		boolean state = this.getBoolByKey("SendBroomstick");
		this.setBoolByKey("SendBroomstick", !state);
	}
	
	// Options
	public boolean getOption(int type) {
		String keyString = "OptionBGM,OptionSFX,OptionRefuseInvitation,OptionReceiveBloomstick,OptionShowMyPicture,OptionNoviceGuide";
		String[] keyArray = keyString.split(",");
		return this.getBoolByKey(keyArray[type]);
	}
	
	// 옵션값을 토글시킨다. YES -> NO, NO -> YES
	public void toggleOption(int type) {
		String keyString = "OptionBGM,OptionSFX,OptionRefuseInvitation,OptionReceiveBloomstick,OptionShowMyPicture,OptionNoviceGuide";
		String[] keyArray = keyString.split(",");
		boolean state = this.getOption(type);
		this.setBoolByKey(keyArray[type], !state);
	}

	public int getTimeMaxBroomstick() {
		return timeMaxBroomstick;
	}
	
	public long getBroomstickRenewalTimeData() {
		return this.getLongByKey("broomstickRenewalTimeData");
	}
	
	public void setBroomstickRenewalTimeData(long value) {
		this.setLongByKey("broomstickRenewalTimeData", value);
	}
	
//	public void addBroomstickRenewalTimeData() {
//		this.setBroomstickRenewalTimeData(this.getBroomstickRenewalTimeData() + this.broomstickRenewalTime);
//		this.addBroomstick(1);
//	}
	
	public int getDailyCount() {
		return this.getByKey("daily");
	}

	public void setDailyCount(int dailyCount) {
		this.setByKey("daily", dailyCount);
	}
	
}