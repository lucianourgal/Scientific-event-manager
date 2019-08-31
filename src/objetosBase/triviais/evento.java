/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package objetosBase.triviais;

import ClassesAuxiliares.BancoDeDados;
import ClassesAuxiliares.geradorCertificados;
import java.util.ArrayList;
import objetosBase.apresentacao;
import objetosBase.apresentador;
import objetosBase.avaliador;
import objetosBase.certificado;

/**
 *
 * @author Serac02
 */
public class evento {
    private String nome;
    private String id;
    private int unidadeSedeCod;
    private String data;
    private int ano;
    private String local;
    private boolean ativo;
    private int sepin;
    
    
    public boolean salvarEventoNoBD(){
    
    int ativoI;
    if(ativo)
        ativoI = 1;
    else
        ativoI = 0;    
        
    String s = "INSERT INTO evento (nome, ano, local, issn, ativo, data, unidadesede, sepin) "
            + "VALUES ('"+nome+"',"+ano+",'"+local+"','"+id+"',"+ativoI+", '"+data+"',"+unidadeSedeCod+","+sepin+");";
    
    int r = BancoDeDados.comando().insert(s);
    
    if(r>0)
        System.out.println("OK: Salvou evento "+nome+" no BD!");
    else
        System.out.println("FALHA: Não salvou evento "+nome+" no BD!");
    
    return(r>0);
    
    }
    
    
    public evento(String no, String i){
        nome = no;
        id = i;
        
    }
    
    public boolean isSepin(){
    return sepin>0;
    }
    
    public void deletarTudoDoEventodoBD(){
    
        
    //apagar certificados
    geradorCertificados geradorCertificados = new geradorCertificados();
    ArrayList<certificado> certificados = BancoDeDados.comando().getCertificados(geradorCertificados, false, false, getId());    
    for(int c=0;c<certificados.size();c++)
        certificados.get(c).apagarCertificado();
        
    System.out.println("OK: Apagou certificados de evento "+ this.getNome());
    
    //apagar apresentacoes
    ArrayList<apresentacao> apresentacoes = BancoDeDados.comando().recApresentacoesParaGerarAnaisDoSeminario(getId(), -1, -1, "id", -1);  //Recupera dados  (unico para cada sheet)    
    for(int a=0;a<apresentacoes.size();a++)
        BancoDeDados.comando().deletarApresentacaoCompletamente(apresentacoes.get(a).getId());
    
    System.out.println("OK: Apagou apresentacoes/avaliacoes/vinculos de evento "+ this.getNome());
    
    //apagar apresentadores (destes apresentadores, verificar quais possuem apresentações ainda)
    ArrayList<apresentador> apresentadores = BancoDeDados.comando().recuperarApresentadoresEfetivosDoCentralizado(this);  //Recupera dados  (unico para cada sheet)
    for(int t=0;t<apresentadores.size();t++)
        if(apresentadores.get(t).recuperaApresentacoes(new ArrayList<coordenador>())<1)
            apresentadores.get(t).apagarApresentadorDoBD();
    
    System.out.println("OK: Apagou apresentadores do evento que não tinham outras apresentações "+ this.getNome());
    
    //apagar avaliadores (menos coordenadores gerais)
    String s = "DELETE FROM coordenador WHERE unidade <> 22;";
    BancoDeDados.comando().update(s);
    System.out.println("OK: Apagou coordenadores de evento de fora da reitoria para "+ this.getNome());
    
    ArrayList<avaliador> avaliadores = BancoDeDados.comando().recuperarAvaliadoresPossiveisParaArea(-1, 1, -1, true, getId()); //avaliadores marcados como acompanhantes, que irão/estão no evento.
    for(int v=0;v<avaliadores.size();v++)
        if(BancoDeDados.comando().recuperaLacosCoordenador(avaliadores.get(v).getCpf()).size()<1) //se não tiver cargo de coordenador, apaga
            avaliadores.get(v).apagarAvaliadorDoBD();
    
    System.out.println("OK: Apagou avaliadores! ");
    
    BancoDeDados.comando().update("UPDATE evento SET ativo = 0 WHERE issn = '"+this.id+"';");
    
    System.out.println("OK: Terminou de apagar tudo do evento "+this.getNome()+"! ");
    
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
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the unidadeSedeCod
     */
    public int getUnidadeSedeCod() {
        return unidadeSedeCod;
    }

    /**
     * @param unidadeSedeCod the unidadeSedeCod to set
     */
    public void setUnidadeSedeCod(int unidadeSedeCod) {
        this.unidadeSedeCod = unidadeSedeCod;
    }

    /**
     * @return the data
     */
    public String getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(String data) {
        this.data = data;
    }

    /**
     * @return the ano
     */
    public int getAno() {
        return ano;
    }

    /**
     * @param ano the ano to set
     */
    public void setAno(int ano) {
        this.ano = ano;
    }

    /**
     * @return the local
     */
    public String getLocal() {
        return local;
    }

    /**
     * @param local the local to set
     */
    public void setLocal(String local) {
        this.local = local;
    }

    /**
     * @return the ativo
     */
    public boolean isAtivo() {
        return ativo;
    }

    /**
     * @param ativo the ativo to set
     */
    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    /**
     * @param sepin the sepin to set
     */
    public void setSepin(int sepin) {
        this.sepin = sepin;
    }
}
