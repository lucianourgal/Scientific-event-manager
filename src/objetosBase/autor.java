/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package objetosBase;

import ClassesAuxiliares.Arrumador;
import ClassesAuxiliares.BancoDeDados;

/**
 *
 * @author Luciano
 */
public class autor {

    private boolean novo =false;
    private int id;
    private int unidadecodigo;
    private int funcaoCod;
    private int bolsaid;
    private int apresentacaoCod;
    private String nome;
    private String email;
    private String unidadeNome;
    private String funcaoNome;
    private String bolsaNome;
    private String cpf;
    private String rg;
    private String sexo;
    private String nascimento;
    private int ativo =0;
    private int ativoInicial=0;
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUnidadecodigo() {
        return unidadecodigo;
    }

    public void setUnidadecodigo(int unidadecodigo) {
        this.unidadecodigo = unidadecodigo;
    }

    public int getFuncaoCod() {
        return funcaoCod;
    }

    public void setFuncaoCod(int funcaoCod) {
        this.funcaoCod = funcaoCod;
    }

    public int getBolsaid() {
        return bolsaid;
    }

    public void setBolsaid(int bolsaid) {
        this.bolsaid = bolsaid;
    }

    public int getApresentacaoCod() {
        return apresentacaoCod;
    }

    public void setApresentacaoCod(int apresentacaoCod) {
        this.apresentacaoCod = apresentacaoCod;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email.toLowerCase();
    }
    
     public String getEmailAnais() {
        if(email.length()>5)
        return "("+email.toLowerCase()+") ";
        else
            return "";
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUnidadeNome() {
        return unidadeNome;
    }

    public void setUnidadeNome(String unidadeNome) {
        this.unidadeNome = unidadeNome;
    }

    public String getFuncaoNome() {
        return funcaoNome;
    }

    public void setFuncaoNome(String funcaoNome) {
        this.funcaoNome = funcaoNome;
    }

    public String getBolsaNome() {
        return bolsaNome;
    }

    public void setBolsaNome(String bolsaNome) {
        this.bolsaNome = bolsaNome;
    }

    public boolean isNovo() {
        return novo;
    }

    public void setNovo(boolean novo) {
        this.novo = novo;
    }

    /**
     * @return the cpf
     */
    public String getCpf() {
        return cpf;
    }

    /**
     * @param cpf the cpf to set
     */
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    /**
     * @return the sexo
     */
    public String getSexo() {
        return sexo;
    }

    /**
     * @param sexo the sexo to set
     */
    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    /**
     * @return the nascimento
     */
    public String getNascimento() {
        return nascimento;
    }

    /**
     * @param nascimento the nascimento to set
     */
    public void setNascimento(String nascimento) {
        this.nascimento = nascimento;
    }

    public String getNascimentoDate() {
       return Arrumador.comando().dataParaDATE(nascimento);
    }

    public void setNascimentoDate(String date) {
        this.nascimento = Arrumador.comando().DATEParaData(date);
    }

    /**
     * @return the ativo
     */
    public int getAtivo() {
        return ativo;
    }

    /**
     * @param ativo the ativo to set
     */
    public void setAtivo(int ativo) {
        this.ativo = ativo;
        
    }

    /**
     * @return the ativoInicial
     */
    public int getAtivoInicial() {
        return ativoInicial;
    }

    /**
     * @param ativoInicial the ativoInicial to set
     */
    public void setAtivoInicial(int ativoInicial) {
        this.ativoInicial = ativoInicial;
        ativo = ativoInicial;
    }

    public boolean atualizarSeMudouStatusAtivo() {
      
        if(ativo!=ativoInicial){
            ativoInicial = ativo;
            BancoDeDados.comando().atualizarAutor(this);
            return true;
            
            
        }
        return false;
    }

    void setarDadosApresentador(String apresentadorCpf) {
       
        apresentador n = BancoDeDados.comando().getApresentador(apresentadorCpf);
        nome = n.getNome();
        cpf = n.getCpf();
        email = n.getEmail();
        sexo = n.getSexo();
        
            
    }

    /**
     * @return the rg
     */
    public String getRg() {
        return rg;
    }

    /**
     * @param rg the rg to set
     */
    public void setRg(String rg) {
        this.rg = rg;
    }

   
    
}
