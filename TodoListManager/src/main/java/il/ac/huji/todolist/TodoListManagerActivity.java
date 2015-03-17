package il.ac.huji.todolist;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class TodoListManagerActivity extends ActionBarActivity {

    ArrayList<String> tasks;
    EditText edtNewItem;
    ArrayAdapter<String> taskAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list_manager);
        tasks = new ArrayList<String>();

        edtNewItem = (EditText) findViewById(R.id.edtNewItem);

        final ListView lstTodoItems = (ListView)findViewById(R.id.lstTodoItems);
        taskAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, tasks){

            //custom function to alternate text colors
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                LayoutInflater inflater =
                        LayoutInflater.from(getContext());
                View view = inflater.inflate(R.layout.row,null);
                TextView task = (TextView) view.findViewById(R.id.task);
                task.setText(getItem(position));
                if(position%2==0)
                {
                    task.setTextColor(Color.RED);
                }
                else
                {
                    task.setTextColor(Color.BLUE);
                }

                return view;
            }
        };
        lstTodoItems.setAdapter(taskAdapter);
         //delete dialog on long click
        lstTodoItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Object o = lstTodoItems.getItemAtPosition(position);
                final String itemToRemove = (String) o;

                AlertDialog alertDialog = new AlertDialog.Builder(view.getContext()).create();
                alertDialog.setTitle(itemToRemove);

                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        tasks.remove(itemToRemove);
                        taskAdapter.notifyDataSetChanged();

                    }
                });

                alertDialog.show();
                return true;
            }

            });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_todo_list_manager, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //task adding option
        if (id == R.id.menuItemAdd) {
            tasks.add(edtNewItem.getText().toString());
            edtNewItem.getText().clear();
            taskAdapter.notifyDataSetChanged();

        }

        return super.onOptionsItemSelected(item);
    }


}
