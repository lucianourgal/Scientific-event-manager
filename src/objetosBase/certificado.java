/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package objetosBase;

import ClassesAuxiliares.BancoDeDados;
import ClassesAuxiliares.geradorCertificados;
import java.util.ArrayList;
import java.util.Random;
import objetosBase.triviais.evento;

/**
 *
 * @author Luciano
 */
public class certificado {

    private String texto;
    private String texto1, texto2, texto3;
    private String verificador;
    private String nomeArq;
    private String evento;
    private String cpf;
    private boolean veioDoBD = false;
    private boolean falha = false;
    private String tipo; //apresentador, poster, oral, iftech, robotica, avaliador, cultural, monitor, sessaoTematica, oficina, palestrante
    private geradorCertificados gc;

    
    public void apagarCertificado(){
    
        String s = "DELETE FROM certificado WHERE verificador = '"+verificador+"'";
        
        if(BancoDeDados.comando().update(s)>0)
            System.out.println("OK: Apagou certificado "+verificador+" do BD!");
        else
            System.out.println("FALHA: Não apagou certificado "+verificador+" do BD!");
        
    }
    
    
    public void salvarNoBD() {

        if (isFalha()) {
            return;
        }

        if (!BancoDeDados.comando().inserirCertificado(this)) {
            verificador = gerarCodigoHexa();
            BancoDeDados.comando().inserirCertificado(this);
        }
    }

    public certificado(geradorCertificados g) {
        gc = g;
    }

    public void criarCertificadoApresentador(apresentador ap, evento event) {
        evento = event.getId();
        /*PARTICIPAÇÃO NO EVENTO (Apresentador)

         Certificamos que NOMEAPRESENTADOR  participou do 
         IV Seminário de Extensão, Ensino, Pesquisa e Inovação do IFPR, no período de 19 a 22 de outubro de 2015
         , em Paranaguá – Paraná, com carga horária de 30 horas.

         (texto, nomeApresentador, texto, nomeEvento, texto, dataEvento, texto, localEvento, texto)*/

        //verifica se apresentador participou do evento
        boolean apr = BancoDeDados.comando().apresentouTrabalho(ap.getCpf(), event.getId());

        if (apr == false) {
            setTipo("fail");
            gc.addFalha("Não há trabalho para " + ap.getNome() + " ( " + ap.getUnidade() + " )");
            setFalha(true);
            return;
        }
        int c = BancoDeDados.comando().selectCount("SELECT count(av.avaliador) "
                + "FROM avaliacao av, apresentacao ap "
                + "WHERE av.ausente < 1 AND av.apresentacao = ap.id AND ap.apresentador = '" + ap.getCpf() + "';");

        int c2 = BancoDeDados.comando().selectCount("SELECT count(t.id) "
                + "FROM apresentacao t, apresentador a "
                + "WHERE t.apresentador = '" + ap.getCpf() + "' AND t.modalidade > 2;");

        if (c < 1 && c2 < 1) {
            gc.addFalha("Sem avaliações sufientes (" + c + ") para " + ap.getNome() + " ( " + ap.getUnidade() + " - " + ap.getCpf() + " )");
            setFalha(true);
            return;
        }

        setCpf(ap.getCpf());
        setTipo("apresentador");
        setVerificador(gerarCodigoHexa());
        setNomeArq(ap.getUnidade() + " Participacao Apresentador " + ap.getNome());
        // Gera texto
        texto1 = "Certificamos que ";
        texto2 = ap.getNome();
        texto3 = " participou do " + event.getNome() + " do IFPR, "
                + "no período de " + event.getData() + ", em " + event.getLocal() + ", com carga horária de 30 horas.";

        setTexto(texto1 + texto2 + texto3);

        getGc().gerarCertificado(this);

    }
    
