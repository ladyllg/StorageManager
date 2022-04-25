package com.example.storagemanager.ui.home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.storagemanager.R;
import com.example.storagemanager.StorageModel;
import com.example.storagemanager.StorageModelDAO;

public class StorageAddNew extends AppCompatActivity {
    private StorageModelDAO storageDAO;
    private int storageId;
    private TextView tipoCategoria, storagePrice, inputNro, inputAltura, inputProfundidade, inputLargura;
    private Spinner spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage_add_new);

        storagePrice = findViewById(R.id.textInputPrice);
        inputNro = findViewById(R.id.textInputNro);
        inputAltura = findViewById(R.id.textInputAltura);
        inputProfundidade = findViewById(R.id.textInputProfundidade);
        inputLargura = findViewById(R.id.textInputLargura);

        spinner = (Spinner) findViewById(R.id.tipoCategoria);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.tipos_storages_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        setTitle("Adicionar Novo Storage");

        storageDAO = new StorageModelDAO(this);

        Intent intent = getIntent();
        storageId = intent.getIntExtra("storageId", -1);
        Log.i("BANCO DE DADOS", "Buscando storage..." + storageId);

        // Verifica se uma senha foi passada como parâmetro
        if (storageId != -1) {
            StorageModel storage = storageDAO.get(storageId);
            storagePrice.setText(Float.toString(storage.getPrice()));
            inputNro.setText(Integer.toString(storage.getNro()));
            inputAltura.setText(Float.toString(storage.getAltura()));
            inputProfundidade.setText(Float.toString(storage.getProfundidade()));
            inputLargura.setText(Float.toString(storage.getLargura()));
            spinner.setSelection(adapter.getPosition(storage.getCategoryString()));
        }
    }

    public void btnSalvar(View view) {
        StorageModel storage = new StorageModel(storageId,
                                                Integer.parseInt(inputNro.getText().toString()),
                                                Float.parseFloat(inputProfundidade.getText().toString()),
                                                Float.parseFloat(inputLargura.getText().toString()),
                                                Float.parseFloat(inputAltura.getText().toString()),
                                                Float.parseFloat(storagePrice.getText().toString()),
                                                spinner.getSelectedItem().toString());

        boolean result;
        if (storageId == -1) result = storageDAO.add(storage);
        else                  result = storageDAO.update(storage);
        Log.i("Banco de Dados", "Storage Adicionado!!");
        if (result) {
            finish();
        }
    }


}