/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClassesAuxiliares;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import objetosBase.apresentacao;
import objetosBase.autor;
import objetosBase.avaliacao;
import objetosBase.avaliador;
import objetosBase.triviais.area;
import objetosBase.triviais.evento;

/**
 *
 * @author SistemaIP
 */
public final class GeradorPDF {

    
    private static GeradorPDF OBJETO;

    private final evento eventoAtual = BancoDeDados.comando().getEventoAtual();
    private String prefixoLink;
    private boolean incluirSubarea;
    private String prefixoNomeArq;
    private String cor = "";

    public static GeradorPDF comando() {
        if (OBJETO == null) {
            OBJETO = new GeradorPDF();
        }
        return OBJETO;
    }
    private ArrayList<area> areas = BancoDeDados.comando().getAreas();
    private final boolean imagemEmTodasPaginas = true;
    private final String charset = "utf-8";//"iso-8859-1" ou "utf-8;

    private boolean linkNoSumario = false;
    private boolean sumarioNegrito = false;

    public void gerarTodasFichasDeAvaliacao(String evento, boolean tudoJunto,boolean semprePar) {
        String t = "";
        //recupera todos os avaliadores do evento
        ArrayList<avaliador> avaliadores = BancoDeDados.comando().recuperarTodosAvaliadores(evento);

        //cria pasta para fichas (ou não)
        if (!tudoJunto) {
            Arrumador.comando().criaDiretorio("Fichas de Avaliação");
        } else {
            //iniciar cabeçalho HTML
            t = this.cabecalhoHtmlPadrao(false, 5) + this.transicaoHeadParaBodyHtml();

        }

        //gera conjunto de fichas para avaliador
        for (avaliador av : avaliadores) {
            if (tudoJunto) {
                t = t + gerarFichasAvaliador(av, evento, false,semprePar);
            } else {
                gerarFichasAvaliador(av, evento, true,semprePar);
            }
        }

        if (tudoJunto) {
            //incluir finalização HTML
            t = t + this.fechamentoHtmlPadrao();
            //salvar arquivo HTML gigante
            salvarEmHtml(t, "Fichas de Avaliação");
        }

    }

    public String gerarFichasAvaliador(avaliador avaliador1, String evento, boolean salvarArquivo,boolean semprePar) {
        String s = "";

        //incluir inicio de texto HTML
        if (salvarArquivo) {
            s = this.cabecalhoHtmlPadrao(false, 5) + this.transicaoHeadParaBodyHtml();

        }

        //recupera avaliações que esse avaliador tem a fazer  (avaliador, centralizado, apresentação, comOral?, inativasTambem, evento)
        ArrayList<avaliacao> avaliacoes = BancoDeDados.comando().recuperarAvaliacoes(avaliador1.getCpf(), 1, -1, false, true, evento);
        avaliacoes.addAll(BancoDeDados.comando().recuperarAvaliacoes(avaliador1.getCpf(), 1, -1, true, true, evento));

        //gera página de avaliação
        for (avaliacao av : avaliacoes) {
            s = s + gerarPaginaDeFicha(av);
        }

        if (salvarArquivo) {
            //incluir final de texto HTML
            s = s + this.fechamentoHtmlPadrao();
            //salvar arquivo
            salvarEmHtml(s, "Fichas de Avaliação/" + avaliador1.getUnidadeNome() + " _ " + avaliador1.getNome());
        } else if(semprePar && (avaliacoes.size() % 2 != 0) ) {
        //se for número impar de páginas e um arquivo só, colocar página em branco ao final(?)
            s = s + "<div style=\"page-break-after: always\"><br class=\"quebrapagina\">"+finalFichaAvaliacao(avaliacoes.get(0));
        }

        return s;
    }

