package com.me.todo;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ResourceCursorAdapter;

import com.me.utils.TodoDBAdapter;

public class ToDoListActivity extends Activity {
    /** Called when the activity is first created. */
	private Menu mMenu;
	private static ArrayList<HashMap<String, String>> todoData = new ArrayList<HashMap<String, String>>();;
	private MyAdapter adapter;
	private static TodoDBAdapter db;
	private Cursor the_cursor;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
    
	protected void onStart(){
		super.onStart();
		
	}
	
    protected void onResume() {
    	super.onResume();
    	db = new TodoDBAdapter(this);
		db.open();
		//db.deleteDB();
		the_cursor = db.fetchAllTasks();
		adapter = new MyAdapter(this, the_cursor);
		
		((Button) findViewById(R.id.b_add)).setOnClickListener(mAddToDoListener);
		
		((ListView)findViewById(R.id.list_todo)).setAdapter(adapter);
    }
    
    protected void onPause(){
		super.onPause();
		this.finish();
		db.close();
	}
    
    protected void onStop(){
    	super.onStop();
    	this.finish();
    	db.close();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Hold on to this
        
        // Inflate the currently selected menu XML resource.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.todo_menu, menu);       
        
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.clear_finished_tasks:
            	db.deleteCompleteTasks();
            	the_cursor = db.fetchAllTasks();
            	adapter.changeCursor(the_cursor);
                return true;

        }   
        return false;
    }
    
    OnClickListener mAddToDoListener = new OnClickListener() {
        public void onClick(View v) {
        	final EditText task = (EditText)findViewById(R.id.editText1);
        	db.createTask(task.getText().toString(), 0);
        	the_cursor = db.fetchAllTasks();
        	adapter.changeCursor(the_cursor);
        }
    };
    
    private class MyAdapter extends ResourceCursorAdapter {
    	
        public MyAdapter(Context context, Cursor cur) {
            super(context, R.layout.todo_row, cur);
        }

        @Override
        public View newView(Context context, Cursor cur, ViewGroup parent) {
            LayoutInflater li = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            return li.inflate(R.layout.todo_row, parent, false);
        }

        @Override
        public void bindView(View view, Context context, Cursor cur) {
            final CheckBox cbListCheck = (CheckBox)view.findViewById(R.id.task);
            cbListCheck.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if(cbListCheck.isChecked())
						db.updateTask(cbListCheck.getText().toString(), 1);
					else
						db.updateTask(cbListCheck.getText().toString(), 0);
				}
			});
            cbListCheck.setText(cur.getString(cur.getColumnIndex(TodoDBAdapter.KEY_TASK)));
            cbListCheck.setChecked((cur.getInt(cur.getColumnIndex(TodoDBAdapter.KEY_STATE))==0? false:true));
        }
    }
}