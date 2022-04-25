package com.example.storagemanager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "SelfStorage.db";
    private static final int DATABASE_VERSION = 4;

    private final String CREATE_STORAGE_TABLE = "CREATE TABLE storages " +
            "                           (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "                           nro INTEGER NOT NULL DEFAULT '1', " +
            "                           price FLOAT NOT NULL DEFAULT '0', " +
            "                           categoria TEXT DEFAULT 'Guarda-Volumes'," +
            "                           altura FLOAT NOT NULL DEFAULT '0'," +
            "                           profundidade FLOAT NOT NULL DEFAULT '0'," +
            "                           largura FLOAT NOT NULL DEFAULT '0');";

    private final String CREATE_ORDERS_TABLE = "CREATE TABLE orders " +
            "                           (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "                           start_date date  NOT NULL ," +
            "                           end_date date  NOT NULL ," +
            "                           insurance tinyint(4) NOT NULL DEFAULT '0'," +
            "                           climate_control tinyint(4) NOT NULL DEFAULT '0'," +
            "                           extra_key tinyint(4) NOT NULL DEFAULT '0'," +
            "                           storage_idstorage int(11) NOT NULL," +
            "                           client_idclient int(11) NOT NULL," +
            "                           total FLOAT NOT NULL DEFAULT '0');";

    private final String SQL_POPULATE_STORAGES_PASS = "INSERT INTO storages VALUES " +
            "(3,1,550,'Guarda-Volumes',45,12,45)," +
            "(5,3,1500.78,'Container',12,16,9)," +
            "(7,4,500.78,'Quarto',3.5,4,5)," +
            "(8,5,600,'Galpão',5,10.5,20.5)";

    String SQL_POPULATE_CLIENTS_PASS = "INSERT INTO clients VALUES (2,'Laurinha', 0, 0),(5,'Jedi 22323', 1, 560.89)";

    String SQL_POPULATE_RENTS_PASS = "INSERT INTO orders VALUES" + "(1,'2022-07-23','2022-07-28',0,0,0,3,2,550), (2,'2022-07-23','2022-07-28',0,0,0,5,2,1500.78), (3,'2022-07-23','2022-07-28',0,0,0,5,2,1500.78), (4,'2022-07-23','2022-07-28',0,0,0,5,2,1500.78),(5,'2022-07-23','2022-07-28',0,0,0,5,2,1500.78),(6,'2022-07-23','2022-07-28',0,0,0,5,2,1500.78),(7,'2022-07-23','2022-07-28',0,0,0,5,2,1500.78),(8,'2022-07-23','2022-07-28',0,0,0,5,2,1500.78)";


    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CLIENT_TABLE = "CREATE TABLE IF NOT EXISTS clients (ID INTEGER PRIMARY KEY AUTOINCREMENT, nome TEXT NOT NULL, closed tinyint(4) NOT NULL DEFAULT '0', total FLOAT NOT NULL DEFAULT '0');";
        db.execSQL(CREATE_CLIENT_TABLE);
        db.execSQL(SQL_POPULATE_CLIENTS_PASS);
        Log.i("BANCO DE DADOS", "Table Clients criado.");

        db.execSQL(CREATE_STORAGE_TABLE);
        db.execSQL(SQL_POPULATE_STORAGES_PASS);
        Log.i("BANCO DE DADOS", "Table Storages criado.");

        db.execSQL(CREATE_ORDERS_TABLE);
        db.execSQL(SQL_POPULATE_RENTS_PASS);
        Log.i("BANCO DE DADOS", "Table Orders criado.");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

}
