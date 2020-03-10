package project.news.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import project.news.R;
import project.news.model.Site;

/**
 * FollowingPage recycler view adapter used for rendering cardview to the recycler view
 *
 * @author Jing Qian
 * @version 1.0
 */

public class followingSitesAdapter extends RecyclerView.Adapter<followingSitesAdapter.followingSitesViewHolder>{
    private Context mContext;
    private List<Site> sourceList;

    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener=listener;
    }

    /**
     * Binding cardview widgets with the viewholder object and override the cardview onclick function.
     * Set the onclick listener on text view(source title) for the ease of swipe.
     * Set onClickListener on the cardview make swipe action very difficult.
     *
     * @author Jing Qian
     */
    public static class followingSitesViewHolder extends RecyclerView.ViewHolder {
        TextView title;

        public followingSitesViewHolder(View itemView, final OnItemClickListener listener){
            super(itemView);
            title=itemView.findViewById(R.id.following_source_name);

            title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null){
                        int position=getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION){
                            listener.onItemClick(v, getAdapterPosition());
                        }
                    }
                }
            });
        }

    }

    /**
     * Constructor.
     *
     * @param context context
     * @param sourceList source list
     */
    public followingSitesAdapter(Context context, List<Site> sourceList ){
        this.mContext=context;
        this.sourceList=sourceList;
    }


    /**
     * An site list holder holding each card view of site.
     *
     * @param parent view group object
     * @param viewType label of view
     * @return followingSitesViewHolder
     */
    @NonNull
    @Override
    public followingSitesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        LayoutInflater inflater=LayoutInflater.from(mContext);
        View FollowingListView=inflater.inflate(R.layout.site_cardview_layout,parent,false);
        return new followingSitesViewHolder(FollowingListView,mListener);
    }

    /**
     * Complete showing content for the each view holder.
     *
     * @param holder following site holder
     * @param position item position of the assigned list.
     */
    @Override
    public void onBindViewHolder(@NonNull followingSitesViewHolder holder, int position){
        Site site=sourceList.get(position);
        holder.title.setText(site.getName());
    }

    /**
     * Return number of list item in the adapter.
     *
     * @return count of items.
     */
    @Override
    public int getItemCount(){
        return sourceList.size();
    }


}
