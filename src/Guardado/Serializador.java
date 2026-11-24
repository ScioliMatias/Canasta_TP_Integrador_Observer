package Guardado;

import Partida.Jugador;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Serializador {
    private final String ganadores = "ganadores";

    public Serializador() {
        super();
    }

    public boolean WriterObjerct(Object o) {
        boolean respuesta = false;
        try (ObjectOutputStream oss = new ObjectOutputStream(new FileOutputStream(ganadores))) {
            oss.writeObject(o);
            respuesta = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return respuesta;
    }

    public boolean addOneObject(Object o) {
        boolean respuesta = false;
        File archivo = new File(ganadores);
        try {
            if (!archivo.exists() || archivo.length() == 0) {
                try (ObjectOutputStream oss = new ObjectOutputStream(new FileOutputStream(archivo))) {
                    oss.writeObject(o);
                }
            } else {
                try (AddableObjectOutStram oss = new AddableObjectOutStram(new FileOutputStream(archivo, true))) {
                    oss.writeObject(o);
                }
            }
            respuesta = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return respuesta;
    }

    public Object[] readObject() {
        File archivo = new File(ganadores);
        if (!archivo.exists() || archivo.length() == 0) {
            return null;
        }

        ArrayList<Object> listOfObject = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
            while (true) {
                listOfObject.add(ois.readObject());
            }
        } catch (EOFException e) {
            // Fin normal del archivo al leer objetos secuencialmente
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        if (listOfObject.isEmpty()) {
            return null;
        }

        Object[] respuesta = new Object[listOfObject.size()];
        int count = 0;
        for (Object o : listOfObject) {
            respuesta[count++] = o;
        }
        return respuesta;
    }

    public boolean registrarGanador(Jugador jugador) {
        if (jugador == null || jugador.getNombre() == null) {
            return false;
        }

        String nombreBase = limpiarNombreBase(jugador.getNombre().trim());
        int victoriasPrevias = contarVictorias(nombreBase);

        String entradaAGuardar = (victoriasPrevias == 0)
                ? nombreBase
                : nombreBase + " " + (victoriasPrevias + 1);

        return addOneObject(entradaAGuardar);
    }

    private String limpiarNombreBase(String nombre) {
        return nombre.replaceAll("\\s+\\d+$", "");
    }

    private int contarVictorias(String nombreBase) {
        Object[] guardados = readObject();
        if (guardados == null) {
            return 0;
        }

        int contador = 0;
        for (Object obj : guardados) {
            if (obj instanceof String) {
                String registro = ((String) obj).trim();
                String baseRegistro = limpiarNombreBase(registro);
                if (baseRegistro.equalsIgnoreCase(nombreBase)) {
                    contador++;
                }
            }
        }
        return contador;
    }

    public String obtenerTop5Ganadores() {
        Object[] guardados = readObject();

        if (guardados == null || guardados.length == 0) {
            return "Todavía no hay ganadores registrados.";
        }

        Map<String, Integer> victoriasPorJugador = new HashMap<>();

        for (Object obj : guardados) {
            if (obj instanceof String) {
                String registro = ((String) obj).trim();
                if (!registro.isEmpty()) {
                    String nombreBase = limpiarNombreBase(registro);
                    victoriasPorJugador.put(
                            nombreBase,
                            victoriasPorJugador.getOrDefault(nombreBase, 0) + 1
                    );
                }
            }
        }

        if (victoriasPorJugador.isEmpty()) {
            return "Todavía no hay ganadores registrados.";
        }

        ArrayList<EntradaRanking> ranking = new ArrayList<>();
        for (String nombre : victoriasPorJugador.keySet()) {
            ranking.add(new EntradaRanking(nombre, victoriasPorJugador.get(nombre)));
        }

        for (int i = 0; i < ranking.size() - 1; i++) {
            for (int j = i + 1; j < ranking.size(); j++) {
                EntradaRanking a = ranking.get(i);
                EntradaRanking b = ranking.get(j);

                boolean debeIntercambiar = false;
                if (b.victorias > a.victorias) {
                    debeIntercambiar = true;
                } else if (b.victorias == a.victorias && a.nombre.compareToIgnoreCase(b.nombre) > 0) {
                    debeIntercambiar = true;
                }

                if (debeIntercambiar) {
                    ranking.set(i, b);
                    ranking.set(j, a);
                }
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append("=== TOP 5 GANADORES ===").append(System.lineSeparator());

        int limite = Math.min(5, ranking.size());
        for (int i = 0; i < limite; i++) {
            EntradaRanking item = ranking.get(i);
            String textoVictorias = (item.victorias == 1) ? "1 victoria" : item.victorias + " victorias";
            sb.append(i + 1)
                    .append(". ")
                    .append(item.nombre)
                    .append(" (")
                    .append(textoVictorias)
                    .append(")")
                    .append(System.lineSeparator());
        }

        return sb.toString().trim();
    }

    private static class EntradaRanking {
        String nombre;
        int victorias;

        EntradaRanking(String nombre, int victorias) {
            this.nombre = nombre;
            this.victorias = victorias;
        }
    }
}