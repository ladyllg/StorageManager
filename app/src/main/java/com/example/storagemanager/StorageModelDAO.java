package com.example.storagemanager;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;
import android.widget.Toast;


import java.util.ArrayList;

public class StorageModelDAO {

    private Context context;
    private static ArrayList<StorageModel> storagesList = new ArrayList<>();

    private final String TABLE_STORAGES = "storages";
    private DbGateway gw;

    public StorageModelDAO(Context context) {
        this.context = context;
        gw = DbGateway.getInstance(context);
    }

    public ArrayList<StorageModel> getList() {
        storagesList = new ArrayList<>();
        Cursor cursor = gw.getDatabase().rawQuery("SELECT * FROM storages", null);
        while(cursor.moveToNext()){
            int id = cursor.getInt(0);
            int nro = cursor.getInt(1);
            float price = cursor.getFloat(2);
            String categoryString = cursor.getString(3);
            float altura = cursor.getFloat(4);
            float profundidade = cursor.getFloat(5);
            float largura = cursor.getFloat(6);
            storagesList.add(new StorageModel(id, nro, profundidade, largura, altura, price, categoryString));
        }
        cursor.close();
        return storagesList;
    }

    public boolean add(StorageModel storage) {
        String sql = "INSERT INTO storages VALUES (NULL, "
                + "'" + storage.getNro() + "', "
                + "'" + storage.getPrice() + "', "
                + "'" + storage.getCategoryString() + "', "
                + "'" + storage.getAltura() + "', "
                + "'" + storage.getProfundidade() + "', "
                + "'" + storage.getLargura() + "')";

        Log.i("Banco de Dados", ""+sql);
        try {
            gw.getDatabase().execSQL(sql);
            Toast.makeText(context, "Storage adicionado!", Toast.LENGTH_SHORT).show();
            return true;
        }
        catch (SQLException e) {
            Toast.makeText(context, "Erro! " + e.getMessage(), Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public boolean update(StorageModel storage) {
        String sql = "UPDATE storages SET "
                + "nro='" + storage.getNro() + "', "
                + "price='" + storage.getPrice() + "', "
                + "categoria='" + storage.getCategoryString() + "', "
                + "altura='" + storage.getAltura() + "', "
                + "profundidade='" + storage.getProfundidade() + "', "
                + "largura='" + storage.getLargura() + "' "
                + "WHERE id=" + storage.getId();

        Log.i("Banco de Dados", ""+sql);
        try {
            gw.getDatabase().execSQL(sql);
            Toast.makeText(context, "Storage atualizada!", Toast.LENGTH_SHORT).show();
            return true;
        }
        catch (SQLException e) {
            Toast.makeText(context, "Erro! " + e.getMessage(), Toast.LENGTH_SHORT).show();
            return false;
        }

    }

    public StorageModel get(int id) {
        String sql = "SELECT * FROM storages WHERE id=" + id;
        Cursor cursor = gw.getDatabase().rawQuery(sql, null);

        if (cursor.moveToNext()) {
            int idStorage = cursor.getInt(0);
            int nro = cursor.getInt(1);
            float price = cursor.getFloat(2);
            String categoryString = cursor.getString(3);
            float altura = cursor.getFloat(4);
            float profundidade = cursor.getFloat(5);
            float largura = cursor.getFloat(6);
            return new StorageModel(idStorage, nro, profundidade, largura, altura, price, categoryString);
        }

        return null;
    }

    public boolean delete(int id) {
        String sql = "SELECT * FROM orders WHERE storage_idstorage=" + id;
        Cursor cursor = gw.getDatabase().rawQuery(sql, null);
        if((cursor != null) && (cursor.getCount() > 0)){
            return false;
        }
        return gw.getDatabase().delete("storages", "ID=?", new String[]{ id + "" }) > 0;
    }

}
