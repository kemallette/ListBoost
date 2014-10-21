package com.kemallette.ListBoostDemo.Activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.kemallette.ListBoostDemo.R;
import com.kemallette.ListBoostDemo.Fragment.BoostExpandableListFragment;
import com.kemallette.ListBoostDemo.Fragment.BoostListFragment;
import com.kemallette.ListBoostDemo.Fragment.BuilderFrag;


public class MainActivity extends ActionBarActivity implements DemoBuilderListener{

	private static final String	TAG	          = "MainActivity";
	private static final String	BUILDER_FRAG	= "builderFrag";
	private static final String	EXP_LIST_FRAG	= "expListFrag";
	private static final String	LIST_FRAG	  = "listFrag";

	public static final String	SWIPE	      = "swipe";
	public static final String	SLIDE	      = "slide";
	public static final String	DRAGDROP	  = "dragdrop";
	public static final String	MULTICHOICE	  = "multichoice";

	public enum ListType {
		LISTVIEW,
		EXPANDABLE_LISTVIEW
	}

	private BuilderFrag	                mBuilderFrag;
	private BoostExpandableListFragment	mExpListFrag;
	private BoostListFragment	        mListFrag;


	@Override
	protected void onCreate(final Bundle saveInstanceState){

		super.onCreate(saveInstanceState);

		setContentView(R.layout.main_activity);

		if (saveInstanceState == null)
			launchBuilderFrag();
		else{
			mBuilderFrag = (BuilderFrag) getSupportFragmentManager().findFragmentByTag(BUILDER_FRAG);
			mExpListFrag = (BoostExpandableListFragment) getSupportFragmentManager().findFragmentByTag(EXP_LIST_FRAG);
			mListFrag = (BoostListFragment) getSupportFragmentManager().findFragmentByTag(LIST_FRAG);
		}
	}


	@Override
	public void onStartDemo(final ListType listType, final Bundle features){

		if (listType == ListType.LISTVIEW){
			showListFrag(features);
		}else{
			showExpandableListFrag(features);
		}
	}


	private void launchBuilderFrag(){

		mBuilderFrag = BuilderFrag.newInstance();

		final FragmentTransaction mTransaction = getSupportFragmentManager().beginTransaction();
		mTransaction.add(R.id.container, mBuilderFrag, BUILDER_FRAG);
		mTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		mTransaction.commit();
	}


	private void showExpandableListFrag(final Bundle features){

		Log.d(TAG, "showing ELV\n multichoiceEnabled?  " + features.getBoolean(MULTICHOICE));

		final FragmentTransaction mTransaction = getSupportFragmentManager().beginTransaction();

		if (mExpListFrag == null){
			mExpListFrag = BoostExpandableListFragment.newInstance();
			mTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		}else
			mTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

		mTransaction.replace(R.id.container, mExpListFrag, EXP_LIST_FRAG);
		mTransaction.addToBackStack(null);
		mExpListFrag.onEnableFeatures(features);
		mTransaction.commit();
	}


	private void showListFrag(final Bundle features){

		final FragmentTransaction mTransaction = getSupportFragmentManager().beginTransaction();

		if (mListFrag == null){
			mListFrag = BoostListFragment.newInstance(features);
			mTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		}else
			mTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

		mTransaction.replace(R.id.container, mListFrag, LIST_FRAG);
		mTransaction.addToBackStack(null);

		mExpListFrag.onEnableFeatures(features);

		mTransaction.commit();
	}

}
