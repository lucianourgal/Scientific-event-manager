/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package objetosBase;

import ClassesAuxiliares.Arrumador;
import ClassesAuxiliares.BancoDeDados;
import ClassesAuxiliares.mailSender;
import java.util.ArrayList;
import objetosBase.triviais.coordenador;

/**
 *
 * @author Serac02
 */
public class apresentacao {

    private int categoriaCod;
    private boolean areaTematica;
    private boolean grandeArea;
    private String linkParaVideo;
    private String nometitulo;
    private String grupoNome;
    private String resumo;
    private String categoriaNome;
    private String palavrasChave;
    private String materiais;
    private String apresentadorCpf;
    private String apresentadorNome;
    private int modalidadeCod;
    private String modalidadeNome;
    private String subareaNome;
    private String areaNome;
    private String eventoNome;
    private String bolsaApresentadorNome;
    private int bolsaApresentadorCod = 1;
    private int id;
    private int subareaCod;
    private String eventoCod;
    private int unidadeCod = -1;
    private int areaCod;
    
    private String inclusao = "-";
    private String diversidade = "-";
    
    private ArrayList<autor> autores;
    private String unidadeNome;
    private String apresentadorEmail;
    private int selecionado, selecionadoInicial;
    private int indiceDoComboBoxDeApresentador = -1;
    private String NomeDoApresentadorDoComboBox;
    private int tematicaExtensaoCod = -1;

    private int hospedagens, hospedagensInicial, alimentacoes, alimentacoesInicial, transportes, transportesInicial, apresentadorEfetivoID, apresentadorEfetivoIDInicial, mediaNotas, nAvaliacoes;
    private String apresentadorEfetivoNome, apresentadorEfetivoNomeInicial, apresentadorEfetivoCPF, apresentadorEfetivoCPFInicial;
    private String capatazNome;

    public void descobrirIDApresentadorEfetivo() {
        if (indiceDoComboBoxDeApresentador == 0) {
            apresentadorEfetivoID = -1;
        } else {
            apresentadorEfetivoID = autores.get(indiceDoComboBoxDeApresentador - 1).getId();
        }

    }

    public String getSelecionadoTexto() {

        if (selecionado > 0) {
            return "Sim";
        } else {
            return "Não";
        }

    }

    public int getIndiceComboBoxApresentadorEfetivo() {

        if (apresentadorEfetivoID < 0) {
            return 0;
        }

        for (int a = 0; a < autores.size(); a++) {
            if (autores.get(a).getId() == apresentadorEfetivoID) {
                return (a + 1);
            }
        }

        System.out.println("Não conseguiu achar o autor que será o apresentador substituto! " + apresentadorEfetivoID);
        return -1;

    }

    public void descobrirCpfdoApresentadorEfetivoAPartirDoId() {

        if (apresentadorEfetivoID == -1) {
            apresentadorEfetivoCPF = apresentadorCpf;
        } else if (apresentadorEfetivoID == -123) {
            apresentadorEfetivoCPF = "n";
        } else {
            for (autor at : autores) {
                if (apresentadorEfetivoID == at.getId()) {
                    apresentadorEfetivoCPF = at.getCpf();
                }
            }
        }
    }

    public String getNometitulo() {
        return nometitulo;
    }

    public void setNometitulo(String nometitulo) {
        this.nometitulo = nometitulo;
    }

    public String getGrupoNome() {
        return grupoNome;
    }

    public void setGrupoNome(String grupoNome) {
        this.grupoNome = grupoNome;
    }

    public String getResumo() {
        return resumo;
    }

    public void setResumo(String resumo) {
        this.resumo = resumo;
    }

    public String getPalavrasChave() {
        return palavrasChave;
    }

    public void setPalavrasChave(String palavrasChave) {
        this.palavrasChave = palavrasChave;
    }

    public String getMateriais() {
        return materiais;
    }

    public void setMateriais(String materiais) {
        this.materiais = materiais;
    }

    public String getApresentadorCpf() {
        return apresentadorCpf;
    }

    public void setApresentadorCpf(String apresentadorCpf) {
        this.apresentadorCpf = apresentadorCpf;
        apresentadorEfetivoCPF = apresentadorCpf;
    }

    public String getApresentadorNome() {
        return apresentadorNome;
    }

    public void setApresentadorNome(String apresentadorNome) {
        this.apresentadorNome = apresentadorNome;
    }

