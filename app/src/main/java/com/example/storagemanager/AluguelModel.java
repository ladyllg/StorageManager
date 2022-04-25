package com.example.storagemanager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AluguelModel {
    private int id;
    private String startDate;
    private String endDate;
    private int storageId;
    private int clientId;
    private int insurance;
    private int extraKey;
    private int climateControl;
    private float total;

    public AluguelModel(int id, String startDate, String endDate, int storageId, int clientId, int insurance, int extraKey, int climateControl, float total) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.storageId = storageId;
        this.clientId = clientId;
        this.insurance = insurance;
        this.extraKey = extraKey;
        this.climateControl = climateControl;
        this.total = total;
    }

    public AluguelModel(int id, String endDate) {
        this.id = id;
        this.endDate = endDate;
    }

    /** Metodo para retornar o Id do aluguel no banco de dados
     * @return int - ID do aluguel */
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /** Metodo para retornar a data quando o aluguel se inicia
     * @return String - data de inicio do aluguel */
    public String getStartDate() {
        return this.startDate;
    }
    public String getStartDateTempl() {
        String outputPattern = "dd/MM/yyyy";
        String inputPattern = "yyyy-MM-dd";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);
        Date date = null;
        String startDateTempl = null;
        try {
            date = inputFormat.parse(this.getStartDate());
            startDateTempl = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return startDateTempl;
    }

    public String getEndDateTempl() {
        String outputPattern = "dd/MM/yyyy";
        String inputPattern = "yyyy-MM-dd";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);
        Date date = null;
        String endDateTempl = null;
        try {
            date = inputFormat.parse(this.getEndDate());
            endDateTempl = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return endDateTempl;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    /** Metodo para retornar a data quando o aluguel termina
     * @return String - data de t�rmino do aluguel */
    public String getEndDate() {
        return this.endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    /** Metodo para retornar o ID do storage alugado
     * @return int - ID do storage alugado */
    public int getStorageId() {
        return storageId;
    }

    public void setStorageId(int storageId) {
        this.storageId = storageId;
    }

    /** Metodo para retornar o ID do cliente que realizou o aluguel
     * @return int - ID do cliente */
    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    /** Metodo para retornar a solicita��o pelo Seguro
     * @return boolean - Seguro */
    public int getInsurance() {
        return this.insurance;
    }

    public void setInsurance(int insurance) {
        this.insurance = insurance;
    }

    /** Metodo para retornar a solicita��o de chave extra
     * @return boolean - chave extra */
    public int getExtraKey() {
        return this.extraKey;
    }

    public void setExtraKey(int extraKey) {
        this.extraKey = extraKey;
    }

    /** Metodo para retornar a solicita��o de controle clim�tico do local
     * @return boolean - controle clim�tico */
    public int getClimateControl() {
        return this.climateControl;
    }

    public void setClimateControl(int climateControl) {
        this.climateControl = climateControl;
    }

    public float getTotal() {
        return this.total;
    }
}
