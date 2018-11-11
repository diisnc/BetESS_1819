/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package betess;

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
        /* QUANDO FOR SERIALIZABLE, TEMOS DE CARREGAR AQUI */
        this.database = new Database();
    }
    
    /* método que permite definir o jogador autenticado na sessao */
    public void setId_utilizador_aut(String id_utilizador_aut) {
        this.id_utilizador_aut = id_utilizador_aut;
    }

    public String getId_utilizador_aut() {
        return id_utilizador_aut;
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
    
    public Map<Integer, EventoDesportivo> getEventosDesportivos(){
        return this.database.getEventosDesportivos();
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
                double odd = -1;
                
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
