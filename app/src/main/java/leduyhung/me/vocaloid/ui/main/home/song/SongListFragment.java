package leduyhung.me.vocaloid.ui.main.home.song;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import leduyhung.me.vocaloid.BaseFragment;
import leduyhung.me.vocaloid.R;
import leduyhung.me.vocaloid.model.song.MessageForListSongFragment;
import leduyhung.me.vocaloid.model.song.Song;

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
        EventBus.getDefault().register(this);
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
                break;
            case MessageForListSongFragment.CODE_LOAD_MORE_LIST_SONG_SUCCESS:
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