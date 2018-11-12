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
public class Equipa {

    private int id_equipa;
    private int id_liga;
    private String designacao;

    public Equipa(int id_equipa, int id_liga, String designacao) {
        this.id_equipa = id_equipa;
        this.id_liga = id_liga;
        this.designacao = designacao;
    }
    
    public Equipa clone(){
        return new Equipa(this.getId_equipa(), this.getId_liga(), this.getDesignacao());
    }

    /* GETTERS */
    public int getId_equipa() {
        return id_equipa;
    }

    public int getId_liga() {
        return id_liga;
    }

    public String getDesignacao() {
        return designacao;
    }
    
    /* SETTERS */

    public void setId_equipa(int id_equipa) {
        this.id_equipa = id_equipa;
    }

    public void setId_liga(int id_liga) {
        this.id_liga = id_liga;
    }

    public void setDesignacao(String designacao) {
        this.designacao = designacao;
    }
    
    
}