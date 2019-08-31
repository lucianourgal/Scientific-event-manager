/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package objetosBase.triviais;

import ClassesAuxiliares.BancoDeDados;
import java.util.Random;

/**
 *
 * @author SistemaIP
 */
public class ticket {
    
    private String codigo;
    private String tipo;
    private int unidade;
    private String unidadeNome;

    
    public void salvarNoBD(){
    
        int f = BancoDeDados.comando().insert("INSERT INTO ticket(codigo,tipo,unidade) VALUES ('"+codigo+"','"+tipo+"',"+unidade+");");
        
        if(f<1)
            System.out.println("FALHA: Não salvou ticket "+codigo+", "+tipo+", "+unidade);
        
    }
    
    
    
    public ticket(String tipo1, int unidade1){
    
        tipo = tipo1;
        unidade = unidade1;
        gerarCodigo();
    }
    
    public ticket(String codigo1, String tipo1, int unidade1){
    
        tipo = tipo1;
        unidade = unidade1;
        codigo = codigo1;
    }
    
    
    public void gerarCodigo() {

        StringBuffer sb = new StringBuffer();
        Random rand = new Random();
        for (int i = 0; i < 16; i++) {
            sb.append(Integer.toString(Math.abs(rand.nextInt()) % 16, 16));
        }
        codigo = sb.toString();
    }
    
    
    /**
     * @return the codigo
     */
    public String getCodigo() {
        return codigo;
    }

    /**
     * @param codigo the codigo to set
     */
    public void setCodigo(String codigo) {
        this.codigo = codigo;
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
     * @return the unidade
     */
    public int getUnidade() {
        return unidade;
    }

    /**
     * @param unidade the unidade to set
     */
    public void setUnidade(int unidade) {
        this.unidade = unidade;
    }

    public String getUnidadeNome() {
        return unidadeNome;
    }
    
    public void setUnidadeNome(String u){
        unidadeNome = u;
    }
    
    
    
}
