package com.kemallette.ListBoostDemo.Fragment;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.actionbarsherlock.app.SherlockFragment;
import com.kemallette.ListBoost.ExpandableList.BoostExpandableList;
import com.kemallette.ListBoost.ExpandableList.BoostExpandableListView;
import com.kemallette.ListBoost.ExpandableList.ExpandableListCheckListener;
import com.kemallette.ListBoostDemo.R;
import com.kemallette.ListBoostDemo.Activity.MainActivity;
import com.kemallette.ListBoostDemo.Adapter.ExampleAdapter;


public class BoostExpandableListFragment extends
										SherlockFragment implements
														ExpandableListCheckListener,
														OnItemSelectedListener{

	private static final String		TAG				= "MainActivity";


	private boolean					enableChoice	= false;
	private boolean					enableSwipe		= false;
	private boolean					enableSlide		= false;
	private boolean					enableDragDrop	= false;

	private int						childMode		= BoostExpandableList.CHECK_MODE_MULTI;
	private int						groupMode		= BoostExpandableList.CHECK_MODE_MULTI;

	private Bundle					mFeatures;
	private ToggleButton			onlyOneItem;
	private BoostExpandableListView	mExpandableList;
	private ExampleAdapter			mAdapter;


	public static BoostExpandableListFragment newInstance(Bundle mFeatures){

		BoostExpandableListFragment mFrag = new BoostExpandableListFragment();

		mFrag.setArguments(mFeatures);

		return mFrag;
	}


	@Override
	public View onCreateView(LayoutInflater inflater,
								ViewGroup container,
								Bundle savedInstanceState){

		return inflater.inflate(R.layout.expandable_list_frag,
								container,
								false);
	}


	@Override
	public void onViewCreated(View view, Bundle savedInstanceState){

		super.onViewCreated(view,
							savedInstanceState);
		extractFeatures();
		initViews();
	}


	/*********************************************************************
	 * ExpandableListCheckListener
	 **********************************************************************/
	@Override
	public void onGroupCheckChange(final Checkable checkedView,
									final int groupPosition,
									final long groupId,
									final boolean isChecked){

		Toast.makeText(	getActivity(),
						"Group Check Change\nid: "
							+ groupId
							+ "\n isChecked: "
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

		Toast.makeText(	getActivity(),
						"Child Check Change\nid: "
							+ childId
							+ "\n isChecked: "
							+ isChecked,
						Toast.LENGTH_SHORT)
				.show();

	}


	/*********************************************************************
	 * Choice Mode Spinner Callbacks
	 **********************************************************************/
	@Override
	public void onItemSelected(AdapterView<?> parent,
								View view,
								int position,
								long id){

		if (!mExpandableList.isChoiceOn())
			mExpandableList.enableChoice(	groupMode,
											childMode);
		onlyOneItem.setChecked(false);

		switch(parent.getId()){

			case R.id.groupChoiceModes:

				switch(position){

					case 0:
						Log.i(	TAG,
								"group to multi");
						groupMode = BoostExpandableList.CHECK_MODE_MULTI;
						break;

					case 1:
						groupMode = BoostExpandableList.CHECK_MODE_ONE;
						break;

					case 2:
						groupMode = BoostExpandableList.CHECK_MODE_NONE;
						break;
				}
				mExpandableList.setGroupChoiceMode(groupMode);
				break;

			case R.id.childChoiceModes:

				switch(position){

					case 0:
						Log.i(	TAG,
								"child to multi");

						childMode = BoostExpandableList.CHECK_MODE_MULTI;
						break;

					case 1:
						childMode = BoostExpandableList.CHILD_CHECK_MODE_ONE_PER_GROUP;
						break;

					case 2:
						childMode = BoostExpandableList.CHECK_MODE_ONE;
						break;

					case 3:
						childMode = BoostExpandableList.CHECK_MODE_NONE;
						break;
				}
				mExpandableList.setChildChoiceMode(childMode);
				break;
		}
	}


	@Override
	public void onNothingSelected(AdapterView<?> parent){


	}


	private void extractFeatures(){

		mFeatures = getArguments();

		if (mFeatures != null
			&& !mFeatures.isEmpty()){

			enableChoice = mFeatures.getBoolean(MainActivity.MULTICHOICE,
												false);
			enableSlide = mFeatures.getBoolean(	MainActivity.SLIDE,
												false);
			enableSwipe = mFeatures.getBoolean(	MainActivity.SWIPE,
												false);
			enableDragDrop = mFeatures.getBoolean(	MainActivity.DRAGDROP,
													false);
		}
	}


	private void initViews(){

		mExpandableList = (BoostExpandableListView) getView().findViewById(R.id.list);
		mExpandableList.setExpandableCheckListener(this);

		initListAdapter();

		if (enableChoice)
			initChoiceModeOptions();
		else{
			LinearLayout mChoiceOptions = (LinearLayout) getView().findViewById(R.id.choice_options_layout);
			mChoiceOptions.setVisibility(View.GONE);
		}

	}


	private void initChoiceModeOptions(){

		final Spinner groupChoiceModes = (Spinner) getView().findViewById(R.id.groupChoiceModes);
		final Spinner childChoiceModes = (Spinner) getView().findViewById(R.id.childChoiceModes);
		groupChoiceModes.setOnItemSelectedListener(this);
		childChoiceModes.setOnItemSelectedListener(this);

		onlyOneItem = (ToggleButton) getView().findViewById(R.id.onlyOneItem);
		onlyOneItem.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
											boolean isChecked){

				mExpandableList.enableOnlyOneItemChoice(isChecked);
				groupChoiceModes.setSelection(2);
				childChoiceModes.setSelection(3);

			}
		});
	}


	private void initListAdapter(){

		mAdapter = new ExampleAdapter(getActivity());
		mExpandableList.setAdapter(mAdapter);
	}
}
