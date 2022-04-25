package com.example.storagemanager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DbGateway {

    private static DbGateway gw;
    private SQLiteDatabase db;

    private DbGateway(Context ctx){
        DbHelper helper = new DbHelper(ctx);
        db = helper.getWritableDatabase();
        /** db.execSQL("DROP TABLE IF EXISTS clients");
        db.execSQL("DROP TABLE IF EXISTS storages");
        db.execSQL("DROP TABLE IF EXISTS orders");
        helper.onCreate(db); **/
        Log.i("Banco de Dados", "Conexao Criada!");
    }

    public static DbGateway getInstance(Context ctx){
        if(gw == null)
            gw = new DbGateway(ctx);
        return gw;
    }

    public SQLiteDatabase getDatabase(){
        return this.db;
    }
}
