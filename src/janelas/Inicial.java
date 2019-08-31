/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor. MARIELEN UMUARAMA
 */
package janelas;

import ClassesAuxiliares.Arrumador;
import ClassesAuxiliares.BancoDeDados;
import ClassesAuxiliares.Comunicador;
import ClassesAuxiliares.VerificarSeHaCadastro;
import ClassesAuxiliares.geradorCertificados;
import java.awt.Color;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import objetosBase.apresentacao;
import objetosBase.autor;
import objetosBase.triviais.coordenador;
import objetosBase.triviais.evento;

/**
 *
 * @author SistemaIP
 */
public class Inicial extends javax.swing.JFrame {

    private String versao = "v19.07.26";
    String versaoBD = "2019.726";//"2018.625";

    private RelatoriosQuantitativos rq;
    private RankingApresentacoes rap;
    private VerificarSeHaCadastro vh2, vh1, vh3;
    private GerarAnaisDoSeminario gas;
    SelecionarApresentacoesParaCentralizado sapc;
    private boolean offline = false;
    EscolheAvaliadoresApresentacoes eav;
    private SelecionarApresentadoresAprCultural saapcul;
    private GerarCertificadosAlunosParaCoordenadores gcapc;

    private GerenciarApresentacoes gap;
    private FuncoesCoordenacao fc;
    private boolean versoesNaoBatem = false;
    private EscolheAcompanhantes esac;
    private boolean inclusaoDeApresentacaoPeloCoordenador = false;
    private RealizarAvaliacoes rav;
    private ArrayList<coordenador> coordenacoes = new ArrayList<>();
    private CadastroOficinasSessoesMonitores cosm;
    private AvaliadoresAusentes avaus;
    private EditarUnidades editu;
    private GerenciarTickets gertic;
    private AlterarConfiguracoes cnf;