    public String gerarPaginaDeFicha(avaliacao av) {
        String s, aux = "";

        //pula para próxima página
        s = "<div style=\"page-break-after: always\"><br class=\"quebrapagina\">";
        //inclui cabeçalho de página
        s = s + cabecalhoDeFichaDeAvaliacao(av);

        //inclui tabela
        aux = tabelaFichaAvaliacao(av);
        if (aux.length() < 3) {
            return "";
        } else {
            s = s + aux;
        }

        //inclui final de página
        s = s + finalFichaAvaliacao(av);

        return s;
    }

    public String cabecalhoDeFichaDeAvaliacao(avaliacao av) {
        String s;

        //imagem
        s = imagem() + "<br><br><br>";
        //evento, seção de posteres/comunicação oral/ IFTECH, categoria
        s = s + tituloCategoria(av);
        //avaliador. nome e id do trabalho; 
        s = s + "<br><br>Avaliador: <strong>" + av.getAvaliadorNome() + "</strong> ("+av.getUnidadeAvaliadorNome()+")";
        s = s + "<br>ID: <strong>" + av.getApresentacao() + "</strong>";
        s = s + "<br>Título: <strong>" + av.getApresentacaoNome() + "</strong> ("+av.getUnidadeApresentadorNome()+")<br>";

        return s;
    }

    public String tituloCategoria(avaliacao av) {
        String s = "<br>Avaliação de Trabalho - " + av.getCategoriaNome();
        String cat = av.getCategoriaNome();

        if (av.getOral() > 0) {
            s = "<br><center>Avaliação de Trabalho - Comunicação Oral</center>";
        } else if (cat.equals("Pesquisa/Inovação")) {
            s = "<br><center>Avaliação de Trabalho - Seção de Pôsteres</center>"
                    + "<br><center>PROJETO DE PESQUISA/INOVAÇÃO</center>";
        } else if (cat.equals("Extensão")) {
              s = "<br><center>Avaliação de Trabalho - Seção de Pôsteres</center>"
                    + "<br><center>PROJETO DE EXTENSÃO</center>";
        } else if (cat.equals("Ensino")) {
              s = "<br><center>Avaliação de Trabalho - Seção de Pôsteres</center>"
                    + "<br><center>PROJETO DE ENSINO</center>";
        } else if (cat.equals("Robótica")) {

        //Não existe ficha para Robótica
        } else if (cat.equals("IFTECH")) {
              s = "<br><center>Avaliação de Trabalho</center>"
                    + "<br><center>PROJETO DE INOVAÇÃO - IFTECH</center>";
        } else if (cat.equals("Apresentação Cultural")) {

        //Não existe ficha para apresentação cultural
        } else {
            System.out.println("ALERTA: '" + cat + "' não tem ação prevista para Tabela de ficha de avaliação");
        }

        return s;
    }

    /*<TABLE BORDER=1> <! Inicia a tabela e coloca uma borda de espessura igual a 1>
     <TR> <! Cria a primeira linha da tabela>
     <TD>PRIMEIRA COLUNA </TD> <! Aqui foi criada uma célula>
     <TD>SEGUNDA COLUNA </TD>
     <TD>TERCEIRA COLUNA </TD>
     </TR> <! Fecha a primeira linha da tabela>
     <TR> <! Abre a segunda linha da tabela>
     <TD> PRIMEIRA COLUNA</TD>
     <TD>SEGUNDA COLUNA </TD>
     <TD>TERCEIRA COLUNA </TD>
     <TR> <! Encerra a Segunda linha da tabela>
     </TABLE> <! Encerra a tabela>*/
    public String comecaTabela() {
        //azul escuro: #283593. Vermelho vivo: #e53935. Roxo: #6a1b9a.  Verde #388e3c. Laranja: #f57c00.  Magenta: #e64a19. Cinza: #455a64

        return "<br><br><TABLE BORDER=1>" + linhaColoridaNaTabela("<strong>Critérios avaliados</strong>", "<strong>Valor</strong>", "<strong>Nota_atribuída</strong>", true, cor);
    }

