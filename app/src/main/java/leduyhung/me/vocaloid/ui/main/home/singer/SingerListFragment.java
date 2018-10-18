package leduyhung.me.vocaloid.ui.main.home.singer;

import android.content.Context;

import leduyhung.me.vocaloid.BaseFragment;
import leduyhung.me.vocaloid.R;

public class SingerListFragment extends BaseFragment {

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        setLayoutId(R.layout.fragment_list_simple);
    }
}
