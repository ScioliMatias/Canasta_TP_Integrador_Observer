package Controlador;

import Cartas.Carta;
import Observer.IObservable;
import Observer.IObservador;
import Partida.Evento;
import Partida.IJugador;
import Partida.Mesa;
import Vista.IVista;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Controlador implements IObservador {

    private IVista vista;
    private final Mesa partida;
    private IJugador jugador;

    public Controlador(Mesa partida){
        this.partida = partida;
        this.partida.AgregarObserver(this);
    }

    public void setVista(IVista vista) {
        this.vista = vista;
    }

    @Override
    public void actualizar(IObservable observable, Object e) {
        Evento evento = (Evento) e;
        switch (evento){
            case DESCARTAR_CARTA :
                this.vista.MostrarDescarte(); //se muestra el nuevo tope de descarte
                break;
            case AGREGAR_CARTA_CANASTA:
                this.vista.AvisarJugada();
                break;
            case JUGAR_TRES_ROJOS:
                this.vista.AvisarTresRojo();
                break;
            //Aca se escribirian los eventos y los cambios que deveria haber con los eventos creados
        }
    }
    public Carta VerTopePilaDescarte(){
        return this.partida.getDescarteVer();
    }

    public IJugador getJugador(){return jugador;}

    public IJugador conectarUsuario(String nombre){
        this.jugador =this.partida.conectarJugador(nombre);
        return jugador;
    }

    public void solicitarListaUsuario(){
        if (this.vista != null){
            this.vista.MostrarJugadores(this.partida.getUsuario());
        }
    }

    public Mesa GetMesa(){
        return this.partida;
    }

    public boolean robarPote(){ //Devuelve false si hace la apcion
        ArrayList<Carta> mano;
        mano = this.getJugador().mostrar_mano();
        boolean comprobador = true;
        Carta tope = this.GetMesa().getDescarte().mostrarTope();
        int repeticion = 0;
        for (Carta c : mano){
            if (tope.getValor() == c.getValor()){
                repeticion++; //sumo 1 a repeticion tiene que ser 2 para poder usar esta apcion
            }
        }
        if (repeticion >= 2){
            this.jugador.agregarDescarteMano(this.GetMesa().getDescarte().robarPila());
            comprobador = false;
        }
        return comprobador;
    }

    public void robarCartaMazo(){ //roba una carta del mazo
        jugador.agregarCartaMano(partida.getMazo().drawCart());
    }

    public void descartarUnaCarta(Integer lugar){
        partida.JugarCartaDescarte(jugador.JugarCartaDescarte(lugar, jugador.tamanoMano()));
    }

    public ArrayList<Carta> PasarJugadas(Integer equipo){
        if (equipo == 1){
            return partida.getJugadasEquipo1();
        }
        else {return partida.getJugadasEquipo2();}
    }

    public ArrayList<Carta> PasarTresRojos(Integer equipo){
        if (equipo == 1){
            return partida.getTreRojosEquipo1();
        }
        else return partida.getTreRojosEquipo2();
    }

    public boolean Abrirse(ArrayList<Integer> lugares){//Hacer una jugada de minimo 3 cartas
        ArrayList<Carta> jugadas;
        if (lugares.isEmpty()){
            return false;
        }
        boolean comprobar = jugador.comprobarJugada(lugares);
        if (comprobar) {
            jugadas = jugador.abrirse(lugares);
            this.partida.abrirse(jugadas, jugador); //Manda a la partida para que se haga la jugada
            //Verdadero hace la adcion falso se sale
            vista.AvisarJugada();
        }
        else {vista.AvisarNoJugada();} //Avisa que la jugada no se realizo
        return comprobar;
    }

    public boolean anadirCanasta(Integer lugarJugada,ArrayList<Integer> lugares){//Agrega cartas a una canasta
        ArrayList<Carta> jugadas;
        boolean comprobante = jugador.comprobarAnadir(lugares);
        if (lugarJugada == -1){
            return  false;
        }
        if (comprobante) { //Verdadero hace la adcion falso se sale
            jugadas = jugador.abrirse(lugares);//Abrirse solamente nos devuelve el conjunto de cartas asi que tambien se puede jugar aca
            comprobante = this.partida.Agregar(jugadas, jugador, lugarJugada); //Manda a la partida para que se haga la jugada
            if (comprobante){
                vista.AvisarJugada();
            }
            else {vista.AvisarNoJugada();
            jugador.devolverCartasJugador(jugadas);} //devolvermos las cartas a la mano del jugador para q no se pierdan
        }
        else {vista.AvisarNoJugada();} //Avisa que la jugada no se realizo
        return comprobante;
    }

    public boolean Canasta(ArrayList<Integer> lugares){//Hacer una jugada de minimo 3 cartas
        ArrayList<Carta> jugadas;
        if (jugador.comprobarJugada(lugares)) {
            jugadas = jugador.abrirse(lugares);
            this.partida.abrirse(jugadas, jugador); //Manda a la partida para que se haga la jugada
            //Verdadero hace la adcion falso se sale
            vista.AvisarJugada();
        }
        else {vista.AvisarNoJugada();} //Avisa que la jugada no se realizo
        return jugador.comprobarJugada(lugares);
    }

    public boolean taponear(Integer i){
        Carta c = this.jugador.JugarCartaDescarte(i,jugador.tamanoMano());
        if (comprobarTapon(i)){
            this.partida.taponear(c);
            return true;
        }
        else return false;
    }

    public boolean comprobarTapon(Integer lugar){
        Carta c = this.jugador.verCarta(lugar);
        if (c.getValorInicial() == -100 || c.getValorInicial() == 999 || c.getValorInicial() == 2){
            this.vista.AvisarTapon();
            return true;
        }
        else {
            this.vista.AvisarNoTapon();
            return false;
        }
    }

    public Integer CantidadDeJugadas(){
        Integer equipo = this.jugador.getEquipo();
        ArrayList<Carta> jugadas = new ArrayList<>();
        Integer cantidad = 0;
        if (equipo == 1){
            jugadas = partida.getJugadasEquipo1();
            for (Carta c: jugadas){
                if (c.getValorInicial() == -1000){
                    cantidad++;
                }
            }
        }
        else {
            jugadas = partida.getJugadasEquipo2();
            for (Carta c: jugadas){
                if (c.getValorInicial() == -1000){ //cada ves que se realiza una jugada se pone este place older para saber que termina entonces sabemos que hay una jugada en esa posicion gracias a esto
                    cantidad++;
                }
            }
        }
        return cantidad;
    }

    public Integer JugarTresRojos(){
        Integer cantidad;
        ArrayList<Carta> Tres;
        Tres = this.jugador.jugarTresRojo();
        this.partida.JugarTresRojos(this.jugador, Tres);
        cantidad = Tres.size();
        return cantidad;
    }
}
