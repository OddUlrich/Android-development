package project.news.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
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

import java.util.ArrayList;
import java.util.List;

import project.news.R;
import project.news.model.Article;
import project.news.FavPostManager;

/**
 * A recycler view adapter for favourite fragment.
 *
 * @author Wyman Wong
 * @version 1.0
 *
 */
public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.FavArticleListHolder> {

    private Context context;
    private OnItemClickListener mListener;
    public FavPostManager favPostManager;
    public List<Article> showingList;
    public List<Article> oldList = new ArrayList<>();

    /**
     * Constructor.
     *
     * @param context main activity context
     * @param favPostManager data manager for managing favourite post list.
     */
    public FavouriteAdapter(Context context, FavPostManager favPostManager) {
        this.context = context;
        this.favPostManager = favPostManager;
        showingList = favPostManager.getFavPostList();
    }

    /**
     * Interface on item click listener for post recycler view.
     *
     * @author Wyman
     */
    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    /**
     * Assign item click listener to instance variable.
     *
     * @author Wyman
     * @param listener item click listener.
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    /**
     * An article list holder holding each card view of article.
     *
     * @author Wyman
     * @param parent parent view group for all the card views.
     * @param viewType type of the view.
     * @return holder
     */
    @Override
    public FavArticleListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View favArticleListView = inflater.inflate(R.layout.fav_articles_cardview, parent, false);

        return new FavArticleListHolder(favArticleListView, mListener);
    }

    /**
     * Complete showing content for the each view holder.
     *
     * @author Wyman
     * @param holder favourite article holder
     * @param position item position of the assigned list.
     */
    @Override
    public void onBindViewHolder(FavArticleListHolder holder, int position) {
        Article article = showingList.get(position);

        //specify request option for loading the image
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.color.placeHolder);
        requestOptions.error(R.color.errorImage);
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
        requestOptions.centerCrop();

        // Load the image of post.
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
                .into(holder.img);

        // Fill the content for each element.
        holder.title.setText(article.getTitle());
        if (article.getSource() != null && article.getSource().getName() != null) {
            holder.source.setText(article.getSource().getName());
        }

        String date = article.getPublishedAt();
        if (date != null && date.length() >= 10) {
            holder.date.setText(article.getPublishedAt().substring(0, 10));
        }

        String tag = "Tag: " + article.getTag();
        if (tag.length() >= 12) {
            holder.tag.setText(tag.substring(0, 12));
        } else {
            holder.tag.setText(tag);
        }
    }

    /**
     * Return number of list item in the adapter.
     *
     * @author Wyman
     * @return count of items.
     */
    @Override
    public int getItemCount() {
        return showingList.size();
    }

    /**
     * Update the showing list with specific list.
     *
     * @author Wyman
     * @param newList the specific list that should be shown.
     */
    public void updateList(List<Article> newList) {
        showingList = new ArrayList<>();

        showingList.addAll(newList);
        notifyDataSetChanged();
    }

    /**
     * Modify the tag of specific post item.
     *
     * @author Wyman
     * @param position item position of the assigned list.
     * @param newTag new assigned tag for the post.
     */
    public void modifyItem(int position, String newTag) {
        Article post = showingList.get(position);

        // Modify item in the showing list.
        post.setTag(newTag);
        notifyDataSetChanged();

        post = oldList.get(position);
        post.setTag(newTag);

        // Modify item from the data manager.
        favPostManager.modifyTagOfPost(post, newTag);
    }

    /**
     * Delete specific post item.
     *
     * @author Wyman
     * @param position item position of the assigned list.
     */
    public void deleteItem(int position) {
        Article post = showingList.get(position);

        oldList.remove(position);

        // Delete item in the showing list.
        showingList.remove(position);
        notifyItemRemoved(position);

        // Delete item from the data manager.
        favPostManager.removeFavPost(post);
    }

    /**
     * Inner class for the recycler view adapter.
     *
     * @author Wyman
     */
    static class FavArticleListHolder extends RecyclerView.ViewHolder {
        View listView;

        TextView title, source, date, tag;
        ImageView img;

        /**\
         * Match holder items with their layout item and set on click listener for each items.
         *
         * @author Wyman
         * @param itemView view for the holder.
         * @param listener on item click listener.
         */
        FavArticleListHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            listView = itemView;

            title = itemView.findViewById(R.id.fav_article_title);
            source = itemView.findViewById(R.id.fav_article_source);
            date = itemView.findViewById(R.id.fav_article_date);
            tag = itemView.findViewById(R.id.fav_article_tag);
            img = itemView.findViewById(R.id.fav_article_img);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        int position = getAdapterPosition();

                        if (position != RecyclerView.NO_POSITION){
                            listener.onItemClick(v, getAdapterPosition());
                        }
                    }
                }
            });
        }
    }
}
