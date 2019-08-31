/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor. marIELEN UMUARAMA
 */
package janelas;

import ClassesAuxiliares.Arrumador;
import ClassesAuxiliares.BancoDeDados;
import java.util.ArrayList;
import objetosBase.triviais.coordenador;
import objetosBase.triviais.unidade;

/**
 *
 * @author Luciano
 */
public class FuncoesCoordenacao extends javax.swing.JFrame {

    private final Inicial JP;
    private CadastroApresentador cad;
    private GerenciarApresentacoes gap;
    private GerarAnaisDoSeminario gas;
    private final ArrayList<coordenador> coord;
    private boolean coordenadorGeral = false;

    private int vagasOcupadas=-1;
    private int vagasTotais=-1;
    
    
    /**
     * Creates new form FuncoesCoordenacao
     *
     * @param j Janela Inicial
     * @param cpf1 CPF do coordenador
     */
    public FuncoesCoordenacao(Inicial j, String cpf1) {
        JP = j;
        coord = BancoDeDados.comando().recuperaLacosCoordenador(cpf1);

        for (coordenador c : coord) {
            if (c.getCentralizado() == 1) {
                coordenadorGeral = true;
            }
        }
        
        
        
        initComponents();
        this.setLocationRelativeTo(null);
        this.setTitle("Funções de Coordenação e Administração");
        //this.setResizable(false);
      //  this.setIconImage(Arrumador.comando().getIcone().getImage());
        this.setVisible(true);
        this.toFront();

        if (!coordenadorGeral) {
            BTGerarAnaisSeminario.setEnabled(false);
            BTSelecionarApresentacoes.setEnabled(false);
            CoordenarAvaliacoes.setEnabled(false);
            BTRelatorios.setEnabled(false);
            BTRankingApresentacoes.setEnabled(false);
            BTPalestrasOficinasMonitores.setEnabled(false);
            BTAvaliadoresAusentes.setEnabled(false);
           // BTGerarCertificados.setEnabled(false);
            BTEditarUnidades.setEnabled(false);
            
            if(BancoDeDados.comando().isBloqueioEdicaoGeralApresentacoes())
                BTApresentacoes.setEnabled(false);
            
            if(BancoDeDados.comando().isBloqueiAreaApresentador())
                BTCadastroAluno.setEnabled(false);
            
            
            if(temVagasDeAcompanhanteLivres(coord.get(0).getUnidadeCodigo())){
            Aviso.setText("ATENÇÃO: Ainda há ("+ (vagasTotais-vagasOcupadas)+" de "+vagasTotais+") "
                    + "vagas para acompanhantes em seu Campus para o SEPIN. \nUtilize a janela de 'Indicar Servidores Acompanhantes' para completar a lista.");
            }
            
            //detecção de trabalhos repetidos - Titulos muito parecidos ou mesmos autores...
            
            //deteção de trabalhos que talvez não sejam do campus - Somente apresentador da unidade é do campus
            
                       
        }else{
        //se for coordenador geral
            String relat = "";
            for(unidade u: BancoDeDados.comando().getUnidades()){
                
                if(temVagasDeAcompanhanteLivres(u.getId())){
                    relat = relat + vagasOcupadas+" de "+vagasTotais+": Campus "+u.getNome()+" ainda não ocupou todas as vagas de Acompanhante/Avaliador.\n";
                }
                
            }
            
            int avaliados = BancoDeDados.comando().selectCount("SELECT COUNT(*) FROM avaliacao WHERE ativa = 1;");
            
            
            if(avaliados > 0) {
                
                int total =  BancoDeDados.comando().selectCount("SELECT COUNT(*) FROM avaliacao;");
                relat = relat + "Avaliacoes: " + avaliados + " lancamentos de " + total + ".\n";
            
                int ausentes = BancoDeDados.comando().selectCount("SELECT COUNT(apresentacao) FROM avaliacao WHERE ativa = 1 AND ausente = 1;");

                if(ausentes > 0)
                    relat = relat + "Avaliacoes: " + ausentes + " apresentacoes ausentes. \n";
                }

                if(relat.length()>1)
                    relat = "ATENÇÃO: Avaliadores não marcados como acompanhantes não aparecem na lista de avaliadores disponíveis na janela de"
                            + "\ndistribuição de avalições, nem tampouco serão certificados.\n\n" + relat;

                Aviso.setText(relat);
        }
        
        
        /*  TFCpf.addKeyListener(new KeyAdapter() {
         public void keyTyped(KeyEvent e) {
         char vChar = e.getKeyChar();
         if (!(Character.isDigit(vChar)
         || (vChar == KeyEvent.VK_BACK_SPACE)
         || (vChar == KeyEvent.VK_DELETE))) {
         e.consume();}        
         if (TFCpf.getText().length() <= 10) {  
         //deixe passar  
         } else {  
         e.setKeyChar((char) KeyEvent.VK_CLEAR);  
         }  
         }
         });*/
        // Cadastro apresentador/apresentacao, Editar apresentacoes,  relatorios, coordenarAvaliacoes, selecionarAvaliacoes
        // Gerar Anais, 
    }
    
