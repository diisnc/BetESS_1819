package betess.ui;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import betess.data.Liga;
import betess.data.EventoDesportivo;
import betess.data.Aposta;
import betess.data.Jogador;
import betess.data.Equipa;
import betess.data.Notificacao;
import betess.data.Model;
import betess.control.Observer;
import betess.control.Controller_BetESS;
import betess.data.Bookie;
import betess.data.NotificacaoBookie;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class AreaUI extends javax.swing.JFrame implements Observer{
    
    private Model mymodel;
    private Controller_BetESS mycontroller;

    /**
     * Creates new form AreaUI
     * @param email
     */
    public AreaUI(String email, Controller_BetESS mycontroller, Model mymodel) {
        initComponents();
        
        this.mycontroller = mycontroller;
        this.mymodel = mymodel;
        
        /* remoção de paineis anteriores */
        area_administrador.setVisible(false);
        area_cliente.setVisible(false);
        area_bookie.setVisible(false);
        
        if (email.equals("admin")){
            area_administrador.setVisible(true);
        }
        else{
            if (email.equals ("bookie")){
                area_bookie.setVisible(true);
            }
            else{
            area_cliente.setVisible(true);
            }
        }
        /* registo da interface como observadora dos dados */
        this.mymodel.registerObserver(this);
    }

    public void setMymodel(Model mymodel) {
        this.mymodel = mymodel;
    }

    public void setMycontroller(Controller_BetESS mycontroller) {
        this.mycontroller = mycontroller;
    }
    
    @Override
    public void update(String arg) {
        if (arg.equals("jogadores")){
            DefaultTableModel model = (DefaultTableModel) jogadores_list.getModel();
            
            model.setRowCount(0);
            
            DecimalFormat dc = new DecimalFormat("0.00");
            
            for (Jogador j : this.mymodel.getJogadores().values()){
                model.addRow(new Object[]{j.getEmail(), j.getNome(), j.getContacto(), Double.parseDouble(dc.format(j.getSaldo()))});
            }
        }
        else if (arg.equals("jogadores_bloqueados")){
            DefaultTableModel model = (DefaultTableModel) jogadores_bloqueados_list.getModel();

            model.setRowCount(0);

            DecimalFormat dc = new DecimalFormat("0.00");

            for (Jogador j : this.mymodel.getJogadoresBloqueados()){
                model.addRow(new Object[]{j.getEmail(), j.getNome(), j.getContacto(), Double.parseDouble(dc.format(j.getSaldo()))});
            }
        }
        else if (arg.equals("eventos")){
            DefaultTableModel model = (DefaultTableModel) eventos_lista.getModel();
            DefaultTableModel model1 = (DefaultTableModel) events_list.getModel();

            model.setRowCount(0);
            model1.setRowCount(0);


            for (EventoDesportivo e : this.mymodel.getEventosDesportivos().values()){
                String equipa_casa = this.mycontroller.getEquipa(e.getequipa_casa()).getDesignacao();
                String equipa_fora = this.mycontroller.getEquipa(e.getequipa_fora()).getDesignacao();
                model.addRow(new Object[]{e.getId_evento(), equipa_casa, equipa_fora, e.getGanha_casa(), e.getGanha_fora(), e.getEmpate(), e.getEstado()});
                model1.addRow(new Object[]{e.getId_evento(), equipa_casa, equipa_fora, e.getOdd_casa(), e.getOdd_fora(), e.getOdd_empate()});
        
            }
        }
        else if (arg.equals("apostas")){
            DefaultTableModel model_admin = (DefaultTableModel) lista_apostas_admin.getModel();
            DefaultTableModel model = (DefaultTableModel) lista_apostas.getModel();
            

            model.setRowCount(0);
            model_admin.setRowCount(0);

            DecimalFormat dc = new DecimalFormat("0.00");

            for (Aposta a : this.mymodel.getApostas().values()){
                model_admin.addRow(new Object[]{a.getId_aposta(), a.getId_evento(), a.getId_jogador(), a.getGanha_casa(), a.getGanha_fora(), a.getEmpate(), Double.parseDouble(dc.format(a.getQuantia()))});
            }
            
            for (Aposta a : this.mymodel.getApostasJogador(this.mycontroller.getId_utilizador_aut())){
                EventoDesportivo e = this.mymodel.getEventoDesportivo(a.getId_evento());
                model.addRow(new Object[]{a.getId_aposta(), a.getId_evento(), e.getequipa_casa(), e.getequipa_fora(), a.getGanha_casa(), a.getGanha_fora(), a.getEmpate(), Double.parseDouble(dc.format(a.getQuantia())), a.getEstado()});
            }
        }
        else if (arg.equals("notificacoes")){
            Jogador j = this.mymodel.checkUser(this.mycontroller.getId_utilizador_aut());

            List<Notificacao> notificacoes = j.getNotificacoes();

            DefaultTableModel model = (DefaultTableModel) notificacoes_list.getModel();

            model.setRowCount(0);

            DecimalFormat dc = new DecimalFormat("0.00");

            for (Notificacao n : notificacoes){
                Aposta a = this.mymodel.getAposta(n.getId_aposta());
                EventoDesportivo e = this.mymodel.getEventoDesportivo(a.getId_evento());
                String equipa_casa = e.getequipa_casa();
                String equipa_fora = e.getequipa_fora();

                model.addRow(new Object[]{a.getId_evento(), a.getId_aposta(), equipa_casa, equipa_fora, Double.parseDouble(dc.format(a.getQuantia())), Double.parseDouble(dc.format(n.getBalanco())), n.getStatus()});
            }
        }
        else if (arg.equals("saldo")){
            DecimalFormat dc = new DecimalFormat("0.00");
            
            if (this.mymodel.checkUser(this.mycontroller.getId_utilizador_aut()) != null){
                saldo_field.setText(dc.format(this.mymodel.checkUser(this.mycontroller.getId_utilizador_aut()).getSaldo()));
            }
        }
        else if (arg.equals("equipas")){
            combo_casa.removeAllItems();
            combo_fora.removeAllItems();

            for (Equipa e : this.mymodel.getEquipas()){
                combo_casa.addItem(e.getDesignacao());
                combo_fora.addItem(e.getDesignacao());
            }
        }
        else if (arg.equals("ligas")){
            ligas_combo.removeAllItems();

            for (Liga l : this.mymodel.getLigas()){
                ligas_combo.addItem(l.getNome());
            }
        }
        else if(arg.equals("notificacoes_bookie")){
            Bookie bookie = this.mycontroller.getBookie();

            List<NotificacaoBookie> notificacoes = bookie.getNotificacoes();

            DefaultTableModel model = (DefaultTableModel) notificacoes_list_bookie.getModel();

            model.setRowCount(0);

            DecimalFormat dc = new DecimalFormat("0.00");

            for (NotificacaoBookie n : notificacoes){
                model.addRow(new Object[]{n.getIdEvento(), Double.parseDouble(dc.format(n.getGanhos())), Double.parseDouble(dc.format(n.getPerdas())), Double.parseDouble(dc.format(n.getBalanco()))});
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        opcoes_aposta_client = new javax.swing.ButtonGroup();
        opcoes_aposta_admin = new javax.swing.ButtonGroup();
        area_cliente = new javax.swing.JPanel();
        Welcome_label_client = new javax.swing.JLabel();
        buttons_panel_client = new javax.swing.JPanel();
        apostar_button = new javax.swing.JButton();
        ver_apostas_button = new javax.swing.JButton();
        creditos_button = new javax.swing.JButton();
        notificacoes_button = new javax.swing.JButton();
        editar_perfil_button = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        logout_button = new javax.swing.JButton();
        options_panel_client = new javax.swing.JPanel();
        apostar_elements_client = new javax.swing.JPanel();
        submit_aposta_button = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        quantia_field = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        ganhos_field = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        events_list = new javax.swing.JTable();
        casa_button = new javax.swing.JRadioButton();
        empate_button = new javax.swing.JRadioButton();
        fora_button = new javax.swing.JRadioButton();
        apostas_elements_client = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        lista_apostas_pane = new javax.swing.JScrollPane();
        lista_apostas = new javax.swing.JTable();
        cashout_button = new javax.swing.JButton();
        creditos_elements_client = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        saldo_field = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        creditos_field = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        creditar_button = new javax.swing.JButton();
        notificacoes_elements_client = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        notificacoes_list = new javax.swing.JTable();
        descartar_button = new javax.swing.JButton();
        edit_perfil_elements_client = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        nome_field = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        email_field = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        palavra_passe_field = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        contacto_field = new javax.swing.JTextField();
        edit_dados_button = new javax.swing.JButton();
        area_administrador = new javax.swing.JPanel();
        Welcome_label_admin = new javax.swing.JLabel();
        buttons_panel_admin = new javax.swing.JPanel();
        ver_jogadores_button = new javax.swing.JButton();
        eventos_desportivos_button = new javax.swing.JButton();
        jogadores_bloq_button = new javax.swing.JButton();
        apostas_button = new javax.swing.JButton();
        jLabel15 = new javax.swing.JLabel();
        nova_equipa_button = new javax.swing.JButton();
        nova_liga_button = new javax.swing.JButton();
        terminar_sessao_button = new javax.swing.JButton();
        options_panel_admin = new javax.swing.JPanel();
        ver_jogadores_elements_admin = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jogadores_list = new javax.swing.JTable();
        remover_jogador_button = new javax.swing.JButton();
        bloquear_jogador_button = new javax.swing.JButton();
        eventos_desportivos_elements_admin = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        eventos_lista = new javax.swing.JTable();
        fechar_evento_button = new javax.swing.JButton();
        registar_evento_button = new javax.swing.JButton();
        jogadores_bloqueados_elements_admin = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        desbloquear_jogador_button = new javax.swing.JButton();
        jScrollPane6 = new javax.swing.JScrollPane();
        jogadores_bloqueados_list = new javax.swing.JTable();
        apostas_elements_admin = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        lista_apostas_admin = new javax.swing.JTable();
        elimina_aposta = new javax.swing.JButton();
        novo_evento_elements_admin = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        combo_casa = new javax.swing.JComboBox<>();
        combo_fora = new javax.swing.JComboBox<>();
        regista_evento_button = new javax.swing.JButton();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        odd_casa_field = new javax.swing.JTextField();
        odd_fora_field = new javax.swing.JTextField();
        odd_empate_field = new javax.swing.JTextField();
        nova_equipa_elements_admin = new javax.swing.JPanel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        nome_equipa_field = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        ligas_combo = new javax.swing.JComboBox<>();
        regista_equipa_button = new javax.swing.JButton();
        nova_liga_elements_admin = new javax.swing.JPanel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        nome_liga_field = new javax.swing.JTextField();
        regista_liga_button = new javax.swing.JButton();
        fechar_evento_elements_admin = new javax.swing.JPanel();
        jLabel31 = new javax.swing.JLabel();
        evento_ganha_casa = new javax.swing.JRadioButton();
        evento_ganha_fora = new javax.swing.JRadioButton();
        evento_empate = new javax.swing.JRadioButton();
        fecha_evento_button = new javax.swing.JButton();
        area_bookie = new javax.swing.JPanel();
        Welcome_label_bookie = new javax.swing.JLabel();
        buttons_panel_bookie = new javax.swing.JPanel();
        eventos_desportivos_button1 = new javax.swing.JButton();
        jLabel32 = new javax.swing.JLabel();
        terminar_sessao_button1 = new javax.swing.JButton();
        notificacoes_button_bookie = new javax.swing.JButton();
        options_panel_bookie = new javax.swing.JPanel();
        eventos_desportivos_elements_admin1 = new javax.swing.JPanel();
        jLabel34 = new javax.swing.JLabel();
        jScrollPane9 = new javax.swing.JScrollPane();
        eventos_lista1 = new javax.swing.JTable();
        registar_evento_button1 = new javax.swing.JButton();
        novo_evento_elements_bookie = new javax.swing.JPanel();
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        combo_casa1 = new javax.swing.JComboBox<>();
        combo_fora1 = new javax.swing.JComboBox<>();
        regista_evento_button1 = new javax.swing.JButton();
        jLabel40 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        odd_casa_field1 = new javax.swing.JTextField();
        odd_fora_field1 = new javax.swing.JTextField();
        odd_empate_field1 = new javax.swing.JTextField();
        interesse_not = new javax.swing.JCheckBox();
        notificacoes_bookie = new javax.swing.JPanel();
        jLabel33 = new javax.swing.JLabel();
        jScrollPane8 = new javax.swing.JScrollPane();
        notificacoes_list_bookie = new javax.swing.JTable();
        descartar_button1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        Welcome_label_client.setText("Bem-vindo à BetESS");

        apostar_button.setText("Apostar");
        apostar_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                apostar_buttonActionPerformed(evt);
            }
        });

        ver_apostas_button.setText("Minhas apostas");
        ver_apostas_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ver_apostas_buttonActionPerformed(evt);
            }
        });

        creditos_button.setText("Créditos");
        creditos_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                creditos_buttonActionPerformed(evt);
            }
        });

        notificacoes_button.setText("Notificações");
        notificacoes_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                notificacoes_buttonActionPerformed(evt);
            }
        });

        editar_perfil_button.setText("Editar Perfil");
        editar_perfil_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editar_perfil_buttonActionPerformed(evt);
            }
        });

        jLabel1.setText("Menu:");

        logout_button.setText("Terminar sessão");
        logout_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logout_buttonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout buttons_panel_clientLayout = new javax.swing.GroupLayout(buttons_panel_client);
        buttons_panel_client.setLayout(buttons_panel_clientLayout);
        buttons_panel_clientLayout.setHorizontalGroup(
            buttons_panel_clientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(buttons_panel_clientLayout.createSequentialGroup()
                .addGroup(buttons_panel_clientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(buttons_panel_clientLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(buttons_panel_clientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(editar_perfil_button, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(apostar_button, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(creditos_button, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(notificacoes_button, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(ver_apostas_button, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(buttons_panel_clientLayout.createSequentialGroup()
                        .addGap(56, 56, 56)
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(buttons_panel_clientLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(logout_button, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)))
                .addContainerGap())
        );
        buttons_panel_clientLayout.setVerticalGroup(
            buttons_panel_clientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(buttons_panel_clientLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(apostar_button)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ver_apostas_button)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(creditos_button)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(notificacoes_button)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(editar_perfil_button)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(logout_button)
                .addContainerGap())
        );

        options_panel_client.setLayout(new java.awt.CardLayout());

        submit_aposta_button.setText("Apostar");
        submit_aposta_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                submit_aposta_buttonActionPerformed(evt);
            }
        });

        jLabel6.setText("Quantia:");

        quantia_field.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                quantia_fieldActionPerformed(evt);
            }
        });

        jLabel7.setText("€ (euros)");

        jLabel8.setText("Ganhos possíveis:");

        ganhos_field.setEditable(false);
        ganhos_field.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ganhos_fieldActionPerformed(evt);
            }
        });

        events_list.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Evento número", "Casa", "Fora", "Odd Casa", "Odd Fora", "Odd Empate"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(events_list);

        jScrollPane1.setViewportView(jScrollPane2);

        opcoes_aposta_client.add(casa_button);
        casa_button.setText("Casa");
        casa_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                casa_buttonActionPerformed(evt);
            }
        });

        opcoes_aposta_client.add(empate_button);
        empate_button.setText("Empate");
        empate_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                empate_buttonActionPerformed(evt);
            }
        });

        opcoes_aposta_client.add(fora_button);
        fora_button.setText("Fora");
        fora_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fora_buttonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout apostar_elements_clientLayout = new javax.swing.GroupLayout(apostar_elements_client);
        apostar_elements_client.setLayout(apostar_elements_clientLayout);
        apostar_elements_clientLayout.setHorizontalGroup(
            apostar_elements_clientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(apostar_elements_clientLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
            .addGroup(apostar_elements_clientLayout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(apostar_elements_clientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(apostar_elements_clientLayout.createSequentialGroup()
                        .addComponent(casa_button)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fora_button)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(empate_button)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(apostar_elements_clientLayout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(quantia_field, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel7)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ganhos_field, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 184, Short.MAX_VALUE)
                        .addComponent(submit_aposta_button)
                        .addGap(36, 36, 36))))
        );
        apostar_elements_clientLayout.setVerticalGroup(
            apostar_elements_clientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(apostar_elements_clientLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(apostar_elements_clientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(quantia_field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(submit_aposta_button)
                    .addComponent(jLabel8)
                    .addComponent(ganhos_field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(apostar_elements_clientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(casa_button)
                    .addComponent(fora_button)
                    .addComponent(empate_button))
                .addContainerGap(46, Short.MAX_VALUE))
        );

        options_panel_client.add(apostar_elements_client, "card2");

        jLabel13.setText("Minhas apostas");

        lista_apostas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Identificador aposta", "Identificador evento", "Equipa Casa", "Equipa Fora", "Vitória Casa", "Vitória Fora", "Empate", "Quantia", "Estado"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Double.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        lista_apostas_pane.setViewportView(lista_apostas);

        cashout_button.setText("Cashout");
        cashout_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cashout_buttonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout apostas_elements_clientLayout = new javax.swing.GroupLayout(apostas_elements_client);
        apostas_elements_client.setLayout(apostas_elements_clientLayout);
        apostas_elements_clientLayout.setHorizontalGroup(
            apostas_elements_clientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(apostas_elements_clientLayout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(apostas_elements_clientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(cashout_button)
                    .addGroup(apostas_elements_clientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(lista_apostas_pane, javax.swing.GroupLayout.PREFERRED_SIZE, 623, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel13)))
                .addContainerGap(90, Short.MAX_VALUE))
        );
        apostas_elements_clientLayout.setVerticalGroup(
            apostas_elements_clientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(apostas_elements_clientLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jLabel13)
                .addGap(18, 18, 18)
                .addComponent(lista_apostas_pane, javax.swing.GroupLayout.PREFERRED_SIZE, 293, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(cashout_button)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        options_panel_client.add(apostas_elements_client, "card2");

        jLabel9.setText("Saldo Atual:");

        saldo_field.setEditable(false);
        saldo_field.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saldo_fieldActionPerformed(evt);
            }
        });

        jLabel10.setText("€ (euros)");

        jLabel11.setText("Creditar");

        jLabel12.setText("€ (euros)");

        creditar_button.setText("Creditar");
        creditar_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                creditar_buttonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout creditos_elements_clientLayout = new javax.swing.GroupLayout(creditos_elements_client);
        creditos_elements_client.setLayout(creditos_elements_clientLayout);
        creditos_elements_clientLayout.setHorizontalGroup(
            creditos_elements_clientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(creditos_elements_clientLayout.createSequentialGroup()
                .addGap(57, 57, 57)
                .addGroup(creditos_elements_clientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(creditos_elements_clientLayout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(creditos_field, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel12))
                    .addGroup(creditos_elements_clientLayout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(saldo_field, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel10)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, creditos_elements_clientLayout.createSequentialGroup()
                .addContainerGap(395, Short.MAX_VALUE)
                .addComponent(creditar_button)
                .addGap(275, 275, 275))
        );
        creditos_elements_clientLayout.setVerticalGroup(
            creditos_elements_clientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(creditos_elements_clientLayout.createSequentialGroup()
                .addGap(77, 77, 77)
                .addGroup(creditos_elements_clientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(saldo_field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10))
                .addGap(57, 57, 57)
                .addGroup(creditos_elements_clientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(creditos_field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12))
                .addGap(18, 18, 18)
                .addComponent(creditar_button)
                .addContainerGap(176, Short.MAX_VALUE))
        );

        options_panel_client.add(creditos_elements_client, "card2");

        jLabel14.setText("Notificações:");

        notificacoes_list.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Identificador Evento", "Identificador Aposta", "Equipa Casa", "Equipa Fora", "Quantia aposta", "Balanço", "Estado"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Double.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(notificacoes_list);

        descartar_button.setText("Descartar");
        descartar_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                descartar_buttonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout notificacoes_elements_clientLayout = new javax.swing.GroupLayout(notificacoes_elements_client);
        notificacoes_elements_client.setLayout(notificacoes_elements_clientLayout);
        notificacoes_elements_clientLayout.setHorizontalGroup(
            notificacoes_elements_clientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(notificacoes_elements_clientLayout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(notificacoes_elements_clientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(descartar_button)
                    .addGroup(notificacoes_elements_clientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel14)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 631, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(87, Short.MAX_VALUE))
        );
        notificacoes_elements_clientLayout.setVerticalGroup(
            notificacoes_elements_clientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(notificacoes_elements_clientLayout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(jLabel14)
                .addGap(28, 28, 28)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(descartar_button)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        options_panel_client.add(notificacoes_elements_client, "card2");

        jLabel2.setText("Nome:");

        nome_field.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nome_fieldActionPerformed(evt);
            }
        });

        jLabel3.setText("Email:");

        email_field.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                email_fieldActionPerformed(evt);
            }
        });

        jLabel4.setText("Palavra-Passe:");

        palavra_passe_field.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                palavra_passe_fieldActionPerformed(evt);
            }
        });

        jLabel5.setText("Contacto:");

        contacto_field.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                contacto_fieldActionPerformed(evt);
            }
        });

        edit_dados_button.setText("Editar Dados");
        edit_dados_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                edit_dados_buttonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout edit_perfil_elements_clientLayout = new javax.swing.GroupLayout(edit_perfil_elements_client);
        edit_perfil_elements_client.setLayout(edit_perfil_elements_clientLayout);
        edit_perfil_elements_clientLayout.setHorizontalGroup(
            edit_perfil_elements_clientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(edit_perfil_elements_clientLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(edit_perfil_elements_clientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(edit_perfil_elements_clientLayout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(contacto_field, javax.swing.GroupLayout.DEFAULT_SIZE, 652, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(edit_perfil_elements_clientLayout.createSequentialGroup()
                        .addGroup(edit_perfil_elements_clientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(edit_perfil_elements_clientLayout.createSequentialGroup()
                                .addGroup(edit_perfil_elements_clientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel2))
                                .addGap(28, 28, 28)
                                .addGroup(edit_perfil_elements_clientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(email_field)
                                    .addComponent(nome_field)))
                            .addGroup(edit_perfil_elements_clientLayout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(palavra_passe_field)))
                        .addGap(6, 6, 6))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, edit_perfil_elements_clientLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(edit_dados_button)
                .addContainerGap())
        );
        edit_perfil_elements_clientLayout.setVerticalGroup(
            edit_perfil_elements_clientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(edit_perfil_elements_clientLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(edit_perfil_elements_clientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(nome_field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(edit_perfil_elements_clientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(email_field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(edit_perfil_elements_clientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(palavra_passe_field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(edit_perfil_elements_clientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(contacto_field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(46, 46, 46)
                .addComponent(edit_dados_button)
                .addContainerGap(200, Short.MAX_VALUE))
        );

        options_panel_client.add(edit_perfil_elements_client, "card2");

        javax.swing.GroupLayout area_clienteLayout = new javax.swing.GroupLayout(area_cliente);
        area_cliente.setLayout(area_clienteLayout);
        area_clienteLayout.setHorizontalGroup(
            area_clienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(area_clienteLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(buttons_panel_client, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(area_clienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(options_panel_client, javax.swing.GroupLayout.PREFERRED_SIZE, 750, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Welcome_label_client))
                .addContainerGap(17, Short.MAX_VALUE))
        );
        area_clienteLayout.setVerticalGroup(
            area_clienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, area_clienteLayout.createSequentialGroup()
                .addGroup(area_clienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(area_clienteLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(buttons_panel_client, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, area_clienteLayout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(Welcome_label_client)
                        .addGap(18, 18, 18)
                        .addComponent(options_panel_client, javax.swing.GroupLayout.PREFERRED_SIZE, 396, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 5, Short.MAX_VALUE)))
                .addContainerGap())
        );

        Welcome_label_admin.setText("Administração do Sistema");

        ver_jogadores_button.setText("Ver Jogadores");
        ver_jogadores_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ver_jogadores_buttonActionPerformed(evt);
            }
        });

        eventos_desportivos_button.setText("Eventos Desportivos");
        eventos_desportivos_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eventos_desportivos_buttonActionPerformed(evt);
            }
        });

        jogadores_bloq_button.setText("Jogadores Bloqueados");
        jogadores_bloq_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jogadores_bloq_buttonActionPerformed(evt);
            }
        });

        apostas_button.setText("Apostas");
        apostas_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                apostas_buttonActionPerformed(evt);
            }
        });

        jLabel15.setText("Menu:");

        nova_equipa_button.setText("Nova Equipa");
        nova_equipa_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nova_equipa_buttonActionPerformed(evt);
            }
        });

        nova_liga_button.setText("Nova Liga");
        nova_liga_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nova_liga_buttonActionPerformed(evt);
            }
        });

        terminar_sessao_button.setText("Terminar Sessão");
        terminar_sessao_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                terminar_sessao_buttonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout buttons_panel_adminLayout = new javax.swing.GroupLayout(buttons_panel_admin);
        buttons_panel_admin.setLayout(buttons_panel_adminLayout);
        buttons_panel_adminLayout.setHorizontalGroup(
            buttons_panel_adminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, buttons_panel_adminLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel15)
                .addGap(69, 69, 69))
            .addGroup(buttons_panel_adminLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(buttons_panel_adminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ver_jogadores_button, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jogadores_bloq_button, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(apostas_button, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(eventos_desportivos_button, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(nova_equipa_button, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(nova_liga_button, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(terminar_sessao_button, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        buttons_panel_adminLayout.setVerticalGroup(
            buttons_panel_adminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(buttons_panel_adminLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel15)
                .addGap(18, 18, 18)
                .addComponent(ver_jogadores_button)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(eventos_desportivos_button)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jogadores_bloq_button)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(apostas_button)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nova_equipa_button)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nova_liga_button)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 135, Short.MAX_VALUE)
                .addComponent(terminar_sessao_button)
                .addContainerGap())
        );

        options_panel_admin.setLayout(new java.awt.CardLayout());

        jLabel16.setText("Lista de Jogadores registados no sistema");

        jogadores_list.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Email", "Nome", "Contacto", "Saldo"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane4.setViewportView(jogadores_list);

        remover_jogador_button.setText("Remover");
        remover_jogador_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                remover_jogador_buttonActionPerformed(evt);
            }
        });

        bloquear_jogador_button.setText("Bloquear");
        bloquear_jogador_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bloquear_jogador_buttonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout ver_jogadores_elements_adminLayout = new javax.swing.GroupLayout(ver_jogadores_elements_admin);
        ver_jogadores_elements_admin.setLayout(ver_jogadores_elements_adminLayout);
        ver_jogadores_elements_adminLayout.setHorizontalGroup(
            ver_jogadores_elements_adminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ver_jogadores_elements_adminLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(ver_jogadores_elements_adminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel16)
                    .addGroup(ver_jogadores_elements_adminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(ver_jogadores_elements_adminLayout.createSequentialGroup()
                            .addComponent(bloquear_jogador_button)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(remover_jogador_button))
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 636, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        ver_jogadores_elements_adminLayout.setVerticalGroup(
            ver_jogadores_elements_adminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ver_jogadores_elements_adminLayout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jLabel16)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(ver_jogadores_elements_adminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(remover_jogador_button)
                    .addComponent(bloquear_jogador_button))
                .addContainerGap(9, Short.MAX_VALUE))
        );

        options_panel_admin.add(ver_jogadores_elements_admin, "card2");

        jLabel17.setText("Eventos Desportivos");

        eventos_lista.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Identificador", "Equipa Casa", "Equipa Fora", "Ganha Casa", "Ganha Fora", "Empate", "Estado"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane5.setViewportView(eventos_lista);

        fechar_evento_button.setText("Fechar Evento");
        fechar_evento_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fechar_evento_buttonActionPerformed(evt);
            }
        });

        registar_evento_button.setText("Registar Evento Desportivo");
        registar_evento_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                registar_evento_buttonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout eventos_desportivos_elements_adminLayout = new javax.swing.GroupLayout(eventos_desportivos_elements_admin);
        eventos_desportivos_elements_admin.setLayout(eventos_desportivos_elements_adminLayout);
        eventos_desportivos_elements_adminLayout.setHorizontalGroup(
            eventos_desportivos_elements_adminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(eventos_desportivos_elements_adminLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(eventos_desportivos_elements_adminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(eventos_desportivos_elements_adminLayout.createSequentialGroup()
                        .addComponent(registar_evento_button)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(fechar_evento_button))
                    .addGroup(eventos_desportivos_elements_adminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 645, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel17)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        eventos_desportivos_elements_adminLayout.setVerticalGroup(
            eventos_desportivos_elements_adminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(eventos_desportivos_elements_adminLayout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jLabel17)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 288, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(eventos_desportivos_elements_adminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fechar_evento_button)
                    .addComponent(registar_evento_button))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        options_panel_admin.add(eventos_desportivos_elements_admin, "card2");

        jLabel18.setText("Jogadores Bloqueados na Plataforma");

        desbloquear_jogador_button.setText("Desbloquear Jogador");
        desbloquear_jogador_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                desbloquear_jogador_buttonActionPerformed(evt);
            }
        });

        jogadores_bloqueados_list.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Email", "Nome", "Contacto", "Saldo"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane6.setViewportView(jogadores_bloqueados_list);

        javax.swing.GroupLayout jogadores_bloqueados_elements_adminLayout = new javax.swing.GroupLayout(jogadores_bloqueados_elements_admin);
        jogadores_bloqueados_elements_admin.setLayout(jogadores_bloqueados_elements_adminLayout);
        jogadores_bloqueados_elements_adminLayout.setHorizontalGroup(
            jogadores_bloqueados_elements_adminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jogadores_bloqueados_elements_adminLayout.createSequentialGroup()
                .addGroup(jogadores_bloqueados_elements_adminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jogadores_bloqueados_elements_adminLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 638, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jogadores_bloqueados_elements_adminLayout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addGroup(jogadores_bloqueados_elements_adminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(desbloquear_jogador_button)
                            .addGroup(jogadores_bloqueados_elements_adminLayout.createSequentialGroup()
                                .addComponent(jLabel18)
                                .addGap(410, 410, 410)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jogadores_bloqueados_elements_adminLayout.setVerticalGroup(
            jogadores_bloqueados_elements_adminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jogadores_bloqueados_elements_adminLayout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(jLabel18)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 287, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(desbloquear_jogador_button)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        options_panel_admin.add(jogadores_bloqueados_elements_admin, "card2");

        jLabel19.setText("Apostas");

        lista_apostas_admin.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Identificador", "Identificador Evento", "Identificador Jogador", "Ganha Casa", "Ganha Fora", "Empate", "Quantia"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane7.setViewportView(lista_apostas_admin);

        elimina_aposta.setText("Eliminar");
        elimina_aposta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                elimina_apostaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout apostas_elements_adminLayout = new javax.swing.GroupLayout(apostas_elements_admin);
        apostas_elements_admin.setLayout(apostas_elements_adminLayout);
        apostas_elements_adminLayout.setHorizontalGroup(
            apostas_elements_adminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(apostas_elements_adminLayout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(apostas_elements_adminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(elimina_aposta)
                    .addGroup(apostas_elements_adminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 630, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel19)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        apostas_elements_adminLayout.setVerticalGroup(
            apostas_elements_adminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(apostas_elements_adminLayout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(jLabel19)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(elimina_aposta)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        options_panel_admin.add(apostas_elements_admin, "card2");

        jLabel20.setText("Novo Evento Desportivo");

        jLabel21.setText("Equipa Casa:");

        jLabel22.setText("Equipa Fora:");

        combo_casa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                combo_casaActionPerformed(evt);
            }
        });

        regista_evento_button.setText("Registar Evento");
        regista_evento_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                regista_evento_buttonActionPerformed(evt);
            }
        });

        jLabel23.setText("Odd Casa:");

        jLabel24.setText("Odd Fora:");

        jLabel25.setText("Odd Empate:");

        javax.swing.GroupLayout novo_evento_elements_adminLayout = new javax.swing.GroupLayout(novo_evento_elements_admin);
        novo_evento_elements_admin.setLayout(novo_evento_elements_adminLayout);
        novo_evento_elements_adminLayout.setHorizontalGroup(
            novo_evento_elements_adminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, novo_evento_elements_adminLayout.createSequentialGroup()
                .addContainerGap(499, Short.MAX_VALUE)
                .addComponent(regista_evento_button)
                .addGap(124, 124, 124))
            .addGroup(novo_evento_elements_adminLayout.createSequentialGroup()
                .addGroup(novo_evento_elements_adminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(novo_evento_elements_adminLayout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addComponent(jLabel20))
                    .addGroup(novo_evento_elements_adminLayout.createSequentialGroup()
                        .addGap(74, 74, 74)
                        .addGroup(novo_evento_elements_adminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(novo_evento_elements_adminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel21)
                                .addComponent(jLabel22))
                            .addGroup(novo_evento_elements_adminLayout.createSequentialGroup()
                                .addGroup(novo_evento_elements_adminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel25)
                                    .addComponent(jLabel24, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel23, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addGap(3, 3, 3)))
                        .addGroup(novo_evento_elements_adminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(combo_casa, 0, 222, Short.MAX_VALUE)
                            .addComponent(combo_fora, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(odd_empate_field, javax.swing.GroupLayout.DEFAULT_SIZE, 222, Short.MAX_VALUE)
                            .addComponent(odd_casa_field)
                            .addComponent(odd_fora_field))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        novo_evento_elements_adminLayout.setVerticalGroup(
            novo_evento_elements_adminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(novo_evento_elements_adminLayout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addComponent(jLabel20)
                .addGap(41, 41, 41)
                .addGroup(novo_evento_elements_adminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21)
                    .addComponent(combo_casa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(novo_evento_elements_adminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(combo_fora, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel22))
                .addGap(18, 18, 18)
                .addGroup(novo_evento_elements_adminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23)
                    .addComponent(odd_casa_field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(novo_evento_elements_adminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24)
                    .addComponent(odd_fora_field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(novo_evento_elements_adminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel25)
                    .addComponent(odd_empate_field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(regista_evento_button)
                .addContainerGap(55, Short.MAX_VALUE))
        );

        options_panel_admin.add(novo_evento_elements_admin, "card2");

        jLabel26.setText("Nova Equipa");

        jLabel27.setText("Nome da equipa:");

        jLabel28.setText("Liga:");

        ligas_combo.setToolTipText("");

        regista_equipa_button.setText("Registar Equipa");
        regista_equipa_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                regista_equipa_buttonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout nova_equipa_elements_adminLayout = new javax.swing.GroupLayout(nova_equipa_elements_admin);
        nova_equipa_elements_admin.setLayout(nova_equipa_elements_adminLayout);
        nova_equipa_elements_adminLayout.setHorizontalGroup(
            nova_equipa_elements_adminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(nova_equipa_elements_adminLayout.createSequentialGroup()
                .addGroup(nova_equipa_elements_adminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(nova_equipa_elements_adminLayout.createSequentialGroup()
                        .addGap(61, 61, 61)
                        .addComponent(jLabel26))
                    .addGroup(nova_equipa_elements_adminLayout.createSequentialGroup()
                        .addGap(83, 83, 83)
                        .addGroup(nova_equipa_elements_adminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(regista_equipa_button)
                            .addGroup(nova_equipa_elements_adminLayout.createSequentialGroup()
                                .addGroup(nova_equipa_elements_adminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel28)
                                    .addComponent(jLabel27))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(nova_equipa_elements_adminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(nome_equipa_field, javax.swing.GroupLayout.PREFERRED_SIZE, 388, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(ligas_combo, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addContainerGap(171, Short.MAX_VALUE))
        );
        nova_equipa_elements_adminLayout.setVerticalGroup(
            nova_equipa_elements_adminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(nova_equipa_elements_adminLayout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(jLabel26)
                .addGap(30, 30, 30)
                .addGroup(nova_equipa_elements_adminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel27)
                    .addComponent(nome_equipa_field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(nova_equipa_elements_adminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel28)
                    .addComponent(ligas_combo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(38, 38, 38)
                .addComponent(regista_equipa_button)
                .addContainerGap(181, Short.MAX_VALUE))
        );

        options_panel_admin.add(nova_equipa_elements_admin, "card2");

        jLabel29.setText("Nova Liga");

        jLabel30.setText("Nome da Liga:");

        regista_liga_button.setText("Registar Liga");
        regista_liga_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                regista_liga_buttonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout nova_liga_elements_adminLayout = new javax.swing.GroupLayout(nova_liga_elements_admin);
        nova_liga_elements_admin.setLayout(nova_liga_elements_adminLayout);
        nova_liga_elements_adminLayout.setHorizontalGroup(
            nova_liga_elements_adminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(nova_liga_elements_adminLayout.createSequentialGroup()
                .addGroup(nova_liga_elements_adminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(nova_liga_elements_adminLayout.createSequentialGroup()
                        .addGap(83, 83, 83)
                        .addGroup(nova_liga_elements_adminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(regista_liga_button)
                            .addGroup(nova_liga_elements_adminLayout.createSequentialGroup()
                                .addComponent(jLabel30)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(nome_liga_field, javax.swing.GroupLayout.PREFERRED_SIZE, 388, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(nova_liga_elements_adminLayout.createSequentialGroup()
                        .addGap(51, 51, 51)
                        .addComponent(jLabel29)))
                .addContainerGap(185, Short.MAX_VALUE))
        );
        nova_liga_elements_adminLayout.setVerticalGroup(
            nova_liga_elements_adminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(nova_liga_elements_adminLayout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(jLabel29)
                .addGap(28, 28, 28)
                .addGroup(nova_liga_elements_adminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel30)
                    .addComponent(nome_liga_field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(40, 40, 40)
                .addComponent(regista_liga_button)
                .addContainerGap(217, Short.MAX_VALUE))
        );

        options_panel_admin.add(nova_liga_elements_admin, "card2");

        jLabel31.setText("Resultado do Evento:");

        opcoes_aposta_admin.add(evento_ganha_casa);
        evento_ganha_casa.setText("Ganha Casa");

        opcoes_aposta_admin.add(evento_ganha_fora);
        evento_ganha_fora.setText("Ganha Fora");

        opcoes_aposta_admin.add(evento_empate);
        evento_empate.setText("Empate");

        fecha_evento_button.setText("Fechar");
        fecha_evento_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fecha_evento_buttonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout fechar_evento_elements_adminLayout = new javax.swing.GroupLayout(fechar_evento_elements_admin);
        fechar_evento_elements_admin.setLayout(fechar_evento_elements_adminLayout);
        fechar_evento_elements_adminLayout.setHorizontalGroup(
            fechar_evento_elements_adminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(fechar_evento_elements_adminLayout.createSequentialGroup()
                .addGroup(fechar_evento_elements_adminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(fechar_evento_elements_adminLayout.createSequentialGroup()
                        .addGap(117, 117, 117)
                        .addComponent(evento_ganha_casa)
                        .addGap(18, 18, 18)
                        .addComponent(evento_ganha_fora)
                        .addGap(18, 18, 18)
                        .addGroup(fechar_evento_elements_adminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(fecha_evento_button)
                            .addComponent(evento_empate)))
                    .addGroup(fechar_evento_elements_adminLayout.createSequentialGroup()
                        .addGap(47, 47, 47)
                        .addComponent(jLabel31)))
                .addContainerGap(291, Short.MAX_VALUE))
        );
        fechar_evento_elements_adminLayout.setVerticalGroup(
            fechar_evento_elements_adminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(fechar_evento_elements_adminLayout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addComponent(jLabel31)
                .addGap(43, 43, 43)
                .addGroup(fechar_evento_elements_adminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(evento_ganha_casa)
                    .addComponent(evento_ganha_fora)
                    .addComponent(evento_empate))
                .addGap(44, 44, 44)
                .addComponent(fecha_evento_button)
                .addContainerGap(196, Short.MAX_VALUE))
        );

        options_panel_admin.add(fechar_evento_elements_admin, "card2");

        javax.swing.GroupLayout area_administradorLayout = new javax.swing.GroupLayout(area_administrador);
        area_administrador.setLayout(area_administradorLayout);
        area_administradorLayout.setHorizontalGroup(
            area_administradorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(area_administradorLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(buttons_panel_admin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(area_administradorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(options_panel_admin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(area_administradorLayout.createSequentialGroup()
                        .addComponent(Welcome_label_admin)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        area_administradorLayout.setVerticalGroup(
            area_administradorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, area_administradorLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(area_administradorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(area_administradorLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(Welcome_label_admin)
                        .addGap(18, 18, 18)
                        .addComponent(options_panel_admin, javax.swing.GroupLayout.PREFERRED_SIZE, 391, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(buttons_panel_admin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        Welcome_label_bookie.setText("Bem vindo à BetESS, Bookie!");

        eventos_desportivos_button1.setText("Eventos Desportivos");
        eventos_desportivos_button1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eventos_desportivos_button1ActionPerformed(evt);
            }
        });

        jLabel32.setText("Menu:");

        terminar_sessao_button1.setText("Terminar Sessão");
        terminar_sessao_button1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                terminar_sessao_button1ActionPerformed(evt);
            }
        });

        notificacoes_button_bookie.setText("Notificações");
        notificacoes_button_bookie.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                notificacoes_button_bookieActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout buttons_panel_bookieLayout = new javax.swing.GroupLayout(buttons_panel_bookie);
        buttons_panel_bookie.setLayout(buttons_panel_bookieLayout);
        buttons_panel_bookieLayout.setHorizontalGroup(
            buttons_panel_bookieLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, buttons_panel_bookieLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel32)
                .addGap(69, 69, 69))
            .addGroup(buttons_panel_bookieLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(buttons_panel_bookieLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(eventos_desportivos_button1, javax.swing.GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE)
                    .addComponent(terminar_sessao_button1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(notificacoes_button_bookie, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        buttons_panel_bookieLayout.setVerticalGroup(
            buttons_panel_bookieLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(buttons_panel_bookieLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel32)
                .addGap(56, 56, 56)
                .addComponent(eventos_desportivos_button1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(notificacoes_button_bookie)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 233, Short.MAX_VALUE)
                .addComponent(terminar_sessao_button1)
                .addGap(22, 22, 22))
        );

        options_panel_bookie.setLayout(new java.awt.CardLayout());

        jLabel34.setText("Eventos Desportivos");

        eventos_lista1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Identificador", "Equipa Casa", "Equipa Fora", "Ganha Casa", "Ganha Fora", "Empate", "Estado"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane9.setViewportView(eventos_lista1);

        registar_evento_button1.setText("Registar Evento Desportivo");
        registar_evento_button1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                registar_evento_button1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout eventos_desportivos_elements_admin1Layout = new javax.swing.GroupLayout(eventos_desportivos_elements_admin1);
        eventos_desportivos_elements_admin1.setLayout(eventos_desportivos_elements_admin1Layout);
        eventos_desportivos_elements_admin1Layout.setHorizontalGroup(
            eventos_desportivos_elements_admin1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(eventos_desportivos_elements_admin1Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(eventos_desportivos_elements_admin1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(registar_evento_button1, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(eventos_desportivos_elements_admin1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 645, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel34)))
                .addContainerGap(83, Short.MAX_VALUE))
        );
        eventos_desportivos_elements_admin1Layout.setVerticalGroup(
            eventos_desportivos_elements_admin1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(eventos_desportivos_elements_admin1Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jLabel34)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 288, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(registar_evento_button1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        options_panel_bookie.add(eventos_desportivos_elements_admin1, "card2");

        jLabel37.setText("Novo Evento Desportivo");

        jLabel38.setText("Equipa Casa:");

        jLabel39.setText("Equipa Fora:");

        combo_casa1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                combo_casa1ActionPerformed(evt);
            }
        });

        regista_evento_button1.setText("Registar Evento");
        regista_evento_button1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                regista_evento_button1ActionPerformed(evt);
            }
        });

        jLabel40.setText("Odd Casa:");

        jLabel41.setText("Odd Fora:");

        jLabel42.setText("Odd Empate:");

        odd_casa_field1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                odd_casa_field1ActionPerformed(evt);
            }
        });

        interesse_not.setText("Notificações");
        interesse_not.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                interesse_notActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout novo_evento_elements_bookieLayout = new javax.swing.GroupLayout(novo_evento_elements_bookie);
        novo_evento_elements_bookie.setLayout(novo_evento_elements_bookieLayout);
        novo_evento_elements_bookieLayout.setHorizontalGroup(
            novo_evento_elements_bookieLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, novo_evento_elements_bookieLayout.createSequentialGroup()
                .addContainerGap(492, Short.MAX_VALUE)
                .addComponent(regista_evento_button1)
                .addGap(124, 124, 124))
            .addGroup(novo_evento_elements_bookieLayout.createSequentialGroup()
                .addGroup(novo_evento_elements_bookieLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(novo_evento_elements_bookieLayout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addComponent(jLabel37))
                    .addGroup(novo_evento_elements_bookieLayout.createSequentialGroup()
                        .addGap(74, 74, 74)
                        .addGroup(novo_evento_elements_bookieLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(novo_evento_elements_bookieLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel38)
                                .addComponent(jLabel39))
                            .addGroup(novo_evento_elements_bookieLayout.createSequentialGroup()
                                .addGroup(novo_evento_elements_bookieLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel42)
                                    .addComponent(jLabel41, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel40, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addGap(3, 3, 3)))
                        .addGroup(novo_evento_elements_bookieLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(combo_casa1, 0, 222, Short.MAX_VALUE)
                            .addComponent(combo_fora1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(odd_empate_field1, javax.swing.GroupLayout.DEFAULT_SIZE, 222, Short.MAX_VALUE)
                            .addComponent(odd_casa_field1)
                            .addComponent(odd_fora_field1))
                        .addGap(76, 76, 76)
                        .addComponent(interesse_not)))
                .addContainerGap(185, Short.MAX_VALUE))
        );
        novo_evento_elements_bookieLayout.setVerticalGroup(
            novo_evento_elements_bookieLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(novo_evento_elements_bookieLayout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addComponent(jLabel37)
                .addGap(41, 41, 41)
                .addGroup(novo_evento_elements_bookieLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel38)
                    .addComponent(combo_casa1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(interesse_not))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(novo_evento_elements_bookieLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(combo_fora1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel39))
                .addGap(18, 18, 18)
                .addGroup(novo_evento_elements_bookieLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel40)
                    .addComponent(odd_casa_field1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(novo_evento_elements_bookieLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel41)
                    .addComponent(odd_fora_field1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(novo_evento_elements_bookieLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel42)
                    .addComponent(odd_empate_field1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(regista_evento_button1)
                .addContainerGap(55, Short.MAX_VALUE))
        );

        options_panel_bookie.add(novo_evento_elements_bookie, "card2");

        jLabel33.setText("Notificações:");

        notificacoes_list_bookie.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Identificador Evento", "Ganhos", "Perdas", "Balanço"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane8.setViewportView(notificacoes_list_bookie);

        descartar_button1.setText("Descartar");
        descartar_button1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                descartar_button1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout notificacoes_bookieLayout = new javax.swing.GroupLayout(notificacoes_bookie);
        notificacoes_bookie.setLayout(notificacoes_bookieLayout);
        notificacoes_bookieLayout.setHorizontalGroup(
            notificacoes_bookieLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(notificacoes_bookieLayout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(notificacoes_bookieLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(descartar_button1)
                    .addGroup(notificacoes_bookieLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel33)
                        .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 631, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(91, Short.MAX_VALUE))
        );
        notificacoes_bookieLayout.setVerticalGroup(
            notificacoes_bookieLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(notificacoes_bookieLayout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(jLabel33)
                .addGap(28, 28, 28)
                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(descartar_button1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        options_panel_bookie.add(notificacoes_bookie, "card2");

        javax.swing.GroupLayout area_bookieLayout = new javax.swing.GroupLayout(area_bookie);
        area_bookie.setLayout(area_bookieLayout);
        area_bookieLayout.setHorizontalGroup(
            area_bookieLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(area_bookieLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(buttons_panel_bookie, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(area_bookieLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(options_panel_bookie, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(area_bookieLayout.createSequentialGroup()
                        .addComponent(Welcome_label_bookie)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        area_bookieLayout.setVerticalGroup(
            area_bookieLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, area_bookieLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(area_bookieLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(area_bookieLayout.createSequentialGroup()
                        .addGap(0, 20, Short.MAX_VALUE)
                        .addComponent(Welcome_label_bookie)
                        .addGap(18, 18, 18)
                        .addComponent(options_panel_bookie, javax.swing.GroupLayout.PREFERRED_SIZE, 391, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(buttons_panel_bookie, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(area_cliente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(16, 16, 16)
                    .addComponent(area_administrador, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap()))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(area_bookie, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGap(16, 16, 16)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(area_cliente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(16, 16, 16)
                    .addComponent(area_administrador, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap()))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(area_bookie, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGap(16, 16, 16)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void ver_jogadores_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ver_jogadores_buttonActionPerformed
        /* remoção de paineis anteriores */
        options_panel_admin.removeAll();
        options_panel_admin.repaint();
        options_panel_admin.revalidate();

        /* alocação do respetivo painel de opções */
        options_panel_admin.add(ver_jogadores_elements_admin);
        options_panel_admin.repaint();
        options_panel_admin.revalidate();

        HashMap<String, Jogador> jogadores = this.mycontroller.getJogadores();

        DefaultTableModel model = (DefaultTableModel) jogadores_list.getModel();

        model.setRowCount(0);
        
        DecimalFormat dc = new DecimalFormat("0.00");

        for (Jogador j : jogadores.values()){
            model.addRow(new Object[]{j.getEmail(), j.getNome(), j.getContacto(), Double.parseDouble(dc.format(j.getSaldo()))});
        }

    }//GEN-LAST:event_ver_jogadores_buttonActionPerformed

    private void eventos_desportivos_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eventos_desportivos_buttonActionPerformed
        /* remoção de paineis anteriores */
        options_panel_admin.removeAll();
        options_panel_admin.repaint();
        options_panel_admin.revalidate();

        /* alocação do respetivo painel de opções */
        options_panel_admin.add(eventos_desportivos_elements_admin);
        options_panel_admin.repaint();
        options_panel_admin.revalidate();

        DefaultTableModel model = (DefaultTableModel) eventos_lista.getModel();

        model.setRowCount(0);
        
        
        for (EventoDesportivo e : this.mycontroller.getEventosDesportivos().values()){
            String equipa_casa = this.mycontroller.getEquipa(e.getequipa_casa()).getDesignacao();
            String equipa_fora = this.mycontroller.getEquipa(e.getequipa_fora()).getDesignacao();
            model.addRow(new Object[]{e.getId_evento(), equipa_casa, equipa_fora, e.getGanha_casa(), e.getGanha_fora(), e.getEmpate(), e.getEstado()});
        }

    }//GEN-LAST:event_eventos_desportivos_buttonActionPerformed

    private void jogadores_bloq_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jogadores_bloq_buttonActionPerformed
        /* remoção de paineis anteriores */
        options_panel_admin.removeAll();
        options_panel_admin.repaint();
        options_panel_admin.revalidate();

        /* alocação do respetivo painel de opções */
        options_panel_admin.add(jogadores_bloqueados_elements_admin);
        options_panel_admin.repaint();
        options_panel_admin.revalidate();

        DefaultTableModel model = (DefaultTableModel) jogadores_bloqueados_list.getModel();

        List<Jogador> jogadores = this.mycontroller.getJogadoresBloqueados();

        model.setRowCount(0);
        
        DecimalFormat dc = new DecimalFormat("0.00");

        for (Jogador j : jogadores){
            model.addRow(new Object[]{j.getEmail(), j.getNome(), j.getContacto(), Double.parseDouble(dc.format(j.getSaldo()))});
        }
    }//GEN-LAST:event_jogadores_bloq_buttonActionPerformed

    private void apostas_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_apostas_buttonActionPerformed
        /* remoção de paineis anteriores */
        options_panel_admin.removeAll();
        options_panel_admin.repaint();
        options_panel_admin.revalidate();

        /* alocação do respetivo painel de opções */
        options_panel_admin.add(apostas_elements_admin);
        options_panel_admin.repaint();
        options_panel_admin.revalidate();

        HashMap<Integer, Aposta> apostas = this.mycontroller.getApostas();

        DefaultTableModel model = (DefaultTableModel) lista_apostas_admin.getModel();

        model.setRowCount(0);
        
        DecimalFormat dc = new DecimalFormat("0.00");

        for (Aposta a : apostas.values()){
            model.addRow(new Object[]{a.getId_aposta(), a.getId_evento(), a.getId_jogador(), a.getGanha_casa(), a.getGanha_fora(), a.getEmpate(), Double.parseDouble(dc.format(a.getQuantia()))});
        }

    }//GEN-LAST:event_apostas_buttonActionPerformed

    private void nova_equipa_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nova_equipa_buttonActionPerformed
        /* remoção de paineis anteriores */
        options_panel_admin.removeAll();
        options_panel_admin.repaint();
        options_panel_admin.revalidate();

        /* alocação do respetivo painel de opções */
        options_panel_admin.add(nova_equipa_elements_admin);
        options_panel_admin.repaint();
        options_panel_admin.revalidate();

        List<Liga> ligas = this.mycontroller.getLigas();

        ligas_combo.removeAllItems();

        for (Liga l : ligas){
            ligas_combo.addItem(l.getNome());
        }
    }//GEN-LAST:event_nova_equipa_buttonActionPerformed

    private void nova_liga_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nova_liga_buttonActionPerformed
        /* remoção de paineis anteriores */
        options_panel_admin.removeAll();
        options_panel_admin.repaint();
        options_panel_admin.revalidate();

        /* alocação do respetivo painel de opções */
        options_panel_admin.add(nova_liga_elements_admin);
        options_panel_admin.repaint();
        options_panel_admin.revalidate();
    }//GEN-LAST:event_nova_liga_buttonActionPerformed

    private void terminar_sessao_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_terminar_sessao_buttonActionPerformed
        this.mycontroller.setId_utilizador_aut("None");
        this.setVisible(false);
        
        this.mymodel.removeObserver(this);

        /* local onde o estado da aplicação é guardado */
        this.mycontroller.save();
    }//GEN-LAST:event_terminar_sessao_buttonActionPerformed

    private void remover_jogador_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_remover_jogador_buttonActionPerformed

        if (jogadores_bloqueados_list.getSelectedRow() != -1){

            int row = jogadores_list.getSelectedRow();
            DefaultTableModel model = (DefaultTableModel) jogadores_list.getModel();

            this.mycontroller.eliminaJogador((String) jogadores_list.getValueAt(row, 0));
            //model.removeRow(row);
        }
        else {
            JOptionPane.showMessageDialog(null, "Selecione um dos jogadores.", "Erro!", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_remover_jogador_buttonActionPerformed

    private void bloquear_jogador_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bloquear_jogador_buttonActionPerformed

        if (jogadores_list.getSelectedRow() != -1){

            int row = jogadores_list.getSelectedRow();
            DefaultTableModel model = (DefaultTableModel) jogadores_list.getModel();

            this.mycontroller.bloqueiaJogador((String) jogadores_list.getValueAt(row, 0));
            //model.removeRow(row);
        }
        else {
            JOptionPane.showMessageDialog(null, "Selecione um dos jogadores.", "Erro!", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_bloquear_jogador_buttonActionPerformed

    private void fechar_evento_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fechar_evento_buttonActionPerformed
        /* remoção de paineis anteriores */
        options_panel_admin.removeAll();
        options_panel_admin.repaint();
        options_panel_admin.revalidate();

        /* alocação do respetivo painel de opções */
        options_panel_admin.add(fechar_evento_elements_admin);
        options_panel_admin.repaint();
        options_panel_admin.revalidate();
    }//GEN-LAST:event_fechar_evento_buttonActionPerformed

    private void registar_evento_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_registar_evento_buttonActionPerformed
        /* remoção de paineis anteriores */
        options_panel_admin.removeAll();
        options_panel_admin.repaint();
        options_panel_admin.revalidate();

        /* alocação do respetivo painel de opções */
        options_panel_admin.add(novo_evento_elements_admin);
        options_panel_admin.repaint();
        options_panel_admin.revalidate();

        List<Equipa> equipas = this.mycontroller.getEquipas();

        combo_casa.removeAllItems();
        combo_fora.removeAllItems();

        for (Equipa e : equipas){
            combo_casa.addItem(e.getDesignacao());
            combo_fora.addItem(e.getDesignacao());
        }
    }//GEN-LAST:event_registar_evento_buttonActionPerformed

    private void desbloquear_jogador_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_desbloquear_jogador_buttonActionPerformed

        if (jogadores_bloqueados_list.getSelectedRow() != -1){

            int row = jogadores_bloqueados_list.getSelectedRow();
            DefaultTableModel model = (DefaultTableModel) jogadores_bloqueados_list.getModel();

            this.mycontroller.desbloqueiaJogador((String) jogadores_bloqueados_list.getValueAt(row, 0));
            //model.removeRow(row);
        }
        else {
            JOptionPane.showMessageDialog(null, "Selecione um dos jogadores.", "Erro!", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_desbloquear_jogador_buttonActionPerformed

    private void elimina_apostaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_elimina_apostaActionPerformed

        if ( lista_apostas_admin.getSelectedRow() != -1){

            int row = lista_apostas_admin.getSelectedRow();
            DefaultTableModel model = (DefaultTableModel) lista_apostas_admin.getModel();

            this.mycontroller.removeAposta((int)lista_apostas_admin.getValueAt(row, 0));
            //model.removeRow(row);
        }
        else {
            JOptionPane.showMessageDialog(null, "Selecione uma das apostas.", "Erro!", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_elimina_apostaActionPerformed

    private void regista_evento_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_regista_evento_buttonActionPerformed

        if (!combo_casa.getSelectedItem().toString().isEmpty() &&
            !combo_fora.getSelectedItem().toString().isEmpty() &&
            !odd_casa_field.getText().isEmpty() &&
            !odd_fora_field.getText().isEmpty() &&
            !odd_empate_field.getText().isEmpty()){

            String c_casa = combo_casa.getSelectedItem().toString();
            String c_fora = combo_fora.getSelectedItem().toString();

            String odd_casa = odd_casa_field.getText();
            String odd_fora = odd_fora_field.getText();
            String odd_empate = odd_empate_field.getText();

            if (c_casa.equals(c_fora)){
                JOptionPane.showMessageDialog(null, "Não é possível definir um evento apenas com uma equipa.", "Falha no registo do evento desportivo", JOptionPane.ERROR_MESSAGE);
            }
            else {
                this.mycontroller.registaEventoDesportivo(c_casa, c_fora, Double.parseDouble(odd_casa), Double.parseDouble(odd_fora), Double.parseDouble(odd_empate), false);
                JOptionPane.showMessageDialog(null, "Evento registado com sucesso.", "BetESS", JOptionPane.PLAIN_MESSAGE);
            }
        }
        else {
            JOptionPane.showMessageDialog(null, "Existem campos do formulário não preenchidos.", "Erro!", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_regista_evento_buttonActionPerformed

    private void regista_equipa_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_regista_equipa_buttonActionPerformed

        if (!nome_equipa_field.getText().isEmpty() &&
            ligas_combo.getSelectedItem() != null){

            String nome_equipa = nome_equipa_field.getText();
            String liga = (String) ligas_combo.getSelectedItem();

            this.mycontroller.registaEquipa(new Equipa(liga, nome_equipa));
        }
        else {
            JOptionPane.showMessageDialog(null, "Existem campos do formulário não preenchidos.", "Erro!", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_regista_equipa_buttonActionPerformed

    private void regista_liga_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_regista_liga_buttonActionPerformed

        if (!nome_liga_field.getText().isEmpty()){

            List<Liga> ligas = this.mycontroller.getLigas();

            String nome_liga = nome_liga_field.getText();

            boolean permite_liga = true;
            for (Liga l: ligas){
                if (l.getNome().equals(nome_liga)){
                    permite_liga = false;
                    break;
                }
            }
            if (permite_liga){
                Liga l = new Liga(nome_liga);
                this.mycontroller.registaLiga(l);
            }
            else {
                JOptionPane.showMessageDialog(null, "Já existe uma Liga registada no sistema com o nome indicado.", "Falha no registo da liga", JOptionPane.ERROR_MESSAGE);
            }
        }
        else {
            JOptionPane.showMessageDialog(null, "Existem campos do formulário não preenchidos.", "Erro!", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_regista_liga_buttonActionPerformed

    private void fecha_evento_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fecha_evento_buttonActionPerformed

        if (opcoes_aposta_admin.getSelection() != null){

            boolean ganha_casa = evento_ganha_casa.isSelected();
            boolean ganha_fora = evento_ganha_fora.isSelected();
            boolean empate = evento_empate.isSelected();

            int row = eventos_lista.getSelectedRow();
            DefaultTableModel model = (DefaultTableModel) eventos_lista.getModel();

            int id_evento = (int) model.getValueAt(row, 0);
            this.mycontroller.fechaEvento(id_evento, ganha_casa, ganha_fora, empate);

            /* remoção de paineis anteriores */
            options_panel_admin.removeAll();
            options_panel_admin.repaint();
            options_panel_admin.revalidate();

            /* alocação do respetivo painel de opções */
            options_panel_admin.add(eventos_desportivos_elements_admin);
            options_panel_admin.repaint();
            options_panel_admin.revalidate();
        }
        else {
            JOptionPane.showMessageDialog(null, "Existem campos do formulário não preenchidos.", "Erro!", JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_fecha_evento_buttonActionPerformed

    private void edit_dados_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_edit_dados_buttonActionPerformed

        if (!nome_field.getText().isEmpty() &&
            !email_field.getText().isEmpty() &&
            !palavra_passe_field.getText().isEmpty() &&
            contacto_field.getText().isEmpty()){

            Jogador autenticado = this.mycontroller.checkUser(this.mycontroller.getId_utilizador_aut());

            autenticado.setNome(nome_field.getText());
            autenticado.setEmail(email_field.getText());
            autenticado.setPassword(palavra_passe_field.getText());
            autenticado.setContacto(contacto_field.getText());
        }
        else {
            JOptionPane.showMessageDialog(null, "Existem campos do formulário não preenchidos.", "Erro!", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_edit_dados_buttonActionPerformed

    private void contacto_fieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_contacto_fieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_contacto_fieldActionPerformed

    private void palavra_passe_fieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_palavra_passe_fieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_palavra_passe_fieldActionPerformed

    private void email_fieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_email_fieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_email_fieldActionPerformed

    private void nome_fieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nome_fieldActionPerformed

    }//GEN-LAST:event_nome_fieldActionPerformed

    private void descartar_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_descartar_buttonActionPerformed

        if (notificacoes_list.getSelectedRow() != -1){

            DefaultTableModel model = (DefaultTableModel) notificacoes_list.getModel();
            int row = notificacoes_list.getSelectedRow();

            //model.removeRow(row);
            this.mycontroller.removeNotificacao(this.mycontroller.getId_utilizador_aut(), (int) model.getValueAt(row, 1));
        }
        else {
            JOptionPane.showMessageDialog(null, "Selecione uma das notificações.", "Erro!", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_descartar_buttonActionPerformed

    private void creditar_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_creditar_buttonActionPerformed

        if (!creditos_field.getText().isEmpty()){

            double creditos = Double.parseDouble(creditos_field.getText());

            this.mycontroller.atualizaSaldo(creditos, this.mycontroller.getId_utilizador_aut());
        }
        else {
            JOptionPane.showMessageDialog(null, "Existem campos do formulário não preenchidos.", "Erro!", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_creditar_buttonActionPerformed

    private void saldo_fieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saldo_fieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_saldo_fieldActionPerformed

    private void cashout_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cashout_buttonActionPerformed

        if (lista_apostas.getSelectedRow() != -1){

            int row = lista_apostas.getSelectedRow();

            DefaultTableModel model = (DefaultTableModel) lista_apostas.getModel();

            String estado = (String) model.getValueAt(row, 8);

            if (estado.equals("Não paga")){

                Double saldo = this.mycontroller.checkUser(this.mycontroller.getId_utilizador_aut()).getSaldo();
                Double valor_aposta = (double) model.getValueAt(row, 7);
                Double novo_saldo = saldo - (0.8 * valor_aposta);
                
                this.mycontroller.removeAposta((int) model.getValueAt(row, 0));

                saldo -= novo_saldo;
                this.mycontroller.atualizaSaldo(saldo, this.mycontroller.getId_utilizador_aut());

                //model.removeRow(row);
            }
        }
        else{
            JOptionPane.showMessageDialog(null, "Selecione uma aposta!", "Erro!", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_cashout_buttonActionPerformed

    private void fora_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fora_buttonActionPerformed
        DefaultTableModel model = (DefaultTableModel) events_list.getModel();
        int row = events_list.getSelectedRow();

        double odd_fora = (double)model.getValueAt(row, 4);
        
        DecimalFormat dc = new DecimalFormat("0.00");

        ganhos_field.setText(dc.format(odd_fora * Double.parseDouble(quantia_field.getText())));
    }//GEN-LAST:event_fora_buttonActionPerformed

    private void empate_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_empate_buttonActionPerformed
        DefaultTableModel model = (DefaultTableModel) events_list.getModel();
        int row = events_list.getSelectedRow();

        double odd_empate = (double)model.getValueAt(row, 5);
        
        DecimalFormat dc = new DecimalFormat("0.00");

        ganhos_field.setText(dc.format(odd_empate * Double.parseDouble(quantia_field.getText())));
    }//GEN-LAST:event_empate_buttonActionPerformed

    private void casa_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_casa_buttonActionPerformed
        DefaultTableModel model = (DefaultTableModel) events_list.getModel();
        int row = events_list.getSelectedRow();

        double odd_casa = (double)model.getValueAt(row, 3);
        
        DecimalFormat dc = new DecimalFormat("0.00");

        ganhos_field.setText(dc.format(odd_casa * Double.parseDouble(quantia_field.getText())));
    }//GEN-LAST:event_casa_buttonActionPerformed

    private void ganhos_fieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ganhos_fieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ganhos_fieldActionPerformed

    private void quantia_fieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_quantia_fieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_quantia_fieldActionPerformed

    private void submit_aposta_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_submit_aposta_buttonActionPerformed

        if (!quantia_field.getText().isEmpty() && (opcoes_aposta_client.getSelection() != null)){

            String user = this.mycontroller.getId_utilizador_aut();
            Jogador j = this.mycontroller.checkUser(user);

            if (j.getSaldo() >= Double.parseDouble(quantia_field.getText())){

                DefaultTableModel model = (DefaultTableModel) events_list.getModel();

                int row = events_list.getSelectedRow();

                int id_evento = (int)model.getValueAt(row, 0);

                boolean casa_selected = casa_button.isSelected();
                boolean fora_selected = fora_button.isSelected();
                boolean empate_selected = empate_button.isSelected();

                boolean aposta_permitida = true;
                for (Aposta a : this.mycontroller.getApostasJogador(user)){
                    if (a.getId_evento() == id_evento){
                        aposta_permitida = false;
                        break;
                    }
                }

                if (aposta_permitida){
                    this.mycontroller.registaAposta(Double.parseDouble(quantia_field.getText()), id_evento, user, casa_selected, fora_selected, empate_selected);

                    JOptionPane.showMessageDialog(null, "Aposta registada com sucesso.", "BetESS", JOptionPane.PLAIN_MESSAGE);
                }
                else {
                    JOptionPane.showMessageDialog(null, "Já tem uma aposta registada para o evento selecionado.", "Falha no registo da aposta", JOptionPane.ERROR_MESSAGE);
                }
            }
            else {
                JOptionPane.showMessageDialog(null, "Créditos insuficientes.", "Falha no registo da aposta", JOptionPane.ERROR_MESSAGE);
            }
        }
        else {
            JOptionPane.showMessageDialog(null, "Existem campos do formulário não preenchidos.", "Erro!", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_submit_aposta_buttonActionPerformed

    private void logout_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logout_buttonActionPerformed
        this.mycontroller.setId_utilizador_aut("None");
        this.setVisible(false);
        
        this.mymodel.removeObserver(this);

        /* local onde o estado da aplicação é guardado */
        this.mycontroller.save();
    }//GEN-LAST:event_logout_buttonActionPerformed

    private void editar_perfil_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editar_perfil_buttonActionPerformed
        Jogador autenticado = this.mycontroller.checkUser(this.mycontroller.getId_utilizador_aut());

        nome_field.setText(autenticado.getNome());
        email_field.setText(autenticado.getEmail());
        contacto_field.setText(autenticado.getContacto());

        /* remoção de paineis anteriores */
        options_panel_client.removeAll();
        options_panel_client.repaint();
        options_panel_client.revalidate();

        /* alocação do respetivo painel de opções */
        options_panel_client.add(edit_perfil_elements_client);
        options_panel_client.repaint();
        options_panel_client.revalidate();
    }//GEN-LAST:event_editar_perfil_buttonActionPerformed

    private void notificacoes_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_notificacoes_buttonActionPerformed
        /* remoção de paineis anteriores */
        options_panel_client.removeAll();
        options_panel_client.repaint();
        options_panel_client.revalidate();

        /* alocação do respetivo painel de opções */
        options_panel_client.add(notificacoes_elements_client);
        options_panel_client.repaint();
        options_panel_client.revalidate();

        Jogador j = this.mycontroller.checkUser(this.mycontroller.getId_utilizador_aut());

        List<Notificacao> notificacoes = j.getNotificacoes();

        DefaultTableModel model = (DefaultTableModel) notificacoes_list.getModel();

        model.setRowCount(0);
        
        DecimalFormat dc = new DecimalFormat("0.00");

        for (Notificacao n : notificacoes){
            Aposta a = this.mycontroller.getAposta(n.getId_aposta());
            EventoDesportivo e = this.mycontroller.getEventoDesportivo(a.getId_evento());
            String equipa_casa = e.getequipa_casa();
            String equipa_fora = e.getequipa_fora();

            model.addRow(new Object[]{a.getId_evento(), a.getId_aposta(), equipa_casa, equipa_fora, Double.parseDouble(dc.format(a.getQuantia())), Double.parseDouble(dc.format(n.getBalanco())), n.getStatus()});
        }
    }//GEN-LAST:event_notificacoes_buttonActionPerformed

    private void creditos_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_creditos_buttonActionPerformed
        /* remoção de paineis anteriores */
        options_panel_client.removeAll();
        options_panel_client.repaint();
        options_panel_client.revalidate();

        /* alocação do respetivo painel de opções */
        options_panel_client.add(creditos_elements_client);
        options_panel_client.repaint();
        options_panel_client.revalidate();
        
        DecimalFormat dc = new DecimalFormat("0.00");

        saldo_field.setText(dc.format(this.mycontroller.checkUser(this.mycontroller.getId_utilizador_aut()).getSaldo()));
    }//GEN-LAST:event_creditos_buttonActionPerformed

    private void ver_apostas_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ver_apostas_buttonActionPerformed
        /* remoção de paineis anteriores */
        options_panel_client.removeAll();
        options_panel_client.repaint();
        options_panel_client.revalidate();

        /* alocação do respetivo painel de opções */
        options_panel_client.add(apostas_elements_client);
        options_panel_client.repaint();
        options_panel_client.revalidate();

        List<Aposta> apostas = this.mycontroller.getApostasJogador(this.mycontroller.getId_utilizador_aut());

        DefaultTableModel model = (DefaultTableModel) lista_apostas.getModel();

        model.setRowCount(0);
        
        DecimalFormat dc = new DecimalFormat("0.00");

        for (Aposta a : apostas){
            EventoDesportivo e = this.mycontroller.getEventoDesportivo(a.getId_evento());;
            model.addRow(new Object[]{a.getId_aposta(), a.getId_evento(), e.getequipa_casa(), e.getequipa_fora(), a.getGanha_casa(), a.getGanha_fora(), a.getEmpate(), Double.parseDouble(dc.format(a.getQuantia())), a.getEstado()});
        }
    }//GEN-LAST:event_ver_apostas_buttonActionPerformed

    private void apostar_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_apostar_buttonActionPerformed
        /* remoção de paineis anteriores */
        options_panel_client.removeAll();
        options_panel_client.repaint();
        options_panel_client.revalidate();

        /* alocação do respetivo painel de opções */
        options_panel_client.add(apostar_elements_client);
        options_panel_client.repaint();
        options_panel_client.revalidate();

        HashMap<Integer, EventoDesportivo> eventos = this.mycontroller.getEventosDesportivos();

        DefaultTableModel model = (DefaultTableModel) events_list.getModel();

        model.setRowCount(0);

        for (EventoDesportivo e : eventos.values()){
            if (e.getEstado().equals("Aberto")){
                String equipa_casa = this.mycontroller.getEquipa(e.getequipa_casa()).getDesignacao();
                String equipa_fora = this.mycontroller.getEquipa(e.getequipa_fora()).getDesignacao();
                model.addRow(new Object[]{e.getId_evento(), equipa_casa, equipa_fora, e.getOdd_casa(), e.getOdd_fora(), e.getOdd_empate()});
            }
        }
    }//GEN-LAST:event_apostar_buttonActionPerformed

    private void combo_casaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_combo_casaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_combo_casaActionPerformed

    private void eventos_desportivos_button1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eventos_desportivos_button1ActionPerformed
        /* remoção de paineis anteriores */
        options_panel_bookie.removeAll();
        options_panel_bookie.repaint();
        options_panel_bookie.revalidate();

        /* alocação do respetivo painel de opções */
        options_panel_bookie.add(eventos_desportivos_elements_admin1);
        options_panel_bookie.repaint();
        options_panel_bookie.revalidate();

        DefaultTableModel model = (DefaultTableModel) eventos_lista1.getModel();

        model.setRowCount(0);

        
        for (EventoDesportivo e : this.mycontroller.getEventosDesportivos().values()){
            String equipa_casa = this.mycontroller.getEquipa(e.getequipa_casa()).getDesignacao();
            String equipa_fora = this.mycontroller.getEquipa(e.getequipa_fora()).getDesignacao();
            model.addRow(new Object[]{e.getId_evento(), equipa_casa, equipa_fora, e.getGanha_casa(), e.getGanha_fora(), e.getEmpate(), e.getEstado()});
        }
    }//GEN-LAST:event_eventos_desportivos_button1ActionPerformed

    private void terminar_sessao_button1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_terminar_sessao_button1ActionPerformed
        this.mycontroller.setId_utilizador_aut("None");
        this.setVisible(false);
        
        this.mymodel.removeObserver(this);

        /* local onde o estado da aplicação é guardado */
        this.mycontroller.save();
    }//GEN-LAST:event_terminar_sessao_button1ActionPerformed

    private void registar_evento_button1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_registar_evento_button1ActionPerformed
        /* remoção de paineis anteriores */
        options_panel_bookie.removeAll();
        options_panel_bookie.repaint();
        options_panel_bookie.revalidate();

        /* alocação do respetivo painel de opções */
        options_panel_bookie.add(novo_evento_elements_bookie);
        options_panel_bookie.repaint();
        options_panel_bookie.revalidate();

        List<Equipa> equipas = this.mycontroller.getEquipas();

        combo_casa1.removeAllItems();
        combo_fora1.removeAllItems();

        for (Equipa e : equipas){
            combo_casa1.addItem(e.getDesignacao());
            combo_fora1.addItem(e.getDesignacao());
        }
    }//GEN-LAST:event_registar_evento_button1ActionPerformed

    private void combo_casa1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_combo_casa1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_combo_casa1ActionPerformed

    private void regista_evento_button1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_regista_evento_button1ActionPerformed

        if (!combo_casa1.getSelectedItem().toString().isEmpty() &&
            !combo_fora1.getSelectedItem().toString().isEmpty() &&
            !odd_casa_field1.getText().isEmpty() &&
            !odd_fora_field1.getText().isEmpty() &&
            !odd_empate_field1.getText().isEmpty()){

            String c_casa = combo_casa1.getSelectedItem().toString();
            String c_fora = combo_fora1.getSelectedItem().toString();

            String odd_casa = odd_casa_field1.getText();
            String odd_fora = odd_fora_field1.getText();
            String odd_empate = odd_empate_field1.getText();

            if (c_casa.equals(c_fora)){
                JOptionPane.showMessageDialog(null, "Não é possível definir um evento apenas com uma equipa.", "Falha no registo do evento desportivo", JOptionPane.ERROR_MESSAGE);
            }
            else {
                this.mycontroller.registaEventoDesportivo(c_casa, c_fora, Double.parseDouble(odd_casa), Double.parseDouble(odd_fora), Double.parseDouble(odd_empate), interesse_not.isSelected());
                JOptionPane.showMessageDialog(null, "Evento registado com sucesso.", "BetESS", JOptionPane.PLAIN_MESSAGE);
            }
        }
        else {
            JOptionPane.showMessageDialog(null, "Existem campos do formulário não preenchidos.", "Erro!", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_regista_evento_button1ActionPerformed

    private void descartar_button1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_descartar_button1ActionPerformed
        if (notificacoes_list_bookie.getSelectedRow() != -1){

            DefaultTableModel model = (DefaultTableModel) notificacoes_list_bookie.getModel();
            int row = notificacoes_list_bookie.getSelectedRow();

            //model.removeRow(row);
            this.mycontroller.removeNotificacaoBookie((int) model.getValueAt(row, 0));
        }
        else {
            JOptionPane.showMessageDialog(null, "Selecione uma das notificações.", "Erro!", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_descartar_button1ActionPerformed

    private void notificacoes_button_bookieActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_notificacoes_button_bookieActionPerformed
        /* remoção de paineis anteriores */
        options_panel_bookie.removeAll();
        options_panel_bookie.repaint();
        options_panel_bookie.revalidate();

        /* alocação do respetivo painel de opções */
        options_panel_bookie.add(notificacoes_bookie);
        options_panel_bookie.repaint();
        options_panel_bookie.revalidate();

        Bookie bookie = this.mycontroller.getBookie();

        List<NotificacaoBookie> notificacoes = bookie.getNotificacoes();

        DefaultTableModel model = (DefaultTableModel) notificacoes_list_bookie.getModel();

        model.setRowCount(0);
        
        DecimalFormat dc = new DecimalFormat("0.00");

        for (NotificacaoBookie n : notificacoes){
            model.addRow(new Object[]{n.getIdEvento(), Double.parseDouble(dc.format(n.getGanhos())), Double.parseDouble(dc.format(n.getPerdas())), Double.parseDouble(dc.format(n.getBalanco()))});
        }
    }//GEN-LAST:event_notificacoes_button_bookieActionPerformed

    private void interesse_notActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_interesse_notActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_interesse_notActionPerformed

    private void odd_casa_field1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_odd_casa_field1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_odd_casa_field1ActionPerformed

    /**
     * @param args the command line arguments
     */
    //public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        /*try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(AreaUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AreaUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AreaUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AreaUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }*/
        //</editor-fold>

        /* Create and display the form */
        /*java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AreaUI().setVisible(true);
            }
        });
    }*/

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Welcome_label_admin;
    private javax.swing.JLabel Welcome_label_bookie;
    private javax.swing.JLabel Welcome_label_client;
    private javax.swing.JButton apostar_button;
    private javax.swing.JPanel apostar_elements_client;
    private javax.swing.JButton apostas_button;
    private javax.swing.JPanel apostas_elements_admin;
    private javax.swing.JPanel apostas_elements_client;
    private javax.swing.JPanel area_administrador;
    private javax.swing.JPanel area_bookie;
    private javax.swing.JPanel area_cliente;
    private javax.swing.JButton bloquear_jogador_button;
    private javax.swing.JPanel buttons_panel_admin;
    private javax.swing.JPanel buttons_panel_bookie;
    private javax.swing.JPanel buttons_panel_client;
    private javax.swing.JRadioButton casa_button;
    private javax.swing.JButton cashout_button;
    private javax.swing.JComboBox<String> combo_casa;
    private javax.swing.JComboBox<String> combo_casa1;
    private javax.swing.JComboBox<String> combo_fora;
    private javax.swing.JComboBox<String> combo_fora1;
    private javax.swing.JTextField contacto_field;
    private javax.swing.JButton creditar_button;
    private javax.swing.JButton creditos_button;
    private javax.swing.JPanel creditos_elements_client;
    private javax.swing.JTextField creditos_field;
    private javax.swing.JButton desbloquear_jogador_button;
    private javax.swing.JButton descartar_button;
    private javax.swing.JButton descartar_button1;
    private javax.swing.JButton edit_dados_button;
    private javax.swing.JPanel edit_perfil_elements_client;
    private javax.swing.JButton editar_perfil_button;
    private javax.swing.JButton elimina_aposta;
    private javax.swing.JTextField email_field;
    private javax.swing.JRadioButton empate_button;
    private javax.swing.JRadioButton evento_empate;
    private javax.swing.JRadioButton evento_ganha_casa;
    private javax.swing.JRadioButton evento_ganha_fora;
    private javax.swing.JButton eventos_desportivos_button;
    private javax.swing.JButton eventos_desportivos_button1;
    private javax.swing.JPanel eventos_desportivos_elements_admin;
    private javax.swing.JPanel eventos_desportivos_elements_admin1;
    private javax.swing.JTable eventos_lista;
    private javax.swing.JTable eventos_lista1;
    private javax.swing.JTable events_list;
    private javax.swing.JButton fecha_evento_button;
    private javax.swing.JButton fechar_evento_button;
    private javax.swing.JPanel fechar_evento_elements_admin;
    private javax.swing.JRadioButton fora_button;
    private javax.swing.JTextField ganhos_field;
    private javax.swing.JCheckBox interesse_not;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JButton jogadores_bloq_button;
    private javax.swing.JPanel jogadores_bloqueados_elements_admin;
    private javax.swing.JTable jogadores_bloqueados_list;
    private javax.swing.JTable jogadores_list;
    private javax.swing.JComboBox<String> ligas_combo;
    private javax.swing.JTable lista_apostas;
    private javax.swing.JTable lista_apostas_admin;
    private javax.swing.JScrollPane lista_apostas_pane;
    private javax.swing.JButton logout_button;
    private javax.swing.JTextField nome_equipa_field;
    private javax.swing.JTextField nome_field;
    private javax.swing.JTextField nome_liga_field;
    private javax.swing.JPanel notificacoes_bookie;
    private javax.swing.JButton notificacoes_button;
    private javax.swing.JButton notificacoes_button_bookie;
    private javax.swing.JPanel notificacoes_elements_client;
    private javax.swing.JTable notificacoes_list;
    private javax.swing.JTable notificacoes_list_bookie;
    private javax.swing.JButton nova_equipa_button;
    private javax.swing.JPanel nova_equipa_elements_admin;
    private javax.swing.JButton nova_liga_button;
    private javax.swing.JPanel nova_liga_elements_admin;
    private javax.swing.JPanel novo_evento_elements_admin;
    private javax.swing.JPanel novo_evento_elements_bookie;
    private javax.swing.JTextField odd_casa_field;
    private javax.swing.JTextField odd_casa_field1;
    private javax.swing.JTextField odd_empate_field;
    private javax.swing.JTextField odd_empate_field1;
    private javax.swing.JTextField odd_fora_field;
    private javax.swing.JTextField odd_fora_field1;
    private javax.swing.ButtonGroup opcoes_aposta_admin;
    private javax.swing.ButtonGroup opcoes_aposta_client;
    private javax.swing.JPanel options_panel_admin;
    private javax.swing.JPanel options_panel_bookie;
    private javax.swing.JPanel options_panel_client;
    private javax.swing.JTextField palavra_passe_field;
    private javax.swing.JTextField quantia_field;
    private javax.swing.JButton regista_equipa_button;
    private javax.swing.JButton regista_evento_button;
    private javax.swing.JButton regista_evento_button1;
    private javax.swing.JButton regista_liga_button;
    private javax.swing.JButton registar_evento_button;
    private javax.swing.JButton registar_evento_button1;
    private javax.swing.JButton remover_jogador_button;
    private javax.swing.JTextField saldo_field;
    private javax.swing.JButton submit_aposta_button;
    private javax.swing.JButton terminar_sessao_button;
    private javax.swing.JButton terminar_sessao_button1;
    private javax.swing.JButton ver_apostas_button;
    private javax.swing.JButton ver_jogadores_button;
    private javax.swing.JPanel ver_jogadores_elements_admin;
    // End of variables declaration//GEN-END:variables
}
