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
import com.kemallette.ListBoostDemo.Activity.MainActivity.ChildItemMenusToggleEvent;
import com.kemallette.ListBoostDemo.Activity.MainActivity.EnableFabMenuToggleEvent;
import com.kemallette.ListBoostDemo.Activity.MainActivity.EnableFabToggleEvent;
import com.kemallette.ListBoostDemo.Activity.MainActivity.EnableMultiChoiceToggleEvent;
import com.kemallette.ListBoostDemo.Activity.MainActivity.EnableSlidingItemMenuToggleEvent;
import com.kemallette.ListBoostDemo.Activity.MainActivity.FabMenuDirectionChangeEvent;
import com.kemallette.ListBoostDemo.Activity.MainActivity.FabScrollModeToggleEvent;
import com.kemallette.ListBoostDemo.Activity.MainActivity.GroupItemMenusToggleEvent;
import com.kemallette.ListBoostDemo.Activity.MainActivity.ListChoiceModeChangeEvent;
import com.kemallette.ListBoostDemo.Fab.FloatingActionButton;
import com.kemallette.ListBoostDemo.Model.WorldCity;
import com.kemallette.ListBoostDemo.Util.SeedDataUtil;

import de.greenrobot.event.EventBus;


public class BoostListFragment extends Fragment implements OnSlidingMenuItemClickListener{

	private final int[]	         mSlidingViewButtonIds	= new int[] { R.id.b1,
	R.id.b2, R.id.b3, R.id.b4, R.id.b5, R.id.b6	   };


	private boolean	             fabScrollingEnabled	= false,
	    fabMenuEnabled = false, fabEnabled = false;

	private boolean	             slidingMenusEnabled	= false;

	private int	                 choiceMode;

	private FloatingActionButton	mFab;

	private BoostListView	     mList;
	private BoostAdapter	     mAdapter;


	public BoostListFragment(){

		super();
	}


	public static BoostListFragment newInstance(){

		return new BoostListFragment();
	}


	@Override
	public void onCreate(Bundle savedInstanceState){

		super.onCreate(savedInstanceState);

		if(!EventBus.getDefault().isRegistered(this))
			EventBus.getDefault().register(this);
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


	private void init(){

		mFab = (FloatingActionButton) getView().findViewById(R.id.fab);
		mList = (BoostListView) getView().findViewById(R.id.list);

		WorldCity[] data = SeedDataUtil.getCities(getActivity(),
		                                          35);

		mAdapter =
		           new BoostDemoAdapter(new ArrayAdapter<WorldCity>(getActivity(),
		                                                            R.layout.combined_list_item,
		                                                            android.R.id.text1,
		                                                            data));

		mList.setAdapter(mAdapter);

		if (slidingMenusEnabled){
			mList.enableSlidingMenus(this,
			                         R.id.slide_toggle_button,
			                         R.id.sliding_view_id,
			                         mSlidingViewButtonIds);
		}

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
			mList.setChoiceMode(choiceMode);
		}else{
			mList.setChoiceMode(BoostListView.CHOICE_MODE_NONE);
		}
	}


	public void onEvent(ListChoiceModeChangeEvent event){

		if (mList.getChoiceMode() != event.choiceMode)
			mList.setChoiceMode(event.choiceMode);
	}


	/******************************************
	 * Sliding Item Menu Events
	 *****************************************/
	public void onEvent(EnableSlidingItemMenuToggleEvent event){

		slidingMenusEnabled = event.toggled;

		if (slidingMenusEnabled){
			mList.enableSlidingMenus(this,
			                         R.id.slide_toggle_button,
			                         R.id.sliding_view_id,
			                         mSlidingViewButtonIds);
		}else{
			mList.disableSlidingMenus();
		}
	}


	public void onEvent(GroupItemMenusToggleEvent event){

		// TODO
	}


	public void onEvent(ChildItemMenusToggleEvent event){

		// TODO
	}


	/******************************************
	 * FAB Events
	 *****************************************/

	public void onEvent(EnableFabToggleEvent event){

		fabEnabled = event.toggled;

		if (fabEnabled){
			mFab.setVisibility(View.VISIBLE);
			if (fabScrollingEnabled){
				mFab.respondTo(mList);
			}else{
				mFab.disableScrolling();
			}
			// TODO: Blocked by fab menu implementation: if(fabMenuEnabled) mFab.enableMenu();
		}else{
			mFab.setVisibility(View.INVISIBLE);
			fabEnabled = false;
		}
	}


	public void onEvent(EnableFabMenuToggleEvent event){

		fabMenuEnabled = event.toggled;
		// TODO: Blocked by fab menu implementation
	}


	public void onEvent(FabScrollModeToggleEvent event){

		if (fabScrollingEnabled != event.toggled)
			fabScrollingEnabled = event.toggled;

		if (fabScrollingEnabled){
			mFab.respondTo(mList);
		}else{
			mFab.disableScrolling();
		}
	}


	public void onEvent(FabMenuDirectionChangeEvent event){

		// TODO: Blocked by FAB menu implementation
	}


	private class BoostDemoAdapter extends BoostAdapter{

		public BoostDemoAdapter(final BaseAdapter wrapped){

			super(wrapped);
		}
		
	}

}
