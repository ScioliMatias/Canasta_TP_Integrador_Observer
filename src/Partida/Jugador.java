package Partida;

import Cartas.Carta;
import Errores.NoExisteCartaPosicion;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Jugador implements IJugador{
    //Partida.Jugador es una entidad que muestra un jugador de la meza el mismo tendra una mano de cartas(la clase mano no es creada ya que seria igual que jugador) y
    private String nombre; //jugador 1 2 o 3 4
    private Integer puntos = 0;
    private Integer Equipo;
    private ArrayList<Carta> mano; //esta tiene que inciar vacia
    private boolean cortePerfecto = false;
    private static  int ID= 0;
    private int id;
    private ArrayList<Carta> Tres = new ArrayList<>(); //se usa para poner los 3 rojos

    public Integer getPuntos() {
        return puntos;
    }

    public Jugador(String nombre){
        this.nombre = nombre;
        puntos = 0;
        mano = new ArrayList<Carta>();
        this.id = Jugador.ID++; //Le pongo id y luego le aumento el id a todos los siguientes usuarios
    }

    public Integer jugarTresRojo(){
        int tiene = 0;
        for (Carta c : mano){ //recorre la mano en busqueda de los 3 rojos
            if (c.getPuntos() == 100){
                this.Tres.add(c);
                tiene++;
            }
        }
        return tiene;
    }

    public ArrayList<Carta> getTresRojos(){
        return this.Tres;
    }

    public int getId() {return id;}

    public void setEquipo(Integer equipo) {
        Equipo = equipo;
    }

    public Integer getEquipo(){return Equipo;}

    public String getNombre() {
        return nombre;
    }

    public void agregarCartaMano(Carta carta){
        this.mano.add(carta); //Agrega una carta a la mano
    }

    public void agregarDescarteMano(ArrayList<Carta> descarte){
        this.mano.addAll(descarte); //agrega a la mano todas las cartas que se le pasaron
    }

    public ArrayList<Carta> mostrar_mano(){ //Esto devuelve un cojunto de todas las cartas en la mano del jugador
        ArrayList<Carta> manoActual = new ArrayList<>();
        for (Carta c: mano){
            manoActual.add(c);
        }
        return manoActual;
    }

    public void VaciarMano(){
        mano.clear();
    }

    public Carta JugarCartaDescarte(Integer lugarCarta, Integer tamanoMano){ //al jugar la carta usamos la posicion que se encuentre en la mano
        if (lugarCarta < 0 || tamanoMano-1 < lugarCarta){
            throw new NoExisteCartaPosicion("No se ecuentra ninguna carta en esa posicion");
        }
        Carta cartaJugada = mano.get(lugarCarta); //recupero la carta que va a ser jugada

        mano.remove(cartaJugada);
        return cartaJugada;
    }

    public Carta mostrarCartaRoboInicial(){
        return mano.getFirst();
    }

    public Carta verCarta(Integer lugar){
        return mano.get(lugar);
    }

    public Integer tamanoMano(){ //Usado para ver la mano del jugador para no pasarse de la mano y no generar error
        Integer tamano = 0;
        for (Carta c : mano){
            tamano++;
        }
        return tamano;
    }

    public void cortePerfecto(){
        cortePerfecto = true;
    }
    public void agregarRestarPuntos(Integer cantidad){
        this.puntos = this.puntos + cantidad;
    }
    public void RealizoCanasta(Integer Puntos){
        if (cortePerfecto){
            agregarRestarPuntos(200);
            cortePerfecto = false;
        }
        this.puntos = this.puntos + Puntos;
    }

    public boolean comprobarAnadir(ArrayList<Integer> lugares){
        boolean comprobante = true; // devuelve verdadero si se cumple que todas las cartas sean del mismo valor / numero
        Integer primero = lugares.getFirst();
        Carta primera = mano.get(primero);
        Carta siguientes;
        boolean bucle = true;
        Integer a = 1;
        while (bucle && (primera.getValorInicial() == 999 || primera.getValorInicial() == 2)){ //en caso de solo ser comodines devuelvo eso
            if (lugares.size() > a){
                primero = lugares.get(a++);
                primera = mano.get(primero); //cambio el primero si es un comodin
            }
            else {bucle = false;}
        }
        for (Integer i : lugares){
            siguientes = mano.get(i);
            if (siguientes.getValor() != primera.getValor()){
                comprobante = false;
            }
            if (siguientes.getValorInicial() == 999 || siguientes.getValorInicial() == 2){ //reviso que la carta no es un comodin en caso de serlo reseteo el comprobante a true
                comprobante = true; //caso de ser un comodin vuelvo a poner el comprobante en verdadero
            }
        }
        return comprobante;
    }

    public boolean comprobarJugada(ArrayList<Integer> lugares){
        boolean comprobante = true; // devuelve verdadero si se cumple que todas las cartas sean del mismo valor / numero
        Integer primero = lugares.getFirst();
        Carta primera = mano.get(primero);
        boolean bucle = true;
        Integer a = 1;
        while (bucle && (primera.getValorInicial() == 999 || primera.getValorInicial() == 2)){
            if (lugares.size() > a){
                primero = lugares.get(a++);
                primera = mano.get(primero); //cambio el primero si es un comodin

            }
            else {bucle = false;}
        }
        Carta siguientes;
        Integer repeticion = 0;
        for (Integer i : lugares){
            siguientes = mano.get(i);
            if (siguientes.getValor() != primera.getValor()){
                comprobante = false;
            }
            if (siguientes.getValorInicial() == 999 || siguientes.getValorInicial() == 2){ //reviso que la carta no es un comodin en caso de serlo reseteo el comprobante a true
                comprobante = true; //caso de ser un comodin vuelvo a poner el comprobante en verdadero
                repeticion++; //Repeticion es un valor que voy a usar al final para comprobar que hay mas cartas que comodines en la jugada
            }
        }
        if( (lugares.size() / 2) < repeticion){
            comprobante = false; //si la cantidad de cartas que son comodines es mayor o igual que la mitad de las cartas hay mas cartas que comodines o igual jugada no valida
        }
        if (lugares.size() < 3 ){ //sale por ser menos de 3 cartas
            comprobante = false;
        }
        return comprobante;
    }

    public boolean comprobarCanasta(ArrayList<Integer> lugares){
        boolean comprobante = comprobarJugada(lugares); //Como lo que tengo que comprobar es lo mismo abrirse la re uso y solo le agrego revisar que el tamano sea 7
        if (lugares.size() < 7){
            comprobante = false;
        }
        return comprobante;
    }

    public ArrayList<Carta> abrirse(ArrayList<Integer> lugares){
        ArrayList<Carta> remover = new ArrayList<>();
        ArrayList<Carta> jugada = new ArrayList<>();
        for (Integer i: lugares){
            jugada.add(mano.get(i));
            remover.add(mano.get(i));
        }
        for (Carta c: remover){
            this.mano.remove(c);
        }
        return jugada; //Como las jugadas no se guardan en el jugador las devuelvo al controlador
    }
}
