package betess.data;

import java.io.Serializable;

public class NotificacaoBookie implements Serializable{
    
    private int id_evento;
    private double ganhos;
    private double perdas;
    
    public NotificacaoBookie(double ganho, double perda){
        this.ganhos = ganho;
        this.perdas = perda;
    }

    public NotificacaoBookie(){
        this.ganhos = 0;
        this.perdas = 0;
    }
    
    
    
    
    /* GETTERS */    
    public double getGanhos() {
        return this.ganhos;
    }  
    
    public double getPerdas() {
        return this.perdas;
    }  
    
    public int getIdEvento(){
        return this.id_evento;
    }
    
    /* SETTERS */
    public void setGanhos(double ganho){
        this.ganhos = ganho;
    }
     
    public void setPerdas(double perda){
        this.perdas = perda;
    }
    
    public void setIdEvento(int idEvento){
        this.id_evento = idEvento;
    }
     
    public NotificacaoBookie clone(){
        return new NotificacaoBookie(this.getGanhos(), this.getPerdas());
    } 
    
}
