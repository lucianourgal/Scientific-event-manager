/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package janelas;

import ClassesAuxiliares.Arrumador;
import ClassesAuxiliares.BancoDeDados;
import ClassesAuxiliares.MysqlToExcel;
import ClassesAuxiliares.separacaoQuartos;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import objetosBase.relatorioUnidade;
import objetosBase.triviais.area;
import objetosBase.triviais.bolsa;
import objetosBase.triviais.evento;
import objetosBase.triviais.modalidade;
import objetosBase.triviais.relatorioApresentacoesPorApresentador;
import objetosBase.triviais.unidade;

/**
 *
 * @author SistemaIP
 */
public class RelatoriosQuantitativos extends javax.swing.JFrame {

    private final Inicial JP;

    separacaoQuartos sep;
    ArrayList<unidade> unidades;
    ArrayList<relatorioUnidade> relatoriosUnidades;
    private boolean iniciado;
    private final ArrayList<bolsa> bolsas;
    private final ArrayList<modalidade> modalidades;
    private final ArrayList<area> areas;
    private relatorioApresentacoesPorApresentador relatApresentadores;
    private final ArrayList<evento> eventos;
    private final ArrayList<String> orders;
    
    /**
     * Creates new form RelatoriosQuantitativos
     * @param Jp Janela Inicial
     */
    public RelatoriosQuantitativos(Inicial Jp) {
        iniciado = false;
        JP = Jp;
        initComponents();
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setVisible(true);
        //this.setIconImage(Arrumador.comando().getIcone().getImage());
        this.setTitle("Relatórios Geral e em Planilha");

        CBEvento.removeAllItems();
        eventos = BancoDeDados.comando().getTodosEventos();
        for (evento evento : eventos) {
            CBEvento.addItem(evento.getNome());
        }
        
        
        
        unidades = BancoDeDados.comando().getUnidades();
        bolsas = BancoDeDados.comando().getBolsas();
        modalidades = BancoDeDados.comando().getModalidades();
        areas = BancoDeDados.comando().getAreas();

        CBVariavel1.removeAllItems();
        CBVariavel1.addItem("Modalidade");
        CBVariavel1.addItem("Area");
        CBVariavel1.addItem("Bolsa");
        CBVariavel1.addItem("Materiais necessarios");
        CBVariavel1.addItem("Apresentacoes por apresentador");
        CBVariavel1.addItem("Lista de participantes");

        CBEtapa.removeAllItems();
        CBEtapa.addItem("Nos campi");
        CBEtapa.addItem("Centralizado");

        CBEtapa.setSelectedIndex(1);
        
        CBMedida.removeAllItems();
        CBMedida.addItem("Apresentacao");
        //CBMedida.addItem("Avaliador");
        //CBMedida.addItem("Autores");

        
        iniciado = true;
        atualizarRelatorio();
        
        
        
        
        orders = new ArrayList<>();
        CBOrder.removeAllItems();
        CBOrder.addItem("id");
        orders.add("apresentacao.id");
        CBOrder.addItem("nome");
        orders.add("apresentacao.nome");
        CBOrder.addItem("unidade, nome");
        orders.add("unidade, apresentacao.nome");
        CBOrder.addItem("unidade, id");
        orders.add("unidade, apresentacao.id");
        CBOrder.addItem("area, id");
        orders.add("area, apresentacao.id");
        CBOrder.addItem("area, nome");
        orders.add("area, apresentacao.nome");
        CBOrder.addItem("area, unidade, id");
        orders.add("area, unidade, apresentacao.id");
        CBOrder.addItem("area, unidade, nome");
        orders.add("area, unidade, apresentacao.nome");
        
        CBEvento.setSelectedIndex(0);
        gerarRelatoriosUnidades(eventos.get(CBEvento.getSelectedIndex()));

    }

