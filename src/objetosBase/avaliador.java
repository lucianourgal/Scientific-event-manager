/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package objetosBase;

import ClassesAuxiliares.BancoDeDados;
import ClassesAuxiliares.mailSender;
import java.util.ArrayList;
import java.util.Random;
import objetosBase.triviais.evento;

/**
 *
 * @author Luciano
 */
public class avaliador {

    private String nome;
    private String cpf;
    private String senha;
    private String sexo;
    private String titulacaoNome;
    private String email;
    private String telefone;
    private String endereco;
    private String municipio;
    private String estado;
    private String bairro;
    private String cep;
    private String banco;
    private String agencia;
    private String conta;
    private String tipoconta;
    private String unidadeNome;
    private String tipoPessoaNome;
    private String formacao;
    
    private String rg;
    private String observacoes;
    
    private boolean acompanhante=false;
    private int siape;
    private int titulacaoCod;
    private int unidadeCod;
    private int tipoPessoaCod;
    private ArrayList<Integer> areaCod = new ArrayList<>();
    private ArrayList<String> areaNome = new ArrayList<>();
    private ArrayList<String> areaNomeRelat = new ArrayList<>();

    private ArrayList<Integer> excluirAreaCod = new ArrayList<>();
    private ArrayList<String> excluirAreaNome = new ArrayList<>();
    private ArrayList<avaliacao> avaliacoesPossiveis;
    
    private int numeroAvaliacoesCadastradas;
    private int horasCert=30;

    public void addArea(String nome, int cod) {

        int t = getExcluirAreaCod().indexOf(cod);
        if (t != -1) {
            excluirAreaCod.remove(excluirAreaNome.indexOf(nome));
            excluirAreaNome.remove(t);
            System.out.println(nome+" será mantida. Temos "+excluirAreaNome.size()+" ("+excluirAreaCod.size()+") para excluir.");
        } else {
            areaCod.add(cod);
            getAreaNome().add(nome);
        }

    }
    
    public void calcularHorasCertificado(boolean save){
    /*só pôster e/ou IFTECH = 3h30m;
    pôster e/ou IFTECH + comunicação oral = 5h
    só comunicação oral = 1h30m*/
    boolean temOral=false; 
    boolean temPoster=false;
    
    horasCert = 0;
    
    for(avaliacao a: avaliacoesPossiveis){
        if(a.getOral()>0)
            temOral = true;
        else
            temPoster = true;
        
    }  
    
    if(temOral)
        horasCert = 90;
    
    if(temPoster)
        horasCert = horasCert + 240;
    
    
    if(save)
        BancoDeDados.comando().update("UPDATE avaliador SET horas = "+horasCert+" WHERE cpf = '"+cpf+"';");
    
    }
    

    public void addExcluirArea(String nome, int cod) {
        excluirAreaCod.add(cod);
        excluirAreaNome.add(nome);
        areaNomeRelat.add(nome);
       
    }

    public void informaQuantidades(){
     System.out.println("Temos "+excluirAreaNome.size()+" no vetor de exclusão, e "+areaCod.size()+" para incluir.");
    }
    
    
    public int getAcompanhante(){
    
        if(acompanhante)
            return 1;
        else
            return 0;
        
    }
    
    /**
     * @return the nome
     */
    public String getNome() {
        return nome;
    }

    public String getAcompanhanteText(){
    
        if(acompanhante)
            return "Sim";
        else
            return "Não";
        
    }
    
