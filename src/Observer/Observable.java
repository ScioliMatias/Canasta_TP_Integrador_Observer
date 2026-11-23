package Observer;

import java.util.ArrayList;
import java.util.List;

//Implementacion de observador
public abstract class Observable implements IObservable {
    private final List<IObservador> observadores = new ArrayList<>();

    @Override
    public void AgregarObserver(IObservador o) {
        observadores.add(o);
    }

    @Override
    public void RemoverObserver(IObservador o) {
        observadores.remove(o);
    }

    @Override
    public void NotificarObservador(Object evento) {
        for (IObservador o : observadores){
            o.actualizar(this, evento);
        }
    }
}
