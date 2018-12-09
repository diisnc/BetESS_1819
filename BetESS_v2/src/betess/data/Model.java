package betess.data;

import betess.control.Observer;
import betess.control.Subject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/*
* This is the CONCRETE SUBJECT !
*/


public class Model implements Serializable, Subject{
    
    /* Core Data */
    private HashMap<String, Jogador> jogadores;
    private Bookie bookie;
    private HashMap<String, Jogador> jogadores_bloqueados;
    private HashMap<Integer, Aposta> apostas;
    private HashMap<Integer, EventoDesportivo> eventos;
    private HashMap<String, Equipa> equipas;
    private HashMap<String, Liga> ligas;
    
    /* CONTADORES DE IDENTIFICAÇÃO */
    private int cont_apostas;
    private int cont_eventos;
    
    
    /* Set of Observers */
    private List<Observer> observers;
    
    
    public Model (){
        this.jogadores = new HashMap<> ();
        this.bookie = new Bookie();
        this.apostas = new HashMap<> ();
        this.eventos = new HashMap<> ();
        this.jogadores_bloqueados = new HashMap<> ();
        this.equipas = new HashMap<> ();
        this.ligas = new HashMap<> ();
        this.observers = new ArrayList<> ();
        this.cont_apostas = 1;
        this.cont_eventos = 1;
    }
    
    public Bookie getBookie(){
        return this.bookie.clone();
    }
    
    public void removeNotificacaoBookie(int id_evento){
        this.bookie.removeNotificacao(id_evento);
    }
    
    @Override
    public void registerObserver(Observer obj) {
        this.observers.add(obj);
    }

    @Override
    public void removeObserver(Observer obj) {
        /* VERIFICAR ESTE CASO SE FUNCIONA */
        this.observers.remove(obj);
    }

    @Override
    public void notifyObserver(String arg) {
        for (Observer o : this.observers){
            /*if (o instanceof AreaUI){
                AreaUI areaui = (AreaUI) o;
                areaui.update(arg);
            }
            else if (o instanceof Login){
                Login login = (Login) o;
                login.update(arg);
            }
            else if (o instanceof Registo){
                Registo registo = (Registo) o;
                registo.update(arg);
            }*/
            o.update(arg);
        }
    }
    
    /* registo de uma equipa */
    public void registaEquipa(Equipa e){
        this.equipas.put(e.getDesignacao(), e);
        notifyObserver("equipas");
    }
    
    public void registaLiga(Liga l){
        this.ligas.put(l.getNome(), l.clone());
        notifyObserver("ligas");
    }
    
    public List<Liga> getLigas(){
        List<Liga> res = new ArrayList<> ();
        
        for (Liga l: this.ligas.values()){
            res.add(l.clone());
        }
        return res;
    }
    
    public void removeNotificacao(String id_utilizador, int id_aposta){
        Jogador j = this.jogadores.get(id_utilizador);
        
        j.removeNotificacao(id_aposta);
        
        this.jogadores.put(id_utilizador, j.clone());
        this.notifyObserver("notificacoes");
    }
    
    public Equipa getEquipa(String id_equipa){
        return this.equipas.get(id_equipa).clone();
    }
    
    public List<Equipa> getEquipas(){
        List<Equipa> res = new ArrayList<> ();
        
        for (Equipa e : this.equipas.values()){
            res.add(e.clone());
        }
        return res;
    }
    
    public void bloqueiaJogador(String id){
        Jogador j = this.jogadores.get(id);
        System.out.println(j.getEmail());
        this.jogadores.remove(id);
        this.jogadores_bloqueados.put(id, j);
        notifyObserver("jogadores_bloqueados");
    }
    
    public void desbloqueiaJogador(String id){
        Jogador j = this.jogadores_bloqueados.get(id);
        this.jogadores_bloqueados.remove(id);
        this.jogadores.put(id, j);
        notifyObserver("jogadores_bloqueados");
    }
    
    /* inserção de um jogador no sistema */
    public void registaJogador(Jogador j){
        this.jogadores.put(j.getEmail(), j);
        notifyObserver("jogadores");
    }
    
    public HashMap<String, Jogador> getJogadores(){
        HashMap<String, Jogador> res = new HashMap ();
        
        for (Jogador j : this.jogadores.values()){
            res.put(j.getEmail(), j.clone());
        }
        return res;
    }
    
    public List<Jogador> getJogadoresBloqueados(){
        List<Jogador> res = new ArrayList<>();
        
        for (Jogador j : this.jogadores_bloqueados.values()){
            res.add(j.clone());
        }
        
        return res;
    }
    
    public void registaAposta(double quantia, int id_evento, String id_jogador, boolean ganha_casa, boolean ganha_fora, boolean empate){
        Aposta a = new Aposta(this.cont_apostas++, quantia, id_evento, id_jogador, ganha_casa, ganha_fora, empate);
        this.apostas.put(a.getId_aposta(), a);
        Jogador j = this.jogadores.get(id_jogador);
        double saldo_atual = j.getSaldo();
        double novo_saldo = saldo_atual - quantia;
        j.setSaldo(novo_saldo);
        this.jogadores.put(id_jogador, j);
        notifyObserver("apostas");
    }
    
    public Jogador checkUser(String username){
        Jogador jogador = null;
        if (this.jogadores.containsKey(username)){
            jogador = this.jogadores.get(username);
        }
        return jogador;
    }
    
    public void eliminaJogador(String id) {
        this.jogadores.remove(id);
        notifyObserver("jogadores");
    }
    
    public void atualizaEventoDesportivo(EventoDesportivo e){
        this.eventos.put(e.getId_evento(), e);
        notifyObserver("eventos");
    }
    
