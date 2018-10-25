package leduyhung.me.vocaloid.ui.main.home;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewTreeObserver;

import leduyhung.me.vocaloid.BaseFragment;
import leduyhung.me.vocaloid.R;

public class HomeFragment extends BaseFragment {

    private final String KEY_HOME_NEED_UPDATE_ADAPTER = "KEY_HOME_NEED_UPDATE_ADAPTER";

    private TabLayout tabLayout;
    private ViewPager vPager;
    private HomeViewPagerAdapter adap;

    private boolean needUpdateAdap = false;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        setLayoutId(R.layout.fragment_home);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null)
            needUpdateAdap = savedInstanceState.getBoolean(KEY_HOME_NEED_UPDATE_ADAPTER);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tabLayout = root.findViewById(R.id.tablayout);
        vPager = root.findViewById(R.id.v_pager);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViewPager();
        initTabLayout();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        needUpdateAdap = true;
        outState.putBoolean(KEY_HOME_NEED_UPDATE_ADAPTER, needUpdateAdap);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        adap.destroy();
    }

    private void initViewPager() {
        adap = new HomeViewPagerAdapter(getChildFragmentManager(), mContext);
        vPager.setAdapter(adap);
        vPager.setOffscreenPageLimit(2);
        vPager.getViewTreeObserver().addOnGlobalFocusChangeListener(new ViewTreeObserver.OnGlobalFocusChangeListener() {
            @Override
            public void onGlobalFocusChanged(View view, View view1) {
                vPager.getViewTreeObserver().removeOnGlobalFocusChangeListener(this);
                if (needUpdateAdap)
                    adap.update();
            }
        });
    }

    private void initTabLayout() {
        tabLayout.setupWithViewPager(vPager);
    }
}