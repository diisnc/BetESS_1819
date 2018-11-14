/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package betess;

import java.io.*;
import java.util.*;

/**
 *
 * @author MarcoSilva
 */
public class BetESS {
    
    private Database database;
    private String id_utilizador_aut;
    
    /* Contrutor BetESS */
    public BetESS(){
        Database d = null;
        try {
            FileInputStream fileIn = new FileInputStream("/tmp/database");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            d = (Database) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException i) {
            i.printStackTrace();
        } catch (ClassNotFoundException c) {
            System.out.println("Database class not found");
            c.printStackTrace();
        }
        if (d == null){
            System.out.println("Estado da aplicação iniciado.");
            this.database = new Database();
        }
        else {
            System.out.println("Restauro da aplicação com sucesso.");
            this.database = d;
        }
    }
    
    public void save() {
        try {
            FileOutputStream fileOut = new FileOutputStream("/tmp/database");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(this.database);
            out.close();
            fileOut.close();
            System.out.printf("Serialized data!");
        } catch (IOException i) {
              i.printStackTrace();
        }
    }
    
    /* método que permite definir o jogador autenticado na sessao */
    public void setId_utilizador_aut(String id_utilizador_aut) {
        this.id_utilizador_aut = id_utilizador_aut;
    }

    public String getId_utilizador_aut() {
        return id_utilizador_aut;
    }
    
    public HashMap<String, Jogador> getJogadores(){
        return this.database.getJogadores();
    }
    
    public void eliminaJogador(String id){
        this.database.eliminaJogador(id);
    }
    
    public void bloqueiaJogador(String id){
        this.database.bloqueiaJogador(id);
    }
    
    public void desbloqueiaJogador(String id){
        this.database.desbloqueiaJogador(id);
    }
    
    public Aposta getAposta(int id_aposta){
        return this.database.getAposta(id_aposta);
    }
    
    public Equipa getEquipa(String id_equipa){
        return this.database.getEquipa(id_equipa);
    }
    
    public List<Equipa> getEquipas(){
        return this.database.getEquipas();
    }
    
    /* inserção de um jogador no sistema */
    public void registaJogador(Jogador j){
        this.database.registaJogador(j);
    }
    
    public void registaAposta(Aposta a){
        this.database.registaAposta(a);
    }
    
    public void atualizaSaldo(double creditos, String id_jogador){
        this.database.atualizaSaldo(creditos, id_jogador);
    }
    
    /* verificação se um jogador se encontra registado no sistema */
    public Jogador checkUser(String username){
        return this.database.checkUser(username);
    }
    
    public EventoDesportivo getEventoDesportivo(int id){
        return this.database.getEventoDesportivo(id);
    }
    
    public void registaLiga(Liga l){
        this.database.registaLiga(l);
    }
    
    public List<Liga> getLigas(){
        return this.database.getLigas();
    }
            
    public void registaEventoDesportivo(String equipa_casa, String equipa_fora){
        this.database.registaEventoDesportivo(equipa_casa, equipa_fora);
    }
    
    public Map<Integer, EventoDesportivo> getEventosDesportivos(){
        return this.database.getEventosDesportivos();
    }
    
    public HashMap<Integer, Aposta> getApostas(){
        return this.database.getApostas();
    }
    
    public void removeAposta(int id){
        this.database.removeAposta(id);
    }
    
    public List<Aposta> getApostasJogador(String id_jogador){
        return this.database.getApostasJogador(id_jogador);
    }
    
    /* método que tratará do fecho de um evento desportivo com o respetivo pagamento das apostas referentes a esse */
    public void fechaEvento(int id_Evento){
        
        EventoDesportivo e = this.database.getEventoDesportivo(id_Evento);
        e.setEstado("Terminado");
        
        for (Aposta a : database.getApostas().values()){
                
            if ( e != null){
                boolean evento_ganha_casa = e.getGanha_casa();
                boolean evento_ganha_fora = e.getGanha_fora();
                boolean evento_empate = e.getEmpate();
                
                boolean aposta_ganha_casa = a.getGanha_casa();
                boolean aposta_ganha_fora = a.getGanha_fora();
                boolean aposta_empate = a.getEmpate();
                
                Jogador j = this.database.checkUser(a.getId_jogador());
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
                else {
                    saldo -= quant_aposta;
                }
                /* lançamento de notificações */
                Notificacao n = new Notificacao(a.getId_aposta(), saldo - saldo_ant);
                Jogador jogador = this.database.checkUser(a.getId_jogador());
                jogador.adicionaNotificacao(n);
                this.database.registaJogador(jogador);
                
                /* atualização do saldo do cliente */
                this.database.updateSaldo(j.getEmail(), saldo);
            }
        }
        this.database.atualizaEventoDesportivo(e);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Login l = new Login();
        l.setLocationRelativeTo(null);
        l.setVisible(true);
    }
}
