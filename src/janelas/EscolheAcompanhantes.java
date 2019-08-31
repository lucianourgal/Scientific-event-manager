/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package janelas;

import ClassesAuxiliares.Arrumador;
import ClassesAuxiliares.BancoDeDados;
import java.util.ArrayList;
import javax.swing.JCheckBox;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import objetosBase.avaliacao;
import objetosBase.avaliador;
import objetosBase.triviais.coordenador;
import objetosBase.triviais.evento;
import objetosBase.triviais.unidade;

/**
 *
 * @author SistemaIP
 */
public class EscolheAcompanhantes extends javax.swing.JFrame {

    int indexCB = -1;
    ArrayList<avaliador> avaliadores;
    ArrayList<JCheckBox> avSelecionados;
    ArrayList<unidade> unidades = BancoDeDados.comando().getUnidades();
    private final ArrayList<coordenador> coord;

    private final int Xminimo = 30;
    private final int Yminimo = 110;

    private final int alturaCH = 25;
    private final int larguraCH = 480;

    private final int espacoVerticalEntreCampos = 5;
    // private final int EspacoHorizontalEntreCampos = 5;

    private final int limiteAlturaJanela = 510;
    private int pagina;
    private ChangeListener listener;
    private boolean iniciado;
    private ArrayList<avaliacao> permissoesAvaliacao;
    private int limiteSelecionados=1;
    private final evento event;
    private Inicial JP;
    /**
     * Creates new form EscolheAvaliadoresApresentacoes
     *
     * @param c ArrayList de registros de coordenador
     */
    public EscolheAcompanhantes(ArrayList<coordenador> c, Inicial J,evento e) {
        coord = c;
        System.out.println("Possui " + c.size() + " cargos de coordenador.");
        event = e;
        JP = J;
        iniciado = false;
        initComponents();
        this.setTitle("Selecionar acompanhantes para o evento central");
        this.setLocationRelativeTo(null);
        //this.setResizable(false);
        //this.setIconImage(Arrumador.comando().getIcone().getImage());
        this.setVisible(true);

        BTSalvar.setEnabled(false);
        BTSalvar.setText("Salvando automaticamente");
        
        
         listener = new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (!iniciado) {
                    System.out.println("Listener inativo... return");
                    return;
                }
                int c = contarSelecionados();
                String dx=""; String dx2="";
                if(c<limiteSelecionados){
                    dx = "APENAS ";
                    dx2 = " Marque até o limite de "+limiteSelecionados+".";
                }else
                    dx2 = " Ok!";
                    
                
                if(c==1)
                LAviso2.setText(dx+c +" servidor marcado como acompanhante no SEPIN."+dx2);
                else
                   LAviso2.setText(dx+ c +" servidores marcados como acompanhantes no SEPIN."+dx2);
                SeletoresEnabled(c < limiteSelecionados);
                salvar();

            }

            private int contarSelecionados() {
                int c = 0;
                for (int a = 0; a < avaliadores.size(); a++) {
                    if (avSelecionados.get(a).isSelected()) {
                        c++;
                    }
                }
                return c;
            }

