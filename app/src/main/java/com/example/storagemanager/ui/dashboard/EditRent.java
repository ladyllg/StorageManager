package com.example.storagemanager.ui.dashboard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.storagemanager.AluguelModel;
import com.example.storagemanager.AluguelModelDAO;
import com.example.storagemanager.ClienteModel;
import com.example.storagemanager.ClienteModelDAO;
import com.example.storagemanager.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EditRent extends AppCompatActivity {
    private AluguelModelDAO aluguelDAO;
    private int rentId;
    private TextView textInputEndDate;
    String outputPattern = "dd/MM/yyyy";
    String inputPattern = "yyyy-MM-dd";

    SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
    SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

    Date date = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_rent);
        setTitle("Renovar Aluguel");

        aluguelDAO = new AluguelModelDAO(this);

        textInputEndDate = findViewById(R.id.textInputEndDate);
        Intent intent = getIntent();
        rentId = intent.getIntExtra("rentId", -1);

        if (rentId != -1) {
            AluguelModel rent = aluguelDAO.get(rentId);
            Log.i("Aluguel", "" + rent.getId() + " startDate " + rent.getStartDate() +" EndDate " + rent.getEndDate());
            String strEndDate = null;
            try {
                date = inputFormat.parse(rent.getEndDate());

                strEndDate = outputFormat.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            textInputEndDate.setText(strEndDate);
        }
    }

    public void btnSalvar(View view) {
        String inputEndDate = textInputEndDate.getText().toString();
        String outputEndDate = null;
        try {
            date = outputFormat.parse(inputEndDate);
            outputEndDate = inputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Log.i("Edit Rent", "Nova Data "+ outputEndDate);
        AluguelModel rent = new AluguelModel(rentId, outputEndDate);

        boolean result;
        result = aluguelDAO.update(rent);
        Log.i("Banco de Dados", "Aluguel Adicionado!!");
        if (result) {
            finish();
        }
    }
}