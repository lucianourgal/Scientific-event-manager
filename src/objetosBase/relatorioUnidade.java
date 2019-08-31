/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package objetosBase;

import ClassesAuxiliares.BancoDeDados;
import java.util.ArrayList;
import objetosBase.triviais.area;
import objetosBase.triviais.bolsa;
import objetosBase.triviais.evento;
import objetosBase.triviais.modalidade;

/**
 *
 * @author SistemaIP
 */
public class relatorioUnidade {

    ArrayList<bolsa> bolsas = new ArrayList<>();
    ArrayList<Integer> quantidadePorBolsa = new ArrayList<>();
    private int quantidadeTotalBolsas=0;

    ArrayList<modalidade> modalidades = new ArrayList<>();
    ArrayList<Integer> quantidadePorModalidade = new ArrayList<>();
    private int quantidadeTotalModalidades=0;
    
    ArrayList<area> areas = new ArrayList<>();
    ArrayList<Integer> quantidadePorArea = new ArrayList<>();
    private int quantidadeTotalAreas=0;

    private ArrayList<String> materiais = new ArrayList<>();
    private ArrayList<Integer> apresentacaoID = new ArrayList<>();
    private ArrayList<String> apresentacaoNome = new ArrayList<>();

    int codigoUnidade;
    String nomeUnidade;

    
    
    
    public relatorioUnidade(int codUnidade, int centralizado, String nomeU, evento e) {

        codigoUnidade = codUnidade;
        nomeUnidade = nomeU;

        //busca infs no BD
        BancoDeDados.comando().relatorioUnidade(codUnidade, centralizado, getThis(),e);

    }

    public int getNumeroApresentacoesQuePrecisamDeMateriais(){
    return getMateriais().size();
    }
    
    
    public void relatDoObjeto() {
        System.out.println("Relatório do objeto de relatório da unidade de n°" + codigoUnidade+ " - "+nomeUnidade);
        System.out.println("Tipo de bolsas: " + bolsas.size());
        System.out.println("Tipo de modalidades: " + modalidades.size());
        System.out.println("Apresentacoes que precisam de materias: " + getMateriais().size());
    }

    public final relatorioUnidade getThis() {
        return this;
    }
    
   

    // relatorio por bolsas
    public void adicionarQuantidadePorBolsa(int quantidade, bolsa b) {
        boolean achou = false;
        for (int a = 0; a < bolsas.size(); a++) {
            if (bolsas.get(a).getId() == b.getId()) {
                achou = true;
                quantidadePorBolsa.set(a, quantidade);
            }

        }
        if (!achou) {
            bolsas.add(b);
            quantidadePorBolsa.add(quantidade);
        }
        quantidadeTotalBolsas+=quantidade;
    }

    public bolsa getBolsa(int index){
        return bolsas.get(index);
    }
    public int getQuantidadeBolsa(int index){
        return quantidadePorBolsa.get(index);
    }
    
    public int getNumeroDeBolsas() {
        return bolsas.size();
    }

    public int getQuantidadeBolsaPorID(int id) {
       
        for(int a=0;a<bolsas.size();a++)
            if(bolsas.get(a).getId()==id)
                return quantidadePorBolsa.get(a);
            
        return 0;
    }
    
    // relatorio por modalidades
    public void adicionarQuantidadePorModalidade(int quantidade, modalidade m) {
        boolean achou = false;
        for (int a = 0; a < modalidades.size(); a++) {
            if (modalidades.get(a).getId() == m.getId()) {
                achou = true;
                quantidadePorModalidade.set(a, quantidade);
            }

        }
        if (!achou) {
            modalidades.add(m);
            quantidadePorModalidade.add(quantidade);
        }
        quantidadeTotalModalidades+=quantidade;
    }
    
    public modalidade getModalidade(int index){
        return modalidades.get(index);
    }
    public int getQuantidadePorModalidade(int index){
        return quantidadePorModalidade.get(index);
    }
    
    public int getQuantidadeModalidadePorID(int id) {
       
        for(int a=0;a<modalidades.size();a++)
            if(modalidades.get(a).getId()==id)
                return quantidadePorModalidade.get(a);
            
        return 0;
    }
    

    // relatorio por areas
        public void adicionarQuantidadePorArea(int quantidade, area m) {
        boolean achou = false;
        for (int a = 0; a < areas.size(); a++) {
            if (areas.get(a).getId() == m.getId()) {
                achou = true;
                quantidadePorArea.set(a, quantidade);
            }

        }
        if (!achou) {
            areas.add(m);
            quantidadePorArea.add(quantidade);
        }
        quantidadeTotalAreas+=quantidade;
    }
    
    public int getQuantidadeAreaPorID(int id) {
       
        for(int a=0;a<areas.size();a++)
            if(areas.get(a).getId()==id)
                return quantidadePorArea.get(a);
            
        return 0;
    }
        
    // relatório de materiais necessários por campus
    public void adicionarMateriaisNecessarios(String materiais1, int aprId, String aprNome) {
        // material, apresentacao ID, apresentacao nome, 
        getMateriais().add(materiais1);
        getApresentacaoID().add(aprId);
        getApresentacaoNome().add(aprNome);
    }
    
    public ArrayList<String> getLinhaMateriais(int index){
    ArrayList<String> m = new ArrayList<>();
    m.add(getMateriais().get(index));
    m.add(""+getApresentacaoID().get(index));
    m.add(getApresentacaoNome().get(index));
    return m;
    }

    

    public String getNomeUnidade() {
       return nomeUnidade;
    }

    /**
     * @return the quantidadeTotalBolsas
     */
    public int getQuantidadeTotalBolsas() {
        return quantidadeTotalBolsas;
    }

    /**
     * @return the quantidadeTotalModalidades
     */
    public int getQuantidadeTotalModalidades() {
        return quantidadeTotalModalidades;
    }

    /**
     * @return the quantidadeTotalAreas
     */
    public int getQuantidadeTotalAreas() {
        return quantidadeTotalAreas;
    }

    /**
     * @return the materiais
     */
    public ArrayList<String> getMateriais() {
        return materiais;
    }

    /**
     * @return the apresentacaoID
     */
    public ArrayList<Integer> getApresentacaoID() {
        return apresentacaoID;
    }

    /**
     * @return the apresentacaoNome
     */
    public ArrayList<String> getApresentacaoNome() {
        return apresentacaoNome;
    }

    public ArrayList<String> getArrayUnidade() {
        ArrayList<String> r = new ArrayList<>();
        
        for(String m: materiais)
            r.add(nomeUnidade);
            
        return r;
    }
    
    

}
