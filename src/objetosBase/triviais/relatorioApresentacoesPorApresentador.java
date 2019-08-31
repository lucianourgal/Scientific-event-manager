/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package objetosBase.triviais;

import ClassesAuxiliares.BancoDeDados;
import java.util.ArrayList;
import objetosBase.apresentacao;
import objetosBase.autor;

/**
 *
 * @author SistemaIP
 */
public class relatorioApresentacoesPorApresentador {
    
    private ArrayList<String> cpf = new ArrayList<>();
    private ArrayList<String> nomes = new ArrayList<>();
    private ArrayList<String> apresentacoesDoCpf = new ArrayList<>();
    private final int centralizado;
    
    
    public String[] getColunaParaRelatorio(){
    String[]coluna=new String[2];
    coluna[0]="Aluno";
    coluna[1]="Apresentacoes";
    return coluna;
    }
    public String[][] getLinhasParaRelatorio(){
    String[][]linhas = new String[cpf.size()][2];
    int at=0;
    for(int a=0;a<cpf.size();a++){
        
        linhas[a+at][0]=cpf.get(a)+ " - "+nomes.get(a);
        linhas[a+at][1]=apresentacoesDoCpf.get(a);
        if(cpf.get(a).equals("n")){
        at=-1;
        }
    }
    
    return linhas;
    }
    
    
    
    public relatorioApresentacoesPorApresentador(int centralizad1o, evento e){
    centralizado = centralizad1o;
    BancoDeDados.comando().relatorioApresentacoesApresentador(getThis(),e);
    }
    
    
    public final relatorioApresentacoesPorApresentador getThis() {
        return this;
    }

    public void addApresentacao(apresentacao a){
    
        processarDados(a.getApresentadorEfetivoCPF(),a.getId(),a.getNometitulo(),"Apresentador", a.getApresentadorNome());
        
        if(a.getModalidadeCod()==4) // Cod de apresentação cultural
        for(autor ab: a.getAutores())
            if(ab.getAtivo()==1)    // se estiver ativo, ou seja, vai para o centralizado
            processarDados(ab.getCpf(),a.getId(),a.getNometitulo(),ab.getFuncaoNome(),ab.getNome());
    }
    
    
    /**
     * @return the cpf
     */
    public ArrayList<String> getCpf() {
        return cpf;
    }

    /**
     * @return the apresentacoesDoCpf
     */
    public ArrayList<String> getApresentacoesDoCpf() {
        return apresentacoesDoCpf;
    }

    private void processarDados(String apresentadorCpf, int id, String nometitulo, String cargo, String aprsNome) {
        boolean encontrou=false;
        String textoAAdd = "ID "+id+" - "+nometitulo+ "("+cargo+");";
        for(int a=0;a<cpf.size();a++){
            
//            if(cpf.get(a).equals("n"))
            
            if(cpf.get(a).equals(apresentadorCpf)){
            encontrou = true;
            apresentacoesDoCpf.set(a, apresentacoesDoCpf.get(a)+" "+textoAAdd);
            a=cpf.size();
            }
        }
        if(!encontrou){
            cpf.add(apresentadorCpf);
            apresentacoesDoCpf.add(textoAAdd);
            nomes.add(aprsNome);
        }

    }

    /**
     * @return the centralizado
     */
    public int getCentralizado() {
        return centralizado;
    }
    
    
}
