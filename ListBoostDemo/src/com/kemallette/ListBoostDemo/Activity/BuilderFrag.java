package com.kemallette.ListBoostDemo.Activity;


import static com.kemallette.ListBoostDemo.Activity.MainActivity.DRAGDROP;
import static com.kemallette.ListBoostDemo.Activity.MainActivity.MULTICHOICE;
import static com.kemallette.ListBoostDemo.Activity.MainActivity.SLIDE;
import static com.kemallette.ListBoostDemo.Activity.MainActivity.SWIPE;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;

import com.kemallette.ListBoostDemo.R;

public class BuilderFrag extends
						Fragment implements
										OnClickListener,
										android.widget.RadioGroup.OnCheckedChangeListener,
										android.widget.CompoundButton.OnCheckedChangeListener{

	private static final String	TAG			= "BuilderFrag";


	private Bundle				mFeatures	= new Bundle();

	private CheckBox			mSlideBox;
	private CheckBox			mSwipeBox;
	private CheckBox			mDragDropBox;
	private CheckBox			mMultiChoiceBox;


	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public BuilderFrag(){

	}


	public static BuilderFrag newInstance(){

		final BuilderFrag mFrag = new BuilderFrag();

		return mFrag;
	}


	@Override
	public View onCreateView(final LayoutInflater inflater,
								final ViewGroup container,
								final Bundle savedInstanceState){

		return inflater.inflate(R.layout.builder_frag,
								container,
								false);
	}


	@Override
	public void onViewCreated(final View view, final Bundle savedInstanceState){

		super.onViewCreated(view,
							savedInstanceState);

		initViews();

		if (savedInstanceState != null){
			mFeatures = new Bundle();

			mFeatures.putBoolean(	SWIPE,
									mSwipeBox.isChecked());
			mFeatures.putBoolean(	SLIDE,
									mSlideBox.isChecked());
			mFeatures.putBoolean(	DRAGDROP,
									mDragDropBox.isChecked());
			mFeatures.putBoolean(	MULTICHOICE,
									mMultiChoiceBox.isChecked());
		}
	}


	@Override
	public void onResume(){

		super.onResume();

	}


	@Override
	public void onClick(final View v){

	}


	@Override
	public void onCheckedChanged(final CompoundButton buttonView,
									final boolean isChecked){

		int id = buttonView.getId();
		if (id == R.id.swipe) {
			mFeatures.putBoolean(	SWIPE,
									isChecked);
		} else if (id == R.id.slide) {
			mFeatures.putBoolean(	SLIDE,
									isChecked);
		} else if (id == R.id.dragdrop) {
			mFeatures.putBoolean(	DRAGDROP,
									isChecked);
		} else if (id == R.id.multiChoice) {
			mFeatures.putBoolean(	MULTICHOICE,
									isChecked);
		}
	}


	@Override
	public void onCheckedChanged(final RadioGroup group, final int checkedId){}


	private void initViews(){

		final RadioGroup mListTypeGroup = (RadioGroup) getView().findViewById(R.id.listTypeGroup);

		mSlideBox = (CheckBox) getView().findViewById(R.id.slide);
		mSwipeBox = (CheckBox) getView().findViewById(R.id.swipe);
		mDragDropBox = (CheckBox) getView().findViewById(R.id.dragdrop);
		mMultiChoiceBox = (CheckBox) getView().findViewById(R.id.multiChoice);

		final Button mStartDemoButton = (Button) getView().findViewById(R.id.startDemo);

		mListTypeGroup.setOnCheckedChangeListener(this);

		mSlideBox.setOnCheckedChangeListener(this);
		mSwipeBox.setOnCheckedChangeListener(this);
		mDragDropBox.setOnCheckedChangeListener(this);
		mMultiChoiceBox.setOnCheckedChangeListener(this);

		mStartDemoButton.setOnClickListener(this);

	}

}
