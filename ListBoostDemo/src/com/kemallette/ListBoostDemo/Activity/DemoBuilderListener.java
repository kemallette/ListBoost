package com.kemallette.ListBoostDemo.Activity;


import android.os.Bundle;

import com.kemallette.ListBoostDemo.Activity.MainActivity.ListType;


public interface DemoBuilderListener{

	public abstract void onStartDemo(ListType listType, Bundle mDemoFeatures);

}
