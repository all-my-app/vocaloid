package leduyhung.me.vocaloid.ui.main.home.song;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import leduyhung.me.vocaloid.Constants;
import leduyhung.me.vocaloid.R;
import leduyhung.me.vocaloid.model.song.SongInfo;

public class SongListRecyclerAdapter extends RecyclerView.Adapter {

    private Context mContext;

    private boolean isLoadMore;
    private ArrayList<SongInfo> arrData;

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;

    public SongListRecyclerAdapter(Context mContext) {
        this.mContext = mContext;
        arrData = new ArrayList();
    }

    @Override
    public int getItemViewType(int position) {
        return arrData.get(position) != null ? Constants.Recycler.RECYCLER_ITEM_HAS_DATA
                : Constants.Recycler.RECYCLER_ITEM_LOAD_MORE;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (i == Constants.Recycler.RECYCLER_ITEM_HAS_DATA) {

            return new ItemView(LayoutInflater.from(mContext).inflate(R.layout.recycler_item_song, viewGroup, false));
        } else {
            return new ItemView(LayoutInflater.from(mContext).inflate(R.layout.recycler_item_load, viewGroup, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof ItemView) {

        } else {

        }
    }

    @Override
    public int getItemCount() {
        return arrData.size();
    }

    public void update(ArrayList<SongInfo> data, boolean override) {

        int currentItem = 0;
        int totalItem = data.size();
        if (override) {

            arrData.clear();
            arrData.addAll(data);
            notifyDataSetChanged();
        } else {
            if (isLoadMore) {
                currentItem = arrData.size() - 1;
                arrData.remove(currentItem);
                notifyItemRemoved(currentItem);
                isLoadMore = false;
            }
            arrData.addAll(data);
            notifyItemRangeInserted(currentItem, totalItem);
        }
    }

    public boolean isLoadMore() {
        return isLoadMore;
    }

    private class ItemView extends RecyclerView.ViewHolder {

        private TextView tName, tSinger, tDuration;
        private ImageView iThumbnail;

        public ItemView(View itemView) {
            super(itemView);
        }
    }

    private class LoadView extends RecyclerView.ViewHolder {

        public LoadView(View itemView) {
            super(itemView);
        }
    }
}
