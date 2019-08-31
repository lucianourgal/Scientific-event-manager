/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package janelas;

import ClassesAuxiliares.Arrumador;
import ClassesAuxiliares.BancoDeDados;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import objetosBase.apresentacao;
import objetosBase.triviais.coordenador;
import objetosBase.triviais.evento;
import objetosBase.triviais.unidade;

/**
 *
 * @author hp00
 */
public class SelecionarApresentacoesParaCentralizado extends javax.swing.JFrame {

    private ArrayList<JCheckBox> CHApresentacao = new ArrayList<>();
    private ArrayList<JComboBox> CBApresentador = new ArrayList<>();
    private ArrayList<JSpinner> SPHospedagem = new ArrayList<>();
    private ArrayList<JSpinner> SPTransporte = new ArrayList<>();
    private ArrayList<JSpinner> SPAlimentacao = new ArrayList<>();
    private ArrayList<JButton> membrosAprCultural = new ArrayList<>();

    private ArrayList<apresentacao> apresentacoes=new ArrayList<>();
    private boolean iniciado;
    private int Xminimo = 30;
    private int Yminimo = 40;

    private int alturaCampos = 25;

    private int larguraCH = 480;
    private int larguraCB = 200;
    private int larguraSP = 40;

    private int espacoVertical = 5;
    private int espacoHorizontal = 12;

    private int limiteAlturaJanela = 500;

    private final ArrayList<coordenador> coord;
    ChangeListener listener ;
    ActionListener listMembrosApresentacaoCultural;
    private final Inicial JP;
    private boolean bloqueioComunicacaoOral = false;
    private boolean bloqueioIfTech = false;
    private boolean bloqueioRobotica = false;
    private ArrayList<unidade> unidades = BancoDeDados.comando().getUnidades();
  //  private Iterable<evento> eventos = BancoDeDados.comando().getEventos();
    private int transporteEAlimentacao;
    private final evento evento;
    
    /**
     * Creates new form SelecionarApresentacoesParaCentralizado
     * @param coor Array de registros de coordenador
     * @param JP Janela Inicial
     */
    public SelecionarApresentacoesParaCentralizado(ArrayList<coordenador> coor, final Inicial JP, evento e) {
        this.JP = JP;
        coord = coor;
        iniciado = false;
        initComponents();

        evento = e;
        
        CBUnidade.removeAllItems();
        for (coordenador c : coord) {
            if (c.getCentralizado() > 0) {
                System.out.println(unidades.size() + " unidades na memoria.");
                for (unidade ap : unidades) {
                    CBUnidade.addItem(ap.getNome());
                }
            } else {
                CBUnidade.addItem(c.getUnidadeNome());
            }
        }
        
        
        listener = new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (!iniciado) {
                    return;
                }
                int c = contarSelecionados();
                SeletoresEnabled(c < coord.get(0).getLimiteApresentacoesSelecionadas());
                testarLimiteComunicacaoOral();
                testarLimiteIfTech();
                testarLimiteRobotica();
                //testarLimiteCultural();
            }

            private int contarSelecionados() { // dif de 5(robotica), 3 (iftech) e 2 (comunicacao oral)
                int c = 0;
                for (int a = 0; a < apresentacoes.size(); a++) {
                    if (CHApresentacao.get(a).isSelected() && 
                            apresentacoes.get(a).getModalidadeCod()!=2 && 
                            apresentacoes.get(a).getModalidadeCod()!=3 && 
                            apresentacoes.get(a).getModalidadeCod()!=5) {
                        c++;
                    }
                }
                return c;
            }

