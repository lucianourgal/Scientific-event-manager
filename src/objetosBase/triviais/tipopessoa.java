/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package objetosBase.triviais;

/**
 *
 * @author SistemaIP
 */
public class tipopessoa {
    
    private String nome;
    private int id;
    private boolean podeApresentar, podeAvaliar;
    
    
    public tipopessoa(String no, int i){
        nome = no;
        id = i;
        
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
     * @return the podeApresentar
     */
    public boolean isPodeApresentar() {
        return podeApresentar;
    }

    /**
     * @param podeApresentar the podeApresentar to set
     */
    public void setPodeApresentar(boolean podeApresentar) {
        this.podeApresentar = podeApresentar;
    }

    /**
     * @return the podeAvaliar
     */
    public boolean isPodeAvaliar() {
        return podeAvaliar;
    }

    /**
     * @param podeAvaliar the podeAvaliar to set
     */
    public void setPodeAvaliar(boolean podeAvaliar) {
        this.podeAvaliar = podeAvaliar;
    }
    
    
    
}