     public void criarCertificadoParticipacaoAvaliador(avaliador ap, evento event) {
        evento = event.getId();
        /*PARTICIPAÇÃO NO EVENTO (Apresentador)

         Certificamos que NOMEAPRESENTADOR  participou do 
         IV Seminário de Extensão, Ensino, Pesquisa e Inovação do IFPR, no período de 19 a 22 de outubro de 2015
         , em Paranaguá – Paraná, com carga horária de 30 horas.

         (texto, nomeApresentador, texto, nomeEvento, texto, dataEvento, texto, localEvento, texto)*/

        //verifica se apresentador participou do evento
       
        int c = BancoDeDados.comando().selectCount("SELECT count(a.apresentacao) FROM avaliacao a, apresentacao ap "
                + "WHERE a.avaliador = '" + ap.getCpf() + "' AND a.ativa = 1 AND a.apresentacao = ap.id AND ap.evento = '" + event.getId() + "';");
        
        if (c < 2 && ap.getUnidadeCod() != 22) {
                        
            gc.addFalha("Sem avaliações suficientes (" + c + ") para " + ap.getCpf() + " - " + ap.getNome() + " ( " + ap.getUnidadeNome() + " )");
            setFalha(true);
            return;
        }
        
        
        setCpf(ap.getCpf());
        setTipo("apresentador");
        setVerificador(gerarCodigoHexa());
        setNomeArq(ap.getUnidadeNome() + " Participacao Avaliador " + ap.getNome());
        // Gera texto
        texto1 = "Certificamos que ";
        texto2 = ap.getNome();
        texto3 = " participou do " + event.getNome() + " do IFPR, "
                + "no período de " + event.getData() + ", em " + event.getLocal() + ", com carga horária de 30 horas.";

        setTexto(texto1 + texto2 + texto3);

        getGc().gerarCertificado(this);

    }

    public void criarCertificadorTrabalho(apresentacao ap, evento e) {

        evento = e.getId();

        if (ap.getModalidadeCod() != 2 && ap.getModalidadeCod() != 4) { //Estava como <0. Passei para !=2, Sempre que for ComunicaçãoOral, passa direto.
            if (!BancoDeDados.comando().trabalhoOkParaAnaisCertificado(ap, e)) { //se a verificação falha

                gc.addFalha("Sem avaliações sufientes para " + ap.getId() + ""
                        + "( " + ap.getUnidadeNome() + " - " + ap.getModalidadeNome() + " )");
                setFalha(true);
                return;
            }
        }
        
        //else: é comunicação oral. Não faz verificação. Gera certificado direto.
        
        setVerificador(gerarCodigoHexa());
        setCpf(ap.getApresentadorCpf() + "." + ap.getId());
//MODALIDADE poster(1), oral(2), iftech(3), robotica(5), cultural (4)

        if (ap.getModalidadeCod() == 1) {  // poster
            criarCertificadoPoster(ap, e);
            setNomeArq(ap.getUnidadeNome() + " Poster " + ap.getNometitulo() + " " + ap.getId());
        } else if (ap.getModalidadeCod() == 2) { //com Oral
            criarCertComunicacaoOral(ap, e);
            setNomeArq(ap.getUnidadeNome() + " ComOral " + ap.getNometitulo() + " " + ap.getId());
        } else if (ap.getModalidadeCod() == 3) { //iftech
            criarCertMostraIFTech(ap, e);
            setNomeArq(ap.getUnidadeNome() + " IFTech " + ap.getNometitulo() + " " + ap.getId());
        } else if (ap.getModalidadeCod() == 5) { //robotica
            criarCertMostraRobotica(ap, e);
            setNomeArq(ap.getUnidadeNome() + " Robotica " + ap.getNometitulo() + " " + ap.getId());
        } else if (ap.getModalidadeCod() == 4) { // cultural
            criarCertApresentacaoCultural(ap, e);
            setNomeArq(ap.getUnidadeNome() + " Cultural " + ap.getNometitulo() + " " + ap.getId());
        } else {
            gc.addFalha("Nao identificou modalidade para " + ap.getId() + " ( " + ap.getUnidadeNome() + " )");
            setFalha(true);
            return;
        }
        getGc().gerarCertificado(this);
    }

    public void criarCertificadoPoster(apresentacao a, evento ev) {
        /*
         MODALIDADE PÔSTER
        
         Certificamos que o trabalho intitulado NOMEAPRESENTACAO, dos autores Apresentador,
         Suplente, Co-autor e Orientador, foi apresentado na modalidade Pôster durante o IV
         Seminário de Extensão, Ensino, Pesquisa e Inovação do IFPR, realizado nos dias 19 a 22 de outubro de 2015, em Paranaguá – Paraná.

         (texto, nomeApresentacao, texto, nomeApresentador, nomeAutores, texto, nomeEvento, texto, dataEvento, texto, localEvento) - igual iftech e mostra robotica
         */

        String autores = a.getApresentadorNome();
        autores = autores + textoAutores(a.getAutores(), a.getApresentadorNome());

        setTipo("poster");
        texto1 = "Certificamos que o trabalho intitulado '" + a.getNometitulo() + "', dos autores ";
        texto2 = autores;
        texto3 = ", foi apresentado na modalidade Pôster, "
                + "durante o " + ev.getNome() + " do IFPR, realizado nos dias " + ev.getData() + ", em " + ev.getLocal() + ".";

        setTexto(texto1 + texto2 + texto3);

    }