            private void SeletoresEnabled(boolean b) {
                for (int a = 0; a < apresentacoes.size(); a++) { // bloqueia quem poderia ser marcado. Só não quando está no limite de CO, é uma comunicação oral, e precisa liberar essa apresentação
                    if (!CHApresentacao.get(a).isSelected() && 
                            !(bloqueioComunicacaoOral && apresentacoes.get(a).getModalidadeCod()==2 && b==true) && 
                            !(bloqueioIfTech && apresentacoes.get(a).getModalidadeCod()==3 && b==true)&& 
                            !(bloqueioRobotica && apresentacoes.get(a).getModalidadeCod()==5 && b==true)
                            
                            ) {
                        
                        
                        CHApresentacao.get(a).setEnabled(b);
                        CBApresentador.get(a).setEnabled(b);
                        SPHospedagem.get(a).setEnabled(b);
                        SPAlimentacao.get(a).setEnabled(b);
                        SPTransporte.get(a).setEnabled(b);
                        membrosAprCultural.get(a).setEnabled(b);
                    }
                }
            }

            //COMUNICACAO ORAL 2
            private void testarLimiteComunicacaoOral() {
                  
               //apresentacoes ( comunicação oral = 2)
                int cont=0;
                
                // caso precise bloquear as comunicacoes orais
                for(int a=0;a<apresentacoes.size();a++){
                    if(CHApresentacao.get(a).isSelected() && apresentacoes.get(a).getModalidadeCod()==2){
                        cont++;
                        if(cont>=coord.get(0).getLimitecomunicacaooralselecionadas()){
                            bloquearComunicacoesOraisNaoSelecionadas(false);
                            bloqueioComunicacaoOral=true;
                            a = apresentacoes.size();
                        }
                        
                    }
                }
                
                cont=0;
                // caso tenha liberado uma comunicação oral. 
                for(int a=0;a<apresentacoes.size();a++){
                    if(CHApresentacao.get(a).isSelected() && apresentacoes.get(a).getModalidadeCod()==2){
                        cont++;
                    }
                    if(cont<coord.get(0).getLimitecomunicacaooralselecionadas() && bloqueioComunicacaoOral){
                            bloquearComunicacoesOraisNaoSelecionadas(true);
                            bloqueioComunicacaoOral=false;
                        }
                }
                
                
            }
            
            //IF TECH 3
            private void testarLimiteIfTech() {
                  
               //apresentacoes (  if tech = 3)
                int cont=0;
                
                // caso precise bloquear as if tech
                for(int a=0;a<apresentacoes.size();a++){
                    if(CHApresentacao.get(a).isSelected() && apresentacoes.get(a).getModalidadeCod()==3){
                        cont++;
                        if(cont>=coord.get(0).getLimiteiftech()){
                            bloquearIfTechNaoSelecionadas(false);
                            bloqueioIfTech=true;
                            a = apresentacoes.size();
                        }
                        
                    }
                }
                
                cont=0;
                // caso tenha liberado uma if tech. 
                for(int a=0;a<apresentacoes.size();a++){
                    if(CHApresentacao.get(a).isSelected() && apresentacoes.get(a).getModalidadeCod()==3){
                        cont++;
                    }
                    if(cont<coord.get(0).getLimiteiftech() && bloqueioIfTech){
                            bloquearIfTechNaoSelecionadas(true);
                            bloqueioIfTech=false;
                        }
                }
                
                
            }
            
             //ROBOTICA 5
            private void testarLimiteRobotica() {
                  
               //apresentacoes (  robotica = 5)
                int cont=0;
                
                // caso precise bloquear as roboticas
                for(int a=0;a<apresentacoes.size();a++){
                    if(CHApresentacao.get(a).isSelected() && apresentacoes.get(a).getModalidadeCod()==5){
                        cont++;
                        if(cont>=coord.get(0).getLimiterobotica()){
                            bloquearRoboticasNaoSelecionadas(false);
                            bloqueioRobotica=true;
                            a = apresentacoes.size();
                        }
                        
                    }
                }
                
                cont=0;
                // caso tenha liberado uma robotica. 
                for(int a=0;a<apresentacoes.size();a++){
                    if(CHApresentacao.get(a).isSelected() && apresentacoes.get(a).getModalidadeCod()==5){
                        cont++;
                    }
                    if(cont<coord.get(0).getLimiterobotica() && bloqueioRobotica){
                            bloquearRoboticasNaoSelecionadas(true);
                            bloqueioRobotica=false;
                        }
                }
                
                
            }

