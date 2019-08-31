/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package objetosBase;

import ClassesAuxiliares.BancoDeDados;

/**
 *
 * @author Luciano
 */
public class avaliacao {

private String avaliador;
private int apresentacao;
private String apresentacaoNome;
private String apresentadorNome;
private String categoriaNome;
private String unidadeApresentadorNome;
private String avaliadorNome;
private String unidadeAvaliadorNome;
private String avaliadorEmail;

private int oral = 0;

private int notaInicial;
private int notaFinal;

private boolean salvoNoBD;
private boolean ausente;
private int centralizado;
private boolean excluir=false;
    private boolean excluirInicial;
    private boolean precisaEstarNoBD;


public boolean adequarNoBD(String novoAvaliador) {
       
    if((!isSalvoNoBD()) && !precisaEstarNoBD){  // ainda nao salvo no BD, e nao está marcado para ser salvo
    
    System.out.println("Apresentacao "+ getApresentacao()+" não cadastrada no BD. Não deve ser cadastrada. Ok.");
    return true;
    
    }else if (isSalvoNoBD() && !precisaEstarNoBD){  //está salvo no BD, mas precisa ser excluido
        System.out.println("isSalvoNoBD() && !precisaEstarNoBD");
        //return BancoDeDados.comando().excluirAvaliacao(this);
        // Com novas regras de negocio, passará a atualizar
        return BancoDeDados.comando().atualizarAvaliacao(this,novoAvaliador);
        
    } else if ((!isSalvoNoBD()) && precisaEstarNoBD){  //não está salvo no BD, e precisa ser incluido
    
    return BancoDeDados.comando().incluirAvaliacao(this);
    //não cairá mais aqui. Sempre já vai estar inclusa antes
    
    } else if (isSalvoNoBD() && precisaEstarNoBD){ //está salvo no BD, e não deve ser excluido. Talvez atualizado.
        if( getNotaInicial()==getNotaFinal()  && isExcluir()==isExcluirInicial()){  // não houve alteração na nota/ativo, não há necessidade de atualizar
        System.out.println("Está salvo no BD, e não deve ser excluido. Sem alterações na nota ou status.");
        return true;
        }else{  // houve alteração na nota, precisa atualizar
        
        return BancoDeDados.comando().atualizarAvaliacao(this,novoAvaliador);
            
        }
    }
    
    //Se cair aqui, não entrou em nenhuma condição, ou seja, tem algo errado
    return false;
    
    }

    public void testarSeEhOral(){
    if(apresentacao<0)
        oral = 1;
    else
        oral = 0;
    }



    /**
     * @return the avaliador
     */
    public String getAvaliador() {
        return avaliador;
    }

    /**
     * @param avaliador the avaliador to set
     */
    public void setAvaliador(String avaliador) {
        this.avaliador = avaliador;
    }

    /**
     * @return the apresentacao
     */
    public int getApresentacao() {
        if(apresentacao>-1)
        return apresentacao;
        else
            return -apresentacao;
    }

    /**
     * @param apresentacao the apresentacao to set
     */
    public void setApresentacao(int apresentacao) {
        this.apresentacao = apresentacao;
    }

    /**
     * @return the notaInicial
     */
    public int getNotaInicial() {
        return notaInicial;
    }

    /**
     * @param notaInicial the notaInicial to set
     */
    public void setNotaInicial(int notaInicial) {
        this.notaInicial = notaInicial;
    }

    /**
     * @return the notaFinal
     */
    public int getNotaFinal() {
        return notaFinal;
    }

    /**
     * @param notaFinal the notaFinal to set
     */
    public void setNotaFinal(int notaFinal) {
        this.notaFinal = notaFinal;
    }

    /**
     * @return the salvoNoBD
     */
    public boolean isSalvoNoBD() {
        return salvoNoBD;
    }

    /**
     * @param salvoNoBD the salvoNoBD to set
     */
    public void setSalvoNoBD(boolean salvoNoBD) {
        this.salvoNoBD = salvoNoBD;
    }

    /**
     * @return the centralizado
     */
    public int getCentralizado() {
        return centralizado;
    }

    /**
     * @param centralizado the centralizado to set
     */
    public void setCentralizado(int centralizado) {
        this.centralizado = centralizado;
    }

    /**
     * @return the excluir
     */
    public boolean isExcluir() {
        return excluir;
    }

