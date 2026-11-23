package Partida;

import java.util.ArrayList;

public interface IEquipo {
    public void agregarJugador(IJugador jugador);
    public void sumarPuntos(Integer puntos);
    public Integer getPuntos();
    public ArrayList<IJugador> getEquipo();
}
