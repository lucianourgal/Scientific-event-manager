/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClassesAuxiliares;

/**
 *
 * @author SistemaIP
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import objetosBase.apresentacao;
import objetosBase.apresentador;
import objetosBase.autor;
import objetosBase.avaliacao;
import objetosBase.avaliador;
import objetosBase.certificado;
import objetosBase.oficinaSessaoMonitor;
import objetosBase.triviais.bolsa;
import objetosBase.triviais.coordenador;
import objetosBase.triviais.evento;
import objetosBase.triviais.ticket;
import objetosBase.triviais.tipopessoa;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

// http://stackoverflow.com/questions/12286662/how-to-apply-bold-text-style-for-an-entire-row-using-apache-poi
public class MysqlToExcel {

    ArrayList<bolsa> bolsas = BancoDeDados.comando().getBolsas();
    ArrayList<tipopessoa> tipopessoas = BancoDeDados.comando().getTiposPessoas();

    public boolean gerarPlanilha(evento event, String urlArquivo, String order) {
//TODOS OS INSCRITOS: CPF, Nome, email, telefone, vinculo, campus, eh avaliador?
//BOLSISTAS e CULTURAL: Mesma coisa, mas MENOS avaliador e MAIS bolsa
//667 TRABALHOS: Id, modalidade, titulo, comu oral? area
//APRESENTACOES CULTURAIS: id, titulo, nome do grupo
//AVALIADORES: CPF, Nome, email, telefone, vinculo, titulacao, area, curso de formação, trabalhos para avaliar
//CONFERENCIA TRABALHOS: id, titulo, comun oral?, nome apresentador, bolsa, campus, selecionado?
//PIBIC-JR: id, titulo, comun oral?, nome apresent, vinculo, bolsa, campus, selecionado
//PIBIC: mesmo PIBIC-JR
//PIBIN: mesmo PIBIC-JR
//EXTENSAO: mesmo PIBIC-JR
//TRABALHOS POR CAMPUS: campus/instituicao, id, titulo, participantes e funçoes destes
//AVALIACOES: ID, TITULO, NOTA, AVALIADOR

        ArrayList<avaliacao> avaliacoes = BancoDeDados.comando().recuperarAvaliacoes("aa", 1, -1, false, true, event.getId());
        System.out.println("Total de " + avaliacoes.size() + " avaliacoes.");
        ArrayList<apresentacao> apresentacoes = BancoDeDados.comando().recApresentacoesParaGerarAnaisDoSeminario(event.getId(), -1, -1, order, -1);  //Recupera dados  (unico para cada sheet)    
        ArrayList<apresentador> apresentadores = BancoDeDados.comando().recuperarApresentadoresEfetivosDoCentralizado(event);  //Recupera dados  (unico para cada sheet)
        ArrayList<avaliador> avaliadores = BancoDeDados.comando().recuperarAvaliadoresPossiveisParaArea(-1, 1, -1, true, event.getId()); //avaliadores marcados como acompanhantes, que irão/estão no evento.
        geradorCertificados geradorCertificados = new geradorCertificados();
        ArrayList<certificado> certificados = BancoDeDados.comando().getCertificados(geradorCertificados, false, false, event.getId());
        int[] apresentacaoAusente = new int[apresentacoes.size()];
        for (int a = 0; a < apresentacoes.size(); a++) {
            apresentacaoAusente[a] = -1;
        }

        for (apresentador x : apresentadores) {
            x.setBolsaNome(recNomeB(x.getBolsaCod()));
            x.setVinculo(recTipoPessoa(x.getTipoPessoa()));
        }

        System.out.println("-------------------\n"
                + "Apresentadores: " + apresentadores.size() + "; Apresentacoes: " + apresentacoes.size() + "; Avaliadores: " + avaliadores.size() + ";\n"
                        + "-------------\n");

        HSSFWorkbook workbook = new HSSFWorkbook(); //inicia planilha

//667 trabalhos
//id, modalidade, titulo, comunicao oral, area, avaliadores
        HSSFSheet sheet = workbook.createSheet("Apresentacoes"); //Cria nova sheet 
        ArrayList<avaliacao> avaliacoes1, avaliacoes2;
        String avalcu = "", avalcu2 = "";
        Map<String, Object[]> data = new LinkedHashMap<String, Object[]>();  //Monta hashMap 
        data.put("1", new Object[]{"ID", "CATEGORIA", "MODALIDADE", "TITULO", "AREA", "RESUMO", "AVALIADORES", "AVALIADORES COM ORAL"});   //Rotulo  (unico para cada sheet)
        int cont = 2;  //Linha que inicia
        String co;
        for (apresentacao ax : apresentacoes) {
            avaliacoes2 = BancoDeDados.comando().recuperarAvaliacoes("aa", 1, ax.getId(), false, false, event.getId());
            avalcu = "";
            co = ax.getId() + "";
            for (avaliacao avsc : avaliacoes2) {
                avalcu = avalcu + "; " + avsc.getAvaliadorNome() + "(" + avsc.getAvaliador() + ")";
            }

            avaliacoes1 = BancoDeDados.comando().recuperarAvaliacoes("aa", 1, ax.getId(), true, false, event.getId());
            avalcu2 = "";
            for (avaliacao avsc : avaliacoes1) {
                avalcu2 = avalcu2 + "; " + avsc.getAvaliadorNome() + "(" + avsc.getAvaliador() + ")";
            }

            data.put("" + cont++, new Object[]{co, ax.getModalidadeNome(), ax.getCategoriaNome(), ax.getNometitulo(), ax.getAreaNome(), ax.getResumo(), avalcu, avalcu2});  //preenche uma linha   (unico para cada sheet)
        }

        Set<String> keyset = data.keySet();
        int rownum = 1;
        for (String key : keyset) {
            Row row1 = sheet.createRow(rownum++); //cria linha
            Object[] objArr = data.get(key);
            int cellnum = 0;
            for (Object obj : objArr) {
                Cell cell1 = row1.createCell(cellnum++); //adiciona uma célula na linha
                if (obj instanceof Date) {
                    cell1.setCellValue((Date) obj);
                } else if (obj instanceof Boolean) {
                    cell1.setCellValue((Boolean) obj);
                } else if (obj instanceof String) {
                    cell1.setCellValue((String) obj);
                } else if (obj instanceof Double) {
                    cell1.setCellValue((Double) obj);
                } else if (obj instanceof Integer) {
                    cell1.setCellValue((Integer) obj);
                }
            }
        }
        for (int t = 1; t < 4; t++) //deixa colunas ajustadas
        {
            sheet.autoSizeColumn(t);
        }

//TODOS OS INSCRITOS: CPF, Nome, , sexo, email, telefone, vinculo, campus, eh avaliador?
/*HSSFSheet*/ sheet = workbook.createSheet("Todos os inscritos"); //Cria nova sheet 
/*Map<String, Object[]>*/ data = new LinkedHashMap<String, Object[]>();  //Monta hashMap 
        data.put("1", new Object[]{"CPF","RG","NOME", "SEXO", "EMAIL", 
            "TELEFONE", "VINCULO", "CAMPUS", "AVALIADOR?", "OBSERVACOES"});   //Rotulo  (unico para cada sheet)
        cont = 2;  //Linha que inicia
        for (apresentador ax : apresentadores) {
            //co = ax.getId()+"";
            data.put("" + cont++, new Object[]{ax.getCpf(), ax.getRg()+" "+ax.getOrgaoexpedidorrg()+" "+ax.getEstadorg() ,ax.getNome(), ax.getSexo(), ax.getEmail(),
                ax.getTelefone(), ax.getEstadonaturalidade(), ax.getUnidade(), "Não", ax.getObservacoes()});  //preenche uma linha   (unico para cada sheet)
        }
        for (avaliador av : avaliadores) {
            data.put("" + cont++, new Object[]{av.getCpf(), av.getRg() ,av.getNome(), av.getSexo(), av.getEmail(), av.getTelefone(), av.getTipoPessoaNome(), av.getUnidadeNome(), "Sim", av.getObservacoes()});  //preenche uma linha   (unico para cada sheet)
        }
        /*Set<String> */
        keyset = data.keySet();
        rownum = 1;
        for (String key : keyset) {
            Row row1 = sheet.createRow(rownum++); //cria linha
            Object[] objArr = data.get(key);
            int cellnum = 0;
            for (Object obj : objArr) {
                Cell cell1 = row1.createCell(cellnum++); //adiciona uma célula na linha
                if (obj instanceof Date) {
                    cell1.setCellValue((Date) obj);
                } else if (obj instanceof Boolean) {
                    cell1.setCellValue((Boolean) obj);
                } else if (obj instanceof String) {
                    cell1.setCellValue((String) obj);
                } else if (obj instanceof Double) {
                    cell1.setCellValue((Double) obj);
                } else if (obj instanceof Integer) {
                    cell1.setCellValue((Integer) obj);
                }
            }
        }
        for (int t = 0; t < 7; t++) //deixa colunas ajustadas
        {
            sheet.autoSizeColumn(t);
        }