    public void criarCertComunicacaoOral(apresentacao a, evento e) {
        /*
         MODALIDADE COMUNICAÇÃO ORAL

         Certificamos que Fulano de Tal apresentou o trabalho intitulado Nome do trabalho na modalidade Comunicação Oral durante o IV Seminário de Extensão, Ensino, Pesquisa e
         Inovação do IFPR, realizado nos dias 19 a 22 de outubro de 2015, em Paranaguá – Paraná.

         (texto, nomeApresentador, texto, nomeApresentacao, texto, nomeEvento, texto, datEvento, texto, localEvento)
         */

        setTipo("oral");
        texto1 = "Certificamos que ";
        texto2 = a.getApresentadorNome();
        texto3 = " apresentou o trabalho intitulado"
                + " '" + a.getNometitulo() + "' na modalidade Comunicação Oral, durante o " + e.getNome()
                + " do IFPR, realizado nos dias " + e.getData() + ", em " + e.getLocal() + ".";
        setTexto(texto1 + texto2 + texto3);

    }

    public void criarCertMostraIFTech(apresentacao a, evento e) {
        /*
         MODALIDADE MOSTRA IFTECH

         Certificamos que o trabalho intitulado Nome do trabalho, dos autores Apresentador,
         Suplente, Co-autor e Orientador, foi apresentado na modalidade Mostra IFTECH durante o IV
         Seminário de Extensão, Ensino, Pesquisa e Inovação do IFPR, realizado nos dias 19 a 22 de outubro de 2015, em Paranaguá – Paraná.

         (texto, nomeApresentacao, texto, nomeApresentador, nomeAutores, texto, nomeEvento, texto, dataEvento, texto, localEvento) - igual poster
         */
        String autores = a.getApresentadorNome();
        autores = autores + textoAutores(a.getAutores(), a.getApresentadorNome());

        setTipo("iftech");
        texto1 = "Certificamos que o trabalho intitulado '" + a.getNometitulo() + "', dos autores ";
        texto2 = autores;
        texto3 = ", foi apresentado na modalidade Mostra IFTech, durante o " + e.getNome()
                + " do IFPR, realizado nos dias " + e.getData() + ", em " + e.getLocal() + ".";

        setTexto(texto1 + texto2 + texto3);

    }

    public void criarCertMostraRobotica(apresentacao a, evento e) {
        /*
         MODALIDADE MOSTRA ROBÓTICA

         Certificamos que o trabalho intitulado Nome do trabalho, dos autores Apresentador, Suplente, Co-autor e Orientador, foi apresentado na modalidade Mostra de Robótica durante
         o IV Seminário de Extensão, Ensino, Pesquisa e Inovação do IFPR, realizado nos dias 19 a 22 de outubro de 2015, em Paranaguá – Paraná.

         (texto, nomeApresentacao, texto, nomeApresentador, nomeAutores, texto, nomeEvento, texto, dataEvento, texto, localEvento)  - igual poster
         */

        setTipo("robotica");

        String autores = a.getApresentadorNome();

        autores = autores + textoAutores(a.getAutores(), a.getApresentadorNome());

        texto1 = "Certificamos que o trabalho intitulado '" + a.getNometitulo() + "', dos autores ";
        texto2 = autores;
        texto3 = ", "
                + "foi apresentado na modalidade Mostra de Robótica durante o " + e.getNome()
                + " do IFPR, realizado nos dias " + e.getData() + ", em " + e.getLocal() + ".";

        setTexto(texto1 + texto2 + texto3);

    }

    public void criarCertApresentacaoCultural(apresentacao a, evento e) {
        /*
         MODALIDADE ATIVIDADE CULTURAL

         Certificamos que o grupo Nome do Grupo, composto por Fulano, Fulano e Fulano, realizou
         Atividade Cultural intitulada Nome da Atividade durante o IV Seminário de Extensão, Ensino,
         Pesquisa e Inovação do IFPR, realizado nos dias 19 a 22 de outubro de 2015, em Paranaguá – Paraná.

         */

        setTipo("cultural");

        String autores = a.getApresentadorNome();
        autores = autores + textoAutores(a.getAutores(), a.getApresentadorNome());

        texto1 = "Certificamos que o grupo composto por ";
        texto2 = autores;
        texto3 = ", "
                + "realizou Atividade Cultural intitulada '" + a.getNometitulo() + "' durante o " + e.getNome()
                + " do IFPR, realizado nos dias " + e.getData() + ", em " + e.getLocal() + ".";

        setTexto(texto1 + texto2 + texto3);

    }

