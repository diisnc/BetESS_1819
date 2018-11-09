/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package betess;

import java.util.HashMap;

/**
 *
 * @author MarcoSilva
 */
public class BetESS {
    
    private HashMap<String, Jogador> jogadores;
    
    /* Contrutor BetESS */
    public BetESS(){
        /* QUANDO FOR SERIALIZABLE, TEMOS DE CARREGAR AQUI */
        this.jogadores = new HashMap<String, Jogador> ();
    }
    
    /* inserção de um jogador no sistema */
    public void regista(Jogador j){
        this.jogadores.put(j.getEmail(), j);
    }
    
    /* verificação se um jogador se encontra registado no sistema */
    public Jogador checkUser(String username){
        Jogador jogador = null;
        if (this.jogadores.containsKey(username)){
            jogador = this.jogadores.get(username);
        }
        
        return jogador;
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
