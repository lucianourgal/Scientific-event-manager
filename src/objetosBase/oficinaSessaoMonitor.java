/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package objetosBase;

import ClassesAuxiliares.BancoDeDados;

/**
 *
 * @author Usuario
 */
public class oficinaSessaoMonitor {
    
    private String tipo;
    private String nomeParticipantes;
    private String verbo;
    private String trabalhoOuCampus;
    private String documento;
    private String evento;
    private int id;
    
    private boolean atualizouDados = false;

    
    public boolean adequarNoBd(){
    
    if(deveSerSalvoNoBD()){
    //insere novo registro
    BancoDeDados.comando().inserirOficinaSessaoMonitor(this);
        
    //recupera id
    id = BancoDeDados.comando().returnMax("SELECT MAX(id) as max FROM oficinasessaopalestra;");
        
    }else if(deveAtualizarNoBD()){
    //atualiza no BD
    BancoDeDados.comando().atualizarOficinaSessaoMonitor(this);
        
    //dados no BD já estão iguais aos da interface e aos deste objeto
    atualizouDados = false;
    }
    
    //não precisou fazer nada
    return true;
    }
    
        
    public boolean deveSerSalvoNoBD(){
    
        // já tem id, então já está no bd
        if(id!=-1)
            return false;
        
        // condições minimas para ser um registro
        if(!tipo.equals("-") && !verbo.equals("-") && nomeParticipantes.length()>4 && trabalhoOuCampus.length()>3)
            return true;
        
        // para que não atualize logo após salvar
        atualizouDados = false;
        
        return false;
    }
    
    
    public void reAlimentarObjeto(String tipo0, String nomeParticipantes0, String verbo0, String trabalhoOuCampus0, String documento0){
    
        if(!tipo0.equals(tipo) || !nomeParticipantes0.equals(nomeParticipantes) || !verbo0.equals(verbo) ||
                !trabalhoOuCampus0.equals(trabalhoOuCampus) || !documento0.equals(documento) && id!=-1)
            atualizouDados = true;
    
        tipo = tipo0;
        nomeParticipantes = nomeParticipantes0;
        verbo = verbo0;
        trabalhoOuCampus = trabalhoOuCampus0;
        documento = documento0;
    
    }
    
    public boolean deveAtualizarNoBD(){
        return atualizouDados;
    }
    
    
    
    /**
     * @return the tipo
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * @param tipo the tipo to set
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    /**
     * @return the nomeParticipantes
     */
    public String getNomeParticipantes() {
        return nomeParticipantes;
    }

    /**
     * @param nomeParticipantes the nomeParticipantes to set
     */
    public void setNomeParticipantes(String nomeParticipantes) {
        this.nomeParticipantes = nomeParticipantes;
    }

    /**
     * @return the verbo
     */
    public String getVerbo() {
        return verbo;
    }

    /**
     * @param verbo the verbo to set
     */
    public void setVerbo(String verbo) {
        this.verbo = verbo;
    }

    /**
     * @return the trabalhoOuCampus
     */
    public String getTrabalhoOuCampus() {
        return trabalhoOuCampus;
    }

    /**
     * @param trabalhoOuCampus the trabalhoOuCampus to set
     */
    public void setTrabalhoOuCampus(String trabalhoOuCampus) {
        this.trabalhoOuCampus = trabalhoOuCampus;
    }

    /**
     * @return the documento
     */
    public String getDocumento() {
        return documento;
    }

    /**
     * @param documento the documento to set
     */
    public void setDocumento(String documento) {
        this.documento = documento;
    }

    /**
     * @return the evento
     */
    public String getEvento() {
        return evento;
    }

    /**
     * @param evento the evento to set
     */
    public void setEvento(String evento) {
        this.evento = evento;
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
            
    
    
}
