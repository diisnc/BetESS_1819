package betess.control;

public interface Subject {

    void registerObserver(Observer obj);

    void removeObserver(Observer obj);

    void notifyObserver(String arg);

}
