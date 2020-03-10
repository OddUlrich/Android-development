package project.news;

import android.content.Context;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


import project.news.adapter.TabviewPagerAdapter;

/**
 * News page fragment triggered by the news icon of the bottom navigation menu.
 *
 * @author Dawen
 * @version 1.0
 */
public class MainPage extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    // Store news category of the NEWS API.
    private List<String> tabTitleList = new ArrayList<>();

    private Context mContext;

    /**
     * Create views in main page fragment.
     *
     * This page contains 7 tab view, all of which are view paper.
     *
     * @param inflater inflates pages into container.
     * @param container container for all views in the current page.
     * @param savedInstanceState state.
     * @return view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment.
        View v = inflater.inflate(R.layout.fragment_main_page, container, false);

        tabLayout = v.findViewById(R.id.mainPage_tabLayout);
        viewPager = v.findViewById(R.id.mainPage_pager);

        // Six news categories of the News API.
        tabTitleList.add("general");
        tabTitleList.add("business");
        tabTitleList.add("entertainment");
        tabTitleList.add("health");
        tabTitleList.add("science");
        tabTitleList.add("sports");
        tabTitleList.add("technology");

        // Initialize the tabview adapter with getChildFragmentManager(),
        // as mainPageFragment is a sub fragment under MainPage fragment
        TabviewPagerAdapter pagerAdapter = new TabviewPagerAdapter(getChildFragmentManager());

        // Call AddFragment function of the tabview adapter.
        // Add tabs stored in the tabTitleList into the tabview adapter as mainPageFragment.
        AddFragments(pagerAdapter);

        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        // Set tab change listener.
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        return v;
    }

    /**
     * Add fragments to the each tab view.
     *
     * @param pagerAdapter paper adapter
     */
    public void AddFragments(TabviewPagerAdapter pagerAdapter) {
        for(String title : tabTitleList) {
            Bundle args = new Bundle();

            // Pass the tabTitle into mainPageFragment as arguments.
            args.putString("key",title);
            MainPageFragment fragment = new MainPageFragment();
            fragment.setArguments(args);
            pagerAdapter.AddFragment(fragment,title);
        }
    }

    /**
     * Save the context.
     *
     * @param context activity context.
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Attach MainActivity Context into current fragment.
        mContext = context;
    }
}
