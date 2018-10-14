package leduyhung.me.vocaloid.ui.main.home;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import leduyhung.me.vocaloid.R;

public class HomeFragment extends Fragment {

    private Context mContext;

    private View v;
    private TabLayout tabLayout;
    private ViewPager vPager;
    private HomeViewPagerAdapter adap;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_home, container, false);
        tabLayout = v.findViewById(R.id.tablayout);
        vPager = v.findViewById(R.id.v_pager);
        return v;
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
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}