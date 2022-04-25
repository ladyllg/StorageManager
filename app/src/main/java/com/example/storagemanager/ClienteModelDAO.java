package com.example.storagemanager;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

public class ClienteModelDAO {
    private Context context;
    private static ArrayList<ClienteModel> clientsList = new ArrayList<>();

    private final String TABLE_STORAGES = "clients";
    private DbGateway gw;

    public ClienteModelDAO(Context context) {
        this.context = context;
        gw = DbGateway.getInstance(context);
    }

    public ArrayList<ClienteModel> getList(String filter) {
        clientsList = new ArrayList<>();
        Cursor cursor = gw.getDatabase().rawQuery("SELECT * FROM clients " + filter, null);
        while(cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            int closed = cursor.getInt(2);
            float total = cursor.getFloat(3);
            clientsList.add(new ClienteModel(id, name, closed, total));
        }
        cursor.close();
        return clientsList;
    }

    public boolean add(ClienteModel client) {
        String sql = "INSERT INTO clients VALUES (NULL, "
                + "'" + client.getName() + "',"
                + "'" + client.isClosed() + "',"
                + "'" + client.getTotal() + "')";

        Log.i("Banco de Dados", ""+sql);
        try {
            gw.getDatabase().execSQL(sql);
            Toast.makeText(context, "Cliente adicionado!", Toast.LENGTH_SHORT).show();
            return true;
        }
        catch (SQLException e) {
            Toast.makeText(context, "Erro! " + e.getMessage(), Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public boolean update(ClienteModel client) {
        String sql = "UPDATE clients SET "
                + "nome='" + client.getName() + "' "
                + "WHERE id=" + client.getId();

        Log.i("Banco de Dados", ""+sql);
        try {
            gw.getDatabase().execSQL(sql);
            Toast.makeText(context, "Cliente atualizada!", Toast.LENGTH_SHORT).show();
            return true;
        }
        catch (SQLException e) {
            Toast.makeText(context, "Erro! " + e.getMessage(), Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public ClienteModel get(int id) {
        String sql = "SELECT * FROM clients WHERE id=" + id;
        Cursor cursor = gw.getDatabase().rawQuery(sql, null);

        if (cursor.moveToNext()) {
            int idCliente = cursor.getInt(0);
            String nomeCliente = cursor.getString(1);
            int closed = cursor.getInt(2);
            float total = cursor.getFloat(3);
            return new ClienteModel(idCliente, nomeCliente, closed,total);
        }

        return null;
    }

    public boolean closeClient(ClienteModel client) {
        String sql = "UPDATE clients SET closed = 1, total="+ client.getTotal() +" WHERE id=" + client.getId();

        Log.i("Banco de Dados", ""+sql);
        try {
            gw.getDatabase().execSQL(sql);
            Toast.makeText(context, "Conta do Cliente fechada com sucesso!!!", Toast.LENGTH_SHORT).show();
            return true;
        }
        catch (SQLException e) {
            Toast.makeText(context, "Erro! " + e.getMessage(), Toast.LENGTH_SHORT).show();
            return false;
        }

    }

    public boolean delete(int id) {
        return gw.getDatabase().delete("clients", "ID=?", new String[]{ id + "" }) > 0;
    }

    public ArrayList<AluguelModel> getRents(ClienteModel client) {
        ArrayList<AluguelModel> rentsList = new ArrayList<AluguelModel>();
        Cursor cursor = gw.getDatabase().rawQuery("SELECT * FROM orders WHERE client_idclient="+client.getId() , null);
        while(cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String  startDate = cursor.getString(1);
            String endDate = cursor.getString(2);
            int insurance = cursor.getColumnIndex("insurance");
            int climateControl = cursor.getColumnIndex("climate_control");
            int extraKey = cursor.getColumnIndex("extra_key");
            int idStorage = cursor.getInt(6);
            int idClient =cursor.getInt(7);
            float total = cursor.getFloat(8);
            rentsList.add(new AluguelModel(id,startDate,endDate,idStorage,idClient,insurance,extraKey,climateControl,total));
        }
        cursor.close();
        return rentsList;
    }

}