    public void criarCertMonitores(String nome, String monitorMonitora, String doc, String unidadeNome, evento e) {
        /*MONITORES

         Certificamos que Fulano de Tal trabalhou como Monitor(a) no IV Seminário de Extensão,
         Ensino, Pesquisa e Inovação do IFPR, no período de 19 a 22 de outubro de 2015, 
         em Paranaguá – Paraná, com carga horária de 30 horas.*/

        evento = e.getId();
        setCpf(doc);
        setVerificador(gerarCodigoHexa());
        setNomeArq(unidadeNome + " Monitor " + nome);
        setTipo("monitor");

        texto1 = "Certificamos que ";
        texto2 = nome;
        texto3 = " trabalhou como " + monitorMonitora + " no "
                + e.getNome() + " do IFPR, no período de " + e.getData() + ", em " + e.getLocal() + ", "
                + "com carga horária de 30 horas.";

        setTexto(texto1 + texto2 + texto3);

        getGc().gerarCertificado(this);

    }

    public void criarCertPalestrante(String nome, String ministrouMinistraram, String palestra, String doc, evento e) {
        /*Certificamos que Fulano de Tal ministrou palestra intitulada Nome da Palestra 
         no IV Seminário de Extensão, Ensino, Pesquisa e Inovação do IFPR, 
         realizado nos dias 19 a 22 de outubro de 2015, em Paranaguá – Paraná.*/
        evento = e.getId();
        setCpf(doc);
        setVerificador(gerarCodigoHexa());
        setNomeArq("Palestrante " + nome + ". " + palestra);
        setTipo("palestrante");
        texto1 = "Certificamos que ";
        texto2 = nome;
        texto3 = " " + ministrouMinistraram + " palestra intitulada '" + palestra + "' no "
                + e.getNome() + " do IFPR, realizado nos dias " + e.getData() + ", em " + e.getLocal() + ".";

        setTexto(texto1 + texto2 + texto3);

        getGc().gerarCertificado(this);

    }

    public void criarCertOficina(String nome, String ministrouMinistraram, String oficina, String doc, evento e) {
        /*Certificamos que Fulano de Tal e outros ministrou/ministraram oficina 
         intitulada Nome da Oficina no 
         IV Seminário de Extensão, Ensino, Pesquisa e Inovação do IFPR, realizado nos dias 19 a 22 de outubro de 2015, em Paranaguá – Paraná.
         .*/
        evento = e.getId();
        setCpf(doc);
        setVerificador(gerarCodigoHexa());
        setNomeArq("Oficina " + oficina + ". " + nome);
        setTipo("oficina");
        texto1 = "Certificamos que ";
        texto2 = nome;
        texto3 = " " + ministrouMinistraram + " oficina intitulada '" + oficina + "' no "
                + e.getNome() + " do IFPR, realizado nos dias " + e.getData() + ", em " + e.getLocal() + ".";

        setTexto(texto1 + texto2 + texto3);

        getGc().gerarCertificado(this);

    }

    public void criarCertSessaoTematica(String nome, String ministrouMinistraram, String oficina, String doc, evento e) {
        /*Certificamos que Fulano de Tal e outros ministrou/ministraram sessão temática intitulada
         Nome da Sessão Temática no IV Seminário de Extensão, Ensino, Pesquisa e Inovação
         do IFPR, realizado nos dias 19 a 22 de outubro de 2015, em Paranaguá – Paraná.*/
        evento = e.getId();
        setCpf(doc);
        setVerificador(gerarCodigoHexa());
        setNomeArq("SessaoTem " + oficina + ". " + nome);
        setTipo("sessaoTematica");

        texto1 = "Certificamos que ";
        texto2 = nome;
        texto3 = " " + ministrouMinistraram + " Sessão Temática intitulada '" + oficina + "' no "
                + e.getNome() + " do IFPR, realizado nos dias " + e.getData() + ", em " + e.getLocal() + ".";

        setTexto(texto1 + texto2 + texto3);

        getGc().gerarCertificado(this);

    }

    public void gerarArquivo() {
        getGc().gerarCertificado(this);
    }

