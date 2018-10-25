package leduyhung.me.vocaloid.ui.main.home;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import leduyhung.me.vocaloid.ui.main.home.favorite.FavoriteSongFragment;
import leduyhung.me.vocaloid.ui.main.home.singer.SingerListFragment;
import leduyhung.me.vocaloid.ui.main.home.song.SongListFragment;

public class HomeViewPagerAdapter extends FragmentPagerAdapter {

    private Context mContext;

    private SongListFragment songListFragment;
    private SingerListFragment singerListFragment;
    private FavoriteSongFragment favoriteSongFragment;

    private FragmentManager fragmentManager;

    public HomeViewPagerAdapter(FragmentManager fm, Context mContext) {
        super(fm);
        this.mContext = mContext;
        this.fragmentManager = fm;
        initFragment();
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                return songListFragment;
            case 1:
                return singerListFragment;
            default:
                return favoriteSongFragment;
        }
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return 3;
    }

    private void initFragment() {
        songListFragment = new SongListFragment();
        singerListFragment = new SingerListFragment();
        favoriteSongFragment = new FavoriteSongFragment();
    }

    public SongListFragment getSongListFragment() {
        return songListFragment;
    }

    public SingerListFragment getSingerListFragment() {
        return singerListFragment;
    }

    public FavoriteSongFragment getFavoriteSongFragment() {
        return favoriteSongFragment;
    }

    public void update() {
        notifyDataSetChanged();
    }

    public void destroy() {
        fragmentManager.beginTransaction()
                .remove(songListFragment)
                .remove(singerListFragment)
                .remove(favoriteSongFragment).commitAllowingStateLoss();
    }
}
