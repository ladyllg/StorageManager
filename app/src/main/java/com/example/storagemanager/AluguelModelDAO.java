package com.example.storagemanager;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

public class AluguelModelDAO {
    private Context context;
    private static ArrayList<AluguelModel> rentsList = new ArrayList<>();
    private DbGateway gw;

    public AluguelModelDAO(Context context) {
        this.context = context;
        gw = DbGateway.getInstance(context);
    }

    public ArrayList<AluguelModel> getList() {
        rentsList = new ArrayList<>();
        Cursor cursor = gw.getDatabase().rawQuery("SELECT * FROM orders", null);
        while(cursor.moveToNext()){
            int id = cursor.getInt(0);
            String  startDate = cursor.getString(1);
            String endDate = cursor.getString(2);
            int insurance = cursor.getInt(3);
            int climateControl = cursor.getInt(4);
            int extraKey = cursor.getInt(5);
            int idStorage = cursor.getInt(6);
            int idClient =cursor.getInt(7);
            float total = cursor.getFloat(8);
            rentsList.add(new AluguelModel(id,startDate,endDate,idStorage,idClient,insurance,extraKey,climateControl, total));
        }
        cursor.close();
        return rentsList;
    }

    public boolean add(AluguelModel rent) {
        if(this.isAvailable(rent)) {
            String sql = "INSERT INTO orders VALUES (NULL, "
                    + "'" + rent.getStartDate() + "', "
                    + "'" + rent.getEndDate() + "', "
                    + "'" + rent.getInsurance() + "', "
                    + "'" + rent.getClimateControl() + "', "
                    + "'" + rent.getExtraKey() + "', "
                    + "'" + rent.getStorageId() + "', "
                    + "'" + rent.getClientId() + "', "
                    + "'" + rent.getTotal() + "')";

            Log.i("Banco de Dados", "" + sql);
            try {
                gw.getDatabase().execSQL(sql);
                Toast.makeText(context, "Aluguel registrado!", Toast.LENGTH_SHORT).show();
                return true;
            } catch (SQLException e) {
                Toast.makeText(context, "Erro! " + e.getMessage(), Toast.LENGTH_SHORT).show();
                return false;
            }
        }else{
            Toast.makeText(context, "Período não disponível! ", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public boolean update(AluguelModel rent) {
        String sql = "UPDATE orders SET "
                + "start_date='" + rent.getStartDate() + "', "
                + "end_date='" + rent.getEndDate() + "', "
                + "storage_idstorage='" + rent.getStorageId() + "', "
                + "client_idclient='" + rent.getClientId() + "', "
                + "extra_key='" + rent.getExtraKey() + "', "
                + "climate_control='" + rent.getClimateControl() + "', "
                + "insurance='" + rent.getInsurance() + "', "
                + "total='" + rent.getTotal() + "' "
                + "WHERE id=" + rent.getId();

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

    public AluguelModel get(int id) {
        String sql = "SELECT * FROM orders WHERE id=" + id;
        Cursor cursor = gw.getDatabase().rawQuery(sql, null);
        if (cursor.moveToNext()) {
            String  startDate = cursor.getString(1);
            String endDate = cursor.getString(2);
            int insurance = cursor.getInt(3);
            int climateControl = cursor.getInt(4);
            int extraKey = cursor.getInt(5);
            int idStorage = cursor.getInt(6);
            int idClient =cursor.getInt(7);
            float total = cursor.getFloat(8);
            return new AluguelModel(id,startDate,endDate,idStorage,idClient,insurance,extraKey,climateControl, total);
        }
        return null;
    }

    public boolean delete(int id) {
        return gw.getDatabase().delete("orders", "ID=?", new String[]{ id + "" }) > 0;
    }

    public boolean isAvailable(AluguelModel rent){
        String sql = "SELECT * FROM orders WHERE storage_idstorage = "+ rent.getStorageId() +
                " AND "+ rent.getStartDate() +" <= end_date AND "+ rent.getEndDate() +" >=  start_date";
        Log.i("BANCO DE DADOS", ""+sql);
        Cursor cursor = gw.getDatabase().rawQuery(sql, null);
        if (cursor.getCount() > 0) {
            Log.i("BANCO DE DADOS", "Periodos conflitam!");
            return false;
        }else{
            return true;
        }

    }

}
