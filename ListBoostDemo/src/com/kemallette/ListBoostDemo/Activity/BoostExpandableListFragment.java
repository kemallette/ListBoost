package com.kemallette.ListBoostDemo.Activity;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;
import android.widget.Toast;

import com.kemallette.ListBoost.ExpandableList.BoostExpandableList;
import com.kemallette.ListBoost.ExpandableList.BoostExpandableListView;
import com.kemallette.ListBoost.ExpandableList.ExpandableListCheckListener;
import com.kemallette.ListBoostDemo.R;
import com.kemallette.ListBoostDemo.Activity.MainActivity.CheckChildrenWithGroupToggleEvent;
import com.kemallette.ListBoostDemo.Activity.MainActivity.ChildChoiceModeChangeEvent;
import com.kemallette.ListBoostDemo.Activity.MainActivity.ChildItemMenusToggleEvent;
import com.kemallette.ListBoostDemo.Activity.MainActivity.EnableFabMenuToggleEvent;
import com.kemallette.ListBoostDemo.Activity.MainActivity.EnableFabToggleEvent;
import com.kemallette.ListBoostDemo.Activity.MainActivity.EnableMultiChoiceToggleEvent;
import com.kemallette.ListBoostDemo.Activity.MainActivity.EnableSlidingItemMenuToggleEvent;
import com.kemallette.ListBoostDemo.Activity.MainActivity.ExpandableListOneItemOnlyToggleEvent;
import com.kemallette.ListBoostDemo.Activity.MainActivity.FabMenuDirectionChangeEvent;
import com.kemallette.ListBoostDemo.Activity.MainActivity.FabScrollModeToggleEvent;
import com.kemallette.ListBoostDemo.Activity.MainActivity.GroupChoiceModeChangeEvent;
import com.kemallette.ListBoostDemo.Activity.MainActivity.GroupItemMenusToggleEvent;
import com.kemallette.ListBoostDemo.Adapter.ExampleAdapter;
import com.kemallette.ListBoostDemo.Fab.FloatingActionButton;

import de.greenrobot.event.EventBus;


public class BoostExpandableListFragment extends Fragment implements ExpandableListCheckListener{

	private static final String	    TAG	            = "BoostExpandableListFrag";

	
	private boolean fabScrollingEnabled = false,
					fabMenuEnabled = false,
					fabEnabled = false;
	
	private boolean isOneItemChoiceOnlyEnabled = false,
					 checkChildrenWithGroup = false;
	
	private int	                    childChoiceMode	=
	                                                  BoostExpandableList.CHECK_MODE_MULTI;
	private int	                    groupChoiceMode	=
	                                                  BoostExpandableList.CHECK_MODE_MULTI;

	private FloatingActionButton    mFab;
	private BoostExpandableListView	mList;
	private ExampleAdapter	        mAdapter;


	public static BoostExpandableListFragment newInstance(){

		return new BoostExpandableListFragment();
	}


	@Override
	public void onCreate(final Bundle savedInstanceState){

		super.onCreate(savedInstanceState);
		
		if(!EventBus.getDefault().isRegistered(this))
			EventBus.getDefault().register(this);
	}


	@Override
	public void onSaveInstanceState(final Bundle outState){

		super.onSaveInstanceState(outState);
	}


	@Override
	public View onCreateView(final LayoutInflater inflater,
	                         final ViewGroup container,
	                         final Bundle savedInstanceState){

		return inflater.inflate(R.layout.expandable_list_frag,
		                        container,
		                        false);
	}


	@Override
	public void onViewCreated(final View view, final Bundle savedInstanceState){

		super.onViewCreated(view,
		                    savedInstanceState);

		mList = (BoostExpandableListView) getView().findViewById(R.id.list);
		mList.setExpandableCheckListener(this);

		mFab = (FloatingActionButton) getView().findViewById(R.id.fab);
		
		mAdapter = new ExampleAdapter(getActivity());
		mList.setAdapter(mAdapter);
	}


	@Override
	public void onResume(){

		super.onResume();

	}


	/*********************************************************************
	 * ExpandableListCheckListener
	 **********************************************************************/
	@Override
	public void onGroupCheckChange(final Checkable checkedView,
	                               final int groupPosition,
	                               final long groupId,
	                               final boolean isChecked){

		Toast.makeText(getActivity(),
		               "Group Check Change\nid: " + groupId + "\n isChecked: "
		                   + isChecked,
		               Toast.LENGTH_SHORT)
		     .show();
	}


