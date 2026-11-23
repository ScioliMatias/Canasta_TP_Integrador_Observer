package Errores;

public class NoExisteCartaPosicion extends RuntimeException {
    public NoExisteCartaPosicion(String message) {
        super(message);
    }
}
