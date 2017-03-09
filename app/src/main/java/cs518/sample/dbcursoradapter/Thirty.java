package cs518.sample.dbcursoradapter;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

/* 
 * Thirty.class
 * 
 * This is a very simple Activity 
 * It is used to populate and display the contents of a simple display
 * It uses a DBHelper class for the sqlite database
 * It uses the get30() DBHelper method for a subset of data
 * The onCreate() and onItemClick() are very similar to DatabaseActivity.class
 * I copied it for simplicity.
 * 
 * Again: Note this Activity performs queries on the UI thread, this is not a
 * good practice and can lead to ANR, lagging display etc.
 * Soon we will look at ways to avoid long running code on the main UI thread.
 */
public class Thirty extends Activity  {
	private DBHelper dbh;

	private SimpleCursorAdapter sca;
	private Cursor cursor;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_30);
		// the projection (fields from the database that we want to use)
		String[] from = { DBHelper.COLUMN_ID,  DBHelper.COLUMN_CLASS,
				DBHelper.COLUMN_GRADE };
		// matching fields on the UI
		int[] to = { R.id.tvid, R.id.tvclass, R.id.tvgrade };
		dbh = DBHelper.getDBHelper(this);
		ListView lv = (ListView) findViewById(R.id.list30);
		
		/*
		 * The helper returns a cursor, which is a set of database records.
		 */
		cursor = dbh.get30();
		sca = new SimpleCursorAdapter(this, R.layout.id_mark_row, cursor, from,
				to, 0);

		lv.setAdapter(sca);
		lv.setOnItemClickListener(showThis);
	}

	/*
	 * This is an Item Click Listener, for use with the ListView When an item in
	 * the list is clicked, the corresponding database record is shown in a toast.
	 */
	public OnItemClickListener showThis = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Cursor temp = (Cursor) parent.getItemAtPosition(position);
			String firstName = temp.getString(temp.getColumnIndex(DBHelper.COLUMN_FIRST_NAME));
			String lastName = temp.getString(temp.getColumnIndex(DBHelper.COLUMN_LAST_NAME));
			
			Toast.makeText(getApplicationContext(), "Student: "+firstName+" "+lastName, Toast.LENGTH_SHORT).show();
		}
	}; // showThis()

}