    /**
     * @param nome the nome to set
     */
    public void setNome(String nome) {
        this.nome = nome;
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
     * @return the senha
     */
    public String getSenha() {
        return senha;
    }

    /**
     * @param senha the senha to set
     */
    public void setSenha(String senha) {
        this.senha = senha;
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
     * @return the titulacaoNome
     */
    public String getTitulacaoNome() {
        return titulacaoNome;
    }

    /**
     * @param titulacaoNome the titulacaoNome to set
     */
    public void setTitulacaoNome(String titulacaoNome) {
        this.titulacaoNome = titulacaoNome;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
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
     * @return the municipio
     */
    public String getMunicipio() {
        return municipio;
    }

    /**
     * @param municipio the municipio to set
     */
    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    /**
     * @return the estado
     */
    public String getEstado() {
        return estado;
    }

    /**
     * @param estado the estado to set
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }

    /**
     * @return the bairro
     */
    public String getBairro() {
        return bairro;
    }

    /**
     * @param bairro the bairro to set
     */
    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    /**
     * @return the cep
     */
    public String getCep() {
        return cep;
    }

    /**
     * @param cep the cep to set
     */
    public void setCep(String cep) {
        this.cep = cep;
    }

    /**
     * @return the banco
     */
    public String getBanco() {
        return banco;
    }

    /**
     * @param banco the banco to set
     */
    public void setBanco(String banco) {
        this.banco = banco;
    }

    /**
     * @return the agencia
     */
    public String getAgencia() {
        return agencia;
    }

    /**
     * @param agencia the agencia to set
     */
    public void setAgencia(String agencia) {
        this.agencia = agencia;
    }

    /**
     * @return the conta
     */
    public String getConta() {
        return conta;
    }

    /**
     * @param conta the conta to set
     */
    public void setConta(String conta) {
        this.conta = conta;
    }

    /**
     * @return the tipoconta
     */
    public String getTipoconta() {
        return tipoconta;
    }

    /**
     * @param tipoconta the tipoconta to set
     */
    public void setTipoconta(String tipoconta) {
        this.tipoconta = tipoconta;
    }

    /**
     * @return the unidadeNome
     */
    public String getUnidadeNome() {
        return unidadeNome;
    }

    /**
     * @param unidadeNome the unidadeNome to set
     */
    public void setUnidadeNome(String unidadeNome) {
        this.unidadeNome = unidadeNome;
    }

    /**
     * @return the tipoPessoaNome
     */
    public String getTipoPessoaNome() {
        return tipoPessoaNome;
    }

    /**
     * @param tipoPessoaNome the tipoPessoaNome to set
     */
    public void setTipoPessoaNome(String tipoPessoaNome) {
        this.tipoPessoaNome = tipoPessoaNome;
    }

    /**
     * @return the siape
     */
    public int getSiape() {
        return siape;
    }

    /**
     * @param siape the siape to set
     */
    public void setSiape(int siape) {
        this.siape = siape;
    }

    /**
     * @return the titulacaoCod
     */
    public int getTitulacaoCod() {
        return titulacaoCod;
    }

    /**
     * @param titulacaoCod the titulacaoCod to set
     */
    public void setTitulacaoCod(int titulacaoCod) {
        this.titulacaoCod = titulacaoCod;
    }

    /**
     * @return the unidadeCod
     */
    public int getUnidadeCod() {
        return unidadeCod;
    }

    /**
     * @param unidadeCod the unidadeCod to set
     */
    public void setUnidadeCod(int unidadeCod) {
        this.unidadeCod = unidadeCod;
    }

    /**
     * @return the tipoPessoaCod
     */
    public int getTipoPessoaCod() {
        return tipoPessoaCod;
    }

    /**
     * @param tipoPessoaCod the tipoPessoaCod to set
     */
    public void setTipoPessoaCod(int tipoPessoaCod) {
        this.tipoPessoaCod = tipoPessoaCod;
    }

    /**
    * @param b Atualizando Senha
     * @return sucesso
    */
    public boolean atualizarNaBase(boolean senha, boolean areas) {
        return BancoDeDados.comando().atualizarAvaliador(this, senha,areas);
    }

    public boolean inserirNaBase() {
        return BancoDeDados.comando().inserirAvaliador(this);
    }

    public ArrayList<Integer> getAreaCod() {
        return areaCod;
    }

    public void setAreaCod(ArrayList<Integer> areaCod) {
        this.areaCod = areaCod;
    }

    public ArrayList<String> getAreaNome() {
        return areaNome;
    }

    public void setAreaNome(ArrayList<String> areaNome) {
        this.areaNome = areaNome;
    }

    public ArrayList<Integer> getExcluirAreaCod() {
        return excluirAreaCod;
    }

    public void setExcluirAreaCod(ArrayList<Integer> excluirAreaCod) {
        this.excluirAreaCod = excluirAreaCod;
    }

    public ArrayList<String> getExcluirAreaNome() {
        return excluirAreaNome;
    }

    public void setExcluirAreaNome(ArrayList<String> excluirAreaNome) {
        this.excluirAreaNome = excluirAreaNome;
    }

    /**
     * @return the formacao
     */
    public String getFormacao() {
        return formacao;
    }

    /**
     * @param formacao the formacao to set
     */
    public void setFormacao(String formacao) {
        this.formacao = formacao;
    }
    
    public String listaAreasAvalia(){
    String s="";
        for(String a: areaNomeRelat)
            s = s + a+"; ";
    
        return s;
    }

    public void setAvaliacoesPossiveis(ArrayList<avaliacao> recuperarAvaliacoes) {
        avaliacoesPossiveis = recuperarAvaliacoes;
    }

    /**
     * @return the avaliacoesPossiveis
     */
    public ArrayList<avaliacao> getAvaliacoesPossiveis() {
        return avaliacoesPossiveis;
    }
    
    public String getListaAvaliacoesPossiveis(){
    String l="";
        for(avaliacao a: avaliacoesPossiveis)
            l = l + a.getApresentacao()+"; ";
    return l;
    }

    /**
     * @return the acompanhante
     */
    public boolean isAcompanhante() {
        return acompanhante;
    }

    /**
     * @param acompanhante the acompanhante to set
     */
    public void setAcompanhante(boolean acompanhante) {
        this.acompanhante = acompanhante;
    }

    public boolean atualizarStatusAcompanhante() {
      return BancoDeDados.comando().atualizarStatusAcompanhanteAvaliador(cpf,isAcompanhante())>0;
    }

    /**
     * @return the numeroAvaliacoesCadastradas
     */
    public int getNumeroAvaliacoesCadastradas() {
        return numeroAvaliacoesCadastradas;
    }

    /**
     * @param numeroAvaliacoesCadastradas the numeroAvaliacoesCadastradas to set
     */
    public void setNumeroAvaliacoesCadastradas(int numeroAvaliacoesCadastradas) {
        this.numeroAvaliacoesCadastradas = numeroAvaliacoesCadastradas;
    }

    public void descobrirQuantasEstaAvaliando(evento e) {
       numeroAvaliacoesCadastradas = BancoDeDados.comando().numeroAvaliacoesCadastradasAvaliador(cpf,e);
    }

    public String sexoExt(){
    
        if(sexo.equals("F"))
            return "Avaliadora";
        else if (sexo.equals("M"))
            return "Avaliador";
        
        return "Avaliador(a)";
    }
    
    public void setHorasCert(int h){
      horasCert = h;
    }

    public String getHorasCert() {
       String h="";
       int horas, min;         
       
       horas = (int) (horasCert /60);
       min = horasCert % 60;
       h = horas +"h"+min;
       
       return h;
    }
    
    
    public boolean resetSenha() {

        if (email == null) {
            return false;
        }

        //criar uma String de senha
        StringBuffer sb = new StringBuffer();
        Random rand = new Random();
        for (int i = 0; i < 8; i++) {
            sb.append(Integer.toString(Math.abs(rand.nextInt()) % 16, 16));
        }
        String senha = sb.toString();

        //atualizar senha no BD    
        String SQL = "UPDATE avaliador SET senha=md5('" + senha + "') WHERE cpf = '" + cpf + "';";
        BancoDeDados.comando().update(SQL);

        //enviar e-mail com nova senha
        mailSender mail = new mailSender();
        mail.sendMail("MENSAGEM AUTOMÁTICA: Senha reiniciada no Sistema do SEPIN.", "Prezado Avaliador " + nome + ",\n\nSua senha foi resetada para:\n" + senha + "\n\nAtenciosamente, Equipe do SEPIN", email);

        return true;
    }

    public void apagarAvaliadorDoBD() {
       
        //apaga areas que pode avaliar
        String s = "DELETE FROM avalia WHERE avaliador = '"+cpf+"';";
        if(BancoDeDados.comando().update(s)>0)
            System.out.println("OK: Apagou áreas que "+nome+" avalia!");
        else
            System.out.println("FALHA: Não apagou áreas que "+nome+" avalia!");
        
        //apagar cadastro de avaliador
        s = "DELETE FROM avaliador WHERE cpf = '"+cpf+"';";
        if(BancoDeDados.comando().update(s)>0)
            System.out.println("OK: Apagou avaliador "+nome+" do BD!");
        else
            System.out.println("FALHA: Não apagou avaliador "+nome+" ("+cpf+") do BD!");
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

    /**
     * @return the observacoes
     */
    public String getObservacoes() {
        return observacoes;
    }

    /**
     * @param observacoes the observacoes to set
     */
    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
    
    
}
