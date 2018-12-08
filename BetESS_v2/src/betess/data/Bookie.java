package betess.data;

import java.io.Serializable;
import java.util.*;

public class Bookie implements Serializable{
    
    //private String username = "bookie";
    //private String password = "bookie";
    public List<NotificacaoBookie> notificacoes;
    public List<Integer> id_eventos_interesse;
    
    public Bookie(){
        this.notificacoes = new ArrayList<> ();
    }
    
    public Bookie(List<NotificacaoBookie> notificacoesB){
        this.notificacoes = notificacoesB;
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
                this.notificacoes.remove(id_evento);
                break;
            }
        }
    }
    
    public Bookie clone(){
        return new Bookie(this.getNotificacoes());
    }
 
    
}
