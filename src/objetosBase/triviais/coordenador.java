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
public class coordenador {
    
    private int centralizado;
    private int unidadeCodigo;
    private String unidadeNome;
    private String avaliadorNome;
    private int limiteApresentacoesSelecionadas, limitecomunicacaooralselecionadas,limiteiftech,limiterobotica;    
    private String avaliador;

    /**
     * @return the centralizado
     */
    public int getCentralizado() {
        return centralizado;
    }

    public String getCentralizadoTexto(){
    if( getCentralizado()==1)
        return "Centralizado";
    else
        return "No campi "+ getUnidadeNome();
    }
    
    
    /**
     * @param centralizado the centralizado to set
     */
    public void setCentralizado(int centralizado) {
        this.centralizado = centralizado;
    }

    /**
     * @return the unidadeCodigo
     */
    public int getUnidadeCodigo() {
        return unidadeCodigo;
    }

    /**
     * @param unidadeCodigo the unidadeCodigo to set
     */
    public void setUnidadeCodigo(int unidadeCodigo) {
        this.unidadeCodigo = unidadeCodigo;
    }

    /**
     * @return the avaliador
     */
    public String getAvaliador() {
        return avaliador;
    }

    /**
     * @param avaliador the avaliador to set
     */
    public void setAvaliador(String avaliador) {
        this.avaliador = avaliador;
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
     * @return the limiteApresentacoesSelecionadas
     */
    public int getLimiteApresentacoesSelecionadas() {
        return limiteApresentacoesSelecionadas;
    }

    /**
     * @param limiteApresentacoesSelecionadas the limiteApresentacoesSelecionadas to set
     */
    public void setLimiteApresentacoesSelecionadas(int limiteApresentacoesSelecionadas) {
        this.limiteApresentacoesSelecionadas = limiteApresentacoesSelecionadas;
    }

    /**
     * @return the limitecomunicacaooralselecionadas
     */
    public int getLimitecomunicacaooralselecionadas() {
        return limitecomunicacaooralselecionadas;
    }

    /**
     * @param limitecomunicacaooralselecionadas the limitecomunicacaooralselecionadas to set
     */
    public void setLimitecomunicacaooralselecionadas(int limitecomunicacaooralselecionadas) {
        this.limitecomunicacaooralselecionadas = limitecomunicacaooralselecionadas;
    }

    /**
     * @return the limiteiftech
     */
    public int getLimiteiftech() {
        return limiteiftech;
    }

    /**
     * @param limiteiftech the limiteiftech to set
     */
    public void setLimiteiftech(int limiteiftech) {
        this.limiteiftech = limiteiftech;
    }

    /**
     * @return the limiterobotica
     */
    public int getLimiterobotica() {
        return limiterobotica;
    }

    /**
     * @param limiterobotica the limiterobotica to set
     */
    public void setLimiterobotica(int limiterobotica) {
        this.limiterobotica = limiterobotica;
    }

    /**
     * @return the avaliadorNome
     */
    public String getAvaliadorNome() {
        return avaliadorNome;
    }

    /**
     * @param avaliadorNome the avaliadorNome to set
     */
    public void setAvaliadorNome(String avaliadorNome) {
        this.avaliadorNome = avaliadorNome;
    }
    
}