    public String linhaNaTabela(String a, String b, String c) {
        return "<TR><TD>" + a + "<TD><center>" + b + "</center><TD><center>" + c + "</center></TR>";
    }

    public String linhaColoridaNaTabela(String a, String b, String c, boolean colorirTodas, String anyColorYouLike) {
        if (colorirTodas) {
            return "<TR><TD bgcolor=\"" + anyColorYouLike + "\">" + a + "<TD bgcolor=\"" + anyColorYouLike + "\"><center>" + b + "</center><TD bgcolor=\"" + anyColorYouLike + "\"><center>" + c + "</center></TR>";
        } else {
            return "<TR><TD bgcolor=\"" + anyColorYouLike + "\">" + a + "<TD bgcolor=\"" + anyColorYouLike + "\"><center>" + b + "</center><TD><center>" + c + "</center></TR>";
        }
    }

    public String terminaTabela() {
        return linhaColoridaNaTabela("<strong>Total</strong>", "<strong>10,00</strong>", textoNota(), false, cor) + ""
                + "</TABLE><p align=\"Right\">Utilizar duas casas decimais</p>";
    }

    public String textoNota() {
        return "___,___ ___";
    }

    public String tabelaFichaAvaliacao(avaliacao av) {
        //Pesquisa/Inovação  Extensão  Ensino Robótica  IFTECH  Apresentação Cultural
        String s = "";
        String cat = av.getCategoriaNome();

     //azul escuro: #283593. Vermelho vivo: #e53935. Roxo: #6a1b9a.  Verde #388e3c. Laranja: #f57c00.  Magenta: #e64a19. Cinza: #455a64
        if (av.getOral() > 0) {

            cor = "#e53935"; // vermelho vivo
            s = comecaTabela();
            s += linhaNaTabela("A questão abordada no projeto está evidenciada corretamente?", "2,00", textoNota());
            s += linhaNaTabela("O trabalho possui relevância, apresentando resultados que tenham aplicabilidade e contribuição direta ou indireta para a sociedade?", "2,00", textoNota());
            s += linhaNaTabela("O trabalho apresenta resultados consistentes e relacionados à metodologia e objetivos?", "2,00", textoNota());
            s += linhaNaTabela("O aluno apresentou boa desenvoltura e demonstrou conhecimento do assunto durante a apresentação do trabalho?", "2,00", textoNota());
            s += linhaNaTabela("Os slides utilizados para a apresentação foram bem elaborados?", "2,00", textoNota());
            s = s + terminaTabela();

        } else if (cat.equals("Pesquisa/Inovação")) {

            cor = "#388e3c"; // verde
            s = comecaTabela();
            s += linhaNaTabela("A questão abordada no projeto está evidenciada corretamente?", "1,00", textoNota());
            s += linhaNaTabela("Existe aplicação de metodologia científica?", "1,00", textoNota());
            s += linhaNaTabela("O trabalho apresenta objetivos que podem ser alcançados de acordo com a metodologia proposta?", "1,00", textoNota());
            s += linhaNaTabela("Os resultados estão relacionados aos objetivos propostos?", "2,00", textoNota());
            s += linhaNaTabela("O trabalho possui relevância científica, apresentando resultados que tenham aplicabilidade e contribuição direta ou indireta para a sociedade?", "2,00", textoNota());
            s += linhaNaTabela("O aluno apresentou boa desenvoltura e demonstrou conhecimento do assunto durante a apresentação do trabalho?", "2,00", textoNota());
            s += linhaNaTabela("O pôster foi bem elaborado? (aspectos visuais, diagramação, qualidade das imagens e do texto)", "1,00", textoNota());
            s = s + terminaTabela();

        } else if (cat.equals("Extensão")) {

            cor = "#283593"; // azul escuro
            s = comecaTabela();
            s += linhaNaTabela("A questão abordada no projeto está evidenciada corretamente?", "1,00", textoNota());
            s += linhaNaTabela("O trabalho apresentou aspectos relacionados à interdisciplinaridade, diálogo entre diferentes saberes, parceria com comunidade externa?", "2,00", textoNota());
            s += linhaNaTabela("O trabalho apresentou objetivos que podem ser alcançados de acordo com a metodologia proposta?", "1,00", textoNota());
            s += linhaNaTabela("Os resultados foram relacionados aos objetivos propostos?", "1,00", textoNota());
            s += linhaNaTabela("O trabalho possui resultados com potencial de provocar mudanças sociais, econômicas, políticas e culturais?", "1,00", textoNota());
            s += linhaNaTabela("O trabalho possui resultados e ações que atenderam às questões, demandas ou problemas concretos da sociedade?", "1,00", textoNota());
            s += linhaNaTabela("O aluno apresentou boa desenvoltura e demonstrou conhecimento do assunto durante a apresentação do trabalho?", "2,00", textoNota());
            s += linhaNaTabela("O pôster foi bem elaborado? (aspectos visuais, diagramação, qualidade das imagens e do texto)", "1,00", textoNota());
            s = s + terminaTabela();

        } else if (cat.equals("Ensino")) {

            cor = "#e64a19"; //magenta
            s = comecaTabela();
            s += linhaNaTabela("A questão abordada no projeto está evidenciada corretamente?", "1,00", textoNota());
            s += linhaNaTabela("O trabalho contribuiu para a formação dos alunos envolvidos, articulando diferentes conhecimentos ao longo de sua execução?", "1,00", textoNota());
            s += linhaNaTabela("O trabalho apresentou objetivos que podem ser alcançados de acordo com a metodologia proposta?", "1,00", textoNota());
            s += linhaNaTabela("As ações desenvolvidas no projeto permitem ressignificar práticas de ensino e de aprendizagem?", "2,00", textoNota());
            s += linhaNaTabela("O trabalho apresentou resultados e ações que contribuem direta ou indiretamente na sociedade?", "2,00", textoNota());
            s += linhaNaTabela("O aluno apresentou boa desenvoltura e demonstra conhecimento do assunto durante a apresentação do trabalho?", "2,00", textoNota());
            s += linhaNaTabela("O pôster está bem elaborado? (aspectos visuais, diagramação, qualidade das imagens e do texto)", "1,00", textoNota());
            s = s + terminaTabela();

        } else if (cat.equals("Robótica")) {

        //Não existe ficha para Robótica
        } else if (cat.equals("IFTECH")) {

            cor = "#6a1b9a"; // roxo
            s = comecaTabela();
            s += linhaNaTabela("O trabalho desenvolvido demonstra originalidade e/ou inovação?", "4,00", textoNota());
            s += linhaNaTabela("O projeto desenvolvido tem potencial para ajudar a transformar positivamente a realidade da comunidade local?", "1,00", textoNota());
            s += linhaNaTabela("O projeto preza pela sustentabilidade e pela responsabilidade social?", "1,00", textoNota());
            s += linhaNaTabela("Os objetivos estão apresentados de forma clara?", "1,00", textoNota());
            s += linhaNaTabela("Os objetivos e os resultados são coerentes com a metodologia proposta?", "1,00", textoNota());
            s += linhaNaTabela("O projeto oportuniza a iniciação do estudante em práticas próprias de processos de inovação durante o desenvolvimento do projeto?", "1,00", textoNota());
            s += linhaNaTabela("Desempenho do aluno durante a explicação sobre o funcionamento/aplicabilidade do protótipo/produto.", "1,00", textoNota());
            s = s + terminaTabela();

        } else if (cat.equals("Apresentação Cultural")) {

        //Não existe ficha para apresentação cultural
        } else {
            System.out.println("ALERTA: '" + cat + "' não tem ação prevista para Tabela de ficha de avaliação");
        }

        return s;
    }

