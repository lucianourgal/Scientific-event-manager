package ClassesAuxiliares;

import janelas.CadastroApresentador;
import janelas.CadastroAvaliador;
import janelas.FuncoesCoordenacao;
import janelas.GerenciarApresentacoes;
import janelas.Inicial;
import janelas.RealizarAvaliacoes;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javax.swing.JFrame;
import objetosBase.apresentador;
import objetosBase.avaliador;
import objetosBase.triviais.coordenador;

/**
 *
 * @author ifpr
 */
public class VerificarSeHaCadastro extends javax.swing.JFrame {

    private int contagem = -1;
    private String tipo;
    private Inicial jan;
    private GerenciarApresentacoes gap;
    private CadastroApresentador cad;

    private CadastroAvaliador cad2;
    private final boolean apresentadorTemSenha;
    boolean jaClicouReset = false;

    /**
     * Creates new form VerificarSeHaCadastro
     */

    public void seekndestroy() {
        try {
            gap.seekndestroy();
        } catch (Exception e) {
        }
        try {
            cad.dispose();
        } catch (Exception e) {
        }
        //try{rav.seekndestroy();}catch(Exception e){}
        try {
            cad2.dispose();
        } catch (Exception e) {
        }
        dispose();
    }

    public VerificarSeHaCadastro(String alunooucolaborador, Inicial j, boolean apresTemSenha) {
        tipo = alunooucolaborador;
        this.apresentadorTemSenha = apresTemSenha;
        initComponents();
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        if (tipo.equals("CadastrarApresentacao")) {
            this.setTitle("Insira o CPF do ALUNO que será o APRESENTADOR");
            LCpf.setText("CPF do Apresentador");
            
            if (!apresTemSenha) { //se não precisar de senha...
                LSenha.setVisible(false);
                TFSenha.setEnabled(false);
                TFSenha.setText("sepin");
            }
            
        } else if (tipo.equals("Avaliador")) {
            this.setTitle("LOGIN Coordenador ou LOGIN/CADASTRO Avaliador/acompanhante");
            LAviso.setText("Para novo cadastro: Apenas CPF");
            
            if(!BancoDeDados.comando().avaliadorTemSenha()){
                this.TFSenha.setEnabled(false);
                LSenha.setText("");
            } 

        } else {
            this.setTitle("Insira o CPF (Coordenador)");
        }

        jan = j;
        // JP.setVisible(false);
        setVisible(true);
        getRootPane().setDefaultButton(BTContinuar);

//        this.setIconImage(Arrumador.comando().getIcone().getImage());

        TFCpf.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char vChar = e.getKeyChar();
                if (!(Character.isDigit(vChar)
                        || (vChar == KeyEvent.VK_BACK_SPACE)
                        || (vChar == KeyEvent.VK_DELETE))) {
                    e.consume();
                }

                if (TFCpf.getText().length() <= 10) {
                    //deixe passar  
                } else {
                    e.setKeyChar((char) KeyEvent.VK_CLEAR);
                }

            }
        });

        BTResetarSenha.setEnabled(false);
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        TFCpf = new javax.swing.JTextField();
        LCpf = new javax.swing.JLabel();
        BTVoltar = new javax.swing.JButton();
        BTContinuar = new javax.swing.JButton();
        LAviso = new javax.swing.JLabel();
        TFSenha = new javax.swing.JPasswordField();
        LSenha = new javax.swing.JLabel();
        BTResetarSenha = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Insira o CPF");

        TFCpf.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        LCpf.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        LCpf.setText("CPF");

        BTVoltar.setText("Voltar");
        BTVoltar.setToolTipText("Fechar essa janela ");
        BTVoltar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTVoltarActionPerformed(evt);
            }
        });

        BTContinuar.setForeground(new java.awt.Color(0, 102, 51));
        BTContinuar.setText("Continuar");
        BTContinuar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTContinuarActionPerformed(evt);
            }
        });

        LAviso.setForeground(new java.awt.Color(153, 0, 0));
        LAviso.setText(".");

        TFSenha.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        TFSenha.setToolTipText("Preencher apenas caso já tenha se cadastrado");

        LSenha.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        LSenha.setText("Senha (caso já cadastrado)");

        BTResetarSenha.setText("Resetar Senha");
        BTResetarSenha.setToolTipText("Reset de senha. Envia para seu e-mail cadastrado.");
        BTResetarSenha.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTResetarSenhaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(141, 141, 141)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(LAviso, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(BTVoltar)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(BTResetarSenha))
                            .addComponent(BTContinuar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(TFCpf)
                            .addComponent(LSenha, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(TFSenha)
                            .addComponent(LCpf, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 141, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(LCpf)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TFCpf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(LSenha)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TFSenha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(BTContinuar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(LAviso)
                .addGap(8, 8, 8)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(BTVoltar)
                    .addComponent(BTResetarSenha))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BTVoltarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTVoltarActionPerformed

        this.dispose();
    }//GEN-LAST:event_BTVoltarActionPerformed

    /**
     * Faz verificacao de validade do cpf, e se ja há cadastro
     *
     * @param evt
     */
    private void BTContinuarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTContinuarActionPerformed
        String SQL;
        String cpf = TFCpf.getText().replace(".", "").replace("-", "");

        if (Arrumador.comando().validarCPF(cpf)) {

            //verifica contagem
            if (tipo.equals("CadastrarApresentacao")) {
                SQL = "SELECT COUNT(*) FROM apresentador WHERE cpf = '" + cpf + "';";
            } else if (tipo.equals("Avaliador")) {
                SQL = "SELECT COUNT(*) FROM avaliador WHERE cpf = '" + cpf + "';";
            } else {//coordenação
                SQL = "SELECT COUNT(*) FROM coordenador WHERE avaliador = '" + cpf + "';"; //se contagem>0, já é algum tipo de coordenador
            }

            contagem = BancoDeDados.comando().selectCount(SQL);
            System.out.println("O número de registros para esse CPF na categoria '" + tipo + "' é: " + contagem + ".");

            if (tipo.equals("CadastrarApresentacao")) {
                if (contagem > 0) {
                    if (loginApresetador() || (!apresentadorTemSenha)) { //SE NAO FOR NECESSARIO SENHA, CONTINUA MESMO QUE LOGIN TENHA FALHADO.

                        Arrumador.comando().recuperarDadosUser(cpf);
                        gap = new GerenciarApresentacoes(jan, cpf, new ArrayList<coordenador>(), BancoDeDados.comando().getEventoAtual());
                        this.setVisible(false);
                    } else {
                        System.out.println("Login falhou!");
                        LAviso.setText("Senha incorreta!");
                        if(BancoDeDados.comando().podeResetarSenha())
                            BTResetarSenha.setEnabled(true);
                    }

                } else {  //ok, nao tem cadastro ainda. Começa cadastro de apresentador
                    cad = new CadastroApresentador("INSERT", jan, cpf, apresentadorTemSenha);
                    this.setVisible(false);
                }

            } else if (tipo.equals("Avaliador")) {
                // caso do avaliador
                if (contagem > 0) { // ja esta cadastrado

                    if (loginAvaliador() || (!BancoDeDados.comando().avaliadorTemSenha())) { //Se login Ok, ou Não cobra senha
                        
                        jan.criarJanelaRealizarAvalicoes(cpf);
                        this.setVisible(false);
                    } else {
                        LAviso.setText("Senha incorreta!");
                        if(BancoDeDados.comando().podeResetarSenha())
                            BTResetarSenha.setEnabled(true);
                      

                    }
            // CadastroAvaliador cad = new CadastroAvaliador("UPDATE",jan,cpf);

                } else { //novo cadastro
                    cad2 = new CadastroAvaliador("INSERT", jan, cpf);
                    this.setVisible(false);

                }

            } else { // coordenador

                if (contagem > 0) { // ja esta cadastrado

                    if (loginAvaliador()) {
                        jan.criarJanelaFuncoesCoordenacao(cpf);
                        this.setVisible(false);
                    } else {
                        LAviso.setText("Senha incorreta!");
                        if(BancoDeDados.comando().podeResetarSenha())
                            BTResetarSenha.setEnabled(true);
                    }

                } else {
                    LAviso.setText("CPF sem cadastro como coordenador!");
                }

            }

        } else {
            LAviso.setText("CPF inválido!");
            // Comunicador a = new Comunicador(this,"CPF inválido!","Falha!");
        }

    }//GEN-LAST:event_BTContinuarActionPerformed

    private void BTResetarSenhaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTResetarSenhaActionPerformed
         String SQL;

         if(!jaClicouReset){
         
             BTResetarSenha.setText("Tem certeza?");
             LAviso.setText("Nova senha será enviada em seu e-mail.");
             jaClicouReset = true;
             return;
         }
         
        String cpf = TFCpf.getText().replace(".", "").replace("-", "");
         
        if (Arrumador.comando().validarCPF(cpf)) {

            //verifica contagem
            if (tipo.equals("CadastrarApresentacao")) {
                SQL = "SELECT COUNT(*) FROM apresentador WHERE cpf = '" + cpf + "';";
            } else if (tipo.equals("Avaliador")) {
                SQL = "SELECT COUNT(*) FROM avaliador WHERE cpf = '" + cpf + "';";
            } else {//coordenação
                SQL = "SELECT COUNT(*) FROM coordenador WHERE avaliador = '" + cpf + "';"; //se contagem>0, já é algum tipo de coordenador
            }

            contagem = BancoDeDados.comando().selectCount(SQL);
            System.out.println("O número de registros para esse CPF na categoria '" + tipo + "' é: " + contagem + ".");

            if (tipo.equals("CadastrarApresentacao")) {
                if (contagem > 0) {
                   
                    //resetar senha
                    apresentador apr = BancoDeDados.comando().getApresentador(cpf);
                    
                    if(apr.resetSenha()){
                        LAviso.setText("Senha resetada. Verifique o email.");
                        BTResetarSenha.setText("Resetar senha");
                        jaClicouReset=false;
                        BTResetarSenha.setEnabled(false);
                    }
                    else
                        LAviso.setText("Sem email cadastrado para reset de senha.");
                    

                } else {  //ok, nao tem cadastro ainda. Começa cadastro de apresentador
                   
                     LAviso.setText("CPF sem cadastro como apresentador!");
                    
                }

            } else if (tipo.equals("Avaliador")) {
                // caso do avaliador
                if (contagem > 0) { // ja esta cadastrado

                    
                    //resetar senha
                    avaliador av = BancoDeDados.comando().recuperarAvaliador(cpf);
                    
                    if(av.resetSenha()){
                        LAviso.setText("Senha resetada. Verifique o email.");
                        BTResetarSenha.setText("Resetar senha");
                        jaClicouReset=false;
                        BTResetarSenha.setEnabled(false);
                    }
                    else
                        LAviso.setText("Sem email cadastrado para reset de senha.");

                } else { //novo cadastro
                   
                    LAviso.setText("CPF sem cadastro como avaliador!");

                }

            } else { // coordenador

                if (contagem > 0) { // ja esta cadastrado

                   //resetar senha
                    avaliador av = BancoDeDados.comando().recuperarAvaliador(cpf);
                    
                    if(av.resetSenha()){
                        LAviso.setText("Senha resetada. Verifique o email.");
                        BTResetarSenha.setText("Resetar senha");
                        jaClicouReset=false;
                        BTResetarSenha.setEnabled(false);
                    }
                    else
                        LAviso.setText("Sem email cadastrado para reset de senha.");

                } else {
                    LAviso.setText("CPF sem cadastro como coordenador!");
                }

            }

        } else {
            LAviso.setText("CPF inválido!");
            // Comunicador a = new Comunicador(this,"CPF inválido!","Falha!");
        }
    }//GEN-LAST:event_BTResetarSenhaActionPerformed

    public int getContagem() {
        return contagem;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(VerificarSeHaCadastro.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VerificarSeHaCadastro.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VerificarSeHaCadastro.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VerificarSeHaCadastro.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                //  new VerificarSeHaCadastro().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BTContinuar;
    private javax.swing.JButton BTResetarSenha;
    private javax.swing.JButton BTVoltar;
    private javax.swing.JLabel LAviso;
    private javax.swing.JLabel LCpf;
    private javax.swing.JLabel LSenha;
    private javax.swing.JTextField TFCpf;
    private javax.swing.JPasswordField TFSenha;
    // End of variables declaration//GEN-END:variables

    public boolean colaborador() {

        return tipo.equals("Colaborador");
//throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String getCpf() {
        return TFCpf.getText().replace(".", "").replace("-", "");
// throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private boolean loginApresetador() {

        String cpf = TFCpf.getText().replace(".", "").replace("-", "");
        boolean permiteLogin = false;
        permiteLogin = BancoDeDados.comando().loginApresentador(cpf, TFSenha.getText());
        return permiteLogin;
    }

    private boolean loginAvaliador() {
        String cpf = TFCpf.getText().replace(".", "").replace("-", "");
        return BancoDeDados.comando().loginAvaliador(cpf, TFSenha.getText());

    }

}