package com.example.storagemanager;

public class StorageModel {

    private int id;
    private int nro;
    private float profundidade;
    private float largura;
    private float altura;
    private float price;
    private String categoryString;

    public StorageModel(int id, int nro, float profundidade, float largura, float altura, float price, String categoryString){
        this.id = id;
        this.nro = nro;
        this.profundidade = profundidade;
        this.largura = largura;
        this.altura = altura;
        this.price = price;
        this.categoryString = categoryString;
    }

    public StorageModel(){
    }

    /** Metodo para retornar o Id do local no banco de dados
     * @return int - ID do local */
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /** Metodo para retornar qual o tipo do storage
     * @return String - ID do local */
    public String getCategoryString() {
        return categoryString;
    }

    public String getString() {
        return "Nro #" + this.getNro() + " - " + categoryString;
    }

    public void setCategoryString(String categoryString) {
        this.categoryString = categoryString;
    }

    /** Metodo para retornar a profundidade do local
     * @return float - profundidade do local */
    public float getProfundidade() {
        return profundidade;
    }

    public void setProfundidade(float profundidade) {
        this.profundidade = profundidade;
    }

    /** Metodo para retornar a largura do storage
     * @return float - largura  do local */
    public float getLargura() {
        return largura;
    }

    public void setLargura(float largura) {
        this.largura = largura;
    }

    /** Metodo para retornar a altura do storage
     * @return float - altura do local */
    public float getAltura() {
        return altura;
    }

    public void setAltura(float altura) {
        this.altura = altura;
    }

    /** Metodo para retornar o endereço(número) do storage
     * @return int - numero do local */
    public int getNro() {
        return this.nro;
    }

    public void setNro(int nro) {
        this.nro = nro;
    }

    /** Metodo para retornar qual o tipo do storage
     * @return String - ID do local */
    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    /** Metodo para retornar a descrição completa do local
     * @return String - categoria + endere�o(n�mero) do local*/
    public String getTemplString() {
        String result = this.getCategoryString() + " | No " + this.getNro();
        return result;
    }

}
