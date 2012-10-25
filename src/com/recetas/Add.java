package com.recetas;

import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Add extends Activity implements OnClickListener, OnItemSelectedListener {
	
	private static final String DATABASENAME = "recetas";
	String[] ingredientesValues = { "arroz", "lechuga", "queso", "aceite"
			, "pasta", "carne", "tomate", "sal", "caracoles"
			, "azúcar", "pimiento", "huevos", "vinagre", "pescado"
			, "leche", "nata líquida" };
	public Spinner ingredientesSelect = null;
	private ArrayList<String> ingredientes = null;
	Button buttonSave;
	private SQLiteDatabase db = null;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        
    	super.onCreate(savedInstanceState);
    	
        setContentView(R.layout.add);
        
        db = (new Database(this)).getWritableDatabase();
		
        ingredientesSelect = (Spinner) findViewById(R.id.ingredientesSelect);
        ingredientes = new ArrayList<String>();
        
    	populateSpinner();
    	
        ingredientesSelect.setOnItemSelectedListener(this);
        
        buttonSave = (Button) findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(this);
    }
	
	@Override
	public void onDestroy() {
		
		super.onDestroy();
		db.close();
	}

	@Override
	public void onClick(View v) {
		
		Intent i = null;
		int id = v.getId();
		
		switch( id ) {
		
			case R.id.buttonSave:
				this.saveReceta();
				finish();
				break;
		}
	}
	
	public boolean onCreateOptionsMenu(Menu menu){
		
		MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add, menu);
        return true;
	}

	public boolean onOptionsItemSelected(MenuItem item){
		
		Intent i = null;
		int itemId = item.getItemId();
		
		switch( itemId ) {
	   
		   case R.id.gotomain_menu:
			   i = new Intent(this, Listado.class);
			   startActivity(i);
			   break;
		   case R.id.help_menu:
			   i = new Intent(this, Ayuda.class);
			   startActivity(i);
			   break;
		}
		
	   return true;
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
		
		String valor = parent.getItemAtPosition(pos).toString();
		
		TextView ingredientesText = (TextView) findViewById(R.id.ingredientesList);
		ingredientesText.append(valor+", ");
		
		ingredientes.add(valor);
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// nothing
	}
	
	private void populateSpinner() {

		ArrayAdapter<String> ad = new ArrayAdapter<String> (this, android.R.layout.simple_dropdown_item_1line, ingredientesValues);
	    ingredientesSelect.setAdapter(ad);
	}
	
	private void saveReceta() {
		
		String nombre = ((EditText)findViewById(R.id.nombreInput)).getText().toString().trim();
		String elaboracion = ((EditText)findViewById(R.id.nombreInput)).getText().toString().trim();
		String ingredientes = ((TextView)findViewById(R.id.ingredientesList)).getText().toString().trim();
		
		ContentValues valores = new ContentValues();
		valores.put("NOMBRE", nombre);
		valores.put("INGREDIENTES", ingredientes);
		valores.put("ELABORACION", elaboracion);
		
		db.insert(DATABASENAME, "nombre", valores);
		
		Intent intent = this.getIntent();
		intent.putExtra("nombre", nombre);
		this.setResult(RESULT_OK, intent);
	}
}