/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package objetosBase;

import java.util.ArrayList;
import objetosBase.triviais.categoria;

/**
 *
 * @author SistemaIP
 */
public class rankingApresentacao {
    
    private String apresentacaoNome;
    private String apresentadorNome;
    private String categoriaNome;
    private int categoriaId;
    private String apresentadorCpf;
    private String unidadeNome;
    private String areaNome;
    private String subAreaNome;
    private int apresentacaoID;
    private int apresentacaoOral;
    private int unidadeID;
    private int areaID;
    private int subAreaID;
    private int numeroAvaliacoes;
    private int media;
    private int colocacao;
    
    private String tipoCurso;

    /**
     * @return the apresentacaoNome
     */
    public String getApresentacaoNome() {
        if(apresentacaoOral==0)
            return apresentacaoNome;
        else
            return "Com Oral: "+apresentacaoNome;
    }

    /**
     * @param apresentacaoNome the apresentacaoNome to set
     */
    public void setApresentacaoNome(String apresentacaoNome) {
        this.apresentacaoNome = apresentacaoNome;
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
     * @return the apresentadorCpf
     */
    public String getApresentadorCpf() {
        return apresentadorCpf;
    }

    /**
     * @param apresentadorCpf the apresentadorCpf to set
     */
    public void setApresentadorCpf(String apresentadorCpf) {
        this.apresentadorCpf = apresentadorCpf;
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
     * @return the areaNome
     */
    public String getAreaNome() {
        return areaNome;
    }

    /**
     * @param areaNome the areaNome to set
     */
    public void setAreaNome(String areaNome) {
        this.areaNome = areaNome;
    }

    /**
     * @return the subAreaNome
     */
    public String getSubAreaNome() {
        return subAreaNome;
    }

    /**
     * @param subAreaNome the subAreaNome to set
     */
    public void setSubAreaNome(String subAreaNome) {
        this.subAreaNome = subAreaNome;
    }

    /**
     * @return the apresentacaoID
     */
    public int getApresentacaoID() {
        return apresentacaoID;
    }

    /**
     * @param apresentacaoID the apresentacaoID to set
     */
    public void setApresentacaoID(int apresentacaoID, int oral) {
        this.apresentacaoID = apresentacaoID;
        apresentacaoOral = oral;
    }

    /**
     * @return the unidadeID
     */
    public int getUnidadeID() {
        return unidadeID;
    }

    /**
     * @param unidadeID the unidadeID to set
     */
    public void setUnidadeID(int unidadeID) {
        this.unidadeID = unidadeID;
    }

    /**
     * @return the areaID
     */
    public int getAreaID() {
        return areaID;
    }

    /**
     * @param areaID the areaID to set
     */
    public void setAreaID(int areaID) {
        this.areaID = areaID;
    }

    /**
     * @return the subAreaID
     */
    public int getSubAreaID() {
        return subAreaID;
    }

    /**
     * @param subAreaID the subAreaID to set
     */
    public void setSubAreaID(int subAreaID) {
        this.subAreaID = subAreaID;
    }

    /**
     * @return the numeroAvaliacoes
     */
    public int getNumeroAvaliacoes() {
        return numeroAvaliacoes;
    }

    /**
     * @param numeroAvaliacoes the numeroAvaliacoes to set
     */
    public void setNumeroAvaliacoes(int numeroAvaliacoes) {
        this.numeroAvaliacoes = numeroAvaliacoes;
      //  System.out.println("Avaliado "+numeroAvaliacoes+" vezes");
    }

    /**
     * @return the media
     */
    public int getMedia() {
        return media;
    }

    /**
     * @param media the media to set
     */
    public void setMedia(int media) {
        this.media = media;
    }

    /**
     * @return the colocacao
     */
    public int getColocacao() {
        return colocacao;
    }

    /**
     * @param colocacao the colocacao to set
     */
    public void setColocacao(int colocacao) {
        this.colocacao = colocacao;
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
    public void setCategoriaNome(int id, ArrayList<categoria> cats) {
        
        for(categoria c: cats)
            if(c.getId()==id){
                categoriaNome = c.getNome();
                return;
            }
        
        categoriaNome = "??";
        
    }

    /**
     * @return the categoriaId
     */
    public int getCategoriaId() {
        return categoriaId;
    }

    /**
     * @param categoriaId the categoriaId to set
     */
    public void setCategoriaId(int categoriaId) {
        this.categoriaId = categoriaId;
    }

    /**
     * @return the tipoCurso
     */
    public String getTipoCurso() {
        return tipoCurso;
    }

    /**
     * @param tipoCurso the tipoCurso to set
     */
    public void setTipoCurso(String tipoCurso) {
        this.tipoCurso = tipoCurso;
    }
    
    
}