//BOLSISTAS e CULTURAL: CPF, Nome, email, telefone, vinculo, campus, BOLSA
/*HSSFSheet*/ sheet = workbook.createSheet("Bolsistas"); //Cria nova sheet 

        /*Map<String, Object[]>*/ data = new LinkedHashMap<String, Object[]>();  //Monta hashMap 
        data.put("1", new Object[]{"CPF", "NOME", "SEXO", "EMAIL", "TELEFONE", "VINCULO", "CAMPUS", "BOLSA"});   //Rotulo  (unico para cada sheet)
        cont = 2;  //Linha que inicia
        for (apresentador ax : apresentadores) {
            data.put("" + cont++, new Object[]{ax.getCpf(), ax.getNome(), ax.getSexo(), ax.getEmail(), ax.getTelefone(), ax.getVinculo(), ax.getUnidade(), ax.getBolsaNome()});  //preenche uma linha   (unico para cada sheet)
        }

        /*Set<String> */        keyset = data.keySet();
        rownum = 1;
        for (String key : keyset) {
            Row row1 = sheet.createRow(rownum++); //cria linha
            Object[] objArr = data.get(key);
            int cellnum = 0;
            for (Object obj : objArr) {
                Cell cell1 = row1.createCell(cellnum++); //adiciona uma célula na linha
                if (obj instanceof Date) {
                    cell1.setCellValue((Date) obj);
                } else if (obj instanceof Boolean) {
                    cell1.setCellValue((Boolean) obj);
                } else if (obj instanceof String) {
                    cell1.setCellValue((String) obj);
                } else if (obj instanceof Double) {
                    cell1.setCellValue((Double) obj);
                } else if (obj instanceof Integer) {
                    cell1.setCellValue((Integer) obj);
                }
            }
        }
        for (int t = 0; t < 7; t++) //deixa colunas ajustadas
        {
            sheet.autoSizeColumn(t);
        }

