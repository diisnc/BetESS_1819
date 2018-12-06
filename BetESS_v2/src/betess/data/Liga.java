package betess.data;

import java.io.Serializable;


public class Liga implements Serializable{
    
    private String nome;

    public Liga(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Liga clone(){
        return new Liga(this.getNome());
    }
}