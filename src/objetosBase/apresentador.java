/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package objetosBase;

import ClassesAuxiliares.BancoDeDados;
import ClassesAuxiliares.mailSender;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import objetosBase.triviais.coordenador;

/**
 *
 * @author SistemaIP
 */
public class apresentador {

    private String nome;
    private ArrayList<String> nomesApresentacao = new ArrayList<>();
    private ArrayList<Integer> idsApresentacao = new ArrayList<>();

    //dados extras   
    private String cpf;
    private String rg;
    private String login;
    private String bolsaNome;
    private String observacoes;
    private int bolsaCod;
    private String senha;
    private String sexo;
    private String datadenascimento;
    private String dataexprg;
    private String orgaoexpedidorrg;
    private String estadorg;
    private String pai;
    private String mae;
    private String naturalidade;
    private String estadonaturalidade;
    private String email;
    private String telefone;
    private String endereco;
    private String municipio;
    private String estado;
    private String cep;
    private String bairro;
    private String banco;
    private String agencia;
    private String conta;
    private String tipoconta;
    private String necessidadesespeciais, declarante, nomedeclarante, cpfdeclarante;
    private int tipoPessoa;
    private String vinculo;
    private int codVinculo;
    private String unidade;
    private int codigoUnidade;

    /**
     * @param coord Array de registros de coordenador
     * @see Id e nome das apresentacoes cadastradas
     */
    public int recuperaApresentacoes(ArrayList<coordenador> coord) {
        String SQL;
        if (coord.size() < 1) {
            SQL = "SELECT DISTINCT a.id, a.nome FROM apresentacao a WHERE apresentador = '" + cpf + "';";
        } else if (coordenadorGeral(coord)) {
            SQL = "SELECT DISTINCT a.id, a.nome FROM apresentacao a, apresentador ap, unidade u "
                    + "WHERE a.apresentador = ap.cpf AND ap.unidade = u.id ORDER BY a.nome;";
        } else {
            SQL = "SELECT DISTINCT a.id, a.nome "
                    + "FROM apresentacao a, apresentador ape "
                    + "WHERE a.apresentador = ape.cpf AND ape.unidade = " + coord.get(0).getUnidadeCodigo() + " ORDER BY a.nome;";
        }

        ResultSet rs = BancoDeDados.comando().select(SQL);
        //nomesApresentacao=new ArrayList<>();
        //idsApresentacao=new ArrayList<>();
        try {
            while (rs.next()) {

                nomesApresentacao.add(rs.getString("nome"));
                idsApresentacao.add(rs.getInt("id"));

            }
        } catch (SQLException ex) {
            Logger.getLogger(apresentador.class.getName()).log(Level.SEVERE, null, ex);
        }
        String re = "";
        for (int a = 0; a < getIdsApresentacao().size(); a++) {
            re = re + getNomesApresentacao().get(a) + ", " + getIdsApresentacao().get(a) + "; ";
        }

        System.out.println("Recuperado: " + re);
        return idsApresentacao.size();

    }

