/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package betess;

/**
 *
 * @author MarcoSilva
 */
public class Jogador {
    public String email;
    public String nome;
    private String password;
    public String contacto;

    
    /* CONSTRUTOR */
    public Jogador(String email, String nome, String password, String contacto) {
        this.email = email;
        this.nome = nome;
        this.password = password;
        this.contacto = contacto;
    }
    
    /* GETTERS */

    public String getEmail() {
        return email;
    }

    public String getNome() {
        return nome;
    }
    
    public String getContacto() {
        return contacto;
    }
    
    /* SETTERS */

    public void setEmail(String email) {
        this.email = email;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public void setContacto(String contacto) {
        this.contacto = contacto;
    }
    
    
    /* método que permite a verificação da password sem que esta seja passada para o exterior */
    public boolean checkPassword(String password){
        return this.password.equals(password);
    }
    
    
}