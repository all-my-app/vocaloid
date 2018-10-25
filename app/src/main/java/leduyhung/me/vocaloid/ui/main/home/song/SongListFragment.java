package leduyhung.me.vocaloid.ui.main.home.song;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.leduyhung.loglibrary.Logg;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import leduyhung.me.vocaloid.BaseFragment;
import leduyhung.me.vocaloid.R;
import leduyhung.me.vocaloid.model.song.Song;
import leduyhung.me.vocaloid.ui.main.MessageForMainActivity;

public class SongListFragment extends BaseFragment {

    private RecyclerView recycler;
    private SongListRecyclerAdapter adap;
    private TextView tNoData;

    private Song song;
    private int currentPage;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        setLayoutId(R.layout.fragment_list_simple);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        song = new Song(mContext);
        currentPage = 1;
        EventBus.getDefault().register(this);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recycler = root.findViewById(R.id.recycler);
        tNoData = root.findViewById(R.id.txt_no_data);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initRecycler();
        getDataFromServer(null, currentPage);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveMessage(MessageForListSongFragment message) {

        switch (message.getCode()) {
            case MessageForListSongFragment.CODE_LOAD_LIST_SONG_SUCCESS:
                adap.update(song.getData(), true);
                EventBus.getDefault().post(new MessageForMainActivity(MessageForMainActivity.CODE_LOAD_DATA_MUSIC, song.getData(), 0));
                Logg.error(getClass(), "song data size: " + song.getData().size() + " with current page: " + song.getCurrent_page());
                break;
            case MessageForListSongFragment.CODE_LOAD_MORE_LIST_SONG_SUCCESS:
                EventBus.getDefault().post(new MessageForMainActivity(MessageForMainActivity.CODE_LOAD_DATA_MUSIC, song.getData(), 0));
                adap.update(song.getData(), false);
                break;
            case MessageForListSongFragment.CODE_SEARCH_LIST_SONG_SUCCESS:
                adap.update(song.getData(), currentPage == 1);
                break;
            case MessageForListSongFragment.CODE_LOAD_LIST_SONG_FAIL:
                setTextNoData(message.getMessage());
                break;
            case MessageForListSongFragment.CODE_LOAD_MORE_SONG_FAIL:
                setTextNoData(message.getMessage());
                break;
            case MessageForListSongFragment.CODE_SEARCH_LIST_SONG_FAIL:
                setTextNoData(message.getMessage());
                break;
            case MessageForListSongFragment.CODE_SERVER_ERROR:
                setTextNoData(message.getMessage());
                break;
            case MessageForListSongFragment.CODE_SONG_LIST_ADAPTER_CLICK:
                Logg.error(getClass(), "song adapter click: " + message.getSongInfos().size() + " index = " + message.getIndex());
                EventBus.getDefault().post(new MessageForMainActivity(MessageForMainActivity.CODE_PLAY_MUSIC, message.getSongInfos(), message.getIndex()));
                break;
        }
    }

    private void initRecycler() {

        adap = new SongListRecyclerAdapter(mContext);
        LinearLayoutManager manager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        recycler.setLayoutManager(manager);
        recycler.setAdapter(adap);
    }

    private void getDataFromServer(String name, int page) {
        song.getListSong(name, page);
    }

    private void setTextNoData(String content) {
        if (content != null)
            tNoData.setText(content);
    }
}