    /**
     * @param excluir the excluir to set
     */
    public void setExcluir(boolean excluir) {
        this.excluir = excluir;
    }

    /**
     * @return the ausente
     */
    public boolean isAusente() {
        return ausente;
    }
    public int getAusente(){
    if( isAusente()) return 1;
    
    return 0;
    }
    
    public String getAusenteTexto(){
    if(isAusente())
        return "Sim";
    else
        return "Não";
    
    }
    
    
    
    public int getAtiva(){
    if(!isExcluir()) return 1;
    return 0;
       }
    
    public String getAtivaTexto(){
    if(!isExcluir()) return "Sim";
    return "Não";
       }
    
    
    public void setAtiva(boolean a){
        setExcluir(!a);
    }
    

    /**
     * @param ausente the ausente to set
     */
    public void setAusente(boolean ausente) {
        this.ausente = ausente;
    }

    /**
     * @param excluirInicial the excluirInicial to set
     */
    public void setExcluirInicial(boolean excluirInicial) {
        this.excluirInicial = excluirInicial;
        setAtiva(!excluirInicial);
    }

    /**
     * @return the excluirInicial
     */
    public boolean isExcluirInicial() {
        return excluirInicial;
    }

    /**
     * @return the precisaEstarNoBD
     */
    public boolean isPrecisaEstarNoBD() {
        return precisaEstarNoBD;
    }

    /**
     * @param precisaEstarNoBD the precisaEstarNoBD to set
     */
    public void setPrecisaEstarNoBD(boolean precisaEstarNoBD) {
        this.precisaEstarNoBD = precisaEstarNoBD;
    }

    /**
     * @return the avaliadorNome
     */
    public String getAvaliadorNome() {
        return avaliadorNome;
    }

    /**
     * @param avaliadorNome the avaliadorNome to set
     */
    public void setAvaliadorNome(String avaliadorNome) {
        this.avaliadorNome = avaliadorNome;
    }

    /**
     * @return the unidadeAvaliadorNome
     */
    public String getUnidadeAvaliadorNome() {
        return unidadeAvaliadorNome;
    }

    /**
     * @param unidadeAvaliadorNome the unidadeAvaliadorNome to set
     */
    public void setUnidadeAvaliadorNome(String unidadeAvaliadorNome) {
        this.unidadeAvaliadorNome = unidadeAvaliadorNome;
    }

    /**
     * @return the apresentacaoNome
     */
    public String getApresentacaoNome() {
        return apresentacaoNome;
    }

    /**
     * @param apresentacaoNome the apresentacaoNome to set
     */
    public void setApresentacaoNome(String apresentacaoNome) {
        this.apresentacaoNome = apresentacaoNome;
    }

    /**
     * @return the avaliadorEmail
     */
    public String getAvaliadorEmail() {
        return avaliadorEmail;
    }

    /**
     * @param avaliadorEmail the avaliadorEmail to set
     */
    public void setAvaliadorEmail(String avaliadorEmail) {
        this.avaliadorEmail = avaliadorEmail;
    }

    /**
     * @return the apresentadorNome
     */
    public String getApresentadorNome() {
        return apresentadorNome;
    }

    /**
     * @param apresentadorNome the apresentadorNome to set
     */
    public void setApresentadorNome(String apresentadorNome) {
        this.apresentadorNome = apresentadorNome;
    }

    /**
     * @return the categoriaNome
     */
    public String getCategoriaNome() {
        return categoriaNome;
    }

    /**
     * @param categoriaNome the categoriaNome to set
     */
    public void setCategoriaNome(String categoriaNome) {
        this.categoriaNome = categoriaNome;
    }

    /**
     * @return the unidadeApresentadorNome
     */
    public String getUnidadeApresentadorNome() {
        return unidadeApresentadorNome;
    }

    /**
     * @param unidadeApresentadorNome the unidadeApresentadorNome to set
     */
    public void setUnidadeApresentadorNome(String unidadeApresentadorNome) {
        this.unidadeApresentadorNome = unidadeApresentadorNome;
    }

    /**
     * @return the oral
     */
    public int getOral() {
        return oral;
    }

    /**
     * @param oral the oral to set
     */
    public void setOral(int oral) {
        
        
        this.oral = oral;
    }

    public String getApresentacaoNomeRealizarAv() {
        
        if(oral>0)
            return "Com Oral: "+apresentacaoNome;
        else
            return apresentacaoNome;
        
    }

    

    
    
    
}
