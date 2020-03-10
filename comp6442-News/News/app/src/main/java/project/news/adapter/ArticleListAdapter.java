package project.news.adapter;

import android.content.Context;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;

import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.util.List;


import project.news.R;

import project.news.Utility;
import project.news.model.Article;

/**
 * mainPage recycler view adapter used for rendering cardview for the recycler view.
 *
 * @author Dawen
 * @version 1.0
 */

public class ArticleListAdapter extends RecyclerView.Adapter<ArticleListAdapter.articleListViewHolder>{

    private Context context;
    private List<Article> articleList;

    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    //provide the set OnItemClickListener function for the adapter
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    /**
     * Binding cardview widgets with the viewholder object and override the card view onclick function
     *
     * @author Dawen
     *
     */
    public static class articleListViewHolder extends RecyclerView.ViewHolder{
        TextView title, time, source;
        ImageView urlImage;

        public articleListViewHolder(View itemView, final OnItemClickListener listener){
            super(itemView);
            title = itemView.findViewById(R.id.articleList_title);
            time = itemView.findViewById(R.id.articleList_time);
            source = itemView.findViewById(R.id.articleList_source);
            urlImage = itemView.findViewById(R.id.articleList_img);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            //when the onclick function of the cardview is invoked,
                            //call the onItemClick listener function of the adapter and start newsdetail activity.
                            listener.onItemClick(v,getAdapterPosition());
                        }
                    }
                }
            });
        }

    }

    //the adapter will accept the MainPageFragment context and the articleList when initialization.
    public ArticleListAdapter(Context context, List<Article> articleList) {
        this.context = context;
        this.articleList = articleList;
    }

    /**
     * Initialize a articles_list card view layout.
     *
     * @author Dawen
     * @param parent will be outer layer recyclerview
     * @param viewType label for the view.
     * @return a customized viewholder object
     */
    @NonNull
    @Override
    public articleListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View articleListView = inflater.inflate(R.layout.articles_list,parent,false);
        return new articleListViewHolder(articleListView,mListener);//Pass onItemClickListener of the adapter to viewholder when initialization
    }

    /**
     * Set data to the viewholder with articleList of the adapter.
     *
     * @author Dawen
     * @param holder view holder
     * @param position will be cardview position in the recyclerview adapter

     */
    @Override
    public void onBindViewHolder(@NonNull articleListViewHolder holder, int position) {
        Article article = articleList.get(position);

        //specify request option for loading the image
        RequestOptions requestOptions = new RequestOptions();
        //set a color block when image is loading
        requestOptions.placeholder(R.color.placeHolder);
        //set a color block when there is no image.
        requestOptions.error(R.color.errorImage);
        //catch all images
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
        //centercrop the image
        requestOptions.centerCrop();

        //using Glide library to load the image of each article.
        Glide.with(context)
                .load(article.getUrlToImage())
                .apply(requestOptions)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(holder.urlImage);


        holder.title.setText(article.getTitle());
        holder.time.setText(Utility.ConvertTime(article.getPublishedAt()));
        holder.source.setText(article.getSource().getName());
    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }
}
