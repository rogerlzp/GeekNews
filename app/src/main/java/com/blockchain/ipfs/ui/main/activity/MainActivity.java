package com.blockchain.ipfs.ui.main.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.blockchain.ipfs.app.App;
import com.blockchain.ipfs.app.Constants;
import com.blockchain.ipfs.base.BaseActivity;
import com.blockchain.ipfs.base.contract.main.MainContract;
import com.blockchain.ipfs.component.RxBus;
import com.blockchain.ipfs.component.UpdateService;
import com.blockchain.ipfs.model.event.SearchEvent;
import com.blockchain.ipfs.ui.main.fragment.AboutFragment;
import com.blockchain.ipfs.ui.main.fragment.LikeFragment;
import com.blockchain.ipfs.ui.main.fragment.SettingFragment;
import com.blockchain.ipfs.ui.vtex.fragment.VtexMainFragment;
import com.blockchain.ipfs.ui.wallet.fragment.WalletFragment;
import com.blockchain.ipfs.ui.wechat.fragment.WechatMainFragment;
import com.blockchain.ipfs.ui.zhihu.fragment.ZhihuMainFragment;
import com.blockchain.ipfs.util.SystemUtil;
import com.blockchain.ipfs.R;
import com.blockchain.ipfs.app.App;
import com.blockchain.ipfs.app.Constants;
import com.blockchain.ipfs.base.BaseActivity;
import com.blockchain.ipfs.component.RxBus;
import com.blockchain.ipfs.component.UpdateService;
import com.blockchain.ipfs.model.event.SearchEvent;
import com.blockchain.ipfs.presenter.main.MainPresenter;
import com.blockchain.ipfs.base.contract.main.MainContract;
import com.blockchain.ipfs.ui.gank.fragment.GankMainFragment;
import com.blockchain.ipfs.ui.gold.fragment.GoldMainFragment;
import com.blockchain.ipfs.ui.ipfs.fragment.IpfsFragment;
import com.blockchain.ipfs.ui.main.fragment.AboutFragment;
import com.blockchain.ipfs.ui.main.fragment.LikeFragment;
import com.blockchain.ipfs.ui.main.fragment.SettingFragment;
import com.blockchain.ipfs.ui.vtex.fragment.VtexMainFragment;
import com.blockchain.ipfs.ui.wallet.fragment.WalletFragment;
import com.blockchain.ipfs.ui.wechat.fragment.WechatMainFragment;
import com.blockchain.ipfs.ui.zhihu.fragment.ZhihuMainFragment;
import com.blockchain.ipfs.util.SystemUtil;
import com.blockchain.ipfs.app.App;
import com.blockchain.ipfs.app.Constants;
import com.blockchain.ipfs.base.BaseActivity;
import com.blockchain.ipfs.base.contract.main.MainContract;
import com.blockchain.ipfs.component.RxBus;
import com.blockchain.ipfs.component.UpdateService;
import com.blockchain.ipfs.model.event.SearchEvent;
import com.blockchain.ipfs.ui.main.fragment.AboutFragment;
import com.blockchain.ipfs.ui.main.fragment.LikeFragment;
import com.blockchain.ipfs.ui.main.fragment.SettingFragment;
import com.blockchain.ipfs.ui.vtex.fragment.VtexMainFragment;
import com.blockchain.ipfs.ui.wallet.fragment.WalletFragment;
import com.blockchain.ipfs.ui.wechat.fragment.WechatMainFragment;
import com.blockchain.ipfs.ui.zhihu.fragment.ZhihuMainFragment;
import com.blockchain.ipfs.util.SystemUtil;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.tbruyelle.rxpermissions2.RxPermissions;

import butterknife.BindView;
import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created by codeest on 16/8/9.
 */

public class MainActivity extends BaseActivity<MainPresenter> implements MainContract.View {

