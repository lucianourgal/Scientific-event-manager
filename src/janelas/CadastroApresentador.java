/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package janelas;

//import com.sun.java.util.jar.pack.Attribute.Layout;
import ClassesAuxiliares.Arrumador;
import ClassesAuxiliares.BancoDeDados;
import ClassesAuxiliares.Comunicador;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import objetosBase.apresentador;
//import objetosBase.triviais.bolsa;
import objetosBase.triviais.tipopessoa;
import objetosBase.triviais.unidade;

/**
 *
 * @author Pronatec-04
 */
public class CadastroApresentador extends javax.swing.JFrame {

    private final Inicial JP;
    private boolean iniciado;
   // private final boolean independente = true;
    private final ArrayList<unidade> unidades;
    private final ArrayList<tipopessoa> vinculos;
    private final String tipo;
    private apresentador apresentador;
   // private ArrayList<bolsa> bolsas;
    private final boolean apresentadorTemSenha;

    /**
     * Creates new form CadastrarAluno
     * @param tip1o: INSERT ou UPDATE
     * @param cpf do apresentador
     * @param jp1: Janela Inicial
     */
    public CadastroApresentador(String tip1o, Inicial jp1, String cpf, boolean apresentadorTemSenha) {
        iniciado = false;
        this.apresentadorTemSenha = apresentadorTemSenha;
        tipo = tip1o;
        initComponents();

        if (!apresentadorTemSenha) {
            TFSenha1.setVisible(false);
            LSenha1.setVisible(false);
            TFSenha2.setVisible(false);
            LSenha2.setVisible(false);
            Aviso.setText("Não há cadastro de senha para apresentadores.");
        }

        TFNaturalidade.setVisible(false);
        LNaturalidade.setVisible(false);
        //CBEstadoDeNaturalidade.setVisible(false);
        //LEstadoNaturalidade.setVisible(false);
        TFTelefone1.setVisible(false);
        LTelefone.setVisible(false);
        LNecessidadeEspecial.setVisible(false);
        TFNecessidadeEspecial.setVisible(false);
        
        TFDataDeNascimento.setVisible(false);
        LDataNascimento.setVisible(false);
        //TFRg.setVisible(false);
        //CBEstadoRg.setVisible(false);
        //TFOrgaoExpedidorRg.setVisible(false);
        //LRg.setVisible(false);
        //LOrgaoExpedidorDoRg.setVisible(false);
        
        BTSalvar.setEnabled(false);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setVisible(true);
        //this.setIconImage(Arrumador.comando().getIcone().getImage());
        this.setTitle("Cadastro de Estudante Apresentador (" + tip1o + ")");
        JP = jp1;
        TFCpf.setText(cpf);
      //   this.getRootPane().setDefaultButton(BTSalvar);
        // TFDataExpRg.setFormatterFactory(new AbstractFormatterFactory("##/##/####") );
        // TFDataExpRg = new JFormattedTextField((setMascara("(##) ####-####"));

        if (tipo.equals("UPDATE")) {
            CHTermoImagem.setSelected(true);
            CHTermoImagem.setEnabled(false);
            BTSalvar.setEnabled(true);
        }else{
            Aviso.setText("Necessário concordar com o regulamento.");
        }

        CBSexo.removeAllItems();
        CBSexo.addItem("-");
        CBSexo.addItem("M");
        CBSexo.addItem("F");

        /*  CBTipoConta.removeAllItems();
         CBTipoConta.addItem("CC");
         CBTipoConta.addItem("CP");
         
         
         CBDeclarante.removeAllItems();
         CBDeclarante.addItem("Proprio");
         CBDeclarante.addItem("Pai");
         CBDeclarante.addItem("Mae");
         CBDeclarante.addItem("Outros");
    
         
         TFCpfDeclarante.setEditable(false);
         TFNomeDeclarante.setEditable(false);*/
        CBUnidade.removeAllItems();
        CBUnidade.addItem("-");
        unidades = BancoDeDados.comando().getUnidades();
        for (int a = 0; a < unidades.size(); a++) {
            CBUnidade.addItem(unidades.get(a).getNome());
        }

        CBVinculo.removeAllItems();
        CBVinculo.addItem("-");
        vinculos = BancoDeDados.comando().getTiposPessoas();
        for (int a = 0; a < vinculos.size(); a++) {
            if (vinculos.get(a).isPodeApresentar()) {
                CBVinculo.addItem(vinculos.get(a).getNome());
            }
        }

       //  CBBolsa.removeAllItems();
       //  bolsas = BancoDeDados.comando().getBolsas();
        //  for(int a =0;a<bolsas.size();a++)
        //   CBVinculo.addItem(bolsas.get(a).getNome());
        /*   CBEstado.removeAllItems();
         CBEstado.addItem("PR");
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
        CBEstadoRg.removeAllItems();
        CBEstadoRg.addItem("-");
        CBEstadoRg.addItem("PR");
        CBEstadoRg.addItem("AC");
        CBEstadoRg.addItem("AL");
        CBEstadoRg.addItem("AP");
        CBEstadoRg.addItem("AM");
        CBEstadoRg.addItem("BA");
        CBEstadoRg.addItem("CE");
        CBEstadoRg.addItem("DF");
        CBEstadoRg.addItem("ES");
        CBEstadoRg.addItem("GO");
        CBEstadoRg.addItem("MA");
        CBEstadoRg.addItem("MT");
        CBEstadoRg.addItem("MS");
        CBEstadoRg.addItem("MG");
        CBEstadoRg.addItem("PA");
        CBEstadoRg.addItem("PB");

        CBEstadoRg.addItem("PE");
        CBEstadoRg.addItem("PI");
        CBEstadoRg.addItem("RJ");
        CBEstadoRg.addItem("RN");
        CBEstadoRg.addItem("RS");
        CBEstadoRg.addItem("RR");
        CBEstadoRg.addItem("SC");
        CBEstadoRg.addItem("SP");
        CBEstadoRg.addItem("SE");
        CBEstadoRg.addItem("TO");

        CBEstadoDeNaturalidade.removeAllItems();
        CBEstadoDeNaturalidade.addItem("-");
        ArrayList<String> niveis = Arrumador.comando().getNiveisDeCurso();
        for(int x=0;x<niveis.size();x++) {
            CBEstadoDeNaturalidade.addItem(niveis.get(x));
        }
        /*CBEstadoDeNaturalidade.addItem("Nivel Medio");
        CBEstadoDeNaturalidade.addItem("Graduacao");
        CBEstadoDeNaturalidade.addItem("Especializacao");
        CBEstadoDeNaturalidade.addItem("Mestrado");
        CBEstadoDeNaturalidade.addItem("Doutorado");*/
       /* CBEstadoDeNaturalidade.addItem("AC");
        CBEstadoDeNaturalidade.addItem("AL");
        CBEstadoDeNaturalidade.addItem("AP");
        CBEstadoDeNaturalidade.addItem("AM");
        CBEstadoDeNaturalidade.addItem("BA");
        CBEstadoDeNaturalidade.addItem("CE");
        CBEstadoDeNaturalidade.addItem("DF");
        CBEstadoDeNaturalidade.addItem("ES");
        CBEstadoDeNaturalidade.addItem("GO");
        CBEstadoDeNaturalidade.addItem("MA");
        CBEstadoDeNaturalidade.addItem("MT");
        CBEstadoDeNaturalidade.addItem("MS");
        CBEstadoDeNaturalidade.addItem("MG");
        CBEstadoDeNaturalidade.addItem("PA");
        CBEstadoDeNaturalidade.addItem("PB");
        CBEstadoDeNaturalidade.addItem("PE");
        CBEstadoDeNaturalidade.addItem("PI");
        CBEstadoDeNaturalidade.addItem("RJ");
        CBEstadoDeNaturalidade.addItem("RN");
        CBEstadoDeNaturalidade.addItem("RS");
        CBEstadoDeNaturalidade.addItem("RR");
        CBEstadoDeNaturalidade.addItem("SC");
        CBEstadoDeNaturalidade.addItem("SP");
        CBEstadoDeNaturalidade.addItem("SE");
        CBEstadoDeNaturalidade.addItem("TO");
        CBEstadoDeNaturalidade.addItem("Estrangeiro");*/

//TFBanco.setText("Banco do Brasil");
        
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
        TFRg.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char vChar = e.getKeyChar();
                if (!(Character.isDigit(vChar)
                        || (vChar == KeyEvent.VK_BACK_SPACE)
                        || (vChar == KeyEvent.VK_DELETE))) {
                    e.consume();
                }
                if (TFRg.getText().length() <= 10) {
                    //deixe passar  
                } else {
                    e.setKeyChar((char) KeyEvent.VK_CLEAR);
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

        /*TFCpfDeclarante.addKeyListener(new KeyAdapter() {
         public void keyTyped(KeyEvent e) {
         char vChar = e.getKeyChar();
         if (!(Character.isDigit(vChar)
         || (vChar == KeyEvent.VK_BACK_SPACE)
         || (vChar == KeyEvent.VK_DELETE))) {
         e.consume();
         }
         if (TFCpfDeclarante.getText().length() <= 10) {  
         //deixe passar  
         } else {  
         e.setKeyChar((char) KeyEvent.VK_CLEAR);  
         }  
         }
         });    */
        //TFDataDeNascimento.setLayout(null);
        if (tipo.equals("UPDATE")) {
            apresentador = BancoDeDados.comando().getApresentador(TFCpf.getText());
            recuperarDadosApresentador();

        }

        iniciado = true;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jFrame1 = new javax.swing.JFrame();
        jFrame2 = new javax.swing.JFrame();
        jTextField1 = new javax.swing.JTextField();
        CBLocalDeTrabalho = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        TFRg = new javax.swing.JTextField();
        TFNome = new javax.swing.JTextField();
        TFCpf = new javax.swing.JTextField();
        LRg = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        LDataNascimento = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        TFEmail = new javax.swing.JTextField();
        TFTelefone1 = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        LTelefone = new javax.swing.JLabel();
        TFNaturalidade = new javax.swing.JTextField();
        LNaturalidade = new javax.swing.JLabel();
        BTSalvar = new javax.swing.JToggleButton();
        BTCancelar = new javax.swing.JToggleButton();
        TFOrgaoExpedidorRg = new javax.swing.JTextField();
        LOrgaoExpedidorDoRg = new javax.swing.JLabel();
        CBEstadoDeNaturalidade = new javax.swing.JComboBox();
        LEstadoNaturalidade = new javax.swing.JLabel();
        CBSexo = new javax.swing.JComboBox();
        TFDataDeNascimento = new javax.swing.JFormattedTextField();
        CBEstadoRg = new javax.swing.JComboBox();
        TFNecessidadeEspecial = new javax.swing.JTextField();
        LNecessidadeEspecial = new javax.swing.JLabel();
        LSenha1 = new javax.swing.JLabel();
        CBUnidade = new javax.swing.JComboBox();
        jLabel13 = new javax.swing.JLabel();
        CBVinculo = new javax.swing.JComboBox();
        jLabel22 = new javax.swing.JLabel();
        TFSenha1 = new javax.swing.JPasswordField();
        TFSenha2 = new javax.swing.JPasswordField();
        LSenha2 = new javax.swing.JLabel();
        CHTermoImagem = new javax.swing.JCheckBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        Aviso = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        TFObservacoes = new javax.swing.JTextArea();
        jLabel15 = new javax.swing.JLabel();
        LRg1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();

        javax.swing.GroupLayout jFrame1Layout = new javax.swing.GroupLayout(jFrame1.getContentPane());
        jFrame1.getContentPane().setLayout(jFrame1Layout);
        jFrame1Layout.setHorizontalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jFrame1Layout.setVerticalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jFrame2Layout = new javax.swing.GroupLayout(jFrame2.getContentPane());
        jFrame2.getContentPane().setLayout(jFrame2Layout);
        jFrame2Layout.setHorizontalGroup(
            jFrame2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jFrame2Layout.setVerticalGroup(
            jFrame2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        jTextField1.setText("jTextField1");

        jLabel14.setText("E-mail");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Cadastro de dados pessoais do apresentador");

        TFNome.setToolTipText("O nome do apresentador deverá ser preenchido completo e correto, pois o mesmo será utilizado no Anais do \tEvento, não podendo ser alterado posteriormente");
        TFNome.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                TFNomeFocusLost(evt);
            }
        });
        TFNome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TFNomeActionPerformed(evt);
            }
        });

        TFCpf.setEditable(false);
        TFCpf.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                TFCpfKeyTyped(evt);
            }
        });

        LRg.setText("RG");

        jLabel2.setText("Nome completo do Apresentador");

        LDataNascimento.setText("Data de nascimento");

        jLabel7.setText("CPF");

        TFEmail.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                TFEmailFocusLost(evt);
            }
        });

        TFTelefone1.setText("0043211234");
        TFTelefone1.setToolTipText("Ex: 4198765432");
        TFTelefone1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                TFTelefone1FocusLost(evt);
            }
        });

        jLabel11.setText("E-mail");

        LTelefone.setText("Telefones com DDD");

        LNaturalidade.setText("Naturalidade");

        BTSalvar.setBackground(new java.awt.Color(255, 255, 255));
        BTSalvar.setForeground(new java.awt.Color(0, 102, 0));
        BTSalvar.setText("Salvar");
        BTSalvar.setToolTipText("Para poder salvar, é necessário concordar que sua imagem seja utilizada para divulgação do SEPIN, assim como também informar sua data de nascimento");
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

        TFOrgaoExpedidorRg.setText("SESP");

        LOrgaoExpedidorDoRg.setText("Orgão expedidor");

        CBEstadoDeNaturalidade.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        CBEstadoDeNaturalidade.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                CBEstadoDeNaturalidadeFocusLost(evt);
            }
        });

        LEstadoNaturalidade.setText("Tipo do curso");

        CBSexo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        TFDataDeNascimento.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter()));
        TFDataDeNascimento.setText("01/01/0001");
        TFDataDeNascimento.setToolTipText("Formato aceito para datas: DD/MM/AAAA");

        CBEstadoRg.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        LNecessidadeEspecial.setText("Necessidade especial");

        LSenha1.setText("Senha");

        CBUnidade.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel13.setText("Campus");

        CBVinculo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel22.setText("Vinculo");

        LSenha2.setText("Senha");

        CHTermoImagem.setText("Confirma que leu o Regulamento de Evento, e está de acordo com o mesmo");
        CHTermoImagem.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                CHTermoImagemItemStateChanged(evt);
            }
        });
        CHTermoImagem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CHTermoImagemActionPerformed(evt);
            }
        });

        Aviso.setEditable(false);
        Aviso.setColumns(20);
        Aviso.setRows(5);
        jScrollPane1.setViewportView(Aviso);

        TFObservacoes.setColumns(20);
        TFObservacoes.setRows(5);
        TFObservacoes.setToolTipText("Indicar necessidades especiais, restrições alimentares, questões de genero, etc");
        jScrollPane2.setViewportView(TFObservacoes);

        jLabel15.setText("Observações (indicar necessidades especiais, restrições alimentares e questões de gênero)");

        LRg1.setText("Sexo");

        jLabel3.setText("O nome do apresentador deverá ser preenchido completo e correto, pois o mesmo será utilizado");

        jLabel4.setText("nos Certificados e Anais do Evento, não podendo ser alterado posteriormente.");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(LNaturalidade)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(TFNaturalidade, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(TFCpf, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(66, 66, 66)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(LDataNascimento)
                                        .addGap(4, 4, 4)
                                        .addComponent(TFDataDeNascimento, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(LEstadoNaturalidade, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(4, 4, 4)
                                        .addComponent(CBEstadoDeNaturalidade, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jScrollPane1)
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(BTSalvar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(BTCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(CHTermoImagem, javax.swing.GroupLayout.PREFERRED_SIZE, 590, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(LTelefone)
                                        .addComponent(TFTelefone1, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel11)
                                        .addComponent(TFEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGap(31, 31, 31)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jScrollPane2)
                                        .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))))
                .addGap(0, 18, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(LRg)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(TFRg, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(TFOrgaoExpedidorRg, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(CBEstadoRg, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(147, 147, 147))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(LOrgaoExpedidorDoRg)
                                .addGap(192, 192, 192)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(TFNecessidadeEspecial, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(LNecessidadeEspecial)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(LSenha2)
                            .addComponent(LSenha1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(TFSenha2, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(TFSenha1, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(TFNome, javax.swing.GroupLayout.DEFAULT_SIZE, 262, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(CBSexo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(LRg1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel13)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(CBUnidade, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(5, 5, 5)
                                        .addComponent(jLabel22)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(CBVinculo, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(LOrgaoExpedidorDoRg)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(TFRg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(LRg)
                                    .addComponent(CBEstadoRg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(TFOrgaoExpedidorRg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(LNecessidadeEspecial)
                                .addGap(1, 1, 1)
                                .addComponent(TFNecessidadeEspecial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(LRg1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(CBSexo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(TFNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(CBVinculo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel22))
                        .addGap(8, 8, 8)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(CBUnidade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel13))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 38, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(TFSenha1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(LSenha1, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(TFSenha2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(LSenha2, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(2, 2, 2))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(LNaturalidade)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(23, 23, 23)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(CBEstadoDeNaturalidade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(LEstadoNaturalidade))
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(TFCpf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel7))))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(LDataNascimento)
                                .addComponent(TFDataDeNascimento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(TFNaturalidade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jLabel15))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(TFEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addComponent(LTelefone)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(TFTelefone1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(CHTermoImagem)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(BTCancelar)
                        .addGap(18, 18, 18)
                        .addComponent(BTSalvar, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(41, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BTCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTCancelarActionPerformed

        this.setVisible(false);
        JP.setVisible(true);

        this.dispose();
    }//GEN-LAST:event_BTCancelarActionPerformed

    private void TFCpfKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TFCpfKeyTyped

    }//GEN-LAST:event_TFCpfKeyTyped

    /**
     * Salvar cadastro do aluno
     *
     * @param evt
     */
    private void BTSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTSalvarActionPerformed

        String stringDeErro = "Falha ao cadastrar aluno!\n";
        Comunicador com;

        //Date d = new Date(Arrumador.comando().dataParaDATE(TFDataDeNascimento.getText()));
        //if(d.after(new Date("01/01/1920"))){
      /*  if (Arrumador.comando().dataXdepoisDataY(TFDataDeNascimento.getText(), "01/01/1920") < 1) {
            stringDeErro = stringDeErro + "\nFavor informar data de Nascimento.";
        }*/

         if (CBEstadoRg.getSelectedItem().equals("-")) {
            stringDeErro = stringDeErro + "\nSelecione o estado de emissão do RG.";
        }
      
        if (CBSexo.getSelectedItem().equals("-")) {
            stringDeErro = stringDeErro + "\nSelecione o sexo do apresentador.";
        }
      
        if (CBUnidade.getSelectedItem().equals("-")) {
            stringDeErro = stringDeErro + "\nSelecione sua unidade (campus).";
        }
        
        
        if (CBEstadoDeNaturalidade.getSelectedItem().equals("-")) {
            stringDeErro = stringDeErro + "\nSelecione o tipo de curso.";
        }
        

        if (CBVinculo.getSelectedItem().equals("-")) {
            stringDeErro = stringDeErro + "\nSelecione seu Vinculo.";
        }

       // if(TFNaturalidade.getText().length()<3)
        //   stringDeErro = stringDeErro + "\nNaturalidade não preenchida."; 
        if (TFNome.getText().length()<3) {
            stringDeErro = stringDeErro + "\nCampo de nome não preenchido.";
        }

        if (TFRg.getText().length()<5) {
            stringDeErro = stringDeErro + "\nCampo de RG não preenchido.";
        }
        

        if(apresentadorTemSenha){
        if (TFSenha1.getText().length() < 1 && tipo.equals("INSERT") && apresentadorTemSenha) {
            stringDeErro = stringDeErro + "\nSenha não definida.";
        }
        if (!TFSenha1.getText().equals(TFSenha2.getText()) && apresentadorTemSenha) {
            stringDeErro = stringDeErro + "\nSenhas não correspondem.";
        }}

        if (!Arrumador.comando().ehUmEmail(TFEmail.getText())) {
            stringDeErro = stringDeErro + "\nEmail inválido.";
        }

        /*if (!Arrumador.comando().ehUmTelefone(TFTelefone1.getText())) {
            stringDeErro = stringDeErro + "\nTelefone inválido.";
        }*/

        if (stringDeErro.equals("Falha ao cadastrar aluno!\n") != true) {
            Aviso.setText(stringDeErro);
            com = new Comunicador(this, stringDeErro, "Falha!");
        } else {

            apresentador novo = new apresentador();

            // novo.setAgencia(TFAgencia.getText());
            // novo.setBairro(TFBairro.getText());
            // novo.setBanco(TFBanco.getText());
            //  novo.setCep(TFCep.getText());
            novo.setCodigoUnidade(codigoUnidade(CBUnidade.getSelectedItem().toString()));
            //  novo.setConta(TFConta.getText());
            novo.setCpf(TFCpf.getText());
            // novo.setCpfdeclarante(TFCpfDeclarante.getText());
            novo.setDatadenascimento(Arrumador.comando().dataParaDATE(TFDataDeNascimento.getText()));
            //  novo.setDataexprg(Arrumador.comando().dataParaDATE(TFDataExpRg.getText()));
            novo.setDataexprg(Arrumador.comando().dataParaDATE("01/01/0001"));
            //  novo.setDeclarante(CBDeclarante.getSelectedItem().toString());
            novo.setEmail(TFEmail.getText());
          //   novo.setEndereco(TFEndereco.getText());
            //   novo.setEstado(CBEstado.getSelectedItem().toString());
            novo.setEstadonaturalidade(CBEstadoDeNaturalidade.getSelectedItem().toString());
            novo.setEstadorg(CBEstadoRg.getSelectedItem().toString());
          //   novo.setMae(TFMae.getText());
            //   novo.setMunicipio(TFMunicipio.getText());
            novo.setNaturalidade(TFNaturalidade.getText());
            novo.setNome(Arrumador.comando().paraMaisculaComecoPalavra(TFNome.getText(),false));
            novo.setNomedeclarante(Arrumador.comando().paraMaisculaComecoPalavra(TFNome.getText(),false));
            novo.setNecessidadesespeciais(TFNecessidadeEspecial.getText());
            novo.setOrgaoexpedidorrg(TFOrgaoExpedidorRg.getText());
            //   novo.setPai(TFPai.getText());
            novo.setRg(TFRg.getText());
            if (TFSenha1.getText().length() > 0 && apresentadorTemSenha) {
                novo.setSenha(TFSenha1.getText());
            } else {
                novo.setSenha("sepin");
            }
            // if(!apresentadorTemSenha)
            //   novo.setSenha("abacaxi");

            novo.setSexo(CBSexo.getSelectedItem().toString());
            novo.setTelefone(TFTelefone1.getText());
            //     novo.setTipoconta(CBTipoConta.getSelectedItem().toString());
            novo.setUnidade(CBUnidade.getSelectedItem().toString());
            novo.setCodVinculo(codigoVinculos(CBVinculo.getSelectedItem().toString()));
            novo.setVinculo(CBVinculo.getSelectedItem().toString());
            novo.setObservacoes(TFObservacoes.getText());

          //   novo.setBolsaNome(CBBolsa.getSelectedItem().toString());
            //  novo.setBolsaCod(codigoBolsa(CBBolsa.getSelectedItem().toString()));
            if (novo.getUnidade().equals("-")) {
                Comunicador c = new Comunicador(this, "Nenhuma unidade selecionada. Selecione a(s)\nunidade a que pertence.", "Falha!");
                return;
            }//else if(novo.getDatadenascimento())

            boolean result;

            if (tipo.equals("UPDATE")) {

                result = novo.atualizarNaBase(TFSenha1.getText().toString().length() > 0);

                if (result) {
                    com = new Comunicador(JP, "Atualização realizada!", "Sucesso!");
                    Arrumador.comando().recuperarDadosUser(TFCpf.getText());
                    dispose();
                } else {
                    com = new Comunicador(this, "Não foi possível realizar a atualização!", "Falha!");
                }

            } else {  // novo

                result = novo.inserirNaBase();

                if (result) {
                    com = new Comunicador(JP, "Apresentador cadastrado!\n\n"+
                            "ATENÇÂO:\n\n"+
                            "Para cadastro da apresentação, é obrigatório incluir todos os participantes do projeto:\n"
                            + "Autores, co autores, orientadores e coorientadores."+
                            "\n\nAtt, Equipe do SEPIN", "Sucesso!");
                    
                    JP.criarJanelaGerenciamentoApresentacoesUmAluno(TFCpf.getText());
                    dispose();
                } else {
                    com = new Comunicador(this, "Não foi possível realizar o cadastro!", "Falha!");
                }

            }

        }   // fim do else em que a operação ocorre sem erros

    }//GEN-LAST:event_BTSalvarActionPerformed

    private void TFNomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TFNomeActionPerformed
        //  atualizaDec();
    }//GEN-LAST:event_TFNomeActionPerformed

    private void TFNomeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_TFNomeFocusLost
        //  atualizaDec();
    }//GEN-LAST:event_TFNomeFocusLost

    /**
     * Se for estrangeiro, permite salvar rne e passaporte
     *
     * @param evt
     */
    private void CBEstadoDeNaturalidadeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_CBEstadoDeNaturalidadeFocusLost

    }//GEN-LAST:event_CBEstadoDeNaturalidadeFocusLost

    private void CHTermoImagemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CHTermoImagemActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_CHTermoImagemActionPerformed

    private void CHTermoImagemItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_CHTermoImagemItemStateChanged
        BTSalvar.setEnabled(CHTermoImagem.isSelected());
    }//GEN-LAST:event_CHTermoImagemItemStateChanged

    private void TFEmailFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_TFEmailFocusLost

        if (!Arrumador.comando().ehUmEmail(TFEmail.getText())) {
            //TFEmail.setText("");
            Aviso.setText("Email de formato inválido");
        } else {
            Aviso.setText("Email ok");
        }

    }//GEN-LAST:event_TFEmailFocusLost

    private void TFTelefone1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_TFTelefone1FocusLost

        if (!Arrumador.comando().ehUmTelefone(TFTelefone1.getText())) {
            Aviso.setText("Telefone de formato inválido");
        } else {
            Aviso.setText("Telefone ok");
        }

    }//GEN-LAST:event_TFTelefone1FocusLost

    /**
     * Atualiza dados do declarante, de acordo com opçao escolhida
     *
     */
    /*public void atualizaDec(){
    
     if(iniciado)
     if(CBDeclarante.getSelectedItem().equals("Proprio")){
     TFCpfDeclarante.setEditable(false);
     TFCpfDeclarante.setText(TFCpf.getText());
     TFNomeDeclarante.setEditable(false);
     TFNomeDeclarante.setText(TFNome.getText());
     }
     else if(CBDeclarante.getSelectedItem().equals("Pai")){
       
     TFCpfDeclarante.setEditable(true);
     TFCpfDeclarante.setText("");
     TFNomeDeclarante.setEditable(false);
     TFNomeDeclarante.setText(TFPai.getText());
     }
       
     else if(CBDeclarante.getSelectedItem().equals("Mae")){
       
     TFCpfDeclarante.setEditable(true);
     TFCpfDeclarante.setText("");
     TFNomeDeclarante.setEditable(false);
     TFNomeDeclarante.setText(TFMae.getText());
     } else {
     TFCpfDeclarante.setEditable(true);
     TFCpfDeclarante.setText("");
     TFNomeDeclarante.setEditable(true);
     TFNomeDeclarante.setText("");
       
     }
    
    
        
            
        
        
     }*/
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
            java.util.logging.Logger.getLogger(CadastroApresentador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CadastroApresentador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CadastroApresentador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CadastroApresentador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
           @Override
            public void run() {
                //  new CadastrarAluno().setVisible(true);
            }
        });
    }

    public void setNome(String n) {
        TFNome.setText(n);
    }

    public void setSexo(String s) {
        CBSexo.setSelectedItem(s);
    }

    public void setEndereco(String end) {
        //TFEndereco.setText(end);
    }

    public void setTelefone1(String t) {
        TFTelefone1.setText(t);
    }

    public void setEmail(String t) {
        TFEmail.setText(t);
    }

    public void setPai(String t) {
        //TFPai.setText(t);
    }

    public void setMae(String t) {
        //TFMae.setText(t);
    }

    public void setDataNasc(String t) {
        TFDataDeNascimento.setText(t);
    }

    public void setRG(String t) {

        if (t == null) {
            return;
        }

        t = t.replace(".", "");
        t = t.replace("-", "");

        TFRg.setText(t);

    }

    public void setCPF(String t) {
        TFCpf.setText(t);
    }

    public void setEscolaridade(String e) {

        if (e == null) {
            return;
        }

        boolean fund, med, superior, inco;
        String teste = e.toUpperCase();

        superior = teste.contains("SUP");
        fund = teste.contains("FUND");
        med = teste.contains("DIO");

        inco = teste.contains("INC");

        if (fund) {
            teste = "Fundamental ";
        }
        if (med) {
            teste = "Médio ";
        }
        if (superior) {
            teste = "Superior ";
        }

        if (inco) {
            teste = teste + "incompleto";
        } else {
            teste = teste + "completo";
        }

        System.out.println(teste);

    }
    /* public void setCPFresp(String t){
    
        
     if(t==null)
     return;
        
     t=t.replace(".","");
     t=t.replace("-","");
        
     CBDeclarante.setSelectedItem("Outros");
     TFCpfDeclarante.setText(t);
    
     }
     public void setBanco(String t){
     TFBanco.setText(t);
     }
     public void setAgencia(String t){
     TFAgencia.setText(t);
     }
     public void setConta(String t){
     TFConta.setText(t);
     }
     */

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

    public int codigoUnidade(String nome) {

        for (int a = 0; a < unidades.size(); a++) {
            if (unidades.get(a).getNome().equals(nome)) {
                return unidades.get(a).getId();
            }
        }

        return -1;
    }

    public String nomeUnidade(int id) {

        for (int a = 0; a < unidades.size(); a++) {
            if (unidades.get(a).getId() == id) {
                return unidades.get(a).getNome();
            }
        }

        return "nulo";
    }

//3015
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea Aviso;
    private javax.swing.JToggleButton BTCancelar;
    private javax.swing.JToggleButton BTSalvar;
    private javax.swing.JComboBox CBEstadoDeNaturalidade;
    private javax.swing.JComboBox CBEstadoRg;
    private javax.swing.JTextField CBLocalDeTrabalho;
    private javax.swing.JComboBox CBSexo;
    private javax.swing.JComboBox CBUnidade;
    private javax.swing.JComboBox CBVinculo;
    private javax.swing.JCheckBox CHTermoImagem;
    private javax.swing.JLabel LDataNascimento;
    private javax.swing.JLabel LEstadoNaturalidade;
    private javax.swing.JLabel LNaturalidade;
    private javax.swing.JLabel LNecessidadeEspecial;
    private javax.swing.JLabel LOrgaoExpedidorDoRg;
    private javax.swing.JLabel LRg;
    private javax.swing.JLabel LRg1;
    private javax.swing.JLabel LSenha1;
    private javax.swing.JLabel LSenha2;
    private javax.swing.JLabel LTelefone;
    private javax.swing.JTextField TFCpf;
    private javax.swing.JFormattedTextField TFDataDeNascimento;
    private javax.swing.JTextField TFEmail;
    private javax.swing.JTextField TFNaturalidade;
    private javax.swing.JTextField TFNecessidadeEspecial;
    private javax.swing.JTextField TFNome;
    private javax.swing.JTextArea TFObservacoes;
    private javax.swing.JTextField TFOrgaoExpedidorRg;
    private javax.swing.JTextField TFRg;
    private javax.swing.JPasswordField TFSenha1;
    private javax.swing.JPasswordField TFSenha2;
    private javax.swing.JTextField TFTelefone1;
    private javax.swing.JFrame jFrame1;
    private javax.swing.JFrame jFrame2;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables

    private void recuperarDadosApresentador() {

        TFNome.setText(apresentador.getNome());
        TFRg.setText(apresentador.getRg());
        CBSexo.setSelectedItem(apresentador.getSexo());
        TFDataDeNascimento.setText(apresentador.getDatadenascimento());
        //   TFDataExpRg.setText(apresentador.getDataexprg());
        TFOrgaoExpedidorRg.setText(apresentador.getOrgaoexpedidorrg());
        CBEstadoRg.setSelectedItem(apresentador.getEstadorg());
        TFObservacoes.setText(apresentador.getObservacoes());
      //  TFPai.setText(apresentador.getPai());
        //  TFMae.setText(apresentador.getMae());
        TFNaturalidade.setText(apresentador.getNaturalidade());
        CBEstadoDeNaturalidade.setSelectedItem(apresentador.getEstadonaturalidade());
        TFEmail.setText(apresentador.getEmail());
        TFTelefone1.setText(apresentador.getTelefone());
     //   TFEndereco.setText(apresentador.getEndereco());
        //   TFMunicipio.setText(apresentador.getMunicipio());
        //   CBEstado.setSelectedItem(apresentador.getEstado());
        //    TFCep.setText(apresentador.getCep());
        //    TFBairro.setText(apresentador.getBairro());

     //   TFBanco.setText(apresentador.getBanco());
        //     TFAgencia.setText(apresentador.getAgencia());
        //     TFConta.setText(apresentador.getConta());
        //     CBTipoConta.setSelectedItem(apresentador.getTipoconta());
        TFNecessidadeEspecial.setText(apresentador.getNecessidadesespeciais());
     //   CBDeclarante.setSelectedItem(apresentador.getDeclarante());
        //    TFCpfDeclarante.setText(apresentador.getCpfdeclarante());

        CBUnidade.setSelectedItem(nomeUnidade(apresentador.getCodigoUnidade()));
        CBVinculo.setSelectedItem(nomeVinculo(apresentador.getCodVinculo()));
      //  CBBolsa.setSelectedItem(nomeBolsa(apresentador.getBolsaCod()));

    }

 /*   private int codigoBolsa(String nome) {
        for (int a = 0; a < bolsas.size(); a++) {
            if (bolsas.get(a).getNome().equals(nome)) {
                return bolsas.get(a).getId();
            }
        }

        return -1;
    }

    public String nomeBolsa(int id) {

        for (int a = 0; a < bolsas.size(); a++) {
            if (bolsas.get(a).getId() == id) {
                return bolsas.get(a).getNome();
            }
        }

        return "nulo";
    }*/

}
