package il.ac.huji.todolist;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Date;
import java.util.GregorianCalendar;


public class AddNewTodoItemActivity extends Activity {

    DatePicker datePicker;
    EditText edtNewItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_todo_item);
        final DatePicker datePicker = (DatePicker) findViewById(R.id.datePicker);

        Button btnOK = (Button) findViewById(R.id.btnOK);
        Button btnCancel = (Button) findViewById(R.id.btnCancel);

        edtNewItem = (EditText) findViewById(R.id.edtNewItem);

        //if OK was clicked, we pass data back to the main activity
        btnOK.setOnClickListener(new View.OnClickListener() {
                                     @Override
                                     public void onClick(View v) {
                                         Intent returnIntent = new Intent();
                                         returnIntent.putExtra("title",edtNewItem.getText().toString());

                                         Date dueDate = transformDate(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
                                         returnIntent.putExtra("dueDate", dueDate);

                                         setResult(RESULT_OK, returnIntent);
                                         finish();
                                     }
                                 }
        );

        //else, do nothing
        btnCancel.setOnClickListener(new View.OnClickListener() {
                                     @Override
                                     public void onClick(View v) {
                                         Intent returnIntent = new Intent();
                                         setResult(RESULT_CANCELED, returnIntent);
                                         finish();
                                     }
                                 }
        );



    }

    public Date transformDate (int year, int month, int day)
    {
        GregorianCalendar calendar = new GregorianCalendar(year, month, day);
        return calendar.getTime();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_new_todo_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
