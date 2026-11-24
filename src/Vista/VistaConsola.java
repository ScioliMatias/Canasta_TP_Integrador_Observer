package Vista;

import Cartas.Carta;
import Controlador.Controlador;

import java.util.ArrayList;
import java.util.Scanner;
import Partida.IJugador;

public class VistaConsola implements IVista {
    private final Controlador controlador;
    private final Scanner scanner;
    private boolean finalizo = false;

    public VistaConsola(Controlador controlador) {
        this.controlador = controlador;
        this.scanner = new Scanner(System.in);
        this.controlador.setVista(this);
    }

    public boolean menu() {
        boolean termino;
        termino = this.procesarOpcion();
        return termino;
    }

    private boolean procesarOpcion() {
        boolean bucle = true;
        String opcion;
        while (bucle){
            this.imprimirMenu();
            opcion = this.scanner.nextLine().trim();
            switch (opcion){
                case ("1"): //Roba el descarte
                    bucle = false;
                    boolean Hecho = false;
                    Hecho = this.controlador.robarPote();
                    while (!Hecho){
                        this.MostrarManoJugador();
                        Hecho = this.controlador.Abrirse(PedirJugada());
                    }
                    break;
                case ("2"): //tomar carta del mazo (agarrar 2 y tirar 1)
                    bucle = false;
                    this.controlador.robarCartaMazo();
                    this.controlador.robarCartaMazo();
                    this.tieneTresRojos(); //Si roba algun 3 rojo esta obligado a jugarlo
                    this.MostrarManoJugador();
                    this.validarOpcionDescarte();
                    break;
                case ("3"): //Hace una jugada entre 3 a 6 cartas no consideramos 7 ya que eso seria hacer una canasta
                    boolean jugada;
                    jugada = this.controlador.Abrirse(PedirJugada()); // por ahora asi debe crearse un menu que pida que cartas quieren ser parte de las jugadas
                    if (jugada){ //la jugada si se realizo si no seguiria en este bucle
                        bucle = false;
                        this.controlador.RevisarManoJugador();
                    }
                    scanner.nextLine();
                    break;
                case ("4")://Taponea el mazo
                    Integer lugar = pedirTapon();
                    if (this.controlador.taponear(lugar)){
                        bucle = false;
                        break;
                    }
                    break;
                case ("5"):
                    boolean agregar = this.controlador.anadirCanasta(PedirGrupoAnadir(),PedirCartasAnadir()); //
                    if (agregar){
                        bucle = false;
                        this.controlador.RevisarManoJugador();
                        break;
                    }//agregar cartas a una canasta (agregas cartas a una canasta para sumar mas puntos)
                    scanner.nextLine();
                    break;
                case ("6"): // jugar una canasta de una vez
                    boolean canasta;
                    canasta = this.controlador.Canasta(PedirJugada());
                    if (canasta){
                        bucle = false;
                        this.controlador.RevisarManoJugador();
                        break;
                    }
                    scanner.nextLine();
                    break;
                case ("7")://Esto no quita el bucle
                        System.out.println("Las jugadas en la mesa son: ");
                        this.MostrarJugada();
                    break;
                case ("8"):
                        System.out.println("Tu mano es: ");
                        this.MostrarManoJugador();
                    break;
                case ("9")://Esto no quita el bucle
                    System.out.println("Los tres rojos jugados son: ");
                    this.MostrarTresRojos();
                    break;
                default:
                    this.mostrarError("Por favor ingrese una opcion valida");
                    break;
            }
        }
        return this.finalizo;// Devuelve verdadero cuando la partida acabe
    }

    private Integer pedirTapon(){
        this.MostrarManoJugador();
        while (true) {
            System.out.print("Ingresa la carta que quiera usar de tapon: ");
            if (scanner.hasNextInt()) {
                Integer lugar = scanner.nextInt();
                if (lugar < controlador.getJugador().tamanoMano()){
                    return lugar;
                }
            }
            System.out.println("Entrada no válida. Escribe un número entero.");
            scanner.next(); // descarta el token inválido
        }
    }

    private ArrayList<Integer> PedirJugada(){
        ArrayList<Integer> lista = new ArrayList<>();
        this.MostrarManoJugador();
        while (true) {
            System.out.println("Elija que cartas quieres jugar (introdusca 999 para terminar): ");
            if (!scanner.hasNextInt()) {
                System.out.println("Entrada no válida. Escribe un número entero.");
                scanner.next(); // descarta el token inválido para no ciclar
                continue;
            }
            int numero = scanner.nextInt();
            if (numero == 999) {
                break;
            }
            lista.add(numero);
        }
        return lista;
    }