//APRESENTACOES CULTURAIS: id, titulo, nome do grupo
//Não utilizado
//AVALIADORES: CPF, Nome, email, telefone, vinculo, titulacao, area, curso de formação, trabalhos para avaliar
/*HSSFSheet*/ sheet = workbook.createSheet("Avaliadores"); //Cria nova sheet 
/*Map<String, Object[]>*/ data = new LinkedHashMap<String, Object[]>();  //Monta hashMap 
        data.put("1", new Object[]{"CPF", "ACOMPANHANTE", "NOME", "SEXO", "UNIDADE", "EMAIL", "TELEFONE", "VINCULO", "TITULACAO", "AREA", "FORMAÇÂO", "TRABALHOS A AVALIAR"});   //Rotulo  (unico para cada sheet)
        cont = 2;  //Linha que inicia
        for (avaliador av : avaliadores) {
            data.put("" + cont++, new Object[]{av.getCpf(), av.getAcompanhanteText(), av.getNome(), av.getSexo(), av.getUnidadeNome(), av.getEmail(), av.getTelefone(), av.getTipoPessoaNome(), av.getTitulacaoNome(), av.listaAreasAvalia(), av.getFormacao(), av.getListaAvaliacoesPossiveis()});  //preenche uma linha   (unico para cada sheet)
        }
        /*Set<String> */
        keyset = data.keySet();
        rownum = 1;
        for (String key : keyset) {
            Row row1 = sheet.createRow(rownum++); //cria linha
            Object[] objArr = data.get(key);
            int cellnum = 0;
            for (Object obj : objArr) {
                Cell cell1 = row1.createCell(cellnum++); //adiciona uma célula na linha
                if (obj instanceof Date) {
                    cell1.setCellValue((Date) obj);
                } else if (obj instanceof Boolean) {
                    cell1.setCellValue((Boolean) obj);
                } else if (obj instanceof String) {
                    cell1.setCellValue((String) obj);
                } else if (obj instanceof Double) {
                    cell1.setCellValue((Double) obj);
                } else if (obj instanceof Integer) {
                    cell1.setCellValue((Integer) obj);
                }
            }
        }
        for (int t = 0; t < 9; t++) //deixa colunas ajustadas
        {
            sheet.autoSizeColumn(t);
        }