    public void atualizaAposta(Aposta a){
        this.apostas.put(a.getId_aposta(), a);
        notifyObserver("apostas");
    }
    
    public void registaEventoDesportivo(String equipa_casa, String equipa_fora, double odd_casa, double odd_fora, double odd_empate, boolean interesse_not){
        EventoDesportivo e = new EventoDesportivo(this.cont_eventos++, equipa_casa, equipa_fora, odd_casa, odd_fora, odd_empate);
        this.eventos.put(e.getId_evento(), e);
        if (interesse_not){
            int cont_eventos_aux = cont_eventos - 1;
            this.bookie.adiciona_interesse(cont_eventos_aux);
        }
        notifyObserver("eventos");
    }
    
    public EventoDesportivo getEventoDesportivo(int id){
        return this.eventos.get(id).clone();
    }
    
    public Aposta getAposta(int id_aposta){
        return this.apostas.get(id_aposta).clone();
    }
    
    public HashMap<Integer, Aposta> getApostas(){
        HashMap<Integer, Aposta> res = new HashMap ();
        
        for (Aposta a : this.apostas.values()){
            res.put(a.getId_aposta(), a.clone());
        }
        
        return res;
    }
    
    public List<Aposta> getApostasEvento(int id_evento){
        List<Aposta> res = new ArrayList<>();
        
        for (Aposta a : this.apostas.values()){
            if (a.getId_evento() == id_evento)
                res.add(a.clone());
        }
        
        return res;
    }
    
    public HashMap<Integer, EventoDesportivo> getEventosDesportivos(){
        HashMap<Integer, EventoDesportivo> res = new HashMap ();
        
        for (EventoDesportivo e : this.eventos.values()){
            res.put(e.getId_evento(), e.clone());
        }
        
        return res;
    }

    public void atualizaSaldo(double creditos, String id_jogador) {
        Jogador j = this.jogadores.get(id_jogador);
        
        if (j != null){
            double saldo = j.getSaldo();
            j.setSaldo(saldo + creditos);
            this.jogadores.put(id_jogador, j);
        }
        notifyObserver("jogadores");
        notifyObserver("saldo");
    }

    public List<Aposta> getApostasJogador(String id_jogador) {
        List<Aposta> res = new ArrayList();
        
        for (Aposta a: this.apostas.values()){
            String id_jogador_aposta = a.getId_jogador();
            
            if (id_jogador_aposta.equals(id_jogador)){
                res.add(a.clone());
            }
        }
        return res;
    }

    public void removeAposta(int id) {
        this.apostas.remove(id);
        notifyObserver("apostas");
    }
    
    /* Único método que tem que fazer o notifyObservers pq retorna um valor modificado */
    public void fechaEvento(int id_Evento, boolean ganha_casa, boolean ganha_fora, boolean empate){
        EventoDesportivo e = this.getEventoDesportivo(id_Evento);
        e.setGanha_casa(ganha_casa);
        e.setGanha_fora(ganha_fora);
        e.setEmpate(empate);
        double ganhos = 0;
        double perdas = 0;
        
        for (Aposta a : this.getApostasEvento(e.getId_evento())){
                
            if ( e != null && e.getEstado().equals("Aberto")){
                boolean evento_ganha_casa = e.getGanha_casa();
                boolean evento_ganha_fora = e.getGanha_fora();
                boolean evento_empate = e.getEmpate();
                
                boolean aposta_ganha_casa = a.getGanha_casa();
                boolean aposta_ganha_fora = a.getGanha_fora();
                boolean aposta_empate = a.getEmpate();
                
                Jogador j = this.checkUser(a.getId_jogador());
                double saldo = j.getSaldo();
                double quant_aposta = a.getQuantia();
                double odd = -10000;
                double saldo_ant = saldo;
                
                if (evento_ganha_casa == aposta_ganha_casa 
                 && evento_ganha_fora == aposta_ganha_fora
                 && evento_empate == aposta_empate)
                {
                    if (evento_ganha_casa){
                        odd = e.getOdd_casa();
                        saldo += odd * quant_aposta;
                        
                    }
                    else if (evento_ganha_fora){
                        odd = e.getOdd_fora();
                        saldo += odd * quant_aposta;
                        
                    }
                    else if (evento_empate){
                        odd = e.getOdd_empate();
                        saldo += odd * quant_aposta;
                        
                    }
                }
                
                a.setEstado("Paga");
                this.atualizaAposta(a);
                
                /* lançamento de notificações cliente */
                Notificacao n = new Notificacao(a.getId_aposta(), saldo - saldo_ant);
                Jogador jogador = this.checkUser(a.getId_jogador());
                jogador.adicionaNotificacao(n);
                this.registaJogador(jogador);
                
                /* atualização do saldo do cliente */
                this.atualizaSaldo(saldo, j.getEmail());
                
                /* Cálculo dos ganhos e perdas para notificação do bookie*/
                
                if ((saldo - saldo_ant) > 0){
                    perdas -= (saldo - saldo_ant);
                }
                else {
                    ganhos -= (saldo - saldo_ant);
                }
                
            }
        }
        
        /* lancamento de notificacoes bookie */
        List<Integer> ids_eventos_bookie = this.bookie.getid_eventos_interesse();
        
        for (int i : ids_eventos_bookie){
            if (i == id_Evento){
                this.bookie.adicionaNotificacao(new NotificacaoBookie(id_Evento, ganhos - perdas, ganhos, perdas));
                break;
            }
        }

        e.setEstado("Terminado");
        this.atualizaEventoDesportivo(e);
        
        notifyObserver("notificacoes_bookie");
        notifyObserver("apostas");
        notifyObserver("eventos");
        notifyObserver("jogadores");
        notifyObserver("saldo");
    }   
}