package com.example.storagemanager.ui.notifications;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.storagemanager.ClienteModel;
import com.example.storagemanager.ClienteModelDAO;
import com.example.storagemanager.R;
import com.example.storagemanager.StorageModel;
import com.example.storagemanager.StorageModelDAO;

public class ClientAddNew extends AppCompatActivity {
    private TextView nomeClient;
    private ClienteModelDAO clientDAO;
    private int clientId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_add_new);
        setTitle("Adicionar Novo Cliente");

        nomeClient = findViewById(R.id.textInputNome);

        clientDAO = new ClienteModelDAO(this);
        Intent intent = getIntent();

        clientId = intent.getIntExtra("clientId", -1);
        Log.i("BANCO DE DADOS", "Buscando cliente..." + clientId);
        if (clientId != -1) {
            ClienteModel client = clientDAO.get(clientId);
            Log.i("Cliente", ""+client.getName());
            nomeClient.setText(client.getName());
        }
    }

    public void btnSalvar(View view) {
        ClienteModel client = new ClienteModel(clientId, nomeClient.getText().toString(), 0, 0);
        boolean result;
        if (clientId == -1) result = clientDAO.add(client);
        else                  result = clientDAO.update(client);
        Log.i("Banco de Dados", "Cliente Adicionado!!");
        if (result) {
            finish();
        }
    }
}