//CONFERENCIA TRABALHOS: id, titulo, comun oral?, nome apresentador, bolsa, campus, selecionado?
/*HSSFSheet*/ sheet = workbook.createSheet("Conferência trabalhos"); //Cria nova sheet 
/*Map<String, Object[]>*/ data = new LinkedHashMap<String, Object[]>();  //Monta hashMap 
        data.put("1", new Object[]{"ID", "TITULO", "MODALIDADE", "APRESENTADOR", "BOLSA", "CAMPUS", "INCLUSAO?" , "DIVERSIDADE?",  "SELECIONADO?"});   //Rotulo  (unico para cada sheet)
        cont = 2;  //Linha que inicia
        for (apresentacao a : apresentacoes) {
            data.put("" + cont++, new Object[]{a.getId(), a.getNometitulo(), a.getModalidadeNome(), a.getApresentadorEfetivoNome(), a.getBolsaApresentadorNome(), a.getUnidadeNome(), a.getInclusao(), a.getDiversidade(), a.getSelecionadoTexto()});  //preenche uma linha   (unico para cada sheet)
        }
        /*Set<String> */
        keyset = data.keySet();
        rownum = 1;
        for (String key : keyset) {
            Row row1 = sheet.createRow(rownum++); //cria linha
            Object[] objArr = data.get(key);
            int cellnum = 0;
            for (Object obj : objArr) {
                Cell cell1 = row1.createCell(cellnum++); //adiciona uma célula na linha
                if (obj instanceof Date) {
                    cell1.setCellValue((Date) obj);
                } else if (obj instanceof Boolean) {
                    cell1.setCellValue((Boolean) obj);
                } else if (obj instanceof String) {
                    cell1.setCellValue((String) obj);
                } else if (obj instanceof Double) {
                    cell1.setCellValue((Double) obj);
                } else if (obj instanceof Integer) {
                    cell1.setCellValue((Integer) obj);
                }
            }
        }
        for (int t = 0; t < 6; t++) //deixa colunas ajustadas
        {
            sheet.autoSizeColumn(t);
        }
