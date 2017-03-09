package cs518.sample.dbcursoradapter;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

/** 
 * DataBaseActivity.class
 * 
 * This is the main Activity , as indicated in the AndroidManifest.xml
 * It is used to populate and display the contents of a simple display
 * It uses a DBHelper class for the sqlite database
 * 
 * Wherever strings constants are used inline "like this" you should use
 * the values directories, the same with any integers, arrays, dimensions or colour constants.
 * 
 * This code uses a ListView with multiple TextViews and a SimpleCursorAdapter 
 * Look at the code SQLite-simple-db to see how to do the same thing
 * but completely programmatically.  
 * This is more efficient and more future proof than implementing it yourself
 * 
 * This code implements an options menu programmatically. 
 * Each menu item click will fire an intent.  
 * 
 * Note this Activity performs queries on the UI thread, this is not a
 * good practice and can lead to ANR, lagging display etc.
 * Soon we will look at ways to avoid long running code on the main UI thread.
 * 
 */
public class DatabaseActivity extends Activity {

	// My DBHelper class
	private static DBHelper dbh;
	public static final int SHOW_AS_ACTION_IF_ROOM = 1;

	private SimpleCursorAdapter sca;
	private Cursor cursor;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// the projection (fields from the database that we want to use)
		String[] from = { DBHelper.COLUMN_ID, DBHelper.COLUMN_FIRST_NAME,
				DBHelper.COLUMN_LAST_NAME, DBHelper.COLUMN_CLASS,
				DBHelper.COLUMN_GRADE };
		// matching fields on the layout to be used with the adapter
		int[] to = { R.id.tv1, R.id.tv2, R.id.tv3, R.id.tv4, R.id.tv5 };
		setContentView(R.layout.activity_main);
		dbh = DBHelper.getDBHelper(this);
		ListView lv = (ListView) findViewById(R.id.list);
		/*
		 * public SimpleCursorAdapter (Context context, int layout, Cursor c,
		 * String[] from, int[] to, int flags)
		 * 
		 * -layout resource identifier of a layout file that defines the views
		 * for this list item. The layout file should include at least those
		 * named views defined in "to"
		 * 
		 * -c The database cursor. Can be null if the cursor is not available
		 * yet.
		 * 
		 * -from A list of column names representing the data to bind to the UI.
		 * Can be null if the cursor is not available yet.
		 * 
		 * -to The views that should display column in the "from" parameter.
		 * These should all be TextViews. The first N views in this list are
		 * given the values of the first N columns in the from parameter. Can be
		 * null if the cursor is not available yet.
		 * 
		 * -flags Flags used to determine the behavior of the adapter, as per
		 * CursorAdapter(Context, Cursor, int).
		 */
		/*
		 * The helper returns a cursor, which is a set of database records.
		 */
		cursor = dbh.getGrades();
		sca = new SimpleCursorAdapter(this, R.layout.grade_row, cursor, from,
				to, 0);

		lv.setAdapter(sca);
		lv.setOnItemClickListener(deleteThis);
		// don't close the cursor, it is used by the adapter
		// cursor.close();
	}

	public void onResume() {
		super.onResume();
		logIt("onResume()");
		// we need to get a new cursor and tell the adapter there is new data
		// sca.notifyDataSetChanged(); is not enough if we have added data 
		// to the db it is not in the current cursor
		refreshView();
		/* 
		 * NOTE it would be better to not do the refresh here but do it dependent 
		 * on the activity result for AddStudent (added or not).  This way the
		 * cursor is recreated no matter what happens (data added or not.)
		 * I leave it for you to try modifying this code to "fix" this.
		 */
	}
	
	public void onPause() {
		super.onPause();
		logIt("onPause()");
		// release resources
		dbh.close();
	}
	public void refreshView() {
		logIt("refreshView()");
		// renew the cursor
		cursor = dbh.getGrades();
		// have the adapter use the new cursor, changeCursor closes old cursor too
		sca.changeCursor(cursor);
		// have the adapter tell the observers
		sca.notifyDataSetChanged();
	}

	/*
	 * This is an Item Click Listener, for use with the ListView When an item in
	 * the list is clicked, the corresponding database record is deleted.
	 */
	public OnItemClickListener deleteThis = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Cursor cursortemp = (Cursor) parent.getItemAtPosition(position);
			for (int i = 0; i < 5; i++)
				logIt(i + "  " + cursortemp.getString(i));
			// CursorAdapter matches id to database record _id
			logIt(" id:" + id);
			// first column is id getInt gets column data as int
			int idDB = cursortemp.getInt(0);
			logIt("_id: " + idDB);
			dbh.deleteStudent((int) id);
			refreshView();
		}
	}; // deleteThis

	@Override
	/*
	 * This is an options menu. There are two items programmatically added to
	 * the menu: add student fires an intent to invoke AddStudent class show 30%
	 * fires an intent to invoke Thirty class
	 * 
	 * Since we are just firing intents with our menu items there is no need to
	 * override onOptionsItemSelected()
	 */
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		// Set up the intents for each button
		Intent add = new Intent(this, AddStudent.class);
		Intent thirty = new Intent(this, Thirty.class);

		// add a menu items with the Intents, should use strings.xml for
		// "Add Student" & "Show 30s"
		/*
		 * This could be done through XML but since we are directly firing an
		 * intent this is more explicit
		 */
		menu.add("Add Student").setIntent(add)
				.setShowAsAction(SHOW_AS_ACTION_IF_ROOM);
		menu.add("Show 30s").setIntent(thirty)
				.setShowAsAction(SHOW_AS_ACTION_IF_ROOM);

		return true;
	}

	// private Context context;
	public void logIt(String msg) {
		final String TAG = "DBCURSOR";
		Log.d(TAG, msg);
	}
}