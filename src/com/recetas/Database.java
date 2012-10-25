package com.recetas;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {

	public static final String DATABASENAME = "recetas";
	public static final String NOMBRE = "nombre";
	public static final String INGREDIENTES = "ingredientes";
	public static final String ELABORACION = "elaboracion";

	public Database(Context context) {
		
		super(context, DATABASENAME, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		db.execSQL("CREATE TABLE "
				+ DATABASENAME
				+ " (_id INTEGER PRIMARY KEY, nombre TEXT, ingredientes TEXT, elaboracion TEXT);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		db.execSQL("DROP TABLE IF EXISTS " + DATABASENAME);
		
		this.onCreate(db);
	}

}
