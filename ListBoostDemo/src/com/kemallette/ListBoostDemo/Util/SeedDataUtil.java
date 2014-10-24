package com.kemallette.ListBoostDemo.Util;


import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.kemallette.ListBoostDemo.R;
import com.kemallette.ListBoostDemo.Model.WorldCity;

import android.content.Context;
import android.content.res.AssetManager;
import au.com.bytecode.opencsv.CSVReader;

public class SeedDataUtil{

	private static int									ID_SENTINAL	= 1;
	private static WorldCity[]							CITIES;
	private static HashMap<Character, List<WorldCity>>	GROUPED_CITIES;
	private static int									LAST_SKIP_INTERVAL;


	/**
	 * Pulls from cities.csv in assets. There are ~15640 cities in the file, so
	 * be sure to use a skip interval if you don't want to use a ton of memory.
	 * 
	 * 
	 * @param context
	 * @param skipInterval
	 *            - how many cities to skip between adds (returns a smaller,
	 *            more performant array). Set 0 to get all of them.
	 * @return
	 */
	public static final WorldCity[]
		getCities(Context context, int skipInterval){

		if (CITIES == null
			|| CITIES.length < 1)
			return populateCities(	context,
									skipInterval);
		else if (skipInterval == LAST_SKIP_INTERVAL)
			return CITIES;
		else
			return populateCities(	context,
									skipInterval);
	}


	/**
	 * Pulls from cities.csv in assets, then groups them by first letter. Each
	 * Character key references a list of cities starting with that letter.
	 * 
	 * @param context
	 * @param skipInterval
	 *            - how many cities to skip between adds (returns a smaller
	 *            number of cities). Use 0 to get all cities.
	 * @return
	 */
	public static final HashMap<Character, List<WorldCity>>
		getAphabetGroupedCities(Context context, int skipInterval){

		if (GROUPED_CITIES == null
			|| GROUPED_CITIES.size() < 1)
			return populateGroupedCities(	context,
											skipInterval);
		else if (skipInterval == LAST_SKIP_INTERVAL)
			return GROUPED_CITIES;
		else
			return populateGroupedCities(	context,
											skipInterval);
	}


	private static final WorldCity[] populateCities(Context context,
													int skipInterval){

		List<String[]> citiesList = new ArrayList<String[]>();
		AssetManager assetManager = context.getAssets();

		try{
			InputStream csvStream = assetManager.open("cities.csv");
			InputStreamReader csvStreamReader = new InputStreamReader(
																		csvStream);
			CSVReader csvReader = new CSVReader(csvStreamReader);

			String[] line;

			// throw away the header
			csvReader.readNext();

			
			int skipCounter = 0;
			while ((line = csvReader.readNext()) != null)
				if (skipCounter == skipInterval){
					citiesList.add(line);
					skipCounter = 0;
				}else
					skipCounter++;

			
			CITIES = new WorldCity[citiesList.size()];

			for (int i = 0; i < citiesList.size(); i++)
				SeedDataUtil.CITIES[i] = new WorldCity(	ID_SENTINAL++,
														citiesList.get(i)[0]);
			csvReader.close();
			
		}catch(IOException e){
			LAST_SKIP_INTERVAL = skipInterval;
			e.printStackTrace();
		}

		LAST_SKIP_INTERVAL = skipInterval;
		return CITIES;
	}


	private static final HashMap<Character, List<WorldCity>>
		populateGroupedCities(Context context, int skipInterval){

		if (GROUPED_CITIES == null)
			GROUPED_CITIES = new HashMap<Character, List<WorldCity>>();

		if (GROUPED_CITIES.keySet() == null
			|| GROUPED_CITIES.keySet()
								.isEmpty())
			populateCharacterKeys();

		CITIES = getCities(	context,
							skipInterval);

		List<WorldCity> mCityGroup;
		for (WorldCity city : CITIES){

			char mKey = city.toString()
							.toLowerCase()
							.trim()
							.charAt(0);

			mCityGroup = GROUPED_CITIES.get(mKey);

			if (mCityGroup == null){
				mCityGroup = new ArrayList<WorldCity>();
				GROUPED_CITIES.put(	mKey,
									mCityGroup);
			}

			mCityGroup.add(city);
		}

		return GROUPED_CITIES;
	}


	private static final void populateCharacterKeys(){

		GROUPED_CITIES = new HashMap<Character, List<WorldCity>>(25);

		for (int i = 0; i < 26; i++)
			GROUPED_CITIES.put(	(char) ('a' + i),
								null);


	}
}
