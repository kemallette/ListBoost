package com.kemallette.ListBoost.List;


import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;


public class BoostCursorAdapter	extends
								BaseBoostAdapter{

	private static final String	TAG	= "BoostCursorAdapter";


	public BoostCursorAdapter(	CursorAdapter toBeWrapped,
								Context context){

		super(toBeWrapped);

	}


	public void swapCursor(Cursor mCursor){

		((CursorAdapter) getWrappedAdapter()).swapCursor(mCursor);

	}
}
