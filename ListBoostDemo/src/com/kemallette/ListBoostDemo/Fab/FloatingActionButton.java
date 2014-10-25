package com.kemallette.ListBoostDemo.Fab;


import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.AbsListView;
import android.widget.ImageView;

import com.kemallette.ListBoostDemo.R;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.view.ViewHelper;

public class FloatingActionButton extends ImageView{

	private static final int		   ANIMATION_DURATION = 400; // in milliseconds
	
	private boolean	           isHidden	      = false;
	
	private float	           displayedYPos  = -1, 
							   hiddenYPos     = -1;
	
	private final Interpolator	mInterpolator	= new AccelerateDecelerateInterpolator();

	private AbsListView mList;
	
	public FloatingActionButton(Context context){

		this(context, null);
	}


	public FloatingActionButton(Context context, AttributeSet attributeSet){

		this(context, attributeSet, 0);
	}


	public FloatingActionButton(Context context, AttributeSet attrs, int defStyleAttr){

		super(context, attrs, defStyleAttr);


	    final WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		final Display display = windowManager.getDefaultDisplay();
		final Point displaySize = new Point();

		// If we have access to 3.2 SDK, use that display API, otherwise, fallback to old getHeight() 
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2){
			display.getSize(displaySize);
			hiddenYPos = displaySize.y;
		}else {
			hiddenYPos = display.getHeight();
		}
	}


	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom){

		super.onLayout(changed, left, top, right, bottom);

		if (displayedYPos == -1)
			displayedYPos = ViewHelper.getY(this);
	}


	public void toggleHide(boolean hide){

		ObjectAnimator animator;
		
		if (isHidden != hide){

			isHidden = hide;

			// Animate the FABs movement to the new y position
			animator = ObjectAnimator
				 			.ofFloat(this, "y", isHidden ? hiddenYPos : displayedYPos)
                            .setDuration(ANIMATION_DURATION);
			animator.setInterpolator(mInterpolator);
			animator.start();
		}
	}

	public void respondTo(AbsListView listView){
		
		if (listView != null) {
			if(mList != null) {
				mList.setOnScrollListener(null);
			}
			mList = listView;
		}
		
		mList.setOnScrollListener(new ScrollDirectionListener(this));
	}

	public void disableScrolling() {
		if(mList != null) {
			mList.setOnScrollListener(null);
		}
	}

	protected class ScrollDirectionListener implements AbsListView.OnScrollListener{

		private static final int		   DIRECTION_CHANGED_THRESH	= 2;
		
		private boolean		               isUpdated;
		private int		                   lastVisibleChildPos, // Position in View parent
										   lastChildTopPos; // Position of child's top coordiante

		private final FloatingActionButton	mFab;
		

		ScrollDirectionListener(FloatingActionButton floatingActionButton){

			mFab = floatingActionButton;
		}


		public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount){

			final View firstViewChild = view.getChildAt(0);
			int firstChildTopPos = 0;
			boolean isMovingDown;
			boolean hasChangedDirection = true;

			if (firstViewChild != null)
				firstChildTopPos = firstViewChild.getTop();

			if (lastVisibleChildPos == firstVisibleItem){
				int topDelta = lastChildTopPos - firstChildTopPos;
				isMovingDown = firstChildTopPos < lastChildTopPos;
				hasChangedDirection = Math.abs(topDelta) > DIRECTION_CHANGED_THRESH;
			}else{
				isMovingDown = firstVisibleItem > lastVisibleChildPos;
			}
			if (hasChangedDirection && isUpdated){
				mFab.toggleHide(isMovingDown);
			}
			lastVisibleChildPos = firstVisibleItem;
			lastChildTopPos = firstChildTopPos;
			isUpdated = true;
		}


		public void onScrollStateChanged(AbsListView view, int scrollState){
			// Here you can implement your own functionality when the list scrolling
			// changes to/from scrollstates:  IDLE, TOUCH_SCROLL, FLING 
			
		}
	}

}
