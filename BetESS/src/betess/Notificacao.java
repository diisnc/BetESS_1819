/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package betess;

/**
 *
 *
 */
public class Notificacao {
    
    private int id_aposta;
    private double balanco;

    public Notificacao(int id_aposta, double balaco) {
        this.id_aposta = id_aposta;
        this.balanco = balanco;
    }
    
    public Notificacao clone(){
        return new Notificacao(this.getId_aposta(), this.getBalanco());
    }

    /* GETTERS */
    public int getId_aposta() {
        return id_aposta;
    }

    public double getBalanco() {
        return balanco;
    }
    
    /* SETTERS */

    public void setId_aposta(int id_aposta) {
        this.id_aposta = id_aposta;
    }

    public void setBalanco(double balanco) {
        this.balanco = balanco;
    }
}