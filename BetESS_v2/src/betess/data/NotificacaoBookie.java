package betess.data;

import java.io.Serializable;

public class NotificacaoBookie implements Serializable{
    
    private int id_evento;
    private double balanco;
    private double ganhos;
    private double perdas;
    
    public NotificacaoBookie(int id_evento, double balanco, double ganhos, double perdas){
        this.id_evento = id_evento; 
        this.balanco = balanco;
        this.ganhos = ganhos;
        this.perdas = perdas;
    }

    public NotificacaoBookie(){
        this.balanco = 0;
        this.ganhos = 0;
        this.perdas = 0;
    }
  

    /* GETTERS */
    
    public double getGanhos(){
        return ganhos;
    }
    
    public double getPerdas() {
        return perdas;
    }

    public double getBalanco() {
        return balanco;
    }

    public int getIdEvento() {
        return this.id_evento;
    }
    
    /* SETTERS */

    public void setGanhos(double ganhos) {
        this.ganhos = ganhos;
    }

    public void setPerdas(double perdas) {
        this.perdas = perdas;
    }

    public void setBalanco(double balanco) {
        this.balanco = balanco;
    }
    
    public void setIdEvento(int idEvento){
        this.id_evento = idEvento;
    }
     
    public NotificacaoBookie clone(){
        return new NotificacaoBookie(this.getIdEvento(), this.getBalanco(), this.getGanhos(), this.getPerdas());
    } 
    
}
