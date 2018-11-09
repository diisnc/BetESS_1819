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
public class Aposta {
    
    public double quantia;
    public String resultado;
    public int id_evento;
    public int id_jogador;
    public boolean ganha_casa;
    public boolean ganha_fora;
    public boolean empate;

    public Aposta(double quantia, String resultado, int id_evento, int id_jogador, boolean ganha_casa, boolean ganha_fora, boolean empate) {
        this.quantia = quantia;
        this.resultado = resultado;
        this.id_evento = id_evento;
        this.id_jogador = id_jogador;
        this.ganha_casa = ganha_casa;
        this.ganha_fora = ganha_fora;
        this.empate = empate;
    }

    /* GETTERS */
    public double getQuantia() {
        return quantia;
    }

    public String getResultado() {
        return resultado;
    }

    public int getId_evento() {
        return id_evento;
    }

    public int getId_jogador() {
        return id_jogador;
    }

    public boolean isGanha_casa() {
        return ganha_casa;
    }

    public boolean isGanha_fora() {
        return ganha_fora;
    }

    public boolean isEmpate() {
        return empate;
    }
    

    
    /* SETTERS */

    public void setQuantia(double quantia) {
        this.quantia = quantia;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public void setId_evento(int id_evento) {
        this.id_evento = id_evento;
    }

    public void setId_jogador(int id_jogador) {
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
    
    
    

}