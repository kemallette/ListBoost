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

	private static final String	TAG			= "BuilderFrag";


	private Bundle				mFeatures	= new Bundle();

	private ListType			mListType;


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
	public void
		onStartDemo(final ListType listType, final Bundle mDemoFeatures){

		((DemoBuilderListener) getActivity()).onStartDemo(	listType,
															mDemoFeatures);
	}


	@Override
	public void onClick(final View v){

		if (v.getId() == R.id.startDemo)
			onStartDemo(mListType,
						mFeatures);
	}


	@Override
	public void onCheckedChanged(final CompoundButton buttonView,
									final boolean isChecked){

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
	public void onCheckedChanged(final RadioGroup group, final int checkedId){

		if (group.getId() == R.id.listTypeGroup)
			mListType = (checkedId == R.id.listRadio)	? ListType.LISTVIEW
														: ListType.EXPANDABLE_LISTVIEW;
	}


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