    public String finalFichaAvaliacao(avaliacao av) {
        if (av.getCategoriaNome().equals("IFTECH")) {
            return "<br>Você indicaria esse trabalho para a FEBRACE? (Feira Brasileira de Ciências e Engenharia)<br>"
                    + "(   ) SIM                                 (    ) NÃO"
                    + "<br>Observações:"
                    + "<br><br>_________________________________________________________________________________________________________"
                    + "<br><br>_________________________________________________________________________________________________________"
                    + "<br><br>_________________________________________________________________________________________________________"                   
                    + "<br><br>_________________________________________________________________________________________________________<br></div>";
        }

        return "<br>Observações:"
                + "<br><br>_________________________________________________________________________________________________________"
                + "<br><br>_________________________________________________________________________________________________________"
                + "<br><br>_________________________________________________________________________________________________________"
                + "<br><br>_________________________________________________________________________________________________________"
                + "<br><br>_________________________________________________________________________________________________________"
                + "<br><br>_________________________________________________________________________________________________________<br></div>";
    }

  

    public boolean gerarHTMLUnicoDoc(ArrayList<apresentacao> ap, String nome, int pagCapa, evento event) {

        /*
         Pular linha: <br>
         Paragrafo:  <p>
         Centralizar:{ <center> </center>
         Negrito:    <strong> </strong>
         */
        String texto;

        // Inicia pelo cabeçalho
        texto = cabecalhoHtmlPadrao(true, 4);

        //adiciona Indices
        if (sumarioNegrito) {
            texto = texto + "<strong>";
        }
        texto += indicesHtmlPadrao(ap, pagCapa, true);
        if (sumarioNegrito) {
            texto = texto + "</strong>";
        }

        //termina indices e prepara para adicionar corpo
        texto += transicaoHeadParaBodyHtml();

        //Laço das apresentações
        for (apresentacao a : ap) {
           // if (a.getModalidadeCod() < 4) { //não pode ser cultural nem robótica, pois esses não tem resumo
                texto += "<nextpage><br>";
                texto += textoHtmlPadraoApresentacao(a,event);
           // }
        }

        // Códigos para fechar o documento
        texto += fechamentoHtmlPadrao();

        // Save document
        return salvarEmHtml(texto, nome + "_SingleFile");

    }