    @BindView(R.id.drawer)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.navigation)
    NavigationView mNavigationView;
    @BindView(R.id.view_search)
    MaterialSearchView mSearchView;

    ZhihuMainFragment mZhihuFragment;
    GankMainFragment mGankFragment;
    WechatMainFragment mWechatFragment;
    GoldMainFragment mGoldFragment;
    VtexMainFragment mVtexFragment;
    LikeFragment mLikeFragment;
    SettingFragment mSettingFragment;
    AboutFragment mAboutFragment;

    IpfsFragment mIpfsFragment;
    WalletFragment mWalletFragment;

    MenuItem mLastMenuItem;
    MenuItem mSearchMenuItem;
    ActionBarDrawerToggle mDrawerToggle;

    private int hideFragment = Constants.TYPE_ZHIHU;
    private int showFragment = Constants.TYPE_ZHIHU;

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    /**
     * 由于recreate 需要特殊处理夜间模式
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            mPresenter.setNightModeState(false);
        } else {
            showFragment = mPresenter.getCurrentItem();
            hideFragment = Constants.TYPE_ZHIHU;
            showHideFragment(getTargetFragment(showFragment), getTargetFragment(hideFragment));
            mNavigationView.getMenu().findItem(R.id.drawer_zhihu).setChecked(false);
            mToolbar.setTitle(mNavigationView.getMenu().findItem(getCurrentItem(showFragment)).getTitle().toString());
            hideFragment = showFragment;
        }
    }

    @Override
    protected void initEventAndData() {
        setToolBar(mToolbar, "知乎日报");
        mZhihuFragment = new ZhihuMainFragment();
        mGankFragment = new GankMainFragment();
        mWalletFragment = new WalletFragment();
        mWechatFragment = new WechatMainFragment();
        mGoldFragment = new GoldMainFragment();
        mVtexFragment = new VtexMainFragment();
        mLikeFragment = new LikeFragment();
        mSettingFragment = new SettingFragment();
        mAboutFragment = new AboutFragment();
        mIpfsFragment = new IpfsFragment();
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawerToggle.syncState();
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mLastMenuItem = mNavigationView.getMenu().findItem(R.id.drawer_zhihu);
//        loadMultipleRootFragment(R.id.fl_main_content,0,mZhihuFragment,mWechatFragment,mGankFragment,mGoldFragment,mVtexFragment,mLikeFragment,mSettingFragment,mAboutFragment,mIpfsFragment,mWalletFragment);

        loadMultipleRootFragment(R.id.fl_main_content, 0, mZhihuFragment, mWechatFragment, mGankFragment, mGoldFragment, mVtexFragment, mLikeFragment, mSettingFragment, mAboutFragment, mIpfsFragment, mWalletFragment);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.drawer_zhihu:
                        showFragment = Constants.TYPE_ZHIHU;
                        mSearchMenuItem.setVisible(false);
                        break;
                    case R.id.drawer_gank:
                        showFragment = Constants.TYPE_GANK;
                        mSearchMenuItem.setVisible(true);
                        break;
                    case R.id.drawer_wechat:
                        showFragment = Constants.TYPE_WECHAT;
                        mSearchMenuItem.setVisible(true);
                        break;
                    case R.id.drawer_gold:
                        showFragment = Constants.TYPE_GOLD;
                        mSearchMenuItem.setVisible(false);
                        break;
                    case R.id.drawer_vtex:
                        showFragment = Constants.TYPE_VTEX;
                        mSearchMenuItem.setVisible(false);
                        break;
                    case R.id.drawer_ipfs:
                        showFragment = Constants.TYPE_IPFS;
                        mSearchMenuItem.setVisible(false);
                        break;
                    case R.id.drawer_wallet:
                        showFragment = Constants.TYPE_WALLET;
                        mSearchMenuItem.setVisible(false);
                        break;
                    case R.id.drawer_setting:
                        showFragment = Constants.TYPE_SETTING;
                        mSearchMenuItem.setVisible(false);
                        break;
                    case R.id.drawer_like:
                        showFragment = Constants.TYPE_LIKE;
                        mSearchMenuItem.setVisible(false);
                        break;
                    case R.id.drawer_about:
                        showFragment = Constants.TYPE_ABOUT;
                        mSearchMenuItem.setVisible(false);
                        break;
                }
                if (mLastMenuItem != null) {
                    mLastMenuItem.setChecked(false);
                }
                mLastMenuItem = menuItem;
                mPresenter.setCurrentItem(showFragment);
                menuItem.setChecked(true);
                mToolbar.setTitle(menuItem.getTitle());
                mDrawerLayout.closeDrawers();
                showHideFragment(getTargetFragment(showFragment), getTargetFragment(hideFragment));
                hideFragment = showFragment;
                return true;
            }
        });
        mSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (showFragment == Constants.TYPE_GANK) {
                    mGankFragment.doSearch(query);
                } else if (showFragment == Constants.TYPE_WECHAT) {
                    RxBus.getDefault().post(new SearchEvent(query, Constants.TYPE_WECHAT));
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        if (!mPresenter.getVersionPoint() && SystemUtil.isWifiConnected()) {
            mPresenter.setVersionPoint(true);
            try {
                PackageManager pm = getPackageManager();
                PackageInfo pi = pm.getPackageInfo(getPackageName(), PackageManager.GET_ACTIVITIES);
                String versionName = pi.versionName;
                mPresenter.checkVersion(versionName);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        item.setVisible(false);
        mSearchView.setMenuItem(item);
        mSearchMenuItem = item;
        return true;
    }

    @Override
    public void onBackPressedSupport() {
        if (mSearchView.isSearchOpen()) {
            mSearchView.closeSearch();
        } else {
            showExitDialog();
        }
    }

    private void showExitDialog() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage("确定退出GeekNews吗");
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                App.getInstance().exitApp();
            }
        });
        builder.show();
    }

    private SupportFragment getTargetFragment(int item) {
        switch (item) {
            case Constants.TYPE_ZHIHU:
                return mZhihuFragment;
            case Constants.TYPE_GANK:
                return mGankFragment;
            case Constants.TYPE_WECHAT:
                return mWechatFragment;
            case Constants.TYPE_GOLD:
                return mGoldFragment;
            case Constants.TYPE_VTEX:
                return mVtexFragment;
            case Constants.TYPE_LIKE:
                return mLikeFragment;
            case Constants.TYPE_SETTING:
                return mSettingFragment;
            case Constants.TYPE_ABOUT:
                return mAboutFragment;
            case Constants.TYPE_IPFS:
                return mIpfsFragment;
            case Constants.TYPE_WALLET:
                return mWalletFragment;
        }
        return mZhihuFragment;
    }

    private int getCurrentItem(int item) {
        switch (item) {
            case Constants.TYPE_ZHIHU:
                return R.id.drawer_zhihu;
            case Constants.TYPE_GANK:
                return R.id.drawer_gank;
            case Constants.TYPE_WECHAT:
                return R.id.drawer_wechat;
            case Constants.TYPE_GOLD:
                return R.id.drawer_gold;
            case Constants.TYPE_VTEX:
                return R.id.drawer_vtex;
            case Constants.TYPE_IPFS:
                return R.id.drawer_ipfs;
            case Constants.TYPE_WALLET:
                return R.id.drawer_wallet;
            case Constants.TYPE_LIKE:
                return R.id.drawer_like;
            case Constants.TYPE_SETTING:
                return R.id.drawer_setting;
            case Constants.TYPE_ABOUT:
                return R.id.drawer_about;
        }
        return R.id.drawer_zhihu;
    }

    @Override
    public void showUpdateDialog(String versionContent) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle("检测到新版本!");
        builder.setMessage(versionContent);
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("马上更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                checkPermissions();
            }
        });
        builder.show();
    }

    @Override
    public void startDownloadService() {
        startService(new Intent(mContext, UpdateService.class));
    }

    public void checkPermissions() {
        mPresenter.checkPermissions(new RxPermissions(this));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*在这里，我们通过碎片管理器中的Tag，就是每个碎片的名称，来获取对应的fragment*/
//        getTargetFragment()
//        Fragment f = fragmentManager.findFragmentByTag(curFragmentTag);
//        /*然后在碎片中调用重写的onActivityResult方法*/
//        f.onActivityResult(requestCode, resultCode, data);
        // TODO:先直接转到mIpfsFragment 看看
        if (requestCode == Constants.REQUEST_FROM_SET_WALLET) {
            mWalletFragment.onActivityResult(requestCode, resultCode, data);
        } else {
            mIpfsFragment.onActivityResult(requestCode, resultCode, data);
        }
    }
}
