package Partida;

import Cartas.Carta;
import Cartas.Mazo;
import Errores.NoExisteCartaPosicion;
import Observer.Observable;
import Vista.IVista;

import java.util.*;

public class Mesa extends Observable {
    private Mazo mazo; //mazo inicial
    private Mazo descarte; //Maneja el descarte de la partida tambien se puede decir que son las cartas que estan en la mesa
    private HashMap<Integer, IJugador> jugadores; //Guarda a los jugadores cada mesa lo guarda para un futuro si se quiere jugar varias mesas a la ves
    private ArrayList<Carta> jugadasEquipo1 = new ArrayList<>(); //Guarda las jugadas que hayan echo los jugadores
    private ArrayList<Carta> treRojosEquipo1 = new ArrayList<>();
    private ArrayList<Carta> jugadasEquipo2 = new ArrayList<>(); //Guarda las jugadas que hayan echo los jugadores
    private ArrayList<Carta> treRojosEquipo2 = new ArrayList<>();
    private Carta corte = new Carta("Corte","0",0,-1000); //Place older para poner al corte de los mazos equipo1/2
    Integer conteoE= 0;
    //Cuando un jugador hace una jugada valida se guarda en el mazo equipo1/2 y se suman los puntos a los jugadores Esta se separan por un objeto carta con valor base -999 para poder
    //para saber cuando termina la jugada y empieza una nueva

    public Mesa (){
        //CARGA EL MAZO LOS CREA (descarte y mazo)
        //INICIO DEL JUEGO
        this.mazo = new Mazo(); //Crea el mazo
        this.descarte = new Mazo(1); //Creo el mazo de descarte le pongo un integer para que lo cree con el segundo iniciador el cual lo crea vacio
        jugadores = new HashMap<>();
    }

    public ArrayList<Carta> getJugadasEquipo1() {
        return jugadasEquipo1;
    }

    public ArrayList<Carta> getJugadasEquipo2() {
        return jugadasEquipo2;
    }

    public ArrayList<Carta> getTreRojosEquipo1(){return treRojosEquipo1;}

    public ArrayList<Carta> getTreRojosEquipo2() {return treRojosEquipo2;}

    public Mazo getDescarte() {
        this.NotificarObservador(Evento.TOMAR_PILA);
        return descarte;
    }

    public Carta getDescarteVer(){
        if (descarte.mostrarTope().getValorInicial() == -1000){
            Carta c =new Carta("El tope esta vacio","",0,0);
            return  c;
        }
        return descarte.mostrarTope();
    }

    public Mazo getMazo() {
        return mazo;
    }

    public IJugador conectarJugador(String nombre){
        IJugador u = new Jugador(nombre);
        if (conteoE %2 == 0){
            u.setEquipo(1);
        }
        else {u.setEquipo(2);}
        conteoE++;
        this.jugadores.put(u.getId(), u); //aun que las funciones
        return u; //No aviso a nadie de que esto paso cosa a tener en cuenta
    }

    public IJugador[] getUsuario(){
        IJugador[] resultado = new Jugador[this.jugadores.size()];
        return this.jugadores.values().toArray(resultado);
    }

    public Carta roboCarta(){ //Roba una carta del mazo
        this.NotificarObservador(Evento.TOMAR_CARTAS_MAZO);
        return mazo.drawCart();
    }

    public Carta roboCartaUltima(){
        return mazo.drawLastCart();
    }

    public void reiniciarMazo(){
        mazo.reiniciarMazo(); //Reinicia el mazo
    }

    public boolean CortarMazo(IJugador jugador){ //devuelve verdadero si el corte es perfecto si no devuelve falso
        Random random = new Random();
        Integer corte = random.nextInt(107); //108 es el tamano del mazo
        Carta RoboInicial;
        boolean perfecto = false;
        mazo.cortarMazo(corte);
        if (corte == 54){
            perfecto = true;
            jugador.agregarRestarPuntos(-100);
            jugador.cortePerfecto(); //activa el corte perfecto
            //Sumar 100 puntos o restar
        }
        return perfecto;
    }

    public boolean comprobarCorte(IJugador jugador){
        Carta carta;
        carta = mazo.drawCart();
        if (carta.getValorInicial() == 999 || (carta.getPuntos() == 100 || carta.getValorInicial() == 2)){
            jugador.agregarCartaMano(carta);
             return false; //sale
        }
        else {mazo.agregarCarta(carta);
             return true;} // caso contrario se devuelve la carta al mazo al final del mismo
    }

