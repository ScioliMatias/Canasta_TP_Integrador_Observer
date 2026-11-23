package Cartas;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Mazo {
    private ArrayList<Carta> mazo;

    public Mazo(){
        this.mazo = new ArrayList<>();
        cargarCartas();
        Collections.shuffle(mazo); //Al crear un nuevo mazo lo mesclo ya que ordenado no me sirve
    }
    public Mazo(Integer i){ // Al agregar un integer lo unico que hago es crear un mazo vacio esto lo uso para poder hacer inciadores de mazo
        this.mazo = new ArrayList<>();
    }

    private void vaciarMazo(){
        this.mazo = new ArrayList<>(); //en ves de eliminar todos los valores del mazo creo un nuevo mazo ya vacio
    }

    public void reiniciarMazo(){ //Encargado de reiniciar el mazo
        vaciarMazo();
        cargarCartas();
        Collections.shuffle(mazo);
    };

    private void cargarCartas(){ //Crea el mazo y va asignando su color y palo
        String[] palos = {"Corazones", "Diamantes", "Picas", "Tréboles"};
        String[] valores = {"As", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
        for (Integer i = 0; i < 2; i++){ //Crea dos mazos
            for (String palo: palos){
                Integer valorInicial = 1; //El valor inicial es creado aca para reiniciarlo cada ves que se cambie de palo
                for (String valor: valores){
                    int puntos = asignarPuntos(valor ,palo);
                    mazo.add(new Carta(valor, palo,puntos,valorInicial));
                    valorInicial++; //El valor inicial es un valor creado unicamente para el inicio de la partida hace que cada carta valga de 1 a 13
                }
            }
            //agregamos los joker
            mazo.add(new Carta("Joker","Comodin",50, 999)); //el valor 999 es una advertencia para las funciones siguientes que indicaran
            mazo.add(new Carta("Joker","Comodin",50, 999)); //que se tiene que volver a sacar una carta esta ves de abajo del mazo
        }

    }

    private int asignarPuntos(String valor, String palo){
        switch (valor){ //asigna los puntos del mazo dependiendo de cual palo y color sea segun las reglas
            case "As": case "2":
                return 20;
            case "8": case "9": case "10": case "J": case "Q": case "K":
                return 10;
            case "4": case "5": case"6": case "7":
                return 5;
            case "3":
                if (palo.equals("Corazones") || palo.equals("Diamantes")){
                    return 100;
                }else {
                    return -100;
                }
            default: return 0;
        }
    }

    public Carta drawCart(){
        Carta primerCarta = mazo.getFirst(); //consigo la primera carta del mazo
        mazo.removeFirst(); //la saco ya que ya esta guardada
        return primerCarta; // la devuelvo para que sea usada
    }

    public Carta mostrarTope(){
        return mazo.getLast();
    }

    public Carta drawLastCart(){
        Carta ultimaCarta = mazo.getLast(); //lo mismo que hace la funcion de arriba pero con la ultima carta del mazo
        mazo.removeLast();
        return ultimaCarta;
    }

    public ArrayList<Carta> robarPila(){ //al robar el descate se roba completo
        ArrayList<Carta> descarte = new ArrayList<Carta>(this.mazo);
        Integer lugar = 0;
        Integer lugarTapon = 0;
        boolean tapon = false;
        for (Carta c: descarte){
            lugar++; // Esto se suma aca ya que el tapon tambien lo tengo que agregar denuevo al deck
            if (c.getValorInicial() == -1000){ //si esto se cumple hay un tapon
                tapon = true;
                lugarTapon= lugar;
            }
        }
        this.vaciarMazo();
        if (tapon){ //Se econtro un tapon y tengo el lugar lo unico que tengo que hacer es remover del descarte la cartas que se encuentre hay y ir agregandolas al descarte real para que se quede
            for (int i = 0; i < lugarTapon; i++){
                this.agregarCarta(descarte.getFirst());
                descarte.removeFirst(); //Voy sacando las cartas del descarte que no puedo devolver por que no son necesaria
            }
        }
        return descarte; // devuelvo
    }

    public void agregarCarta(Carta carta){ mazo.add(carta);} //Agrego una carta al mazo usado para el descarte

    public void cortarMazo(Integer corte){ //Esto es usado para corta el mazo
        List<Carta> parte1 = mazo.subList(0,corte); //parte el mazo en 2 partes
        List<Carta> parte2 = mazo.subList(corte, mazo.size());

        this.mazo = new ArrayList<>(parte2); //crea un nuevo mazo con la segunda parte
        mazo.addAll(parte1); //Agregamos la primera parte para que el mazo este entero
    }
}