    public boolean gerarArquivos(ArrayList<apresentacao> ap, String nome, int pagCapa, evento e) {

        prefixoNomeArq = nome;

        return (gerarHTMLDocsIndividuais(ap, nome, pagCapa, e) && gerarHTMLUnicoDoc(ap, nome, pagCapa,e));

    }

    public boolean gerarHTMLDocsIndividuais(ArrayList<apresentacao> ap, String nome, int pagCapa, evento event) {

        /*
         Pular linha: <br>
         Paragrafo:  <p>
         Centralizar:{ <center> </center>
         Negrito:    <strong> </strong>
         */
        String texto;

        // Inicia pelo cabeçalho
        texto = cabecalhoHtmlPadrao(true, 4);

        //adiciona Indices
        texto += indicesHtmlPadrao(ap, pagCapa, false);

        //termina indices e prepara para adicionar corpo
        texto += transicaoHeadParaBodyHtml();
        // Códigos para fechar o documento
        texto += fechamentoHtmlPadrao();

        // Save document
        salvarEmHtml(texto, "ArquivosAnaisIndividuais/" + nome + "_Indices");

        //Laço das apresentações
        for (apresentacao a : ap) {

            // Inicia pelo cabeçalho
            texto = cabecalhoHtmlPadrao(true, 4);
            //termina indices e prepara para adicionar corpo
            texto += transicaoHeadParaBodyHtml();

            // texto padrão da apresentação
            texto += textoHtmlPadraoApresentacao(a,event);

            // Códigos para fechar o documento
            texto += fechamentoHtmlPadrao();

            // Save document
            salvarEmHtml(texto, "ArquivosAnaisIndividuais/" + nome + "_Apresent_" + a.getId());
        }

        return true;
    }

    public String transicaoHeadParaBodyHtml() {
        return "</head><body>";
    }

