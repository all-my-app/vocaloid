package leduyhung.me.vocaloid.ui.main;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import de.hdodenhof.circleimageview.CircleImageView;
import leduyhung.me.vocaloid.R;
import leduyhung.me.vocaloid.model.user.User;
import leduyhung.me.vocaloid.player.PlayerBrowser;
import leduyhung.me.vocaloid.ui.main.home.HomeFragment;
import leduyhung.me.vocaloid.util.ImageUtil;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, User.OnUserCallback {

    private ImageView iMenuDrawer;
    private DrawerLayout drawer;
    private RelativeLayout rHome, rWallpaper, rFeedback, rLogin, rLogout;
    private TextView tUserName;
    private CircleImageView iAvatar;

    private HomeFragment homeFragment;

    private PlayerBrowser playerBrowser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playerBrowser = new PlayerBrowser(this);
        initView();
        checkUserLogin();
        addHomeFragment();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        playerBrowser.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        playerBrowser.disconnect();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        User.newInstance().unRegisterCallback();
        EventBus.getDefault().unregister(this);
        playerBrowser.disconnect();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.img_menu_drawer:
                if (drawer.isDrawerOpen(Gravity.START)) {
                    drawer.closeDrawer(Gravity.START);
                } else {
                    drawer.openDrawer(Gravity.START);
                }
                break;
            case R.id.relative_home:
                addHomeFragment();
                break;
            case R.id.relative_wallpaper:
                break;
            case R.id.relative_feedback:
                break;
            case R.id.relative_login:
                User.newInstance().create(this).login();
                break;
            case R.id.relative_logout:
                User.newInstance().create(this).logout();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    TransitionManager.beginDelayedTransition((ViewGroup) rLogout.getRootView());
                }
                rLogin.setVisibility(View.VISIBLE);
                rLogout.setVisibility(View.INVISIBLE);
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveMessage(MessageForMainActivity message) {
        switch (message.getCode()) {
            case MessageForMainActivity.CODE_PLAY_MUSIC:
                playerBrowser.playSequence(message.getIndex());
                break;
            case MessageForMainActivity.CODE_LOAD_DATA_MUSIC:
                playerBrowser.prepareSong(message.getSongInfos());
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        User.newInstance().create(this).setActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onLoginComplete() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            TransitionManager.beginDelayedTransition((ViewGroup) rLogout.getRootView());
        }
        rLogin.setVisibility(View.INVISIBLE);
        rLogout.setVisibility(View.VISIBLE);
        tUserName.setText(User.newInstance().getUserData().getName());
        ImageUtil.newInstance().loadImageFromNet(this, iAvatar, User.newInstance().getUserData().getAvatar());
    }

    private void initView() {
        iMenuDrawer = findViewById(R.id.img_menu_drawer);
        drawer = findViewById(R.id.drawer_layout);
        rHome = findViewById(R.id.relative_home);
        rWallpaper = findViewById(R.id.relative_wallpaper);
        rFeedback = findViewById(R.id.relative_feedback);
        rLogin = findViewById(R.id.relative_login);
        rLogout = findViewById(R.id.relative_logout);
        tUserName = findViewById(R.id.txt_name);
        iAvatar = findViewById(R.id.img_avatar);
        iMenuDrawer.setOnClickListener(this);
        rHome.setOnClickListener(this);
        rWallpaper.setOnClickListener(this);
        rFeedback.setOnClickListener(this);
        rLogin.setOnClickListener(this);
        rLogout.setOnClickListener(this);
    }

    private void checkUserLogin() {
        if (User.newInstance().create(this).isUserLogin()) {
            rLogin.setVisibility(View.INVISIBLE);
            rLogout.setVisibility(View.VISIBLE);
            tUserName.setText(User.newInstance().getUserData().getName());
            ImageUtil.newInstance().loadImageFromNet(this, iAvatar, User.newInstance().getUserData().getAvatar());
        } else {
            rLogin.setVisibility(View.VISIBLE);
            rLogout.setVisibility(View.INVISIBLE);
        }
        User.newInstance().registerCallback(this);
    }

    private void addHomeFragment() {

        if (homeFragment == null)
            homeFragment = new HomeFragment();
        FragmentManager manager = getSupportFragmentManager();

        if (manager.findFragmentById(R.id.frame_contain) instanceof HomeFragment)
            return;

        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.frame_contain, homeFragment);
        transaction.commit();
    }
}