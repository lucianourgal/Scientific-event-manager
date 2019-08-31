/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package janelas;

import ClassesAuxiliares.Arrumador;
import java.util.ArrayList;
import javax.swing.JCheckBox;
import objetosBase.apresentacao;
import objetosBase.autor;
import objetosBase.triviais.coordenador;

/**
 *
 * @author hp00
 */
public class SelecionarApresentadoresAprCultural extends javax.swing.JFrame {

    private final int Xminimo = 30;
    private final int Yminimo = 31;

    private final int alturaCampos = 25;

    private final int larguraCH = 250;
    private final int espacoVertical = 5;
    private final int espacoHorizontal = 12;
    ArrayList<apresentacao> apr;
    
    private ArrayList<JCheckBox> CHApresentador = new ArrayList<>();
    ArrayList<JCheckBox> nomesApresentacao = new ArrayList<>();
   
    ArrayList<autor> todosAutores = new ArrayList<>();

    int limiteVertical = 450;
    int limiteHorizontal = 800;

    int limiteLinhasPorPagina;
    int autoresPorLinha;
    int[] numeroAutores;

    int[] linhaDoTitulo;
    int[] linhaDoAutor;
    int[] colunaDoAutor;

    int[] paginaDoTitulo;
    int[] paginaDoAutor;

    private final ArrayList<apresentacao> apresentacoes;
    private int limitePag;
    private boolean iniciado=false;

