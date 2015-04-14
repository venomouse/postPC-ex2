package il.ac.huji.todolist;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.format.Time;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;


public class TodoListManagerActivity extends ActionBarActivity {

    ArrayList<TaskEntry> tasks;
    ArrayAdapter<TaskEntry> taskAdapter;
    //this variable is used to checking overdue dates
    GregorianCalendar today;
    ListView lstTodoItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list_manager);

        tasks = new ArrayList<TaskEntry> ();
        today = new GregorianCalendar();

        lstTodoItems = (ListView)findViewById(R.id.lstTodoItems);

        //defining the adapter for taskEntries
        taskAdapter = new ArrayAdapter<TaskEntry>(
                this, android.R.layout.simple_list_item_1, tasks){

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                LayoutInflater inflater =
                        LayoutInflater.from(getContext());
                View view = inflater.inflate(R.layout.row,null);
                TextView txtTodoTitle = (TextView) view.findViewById(R.id.txtTodoTitle);
                txtTodoTitle.setText(getItem(position).getTask());
                TextView txtTodoDueDate = (TextView) view.findViewById(R.id.txtTodoDueDate);
                txtTodoDueDate.setText(getItem(position).getDateString());

                //overdue tasks are in red
                Date taskDate = getItem(position).getDate();
                if(taskDate.before(today.getTime()))
                {
                    txtTodoTitle.setTextColor(Color.RED);
                    txtTodoDueDate.setTextColor(Color.RED);
                }

                return view;
            }
        };

        lstTodoItems.setAdapter(taskAdapter);

         //delete dialog on long click
        lstTodoItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                return callContextMenuDialog(parent, view, position, id);
            }
        });
    }

    /**
     * This function is the responsible for the context menu
     */


    public boolean callContextMenuDialog(AdapterView<?> parent, View view,
                                   int position, long id)
    {
        final TaskEntry currItem = (TaskEntry) lstTodoItems.getItemAtPosition(position);

        final AlertDialog alertDialog = new AlertDialog.Builder(view.getContext()).create();
        alertDialog.setTitle(currItem.getTask());

        LayoutInflater inflater = this.getLayoutInflater();
        View contextDialogView = inflater.inflate(R.layout.context_dialog, null);

        Button menuItemDelete = (Button) contextDialogView.findViewById(R.id.menuItemDelete);

        menuItemDelete.setOnClickListener(new View.OnClickListener() {
                                              @Override
                                              public void onClick(View v) {

                                                  tasks.remove(currItem);
                                                  taskAdapter.notifyDataSetChanged();
                                                  alertDialog.hide();

                                              }
                                          }
        );

        Button menuItemCall = (Button) contextDialogView.findViewById(R.id.menuItemCall);
        String keyWord = "Call ";
        //if we have a call task, add call button
        if (currItem.getTask().startsWith(keyWord))
        {
            final String number = currItem.getTask().substring(keyWord.length());
            menuItemCall.setVisibility(View.VISIBLE);
            menuItemCall.setText(currItem.getTask());
            menuItemCall.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    Intent dial = new Intent(Intent.ACTION_DIAL,
                                                        Uri.parse("tel:" + number));
                                                    startActivity(dial);
                                                }
                                            }
            );
        }

        alertDialog.setView(contextDialogView);
        alertDialog.show();
        return true;
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
            //start the activity of adding the item
            Intent intent = new Intent(this, AddNewTodoItemActivity.class);
            startActivityForResult(intent, 1);

        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Deals with the results of AddNewTodoItemActivity
     */
    public void  onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == 1 )
        {
            if(resultCode == RESULT_OK){
                String task =data.getStringExtra("title");
                Date date = (Date) data.getSerializableExtra("dueDate");
                GregorianCalendar d= new GregorianCalendar(date.getYear(), date.getMonth(), date.getDay());

                TaskEntry newTask = new TaskEntry(task, date.getDay(),  date.getMonth(), date.getYear());
                tasks.add(newTask);
                taskAdapter.notifyDataSetChanged();

            }
            if (resultCode == RESULT_CANCELED) {
                // do nothing
            }
        }
    }

    /**
     * This class represents an entry at the todolist
     */
    class TaskEntry
    {
        String task;
        Date date;
        SimpleDateFormat df;

        TaskEntry(String task, int day, int month, int year)
        {
            this.task = task;
            GregorianCalendar calendar = new GregorianCalendar(year, month, day);
            this.date = calendar.getTime();
            this.df = new SimpleDateFormat("dd/MM/yyyy");
        }

        public String getTask()
        {
            return task;
        }

        public String getDateString()
        {
            if (date == null )
            {
                return ("No due date");
            }

            return df.format(date);
        }

        public Date getDate()
        {
            return date;
        }

    }


}
