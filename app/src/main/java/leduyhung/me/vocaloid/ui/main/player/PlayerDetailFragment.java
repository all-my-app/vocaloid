package leduyhung.me.vocaloid.ui.main.player;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import leduyhung.me.vocaloid.BaseFragment;

import leduyhung.me.vocaloid.R;

public class PlayerDetailFragment extends BaseFragment {

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        setLayoutId(R.layout.fragment_player_detail);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}