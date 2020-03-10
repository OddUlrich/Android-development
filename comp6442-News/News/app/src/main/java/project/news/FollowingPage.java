package project.news;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import project.news.adapter.TabviewPagerAdapter;

/**
 * The fragment is to display lists of news sources that user have followed and all available news sources.
 * By implement tab layout that allows users to switch between two lists.
 *
 * @author Jing Qian
 * @version 1.0
 */

public class FollowingPage extends Fragment{

    private CustomViewPager viewPager;
    private TabLayout tabLayout;

    private List<String> tabs=new ArrayList<>();

    private Context mContext;

    /**
     * Create views in following page fragment.
     *
     * This page contains two tab view, both of which are view paper.
     *
     * @param inflater inflates pages into container.
     * @param container container for all views in the current page.
     * @param savedInstanceState state.
     * @return view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_following_page, container, false); // Set layout of this fragment.
        viewPager=view.findViewById(R.id.following_pager);  // Find view pager layout to show the subfragment recycler view.
        tabLayout=view.findViewById(R.id.following_tabLayout);  // Set the tab layout.

        tabs.add("Currently Following");  // Add tab names to the list.
        tabs.add("Available Sources");

        // Customised to disable the use of swipe action to change tabs.
        viewPager.setSwipeToChangeEnabled(false);

        // Initialize the tablayout adapter with getChildFragmentManager(),
        // as FollowedSiteFragment and AllsiteFragment are sub fragments under FollowingPage fragment.
        TabviewPagerAdapter pageAdapter=new TabviewPagerAdapter(getChildFragmentManager());//

        // Add tabs stored in the tabs list into the tablayout adapter as FollowedSiteFragment and AllsiteFragment.
        addFragments(pageAdapter);

        viewPager.setAdapter(pageAdapter);
        tabLayout.setupWithViewPager(viewPager);

        // Set onPageChangeListener to change listen for tab change.
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        return view;
    }

    /**
     * Add fragments to the two tab view.
     *
     * @param adapter paper adapter
     */
    public void addFragments(TabviewPagerAdapter adapter){
        for(String title:tabs){
            if(title.equals("Currently Following")){
                Bundle args=new Bundle();
                args.putString("key","Currently Following");//parse tab titles as arguments
                FollowedSiteFragment fragment = new FollowedSiteFragment();
                fragment.setArguments(args);
                adapter.AddFragment(fragment,title);
            }else if (title.equals("Available Sources")){
                Bundle args=new Bundle();
                args.putString("key","Available Sources");//parse tab titles as arguments
                AllSiteFragment fragment = new AllSiteFragment();
                fragment.setArguments(args);
                adapter.AddFragment(fragment,title);
            }
        }
    }

    /**
     * Save the context.
     *
     * @param context activity context.
     */
    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        // Attach MainActivity Context into current fragment.
        mContext=context;
    }

}