    public String indicesHtmlPadrao(ArrayList<apresentacao> aps, int pagCapa, boolean arquivoUnico) {
        String texto = "";
        int redutor = 0;

        if (arquivoUnico) {
            texto = "<div style=\"page-break-after: always\">";
        } else {
            Arrumador.comando().criaDiretorio("ArquivosAnaisIndividuais");
        }

        for (int x = 0; x < aps.size(); x++) {

            /*if (aps.get(x).getModalidadeCod() > 3) {

                redutor++;

            } else*/ if (arquivoUnico) {
                if (linkNoSumario) {
                    texto += "<br><a href=\"#arc" + aps.get(x).getId() /*  page=" + ((x-redutor) + 0+ pagCapa + Math.round(aps.size() / 52)) */ + "\">Página " + ((x - redutor) + 0 + pagCapa + Math.round(aps.size() / 52)) + ":"
                            + " " + aps.get(x).getNometitulo() + "</a> "
                            + "(  ID " + aps.get(x).getId() + " ) ";
                } else {
                    texto += "<br>Página " + ((x - redutor) + 0 + pagCapa + Math.round(aps.size() / 52)) + ":"
                            + " " + aps.get(x).getNometitulo() + " (  ID " + aps.get(x).getId() + " )";
                }
            } else {
                texto += "<br><a href=\"" + prefixoLink + prefixoNomeArq + "_Apresent_" + aps.get(x).getId() + ".html\">Pagina " + ((x - redutor) + 0 + pagCapa + Math.round(aps.size() / 40)) + ":"
                        + " " + aps.get(x).getNometitulo() + "</a> "
                        + "(  ID " + aps.get(x).getId() + " ) ";
            }
        }

        if (arquivoUnico) {
            texto = texto + "</div>";
        }

        return texto;
    }

    public String fechamentoHtmlPadrao() {
        return "</font></body></html>";
    }

    public String imagem() {
         return "<img src=\"lib/cabecSEPIN.png\">";
        //return "<img src=\"http://sepin.ifpr.edu.br/wp-content/uploads/2015/08/banner-topo-e1440769515510.png\">";
    }

    public String cabecalhoHtmlPadrao(boolean usarImagem, int fonte) {
        String texto = "<!doctype html><charset=" + charset + "><head>"
                + "<style type=\"text/css\">\n"
                + ".quebrapagina {\n"
                + "   page-break-before: always;\n"
                + " page-break-inside: avoid;"
                + "}\n"
                + "</style>"
                //     + "<title>Arquivo de Anais do seminário</title>"
                + "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=" + charset + "\">";

        if (usarImagem) {
            texto = texto + " "+imagem();
        }

        texto = texto + " <font size =\"" + fonte + "\">";
        return texto;
    }

