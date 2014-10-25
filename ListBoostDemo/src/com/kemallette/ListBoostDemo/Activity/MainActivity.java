package com.kemallette.ListBoostDemo.Activity;


import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.kemallette.ListBoost.ExpandableList.BoostExpandableList;
import com.kemallette.ListBoostDemo.R;

import de.greenrobot.event.EventBus;

public class MainActivity extends ActionBarActivity implements OnClickListener, 
															   OnCheckedChangeListener, android.widget.RadioGroup.OnCheckedChangeListener,
															   OnBackStackChangedListener{

	private static final String	        TAG	          = "MainActivity";
	private static final String	        INFO_FRAG	  = "InfoFrag";
	private static final String	        EXP_LIST_FRAG = "BoostElvFrag";
	private static final String	        LIST_FRAG	  = "BoostLvFrag";


	private TextView					mGroupChoiceOptionsHeading, mChildChoiceModesHeading;
	
	private ToggleButton	            mMultiChoiceToggle, mSlidingItemMenusToggle, mFabToggle;

	private CheckBox	                mOneChoiceOnlyCheck, mCheckChildrenWithGroup;
	private CheckBox	                mFabScrollModeToggle, mFabMenuToggle;
	private CheckBox	                mSlidingMenuGroupToggle, mSlidingMenuChildToggle;

	private RadioGroup	                mGroupChoiceModeRadios, mChildChoiceModeRadios;
	private RadioGroup	                mFabMenuDirectionRadios;

	private RadioButton	                mGroupModeMultiRadio, mGroupModeOneRadio, mGroupModeNoneRadio;
	private RadioButton	                mChildModeMultiRadio, mChildModeOnePerGroupRadio, mChildModeOneRadio, mChildModeNoneRadio;
	private RadioButton	                mFabMenuDirectionVerticalRadio, mFabMenuDirectionHorizontalRadio;

	
	private DrawerLayout	            mDrawerLayout;
	private ActionBarDrawerToggle	    mDrawerToggle;
	private ViewGroup	                mLeftDrawerView;
	private ViewGroup	                mRightDrawerView;

	private InfoFrag	                mInfoFrag;
	private BoostExpandableListFragment	mExpListFrag;
	private BoostListFragment	        mListFrag;
	private Fragment	                mCurrentFrag;
	private FragmentManager	            mFragManager;

	private EventBus	                mEventBus;


	@Override
	protected void onCreate(final Bundle saveInstanceState){

		super.onCreate(saveInstanceState);

		setContentView(R.layout.main_activity);

		mEventBus = EventBus.getDefault();
		mFragManager = getSupportFragmentManager();
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);

		mFragManager.addOnBackStackChangedListener(this);
		
		if (saveInstanceState == null){
			showInfoFrag();
		}else{
			mInfoFrag = (InfoFrag) mFragManager.findFragmentByTag(INFO_FRAG);
			mExpListFrag = (BoostExpandableListFragment) mFragManager.findFragmentByTag(EXP_LIST_FRAG);
			mListFrag = (BoostListFragment) mFragManager.findFragmentByTag(LIST_FRAG);
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
	protected void onResume(){

		super.onResume();

		setRightNavControlsEnabled(!mInfoFrag.equals(mCurrentFrag));
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


	/***********************************************************
	 * 
	 * View Initialization
	 * 
	 ************************************************************/


	private void setupNavDrawers(){

		if (mDrawerLayout == null || mLeftDrawerView == null || mRightDrawerView == null || mDrawerToggle == null){

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
		final TextView listViewNavItem = (TextView) findViewById(R.id.list_view_nav_item);
		final TextView expandableListNavItem = (TextView) findViewById(R.id.expandable_list_view_nav_item);

		welcomeNavItem.setOnClickListener(this);
		listViewNavItem.setOnClickListener(this);
		expandableListNavItem.setOnClickListener(this);
	}


	private void initRightDrawerViews(){

		mGroupChoiceOptionsHeading = (TextView) findViewById(R.id.group_choice_options_heading);
		mChildChoiceModesHeading = (TextView) findViewById(R.id.child_choice_options_heading);
		
		// MultiChoice Controls
		mMultiChoiceToggle = (ToggleButton) findViewById(R.id.toggle_multichoice);
		mMultiChoiceToggle.setOnCheckedChangeListener(this);

		mOneChoiceOnlyCheck = (CheckBox) findViewById(R.id.toggle_one_item_only);
		mCheckChildrenWithGroup = (CheckBox) findViewById(R.id.toggle_check_children_with_group);
		mCheckChildrenWithGroup.setOnCheckedChangeListener(this);
		mOneChoiceOnlyCheck.setOnCheckedChangeListener(this);

		mGroupChoiceModeRadios = (RadioGroup) findViewById(R.id.group_choice_modes_radios);
		mChildChoiceModeRadios = (RadioGroup) findViewById(R.id.child_choice_modes_radios);
		mGroupChoiceModeRadios.setOnCheckedChangeListener(this);
		mChildChoiceModeRadios.setOnCheckedChangeListener(this);

		mGroupModeMultiRadio = (RadioButton) findViewById(R.id.group_multichoice_radio);
		mGroupModeOneRadio = (RadioButton) findViewById(R.id.group_one_choice_radio);
		mGroupModeNoneRadio = (RadioButton) findViewById(R.id.group_no_choice_radio);

		mChildModeMultiRadio = (RadioButton) findViewById(R.id.child_multichoice_radio);
		mChildModeOnePerGroupRadio = (RadioButton) findViewById(R.id.child_one_per_group_choice_radio);
		mChildModeOneRadio = (RadioButton) findViewById(R.id.child_one_choice_radio);
		mChildModeNoneRadio = (RadioButton) findViewById(R.id.child_no_choice_radio);


		// Sliding Item Menus Controls
		mSlidingItemMenusToggle = (ToggleButton) findViewById(R.id.toggle_sliding_item_menus);
		mSlidingItemMenusToggle.setOnCheckedChangeListener(this);

		mSlidingMenuGroupToggle = (CheckBox) findViewById(R.id.toggle_group_sliding_menus);
		mSlidingMenuChildToggle = (CheckBox) findViewById(R.id.toggle_child_sliding_menus);
		mSlidingMenuGroupToggle.setOnCheckedChangeListener(this);
		mSlidingMenuChildToggle.setOnCheckedChangeListener(this);


		// FAB controls
		mFabToggle = (ToggleButton) findViewById(R.id.toggle_fab);
		mFabToggle.setOnCheckedChangeListener(this);

		mFabScrollModeToggle = (CheckBox) findViewById(R.id.fab_scroll_mode_toggle);
		mFabMenuToggle = (CheckBox) findViewById(R.id.toggle_fab_menus);
		mFabScrollModeToggle.setOnCheckedChangeListener(this);
		mFabMenuToggle.setOnCheckedChangeListener(this);

		mFabMenuDirectionRadios = (RadioGroup) findViewById(R.id.fab_menu_direction_radios);
		mFabMenuDirectionRadios.setOnCheckedChangeListener(this);

		mFabMenuDirectionHorizontalRadio = (RadioButton) findViewById(R.id.fab_menu_horizontal_radio);
		mFabMenuDirectionVerticalRadio = (RadioButton) findViewById(R.id.fab_menu_vertical_radio);
	}
	
	/******************************************
	 * Backstack Change Listener
	 *****************************************/
	@Override
	public void onBackStackChanged(){
		if(mInfoFrag != null && mRightDrawerView != null) {
			if(mInfoFrag.isVisible()) {
				resetRightNavControls();
				setRightNavControlsEnabled(false);
			}else if(mListFrag != null && mListFrag.isVisible()){
				hideExpandableListOptions();
			}else if(mExpListFrag != null && mExpListFrag.isVisible()){
				showExpandableListOptions();
			}
		}
	}

	/******************************************
	 * Click Listener
	 *****************************************/
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

			// Fab Menu direction changes
			case R.id.fab_menu_horizontal_radio:
				break;

			case R.id.fab_menu_vertical_radio:
				break;
		}
	}


	/******************************************
	 * CompoundButton Checked Change
	 *****************************************/
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){

		
		switch(buttonView.getId()){

			// Feature Toggles
			case R.id.toggle_multichoice:
				
				if(isChecked) {
					if(!mOneChoiceOnlyCheck.isChecked()) {
						setChoiceOptionControlsEnabled(true);
					}else {
						mOneChoiceOnlyCheck.setEnabled(true);
					}
				}else {
					setChoiceOptionControlsEnabled(false);
				}
				mEventBus.post(new EnableMultiChoiceToggleEvent(isChecked));
				break;

			case R.id.toggle_sliding_item_menus:
				setSlidingMenuOptionControlsEnabled(isChecked);
				mEventBus.post(new EnableSlidingItemMenuToggleEvent(isChecked));
				break;

			case R.id.toggle_fab:
				setFabOptionControlsEnable(isChecked);
				mEventBus.post(new EnableFabToggleEvent(isChecked));
				break;

			// MultiChoice Options Toggles
			case R.id.toggle_one_item_only:
				mCheckChildrenWithGroup.setEnabled(!isChecked);
				setGroupChoiceModeControlsEnabled(!isChecked);
				setChildChoiceModeControlsEnabled(!isChecked);
				if(!isChecked && mCheckChildrenWithGroup.isChecked()) {
					setChildChoiceModeControlsEnabled(false);
					mChildModeMultiRadio.setEnabled(true);
					mChildModeMultiRadio.setChecked(true);
				}
				mEventBus.post(new ExpandableListOneItemOnlyToggleEvent(isChecked));
				break;

			case R.id.toggle_check_children_with_group:
				if (isChecked){
					setChildChoiceModeControlsEnabled(false);
					mChildModeMultiRadio.setEnabled(true);
					mChildModeMultiRadio.setChecked(true);
				}else{
					setChildChoiceModeControlsEnabled(true);
				}
				mEventBus.post(new CheckChildrenWithGroupToggleEvent(isChecked));
				break;


			// FAB Options Toggles
			case R.id.toggle_fab_menus:
				mEventBus.post(new EnableFabMenuToggleEvent(isChecked));
				break;
			case R.id.fab_scroll_mode_toggle:
				mEventBus.post(new FabScrollModeToggleEvent(isChecked));
				break;
		}
	}


	/***********************************************************
	 * 
	 * Show/Hide/Enable/Disable/Reset Right Nav Drawer Controls
	 * 
	 ************************************************************/
	private void hideExpandableListOptions(){

		mSlidingMenuChildToggle.setVisibility(View.GONE);
		mSlidingMenuGroupToggle.setVisibility(View.GONE);
		mOneChoiceOnlyCheck.setVisibility(View.GONE);
		mCheckChildrenWithGroup.setVisibility(View.GONE);
		mGroupChoiceOptionsHeading.setText(getResources().getString(R.string.choice_modes));
		mChildChoiceModesHeading.setVisibility(View.GONE);
		mChildChoiceModeRadios.setVisibility(View.GONE);
    }
	private void showExpandableListOptions(){

//		mSlidingMenuChildToggle.setVisibility(View.VISIBLE);
//		mSlidingMenuGroupToggle.setVisibility(View.VISIBLE);
		
		mSlidingItemMenusToggle.setEnabled(false);
		mOneChoiceOnlyCheck.setVisibility(View.VISIBLE);
		mCheckChildrenWithGroup.setVisibility(View.VISIBLE);
		mChildChoiceModeRadios.setVisibility(View.VISIBLE);
		mChildChoiceModesHeading.setVisibility(View.VISIBLE);
		mGroupChoiceOptionsHeading.setText(getResources().getString(R.string.group_choice_modes_heading));
    }

	private void setRightNavControlsEnabled(boolean enable){
		setRightNavControlsEnabled(enable, enable);
	}

	
	private void setRightNavControlsEnabled(boolean enableFeatureToggles, boolean enableFeatureOptionsControls){

		mMultiChoiceToggle.setEnabled(enableFeatureToggles);
		mSlidingItemMenusToggle.setEnabled(enableFeatureToggles);
		mFabToggle.setEnabled(enableFeatureToggles);

		setChoiceOptionControlsEnabled(enableFeatureOptionsControls);
		setSlidingMenuOptionControlsEnabled(enableFeatureOptionsControls);
		setFabOptionControlsEnable(enableFeatureOptionsControls);
	}


	// MultiChoice Controls

	private void setChoiceOptionControlsEnabled(boolean enable){

		mOneChoiceOnlyCheck.setEnabled(enable);
		mCheckChildrenWithGroup.setEnabled(enable);

		setGroupChoiceModeControlsEnabled(enable);
		setChildChoiceModeControlsEnabled(enable);
	}


	private void setChildChoiceModeControlsEnabled(boolean enable){

		mChildModeMultiRadio.setEnabled(enable);
		mChildModeOneRadio.setEnabled(enable);
		mChildModeOnePerGroupRadio.setEnabled(enable);
		mChildModeNoneRadio.setEnabled(enable);
	}


	private void setGroupChoiceModeControlsEnabled(boolean enable){

		mGroupModeMultiRadio.setEnabled(enable);
		mGroupModeOneRadio.setEnabled(enable);
		mGroupModeNoneRadio.setEnabled(enable);
	}


	// Fab Controls

	private void setFabOptionControlsEnable(boolean enable){

		mFabScrollModeToggle.setEnabled(enable);
		mFabMenuToggle.setEnabled(enable);
		mFabMenuDirectionHorizontalRadio.setEnabled(enable);
		mFabMenuDirectionVerticalRadio.setEnabled(enable);
	}


	// Sliding Item Menu Controls

	private void setSlidingMenuOptionControlsEnabled(boolean enable){

		mSlidingMenuGroupToggle.setEnabled(enable);
		mSlidingMenuChildToggle.setEnabled(enable);
	}

	private void resetRightNavControls(){
		resetMultiChoiceControls();
		resetSlidingMenuControls();
		resetFabControls();
    }

	private void resetMultiChoiceControls(){
		mMultiChoiceToggle.setChecked(false);
		mOneChoiceOnlyCheck.setChecked(false);
		mCheckChildrenWithGroup.setChecked(false);
		mGroupChoiceModeRadios.check(mGroupModeMultiRadio.getId());
		mChildChoiceModeRadios.check(mChildModeMultiRadio.getId());
	}
	
	private void resetSlidingMenuControls(){
		mSlidingItemMenusToggle.setChecked(false);
		mSlidingMenuGroupToggle.setChecked(false);
		mSlidingMenuChildToggle.setChecked(false);
	}
	
	private void resetFabControls(){
		mFabToggle.setChecked(false);
		mFabScrollModeToggle.setChecked(false);
	}
	
	/***********************************************************
	 * 
	 * Show Frags
	 * 
	 ************************************************************/

	private void showInfoFrag(){

		if (mInfoFrag == null)
			mInfoFrag = InfoFrag.newInstance();

		mCurrentFrag = mInfoFrag;

		final FragmentTransaction mTransaction =
		                                         mFragManager.beginTransaction()
		                                                     .replace(R.id.container, mInfoFrag, INFO_FRAG)
		                                                     .setTransition(FragmentTransaction.TRANSIT_NONE);

		mTransaction.commit();
		
		if(mRightDrawerView != null) {
			setRightNavControlsEnabled(false);
		}
	}


	private void showExpandableListFrag(){

		if (mExpListFrag == null)
			mExpListFrag = BoostExpandableListFragment.newInstance();

		mCurrentFrag = mExpListFrag;

		final FragmentTransaction mTransaction =
		                                         mFragManager.beginTransaction()
		                                                     .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
		                                                     .replace(R.id.container, mExpListFrag, EXP_LIST_FRAG)
		                                                     .addToBackStack(null);

		mTransaction.commit();

		setRightNavControlsEnabled(true, false);
	}


	private void showListFrag(){

		if (mListFrag == null)
			mListFrag = BoostListFragment.newInstance();

		mCurrentFrag = mListFrag;

		final FragmentTransaction mTransaction =
		                                         mFragManager.beginTransaction()
		                                                     .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
		                                                     .replace(R.id.container, mListFrag, LIST_FRAG)
		                                                     .addToBackStack(null);

		mTransaction.commit();
		
		setRightNavControlsEnabled(true, false);
		hideExpandableListOptions();
	}

	/***********************************************************
	 * 
	 * EventBus events objects for feature toggling
	 * 
	 ************************************************************/

	protected abstract class ToggleEvent{

		protected boolean	toggled;


		ToggleEvent(boolean toggled){

			this.toggled = toggled;
		}
	}


	// Enable Features Toggle Events

	protected class EnableMultiChoiceToggleEvent extends ToggleEvent{

		protected EnableMultiChoiceToggleEvent(boolean toggled){

			super(toggled);
		}

	}

	protected class EnableSlidingItemMenuToggleEvent extends ToggleEvent{

		protected EnableSlidingItemMenuToggleEvent(boolean toggled){

			super(toggled);
		}
	}

	protected class EnableFabToggleEvent extends ToggleEvent{

		protected EnableFabToggleEvent(boolean toggled){

			super(toggled);
		}
	}


	// MultiChoice Options Events

	protected class ExpandableListOneItemOnlyToggleEvent extends ToggleEvent{

		protected ExpandableListOneItemOnlyToggleEvent(boolean toggled){

			super(toggled);
		}
	}

	protected class CheckChildrenWithGroupToggleEvent extends ToggleEvent{

		protected CheckChildrenWithGroupToggleEvent(boolean toggled){

			super(toggled);
		}
	}

	protected class ListChoiceModeChangeEvent{

		protected final int	choiceMode;


		protected ListChoiceModeChangeEvent(final int choiceMode){

			this.choiceMode = choiceMode;
		}
	}

	protected class GroupChoiceModeChangeEvent{

		protected final int	choiceMode;


		protected GroupChoiceModeChangeEvent(final int choiceMode){

			this.choiceMode = choiceMode;
		}
	}

	protected class ChildChoiceModeChangeEvent{

		protected final int	choiceMode;


		protected ChildChoiceModeChangeEvent(final int choiceMode){

			this.choiceMode = choiceMode;
		}
	}


	// Sliding Menus Options Events

	protected class GroupItemMenusToggleEvent extends ToggleEvent{

		protected GroupItemMenusToggleEvent(boolean toggled){

			super(toggled);
		}
	}

	protected class ChildItemMenusToggleEvent extends ToggleEvent{

		protected ChildItemMenusToggleEvent(boolean toggled){

			super(toggled);
		}
	}


	// FAB Options Events
	protected class FabScrollModeToggleEvent extends ToggleEvent{

		protected FabScrollModeToggleEvent(boolean toggled){

			super(toggled);
		}
	}

	protected class EnableFabMenuToggleEvent extends ToggleEvent{

		protected EnableFabMenuToggleEvent(boolean toggled){

			super(toggled);
		}
	}

	protected class FabMenuDirectionChangeEvent{

		protected final int	direction;


		protected FabMenuDirectionChangeEvent(final int direction){

			this.direction = direction;
		}
	}
}