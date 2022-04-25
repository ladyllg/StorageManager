package com.example.storagemanager.ui.notifications;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.storagemanager.AluguelModel;
import com.example.storagemanager.ClienteModel;
import com.example.storagemanager.ClienteModelDAO;
import com.example.storagemanager.DbGateway;
import com.example.storagemanager.R;
import com.example.storagemanager.StorageModel;
import com.example.storagemanager.ui.dashboard.ClientAdapter;

import java.util.ArrayList;

public class ClientCloseAccount extends AppCompatActivity {
    private TextView nomeClient, total;
    private int clientId;
    private ClienteModelDAO clientDAO;
    private RecyclerView recyclerView;
    private ArrayList<AluguelModel> rents;
    private MyRecyclerViewAdapter adapter;
    private DbGateway gw;
    private float sumTotal = 0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_close_account);
        setTitle("Fechar Conta");

        gw = DbGateway.getInstance(this);

        nomeClient = findViewById(R.id.textInputNome);
        total = findViewById(R.id.textInputPrice);

        clientDAO = new ClienteModelDAO(this);
        Intent intent = getIntent();

        clientId = intent.getIntExtra("clientId", -1);

        recyclerView = findViewById(R.id.recyclerRentView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        if (clientId != -1) {
            ClienteModel client = clientDAO.get(clientId);
            Log.i("Cliente", ""+client.getName());
            nomeClient.setText(client.getName());

            rents = clientDAO.getRents(client);
            for(AluguelModel rent: rents){
                sumTotal += rent.getTotal();
            }

            total.setText("R$ "+ sumTotal);

            adapter = new MyRecyclerViewAdapter(this, rents);
            recyclerView.setAdapter(adapter);
            recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        }

    }

    public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

        private ArrayList<AluguelModel> rents;
        private LayoutInflater mInflater;

        MyRecyclerViewAdapter(Context context, ArrayList<AluguelModel> data) {
            this.mInflater = LayoutInflater.from(context);
            this.rents = data;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.close_client_rents_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            // Buscando pelo storage no banco de dados
            Cursor cursor = gw.getDatabase().rawQuery("SELECT categoria FROM storages WHERE ID = " + rents.get(position).getStorageId(), null);
            if(cursor != null && cursor.moveToFirst()){
                String tipoStorage = cursor.getString(0);
                holder.tipoStorage.setText(tipoStorage);
            }
            holder.priceRent.setText("R$ "+rents.get(position).getTotal());
            holder.rentPeriodo.setText("De " + rents.get(position).getStartDateTempl() + " até " + rents.get(position).getEndDateTempl());
        }

        @Override
        public int getItemCount() {
            return this.rents.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder{
            TextView tipoStorage, rentPeriodo, priceRent;

            ViewHolder(View itemView) {
                super(itemView);
                tipoStorage = itemView.findViewById(R.id.tipoCategoria);
                rentPeriodo = itemView.findViewById(R.id.rentPeriodo);
                priceRent = itemView.findViewById(R.id.priceRent);
            }

        }

    }

    public void btnSalvar(View view) {
        boolean result;
        ClienteModel client = clientDAO.get(clientId);
        client.setTotal(sumTotal);
        result = clientDAO.closeClient(client);

        if (result) {
            finish();
        }
    }

}