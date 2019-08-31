/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package objetosBase.triviais;

import java.util.ArrayList;

/**
 *
 * @author SistemaIP
 */
public class categoria {
    
    private String nome;
    private int id;
    private ArrayList<Integer> modalidades = new ArrayList<>();
    private boolean grandeArea;
    private boolean cultural;
    private boolean areaTematica;
    private boolean comResumo;

    
   
    
    public void addModalidade(int cat, int m){
       
        if(cat==id)
        getModalidades().add(m);
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
     * @return the modalidades
     */
    public ArrayList<Integer> getModalidades() {
        return modalidades;
    }

    /**
     * @param modalidades the modalidades to set
     */
    public void setModalidades(ArrayList<Integer> modalidades) {
        this.modalidades = modalidades;
    }

    /**
     * @return the grandeArea
     */
    public boolean isGrandeArea() {
        return grandeArea;
    }

    /**
     * @param grandeArea the grandeArea to set
     */
    public void setGrandeArea(boolean grandeArea) {
        this.grandeArea = grandeArea;
    }

    /**
     * @return the areaTematica
     */
    public boolean isAreaTematica() {
        return areaTematica;
    }

    /**
     * @param areaTematica the areaTematica to set
     */
    public void setAreaTematica(boolean areaTematica) {
        this.areaTematica = areaTematica;
    }

    /**
     * @return the comResumo
     */
    public boolean isComResumo() {
        return comResumo;
    }

    /**
     * @param comResumo the comResumo to set
     */
    public void setComResumo(boolean comResumo) {
        this.comResumo = comResumo;
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
