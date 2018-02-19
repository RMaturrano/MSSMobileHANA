package com.proyecto.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;

public class DataBaseHelper{
	
    private static DataBaseHelper instance;
    private static Context contexto;
    private static MyDataBase cn;
	private static SQLiteDatabase db;

    public DataBaseHelper(Context context) {
    	contexto = context;
    	cn = new MyDataBase(contexto, null, null, MyDataBase.DATABASE_VERSION);
		db = cn.getWritableDatabase();
	}

	public static synchronized DataBaseHelper getHelper(@Nullable Context context)
    {
        if (instance == null && context != null)
            instance = new DataBaseHelper(context);
        else if(instance == null && context == null)
            instance = new DataBaseHelper(contexto);

        return instance;
    }
	
	public SQLiteDatabase getDataBase(){
		return db; 
	}
    
}
