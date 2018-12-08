package betess.data;

import java.io.Serializable;
import java.util.*;

public class Bookie implements Serializable{
    
    /* Se queroN = true, notificações estão ativas */
    private boolean queroN;
    public List<NotificacaoBookie> notificacoes;
    
    public Bookie(boolean queroNotificacao){
        this.queroN = queroNotificacao;
    }
    
    public Bookie(boolean queroNotificacao, List<NotificacaoBookie> notificacoesB){
        this.queroN = queroNotificacao;
        this.notificacoes = notificacoesB;
    }

  
    public boolean getQueroN(){
        return this.queroN;
    }

    public List<NotificacaoBookie> getNotificacoes(){
        List<NotificacaoBookie> aux = new ArrayList ();
        
        for(NotificacaoBookie n : this.notificacoes){
            aux.add(n.clone());
        }
        
        return aux;
    }  
    
    public void setQueroN(boolean queroNotific){
        this.queroN = queroNotific; 
    }

    public void adicionaNotificacao(NotificacaoBookie n){
        this.notificacoes.add(n);
    }
    
    public void removeNotificacao(int id_evento){
        for (NotificacaoBookie n : this.notificacoes){
            if (n.getIdEvento() == id_evento){
                this.notificacoes.remove(id_evento);
                break;
            }
        }
    }
 
    
}
