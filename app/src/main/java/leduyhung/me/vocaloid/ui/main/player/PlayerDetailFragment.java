package leduyhung.me.vocaloid.ui.main.player;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import leduyhung.me.vocaloid.BaseFragment;

import leduyhung.me.vocaloid.R;
import leduyhung.me.vocaloid.model.song.SongInfo;
import leduyhung.me.vocaloid.player.PlayerCallback;
import leduyhung.me.vocaloid.player.PlayerCallbackModel;
import leduyhung.me.vocaloid.player.PlayerFactory;
import leduyhung.me.vocaloid.player.PlayerMode;
import leduyhung.me.vocaloid.player.PlayerState;

public class PlayerDetailFragment extends BaseFragment implements View.OnClickListener, PlayerCallback{

    private ImageView iCollapse, iGif, iPrevious, iPlayControl, iNext, iPlayMode;
    private TextView tCurrentDuration, tTotalDuration, tSong, tSinger;
    private RelativeLayout rDownload;

    private PlayerCallbackModel playerCallback;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        setLayoutId(R.layout.fragment_player_detail);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        playerCallback = new PlayerCallbackModel(getClass(), this);
        PlayerFactory.newInstance().setCallback(playerCallback);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        getConfigView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        PlayerFactory.newInstance().removeCallback(playerCallback);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_collapse:
                break;
            case R.id.img_previous:
                break;
            case R.id.img_play_control:
                handlePlayControlCLick();
                break;
            case R.id.img_next:
                break;
            case R.id.img_play_mode:
                break;
            case R.id.relative_download:
                break;
        }
    }

    @Override
    public void onPrepare(SongInfo songInfo, int index) {

    }

    @Override
    public void onStart(SongInfo songInfo) {

    }

    @Override
    public void onPlaying(SongInfo songInfo, int currentTime) {

    }

    @Override
    public void onPlayingComplete() {

    }

    @Override
    public void onError() {

    }

    private void initView() {
        iCollapse = root.findViewById(R.id.img_collapse);
        iGif = root.findViewById(R.id.img_gif);
        iPrevious = root.findViewById(R.id.img_previous);
        iPlayControl = root.findViewById(R.id.img_play_control);
        iNext = root.findViewById(R.id.img_next);
        iPlayMode = root.findViewById(R.id.img_play_mode);
        tCurrentDuration = root.findViewById(R.id.txt_current_duration);
        tTotalDuration = root.findViewById(R.id.txt_total_duration);
        tSong = root.findViewById(R.id.txt_title_song);
        tSinger = root.findViewById(R.id.txt_title_singer);
        rDownload = root.findViewById(R.id.relative_download);

        iCollapse.setOnClickListener(this);
        iPrevious.setOnClickListener(this);
        iPlayControl.setOnClickListener(this);
        iPlayMode.setOnClickListener(this);
        iNext.setOnClickListener(this);
        rDownload.setOnClickListener(this);
    }

    private void getConfigView() {

        PlayerState state = PlayerFactory.newInstance().getState();
        if (state == PlayerState.PLAYING) {

            iPlayControl.setImageDrawable(mContext.getResources().getDrawable(R.drawable.anim_pause_play));
        } else {

            iPlayControl.setImageDrawable(mContext.getResources().getDrawable(R.drawable.anim_play_pause));
        }

        PlayerMode mode = PlayerFactory.newInstance().getMode();
        if (mode == PlayerMode.LOOP) {

            iPlayMode.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_loop_white));
        } else if (mode == PlayerMode.SEQUENCE) {

            iPlayMode.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_sequence_white));
        } else {

            iPlayMode.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_shuffle_white));
        }
    }

    private void handlePlayControlCLick() {

        PlayerState state = PlayerFactory.newInstance().getState();
        if (state == PlayerState.PLAYING) {

            animationIconPlayControl(false);
        } else {

            animationIconPlayControl(true);
        }
    }

    private void animationIconPlayControl(boolean isPlay) {
        Drawable drawable = iPlayControl.getDrawable();
        if (drawable instanceof Animatable) {
            ((Animatable) drawable).start();
            if (isPlay) {
                iPlayControl.setImageDrawable(mContext.getResources().getDrawable(R.drawable.anim_pause_play));
            } else {

                iPlayControl.setImageDrawable(mContext.getResources().getDrawable(R.drawable.anim_play_pause));
            }
        }
    }
}