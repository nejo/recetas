package com.recetas;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class Ver extends Activity {
	
	private SQLiteDatabase db = null;
	Cursor cursor;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        
    	super.onCreate(savedInstanceState);
    	
        setContentView(R.layout.ver);
        
        db = (new Database(this)).getWritableDatabase();
		
        cursor = db.rawQuery("SELECT " + Database.NOMBRE + ", " + Database.INGREDIENTES + ", " + Database.ELABORACION + " FROM " + Database.DATABASENAME + " LIMIT 1;", null);
        
        if( cursor.moveToFirst() ) {

        	int index;
        	
	        TextView nombreValue = (TextView) findViewById(R.id.nombreValue);
	        index = cursor.getColumnIndex(Database.NOMBRE);
	        nombreValue.setText(cursor.getString(index));
	        
	        TextView ingredientesList = (TextView) findViewById(R.id.ingredientesList);
	        index = cursor.getColumnIndex(Database.INGREDIENTES);
	        ingredientesList.setText(cursor.getString(index));
	        
	        TextView elaboracionValue = (TextView) findViewById(R.id.elaboracionValue);
	        index = cursor.getColumnIndex(Database.ELABORACION);
	        elaboracionValue.setText(cursor.getString(index));
        }

    }
    
    public void onDestroy() {
    	
    	super.onDestroy();
    	
    	cursor.close();
    }
}
