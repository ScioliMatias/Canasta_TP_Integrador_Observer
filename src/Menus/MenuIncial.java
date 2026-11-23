package Menus;

import Cartas.Carta;
import Controlador.Controlador;
import Partida.Equipo;
import Partida.Jugador;
import Partida.Mesa;
import Vista.IVista;
import Vista.VistaConsola;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

//El menu esta encargado de aparte de ser el menu inicial hacer que los jugadores pasen entre si y finalizar la partida
public class MenuIncial {
    private ArrayList<IVista> jugadores = new ArrayList<>();

    //El menu solo se encarga de inciar la partida
    public MenuIncial(){
        System.out.println("Seleciones una forma de jugar: " + "\n" + "1. 4 jugadores" + "\n" + "2. 6 jugadores");
        //Futura implementacion ver de resultados partida anteriores y ranking top 5 players guardados
        Scanner teclado = new Scanner(System.in);
        boolean entradaValida = false;
        Integer opcionMenu = 0;//solo lee numeros la inicializamos
        while (!entradaValida) {
            try {
                opcionMenu = teclado.nextInt();
                if (opcionMenu > 0 && opcionMenu < 3){
                entradaValida = true; }// Si la lectura es exitosa y se encuentra dentro de las opciones dadas sale del bucle
            } catch (InputMismatchException e) {
                // Mensaje de error personalizado
                System.out.println("Error: Debes introducir un número entero. Inténtalo de nuevo.");
                //Limpiar la vista
                teclado.nextLine();
            }
        }
        Mesa mesa = new Mesa();
        if (opcionMenu == 1){
            for (int i = 0; i < 4 ; i++){
                Controlador controlador = new Controlador(mesa);
                VistaConsola vista = new VistaConsola(controlador);
                vista.iniciar(); //La inicio para que no sea nulo parte importante
                jugadores.add(vista); //Guardo la vista antes de pisar los datos con el nuevo jugador
            }
            this.EmpezarPartida();
        }
        if (opcionMenu == 2){
            for (int i = 0; i < 6 ; i++){
                Controlador controlador = new Controlador(mesa);
                VistaConsola vista = new VistaConsola(controlador);
                vista.iniciar(); // lo mismo que arriba
                jugadores.add(vista); //Guardo la vista antes de pisar los datos con el nuevo jugador
            }
            this.EmpezarPartida();
        }
    }

    private void EmpezarPartida(){ //Tiene que decidir quien empieza la partida por la carta mas alta tambien arregla la lista de jugadores para que queden ordenados
        Integer iniciador = -1;
        Carta cartaInicial;
        IVista jugadorInicial = null; // la inicializamos para que no salte un error pero no es necesario
        for (IVista v1: jugadores){ //Turno de cada jugador
            cartaInicial = v1.devolverCartaRoboInicial(); //Obtengo la primera carta que robo cada jugador
            while (cartaInicial.getValorInicial() == 999){ //Saco joquer
                cartaInicial = v1.RoboInicialJoquer(); //Usada si sale un joquer
            }
            if (cartaInicial.getValorInicial() > iniciador){
                iniciador = cartaInicial.getValorInicial();
                jugadorInicial = v1; //Nuevo jugador inicial importante para mas adelante correjir quien va primero
            }
            v1.vaciarMano(); //tengo que vaciar su mano ya que la primera carta no se queda en su mano
        }
        while(jugadorInicial != jugadores.getFirst()){
            IVista vistaAx = jugadores.getFirst(); //lo copio
            this.jugadores.removeFirst();
            jugadores.add(vistaAx); // Lo vuelvo a agregar a la lista para que quede bien el primer jugador
        }
        jugadores.getFirst().mostrarQueInicia(); //Solamente avisa quien es el jugador que va a iniciar la partida tambien reincia el mazo
        repartirCartas();
    }

    private void repartirCartas(){ //Una ves aca ya tenemos la lista de jugadores quien inicia ahora toca hacer el corte del mazo y repartir las cartas
            //Jugadores tiene la lista ordenada necesitamos tomar el anteultimo de la lista para hacer que corte el mazo
        ArrayList<IVista> jugadoresReversa = new ArrayList<>(jugadores.reversed()); //Creo una nueva lista que es una copia de jugadores
        // la damos vuelta para poder hacer que el jugador que queremos corte el mazo
        jugadoresReversa.removeFirst(); //sacamos el primer valor ya que nonos importa este
        IVista corta = jugadoresReversa.getFirst(); //agarramos ahora si el jugador encargado de cortar el mazo
        corta.cortarMazo(); //El mazo ya esta cortado lo cual quiere decir que la parte 2 del mazo esta arriba por lo que la carta que muestra es la carta del corte
        //Empieza a rapatirse las cartas para esto vamos a dejar de usar la lista nueva y vamos a volver a la vieja lista
        for (int i = 0; i < 15 ; i++){
            for (IVista v1 : jugadores){
                v1.RobarCarta(); // roba cartas una por una hasta que todos tengan 15
            }
        }
        //Comprobara si los jugadores tienen 3 rojos de ser asi se les preguntara que quieren hacer con ello
        for (IVista v1 : jugadores){ //Mostramos las manos iniciales no necesario solo para pruebas
            Integer cantidad = 0;
            cantidad = v1.tieneTresRojos();
            for (Integer i = 0 ; i < cantidad; i++){
                v1.RobarCarta(); //roba cartas hasta recuperar las cartas de los tres rojos
            }
        }
        corta.repartirCartaInicialesMesa(); //Se reparten las 6 cartas iniciales
        jugadores.getFirst().jugadaInicial(); //La jugada inicial es una jugada que solo puede hacer el primer jugador por eso lo tomamos como algo necesario para el inicio de la partida
        //ahora si empieza la partida
        Turno();
    }




    private void Turno(){
        boolean termino = false;
        Integer primer = 0;
        while (!termino){
            for (IVista v1: jugadores){ //Turno de cada jugador
                if (primer == 0){ primer = 1;} //Saltear el primer turno del primer jugador ya que ya lo jugo
                else {//ahora si el sistema de turnos
                    v1.menu(); //Menu se encarga de los turnos
                }
                //lAS FUCIONES DEL JUEGO DEVERAN DEVOLVER UN BOOLEANO SIEMPRE SERA FALSO HASTA QUE DEVUELVAN EL FINAL DEL JUEGO EN ESE CASO PASA A TRUE Y SE ACABA LA PARTIDA

            }
        }
    }
}
