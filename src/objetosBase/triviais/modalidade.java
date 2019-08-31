/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package objetosBase.triviais;

/**
 *
 * @author Serac02
 */
public class modalidade {
      private String nome;
    private int id, usamateriais, temnomegrupo;
 
    
    
    public modalidade(String no, int i){
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
     * @return the usamateriais
     */
    public boolean Usamateriais() {
        return usamateriais>0;
    }

    /**
     * @param usamateriais the usamateriais to set
     */
    public void setUsamateriais(int usamateriais) {
        this.usamateriais = usamateriais;
    }

    /**
     * @return the temnomegrupo
     */
    public boolean Temnomegrupo() {
        return temnomegrupo>0;
    }

    /**
     * @param temnomegrupo the temnomegrupo to set
     */
    public void setTemnomegrupo(int temnomegrupo) {
        this.temnomegrupo = temnomegrupo;
    }

    /**
     * @param classificatematica the classificatematica to set
     */
   
    
    
}
