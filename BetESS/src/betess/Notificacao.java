/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package betess;

import java.io.Serializable;

/**
 *
 *
 */
public class Notificacao implements Serializable{
    
    private int id_aposta;
    private double balanco;
    private String status;

    public Notificacao(int id_aposta, double balanco) {
        this.id_aposta = id_aposta;
        this.balanco = balanco;
        this.status = "Não Lida";
        
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

    public String getStatus() {
        return status;
    }
    
    
    /* SETTERS */

    public void setId_aposta(int id_aposta) {
        this.id_aposta = id_aposta;
    }

    public void setBalanco(double balanco) {
        this.balanco = balanco;
    }
    
    public void marcarLida(){
        this.status = "Lida";
    }
}