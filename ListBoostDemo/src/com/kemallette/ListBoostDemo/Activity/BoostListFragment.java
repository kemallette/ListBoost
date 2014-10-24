package com.kemallette.ListBoostDemo.Activity;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.kemallette.ListBoost.List.BoostAdapter;
import com.kemallette.ListBoost.List.BoostListView;
import com.kemallette.ListBoost.List.OnSlidingMenuItemClickListener;
import com.kemallette.ListBoostDemo.R;
import com.kemallette.ListBoostDemo.Activity.MainActivity.ListChoiceModeChangeEvent;
import com.kemallette.ListBoostDemo.Activity.MainActivity.MultiChoiceToggleEvent;

import de.greenrobot.event.EventBus;


public class BoostListFragment extends Fragment implements OnSlidingMenuItemClickListener{

	private final int[]	  mSlidingViewButtonIds	= new int[] { R.id.b1, R.id.b2,
	R.id.b3, R.id.b4	                        };

	private int	          choiceMode;

	private BoostListView	mList;
	private BoostAdapter	mAdapter;


	public BoostListFragment(){

		super();
	}


	public static BoostListFragment newInstance(){

		return new BoostListFragment();
	}


	@Override
	public void onCreate(Bundle savedInstanceState){

		super.onCreate(savedInstanceState);

		EventBus.getDefault()
		        .register(this);
	}


	@Override
	public View onCreateView(final LayoutInflater inflater,
	                         final ViewGroup container,
	                         final Bundle savedInstanceState){

		return inflater.inflate(R.layout.boost_list_frag,
		                        container,
		                        false);
	}


	@Override
	public void onViewCreated(final View view, final Bundle savedInstanceState){

		super.onViewCreated(view,
		                    savedInstanceState);

		init();

	}


	@Override
	public void onSlideItemClick(final View itemView,
	                             final View clickedView,
	                             final int position){

		Toast.makeText(getActivity(),
		               ((Button) clickedView).getText(),
		               Toast.LENGTH_SHORT)
		     .show();
	}


	private void init(){

		mList = (BoostListView) getView().findViewById(R.id.list);

		String[] data = new String[] { "first", "second", "third", "fourth" };

		mAdapter =
		           new BoostDemoAdapter(new ArrayAdapter<String>(getActivity(),
		                                                         R.layout.combined_list_item,
		                                                         android.R.id.text1,
		                                                         data));

		mList.setAdapter(mAdapter);

		if (getArguments() != null && !getArguments().isEmpty()){

			// int listFeature =
			// getArguments().getInt(ActivityUtil.LIST_FEATURE);


		}

	}


	/***********************************************************
	 * 
	 * EventBus event listeners
	 * 
	 ************************************************************/
	public void onEvent(MultiChoiceToggleEvent event){

		if (event.toggled){ // Enable MultiChoice
			mList.setChoiceMode(choiceMode);
		}else{
			mList.setChoiceMode(BoostListView.CHOICE_MODE_NONE);
		}
	}


	public void onEvent(ListChoiceModeChangeEvent event){

		if (mList.getChoiceMode() != event.choiceMode)
			mList.setChoiceMode(event.choiceMode);
	}

	private class BoostDemoAdapter extends BoostAdapter{

		private class Holder{

			CheckBox	mBox;
			Button		mSlideToggle;
			TextView	mText;
		}

		private Holder	mHolder;


		public BoostDemoAdapter(final BaseAdapter wrapped){

			super(wrapped);
		}


		@Override
		public View getView(final int position,
		                    View convertView,
		                    final ViewGroup viewGroup){

			convertView = super.getView(position,
			                            convertView,
			                            viewGroup);

			if (convertView != null){

				mHolder = (Holder) convertView.getTag();
				if (mHolder == null){

					mHolder = new Holder();

					mHolder.mBox =
					               (CheckBox) convertView.findViewById(android.R.id.checkbox);
					mHolder.mSlideToggle =
					                       (Button) convertView.findViewById(R.id.slide_toggle_button);
					mHolder.mText =
					                (TextView) convertView.findViewById(android.R.id.text1);
				}

				mHolder.mText.setText(getItem(position).toString());

			}
			return convertView;
		}
	}

}
