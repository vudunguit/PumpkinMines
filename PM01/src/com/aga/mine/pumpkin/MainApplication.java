package com.aga.mine.pumpkin;

import com.facebook.SessionDefaultAudience;
import com.sromku.simple.fb.Permission;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.SimpleFacebookConfiguration;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Handler;

public class MainApplication extends Application {
    //private static final String APP_ID = "625994234086470";
//    private static final String APP_ID = "1387999011424838";
//    private static final String APP_NAMESPACE = "sromkuapp";
    private static final String APP_ID = "584562878287984";
    private static final String APP_NAMESPACE = "comagaminelayers";
    
    private Context mContext;
    private MainActivity mActivity;
    
    private static MainApplication mApplication;
    
    public static MainApplication getInstance(){
        return mApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
        mContext = this.getApplicationContext();
        
        // initialize facebook configuration
        Permission[] permissions = new Permission[] { Permission.BASIC_INFO,
                                                     Permission.USER_CHECKINS,
                                                     Permission.USER_EVENTS,
                                                     Permission.USER_GROUPS,
                                                     Permission.USER_LIKES,
                                                     Permission.USER_PHOTOS,
                                                     Permission.USER_VIDEOS,
                                                     Permission.FRIENDS_EVENTS,
                                                     Permission.FRIENDS_PHOTOS,
                                                     Permission.FRIENDS_ABOUT_ME,
                                                     Permission.PUBLISH_STREAM };

        SimpleFacebookConfiguration configuration = new SimpleFacebookConfiguration.Builder().setAppId(APP_ID)
                    .setNamespace(APP_NAMESPACE)
                    .setPermissions(permissions)
                    .setDefaultAudience(SessionDefaultAudience.FRIENDS)
                    .setAskForAllPermissionsAtOnce(false)
                    .build();

        SimpleFacebook.setConfiguration(configuration);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH) 
    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }

    public void setActivity(MainActivity activity) {
        mActivity = activity;
    }
    
    public MainActivity getActivity() {
        return mActivity;
    }
    
    public Handler mHandler = new Handler() {
        
    };
}