            private void SeletoresEnabled(boolean b) {
                for (int a = 0; a < avaliadores.size(); a++) { // bloqueia quem poderia ser marcado. Sأ³ nأ£o quando estأ، no limite de CO, أ© uma comunicaأ§أ£o oral, e precisa liberar essa apresentaأ§أ£o

                    if (!avSelecionados.get(a).isSelected()) {
                        avSelecionados.get(a).setEnabled(b);
                    }

                }
            }

        };
        
        
            if (e.isAtivo()) {
                if ( e.getUnidadeSedeCod() == unidades.get(0).getId()) { // se a unidade em questão for a sede, é ilimitado
                    limiteSelecionados = 1000;
                } else {
                    limiteSelecionados = unidades.get(CBUnidade.getSelectedIndex()).getLimiteAcompanhantes();
                }
              //  break;
            }
        

        reloadApresentacoes();
        iniciado = false;
        pesquisarAvaliadores();

        iniciado = true;
         int c2 = contarSelecionados();
       String dx=""; String dx2="";
                if(c2<limiteSelecionados){
                    dx = "APENAS ";
                    dx2 = " Marque até o limite de "+limiteSelecionados+".";
                }else
                    dx2 = " Ok!";

        
               
                if(c2==1)
                LAviso2.setText(dx+c2 +" servidor marcado como acompanhante no SEPIN."+dx2);
                else
                LAviso2.setText(dx+c2 +" servidores marcados como acompanhantes no SEPIN."+dx2);    
                
                SeletoresEnabled(c2 < limiteSelecionados);
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

        CBUnidade = new javax.swing.JComboBox();
        BTSalvar = new javax.swing.JButton();
        Slider = new javax.swing.JSlider();
        LAviso = new javax.swing.JLabel();
        BTCadastrarNovo = new javax.swing.JButton();
        LAviso2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Selecionar acompanhantes para o evento central");

        CBUnidade.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        CBUnidade.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                CBUnidadeItemStateChanged(evt);
            }
        });

        BTSalvar.setText("Salvar");
        BTSalvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTSalvarActionPerformed(evt);
            }
        });

        Slider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                SliderStateChanged(evt);
            }
        });

        LAviso.setText("Para cadastro de acompanhantes/avaliadores, utilizar \"Cadastrar novo\" ");

        BTCadastrarNovo.setText("Cadastrar novo");
        BTCadastrarNovo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTCadastrarNovoActionPerformed(evt);
            }
        });

        LAviso2.setForeground(new java.awt.Color(255, 0, 0));
        LAviso2.setText(".");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(CBUnidade, 0, 501, Short.MAX_VALUE)
                    .addComponent(LAviso, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(LAviso2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(BTCadastrarNovo, javax.swing.GroupLayout.DEFAULT_SIZE, 258, Short.MAX_VALUE)
                    .addComponent(Slider, javax.swing.GroupLayout.DEFAULT_SIZE, 258, Short.MAX_VALUE)
                    .addComponent(BTSalvar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(174, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(CBUnidade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(BTSalvar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(Slider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(BTCadastrarNovo))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(LAviso)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(LAviso2)))
                .addContainerGap(493, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void CBUnidadeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_CBUnidadeItemStateChanged

        SalvaEAtualizaAvaliadores();
    }//GEN-LAST:event_CBUnidadeItemStateChanged

    private int contarSelecionados() {
                int c = 0;
                for (int a = 0; a < avaliadores.size(); a++) {
                    if (avSelecionados.get(a).isSelected()) {
                        c++;
                    }
                }
                return c;
            }

            private void SeletoresEnabled(boolean b) {
                for (int a = 0; a < avaliadores.size(); a++) { // bloqueia quem poderia ser marcado. Sأ³ nأ£o quando estأ، no limite de CO, أ© uma comunicaأ§أ£o oral, e precisa liberar essa apresentaأ§أ£o

                    if (!avSelecionados.get(a).isSelected()) {
                        avSelecionados.get(a).setEnabled(b);
                    }

                }
            }
    
    
    public void SalvaEAtualizaAvaliadores(){
    if(indexCB==CBUnidade.getSelectedIndex())
            return;
        
        if (!iniciado) {
            return;
        }

        indexCB = CBUnidade.getSelectedIndex();
        salvar();
        pesquisarAvaliadores();
    }
    
    
    private void BTSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTSalvarActionPerformed
        salvar();
    }//GEN-LAST:event_BTSalvarActionPerformed

    private void SliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_SliderStateChanged

        ativarAvaliadoresNaInterface(Slider.getValue());

    }//GEN-LAST:event_SliderStateChanged

    private void BTCadastrarNovoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTCadastrarNovoActionPerformed
        JP.criarVh2();
    }//GEN-LAST:event_BTCadastrarNovoActionPerformed

    public void pesquisarAvaliadores() {
        if (unidades.size() < 1) {
            return;
        }

        // recupera lista de possiveis avaliadores
        avaliadores = BancoDeDados.comando().recuperarAvaliadoresDaUnidade(unidades.get(CBUnidade.getSelectedIndex()).getId());
        
        String av = "";
        for (int x=0;x<avaliadores.size();x++)
            av = av + avaliadores.get(x).getNome()+ ", ";

        System.out.println(avaliadores.size() + " avaliadores na memoria: " + av);

        // muda a interface
        atualizarInterface();

        // verifica opأ§أµes ja marcadas
        recuperarPermissoesDeAvaliacao();
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
            java.util.logging.Logger.getLogger(EscolheAcompanhantes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(EscolheAcompanhantes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(EscolheAcompanhantes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(EscolheAcompanhantes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                // new EscolheAvaliadoresApresentacoes(coord).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BTCadastrarNovo;
    private javax.swing.JButton BTSalvar;
    private javax.swing.JComboBox CBUnidade;
    private javax.swing.JLabel LAviso;
    private javax.swing.JLabel LAviso2;
    private javax.swing.JSlider Slider;
    // End of variables declaration//GEN-END:variables

    private void atualizarInterface() {

        iniciado = false;

        //checkbox ( nome, titulo, unidade) tooltip: Telefone, tipopessoa
        if (avSelecionados != null) {
            for (int a = 0; a < avSelecionados.size(); a++) {
                avSelecionados.get(a).setBounds(1, 1, 1, 1);
                avSelecionados.get(a).setEnabled(false);
                avSelecionados.get(a).setVisible(false);
            }
        }

        avSelecionados = new ArrayList<>();
        boolean visible = true;
        pagina = 0;
        int atraso = 0;

        for (int a = 0; a < avaliadores.size(); a++) {

            //adiciona ao vetor local
            avSelecionados.add(new JCheckBox(avaliadores.get(a).getNome() + " (" + avaliadores.get(a).getTitulacaoNome() + ", " + avaliadores.get(a).getUnidadeNome() + ")"));
            // set bounds
            avSelecionados.get(a).setBounds(Xminimo,
                    Yminimo + ((a - atraso) * (alturaCH + espacoVerticalEntreCampos)),
                    larguraCH, alturaCH);
            // set tooltip
            avSelecionados.get(a).setToolTipText("Tel: " + avaliadores.get(a).getTelefone() + " (" + avaliadores.get(a).getTipoPessoaNome() + ")");
            // set visible e enabled
            avSelecionados.get(a).setVisible(visible);
            avSelecionados.get(a).setEnabled(visible);
            avSelecionados.get(a).addChangeListener(listener);
            // adiciona ao panel
            this.add(avSelecionados.get(avSelecionados.size() - 1));

            //se passar do limiteAltura, volta ao inicio, mas sempre deixar visible=false
            if ((Yminimo + (a - atraso) * (alturaCH + espacoVerticalEntreCampos)) > limiteAlturaJanela) {
                visible = false;
                pagina++;
                atraso = a + 1;
                System.out.println("Avaliador nآ°" + a + " foi o أ؛ltimo da pأ،gina " + (pagina - 1));
            }

        }
        Slider.setMinimum(0);
        Slider.setMaximum(pagina);
        iniciado = true;

    }

    private void reloadApresentacoes() {

        iniciado = false;
        CBUnidade.removeAllItems();

        for (coordenador c : coord) {
            if (c.getCentralizado() > 0) {
                System.out.println(unidades.size() + " unidades na memoria.");
                for (unidade ap : unidades) {
                    CBUnidade.addItem(ap.getNome());
                }
            } else {
                unidade u2 = new unidade("ERROR",-1);
                CBUnidade.addItem(c.getUnidadeNome());
                CBUnidade.setSelectedIndex(0);
                for(unidade u1: unidades)
                    if(u1.getId()==c.getUnidadeCodigo())
                    {
                    u2 = u1;                   
                    }
                unidades = new ArrayList<>();
                unidades.add(u2);
            }
        }

        iniciado = true;
    }

    private void recuperarPermissoesDeAvaliacao() {
        iniciado = false;

        
        // for (evento e : eventos) {
            if (event.isAtivo()) {
                if ( event.getUnidadeSedeCod() == unidades.get(CBUnidade.getSelectedIndex()).getId()) { // se a unidade em questão for a sede, é ilimitado
                    limiteSelecionados = 1000;
                } else {
                    limiteSelecionados = limiteSelecionados = unidades.get(CBUnidade.getSelectedIndex()).getLimiteAcompanhantes();
                }
              //  break;
            }
      //  }
        LAviso.setText("Para cadastro, utilizar 'Cadastrar novo'. MARCAR CAIXA PARA CONFIRMAR PRESENÇA (máximo de selecionados: "+limiteSelecionados+")");
        System.out.println("Limite de acompanhantes: "+ limiteSelecionados);
        
        
        
        for (int a = 0; a < avaliadores.size(); a++) {
            avSelecionados.get(a).setSelected(avaliadores.get(a).isAcompanhante());
        }
        iniciado = true;
        
    }

    public void salvar() {

        if (avaliadores == null) {
            return;
        }

        for (int a = 0; a < avaliadores.size(); a++) {

            if (avaliadores.get(a).isAcompanhante() != avSelecionados.get(a).isSelected()) {
                System.out.println("Atualizando status de acompanhante "+ (a+1));
                avaliadores.get(a).setAcompanhante(avSelecionados.get(a).isSelected());
                if(avaliadores.get(a).atualizarStatusAcompanhante()) // nأ£o atualiza senha
                    System.out.println("Atualizado!");
                else
                    System.out.println("Atualização falhou!");
            }

        }

    }

    private void ativarAvaliadoresNaInterface(int pagina) {
        if (!iniciado) {
            return;
        }

        int pagAtual = 0;
        int atraso = 0;

        for (int a = 0; a < avaliadores.size(); a++) {
            
            atraso++;
            
            if(atraso==16){
                atraso = atraso-16;
                pagAtual++;
            }
            
            avSelecionados.get(a).setVisible(pagina == pagAtual);
            avSelecionados.get(a).setEnabled(pagina == pagAtual);
              /*
            if ((Yminimo + (a - atraso) * (alturaCH + espacoVerticalEntreCampos)) > limiteAlturaJanela) {
                pagAtual++;
                atraso++;
            }
            //atraso = a + 1; */
            
            
           
            
        }

    }

   
}
