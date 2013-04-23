package com.kemallette.ListBoostDemo.Task;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 */
public class Tasks{

	/**
	 * An array of sample tasks.
	 */
	public static List<Task>			TASKS		= new ArrayList<Task>();

	/**
	 * A map of sample tasks, by ID.
	 */
	public static Map<Integer, Task>	TASK_MAP	= new HashMap<Integer, Task>();


	static{
		// Add sample tasks.
		addTask(new Task(	1,
							"<b>Take out trash</b>",
							"<br><i>Garbage trucks pick-up on Monday mornings.</i>"));

		addTask(new Task(	2,
							"<b>Pick-up Groceries</b>",
							"<br><i>Check coupons before heading out.</i>"));
		addTask(new Task(	3,
							"<b>Do work</b>",
							"<br><i>Finish implementing ListBoost.</i>"));
		addTask(new Task(	4,
							"<b>Dishes</b>",
							"<br><i>Hand wash the nicer dishes.</i>"));
		addTask(new Task(	5,
							"<b>Laundry</b>",
							"<br><i>Seperate colors like boss.</i>"));
		addTask(new Task(	6,
							"<b>Do more work</b>",
							"<br><i>Finish implementing RichEditText.</i>"));
		addTask(new Task(	7,
							"<b>Workout</b>",
							"<br><i>Don't ever skip legs.</i>"));
		addTask(new Task(	8,
							"<b>Relax</b>",
							"<br><i>You deserve it. Sort of.</i>"));
		addTask(new Task(	9,
							"<b>Do even more work</b>",
							"<br><i>Finish implementing LiftProPremium.</i>"));
		addTask(new Task(	10,
							"<b>Set up Pebble SDK</b>",
							"<br><i>Better learn C.</i>"));
		addTask(new Task(	11,
							"<b>Waste time making task lists</b>",
							"<br><i>But it feels like I'm being productive!</i>"));
		addTask(new Task(	12,
							"<b>Go outside</b>",
							"<br><i>Make sure you get your daily dose of Vitamin D.</i>"));

	}


	private static void addTask(Task task){

		TASKS.add(task);
		TASK_MAP.put(	task.id,
						task);
	}

	public static class Task{

		public int		id;
		public String	title;
		public String	content;


		public Task(int id,
					String title,
					String content){

			this.id = id;
			this.title = title;
			this.content = content;
		}


		@Override
		public String toString(){

			return title
					+ content;
		}
	}
}
