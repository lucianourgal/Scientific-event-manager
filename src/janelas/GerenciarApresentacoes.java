/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package janelas;

import ClassesAuxiliares.Arrumador;
import ClassesAuxiliares.BancoDeDados;
import ClassesAuxiliares.Comunicador;
import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import objetosBase.apresentacao;
import objetosBase.apresentador;
import objetosBase.autor;
import objetosBase.triviais.area;
import objetosBase.triviais.bolsa;
import objetosBase.triviais.categoria;
import objetosBase.triviais.coordenador;
import objetosBase.triviais.evento;
import objetosBase.triviais.funcao;
import objetosBase.triviais.modalidade;
import objetosBase.triviais.subarea;
import objetosBase.triviais.unidade;

/**
 *
 * @author Serac02
 */
public class GerenciarApresentacoes extends javax.swing.JFrame {

    /**
     * Creates new form GerenciarApresentacoes
     */
    String cpf;
    Inicial JP;
    private apresentacao apresentacaoAtual;

    private ArrayList<bolsa> bolsas = new ArrayList<>();
    private ArrayList<modalidade> modalidades = new ArrayList<>();
   // private ArrayList<evento> eventos = new ArrayList<>();
    private ArrayList<area> areas = new ArrayList<>();
    private ArrayList<funcao> funcoes = new ArrayList<>();
    boolean iniciado = false;
    private ArrayList<subarea> subareas = new ArrayList<>();
    private ArrayList<autor> autores = new ArrayList<>();
    private ArrayList<unidade> unidades = new ArrayList<>();
    private unidade unidadePrincipal;
    private ArrayList<Integer> autoresAExcluir = new ArrayList<>();
    private ArrayList<String> nomesApresentacao = new ArrayList<>();
    private ArrayList<Integer> idsApresentacao = new ArrayList<>();
    
    private ArrayList<String> nomesIncluidos = new ArrayList<>();

    private CadastroApresentador cad;
    private ArrayList<coordenador> coordenacoes = new ArrayList<>();
    private final ArrayList<categoria> categorias;
    private boolean temResumo;
    private boolean areaTematica;
    private apresentador apresentador;
    private boolean aindaNaoClicouEmExcluir = true;
    private evento eventoP;
    private boolean usaCodigoParaCadastro; 
    
    public void seekndestroy() {
        try {
            cad.dispose();
        } catch (Exception e) {
        }
        dispose();
    }

