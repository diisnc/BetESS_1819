package betess.data;

import java.io.Serializable;
import java.util.*;

public class Bookie implements Serializable{
    
    public List<NotificacaoBookie> notificacoes;
    public List<Integer> id_eventos_interesse;
    
    public Bookie(){
        this.notificacoes = new ArrayList<> ();
        this.id_eventos_interesse = new ArrayList<> ();
    }
    
    public Bookie(List<NotificacaoBookie> notificacoesB, List<Integer> ids){
        this.notificacoes = notificacoesB;
        this.id_eventos_interesse = ids;
    }
    
    public List<NotificacaoBookie> getNotificacoes(){
        List<NotificacaoBookie> aux = new ArrayList ();
        
        for(NotificacaoBookie n : this.notificacoes){
            aux.add(n.clone());
        }
        
        return aux;
    }
    
    public List<Integer> getid_eventos_interesse(){
        List<Integer> res = new ArrayList<> ();
        
        for (int i : this.id_eventos_interesse){
            res.add(i);
        }
        
        return res;
    }

    public void adicionaNotificacao(NotificacaoBookie n){
        this.notificacoes.add(n);
    }
    
    public void adiciona_interesse(int id_evento){
        this.id_eventos_interesse.add(id_evento);
    }
    
    public void removeNotificacao(int id_evento){
        for (NotificacaoBookie n : this.notificacoes){
            if (n.getIdEvento() == id_evento){
                this.notificacoes.remove(n);
                break;
            }
        }
    }
    
    public Bookie clone(){
        return new Bookie(this.getNotificacoes(), this.getid_eventos_interesse());
    }
 
    
}
