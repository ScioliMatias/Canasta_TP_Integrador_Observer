package Cartas;

public class Carta {
    private String valor;
    private String palo;
    private Integer puntos;
    private Integer valorInicial; //Esto es usado para el valor que tiene la carta para el inicio de la partida

    public Carta(String valor,String palo, Integer puntos, Integer valorInicial){
        this.valor = valor;
        this.palo = palo;
        this.puntos = puntos;
        this.valorInicial = valorInicial;
    }

    public String getValor() {
        return valor;
    }
    public String getPalo() {
        return palo;
    }
    public Integer getPuntos() {
        return puntos;
    }
    public Integer getValorInicial(){return valorInicial;};
    
}
