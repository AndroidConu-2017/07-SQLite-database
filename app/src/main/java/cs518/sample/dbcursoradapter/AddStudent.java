package cs518.sample.dbcursoradapter;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/* 
 * AddStudent.class
 * 
 * This Activity is a UI used simply to add records to the db
 *
 * It uses a DBHelper class for the sqlite database
 */
public class AddStudent extends Activity implements OnClickListener {
	
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_add);
    	
    	Button btn_submit = (Button)findViewById(R.id.btn_submit);
    	btn_submit.setOnClickListener(this);
    }

	public void onClick(View v) {
		DBHelper dbh = DBHelper.getDBHelper(this);
		int grade;
		// get the data from the UI
		String firstName = ((EditText)findViewById(R.id.et_name)).getText().toString();
		String lastName = ((EditText)findViewById(R.id.et_lastName)).getText().toString();
		String className = ((EditText)findViewById(R.id.et_class)).getText().toString();
		try {
			grade = Integer.parseInt(((EditText)findViewById(R.id.et_grade)).getText().toString());
			} catch (NumberFormatException e) {
				grade = -1;    // not a valid grade
			}
		// add the data to the db
		long code = dbh.insertNewStudent(firstName, lastName, className, grade);
		if (code != -1)
			Toast.makeText(this, R.string.insert_ok, Toast.LENGTH_LONG).show();
		// finish() means one entry at a time
		// finish();  
	}
}