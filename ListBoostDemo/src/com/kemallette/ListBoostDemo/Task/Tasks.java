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
							"Take out trash",
							"Garbage trucks pick-up on Monday mornings."));

		addTask(new Task(	2,
							"Pick-up Groceries",
							"Check coupons before heading out."));
		addTask(new Task(	3,
							"Do work",
							"Finish implementing ListBoost."));
		addTask(new Task(	4,
							"Dishes",
							"Hand wash the nicer dishes."));
		addTask(new Task(	5,
							"Laundry",
							"Seperate colors like boss."));
		addTask(new Task(	6,
							"Do more work",
							"Finish implementing RichEditText"));
		addTask(new Task(	7,
							"Workout",
							"Don't ever skip legs."));
		addTask(new Task(	8,
							"Relax",
							"You deserve it. Sort of."));
		addTask(new Task(	9,
							"Do even more work",
							"Finish implementing LiftProPremium."));
		addTask(new Task(	10,
							"Set up Pebble SDK",
							"Better learn C."));
		addTask(new Task(	11,
							"Waste time making task lists",
							"But it feels like I'm being productive!"));
		addTask(new Task(	12,
							"Go outside",
							"Make sure you get your daily dose of Vitamin D."));

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
					+ "\n"
					+ content;
		}
	}
}
