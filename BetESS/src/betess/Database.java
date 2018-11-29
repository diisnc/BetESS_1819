/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package betess;

import java.io.Serializable;
import java.util.*;


public class Database implements Serializable{
    
    private HashMap<String, Jogador> jogadores;
    private HashMap<String, Jogador> jogadores_bloqueados;
    private HashMap<Integer, Aposta> apostas;
    private HashMap<Integer, EventoDesportivo> eventos;
    private HashMap<String, Equipa> equipas;
    private HashMap<String, Liga> ligas;
    
    /* CONTADORES DE IDENTIFICAÇÃO */
    private int cont_apostas;
    private int cont_eventos;
    
    public Database (){
        this.jogadores = new HashMap<> ();
        this.apostas = new HashMap<> ();
        this.eventos = new HashMap<> ();
        this.jogadores_bloqueados = new HashMap<> ();
        this.equipas = new HashMap<> ();
        this.ligas = new HashMap<> ();
        this.cont_apostas = 1;
        this.cont_eventos = 1;
    }
    
    /* registo de uma equipa */
    public void registaEquipa(Equipa e){
        this.equipas.put(e.getDesignacao(), e);
    }
    
    public void registaLiga(Liga l){
        this.ligas.put(l.getNome(), l.clone());
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
        this.jogadores.remove(id);
        this.jogadores_bloqueados.put(id, j);
    }
    
    public void desbloqueiaJogador(String id){
        Jogador j = this.jogadores_bloqueados.get(id);
        this.jogadores_bloqueados.remove(id);
        this.jogadores.put(id, j);
    }
    
    /* inserção de um jogador no sistema */
    public void registaJogador(Jogador j){
        this.jogadores.put(j.getEmail(), j);
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
    }
    
    public void updateSaldo(String id_jogador, double novo_saldo){
        Jogador j = this.jogadores.get(id_jogador);
        j.setSaldo(novo_saldo);
    }
    
    public void atualizaEventoDesportivo(EventoDesportivo e){
        this.eventos.put(e.getId_evento(), e);
    }
    
    public void atualizaAposta(Aposta a){
        this.apostas.put(a.getId_aposta(), a);
    }
    
    public void registaEventoDesportivo(String equipa_casa, String equipa_fora, double odd_casa, double odd_fora, double odd_empate){
        EventoDesportivo e = new EventoDesportivo(this.cont_eventos++, equipa_casa, equipa_fora, odd_casa, odd_fora, odd_empate);
        this.eventos.put(e.getId_evento(), e);
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

    public void removeAposta(int id) {
        this.apostas.remove(id);
    }
}