    public String textoHtmlPadraoApresentacao(apresentacao a, evento event) {
        String texto = "";
        // evento, ISSN, modalidade, ID
        texto += "<div id=\"arc" + a.getId() + "\" style=\"page-break-after: always\"><br class=\"quebrapagina\">";

        if (imagemEmTodasPaginas) {
            texto += imagem();
        }

        texto += "<br><br><p>" + a.getEventoNome() + " - " + event.getData() + " de " + event.getAno() + ", " + event.getLocal() + " - ISSN: 2358-6869" +/* a.getEventoCod() + */ "  <br>" + a.getModalidadeNome() + " ID " + a.getId();
        // nome, area, subarea
        if (incluirSubarea) {
            texto += "<strong><br><br><a name=\"ar" + a.getId() + "\">" + a.getNometitulo() + "</a> </strong>( " + nomeArea(a.getAreaParaSelecaoAvaliadores()) + ", " + a.getSubareaNome() + " )";
        } else {
            texto += "<strong><br><br><a name=\"#" + a.getId() + "\">" + a.getNometitulo() + "</a> </strong>( " + nomeArea(a.getAreaParaSelecaoAvaliadores()) + " )";
        }

        // apresentador
        if (a.getApresentadorEfetivoID() == -1) {
            texto += "<br><u>" + a.getApresentadorEfetivoNome() + "</u> " + a.getApresentadorEmailAnais() + "- " + a.getUnidadeNome();
        } else {
            texto += "<br>" + a.getApresentadorEfetivoNome() + " " + a.getApresentadorEmailAnais() + "- " + a.getUnidadeNome();
        }

        //pula duas linhas para outros autores                               
        texto += "<br>";
        for (int x=0;x<a.getAutores().size();x++) {
          autor autz = a.getAutores().get(x);  
            if (autz.getFuncaoCod() != 2 && !autz.getNome().toUpperCase().equals("NÃO HÁ") 
                    && !autz.getNome().equals(a.getApresentadorNome()) && !nomeDuplicadoParaFrenteNoVetor(x,a.getAutores())) //se nao for o orientador
            { //se não for orientador, se não for "NÃO HÁ", se não repetido com apresentador, se não aparece esse mesmo nome para frente no vetor
                if (a.getApresentadorEfetivoID() == autz.getId()) //se for o apresentador efetivo
                {
                    texto += "<u>" + autz.getNome() + "</u> " + autz.getEmailAnais() + "- " + autz.getUnidadeNome() + ";   ";
                } else //se for um comum
                {
                    texto += "" + autz.getNome() + " " + autz.getEmailAnais() + "- " + autz.getUnidadeNome() + ";   ";
                }
            }
        }

        for (autor aut : a.getAutores()) {
            if (aut.getFuncaoCod() == 2) //se for o orientador (Ele sempre fica por último)
            {
                if (a.getApresentadorEfetivoID() == aut.getId()) //se for o apresentador efetivo
                {
                    texto += "<u>" + aut.getNome() + "</u> " + aut.getEmailAnais() + "- " + aut.getUnidadeNome() + ";   ";
                } else //se for um comum
                {
                    texto += "" + aut.getNome() + " " + aut.getEmailAnais() + "- " + aut.getUnidadeNome() + ";   ";
                }
            }
        }

               // texto = texto.substring(0, texto.length()-3);
        //inserir resumo
        texto += "</p><br><br><p align=\"justify\">" + a.getResumo();
        texto += "<br><br><strong>Palavras Chave:</strong> ";
        texto += a.getPalavrasChave() + "</p></div>";

        return texto;
    }

  private boolean existeOrientadorComEsseNome(String nome, ArrayList<autor> autores){
  
  for(autor at: autores)
      if(at.getNome().equals(nome) && at.getFuncaoCod()==2)
          return true;
  
  return false;
  }
  
  private boolean nomeDuplicadoParaFrenteNoVetor(int pos, ArrayList<autor> autores){
  
      for(int x=(pos+1);x<autores.size();x++)
          if(autores.get(x).getNome().equals(autores.get(pos).getNome()))
                  return true;
  
      return false;
  }

 
   
    private boolean salvarEmHtml(String content, String fileName) {
        try {
            File file = new File(fileName + ".html");

            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(content);
            bw.close();
            // j.salvou(true);
            System.out.println("Salvou " + fileName + ".html");

        } catch (IOException e) {
            //j.salvou(false);
            System.out.println("Falha para" + fileName + ".html");
          //  e.printStackTrace();
            return false;

        }
        return true;
    }

    /**
     * @param prefixoLink the prefixoLink to set
     */
    public void setPrefixoLink(String prefixoLink) {
        this.prefixoLink = prefixoLink;
    }

    /**
     * @param incluirSubarea the incluirSubarea to set
     */
    public void setIncluirSubarea(boolean incluirSubarea) {
        this.incluirSubarea = incluirSubarea;
    }

    private String nomeArea(int id) {

        for (area a : areas) {
            if (a.getId() == id) {
                return a.getNome();
            }
        }

        return "-";
    }

}
