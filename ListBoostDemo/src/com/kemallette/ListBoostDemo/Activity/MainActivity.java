package com.kemallette.ListBoostDemo.Activity;


import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.kemallette.ListBoost.ExpandableList.BoostExpandableList;
import com.kemallette.ListBoostDemo.R;

import de.greenrobot.event.EventBus;

public class MainActivity extends ActionBarActivity implements OnClickListener, OnCheckedChangeListener, android.widget.RadioGroup.OnCheckedChangeListener{

	private static final String	TAG	          = "MainActivity";
	private static final String	INFO_FRAG	  = "infoFrag";
	private static final String	EXP_LIST_FRAG = "elvFrag";
	private static final String	LIST_FRAG	  = "lvFrag";

	public static final String	SWIPE	      = "swipe";
	public static final String	SLIDE	      = "slide";
	public static final String	DRAGDROP	  = "dragdrop";
	public static final String	MULTICHOICE	  = "multichoice";

	public enum ListType {
		LISTVIEW,
		EXPANDABLE_LISTVIEW
	}

	private ToggleButton mMultiChoiceToggle, mSlidingItemMenusToggle, mFabToggle;
	
	private CheckBox mOneChoiceOnlyCheck, mCheckChildrenWithGroup;
	
	private RadioGroup mGroupChoiceModeRadios, mChildChoiceModeRadios;
	
	private DrawerLayout	            mDrawerLayout;
	private ActionBarDrawerToggle	    mDrawerToggle;
	private ViewGroup	                mLeftDrawerView;
	private ViewGroup	                mRightDrawerView;

	private InfoFrag	                mInfoFrag;
	private BoostExpandableListFragment	mExpListFrag;
	private BoostListFragment	        mListFrag;
	
	private EventBus mEventBus;


	@Override
	protected void onCreate(final Bundle saveInstanceState){

		super.onCreate(saveInstanceState);

		setContentView(R.layout.main_activity);
		
		mEventBus = EventBus.getDefault();

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);

