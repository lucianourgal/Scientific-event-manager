/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClassesAuxiliares;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import objetosBase.apresentacao;
import objetosBase.apresentador;
import objetosBase.avaliador;
import objetosBase.certificado;
import objetosBase.oficinaSessaoMonitor;
import objetosBase.triviais.evento;

/**
 * @author SistemaIP
 */
public class geradorCertificados {

    private final String linkValidacao = "http://zip.net/bcsnSF";//"200.17.98.136/proepi/autenticidade";

    private final String localJasper = "lib/sepinGenerico.jasper"; //"lib/sepinGenerico.jasper";
    private final String localJasperRedux = "lib/sepinGenericoRedux.jasper";//"lib/sepinGenericoRedux.jasper";

    private final String localAvaliador = "Certificados/Avaliadores/";//"C:/local/Avaliadores/";
    private final String localTrabalhos = "Certificados/Todos os Trabalhos/";//"C:/local/Todos os Trabalhos/";
    private final String localApresentador = "Certificados/Apresentadores, Participacao/";//"C:/local/Apresentadores, Participacao/";
    private final String localMonitor = "Certificados/Monitores do Evento/";//"C:/local/Monitores do Evento/";
    private final String localPalestra = "Certificados/Palestrantes/";//"C:/local/Palestrantes/";
    private final String localSessaoTematica = "Certificados/Sessoes Tematicas/";//"C:/local/Sessoes Tematicas/";
    private final String localOficina = "Certificados/Oficineiros/";//"C:/local/Oficineiros/";

    ArrayList<certificado> certificadosApresentadores = new ArrayList<>();
    ArrayList<certificado> certificadosAvaliadores = new ArrayList<>();
    ArrayList<certificado> certificadosTrabalhos = new ArrayList<>();
    ArrayList<certificado> certificadosMonitores, certificadosOficinas, certificadosSessoesTematicas, certificadosPalestras;

    private String sucesso = "", falha = "", relatorio = "";
    private int sucessos = 0, falhas = 0;
    private boolean salvandoNoBD = false;
    private ArrayList<oficinaSessaoMonitor> monitoresOficineirosSessoesPalestras;
    private evento event;

    public void salvarTodosNoBD() {
        ArrayList<certificado> todos = new ArrayList<>();
        todos.addAll(certificadosApresentadores);
        todos.addAll(certificadosAvaliadores);
        todos.addAll(certificadosTrabalhos);
        todos.addAll(certificadosMonitores);
        todos.addAll(certificadosOficinas);
        todos.addAll(certificadosSessoesTematicas);
        todos.addAll(certificadosPalestras);

        for (certificado c : todos) {
            if (!c.isVeioDoBD()) {
                c.salvarNoBD();
            }
        }

    }

    public void criarDiretorios() {
        Arrumador.comando().criaDiretorio("Certificados");
        Arrumador.comando().criaDiretorio(localAvaliador);
        Arrumador.comando().criaDiretorio(localTrabalhos);
        Arrumador.comando().criaDiretorio(localApresentador);
        Arrumador.comando().criaDiretorio(localMonitor);
        Arrumador.comando().criaDiretorio(localPalestra);
        Arrumador.comando().criaDiretorio(localSessaoTematica);
        Arrumador.comando().criaDiretorio(localOficina);
    }

    public void addFalha(String f) {
        String newLine = System.getProperty("line.separator");
        falha = falha + f + "; " + newLine;
        falhas++;
    }

    public void addSucesso(String f) {
        String newLine = System.getProperty("line.separator");
        sucesso = sucesso + f + "; " + newLine;
        sucessos++;
    }

