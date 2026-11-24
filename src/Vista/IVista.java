package Vista;

import Cartas.Carta;
import Partida.IJugador;

public interface IVista {
    void MostrarManoJugador();
    void iniciar();
    boolean menu();

    //Usadas en el inicio del juego para ver quien inicia y repartir las cartas
    Carta devolverCartaRoboInicial();
    Carta RoboInicialJoquer();
    void mostrarQueInicia();
    void vaciarMano();

    //usadas para repatir cartas y cortar el mazo
    void cortarMazo();
    void RobarCarta();
    Integer tieneTresRojos();
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
    void AvisarGanoEquipo2();
    void AvisarGanoEquipo1();
}