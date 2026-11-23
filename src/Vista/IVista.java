package Vista;

import Cartas.Carta;
import Partida.IJugador;

public interface IVista {
    void MostrarJugadores(IJugador[] jugador);
    void MostrarManoJugador();
    void iniciar();
    boolean menu();

    //Usadas en el inicio del juego para ver quien inicia y repartir las cartas
    public Carta devolverCartaRoboInicial();
    public Carta RoboInicialJoquer();
    public void mostrarQueInicia();
    public void vaciarMano();

    //usadas para repatir cartas y cortar el mazo
    public void cortarMazo();
    public void RobarCarta();
    public Integer tieneTresRojos();
    void repartirCartaInicialesMesa();

    //Jugada
    void AvisarJugada();
    void AvisarNoJugada();
    void AvisarAbrirse();
    void AvisarAgregoCartaCanasta();
    //Tapon
    void AvisarTapon();
    void AvisarNoTapon();

    //descartarCarta
    void MostrarDescarte();
    void AvisarRoboDescarte();

    //TresRojos
    void AvisarTresRojo();

    //Ganar
    public void AvisarGanoEquipo2();
    public void AvisarGanoEquipo1();
}
