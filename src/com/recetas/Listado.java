package com.recetas;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class Listado extends ListActivity implements OnItemClickListener, OnItemLongClickListener, OnCreateContextMenuListener {

	public static final int ADD_CODE = 1;
	private SQLiteDatabase db = null;
	private Cursor cursor = null;
	ArrayAdapter<String> adapter;
    ArrayList<String> listItems = new ArrayList<String>();
    protected static final int CONTEXTMENU_VIEWITEM = 1;
    protected static final int CONTEXTMENU_EDITITEM = 2;
    protected static final int CONTEXTMENU_DELETEITEM = 3;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        
    	super.onCreate(savedInstanceState);
    	
        setContentView(R.layout.list);
        
        db = (new Database(this)).getWritableDatabase();
        this.creaTabla();
        
        ListView lv = getListView();
        //porque setOnCreateContextMenuListener crea automáticamente el onItemLongClick ?
        lv.setOnCreateContextMenuListener(this);
    }
    
    public void onDestroy() {
    	
    	super.onDestroy();
    	
    	cursor.close();
    }
    
    public void onResume() {
    	
    	super.onResume();
    	
    	this.creaTabla();
    }

	public boolean onCreateOptionsMenu(Menu menu){
		
		MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.list, menu);
        return true;
	}

	public boolean onOptionsItemSelected(MenuItem item){
		
		Intent i = null;
		int itemId = item.getItemId();
		
		switch( itemId ) {
	   
		   case R.id.add_menu_list:
			   i = new Intent(this, Add.class);
			   startActivityForResult(i, ADD_CODE);
			   break;
		   case R.id.help_menu_list:
			   i = new Intent(this, Ayuda.class);
			   startActivity(i);
			   break;
		}
		
	   return true;
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
        if (requestCode == ADD_CODE) {
        	
            if (resultCode == RESULT_OK) {
            	
				String msg = data.getStringExtra("nombre");
				Toast toast = Toast.makeText(getApplicationContext(), "Nueva receta: " + msg, Toast.LENGTH_SHORT);
				toast.show();
            }
        }
    }
	
	public void creaTabla() {
		
		cursor = db.rawQuery("SELECT _id, nombre FROM " + Database.DATABASENAME, null);
        ListAdapter adapter = new SimpleCursorAdapter(this,
 				R.layout.row,
 				cursor,
 				new String[] {"_id", "nombre"},
 				new int[] {R.id.idReceta, R.id.nombreReceta});
        setListAdapter(adapter);
        
        // porque setOnCreateContextMenuListener crea automáticamente el onItemLongClick ?
        //ListView lv = getListView();
        //lv.setOnItemLongClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		
		Intent i = new Intent(this, View.class);
		startActivity(i);
	}
	
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		
		int order = Integer.parseInt(((TextView) v.findViewById(R.id.idReceta)).getText().toString());
		
		// TODO Auto-generated method stub
		menu.setHeaderTitle("Acciones");
		menu.add(0, CONTEXTMENU_VIEWITEM, order, "Ver");
		menu.add(0, CONTEXTMENU_EDITITEM, order, "Editar");
		menu.add(0, CONTEXTMENU_DELETEITEM, order, "Eliminar");
	}

	//porque setOnCreateContextMenuListener crea automáticamente el onItemLongClick ?
	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem aItem) {
		
		AdapterContextMenuInfo menuInfo = (AdapterContextMenuInfo) aItem.getMenuInfo();
	
		ListView lv = getListView();
		
		/* Switch on the ID of the item, to get what the user selected. */
		switch (aItem.getItemId()) {
		
			case CONTEXTMENU_VIEWITEM:
				Intent i = new Intent(this, Ver.class);
				startActivity(i);
				return true; /* true means: 'we handled the event'. */
	
				
			case CONTEXTMENU_EDITITEM:
				/* Get the selected item out of the Adapter by its position. */
				lv.getAdapter().getItem(menuInfo.position);
			
				this.creaTabla();
				
				return true; /* true means: 'we handled the event'. */
	
				
			case CONTEXTMENU_DELETEITEM:
				/* Get the selected item out of the Adapter by its position. */
				lv.getAdapter().getItem(menuInfo.position);

				this.deleteReceta(aItem.getOrder());
			
				this.creaTabla();
				
				return true; /* true means: 'we handled the event'. */
			
		}
		
		return false;
	}

	private void deleteReceta(int id) {
		
		db.execSQL("DELETE FROM " + Database.DATABASENAME + " WHERE _id = "+id+";");
	}

}
