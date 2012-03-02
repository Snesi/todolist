package com.me.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class TodoDBAdapter {

	// Database fields
	public static final String KEY_ROWID = "_id";
	public static final String KEY_TASK= "task";
	public static final String KEY_STATE= "state";
	private static final String DATABASE_TABLE = "todolist";
	private Context context;
	private SQLiteDatabase database;
	private MySQLiteHelper dbHelper;

	public TodoDBAdapter(Context context) {
		this.context = context;
	}

	public TodoDBAdapter open() throws SQLException {
		dbHelper = new MySQLiteHelper(context);
		database = dbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		dbHelper.close();
	}

	public long createTask(String task, int completed) {
		ContentValues values = new ContentValues();
		values.put(KEY_TASK, task);
		values.put(KEY_STATE, completed);
		Log.d("DBTEST", values.toString());
		return database.insert(DATABASE_TABLE, null, values);
	}
	
	public boolean isTaskChecked(String task){
		Cursor c = database.query(DATABASE_TABLE, new String[] { KEY_STATE }, 
				KEY_TASK+"=?", new String[] {task}, null,
				null, null);
		Log.d("DBTEST", ""+c.getInt(c.getColumnIndex(KEY_STATE)));
		return c.getInt(c.getColumnIndex(KEY_STATE))==1;
	}
	
	public long updateTask(String task, int completed) {
		ContentValues values = new ContentValues();
		values.put(KEY_STATE, completed);
		Log.d("DBTEST", "UPDATING value" + values.toString());
		return database.update(DATABASE_TABLE, values, 
				KEY_TASK+"=?", new String[] {task});
	}
	
	public long deleteDB(){
		return database.delete(DATABASE_TABLE, null, null);
	}
	
	public long deleteTask(String task) {		
		return database.delete(DATABASE_TABLE, KEY_TASK+"=?", new String[] {task});
	}
	
	public long deleteCompleteTasks(){
		return database.delete(DATABASE_TABLE, KEY_STATE+"=?", new String[] {""+1});
	}

	public Cursor fetchAllTasks() {
		return database.query(DATABASE_TABLE, new String[] { KEY_ROWID,
				KEY_TASK, KEY_STATE }, null, null, null,
				null, null);
	}
	
	public Cursor fetchTask(String task){
		return database.query(DATABASE_TABLE, new String[] { KEY_ROWID,
				KEY_TASK, KEY_STATE }, KEY_TASK+"=?",
				new String[] {task}, null, null, null);
	}
	
}
