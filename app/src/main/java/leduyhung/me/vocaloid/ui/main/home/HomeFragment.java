package leduyhung.me.vocaloid.ui.main.home;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import leduyhung.me.vocaloid.BaseFragment;
import leduyhung.me.vocaloid.R;

public class HomeFragment extends BaseFragment {

    private TabLayout tabLayout;
    private ViewPager vPager;
    private HomeViewPagerAdapter adap;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        setLayoutId(R.layout.fragment_home);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void initViewPager() {
        adap = new HomeViewPagerAdapter(getChildFragmentManager(), mContext);
        vPager.setAdapter(adap);
        vPager.setOffscreenPageLimit(2);
    }

    private void initTabLayout() {
        tabLayout.setupWithViewPager(vPager);
    }
}