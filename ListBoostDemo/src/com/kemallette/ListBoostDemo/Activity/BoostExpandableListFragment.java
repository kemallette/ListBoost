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
import com.kemallette.ListBoostDemo.Activity.MainActivity.ExpandableListOneItemOnlyToggleEvent;
import com.kemallette.ListBoostDemo.Activity.MainActivity.GroupChoiceModeChangeEvent;
import com.kemallette.ListBoostDemo.Activity.MainActivity.MultiChoiceToggleEvent;
import com.kemallette.ListBoostDemo.Adapter.ExampleAdapter;

import de.greenrobot.event.EventBus;


public class BoostExpandableListFragment extends Fragment implements ExpandableListCheckListener{

	private static final String	    TAG	            = "BoostExpandableListFrag";

	private int	                    childChoiceMode	=
	                                                  BoostExpandableList.CHECK_MODE_MULTI;
	private int	                    groupChoiceMode	=
	                                                  BoostExpandableList.CHECK_MODE_MULTI;

	private BoostExpandableListView	mList;
	private ExampleAdapter	        mAdapter;


	public static BoostExpandableListFragment newInstance(){

		return new BoostExpandableListFragment();
	}


	@Override
	public void onCreate(final Bundle savedInstanceState){

		super.onCreate(savedInstanceState);
		
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
	
	// Enable/Disable MultiChoice
	public void onEvent(MultiChoiceToggleEvent event){

		if (event.toggled){ // Enable MultiChoice
			mList.enableChoice(groupChoiceMode,
			                   childChoiceMode);
		}else{
			mList.disableChoice();
		}
	}
	
	// Enable/Disable Checking Children on Parent Group Check
	public void onEvent(CheckChildrenWithGroupToggleEvent event){
		if(event.toggled != mList.checkChildrenWithGroup())
			mList.checkChildrenWithGroup(event.toggled);
	}
	
	public void onEvent(ExpandableListOneItemOnlyToggleEvent event){
		if(event.toggled != mList.isOneItemChoiceOn())
			mList.enableOnlyOneItemChoice(event.toggled);
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
}
