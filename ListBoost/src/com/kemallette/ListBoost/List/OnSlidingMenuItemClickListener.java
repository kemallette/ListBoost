package com.kemallette.ListBoost.List;

import android.view.View;

/**
 * Interface for callback to be invoked whenever an action is clicked in the
 * expandle area of the list item.
 */
public interface OnSlidingMenuItemClickListener{

	/**
	 * Called when an item in the sliding menu layout is clicked.
	 * 
	 * @param itemView
	 *            the view of the list item
	 * @param clickedView
	 *            the view clicked
	 * @param position
	 *            the position in the listview
	 */
	public void onSlideItemClick(View itemView,
									View clickedView,
									int position);
}