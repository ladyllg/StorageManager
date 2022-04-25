package com.example.storagemanager.ui.dashboard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.example.storagemanager.AluguelModel;
import com.example.storagemanager.AluguelModelDAO;
import com.example.storagemanager.ClienteModel;
import com.example.storagemanager.ClienteModelDAO;
import com.example.storagemanager.R;
import com.example.storagemanager.StorageModel;
import com.example.storagemanager.StorageModelDAO;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RentAddNew extends AppCompatActivity {
    private Spinner spinnerStorage, spinnerCliente;
    AluguelModelDAO aluguelDao;
    private StorageModelDAO storageDAO;
    private ClienteModelDAO clienteDAO;
    private AluguelModelDAO rentDAO;
    private ArrayList<StorageModel> storages;
    private ArrayList<ClienteModel> clients;
    private int storageId, clientId, rentId;
    private TextView textInputStartDate, textInputEndDate;
    private SwitchMaterial checkSeguro, checkExtra, checkClimate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent_add_new);
        setTitle("Registrar Novo Aluguel");

        textInputStartDate = findViewById(R.id.textInputStartDate);
        textInputEndDate = findViewById(R.id.textInputEndDate);
        checkSeguro = findViewById(R.id.checkSeguro);
        checkExtra = findViewById(R.id.checkExtra);
        checkClimate = findViewById(R.id.checkClimate);

        aluguelDao = new AluguelModelDAO(this);

        storageDAO = new StorageModelDAO(this);
        storages = storageDAO.getList();

        clienteDAO = new ClienteModelDAO(this);
        clients = clienteDAO.getList("WHERE closed=0");

        spinnerStorage = (Spinner) findViewById(R.id.spinnerStorage);
        StorageAdapter adapterStorage = new StorageAdapter(this, android.R.layout.simple_spinner_dropdown_item, storages);
        spinnerStorage.setAdapter(adapterStorage);

        spinnerCliente = (Spinner) findViewById(R.id.spinnerCliente);
        ClientAdapter adapterClient = new ClientAdapter(this, android.R.layout.simple_spinner_dropdown_item, clients);
        spinnerCliente.setAdapter(adapterClient);

        Intent intent = getIntent();
        storageId = intent.getIntExtra("storageId", -1);
        clientId = intent.getIntExtra("clientId", -1);
        rentId = intent.getIntExtra("rentId", -1);

        if (storageId != -1) {
            for (int position = 0; position < adapterStorage.getCount(); position++) {
                if(((StorageModel)adapterStorage.getItem(position)).getId() == storageId) {
                    spinnerStorage.setSelection(position);
                }
            }
        }
        if (clientId != -1) {
            for (int position = 0; position < adapterClient.getCount(); position++) {
                if(((ClienteModel)adapterClient.getItem(position)).getId() == clientId) {
                    spinnerCliente.setSelection(position);
                }
            }
        }

        Log.i("Novo Aluguel", ""+ rentId + " " + storageId + " " + clientId);
        rentDAO = new AluguelModelDAO(this);
        if(rentId != -1){
            AluguelModel rent = rentDAO.get(rentId);
            textInputStartDate.setText(rent.getStartDateTempl());
            textInputEndDate.setText(rent.getEndDateTempl());
            Log.i("Checks", ""+rent.getInsurance()+ " " + rent.getExtraKey() + " " + rent.getClimateControl());
            if(rent.getInsurance() == 1){ checkSeguro.setChecked(true); }
            if(rent.getExtraKey() == 1){ checkExtra.setChecked(true); }
            if(rent.getClimateControl() == 1){ checkClimate.setChecked(true); }
        }


    }

    public void btnSalvar(View view) {
        Log.i("Criando Aluguel", "entrou na função");
        StorageModel storage = (StorageModel) spinnerStorage.getAdapter().getItem(spinnerStorage.getSelectedItemPosition());
        ClienteModel client = (ClienteModel) spinnerCliente.getAdapter().getItem(spinnerCliente.getSelectedItemPosition());
        int checkSeguroInt = checkSeguro.isChecked() ? 1 : 0;
        int checkExtraKeyInt = checkExtra.isChecked() ? 1 : 0;
        int checkClimateInt = checkClimate.isChecked() ? 1 : 0;

        Log.i("Aluguel", "startDate " + textInputStartDate.getText().toString());
        Log.i("Aluguel", "endDate " + textInputEndDate.getText().toString());

        // Validando os campos de data Inicio e data Fim
        if(TextUtils.isEmpty(textInputStartDate.getText().toString())){
            textInputStartDate.setError("Obrigatório");
            return;
        }else{
            if(!textInputStartDate.getText().toString().matches("([0-9]{2})/([0-9]{2})/([0-9]{4})")){
                textInputStartDate.setError("Formato inválido!");
                return;
            }
        }
        if(TextUtils.isEmpty(textInputEndDate.getText().toString())){
            textInputEndDate.setError("Obrigatório");
            return;
        }else{
            if(!textInputEndDate.getText().toString().matches("([0-9]{2})/([0-9]{2})/([0-9]{4})")){
                textInputStartDate.setError("Formato inválido!");
                return;
            }
        }

        String inputPattern = "dd/MM/yyyy";
        String outputPattern = "yyyy-MM-dd";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String strStartDate = null, strEndDate = null;

        try {
            date = inputFormat.parse(textInputStartDate.getText().toString());
            strStartDate = outputFormat.format(date);
            date = inputFormat.parse(textInputEndDate.getText().toString());
            strEndDate = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        AluguelModel rent = new AluguelModel(rentId,
                strStartDate,
                strEndDate,
                storage.getId(),
                client.getId(),
                checkSeguroInt,
                checkExtraKeyInt,
                checkClimateInt,
                storage.getPrice());


        Log.i("Check Selecionados", ""+checkSeguroInt+" "+checkClimateInt+" "+checkExtraKeyInt+" ALuguel " + rentId);
        boolean result;
        if (rentId == -1) result = aluguelDao.add(rent);
        else              result = aluguelDao.update(rent);
        if (result) {
            Log.i("Banco de Dados", "Storage Adicionado!!");
            finish();
        }
        return;
    }

}