package Partida;

import Cartas.Carta;

import java.util.ArrayList;
import java.util.List;

public interface IJugador {

    int getId();
    String getNombre();
    ArrayList<Carta> mostrar_mano();
    Integer getPuntos();
    void agregarDescarteMano(ArrayList<Carta> descarte);
    void agregarCartaMano(Carta carta);
    void VaciarMano();
    //Abrise
    boolean comprobarJugada(ArrayList<Integer> lugares);
    ArrayList<Carta> abrirse(ArrayList<Integer> lugares);
    Integer getEquipo();
    //Canasta
    boolean comprobarCanasta(ArrayList<Integer> lugares);
    //Anadir carta canasta
    boolean comprobarAnadir(ArrayList<Integer> lugares);
    void setEquipo(Integer equipo);
    void devolverCartasJugador(ArrayList<Carta> cartas);
    //TRES ROJOS
    ArrayList<Carta> jugarTresRojo();

    public Carta JugarCartaDescarte(Integer lugarCarta, Integer tamanoMano);
    public Carta mostrarCartaRoboInicial();
    public Carta verCarta(Integer lugar);
    public Integer tamanoMano();
    public void cortePerfecto();
    public void agregarRestarPuntos(Integer cantidad);
}
