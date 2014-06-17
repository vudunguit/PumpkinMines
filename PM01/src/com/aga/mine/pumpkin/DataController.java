package com.aga.mine.pumpkin;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Vector;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.os.AsyncTask;
import android.util.Log;

// requestID는 facebook 알림글 번호로 넣을 것.
public class DataController extends AsyncTask<String, Void, String>{
	
	String[] commands = {
			"RequestModeIsServerOk", 
			"RequestModeRead", 
			"RequestModeUpdate", 
			"RequestModeAddScore", 
			"RequestModeMailBoxAdd", 
			"RequestModeMailBoxRead", 
			"RequestModeMailBoxDelete", 
			"RequestModeGetInDBUserList", 
			"RequestModeGetWeeklyLeftTime", 
			"RequestModeDailyCheck"
			};
	
	String[] keys = {
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
	
	public DataController() {
	}

	private String sendData(String param) {
		HttpPost request = makeHttpPost(param, "http://211.110.139.226/index.php");
		HttpClient client = new DefaultHttpClient();
		HttpParams params = client.getParams();
		HttpConnectionParams.setConnectionTimeout(params, 6000);
		HttpConnectionParams.setSoTimeout(params, 6000);
		ResponseHandler<String> reshandler = new BasicResponseHandler();
		String result = null;
		try {
			result = client.execute(request, reshandler);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	private HttpPost makeHttpPost(String param, String url) {
		HttpPost request = new HttpPost(url);
		Vector<NameValuePair> nameValue = new Vector<NameValuePair>();
		
		String[] paramSplitStr1 = param.split("\\*");
		for (String string : paramSplitStr1) {
			String[] splitStr2 = new String[2];
			if (string.indexOf("23") == 0 || string.indexOf("16") == 0) {
				splitStr2[0] = string.substring(0, 2);
				splitStr2[1] = string.substring(3);
			} else {
				splitStr2 = string.split(",");	
			}
//			for (String string2 : splitStr2) {
//				Log.e("DataController", "makeHttpPost [" + string2 + "]");			
//			}
			nameValue.add(new BasicNameValuePair(
					keys[Integer.parseInt(splitStr2[0])], splitStr2[1]));
		}
		
		request.setEntity(makeEntity(nameValue));
		return request;
	}

	private HttpEntity makeEntity(Vector<NameValuePair> nameValue) {
		HttpEntity result = null;
		try {
			result = new UrlEncodedFormEntity(nameValue);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	@Override
	protected String doInBackground(String... params) {
		String result = sendData(params[0]);
		return result;
	}
	
	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
	}

}