    public Carta repartirCartasInicialesMesa(){
        Carta inicial;
        for (Integer i = 0; i < 6; i++){
            descarte.agregarCarta(mazo.drawCart());
        }
        inicial = mazo.drawCart();
        descarte.agregarCarta(inicial);
        return inicial;
    }

    public void abrirse(ArrayList<Carta> jugada,IJugador jugador){ //Esto lo unico que hace es crear las jugadas
        if (jugador.getEquipo() == 1){
            jugadasEquipo1.addAll(jugada);
            jugadasEquipo1.add(corte); //Agregamos el corte para saber cuando termina la jugada
        }
        else {
            jugadasEquipo2.addAll(jugada);
            jugadasEquipo2.add(corte);
        }
        this.NotificarObservador(Evento.ABRIRSE); // Alguien se abrio
    }

    public void taponear(Carta c){
        descarte.agregarCarta(c);
        descarte.agregarCarta(corte); // El corte se para saber que el mazo de descarte esta taponeado
        this.NotificarObservador(Evento.TAPONEAR_DESCARTE);
    }

    public boolean Agregar(ArrayList <Carta> jugada, IJugador jugador, Integer lugar) {
        Carta cartaAbuscar = new Carta("","",0,999);
        boolean comprobante;
        for (Carta c: jugada){
            if (c.getValorInicial() != 999 && c.getValorInicial() != 2){
                cartaAbuscar = c; //Si la carta no es un comodin la guardo para saber a cual tengo que agregar
            }
        }
        if (jugador.getEquipo() == 1){
            comprobante = AgregarNuevaCartas(cartaAbuscar, jugada,this.jugadasEquipo1, lugar);
        }
        else {
            comprobante = AgregarNuevaCartas(cartaAbuscar,jugada,this.jugadasEquipo2,lugar);
        }
        return comprobante;
    }


    private boolean AgregarNuevaCartas(Carta cartaAbuscar, ArrayList<Carta> jugada,ArrayList<Carta> equipo, Integer lugarAgregar){
        Integer contador = 0;
        Integer contadorTotal = 0;
        Integer contadorJugadas = 0;
        Integer lugar = 0;
        boolean comprobante = false;
        ArrayList<Carta> nuevaJugada = new ArrayList<>(equipo); // nueva jugada es un array nuevo que es una copia de jugadas equipo
        for (Carta c: equipo){ // Se agrega
            contador++;
            contadorTotal++;
            if (contadorJugadas == lugarAgregar){ //bien
                if (c.getValorInicial() == cartaAbuscar.getValorInicial()){
                    comprobante = true;
                }
                if ( 7 > contador + jugada.size()){ //tiene que ser menor a 7 para anadir
                    lugar = contadorTotal;
                }
                else {
                    comprobante = false;
                }
            }
            if (c.getValorInicial() == -1000){
                contador = 0; //momento de un corte reiniciamos el contador
                contadorJugadas++; //Agregamos mas valor a el contador jugadas ya que pasamos a la segunda jugada
            }
        } //una ves salido de aca revisamos donde puedo agregar el valor y si se puede agregar
        contadorTotal = 0;
        equipo.clear();
        if (comprobante){
            for (Carta c : nuevaJugada){
                equipo.add(c);
                if (contadorTotal == lugar){ //cuando contador sea igual a lugar significa que voy a agregar los valores nuevos
                    equipo.addAll(jugada); //Agregamos los valores
                }
                contadorTotal++;
            }
            this.NotificarObservador(Evento.AGREGAR_CARTA_CANASTA);
        }
        return comprobante;
    }

    public void JugarCartaDescarte(Carta carta){
        descarte.agregarCarta(carta);
        this.NotificarObservador(Evento.DESCARTAR_CARTA);
    }

    private void SumarPuntos(IJugador jugador,Integer puntos){
        ;
    }

    public void JugarTresRojos(IJugador jugador, ArrayList<Carta> tres){
        if (jugador.getEquipo() == 1){
            this.treRojosEquipo1.addAll(tres);
        }
        else {
            this.treRojosEquipo2.addAll(tres);
        }
        this.NotificarObservador(Evento.JUGAR_TRES_ROJOS);
    }
}