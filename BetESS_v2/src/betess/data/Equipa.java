package betess.data;

import java.io.Serializable;


public class Equipa implements Serializable{

    private String id_liga;
    private String designacao;

    public Equipa(String id_liga, String designacao) {
        this.id_liga = id_liga;
        this.designacao = designacao;
    }
    
    public Equipa clone(){
        return new Equipa(this.getId_liga(), this.getDesignacao());
    }

    /* GETTERS */
    public String getId_liga() {
        return id_liga;
    }

    public String getDesignacao() {
        return designacao;
    }
    
    /* SETTERS */
    public void setId_liga(String id_liga) {
        this.id_liga = id_liga;
    }

    public void setDesignacao(String designacao) {
        this.designacao = designacao;
    }
    
    
}