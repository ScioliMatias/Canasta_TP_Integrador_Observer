package Observer;

public interface IObservable {
    void AgregarObserver(IObservador o);
    void RemoverObserver(IObservador o);
    void NotificarObservador(Object Evento);
}
