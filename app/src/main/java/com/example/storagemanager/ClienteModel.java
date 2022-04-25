package com.example.storagemanager;

public class ClienteModel {
    private int id, closed;
    private String name;
    private int rents;
    private float total;

    public ClienteModel(int id, String name, int closed, float total) {
        this.name = name;
        this.id = id;
        this.closed = closed;
        this.total = total;
    }

    /**
     * Informa o Id do cliente
     * @return int id do cliente
     */
    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     * Informa o nome do cliente
     * @return String nome do cliente
     */
    public String getName() {
        return this.name;
    }

    public String getString() {
        return "ID #" + this.getId() + " - " + this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retorna a quantidade de alugueis efetuados por aquele cliente
     * @return int qntd. de alugueis do cliente
     */
    public int getRents() {
        return rents;
    }

    public void setRents(int rents) {
        this.rents = rents;
    }

    public int isClosed() {
        return this.closed;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public float getTotal() {
        return this.total;
    }
}
