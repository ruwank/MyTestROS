package com.relianceit.relianceorder.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.relianceit.relianceorder.R;
import com.relianceit.relianceorder.appsupport.tab.SlidingTabLayout;
import com.relianceit.relianceorder.fragment.CustomerOutstandingFragment;
import com.relianceit.relianceorder.fragment.RelianceOperationFragment;
import com.relianceit.relianceorder.fragment.StockStatementFragment;

public class HomeActivity extends RelianceBaseActivity
		{
            public static final String TAG = HomeActivity.class.getSimpleName();
            /**
	 * The serialization (saved instance state) Bundle key representing the
	 * current dropdown position.
	 */
	private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";
    private Toolbar toolbar;
    SlidingTabLayout mTab;
    ViewPager mPager;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

//        final ActionBar actionBar = getSupportActionBar();
//        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//        actionBar.setCustomView(R.layout.action_bar_custom_layout);
        //toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
       // setSupportActionBar(toolbar);
        mTab =(SlidingTabLayout)findViewById(R.id.sliding_tabs);
        mPager=(ViewPager)findViewById(R.id.view_pager);
        mPager.setAdapter(new sectionPageAdapter(getSupportFragmentManager()));
        mTab.setViewPager(mPager);
        customizeActionBar(0);

/*
        // Set up the action bar to show a dropdown list.
		final ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);


		// Set up the dropdown list navigation in the action bar.
		actionBar.setListNavigationCallbacks(
		// Specify a SpinnerAdapter to populate the dropdown list.
				new ArrayAdapter<String>(actionBar.getThemedContext(),
						android.R.layout.simple_list_item_1,
						android.R.id.text1, new String[] {
								getString(R.string.title_section1),
								getString(R.string.title_section2),
								getString(R.string.title_section3), }), this);
								*/
	}


    private void customizeActionBar(int section){
        final ActionBar actionBar = getSupportActionBar();
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(//Center the textview in the ActionBar !
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);
        View viewActionBar = getLayoutInflater().inflate(R.layout.action_bar_custom_layout, null);
        TextView textViewTitle = (TextView) viewActionBar.findViewById(R.id.actionbar_textview);

        String titleText=getString(R.string.app_name);

        switch (section) {
            case 0:
            {
            }
            break;
            case 1:
            {
            }
            break;
            case 2:
            {
            }
            break;
            default:
                //titleText=titleText;
                break;
        }


        textViewTitle.setText(titleText);

        actionBar.setCustomView(viewActionBar,params);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
       // actionBar.setDisplayHomeAsUpEnabled(true);
       // actionBar.setHomeButtonEnabled(true);

    }


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}else if (id == R.id.action_logout) {
            Log.i(TAG, "Logout button tapped.");
            return true;
        }else if (id == R.id.action_refresh) {
            Log.i(TAG, "Refresh button tapped.");
            return true;
        }
		return super.onOptionsItemSelected(item);
	}
/*
	@Override
	public boolean onNavigationItemSelected(int position, long id) {
		// When the given dropdown item is selected, show its contents in the
		// container view.
        customizeActionBar(position);
		switch (position) {
		case 0:
		{
			FragmentManager fragmentManager = getSupportFragmentManager();

			RelianceOperationFragment fragment = new RelianceOperationFragment();
			fragmentManager.beginTransaction()
					.replace(R.id.container, fragment)
					.commit();
		}
			break;
		case 1:
		{
			FragmentManager fragmentManager = getSupportFragmentManager();

			StockStatementFragment fragment = new StockStatementFragment();
			fragmentManager.beginTransaction()
					.replace(R.id.container, fragment)
					.commit();
		}
			break;
		case 2:
		{
			FragmentManager fragmentManager = getSupportFragmentManager();

			CustomerOutstandingFragment fragment = new CustomerOutstandingFragment();
			fragmentManager.beginTransaction()
					.replace(R.id.container, fragment)
					.commit();
		}
			break;
		default:
		
			break;
		

		}

		return true;
	}
	*/
class sectionPageAdapter extends FragmentPagerAdapter {

    String [] tabs;
    public sectionPageAdapter(FragmentManager fm) {
        super(fm);
        tabs=getResources().getStringArray(R.array.navigation_section);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        customizeActionBar(position);
        switch (position) {
            case 0:
            {

               fragment = new RelianceOperationFragment();

            }
            break;
            case 1:
            {
               fragment = new StockStatementFragment();

            }
            break;
            case 2:
            {
               fragment = new CustomerOutstandingFragment();

            }
            break;
            default:
                fragment = new RelianceOperationFragment();
                break;


        }
        return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabs[position];
    }

    @Override
    public int getCount() {
        return tabs.length;
    }
}
	

}
