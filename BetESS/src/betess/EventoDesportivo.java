/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package betess;

import java.io.Serializable;


public class EventoDesportivo implements Serializable{
    
    public int id_evento;
    public String equipa_casa;
    public String equipa_fora;
    public String estado;
    public boolean ganha_casa;
    public boolean ganha_fora;
    public boolean empate;
    public double odd_casa;
    public double odd_fora;
    public double odd_empate;
    /* COLOCAMOS A HORA DO JOGO OU NÃO VALE A PENA? */

    public EventoDesportivo(int id_evento, String equipa_casa, String equipa_fora, double odd_casa, double odd_fora, double empate) {
        this.id_evento = id_evento;
        this.equipa_casa = equipa_casa;
        this.equipa_fora = equipa_fora;
        this.estado = "Aberto";
        this.ganha_casa = false;
        this.ganha_fora = false;
        this.empate = false;
        this.odd_casa = odd_casa;
        this.odd_fora = odd_fora;
        this.odd_empate = empate;
    }

    /* construtor utilizado no clone */
    public EventoDesportivo(int id_evento, String equipa_casa, String equipa_fora, String estado, boolean ganha_casa, boolean ganha_fora, boolean empate, double odd_casa, double odd_fora, double odd_empate) {
        this.id_evento = id_evento;
        this.equipa_casa = equipa_casa;
        this.equipa_fora = equipa_fora;
        this.estado = estado;
        this.ganha_casa = ganha_casa;
        this.ganha_fora = ganha_fora;
        this.empate = empate;
        this.odd_casa = odd_casa;
        this.odd_fora = odd_fora;
        this.odd_empate = odd_empate;
    }
    
    
    
    /* GETTERS */

    public int getId_evento() {
        return id_evento;
    }

    public String getequipa_casa() {
        return equipa_casa;
    }

    public String getequipa_fora() {
        return equipa_fora;
    }

    public String getEstado() {
        return estado;
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

    public double getOdd_casa() {
        return odd_casa;
    }

    public double getOdd_fora() {
        return odd_fora;
    }

    public double getOdd_empate() {
        return odd_empate;
    }
    
    
    
    
    
    /* SETTERS */

    public void setId_equipa_casa(String equipa_casa) {
        this.equipa_casa = equipa_casa;
    }

    public void setId_equipa_fora(String equipa_fora) {
        this.equipa_fora = equipa_fora;
    }

    public void setEstado(String estado) {
        this.estado = estado;
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

    public void setOdd_casa(double odd_casa) {
        this.odd_casa = odd_casa;
    }

    public void setOdd_fora(double odd_fora) {
        this.odd_fora = odd_fora;
    }

    public void setOdd_empate(double odd_empate) {
        this.odd_empate = odd_empate;
    }
    
    public EventoDesportivo clone(){
        return new EventoDesportivo(this.getId_evento(), this.getequipa_casa(), this.getequipa_fora(),
                this.getEstado(), this.getGanha_casa(), this.getGanha_fora(), this.getEmpate(), this.getOdd_casa(),
                this.getOdd_fora(), this.getOdd_empate());
    }
    
}