package com.kemallette.ListBoostDemo.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import android.widget.ToggleButton;

import com.kemallette.ListBoost.ExpandableList.BoostExpandableList;
import com.kemallette.ListBoost.ExpandableList.BoostExpandableListView;
import com.kemallette.ListBoost.ExpandableList.ExpandableListCheckListener;
import com.kemallette.ListBoostDemo.R;
import com.kemallette.ListBoostDemo.Activity.ListFeatureListener;
import com.kemallette.ListBoostDemo.Activity.MainActivity;
import com.kemallette.ListBoostDemo.Adapter.ExampleAdapter;


public class BoostExpandableListFragment extends
										Fragment implements
														ExpandableListCheckListener,
														OnItemSelectedListener,
														ListFeatureListener{

	private static final String		TAG					= "MainActivity";
	private static final String		GROUP_CHOICE_MODE	= "groupChoiceMode";
	private static final String		CHILD_CHOICE_MODE	= "childChoiceMode";


	private boolean					enableChoice		= false;
	private boolean					enableSwipe			= false;
	private boolean					enableSlide			= false;
	private boolean					enableDragDrop		= false;

	private int						childChoiceMode		= BoostExpandableList.CHECK_MODE_MULTI;
	private int						groupChoiceMode		= BoostExpandableList.CHECK_MODE_MULTI;
	private int						groupModeSpinnerPosition;
	private int						childModeSpinnerPosition;

	private ToggleButton			onlyOneItem;

	private Spinner					groupChoiceModes;
	private Spinner					childChoiceModes;

	private LinearLayout			mChoiceOptions;

	private BoostExpandableListView	mList;
	private ExampleAdapter			mAdapter;


	public static BoostExpandableListFragment
		newInstance(){

		final BoostExpandableListFragment frag = new BoostExpandableListFragment();
		// frag.setArguments(features);

		return frag;
	}


	@Override
	public void onCreate(final Bundle savedInstanceState){

		super.onCreate(savedInstanceState);

		if (savedInstanceState != null
			&& !savedInstanceState.isEmpty()){
			enableChoice = savedInstanceState.getBoolean(	MainActivity.MULTICHOICE,
															false);
			enableSlide = savedInstanceState.getBoolean(MainActivity.SLIDE,
														false);
			enableSwipe = savedInstanceState.getBoolean(MainActivity.SWIPE,
														false);
			enableDragDrop = savedInstanceState.getBoolean(	MainActivity.DRAGDROP,
															false);
			if (enableChoice){
				groupModeSpinnerPosition = savedInstanceState.getInt(GROUP_CHOICE_MODE);
				childModeSpinnerPosition = savedInstanceState.getInt(CHILD_CHOICE_MODE);
			}
		}
	}


	@Override
	public void onSaveInstanceState(final Bundle outState){

		outState.putBoolean(MainActivity.SWIPE,
							enableSwipe);
		outState.putBoolean(MainActivity.SLIDE,
							enableSlide);
		outState.putBoolean(MainActivity.DRAGDROP,
							enableDragDrop);
		outState.putBoolean(MainActivity.MULTICHOICE,
							enableChoice);

		if (enableChoice){
			outState.putInt(GROUP_CHOICE_MODE,
							groupChoiceModes.getSelectedItemPosition());
			outState.putInt(CHILD_CHOICE_MODE,
							childChoiceModes.getSelectedItemPosition());
		}

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

//		initChoiceModeOptions();
	}


	@Override
	public void onResume(){

		super.onResume();

		enableFeatures();
	}


	@Override
	public void onEnableFeatures(final Bundle features){

		extractFeatures(features);

		if (mList != null){
			enableFeatures();
		}
	}


	/*********************************************************************
	 * ExpandableListCheckListener
	 **********************************************************************/
	@Override
	public void onGroupCheckChange(final Checkable checkedView,
									final int groupPosition,
									final long groupId,
									final boolean isChecked){

		// Toast.makeText( getActivity(),
		// "Group Check Change\nid: "
		// + groupId
		// + "\n isChecked: "
		// + isChecked,
		// Toast.LENGTH_SHORT)
		// .show();
	}


	@Override
	public void onChildCheckChange(final Checkable checkedView,
									final int groupPosition,
									final long groupId,
									final int childPosition,
									final long childId,
									final boolean isChecked){

		// Toast.makeText( getActivity(),
		// "Child Check Change\nid: "
		// + childId
		// + "\n isChecked: "
		// + isChecked,
		// Toast.LENGTH_SHORT)
		// .show();

	}


	/*********************************************************************
	 * Choice Mode Spinner Callbacks
	 **********************************************************************/
	@Override
	public void onItemSelected(final AdapterView<?> parent,
								final View view,
								final int position,
								final long id){

//		onlyOneItem.setChecked(false);
//
//		if (!mList.isChoiceOn()){
//			mChoiceOptions.setVisibility(View.VISIBLE);
//			mList.enableChoice(	groupChoiceMode,
//								childChoiceMode);
//		}
//
//		int parentId = parent.getId();
//		if (parentId == R.id.groupChoiceModes) {
//			switch(position){
//
//				case 0:
//					groupChoiceMode = BoostExpandableList.CHECK_MODE_MULTI;
//					break;
//
//				case 1:
//					groupChoiceMode = BoostExpandableList.CHECK_MODE_ONE;
//					break;
//
//				case 2:
//					groupChoiceMode = BoostExpandableList.CHECK_MODE_NONE;
//					break;
//			}
//			mList.setGroupChoiceMode(groupChoiceMode);
//			groupModeSpinnerPosition = position;
//		} else if (parentId == R.id.childChoiceModes) {
//			switch(position){
//
//				case 0:
//					childChoiceMode = BoostExpandableList.CHECK_MODE_MULTI;
//					break;
//
//				case 1:
//					childChoiceMode = BoostExpandableList.CHILD_CHECK_MODE_ONE_PER_GROUP;
//					break;
//
//				case 2:
//					childChoiceMode = BoostExpandableList.CHECK_MODE_ONE;
//					break;
//
//				case 3:
//					childChoiceMode = BoostExpandableList.CHECK_MODE_NONE;
//					break;
//			}
//			mList.setChildChoiceMode(childChoiceMode);
//			childModeSpinnerPosition = position;
//		}
	}


	@Override
	public void onNothingSelected(final AdapterView<?> parent){


	}

//
//	private void initChoiceModeOptions(){
//
//		mChoiceOptions = (LinearLayout) getView().findViewById(R.id.choice_options_layout);
//
//		groupChoiceModes = (Spinner) mChoiceOptions.findViewById(R.id.groupChoiceModes);
//		childChoiceModes = (Spinner) mChoiceOptions.findViewById(R.id.childChoiceModes);
//
//		groupChoiceModes.setOnItemSelectedListener(this);
//		childChoiceModes.setOnItemSelectedListener(this);
//
//		onlyOneItem = (ToggleButton) mChoiceOptions.findViewById(R.id.onlyOneItem);
//		onlyOneItem.setOnCheckedChangeListener(new OnCheckedChangeListener(){
//
//			@Override
//			public void onCheckedChanged(final CompoundButton buttonView,
//											final boolean isChecked){
//
//				mList.enableOnlyOneItemChoice(isChecked);
//				groupChoiceModes.setSelection(2); // 2 is position for
//													// CHECK_MODE_NONE
//				childChoiceModes.setSelection(3); // 3 is position for
//													// CHECK_MODE_NONE
//
//			}
//		});
//
//	}


	private void extractFeatures(final Bundle features){

		if (features != null
			&& !features.isEmpty()){

			enableChoice = features.getBoolean(	MainActivity.MULTICHOICE,
												false);
			enableSlide = features.getBoolean(	MainActivity.SLIDE,
												false);
			enableSwipe = features.getBoolean(	MainActivity.SWIPE,
												false);
			enableDragDrop = features.getBoolean(	MainActivity.DRAGDROP,
													false);
		}
	}


	private void enableFeatures(){

		if (enableChoice){
			mChoiceOptions.setVisibility(View.VISIBLE);

			groupChoiceModes.setSelection(groupModeSpinnerPosition);
			childChoiceModes.setSelection(childModeSpinnerPosition);

		}else{
			mList.disableChoice();
			mChoiceOptions.setVisibility(View.GONE);
		}

		if (enableDragDrop){
			// TODO when implemented
		}

		if (enableSlide){
			// TODO when implemented
		}

		if (enableSwipe){
			// TODO when implemented
		}
	}


}
