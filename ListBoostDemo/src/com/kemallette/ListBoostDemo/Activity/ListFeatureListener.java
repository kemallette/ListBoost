package com.kemallette.ListBoostDemo.Activity;


import android.os.Bundle;
import com.kemallette.ListBoostDemo.R;


public interface ListFeatureListener{

	/**
	 * Used to notify frag(s) that the user has requested a set of features to be
	 * enabled. 
	 * 
	 * @param features
	 */
	public void onEnableFeatures(Bundle features);
}