    public GerenciarApresentacoes(Inicial jp1, String cpf1, ArrayList<coordenador> coord, evento e) {
        cpf = cpf1;
        coordenacoes = coord;
        eventoP =e;
        initComponents();
        
        if(coord.size()>0)  //janela de edição não usa código de cadastro
            usaCodigoParaCadastro = false;
        else    //janela de cadastro comum. Se o padrão do sistema é usar, e da unidade também.
            usaCodigoParaCadastro = BancoDeDados.comando().usaCodigoDeCadastro() && BancoDeDados.comando().unidadeDoApresentadorUsaCodigoParaCadastro(cpf);
        
        //eventos.add(e);
        TFNasc.setVisible(false);
        LNasc.setVisible(false);
        TFNascAtt.setVisible(false);
        LNascAtt.setVisible(false);
        JP = jp1;
        BTExcluir.setEnabled(false);

        LTicket.setVisible(usaCodigoParaCadastro);
        TFTicket.setVisible(usaCodigoParaCadastro);
        
        this.setTitle("Gerenciar apresentações ( " + JP.getVersao() + " )");
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setVisible(true);
        //this.setIconImage(Arrumador.comando().getIcone().getImage());
        
        LResumo.setText("Resumo (mínimo " + BancoDeDados.comando().getMinimoResumo() + ", máximo " + BancoDeDados.comando().getMaximoResumo() + " palavras)");
        if (coordenacoes.size() > 0) {
            BTEditarDadosPessoais.setVisible(false);
            
                    BTExcluir.setEnabled(true);

        } else if (BancoDeDados.comando().isBloqueioEdicaoGeralApresentacoes()) {
            BTSalvar.setVisible(false);
            LAviso.setText("Edição de apresentações desabilitada.");
            BTExcluir.setEnabled(BancoDeDados.comando().isCoordenadorCampusPodeExcluirApresentacao());
        }

        recuperarApresentacoes();

        CBCategoria.removeAllItems();
        categorias = BancoDeDados.comando().getCategorias();
        for (categoria c : categorias) {
            // if(c.getId()!=6)
            CBCategoria.addItem(c.getNome());
            //else if(coordenacoes.size()>0 || JP.temCoordenacaoGeral())
            //   if(coordenacoes.get(0).getCentralizado()==1 || JP.temCoordenacaoGeral())
            // else if (JP.temCoordenacaoGeral())
            //     CBCategoria.addItem(c.getNome());
        }

        CBAutor1Sexo.removeAllItems();
        CBSexoAtt.removeAllItems();
        CBAutor1Sexo.addItem("-");
        CBAutor1Sexo.addItem("M");
        CBAutor1Sexo.addItem("F");
        CBSexoAtt.addItem("-");
        CBSexoAtt.addItem("M");
        CBSexoAtt.addItem("F");
        
        CBInclusao.removeAllItems();
        CBInclusao.addItem("-");
        CBInclusao.addItem("Não");
        CBInclusao.addItem("Sim");
        CBDiversidade.removeAllItems();
        CBDiversidade.addItem("-");
        CBDiversidade.addItem("Não");
        CBDiversidade.addItem("Sim");

        CBAutor1Unidade.removeAllItems();
        CBAutor1Unidade.addItem("-");
        CBAutorUnidadeAt.removeAllItems();
        unidades = BancoDeDados.comando().getUnidades();
        for (int a = 0; a < unidades.size(); a++) {
            CBAutor1Unidade.addItem(unidades.get(a).getNome());
            CBAutorUnidadeAt.addItem(unidades.get(a).getNome());
            CBAutor1Unidade.setSelectedItem(nomeUnidade(Arrumador.comando().getUserApresentador().getCodigoUnidade()));
            CBAutorUnidadeAt.setSelectedItem(nomeUnidade(Arrumador.comando().getUserApresentador().getCodigoUnidade()));

            if (coord != null) {
                if (coord.size() > 0) {
                    if (unidades.get(a).getId() == coord.get(0).getUnidadeCodigo()) {
                        unidadePrincipal = unidades.get(a);
                    }
                } else if (unidades.get(a).getId() == Arrumador.comando().getUserApresentador().getCodigoUnidade()) {
                    unidadePrincipal = unidades.get(a);
                }

            } else if (unidades.get(a).getId() == Arrumador.comando().getUserApresentador().getCodigoUnidade()) {
                unidadePrincipal = unidades.get(a);
            }

        }

        preencherAreas(true, false);

        CBBolsa.removeAllItems();
        CBAutor1Bolsa.removeAllItems();
        CBAutor1Bolsa.addItem("-");
        CBAutorBolsaAt.removeAllItems();
        bolsas = BancoDeDados.comando().getBolsas();
        CBBolsa.addItem("-");
        for (int a = 0; a < bolsas.size(); a++) {
            CBBolsa.addItem(bolsas.get(a).getNome());
            CBAutor1Bolsa.addItem(bolsas.get(a).getNome());
            CBAutorBolsaAt.addItem(bolsas.get(a).getNome());
        }
        CBAutor1Funcao.removeAllItems();
        CBAutor1Funcao.addItem("-");
        CBAutorFuncaoAt.removeAllItems();
        funcoes = BancoDeDados.comando().getFuncoes();
        for (int a = 0; a < funcoes.size(); a++) {
            CBAutor1Funcao.addItem(funcoes.get(a).getNome());
            CBAutorFuncaoAt.addItem(funcoes.get(a).getNome());
        }

        CBModalidade.removeAllItems();
        modalidades = BancoDeDados.comando().getModalidades();
        CBModalidade.addItem("-");
        for (int a = 0; a < modalidades.size(); a++) {
            CBModalidade.addItem(modalidades.get(a).getNome());
        }
        CBModalidade.setToolTipText("No evento centralizado, 'Comunicação oral' deverá também incluir apresentação de POSTER");

        CBEvento.removeAllItems();
        //  eventos = BancoDeDados.comando().getEventos();
      //  for (int a = 0; a < eventos.size(); a++) {
            CBEvento.addItem(eventoP.getNome());
       // }

        CBTematica.removeAllItems();
        CBTematica.addItem("-");
        //tematicas = BancoDeDados.comando().getTematicas();
        for (int a = 0; a < areas.size(); a++) {
            if (areas.get(a).isTematica()) {
                CBTematica.addItem(areas.get(a).getNome());
            }
        }
        CBTematica.setEnabled(false);

        CBSubArea.setEnabled(false);
        LSubArea.setVisible(false);

        iniciado = true;

        try {
            apresentador = BancoDeDados.comando().getApresentador(cpf1);
            apresentacaoAtual = new apresentacao();
            apresentacaoAtual.setApresentadorEfetivoNome(apresentador.getNome());
            apresentacaoAtual.setApresentadorNome(apresentador.getNome());
            apresentacaoAtual.setApresentadorEmail(apresentador.getEmail());
            apresentacaoAtual.setUnidadeNome(apresentador.getUnidade());
            apresentacaoAtual.setApresentadorCpf(cpf1);
            atualizarTabelaAutores();
            System.out.println("Atualizando tabela com o apresentador... " + cpf1);
        } catch (Exception ex) {
            System.out.println("Não foi possivel recuperar dados de apresentador de CPF " + cpf1);
        }

        atualizaListaSubAreas();

        atualizarCBAutores();
        ajustaMudancaCategoria();
        verificarModalidade();

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

        TFCpfAtt.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char vChar = e.getKeyChar();
                if (!(Character.isDigit(vChar)
                        || (vChar == KeyEvent.VK_BACK_SPACE)
                        || (vChar == KeyEvent.VK_DELETE))) {
                    e.consume();
                }

                if (TFCpfAtt.getText().length() <= 10) {
                    //deixe passar  
                } else {
                    e.setKeyChar((char) KeyEvent.VK_CLEAR);
                }

            }
        });
        
        TFRg1.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char vChar = e.getKeyChar();
                if (!(Character.isDigit(vChar)
                        || (vChar == KeyEvent.VK_BACK_SPACE)
                        || (vChar == KeyEvent.VK_DELETE))) {
                    e.consume();
                }
                if (TFRg1.getText().length() <= 11) {
                    //deixe passar  
                } else {
                    e.setKeyChar((char) KeyEvent.VK_CLEAR);
                }
            }
        });
        
        TFRgAtt1.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char vChar = e.getKeyChar();
                if (!(Character.isDigit(vChar)
                        || (vChar == KeyEvent.VK_BACK_SPACE)
                        || (vChar == KeyEvent.VK_DELETE))) {
                    e.consume();
                }
                if (TFRgAtt1.getText().length() <= 11) {
                    //deixe passar  
                } else {
                    e.setKeyChar((char) KeyEvent.VK_CLEAR);
                }
            }
        });

        if (coordenacoes.size() > 0) {
            carregarApresentacao();
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

        CBApresentacao = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        BTEditarDadosPessoais = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        CBEvento = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        TFNome = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        CBModalidade = new javax.swing.JComboBox();
        LGrupo = new javax.swing.JLabel();
        TFNomeGrupo = new javax.swing.JTextField();
        TFMateriais = new javax.swing.JTextField();
        LMateriais = new javax.swing.JLabel();
        CBSubArea = new javax.swing.JComboBox();
        LSubArea = new javax.swing.JLabel();
        CBArea = new javax.swing.JComboBox();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        TFPalavrasChave = new javax.swing.JTextField();
        LResumo = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        TFResumo = new javax.swing.JTextPane();
        jLabel11 = new javax.swing.JLabel();
        CBBolsa = new javax.swing.JComboBox();
        jLabel12 = new javax.swing.JLabel();
        TFAutor1Nome = new javax.swing.JTextField();
        TFAutor1Email = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        CBAutor1Unidade = new javax.swing.JComboBox();
        CBAutor1Funcao = new javax.swing.JComboBox();
        LAviso = new javax.swing.JLabel();
        BTSair = new javax.swing.JButton();
        jLabel29 = new javax.swing.JLabel();
        CBAutor1Bolsa = new javax.swing.JComboBox();
        jScrollPane2 = new javax.swing.JScrollPane();
        TAutores = new javax.swing.JTable();
        BTAdicionarAutor = new javax.swing.JButton();
        jLabel30 = new javax.swing.JLabel();
        CBAutorBolsaAt = new javax.swing.JComboBox();
        BTAtualizarAutor = new javax.swing.JButton();
        jLabel16 = new javax.swing.JLabel();
        TFNomeAutorAt = new javax.swing.JTextField();
        TFEmailAutorAt = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        CBAutorUnidadeAt = new javax.swing.JComboBox();
        CBAutorFuncaoAt = new javax.swing.JComboBox();
        CBNomeAutorAt = new javax.swing.JComboBox();
        BTSalvar = new javax.swing.JButton();
        LResult = new javax.swing.JLabel();
        BTExcluirAutor = new javax.swing.JButton();
        TFCpf = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        TFCpfAtt = new javax.swing.JTextField();
        CBAutor1Sexo = new javax.swing.JComboBox();
        jLabel31 = new javax.swing.JLabel();
        TFNasc = new javax.swing.JFormattedTextField();
        LNasc = new javax.swing.JLabel();
        CBSexoAtt = new javax.swing.JComboBox();
        jLabel32 = new javax.swing.JLabel();
        TFNascAtt = new javax.swing.JFormattedTextField();
        LNascAtt = new javax.swing.JLabel();
        LVideo = new javax.swing.JLabel();
        TFVideoApresentacao = new javax.swing.JTextField();
        CBTematica = new javax.swing.JComboBox();
        jLabel24 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        CBCategoria = new javax.swing.JComboBox();
        BTMudarApresentador = new javax.swing.JButton();
        BTExcluir = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        LTicket = new javax.swing.JLabel();
        TFTicket = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        CBInclusao = new javax.swing.JComboBox<>();
        CBDiversidade = new javax.swing.JComboBox<>();
        jLabel23 = new javax.swing.JLabel();
        TFRgAtt1 = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        TFRg1 = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        CBApresentacao.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        CBApresentacao.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                CBApresentacaoItemStateChanged(evt);
            }
        });
        CBApresentacao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CBApresentacaoActionPerformed(evt);
            }
        });

        jLabel1.setText("Apresentação");

        BTEditarDadosPessoais.setText("Editar dados pessoais");
        BTEditarDadosPessoais.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTEditarDadosPessoaisActionPerformed(evt);
            }
        });

        jLabel2.setText("Evento");

        CBEvento.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        CBEvento.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                CBEventoItemStateChanged(evt);
            }
        });

        jLabel3.setText("Título do trabalho");

        TFNome.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                TFNomeFocusGained(evt);
            }
        });

        jLabel4.setForeground(new java.awt.Color(204, 0, 0));
        jLabel4.setText("Modalidade");

        CBModalidade.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        CBModalidade.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                CBModalidadeItemStateChanged(evt);
            }
        });

        LGrupo.setText("Nome do Grupo ");

        LMateriais.setText("Materiais a serem utilizados ");

        CBSubArea.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        LSubArea.setText("Area");

        CBArea.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        CBArea.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                CBAreaItemStateChanged(evt);
            }
        });

        jLabel8.setText("Grande Area");

        jLabel9.setText("Palavras Chave (separar por ponto e vírgula. Mínimo 3, máximo 5)");

        TFPalavrasChave.setToolTipText("Separar por ponto e vírgula");
        TFPalavrasChave.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                TFPalavrasChaveFocusGained(evt);
            }
        });

        LResumo.setText("Resumo (mínimo 200, máximo 500 palavras)");

        TFResumo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                TFResumoFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                TFResumoFocusLost(evt);
            }
        });
        jScrollPane1.setViewportView(TFResumo);

        jLabel11.setForeground(new java.awt.Color(204, 0, 0));
        jLabel11.setText("Programa/bolsa do apresentador neste projeto");

        CBBolsa.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        CBBolsa.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                CBBolsaItemStateChanged(evt);
            }
        });

        jLabel12.setText("Nome");

        TFAutor1Email.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TFAutor1EmailActionPerformed(evt);
            }
        });

        jLabel13.setText("Email");

        jLabel14.setText("Função");

        jLabel15.setText("Campus");

        CBAutor1Unidade.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        CBAutor1Funcao.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        LAviso.setBackground(new java.awt.Color(204, 204, 0));
        LAviso.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        LAviso.setForeground(new java.awt.Color(102, 0, 102));
        LAviso.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        LAviso.setText("Cadastro dos demais participantes do trabalho");

        BTSair.setForeground(new java.awt.Color(204, 0, 0));
        BTSair.setText("Sair");
        BTSair.setToolTipText("Sair sem salvar");
        BTSair.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTSairActionPerformed(evt);
            }
        });

        jLabel29.setText("Bolsa/programa");

        CBAutor1Bolsa.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        TAutores.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Nome", "Email", "Funcao", "Unidade", "Bolsa/prog."
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        TAutores.setToolTipText("Dados dos autores relacionados a essa apresentação. Autores não cadastrados aqui não farão parte dos anais e dos certificados.");
        jScrollPane2.setViewportView(TAutores);

        BTAdicionarAutor.setForeground(new java.awt.Color(51, 0, 204));
        BTAdicionarAutor.setText("Adicionar");
        BTAdicionarAutor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTAdicionarAutorActionPerformed(evt);
            }
        });

        jLabel30.setText("Bolsa/programa");

        CBAutorBolsaAt.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        BTAtualizarAutor.setForeground(new java.awt.Color(51, 0, 204));
        BTAtualizarAutor.setText("Atualizar autor");
        BTAtualizarAutor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTAtualizarAutorActionPerformed(evt);
            }
        });

        jLabel16.setText("Nome");

        jLabel17.setText("Email");

        jLabel18.setText("Função");

        jLabel19.setText("Campus");

        CBAutorUnidadeAt.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        CBAutorFuncaoAt.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        CBNomeAutorAt.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        CBNomeAutorAt.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                CBNomeAutorAtItemStateChanged(evt);
            }
        });

        BTSalvar.setForeground(new java.awt.Color(0, 102, 0));
        BTSalvar.setText("Salvar apresentação");
        BTSalvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTSalvarActionPerformed(evt);
            }
        });

        LResult.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        LResult.setForeground(new java.awt.Color(153, 0, 0));
        LResult.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        LResult.setText(".");

        BTExcluirAutor.setForeground(new java.awt.Color(102, 0, 204));
        BTExcluirAutor.setText("Excluir autor");
        BTExcluirAutor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTExcluirAutorActionPerformed(evt);
            }
        });

        TFCpf.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        TFCpf.setToolTipText("Não obrigatório");
        TFCpf.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                TFCpfFocusLost(evt);
            }
        });

        jLabel20.setText("CPF");

        jLabel21.setText("CPF");

        TFCpfAtt.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        TFCpfAtt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                TFCpfAttFocusLost(evt);
            }
        });

        CBAutor1Sexo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        CBAutor1Sexo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CBAutor1SexoActionPerformed(evt);
            }
        });

        jLabel31.setText("Sexo");

        TFNasc.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter()));

        LNasc.setText("Nascimento");

        CBSexoAtt.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel32.setText("Sexo");

        TFNascAtt.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter()));

        LNascAtt.setText("Nascimento");

        LVideo.setText("Video");

        TFVideoApresentacao.setToolTipText("Obrigatório: Link para video da apresentação, se cultural. ");

        CBTematica.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        CBTematica.setToolTipText("Somente para extensão");

        jLabel24.setText("Area Temática");

        jLabel5.setForeground(new java.awt.Color(204, 0, 0));
        jLabel5.setText("Categoria");

        CBCategoria.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        CBCategoria.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                CBCategoriaItemStateChanged(evt);
            }
        });

        BTMudarApresentador.setText("Esse autor é agora o apresentador");
        BTMudarApresentador.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTMudarApresentadorActionPerformed(evt);
            }
        });

        BTExcluir.setBackground(new java.awt.Color(204, 204, 204));
        BTExcluir.setForeground(new java.awt.Color(102, 0, 0));
        BTExcluir.setText("Excluir Apresentação");
        BTExcluir.setToolTipText("Somente a coordenação pode excluir um trabalho cadastrado");
        BTExcluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTExcluirActionPerformed(evt);
            }
        });

        jLabel6.setBackground(java.awt.SystemColor.controlHighlight);
        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 153, 0));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Edição de dados de partipantes do trabalho");

        LTicket.setText("Código para cadastro (ticket)");

        jLabel10.setText("O projeto promove inclusão?");

        jLabel22.setText("O projeto promove diversidade?");

        CBInclusao.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        CBInclusao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CBInclusaoActionPerformed(evt);
            }
        });

        CBDiversidade.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        CBDiversidade.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CBDiversidadeActionPerformed(evt);
            }
        });

        jLabel23.setText("RG");

        TFRgAtt1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        TFRgAtt1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                TFRgAtt1FocusLost(evt);
            }
        });

        jLabel25.setText("RG");

        TFRg1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        TFRg1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                TFRg1FocusLost(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(8, 8, 8)
                                .addComponent(CBModalidade, javax.swing.GroupLayout.PREFERRED_SIZE, 420, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(4, 4, 4)
                                .addComponent(TFNome, javax.swing.GroupLayout.PREFERRED_SIZE, 381, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(LMateriais)
                                .addGap(4, 4, 4)
                                .addComponent(TFMateriais, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(LGrupo, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(8, 8, 8)
                                .addComponent(TFNomeGrupo, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(1, 1, 1)
                                .addComponent(LVideo)
                                .addGap(4, 4, 4)
                                .addComponent(TFVideoApresentacao, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 510, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(TFPalavrasChave, javax.swing.GroupLayout.PREFERRED_SIZE, 508, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(4, 4, 4)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(12, 12, 12)
                                        .addComponent(CBArea, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(10, 10, 10)
                                        .addComponent(LSubArea)
                                        .addGap(8, 8, 8)
                                        .addComponent(CBSubArea, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(4, 4, 4)
                                        .addComponent(CBTematica, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 287, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(CBBolsa, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(LResumo, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(11, 11, 11)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel12)
                                        .addGap(10, 10, 10)
                                        .addComponent(TFAutor1Nome, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(10, 10, 10)
                                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(10, 10, 10)
                                        .addComponent(CBAutor1Unidade, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(12, 12, 12)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(LNasc, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(4, 4, 4)
                                                .addComponent(TFNasc, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(20, 20, 20)
                                                .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(10, 10, 10)
                                                .addComponent(CBAutor1Sexo, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel13)
                                                .addGap(10, 10, 10)
                                                .addComponent(TFAutor1Email, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(10, 10, 10)
                                                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(10, 10, 10)
                                                .addComponent(CBAutor1Funcao, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel20)
                                                .addGap(15, 15, 15)
                                                .addComponent(TFCpf, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(10, 10, 10)
                                                .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(10, 10, 10)
                                                .addComponent(CBAutor1Bolsa, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(16, 16, 16)
                                                .addComponent(TFRg1, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(BTAdicionarAutor, javax.swing.GroupLayout.PREFERRED_SIZE, 287, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGap(43, 43, 43))))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane2)
                                .addContainerGap())))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel1)
                                    .addComponent(CBApresentacao, javax.swing.GroupLayout.PREFERRED_SIZE, 619, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(CBEvento, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addComponent(BTEditarDadosPessoais, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(4, 4, 4)
                                .addComponent(CBCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 420, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(LAviso, javax.swing.GroupLayout.PREFERRED_SIZE, 556, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 12, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 508, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jLabel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGap(18, 18, 18)
                                    .addComponent(CBDiversidade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(CBInclusao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(LTicket, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(TFTicket, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(6, 6, 6)
                                        .addComponent(TFEmailAutorAt, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(17, 17, 17)
                                        .addComponent(CBAutorBolsaAt, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(16, 16, 16)
                                                .addComponent(TFRgAtt1, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(TFCpfAtt, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGap(16, 16, 16)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addComponent(LNascAtt)
                                                .addGap(53, 53, 53)
                                                .addComponent(TFNascAtt, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addComponent(jLabel32)
                                                .addGap(6, 6, 6)
                                                .addComponent(CBSexoAtt, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(3, 3, 3)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 518, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addGroup(layout.createSequentialGroup()
                                                    .addComponent(CBNomeAutorAt, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                    .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addGap(30, 30, 30)
                                                    .addComponent(CBAutorUnidadeAt, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGroup(layout.createSequentialGroup()
                                                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addGap(3, 3, 3)
                                                    .addComponent(TFNomeAutorAt, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addGap(68, 68, 68)
                                                    .addComponent(CBAutorFuncaoAt, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addComponent(BTExcluir, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(BTSalvar, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(22, 22, 22)
                                                .addComponent(BTSair, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addComponent(LResult, javax.swing.GroupLayout.PREFERRED_SIZE, 520, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(1, 1, 1))))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addComponent(BTMudarApresentador, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(BTExcluirAutor, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(BTAtualizarAutor, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap())))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(6, 6, 6)
                        .addComponent(CBApresentacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(6, 6, 6)
                        .addComponent(CBEvento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addComponent(BTEditarDadosPessoais, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel5))
                    .addComponent(CBCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(LAviso))
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(3, 3, 3)
                                .addComponent(jLabel11))
                            .addComponent(CBBolsa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(26, 26, 26)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(3, 3, 3)
                                .addComponent(jLabel4))
                            .addComponent(CBModalidade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(11, 11, 11)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(3, 3, 3)
                                .addComponent(jLabel3))
                            .addComponent(TFNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(6, 6, 6)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(TFMateriais, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(TFVideoApresentacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(3, 3, 3)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(LMateriais)
                                    .addComponent(LGrupo)
                                    .addComponent(TFNomeGrupo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(LVideo))))
                        .addGap(3, 3, 3)
                        .addComponent(jLabel9)
                        .addGap(6, 6, 6)
                        .addComponent(TFPalavrasChave, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(3, 3, 3)
                                .addComponent(jLabel8))
                            .addComponent(CBArea, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(7, 7, 7)
                                .addComponent(LSubArea, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(3, 3, 3)
                                .addComponent(CBSubArea, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(5, 5, 5)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(4, 4, 4)
                                .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(CBTematica, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(16, 16, 16)
                        .addComponent(LResumo))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(TFAutor1Nome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(CBAutor1Unidade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(3, 3, 3)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel12)
                                    .addComponent(jLabel15))))
                        .addGap(6, 6, 6)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(TFAutor1Email, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(CBAutor1Funcao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(3, 3, 3)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel13)
                                    .addComponent(jLabel14))))
                        .addGap(6, 6, 6)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(TFCpf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(3, 3, 3)
                                .addComponent(jLabel29))
                            .addComponent(CBAutor1Bolsa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(6, 6, 6)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(LNasc, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(TFNasc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(4, 4, 4)
                                .addComponent(jLabel31))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addComponent(CBAutor1Sexo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(7, 7, 7)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(TFRg1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(BTAdicionarAutor))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(CBInclusao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel22)
                            .addComponent(CBDiversidade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(10, 10, 10)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(LTicket, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(TFTicket, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(CBNomeAutorAt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel19))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(2, 2, 2)
                                .addComponent(CBAutorUnidadeAt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(8, 8, 8)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel16)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(TFNomeAutorAt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel18))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(2, 2, 2)
                                .addComponent(CBAutorFuncaoAt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(5, 5, 5)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel17)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(TFEmailAutorAt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel30))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(5, 5, 5)
                                .addComponent(CBAutorBolsaAt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(TFNascAtt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(TFCpfAtt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(LNascAtt, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel32)
                                    .addComponent(CBSexoAtt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(TFRgAtt1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(BTMudarApresentador)
                            .addComponent(BTAtualizarAutor)
                            .addComponent(BTExcluirAutor))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(LResult, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(BTExcluir, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(BTSalvar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(BTSair, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BTEditarDadosPessoaisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTEditarDadosPessoaisActionPerformed
        cad = new CadastroApresentador("UPDATE", JP, cpf,BancoDeDados.comando().apresentadorTemSenha());
    }//GEN-LAST:event_BTEditarDadosPessoaisActionPerformed

    private void CBAreaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_CBAreaItemStateChanged

        if (!iniciado) {
            return;
        }
        atualizaListaSubAreas();

    }//GEN-LAST:event_CBAreaItemStateChanged

    public void atualizaListaSubAreas() {
        if (!iniciado || CBArea.getSelectedIndex() == -1) {
            return;
        }

        subareas = getSubAreasDaArea(CBArea.getSelectedItem().toString());

        CBSubArea.removeAllItems();
        for (subarea subarea : subareas) {
            CBSubArea.addItem(subarea.getNome());
        }
    }

    private void BTSairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTSairActionPerformed

        try {
            // BancoDeDados.comando().fecharConexao();
            // JP.dispose();
            this.dispose();
        } catch (Exception e) {
        }

    }//GEN-LAST:event_BTSairActionPerformed

    private boolean cpfLivreParaAutor(String c, int n) {

        if (c.equals(cpf)) // se é o mesmo do apresentador principal, não pode
        {
            return false;
        }

        if (c.length() < 3) // se for em branco, passa
        {
            return true;
        }

        for (int a = 0; a < autores.size(); a++) {  // de autor em autor salvo
            if (a != n && c.equals(autores.get(a).getCpf())) // se não for o mesmo, e os cpfs forem iguais, não pode
            {
                return false;
            }
        }

        return true; // não encontrou problemas
    }


    private void BTAdicionarAutorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTAdicionarAutorActionPerformed
        //não permite nomes duplicados
        //não permite caso não tenha nome
        if (TFAutor1Nome.getText().toString().length() < 1) {
            System.out.println("Nome de autor não preenchido");
            LAviso.setText("Cadastro de autores (FALHA AO INCLUIR: Nome não informado)");
            LAviso.setBackground(Color.red);
            BTAdicionarAutor.setText("Adicionar Autor (leia erro acima)");
            return;
        }
        
        String cpf = TFCpf.getText().replace("-", "").replace(".", "");
        
        if (cpf.length() < 6) {
            System.out.println("CPF de autor não preenchido");
            LAviso.setText("Cadastro de autores (FALHA AO INCLUIR: CPF não informado)");
            LAviso.setBackground(Color.red);
            BTAdicionarAutor.setText("Adicionar Autor (leia erro acima)");
            return;
        }
        
        if (TFRg1.getText().toString().length() < 4) {
            System.out.println("RG de autor não preenchido");
            LAviso.setText("Cadastro de autores (FALHA AO INCLUIR: RG não informado)");
            LAviso.setBackground(Color.red);
            BTAdicionarAutor.setText("Adicionar Autor (leia erro acima)");
            return;
        }
        
        
        if (nomeAutorJaExiste(TFAutor1Nome.getText())) {
            System.out.println("Nome de autor já existe");
            LAviso.setText("Cadastro de autores (FALHA AO INCLUIR: Nome já existe na apresentação)");
            LAviso.setBackground(Color.red);
            BTAdicionarAutor.setText("Adicionar Autor (leia erro acima)");
            return;
        }
        if (!cpfLivreParaAutor(cpf, -1)) {
            System.out.println("CPF já existe na apresentação");
            LAviso.setText("Cadastro de autores (FALHA AO INCLUIR: CPF já existe na apresentação)");
            LAviso.setBackground(Color.red);
            BTAdicionarAutor.setText("Adicionar Autor (leia erro acima)");
            return;
        }

        if (CBAutor1Unidade.getSelectedIndex() < 1) {
            LAviso.setText("Cadastro de autores (FALHA AO INCLUIR: Não selecionou Campus do novo autor)");
            LAviso.setBackground(Color.red);
            BTAdicionarAutor.setText("Adicionar Autor (leia erro acima)");
            return;
        }

        if (CBAutor1Funcao.getSelectedIndex() < 1) {
            LAviso.setText("Cadastro de autores (FALHA: Não selecionou Função do novo autor)");
            LAviso.setBackground(Color.red);
            BTAdicionarAutor.setText("Adicionar Autor (leia erro acima)");
            return;
        }
        if (CBAutor1Bolsa.getSelectedIndex() < 1) {
            LAviso.setText("Cadastro de autores (FALHA: Não selecionou Bolsa/Programa do novo autor)");
            LAviso.setBackground(Color.red);
            BTAdicionarAutor.setText("Adicionar Autor (leia erro acima)");
            return;
        }

        if (!Arrumador.comando().ehUmEmail(this.TFAutor1Email.getText())) {
            LAviso.setText("Cadastro de autores (FALHA: E-mail inválido. É obrigatório)");
            LAviso.setBackground(Color.red);
            BTAdicionarAutor.setText("Adicionar Autor (leia erro acima)");
            return;
        }
        
        
        
        LAviso.setText("Cadastro dos demais participantes do trabalho");
        LAviso.setBackground(Color.LIGHT_GRAY);

        autor novo = new autor();

        if (Arrumador.comando().validarCPF(cpf)) {
            novo.setCpf(cpf);
        }
        novo.setId(-1);
        novo.setBolsaNome(CBAutor1Bolsa.getSelectedItem().toString());
        novo.setBolsaid(codigoBolsa(CBAutor1Bolsa.getSelectedItem().toString()));
        novo.setEmail(TFAutor1Email.getText());
        novo.setFuncaoNome(CBAutor1Funcao.getSelectedItem().toString());
        novo.setFuncaoCod(codigoFuncao(CBAutor1Funcao.getSelectedItem().toString()));
        novo.setNome(Arrumador.comando().paraMaisculaComecoPalavra(TFAutor1Nome.getText(), false));
        novo.setUnidadeNome(CBAutor1Unidade.getSelectedItem().toString());
        novo.setUnidadecodigo(codigoUnidade(CBAutor1Unidade.getSelectedItem().toString()));
        novo.setSexo(CBAutor1Sexo.getSelectedItem().toString());
        novo.setNascimento(TFNasc.getText());
        if(TFRg1.getText().length()<13)
            novo.setRg(TFRg1.getText());
        else
            novo.setRg(TFRg1.getText().substring(0, 12));
        novo.setNovo(true);

        TFAutor1Nome.setText("");
        TFAutor1Email.setText("");
        TFCpf.setText("");
        TFRg1.setText("");
        CBAutor1Unidade.setSelectedIndex(0);
        CBAutor1Funcao.setSelectedIndex(0);
        CBAutor1Bolsa.setSelectedIndex(0);
        CBAutor1Sexo.setSelectedIndex(0);

        autores.add(novo);
        atualizarTabelaAutores();
        
        BTAdicionarAutor.setText("Adicionar Autor");
    }//GEN-LAST:event_BTAdicionarAutorActionPerformed

    private void BTAtualizarAutorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTAtualizarAutorActionPerformed

        int index = CBNomeAutorAt.getSelectedIndex();

        if (!cpfLivreParaAutor(TFCpfAtt.getText(), index)) {
            System.out.println("CPF já existe na apresentação");
            LAviso.setText("Outros autores do projeto (Falha ao alterar: Cpf já existe na apresentação)");
            LAviso.setBackground(Color.red);
            return;
        }

        autores.get(index).setNome(Arrumador.comando().paraMaisculaComecoPalavra(TFNomeAutorAt.getText(), false));
        autores.get(index).setBolsaNome(CBAutorBolsaAt.getSelectedItem().toString());
        autores.get(index).setBolsaid(codigoBolsa(CBAutorBolsaAt.getSelectedItem().toString()));
        autores.get(index).setEmail(TFEmailAutorAt.getText());
        autores.get(index).setFuncaoNome(CBAutorFuncaoAt.getSelectedItem().toString());
        autores.get(index).setFuncaoCod(codigoFuncao(CBAutorFuncaoAt.getSelectedItem().toString()));
        autores.get(index).setUnidadeNome(CBAutorUnidadeAt.getSelectedItem().toString());
        autores.get(index).setUnidadecodigo(codigoUnidade(CBAutorUnidadeAt.getSelectedItem().toString()));
        if (Arrumador.comando().validarCPF(TFCpfAtt.getText())) {
            autores.get(index).setCpf(TFCpfAtt.getText());
        }
        autores.get(index).setSexo(CBSexoAtt.getSelectedItem().toString());
        autores.get(index).setNascimento(TFNascAtt.getText());
        if(TFRgAtt1.getText().length()<13)
            autores.get(index).setRg(this.TFRgAtt1.getText());
        else
            autores.get(index).setRg(this.TFRgAtt1.getText().substring(0, 12));

        atualizarTabelaAutores();
    }//GEN-LAST:event_BTAtualizarAutorActionPerformed

    private void BTSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTSalvarActionPerformed


        if (!iniciado) {
            LResult.setText("Inserção durante carregamento cancelada.");
            return;            
        }
        
        if (CBModalidade.getSelectedIndex() == -1) {
            LResult.setText("Selecione a modalidade! Ação cancelada.");
            return;

        }
        
        if (CBInclusao.getSelectedItem().equals("-")) {
            LResult.setText("Selecione campo sobre inclusão.");
            return;           
        }  
        
        if (CBDiversidade.getSelectedItem().equals("-")) {
            LResult.setText("Selecione campo sobre diversidade.");
            return;           
        }  
        

        if (!ativarLimiteModalidade()) {
            System.out.println("RETURN: !ativarLimiteModalidades (BTSalvarAction)");
            return;
        }

        deixarBackgroundWhite();
        
        if (CBApresentacao.getSelectedItem().toString().equals("Nova")) {
            inserirApresentacao();
        } else {
            atualizarApresentacao();
        }
       
    }//GEN-LAST:event_BTSalvarActionPerformed

    private void CBApresentacaoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_CBApresentacaoItemStateChanged
        carregarApresentacao();
        LAviso.setText("Cadastro dos demais participantes do trabalho");
        BTExcluir.setText("Excluir apresentação");
        aindaNaoClicouEmExcluir = true;
        LResult.setText(".");
        
        if(iniciado){
        LTicket.setVisible(usaCodigoParaCadastro && CBApresentacao.getSelectedItem().toString().equals("Nova"));
        TFTicket.setVisible(usaCodigoParaCadastro && CBApresentacao.getSelectedItem().toString().equals("Nova"));
        }
        
    }//GEN-LAST:event_CBApresentacaoItemStateChanged

    private void carregarApresentacao() {
        
        if (!iniciado) {
            return;
        }

        if (CBApresentacao.getSelectedIndex() == -1) {
            return;
        }

        if (CBApresentacao.getSelectedItem().toString().equals("Nova")) {

            BTSalvar.setText("Salvar apresentação");
            limparCampos();
        } else {

            BTSalvar.setText("Atualizar apresentação");
            recuperarDadosApresentacao();
            if (coordenacoes.size() > 0) {
                Arrumador.comando().recuperarDadosUser(apresentacaoAtual.getApresentadorCpf());
                cpf = apresentacaoAtual.getApresentadorCpf();
                CBApresentacao.setToolTipText("Atual apresentador: " + cpf);
            }

        }
        //BTMudarApresentador.setEnabled(false);
        verificarModalidade();
        verificarBolsa();
    }

    private void CBNomeAutorAtItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_CBNomeAutorAtItemStateChanged
        recuperarDadosAutorSelecionado();
    }//GEN-LAST:event_CBNomeAutorAtItemStateChanged

    private void CBApresentacaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CBApresentacaoActionPerformed

    }//GEN-LAST:event_CBApresentacaoActionPerformed

    private void BTExcluirAutorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTExcluirAutorActionPerformed

        if (autores == null) {
            return;
        }

        if (autores.get(CBNomeAutorAt.getSelectedIndex()).getId() != -1) {
            autoresAExcluir.add(autores.get(CBNomeAutorAt.getSelectedIndex()).getId());
        }

        autores.remove(autores.get(CBNomeAutorAt.getSelectedIndex()));
        atualizarTabelaAutores();
    }//GEN-LAST:event_BTExcluirAutorActionPerformed

    private void TFResumoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_TFResumoFocusLost

        LResumo.setText("Resumo (mínimo " + BancoDeDados.comando().getMinimoResumo() + ", máximo " + BancoDeDados.comando().getMaximoResumo() + " palavras). Atual: " + contarNumeroPalavras());

    }//GEN-LAST:event_TFResumoFocusLost

    private void CBModalidadeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_CBModalidadeItemStateChanged
        if (!iniciado) {
            return;
        }
        // alterar status campos nome do grupo e materiais
        verificarModalidade();

        //verifica se há espaço para nova apresentação
        ativarLimiteModalidade();

    }//GEN-LAST:event_CBModalidadeItemStateChanged

    private boolean ativarLimiteModalidade() {
        
        if(!eventoP.isSepin()) // Se não for SEPIN, pode tudo.
            return true;
        

        if(CBModalidade.getSelectedIndex()<0){
           System.out.println("ERROR: CBModalidade.getSelectedIndex()<0 em podeIncluirNessaModalidade()");
           if(modalidades.size() == 1) {
               CBModalidade.setSelectedIndex(0);
           } else {
               System.out.println("ERROR: CBModalidade.getSelectedIndex()<0 e modalidades.size() != 1");
                return false;
           }
           //CBModalidade.setSelectedIndex(0);
       }
        
        
        boolean result;
        
        if (!CBApresentacao.getSelectedItem().toString().equals("Nova")) { //não é nova. Está atualizando
            BTSalvar.setEnabled(true);
            LAviso.setText("Cadastro dos demais participantes do trabalho");
            //está apenas atualizando.
            //System.out.println("Comparando "+apresentacaoAtual.getModalidadeCod()+" e "+codigoModalidade(CBModalidade.getSelectedItem().toString())+"...");
            result = podeIncluirNessaModalidade
        (apresentacaoAtual.getModalidadeCod()==codigoModalidade(CBModalidade.getSelectedItem().toString()));
            
        }else{
            result = podeIncluirNessaModalidade(false); //é nova
        }
            
        if (result) {
            BTSalvar.setEnabled(true);
            LAviso.setText("Cadastro dos demais participantes do trabalho");
            LResult.setText("");
            return true;
        } else {
            BTSalvar.setEnabled(false);
            LAviso.setText("Limite para essa Modalidade já atingido para o Campus.");
            LResult.setText("Limite para essa Modalidade já atingido para o Campus.");
            return false;
        }

    }

    private boolean podeIncluirNessaModalidade(boolean mantemModalidadeJaIncluida) {
       
        
        
         try {
            apresentador = BancoDeDados.comando().getApresentador(apresentacaoAtual.getApresentadorCpf());
        } catch (Exception e) {
            apresentador = BancoDeDados.comando().getApresentador(cpf);
        }
        

        //unidadePrincipal
        int uni = apresentador.getCodigoUnidade();
                
                //unidadePrincipal.getId();
        //poster 1, comoral 2, iftech 3, robotica 5, cultural 4
        modalidade currentModalidade = new modalidade("-", -1);
        
        if(modalidades.size() == 1){
            currentModalidade = modalidades.get(0);
        }else{
            for (modalidade m : modalidades) {
            if (m.getNome().equals(CBModalidade.getSelectedItem().toString())) {
                currentModalidade = m;
            }
        }
    }
        int extra =0;
        if(mantemModalidadeJaIncluida){
            extra = 1;
            System.out.println("Adicinou 1 ao extra!");
        }
        
        int mod = currentModalidade.getId();
        int contador = contApres(uni, mod)-extra;
        System.out.println("Contador: "+contador);
        
        try{
        switch (mod) {
            case 1:
                //poster
                System.out.println("Limite: " + unidadePrincipal.getLimiteApresentacoesSelecionadas());
                return ( contador < unidadePrincipal.getLimiteApresentacoesSelecionadas());
            case 2:
                //comunicação oral
                System.out.println("Limite: " + unidadePrincipal.getLimiteComunicacaoOralSelecionadas());
                return (contador < unidadePrincipal.getLimiteComunicacaoOralSelecionadas());
            case 3:
                // iftech
                System.out.println("Limite: " + unidadePrincipal.getLimiteIftech());
                return (contador < unidadePrincipal.getLimiteIftech());
            case 5:
                //robotica
                System.out.println("Limite: " + unidadePrincipal.getLimiteRobotica());
                return (contador < unidadePrincipal.getLimiteRobotica());
            case 4:
                //cultural
                System.out.println("Limite: " + unidadePrincipal.getLimiteculturais());
                return (contador < unidadePrincipal.getLimiteculturais());
            case 7:
                //oficina: Trata junto com poster
                System.out.println("Limite: " + unidadePrincipal.getLimiteApresentacoesSelecionadas());
                return ( contador < unidadePrincipal.getLimiteApresentacoesSelecionadas());
            case 8:
                //sessão tematica: Trata junto com poster
                System.out.println("Limite: " + unidadePrincipal.getLimiteApresentacoesSelecionadas());
                return ( contador < unidadePrincipal.getLimiteApresentacoesSelecionadas());
            default:
                break;
        } 
        }catch(Exception e){
            return true;
        }

        System.out.println("ERROR: Não identificou modalidade para ativarLimiteModalidade()");
        return false;
    }

    public int contApres(int unidade, int modalidade) { //apresentações desta unidade, nesta modalidade e nesse evento.
        return BancoDeDados.comando().selectCount("SELECT count(id) FROM apresentacao a, apresentador ap WHERE "
                + "ap.unidade = " + unidade + " AND ap.cpf = a.apresentador "
                + "AND a.modalidade = " + modalidade + " AND a.evento = '" + eventoP.getId() + "';");
    }


    private void TFCpfFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_TFCpfFocusLost
        if (!Arrumador.comando().validarCPF(TFCpf.getText())) {
            TFCpf.setText("");
        }
    }//GEN-LAST:event_TFCpfFocusLost

    private void TFCpfAttFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_TFCpfAttFocusLost
        if (!Arrumador.comando().validarCPF(TFCpfAtt.getText())) {
            TFCpfAtt.setText("");
        }
    }//GEN-LAST:event_TFCpfAttFocusLost

    private void CBBolsaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_CBBolsaItemStateChanged

      //  verificarBolsa();
        // Não está mais vinculado a bolsa. Agora muda de acordo com categoria.

    }//GEN-LAST:event_CBBolsaItemStateChanged

    private void CBCategoriaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_CBCategoriaItemStateChanged

        if (!iniciado) {
            return;
        }

        // ajusta modalidades
        ajustaMudancaCategoria();
        verificarModalidade();


    }//GEN-LAST:event_CBCategoriaItemStateChanged

    private void ajustaMudancaCategoria() {

        System.out.println("Iniciado ajuste da janela de acordo com a categoria...");

        ArrayList<Integer> idsPermitidos = new ArrayList<>();
        for (categoria c : categorias) {
            if (c.getNome().equals(CBCategoria.getSelectedItem().toString())) {
                
                System.out.println("Categoria "+ c.getNome() + " tem " + 
                        c.getModalidades().size() + " modalidade(s): " + c.getModalidades().toString());
                idsPermitidos = c.getModalidades();

                CBTematica.setEnabled(c.isAreaTematica());

                CBArea.setEnabled(c.isGrandeArea() || c.isCultural());
                CBSubArea.setEnabled(c.isGrandeArea() || c.isCultural());

                temResumo = c.isComResumo();
                areaTematica = c.isAreaTematica();

                preencherAreas(c.isGrandeArea(), c.isCultural());
                
            }
        }

        iniciado = false;
        int cont = 0;
        CBModalidade.removeAllItems();
        for (modalidade m : modalidades) {
            for (Integer i : idsPermitidos) {
                if (m.getId() == i) {
                    CBModalidade.addItem(m.getNome());
                    cont++;
                }
            }
        }
        iniciado = true;
        System.out.println("Adicionou "+ cont+" modalidades da categoria ao ComboBox");

        TFResumo.setEnabled(temResumo);
        if (!temResumo) {
            TFResumo.setText("Resumo não exigido para esta categoria");
        } else if (TFResumo.getText().equals("Resumo não exigido para esta categoria")) {
            TFResumo.setText("");
        }

        LResumo.setVisible(temResumo);

        atualizaListaSubAreas();

    }


    private void CBAutor1SexoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CBAutor1SexoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_CBAutor1SexoActionPerformed

    private void BTMudarApresentadorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTMudarApresentadorActionPerformed

        apresentacaoAtual.autorViraApresentador(autores.get(CBNomeAutorAt.getSelectedIndex()).getId());
        carregarApresentacao();

    }//GEN-LAST:event_BTMudarApresentadorActionPerformed

    private void BTExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTExcluirActionPerformed

        if (aindaNaoClicouEmExcluir) {

            BTExcluir.setText("Tem Certeza?");
            aindaNaoClicouEmExcluir = false;

        } else {

            boolean result = apresentacaoAtual.excluirApresentacaoTotalmente(false, coordenacoes); //reinserir = false; Precisaria reinserir apresentador também, mas não faz isso ainda
            if (result){
                BTExcluir.setText("Excluída!");
                recuperarApresentacoes();
            }
            else
                BTExcluir.setText("Falha ao excluir.");

        }


    }//GEN-LAST:event_BTExcluirActionPerformed

    private void CBEventoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_CBEventoItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_CBEventoItemStateChanged

    private void TFAutor1EmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TFAutor1EmailActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TFAutor1EmailActionPerformed

    private void CBInclusaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CBInclusaoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_CBInclusaoActionPerformed

    private void CBDiversidadeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CBDiversidadeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_CBDiversidadeActionPerformed

    private void TFRgAtt1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_TFRgAtt1FocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_TFRgAtt1FocusLost

    private void TFRg1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_TFRg1FocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_TFRg1FocusLost

    private void TFResumoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_TFResumoFocusGained
       TFResumo.setBackground(Color.white);
    }//GEN-LAST:event_TFResumoFocusGained

    private void TFPalavrasChaveFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_TFPalavrasChaveFocusGained
       TFResumo.setBackground(Color.white);
    }//GEN-LAST:event_TFPalavrasChaveFocusGained

    private void TFNomeFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_TFNomeFocusGained
        TFNome.setBackground(Color.white);
    }//GEN-LAST:event_TFNomeFocusGained

    public void verificarModalidade() {

        if (CBModalidade.getSelectedIndex() == -1) {
            System.out.println("Modalidade não selecionada, saindo de verificarModalidade()...");
            return;
        }

        if (CBModalidade.getSelectedIndex() < 0 && CBApresentacao.getSelectedIndex() < 1) {
            TFNomeGrupo.setText("");
            LGrupo.setVisible(false);
            TFNomeGrupo.setVisible(false);
            TFVideoApresentacao.setText("");
            TFVideoApresentacao.setVisible(false);
            LVideo.setVisible(false);
            TFMateriais.setVisible(false);
            LMateriais.setVisible(false);
            TFMateriais.setText("");
            return;
        }

        modalidade m = modalidadeDeNome(CBModalidade.getSelectedItem().toString());
        if (m.Temnomegrupo()) {
            TFNomeGrupo.setEnabled(true);
            TFVideoApresentacao.setEnabled(true);
        } else {
            TFNomeGrupo.setText("");
            TFNomeGrupo.setEnabled(false);
            TFVideoApresentacao.setText("");
            TFVideoApresentacao.setEnabled(false);
        }

        if (m.Usamateriais()) {
            TFMateriais.setEnabled(true);
        } else {
            TFMateriais.setEnabled(false);
            TFMateriais.setText("");
        }
    }

    public void atualizarTabelaAutores() {
        String[][] aut = new String[autores.size() + 1][5];

        if (autores.size() < 1) {
            aut[0][0] = "";
            aut[0][1] = "";
            aut[0][2] = "";
            aut[0][3] = "";
            aut[0][4] = "";
        }

        String[] cabec = new String[5];
        cabec[0] = "Nome";
        cabec[1] = "Email";
        cabec[2] = "Função";
        cabec[3] = "Campus";
        cabec[4] = "Bolsa";

        try {
            apresentador = BancoDeDados.comando().getApresentador(apresentacaoAtual.getApresentadorCpf());
        } catch (Exception e) {
            apresentador = BancoDeDados.comando().getApresentador(cpf);
        }

        aut[0][0] = apresentador.getNome();
        aut[0][1] = apresentador.getEmail();
        aut[0][2] = "Apresentador";
        aut[0][3] = apresentador.getUnidade();
        aut[0][4] = "Definida acima";

        for (int a = 0; a < autores.size(); a++) {
            aut[a + 1][0] = autores.get(a).getNome();
            aut[a + 1][1] = autores.get(a).getEmail();
            aut[a + 1][2] = autores.get(a).getFuncaoNome();
            aut[a + 1][3] = autores.get(a).getUnidadeNome();
            aut[a + 1][4] = autores.get(a).getBolsaNome();
        }

        DefaultTableModel model = new DefaultTableModel(aut, cabec);
        TAutores.setModel(model);
        TAutores.setEnabled(false);
        atualizarCBAutores();
    }

    public void atualizarCBAutores() {

        int index = CBNomeAutorAt.getSelectedIndex();
        boolean k = autores.size() > 0;

        if (!k) {
            BTMudarApresentador.setText("Não há autores cadastrados");
            BTMudarApresentador.setEnabled(false);
        }

        CBNomeAutorAt.setEnabled(k);
        CBAutorUnidadeAt.setEnabled(k);
        CBAutorFuncaoAt.setEnabled(k);
        CBAutorBolsaAt.setEnabled(k);
        CBSexoAtt.setEnabled(k);
        TFNascAtt.setEnabled(k);
        TFCpfAtt.setEnabled(k);
        this.TFRgAtt1.setEnabled(k);
        TFNomeAutorAt.setEnabled(k);
        TFEmailAutorAt.setEnabled(k);
        BTExcluirAutor.setEnabled(k);
        BTAtualizarAutor.setEnabled(k);

        CBNomeAutorAt.removeAllItems();
        for (autor autor : autores) {
            CBNomeAutorAt.addItem(autor.getNome());
        }

        if (index < autores.size() && index >= 0) {
            CBNomeAutorAt.setSelectedIndex(index);
        }

        recuperarDadosAutorSelecionado();

    }

    public void recuperarDadosAutorSelecionado() {

        if (autores.size() < 1) {
            System.out.println("Não há autores para incluir no CB de atualização");
            return;
        }

        if (CBNomeAutorAt.getSelectedIndex() == -1) {
            return;
        }

        int index = CBNomeAutorAt.getSelectedIndex();

        TFNomeAutorAt.setText(autores.get(index).getNome());
        TFEmailAutorAt.setText(autores.get(index).getEmail());
        CBAutorUnidadeAt.setSelectedItem(autores.get(index).getUnidadeNome());
        CBAutorFuncaoAt.setSelectedItem(autores.get(index).getFuncaoNome());
        CBAutorBolsaAt.setSelectedItem(autores.get(index).getBolsaNome());
        TFCpfAtt.setText(autores.get(index).getCpf());
        this.TFRgAtt1.setText(autores.get(index).getRg());
        TFNascAtt.setText(autores.get(index).getNascimento());
        CBSexoAtt.setSelectedItem(autores.get(index).getSexo());

        if (autores.get(index).getCpf() == null) {
            BTMudarApresentador.setText("Necessário CPF para autor poder ser apresentador");
            BTMudarApresentador.setEnabled(false);
            return;
        }

        if (autores.get(index).getEmail() == null) {
            BTMudarApresentador.setText("Necessário Email para autor poder ser apresentador");
            BTMudarApresentador.setEnabled(false);
            return;
        }

        if (autores.get(index).getSexo() == null) {
            BTMudarApresentador.setText("Necessário informar Sexo para autor poder ser apresentador");
            BTMudarApresentador.setEnabled(false);
            return;
        }

        BTMudarApresentador.setText("Tornar '" + autores.get(index).getNome() + "' Apresentador(a)");
        BTMudarApresentador.setEnabled(true);

    }

    private int codigoBolsa(String nome) {
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
    }

    private int codigoFuncao(String nome) {
        for (int a = 0; a < funcoes.size(); a++) {
            if (funcoes.get(a).getNome().equals(nome)) {
                return funcoes.get(a).getId();
            }
        }

        return -1;
    }

    public String nomeFuncao(int id) {

        for (int a = 0; a < funcoes.size(); a++) {
            if (funcoes.get(a).getId() == id) {
                return funcoes.get(a).getNome();
            }
        }

        return "nulo";
    }

    public boolean nomeAutorJaExiste(String nome) {

        if (autores.size() < 1) {
            return false;
        }

        for (autor autor : autores) {
            if (autor.getNome().equals(nome)) {
                return true;
            }
        }

        return false;
    }

    public ArrayList<subarea> getSubAreasDaArea(String nome) {
        for (area area : areas) {
            if (area.getNome().equals(nome)) {
                return area.getSubareas();
            }
        }

        System.out.println("Não conseguiu achar objeto da Area " + nome + "!");
        return null;
    }

  /*  private String nomeEvento(String issn) {
        for (int a = 0; a < eventos.size(); a++) {
            if (eventos.get(a).getId().equals(issn)) {
                return eventos.get(a).getNome();
            }
        }
        return "nulo";
    }

    private String issnEvento(String nome) {
        for (int a = 0; a < eventos.size(); a++) {
            if (eventos.get(a).getNome().equals(nome)) {
                return eventos.get(a).getId();
            }
        }

        return "nulo";
    }

    public String nomeBolsa(String id) {

        for (int a = 0; a < eventos.size(); a++) {
            if (eventos.get(a).getId().equals(id)) {
                return eventos.get(a).getNome();
            }
        }

        return "nulo";
    }*/

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

    private int codigoModalidade(String nome) {
        for (int a = 0; a < modalidades.size(); a++) {
            if (modalidades.get(a).getNome().equals(nome)) {
                return modalidades.get(a).getId();
            }
        }

        return -1;
    }

    private modalidade modalidadeDeNome(String nome) {
        for (int a = 0; a < modalidades.size(); a++) {
            if (modalidades.get(a).getNome().equals(nome)) {
                return modalidades.get(a);
            }
        }

        return null;
    }

    public String nomeModalidade(int id) {

        for (int a = 0; a < modalidades.size(); a++) {
            if (modalidades.get(a).getId() == id) {
                return modalidades.get(a).getNome();
            }
        }

        return "nulo";
    }

    private int codigoSubarea(String nome) {
        for (int a = 0; a < subareas.size(); a++) {
            if (subareas.get(a).getNome().equals(nome)) {
                return subareas.get(a).getId();
            }
        }

        return -1;
    }

    public String nomeSubarea(int id) {

        for (int a = 0; a < subareas.size(); a++) {
            if (subareas.get(a).getId() == id) {
                return subareas.get(a).getNome();
            }
        }

        return "nulo";
    }

    private void deixarBackgroundWhite(){
        TFNome.setBackground(Color.white);
        TFPalavrasChave.setBackground(Color.white);
        TFResumo.setBackground(Color.white);
    }
    
    public boolean validaAlteracoesBD() {

        deixarBackgroundWhite();
        
        if (TFNome.getText().toString().length() < 1) {
            TFNome.setBackground(Color.red);
            LResult.setText("Campo de nome da apresentação em branco. Ação cancelada.");
            return false;
        }
        if (TFPalavrasChave.getText().toString().length() < 1) {
            TFPalavrasChave.setBackground(Color.red);
            LResult.setText("Campo de palavras chave em branco. Ação cancelada.");
            return false;
        }
        if (TFResumo.getText().toString().length() < 1) {
            TFResumo.setBackground(Color.red);
            LResult.setText("Campo de resumo em branco. Ação cancelada.");
            return false;
        }

        if (BancoDeDados.comando().haRestricaoResumo() && !BancoDeDados.comando().tamanhoDoResumoOk(contarNumeroPalavras()) && temResumo) {
            TFResumo.setBackground(Color.red);
            LResult.setText("Necessário adequar o tamanho do resumo. Ação cancelada.");
            return false;
        }

        if (!temPontoVirgula(TFPalavrasChave.getText())) {
            TFPalavrasChave.setBackground(Color.red);
            LResult.setText("Palavras chave (Três a cinco) devem ser dividadas por ponto e vírgula. Ação cancelada.");
            return false;
        }


        /*if(CBSexo.getSelectedIndex()==0)
         {
         LResult.setText("Sexo não selecionado. Ação cancelada.");
         return;
         }
        if (CBModalidade.getSelectedItem().toString().equals("Apresentacao cultural")
                && (TFCpf.getText().length() < 5 || TFNasc.getText().length() < 4)) {
            LResult.setText("Necessário sexo e data de nasc. Ação cancelada.");
            return false;
        }*/
        
        if (CBBolsa.getSelectedItem().toString().equals("-")) {
            LResult.setText("Selecione sua bolsa/programa. Ação cancelada.");
            return false;
        }
        if (CBModalidade.getSelectedItem().toString().equals("Apresentacao cultural")
                && (TFVideoApresentacao.getText().length() < 5)) {
            LResult.setText("Necessário informar LINK da apresentação. Ação cancelada.");
            return false;
        }

        if (CBTematica.isEnabled() && CBTematica.getSelectedItem().equals("-")) {
            LResult.setText("Necessário escolher uma area temática.");
            return false;

        }

        if (semOrientadorCadastrado()) {
            LResult.setText("Obrigatório cadastrar ORIENTADOR. Ação cancelada.");
            return false;
        }
        
        if (semSuplenteCadastrado() && temAutoresComuns()) {
            LResult.setText("Necessário indicar 1 coautor como suplente. Ação cancelada.");
            return false;
        }

        return true;
    }

    
    public boolean temAutoresComuns() {
        // ID 1: Coautor
        for (autor a : autores) {
            if (a.getFuncaoCod() == 1) {
                return true;
            }
        }

        return false;
    }
    
    public boolean semSuplenteCadastrado() {
        // ID 3: suplente
        for (autor a : autores) {
            if (a.getFuncaoCod() == 3) {
                return false;
            }
        }

        return true;
    }
    
    public boolean semOrientadorCadastrado() {
        //ID 2: orientador
        for (autor a : autores) {
            if (a.getFuncaoCod() == 2) {
                return false;
            }
        }

        return true;
    }
    

    public void inserirApresentacao() {

        if (!validaAlteracoesBD() || !iniciado) {
            return;
        }
       
        BTSalvar.setEnabled(false);
        
        //se precisa de código
        if(usaCodigoParaCadastro){
        
          
            
        //verifica se ticket é válido para cadastrar trabalho
            if(!BancoDeDados.comando().existeTicketTrabalho(TFTicket.getText(),"trabalho",cpf)){
                //caso não seja um código válido, encerra a operação e dá mensagem de erro.
                      
                LResult.setText("Falha ao Cadastras: Ticket inválido!");
                BTSalvar.setEnabled(true);
                return;
                
            }//se passou do IF, está tudo OK.
        
        }
                
        LResult.setText(".");

        apresentacao nova = new apresentacao();

        nova.setApresentadorCpf(cpf);
        nova.setCategoriaCod(codigoCategoria(CBCategoria.getSelectedItem().toString()));
        nova.setCategoriaNome(CBCategoria.getSelectedItem().toString());
        nova.setEventoCod(eventoP.getId());
        nova.setEventoNome(CBEvento.getSelectedItem().toString());
        nova.setMateriais(TFMateriais.getText());
        nova.setPalavrasChave(Arrumador.comando().paraMaisculaComecoPalavra(TFPalavrasChave.getText(), true));
        nova.setResumo(TFResumo.getText());
        nova.setGrupoNome(TFNomeGrupo.getText());
        nova.setLinkParaVideo(TFVideoApresentacao.getText());
        nova.setModalidadeNome(CBModalidade.getSelectedItem().toString());
        nova.setModalidadeCod(codigoModalidade(CBModalidade.getSelectedItem().toString()));
        nova.setNometitulo(Arrumador.comando().paraMaisculaComecoPalavra(TFNome.getText(), false));
        //TFNome.setText(""); // para evitar que salve duas vezes
        nova.setInclusao(CBInclusao.getSelectedItem().toString());
        nova.setDiversidade(CBDiversidade.getSelectedItem().toString());

        nova.setSelecionado(1); //padrão 1: Selecionada
        
        categoria cats = getCategoria(CBCategoria.getSelectedItem().toString());

        if (cats.isGrandeArea() || cats.isCultural()) {
            nova.setAreaNome(CBArea.getSelectedItem().toString());
            nova.setSubareaNome(CBSubArea.getSelectedItem().toString()); 
            nova.setSubareaCod(codigoSubarea(CBSubArea.getSelectedItem().toString()));
        } else {
            nova.setAreaNome(CBTematica.getSelectedItem().toString());
            nova.setSubareaNome(CBTematica.getSelectedItem().toString());
            nova.setSubareaCod(codigoArea(CBTematica.getSelectedItem().toString()));
        }

        nova.setApresentadorNome(Arrumador.comando().getUserApresentador().getNome());
        nova.setAutores(autores);
        nova.setBolsaApresentadorNome(CBBolsa.getSelectedItem().toString());
        nova.setBolsaApresentadorCod(codigoBolsa(CBBolsa.getSelectedItem().toString()));
        //if (!(codigoBolsa(CBBolsa.getSelectedItem().toString()) == 1)) {
        //  nova.setTematicaExtensaoCod(-1);
        // } else {
        nova.setTematicaExtensaoCod(codigoArea(CBTematica.getSelectedItem().toString()));
        //  }

        if (CBApresentacao.getSelectedItem().toString().equals("Nova")) {
            nova.setId(-1);
        }

        boolean r = false;
        if ( !nomesIncluidos.contains(nova.getNometitulo())){
            r = nova.inserirNaBase();
        } else {
            System.out.println("OK: Ia incluir app uma segunda vez. Evitou");
            return;
        }
        //recuperou o ID cadastrado após cadastrar

        if (r) {
            nomesIncluidos.add(nova.getNometitulo());
            LResult.setText("Sucesso: Cadastro de '" + nova.getNometitulo() + "'! (ID "+nova.getId()+")");
            iniciado = false;
            recuperarApresentacoes();
            
            //envia e-mail com informações
            try{
            nova.enviarEmailParaAutoresComInformacoes(true,false);
            } catch (Exception e){
                System.out.println("ERROR: Nao conseguiu enviar e-mail.");
            }
            
            
            //retira ticket que foi utilizado, se houve.
            if(usaCodigoParaCadastro)
                BancoDeDados.comando().update("DELETE FROM ticket WHERE codigo = '"+TFTicket.getText().replace("'", ".")+"';");

            if (JP.inclusaoDeApresentacaoPeloCoordenador()) {

                JP.mensagemCoordenadorSucessoCadastroApresentacao(nova.getNometitulo());
                this.dispose();

            }else{
                Comunicador c = new Comunicador(JP,"Cadastro de apresentação foi bem sucedido.\nUm e-mail de confirmação foi enviado para\ntodos os autores e o coordenador de pesquisa.\n\n\nDúvidas? Consulte a equipe organizadora.","Sucesso!"); //Janela, texto, título
                this.dispose();
            }

        } else {
            LResult.setText("Falha ao cadastrar apresentação " + nova.getNometitulo() + "!");
            BTSalvar.setEnabled(true);
        }
        iniciado = true;
        
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
            java.util.logging.Logger.getLogger(GerenciarApresentacoes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GerenciarApresentacoes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GerenciarApresentacoes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GerenciarApresentacoes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                //   new GerenciarApresentacoes().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BTAdicionarAutor;
    private javax.swing.JButton BTAtualizarAutor;
    private javax.swing.JButton BTEditarDadosPessoais;
    private javax.swing.JButton BTExcluir;
    private javax.swing.JButton BTExcluirAutor;
    private javax.swing.JButton BTMudarApresentador;
    private javax.swing.JButton BTSair;
    private javax.swing.JButton BTSalvar;
    private javax.swing.JComboBox CBApresentacao;
    private javax.swing.JComboBox CBArea;
    private javax.swing.JComboBox CBAutor1Bolsa;
    private javax.swing.JComboBox CBAutor1Funcao;
    private javax.swing.JComboBox CBAutor1Sexo;
    private javax.swing.JComboBox CBAutor1Unidade;
    private javax.swing.JComboBox CBAutorBolsaAt;
    private javax.swing.JComboBox CBAutorFuncaoAt;
    private javax.swing.JComboBox CBAutorUnidadeAt;
    private javax.swing.JComboBox CBBolsa;
    private javax.swing.JComboBox CBCategoria;
    private javax.swing.JComboBox<String> CBDiversidade;
    private javax.swing.JComboBox CBEvento;
    private javax.swing.JComboBox<String> CBInclusao;
    private javax.swing.JComboBox CBModalidade;
    private javax.swing.JComboBox CBNomeAutorAt;
    private javax.swing.JComboBox CBSexoAtt;
    private javax.swing.JComboBox CBSubArea;
    private javax.swing.JComboBox CBTematica;
    private javax.swing.JLabel LAviso;
    private javax.swing.JLabel LGrupo;
    private javax.swing.JLabel LMateriais;
    private javax.swing.JLabel LNasc;
    private javax.swing.JLabel LNascAtt;
    private javax.swing.JLabel LResult;
    private javax.swing.JLabel LResumo;
    private javax.swing.JLabel LSubArea;
    private javax.swing.JLabel LTicket;
    private javax.swing.JLabel LVideo;
    private javax.swing.JTable TAutores;
    private javax.swing.JTextField TFAutor1Email;
    private javax.swing.JTextField TFAutor1Nome;
    private javax.swing.JTextField TFCpf;
    private javax.swing.JTextField TFCpfAtt;
    private javax.swing.JTextField TFEmailAutorAt;
    private javax.swing.JTextField TFMateriais;
    private javax.swing.JFormattedTextField TFNasc;
    private javax.swing.JFormattedTextField TFNascAtt;
    private javax.swing.JTextField TFNome;
    private javax.swing.JTextField TFNomeAutorAt;
    private javax.swing.JTextField TFNomeGrupo;
    private javax.swing.JTextField TFPalavrasChave;
    private javax.swing.JTextPane TFResumo;
    private javax.swing.JTextField TFRg1;
    private javax.swing.JTextField TFRgAtt1;
    private javax.swing.JTextField TFTicket;
    private javax.swing.JTextField TFVideoApresentacao;
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
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    // End of variables declaration//GEN-END:variables

    private void limparCampos() {
        TFNome.setText("");
        TFNomeGrupo.setText("");
        TFVideoApresentacao.setText("");
        TFMateriais.setText("");
        TFPalavrasChave.setText("");
        TFResumo.setText("");
        TFAutor1Nome.setText("");
        TFAutor1Email.setText("");
        autores = new ArrayList<>();
        atualizarTabelaAutores();
        TFNomeAutorAt.setText("");
        TFEmailAutorAt.setText("");
    }

    private void recuperarDadosApresentacao() {

        
        
        if (coordenacoes.size() == 0) {
            apresentacaoAtual = BancoDeDados.comando().recuperarApresentacao(idsApresentacao.get(CBApresentacao.getSelectedIndex() - 1));
        } else {
            apresentacaoAtual = BancoDeDados.comando().recuperarApresentacao(idsApresentacao.get(CBApresentacao.getSelectedIndex()));
            
            for(int a=0;a<unidades.size();a++)
                if(unidades.get(a).getId()==apresentacaoAtual.getUnidadeCod())
                    unidadePrincipal = unidades.get(a);
            
        }
        System.out.println("LOADING APRESENTACAO " + apresentacaoAtual.getId());
        
        
        autores = apresentacaoAtual.getAutores();
        //System.out.println("Possui " + autores.size() + " autores além do apresentador.");
        atualizarTabelaAutores();

        TFNome.setText(apresentacaoAtual.getNometitulo());
        TFNomeGrupo.setText(apresentacaoAtual.getGrupoNome());
        TFVideoApresentacao.setText(apresentacaoAtual.getLinkParaVideo());
        TFMateriais.setText(apresentacaoAtual.getMateriais());
        TFPalavrasChave.setText(apresentacaoAtual.getPalavrasChave());
        TFResumo.setText(apresentacaoAtual.getResumo());

        System.out.println("Area: " + apresentacaoAtual.getAreaNome() + "(" + apresentacaoAtual.getSubareaNome() + "), Bolsa: " + nomeBolsa(apresentacaoAtual.getBolsaApresentadorCod()) + ", "
                + "Evento: " + eventoP.getNome() + ", Modalidade: " + nomeModalidade(apresentacaoAtual.getModalidadeCod()));

        CBCategoria.setSelectedItem(nomeCategoria(apresentacaoAtual.getCategoriaCod()));
        iniciado = false;
        ajustaMudancaCategoria();
       // iniciado = true;
        CBModalidade.setSelectedItem(nomeModalidade(apresentacaoAtual.getModalidadeCod()));

        iniciado = false;

        if (getCategoriaDeCodigo(apresentacaoAtual.getCategoriaCod()).isAreaTematica()) {
            CBTematica.setSelectedItem(nomeArea(apresentacaoAtual.getTematicaExtensaoCod()));
            System.out.println("Selecionou Area Tematica: " + nomeArea(apresentacaoAtual.getTematicaExtensaoCod()));
        }

        if (getCategoriaDeCodigo(apresentacaoAtual.getCategoriaCod()).isGrandeArea() || getCategoriaDeCodigo(apresentacaoAtual.getCategoriaCod()).isCultural()) {
            CBArea.setSelectedItem(nomeArea(apresentacaoAtual.getAreaCod()));
            System.out.println("Selecionou Area: " + apresentacaoAtual.getAreaNome() + " ( " + apresentacaoAtual.getAreaCod() + " )");
        }

        CBBolsa.setSelectedItem(nomeBolsa(apresentacaoAtual.getBolsaApresentadorCod()));
        System.out.println("Selecionou Bolsa.");
        CBEvento.setSelectedItem(eventoP.getNome());

        CBInclusao.setSelectedItem(apresentacaoAtual.getInclusao());
        CBDiversidade.setSelectedItem(apresentacaoAtual.getDiversidade());
        
        iniciado = true;
        atualizaListaSubAreas();
        CBSubArea.setSelectedItem(apresentacaoAtual.getSubareaNome());

        if (apresentacaoAtual.getTematicaExtensaoCod() > -1) {
            CBTematica.setSelectedItem(nomeArea(apresentacaoAtual.getTematicaExtensaoCod()));
        }

    }

    private categoria getCategoriaDeCodigo(int cod) {

        for (categoria c : categorias) {
            if (c.getId() == cod) {
                return c;
            }
        }

        return null;
    }

    private void atualizarApresentacao() {
        String res;

        if (!validaAlteracoesBD()) {
            return;
        }

        apresentacaoAtual.setCategoriaCod(codigoCategoria(CBCategoria.getSelectedItem().toString()));
        apresentacaoAtual.setEventoCod(eventoP.getId());
        apresentacaoAtual.setEventoNome(CBEvento.getSelectedItem().toString());
        apresentacaoAtual.setMateriais(TFMateriais.getText());
        apresentacaoAtual.setPalavrasChave(Arrumador.comando().paraMaisculaComecoPalavra(TFPalavrasChave.getText(), true));
        apresentacaoAtual.setResumo(TFResumo.getText());
        apresentacaoAtual.setGrupoNome(TFNomeGrupo.getText());
        apresentacaoAtual.setLinkParaVideo(TFVideoApresentacao.getText());
        apresentacaoAtual.setModalidadeNome(CBModalidade.getSelectedItem().toString());
        apresentacaoAtual.setModalidadeCod(codigoModalidade(CBModalidade.getSelectedItem().toString()));
        apresentacaoAtual.setNometitulo(Arrumador.comando().paraMaisculaComecoPalavra(TFNome.getText(), false));

        apresentacaoAtual.setInclusao(CBInclusao.getSelectedItem().toString());
        apresentacaoAtual.setDiversidade(CBDiversidade.getSelectedItem().toString());
        
        categoria cats = getCategoria(CBCategoria.getSelectedItem().toString());

        if (cats.isGrandeArea() || cats.isCultural()) {
            apresentacaoAtual.setAreaNome(CBArea.getSelectedItem().toString());
            apresentacaoAtual.setSubareaNome(CBSubArea.getSelectedItem().toString());
            apresentacaoAtual.setSubareaCod(codigoSubarea(CBSubArea.getSelectedItem().toString()));
            System.out.println("Setou subArea (grande area) como " + CBSubArea.getSelectedItem().toString());
            
        } else {
            apresentacaoAtual.setSubareaNome("areatem");
            apresentacaoAtual.setSubareaCod(codigoArea(CBTematica.getSelectedItem().toString()));
           // System.out.println("Setou subArea (tematica) como " + CBSubArea.getSelectedItem().toString());
        }

        apresentacaoAtual.setAutores(autores);
        apresentacaoAtual.setBolsaApresentadorCod(codigoBolsa(CBBolsa.getSelectedItem().toString()));
        //  if (!(codigoBolsa(CBBolsa.getSelectedItem().toString()) == 1)) {
        //   apresentacaoAtual.setTematicaExtensaoCod(-1);
        //} else {
        apresentacaoAtual.setTematicaExtensaoCod(codigoArea(CBTematica.getSelectedItem().toString()));
        // }

        if (apresentacaoAtual.excluirAutores(autoresAExcluir)) {
            res = "Excluiu autor(es); ";
        } else {
            res = "Não excluiu autores; ";
        }
        if (apresentacaoAtual.atualizarNaBase()) {
            res = res + "Atualizou apresentação (" + apresentacaoAtual.getId() + ");";
            //envia e-mail com informações
            try {
            apresentacaoAtual.enviarEmailParaAutoresComInformacoes(false,false);
            } catch (Exception e) {
                System.out.println("ERROR: Nao conseguiu enviar email.");
                res = res + " Não enviou email.";
            }
            
        } else {
            res = res + "Não atualizou apresentação;";
        }

        LResult.setText(res);

    }

    private int contarNumeroPalavras() {
        String t = TFResumo.getText();
        int palavras = 0;
        char[] t1 = t.toCharArray();
        for (int a = 1; a < t.length(); a++) {
            if (t1[a] == ' ' && t1[a - 1] != ' ') {
                palavras++;
            }
        }

        return palavras + 1;
    }

    private boolean temPontoVirgula(String c) {
        int palavras = 0;
        char[] t1 = c.toCharArray();
        for (int a = 1; a < c.length(); a++) {
            if (t1[a] == ';' && t1[a - 1] != ' ') {
                palavras++;
            }
        }

        return ((palavras > 1)&&(palavras <5));

    }

    private void recuperarApresentacoes() {
        Arrumador.comando().getUserApresentador().recuperaApresentacoes(coordenacoes);
        
        idsApresentacao = Arrumador.comando().getUserApresentador().getIdsApresentacao();
        nomesApresentacao = Arrumador.comando().getUserApresentador().getNomesApresentacao();
        CBApresentacao.removeAllItems();
        
        if (coordenacoes.isEmpty()) {
            CBApresentacao.addItem("Nova");
          //  LResult.setText("Ao salvar, essa janela se fechará.");
        }
        for (int a = 0; a < nomesApresentacao.size(); a++) {
            CBApresentacao.addItem(nomesApresentacao.get(a) + " (id: " + idsApresentacao.get(a) + ")");
        }

    }

    private String nomeArea(int t) {
        for (int a = 0; a < areas.size(); a++) {
            if (areas.get(a).getId() == t) {
                return areas.get(a).getNome();
            }
        }
        System.out.println("Não encontrou area tematica de ID " + t);
        return "-";
    }

    private int codigoArea(String t) {
        for (int a = 0; a < areas.size(); a++) {
            if (areas.get(a).getNome().equals(t)) {
                return areas.get(a).getId();
            }
        }

        return -1;
    }

    private void verificarBolsa() {
        // if (!iniciado) {
        //  return;
        // }

        /*  if (codigoBolsa(CBBolsa.getSelectedItem().toString()) == 1) {

         CBTematica.setEnabled(true);
         if (apresentacaoAtual.getTematicaExtensaoCod() > -1) {
         CBTematica.setSelectedItem(nomeTematica(apresentacaoAtual.getTematicaExtensaoCod()));
         }
         } else {
         CBTematica.setSelectedIndex(0);
         CBTematica.setEnabled(false);

         }*/
        /*    for (int b = 0; b < bolsas.size(); b++) {
         if (CBBolsa.getSelectedItem().toString().equals(bolsas.get(b).getNome())) {
         //   CBTematica.setEnabled(bolsas.get(b).classificaTematica());
         if (apresentacaoAtual != null) {
         if (bolsas.get(b).classificaTematica() && apresentacaoAtual.getTematicaExtensaoCod() > -1) {
         CBTematica.setSelectedItem(nomeArea(apresentacaoAtual.getTematicaExtensaoCod()));
         } else {
         CBTematica.setSelectedIndex(0);
         }
         }

         //boolean usaArea = bolsas.get(b).classificaArea();

         CBArea.setEnabled(usaArea);
         CBSubArea.setEnabled(usaArea);
         if (!usaArea) {
         CBArea.setSelectedIndex(0);
         CBSubArea.setSelectedIndex(0);
         }

         }
         }*/
    }

    private String nomeCategoria(int c) {
        for (int a = 0; a < categorias.size(); a++) {
            if (categorias.get(a).getId() == c) {
                return categorias.get(a).getNome();
            }
        }

        return "-";
    }

    private int codigoCategoria(String ab) {
        for (int a = 0; a < categorias.size(); a++) {
            if (categorias.get(a).getNome().equals(ab)) {
                return categorias.get(a).getId();
            }
        }

        return -1;

    }

    private categoria getCategoria(int a1) {
        for (int a = 0; a < categorias.size(); a++) {
            if (categorias.get(a).getId() == a1) {
                return categorias.get(a);
            }
        }
        System.out.println("ERROR: Nao encontrou categoria " + a1 + " em getCategoria()");
        return new categoria();
    }

    private categoria getCategoria(String a1) {
        for (int a = 0; a < categorias.size(); a++) {
            if (categorias.get(a).getNome().equals(a1)) {
                return categorias.get(a);
            }
        }
        System.out.println("ERROR: Nao encontrou categoria " + a1 + " em getCategoria()");
        return new categoria();
    }

    private void preencherAreas(boolean grandeArea, boolean cultural) {
        CBArea.removeAllItems();
        CBSubArea.removeAllItems();
        areas = BancoDeDados.comando().getAreas();
        for (int a = 0; a < areas.size(); a++) {
            if (!areas.get(a).isTematica()) {

                if (areas.get(a).isCultural() && cultural) {
                    CBArea.addItem(areas.get(a).getNome());
                } else if (!areas.get(a).isCultural() && grandeArea) {
                    CBArea.addItem(areas.get(a).getNome());
                }
            }
        }
    }

}
