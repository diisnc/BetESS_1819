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
    
    public int id_equipa_casa;
    public int id_equipa_fora;
    public String estado;
    /* COLOCAMOS A HORA DO JOGO OU NÃO VALE A PENA? */

    public EventoDesportivo(int id_equipa_casa, int id_equipa_fora) {
        this.id_equipa_casa = id_equipa_casa;
        this.id_equipa_fora = id_equipa_fora;
    }
    
    /* GETTERS */

    public int getId_equipa_casa() {
        return id_equipa_casa;
    }

    public int getId_equipa_fora() {
        return id_equipa_fora;
    }

    public String getEstado() {
        return estado;
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
    
    

}