    /**
     * Creates new form Inicial
     */
    public Inicial() {

        initComponents();
//        this.setIconImage(Arrumador.comando().getIcone().getImage());
        setVisible(true);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setTitle("Sistema H.Urgal SEPIN " + versao);

        //evento e = BancoDeDados.comando().getEventoAtual();
        //e.deletarTudoDoEventodoBD(); //MUITO PERIGOSO. CUIDADO!
        
        /*evento e = new evento("VIII Seminário de Extensão, Ensino, Pesquisa e Inovação","2358 - 6869.18");
        e.setAno(2019);
        e.setAtivo(true);
        e.setData("16 a 19 de outubro de 2018");
        e.setUnidadeSedeCod(13);
        e.setId("2358 - 6869.18");
        e.setLocal("Londrina - Paraná");
        e.setSepin(1);
        e.salvarEventoNoBD();*/
        
        //System.out.println(Arrumador.comando().relatorioRapidoTrabalho(330));
        try {
            BancoDeDados.comando().recuperarValoresBase();
        } catch (Exception a) {
            conexaoFechada();
        }
        // ATUALIZA VERSÃO NO BD
        //BancoDeDados.comando().update("UPDATE valores set valor2 = 1205 WHERE rotulo = 'versao';");
        
        if (BancoDeDados.comando().versaoDifere(versaoBD) && !offline) {
            System.out.println("Versoes não batem.");
            // BTGerarCertificados.setEnabled(false);
            BTMinhasApresentacoes.setEnabled(false);
            // BTRelatoriosQuantitativos.setEnabled(false);
            // BTRankingApresentacoes.setEnabled(false);
            BTAreaDoAvaliador.setEnabled(false);
            BTCoordenacao.setEnabled(false);
            jLabel1.setText("Desatualizado - Atual em: goo.gl/VjhLwd");
            jLabel1.setForeground(Color.orange);
            versoesNaoBatem = true;
        }

        if (!offline && !versoesNaoBatem && BancoDeDados.comando().telaInicialSimples()) { //JANELA AVALIADOR
            System.out.println("Tela inicial simples: ATIVA");
            //  BTGerarCertificados.setEnabled(false);
            if (!BancoDeDados.comando().acessoApresentadorPermitido()) {
                BTMinhasApresentacoes.setEnabled(false);
            }

            BTAreaDoAvaliador.setEnabled(true);
            BTCoordenacao.setEnabled(true);
            
            
            vh2 = new VerificarSeHaCadastro("Avaliador", this, false);
        }

    //    geradorCertificados cert = new geradorCertificados();
      //    cert.compilarArquivoJasperSEPIN();
        // cert.gerarConjuntoCertsSEPIN(false);
        // cert.gerarCertificadoSepin(112, "testeas");
        // cert.criarDiretorios();
        /* apresentacao teste = new apresentacao();
         ArrayList<autor> autres = new ArrayList<>();
         teste.setId(1212);
         autor tt = new autor();
         tt.setNome("Autor 2 Edemilso");
         tt.setEmail("lucianourgal");
         tt.setFuncaoNome("Coautor");
         tt.setUnidadeNome("Campo Largo");
         autres.add(tt);
         teste.setAutores(autres);
         teste.setCategoriaNome("Categoria");
         teste.setModalidadeNome("Poster");
         teste.setAreaNome("Area");
         teste.setPalavrasChave("Palavra1; Palavra2; Palavra3.");
         teste.setNometitulo("Trabalho Oficial do SEPIN 2016 Para Avanço da Ciência e Tecnologia");
         teste.setResumo("Bla bla bla bla bla bla. Bla bla bla bla bla bla.Bla bla bla bla bla bla. Bla bla bla bla bla bla. Bla bla bla bla bla bla. Bla bla bla bla bla bla. Bla bla bla bla bla bla. Bla bla bla bla bla bla. Bla bla bla bla bla bla. Bla bla bla bla bla bla. Bla bla bla bla bla bla. Bla bla bla bla bla bla. Bla bla bla bla bla bla. Bla bla bla bla bla bla. Bla bla bla bla bla bla. Bla bla bla bla bla bla. Bla bla bla bla bla bla. Bla bla bla bla bla bla.");
         teste.setApresentadorEmail("luciano.pando@ifpr.edu.br");
         teste.enviarEmailParaAutoresComInformacoesTESTE();*/
       // BancoDeDados.comando().reprocessarTitulosApresentações(false);
        // BancoDeDados.comando().reprocessarNomesAutoresEApresentadores();
       //Arrumador.comando().colocarAcentosBD();
        /*
        
         certificado x = new certificado(cert);
         x.criarCertificadorTrabalho(a, BancoDeDados.comando().getEventos().get(0));
         x.setTipo("oral");
         x.gerarArquivo();*/
       // BancoDeDados.comando().update("UPDATE certificado SET texto3 = ' coordenou e organizou oficina intitulada \"Oficina de Inkscape\" no IV Seminário de Extensão, Ensino, Pesquisa e Inovação do IFPR, realizado nos dias 19 a 22 de outubro, em Paranaguá - Paraná.' WHERE verificador = '85f550f0ca99';");
        //  BancoDeDados.comando().update("UPDATE certificado SET texto3 = ' ministrou e auxiliou na organização da oficina intitulada "
        //    + "\"Oficina de Inkscape\" no IV Seminário de Extensão, Ensino, Pesquisa e Inovação do IFPR, "
        //    + "realizado nos dias 19 a 22 de outubro, em Paranaguá - Paraná.' WHERE verificador = 'a2bfa7f92a30';");
        // BancoDeDados.comando().update("UPDATE certificado SET texto2 = ' Talita Fatima Paula Monteiro, "
        //      + "Lauriana Paludo e Lilian do Nascimento Araujo Lazzarin' WHERE verificador = '17aa8a256206';");
        //gerarCadastroOficinaSessoesMonitores();
       // GeradorPDF gpdf = new GeradorPDF();
        // gpdf.gerarTodasFichasDeAvaliacao(BancoDeDados.comando().getEventos().get(0).getId(), true);
        
     
     //BancoDeDados.comando().update("UPDATE certificado set divulgar = 1;");   
       
     /*BancoDeDados.comando().update("UPDATE bolsa set nome = 'PIADH' where id = 2");
     BancoDeDados.comando().update("UPDATE bolsa set nome = 'PIBIC/PIBIC Jr' where id = 3");
     BancoDeDados.comando().update("UPDATE bolsa set nome = 'PRADI/PIBITI' where id = 4");
     BancoDeDados.comando().update("UPDATE bolsa set nome = 'PBIS' where id = 5");
     BancoDeDados.comando().update("UPDATE bolsa set nome = 'PIBID' where id = 6");
     BancoDeDados.comando().update("UPDATE bolsa set nome = 'IFTech' where id = 7");
     BancoDeDados.comando().update("UPDATE bolsa set nome = 'Robótica' where id = 8");
     BancoDeDados.comando().update("UPDATE bolsa set nome = 'Não bolsista/Voluntário' where id = 9");*/
     
        
  /*    String [] cods = new String[100];
      String [] texto3 = new String[100];
      String [] texto = new String[100];
        
      String SQL = "SELECT verificador, texto, texto3 FROM certificado WHERE evento = '2358 - 6869' and tipo = 'oral';";
      
      ResultSet r = BancoDeDados.comando().select(SQL);
      int c=0;
        try {
            while(r.next()){
                cods[c]= r.getString("verificador");
                texto[c] = r.getString("texto");
                texto3[c] = r.getString("texto3");
                c++;
            } } catch (SQLException ex) {
            Logger.getLogger(Inicial.class.getName()).log(Level.SEVERE, null, ex);
        }
      
        for(int a=0;a<c;a++){
            SQL = "UPDATE certificado SET texto = ?, texto3 = ? WHERE verificador = ?;";
           PreparedStatement stmt1;
            try {
                stmt1 = BancoDeDados.comando().getConnection().prepareStatement(SQL);
                stmt1.setString(1, texto3[a].replace(" de 2015", ""));
                stmt1.setString(2, texto[a].replace(" de 2015", ""));
                stmt1.setString(3, cods[a]);

                stmt1.execute();
            } catch (SQLException ex) {
                Logger.getLogger(Inicial.class.getName()).log(Level.SEVERE, null, ex);
            }   
        }*/
  
  
        //BancoDeDados.comando().update("DELETE FROM apresentacao WHERE id = 1836;");
        
        // System.out.println("Ha " +  BancoDeDados.comando().selectCount("SELECT * from apresentacao WHERE selecionada = 0;") + " trabalhos nao selecionados");
        
        //BancoDeDados.comando().update("DELETE FROM apresentador WHERE cpf = 'YOURCPF';");
        //BancoDeDados.comando().update("DELETE FROM apresentacao WHERE id = 1551 OR id = 1554 OR id = 1558;");
        //BancoDeDados.comando().contApresentadores();
        //BancoDeDados.comando().getEventoAtual().deletarTudoDoEventodoBD();
        //BancoDeDados.comando().update("UPDATE evento SET ativo = 1 WHERE ano = 2018;");
      
   /*checkSubstituicaoAvaliador("UPDATE avaliacao SET avaliador = 'newAvaliador' WHERE avaliador = 'oldAvaliador';");
         */ 
    }

