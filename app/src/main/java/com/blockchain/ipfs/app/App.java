package com.blockchain.ipfs.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatDelegate;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.blockchain.ipfs.component.InitializeService;
import com.blockchain.ipfs.di.component.AppComponent;
import com.blockchain.ipfs.di.module.AppModule;
import com.blockchain.ipfs.di.module.HttpModule;
import com.blockchain.ipfs.component.InitializeService;
import com.blockchain.ipfs.di.component.AppComponent;
import com.blockchain.ipfs.di.component.DaggerAppComponent;
import com.blockchain.ipfs.di.module.AppModule;
import com.blockchain.ipfs.di.module.HttpModule;
import com.blockchain.ipfs.model.bean.DaoMaster;
import com.blockchain.ipfs.model.bean.DaoSession;
import com.blockchain.ipfs.component.InitializeService;
import com.blockchain.ipfs.di.component.AppComponent;
import com.blockchain.ipfs.di.module.AppModule;
import com.blockchain.ipfs.di.module.HttpModule;
import com.blockchain.ipfs.model.db.GreenDaoHelper;

import org.greenrobot.greendao.database.Database;

import java.util.HashSet;
import java.util.Set;

//import io.realm.Realm;

/**
 * Created by codeest on 2016/8/2.
 */
public class App extends Application {

    private static App instance;
    public static AppComponent appComponent;

    // SQLITE DB名字
    private static final String SQLITE_DB_NAME = "sqlite_ipfs_db_20180614";

    private Set<Activity> allActivities;

    public static int SCREEN_WIDTH = -1;
    public static int SCREEN_HEIGHT = -1;
    public static float DIMEN_RATE = -1.0F;
    public static int DIMEN_DPI = -1;

    public DaoSession mDaoSession;
    public boolean isIPFSDaemonRunning = false;

    public static String getMainWalletAddress() {
        return mainWalletAddress;
    }

    public static void setMainWalletAddress(String _mainWalletAddress) {
        mainWalletAddress = _mainWalletAddress;
    }

    private static String mainWalletAddress = null;

    public static synchronized App getInstance() {
        return instance;
    }

    static {
        AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_NO);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        //初始化屏幕宽高
        getScreenSize();

        //初始化数据库
//        Realm.init(getApplicationContext());
        GreenDaoHelper greenDaoHelper = new GreenDaoHelper();
        //初始化sqlite db
        initSQLiteDatabase();

        //在子线程中完成其他初始化
        InitializeService.start(this);
    }


    /**
     * 初始化数据库
     * TODO：更新数据库文件后在重新读入
     */
    public void initSQLiteDatabase() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, SQLITE_DB_NAME);
        Database database = helper.getWritableDb();
        mDaoSession = new DaoMaster(database).newSession();
        //在ipfs节点启动后，更新节点数据
//        ImageDao.updateLocalData(getApplication(), mDaoSession);
//        DownloadUtils.init(mDaoSession.getBeautyPhotoInfoDao());
    }

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public void addActivity(Activity act) {
        if (allActivities == null) {
            allActivities = new HashSet<>();
        }
        allActivities.add(act);
    }

    public void removeActivity(Activity act) {
        if (allActivities != null) {
            allActivities.remove(act);
        }
    }

    public void exitApp() {
        if (allActivities != null) {
            synchronized (allActivities) {
                for (Activity act : allActivities) {
                    act.finish();
                }
            }
        }
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

    public void getScreenSize() {
        WindowManager windowManager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        Display display = windowManager.getDefaultDisplay();
        display.getMetrics(dm);
        DIMEN_RATE = dm.density / 1.0F;
        DIMEN_DPI = dm.densityDpi;
        SCREEN_WIDTH = dm.widthPixels;
        SCREEN_HEIGHT = dm.heightPixels;
        if (SCREEN_WIDTH > SCREEN_HEIGHT) {
            int t = SCREEN_HEIGHT;
            SCREEN_HEIGHT = SCREEN_WIDTH;
            SCREEN_WIDTH = t;
        }
    }

    public static AppComponent getAppComponent() {
        if (appComponent == null) {
            appComponent = DaggerAppComponent.builder()
                    .appModule(new AppModule(instance))
                    .httpModule(new HttpModule())
                    .build();
        }
        return appComponent;
    }
}