    private void atualizarRelatorio() {

        if (!iniciado) {
            return;
        }

        String[] colunas = new String[1];
        String[][] linhas = new String[1][1];
        int[] somas;
        //CBVariavel1:  Por bolsas, por modalidade, "Materiais", por area
        //CBUnidade, CBEtapa (centralizado)
        //CBMedida:  Apresentações, avaliadores, autores(?)
        int aux;

        if (CBVariavel1.getSelectedItem().toString().equals("Bolsa")) {  // caso seja por bolsa
            colunas = new String[bolsas.size() + 2];
            linhas = new String[unidades.size() + 1][bolsas.size() + 2];

            colunas[0] = "*";
            colunas[bolsas.size() + 1] = "Soma";
            somas = new int[bolsas.size()];
            for (int b = 0; b < bolsas.size(); b++) {
                somas[b] = 0;
                colunas[b + 1] = bolsas.get(b).getNome();
            }

            for (int a = 0; a < unidades.size(); a++) {

                linhas[a][0] = relatoriosUnidades.get(a).getNomeUnidade();
                for (int b = 0; b < bolsas.size(); b++) {
                    aux = relatoriosUnidades.get(a).getQuantidadeBolsaPorID(bolsas.get(b).getId());
                    linhas[a][b + 1] = "" + aux;
                    somas[b] += aux;
                }
                linhas[a][bolsas.size() + 1] = "" + relatoriosUnidades.get(a).getQuantidadeTotalBolsas();
            }
            for (int b = 0; b < bolsas.size(); b++) {
                linhas[unidades.size()][b + 1] = "" + somas[b];
            }

        } // fim de caso seja por bolsa
        else if (CBVariavel1.getSelectedItem().toString().equals("Modalidade")) {  // caso seja por modalidade
            colunas = new String[modalidades.size() + 2];
            linhas = new String[unidades.size() + 1][modalidades.size() + 2];
            somas = new int[modalidades.size()];

            colunas[0] = "*";
            colunas[modalidades.size() + 1] = "Soma";
            for (int b = 0; b < modalidades.size(); b++) {
                colunas[b + 1] = modalidades.get(b).getNome();
                somas[b] = 0;
            }

            int qtdTotal = 0;
            for (int a = 0; a < unidades.size(); a++) {

                linhas[a][0] = relatoriosUnidades.get(a).getNomeUnidade();
                for (int b = 0; b < modalidades.size(); b++) {
                    aux = relatoriosUnidades.get(a).getQuantidadeModalidadePorID(modalidades.get(b).getId());
                    linhas[a][b + 1] = "" + aux;
                    somas[b] += aux;
                    qtdTotal += aux;
                }
                linhas[a][modalidades.size() + 1] = "" + relatoriosUnidades.get(a).getQuantidadeTotalModalidades();
            }

            for (int b = 0; b < modalidades.size(); b++) {
                linhas[unidades.size()][b + 1] = "" + somas[b];
            }
            linhas[unidades.size()][modalidades.size()+1] = qtdTotal+"";
        } // fim de caso seja por modalidade
        else if (CBVariavel1.getSelectedItem().toString().equals("Area")) {  // caso seja por area
            colunas = new String[areas.size() + 2];
            linhas = new String[unidades.size() + 1][areas.size() + 2];
            somas = new int[areas.size()];

            colunas[0] = "*";
            colunas[areas.size() + 1] = "Soma";
            for (int b = 0; b < areas.size(); b++) {
                colunas[b + 1] = areas.get(b).getNome();
                somas[b] = 0;
            }

            int qtdTotal = 0;
            for (int a = 0; a < unidades.size(); a++) {

                linhas[a][0] = relatoriosUnidades.get(a).getNomeUnidade();
                for (int b = 0; b < areas.size(); b++) {
                    aux = relatoriosUnidades.get(a).getQuantidadeAreaPorID(areas.get(b).getId());
                    linhas[a][b + 1] = "" + aux;
                    somas[b] += aux;
                    qtdTotal += aux;
                }
                linhas[a][areas.size() + 1] = "" + relatoriosUnidades.get(a).getQuantidadeTotalAreas();
            }

            for (int b = 0; b < areas.size(); b++) {
                linhas[unidades.size()][b + 1] = "" + somas[b];
            }
            linhas[unidades.size()][areas.size() + 1] = qtdTotal+"";
        } // fim de caso seja por area
        else if (CBVariavel1.getSelectedItem().toString().equals("Materiais necessarios")) {  // caso seja por materiais necessários
            aux = 0;
            ArrayList<String> materiais = new ArrayList<>();
            ArrayList<String> apresentacaoNome = new ArrayList<>();
            ArrayList<Integer> apresentacaoID = new ArrayList<>();
            ArrayList<String> apresentacaoUnidade = new ArrayList<>();
            //descobrir quantos materias serão necessários     
            for (relatorioUnidade u : relatoriosUnidades) {
                aux += u.getNumeroApresentacoesQuePrecisamDeMateriais();
                for (int a = 0; a < u.getNumeroApresentacoesQuePrecisamDeMateriais(); a++) {
                    materiais.addAll(u.getMateriais());
                    apresentacaoNome.addAll(u.getApresentacaoNome());
                    apresentacaoID.addAll(u.getApresentacaoID());
                    apresentacaoUnidade.addAll(u.getArrayUnidade());
                }
            }

            colunas = new String[4];
            linhas = new String[aux][4];

            colunas[0] = "ID apr.";
            colunas[1] = "Nome apr.";
            colunas[2] = "Materiais necessários";
            colunas[3] = "Unidade";

            for (int a = 0; a < aux; a++) {

                linhas[a][0] = apresentacaoID.get(a).toString();
                linhas[a][1] = apresentacaoNome.get(a);
                linhas[a][2] = materiais.get(a);
                linhas[a][3] = apresentacaoUnidade.get(a);
            }

        } // fim de caso seja por materiais
        //"Apresentacoes por apresentador"
        else if (CBVariavel1.getSelectedItem().toString().equals("Apresentacoes por apresentador")) {

            if (relatApresentadores == null) {
                relatApresentadores = new relatorioApresentacoesPorApresentador(CBEtapa.getSelectedIndex(),eventos.get(CBEvento.getSelectedIndex()));
            }

            colunas = relatApresentadores.getColunaParaRelatorio();
            linhas = relatApresentadores.getLinhasParaRelatorio();

        } // fim para caso de apresentacoes por apresentador
        else if (CBVariavel1.getSelectedItem().toString().equals("Lista de participantes")) {

            if (sep == null) {
                sep = new separacaoQuartos(eventos.get(CBEvento.getSelectedIndex()));
            }

            colunas = sep.getColunasTitulo();
            linhas = sep.getLinhas();

        }

        setInformacoesNaTabela(colunas, linhas);

    }

