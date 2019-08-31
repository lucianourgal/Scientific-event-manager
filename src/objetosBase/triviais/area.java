/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package objetosBase.triviais;

import java.util.ArrayList;

/**
 *
 * @author Serac02
 */
public class area {
       private String nome;
    private int id;
    private boolean tematica=false;
    private boolean cultural = false;
    private ArrayList<subarea> subareas = new ArrayList<>();
    
    public area(String no, int i){
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

    public ArrayList<subarea> getSubareas() {
        return subareas;
    }

    public void setSubareas(ArrayList<subarea> subareas) {
        this.subareas = subareas;
    }
    
    public void addSubArea(subarea sa){
    subareas.add(sa);
    }
    
     private int codigoSubArea(String nome) {
           for (subarea subarea : subareas) {
               if (subarea.getNome().equals(nome)) {
                   return subarea.getId();
               }
           }
        
        return -1;
    }
    
     public String nomeSubArea(int id){
    
           for (subarea subarea : subareas) {
               if (subarea.getId() == id) {
                   return subarea.getNome();
               }
           }
        
        return "nulo";
    }

    /**
     * @return the tematica
     */
    public boolean isTematica() {
        return tematica;
    }

    /**
     * @param tematica the tematica to set
     */
    public void setTematica(boolean tematica) {
        this.tematica = tematica;
    }

    /**
     * @return the cultural
     */
    public boolean isCultural() {
        return cultural;
    }

    /**
     * @param cultural the cultural to set
     */
    public void setCultural(boolean cultural) {
        this.cultural = cultural;
    }
    
    
}