    private void checkSubstituicaoAvaliador(String s) {
    
        String s1, s2;
        s1 = s.split("'")[1];
        s2 = s.split("'")[3];
        
        if(!Arrumador.comando().validarCPF(s1)){
            System.out.println("CPF " + s1 + " errado");
            return;
        }
        if(!Arrumador.comando().validarCPF(s2)){
            System.out.println("CPF " + s2 + " errado");
            return;
        }
        
        if (BancoDeDados.comando().selectCount("SELECT count(cpf) FROM avaliador WHERE cpf = '"+s1+"';")<1){
            System.out.println("Sem avaliador de cpf "+s1);
            return;
        }
        
        if (BancoDeDados.comando().selectCount("SELECT count(cpf) FROM avaliador WHERE cpf = '"+s2+"';")<1){
            System.out.println("Sem avaliador de cpf "+s2);
            return;
        }
        System.out.println("CPFs "+s1+" e "+s2+" estao ok para substituicoes de avaliador!");
        BancoDeDados.comando().update(s);
        System.out.println("Atualizado!");
    }
    
    private int seekndestroy() {

        try {
            rav.dispose();
        } catch (Exception e) {
        }
        try {
            esac.dispose();
        } catch (Exception e) {
        }
        try {
            fc.seekndestroy();
        } catch (Exception e) {
        }
        try {
            gap.seekndestroy();
        } catch (Exception e) {
        }
        try {
            editu.dispose();
        } catch (Exception e) {
        }
        try {
            cnf.dispose();
        } catch (Exception e) {
        }
        try {
            rq.dispose();
        } catch (Exception e) {
        }
        try {
            rap.dispose();
        } catch (Exception e) {
        }
        try {
            vh3.seekndestroy();
        } catch (Exception e) {
        }
        try {
            vh2.seekndestroy();
        } catch (Exception e) {
        }
        try {
            vh1.seekndestroy();
        } catch (Exception e) {
        }
        try {
            gertic.dispose();
        } catch (Exception e) {
        }
        try {
            gas.dispose();
        } catch (Exception e) {
        }
        try {
            eav.dispose();
        } catch (Exception e) {
        }
        try {
            sapc.dispose();
        } catch (Exception e) {
        }
        try {
            saapcul.dispose();
        } catch (Exception e) {
        }
        try {
            gcapc.dispose();
        } catch (Exception e) {
        }
        try {
            cosm.dispose();
        } catch (Exception e) {
        }
        try {
            avaus.dispose();
        } catch (Exception e) {
        }
        try {
            BancoDeDados.comando().fecharConexao();
        } catch (SQLException ex) {
            this.dispose();
            //Logger.getLogger(Inicial.class.getName()).log(Level.SEVERE, null, ex);
            // return 1;
        }
        this.dispose();
        return 0;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        BTAreaDoAvaliador = new javax.swing.JButton();
        BTSair = new javax.swing.JButton();
        BTMinhasApresentacoes = new javax.swing.JButton();
        BTCoordenacao = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("H.Urgal SEPIN");

        BTAreaDoAvaliador.setText("Area do Avaliador/Servidor");
        BTAreaDoAvaliador.setToolTipText("Cadastro de servidor/avaliador e realização de avaliações, quando servidor é avaliador");
        BTAreaDoAvaliador.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTAreaDoAvaliadorActionPerformed(evt);
            }
        });

        BTSair.setForeground(new java.awt.Color(153, 0, 0));
        BTSair.setText("Sair");
        BTSair.setToolTipText("Fecha completamente o sistema e todas as janelas deste");
        BTSair.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTSairActionPerformed(evt);
            }
        });

        BTMinhasApresentacoes.setText("Cadastro e Edição de Apresentações");
        BTMinhasApresentacoes.setToolTipText("Acessa apresentações em um determinado CPF");
        BTMinhasApresentacoes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTMinhasApresentacoesActionPerformed(evt);
            }
        });

        BTCoordenacao.setText("Funções de Coordenação");
        BTCoordenacao.setToolTipText("Funções específicas para coordenadores de pesquisa e PROEPI");
        BTCoordenacao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTCoordenacaoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(BTAreaDoAvaliador, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                .addComponent(BTCoordenacao, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(BTMinhasApresentacoes, javax.swing.GroupLayout.PREFERRED_SIZE, 413, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 8, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(154, 154, 154)
                .addComponent(BTSair, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(BTMinhasApresentacoes, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(BTCoordenacao, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(BTAreaDoAvaliador, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 22, Short.MAX_VALUE)
                .addComponent(BTSair, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public void criarJanelaRanking() {
        rap = new RankingApresentacoes(this);
    }

    private void BTSairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTSairActionPerformed
        seekndestroy();
    }//GEN-LAST:event_BTSairActionPerformed

    private void BTAreaDoAvaliadorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTAreaDoAvaliadorActionPerformed
        criarVh2();
    }//GEN-LAST:event_BTAreaDoAvaliadorActionPerformed

    public void criarVh2() { //somente para VH avaliador
        vh2 = new VerificarSeHaCadastro("Avaliador", this, false);
    }


    private void BTMinhasApresentacoesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTMinhasApresentacoesActionPerformed
        //Minhas apresentações da janela inicial. Segue configuração do BD.
        vh1 = new VerificarSeHaCadastro("CadastrarApresentacao", this, BancoDeDados.comando().apresentadorTemSenha());
    }//GEN-LAST:event_BTMinhasApresentacoesActionPerformed

    public void criarVHSCApresentacao(boolean coordCadastrando) {
        boolean usarSenhaApres;
        if (coordCadastrando) {
            usarSenhaApres = false;
        } else {
            usarSenhaApres = BancoDeDados.comando().apresentadorTemSenha();
        }

        vh1 = new VerificarSeHaCadastro("CadastrarApresentacao", this, usarSenhaApres);
        inclusaoDeApresentacaoPeloCoordenador = coordCadastrando;
    }

    private void BTCoordenacaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTCoordenacaoActionPerformed
        //VH coordenação/avaliador.
        vh3 = new VerificarSeHaCadastro("Coordenacao", this, false);
    }//GEN-LAST:event_BTCoordenacaoActionPerformed

    public void criarJanelaEscolherAvaliadores(ArrayList<coordenador> c) {
        eav = new EscolheAvaliadoresApresentacoes(c, BancoDeDados.comando().getEventoAtual());
    }

    public void refreshEscolheAcompanhantes() {
        try {
            esac.SalvaEAtualizaAvaliadores();
        } catch (Exception e) {
            //janela de escolher acompanhantes não está aberta
        }

    }

    public void conexaoFechada() {
        offline = true;
        //BTGerarAnaisSeminario.setEnabled(false);
        //  BTGerarCertificados.setEnabled(false);
        BTMinhasApresentacoes.setEnabled(false);
        //BTRelatoriosQuantitativos.setEnabled(false);
        // BTRankingApresentacoes.setEnabled(false);
        BTAreaDoAvaliador.setEnabled(false);
        BTCoordenacao.setEnabled(false);
        jLabel1.setText("H.Urgal SEPIN - Offline");
        jLabel1.setForeground(Color.orange);
        Comunicador c = new Comunicador(this, "Falha!\nPode ser a internet desse computador,\nou o servidor do sistema.", "Erro na conexão.");
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
            java.util.logging.Logger.getLogger(Inicial.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Inicial.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Inicial.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Inicial.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Inicial().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BTAreaDoAvaliador;
    private javax.swing.JButton BTCoordenacao;
    private javax.swing.JButton BTMinhasApresentacoes;
    private javax.swing.JButton BTSair;
    private javax.swing.JLabel jLabel1;
    // End of variables declaration//GEN-END:variables

    void criarJanelaSelecionarApresentacoes(ArrayList<coordenador> coord) {
        sapc = new SelecionarApresentacoesParaCentralizado(coord, this, BancoDeDados.comando().getEventoAtual());
    }

    void criarJanelaSelecaoApresentadoresApresCultural(ArrayList<coordenador> coord, ArrayList<apresentacao> aps) {
        saapcul = new SelecionarApresentadoresAprCultural(coord, this, aps);
    }

    void criarJanelaAnais() {
        gas = new GerarAnaisDoSeminario();
    }

    void criarJanelaRelatorios() {
        rq = new RelatoriosQuantitativos(this);
    }

    void criarJanelaGerenciamentoApresentacoes(ArrayList<coordenador> cord) {
        gap = new GerenciarApresentacoes(this, "N a CPF", cord, BancoDeDados.comando().getEventoAtual());
    }

    void criarJanelaGerenciamentoApresentacoesUmAluno(String cpf22) {
        gap = new GerenciarApresentacoes(this, cpf22, new ArrayList<coordenador>(), BancoDeDados.comando().getEventoAtual());
    }

    public void criarJanelaFuncoesCoordenacao(String text) {
        fc = new FuncoesCoordenacao(this, text);
    }

    public void criarJanelaSelecaoAcompanhantes(ArrayList<coordenador> c) {
        esac = new EscolheAcompanhantes(c, this, BancoDeDados.comando().getEventoAtual());
    }

    public void mensagemCoordenadorSucessoCadastroApresentacao(String nometitulo) {
        fc.setMessage("Sucesso ao cadastrar -- " + nometitulo + " --!\nPara cadastrar outras apresentações, comece pelo CPF do próximo apresentador, assim como fez para cadastrar essa apresentação");
    }

    boolean inclusaoDeApresentacaoPeloCoordenador() {
        return inclusaoDeApresentacaoPeloCoordenador;
    }

    public void criarJanelaRealizarAvalicoes(String text) {
        rav = new RealizarAvaliacoes(this, text, BancoDeDados.comando().getEventoAtual());
        coordenacoes = BancoDeDados.comando().recuperaLacosCoordenador(text);
    }

    public boolean temCoordenacaoGeral() {
        if (coordenacoes.size() < 1) {
            return false;
        }

        for (coordenador c : coordenacoes) {
            if (c.getCentralizado() > 0) {
                return true;
            }
        }

        return false;

    }

    public void gerarCadastroOficinaSessoesMonitores() {

        cosm = new CadastroOficinasSessoesMonitores(this);

    }

    public void gerarAvaliadoresAusentes() {

        avaus = new AvaliadoresAusentes(this, BancoDeDados.comando().getEventoAtual());

    }

    public void gerarEditarUnidades() {

        editu = new EditarUnidades(this);

    }

    public void criarJanelaGerarCertificados(boolean cordGeral) {

        gcapc = new GerarCertificadosAlunosParaCoordenadores(cordGeral);

    }

    public void testarDeleteApresentador() {
        BancoDeDados.comando().insert("INSERT INTO apresentador(cpf,nome,senha) VALUES('646464','Teste',md5('teste'));");
        BancoDeDados.comando().update("DELETE FROM apresentador WHERE cpf = '646464';");

    }

    void criarJanelaDeTickets(boolean coordenadorGeral, ArrayList<coordenador> coords) {
        
        gertic = new GerenciarTickets(coordenadorGeral,coords);
        
    }
    
    void criarJanelaConfigs(){
    
        cnf = new AlterarConfiguracoes();
    
    }

    /**
     * @return the versao
     */
    public String getVersao() {
        return versao;
    }

}
