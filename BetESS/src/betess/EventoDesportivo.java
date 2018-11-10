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
public class EventoDesportivo {
    
    public int id_evento;
    public int id_equipa_casa;
    public int id_equipa_fora;
    public String estado;
    public boolean ganha_casa;
    public boolean ganha_fora;
    public boolean empate;
    public double odd_casa;
    public double odd_fora;
    public double odd_empate;
    /* COLOCAMOS A HORA DO JOGO OU NÃO VALE A PENA? */

    public EventoDesportivo(int id_evento, int id_equipa_casa, int id_equipa_fora) {
        this.id_equipa_casa = id_equipa_casa;
        this.id_equipa_fora = id_equipa_fora;
        this.estado = "Aberto";
        this.ganha_casa = false;
        this.ganha_fora = false;
        this.empate = false;
    }
    
    /* GETTERS */

    public int getId_evento() {
        return id_evento;
    }

    public int getId_equipa_casa() {
        return id_equipa_casa;
    }

    public int getId_equipa_fora() {
        return id_equipa_fora;
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
    
    

    public void setId_equipa_casa(int id_equipa_casa) {
        this.id_equipa_casa = id_equipa_casa;
    }

    public void setId_equipa_fora(int id_equipa_fora) {
        this.id_equipa_fora = id_equipa_fora;
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
    
    
    
}