/*
//PIBIC-JR: id, titulo, comun oral?, nome apresent, vinculo, bolsa, campus, selecionado
 sheet = workbook.createSheet("PIBIC Jr"); //Cria nova sheet 
data = new LinkedHashMap<String, Object[]>();  //Monta hashMap 
        data.put("1", new Object[]{"ID", "TITULO", "MODALIDADE", "APRESENTADOR", "BOLSA", "CAMPUS", "SELECIONADO?"});   //Rotulo  (unico para cada sheet)
        cont = 2;  //Linha que inicia
        for (apresentacao a : apresentacoes) {
            if (a.getModalidadeCod() == 6) //PIBIC JR: 6, PIBIC: 5, PIBEX: 1, PBIS: 3, IFTECH: 8, PIBIN: 4
            {
                data.put("" + cont++, new Object[]{a.getId(), a.getNometitulo(), a.getModalidadeNome(), a.getApresentadorEfetivoNome(), a.getBolsaApresentadorNome(), a.getUnidadeNome(), a.getSelecionadoTexto()});  //preenche uma linha   (unico para cada sheet)
            }
        }
       
        keyset = data.keySet();
        rownum = 1;
        for (String key : keyset) {
            Row row1 = sheet.createRow(rownum++); //cria linha
            Object[] objArr = data.get(key);
            int cellnum = 0;
            for (Object obj : objArr) {
                Cell cell1 = row1.createCell(cellnum++); //adiciona uma célula na linha
                if (obj instanceof Date) {
                    cell1.setCellValue((Date) obj);
                } else if (obj instanceof Boolean) {
                    cell1.setCellValue((Boolean) obj);
                } else if (obj instanceof String) {
                    cell1.setCellValue((String) obj);
                } else if (obj instanceof Double) {
                    cell1.setCellValue((Double) obj);
                } else if (obj instanceof Integer) {
                    cell1.setCellValue((Integer) obj);
                }
            }
        }
        for (int t = 0; t < 6; t++) //deixa colunas ajustadas
        {
            sheet.autoSizeColumn(t);
        }

//PIBIC: mesmo PIBIC-JR
 sheet = workbook.createSheet("PIBIC"); //Cria nova sheet 
data = new LinkedHashMap<String, Object[]>();  //Monta hashMap 
        data.put("1", new Object[]{"ID", "TITULO", "MODALIDADE", "APRESENTADOR", "BOLSA", "CAMPUS", "SELECIONADO?"});   //Rotulo  (unico para cada sheet)
        cont = 2;  //Linha que inicia
        for (apresentacao a : apresentacoes) {
            if (a.getModalidadeCod() == 5) //PIBIC JR: 6, PIBIC: 5, PIBEX: 1, PBIS: 3, IFTECH: 8, PIBIN: 4
            {
                data.put("" + cont++, new Object[]{a.getId(), a.getNometitulo(), a.getModalidadeNome(), a.getApresentadorEfetivoNome(), a.getBolsaApresentadorNome(), a.getUnidadeNome(), a.getSelecionadoTexto()});  //preenche uma linha   (unico para cada sheet)
            }
        }
        
        keyset = data.keySet();
        rownum = 1;
        for (String key : keyset) {
            Row row1 = sheet.createRow(rownum++); //cria linha
            Object[] objArr = data.get(key);
            int cellnum = 0;
            for (Object obj : objArr) {
                Cell cell1 = row1.createCell(cellnum++); //adiciona uma célula na linha
                if (obj instanceof Date) {
                    cell1.setCellValue((Date) obj);
                } else if (obj instanceof Boolean) {
                    cell1.setCellValue((Boolean) obj);
                } else if (obj instanceof String) {
                    cell1.setCellValue((String) obj);
                } else if (obj instanceof Double) {
                    cell1.setCellValue((Double) obj);
                } else if (obj instanceof Integer) {
                    cell1.setCellValue((Integer) obj);
                }
            }
        }
        for (int t = 0; t < 6; t++) //deixa colunas ajustadas
        {
            sheet.autoSizeColumn(t);
        }

//PIBIN: mesmo PIBIC-JR
sheet = workbook.createSheet("PIBIN"); //Cria nova sheet 
data = new LinkedHashMap<String, Object[]>();  //Monta hashMap 
        data.put("1", new Object[]{"ID", "TITULO", "MODALIDADE", "APRESENTADOR", "BOLSA", "CAMPUS", "SELECIONADO?"});   //Rotulo  (unico para cada sheet)
        cont = 2;  //Linha que inicia
        for (apresentacao a : apresentacoes) {
            if (a.getModalidadeCod() == 4) //PIBIC JR: 6, PIBIC: 5, PIBEX: 1, PBIS: 3, IFTECH: 8, PIBIN: 4
            {
                data.put("" + cont++, new Object[]{a.getId(), a.getNometitulo(), a.getModalidadeNome(), a.getApresentadorEfetivoNome(), a.getBolsaApresentadorNome(), a.getUnidadeNome(), a.getSelecionadoTexto()});  //preenche uma linha   (unico para cada sheet)
            }
        }
        
        keyset = data.keySet();
        rownum = 1;
        for (String key : keyset) {
            Row row1 = sheet.createRow(rownum++); //cria linha
            Object[] objArr = data.get(key);
            int cellnum = 0;
            for (Object obj : objArr) {
                Cell cell1 = row1.createCell(cellnum++); //adiciona uma célula na linha
                if (obj instanceof Date) {
                    cell1.setCellValue((Date) obj);
                } else if (obj instanceof Boolean) {
                    cell1.setCellValue((Boolean) obj);
                } else if (obj instanceof String) {
                    cell1.setCellValue((String) obj);
                } else if (obj instanceof Double) {
                    cell1.setCellValue((Double) obj);
                } else if (obj instanceof Integer) {
                    cell1.setCellValue((Integer) obj);
                }
            }
        }
        for (int t = 0; t < 6; t++) //deixa colunas ajustadas
        {
            sheet.autoSizeColumn(t);
        }

//EXTENSAO: mesmo PIBIC-JR
sheet = workbook.createSheet("PIBEX"); //Cria nova sheet 
 data = new LinkedHashMap<String, Object[]>();  //Monta hashMap 
        data.put("1", new Object[]{"ID", "TITULO", "MODALIDADE", "APRESENTADOR", "BOLSA", "CAMPUS", "SELECIONADO?"});   //Rotulo  (unico para cada sheet)
        cont = 2;  //Linha que inicia
        for (apresentacao a : apresentacoes) {
            if (a.getModalidadeCod() == 1) //PIBIC JR: 6, PIBIC: 5, PIBEX: 1, PBIS: 3, IFTECH: 8, PIBIN: 4
            {
                data.put("" + cont++, new Object[]{a.getId(), a.getNometitulo(), a.getModalidadeNome(), a.getApresentadorEfetivoNome(), a.getBolsaApresentadorNome(), a.getUnidadeNome(), a.getSelecionadoTexto()});  //preenche uma linha   (unico para cada sheet)
            }
        }
        
        keyset = data.keySet();
        rownum = 1;
        for (String key : keyset) {
            Row row1 = sheet.createRow(rownum++); //cria linha
            Object[] objArr = data.get(key);
            int cellnum = 0;
            for (Object obj : objArr) {
                Cell cell1 = row1.createCell(cellnum++); //adiciona uma célula na linha
                if (obj instanceof Date) {
                    cell1.setCellValue((Date) obj);
                } else if (obj instanceof Boolean) {
                    cell1.setCellValue((Boolean) obj);
                } else if (obj instanceof String) {
                    cell1.setCellValue((String) obj);
                } else if (obj instanceof Double) {
                    cell1.setCellValue((Double) obj);
                } else if (obj instanceof Integer) {
                    cell1.setCellValue((Integer) obj);
                }
            }
        }
        for (int t = 0; t < 6; t++) //deixa colunas ajustadas
        {
            sheet.autoSizeColumn(t);
        }
*/
        
