package com.kemallette.ListBoostDemo.Fragment;


import static com.kemallette.ListBoostDemo.Activity.MainActivity.DRAGDROP;
import static com.kemallette.ListBoostDemo.Activity.MainActivity.MULTICHOICE;
import static com.kemallette.ListBoostDemo.Activity.MainActivity.SLIDE;
import static com.kemallette.ListBoostDemo.Activity.MainActivity.SWIPE;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;

import com.actionbarsherlock.app.SherlockFragment;
import com.kemallette.ListBoostDemo.R;
import com.kemallette.ListBoostDemo.Activity.DemoBuilderListener;
import com.kemallette.ListBoostDemo.Activity.MainActivity.ListType;

public class BuilderFrag extends
						SherlockFragment implements
										DemoBuilderListener,
										OnClickListener,
										android.widget.RadioGroup.OnCheckedChangeListener,
										android.widget.CompoundButton.OnCheckedChangeListener{

	private final Bundle	mFeatures	= new Bundle();

	private ListType		mListType;


	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public BuilderFrag(){

	}


	public static BuilderFrag newInstance(){

		BuilderFrag mFrag = new BuilderFrag();

		return mFrag;
	}


	@Override
	public View onCreateView(LayoutInflater inflater,
								ViewGroup container,
								Bundle savedInstanceState){

		return inflater.inflate(R.layout.builder_frag,
								container,
								false);
	}


	@Override
	public void onViewCreated(View view, Bundle savedInstanceState){

		super.onViewCreated(view,
							savedInstanceState);

		initViews();
	}


	@Override
	public void onStartDemo(ListType listType, Bundle mDemoFeatures){

		((DemoBuilderListener) getActivity()).onStartDemo(	listType,
															mDemoFeatures);
	}


	@Override
	public void onClick(View v){

		if (v.getId() == R.id.startDemo)
			onStartDemo(mListType,
						mFeatures);
	}


	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){

		switch(buttonView.getId()){

			case R.id.swipe:
				mFeatures.putBoolean(	SWIPE,
										isChecked);
				break;

			case R.id.slide:
				mFeatures.putBoolean(	SLIDE,
										isChecked);
				break;

			case R.id.dragdrop:
				mFeatures.putBoolean(	DRAGDROP,
										isChecked);
				break;

			case R.id.multiChoice:
				mFeatures.putBoolean(	MULTICHOICE,
										isChecked);
				break;
		}
	}


	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId){

		if (group.getId() == R.id.listTypeGroup)
			mListType = (checkedId == R.id.listRadio)	? ListType.LISTVIEW
														: ListType.EXPANDABLE_LISTVIEW;
	}


	private void initViews(){

		RadioGroup mListTypeGroup = (RadioGroup) getView().findViewById(R.id.listTypeGroup);

		CheckBox mSlideBox = (CheckBox) getView().findViewById(R.id.slide);
		CheckBox mSwipeBox = (CheckBox) getView().findViewById(R.id.swipe);
		CheckBox mDragDropBox = (CheckBox) getView().findViewById(R.id.dragdrop);
		CheckBox mMultiChoiceBox = (CheckBox) getView().findViewById(R.id.multiChoice);

		Button mStartDemoButton = (Button) getView().findViewById(R.id.startDemo);

		mListTypeGroup.setOnCheckedChangeListener(this);

		mSlideBox.setOnCheckedChangeListener(this);
		mSwipeBox.setOnCheckedChangeListener(this);
		mDragDropBox.setOnCheckedChangeListener(this);
		mMultiChoiceBox.setOnCheckedChangeListener(this);

		mStartDemoButton.setOnClickListener(this);

	}

}