    private Integer PedirGrupoAnadir(){ //Se usa para pedir en que lugar quiere anadir la jugada
        this.MostrarJugada();
        while (true) {
            System.out.print("Ingresa la jugada que le quiere añadir la/s carta/s(ponga 999 para salir): ");
            if (scanner.hasNextInt()) {
                Integer lugar = scanner.nextInt();
                if (controlador.CantidadDeJugadas() == 0){
                    return -1; //si no hay jugadas sale
                }
                if (lugar < controlador.CantidadDeJugadas() + 1){ //se fija que el lugar sea igual que la cantida de jugadas
                    return lugar - 1;
                }
                if (lugar == 999){
                    return -1;
                }
                else {
                    System.out.printf("Entrada no valida introdusca un lugar valido");
                }
            }
            else {
                System.out.println("Entrada no válida. Escribe un número entero.");
                scanner.next(); // descarta el token inválido
            }
        }

    }

    private ArrayList<Integer> PedirCartasAnadir(){
        ArrayList<Integer> lista = new ArrayList<>();
        this.MostrarManoJugador();
        while (true) {
            System.out.println("Elija que cartas quieres añadir a una canasta (introdusca 999 para terminar): ");
            if (!scanner.hasNextInt()) {
                System.out.println("Entrada no válida. Escribe un número entero.");
                scanner.next(); // descarta el token inválido para no ciclar
                continue;
            }
            int numero = scanner.nextInt();
            if (numero == 999) {
                break;
            }
            lista.add(numero);
        }
        return lista;
    }

    public void imprimirMenu(){
        System.out.println("\n [1.Robar pote | 2.Tomar carta del mazo | 3.Abrirse | 4.Taponear pote | " +
                "5. Agregar cartas a una canasta | 6.Jugar canasta oculta | 7.Ver jugadas | 8. Ver tu mano | 9. mostrar 3 rojos en mesa.]");
        System.out.println(this.controlador.getJugador().getNombre() + " Ingrese una opcion: ");
    }

    @Override
    public void MostrarManoJugador() {
        Integer contador= 0;
        ArrayList<Carta> manoDelJugador = new ArrayList<>();
        manoDelJugador = controlador.getJugador().mostrar_mano();
        System.out.println("La mano del jugador " + controlador.getJugador().getNombre() + " es:");
        for (Carta c : manoDelJugador) {
            System.out.println( contador.toString()+"." + this.mostrarCarta(c));
            contador++;
        }
    }

    private String mostrarCarta(Carta c) {
        return c.getValor() + " " + c.getPalo() ;
    }

    private void MostrarTresRojos(){
        System.out.println("Los 3 rojos del equipo 1 son");
        MostrarTresRojosBucle(1);
        System.out.println("Los 3 rojos del equipo 2 son");
        MostrarTresRojosBucle(2);
    }

    private void MostrarTresRojosBucle(Integer equipo){
        ArrayList<Carta> jugadas;
        jugadas = controlador.PasarTresRojos(equipo);
        if (jugadas.isEmpty()){
            System.out.println("No hay Tres Rojos");
        }
        else { //Solo hay 1 cojunto de tres rojos
            for (Carta c : jugadas) {
                System.out.print("- ");
                System.out.printf(this.mostrarCarta(c));
            }
            System.out.println("");
        }
    }

    private void MostrarJugada() { //Muesta las jugadas que hay en mesa
        System.out.println("Las jugadas del equipo 1 son: ");
        MostrarJugadaBucle(1);
        System.out.println("Las jugadas del equipo 2 son: ");
        MostrarJugadaBucle(2);
    }

    private void MostrarJugadaBucle(Integer equipo){
        ArrayList<Carta> jugadas;
        jugadas = controlador.PasarJugadas(equipo);
        if (jugadas.isEmpty()){
            System.out.println("No hay jugadas");
        }
        else {
            System.out.println("Primera jugada: ");
            for (Carta c : jugadas) {
                System.out.print("- ");
                if (c.getValorInicial() == -1000) {
                    System.out.println("Siguiente jugada: ");
                } else {
                    System.out.printf(this.mostrarCarta(c));
                }
            }
        }
    }

    @Override
    public void iniciar() { //CREA UN USUARIO
        while (this.controlador.getJugador() == null) {
            System.out.println("Igrese nombre de usuario: ");
            String nombre = this.scanner.nextLine();
            try {
                this.controlador.conectarUsuario(nombre);
            } catch (IllegalArgumentException e) {
                this.mostrarError("Nombre invalido: " + e.getMessage());
            }
        }
    }

    public Integer tieneTresRojos() {
        Integer cantidad;
        cantidad = this.controlador.JugarTresRojos();
        return cantidad;
    }


    public void RobarCarta() {
        this.controlador.getJugador().agregarCartaMano(this.controlador.GetMesa().roboCarta()); //Hace que el jugador robe una carta del mazo
    }

    public Carta devolverCartaRoboInicial() { //Devuelve la primera carta de la mano del jugador util para los robo iniciales al empezar la partida
        RobarCarta();//Primero imprime por pantalla un mensaje con la carta que robo el jugador para que los jugadores sepan cual es despues la devuelve al menu para que el decida quien inicia
        System.out.println("El jugador " + this.controlador.getJugador().getNombre() + " agarro la carta " + this.mostrarCarta(this.controlador.getJugador().mostrarCartaRoboInicial()));
        return this.controlador.getJugador().mostrarCartaRoboInicial();
    }

