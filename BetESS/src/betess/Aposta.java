/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package betess;

import java.io.Serializable;


public class Aposta implements Serializable{
    
    public int id_aposta;
    public double quantia;
    public int id_evento;
    public String id_jogador;
    public boolean ganha_casa;
    public boolean ganha_fora;
    public boolean empate;
    public String estado;

    public Aposta(int id_aposta, double quantia, int id_evento, String id_jogador, boolean ganha_casa, boolean ganha_fora, boolean empate) {
        this.id_aposta = id_aposta;
        this.quantia = quantia;
        this.id_evento = id_evento;
        this.id_jogador = id_jogador;
        this.ganha_casa = ganha_casa;
        this.ganha_fora = ganha_fora;
        this.empate = empate;
        this.estado = "Não paga";
    }

    public Aposta(int id_aposta, double quantia, int id_evento, String id_jogador, boolean ganha_casa, boolean ganha_fora, boolean empate, String estado) {
        this.id_aposta = id_aposta;
        this.quantia = quantia;
        this.id_evento = id_evento;
        this.id_jogador = id_jogador;
        this.ganha_casa = ganha_casa;
        this.ganha_fora = ganha_fora;
        this.empate = empate;
        this.estado = estado;
    }
    
    

    /* GETTERS */
    public int getId_aposta() {
        return id_aposta;
    }

    public double getQuantia() {
        return quantia;
    }

    public int getId_evento() {
        return id_evento;
    }

    public String getId_jogador() {
        return id_jogador;
    }

    public boolean getGanha_casa() {
        return ganha_casa;
    }

    public boolean getGanha_fora() {
        return ganha_fora;
    }

    public boolean getEmpate() {
        return empate;
    }

    public String getEstado() {
        return estado;
    }
    
    
    
    /* SETTERS */

    public void setId_aposta(int id_aposta) {
        this.id_aposta = id_aposta;
    }

    public void setQuantia(double quantia) {
        this.quantia = quantia;
    }

    public void setId_evento(int id_evento) {
        this.id_evento = id_evento;
    }

    public void setId_jogador(String id_jogador) {
        this.id_jogador = id_jogador;
    }

    public void setGanha_casa(boolean ganha_casa) {
        this.ganha_casa = ganha_casa;
    }

    public void setGanha_fora(boolean ganha_fora) {
        this.ganha_fora = ganha_fora;
    }

    public void setEmpate(boolean empate) {
        this.empate = empate;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
    
    public Aposta clone(){
        return new Aposta(this.getId_aposta(), this.getQuantia(), this.getId_evento(), this.getId_jogador(), this.getGanha_casa(), this.getGanha_fora(), this.getEmpate(), this.getEstado());
    }
}