    public boolean coordenadorGeral(ArrayList<coordenador> c) {

        for (coordenador c2 : c) {
            if (c2.getCentralizado() > 0) {
                return true;
            }
        }

        return false;
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
     * @return the login
     */
    public String getLogin() {
        return login;
    }

    /**
     * @param login the login to set
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * @return the md5(senha)
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
     * @return the datadenascimento
     */
    public String getDatadenascimento() {
        return datadenascimento;
    }

    /**
     * @param datadenascimento the datadenascimento to set
     */
    public void setDatadenascimento(String datadenascimento) {
        this.datadenascimento = datadenascimento;
    }

    /**
     * @return the dataexprg
     */
    public String getDataexprg() {
        return dataexprg;
    }

    /**
     * @param dataexprg the dataexprg to set
     */
    public void setDataexprg(String dataexprg) {
        this.dataexprg = dataexprg;
    }

    /**
     * @return the orgaoexpedidorrg
     */
    public String getOrgaoexpedidorrg() {
        return orgaoexpedidorrg;
    }

    /**
     * @param orgaoexpedidorrg the orgaoexpedidorrg to set
     */
    public void setOrgaoexpedidorrg(String orgaoexpedidorrg) {
        this.orgaoexpedidorrg = orgaoexpedidorrg;
    }

    /**
     * @return the estadorg
     */
    public String getEstadorg() {
        return estadorg;
    }

    /**
     * @param estadorg the estadorg to set
     */
    public void setEstadorg(String estadorg) {
        this.estadorg = estadorg;
    }

    /**
     * @return the pai
     */
    public String getPai() {
        return pai;
    }

    /**
     * @param pai the pai to set
     */
    public void setPai(String pai) {
        this.pai = pai;
    }

    /**
     * @return the mae
     */
    public String getMae() {
        return mae;
    }

    /**
     * @param mae the mae to set
     */
    public void setMae(String mae) {
        this.mae = mae;
    }

    /**
     * @return the naturalidade
     */
    public String getNaturalidade() {
        return naturalidade;
    }

    /**
     * @param naturalidade the naturalidade to set
     */
    public void setNaturalidade(String naturalidade) {
        this.naturalidade = naturalidade;
    }

    /**
     * @return the estadonaturalidade
     */
    public String getEstadonaturalidade() {
        return estadonaturalidade;
    }

    /**
     * @param estadonaturalidade the estadonaturalidade to set
     */
    public void setEstadonaturalidade(String estadonaturalidade) {
        this.estadonaturalidade = estadonaturalidade;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        if (email != null) {
            return email.toLowerCase();
        } else {
            return "";
        }
    }

    public String getEmailAnais() {
        if (email.length() > 5) {
            return "(" + email.toLowerCase() + ") ";
        } else {
            return "";
        }
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
     * @return the vinculo
     */
    public String getVinculo() {
        return vinculo;
    }

    /**
     * @param vinculo the vinculo to set
     */
    public void setVinculo(String vinculo) {
        this.vinculo = vinculo;
    }

    /**
     * @return the unidade
     */
    public String getUnidade() {
        return unidade;
    }

    /**
     * @param unidade the unidade to set
     */
    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }

    /**
     * @return the codigoUnidade
     */
    public int getCodigoUnidade() {
        return codigoUnidade;
    }

    /**
     * @param codigoUnidade the codigoUnidade to set
     */
    public void setCodigoUnidade(int codigoUnidade) {
        this.codigoUnidade = codigoUnidade;
    }

    /**
     * @return the codVinculo
     */
    public int getCodVinculo() {
        return codVinculo;
    }

    /**
     * @param codVinculo the codVinculo to set
     */
    public void setCodVinculo(int codVinculo) {
        this.codVinculo = codVinculo;
    }

    /**
     * @return the necessidadesespeciais
     */
    public String getNecessidadesespeciais() {
        return necessidadesespeciais;
    }

    /**
     * @param necessidadesespeciais the necessidadesespeciais to set
     */
    public void setNecessidadesespeciais(String necessidadesespeciais) {
        this.necessidadesespeciais = necessidadesespeciais;
    }

    /**
     * @return the declarante
     */
    public String getDeclarante() {
        return declarante;
    }

    /**
     * @param declarante the declarante to set
     */
    public void setDeclarante(String declarante) {
        this.declarante = declarante;
    }

    /**
     * @return the nomedeclarante
     */
    public String getNomedeclarante() {
        return nomedeclarante;
    }

    /**
     * @param nomedeclarante the nomedeclarante to set
     */
    public void setNomedeclarante(String nomedeclarante) {
        this.nomedeclarante = nomedeclarante;
    }

    /**
     * @return the cpfdeclarante
     */
    public String getCpfdeclarante() {
        return cpfdeclarante;
    }

    /**
     * @param cpfdeclarante the cpfdeclarante to set
     */
    public void setCpfdeclarante(String cpfdeclarante) {
        this.cpfdeclarante = cpfdeclarante;
    }

    /**
     * @return the bolsaCod
     */
    public int getBolsaCod() {
        return bolsaCod;
    }

    /**
     * @param bolsaCod the bolsaCod to set
     */
    public void setBolsaCod(int bolsaCod) {
        this.bolsaCod = bolsaCod;
    }

    /**
     * @return the bolsaNome
     */
    public String getBolsaNome() {
        return bolsaNome;
    }

    /**
     * @param bolsaNome the bolsaNome to set
     */
    public void setBolsaNome(String bolsaNome) {
        this.bolsaNome = bolsaNome;
    }

    public ArrayList<String> getNomesApresentacao() {
        return nomesApresentacao;
    }

    public void setNomesApresentacao(ArrayList<String> nomesApresentacao) {
        this.nomesApresentacao = nomesApresentacao;
    }

    public ArrayList<Integer> getIdsApresentacao() {
        return idsApresentacao;
    }

    public void setIdsApresentacao(ArrayList<Integer> idsApresentacao) {
        this.idsApresentacao = idsApresentacao;
    }

    public boolean inserirNaBase() {
        return BancoDeDados.comando().inserirApresentador(this);
    }

    public boolean atualizarNaBase(boolean attSenha) {
        return BancoDeDados.comando().atualizarApresentador(this, attSenha);
    }

    /**
     * @return the tipoPessoa
     */
    public int getTipoPessoa() {
        return tipoPessoa;
    }

    /**
     * @param tipoPessoa the tipoPessoa to set
     */
    public void setTipoPessoa(int tipoPessoa) {
        this.tipoPessoa = tipoPessoa;
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

    void setarDadosAutor(autor novoApr) {
        cpf = novoApr.getCpf();
        nome = novoApr.getNome();
        email = novoApr.getEmail();
        sexo = novoApr.getSexo();
        datadenascimento = novoApr.getNascimento();

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
        String SQL = "UPDATE apresentador SET senha=md5('" + senha + "') WHERE cpf = '" + cpf + "';";
        BancoDeDados.comando().update(SQL);

        //enviar e-mail com nova senha
        mailSender mail = new mailSender();
        mail.sendMail("MENSAGEM AUTOMÁTICA: Senha reiniciada no Sistema do SEPIN.", "Prezado apresentador " + nome + ",\n\nSua senha foi resetada para:\n" + senha + "\n\nAtenciosamente, Equipe do SEPIN", email);

        return true;
    }

    boolean testeDadosBasicos() {

        if (cpf.length() < 6) {
            System.out.println("CPF não preenchido");
            return false;
        }
        if (nome.length() < 6) {
            System.out.println("Nome não preenchido");
            return false;
        }
        if (email.length() < 6) {
            System.out.println("Email não preenchido");
            return false;
        }
        if (codigoUnidade < 1) {
            System.out.println("Código da unidade não preenchido");
            return false;
        }

        return true;
    }

    public void apagarApresentadorDoBD() {
        String s = "DELETE FROM apresentador WHERE cpf = '"+this.cpf+"'";
        if(BancoDeDados.comando().update(s)>0)
            System.out.println("OK: Apagou apresentador "+nome+ " do BD!");
        else
            System.out.println("Falha: Nao apagou apresentador "+nome+ " do BD!");
        
    }

}
