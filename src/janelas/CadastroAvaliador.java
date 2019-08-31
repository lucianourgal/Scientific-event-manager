/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package janelas;

import ClassesAuxiliares.Arrumador;
import ClassesAuxiliares.BancoDeDados;
import ClassesAuxiliares.Comunicador;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javax.swing.JCheckBox;
import objetosBase.avaliador;
import objetosBase.triviais.area;
import objetosBase.triviais.tipopessoa;
import objetosBase.triviais.unidade;

/**
 *
 * @author Pronatec-04
 */
public class CadastroAvaliador extends javax.swing.JFrame {

    private final Inicial JP;
    String tipo;
    private avaliador atual = new avaliador();
    private final ArrayList<unidade> unidades;
    private final ArrayList<tipopessoa> vinculos;
    private final ArrayList<area> areas;
    private ArrayList<JCheckBox> areasEsc = new ArrayList<>();

    /**
     * Creates new form CadastrarColaborador
     * @param tipo1 INSERT ou UPDATE
     * @param jp1 Janela Inicial
     * @param cpf do avaliador
     */
    public CadastroAvaliador(String tipo1, Inicial jp1, String cpf) {
        tipo = tipo1;
        initComponents();
        this.setLocationRelativeTo(null);
        this.setResizable(false);
       // this.setIconImage(Arrumador.comando().getIcone().getImage());
        this.setVisible(true);
        this.setTitle("Cadastro de Avaliador ("+tipo1+")");
        JP = jp1;
        TFCpf.setText(cpf);

        areas = BancoDeDados.comando().getAreas();

        JCheckBox CHAux;

        for (int a = 0; a < areas.size(); a++) {
            CHAux = new JCheckBox();
            CHAux.setText(areas.get(a).getNome());
            areasEsc.add(new JCheckBox(areas.get(a).getNome()));
            this.add(areasEsc.get(a));
            areasEsc.get(a).setBounds(415, 120 + a * 25, 220, 20);
            areasEsc.get(a).setEnabled(false);
            areasEsc.get(a).setSelected(true);
        }

        CBUnidade.removeAllItems();
      
        CBUnidade.addItem("-");
        unidades = BancoDeDados.comando().getUnidades();

        for (unidade unidade : unidades) {
            CBUnidade.addItem(unidade.getNome());
        }

        CBVinculo.removeAllItems();
        CBVinculo.addItem("-");
        vinculos = BancoDeDados.comando().getTiposPessoas();
        for (int a = 0; a < vinculos.size(); a++) {
            
            if(vinculos.get(a).isPodeAvaliar())
            CBVinculo.addItem(vinculos.get(a).getNome());
        }

        CBSexo.removeAllItems();
        CBSexo.addItem("M");
        CBSexo.addItem("F");

  /*      CBTipoConta.removeAllItems();
        CBTipoConta.addItem("CC");
        CBTipoConta.addItem("CP");

        CBEstado.removeAllItems();
        CBEstado.addItem("PR");
        CBEstado.addItem("-");
        CBEstado.addItem("AC");
        CBEstado.addItem("AL");
        CBEstado.addItem("AP");
        CBEstado.addItem("AM");
        CBEstado.addItem("BA");
        CBEstado.addItem("CE");
        CBEstado.addItem("DF");
        CBEstado.addItem("ES");
        CBEstado.addItem("GO");
        CBEstado.addItem("MA");
        CBEstado.addItem("MT");
        CBEstado.addItem("MS");
        CBEstado.addItem("MG");
        CBEstado.addItem("PA");
        CBEstado.addItem("PB");

        CBEstado.addItem("PE");
        CBEstado.addItem("PI");
        CBEstado.addItem("RJ");
        CBEstado.addItem("RN");
        CBEstado.addItem("RS");
        CBEstado.addItem("RR");
        CBEstado.addItem("SC");
        CBEstado.addItem("SP");
        CBEstado.addItem("SE");
        CBEstado.addItem("TO");
        CBEstado.addItem("Estrangeiro");*/

        CBTitulacao.removeAllItems();
        CBTitulacao.addItem("-");
        CBTitulacao.addItem("Graduacao");
        CBTitulacao.addItem("Pos-Graduacao");
        CBTitulacao.addItem("Mestrado");
        CBTitulacao.addItem("Doutorado");
        CBTitulacao.addItem("Pos-Doutorado");

        // TFMunicipio.setText(JP.getUnidade0());
        // TFNaturalidade.setText(JP.getUnidade0());
       // TFBanco.setText("BB");

        TFCpf.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char vChar = e.getKeyChar();
                if (!(Character.isDigit(vChar)
                        || (vChar == KeyEvent.VK_BACK_SPACE)
                        || (vChar == KeyEvent.VK_DELETE))) {
                    e.consume();
                }
            }
        });

        TFTelefone1.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char vChar = e.getKeyChar();
                if (!(Character.isDigit(vChar)
                        || (vChar == KeyEvent.VK_BACK_SPACE)
                        || (vChar == KeyEvent.VK_DELETE))) {
                    e.consume();
                }
                if (TFTelefone1.getText().length() <= 10) {
                    //deixe passar  
                } else {
                    e.setKeyChar((char) KeyEvent.VK_CLEAR);
                }

            }
        });

        TFSiape.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char vChar = e.getKeyChar();
                if (!(Character.isDigit(vChar)
                        || (vChar == KeyEvent.VK_BACK_SPACE)
                        || (vChar == KeyEvent.VK_DELETE))) {
                    e.consume();
                }
            }
        });

