/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package objetosBase.triviais;

import ClassesAuxiliares.BancoDeDados;

/**
 *
 * @author SistemaIP
 */
public class unidade {
    
    private String nome;
    private String endereco;
    private String telefone;
    private int id;
    private String cpfCoordenador;
    private int limiteApresentacoesSelecionadas;
    private int limiteRobotica;
    private int limiteAcompanhantes;
    private int limiteIftech;
    private int limiteComunicacaoOralSelecionadas;
    private int limiteculturais;
    private String diretor;
    private boolean mudou = false;
    

    public int getTotalTrabalhos(){
    
    return (limiteApresentacoesSelecionadas+limiteRobotica+limiteIftech+limiteculturais+limiteComunicacaoOralSelecionadas);
    }
    
    
    public unidade(String nom, int ide){
    id = ide;
    nome = nom;
    }
    
    
    /**
     * @return the nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * @param nome the nome to set
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * @return the endereco
     */
    public String getEndereco() {
        return endereco;
    }

    /**
     * @param endereco the endereco to set
     */
    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    /**
     * @return the telefone
     */
    public String getTelefone() {
        return telefone;
    }

    /**
     * @param telefone the telefone to set
     */
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the limiteApresentacoesSelecionadas
     */
    public int getLimiteApresentacoesSelecionadas() {
        return limiteApresentacoesSelecionadas;
    }

    /**
     * @param limiteApresentacoesSelecionadas the limiteApresentacoesSelecionadas to set
     */
    public void setLimiteApresentacoesSelecionadas(int limiteApresentacoesSelecionadas) {
        this.limiteApresentacoesSelecionadas = limiteApresentacoesSelecionadas;
    }

    /**
     * @return the limiteRobotica
     */
    public int getLimiteRobotica() {
        return limiteRobotica;
    }

    /**
     * @param limiteRobotica the limiteRobotica to set
     */
    public void setLimiteRobotica(int limiteRobotica) {
        this.limiteRobotica = limiteRobotica;
    }

    /**
     * @return the limiteIftech
     */
    public int getLimiteIftech() {
        return limiteIftech;
    }

    /**
     * @param limiteIftech the limiteIftech to set
     */
    public void setLimiteIftech(int limiteIftech) {
        this.limiteIftech = limiteIftech;
    }

    /**
     * @return the limiteComunicacaoOralSelecionadas
     */
    public int getLimiteComunicacaoOralSelecionadas() {
        return limiteComunicacaoOralSelecionadas;
    }

    /**
     * @param limiteComunicacaoOralSelecionadas the limiteComunicacaoOralSelecionadas to set
     */
    public void setLimiteComunicacaoOralSelecionadas(int limiteComunicacaoOralSelecionadas) {
        this.limiteComunicacaoOralSelecionadas = limiteComunicacaoOralSelecionadas;
    }

    /**
     * @return the diretor
     */
    public String getDiretor() {
        return diretor;
    }

    /**
     * @param diretor the diretor to set
     */
    public void setDiretor(String diretor) {
        this.diretor = diretor;
    }

    /**
     * @return the limiteAcompanhantes
     */
    public int getLimiteAcompanhantes() {
        return limiteAcompanhantes;
    }

    /**
     * @param limiteAcompanhantes the limiteAcompanhantes to set
     */
    public void setLimiteAcompanhantes(int limiteAcompanhantes) {
        this.limiteAcompanhantes = limiteAcompanhantes;
    }

    /**
     * @return the limiteculturais
     */
    public int getLimiteculturais() {
        return limiteculturais;
    }

    /**
     * @param limiteculturais the limiteculturais to set
     */
    public void setLimiteculturais(int limiteculturais) {
        this.limiteculturais = limiteculturais;
    }

    public void reAlimentarObjeto(Integer acomp, Integer posteres, Integer comorais, Integer iftechs, Integer roboticas, Integer culturais) {
        //acompanhantes,posteres,comorais,iftechs,roboticas,culturais
    
    if(limiteApresentacoesSelecionadas!=posteres){
    mudou = true;
    limiteApresentacoesSelecionadas = posteres;
    }   
    
    if(limiteRobotica!=roboticas){
    mudou = true;
    limiteRobotica = roboticas;
    }
    
    if(limiteAcompanhantes!=acomp){
    mudou = true;
    limiteAcompanhantes = acomp;
    }
     
    if(limiteIftech!=iftechs){
    mudou = true;
    limiteIftech = iftechs;
    }
    
    if(limiteComunicacaoOralSelecionadas!=comorais){
    mudou = true;
    limiteComunicacaoOralSelecionadas = comorais;
    }
    
    if(limiteculturais!=culturais){
    mudou = true;
    limiteculturais = culturais;
    }
    
        
    }
    /*private int limiteApresentacoesSelecionadas;
    private int limiteRobotica;
    private int limiteAcompanhantes;
    private int limiteIftech;
    private int limiteComunicacaoOralSelecionadas;
    private int limiteculturais;*/

    public void adequarNoBd() {
       
        if(!mudou)
            return;
        
        BancoDeDados.comando().atualizarUnidade(this);
        
    }

    /**
     * @return the cpfCoordenador
     */
    public String getCpfCoordenador() {
        return cpfCoordenador;
    }

    /**
     * @param cpfCoordenador the cpfCoordenador to set
     */
    public void setCpfCoordenador(String cpfCoordenador) {
        this.cpfCoordenador = cpfCoordenador;
    }
    
    
    public void adequarCoordenador(String cpfNovo){
    //avaliador, unidade, centralizado
        if(id==22)//ID REITORIA. Não atualiza coordenador
            return;
        
    //caso 1: Sem coordenador até agora (original - , novo CPF )    
     //INSERT
       if(cpfCoordenador.equals("-") && !cpfNovo.equals("-"))
       BancoDeDados.comando().insert("INSERT INTO coordenador(avaliador,unidade,centralizado) VALUES('"+cpfNovo+"',"+id+",0);");
        
    //caso 2: Mudança de coordenador (original CPF1, novo CPF2 ) - Não sendo reitoria
     //UPDATE
        if(!cpfCoordenador.equals("-") && !cpfNovo.equals("-"))
       BancoDeDados.comando().update("UPDATE coordenador SET avaliador = '"+cpfNovo+"' WHERE unidade = "+id+" AND avaliador = '"+cpfCoordenador+"';");
        
    //caso 3: Retirada de coordenador (original CPF, novo - )
      //DELETE
        if(!cpfCoordenador.equals("-") && cpfNovo.equals("-")){
        BancoDeDados.comando().update("DELETE FROM coordenador WHERE avaliador = '"+cpfCoordenador+"' AND unidade = "+id+";");
        }
        
    cpfCoordenador = cpfNovo;
    }
    
    
    
}