    public void gerarConjuntoCertsSEPIN(evento event, boolean salvarNoBD) {
        salvandoNoBD = salvarNoBD;
        criarDiretorios();
        this.event = event;
        //apresentador, poster, oral, iftech, robotica, avaliador
       /* ArrayList<evento> events = BancoDeDados.comando().getEventos();
         evento event = new evento("none", "none");
         for (evento e : events) {
         if (e.isAtivo()) {
         event = e;
         }
         }*/
        
        ArrayList<apresentador> apresentadores = BancoDeDados.comando().recuperarApresentadoresEfetivosDoCentralizado(event);
        System.out.println("Recuperou " + apresentadores.size() + " apresentadores do evento "+event.getNome()+".");
        ArrayList<apresentacao> apresentacoes = BancoDeDados.comando().recuperarListaGeralApresentacoes(1, -1,event);
        System.out.println("Recuperou " + apresentacoes.size() + " apresentações do evento "+event.getNome()+".");
        ArrayList<avaliador> avaliadores = BancoDeDados.comando().recuperarTodosAvaliadores(event.getId());
        //ArrayList<avaliador> avaliadores = BancoDeDados.comando().recuperarAvaliadoresDaUnidade(-1);
        System.out.println("Recuperou " + avaliadores.size() + " avaliadores (todos. Separação posterior).");

        System.out.println("Iniciando certificados de PARTICIPAÇÃO...");
        //apresentadores
       /* apresentadores.stream().map((ap) -> {
            certificado novo = new certificado(this);
            novo.criarCertificadoApresentador(ap, event);
            return novo;
        }).forEachOrdered((novo) -> {
            certificadosApresentadores.add(novo);
        });*/
        for (int x=0; x< apresentadores.size(); x++){
            certificado novo = new certificado(this);
            novo.criarCertificadoApresentador(apresentadores.get(x), event);
            certificadosApresentadores.add(novo);
        }
        for (avaliador av : avaliadores) {
            certificado novo = new certificado(this);
            novo.criarCertificadoParticipacaoAvaliador(av, event);
            certificadosApresentadores.add(novo);
        }
         salvarLogs(localApresentador); 
        
        
        System.out.println("Iniciando certificados de AVALIADORES...");
        //avaliadores
        certificadosAvaliadores = new ArrayList<>();
        for (avaliador av : avaliadores) {
            av.calcularHorasCertificado(true); //calculando quantas horas cada certificado terá
            certificado novo = new certificado(this);
            novo.criarCertAvaliadores(av, event);
            certificadosAvaliadores.add(novo);
        }

        //certificadosAvaliadores.addAll(criarCertificadosAvaliadoresHardCoded(event));

        salvarLogs(localAvaliador);

        System.out.println("Iniciando certificados de TRABALHOS...");
        //Trabalhos
        certificadosTrabalhos = new ArrayList<>();
        for (apresentacao a : apresentacoes) {  //MODALIDADE poster(1), oral(2), iftech(3), cultural (4), robotica(5)
            certificado novo = new certificado(this);
            novo.criarCertificadorTrabalho(a, event);
            certificadosTrabalhos.add(novo);
            if (a.getModalidadeCod() == 2) { //COMUNICAÇÃO ORAL
                a.setModalidadeCod(1);
                certificado novo2 = new certificado(this);
                novo2.criarCertificadorTrabalho(a, event);
                certificadosTrabalhos.add(novo2);
            }

        }
        salvarLogs(localTrabalhos);

        //Recuperação dos cadastros feitos pela coordenação
        monitoresOficineirosSessoesPalestras = BancoDeDados.comando().getArrayOficinaSessaoMonitor(event.getId());

        System.out.println("Iniciando certificados de MONITORES...");

        certificadosMonitores = criarCertsMonitores(monitoresOficineirosSessoesPalestras,event);//criarCertsMonitoresHardCoded(event);
        salvarLogs(localMonitor);

        System.out.println("Iniciando certificados de OFICINEIROS...");

        certificadosOficinas = criarCertsOficinas(monitoresOficineirosSessoesPalestras,event);//criarCertsOficinasHardCoded(event);
        salvarLogs(localOficina);

        System.out.println("Iniciando certificados de SESSOES TEMATICAS...");

        certificadosSessoesTematicas = criarCertsSessoesTematicas(monitoresOficineirosSessoesPalestras,event);//criarCertsSessoesTematicasHardCoded(event);
        salvarLogs(localSessaoTematica);

        System.out.println("Iniciando certificados de PALESTRAS...");

        certificadosPalestras = criarCertsPalestras(monitoresOficineirosSessoesPalestras,event);//criarCertsPalestrasHardCoded(event);
        salvarLogs(localPalestra);

        System.out.println("Terminou de gerar certificados!");

        if (salvarNoBD) {
            salvarTodosNoBD();
        }

        salvandoNoBD = false;

    }

    public String getRelatorio() {
        return relatorio;
    }

