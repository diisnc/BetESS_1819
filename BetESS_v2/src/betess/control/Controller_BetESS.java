package betess.control;

import betess.data.Aposta;
import betess.ui.AreaUI;
import betess.data.Equipa;
import betess.data.EventoDesportivo;
import betess.data.Jogador;
import betess.data.Liga;
import betess.ui.Login;
import betess.data.Model;
import betess.ui.Registo;
import java.io.*;
import java.util.HashMap;
import java.util.List;

public class Controller_BetESS implements Observer{
    
    /*
    * This is one of the CONCRETE OBSERVERS !
    */
    
    private Model model;
    private Login login;
    private Registo registo;
    private AreaUI areaUI;
    private String id_utilizador_aut;

 
    
    /* Construtor Controller_BetESS  */
    public Controller_BetESS(){
        Model m = null;
        try {
            FileInputStream fileIn = new FileInputStream("/tmp/database");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            m = (Model) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException i) {
            i.printStackTrace();
        } catch (ClassNotFoundException c) {
            System.out.println("Database class not found");
            c.printStackTrace();
        }
        if (m == null){
            System.out.println("Estado da aplicação iniciado.");
            this.model = new Model();
        }
        else {
            System.out.println("Restauro da aplicação com sucesso.");
            this.model = m;
        }
    }

    public void save() {
        try {
            FileOutputStream fileOut = new FileOutputStream("/tmp/database");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(this.model);
            out.close();
            fileOut.close();
            System.out.printf("Serialized data!");
        } catch (IOException i) {
              i.printStackTrace();
        }
    }
    
    @Override
    public void update(String arg) {
        
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public void setLogin(Login login) {
        this.login = login;
    }

    public void setRegisto(Registo registo) {
        this.registo = registo;
    }

    public void setAreaUI(AreaUI areaUI) {
        this.areaUI = areaUI;
    }
    
    /* Método que permite definir o jogador autenticado na sessao */
    public void setId_utilizador_aut(String id_utilizador_aut) {
        this.id_utilizador_aut = id_utilizador_aut;
    } 
    
    public String getId_utilizador_aut() {
        return id_utilizador_aut;
    }
    
    public HashMap<String, Jogador> getJogadores(){
        return this.model.getJogadores();
    }
 
    public List<Jogador> getJogadoresBloqueados(){
        return this.model.getJogadoresBloqueados();
    }
    
    public void eliminaJogador(String id){
        this.model.eliminaJogador(id);
    }
    
    public void bloqueiaJogador(String id){
        this.model.bloqueiaJogador(id);
    }
    
    public void desbloqueiaJogador(String id){
        this.model.desbloqueiaJogador(id);
    }
    
    public Aposta getAposta(int id_aposta){
        return this.model.getAposta(id_aposta);
    }
    
    public Equipa getEquipa(String id_equipa){
        return this.model.getEquipa(id_equipa);
    }
    
    public List<Equipa> getEquipas(){
        return this.model.getEquipas();
    }
    
    public void registaEquipa(Equipa e){
        this.model.registaEquipa(e);
    }
    
    /* inserção de um jogador no sistema */
    public void registaJogador(Jogador j){
        this.model.registaJogador(j);
    }
    
    public void registaAposta(double quantia, int id_evento, String id_jogador, boolean ganha_casa, boolean ganha_fora, boolean empate){
        this.model.registaAposta(quantia, id_evento, id_jogador, ganha_casa, ganha_fora, empate);
    }
    
    public void atualizaSaldo(double creditos, String id_jogador){
        this.model.atualizaSaldo(creditos, id_jogador);
    }
    
    /* verificação se um jogador se encontra registado no sistema */
    public Jogador checkUser(String username){
        return this.model.checkUser(username);
    }
    
    public EventoDesportivo getEventoDesportivo(int id){
        return this.model.getEventoDesportivo(id);
    }
    
    public void registaLiga(Liga l){
        this.model.registaLiga(l);
    }
    
    public List<Liga> getLigas(){
        return this.model.getLigas();
    }
    
    public void removeNotificacao(String id_utilizador, int id_aposta){
        this.model.removeNotificacao(id_utilizador, id_aposta);
    }
            
    public void registaEventoDesportivo(String equipa_casa, String equipa_fora, double odd_casa, double odd_fora, double odd_empate){
        this.model.registaEventoDesportivo(equipa_casa, equipa_fora, odd_casa, odd_fora, odd_empate);
    }
    
    public HashMap<Integer, EventoDesportivo> getEventosDesportivos(){
        return this.model.getEventosDesportivos();
    }
    
    public HashMap<Integer, Aposta> getApostas(){
        return this.model.getApostas();
    }
    
    public void removeAposta(int id){
        this.model.removeAposta(id);
    }
    
    public List<Aposta> getApostasJogador(String id_jogador){
        return this.model.getApostasJogador(id_jogador);
    }
    
    
    /* 
    * Método que tratará do fecho de um evento desportivo com o respetivo pagamento das apostas referentes a esse 
    * Estava aqui definido, passa para o Model a lógica toda !
    */
    public void fechaEvento(int id_Evento, boolean ganha_casa, boolean ganha_fora, boolean empate){
        this.model.fechaEvento(id_Evento, ganha_casa, ganha_fora, empate);
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Login l = new Login();
        l.setLocationRelativeTo(null);
        l.setVisible(true);
    }
    
     /*
     * @param args the command line arguments
     */
    /*public static void main(String[] args) {
        Login l = new Login();
        Registo r = new Registo();
        Model m = new Model();
        AreaUI a = new AreaUI();
        Controller_BetESS c = new Controller_BetESS();
        
        a.setMymodel(m);
        a.setMycontroller(c);
        
        c.setModel(m);
        c.setLogin(l);
        c.setRegisto(r);
        c.setAreaUI(a);
        
        l.setLocationRelativeTo(null);
        l.setVisible(true);
    }*/
}