    public Carta RoboInicialJoquer() {
        System.out.println("El jugador " + this.controlador.getJugador().getNombre() + " saco un joquer volvera a sacar una carta de abajo del mazo");
        this.controlador.getJugador().VaciarMano(); //Vaciamos la mano inicial
        this.controlador.getJugador().agregarCartaMano(this.controlador.GetMesa().roboCartaUltima());
        System.out.println("El jugador " + this.controlador.getJugador().getNombre() + " agarro la carta " + this.mostrarCarta(this.controlador.getJugador().mostrarCartaRoboInicial()));
        return this.controlador.getJugador().mostrarCartaRoboInicial();
    }

    public void mostrarQueInicia() {
        System.out.println("El jugador " + this.controlador.getJugador().getNombre() + " arrancara la partida.");
        this.controlador.GetMesa().reiniciarMazo(); //Reinicia el mazo ya que se sacaron cartas del mismo para usarlas en el robo inicial
    } //Esto se hace haci ya que es mas facil reiniciar el mazo que devolver los valores

    public void vaciarMano() {
        this.controlador.getJugador().VaciarMano();
    }

    @Override
    public void cortarMazo() { //Encargado de cortar mazo y mostrar por patalla si saca una carta que se podra quedar
        boolean perfecto;
        boolean repetir = false;
        System.out.println("El jugador " + this.controlador.getJugador().getNombre() + " cortara el mazo");
        perfecto = this.controlador.GetMesa().CortarMazo(this.controlador.getJugador());
        if (perfecto) {
            System.out.println("corto el mazo perfectamente por la mitad se le sumaran 100 puntos si el jugador consigue una canasta");
        }
        int repeticion = 0;
        while (!repetir) {
            repetir = (this.controlador.GetMesa().comprobarCorte(this.controlador.getJugador()));
            if (!repetir) {
                System.out.println("El jugador " + this.controlador.getJugador().getNombre() + " robo " + this.mostrarCarta(this.controlador.getJugador().verCarta(repeticion++)));
            }
        }

    }

    public void mostrarError(String mensajeError) {
        System.out.println(" [!] ERROR: " + mensajeError);
    }

    public void repartirCartaInicialesMesa() {
        Carta inicial;
        inicial = controlador.GetMesa().repartirCartasInicialesMesa();
        System.out.println("Se pusieron 6 carta en el pote boca abajo.");
        System.out.println("Se puso " + this.mostrarCarta(inicial) + " en la mesa");
    }

    private void validarOpcionDescarte(){
        if (this.controlador.getJugador().tamanoMano() <= 0 ){
            this.mostrarError("La mano no tiene cartas para descartar");
        }
        else {
            int tamanoMano = controlador.getJugador().tamanoMano();
            Integer opcion = null;
            while (opcion == null){
                System.out.println("Elije la carta que quieras descartar: ");
                if (scanner.hasNextInt()){
                    int entrada = scanner.nextInt();
                    if (entrada >= 0 && entrada <=tamanoMano){
                        opcion = entrada;
                    }
                    else {
                        System.out.println("Numero fuera de rango por favor introdusca un numero dentro del rango");
                    }
                }
                else {
                    System.out.println("Entrada no valida. Ingrese un numero.");
                    scanner.next();
                }
            }
            controlador.descartarUnaCarta(opcion); //Descarta la carta
        }
    }

    public void MostrarDescarte(){
        Carta c = controlador.VerTopePilaDescarte();
        System.out.println("En la pila de descarte hay: " + mostrarCarta(c));
    }

    public void AvisarJugada(){
        System.out.println("La jugada se hiso correctamente");
    }

    public void AvisarAgregoCartaCanasta(){System.out.println("Se agrego una carta a una canasta");}

    public void AvisarAbrirse(){System.out.println("El jugador " + controlador.getJugador().getNombre() + " se ha abierto");}

    public void AvisarNoJugada(){
        System.out.println("Esa jugada no es valida");
    }

    @Override
    public void AvisarNoTapon() {
        System.out.println("Esa carta no se puede usar como tapon");
    }

    @Override
    public void AvisarTapon() {
        System.out.println("Taponeado con exito");
    }

    public void AvisarTresRojo(){
        System.out.println("Se han colocado 3 rojos");
    }

    public void AvisarRoboDescarte(){
        System.out.println("Se a robado el descarte");
    }

    public void AvisarGanoEquipo1(){
        System.out.println("El equipo 1 a ganado el juego.");
        finalizo = true;
    }
    public void AvisarGanoEquipo2(){
        System.out.println("El equipo 2 ha ganado el juego.");
        finalizo = true;
    }
}