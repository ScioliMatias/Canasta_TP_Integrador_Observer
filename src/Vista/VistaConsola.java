package Vista;

import Cartas.Carta;
import Controlador.Controlador;

import java.util.ArrayList;
import java.util.Scanner;
import Controlador.Controlador;
import Partida.IJugador;

public class VistaConsola implements IVista {
    private final Controlador controlador;
    private final Scanner scanner;

    public VistaConsola(Controlador controlador) {
        this.controlador = controlador;
        this.scanner = new Scanner(System.in);
        this.controlador.setVista(this);
    }

    @Override
    public void MostrarJugadores(IJugador[] jugador) {
        System.out.println("Lista de usuario" + "\n");
        for (IJugador s : jugador) {
            System.out.println(s.getNombre() + "-id-" + s.getId());
        }
        System.out.println("=========================");
    }

    public boolean menu() {
        boolean termino = false;
        termino = this.procesarOpcion();
        return termino;
    }

    private boolean procesarOpcion() {
        boolean bucle = true;
        boolean termino = false;
        String opcion;
        while (bucle){
            this.imprimirMenu();
            opcion = this.scanner.nextLine().trim();
            switch (opcion){
                case ("1"): //Roba el descarte
                    bucle = false;
                    this.controlador.robarPote();
                    break;
                case ("2"): //tomar carta del mazo (agarrar 2 y tirar 1)
                    bucle = false;
                    this.controlador.robarCartaMazo();
                    this.controlador.robarCartaMazo();
                    this.validarOpcionDescarte();
                    break;
                case ("3"): //Hace una jugada entre 3 a 6 cartas no consideramos 7 ya que eso seria hacer una canasta
                    boolean jugada;
                    jugada = this.controlador.Abrirse(PedirJugada()); // por ahora asi debe crearse un menu que pida que cartas quieren ser parte de las jugadas
                    if (jugada){ //la jugada si se realizo si no seguiria en este bucle
                        bucle = false;
                        break;
                    }
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
                        break;
                    }//agregar cartas a una canasta (agregas cartas a una canasta para sumar mas puntos)
                    break;
                case ("6"): // jugar una canasta de una vez
                    boolean canasta;
                    canasta = this.controlador.Canasta(PedirJugada());
                    if (canasta){
                        bucle = false;
                        break;
                    }
                    break;
                case ("7")://Esto no quita el bucle
                        System.out.println("Las jugadas en la mesa son: ");
                        this.MostrarJugada();
                    break;
                case ("8"):
                        System.out.println("Tu mano es: ");
                        this.MostrarManoJugador();
                    break;
                default:
                    this.mostrarError("Por favor ingrese una opcion valida");
                    break;
            }
        }
        return termino;// Devuelve verdadero cuando la partida acabe
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
                "5. Agregar cartas a una canasta 6.Jugar canasta oculta 7.Ver jugadas 8. Ver tu mano]");
        System.out.println(this.controlador.getJugador().getNombre() + " Ingrese una opcion: ");
    };

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
        cantidad = this.controlador.getJugador().jugarTresRojo();
        if (cantidad != 0) {
            System.out.println("Se jugadoron los 3 rojos");
        }
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

    public void jugadaInicial(){
        boolean valida = true;
        this.MostrarManoJugador();// Le muestro su mano ya que tiene que tomar una decision en base a ella
        System.out.println("Te interesa llevarte el pote: \n1. si \n2.no");
        while (valida) {
            System.out.println("Elija una opcion: ");
            String opcion = scanner.nextLine();
            switch (opcion) {
                case "1":
                    if (this.controlador.robarPoteInicial()) { //si esto devuelve verdadero tambien hace la apcion
                        valida = false;
                        System.out.println("El jugador " + controlador.getJugador().getNombre() + " se robo el tope");
                        //Controlador.HacerJugada
                        break;
                    } else {
                        System.out.println("No cumple con las condiciones para hacer esta opcion. ");
                    }
                case "2":
                    controlador.robarCartaMazo();
                    controlador.robarCartaMazo();
                    this.MostrarManoJugador(); // muestro la mano ya que tiene que elejir una carta para descartar
                    validarOpcionDescarte(); //Descarta la carta una ves elejida esta opcion no hay vuelta atras
                    valida = false;
                    break;
                default:
                    this.mostrarError("Elija una opcion de 1 si o 2 no: ");
            }
        }
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
}