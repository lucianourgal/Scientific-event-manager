/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package janelas;

import ClassesAuxiliares.Arrumador;
import ClassesAuxiliares.BancoDeDados;
import ClassesAuxiliares.geradorCertificados;
import java.util.ArrayList;
import objetosBase.triviais.evento;
import objetosBase.triviais.modalidade;
import objetosBase.triviais.unidade;

/**
 *
 * @author hp00
 */
public class GerarCertificadosAlunosParaCoordenadores extends javax.swing.JFrame {

    private final ArrayList<evento> eventos;
    private final ArrayList<modalidade> modalidades;
    private final ArrayList<unidade> unidades;
    
    private ArrayList<Integer> apresentacoesApresentadorPrincipalSeManteve = new ArrayList<>();
    private ArrayList<Integer> autoresAReceberCertificado = new ArrayList<>();
    
    
    /**
     * Creates new form GerarCertificadosAlunosParaCoordenadores
     */
    public GerarCertificadosAlunosParaCoordenadores(boolean coordGeral) {
        initComponents();
        
        if(!coordGeral){
            BTGerarSalvarBD.setEnabled(false);
            BTGerarNaoSalvarBD.setEnabled(false);
        }
        
        this.setTitle("Gerar conjunto de Certificados do Evento");
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setVisible(true);
        //this.setIconImage(Arrumador.comando().getIcone().getImage());
        
        CBCentralizado.removeAllItems();
        //CBCentralizado.addItem("Não");
        CBCentralizado.addItem("Sim");
        
        CBEvento.removeAllItems();
        if(coordGeral)
        eventos = BancoDeDados.comando().getTodosEventos();
        else{
        eventos = new ArrayList<>();
        eventos.add(BancoDeDados.comando().getEventoAtual());
           }  
                
        for (evento evento : eventos) {
            CBEvento.addItem(evento.getNome());
        }
        CBModalidade.removeAllItems();
        CBModalidade.addItem("Todas");
        modalidades = BancoDeDados.comando().getModalidades();
        for (modalidade modalidade : modalidades) {
       //     CBModalidade.addItem(modalidade.getNome());
        }
        CBUnidade.removeAllItems();
        CBUnidade.addItem("Todas");
        unidades = BancoDeDados.comando().getUnidades();
        for(int a =0;a<unidades.size();a++){
         //   CBUnidade.addItem(unidades.get(a).getNome());
        }
        
    }
    
    public void descobrirQuemApresentou(){
    
    if(modalidades.get(CBModalidade.getSelectedIndex()).getId()==4){
    // É para apresentação cultural. Usa funções diferenciadas
    
        autoresAReceberCertificado = recuperarIdAutoresQueApresentaramCultural();
        apresentacoesApresentadorPrincipalSeManteve = recuperarIdApresentacoesApresentadorSeManteve();
        
    }else{
    //não é apresentação cultural
    
        autoresAReceberCertificado = recuperarIDAutoresQueApresentaramNaoCultural();
        apresentacoesApresentadorPrincipalSeManteve = recuperarIdApresentacoesApresentadorSeManteve();
        
    }
    
    Relatorio.setText("Autores a receber certificado: "+ autoresAReceberCertificado.size()+""
            + "\nApresentadores que se mantiveram a receber certificado: "+apresentacoesApresentadorPrincipalSeManteve.size());
    
    }
    
    
    public ArrayList<Integer> recuperarIDAutoresQueApresentaramNaoCultural(){
    // Toda apresentação que tiver apresentadorEfetivo = -1, modalidade escolhida.  Com minimo duas avaliações
        ArrayList<Integer> ids;
        
        ids = BancoDeDados.comando().recuperarIDAutoresQueApresentaramNaoCultural(eventos.get(CBEvento.getSelectedIndex()).getId(), 
                modalidades.get(CBModalidade.getSelectedIndex()).getId(),unidades.get(CBUnidade.getSelectedIndex()).getId() );
             
    
    return ids;    
    }
    public ArrayList<Integer> recuperarIdApresentacoesApresentadorSeManteve(){
      // Todos os valores positivos que aparecerem em apresentadorEfetivo, modalidade escolhida. Com minimo duas avaliacoes
     ArrayList<Integer> ids;
        //ids = new ArrayList<>();
       
    
        ids = BancoDeDados.comando().recuperarIdApresentacoesApresentadorSeManteve(eventos.get(CBEvento.getSelectedIndex()).getId(), 
                modalidades.get(CBModalidade.getSelectedIndex()).getId(),unidades.get(CBUnidade.getSelectedIndex()).getId() );
        
    
    return ids;  
    }
    
