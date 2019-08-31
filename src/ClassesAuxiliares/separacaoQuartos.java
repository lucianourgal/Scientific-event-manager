/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClassesAuxiliares;

import java.util.ArrayList;
import objetosBase.apresentador;
import objetosBase.autor;
import objetosBase.avaliador;
import objetosBase.triviais.evento;

/**
 *
 * @author SistemaIP
 */
public class separacaoQuartos {

    int[] numeroQuartos;

    ArrayList<autor> autores;
    ArrayList<apresentador> apresentadores;
    ArrayList<avaliador> avaliadores;

    // nome, idade, sexo, campus
    /*
     Categorias:
     1 - alunos menores   ( autores e apresentadores )
     2 - alunos maiores   ( autores e apresentadores )
     3 - servidores    ( avaliadores, membros organizadores, interpretes )
        
     */
    public separacaoQuartos(evento e) {

        numeroQuartos = new int[4];
        recuperarAlunos(e);
       // recuperarAvaliadores();
        //recuperarOutrosParticipantes();
        //criarObjetosParticipante();  desnecessário
        //ordenarListAlunos();
    }

    public void setNumeroQuartos(int quant, int capac) {

        numeroQuartos[capac] = quant;

    }

    public String[] getColunasTitulo() {
        String[] co = new String[6];

        co[0] = "Nome";
        co[1] = "CPF";
        co[2] = "Nascimento";
        co[3] = "Categoria";
        co[4] = "Sexo";
        co[5] = "Unidade";

        return co;
    }

    public String[][] getLinhas() {
        String[][] l = new String[apresentadores.size() + autores.size() + avaliadores.size()][6];
        int indice = 0;
        for (; indice < apresentadores.size(); indice++) {
            l[indice][0] = apresentadores.get(indice).getNome();
            l[indice][1] = apresentadores.get(indice).getCpf();
            l[indice][2] = apresentadores.get(indice).getDatadenascimento();
            l[indice][3] = "Apresentador";
            l[indice][4] = apresentadores.get(indice).getSexo();
            l[indice][5] = apresentadores.get(indice).getUnidade();
            System.out.println("Ind "+indice+" - Apresentador");
        }
        for(;indice<(apresentadores.size()+autores.size());indice++){
            l[indice][0] = autores.get(indice-apresentadores.size()).getNome();
            l[indice][1] = autores.get(indice-apresentadores.size()).getCpf();
            l[indice][2] = autores.get(indice-apresentadores.size()).getNascimento();
            l[indice][3] = "Apresentador";
            l[indice][4] = autores.get(indice-apresentadores.size()).getSexo();
            l[indice][5] = autores.get(indice-apresentadores.size()).getUnidadeNome();    
             System.out.println("Ind "+indice+" - Apresentador/aut");
        }
        for(;indice<(apresentadores.size()+autores.size()+avaliadores.size());indice++){
            l[indice][0] = avaliadores.get(indice-apresentadores.size()-autores.size()).getNome();
            l[indice][1] = avaliadores.get(indice-apresentadores.size()-autores.size()).getCpf();
            l[indice][2] = "";/*avaliadores.get(indice-apresentadores.size()-autores.size()).get*/
            l[indice][3] = "Avaliador";
            l[indice][4] = avaliadores.get(indice-apresentadores.size()-autores.size()).getSexo();
            l[indice][5] = avaliadores.get(indice-apresentadores.size()-autores.size()).getUnidadeNome();  
             System.out.println("Ind "+indice+" - Avaliador");
        }
        

        return l;
    }

    /*
     Recupera todos os apresentadores e alunos presentes
     */
    public void recuperarAlunos(evento ev) {
        autor n;
        autores = BancoDeDados.comando().recuperarAutoresAtivosDoCentralizado(ev);
        apresentadores = BancoDeDados.comando().recuperarApresentadoresEfetivosDoCentralizado(ev);
        System.out.println("Recuperou " + (autores.size() + apresentadores.size()) + " alunos, sendo " + autores.size() + " autores e " + apresentadores.size() + " apresentadores.");

       /* for (apresentador a : apresentadores) {
            n = new autor();
            n.setCpf(a.getCpf());
            n.setNome(a.getNome());
            n.setNascimento(a.getDatadenascimento());
            n.setUnidadeNome(a.getUnidade());
            n.setSexo(a.getSexo());
            autores.add(n);
        }*/

    }

    /*
     Recupera avaliadores
     */
   // public void recuperarAvaliadores() {

  //      avaliadores = BancoDeDados.comando().recuperarAvaliadoresPossiveisParaArea(-1, 1,-1,true);
  //      System.out.println("Recuperou " + avaliadores.size() + " avaliadores.");

  //  }

    private void ordenarListAlunos() {

        for (int a = 0; a < autores.size(); a++) {

            orderAut(a);

        }

    }

    private void orderAut(int a) {
        System.out.println("Ordenando autores por data de nascimento...");
        if (a < 0) {
            return;
        }

        if (Arrumador.comando().dataXdepoisDataY(autores.get(a).getNascimento(), autores.get(a + 1).getNascimento()) > 0) {
            autor aux = autores.get(a);
            autores.set(a, autores.get(a + 1));
            autores.set(a + 1, aux);

            orderAut(a - 1);
        }

    }

    public void gerarEscolhas() {
    //categoria, sexo, unidade, nascimento, 

    }

}
