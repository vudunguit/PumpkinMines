package com.aga.mine.pumpkin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.opengl.CCGLSurfaceView;
import org.cocos2d.sound.SoundEngine;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.ColorDrawable;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.aga.mine.pages2.GameData;
import com.aga.mine.view.EmoticonListAdapter;
import com.aga.mine.view.FriendListAdapter;
import com.aga.mine.view.InviteListAdapter;
import com.aga.mine.view.MailItem;
import com.aga.mine.view.MailListAdapter;
import com.aga.mine.view.MatchListAdapter;
import com.sromku.simple.fb.Permission;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.entities.Profile;
import com.sromku.simple.fb.listeners.OnFriendsListener;
import com.sromku.simple.fb.listeners.OnInviteListener;
import com.sromku.simple.fb.listeners.OnLoginListener;
import com.sromku.simple.fb.listeners.OnLogoutListener;
import com.sromku.simple.fb.listeners.OnProfileListener;

public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";

    public SimpleFacebook mSimpleFacebook;
    private ProgressDialog mProgress;

    private CCGLSurfaceView mGLSurfaceView;
    CCDirector director = CCDirector.sharedDirector();
    public Vibrator mVibrator;
    
    
    private RelativeLayout main;
    public ListView mListView;
    public GridView mGridView;
    public EmoticonListAdapter mEmoticonAdapter;

    private int nMargin = 0;
    private float frameCenterPosition = 0.525f;
    private float frameGeneralWidth = 640;
    // 홈 랭크 리스트뷰 위치 (화면중심 높이 52.5% 위치)
    private float frameGeneralHeight = 889;
    private float rankListFrameLeftPosition = 90;
    private float rankListFrameTopPosition = 364;
    private float rankListFrameRightPosition = 100;
    private float rankListFrameBottomPosition = 186;
    
    // 우편함 리스트뷰 위치 (화면중심 높이 50% 기준)
    private int verticalShiftPosition = 18;
    private float itemListBackgroundHeight = 596;
    private float itemListMarginLeft = 106; // 107
    private float itemListMarginTop = 131;
    private float itemListMarginRight = 106; // 107
    private float itemListMarginBottom = 12;
    
    // 친구초대 리스트뷰 위치 (화면중심 높이 52.5% 위치)
    private float inviteListBackgroundHeight = 580;
    private float inviteListMarginLeft = 108;
    private float inviteListMarginTop = 60;
    private float inviteListMarginRight = 108;
    private float inviteListMarginBottom = 216;
    
    // 이모티콘 리스트뷰 위치 (화면중심 높이 52.5% 위치)
    private float emoticonListBackgroundHeight = 889;
    private float emoticonListMarginLeft = 95;
    private float emoticonListMarginTop = 204;
    private float emoticonListMarginRight = 106;
    private float emoticonListMarginBottom = 186;
    //
    // 초대매치 리스트뷰 위치 (화면중심 높이 52.5% 위치)
    private float matchListBackgroundHeight = 889;
    private float matchListMarginLeft = 88;
    private float matchListMarginTop = 440;
    private float matchListMarginRight = 100;
    private float matchListMarginBottom = 186;
    
    public Handler mHandler = new Handler(Looper.getMainLooper()) {
		@Override
		public void handleMessage(Message msg) {
			// 랭킹에 들어가는 페이스북 이미지 크기 및 리스트뷰 오른쪽 2번째 이미지(선물상자)의 rightMargin이 해상도 별로 변경됩니다.
			// 해상도에 맞게 이미지는 커지고 마진은 작아져야 됩니다.(scale값 적용) 
			float scale = main.getWidth() / frameGeneralWidth ;
			float height = main.getHeight();
			
			switch(msg.what) {
			case Constant.MSG_DISPLAY_RANKLIST:
				FriendListAdapter adapter = new FriendListAdapter(MainActivity.this);
				mListView.setAdapter(adapter);
				mListView.setCacheColorHint(Color.alpha(0));
				RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
				//mListView.setDivider(getResources().getDrawable(R.drawable.friend_list_divider));
				//mListView.setDividerHeight(10);
				// assets의 frameGeneral-hd.png, frameMatching-hd.png파일의 width, height을 받아와서 작업을 해야되는데
				// inputstream을 해야되고 핸들러에서 작업 하기에는 문제가 있어 이미지 크기를 상수로 정의하여 작업했습니다.
				params.setMargins(
						(int) (rankListFrameLeftPosition * scale) + nMargin, 
						(int) (height * (1 - frameCenterPosition) + (rankListFrameTopPosition - frameGeneralHeight * 0.5f) * scale) + nMargin, 
						(int) (rankListFrameRightPosition * scale) + nMargin, 
						(int) (height * frameCenterPosition + (rankListFrameBottomPosition - frameGeneralHeight * 0.5f) * scale) + nMargin);
				//MainActivity.this.addContentView(listview, params);
				
				if(mListView.getParent() != null) {
					main.removeView(mListView);
					main.addView(mListView, params);
				} else {
					main.addView(mListView, params);
				}
				break;
				
			case Constant.MSG_DISPLAY_ITEMLIST:
				int tab = msg.arg1;
				ArrayList<MailItem> broomstickList = (ArrayList<MailItem>) msg.obj;
				MailListAdapter mailAdapter = new MailListAdapter(MainActivity.this, broomstickList, tab);
				mListView.setAdapter(mailAdapter);
				RelativeLayout.LayoutParams itemParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
				itemParams.setMargins(
						(int) (itemListMarginLeft * scale) + nMargin, 
						(int) (height / 2 + (itemListMarginTop - itemListBackgroundHeight * 0.5f - verticalShiftPosition) * scale) + nMargin, 
						(int) (itemListMarginRight * scale) + nMargin, 
						(int) (height / 2 + (itemListMarginBottom - itemListBackgroundHeight * 0.5f + verticalShiftPosition) * scale) + nMargin);
				if(mListView.getParent() != null) {
					main.removeView(mListView);
					main.addView(mListView, itemParams);
				} else {
					main.addView(mListView, itemParams);
				}
				break;
				
			case Constant.MSG_DISPLAY_INVITELIST:
				InviteListAdapter inviteAdapter = new InviteListAdapter(MainActivity.this);
				mListView.setAdapter(inviteAdapter);
//				mListView.setDivider(null);
				RelativeLayout.LayoutParams inviteParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
				inviteParams.setMargins(
						(int) (inviteListMarginLeft * scale) + nMargin, 
						(int) (height * (1 - frameCenterPosition) + (inviteListMarginTop - inviteListBackgroundHeight * 0.5f) * scale) + nMargin, 
						(int) (inviteListMarginRight * scale) + nMargin, 
						(int) (height * frameCenterPosition + (inviteListMarginBottom - inviteListBackgroundHeight * 0.5f) * scale) + nMargin);
				if(mListView.getParent() != null) {
					main.removeView(mListView);
					main.addView(mListView, inviteParams);
				} else {
					main.addView(mListView, inviteParams);
				}
				break;
				
			case Constant.MSG_DISPLAY_EMOTICONLIST:
				mEmoticonAdapter = new EmoticonListAdapter(MainActivity.this);
				mGridView.setAdapter(mEmoticonAdapter);
				RelativeLayout.LayoutParams emoticonParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
				emoticonParams.setMargins(
						(int) (emoticonListMarginLeft * scale) + nMargin, 
						(int) (height * (1 - frameCenterPosition) + (emoticonListMarginTop - emoticonListBackgroundHeight * 0.5f) * scale) + nMargin, 
						(int) (emoticonListMarginRight * scale) + nMargin, 
						(int) (height * frameCenterPosition + (emoticonListMarginBottom - emoticonListBackgroundHeight * 0.5f) * scale) + nMargin);
				if(mGridView.getParent() != null) {
					main.removeView(mGridView);
					main.addView(mGridView, emoticonParams);
				} else {
					main.addView(mGridView, emoticonParams);
				}
				break;
				
			case Constant.MSG_DISPLAY_MATCHLIST:
				ArrayList<GameScore> matchList = (ArrayList<GameScore>) FacebookData.getinstance().getGameScore();
				MatchListAdapter matchAdapter = new MatchListAdapter(MainActivity.this, matchList); // 수정중
//				InviteListAapter matchAdapter = new InviteListAapter(MainActivity.this);
				mListView.setAdapter(matchAdapter);
				RelativeLayout.LayoutParams matchParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
				matchParams.setMargins(
						(int) (matchListMarginLeft * scale) + nMargin, 
						(int) (height * (1 - frameCenterPosition) + (matchListMarginTop - matchListBackgroundHeight * 0.5f) * scale) + nMargin, 
						(int) (matchListMarginRight * scale) + nMargin, 
						(int) (height * frameCenterPosition + (matchListMarginBottom - matchListBackgroundHeight * 0.5f) * scale) + nMargin);
				if(mListView.getParent() != null) {
					main.removeView(mListView);
					main.addView(mListView, matchParams);
				} else {
					main.addView(mListView, matchParams);
				}
				break;
				
			case Constant.MSG_HIDE_SCROLLVIEW:
				main.removeView(mListView);
				main.removeView(mGridView);
				break;
				
			case Constant.MSG_DISPLAY_POPUP:
				int emoticonID = msg.arg1;
				
				Intent intent = new Intent(MainActivity.this, PopupActivity.class);
				intent.putExtra("emoticonID", emoticonID);
				startActivityForResult(intent, 111);

				break;
			}
		}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("MainActivity", "onCreate");

        MainApplication.getInstance().setActivity(this);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        mGLSurfaceView = new CCGLSurfaceView(this);
        mGLSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        mGLSurfaceView.getHolder().setFormat(PixelFormat.RGBA_8888);
        //setContentView(mGLSurfaceView);
        setContentView(R.layout.mainactivity);
        main = (RelativeLayout) findViewById(R.id.main);
        main.addView(mGLSurfaceView);
        mListView = new ListView(this);
        mGridView = new GridView(this);
        mGridView.setNumColumns(4);
        mGridView.setGravity(Gravity.CENTER);

        director.attachInView(mGLSurfaceView);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        director.setDeviceOrientation(CCDirector.kCCDeviceOrientationPortrait);

        setDisplayMetrics();
        CCDirector.sharedDirector().setDisplayFPS(false); // FPS 표시
        CCDirector.sharedDirector().setAnimationInterval(1.0f / 60);

		SoundEngine.sharedEngine().preloadSound(this, R.raw.bgm); // 배경음악
		SoundEngine.sharedEngine().preloadEffect(this, R.raw.click); // 클릭음
		SoundEngine.sharedEngine().preloadEffect(this, R.raw.buy); // 구입음
//		SoundEngine.sharedEngine().preloadEffect(this, R.raw.mushroom); // 게임 효과음 (버섯)
		mVibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		
        CCScene scene = Logo.scene();
        director.runWithScene(scene);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
        CCDirector.sharedDirector().onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSimpleFacebook = SimpleFacebook.getInstance(this);
        CCDirector.sharedDirector().onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
		SoundEngine.sharedEngine().realesAllEffects();
		SoundEngine.sharedEngine().realesAllSounds();
		SoundEngine.purgeSharedEngine();
        try {
            // 종료시 무조건 패배 (게임중이 아닐시 연결만 끊을 것)
            NetworkController.getInstance().sendRequestGameOver(0);
            NetworkController.getInstance().closeSocket();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.e("MainActivity", "mine 종료");
        new Process().killProcess(Process.myPid());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
        mSimpleFacebook.onActivityResult(this, requestCode, resultCode, data);
    }

    // Back-key 클릭시 프로그램 종료 코드
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	String exitStr = "Quit (now) ?";
		if (Locale.getDefault().getLanguage().toString().equals("ko"))
			exitStr = "종료하겠습니까?";
        switch (keyCode) {
        case KeyEvent.KEYCODE_BACK:
        	click();
            new AlertDialog.Builder(this).setTitle(R.string.app_name)
                        .setMessage(exitStr)
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                            	// 본Activity finish후 다른 Activity가 뜨는 걸 방지. finish();
                                moveTaskToBack(true);
                                // 해당 어플의 프로세스를 강제 Kill시킨다.
                                new Process().killProcess(Process.myPid());

                            }
                        })
                        .setNegativeButton("NO", null)
                        .show();
        }

        return true;
    }

    private void setDisplayMetrics() {
        DisplayMetrics metrics = this.getResources().getDisplayMetrics();
        int dh = metrics.heightPixels;
        int dw = metrics.widthPixels;
        int deviceWidth;
        int deviceHeight;

        if (dw < dh) {
            deviceWidth = dw;
            deviceHeight = dh;
        } else {
            deviceWidth = dh;
            deviceHeight = dw;
        }

        float WidthReset = 640f;
        float HeightReset = 1920 * 0.9f;

        if (deviceWidth != WidthReset) {
            deviceHeight = (int)(deviceHeight / (deviceWidth / WidthReset));
            deviceWidth = (int)WidthReset;
        } else if (deviceHeight > HeightReset) {
            deviceWidth = (int)(deviceWidth / (deviceHeight / HeightReset));
            deviceHeight = (int)HeightReset;
        }

        CCDirector.sharedDirector().setScreenSize(deviceWidth, deviceHeight);
    }
    
    //invite callback--------------------------------------------------------------------------
    public InviteCallback mInviteCallback;
    
    interface InviteCallback {
    	public void onInvited(List<String> invitedFriends, String requestId);
    }
    
    public void setInviteCallback(InviteCallback callback) {
    	Log.e("MainActivity", "Callback_2 - setInviteCallback");
    	mInviteCallback = callback;
    }
    //facebook setting--------------------------------------------------------------------------
    private void showDialog(String title, String message) {
        mProgress = ProgressDialog.show(this, title, message, true);
    }
    
    private void hideDialog() {
        if (mProgress != null) {
            mProgress.hide();
        }
    }
    
    public void loginFaceBook() {
        mSimpleFacebook.login(mOnLoginListener);
    }
    
    public void logoutFaceBook() {
    	mSimpleFacebook.logout(mLogoutListener);
    }
    
    public void getMyProfile() {
        mSimpleFacebook.getProfile(mOnProfileListener);
    }
    
    public void getFriendsInfo() {
        mSimpleFacebook.getFriends(mOnFriendsListener);
    }
    
    public void sendInvite(String friend, String message, String data) {
    	Log.e("MainActivity", "Callback_4 - sendInvite()");
    	Log.e("MainActivity", "친구초대를 제외한 나머지에서는 확인안누르고 되야함");
    	mSimpleFacebook.invite(friend, message, mOnInviteListener, data); // 이걸로 사용하는게 맞는지 모르겠네요.
    	// 실패(오류) - "요청 실패"라는 토스트
    	// 취소 - "요청 취소" 라는 토스트
    	// 성공시 resultURL을 받아서 그것으로 DB에 저장할때 씁니다. (혹시 이걸로 안쓸수도 있네요. 아이폰에서 전에 쓰던부분 주석 처리 해버렸네요. 더 찾아보겠습니다.)
    	// 성공시 그 친구 초대버튼 비활성화
    }
    
    // Login listener
    private OnLoginListener mOnLoginListener = new OnLoginListener() {

        @Override
        public void onFail(String reason) {
            Log.w(TAG, "Failed to login");
            //hideDialog();
        }

        @Override
        public void onException(Throwable throwable) {
            Log.e(TAG, "Bad thing happened", throwable);
            //hideDialog();
        }

        @Override
        public void onThinking() {
            // show progress bar or something to the user while login is happing
            //showDialog();
        }

        @Override
        public void onLogin() {
            // change the state of the button or do whatever you want
            //hideDialog();
            
            //go to Daily scene
            getMyProfile();
            getFriendsInfo();
        }

        @Override
        public void onNotAcceptingPermissions(Permission.Type type) {
            //hideDialog();
        }
    };
    

    private OnLogoutListener mLogoutListener = new OnLogoutListener() {
		
		@Override
		public void onFail(String reason) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onException(Throwable throwable) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onThinking() {
			showDialog("Please wait", "Logging out...");
			
		}
		
		@Override
		public void onLogout() {
			hideDialog();
			// clear facebook data and value and go to login scene
			FacebookData.initialize();
			GameData.share().isGuestMode = true;
			CCScene scene = Login.scene();
			director.replaceScene(scene);
		}
	};
	
    
    // listener for profile request
    final OnProfileListener mOnProfileListener = new OnProfileListener() {

        @Override
        public void onFail(String reason) {
            // insure that you are logged in before getting the profile
            Log.d(TAG, reason);
        }

        @Override
        public void onException(Throwable throwable) {
            Log.e(TAG, "Bad thing happened", throwable);
        }

        @Override
        public void onThinking() {
            // show progress bar or something to the user while fetching
            // profile
            Log.i(TAG, "Login...");
        }

        @Override
        public void onComplete(Profile profile) {
            //save my profile
            FacebookData.getinstance().setUserInfo(profile);
            Log.e("FacebookHelper", "setUserInfo");
            nextCallback(FacebookData.getinstance().facebookReady(1));
        }
    };
    
    // get friends listener
    private OnFriendsListener mOnFriendsListener = new OnFriendsListener() {

        @Override
        public void onFail(String reason) {
            hideDialog();
            Log.w(TAG, reason);
        }

        @Override
        public void onException(Throwable throwable) {
            hideDialog();
            Log.e(TAG, "Bad thing happened", throwable);
        }

        @Override
        public void onThinking() {
            showDialog("Please Wait", "Loading friends infomation...");
            Log.i(TAG, "Thinking...");
        }

        @Override
        public void onComplete(List<Profile> friends) {
            hideDialog();
            FacebookData.getinstance().setFriendsInfo(friends);
            Log.e("FacebookHelper", "setFriendsInfo");
            nextCallback(FacebookData.getinstance().facebookReady(100));
            
            //ToDo: go to Daily Scene
//          CCScene scene = Daily.scene();
//          director.replaceScene(scene);
        }
    };
    
    OnInviteListener mOnInviteListener = new OnInviteListener() {

        @Override
        public void onFail(String reason) {
            // insure that you are logged in before inviting
            Log.w(TAG, reason);
        }

        @Override
        public void onException(Throwable throwable) {
            Log.e(TAG, "Bad thing happened", throwable);
        }

        @Override
        public void onComplete(List<String> invitedFriends, String requestId) {
            //String msg = "Invitation was sent to " + invitedFriends.size() + " users, invite request=" + requestId;
            //ToDo:
//        	Log.e("MainActivity", "Callback_5 - onComplete()");
//        	for (String string : invitedFriends) {
//            	Log.e("MainActivity", "friends : " + string);
//			}
        	
    		if(mInviteCallback != null) {
    			Log.e("MainActivity", "Callback_6 - mInviteCallback != null");
    			mInviteCallback.onInvited(invitedFriends, requestId);
    		}
        }

        @Override
        public void onCancel() {
            Log.w(TAG, "cancled dialog");
        }

    };

    
    private void nextCallback(boolean facebookReady) {
        Log.e("FacebookHelper", "nextCallback");
        if (facebookReady) {
        	//ToDo: 성능개선: 화면을 멈추지 않고 데이터 로딩후 출석부로 이동
        	//1) 로딩바 표시
        	//2) 핸들러 send message
        	//3) 데이터 로딩 후 로딩바 해제
        	//4) 화면 이동
        	
        	// facebook에 접속후 id와 name을 받아와야 게임서버에 자신의 계정으로 접속 가능.
            NetworkController.getInstance();
        	
        	// 게임 모드 설정
        	Log.e("Main", "isGuestMode_B : " + GameData.share().isGuestMode);
			GameData.share().isGuestMode = false;
        	Log.e("Main", "isGuestMode_A : " + GameData.share().isGuestMode);
        	
        	// 게임스코어 받아오기
    		FacebookData.getinstance().setGameScore(DataFilter.getGameRank());
        	
    		if (GameData.share().getInitialConnection()) {
    			String gold = "3500"; // 최초 아이디 생성시 3500gold 우편함으로 선물
    			long requestID = (long) (Math.random() * 72036854775807L);  //facebook 알림글번호로 대체할 것
    			String recipientID = FacebookData.getinstance().getUserInfo().getId(); // 상점 이동 방식에 따른 ID 변경
    			String senderID = "1";
    			String data = 
    					"0,RequestModeMailBoxAdd*22," + requestID + "*1," + recipientID + "*19," + senderID + "*20,Gold*21," + gold;				
    			DataFilter.sendMail(data);
    			CCScene scene = GameGuide1.scene();
    			CCDirector.sharedDirector().replaceScene(scene);
			} else {
				DataFilter.dailyFilter(director, FacebookData.getinstance().getUserInfo().getId());
			}
        }
    }
    
    public void vibe() {
    	if (android.os.Build.VERSION.SDK_INT  >= android.os.Build.VERSION_CODES.HONEYCOMB) {
//    		if (mVibrator != null && mVibrator.hasVibrator()) {
        	if (mVibrator.hasVibrator()) {
        		mVibrator.vibrate(200);
    		} else {
    			Log.e(TAG, "진동기능이 제공되지 않습니다.");
//    			SoundEngine.sharedEngine().playEffect(this, R.raw.mushroom);
    		}
    	}
	}

    public void click() {
		SoundEngine.sharedEngine().playEffect(this, R.raw.click); // buttonClick
	}
}
