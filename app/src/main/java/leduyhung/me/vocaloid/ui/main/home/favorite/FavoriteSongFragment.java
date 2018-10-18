package leduyhung.me.vocaloid.ui.main.home.favorite;

import android.content.Context;

import leduyhung.me.vocaloid.BaseFragment;
import leduyhung.me.vocaloid.R;

public class FavoriteSongFragment extends BaseFragment {

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        setLayoutId(R.layout.fragment_list_simple);
    }
}