    public void setInformacoesNaTabela(String[] colunas, String[][] linhas) {
        DefaultTableModel model = new DefaultTableModel(linhas, colunas);
        TRelatorio.setModel(model);
        System.out.println("Relatorio atualizado!");

    }

    public void gerarRelatoriosUnidades(evento e) {
        relatoriosUnidades = new ArrayList<>();

        for (unidade u : unidades) {
            relatoriosUnidades.add(new relatorioUnidade(u.getId(), CBEtapa.getSelectedIndex(), u.getNome(),e));
            relatoriosUnidades.get(relatoriosUnidades.size() - 1).relatDoObjeto();
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

        jLabel1 = new javax.swing.JLabel();
        CBVariavel1 = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        CBMedida = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        CBEtapa = new javax.swing.JComboBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        TRelatorio = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        CBEvento = new javax.swing.JComboBox();
        jLabel7 = new javax.swing.JLabel();
        CBOrder = new javax.swing.JComboBox();
        TFUrl = new javax.swing.JTextField();
        BTGerarPlanilha = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setText("Variavel 1");

        CBVariavel1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        CBVariavel1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                CBVariavel1ItemStateChanged(evt);
            }
        });

        jLabel3.setText("Medida");

        CBMedida.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        CBMedida.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                CBMedidaItemStateChanged(evt);
            }
        });

        jLabel4.setText("Etapa");

        CBEtapa.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        CBEtapa.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                CBEtapaItemStateChanged(evt);
            }
        });

        TRelatorio.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(TRelatorio);

        jLabel5.setText("Evento");

        CBEvento.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel7.setText("Ordenar por:");

        CBOrder.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        CBOrder.setToolTipText("Ordem dos dados na planilha");
        CBOrder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CBOrderActionPerformed(evt);
            }
        });

        TFUrl.setText("Relatorio");
        TFUrl.setToolTipText("Local onde será salva a planilha");

        BTGerarPlanilha.setText("Gerar Planilha");
        BTGerarPlanilha.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTGerarPlanilhaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(CBVariavel1, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(CBEvento, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(CBMedida, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(CBEtapa, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(CBOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(TFUrl, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(BTGerarPlanilha, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1053, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel4)
                        .addComponent(CBEtapa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3)
                        .addComponent(CBMedida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(CBVariavel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel7)
                        .addComponent(CBOrder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(TFUrl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(BTGerarPlanilha))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel5)
                        .addComponent(CBEvento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(435, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addContainerGap(74, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 415, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap()))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void CBEtapaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_CBEtapaItemStateChanged
        gerarRelatoriosUnidades(eventos.get(CBEvento.getSelectedIndex()));
        atualizarRelatorio();
    }//GEN-LAST:event_CBEtapaItemStateChanged

    private void CBVariavel1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_CBVariavel1ItemStateChanged
        atualizarRelatorio();
    }//GEN-LAST:event_CBVariavel1ItemStateChanged

    private void CBMedidaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_CBMedidaItemStateChanged
        atualizarRelatorio();
    }//GEN-LAST:event_CBMedidaItemStateChanged

    private void CBOrderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CBOrderActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_CBOrderActionPerformed

    private void BTGerarPlanilhaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTGerarPlanilhaActionPerformed
      MysqlToExcel mq = new MysqlToExcel();
      boolean b = mq.gerarPlanilha(eventos.get(CBEvento.getSelectedIndex()), TFUrl.getText()+" "+CBEvento.getSelectedItem().toString() ,  orders.get(CBOrder.getSelectedIndex())); //evento, url, order
      
      if(b){
          BTGerarPlanilha.setText("Arquivo gerado!");
          BTGerarPlanilha.setEnabled(false);
      }else
          BTGerarPlanilha.setText("Falha! Tentar novamente");
      
    }//GEN-LAST:event_BTGerarPlanilhaActionPerformed

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
            java.util.logging.Logger.getLogger(RelatoriosQuantitativos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(RelatoriosQuantitativos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(RelatoriosQuantitativos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(RelatoriosQuantitativos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                //  new RelatoriosQuantitativos().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BTGerarPlanilha;
    private javax.swing.JComboBox CBEtapa;
    private javax.swing.JComboBox CBEvento;
    private javax.swing.JComboBox CBMedida;
    private javax.swing.JComboBox CBOrder;
    private javax.swing.JComboBox CBVariavel1;
    private javax.swing.JTextField TFUrl;
    private javax.swing.JTable TRelatorio;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables

}