//TFSiape.setEditable(false);
        if (tipo.equals("UPDATE")) {

            atual = BancoDeDados.comando().recuperarAvaliador(cpf);
            atualizarCampos();
        }

        //tirar parte do ticket
        Lticket.setVisible(false);
        this.TFTicket.setVisible(false);
        
        
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel8 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        TFEmail = new javax.swing.JTextField();
        TFTelefone1 = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        TFNome = new javax.swing.JTextField();
        TFCpf = new javax.swing.JTextField();
        BTSalvar = new javax.swing.JToggleButton();
        BTCancelar = new javax.swing.JToggleButton();
        TFSiape = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        CBSexo = new javax.swing.JComboBox();
        CBTitulacao = new javax.swing.JComboBox();
        jLabel25 = new javax.swing.JLabel();
        CBUnidade = new javax.swing.JComboBox();
        jLabel13 = new javax.swing.JLabel();
        CBVinculo = new javax.swing.JComboBox();
        jLabel26 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        TFSenha1 = new javax.swing.JPasswordField();
        TFSenha2 = new javax.swing.JPasswordField();
        jLabel29 = new javax.swing.JLabel();
        TFFormacao = new javax.swing.JTextField();
        Aviso = new javax.swing.JTextField();
        Lticket = new javax.swing.JLabel();
        TFTicket = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        TFRg = new javax.swing.JTextField();
        jLabel31 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        TFObservacoes = new javax.swing.JTextArea();

        jLabel8.setText("jLabel8");

        jTextField1.setText("jTextField1");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Cadastro de avaliador/acompanhante");

        TFEmail.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                TFEmailFocusLost(evt);
            }
        });

        TFTelefone1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                TFTelefone1FocusLost(evt);
            }
        });

        jLabel11.setText("E-mail");

        jLabel12.setText("Telefone com DDD ");

        jLabel2.setText("Nome completo");

        jLabel7.setText("CPF");

        TFCpf.setEditable(false);

        BTSalvar.setBackground(new java.awt.Color(255, 255, 255));
        BTSalvar.setForeground(new java.awt.Color(0, 102, 0));
        BTSalvar.setText("Salvar");
        BTSalvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTSalvarActionPerformed(evt);
            }
        });

        BTCancelar.setForeground(new java.awt.Color(153, 0, 0));
        BTCancelar.setText("Cancelar");
        BTCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTCancelarActionPerformed(evt);
            }
        });

        jLabel20.setText("SIAPE");

        jLabel9.setText("Senha (repetir)");

        jLabel23.setText("Senha ");

        CBSexo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        CBTitulacao.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel25.setText("Titulação");

        CBUnidade.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel13.setText("Campus");

        CBVinculo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel26.setText("Vinculo");

        jLabel28.setText("Pode avaliar as Areas:");

        jLabel29.setText("Curso de Formação");

        Aviso.setEditable(false);

        Lticket.setText("Ticket de Coordenador (opcional)");

        TFTicket.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TFTicketActionPerformed(evt);
            }
        });

        jLabel30.setText("RG");

        jLabel31.setText("Observações (necessidades especiais, restrições alimentares etc)");

        TFObservacoes.setColumns(20);
        TFObservacoes.setRows(5);
        jScrollPane1.setViewportView(TFObservacoes);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(Aviso))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(BTCancelar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(BTSalvar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(TFFormacao, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(Lticket, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 19, Short.MAX_VALUE)
                                .addComponent(TFTicket, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addComponent(jLabel13)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(CBUnidade, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(CBSexo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                            .addComponent(jLabel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(CBTitulacao, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(jLabel26)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(CBVinculo, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addComponent(TFRg, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(TFTelefone1, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel12))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel11)
                                            .addComponent(TFEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addGap(77, 77, 77)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                    .addComponent(jLabel9)
                                                    .addComponent(jLabel23)))
                                            .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(77, 77, 77)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(TFSenha2, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)
                                            .addComponent(TFSenha1))))
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jLabel20)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(TFSiape, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(TFNome, javax.swing.GroupLayout.DEFAULT_SIZE, 289, Short.MAX_VALUE)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(103, 103, 103)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel7)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(TFCpf))
                                    .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addGap(95, 95, 95))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel30, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel31, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 330, Short.MAX_VALUE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(88, 88, 88)
                        .addComponent(jLabel28))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel2)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(TFNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(CBSexo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(TFCpf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(TFSiape, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel20))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(CBUnidade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel13))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(CBTitulacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel25))))
                        .addGap(8, 8, 8)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel29)
                            .addComponent(TFFormacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(CBVinculo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel26))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 21, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel30)
                    .addComponent(TFRg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel31)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(TFTicket, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Lticket, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(TFSenha1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel23))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(TFSenha2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addGap(33, 33, 33)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jLabel12))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(TFTelefone1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TFEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(Aviso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34)
                .addComponent(BTCancelar)
                .addGap(5, 5, 5)
                .addComponent(BTSalvar, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Cancela cadastro - descarta dados
     *
     * @param evt
     */
    private void BTCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTCancelarActionPerformed
        this.setVisible(false);
        JP.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_BTCancelarActionPerformed

    /**
     * Salva cadastro do colaborador
     *
     * @param evt
     */
    private void BTSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTSalvarActionPerformed

        String stringDeErro = "Falha ao cadastrar avaliador!";
        Comunicador com;

        if(CBTitulacao.getSelectedItem().equals("-"))
            stringDeErro = stringDeErro + "\nSelecione sua titulacao (graduacao, pos-grad, mestrado, doutorado)";
        
          if(CBVinculo.getSelectedItem().equals("-"))
            stringDeErro = stringDeErro + "\nSelecione seu vínculo";

        
        if (TFNome.getText().equals("")) {
            stringDeErro = stringDeErro + "\nCampo de nome não preenchido.";
        }
        
         if(CBUnidade.getSelectedItem().equals("-"))
            stringDeErro = stringDeErro + "\nSelecione sua unidade (campus)";

        if (TFSenha1.getText().length() < 1 && tipo.equals("INSERT")) {
            stringDeErro = stringDeErro + "\nSenha não definida.";
        }

        if (!TFSenha1.getText().equals(TFSenha2.getText())) {
            stringDeErro = stringDeErro + "\nSenhas não correspondem.";
        }

        if(TFFormacao.getText().length()<8){
            stringDeErro = stringDeErro + "\nCurso de formação não preenchido.";
        }
        
         if(TFRg.getText().length()<5){
            stringDeErro = stringDeErro + "\nCampo de RG não preenchido.";
        }
         
         if(TFSiape.getText().length()<5){
            stringDeErro = stringDeErro + "\nCampo de SIAPE não preenchido.";
        }
           
        
        if(!Arrumador.comando().ehUmEmail(TFEmail.getText()))
           stringDeErro = stringDeErro + "\nEmail inválido.";
       
        if(!Arrumador.comando().ehUmTelefone(TFTelefone1.getText()))
           stringDeErro = stringDeErro + "\nTelefone inválido.";
        
        
        if (stringDeErro.equals("Falha ao cadastrar avaliador!") != true) {
            com = new Comunicador(this, stringDeErro, "Falha!");
        } else {

            avaliador novo = atual;

            if (TFSiape.getText().equals("")) {
                TFSiape.setText("" + 0);
            }

           // novo.setAgencia(TFAgencia.getText());
            //novo.setBairro(TFBairro.getText());
           // novo.setBanco(TFBanco.getText());
           // novo.setCep(TFCep.getText());
            novo.setUnidadeCod(codigoUnidade(CBUnidade.getSelectedItem().toString()));
          //  novo.setConta(TFConta.getText());
            novo.setCpf(TFCpf.getText());
            novo.setEmail(TFEmail.getText());
          //  novo.setEndereco(TFEndereco.getText());
          //  novo.setEstado(CBEstado.getSelectedItem().toString());
          //  novo.setMunicipio(TFMunicipio.getText());
            novo.setNome(Arrumador.comando().paraMaisculaComecoPalavra(TFNome.getText(),false));
            novo.setSiape(Integer.parseInt(TFSiape.getText()));
            if (TFSenha1.getText().toString().length() > 0) {
                novo.setSenha(TFSenha1.getText());
            }
            novo.setSexo(CBSexo.getSelectedItem().toString());
            novo.setTelefone(TFTelefone1.getText());
          //  novo.setTipoconta(CBTipoConta.getSelectedItem().toString());
            novo.setUnidadeNome(CBUnidade.getSelectedItem().toString());
            novo.setTipoPessoaCod(codigoVinculos(CBVinculo.getSelectedItem().toString()));
            novo.setTipoPessoaNome(CBVinculo.getSelectedItem().toString());
            novo.setTitulacaoNome(CBTitulacao.getSelectedItem().toString());
            novo.setFormacao(TFFormacao.getText());
            novo.setRg(TFRg.getText());
            novo.setObservacoes(TFObservacoes.getText());

            int areasCad=0;
            
            for (int a = 0; a < areas.size(); a++) {
                if (areasEsc.get(a).isSelected()) {
                    System.out.println("Uma area a ser avaliada sendo adicionada...");
                    novo.addArea(areas.get(a).getNome(), areas.get(a).getId());
                    areasCad++;
                }
            }

            if(areasCad<1){
                Comunicador c = new Comunicador(this,"Nenhuma área selecionada. Selecione a(s)\nareas que irá avaliar.","Falha!");
                return;
            }
            
            
            boolean result;

            if (tipo.equals("UPDATE")) {

                result = novo.atualizarNaBase(TFSenha1.getText().toString().length() > 0,true);

                if (result) {
                    com = new Comunicador(JP, "Atualização realizada!", "Sucesso!");
                    Arrumador.comando().recuperarDadosUser(TFCpf.getText());
                    
                      //verifica código ticket coordenador
            if(BancoDeDados.comando().existeTicket(TFTicket.getText(),"coordenador",unidades.get(CBUnidade.getSelectedIndex()).getId())){
               
                      //se estiver ok, adequa coordenador
                      unidades.get(CBUnidade.getSelectedIndex()).adequarCoordenador(TFCpf.getText());
                      //exclui ticket de coordenador
                      BancoDeDados.comando().update("DELETE FROM ticket WHERE codigo = '"+TFTicket.getText()+"'");
            }
                    
                    
                    dispose();
                } else {
                    com = new Comunicador(this, "Não foi possível realizar a atualização!", "Falha!");
                }

            } else {  // novo

                result = novo.inserirNaBase();

                if (result) {
                    
                    //verifica código ticket coordenador
            if(BancoDeDados.comando().existeTicket(TFTicket.getText(),"coordenador",unidades.get(CBUnidade.getSelectedIndex()).getId())){
               
                      //se estiver ok, adequa coordenador
                      unidades.get(CBUnidade.getSelectedIndex()).adequarCoordenador(TFCpf.getText());
                      //exclui ticket de coordenador
                      BancoDeDados.comando().update("DELETE FROM ticket WHERE codigo = '"+TFTicket.getText()+"'");
            }
             
                    
                    
                    JP.refreshEscolheAcompanhantes();
                    com = new Comunicador(JP, "Avaliador cadastrado!", "Sucesso!");
                    
                    dispose();
                } else {
                    com = new Comunicador(this, "Não foi possível realizar o cadastro!", "Falha!");
                }

            }

        }   // fim do else em que a operação ocorre sem erros

    }//GEN-LAST:event_BTSalvarActionPerformed

    
    
    
    private void TFTelefone1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_TFTelefone1FocusLost
       if(!Arrumador.comando().ehUmTelefone(TFTelefone1.getText()))
        {
        Aviso.setText("Telefone de formato inválido");
        }else
        Aviso.setText("Telefone ok");
    }//GEN-LAST:event_TFTelefone1FocusLost

    private void TFEmailFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_TFEmailFocusLost
       if(!Arrumador.comando().ehUmEmail(TFEmail.getText())){
        //TFEmail.setText("");
        Aviso.setText("Email de formato inválido");
        }else
          Aviso.setText("Email ok");
    }//GEN-LAST:event_TFEmailFocusLost

    private void TFTicketActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TFTicketActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TFTicketActionPerformed

    public int codigoUnidade(String nome) {

        for (int a = 0; a < unidades.size(); a++) {
            if (unidades.get(a).getNome().equals(nome)) {
                return unidades.get(a).getId();
            }
        }

        return -1;
    }

    public int codigoVinculos(String nome) {

        for (int a = 0; a < vinculos.size(); a++) {
            if (vinculos.get(a).getNome().equals(nome)) {
                return vinculos.get(a).getId();
            }
        }

        return -1;
    }

    public String nomeVinculo(int id) {

        for (int a = 0; a < vinculos.size(); a++) {
            if (vinculos.get(a).getId() == id) {
                return vinculos.get(a).getNome();
            }
        }

        return "nulo";
    }

    public String nomeUnidade(int id) {

        for (int a = 0; a < unidades.size(); a++) {
            if (unidades.get(a).getId() == id) {
                return unidades.get(a).getNome();
            }
        }

        return "nulo";
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
            java.util.logging.Logger.getLogger(CadastroAvaliador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CadastroAvaliador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CadastroAvaliador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CadastroAvaliador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                //   new CadastrarColaborador().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField Aviso;
    private javax.swing.JToggleButton BTCancelar;
    private javax.swing.JToggleButton BTSalvar;
    private javax.swing.JComboBox CBSexo;
    private javax.swing.JComboBox CBTitulacao;
    private javax.swing.JComboBox CBUnidade;
    private javax.swing.JComboBox CBVinculo;
    private javax.swing.JLabel Lticket;
    private javax.swing.JTextField TFCpf;
    private javax.swing.JTextField TFEmail;
    private javax.swing.JTextField TFFormacao;
    private javax.swing.JTextField TFNome;
    private javax.swing.JTextArea TFObservacoes;
    private javax.swing.JTextField TFRg;
    private javax.swing.JPasswordField TFSenha1;
    private javax.swing.JPasswordField TFSenha2;
    private javax.swing.JTextField TFSiape;
    private javax.swing.JTextField TFTelefone1;
    private javax.swing.JTextField TFTicket;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables

    private void atualizarCampos() {
        atual.informaQuantidades();
        TFNome.setText(atual.getNome());
        TFSiape.setText("" + atual.getSiape());
     //   TFBanco.setText(atual.getBanco());
     //   TFAgencia.setText(atual.getAgencia());
     //   TFConta.setText(atual.getConta());
        TFTelefone1.setText(atual.getTelefone());
        TFEmail.setText(atual.getEmail());
     //   TFCep.setText(atual.getCep());
     //   TFBairro.setText(atual.getBairro());
     //   TFEndereco.setText(atual.getEndereco());
     //   TFMunicipio.setText(atual.getMunicipio());
        TFFormacao.setText(atual.getFormacao());
        CBSexo.setSelectedItem(atual.getSexo());
        CBUnidade.setSelectedItem(atual.getUnidadeNome());
        CBVinculo.setSelectedItem(atual.getTipoPessoaNome());
        CBTitulacao.setSelectedItem(atual.getTitulacaoNome());
     //   CBTipoConta.setSelectedItem(atual.getTipoconta());
    //    CBEstado.setSelectedItem(atual.getEstado());
    
        TFRg.setText(atual.getRg());
        TFObservacoes.setText(atual.getObservacoes());
        
        ArrayList<String> areasAtuais = atual.getExcluirAreaNome();
        for (int a = 0; a < areasAtuais.size(); a++) {
            for (int b = 0; b < areas.size(); b++) {
                if (areasEsc.get(b).getText().equals(areasAtuais.get(a))) {
                    areasEsc.get(b).setSelected(true);
                }
            }
        }
    }
}