//TRABALHOS POR CAMPUS: campus/instituicao, id, titulo, participantes e funçoes destes
/*HSSFSheet*/ sheet = workbook.createSheet("Trabalhos por campus"); //Cria nova sheet 
/*Map<String, Object[]>*/ data = new LinkedHashMap<String, Object[]>();  //Monta hashMap 
        data.put("1", new Object[]{"CAMPUS", "ID", "MODALIDADE", "TITULO", "APRESENTADOR", "SUPLENTE", "OUTROS AUTORES", "FUNCOES OUTROS", "CPF OUTROS", "RG OUTROS"});   //Rotulo  (unico para cada sheet)
        cont = 2;  //Linha que inicia
        String nomeA = "", funcA = "", suple = "", rgs = "", cpfs = "";

        for (apresentacao a : apresentacoes) {
            // nomeA =a.getApresentadorEfetivoNome()+";  "; funcA="Apresentador;  ";
            nomeA = "";
            funcA = "";
            rgs = "";
            cpfs = "";
            
            for (autor r : a.getAutores()) {
                if (r.getFuncaoCod() != 3) {
                    nomeA = nomeA + r.getNome() + "; ";
                    funcA = funcA + r.getFuncaoNome() + "; ";
                    rgs = rgs + r.getRg()+ "; ";
                    cpfs = cpfs + r.getCpf()+ "; ";
                } else {
                    suple = r.getNome() + "(CPF "+r.getCpf()+" RG "+r.getRg()+")";
                }
            }

            data.put("" + cont++, new Object[]{a.getUnidadeNome(), a.getId(), a.getModalidadeNome(), a.getNometitulo(), a.getApresentadorNome(), suple, nomeA, funcA, cpfs, rgs});  //preenche uma linha   (unico para cada sheet)

        }

        /*Set<String> */        keyset = data.keySet();
        rownum = 1;
        for (String key : keyset) {
            Row row1 = sheet.createRow(rownum++); //cria linha
            Object[] objArr = data.get(key);
            int cellnum = 0;
            for (Object obj : objArr) {
                Cell cell1 = row1.createCell(cellnum++); //adiciona uma célula na linha
                if (obj instanceof Date) {
                    cell1.setCellValue((Date) obj);
                } else if (obj instanceof Boolean) {
                    cell1.setCellValue((Boolean) obj);
                } else if (obj instanceof String) {
                    cell1.setCellValue((String) obj);
                } else if (obj instanceof Double) {
                    cell1.setCellValue((Double) obj);
                } else if (obj instanceof Integer) {
                    cell1.setCellValue((Integer) obj);
                }
            }
        }
        for (int t = 0; t < 6; t++) //deixa colunas ajustadas
        {
            sheet.autoSizeColumn(t);
        }

        ArrayList<String> naoLanc = new ArrayList<>();
        for (avaliacao a : avaliacoes) {
            if (a.getAtiva() < 1) {
                if (naoLanc.indexOf(a.getAvaliador()) > -1) {
                    naoLanc.add(a.getAvaliador());
                }
            }
        }
        int indexAs;
        for (int x = 0; x < avaliacoes.size(); x++) {
            indexAs = indiceNoArray(apresentacoes, avaliacoes.get(x).getApresentacao());
            if (!avaliacoes.get(x).isAusente() || avaliacoes.get(x).getAtiva() < 1) //se não estiver ausente em alguma avaliação, ou se ainda faltar alguma avaliação
            {
                apresentacaoAusente[indexAs] = 0; //marcou como ok. Não poderá ser marcado como ausente mais.
            } else if (avaliacoes.get(x).isAusente() && apresentacaoAusente[indexAs] == -1) //ainda não marcado como ok, e tem uma avaliação ausente
            {
                apresentacaoAusente[indexAs] = 1; //marcou como ausente, mas se encontrar um "ok", poderá ficar como ok ainda.
            }
        }

//AVALIACOES: ID, TITULO, NOTA, AVALIADOR
/*HSSFSheet*/ sheet = workbook.createSheet("Avaliacoes (flt " + naoLanc.size() + "av)"); //Cria nova sheet 
/*Map<String, Object[]>*/ data = new LinkedHashMap<String, Object[]>();  //Monta hashMap 
        data.put("1", new Object[]{"ID", "APRESENTACAO", "APRESENTACAO UNIDADE", "NOTA", "AUSENTE?", "AVALIADOR CPF", "AVALIADOR NOME", "AVALIADOR UNIDADE", "ATIVA"});   //Rotulo  (unico para cada sheet)
        cont = 2;  //Linha que inicia

        for (avaliacao a : avaliacoes) {
            data.put("" + cont++, new Object[]{a.getApresentacao(), a.getApresentacaoNome(), a.getUnidadeApresentadorNome(), a.getNotaInicial(),
                a.getAusenteTexto(), a.getAvaliador(), a.getAvaliadorNome(), a.getUnidadeAvaliadorNome(), a.getAtivaTexto()});  //preenche uma linha   (unico para cada sheet) 
        }

        /*Set<String> */        keyset = data.keySet();
        rownum = 1;
        for (String key : keyset) {
            Row row1 = sheet.createRow(rownum++); //cria linha
            Object[] objArr = data.get(key);
            int cellnum = 0;
            for (Object obj : objArr) {
                Cell cell1 = row1.createCell(cellnum++); //adiciona uma célula na linha
                if (obj instanceof Date) {
                    cell1.setCellValue((Date) obj);
                } else if (obj instanceof Boolean) {
                    cell1.setCellValue((Boolean) obj);
                } else if (obj instanceof String) {
                    cell1.setCellValue((String) obj);
                } else if (obj instanceof Double) {
                    cell1.setCellValue((Double) obj);
                } else if (obj instanceof Integer) {
                    cell1.setCellValue((Integer) obj);
                }
            }
        }
        for (int t = 0; t < 9; t++) //deixa colunas ajustadas
        {
            sheet.autoSizeColumn(t);
        }

