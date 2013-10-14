package com.kemallette.ListBoostDemo.Activity;


import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.kemallette.ListBoostDemo.R;
import com.kemallette.ListBoostDemo.Fragment.BoostExpandableListFragment;
import com.kemallette.ListBoostDemo.Fragment.BoostListFragment;
import com.kemallette.ListBoostDemo.Fragment.BuilderFrag;


public class MainActivity	extends
							SherlockFragmentActivity implements
													DemoBuilderListener{

	private static final String	TAG			= "MainActivity";

	public static final String	SWIPE		= "swipe";
	public static final String	SLIDE		= "slide";
	public static final String	DRAGDROP	= "dragdrop";
	public static final String	MULTICHOICE	= "multichoice";

	public enum ListType {
		LISTVIEW,
		EXPANDABLE_LISTVIEW
	}


	@Override
	protected void onCreate(Bundle arg0){

		super.onCreate(arg0);
		setContentView(R.layout.main_activity);

		launchBuilderFrag();
	}


	@Override
	public void onStartDemo(ListType listType, Bundle mDemoFeatures){


	}


	private void launchBuilderFrag(){

		FragmentTransaction mTransaction = getSupportFragmentManager().beginTransaction();

		mTransaction.add(	R.id.container,
							BuilderFrag.newInstance());
		mTransaction.commit();
	}


	private void launchExpandableListFrag(Bundle mFeatures){

		FragmentTransaction mTransaction = getSupportFragmentManager().beginTransaction();

		mTransaction.replace(	R.id.container,
								BoostExpandableListFragment.newInstance(mFeatures));
		mTransaction.commit();
	}


	private void launchListFrag(Bundle mFeatures){

		FragmentTransaction mTransaction = getSupportFragmentManager().beginTransaction();

		mTransaction.replace(	R.id.container,
								BoostListFragment.newInstance(mFeatures));
		mTransaction.commit();
	}

}