    public ArrayList<Integer> recuperarIdAutoresQueApresentaramCultural(){
        // Todo autor com ativo = 1, de uma apresentação que seja cultural
     ArrayList<Integer> ids;
        
     ids = BancoDeDados.comando().recuperarIdAutoresQueApresentaramCultural(eventos.get(CBEvento.getSelectedIndex()).getId(), 
                modalidades.get(CBModalidade.getSelectedIndex()).getId(),unidades.get(CBUnidade.getSelectedIndex()).getId() );
       
    
    return ids;  
    
    }
    
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        CBUnidade = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        CBEvento = new javax.swing.JComboBox();
        CBModalidade = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        BTGerarNaoSalvarBD = new javax.swing.JButton();
        CBCentralizado = new javax.swing.JComboBox();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        Relatorio = new javax.swing.JTextArea();
        BTGerarDoBD = new javax.swing.JButton();
        BTGerarSalvarBD = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        CBUnidade.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel3.setText("Evento");

        jLabel6.setText("Unidade");

        CBEvento.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        CBModalidade.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel5.setText("Modalidade");

        BTGerarNaoSalvarBD.setText("Gerar Arquivos do Zero sem BD");
        BTGerarNaoSalvarBD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTGerarNaoSalvarBDActionPerformed(evt);
            }
        });

        CBCentralizado.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel7.setText("Centralizado");

        Relatorio.setEditable(false);
        Relatorio.setColumns(20);
        Relatorio.setRows(5);
        Relatorio.setText("As operações podem levar vários minutos.\nPara gerar os certificados já cadastrados, clicar em\n\"Gerar Arquivos existentes no BD\".");
        jScrollPane1.setViewportView(Relatorio);

        BTGerarDoBD.setForeground(new java.awt.Color(0, 51, 51));
        BTGerarDoBD.setText("Gerar Arquivos existentes no BD");
        BTGerarDoBD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTGerarDoBDActionPerformed(evt);
            }
        });

        BTGerarSalvarBD.setText("Gerar Arquivos, Salvar no BD");
        BTGerarSalvarBD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTGerarSalvarBDActionPerformed(evt);
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
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(CBModalidade, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(CBEvento, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addGap(1, 1, 1)
                                        .addComponent(CBUnidade, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(CBCentralizado, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(33, 33, 33))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(BTGerarNaoSalvarBD, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 214, Short.MAX_VALUE)
                                    .addComponent(BTGerarSalvarBD, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(BTGerarDoBD, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 437, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(CBEvento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(CBModalidade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(CBUnidade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(CBCentralizado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(BTGerarNaoSalvarBD, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(BTGerarSalvarBD, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(BTGerarDoBD, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BTGerarNaoSalvarBDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTGerarNaoSalvarBDActionPerformed
        evento e = eventos.get(CBEvento.getSelectedIndex());
        geradorCertificados gc = new geradorCertificados();
        gc.gerarConjuntoCertsSEPIN(e,false);
        Relatorio.setText("Gerou e NÃO salvou no BD: \n"+gc.getRelatorio());
        
    }//GEN-LAST:event_BTGerarNaoSalvarBDActionPerformed

    private void BTGerarDoBDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTGerarDoBDActionPerformed
        evento e = eventos.get(CBEvento.getSelectedIndex());
        geradorCertificados gc = new geradorCertificados();
        BancoDeDados.comando().getCertificados(gc,true,false,e.getId());
        Relatorio.setText("Gerou certificados direto do BD para a pasta local. \n"+BancoDeDados.comando().getRelatorioCerts());
    }//GEN-LAST:event_BTGerarDoBDActionPerformed

    private void BTGerarSalvarBDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTGerarSalvarBDActionPerformed
        evento e = eventos.get(CBEvento.getSelectedIndex());
        geradorCertificados gc = new geradorCertificados();
        gc.gerarConjuntoCertsSEPIN(e,true);
        Relatorio.setText("Gerou e salvou no BD: \n"+gc.getRelatorio());
        
    }//GEN-LAST:event_BTGerarSalvarBDActionPerformed

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
            java.util.logging.Logger.getLogger(GerarCertificadosAlunosParaCoordenadores.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GerarCertificadosAlunosParaCoordenadores.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GerarCertificadosAlunosParaCoordenadores.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GerarCertificadosAlunosParaCoordenadores.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
         //       new GerarCertificadosAlunosParaCoordenadores().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BTGerarDoBD;
    private javax.swing.JButton BTGerarNaoSalvarBD;
    private javax.swing.JButton BTGerarSalvarBD;
    private javax.swing.JComboBox CBCentralizado;
    private javax.swing.JComboBox CBEvento;
    private javax.swing.JComboBox CBModalidade;
    private javax.swing.JComboBox CBUnidade;
    private javax.swing.JTextArea Relatorio;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