		if (saveInstanceState == null)
			showInfoFrag();
		else{
			mInfoFrag = (InfoFrag) getSupportFragmentManager().findFragmentByTag(INFO_FRAG);
			mExpListFrag = (BoostExpandableListFragment) getSupportFragmentManager().findFragmentByTag(EXP_LIST_FRAG);
			mListFrag = (BoostListFragment) getSupportFragmentManager().findFragmentByTag(LIST_FRAG);
		}
	}


	@Override
	protected void onPostCreate(Bundle savedInstanceState){

		super.onPostCreate(savedInstanceState);

		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}


	@Override
	protected void onStart(){
		super.onStart();
		
		setupNavDrawers();
	}


	@Override
	public boolean onPrepareOptionsMenu(Menu menu){

		// If the nav drawer is open, hide action items related to the content view
		for (int i = 0; i < menu.size(); i++)
			menu.getItem(i).setVisible(!mDrawerLayout.isDrawerOpen(mLeftDrawerView));

		return super.onPrepareOptionsMenu(menu);
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item){

		switch(item.getItemId()){
			case android.R.id.home:
				mDrawerToggle.onOptionsItemSelected(item);

				if (mDrawerLayout.isDrawerOpen(mRightDrawerView))
					mDrawerLayout.closeDrawer(mRightDrawerView);

				return true;
		}

		return super.onOptionsItemSelected(item);
	}


	@Override
	public void onConfigurationChanged(Configuration newConfig){

		super.onConfigurationChanged(newConfig);

		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	

	@Override
    public void onClick(View v){
		final int id = v.getId();
		
		switch(id){
			case R.id.welcome:
				showInfoFrag();
				break;
			case R.id.list_view_nav_item:
				showListFrag();
				break;
			case R.id.expandable_list_view_nav_item:
				showExpandableListFrag();
				break;
		}
    }

	/******************************************
	* RadioGroup Checked Changed 
	*****************************************/
	@Override
    public void onCheckedChanged(RadioGroup group, int checkedId){
		
		switch(checkedId){
			// Group Choice Mode Changes
			case R.id.group_multichoice_radio:
				mEventBus.post(new GroupChoiceModeChangeEvent(BoostExpandableList.CHECK_MODE_MULTI));
				break;
				
			case R.id.group_one_choice_radio:
				mEventBus.post(new GroupChoiceModeChangeEvent(BoostExpandableList.CHECK_MODE_ONE));
				break;
				
			case R.id.group_no_choice_radio:
				mEventBus.post(new GroupChoiceModeChangeEvent(BoostExpandableList.CHECK_MODE_NONE));
				break;
				
			// Child Choice mode changes
			case R.id.child_multichoice_radio:
				mEventBus.post(new ChildChoiceModeChangeEvent(BoostExpandableList.CHECK_MODE_MULTI));
				break;

			case R.id.child_one_per_group_choice_radio:
				mEventBus.post(new ChildChoiceModeChangeEvent(BoostExpandableList.CHECK_MODE_ONE_CHILD_PER_GROUP));
				break;
				
			case R.id.child_one_choice_radio:
				mEventBus.post(new ChildChoiceModeChangeEvent(BoostExpandableList.CHECK_MODE_ONE));
				break;
				
			case R.id.child_no_choice_radio:
				mEventBus.post(new ChildChoiceModeChangeEvent(BoostExpandableList.CHECK_MODE_NONE));
				break;
				
		}
    }
	
	/******************************************
	* CompoundButton Checked Change 
	*****************************************/
	@Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
		
		final int id = buttonView.getId();
		switch(id){
			// Features
			case R.id.toggle_sliding_item_menus:
				mEventBus.post(new SlidingItemMenuToggleEvent(isChecked));
				break;
			case R.id.toggle_fab:
				mEventBus.post(new FabToggleEvent(isChecked));
				break;
			case R.id.toggle_multichoice:
				mEventBus.post(new MultiChoiceToggleEvent(isChecked));
				break;
				
			// MultiChoice Options
			case R.id.toggle_one_item_only:
				mEventBus.post(new ExpandableListOneItemOnlyToggleEvent(isChecked));
				break;
			case R.id.toggle_check_children_with_group:
				mEventBus.post(new CheckChildrenWithGroupToggleEvent(isChecked));
				break;
		}
    }
	
	private void setupNavDrawers(){

		if (mDrawerLayout == null || 
			mLeftDrawerView == null || 
			mRightDrawerView == null || 
			mDrawerToggle == null){
			
			// Configure navigation drawer
			mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
			mLeftDrawerView = (ViewGroup) findViewById(R.id.left_nav_drawer_content);
			mRightDrawerView = (ViewGroup) findViewById(R.id.right_nav_drawer_content);
			mDrawerToggle = new ActionBarDrawerToggle(this,
			                                          mDrawerLayout,
			                                          R.drawable.ic_drawer,
			                                          R.string.app_name,
			                                          R.string.welcome_and_instructions){

				/** Called when a drawer has settled in a completely closed state. */
				public void onDrawerClosed(View drawerView){

					if (drawerView.equals(mLeftDrawerView)){
						getSupportActionBar().setTitle(getTitle());
						supportInvalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
						mDrawerToggle.syncState();
					}
				}


				/** Called when a drawer has settled in a completely open state. */
				public void onDrawerOpened(View drawerView){

					if (drawerView.equals(mLeftDrawerView)){
						getSupportActionBar().setTitle(getString(R.string.app_name));
						supportInvalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
						mDrawerToggle.syncState();
					}
				}


				@Override
				public void onDrawerSlide(View drawerView, float slideOffset){

					// Avoid normal indicator glyph behaviour. This is to avoid glyph movement when
					// opening the right drawer
					// super.onDrawerSlide(drawerView, slideOffset);
				}
			};

			mDrawerLayout.setDrawerListener(mDrawerToggle); // Set the drawer toggle as the
															// DrawerListener
		}
		
		initLeftDrawerViews();
		initRightDrawerViews();
	}
	
	private void initLeftDrawerViews(){
		final TextView welcomeNavItem = (TextView) findViewById(R.id.welcome);
		final TextView listViewNavItem = (TextView)findViewById(R.id.list_view_nav_item);
		final TextView expandableListNavItem = (TextView) findViewById(R.id.expandable_list_view_nav_item);
		
		welcomeNavItem.setOnClickListener(this);
		listViewNavItem.setOnClickListener(this);
		expandableListNavItem.setOnClickListener(this);
	}
	
	
    private void initRightDrawerViews(){
    	
    	// MultiChoice Controls
    	mMultiChoiceToggle = (ToggleButton) findViewById(R.id.toggle_multichoice);
    	mMultiChoiceToggle.setOnCheckedChangeListener(this);
    	
    	mOneChoiceOnlyCheck = (CheckBox) findViewById(R.id.toggle_one_item_only);
    	mCheckChildrenWithGroup= (CheckBox) findViewById(R.id.toggle_check_children_with_group);
    	mCheckChildrenWithGroup.setOnCheckedChangeListener(this);    	
    	mOneChoiceOnlyCheck.setOnCheckedChangeListener(this);   
    	
    	mGroupChoiceModeRadios = (RadioGroup) findViewById(R.id.group_choice_modes_radios);
    	mChildChoiceModeRadios = (RadioGroup) findViewById(R.id.child_choice_modes_radios);
    	mGroupChoiceModeRadios.setOnCheckedChangeListener(this);
    	mChildChoiceModeRadios.setOnCheckedChangeListener(this);
    	
    	// Sliding Item Menus Controls
    	mSlidingItemMenusToggle = (ToggleButton) findViewById(R.id.toggle_sliding_item_menus);
    	mSlidingItemMenusToggle.setOnCheckedChangeListener(this);
    	
    	
    	// FAB controls
    	mFabToggle = (ToggleButton) findViewById(R.id.toggle_fab);
    	mFabToggle.setOnCheckedChangeListener(this);
    	
    }
	
	private void showInfoFrag(){

		if(mInfoFrag == null)
			mInfoFrag = InfoFrag.newInstance();

		final FragmentTransaction mTransaction = getSupportFragmentManager()
			.beginTransaction()
            .replace(R.id.container, mInfoFrag, INFO_FRAG)
            .setTransition(FragmentTransaction.TRANSIT_NONE);

		mTransaction.commit();
	}


	private void showExpandableListFrag(){

		if (mExpListFrag == null)
			mExpListFrag = BoostExpandableListFragment.newInstance();

		final FragmentTransaction mTransaction = getSupportFragmentManager()
			.beginTransaction()
		    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
		    .replace(R.id.container, mExpListFrag, EXP_LIST_FRAG)
		    .addToBackStack(null);

		mTransaction.commit();
	}


	private void showListFrag(){

		if (mListFrag == null)
			mListFrag = BoostListFragment.newInstance();

		final FragmentTransaction mTransaction = getSupportFragmentManager()
			.beginTransaction()
		    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
		    .replace(R.id.container, mListFrag, LIST_FRAG)
		    .addToBackStack(null);
		
		mTransaction.commit();
	}

	
	/***********************************************************
	 *
	 * EventBus events objects for feature toggling
 	 *
 	 ************************************************************/
	
	protected abstract class ToggleEvent{
		
		protected boolean toggled;
		
		ToggleEvent(boolean toggled){
			this.toggled = toggled;
		}
	}
	
	protected class MultiChoiceToggleEvent extends ToggleEvent{
		
		protected MultiChoiceToggleEvent(boolean toggled){
			super(toggled);
		}
		
	}
	
	protected class SlidingItemMenuToggleEvent extends ToggleEvent{
		
		protected SlidingItemMenuToggleEvent(boolean toggle){
			super(toggle);
		}
	}
	

	protected class ExpandableListOneItemOnlyToggleEvent extends ToggleEvent{
		
		protected ExpandableListOneItemOnlyToggleEvent(boolean toggle){
			super(toggle);
		}
	}
	
	

	protected class CheckChildrenWithGroupToggleEvent extends ToggleEvent{
		
		protected CheckChildrenWithGroupToggleEvent(boolean toggle){
			super(toggle);
		}
	}
	
	protected class FabToggleEvent extends ToggleEvent{
		
		protected FabToggleEvent(boolean toggle){
			super(toggle);
		}
	}
	
	protected class ListChoiceModeChangeEvent{
		
		protected final int choiceMode;
		
		protected ListChoiceModeChangeEvent(final int choiceMode){
			this.choiceMode = choiceMode;
		}
	}
	
	protected class GroupChoiceModeChangeEvent{
		
		protected final int choiceMode;
		
		protected GroupChoiceModeChangeEvent(final int choiceMode){
			this.choiceMode = choiceMode;
		}
	}
	
	protected class ChildChoiceModeChangeEvent{
		
		protected final int choiceMode;
		
		protected ChildChoiceModeChangeEvent(final int choiceMode){
			this.choiceMode = choiceMode;
		}
	}
}