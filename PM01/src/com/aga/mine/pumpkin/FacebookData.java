package com.aga.mine.pumpkin;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.cocos2d.nodes.CCDirector;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.facebook.Session;
import com.sromku.simple.fb.entities.Profile;


public class FacebookData {

	private boolean login = false;
	private Session faceSession = null;
	private String accessToken = "";
	private Profile userInfo = null;
	private Bitmap userPhoto = null;
	private List<Profile> friendsInfo = new ArrayList<Profile>();
	private List<GameScore> gameScore = new ArrayList<GameScore>();
	private List<GameScore> notAPlayers = new ArrayList<GameScore>();
	Map<String, String> data = new HashMap<String, String>();
	int ranking = 0;
	
//	private long requestID = (long) (Math.random() * 99L);  //test용 
	private String recipientID = null;
	
	
	private static FacebookData facebookData;

	public static synchronized FacebookData getinstance() {
		if (facebookData == null)
			facebookData = new FacebookData();
		return facebookData;
	}
	
	public static void initialize() {
		facebookData = null;
	}
	
	public String getDBData(String key) {
		return data.get(key); 
	}
	
	public void modDBData(String key, String value) {
		Log.e("FacebookData / modDBData", "key : " + key + ", value : " + value);
		if (key == null || value == null || key.equals("") || value.equals("")) {
			return;
		}
		data.put(key, value);
		DataFilter.setUserDBData(data);
	}
	
	
	public void modDBData(Map<String, String> db) {
		if (db == null) {
			return;
		}
		Collection k = db .keySet();
		Iterator itr = k.iterator();
			while(itr.hasNext()){
				String key = (String) itr.next();
				data.put(key, db.get(key));
		}
		DataFilter.setUserDBData(data);
	}
	
	public void setDBData(String data) {
		Log.e("FacebookData", "setDBData");
		setDBData(DataFilter.userDBFilter(data));
	}

	public void setDBData(String[] value) {
		String keyStr = 
				"FacebookId,LevelCharacter,SphereNumber,Exp,Gold,Point," +
				"HistoryWin,HistoryLose,ReceivedBroomstick," +
				"LevelFire,LevelWind,LevelCloud,LevelDivine,LevelEarth,LevelMirror," +
				"Emoticons,InviteNumber";
		String[] keyArray = keyStr.split(",");
		int count = 0;
		HashMap<String, String> tempData = new HashMap<String, String>();
		for (String key : keyArray) {
			tempData.put(key, value[count+1]);
			count++;
		}
		setDBData(tempData);
	}
	
	public void setDBData(HashMap<String, String> data) {
		this.data = data;
	}
	
	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public Profile getUserInfo() {
		return userInfo;
	}
	
	BufferedInputStream buf;
	public void setUserInfo(Profile userInfo) {
		this.userInfo = userInfo;
		//userPhoto = getBitmapFromURL("https://graph.facebook.com/" + userInfo.getUsername() +"/picture");
		userPhoto = BitmapFactory.decodeResource(MainApplication.getInstance().getActivity().getResources(), R.drawable.ic_launcher);
		
		// DB 정보 받아오기(checkUserDBData()하여 없는 ID이면 새로 생성)
		setDBData(DataFilter.checkUserDBData(userInfo.getId()));		
		if (userPhoto != null && userPhoto.getRowBytes() < 100) {
			try {	
				AssetManager am = CCDirector.sharedDirector().getActivity().getResources().getAssets();
				buf = new BufferedInputStream(am.open("noPicture.png"));
				userPhoto = BitmapFactory.decodeStream(buf);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	


	public List<Profile> getFriendsInfo() {
		return friendsInfo;
	}

	public void setFriendsInfo(List<Profile> friendsInfo) {
		this.friendsInfo = friendsInfo;
	}

	public Bitmap getUserPhoto() {
//		if (userPhoto != null && userPhoto.getRowBytes() > 100) {
			return userPhoto;	
//		}
//		return null;
	}
	
	public boolean isLogin() {
		return login;
	}

	public void setLogin(boolean login) {
		this.login = login;
	}
	
	public Session getFaceSession() {
		return faceSession;
	}

	public void setFaceSession(Session faceSession) {
		this.faceSession = faceSession;
	}

	public Bitmap getBitmapFromURL(String src) {
	    try {
	        URL url = new URL(src);
	        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	        connection.setDoInput(true);
	        connection.connect();
	        InputStream input = connection.getInputStream();
	        Bitmap myBitmap = BitmapFactory.decodeStream(input);
	        return myBitmap;
	    } catch (IOException e) {
	        e.printStackTrace();
	        return null;
	    }
	}
	
	private int count = 0;
	public boolean facebookReady(int completeValue) {
		count += completeValue;
		if (count % 100 > 0 && count > 100) 
			return true;
		return false;
	}

	public String getRecipientID() {
		return recipientID;
	}

	public void setRecipientID(String recipientID) {
		this.recipientID = recipientID;
	}

	public long getRequestID(String recipientID) {
//		MainApplication.getInstance().getActivity().sendInvite(recipientID, "우편물 발송", null);
		return  (long) (100 * Math.random()) + System.currentTimeMillis() * 100;
//		return  (long) (Math.random() * 9223372036854775807L);  //test용
	}

	public List<GameScore> getGameScore() {
		return gameScore;
	}

	public void setGameScore(List<GameScore> gameScore) {
		this.gameScore = gameScore;
	}

	public List<GameScore> getNotAPlayers() {
		return notAPlayers;
	}

	public void setNotAPlayers(List<GameScore> notAPlayers) {
		this.notAPlayers = notAPlayers;
	}

	
}
