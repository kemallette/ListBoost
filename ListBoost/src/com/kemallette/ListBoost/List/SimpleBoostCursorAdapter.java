package com.kemallette.ListBoost.List;


import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;


public class SimpleBoostCursorAdapter	extends
										BoostCursorAdapter{

	private static final String	TAG	= "SimpleBoostCursorAdapter";


	public SimpleBoostCursorAdapter(Context context,
									int itemLayout,
									Cursor c,
									String[] from,
									int[] to,
									int flags){

		super(	new SimpleCursorAdapter(context,
										itemLayout,
										c,
										from,
										to,
										flags),
				context);

	}

}