//AUSENTES: id, titulo, comun oral?, nome apresentador, bolsa, campus, selecionado?
/*HSSFSheet*/ sheet = workbook.createSheet("Ausentes"); //Cria nova sheet 
/*Map<String, Object[]>*/ data = new LinkedHashMap<String, Object[]>();  //Monta hashMap 
        data.put("1", new Object[]{"ID", "TITULO", "MODALIDADE", "APRESENTADOR", "BOLSA", "CAMPUS", "SELECIONADO?"});   //Rotulo  (unico para cada sheet)
        cont = 2;  //Linha que inicia

        for (int a1 = 0; a1 < apresentacoes.size(); a1++) {
            apresentacao a = apresentacoes.get(a1);
            if (apresentacaoAusente[a1] == 1) {
                data.put("" + cont++, new Object[]{a.getId(), a.getNometitulo(), a.getModalidadeNome(), a.getApresentadorEfetivoNome(), a.getBolsaApresentadorNome(), a.getUnidadeNome(), a.getSelecionadoTexto()});  //preenche uma linha   (unico para cada sheet)
            }
        }

        /*Set<String> */keyset = data.keySet();
        rownum = 1;
        for (String key : keyset) {
            Row row1 = sheet.createRow(rownum++); //cria linha
            Object[] objArr = data.get(key);
            int cellnum = 0;
            for (Object obj : objArr) {
                Cell cell1 = row1.createCell(cellnum++); //adiciona uma célula na linha
                if (obj instanceof Date) {
                    cell1.setCellValue((Date) obj);
                } else if (obj instanceof Boolean) {
                    cell1.setCellValue((Boolean) obj);
                } else if (obj instanceof String) {
                    cell1.setCellValue((String) obj);
                } else if (obj instanceof Double) {
                    cell1.setCellValue((Double) obj);
                } else if (obj instanceof Integer) {
                    cell1.setCellValue((Integer) obj);
                }
            }
        }
        for (int t = 0; t < 6; t++) //deixa colunas ajustadas
        {
            sheet.autoSizeColumn(t);
        }

//CERTIFICADOS: texto, verficador, nomeArq, tipo, cpf, texto2
/*HSSFSheet*/ sheet = workbook.createSheet("Certificados"); //Cria nova sheet 
/*Map<String, Object[]>*/ data = new LinkedHashMap<String, Object[]>();  //Monta hashMap 
        data.put("1", new Object[]{"TIPO", "PRINCIPAL", "IDENTIF", "VERIFICADOR", "TODO TEXTO", "NomeArq", "Evento"});   //Rotulo  (unico para cada sheet)
        cont = 2;  //Linha que inicia

        for (certificado a : certificados) {

            data.put("" + cont++, new Object[]{a.getTipoNomeBonito(), a.getTexto2(), a.getCpfSemID(), a.getVerificador(), a.getTexto(), a.getNomeArq(), a.getEvento()});  //preenche uma linha   (unico para cada sheet)

        }

        /*Set<String> */        keyset = data.keySet();
        rownum = 1;
        for (String key : keyset) {
            Row row1 = sheet.createRow(rownum++); //cria linha
            Object[] objArr = data.get(key);
            int cellnum = 0;
            for (Object obj : objArr) {
                Cell cell1 = row1.createCell(cellnum++); //adiciona uma célula na linha
                if (obj instanceof Date) {
                    cell1.setCellValue((Date) obj);
                } else if (obj instanceof Boolean) {
                    cell1.setCellValue((Boolean) obj);
                } else if (obj instanceof String) {
                    cell1.setCellValue((String) obj);
                } else if (obj instanceof Double) {
                    cell1.setCellValue((Double) obj);
                } else if (obj instanceof Integer) {
                    cell1.setCellValue((Integer) obj);
                }
            }
        }
        for (int t = 0; t < 4; t++) //deixa colunas ajustadas
        {
            if(t!=1)
            sheet.autoSizeColumn(t);
        }

