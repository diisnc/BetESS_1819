/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package betess;

import java.util.*;

/**
 *
 *
 */
public class Database {
    
    private HashMap<String, Jogador> jogadores;
    private HashMap<Integer, Aposta> apostas;
    private HashMap<Integer, EventoDesportivo> eventos;
    
    /* CONTADORES DE IDENTIDICAÇÃO */
    private int cont_apostas;
    private int cont_eventos;
    
    public Database (){
        this.jogadores = new HashMap<String, Jogador> ();
        this.apostas = new HashMap<Integer, Aposta> ();
        this.eventos = new HashMap<Integer, EventoDesportivo> ();
        this.cont_apostas = 1;
        this.cont_eventos = 1;
    }
    
    /* inserção de um jogador no sistema */
    public void registaJogador(Jogador j){
        this.jogadores.put(j.getEmail(), j);
    }
    
    public void registaEvento(EventoDesportivo e){
        this.eventos.put(cont_eventos++, e);
    }
    
    public void registaAposta(Aposta a){
        this.apostas.put(cont_apostas++, a);
    }
    
    public Jogador checkUser(String username){
        Jogador jogador = null;
        if (this.jogadores.containsKey(username)){
            jogador = this.jogadores.get(username);
        }
        return jogador;
    }
    
    public void updateSaldo(String id_jogador, double novo_saldo){
        Jogador j = this.jogadores.get(id_jogador);
        j.setSaldo(novo_saldo);
    }
    
    public void atualizaEventoDesportivo(EventoDesportivo e){
        this.eventos.put(e.getId_evento(), e);
    }
    
    public void registaEventoDesportivo(EventoDesportivo e){
        this.eventos.put(cont_eventos++, e);
    }
    
    public EventoDesportivo getEventoDesportivo(int id){
        return this.eventos.get(id).clone();
    }
    
    public HashMap<Integer, Aposta> getApostas(){
        HashMap<Integer, Aposta> res = new HashMap ();
        
        for (Aposta a : this.apostas.values()){
            res.put(a.getId_aposta(), a.clone());
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

    void atualizaSaldo(double creditos, String id_jogador) {
        Jogador j = this.jogadores.get(id_jogador);
        
        if (j != null){
            double saldo = j.getSaldo();
            j.setSaldo(saldo + creditos);
            this.jogadores.put(id_jogador, j);
        }
    }

    List<Aposta> getApostasJogador(String id_jogador) {
        List<Aposta> res = new ArrayList();
        
        for (Aposta a: this.apostas.values()){
            String id_jogador_aposta = a.getId_jogador();
            
            if (id_jogador_aposta.equals(id_jogador)){
                res.add(a.clone());
            }
        }
        return res;
    }
    
}