    /**
     * Creates new form SelecionarApresentadoresAprCultural
     * @param coor Array de registros de coordenador
     * @param JP Janela Inicial
     * @param aps Conjunto de apresentacoes
     */
    public SelecionarApresentadoresAprCultural(ArrayList<coordenador> coor, final Inicial JP, ArrayList<apresentacao> aps) {
        apresentacoes = aps;
        descobrirLimiteLinhasPagina();
       
        initComponents();
        
        Slider.setMinimum(0);
        Slider.setMaximum(limitePag);
        apr = aps;
        this.setTitle("Selecionar apresentadores que irão ao evento centralizado");
        //this.setIconImage(Arrumador.comando().getIcone().getImage());
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        int ch = 0;

      /*   for (int apres = 0; apres < aps.size(); apres++) {

            nomesApresentacao.add(new JLabel(aps.get(apres).getId() + ": " + aps.get(apres).getNometitulo()));
            nomesApresentacao.get(apres).setBounds(Xminimo + 0 * (larguraCH + espacoHorizontal), Yminimo + apres * (alturaCampos + espacoVertical), larguraCH, alturaCampos);
            nomesApresentacao.get(apres).setToolTipText(aps.get(apres).getId() + ": " + aps.get(apres).getNometitulo());
            this.add(nomesApresentacao.get(apres));

            for (int aut = 0; aut < aps.get(apres).getAutores().size(); aut++) {

                CHApresentador.add(new JCheckBox(aps.get(apres).getAutores().get(aut).getNome() + " ( " +/*aps.get(apr).getAutores().get(a).getFuncaoNome()+ " CPF " + aps.get(apres).getAutores().get(aut).getCpf() + ") "));
                CHApresentador.get(ch).setBounds(Xminimo + (aut + 1) * (larguraCH + espacoHorizontal), Yminimo + apres * (alturaCampos + espacoVertical), larguraCH, alturaCampos);
                CHApresentador.get(ch).setToolTipText(aps.get(apres).getAutores().get(aut).getNome() + " ( " +/*aps.get(apr).getAutores().get(a).getFuncaoNome()+ " CPF " + aps.get(apres).getAutores().get(aut).getCpf() + ") ");
                this.add(CHApresentador.get(ch));
                ch++;
            }
        }*/
        for (int apres = 0; apres < aps.size(); apres++) {

            nomesApresentacao.add(new JCheckBox(aps.get(apres).getApresentadorNome()+" (principal)  -*-*-*-*-*   ID "+aps.get(apres).getId() + ": '" + aps.get(apres).getNometitulo()+"'"));
            nomesApresentacao.get(apres).setBounds(Xminimo, 
                    Yminimo + (linhaDoTitulo[apres]-paginaDoTitulo[apres]*limiteLinhasPorPagina) * (alturaCampos + espacoVertical),
                    limiteHorizontal - larguraCH, alturaCampos);
            nomesApresentacao.get(apres).setToolTipText(aps.get(apres).getId() + ": " + aps.get(apres).getNometitulo());
            this.add(nomesApresentacao.get(apres));

                }
        for (int aut = 0; aut < todosAutores.size(); aut++) {

                CHApresentador.add(new JCheckBox(todosAutores.get(aut).getNome() + " ( " + " CPF " + todosAutores.get(aut).getCpf() + ") "));
                CHApresentador.get(ch).setBounds(
                        Xminimo + (colunaDoAutor[aut]) * (larguraCH + espacoHorizontal), 
                        Yminimo + (linhaDoAutor[aut]-paginaDoAutor[aut]*limiteLinhasPorPagina) * (alturaCampos + espacoVertical), 
                        larguraCH, alturaCampos);
                CHApresentador.get(ch).setToolTipText(todosAutores.get(aut).getNome() + " ( " + " CPF " + todosAutores.get(aut).getCpf() + ") ");
                this.add(CHApresentador.get(ch));
                ch++;
            }
        
        
        atualizarInterface();
        iniciado = true;
        deixarPaginaVisivel();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        Slider = new javax.swing.JSlider();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jButton1.setText("Salvar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        Slider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                SliderStateChanged(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(492, Short.MAX_VALUE)
                .addComponent(Slider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Slider, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(471, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        aplicarAoBd();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void SliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_SliderStateChanged
        
        if(!iniciado)
            return;
        
        deixarPaginaVisivel();
        
    }//GEN-LAST:event_SliderStateChanged

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
            java.util.logging.Logger.getLogger(SelecionarApresentadoresAprCultural.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SelecionarApresentadoresAprCultural.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SelecionarApresentadoresAprCultural.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SelecionarApresentadoresAprCultural.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                //    new SelecionarApresentadoresAprCultural().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JSlider Slider;
    private javax.swing.JButton jButton1;
    // End of variables declaration//GEN-END:variables

    // recupera situação atual
    public void atualizarInterface() {

        for (int a = 0; a < todosAutores.size(); a++) {
            CHApresentador.get(a).setSelected(todosAutores.get(a).getAtivo() == 1);
        }
        for(int b=0;b<apresentacoes.size();b++)
            nomesApresentacao.get(b).setSelected(apresentacoes.get(b).getApresentadorEfetivoID()==-1);
        
    }

    //salva alterações
    public void aplicarAoBd() {
        int aux;
        for (int a = 0; a < todosAutores.size(); a++) {
            if (CHApresentador.get(a).isSelected()) {
                aux = 1;
            } else {
                aux = 0;
            }
            todosAutores.get(a).setAtivo(aux);
            todosAutores.get(a).atualizarSeMudouStatusAtivo();
        }

        for(int b=0;b<apresentacoes.size();b++){
            if(nomesApresentacao.get(b).isSelected())
            apresentacoes.get(b).setApresentadorEfetivoID(-1);
            //else, algum outro que esteja ativo deve ser o efetivo
            else
            apresentacoes.get(b).setApresentadorEfetivoID(-123);
                
            apresentacoes.get(b).atualizarAtivacaoAutores();
        
        }
        
        
    }

    public void descobrirLimiteLinhasPagina() {

        limiteLinhasPorPagina = limiteVertical / (alturaCampos + espacoVertical);
        autoresPorLinha = limiteHorizontal / (larguraCH + espacoHorizontal);

        System.out.println(limiteLinhasPorPagina+ " linhas por página, "+autoresPorLinha+" autores por linha.");
        
        descobrirLinhas();

    }

    public void descobrirLinhas() {

        int linhaAtual = 0;
        int autoresNaLinha;
        int indiceAutor = 0;

        numeroAutores = new int[apresentacoes.size()];

        for (int a = 0; a < apresentacoes.size(); a++) {
            todosAutores.addAll(apresentacoes.get(a).getAutores());
            numeroAutores[a] = apresentacoes.get(a).getAutores().size();
        }

        linhaDoTitulo = new int[apresentacoes.size()];
        paginaDoTitulo  = new int[apresentacoes.size()];
        linhaDoAutor = new int[todosAutores.size()];
        colunaDoAutor = new int[todosAutores.size()];
        paginaDoAutor = new int[todosAutores.size()];

        //define em que linha/coluna cada componente ficará
        for (int a = 0; a < apresentacoes.size(); a++) {
            
            linhaDoTitulo[a] = linhaAtual;
            linhaAtual++;
            autoresNaLinha = 0;
            
            for (int aut = 0; aut < numeroAutores[a]; aut++) {
                if (autoresNaLinha < autoresPorLinha) {
                    linhaDoAutor[indiceAutor] = linhaAtual;
                    colunaDoAutor[indiceAutor] = autoresNaLinha;
                    System.out.println("Autor "+aut+" L"+linhaAtual+" C"+autoresNaLinha);
                    autoresNaLinha++;
                } else {
                    autoresNaLinha = 0;
                    linhaAtual++;
                    linhaDoAutor[indiceAutor] = linhaAtual;
                    colunaDoAutor[indiceAutor] = autoresNaLinha;
                }
                indiceAutor++;
            }
            linhaAtual++;
        }

        int paginaAtual = 0;
        //define em que página cada componente ficará  ( limiteLinhasPorPagina )
        for (int a = 0; a < todosAutores.size(); a++) {
            paginaDoAutor[a] = paginaAtual;
            System.out.println("Autor "+a+", pág "+paginaAtual);
            if (linhaDoAutor[a] % limiteLinhasPorPagina == (limiteLinhasPorPagina - 1)) {
                paginaAtual++;
            }
        }
        paginaAtual = 0;
        for (int a = 0; a < apresentacoes.size(); a++) {
            paginaDoTitulo[a] = paginaAtual;
            if (linhaDoTitulo[a] % limiteLinhasPorPagina == (limiteLinhasPorPagina - 1)) {
                paginaAtual++;
            }
        }
        limitePag = paginaAtual;
    }
    
    public void deixarPaginaVisivel(){
    int p = Slider.getValue();
    
    for(int a=0;a<apresentacoes.size();a++){
        nomesApresentacao.get(a).setVisible(p==paginaDoTitulo[a]);
    }
    for(int b=0;b<todosAutores.size();b++)
        CHApresentador.get(b).setVisible(p==paginaDoAutor[b]);
    
    
    }
    

}