	@Override
	public void onChildCheckChange(final Checkable checkedView,
	                               final int groupPosition,
	                               final long groupId,
	                               final int childPosition,
	                               final long childId,
	                               final boolean isChecked){

		Toast.makeText(getActivity(),
		               "Child Check Change\nid: " + childId + "\n isChecked: "
		                   + isChecked,
		               Toast.LENGTH_SHORT)
		     .show();

	}


	/***********************************************************
	 * 
	 * EventBus event listeners
	 * 
	 ************************************************************/

	
	/******************************************
	* MultiChoice Events
	*****************************************/
	
	// Enable/Disable MultiChoice
	public void onEvent(EnableMultiChoiceToggleEvent event){

		if (event.toggled){ // Enable MultiChoice
			mList.enableChoice(groupChoiceMode,
			                   childChoiceMode);
			if(isOneItemChoiceOnlyEnabled) {
				mList.enableOnlyOneItemChoice(isOneItemChoiceOnlyEnabled);
			}else if(checkChildrenWithGroup) {
				mList.checkChildrenWithGroup(checkChildrenWithGroup);
			}
		}else{
			mList.disableChoice();
		}
	}
	
	// Enable/Disable Checking Children on Parent Group Check
	public void onEvent(CheckChildrenWithGroupToggleEvent event){
		
		checkChildrenWithGroup = event.toggled;
		
		if(event.toggled != mList.checkChildrenWithGroup())
			mList.checkChildrenWithGroup(event.toggled);
	}
	
	public void onEvent(ExpandableListOneItemOnlyToggleEvent event){
		
		isOneItemChoiceOnlyEnabled = event.toggled;
		
		if(isOneItemChoiceOnlyEnabled)
			mList.enableOnlyOneItemChoice(true);
		
		if(!isOneItemChoiceOnlyEnabled) {
			mList.enableChoice(groupChoiceMode, childChoiceMode);
			if(checkChildrenWithGroup) {
				mList.checkChildrenWithGroup(checkChildrenWithGroup);
			}
		}
	}
	
	// Set Group Items Choice Mode
	public void onEvent(GroupChoiceModeChangeEvent event){

		groupChoiceMode = event.choiceMode;
		
		switch(event.choiceMode){

			case BoostExpandableList.CHECK_MODE_NONE:
			case BoostExpandableList.CHECK_MODE_MULTI:
			case BoostExpandableList.CHECK_MODE_ONE:
				if(mList.getGroupChoiceMode() != groupChoiceMode)
					mList.setGroupChoiceMode(groupChoiceMode);
				break;
		}
	}
	
	// Set Child Items Choice Mode
	public void onEvent(ChildChoiceModeChangeEvent event){
		
		childChoiceMode = event.choiceMode;
		
		switch(event.choiceMode){

			case BoostExpandableList.CHECK_MODE_NONE:
			case BoostExpandableList.CHECK_MODE_MULTI:
			case BoostExpandableList.CHECK_MODE_ONE:
			case BoostExpandableList.CHECK_MODE_ONE_CHILD_PER_GROUP:
				if(mList.getChildChoiceMode() != childChoiceMode)
					mList.setChildChoiceMode(childChoiceMode);
				break;
		}
	}
	
	
	/******************************************
	* Sliding Item Menu Events
	*****************************************/
	public void onEvent(EnableSlidingItemMenuToggleEvent event) {
		//TODO
	}
	
	public void onEvent(GroupItemMenusToggleEvent event) {
		//TODO
	}


	public void onEvent(ChildItemMenusToggleEvent event) {
		//TODO
	}

	
	/******************************************
	* FAB Events
	*****************************************/

	public void onEvent(EnableFabToggleEvent event) {
		
		fabEnabled = event.toggled;
		
		if(fabEnabled) {
    		mFab.setVisibility(View.VISIBLE);
    		if(fabScrollingEnabled) {
    			mFab.respondTo(mList);
    		}else {
    			mFab.disableScrolling();
    		}
    		// TODO: Blocked by fab menu implementation: if(fabMenuEnabled) mFab.enableMenu(); 
    	}else {
    		mFab.setVisibility(View.INVISIBLE);
    		fabEnabled = false;
    	}
	}
	
	
	public void onEvent(EnableFabMenuToggleEvent event) {
    	fabMenuEnabled = event.toggled;
    	//TODO: Blocked by fab menu implementation
	}
	
    public void onEvent(FabScrollModeToggleEvent event) {
    	
    	if(fabScrollingEnabled != event.toggled)
    		fabScrollingEnabled = event.toggled;

    	if(fabScrollingEnabled) {
    		mFab.respondTo(mList);
    	}else {
    		mFab.disableScrolling();
    	}
    }  


    public void onEvent(FabMenuDirectionChangeEvent event) {
    	//TODO: Blocked by FAB menu implementation
	}
    

      
}
