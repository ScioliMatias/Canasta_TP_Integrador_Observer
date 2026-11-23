package Partida;

import Cartas.Carta;

import java.util.ArrayList;
import java.util.List;

public interface IJugador {

    public int getId();
    String getNombre();
    public ArrayList<Carta> mostrar_mano();
    public Integer jugarTresRojo();
    public ArrayList<Carta> getTresRojos();
    public Integer getPuntos();
    void agregarDescarteMano(ArrayList<Carta> descarte);
    public void agregarCartaMano(Carta carta);
    public void VaciarMano();
    //Abrise
    public boolean comprobarJugada(ArrayList<Integer> lugares);
    public ArrayList<Carta> abrirse(ArrayList<Integer> lugares);
    public Integer getEquipo();
    //Canasta
    public boolean comprobarCanasta(ArrayList<Integer> lugares);
    //Anadir carta canasta
    public boolean comprobarAnadir(ArrayList<Integer> lugares);
    public void setEquipo(Integer equipo);


    public Carta JugarCartaDescarte(Integer lugarCarta, Integer tamanoMano);
    public Carta mostrarCartaRoboInicial();
    public Carta verCarta(Integer lugar);
    public Integer tamanoMano();
    public void cortePerfecto();
    public void agregarRestarPuntos(Integer cantidad);
    public void RealizoCanasta(Integer Puntos);
}