    public void salvarLogs(String local) {

        relatorio = relatorio + local + ": " + "Suc " + sucessos + "\n";
        Arrumador.comando().salvarTXT(local + "AA Log Falhas (" + falhas + ")", falha);
        falha = "";

        Arrumador.comando().salvarTXT(local + "AA Log Sucessos (" + sucessos + ")", sucesso);
        System.out.println("Sucessos: " + sucessos + "; Falhas: " + falhas);
        sucesso = "";
        sucessos = 0;
        falhas = 0;
    }

    public void gerarCertificado(certificado cert) {
        HashMap filtro = new HashMap();

        //texto1, texto2, texto3, verificador, nomeArq, tipo, unico
        String texto1 = cert.getTexto1();
        String texto2 = cert.getTexto2();
        String texto3 = cert.getTexto3();
        String verificador = cert.getVerificador();
        String nomeArq = cert.getNomeArq();
        String tipo = cert.getTipo();
        String unico = cert.getCpf();

        if (certificadoExisteNoBD(tipo, unico, cert.getEvento())) {
                addFalha("Já cadastrado no BD: " + nomeArq);
                cert.setFalha(true);
                return;
        }
        
        
        filtro.put("idalunos", texto1 + texto2 + texto3); //"\n\nCódigo verificador: "+verificador);
        filtro.put("texto1", texto1); //"\n\nCódigo verificador: "+verificador);
        filtro.put("texto2", texto2); //"\n\nCódigo verificador: "+verificador);
        filtro.put("texto3", texto3); //"\n\nCódigo verificador: "+verificador);
        filtro.put("validador", "Para verificar a autenticidade, acesse " + linkValidacao + " com o código " + verificador + "");
        try {

            JasperPrint print;

            try{
                if ((texto1 + texto2 + texto3).length() < 540) {
                    print = JasperFillManager.fillReport(localJasper, filtro,
                            BancoDeDados.comando().getConnection());
                } else {
                    print = JasperFillManager.fillReport(localJasperRedux, filtro,
                            BancoDeDados.comando().getConnection());
                }
            } catch(JRException e){
                this.compilarArquivoJasperSEPIN();
                if ((texto1 + texto2 + texto3).length() < 540) {
                    print = JasperFillManager.fillReport(localJasper, filtro,
                            BancoDeDados.comando().getConnection());
                } else {
                    print = JasperFillManager.fillReport(localJasperRedux, filtro,
                            BancoDeDados.comando().getConnection());
                }
            }
            //JasperViewer viewer = new JasperViewer(print,false);

            //viewer.setVisible(true);
            String local;
            if (tipo.equals("avaliador")) {
                local = localAvaliador;
            } else if (tipo.equals("apresentador")) {
                local = localApresentador;
            } else if (tipo.equals("monitor")) {
                local = localMonitor;
            } else if (tipo.equals("palestrante")) {
                local = localPalestra;
            } else if (tipo.equals("oficina")) {
                local = localOficina;
            } else if (tipo.equals("sessaoTematica")) {
                local = localSessaoTematica;
            } else {
                local = localTrabalhos;
            }

            if (nomeArq.length() > 150) {
                nomeArq = nomeArq.substring(0, 149);
            }



            JasperExportManager.exportReportToPdfFile(print, local + nomeArq.replace("\"", ".").replace("?", ".").replace("\\", ".").replace("/", ".").replace(":", ".").replace("ç", "c") + " " + cert.getVerificador() + ".pdf");
            addSucesso("(" + (texto1 + texto2 + texto3).length() + ") Certificado " + nomeArq + " gerado com sucesso. Evento: " + cert.getEvento());

        } catch (JRException ex) {
            Logger.getLogger(geradorCertificados.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Falha em certificado " + nomeArq + "!");
        }
    }

    /* public void gerarCerts(){
     // exportacao do relatorio para outro formato, no caso PDF 
     JasperExportManager.exportReportToPdfFile(print, "relatorios/RelatorioClientes.pdf");       
     System.out.println("Relatório gerado.");     
     }*/
    public void compilarArquivoJasperSEPIN() {
        try {
            //https://www.youtube.com/watch?v=uJ7840fmSZo
            //http://davidbuzatto.com.br/2010/10/09/jasperreports-trabalhando-com-relatorios-em-java-parte-1/  
            JasperCompileManager.compileReportToFile(
                    "lib/sepin8.jrxml", //    "lib/sepin6.jrxml",//the path to the jrxml file to compile
                    localJasper);

            JasperCompileManager.compileReportToFile(
                    "lib/sepin8redux.jrxml", //    "lib/sepin6redux.jrxml",//the path to the jrxml file to compile
                    localJasperRedux);

        } catch (JRException ex) {
            Logger.getLogger(geradorCertificados.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

 

    public void gerarCertificadoSepin(int apresentacao, String textoC) {
        HashMap filtro = new HashMap();
        //filtro.put("idaapresentacao",apresentacao);
        filtro.put("idalunos", textoC);

        filtro.put("validador", "Para verificar autenticidade, acesse com o código 03819831809 em 200.17.98.138/tascadoiahsd");
        // filtro.put("idalunos2",apresentacao+"fioahdiuahsao");
        try {
            JasperPrint print = JasperFillManager.fillReport("C:/local/sepinGenerico.jasper", filtro, BancoDeDados.comando().getConnection());
            JasperViewer viewer = new JasperViewer(print, false);

            viewer.setVisible(true);
            System.out.println("Certificado da apresentacao " + apresentacao + " gerado com sucesso!");
            // exportacao do relatorio para outro formato, no caso PDF 
            //JasperExportManager.exportReportToPdfFile(print, "C:/local/CertTeste.pdf");  

        } catch (JRException ex) {
            this.compilarArquivoJasperSEPIN();
            Logger.getLogger(geradorCertificados.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Falha em certificado de apresentacao " + apresentacao + "!");
        }
    }

    public ArrayList<certificado> criarCertsOficinasHardCoded(evento e) {
        ArrayList<certificado> certs = new ArrayList<>();

        certificado n;
        n = new certificado(this);
        n.criarCertOficina("Pessoas x e y", "ministraram", "Uso de Experimentos no Ensino de Química", "SemDoc1", e);
        certs.add(n);

        return certs;
    }

    public ArrayList<certificado> criarCertsSessoesTematicasHardCoded(evento e) {
        ArrayList<certificado> certs = new ArrayList<>();

        certificado n;
        n = new certificado(this);
        n.criarCertSessaoTematica("Pessoa Z", "ministrou", "A Identidade Feminina na Ciência e na Indústria ao Longo dos Tempos: Identificação das Pioneiras, Resgate Histórico e de Imagem", "SemDoc36", e);
        certs.add(n);

        return certs;
    }

    public ArrayList<certificado> criarCertsPalestrasHardCoded(evento e) {
        ArrayList<certificado> certs = new ArrayList<>();

        certificado n;

        n = new certificado(this);
        n.criarCertPalestrante("Pessoa baadyad", "ministrou", "Importância dos Portos no Contexto Nacional", "SemDoc51", e);
        certs.add(n);

        return certs;
    }

    public ArrayList<certificado> criarCertsMonitoresHardCoded(evento e) {
        ArrayList<certificado> monitores = new ArrayList<>();

        certificado n = new certificado(this);
        n.criarCertMonitores("Milena R", "monitora", "doc1", "Paranaguá", e);
        monitores.add(n);
       
        return monitores;
    }

    private ArrayList<certificado> criarCertificadosAvaliadoresHardCoded(evento e) {
        //criarCertAvaliadorHardCoded(String nome, String avaliadorAvaliadora, String unidade, String doc, String horas, evento e)
        ArrayList<certificado> certs = new ArrayList<>();

        certificado n;

        //substitutos não diretos
        n = new certificado(this);
        n.criarCertAvaliadorHardCoded("Antonio xcxxx", "Avaliador", "Paranaguá", "Substituto1", "1h30", e);
        certs.add(n);

        //iftechs
        n = new certificado(this);
        n.criarCertAvaliadorHardCoded("Vania eeeo", "Avaliadora", "Iftech", "iftech1", "3h30", e);
        certs.add(n);

        return certs;
    }

    public boolean certificadoExisteNoBD(String tipo, String texto, String eventoT) {
        String SQL = "SELECT count(verificador) FROM certificado "
                + "WHERE cpf = '" + texto + "' AND tipo = '" + tipo + "' AND evento = '"+eventoT+"';";
        
        /*   if (tipo.equals("avaliador")) {
         SQL = "SELECT count(verificador) FROM certificado "
         + "WHERE cpf ='"+texto+"' AND tipo = '"+tipo+"';";
         } else if (tipo.equals("apresentador")) {
         SQL = "SELECT count(verificador) FROM certificado "
         + "WHERE cpf ='"+texto+"' AND tipo = '"+tipo+"';";
         } else if (tipo.equals("monitor")) {
         SQL = "SELECT count(verificador) FROM certificado "
         + "WHERE cpf ='"+texto+"' AND tipo = '"+tipo+"';";
         } else if (tipo.equals("palestrante")) {
         SQL = "SELECT count(verificador) FROM certificado "
         + "WHERE cpf ='"+texto+"' AND tipo = '"+tipo+"';";
         } else if (tipo.equals("oficina")) {
         SQL = "SELECT count(verificador) FROM certificado "
         + "WHERE cpf ='"+texto+"' AND tipo = '"+tipo+"';";
         } else if (tipo.equals("sessaoTematica")) {            
         SQL = "SELECT count(verificador) FROM certificado "
         + "WHERE cpf ='"+texto+"' AND tipo = '"+tipo+"';";
         } else { // é de trabalho. Poster, comOral, ifTech, Robotica, Cultural
         SQL = "SELECT count(verificador) FROM certificado "
         + "WHERE cpf ='"+texto+"' AND tipo = '"+tipo+"';";
         }*/
        int x = BancoDeDados.comando().selectCount(SQL);
        //System.out.println(SQL + "  = " + x);
        
        return (x > 0);
    }

    private ArrayList<certificado> criarCertsMonitores(ArrayList<oficinaSessaoMonitor> objetos, evento e) {
        ArrayList<certificado> monitores = new ArrayList<>();
        for (int a = 0; a < objetos.size(); a++) {
            if (objetos.get(a).getTipo().equals("Monitor")) {

                certificado n = new certificado(this);
                n.criarCertMonitores(objetos.get(a).getNomeParticipantes(), objetos.get(a).getVerbo(), 
                        objetos.get(a).getDocumento(), objetos.get(a).getTrabalhoOuCampus(), e);
                monitores.add(n);
            }
        }

        return monitores;
    }

    private ArrayList<certificado> criarCertsOficinas(ArrayList<oficinaSessaoMonitor> objetos, evento e) {
      ArrayList<certificado> oficinas = new ArrayList<>();
        for (int a = 0; a < objetos.size(); a++) {
            if (objetos.get(a).getTipo().equals("Oficina")) {

                certificado n = new certificado(this);
                n.criarCertOficina(objetos.get(a).getNomeParticipantes(), objetos.get(a).getVerbo(), 
                        objetos.get(a).getTrabalhoOuCampus(), objetos.get(a).getDocumento(), e);
        
                oficinas.add(n);
            }
        }

        return oficinas;
    }

    private ArrayList<certificado> criarCertsSessoesTematicas(ArrayList<oficinaSessaoMonitor> objetos, evento e) {
       ArrayList<certificado> sessoes = new ArrayList<>();
        for (int a = 0; a < objetos.size(); a++) {
            if (objetos.get(a).getTipo().equals("Sessão Temática")) {

                certificado n = new certificado(this);
                n.criarCertSessaoTematica(objetos.get(a).getNomeParticipantes(), objetos.get(a).getVerbo(), 
                        objetos.get(a).getTrabalhoOuCampus(), objetos.get(a).getDocumento(), e);
        
                sessoes.add(n);
            }
        }

        return sessoes;
    }
    
     private ArrayList<certificado> criarCertsPalestras(ArrayList<oficinaSessaoMonitor> objetos, evento e) {
       ArrayList<certificado> palestras = new ArrayList<>();
        for (int a = 0; a < objetos.size(); a++) {
            if (objetos.get(a).getTipo().equals("Palestra")) {

                certificado n = new certificado(this);
                n.criarCertPalestrante(objetos.get(a).getNomeParticipantes(), objetos.get(a).getVerbo(), 
                        objetos.get(a).getTrabalhoOuCampus(), objetos.get(a).getDocumento(), e);
        
                palestras.add(n);
            }
        }

        return palestras;
    }

}