    private boolean temVagasDeAcompanhanteLivres(int codigoUnidade){
    
        vagasOcupadas = BancoDeDados.comando().selectCount("SELECT count(cpf) FROM avaliador WHERE acompanhante = 1 "
                    + "AND unidade = "+codigoUnidade+";");
        
        vagasTotais = BancoDeDados.comando().selectCount
        ("SELECT max(limiteacompanhantes) FROM unidade WHERE id ="+codigoUnidade+";");
        
        return (vagasOcupadas < vagasTotais);
        
    }
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        BTCadastroAluno = new javax.swing.JButton();
        BTApresentacoes = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        Aviso = new javax.swing.JTextArea();
        BTGerarAnaisSeminario = new javax.swing.JButton();
        BTRelatorios = new javax.swing.JButton();
        CoordenarAvaliacoes = new javax.swing.JButton();
        BTSelecionarApresentacoes = new javax.swing.JButton();
        BTCadastroAluno1 = new javax.swing.JButton();
        BTRankingApresentacoes = new javax.swing.JButton();
        BTPalestrasOficinasMonitores = new javax.swing.JButton();
        BTAvaliadoresAusentes = new javax.swing.JButton();
        BTEditarUnidades = new javax.swing.JButton();
        BTGerarCertificados = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        BTCadastroAluno.setText("Cadastro de Apresentação");
        BTCadastroAluno.setToolTipText("Cadastro do apresentador, seguido de cadastro de trabalhos que este tem para apresentar");
        BTCadastroAluno.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTCadastroAlunoActionPerformed(evt);
            }
        });

        BTApresentacoes.setText("Editar Apresentações");
        BTApresentacoes.setToolTipText("Edição de apresentações já cadastradas");
        BTApresentacoes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTApresentacoesActionPerformed(evt);
            }
        });

        Aviso.setEditable(false);
        Aviso.setColumns(20);
        Aviso.setRows(5);
        jScrollPane1.setViewportView(Aviso);

        BTGerarAnaisSeminario.setText("Gerar Anais do Seminário");
        BTGerarAnaisSeminario.setToolTipText("Geração de arquivos de anais");
        BTGerarAnaisSeminario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTGerarAnaisSeminarioActionPerformed(evt);
            }
        });

        BTRelatorios.setText("Relatórios");
        BTRelatorios.setToolTipText("Geração de relatórios quantitativos, e relatório geral em planilha eletrônica");
        BTRelatorios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTRelatoriosActionPerformed(evt);
            }
        });

        CoordenarAvaliacoes.setText("Coordenar Avaliações");
        CoordenarAvaliacoes.setToolTipText("Selecionar que avaliadores avaliaram cada apresentação");
        CoordenarAvaliacoes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CoordenarAvaliacoesActionPerformed(evt);
            }
        });

        BTSelecionarApresentacoes.setText("Selecionar Apresentações");
        BTSelecionarApresentacoes.setToolTipText("Marcar apresentações como confirmadas, casos de apresentadores substituidos, entre outros.");
        BTSelecionarApresentacoes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTSelecionarApresentacoesActionPerformed(evt);
            }
        });

        BTCadastroAluno1.setText("Indicar Servidores Acompanhantes");
        BTCadastroAluno1.setToolTipText("Selecionar servidores acompanhantes para o evento central ");
        BTCadastroAluno1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTCadastroAluno1ActionPerformed(evt);
            }
        });

        BTRankingApresentacoes.setText("Ranking de Apresentações");
        BTRankingApresentacoes.setToolTipText("Ranking de notas de apresentações");
        BTRankingApresentacoes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTRankingApresentacoesActionPerformed(evt);
            }
        });

        BTPalestrasOficinasMonitores.setText("Palestras, Oficinas, Sessões Tem,  Monitores");
        BTPalestrasOficinasMonitores.setToolTipText("Cadastro pela Coordenação Geral de Palestras, Oficinas, Sessões Temáticas e Monitorias, para certificados");
        BTPalestrasOficinasMonitores.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTPalestrasOficinasMonitoresActionPerformed(evt);
            }
        });

        BTAvaliadoresAusentes.setText("Marcar Avaliadores Ausentes");
        BTAvaliadoresAusentes.setToolTipText("Indicar Avaliadores ausentes no evento");
        BTAvaliadoresAusentes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTAvaliadoresAusentesActionPerformed(evt);
            }
        });

        BTEditarUnidades.setText("Dados de Unidades e Configurações");
        BTEditarUnidades.setToolTipText("Edição de dados das unidades/campi");
        BTEditarUnidades.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTEditarUnidadesActionPerformed(evt);
            }
        });

        BTGerarCertificados.setText("Gerar Certificados");
        BTGerarCertificados.setToolTipText("Geração de certificados de evento. Pode ser utilizado pelo coordenador de pesquida do campus.");
        BTGerarCertificados.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTGerarCertificadosActionPerformed(evt);
            }
        });

        jButton1.setText("Tickets de Cadastro");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1))
            .addGroup(layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(BTEditarUnidades, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(BTGerarCertificados, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(BTSelecionarApresentacoes, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(CoordenarAvaliacoes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(BTRelatorios, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(BTAvaliadoresAusentes, javax.swing.GroupLayout.DEFAULT_SIZE, 249, Short.MAX_VALUE)
                                    .addComponent(BTGerarAnaisSeminario, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addComponent(BTCadastroAluno, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(BTCadastroAluno1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 468, Short.MAX_VALUE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                    .addGap(15, 15, 15)
                                    .addComponent(BTApresentacoes, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                    .addGap(17, 17, 17)
                                    .addComponent(BTPalestrasOficinasMonitores, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(17, 17, 17)
                                .addComponent(BTRankingApresentacoes, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                .addGap(13, 13, 13))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(BTCadastroAluno, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(BTApresentacoes, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(BTCadastroAluno1, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(BTAvaliadoresAusentes, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(CoordenarAvaliacoes, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(BTPalestrasOficinasMonitores, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(BTEditarUnidades, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(BTRelatorios, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(BTSelecionarApresentacoes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(BTGerarCertificados, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(11, 11, 11)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(BTGerarAnaisSeminario, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(BTRankingApresentacoes, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 134, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BTCadastroAlunoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTCadastroAlunoActionPerformed

        JP.criarVHSCApresentacao(true);

        /*  if(!Arrumador.comando().validarCPF(TFCpf.getText()))
         {
         Aviso.setText("CPF Incorreto!");
         return;        
         }
            
         String SQL = "SELECT COUNT(*) FROM apresentador WHERE cpf = '"+ TFCpf.getText()+"';";
         int contagem = BancoDeDados.comando().selectCount(SQL);
         System.out.println("O número de registros para esse CPF na categoria é: "+ contagem+ ".");
        
         if(contagem==0){
         cad = new CadastroApresentador("INSERT", JP, TFCpf.getText());
         Aviso.setText("Novo cadastro de apresentador em andamento!");
         }
         else {
         Arrumador.comando().recuperarDadosUser(TFCpf.getText());
         cad = new CadastroApresentador("UPDATE", JP, TFCpf.getText());
         Aviso.setText("Cadastro já existente. Iniciando alteração!");
         }*/

    }//GEN-LAST:event_BTCadastroAlunoActionPerformed

    private void BTGerarAnaisSeminarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTGerarAnaisSeminarioActionPerformed

        JP.criarJanelaAnais();
    }//GEN-LAST:event_BTGerarAnaisSeminarioActionPerformed

    private void BTRelatoriosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTRelatoriosActionPerformed
        JP.criarJanelaRelatorios();

    }//GEN-LAST:event_BTRelatoriosActionPerformed

    private void CoordenarAvaliacoesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CoordenarAvaliacoesActionPerformed
        JP.criarJanelaEscolherAvaliadores(coord);
    }//GEN-LAST:event_CoordenarAvaliacoesActionPerformed

    private void BTSelecionarApresentacoesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTSelecionarApresentacoesActionPerformed
        JP.criarJanelaSelecionarApresentacoes(coord);
    }//GEN-LAST:event_BTSelecionarApresentacoesActionPerformed

    private void BTApresentacoesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTApresentacoesActionPerformed

        JP.criarJanelaGerenciamentoApresentacoes(coord);
    }//GEN-LAST:event_BTApresentacoesActionPerformed

    private void BTCadastroAluno1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTCadastroAluno1ActionPerformed
        JP.criarJanelaSelecaoAcompanhantes(coord);
    }//GEN-LAST:event_BTCadastroAluno1ActionPerformed

    private void BTRankingApresentacoesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTRankingApresentacoesActionPerformed
        JP.criarJanelaRanking();
    }//GEN-LAST:event_BTRankingApresentacoesActionPerformed

    private void BTPalestrasOficinasMonitoresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTPalestrasOficinasMonitoresActionPerformed
        JP.gerarCadastroOficinaSessoesMonitores();
    }//GEN-LAST:event_BTPalestrasOficinasMonitoresActionPerformed

    private void BTAvaliadoresAusentesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTAvaliadoresAusentesActionPerformed
        JP.gerarAvaliadoresAusentes();
    }//GEN-LAST:event_BTAvaliadoresAusentesActionPerformed

    private void BTEditarUnidadesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTEditarUnidadesActionPerformed
       JP.gerarEditarUnidades();
    }//GEN-LAST:event_BTEditarUnidadesActionPerformed

    private void BTGerarCertificadosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTGerarCertificadosActionPerformed
       JP.criarJanelaGerarCertificados(coordenadorGeral);
    }//GEN-LAST:event_BTGerarCertificadosActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        JP.criarJanelaDeTickets(coordenadorGeral,coord);
    }//GEN-LAST:event_jButton1ActionPerformed

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
            java.util.logging.Logger.getLogger(FuncoesCoordenacao.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FuncoesCoordenacao.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FuncoesCoordenacao.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FuncoesCoordenacao.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                //new FuncoesCoordenacao().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea Aviso;
    private javax.swing.JButton BTApresentacoes;
    private javax.swing.JButton BTAvaliadoresAusentes;
    private javax.swing.JButton BTCadastroAluno;
    private javax.swing.JButton BTCadastroAluno1;
    private javax.swing.JButton BTEditarUnidades;
    private javax.swing.JButton BTGerarAnaisSeminario;
    private javax.swing.JButton BTGerarCertificados;
    private javax.swing.JButton BTPalestrasOficinasMonitores;
    private javax.swing.JButton BTRankingApresentacoes;
    private javax.swing.JButton BTRelatorios;
    private javax.swing.JButton BTSelecionarApresentacoes;
    private javax.swing.JButton CoordenarAvaliacoes;
    private javax.swing.JButton jButton1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables

    void seekndestroy() {
        dispose();
    }

    void setMessage(String string) {
        Aviso.setText(string);
    }

    
}