            private void bloquearComunicacoesOraisNaoSelecionadas(boolean b) {
                
                for(int a=0;a<apresentacoes.size();a++)
                    if(!CHApresentacao.get(a).isSelected() && apresentacoes.get(a).getModalidadeCod()==2){
                        CHApresentacao.get(a).setEnabled(b);
                        CBApresentador.get(a).setEnabled(b);
                        SPHospedagem.get(a).setEnabled(b);
                        SPAlimentacao.get(a).setEnabled(b);
                        SPTransporte.get(a).setEnabled(b);
                        membrosAprCultural.get(a).setEnabled(b);
   
                    }
                
            }
            
            private void bloquearIfTechNaoSelecionadas(boolean b) {
                
                for(int a=0;a<apresentacoes.size();a++)
                    if(!CHApresentacao.get(a).isSelected() && apresentacoes.get(a).getModalidadeCod()==3){
                        CHApresentacao.get(a).setEnabled(b);
                        CBApresentador.get(a).setEnabled(b);
                        SPHospedagem.get(a).setEnabled(b);
                        SPAlimentacao.get(a).setEnabled(b);
                        SPTransporte.get(a).setEnabled(b);
                        membrosAprCultural.get(a).setEnabled(b);
   
                    }
                
            }
            private void bloquearRoboticasNaoSelecionadas(boolean b) {
                
                for(int a=0;a<apresentacoes.size();a++)
                    if(!CHApresentacao.get(a).isSelected() && apresentacoes.get(a).getModalidadeCod()==5){
                        CHApresentacao.get(a).setEnabled(b);
                        CBApresentador.get(a).setEnabled(b);
                        SPHospedagem.get(a).setEnabled(b);
                        SPAlimentacao.get(a).setEnabled(b);
                        SPTransporte.get(a).setEnabled(b);
                        membrosAprCultural.get(a).setEnabled(b);
   
                    }
                
            }
            
            
            
            
        };

        listMembrosApresentacaoCultural= new ActionListener(){
        

            @Override
            public void actionPerformed(ActionEvent e) {
                JP.criarJanelaSelecaoApresentadoresApresCultural(coord, Arrumador.comando().retornaApenasCulturaisSelecionadas(apresentacoes) );
            }
        
        };
        
        
        this.setLocationRelativeTo(null);
        //this.setResizable(false);
        //this.setIconImage(Arrumador.comando().getIcone().getImage());
        this.setVisible(true);
        this.setTitle("Selecionar apresentações para etapa centralizada");
        atualizarInterface();
        
    }

    public void limparInterface(){
    
        if(apresentacoes.isEmpty())
            return;
    //CHApresentacao, CBApresentador, SPHospedagem, SPTransporte, SPAlimentacao
     for (int a = 0; a < apresentacoes.size(); a++){
         CHApresentacao.get(a).setVisible(false);
         CBApresentador.get(a).setVisible(false);
         SPHospedagem.get(a).setVisible(false);
         SPTransporte.get(a).setVisible(false);
         SPAlimentacao.get(a).setVisible(false);
        
     }
      CHApresentacao = new ArrayList<>();
         CBApresentador = new ArrayList<>(); 
         SPHospedagem =  new ArrayList<>();
         SPTransporte =  new ArrayList<>();
         SPAlimentacao =  new ArrayList<>();
        
    }
    
    
    public void atualizarInterface(){
        iniciado = false;
        System.out.println("Coordenação da unidade " + CBUnidade.getSelectedItem().toString() + ". " + unidades.get(CBUnidade.getSelectedIndex()).getLimiteApresentacoesSelecionadas());
        
       //  for (evento e : eventos) {
            if (evento.isAtivo()) {
                if ( evento.getUnidadeSedeCod() == unidades.get(CBUnidade.getSelectedIndex()).getId()) { // se a unidade em questão for a sede, é ilimitado
                    transporteEAlimentacao = 0;
                } else {
                    transporteEAlimentacao = 1;
                }
            //    break;
            }
     //   }
        
        

        // seleciona todas as apresentações do campus
        apresentacoes = new ArrayList<>();
        apresentacoes = BancoDeDados.comando().recuperarApresentacoesParaSelecao(unidades.get(CBUnidade.getSelectedIndex()).getId(),evento);

        
        //limparInterface();
        
        int pagina = 0;
        int atraso = 0;
        boolean visible = true;

        // gerar interface
        System.out.println("Gerando interface com " + apresentacoes.size() + " apresentações para "+unidades.get(CBUnidade.getSelectedIndex()).getNome());
        for (int a = 0; a < apresentacoes.size(); a++) {
            
            if(apresentacoes.get(a).getModalidadeCod()!=4 && (apresentacoes.get(a).getHospedagens()>1)){
                apresentacoes.get(a).setHospedagens(1);
            }
            if(apresentacoes.get(a).getModalidadeCod()!=4 && (apresentacoes.get(a).getAlimentacoes()>1)){
                apresentacoes.get(a).setAlimentacoes(1);
            }
            if(apresentacoes.get(a).getModalidadeCod()!=4 && (apresentacoes.get(a).getTransportes()>1)){
                apresentacoes.get(a).setTransportes(1);
            }
            
            //CHApresentacao, CBApresentador, SPHospedagem, SPTransporte, SPAlimentacao
            CHApresentacao.add(new JCheckBox(apresentacoes.get(a).getNometitulo() + " ( " + apresentacoes.get(a).getAreaNome() + " )"));
            CHApresentacao.get(a).setBounds(Xminimo, Yminimo + (a - atraso) * (alturaCampos + espacoVertical), larguraCH, alturaCampos);
            CHApresentacao.get(a).addChangeListener(listener);
            CHApresentacao.get(a).setToolTipText(apresentacoes.get(a).getNometitulo() + " ( " + apresentacoes.get(a).getAreaNome() + " )");
            CHApresentacao.get(a).setSelected(apresentacoes.get(a).getSelecionado() == 1);
            CHApresentacao.get(a).setVisible(visible);
            //CHApresentacao.get(a).setEnabled(visible);
            this.add(CHApresentacao.get(a));

            CBApresentador.add(new JComboBox());
            CBApresentador.get(a).setBounds(Xminimo + larguraCH + espacoHorizontal, Yminimo + (a - atraso) * (alturaCampos + espacoVertical), larguraCB, alturaCampos);
            CBApresentador.get(a).addItem(apresentacoes.get(a).getApresentadorEfetivoNome());
            CBApresentador.get(a).setVisible(visible && apresentacoes.get(a).getModalidadeCod()!=4);
            //CBApresentador.get(a).setEnabled(visible);
            for (int j = 0; j < apresentacoes.get(a).getAutores().size(); j++) {
                CBApresentador.get(a).addItem(apresentacoes.get(a).getAutores().get(j).getNome());
            }
            this.add(CBApresentador.get(a));
            CBApresentador.get(a).setSelectedIndex(apresentacoes.get(a).getIndiceComboBoxApresentadorEfetivo());

            int max = apresentacoes.get(a).getNumeroMaximoHospedagens();
            SpinnerNumberModel model1 = new SpinnerNumberModel(apresentacoes.get(a).getHospedagens(), 0, max, 1); //inicial, min, max, step
            SPHospedagem.add(new JSpinner(model1));
            SPHospedagem.get(a).setBounds(Xminimo + larguraCH + espacoHorizontal * 2 + larguraCB, Yminimo + (a - atraso) * (alturaCampos + espacoVertical), larguraSP, alturaCampos);
            SPHospedagem.get(a).setVisible(visible);
            SPHospedagem.get(a).setToolTipText("Numero de requisições de hospedagens para esse trabalho");
            //SPHospedagem.get(a).setEnabled(visible);
            this.add(SPHospedagem.get(a));

            SpinnerNumberModel model2 = new SpinnerNumberModel(apresentacoes.get(a).getTransportes()*transporteEAlimentacao, 0, max*transporteEAlimentacao, 1*transporteEAlimentacao);
            SPTransporte.add(new JSpinner(model2));
            SPTransporte.get(a).setBounds(Xminimo + larguraCH + espacoHorizontal * 3 + larguraCB + larguraSP, Yminimo + (a - atraso) * (alturaCampos + espacoVertical), larguraSP, alturaCampos);
            SPTransporte.get(a).setVisible(visible);
            SPTransporte.get(a).setToolTipText("Numero de requisições de transporte para esse trabalho");
            //SPTransporte.get(a).setEnabled(visible);
            this.add(SPTransporte.get(a));

            SpinnerNumberModel model3 = new SpinnerNumberModel(apresentacoes.get(a).getAlimentacoes()*transporteEAlimentacao, 0, max*transporteEAlimentacao, 1*transporteEAlimentacao);
            SPAlimentacao.add(new JSpinner(model3));
            SPAlimentacao.get(a).setBounds(Xminimo + larguraCH + espacoHorizontal * 4 + larguraCB + larguraSP * 2, Yminimo + (a - atraso) * (alturaCampos + espacoVertical), larguraSP, alturaCampos);
            SPAlimentacao.get(a).setVisible(visible);
            SPAlimentacao.get(a).setToolTipText("Numero de requisições de alimentação para esse trabalho");
            //SPAlimentacao.get(a).setEnabled(visible);
            this.add(SPAlimentacao.get(a));

            
            membrosAprCultural.add(new JButton("Escolher participantes"));
            membrosAprCultural.get(a).setBounds(Xminimo + larguraCH + espacoHorizontal * 5 + larguraCB + larguraSP * 3, Yminimo + (a - atraso) * (alturaCampos + espacoVertical), larguraSP*3, alturaCampos);
            membrosAprCultural.get(a).setVisible(visible && apresentacoes.get(a).getModalidadeCod()==4);
            membrosAprCultural.get(a).addActionListener(listMembrosApresentacaoCultural);
            this.add(membrosAprCultural.get(a));
            
            //se passar do limiteAltura, volta ao inicio, mas sempre deixar visible=false
            if ((Yminimo + (a - atraso) * (alturaCampos + espacoVertical)) > limiteAlturaJanela) {
                visible = false;
                pagina++;
                atraso = a + 1;
                System.out.println("Apresentação n°" + a + " foi o última da página " + (pagina - 1));
            }

        }// fim do laço
        Slider.setMinimum(0);
        Slider.setMaximum(pagina);
        iniciado = true;
        BTSalvar.setEnabled(apresentacoes.size()>0);
    }
    
    
    public void atualizarObjetos() {

        for (int a = 0; a < apresentacoes.size(); a++) {
            apresentacoes.get(a).setAlimentacoes((int) SPAlimentacao.get(a).getValue());
            apresentacoes.get(a).setHospedagens((int) SPHospedagem.get(a).getValue());
            apresentacoes.get(a).setTransportes((int) SPTransporte.get(a).getValue());
            apresentacoes.get(a).estaSelecionada(CHApresentacao.get(a).isSelected());
            apresentacoes.get(a).setIndiceDoComboBoxDeApresentador(CBApresentador.get(a).getSelectedIndex());
            apresentacoes.get(a).setNomeDoApresentadorDoComboBox(CBApresentador.get(a).getSelectedItem().toString());
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
        BTSalvar = new javax.swing.JButton();
        Slider = new javax.swing.JSlider();
        CBUnidade = new javax.swing.JComboBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setText("   Apresentador   /      N de  Hospedagens/Transporte/Alimentação");

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

        CBUnidade.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        CBUnidade.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                CBUnidadeItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addComponent(BTSalvar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(Slider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 449, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(CBUnidade, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(CBUnidade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Slider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(BTSalvar)))
                .addContainerGap(473, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BTSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTSalvarActionPerformed
        /* for(int a =0;a<apresentacoes.size();a++){
         apresentacoes.get(a).atualizarSelecao(CHApresentacao.get(a).isSelected(),
         CBApresentador.get(a).getSelectedIndex(), (int)SPHospedagem.get(a).getValue(),
         (int)SPTransporte.get(a).getValue(), (int)SPAlimentacao.get(a).getValue());   // selecionado, indiceApresentador, hospedagem, transporte, alimentação
         }*/

        atualizarObjetos();
        if(atualizarNoBD()){
            BTSalvar.setText("Salvo");
           // BTSalvar.setEnabled(false);
        }else{
            BTSalvar.setText("Falha!");            
        }
    }//GEN-LAST:event_BTSalvarActionPerformed

    private void SliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_SliderStateChanged
        ativarAvaliadoresNaInterface(Slider.getValue());
    }//GEN-LAST:event_SliderStateChanged

    private void CBUnidadeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_CBUnidadeItemStateChanged

        if (!iniciado) {
            return;
        }
        
        atualizarNoBD();
        
        BTSalvar.setText("Salvar");
       // BTSalvar.setEnabled(true);
        
        
        limparInterface();
        atualizarInterface();
        
    }//GEN-LAST:event_CBUnidadeItemStateChanged

    
    
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
            java.util.logging.Logger.getLogger(SelecionarApresentacoesParaCentralizado.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SelecionarApresentacoesParaCentralizado.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SelecionarApresentacoesParaCentralizado.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SelecionarApresentacoesParaCentralizado.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                // new SelecionarApresentacoesParaCentralizado().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BTSalvar;
    private javax.swing.JComboBox CBUnidade;
    private javax.swing.JSlider Slider;
    private javax.swing.JLabel jLabel1;
    // End of variables declaration//GEN-END:variables

    private boolean atualizarNoBD() {
        for (apresentacao a : apresentacoes) {
            if (!a.atualizarApresentacaoModoDeSelecao()) {
                System.out.println("Houve uma falha ao atualizar a apresentacao " + a.getId() + "!");
                return false;
            }/*else{ // já que atualizou sem problemas, atualiza também a situação dos autores
                a.atualizarAtivacaoAutores(); já internalizado na classe de apresentação
            }*/
        }
        return true;
    }

    private void ativarAvaliadoresNaInterface(int pagina) {
        //CHApresentacao, CBApresentador, SPHospedagem, SPTransporte, SPAlimentacao
        if (!iniciado) {
            return;
        }

        int pagAtual = 0;
        int atraso = 0;

        for (int a = 0; a < apresentacoes.size(); a++) {
            CHApresentacao.get(a).setVisible(pagina == pagAtual);
            //CHApresentacao.get(a).setEnabled(pagina == pagAtual);
            
            CBApresentador.get(a).setVisible(pagina == pagAtual && apresentacoes.get(a).getModalidadeCod()!=4);
            //CBApresentador.get(a).setEnabled(pagina == pagAtual);
            
            SPHospedagem.get(a).setVisible(pagina == pagAtual);
            //SPHospedagem.get(a).setEnabled(pagina == pagAtual);
            
            SPTransporte.get(a).setVisible(pagina == pagAtual);
            //SPTransporte.get(a).setEnabled(pagina == pagAtual);
            
            SPAlimentacao.get(a).setVisible(pagina == pagAtual);
            //SPAlimentacao.get(a).setEnabled(pagina == pagAtual);
            
            membrosAprCultural.get(a).setVisible(pagina == pagAtual && apresentacoes.get(a).getModalidadeCod()==4);

            if ((Yminimo + (a - atraso) * (alturaCampos + espacoVertical)) > limiteAlturaJanela) {
                pagAtual++;
                atraso = a+1;
            }
            
        }

    }

}
