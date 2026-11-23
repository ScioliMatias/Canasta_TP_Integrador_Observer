package Partida;

import java.util.ArrayList;

public class Equipo implements IEquipo{
    private ArrayList<IJugador> equipo;
    private Integer puntos;
    private String nombre;

    public Equipo(String nombre){
        this.nombre = nombre;
        this.puntos = 0;
        this.equipo = new ArrayList<>();
    }
    @Override
    public void agregarJugador(IJugador jugador) {
        equipo.add(jugador);
    }

    @Override
    public void sumarPuntos(Integer puntos) {
        this.puntos += puntos;
    }

    @Override
    public Integer getPuntos() {
        return this.puntos;
    }

    @Override
    public ArrayList<IJugador> getEquipo() {
        return this.equipo;
    }
}