    public void criarCertAvaliadores(avaliador a, evento e) {
        /*
         AVALIADORES

         Certificamos que Fulano de Tal participou como Avaliador(a) de Trabalhos durante o IV
         Seminário de Extensão, Ensino, Pesquisa e Inovação do IFPR, realizado nos dias 19 a 22 de outubro de 2015, em Paranaguá – Paraná, com carga horária de XhXXm.*

         *a carga horária irá variar para os avaliadores que participarem também da comunicação oral.
         */
        evento = e.getId();

        int c = BancoDeDados.comando().selectCount("SELECT count(a.apresentacao) FROM avaliacao a, apresentacao ap "
                + "WHERE a.avaliador = '" + a.getCpf() + "' AND a.ativa = 1 AND a.apresentacao = ap.id AND ap.evento = '" + e.getId() + "';");
        setCpf(a.getCpf());
        if (c < 2) {
            
            if (a.getUnidadeCod() == 22) {
                gc.addFalha("Reitoria não avalia - " + a.getCpf() + " - " + a.getNome() + " ( " + a.getUnidadeNome() + " )");
                setFalha(true);
                return;
            }
            
            gc.addFalha("Sem avaliações suficientes (" + c + ") para " + a.getCpf() + " - " + a.getNome() + " ( " + a.getUnidadeNome() + " )");
            setFalha(true);
            return;
        }

        setVerificador(gerarCodigoHexa());
        setNomeArq(a.getNome() + " Avaliador " + a.getUnidadeNome());
        setTipo("avaliador");

        texto1 = "Certificamos que ";
        texto2 = a.getNome();
        texto3 = " participou como " + a.sexoExt() + " de Trabalhos durante o "
                + e.getNome() + " do IFPR, realizado nos dias " + e.getData() + ", em " + e.getLocal() + ", "
                + "com carga horária de " + a.getHorasCert() + "min e avaliou " + c + " trabalhos.";

        setTexto(texto1 + texto2 + texto3);

        getGc().gerarCertificado(this);

    }

        public void criarCertParticipacaoHardCoded(String nome, String unidade, String doc, String horas, evento e) {
        /*
         PARTICIPACAO
         
          Certificamos que NOMEAPRESENTADOR  participou do IV Seminário de Extensão, Ensino, Pesquisa e Inovação do IFPR, 
            no período de 19 a 22 de outubro de 2015
         , em Paranaguá – Paraná, com carga horária de 30 horas.

         (texto, nomeApresentador, texto, nomeEvento, texto, dataEvento, texto, localEvento, texto)*/
        setCpf(doc);
        evento = e.getId();

        setVerificador(gerarCodigoHexa());
        setTipo("apresentador");

        setNomeArq(nome + " Participacao " + unidade);
        texto1 = "Certificamos que ";
        texto2 = nome;
        texto3 = " participou do "
                + e.getNome() + " do IFPR, realizado nos dias " + e.getData() + ", em " + e.getLocal() + ", "
                + "com carga horária de " + horas + ".";

        setTexto(texto1 + texto2 + texto3);

        getGc().gerarCertificado(this);
    }
    
    
    
    public void criarCertAvaliadorHardCoded(String nome, String avaliadorAvaliadora, String unidade, String doc, String horas, evento e) {
        /*
         AVALIADORES

         Certificamos que Fulano de Tal participou como Avaliador(a) de Trabalhos durante o IV
         Seminário de Extensão, Ensino, Pesquisa e Inovação do IFPR, realizado nos dias 19 a 22 de outubro de 2015, em Paranaguá – Paraná, com carga horária de XhXXm.*

         *a carga horária irá variar para os avaliadores que participarem também da comunicação oral.
         */
        setCpf(doc);
        evento = e.getId();

        setVerificador(gerarCodigoHexa());
        setTipo("avaliador");

        setNomeArq(nome + " Avaliador " + unidade);
        texto1 = "Certificamos que ";
        texto2 = nome;
        texto3 = " participou como " + avaliadorAvaliadora + " de Trabalhos durante o "
                + e.getNome() + " do IFPR, realizado nos dias " + e.getData() + ", em " + e.getLocal() + ", "
                + "com carga horária de " + horas + ".";

        setTexto(texto1 + texto2 + texto3);

        getGc().gerarCertificado(this);
    }

    /**
     * @return the texto
     */
    public String getTexto() {
        return texto;
    }

    /**
     * @return the verificador
     */
    public String getVerificador() {
        return verificador;
    }