//OFICINAS, PALESTRAS, SESSOES, MONITORES
        ArrayList<oficinaSessaoMonitor> opsm = BancoDeDados.comando().getArrayOficinaSessaoMonitor(event.getId());
        sheet = workbook.createSheet("Ofic Pales Sess Mon"); //Cria nova sheet 
/*Map<String, Object[]>*/ data = new LinkedHashMap<String, Object[]>();  //Monta hashMap 
        data.put("1", new Object[]{"TIPO", "PARTICIPANTES", "VERBO", "TRABALHO OU CAMPUS", "DOCUMENTO", "ID REGISTRO"});   //Rotulo  (unico para cada sheet)
        cont = 2;  //Linha que inicia

        for (oficinaSessaoMonitor a : opsm) {

            data.put("" + cont++, new Object[]{a.getTipo(), a.getNomeParticipantes(), a.getVerbo(), a.getTrabalhoOuCampus(), a.getDocumento(), a.getId()});  //preenche uma linha   (unico para cada sheet)

        }

        /*Set<String> */        keyset = data.keySet();
        rownum = 1;
        for (String key : keyset) {
            Row row1 = sheet.createRow(rownum++); //cria linha
            Object[] objArr = data.get(key);
            int cellnum = 0;
            for (Object obj : objArr) {
                Cell cell1 = row1.createCell(cellnum++); //adiciona uma célula na linha
                if (obj instanceof Date) {
                    cell1.setCellValue((Date) obj);
                } else if (obj instanceof Boolean) {
                    cell1.setCellValue((Boolean) obj);
                } else if (obj instanceof String) {
                    cell1.setCellValue((String) obj);
                } else if (obj instanceof Double) {
                    cell1.setCellValue((Double) obj);
                } else if (obj instanceof Integer) {
                    cell1.setCellValue((Integer) obj);
                }
            }
        }
        for (int t = 0; t < 6; t++) //deixa colunas ajustadas
        {
            sheet.autoSizeColumn(t);
        }
        
        
        //TICKETS DE CADASTRO
        ArrayList<ticket> tickets = BancoDeDados.comando().getTickets(true, new ArrayList<coordenador>());
        sheet = workbook.createSheet("Tickets de Cadastro"); //Cria nova sheet 
/*Map<String, Object[]>*/ data = new LinkedHashMap<String, Object[]>();  //Monta hashMap 
        data.put("1", new Object[]{"TIPO", "CÓDIGO", "UNIDADE"});   //Rotulo  (unico para cada sheet)
        cont = 2;  //Linha que inicia

        for (ticket t : tickets) {

            data.put("" + cont++, new Object[]{t.getTipo(),t.getCodigo(),t.getUnidadeNome()});  //preenche uma linha   (unico para cada sheet)

        }

        /*Set<String> */        keyset = data.keySet();
        rownum = 1;
        for (String key : keyset) {
            Row row1 = sheet.createRow(rownum++); //cria linha
            Object[] objArr = data.get(key);
            int cellnum = 0;
            for (Object obj : objArr) {
                Cell cell1 = row1.createCell(cellnum++); //adiciona uma célula na linha
                if (obj instanceof Date) {
                    cell1.setCellValue((Date) obj);
                } else if (obj instanceof Boolean) {
                    cell1.setCellValue((Boolean) obj);
                } else if (obj instanceof String) {
                    cell1.setCellValue((String) obj);
                } else if (obj instanceof Double) {
                    cell1.setCellValue((Double) obj);
                } else if (obj instanceof Integer) {
                    cell1.setCellValue((Integer) obj);
                }
            }
        }
        for (int t = 0; t < 6; t++) //deixa colunas ajustadas
        {
            sheet.autoSizeColumn(t);
        }
        
        

// SALVANDO O ARQUIVO EXCEL 
        try {
            FileOutputStream out
                    = new FileOutputStream(new File(urlArquivo+".xls"));
            workbook.write(out);
            out.close();
            System.out.println("ATENÇÂO: Relatorio Excel salvo com sucesso!");
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private String recNomeB(int bolsaCod) {

        for (bolsa b : bolsas) {
            if (b.getId() == bolsaCod) {
                return b.getNome();
            }
        }
        System.out.println("Não achou bolsa de codigo " + bolsaCod);
        return "ERR";
    }

    private String recTipoPessoa(int tipoPessoa) {
        for (tipopessoa b : tipopessoas) {
            if (b.getId() == tipoPessoa) {
                return b.getNome();
            }
        }

        return "ERR";
    }

    private int indiceNoArray(ArrayList<apresentacao> apresentacoes, int id) {

        for (int x = 0; x < apresentacoes.size(); x++) {
            if (id == apresentacoes.get(x).getId()) {
                return x;
            }
        }

        return -1;
    }

}