    public String getSubareaNome() {
        return subareaNome;
    }

    public void setSubareaNome(String subareaNome) {
        this.subareaNome = subareaNome;
    }

    public String getEventoNome() {
        return eventoNome;
    }

    public void setEventoNome(String eventoNome) {
        this.eventoNome = eventoNome;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSubareaCod() {
        return subareaCod;
    }

    public void setSubareaCod(int subareaCod) {
        this.subareaCod = subareaCod;
    }

    public String getEventoCod() {
        return eventoCod;
    }

    public void setEventoCod(String eventoCod) {
        this.eventoCod = eventoCod;
    }

    public int getUnidadeCod() {
        return unidadeCod;
    }

    public void setUnidadeCod(int unidadeCod) {
        this.unidadeCod = unidadeCod;
    }

    public ArrayList<autor> getAutores() {
        return autores;
    }

    public void setAutores(ArrayList<autor> autores) {
        this.autores = autores;
    }

    /**
     * @return the modalidadeCod
     */
    public String getModalidadeNome() {
        return modalidadeNome;
    }

    /**
     * @param modalidadeCod the modalidadeCod to set
     */
    public void setModalidadeNome(String modalidadeCod) {
        this.modalidadeNome = modalidadeCod;
    }

    /**
     * @return the modalidadeNome
     */
    public int getModalidadeCod() {
        return modalidadeCod;
    }

    /**
     * @param modalidadeNome the modalidadeNome to set
     */
    public void setModalidadeCod(int modalidadeNome) {
        this.modalidadeCod = modalidadeNome;
    }

    /**
     * @return the bolsaApresentadorNome
     */
    public String getBolsaApresentadorNome() {
        return bolsaApresentadorNome;
    }

    /**
     * @param bolsaApresentadorNome the bolsaApresentadorNome to set
     */
    public void setBolsaApresentadorNome(String bolsaApresentadorNome) {
        this.bolsaApresentadorNome = bolsaApresentadorNome;
    }

    /**
     * @return the bolsaApresentadorCod
     */
    public int getBolsaApresentadorCod() {
        return bolsaApresentadorCod;
    }

    /**
     * @param bolsaApresentadorCod the bolsaApresentadorCod to set
     */
    public void setBolsaApresentadorCod(int bolsaApresentadorCod) {
        this.bolsaApresentadorCod = bolsaApresentadorCod;
        System.out.println("Bolsa do apresentador setada como " + bolsaApresentadorCod);
    }

    public boolean excluirAutores(ArrayList<Integer> autoresAExcluir) {
        return BancoDeDados.comando().excluirAutores(autoresAExcluir, getId());
    }

    public boolean atualizarNaBase() {
        return BancoDeDados.comando().atualizarApresentacao(this);
    }

    public boolean inserirNaBase() {
        boolean r = BancoDeDados.comando().cadastrarApresentacao(this);
        id = BancoDeDados.comando().selectCount("SELECT MAX(id) FROM apresentacao;");
        return r;
    }

    public String getAreaNome() {
        return areaNome;
    }

    public void setAreaNome(String areaNome) {
        this.areaNome = areaNome;
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
     * @return the apresentadorEmail
     */
    public String getApresentadorEmail() {
        return apresentadorEmail;
    }

    public String getApresentadorEmailAnais() {
        if (apresentadorEmail.length() > 5) {
            return "(" + apresentadorEmail.toLowerCase() + ") ";
        } else {
            return "";
        }
    }

    /**
     * @param apresentadorEmail the apresentadorEmail to set
     */
    public void setApresentadorEmail(String apresentadorEmail) {
        this.apresentadorEmail = apresentadorEmail;
    }

    /**
     * @return the areaCod
     */
    public int getAreaCod() {
        return areaCod;
    }

    /**
     * @param areaCod the areaCod to set
     */
    public void setAreaCod(int areaCod) {
        this.areaCod = areaCod;
    }

    /**
     * @return the hospedagens
     */
    public int getHospedagens() {
        return hospedagens;
    }

    /**
     * @param hospedagens the hospedagens to set
     */
    public void setHospedagens(int hospedagens) {
        this.hospedagens = hospedagens;
    }

    /**
     * @return the alimentacoes
     */
    public int getAlimentacoes() {
        return alimentacoes;
    }

    /**
     * @param alimentacoes the alimentacoes to set
     */
    public void setAlimentacoes(int alimentacoes) {
        this.alimentacoes = alimentacoes;
    }

    /**
     * @return the transportes
     */
    public int getTransportes() {
        return transportes;
    }

    /**
     * @param transportes the transportes to set
     */
    public void setTransportes(int transportes) {
        this.transportes = transportes;
    }

    /**
     * @return the apresentadorEfetivoID
     */
    public int getApresentadorEfetivoID() {
        if (getApresentadorEfetivoNome() == null) {
            return -1;
        }
        return apresentadorEfetivoID;
    }

    /**
     * @param apresentadorEfetivoID the apresentadorEfetivoID to set
     */
    public void setApresentadorEfetivoID(int apresentadorEfetivoID) {
        this.apresentadorEfetivoID = apresentadorEfetivoID;
    }

    public int getNumeroMaximoHospedagens() {
        if (getModalidadeNome().equals("Apresentacao cultural")) {
            return getAutores().size() + 1;
        }
        return 1;
    }

    /**
     * @return the apresentadorEfetivoNome
     */
    public String getApresentadorEfetivoNome() {
        if (apresentadorEfetivoNome == null) {
            return getApresentadorNome();
        }
        return apresentadorEfetivoNome;
    }

    /**
     * @param apresentadorEfetivoNome the apresentadorEfetivoNome to set
     */
    public void setApresentadorEfetivoNome(String apresentadorEfetivoNome) {
        this.apresentadorEfetivoNome = apresentadorEfetivoNome;
    }

    /**
     * @return the apresentadorEfetivoCPF
     */
    public String getApresentadorEfetivoCPF() {
        return apresentadorEfetivoCPF;
    }

    /**
     * @param apresentadorEfetivoCPF the apresentadorEfetivoCPF to set
     */
    public void setApresentadorEfetivoCPF(String apresentadorEfetivoCPF) {
        this.apresentadorEfetivoCPF = apresentadorEfetivoCPF;
    }

    /**
     * @return the mediaNotas
     */
    public int getMediaNotas() {
        return mediaNotas;
    }

    /**
     * @param mediaNotas the mediaNotas to set
     */
    public void setMediaNotas(int mediaNotas) {
        this.mediaNotas = mediaNotas;
    }

    /**
     * @return the nAvaliacoes
     */
    public int getnAvaliacoes() {
        return nAvaliacoes;
    }

    /**
     * @param nAvaliacoes the nAvaliacoes to set
     */
    public void setnAvaliacoes(int nAvaliacoes) {
        this.nAvaliacoes = nAvaliacoes;
    }

    /**
     * @param selecionads Atualiza no BD, para selecionado, quem é o
     * apresentador, quantos recursos serão usados IndiceApresentad é 0 para
     * apresentador padrao, e 1 para o primeiro autor, 2 para o segundo
     * @deprecated
     */
    public boolean atualizarSelecao(boolean selecionads, int indiceApresentad, int hospedagem, int transporte, int alimentacao) {

        /*  if (selecionads) {
         setSelecionado(1);
         } else {
         setSelecionado(0);
         }

         //indice apr :?????
         if (indiceApresentad == 0) {  // se é o apresentador original
         setApresentadorEfetivoNome(getApresentadorNome());
         setApresentadorEfetivoCPF(getApresentadorCpf());
         setApresentadorEfetivoID(-1);
         } else {  // se será um dos autores
         setApresentadorEfetivoNome(getAutores().get(indiceApresentad - 1).getNome());
         setApresentadorEfetivoCPF(getAutores().get(indiceApresentad - 1).getCpf());
         setApresentadorEfetivoID(getAutores().get(indiceApresentad - 1).getId());
         }

         this.setHospedagens(hospedagem);
         this.setTransportes(transporte);
         this.setAlimentacoes(alimentacao);
         */
        System.out.println("NÃO UTILIZAR ESSE METODO");
        //BancoDeDados.comando().atualizarSelecaoApresentacao(this);
        return false;
    }

    /**
     * @return the selecionado
     */
    public int getSelecionado() {
        return selecionado;
    }

    /**
     * @param selecionado the selecionado to set
     */
    public void setSelecionado(int selecionado) {
        this.selecionado = selecionado;
    }

    public void estaSelecionada(boolean selected) {

        if (selected) {
            setSelecionado(1);
        } else {
            setSelecionado(0);
        }

    }

    public void setIndiceDoComboBoxDeApresentador(int selectedIndex) {
        indiceDoComboBoxDeApresentador = selectedIndex;
        descobrirIDApresentadorEfetivo();
    }

    /**
     * @return the indiceDoComboBoxDeApresentador
     */
    public int getIndiceDoComboBoxDeApresentador() {
        return indiceDoComboBoxDeApresentador;
    }

    /**
     * @return the NomeDoApresentadorDoComboBox
     */
    public String getNomeDoApresentadorDoComboBox() {
        return NomeDoApresentadorDoComboBox;
    }

    /**
     * @param NomeDoApresentadorDoComboBox the NomeDoApresentadorDoComboBox to
     * set
     */
    public void setNomeDoApresentadorDoComboBox(String NomeDoApresentadorDoComboBox) {
        this.NomeDoApresentadorDoComboBox = NomeDoApresentadorDoComboBox;
    }

    public boolean atualizarApresentacaoModoDeSelecao() {

        if (naoHouveramMudancasNestaApresentacao()) {
            System.out.println("Nao houveram mudanças na apresentacao " + id);
            return true;
        }

        boolean r = BancoDeDados.comando().atualizarApresentacaoModoDeSelecao(this);

        if (modalidadeCod != 4) {
            atualizarAtivacaoAutores();
        }

        if (r) {
            System.out.println("Atualizou apresentacao de ID " + id + "!");
        }

        return r;

    }

    public boolean atualizarAtivacaoAutores() {

        //apresentadorEfetivoID  apresentadorEfetivoIDInicial
        // se não houver mudança, return true;
        if (apresentadorEfetivoID == apresentadorEfetivoIDInicial) {
            return true;
        }

        // se apresentadorEfetivoIDInicial == -1
        // apenas setar o novo como ativo
        if (apresentadorEfetivoIDInicial == -1) {

            if (apresentadorEfetivoID != -123) // se não é o caso de apr. principal estar deixando de ir, é um autor assumindo
            {
                return BancoDeDados.comando().setAutorComoAtivo(apresentadorEfetivoID);
            } else // caso seja apr. principal de apr. cultural deixando de ir
            {
                return BancoDeDados.comando().setApresentadorEfetivoApresentacao(id, -123);
            }

        }

        // se era != -1
        //  setar antigo como inativo    ( para não AC )
        // setar o novo como ativo, a menos que seja -1  ( para não AC )
        if (apresentadorEfetivoID > -1) {
            BancoDeDados.comando().setAutorComoAtivo(apresentadorEfetivoID); //autor assumindo ( não pode ser o -1 nem -123)
        }
        if (apresentadorEfetivoIDInicial > -1) {
            return BancoDeDados.comando().setAutorComoInativo(apresentadorEfetivoIDInicial); // autor desistindo ( não pode ter sido o -123 )
        }          // caso seja o apr. principal de apr. cultural passando a ir
        if (apresentadorEfetivoIDInicial == -123 && apresentadorEfetivoID == -1) {
            return BancoDeDados.comando().setApresentadorEfetivoApresentacao(id, -1);
        }

        return true;

        //  return false;
    }

    public boolean naoHouveramMudancasNestaApresentacao() {

        if (selecionado != selecionadoInicial) {
            return false;
        }
        if (transportes != transportesInicial) {
            return false;
        }
        if (hospedagens != hospedagensInicial) {
            return false;
        }
        if (alimentacoes != alimentacoesInicial) {
            return false;
        }
        /* if (!apresentadorEfetivoCPF.equals(apresentadorEfetivoCPFInicial)) {
         return false;
         }
         if (!apresentadorEfetivoNome.equals(apresentadorEfetivoNomeInicial)) {
         return false;
         }*/
        return (apresentadorEfetivoID == apresentadorEfetivoIDInicial);

    }

    /**
     * @return the hospedagensInicial
     */
    public int getHospedagensInicial() {
        return hospedagensInicial;
    }

    /**
     * @param hospedagensInicial the hospedagensInicial to set
     */
    public void setHospedagensInicial(int hospedagensInicial) {
        this.hospedagensInicial = hospedagensInicial;
        hospedagens = hospedagensInicial;
    }

    /**
     * @return the alimentacoesInicial
     */
    public int getAlimentacoesInicial() {
        return alimentacoesInicial;
    }

    /**
     * @param alimentacoesInicial the alimentacoesInicial to set
     */
    public void setAlimentacoesInicial(int alimentacoesInicial) {
        this.alimentacoesInicial = alimentacoesInicial;
        alimentacoes = alimentacoesInicial;
    }

    /**
     * @return the transportesInicial
     */
    public int getTransportesInicial() {
        return transportesInicial;
    }

    /**
     * @param transportesInicial the transportesInicial to set
     */
    public void setTransportesInicial(int transportesInicial) {
        this.transportesInicial = transportesInicial;
        transportes = transportesInicial;
    }

    /**
     * @return the apresentadorEfetivoIDInicial
     */
    public int getApresentadorEfetivoIDInicial() {
        return apresentadorEfetivoIDInicial;
    }

    /**
     * @param apresentadorEfetivoIDInicial the apresentadorEfetivoIDInicial to
     * set
     */
    public void setApresentadorEfetivoIDInicial(int apresentadorEfetivoIDInicial) {
        this.apresentadorEfetivoIDInicial = apresentadorEfetivoIDInicial;
        setApresentadorEfetivoID(apresentadorEfetivoIDInicial);
    }

    /**
     * @return the apresentadorEfetivoNomeInicial
     */
    public String getApresentadorEfetivoNomeInicial() {
        return apresentadorEfetivoNomeInicial;
    }

    /**
     * @param apresentadorEfetivoNomeInicial the apresentadorEfetivoNomeInicial
     * to set
     */
    public void setApresentadorEfetivoNomeInicial(String apresentadorEfetivoNomeInicial) {
        this.apresentadorEfetivoNomeInicial = apresentadorEfetivoNomeInicial;
        apresentadorEfetivoNome = apresentadorEfetivoNomeInicial;
    }

    /**
     * @return the apresentadorEfetivoCPFInicial
     */
    public String getApresentadorEfetivoCPFInicial() {
        return apresentadorEfetivoCPFInicial;
    }

    /**
     * @param apresentadorEfetivoCPFInicial the apresentadorEfetivoCPFInicial to
     * set
     */
    public void setApresentadorEfetivoCPFInicial(String apresentadorEfetivoCPFInicial) {
        this.apresentadorEfetivoCPFInicial = apresentadorEfetivoCPFInicial;
        apresentadorEfetivoCPF = apresentadorEfetivoCPFInicial;
    }

    /**
     * @return the selecionadoInicial
     */
    public int getSelecionadoInicial() {
        return selecionadoInicial;
    }

    /**
     * @param selecionadoInicial the selecionadoInicial to set
     */
    public void setSelecionadoInicial(int selecionadoInicial) {
        this.selecionadoInicial = selecionadoInicial;
        selecionado = selecionadoInicial;
    }

    /**
     * @return the linkParaVideo
     */
    public String getLinkParaVideo() {
        return linkParaVideo;
    }

    /**
     * @param linkParaVideo the linkParaVideo to set
     */
    public void setLinkParaVideo(String linkParaVideo) {
        this.linkParaVideo = linkParaVideo;
    }

    /**
     * @return the tematicaExtensaoCod
     */
    public int getTematicaExtensaoCod() {
        return tematicaExtensaoCod;
    }

    /**
     * @param tematicaExtensaoCod the tematicaExtensaoCod to set
     */
    public void setTematicaExtensaoCod(int tematicaExtensaoCod) {
        this.tematicaExtensaoCod = tematicaExtensaoCod;
        System.out.println("Setou tematica como " + tematicaExtensaoCod);
    }

    /**
     * @return the categoriaCod
     */
    public int getCategoriaCod() {
        return categoriaCod;
    }

    /**
     * @param categoriaCod the categoriaCod to set
     */
    public void setCategoriaCod(int categoriaCod) {
        this.categoriaCod = categoriaCod;
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

    public int getAreaParaSelecaoAvaliadores() {

        if (grandeArea) { //Tematica 0: Grandes áreas
            return areaCod;
        } else if (areaTematica) { //Tematica 1
            return tematicaExtensaoCod;
        } else if (areaCod > 20) { // Tematica 2: Extensão
            return areaCod;
        } else {
            System.out.println("ERRO! Apresentacao (" + nometitulo + ") não tem area tematica e nem grande area como classificaçao de categoria (" + areaCod + " -  " + tematicaExtensaoCod + " )");
            return areaCod;
        }

    }

    /**
     * @return the categoriaNome
     */
    public String getCategoriaNome() {
        return categoriaNome;
    }

    /**
     * @param categoriaNome the categoriaNome to set
     */
    public void setCategoriaNome(String categoriaNome) {
        this.categoriaNome = categoriaNome;
    }

    public int isOral() {
        if (id < 0) {
            System.out.println("Apresentacao " + id + " é oral!");
            return 1;

        } else {
            System.out.println("Apresentacao " + id + " NÃO é oral!");
        }
        return 0;

    }

    public boolean autorViraApresentador(int autor) {
        autor novoApr = new autor();
        int index = -1;
        String apresentadorInical = apresentadorCpf;
        System.out.println("Iniciando troca de apresentador...");

        //encontra autor em questão
        for (int a = 0; a < autores.size(); a++) {
            if (autores.get(a).getId() == autor) {
                novoApr = autores.get(a);
                index = a;
            }
        }

        if (index == -1) {
            System.out.println("ABORT: Não encontrou autor.");
            return false;
        }
        if (novoApr.getCpf() == null) {
            System.out.println("ABORT: Autor não tem CPF.");
            return false;
        }

        //verifica se autor já é cadastrado como apresentador
        int jaApresentador = BancoDeDados.comando().selectCount("SELECT count(nome) FROM apresentador WHERE cpf = '" + novoApr.getCpf() + "';");

        //IF: cadastra autor como apresentador se necessário
        apresentador novoAprBD;
        novoAprBD = BancoDeDados.comando().getApresentador(apresentadorCpf);
        novoAprBD.setarDadosAutor(novoApr);
        novoAprBD.setUnidade(novoApr.getUnidadeNome());
        novoAprBD.setCodigoUnidade(unidadeCod);
        novoAprBD.setSenha("sepin");

        novoAprBD.setDatadenascimento(Arrumador.comando().dataParaDATE(novoApr.getNascimento()));
        novoAprBD.setDataexprg(Arrumador.comando().dataParaDATE(novoApr.getNascimento()));

        if (jaApresentador == 0) {
            System.out.println("Autor que virou apresentador precisa ser cadastrado como apresentador...");

            if (novoAprBD.testeDadosBasicos()) {
                if (novoAprBD.inserirNaBase()) {
                    System.out.println("Autor que virou Apresentador inserido!");
                } else {
                    System.out.println("ABORT: Falha no momento da inserção de autor que virou apresentador");
                    return false;
                }
            } else {
                System.out.println("ABORT: Não passou no teste de dados básicos para ser apresentador");
                return false;
            }
        } else {
            System.out.println("Autor que virou apresentador já está cadastrado como apresentador");
        }

        //verifica se atual apresentador tem outros trabalhos    
        int numeroTrabalhosApresentadorAtual = BancoDeDados.comando().selectCount("SELECT count(*) FROM apresentacao "
                + "WHERE apresentador = '" + apresentadorCpf + "';");
        System.out.println("Apresentador tem mais " + (numeroTrabalhosApresentadorAtual - 1) + " trabalhos além desse.");

        //atualiza dados do apresentador no registro do autor
        novoApr.setarDadosApresentador(apresentadorCpf);
        // novoApr.setAtivo(1);
        autores.set(index, novoApr);
        novoApr.atualizarSeMudouStatusAtivo();
        System.out.println("Atualizou dados de apresentador que virou autor em autor");

        //atualiza tabela de convênios: para apresentador (apresentacao, apresentador)
        if (BancoDeDados.comando().update("UPDATE convenio SET apresentador = '" + novoAprBD.getCpf() + "' "
                + "WHERE apresentacao = " + id + " AND apresentador = '" + apresentadorCpf + "';") > 0) {
            System.out.println("Atualizou dados de convenio de apresentador");
        } else {
            System.out.println("ABORT: Falha para atualizar dados de convenio de apresentador!");
        }

        //atualiza tabela apresentação: mudou o apresentador
        apresentadorCpf = novoAprBD.getCpf();
        this.atualizarNaBase();

        //IF: exclui apresentador antigo, se esse não tiver mais apresentações cadastradas  
        if (numeroTrabalhosApresentadorAtual > 1) {
            System.out.println("Não há necessidade de excluir apresentador antigo");
        } else {
            if (!(BancoDeDados.comando().update("DELETE FROM apresentador WHERE cpf = '" + apresentadorInical + "';") > 0)) {
                System.out.println("ABORT: Falha para apagar apresentador antigo (" + apresentadorInical + ") do BD");
                return false;
            } else {
                System.out.println("Apagou apresentador antigo (" + apresentadorInical + ") do BD");
            }
        }

        System.out.println("Atualização de apresentador com sucesso (ou não)!");
        return true;
    }

    public boolean excluirApresentacaoTotalmente(boolean reinserir, ArrayList<coordenador> cords) {
        /*1 - apagar todas as avaliações
         2 - apagar convenios
         3 - apagar autores
         4 - apagar apresentacao
         5 - apagar apresentador SE NÃO TIVER OUTRA APRESENTAÇÃO
         Fazer botão de “Tem certeza?” - Necessário dois cliques*/

        //(1) apagar avaliações       
        System.out.println("Apagou " + BancoDeDados.comando().update("DELETE FROM avaliacao WHERE apresentacao = '" + id + "';") + " "
                + "AVALIACOES da apresentação (" + id + ") do BD");

        //(2) apagar convênios
        if (!(BancoDeDados.comando().update("DELETE FROM convenio WHERE apresentacao = '" + id + "';") > 0)) {
            System.out.println("FAIL: Não apagou nenhuum CONVENIO da apresentação (" + id + ") do BD");
            //return;
        } else {
            System.out.println("Apagou CONVENIOS da apresentação (" + id + ") do BD");
        }

        //(3) apagar autores
        if (!(BancoDeDados.comando().update("DELETE FROM autor WHERE apresentacao = '" + id + "';") > 0)) {
            System.out.println("FAIL: Não apagou nenhum AUTOR da apresentação (" + id + ") do BD");
            //return;
        } else {
            System.out.println("Apagou AUTORES da apresentação (" + id + ") do BD");
        }

        //(4) apagar apresentação
        if (!(BancoDeDados.comando().update("DELETE FROM apresentacao WHERE id = '" + id + "';") > 0)) {
            System.out.println("ABORT: Falha para apagar apresentação (" + id + ") do BD");
            return false;
        } else {
            System.out.println("Apagou apresentação (" + id + ") do BD");
        }

        //(4 INF) Enviar email sobre exclusão da apresentação
        capatazNome = "";
        if(cords.size()>0){
        avaliador capataz = BancoDeDados.comando().recuperarAvaliador(cords.get(0).getAvaliador());
        capatazNome = capataz.getNome();
        }
        this.enviarEmailParaAutoresComInformacoes(false, true);
        
        
        //(5)verifica se atual apresentador tem outros trabalhos    
        int numeroTrabalhosApresentadorAtual = BancoDeDados.comando().selectCount("SELECT count(*) FROM apresentacao "
                + "WHERE apresentador = '" + apresentadorCpf + "';");
        System.out.println("Apresentador tem mais " + (numeroTrabalhosApresentadorAtual - 1) + " trabalhos além desse.");
        //IF: exclui apresentador antigo, se esse não tiver mais apresentações cadastradas  
        if (numeroTrabalhosApresentadorAtual > 1) {
            System.out.println("Não há necessidade de excluir apresentador.");
        } else {
            if (!(BancoDeDados.comando().update("DELETE FROM apresentador WHERE cpf = '" + apresentadorCpf + "';") > 0)) {
                System.out.println("ABORT: Falha para apagar apresentador antigo (" + apresentadorCpf + ") do BD");
                return false;
            } else {
                System.out.println("Apagou apresentador (" + apresentadorCpf + ") do BD");
            }
        }

        System.out.println("Terminou exclusão com sucesso!");

        if (reinserir) {
            this.inserirNaBase();
        }
        return true;
    }

    public void enviarEmailParaAutoresComInformacoes(boolean insert, boolean exclusao) {
        mailSender mail = new mailSender();
        apresentador apr = BancoDeDados.comando().getApresentador(apresentadorCpf);
        String titulo;

        if (insert) {
            titulo = "AUTO MESSAGE: Cadastro de <" + this.getNometitulo() + ", ID " + id + "> foi efetuado no sistema do SEPIN!";
        } else {
            titulo = "AUTO MESSAGE: Atualização de <" + this.getNometitulo() + ", ID " + id + "> efetuada no sistema do SEPIN!";
        }

        if (exclusao) {
            titulo = "AUTO MESSAGE: Exclusão de <" + this.getNometitulo() + ", ID " + id + "> do sistema do SEPIN por "+capatazNome;
        }

        String recebedores = "";
        if (apr.getEmail() != null) {
            recebedores = apr.getEmail();
        }
        for (autor a : autores) {
            if (a.getEmail() != null) {
                recebedores = recebedores + ", " + a.getEmail();
            }
        }

        if (unidadeCod >= 0) {
            recebedores = recebedores + BancoDeDados.comando().getEmailCoordenadorUnidade(unidadeCod,true);
            //observação: Virgula internalizada no método
        } else {
            recebedores = recebedores + BancoDeDados.comando().getEmailCoordenadorApresentador(apresentadorCpf,true);
            //observação: virgula internalizada no metodo
        }

        //Autores e suas funções, Categoria, modalidade, nome da área, palavras chave,  resumo  ++++ Aviso de como modificar a apresentação
        String corpo = "Título: " + this.getNometitulo() + "\n\n"
                + "Autores:\n" + apr.getNome() + ": Apresentador;\n";
        for (autor at : autores) {
            corpo = corpo + at.getNome() + ": " + at.getFuncaoNome() + ", Campus " + at.getUnidadeNome() + ";\n";
        }
        corpo = corpo + "\n";
        corpo = corpo + "Categoria: " + this.categoriaNome + ".\n";
        corpo = corpo + "Modalidade: " + this.modalidadeNome + ".\n";
        corpo = corpo + "Área: " + this.areaNome + "\n\n.";

        corpo = corpo + "Palavras Chave: " + this.palavrasChave + "\n";

        corpo = corpo + "Resumo: " + this.resumo + "\n\n\n";

        if (!exclusao) {
            corpo = corpo + "Reiteramos a necessidade do preenchimendo das informações de forma correta e completa, para futura geração de certificados e anais de evento.\n";
        }

        corpo = corpo + "É possível modificar dados de apresentações.\n\nAtenciosamente, Equipe do SEPIN";

        mail.sendMail(titulo, corpo, recebedores);

    }

    public void enviarEmailParaAutoresComInformacoesTESTE() {
        mailSender mail = new mailSender();
        apresentador apr = BancoDeDados.comando().getApresentador(apresentadorCpf);

        String titulo = "AUTO MESSAGE: Cadastro de <" + this.getNometitulo() + ", ID " + id + "> foi efetuado no sistema do SEPIN!";

        String recebedores = "";
        // if(apr.getEmail()!=null)
        recebedores = apresentadorEmail;
        for (autor a : autores) {
            if (a.getEmail() != null) {
                recebedores = recebedores + ", " + a.getEmail();
            }
        }

        //Autores e suas funções, Categoria, modalidade, nome da área, palavras chave,  resumo  ++++ Aviso de como modificar a apresentação
        String corpo = "Autores:\n" + apr.getNome() + ": Apresentador;\n";
        for (autor at : autores) {
            corpo = corpo + at.getNome() + ": " + at.getFuncaoNome() + ", " + at.getUnidadeNome() + "\n";
        }
        corpo = corpo + "\n";
        corpo = corpo + "Categoria: " + this.categoriaNome + "\n";
        corpo = corpo + "Modalidade: " + this.modalidadeNome + "\n";
        corpo = corpo + "Área: " + this.areaNome + "\n\n";

        corpo = corpo + "Palavras Chave: " + this.palavrasChave + "\n\n";

        corpo = corpo + "Resumo: " + this.resumo + "\n\n";

        corpo = corpo + "É possível modificar dados da apresentação. \nAtenciosamente, Equipe do SEPIN";

        mail.sendMail(titulo, corpo, recebedores);

    }

    /**
     * @return the inclusao
     */
    public String getInclusao() {
        return inclusao;
    }

    /**
     * @param inclusao the inclusao to set
     */
    public void setInclusao(String inclusao) {
        this.inclusao = inclusao;
    }

    /**
     * @return the diversidade
     */
    public String getDiversidade() {
        return diversidade;
    }

    /**
     * @param diversidade the diversidade to set
     */
    public void setDiversidade(String diversidade) {
        this.diversidade = diversidade;
    }

}