    /**
     * @return the tipo
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * @return the nomeArq
     */
    public String getNomeArq() {
        return nomeArq;
    }

    public String gerarCodigoHexa() {

        StringBuffer sb = new StringBuffer();
        Random rand = new Random();
        for (int i = 0; i < 12; i++) {
            sb.append(Integer.toString(Math.abs(rand.nextInt()) % 16, 16));
        }
        return sb.toString();
    }

    /**
     * @param texto the texto to set
     */
    public void setTexto(String texto) {
        this.texto = texto;
    }

    /**
     * @param verificador the verificador to set
     */
    public void setVerificador(String verificador) {
        this.verificador = verificador;
    }

    /**
     * @param nomeArq the nomeArq to set
     */
    public void setNomeArq(String nomeArq) {
        this.nomeArq = nomeArq;
    }

    /**
     * @return the cpf
     */
    public String getCpf() {
        return cpf;
    }

    /**
     * @param cpf the cpf to set
     */
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    /**
     * @param tipo the tipo to set
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    /**
     * @return the gc
     */
    public geradorCertificados getGc() {
        return gc;
    }

    /**
     * @param gc the gc to set
     */
    public void setGc(geradorCertificados gc) {
        this.gc = gc;
    }

    /**
     * @return the veioDoBD
     */
    public boolean isVeioDoBD() {
        return veioDoBD;
    }

    /**
     * @param veioDoBD the veioDoBD to set
     */
    public void setVeioDoBD(boolean veioDoBD) {
        this.veioDoBD = veioDoBD;
    }

    /**
     * @return the texto1
     */
    public String getTexto1() {
        return texto1;
    }

    /**
     * @param texto1 the texto1 to set
     */
    public void setTexto1(String texto1) {
        this.texto1 = texto1;
    }

    /**
     * @return the texto2
     */
    public String getTexto2() {
        return texto2;
    }

    /**
     * @param texto2 the texto2 to set
     */
    public void setTexto2(String texto2) {
        this.texto2 = texto2;
    }

    /**
     * @return the texto3
     */
    public String getTexto3() {
        return texto3;
    }

    /**
     * @param texto3 the texto3 to set
     */
    public void setTexto3(String texto3) {
        this.texto3 = texto3;
    }

    public String textoAutores(ArrayList<autor> aut, String tabu) {
        String t = "";

        for (int a = 0; a < aut.size(); a++) {
            if (aut.get(a).getNome().toUpperCase().equals("NÃO HÁ") || aut.get(a).getNome().equals(tabu)) {
                aut.remove(a);
            }
        }

        for (int a = 0; a < aut.size(); a++) {
            for (int b = a; b < aut.size(); b++) {
                if (aut.get(a).getNome().equals(aut.get(b).getNome()) && (a != b)) { //nomes iguais, indices(autores) diferentes. Retira 
                    aut.remove(b);
                    b = a;
                }
            }
        }

        for (int a = 0; a < aut.size(); a++) {
            if (a < (aut.size() - 1)) {
                t = t + ", " + aut.get(a).getNome();
            } else {
                t = t + " e " + aut.get(a).getNome();
            }
        }

        return t;
    }

    /**
     * @return the falha
     */
    public boolean isFalha() {
        return falha;
    }

    /**
     * @param falha the falha to set
     */
    public void setFalha(boolean falha) {
        this.falha = falha;
    }

    public String getTipoNomeBonito() {

        if (tipo.equals("avaliador")) {
            return "Avaliador";
        } else if (tipo.equals("apresentador")) {
            return "Participação";
        } else if (tipo.equals("monitor")) {
            return "Monitor";
        } else if (tipo.equals("palestrante")) {
            return "Palestrante";
        } else if (tipo.equals("oficina")) {
            return "Oficina";
        } else if (tipo.equals("sessaoTematica")) {
            return "Sessão Temática";
        } else if (tipo.equals("iftech")) {
            return "IFTech";
        } else if (tipo.equals("robotica")) {
            return "Robótica";
        } else if (tipo.equals("poster")) {
            return "Poster";
        } else if (tipo.equals("oral")) {
            return "Comunicação Oral";
        }

        return tipo;
    }

    public void reprocessar(boolean salvarBD) {

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

   

    public String getCpfSemID() {
        
        String [] c;
        
        if(!cpf.contains("."))
            return cpf;
        else
           c = cpf.split(".");
        
        if(c.length<1)
            return cpf;
        
        return c[0];
    }

}
