/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ClassesAuxiliares;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import java.sql.*;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import objetosBase.apresentacao;
import objetosBase.apresentador;
import objetosBase.autor;
import objetosBase.avaliacao;
import objetosBase.avaliador;
import objetosBase.certificado;
import objetosBase.oficinaSessaoMonitor;
import objetosBase.rankingApresentacao;
import objetosBase.relatorioUnidade;
import objetosBase.triviais.area;
import objetosBase.triviais.bolsa;
import objetosBase.triviais.categoria;
import objetosBase.triviais.coordenador;
import objetosBase.triviais.evento;
import objetosBase.triviais.funcao;
import objetosBase.triviais.modalidade;
import objetosBase.triviais.relatorioApresentacoesPorApresentador;
import objetosBase.triviais.subarea;
import objetosBase.triviais.ticket;
import objetosBase.triviais.tipopessoa;
import objetosBase.triviais.unidade;

/**
 *
 * @author Luciano
 */
public final class BancoDeDados {
/////////////////////////////Definiçoes_do_Singleton////////////////////////////

    int retryConnection = 50;
    private static BancoDeDados OBJETO;

    public static BancoDeDados comando() {
        if (OBJETO == null) {
            OBJETO = new BancoDeDados();
        }
        return OBJETO;
    }
////////////////////////////////////////////////////////////////////////////////

    private final String url = "jdbc:mysql://127.0.0.1:3306/sepin";
    private final String login = "yourlogin";
    private final String senha = "yourpassword2";

    private final String driver = "com.mysql.jdbc.Driver";
    private Connection con;
    private Statement stmt;
    private final String setSchema = "";
    private Session session;

    //atributos ssh
    static int lport;
    static String rhost;
    static int rport;
    private double centralizado;
    private boolean falha = false;
    private boolean restricaoResumo = false;
    private int minimoResumo;
    private int maximoResumo;
    private String versao;
    private boolean apresentadorTemSenha;
    private boolean telaInicialSimples;
    private boolean acessoApresentador;
    private boolean bloqueioEdicaoGeral;
    private boolean bloqueiAreaApresentador;
    private evento eventoAtual;
    private boolean avaliadorTemSenha;
    private boolean coordenadorCampusPodeExcluirApresentacao;
    private boolean podeResetarSenha;
    private boolean usaCodigoDeCadastro;

    public void sshtunnel() {
        String user = "youruser"
        String password = "yourpassword";
        String host = "127.0.0.1";
        int port = 22;
        try {
            JSch jsch = new JSch();
            session = jsch.getSession(user, host, port);
            lport = 3306;
            rhost = host;//"localhost";
            rport = 3306;

            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            System.out.println("Establishing Connection...");
            session.connect();
            int assinged_port = session.setPortForwardingL(lport, rhost, rport);
            System.out.println("Tunel SSH: O que for recebido em 'localhost:" + assinged_port + "' é direcionado para '" + rhost + ":" + rport + "'");

        } catch (JSchException e) {
            falha = true;
            System.err.print(e);
        }
    }

    public boolean connectionFailed() {
        return falha;
    }

    /**
     * Para inserir ou atualizar no BD
     *
     * @param q
     * @return sucesso
     */
    public int insert(String q) {
        return updateOUinsert(q);
    }

    public int update(String q) {
        return updateOUinsert(q);
    }

    public int updateOUinsert(String query) {
        int retorno = 0;
        try {
            retorno = stmt.executeUpdate(this.setSchema + query);
        } catch (SQLException ex) {
            ex.printStackTrace();
            try {
                if (retryConnection < 1) {
                    return -1;
                }
                System.out.println("Reiniciando conexao... (update)");
                Thread.sleep(500);
                criarConexao();
                retryConnection--;
                retorno = updateOUinsert(query);
            } catch (SQLException ex1) {
                ex1.printStackTrace();
            } catch (InterruptedException ex1) {
                Arrumador.comando().salvarTXT("Erro de conexao registrado", "Falha de conexão ao BD.");
                ex1.printStackTrace();
            }
        }
        return retorno;
    }

    /**
     * Para fazer um select no bd
     *
     * @param query
     * @return resultset
     */
    public ResultSet select(String query) {
        ResultSet select = null;
        try {
            select = stmt.executeQuery(this.setSchema + query);
            return select;
        } catch (SQLException ex) {
            Logger.getLogger(BancoDeDados.class.getName()).log(Level.SEVERE, null, ex);
            try {
                if (retryConnection < 1) {
                    return null;
                }
                Thread.sleep(250);
                System.out.println("Reiniciando conexao... (select)");
                criarConexao();
                retryConnection--;
                select = select(query);

            } catch (SQLException ex1) {
                Logger.getLogger(BancoDeDados.class.getName()).log(Level.SEVERE, null, ex1);
            } catch (InterruptedException ex1) {
                Logger.getLogger(BancoDeDados.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        return select;
    }

    /**
     * @throws SQLException
     */
    public void fecharStatement() throws SQLException {
        this.stmt.close();
    }

    /**
     *
     * @throws SQLException
     */
    public void fecharConexao() throws SQLException {

        try {
            this.con.close();
            session.disconnect();
            System.out.println("Connection closed");
        } catch (SQLException e) {
            System.out.println("Connection failed to close");
        }

    }

    /**
     * Ao iniciar o programa
     *
     * @throws SQLException
     */
    public void criarConexao() throws SQLException {

        sshtunnel();

        try {
            Class.forName(driver);
        } catch (java.lang.ClassNotFoundException e) {
            System.err.print("ClassNotFoundException: ");
            System.err.println(e.getMessage());
        }
        this.con = DriverManager.getConnection(url, login, senha);

        System.out.println("URL do Banco de dados: '" + url + "'");
        criarStatement();
        System.out.println("Conectado!");
    }

    public void criarStatement() throws SQLException {
        this.stmt = con.createStatement();
    }

    public void recuperarValoresBase() {
        System.out.println("Recuperando valores...");
        ResultSet rs = getValoresBase();
        String rotulo;
        int valor1, valor2;

        try {

            while (rs.next()) {
                rotulo = rs.getString("rotulo");
                valor1 = rs.getInt("valor1");
                valor2 = rs.getInt("valor2");
                recuperar(rotulo, valor1, valor2);
            }

        } catch (SQLException e) {
        }
    }

    public ResultSet getValoresBase() {
        ResultSet rs;

        rs = select("SELECT * FROM valores;");

        return rs;
    }

    public void recuperar(String rotulo, int valor1, int valor2) {
        switch (rotulo) {
            case "centralizado":
                centralizado = valor1 + ((double) valor2) / 100;
                break;
            case "restricaoresumo":
                restricaoResumo = valor1 > 0;
                break;
            case "tamanhoresumo":
                minimoResumo = valor1;
                maximoResumo = valor2;
                break;
            case "versao":
                versao = valor1 + "." + valor2;
                break;
            case "senhaapresentador":
                apresentadorTemSenha = valor1 > 0;
                break;
            case "senhaavaliador":
                avaliadorTemSenha = valor1 > 0;
                break;
            case "telainicialsimples":
                telaInicialSimples = valor1 > 0;
                break;
            case "bloqueioedicaoapresentacao":
                bloqueioEdicaoGeral = valor1 > 0;
                break;
            case "acessoapresentador":
                acessoApresentador = valor1 > 0;
                break;
            case "acessoapresentacaocoordenador":
                bloqueiAreaApresentador = valor1 < 1;
                break;
            case "coordenadorcampuspodeexcluir":
                coordenadorCampusPodeExcluirApresentacao = valor1 < 1;
                break;
            case "poderesetarsenha":
                podeResetarSenha = valor1 > 0;
                break;
            case "usacodigosdecadastro":
                setUsaCodigoDeCadastro(valor1 > 0);
                break;

        }

    }

    public boolean usaCodigoDeCadastro() {
        return isUsaCodigoDeCadastro();
    }

    public boolean podeResetarSenha() {
        return podeResetarSenha;
    }

    public boolean telaInicialSimples() {
        return telaInicialSimples;
    }

    public boolean acessoApresentadorPermitido() {
        return acessoApresentador;
    }

    public boolean apresentadorTemSenha() {
        return apresentadorTemSenha;
    }

    public boolean avaliadorTemSenha() {
        return avaliadorTemSenha;
    }

    public int getMinimoResumo() {
        return minimoResumo;
    }

    public int getMaximoResumo() {
        return maximoResumo;
    }

    public boolean haRestricaoResumo() {
        if (!restricaoResumo) {
            System.out.println("ALERTA: Não há restrição para o resumo");
        }
        return restricaoResumo;
    }

    /**
     * Vinculos cadastrados por unidade. Se enviar unidade="todas", retorna de
     * todas as unidades
     *
     * @return lista de vinculos unidades = "todas"; retorna de todas as
     * unidades
     */
    /**
     * Salva um log para ações dos usuarios
     *
     * @param cpf
     * @param acao
     * @param descricao
     * @param comando
     */
    public void salvarLog(String cpf, String acao, String descricao, String comando) {
        String retirar = "'";
        comando = comando.replace(retirar.charAt(0), '|');
        String SQL = "INSERT INTO log(colaborador, acao, descricao, comandoexecutado,horario) VALUES ('" + cpf + "','" + acao + "','" + descricao + "','" + comando + "', NOW());";
        System.out.println(updateOUinsert(SQL) + " registro de log salvo");

    }

    int valorMin = 700;
    int minAvaliacoes = 1;

    public ArrayList<Integer> recuperarIDAutoresQueApresentaramNaoCultural(String issnEvento, int modalidade, int unidade) {
        // Toda apresentação que tiver apresentadorEfetivo <> -1, modalidade escolhida.  Com minimo duas avaliações
        ArrayList<Integer> ids;
        ids = new ArrayList<>();
        String SQL = "SELECT ap.apresentadorefetivo, AVG(avaliacao.nota) AS mednotas, COUNT(avaliacao.nota) as numavaliacoes "
                + "FROM apresentacao ap, avaliacao, apresentador aprs "
                + "WHERE ap.modalidade <> 4 AND ap.apresentadorefetivo <> -1 "
                + "AND ap.evento = '" + issnEvento + "' AND ap.modalidade = " + modalidade + " AND aprs.unidade = " + unidade + " AND ap.apresentador = aprs.cpf "
                + "AND avaliacao.apresentacao = ap.id;";  // selecionada =1

        ResultSet rs = select(SQL);
        try {
            while (rs.next()) {

                if (rs.getInt("mednotas") > valorMin && rs.getInt("numavaliacoes") >= minAvaliacoes) {
                    ids.add(rs.getInt("apresentadorefetivo"));
                }

            }
        } catch (SQLException ex) {
            Logger.getLogger(BancoDeDados.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Autores que apresentaram, não cultural. Recuperou " + ids.size() + ".");

        return ids;
    }

    public ArrayList<Integer> recuperarIdApresentacoesApresentadorSeManteve(String issnEvento, int modalidade, int unidade) {
        // Todos os valores positivos que aparecerem em apresentadorEfetivo, modalidade escolhida. Com minimo duas avaliacoes
        ArrayList<Integer> ids;
        ids = new ArrayList<>();
        String SQL = "SELECT ap.id, AVG(avaliacao.nota) AS mednotas, COUNT(avaliacao.nota) as numavaliacoes "
                + "FROM apresentacao ap, avaliacao, apresentador aprs "
                + "WHERE ap.apresentadorefetivo <> -1 "
                + "AND ap.evento = '" + issnEvento + "' AND ap.modalidade = " + modalidade + " AND aprs.unidade = " + unidade + " AND ap.apresentador = aprs.cpf "
                + "AND avaliacao.apresentacao = ap.id;";  // selecionada =1

        ResultSet rs = select(SQL);
        try {
            while (rs.next()) {

                if (rs.getInt("mednotas") > valorMin && rs.getInt("numavaliacoes") >= minAvaliacoes) {
                    ids.add(rs.getInt("id"));
                }

            }
        } catch (SQLException ex) {
            Logger.getLogger(BancoDeDados.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Apresentadores que efetivamente apresentaram. Recuperou " + ids.size() + ".");

        return ids;
    }

    public ArrayList<Integer> recuperarIdAutoresQueApresentaramCultural(String issnEvento, int modalidade, int unidade) {
        // Todo autor com ativo = 1, de uma apresentação que seja cultural
        ArrayList<Integer> ids;
        ids = new ArrayList<>();

        String SQL = "SELECT aut.id, AVG(avaliacao.nota) AS mednotas, COUNT(avaliacao.nota) as numavaliacoes "
                + "FROM apresentacao ap, avaliacao, apresentador aprs, autor aut "
                + "WHERE "
                + "ap.evento = '" + issnEvento + "' AND ap.modalidade = " + modalidade + " AND aprs.unidade = " + unidade + " AND ap.apresentador = aprs.cpf "
                + "AND avaliacao.apresentacao = ap.id AND aut.ativo = 1 AND aut.apresentacao = ap.id;";  // selecionada =1

        ResultSet rs = select(SQL);
        try {
            while (rs.next()) {

                if (rs.getInt("mednotas") > valorMin && rs.getInt("numavaliacoes") >= minAvaliacoes) {
                    ids.add(rs.getInt("id"));
                }

            }
        } catch (SQLException ex) {
            Logger.getLogger(BancoDeDados.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Autores que apresentaram, Cultural. Recuperou " + ids.size() + ".");

        return ids;

    }

    /**
     * especifico para select count
     *
     * @param SQL
     * @return ocorrencias
     */
    public int selectCount(String SQL) {

        ResultSet rs;
        int numero = -1;
        try {
            rs = select(SQL);
            while (rs.next()) {
                numero = rs.getInt(1);
            }

        } catch (SQLException ex) {
            Logger.getLogger(BancoDeDados.class.getName()).log(Level.SEVERE, null, ex);
        }

        return numero;

    }

    public ArrayList<unidade> getUnidades() {
        ArrayList<unidade> uns = new ArrayList<>();
        unidade aux;
        ResultSet rs = select("SELECT * FROM unidade ORDER BY nome;");

        try {
            while (rs.next()) {
                aux = new unidade(rs.getString("nome"), rs.getInt("id"));
                aux.setDiretor(rs.getString("diretor"));
                aux.setTelefone(rs.getString("telefone"));
                aux.setEndereco(rs.getString("endereco"));
                aux.setLimiteApresentacoesSelecionadas(rs.getInt("limiteapresentacoesselecionadas"));
                aux.setLimiteComunicacaoOralSelecionadas(rs.getInt("limitecomunicacaooralselecionadas"));
                aux.setLimiteIftech(rs.getInt("limiteiftech"));
                aux.setLimiteRobotica(rs.getInt("limiterobotica"));
                aux.setLimiteAcompanhantes(rs.getInt("limiteacompanhantes"));
                aux.setLimiteculturais(rs.getInt("limitecultural"));
                uns.add(aux);

            }
        } catch (SQLException ex) {
            Logger.getLogger(BancoDeDados.class.getName()).log(Level.SEVERE, null, ex);
        }

        return uns;
    }

    public boolean atualizarUnidade(unidade u) {
        //nome,endereco,telefone,id,diretor,limiteapresentacoesselecionadas,
        //limitecomunicacaooralselecionadas,limiteiftech,limiterobotica,limitecultural,limiteacompanhantes

        String SQL = "UPDATE unidade SET nome=?, endereco=?, telefone=?, diretor=?,"
                + " limiteapresentacoesselecionadas=?, limitecomunicacaooralselecionadas=?, limiteiftech=?,"
                + " limiterobotica=?, limitecultural=?, limiteacompanhantes=? WHERE id=?;";

        try {
            PreparedStatement st = con.prepareStatement(SQL);
            st.setString(1, u.getNome());
            st.setString(2, u.getEndereco());
            st.setString(3, u.getTelefone());
            st.setString(4, u.getDiretor());
            st.setInt(5, u.getLimiteApresentacoesSelecionadas());
            st.setInt(6, u.getLimiteComunicacaoOralSelecionadas());
            st.setInt(7, u.getLimiteIftech());
            st.setInt(8, u.getLimiteRobotica());
            st.setInt(9, u.getLimiteculturais());
            st.setInt(10, u.getLimiteAcompanhantes());
            st.setInt(11, u.getId());

            return st.execute();

        } catch (SQLException ex) {
            Logger.getLogger(BancoDeDados.class.getName()).log(Level.SEVERE, null, ex);
        }

        return false;
    }

    public ArrayList<tipopessoa> getTiposPessoas() {
        ArrayList<tipopessoa> uns = new ArrayList<>();
        tipopessoa aux;
        ResultSet rs = select("SELECT * FROM tipopessoa ORDER BY id ASC;");

        try {
            while (rs.next()) {
                aux = new tipopessoa(rs.getString("nome"), rs.getInt("id"));
                aux.setPodeApresentar(rs.getInt("podeapresentar") > 0);
                aux.setPodeAvaliar(rs.getInt("podeavaliar") > 0);
                uns.add(aux);

            }
        } catch (SQLException ex) {
            Logger.getLogger(BancoDeDados.class.getName()).log(Level.SEVERE, null, ex);
        }

        return uns;
    }

    public ArrayList<bolsa> getBolsas() {
        ArrayList<bolsa> uns = new ArrayList<>();
        bolsa aux;
        ResultSet rs = select("SELECT * FROM bolsa ORDER by id;");

        try {
            while (rs.next()) {
                aux = new bolsa(rs.getString("nome"), rs.getInt("id"));
                //  aux.setClassificaarea(rs.getInt("classificaarea"));
                //aux.setClassificatematica(rs.getInt("classificatematica"));
                uns.add(aux);

            }
        } catch (SQLException ex) {
            Logger.getLogger(BancoDeDados.class.getName()).log(Level.SEVERE, null, ex);
        }

        return uns;
    }

    public ArrayList<modalidade> getModalidades() {
        ArrayList<modalidade> uns = new ArrayList<>();
        modalidade aux;
        ResultSet rs = select("SELECT * FROM modalidade WHERE enabled =1 ORDER BY id ASC;");

        try {
            while (rs.next()) {
                aux = new modalidade(rs.getString("nome"), rs.getInt("id"));
                aux.setTemnomegrupo(rs.getInt("temnomegrupo"));
                aux.setUsamateriais(rs.getInt("usamateriais"));
                //aux.setClassificaarea(rs.getInt("classificaarea"));
                //aux.setClassificatematica(rs.getInt("classificatematica"));
                uns.add(aux);

            }
        } catch (SQLException ex) {
            Logger.getLogger(BancoDeDados.class.getName()).log(Level.SEVERE, null, ex);
        }

        return uns;
    }

    private ArrayList<evento> getEventos() {

        ArrayList<evento> uns = new ArrayList<>();
        evento aux;
        ResultSet rs = select("SELECT * FROM evento WHERE ativo>0 ORDER BY ano DESC;");

        try {
            while (rs.next()) {
                aux = new evento(rs.getString("nome"), rs.getString("issn"));
                aux.setAno(rs.getInt("ano"));
                aux.setAtivo(rs.getInt("ativo") > 0);
                aux.setData(rs.getString("data"));
                aux.setLocal(rs.getString("local"));
                aux.setUnidadeSedeCod(rs.getInt("unidadesede"));
                aux.setSepin(rs.getInt("sepin"));
                uns.add(aux);

            }
        } catch (SQLException ex) {
            Logger.getLogger(BancoDeDados.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (uns.size() > 1) {
            System.out.println("ALERTA: " + uns.size() + " eventos ativos!!");
        }

        return uns;
    }

    public evento getEventoAtual() {

        if (eventoAtual == null) {
            eventoAtual = getEventos().get(0);
        }

        return eventoAtual;
    }

    public void setEventoAtual(evento e) {
        eventoAtual = e;
    }

    public ArrayList<evento> getTodosEventos() {

        ArrayList<evento> uns = new ArrayList<>();
        evento aux;
        ResultSet rs = select("SELECT * FROM evento ORDER BY ano DESC;");

        try {
            while (rs.next()) {
                aux = new evento(rs.getString("nome"), rs.getString("issn"));
                aux.setAno(rs.getInt("ano"));
                aux.setAtivo(rs.getInt("ativo") > 0);
                aux.setData(rs.getString("data"));
                aux.setLocal(rs.getString("local"));
                aux.setUnidadeSedeCod(rs.getInt("unidadesede"));
                aux.setSepin(rs.getInt("sepin"));
                uns.add(aux);

            }
        } catch (SQLException ex) {
            Logger.getLogger(BancoDeDados.class.getName()).log(Level.SEVERE, null, ex);
        }

        return uns;
    }

    public ArrayList<funcao> getFuncoes() {
        ArrayList<funcao> uns = new ArrayList<>();
        funcao aux;
        ResultSet rs = select("SELECT nome, id FROM funcao ORDER BY id ASC;");

        try {
            while (rs.next()) {
                aux = new funcao(rs.getString("nome"), rs.getInt("id"));
                uns.add(aux);

            }
        } catch (SQLException ex) {
            Logger.getLogger(BancoDeDados.class.getName()).log(Level.SEVERE, null, ex);
        }

        return uns;
    }

    public ArrayList<area> getAreas() {
        ArrayList<area> uns = new ArrayList<>();
        area aux;
        int cat;
        String nome;
        boolean tematica;
        boolean cultural;
        boolean salvou;
        int cod;
        ResultSet rs = select("SELECT a.*, sa.nome, sa.id FROM area a, subarea sa WHERE sa.area = a.id ORDER BY a.nome, sa.nome ASC;");

        try {
            while (rs.next()) {
                cod = rs.getInt("a.id");
                cat = rs.getInt("tematica");
                tematica = cat == 1;
                cultural = cat == 2;
                salvou = false;
                for (int a = 0; a < uns.size(); a++) {
                    if (uns.get(a).getId() == cod) {
                        uns.get(a).addSubArea(new subarea(rs.getString("sa.nome"), rs.getInt("sa.id")));
                        salvou = true;
                    }
                }

                if (!salvou) {
                    uns.add(new area(rs.getString("a.nome"), cod));
                    uns.get(uns.size() - 1).setTematica(tematica);
                    uns.get(uns.size() - 1).setCultural(cultural);
                    uns.get(uns.size() - 1).addSubArea(new subarea(rs.getString("sa.nome"), rs.getInt("sa.id")));
                }

            }
        } catch (SQLException ex) {
            Logger.getLogger(BancoDeDados.class.getName()).log(Level.SEVERE, null, ex);
        }

        return uns;
    }

    public apresentador getApresentador(String text) {
        apresentador apr = new apresentador();

        ResultSet rs = select("SELECT apresentador.*, unidade.nome AS nomeunidade "
                + "FROM apresentador, unidade "
                + "WHERE cpf = '" + text + "' AND apresentador.unidade = unidade.id;");

        try {
            while (rs.next()) {

                apr.setAgencia(rs.getString("agencia"));
                apr.setBairro(rs.getString("bairro"));
                apr.setBanco(rs.getString("banco"));
                apr.setCep(rs.getString("cep"));
                apr.setCodVinculo(rs.getInt("tipopessoa"));
                apr.setCodVinculo(rs.getInt("tipopessoa"));
                apr.setConta(rs.getString("conta"));
                apr.setCodigoUnidade(rs.getInt("unidade"));
                apr.setObservacoes(rs.getString("observacoes"));
                apr.setCpf(text);
                apr.setDatadenascimento(Arrumador.comando().DATEParaData(rs.getString("datadenascimento")));
                apr.setDataexprg(Arrumador.comando().DATEParaData(rs.getString("dataexprg")));
                apr.setEmail(rs.getString("email"));
                apr.setEndereco(rs.getString("endereco"));
                apr.setEstado(rs.getString("estado"));
                apr.setEstadonaturalidade(rs.getString("estadodenaturalidade"));
                apr.setEstadorg(rs.getString("estadorg"));
                apr.setMae(rs.getString("mae"));
                apr.setMunicipio(rs.getString("municipio"));
                apr.setNaturalidade(rs.getString("naturalidade"));
                apr.setNome(rs.getString("nome"));
                apr.setOrgaoexpedidorrg(rs.getString("orgaoexpedidorrg"));
                apr.setPai(rs.getString("pai"));
                apr.setRg(rs.getString("rg"));
                apr.setSexo(rs.getString("sexo"));
                // apr.setSenha(rs.getString("senha"));
                apr.setTelefone(rs.getString("telefone"));
                apr.setTipoconta(rs.getString("tipoconta"));
                apr.setUnidade(rs.getString("nomeunidade"));
                apr.setVinculo(rs.getString("tipopessoa"));
                apr.setNecessidadesespeciais(rs.getString("necessidadeespecial"));
                apr.setDeclarante(rs.getString("declarante"));
                apr.setNomedeclarante(rs.getString("nomedeclarante"));
                apr.setCpfdeclarante(rs.getString("cpfdeclarante"));

            }
        } catch (SQLException ex) {
            Logger.getLogger(BancoDeDados.class.getName()).log(Level.SEVERE, null, ex);
        }

        return apr;
    }

    public boolean atualizarApresentador(apresentador novo, boolean atualizarSenha) {

        String sqlString = "UPDATE apresentador set agencia = ?, bairro = ?, banco = ?, cep = ?, conta = ?,"
                + " cpfdeclarante = ?, datadenascimento = ?, dataexprg = ?, declarante = ?, email = ?,"
                + " endereco = ?, estado = ?, estadodenaturalidade = ?, estadorg = ?, mae = ?,"
                + " municipio = ?, naturalidade = ?, necessidadeespecial = ?, nome = ?, nomedeclarante = ?,"
                + "orgaoexpedidorrg = ?, pai = ?, rg = ?, sexo = ?, telefone = ?,"
                + "tipoconta = ?, unidade = ?, tipopessoa = ?, observacoes = ?";

        if (atualizarSenha) {
            sqlString = sqlString + ", senha = md5(?) WHERE cpf = ?;";
        } else {
            sqlString = sqlString + " WHERE cpf = ?;";
        }

        try {

            PreparedStatement st = con.prepareStatement(sqlString);
// Associa valores aos parâmetros SQL (bind)
// 1 e 2 especificam a posição (ordem) de cada parâmetro
            st.setString(1, novo.getAgencia());
            st.setString(2, novo.getBairro());
            st.setString(3, novo.getBanco());
            st.setString(4, novo.getCep());
            st.setString(5, novo.getConta());
            st.setString(6, novo.getCpfdeclarante());
            st.setDate(7, Date.valueOf(novo.getDatadenascimento()));
            //  stmt.setString(7, novo.getDatadenascimento());
            st.setDate(8, Date.valueOf(novo.getDataexprg()));
            //stmt.setString(8, novo.getDataexprg());
            st.setString(9, novo.getDeclarante());
            st.setString(10, novo.getEmail());
            st.setString(11, novo.getEndereco());
            st.setString(12, novo.getEstado());
            st.setString(13, novo.getEstadonaturalidade());
            st.setString(14, novo.getEstadorg());
            st.setString(15, novo.getMae());
            st.setString(16, novo.getMunicipio());
            st.setString(17, novo.getNaturalidade());
            st.setString(18, novo.getNecessidadesespeciais());
            st.setString(19, novo.getNome());
            st.setString(20, novo.getNomedeclarante());
            st.setString(21, novo.getOrgaoexpedidorrg());
            st.setString(22, novo.getPai());
            st.setString(23, novo.getRg());
            // stmt.setString(24, novo.getSenha());
            st.setString(24, novo.getSexo());
            st.setString(25, novo.getTelefone());
            st.setString(26, novo.getTipoconta());
            st.setInt(27, novo.getCodigoUnidade());
            st.setInt(28, novo.getCodVinculo());
            st.setString(29, novo.getObservacoes());
            //  stmt.setInt(29, novo.getBolsaCod());
            if (atualizarSenha) {
                st.setString(30, novo.getSenha());
                st.setString(31, novo.getCpf());
            } else {
                st.setString(30, novo.getCpf());
            }

            // Executa o comando SQL, com os parâmetros fornecidos
            boolean result = st.execute();
            return true;

        } catch (SQLException ex) {
            Logger.getLogger(BancoDeDados.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

    }

    public boolean inserirApresentador(apresentador novo) {

        String SQL = "INSERT INTO apresentador (agencia, bairro, banco, cep, conta,"
                + "cpfdeclarante, datadenascimento, dataexprg, declarante, email,"
                + "endereco, estado, estadodenaturalidade, estadorg, mae,"
                + "municipio, naturalidade, necessidadeespecial, nome, nomedeclarante,"
                + "orgaoexpedidorrg, pai, rg, sexo, telefone, "
                + "tipoconta, unidade, tipopessoa, senha, "
                + "cpf,bolsa,observacoes) VALUES "
                + "(?,?,?,?,?,"
                + "?,?,?,?,?,"
                + "?,?,?,?,?,"
                + "?,?,?,?,?,"
                + "?,?,?,?,?,"
                + "?,?,?,md5(?),"
                + "?,?,?);";

        try {

            PreparedStatement stmtx = con.prepareStatement(SQL);
// 1 e 2 especificam a posição (ordem) de cada parâmetro
            stmtx.setString(1, novo.getAgencia());
            stmtx.setString(2, novo.getBairro());
            stmtx.setString(3, novo.getBanco());
            stmtx.setString(4, novo.getCep());
            stmtx.setString(5, novo.getConta());
            stmtx.setString(6, novo.getCpfdeclarante());
            stmtx.setDate(7, Date.valueOf(novo.getDatadenascimento()));
            //  stmt.setString(7, novo.getDatadenascimento());
            stmtx.setDate(8, Date.valueOf(novo.getDataexprg()));
            //stmt.setString(8, novo.getDataexprg());
            stmtx.setString(9, novo.getDeclarante());
            stmtx.setString(10, novo.getEmail());
            stmtx.setString(11, novo.getEndereco());
            stmtx.setString(12, novo.getEstado());
            stmtx.setString(13, novo.getEstadonaturalidade());
            stmtx.setString(14, novo.getEstadorg());
            stmtx.setString(15, novo.getMae());
            stmtx.setString(16, novo.getMunicipio());
            stmtx.setString(17, novo.getNaturalidade());
            stmtx.setString(18, novo.getNecessidadesespeciais());
            stmtx.setString(19, novo.getNome());
            stmtx.setString(20, novo.getNomedeclarante());
            stmtx.setString(21, novo.getOrgaoexpedidorrg());
            stmtx.setString(22, novo.getPai());
            stmtx.setString(23, novo.getRg());
            // stmt.setString(24, novo.getSenha());
            stmtx.setString(24, novo.getSexo());
            stmtx.setString(25, novo.getTelefone());
            stmtx.setString(26, novo.getTipoconta());
            stmtx.setInt(27, novo.getCodigoUnidade());
            stmtx.setInt(28, novo.getCodVinculo());
            stmtx.setString(29, novo.getSenha());
            stmtx.setString(30, novo.getCpf());
            // stmt.setString(29, novo.getCpf());
            stmtx.setInt(31, novo.getBolsaCod());
            stmtx.setString(32, novo.getObservacoes());

            // Executa o comando SQL, com os parâmetros fornecidos
            boolean result = stmtx.execute();

            System.out.println("Apresentador cadastrado com sucesso!");
            return true;

        } catch (SQLException ex) {
            Logger.getLogger(BancoDeDados.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

    }

    public boolean inserirCertificado(certificado c) {
        String SQL = "INSERT INTO certificado(texto,verificador,nomearq,tipo,cpf,"
                + "texto1,texto2,texto3,evento) "
                + "VALUES(?,?,?,?,?,"
                + "?,?,?,?);";
        try {
            PreparedStatement stmt1 = con.prepareStatement(SQL);
            stmt1.setString(1, c.getTexto());
            stmt1.setString(2, c.getVerificador());
            stmt1.setString(3, c.getNomeArq());
            stmt1.setString(4, c.getTipo());
            stmt1.setString(5, c.getCpf());

            stmt1.setString(6, c.getTexto1());
            stmt1.setString(7, c.getTexto2());
            stmt1.setString(8, c.getTexto3());
            stmt1.setString(9, c.getEvento());

            stmt1.execute();

            return true;

        } catch (SQLException ex) {
            System.out.println("FALHA EM " + c.getNomeArq());
            Logger.getLogger(BancoDeDados.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

    }
    String relatorioCerts = "";

    public ArrayList<certificado> getCertificados(geradorCertificados t, boolean gerarArquivos, boolean reprocessar, String evento) {
        ArrayList<certificado> certs = new ArrayList<>();
        geradorCertificados t2 = t;

        ResultSet c = select("SELECT * FROM certificado WHERE evento = '" + evento + "';");
        certificado ax;

        try {
            while (c.next()) {

                ax = new certificado(t2);

                ax.setCpf(c.getString("cpf"));
                ax.setEvento(c.getString("evento"));
                ax.setNomeArq(c.getString("nomearq"));
                ax.setTexto(c.getString("texto"));
                ax.setTipo(c.getString("tipo"));
                ax.setVerificador(c.getString("verificador"));
                ax.setTexto1(c.getString("texto1").replace("19 a 22 de outubro", "19 a 22 de outubro de 2015"));
                ax.setTexto2(c.getString("texto2").replace("19 a 22 de outubro", "19 a 22 de outubro de 2015"));
                ax.setTexto3(c.getString("texto3").replace("19 a 22 de outubro", "19 a 22 de outubro de 2015"));

                ax.setVeioDoBD(true);
                certs.add(ax);

            }
        } catch (SQLException ex) {
            Logger.getLogger(BancoDeDados.class.getName()).log(Level.SEVERE, null, ex);
        }

        t2.criarDiretorios();

        if (reprocessar) {
            for (certificado ces : certs) {
                ces.reprocessar(true);
            }
        }

        System.out.println("Iniciando geração de " + certs.size() + " arquivos de certificados!");
        if (gerarArquivos) {
            for (certificado ces : certs) {
                ces.gerarArquivo();
            }
        }

        relatorioCerts = t2.getRelatorio();
        System.out.println("Gerou arquivos a partir dos certificados salvos no Banco de Dados!");

        return certs;
    }

    public String getRelatorioCerts() {
        return relatorioCerts;
    }

    public boolean loginApresentador(String text, String password) {

        String SQL = "SELECT COUNT(*) AS conte FROM apresentador WHERE cpf = ? AND"
                + " senha = md5(?);";

        try {
            PreparedStatement stmt1 = con.prepareStatement(SQL);
            stmt1.setString(1, text);
            stmt1.setString(2, password);

            ResultSet rs = stmt1.executeQuery();

            while (rs.next()) {
                return (rs.getInt("conte") > 0);
            }

        } catch (SQLException ex) {
            Logger.getLogger(BancoDeDados.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

        return false;
    }

    public boolean cadastrarApresentacao(apresentacao apr) {

        String SQL = "INSERT INTO apresentacao("
                + "nome,grupo,resumo,"
                + "palavraschave,materiais,apresentador,"
                + "modalidade,subarea,evento,"
                + "linkvideo,areatematica,categoria,selecionada,inclusao,diversidade) VALUES"
                + "(?,?,?,"
                + "?,?,?,"
                + "?,?,?,"
                + "?,?,?,?,?,?);";
        try {
            PreparedStatement stmt1 = con.prepareStatement(SQL);
            stmt1.setString(1, apr.getNometitulo());
            stmt1.setString(2, apr.getGrupoNome());
            stmt1.setString(3, apr.getResumo());

            stmt1.setString(4, apr.getPalavrasChave());
            stmt1.setString(5, apr.getMateriais());
            stmt1.setString(6, apr.getApresentadorCpf());

            stmt1.setInt(7, apr.getModalidadeCod());
            stmt1.setInt(8, apr.getSubareaCod());
            stmt1.setString(9, apr.getEventoCod());
            stmt1.setString(10, apr.getLinkParaVideo());
            stmt1.setInt(11, apr.getTematicaExtensaoCod());
            stmt1.setInt(12, apr.getCategoriaCod());
            stmt1.setInt(13, apr.getSelecionado());
            
            stmt1.setString(14, apr.getInclusao());
            stmt1.setString(15, apr.getDiversidade());

            stmt1.execute();
            System.out.println("Cadastrou informações básicas da apresentação.");
            apr.setId(selectCount("SELECT MAX(id) FROM apresentacao;"));

            ArrayList<autor> autores = apr.getAutores();

            if (!cadastrarConvenioApresentador(apr)) {
                return false;
            }

            for (int a = 0; a < autores.size(); a++) {
                autores.get(a).setApresentacaoCod(apr.getId());
                if (!cadastrarAutor(autores.get(a), apr.getModalidadeCod() == 4)) {
                    return false;
                }

            }

            return true;
        } catch (SQLException ex) {
            Logger.getLogger(BancoDeDados.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean cadastrarAutor(autor aut, boolean ehAprCultural) {

        String SQL = "INSERT INTO autor(apresentacao,"
                + "nome,email,unidade,funcao,cpf,sexo,nascimento,ativo,rg) VALUES"
                + "(?,?,?,?,?,?,?,?,?,?);";
        try {
            PreparedStatement stmt1 = con.prepareStatement(SQL);
            stmt1.setInt(1, aut.getApresentacaoCod());
            stmt1.setString(2, aut.getNome());
            stmt1.setString(3, aut.getEmail());
            stmt1.setInt(4, aut.getUnidadecodigo());
            stmt1.setInt(5, aut.getFuncaoCod());
            stmt1.setString(6, aut.getCpf());
            stmt1.setString(7, aut.getSexo());
            stmt1.setString(8, aut.getNascimentoDate());
            if (ehAprCultural) {
                stmt1.setInt(9, 1);
            } else {
                stmt1.setInt(9, 0);
            }
            stmt1.setString(10, aut.getRg());

            stmt1.execute();
            System.out.println("Cadastrou autor " + aut.getNome() + ".");

            aut.setId(selectCount("SELECT MAX(id) FROM autor;"));

            cadastrarConvenioAutor(aut);

            return true;
        } catch (SQLException ex) {
            Logger.getLogger(BancoDeDados.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean cadastrarConvenioAutor(autor aut) {
        String SQL = "INSERT INTO convenio(autor,apresentacao,bolsa) VALUES"
                + "(?,?,?);";
        try {
            PreparedStatement stmt1 = con.prepareStatement(SQL);
            stmt1.setInt(1, aut.getId());
            stmt1.setInt(2, aut.getApresentacaoCod());
            stmt1.setInt(3, aut.getBolsaid());

            stmt1.execute();
            System.out.println("Cadastrou convênio do autor " + aut.getNome() + ".");

            return true;
        } catch (SQLException ex) {
            Logger.getLogger(BancoDeDados.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean atualizarAutor(autor aut) {

        String SQL = "UPDATE autor SET nome=?, email=?, unidade=?, funcao=?, cpf=?, sexo=?, nascimento=?, ativo=?, rg=? "
                + "WHERE id = ?;";
        try {
            PreparedStatement stmt1 = con.prepareStatement(SQL);

            stmt1.setString(1, aut.getNome());
            stmt1.setString(2, aut.getEmail());
            stmt1.setInt(3, aut.getUnidadecodigo());
            stmt1.setInt(4, aut.getFuncaoCod());

            stmt1.setString(5, aut.getCpf());
            stmt1.setString(6, aut.getSexo());
            stmt1.setString(7, aut.getNascimentoDate());
            stmt1.setInt(8, aut.getAtivo());
            stmt1.setString(9, aut.getRg());
            
            stmt1.setInt(10, aut.getId());

            stmt1.execute();
            System.out.println("Atualizou autor " + aut.getNome() + " (" + aut.getId() + ").");

            atualizarConvenioAutor(aut);

            return true;
        } catch (SQLException ex) {
            Logger.getLogger(BancoDeDados.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean atualizarConvenioAutor(autor aut) {
        String SQL = "UPDATE convenio SET bolsa=? WHERE autor = ? AND apresentacao = ?;";
        try {
            PreparedStatement stmt1 = con.prepareStatement(SQL);
            stmt1.setInt(1, aut.getBolsaid());
            stmt1.setInt(2, aut.getId());
            stmt1.setInt(3, aut.getApresentacaoCod());

            stmt1.execute();
            System.out.println("Atualizou convênio do autor " + aut.getNome() + "(" + aut.getId() + ").");

            return true;
        } catch (SQLException ex) {
            Logger.getLogger(BancoDeDados.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public ArrayList<apresentacao> recuperarApresentacoesASeremAvaliadas(String cpf, evento e) {
        ArrayList<apresentacao> aps = new ArrayList<>();
        String SQL = "SELECT DISTINCT apresentacao.id FROM apresentacao, avalia, area, subarea, avaliador, apresentador, unidade, evento e  "
                + "WHERE avaliador.cpf = '" + cpf + "' AND avalia.avaliador = avaliador.cpf AND avalia.area = subarea.area "
                + "AND apresentacao.subarea = subarea.id AND apresentacao.evento = e.issn AND e.issn='" + e.getId() + "'";
        if (centralizado == 0) {
            SQL = SQL + " AND avaliador.unidade = apresentador.unidade AND apresentacao.apresentador = apresentador.cpf";
        }

        SQL = SQL + ";";

        ArrayList<Integer> ids = new ArrayList<>();
        ResultSet rs = select(SQL);
        try {
            while (rs.next()) {
                ids.add(rs.getInt("id"));
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(BancoDeDados.class.getName()).log(Level.SEVERE, null, ex);
        }
        for (int a = 0; a < ids.size(); a++) {
            aps.add(recuperarApresentacao(ids.get(a)));
        }

        System.out.println("Recuperou " + aps.size() + " apresentações que podem ser avaliadas.");
        return aps;
    }

    public ArrayList<apresentacao> recuperarApresentacoesASeremAvaliadasComRestricoes(String cpf, int centr, evento e) {
        ArrayList<apresentacao> aps = new ArrayList<>();
        String SQL = "SELECT DISTINCT av.apresentacao AS id FROM avaliacao av, avalia, area, subarea, avaliador, apresentador, unidade, apresentacao, evento  "
                + "WHERE av.apresentacao = apresentacao.id AND avaliador.cpf = '" + cpf + "' AND avalia.avaliador = avaliador.cpf AND avalia.area = subarea.area "
                + "AND apresentacao.subarea = subarea.id AND apresentacao.evento = evento.issn AND evento.issn='" + e.getId() + "'";
        if (centr == 0) {
            SQL = SQL + " AND avaliador.unidade = apresentador.unidade AND apresentacao.apresentador = apresentador.cpf";
        } else {
            SQL = SQL + " AND avaliador.unidade <> apresentador.unidade AND apresentacao.apresentador = apresentador.cpf";
        }

        SQL = SQL + ";";

        ArrayList<Integer> ids = new ArrayList<>();
        ResultSet rs = select(SQL);
        try {
            while (rs.next()) {
                ids.add(rs.getInt("id"));
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(BancoDeDados.class.getName()).log(Level.SEVERE, null, ex);
        }
        for (int a = 0; a < ids.size(); a++) {
            aps.add(recuperarApresentacao(ids.get(a)));
        }

        System.out.println("Recuperou " + aps.size() + " apresentações que podem ser avaliadas.");
        return aps;
    }

    public ArrayList<apresentacao> recuperarListaGeralApresentacoes(int centr, int unid, evento e) {
        ArrayList<apresentacao> aps = new ArrayList<>();

        String SQL = "SELECT DISTINCT a.*, e.nome, aut.nome AS autnome, c.bolsa, "
                + " ar.nome AS area, ar.id AS codarea, sa.nome AS subareanome, u.nome, aut.email, m.nome, cat.grandearea, cat.areatematica, a.areatematica as tematicaext"
                + " FROM apresentacao a, area ar, subarea sa, convenio c, apresentador aut, evento e, unidade u, modalidade m, categoria cat"
                + " WHERE sa.id = a.subarea AND sa.area = ar.id"
                + " AND aut.cpf = a.apresentador AND c.apresentador = aut.cpf AND c.apresentacao "
                + "AND e.issn = a.evento AND e.issn='" + e.getId() + "' AND aut.unidade = u.id AND m.id = a.modalidade AND cat.id = a.categoria ";
        if (centr == 1) {
            SQL = SQL + "AND a.selecionada > 0 ";
        } else if (unid > -1) {
            SQL = SQL + "AND aut.unidade = " + unid + " ";
        }
        SQL = SQL + "ORDER BY a.nome;";
        try {
            ResultSet rs2 = select(SQL);

            while (rs2.next()) {
                apresentacao ap = new apresentacao();
                ap.setApresentadorCpf(rs2.getString("a.apresentador"));
                ap.setEventoCod(rs2.getString("a.evento"));
                ap.setGrandeArea(rs2.getInt("grandearea") > 0);
                ap.setAreaTematica(rs2.getInt("areatematica") > 0);
                ap.setTematicaExtensaoCod(rs2.getInt("tematicaext"));
                ap.setId(rs2.getInt("id"));
                ap.setResumo(rs2.getString("a.resumo"));
                ap.setPalavrasChave(rs2.getString("a.palavraschave"));
                ap.setNometitulo(rs2.getString("a.nome"));
                ap.setMateriais(rs2.getString("a.materiais"));
                ap.setGrupoNome(rs2.getString("a.grupo"));
                ap.setSubareaCod(rs2.getInt("a.subarea"));
                ap.setSubareaNome(rs2.getString("subareanome"));
                ap.setAreaNome(rs2.getString("area"));
                ap.setAreaCod(rs2.getInt("codarea"));
                ap.setModalidadeCod(rs2.getInt("modalidade")); // Codigo modalidade
                ap.setApresentadorNome(rs2.getString("autnome"));
                ap.setEventoNome(rs2.getString("e.nome"));
                ap.setCategoriaCod(rs2.getInt("categoria"));
                ap.setUnidadeNome(rs2.getString("u.nome"));
                ap.setApresentadorEmail(rs2.getString("aut.email"));
                ap.setModalidadeNome(rs2.getString("m.nome"));
                ap.setBolsaApresentadorCod(rs2.getInt("c.bolsa"));
                aps.add(ap);
            }

        } catch (SQLException ex) {
            Logger.getLogger(BancoDeDados.class.getName()).log(Level.SEVERE, null, ex);
        }

        for (apresentacao ax : aps) {
            ax.setAutores(recuperarAutoresApresentacao(ax.getId()));
        }

        return aps;
    }

    public ArrayList<apresentacao> recuperarApresentacoesParaSelecao(int unid, evento e) {
        ArrayList<apresentacao> aps = new ArrayList<>();

        String SQL = "SELECT DISTINCT "/*AVG(av.nota) AS media, COUNT(DISTINCT av.avaliador) AS navaliacoes,"*/
                + "a.*, e.nome, "
                + "aut.nome AS autnome, ar.nome AS area,"
                + " ar.id AS codarea, sa.nome AS subareanome, u.nome, aut.email, m.nome, m.id as mid"
                + " FROM apresentacao a, area ar, subarea sa, convenio c, "
                + "apresentador aut, evento e, unidade u, modalidade m, avaliacao av"
                + " WHERE sa.id = a.subarea AND sa.area = ar.id  "
                + " AND aut.cpf = a.apresentador "//AND av.apresentacao = a.id "
                + "AND e.issn = a.evento AND e.issn='" + e.getId() + "' AND aut.unidade = u.id AND m.id = a.modalidade ";

        ///if(unid>-1)
        SQL = SQL + "AND aut.unidade = " + unid + "; ";
        //    SQL = SQL + "ORDER BY u.nome, ar.nome, a.nome;";
        try {
            ResultSet rs2 = select(SQL);

            while (rs2.next()) {
                apresentacao ap = new apresentacao();
                ap.setApresentadorCpf(rs2.getString("a.apresentador"));
                ap.setEventoCod(rs2.getString("a.evento"));
                ap.setId(rs2.getInt("id"));
                ap.setApresentadorEfetivoIDInicial(rs2.getInt("apresentadorefetivo"));
                //  ap.setMediaNotas(rs2.getInt("media"));
                //  ap.setnAvaliacoes(rs2.getInt("navaliacoes"));
                ap.setPalavrasChave(rs2.getString("a.palavraschave"));
                ap.setNometitulo(rs2.getString("a.nome"));
                ap.setMateriais(rs2.getString("a.materiais"));
                ap.setGrupoNome(rs2.getString("a.grupo"));
                ap.setSubareaCod(rs2.getInt("a.subarea"));
                ap.setSubareaNome(rs2.getString("subareanome"));
                ap.setAreaNome(rs2.getString("area"));
                ap.setAreaCod(rs2.getInt("codarea"));
                ap.setModalidadeCod(rs2.getInt("modalidade"));
                ap.setApresentadorNome(rs2.getString("autnome"));
                ap.setApresentadorEfetivoCPFInicial(ap.getApresentadorCpf());
                ap.setApresentadorEfetivoNomeInicial(ap.getApresentadorNome());
                //  ap.setApresentadorEfetivoIDInicial(0);
                ap.setEventoNome(rs2.getString("e.nome"));
                ap.setUnidadeNome(rs2.getString("u.nome"));
                ap.setApresentadorEmail(rs2.getString("aut.email"));
                ap.setModalidadeNome(rs2.getString("m.nome"));
                ap.setModalidadeCod(rs2.getInt("mid"));
                ap.setHospedagensInicial(rs2.getInt("hospedagem"));
                ap.setAlimentacoesInicial(rs2.getInt("alimentacao"));
                ap.setTransportesInicial(rs2.getInt("transporte"));
                ap.setSelecionadoInicial(rs2.getInt("selecionada"));
                aps.add(ap);
            }

        } catch (SQLException ex) {
            Logger.getLogger(BancoDeDados.class.getName()).log(Level.SEVERE, null, ex);
        }

        for (apresentacao ax : aps) {
            ax.setAutores(recuperarAutoresApresentacao(ax.getId()));
        }

        return aps;
    }

    /*   public ArrayList<avaliacao> recuperarAvaliacoesPermitidas(int apresentacao){
     ArrayList<avalia> avz = new ArrayList<>();
     ResultSet rx = select("SELECT * FROM avalia WHERE apresentacao ="+apresentacao+";");
     return avz;
     }*/
    public ArrayList<apresentacao> recApresentacoesParaGerarAnaisDoSeminario(
            String issnEvento, int codModalidade, int codUnidade, String order, int codArea) {
        ArrayList<apresentacao> aps = new ArrayList<>();
        String SQL = "SELECT DISTINCT apresentacao.id "
                + "FROM apresentacao";
        if (codUnidade > -1 || order.contains("unidade")) {
            SQL = SQL + ", apresentador";
        }
        if (codArea > -1 || order.contains("area")) {
            SQL = SQL + ", subarea";
        }

        SQL = SQL + " WHERE evento = '" + issnEvento + "' ";
        if (codModalidade > -1) {
            SQL = SQL + "AND modalidade = " + codModalidade + " ";
        }
        if (codUnidade > -1) {
            SQL = SQL + "AND apresentador.cpf = apresentacao.apresentador AND unidade = " + codUnidade + " ";
        }
        if (codArea > -1) {
            SQL = SQL + "AND subarea.id = apresentacao.subarea AND area = " + codArea + " ";
        }

        if (order.contains("unidade") && codUnidade == -1) {
            SQL = SQL + "AND apresentador.cpf = apresentacao.apresentador ";
        }
        if (order.contains("area") && codArea == -1) {
            SQL = SQL + "AND subarea.id = apresentacao.subarea ";
        }

        SQL = SQL + "ORDER BY " + order + ";";

        ArrayList<Integer> ids = new ArrayList<>();
        ResultSet rs = select(SQL);
        try {
            while (rs.next()) {
                ids.add(rs.getInt("id"));
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(BancoDeDados.class.getName()).log(Level.SEVERE, null, ex);
        }

        for (int a = 0; a < ids.size(); a++) {
            aps.add(recuperarApresentacao(ids.get(a)));
        }

        System.out.println("Recuperou " + aps.size() + " apresentações para gerar o arquivo de anais do seminário.");
        return aps;
    }

    public apresentacao recuperarApresentacao(int id) {
        apresentacao ap = new apresentacao();
        System.out.println("Recuperando apresentação de número " + id + ";");
        String SQL = "SELECT a.diversidade, a.inclusao, a.apresentador, a.categoria, catg.nome as catnome, a.areatematica, a.apresentadorefetivo, a.evento, a.linkvideo, e.nome, a.modalidade, a.resumo, a.palavraschave, aut.nome AS autnome, "
                + "a.nome, a.materiais, a.grupo, a.subarea, ar.nome AS area, ar.id AS areacod, sa.nome AS subareanome, u.nome, u.id as uid,aut.email, m.nome, b.nome as bolsanome, b.id as bolsaid, catg.grandearea as tga, catg.areatematica as tat"
                + " FROM apresentacao a, area ar, subarea sa, convenio c, apresentador aut, evento e, unidade u, modalidade m, bolsa b, categoria catg"
                + " WHERE a.id = " + id + " AND sa.id = a.subarea AND sa.area = ar.id AND catg.id = a.categoria AND "
                + "c.apresentador = a.apresentador AND c.apresentacao = a.id AND aut.cpf = a.apresentador "
                + "AND e.issn = a.evento AND aut.unidade = u.id AND m.id = a.modalidade AND c.apresentador = a.apresentador AND c.bolsa = b.id;";
        try {
            ResultSet rs2 = select(SQL);

            while (rs2.next()) {

                ap.setId(id);
                
                ap.setDiversidade(rs2.getString("a.diversidade"));
                ap.setInclusao(rs2.getString("a.inclusao"));
                
                ap.setApresentadorCpf(rs2.getString("a.apresentador"));
                ap.setApresentadorEfetivoIDInicial(rs2.getInt("apresentadorefetivo"));
                ap.setEventoCod(rs2.getString("a.evento"));
                ap.setCategoriaNome(rs2.getString("catnome"));
                ap.setGrandeArea(rs2.getInt("tga") > 0);
                ap.setAreaTematica(rs2.getInt("tat") > 0);
                ap.setAreaCod(rs2.getInt("areacod"));

                //System.out.print("Load 1; ");
                ap.setCategoriaCod(rs2.getInt("categoria"));
                ap.setBolsaApresentadorNome(rs2.getString("bolsanome"));
                ap.setBolsaApresentadorCod(rs2.getInt("bolsaid"));

                //System.out.print("Load 2; ");
                //ap.setId(id);
                ap.setTematicaExtensaoCod(rs2.getInt("areatematica"));
                ap.setResumo(rs2.getString("a.resumo"));
                ap.setPalavrasChave(rs2.getString("a.palavraschave"));
                ap.setNometitulo(rs2.getString("a.nome"));
                // System.out.print("Load 3; ");
                ap.setMateriais(rs2.getString("a.materiais"));
                ap.setGrupoNome(rs2.getString("a.grupo"));
                ap.setSubareaCod(rs2.getInt("a.subarea"));
                ap.setSubareaNome(rs2.getString("subareanome"));
                ap.setAreaNome(rs2.getString("area"));
                ap.setModalidadeCod(rs2.getInt("modalidade"));
                // System.out.print("Load 4; ");
                ap.setApresentadorNome(rs2.getString("autnome"));
                ap.setEventoNome(rs2.getString("e.nome"));
                ap.setUnidadeNome(rs2.getString("u.nome"));
                ap.setUnidadeCod(rs2.getInt("uid"));

                ap.setApresentadorEmail(rs2.getString("aut.email"));
                ap.setModalidadeNome(rs2.getString("m.nome"));
                ap.setModalidadeCod(rs2.getInt("a.modalidade"));
                ap.setLinkParaVideo(rs2.getString("linkvideo"));
                // System.out.println("Load 5; ");
                // ap.setBolsaApresentadorNome(rs2.getString("bolsanome"));
                //ap.setBolsaApresentadorCod(rs2.getInt("c.bolsa"));
            }

        } catch (SQLException ex) {
            Logger.getLogger(BancoDeDados.class.getName()).log(Level.SEVERE, null, ex);
        }

        ap.setAutores(recuperarAutoresApresentacao(id));
        ap.setBolsaApresentadorCod(recuperarBolsaApresentador(id, ap.getApresentadorCpf()));

        return ap;
    }

    public ArrayList<autor> recuperarAutoresApresentacao(int id) {
        ArrayList<autor> autores = new ArrayList<>();
        autor aux;
        String SQL = "SELECT DISTINCT a.nome, a.id, a.email, a.unidade, a.funcao, c.bolsa, a.cpf, a.nascimento, a.sexo, a.rg, "
                + "f.nome AS nomefuncao, u.nome AS nomeunidade, b.nome AS nomebolsa, a.ativo as autativo "
                + "FROM autor a, convenio c, unidade u, funcao f, bolsa b "
                + "WHERE c.apresentacao = " + id + " AND a.id = c.autor AND c.bolsa = b.id AND a.funcao = f.id AND a.unidade = u.id;";
        ResultSet rs = select(SQL);
        try {
            while (rs.next()) {
                aux = new autor();
                aux.setApresentacaoCod(id);
                aux.setBolsaid(rs.getInt("bolsa"));
                aux.setEmail(rs.getString("email"));
                aux.setFuncaoCod(rs.getInt("funcao"));
                aux.setId(rs.getInt("id"));
                aux.setNome(rs.getString("nome"));
                aux.setUnidadecodigo(rs.getInt("unidade"));
                aux.setBolsaNome(rs.getString("nomebolsa"));
                aux.setFuncaoNome(rs.getString("nomefuncao"));
                aux.setUnidadeNome(rs.getString("nomeunidade"));
                aux.setCpf(rs.getString("cpf"));
                aux.setRg(rs.getString("a.rg"));
                aux.setSexo(rs.getString("sexo"));
                aux.setNascimentoDate(rs.getString("nascimento"));
                aux.setAtivoInicial(rs.getInt("autativo"));
                autores.add(aux);
            }
        } catch (SQLException ex) {
            Logger.getLogger(BancoDeDados.class.getName()).log(Level.SEVERE, null, ex);
        }
        return autores;
    }

    public boolean excluirAutores(ArrayList<Integer> autoresAExcluir, int idApresentacao) {
        if (autoresAExcluir.size() < 1) {
            return false;
        }

        for (int a = 0; a < autoresAExcluir.size(); a++) {
            if (autoresAExcluir.get(a) != -1) {
                if (!excluirAutor(autoresAExcluir.get(a), idApresentacao)) {
                    return false;
                } else {
                    System.out.println("Excluiu autor de código " + autoresAExcluir.get(a) + ".");
                }
            }
        }

        return true;
    }

    public boolean excluirAutor(int at, int idApresentacao) {
        String SQL = "DELETE FROM convenio WHERE apresentacao = " + idApresentacao + " AND autor = " + at + ";";
        if (BancoDeDados.comando().updateOUinsert(SQL) < 1) {
            return false;
        }

        SQL = "DELETE FROM autor WHERE id = " + at + ";";

        return (BancoDeDados.comando().updateOUinsert(SQL) > 0);

    }

    public boolean atualizarApresentacao(apresentacao apr) {

        String SQL = "UPDATE apresentacao SET"
                + " nome=?, grupo=?, resumo=?, "
                + "palavraschave=?, materiais=?, apresentador=?,"
                + "modalidade=?, subarea =?, evento=?, "
                + "linkvideo=?, areatematica=?, categoria=?, inclusao=?, diversidade=? WHERE id = ?;";
        try {
            PreparedStatement stmt1 = con.prepareStatement(SQL);
            stmt1.setString(1, apr.getNometitulo());
            stmt1.setString(2, apr.getGrupoNome());
            stmt1.setString(3, apr.getResumo());

            stmt1.setString(4, apr.getPalavrasChave());
            stmt1.setString(5, apr.getMateriais());
            stmt1.setString(6, apr.getApresentadorCpf());

            stmt1.setInt(7, apr.getModalidadeCod());
            stmt1.setInt(8, apr.getSubareaCod());
            stmt1.setString(9, apr.getEventoCod());

            stmt1.setString(10, apr.getLinkParaVideo());
            stmt1.setInt(11, apr.getTematicaExtensaoCod());
            stmt1.setInt(12, apr.getCategoriaCod());
            

            stmt1.setString(13,apr.getInclusao());
            stmt1.setString(14,apr.getDiversidade());
            
            stmt1.setInt(15, apr.getId());
            // System.out.println(stmt1);
            
            stmt1.execute();

            ArrayList<autor> autores = apr.getAutores();

            if (!atualizarConvenioApresentador(apr)) {
                return false;
            }

            for (int a = 0; a < autores.size(); a++) {
                if (autores.get(a).isNovo()) {
                    System.out.println("Cadastrando novo autor...");
                    autores.get(a).setApresentacaoCod(apr.getId());
                    cadastrarAutor(autores.get(a), apr.getModalidadeCod() == 4);
                } else {  // atualizar dados de autor e convênio
                    atualizarAutor(autores.get(a));
                }
            }

            return true;
        } catch (SQLException ex) {
            Logger.getLogger(BancoDeDados.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

    }

    private boolean cadastrarConvenioApresentador(apresentacao apr) {
        String SQL = "INSERT INTO convenio(apresentador,apresentacao,bolsa) VALUES"
                + "(?,?,?);";
        try {
            PreparedStatement stmt1 = con.prepareStatement(SQL);
            stmt1.setString(1, apr.getApresentadorCpf());
            stmt1.setInt(2, apr.getId());
            stmt1.setInt(3, apr.getBolsaApresentadorCod());

            stmt1.execute();
            System.out.println("Cadastrou convênio do apresentador " + apr.getApresentadorNome() + ".");

            return true;
        } catch (SQLException ex) {
            Logger.getLogger(BancoDeDados.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Falha cadastrando convênio do apresentador " + apr.getApresentadorNome() + ".");
            return false;
        }
    }

    public boolean atualizarConvenioApresentador(apresentacao apr) {
        String SQL = "UPDATE convenio SET bolsa=? WHERE apresentador = ? AND apresentacao = ?;";
        try {
            PreparedStatement stmt1 = con.prepareStatement(SQL);
            stmt1.setInt(1, apr.getBolsaApresentadorCod());
            stmt1.setString(2, apr.getApresentadorCpf());
            stmt1.setInt(3, apr.getId());

            stmt1.execute();
            System.out.println("Atualizou convênio do apresentador " + apr.getApresentadorNome() + "(" + apr.getApresentadorCpf() + ").");

            return true;
        } catch (SQLException ex) {
            Logger.getLogger(BancoDeDados.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Falha atualizando convênio do apresentador " + apr.getApresentadorNome() + ".");
            return false;
        }
    }

    private int recuperarBolsaApresentador(int id, String Cpf) {
        int r = -1;
        String SQL = "SELECT bolsa FROM convenio WHERE apresentador = '" + Cpf + "' AND apresentacao = " + id + ";";
        ResultSet rs = select(SQL);
        try {
            while (rs.next()) {
                r = rs.getInt("bolsa");
            }
        } catch (SQLException ex) {
            Logger.getLogger(BancoDeDados.class.getName()).log(Level.SEVERE, null, ex);
        }

        return r;
    }

    public boolean inserirAvaliador(avaliador novo) {
        String SQL = "INSERT INTO avaliador(nome,cpf,siape,senha,sexo,"
                + "titulacao,email,telefone,endereco,municipio,"
                + "estado,bairro,cep,banco,agencia,"
                + "conta,tipoconta,unidade,tipopessoa,formacao, rg, observacoes) VALUES"
                + "(?,?,?,md5(?),?,"
                + "?,?,?,?,?,"
                + "?,?,?,?,?,"
                + "?,?,?,?,?,?,?);";
        try {
            PreparedStatement st = con.prepareStatement(SQL);
            st.setString(1, novo.getNome());
            st.setString(2, novo.getCpf());
            st.setInt(3, novo.getSiape());
            st.setString(4, novo.getSenha());
            st.setString(5, novo.getSexo());

            st.setString(6, novo.getTitulacaoNome());
            st.setString(7, novo.getEmail());
            st.setString(8, novo.getTelefone());
            st.setString(9, novo.getEndereco());
            st.setString(10, novo.getMunicipio());

            st.setString(11, novo.getEstado());
            st.setString(12, novo.getBairro());
            st.setString(13, novo.getCep());
            st.setString(14, novo.getBanco());
            st.setString(15, novo.getAgencia());

            st.setString(16, novo.getConta());
            st.setString(17, novo.getTipoconta());
            st.setInt(18, novo.getUnidadeCod());
            st.setInt(19, novo.getTipoPessoaCod());
            st.setString(20, novo.getFormacao());
            
            st.setString(21, novo.getRg());
            st.setString(22, novo.getObservacoes());

            st.execute();
            System.out.println("Cadastrou avaliador " + novo.getNome() + ".");

            return inserirAreasQueAvalia(novo.getAreaCod(), novo.getCpf());
        } catch (SQLException ex) {
            Logger.getLogger(BancoDeDados.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Falha cadastrando avaliador " + novo.getNome() + ".");
            return false;
        }
    }

    public boolean inserirAreasQueAvalia(ArrayList<Integer> areas, String cpf) {
        System.out.println(areas.size() + " a avaliar para inserir...");
        String SQL;
        for (int a = 0; a < areas.size(); a++) {
            SQL = "INSERT INTO avalia(avaliador, area) VALUES"
                    + "('" + cpf + "'," + areas.get(a) + ");";
            if (updateOUinsert(SQL) < 1) {
                System.out.println("Falha para inserir registro de que avaliador avalia area.");
                return false;
            }
        }

        return true;
    }

    public boolean atualizarAvaliador(avaliador novo, boolean atualizarSenha, boolean atualizaAreas) {
        String SQL = "UPDATE avaliador SET nome=?,siape=?,sexo=?,"
                + "titulacao=?,email=?,telefone=?,endereco=?,municipio=?,"
                + "estado=?,bairro=?,cep=?,banco=?,agencia=?,"
                + "conta=?,tipoconta=?,unidade=?,tipopessoa=?,formacao=?,acompanhante=?,rg=?,observacoes=?";
        if (atualizarSenha) {
            SQL = SQL + ", senha = md5(?)";
        }

        SQL = SQL + " WHERE cpf = ?;";
        try {
            //nome, siape, sexo,titulacao,email,telefone,endereco,municipio,estado,bairro,cep,banco,agencia,conta,tipoconta,unidade,formacao,acompanhante
            PreparedStatement st = con.prepareStatement(SQL);
            st.setString(1, novo.getNome());
            st.setInt(2, novo.getSiape());
            st.setString(3, novo.getSexo());

            st.setString(4, novo.getTitulacaoNome());
            st.setString(5, novo.getEmail());
            st.setString(6, novo.getTelefone());
            st.setString(7, novo.getEndereco());
            st.setString(8, novo.getMunicipio());

            st.setString(9, novo.getEstado());
            st.setString(10, novo.getBairro());
            st.setString(11, novo.getCep());
            st.setString(12, novo.getBanco());
            st.setString(13, novo.getAgencia());

            st.setString(14, novo.getConta());
            st.setString(15, novo.getTipoconta());
            st.setInt(16, novo.getUnidadeCod());
            st.setInt(17, novo.getTipoPessoaCod());
            st.setString(18, novo.getFormacao());
            st.setInt(19, novo.getAcompanhante());
            
            st.setString(20, novo.getRg());
            st.setString(21, novo.getObservacoes());
            
            if (atualizarSenha) {
                st.setString(22, novo.getSenha());
                st.setString(23, novo.getCpf());
            } else {
                st.setString(22, novo.getCpf());
            }

            st.execute();
            System.out.println("Atualizou avaliador " + novo.getNome() + ".");

            if (atualizaAreas) {
                return atualizarAreasQueAvalia(novo);
            } else {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(BancoDeDados.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Falha atualizando avaliador " + novo.getNome() + ".");
            return false;
        }

    }

    public avaliador recuperarAvaliador(String cpf) {
        avaliador av = new avaliador();
        av.setNome("null");
        String SQL = "SELECT a.*, t.nome AS tipopessoanome, u.nome AS unidadenome "
                + "FROM avaliador a, tipopessoa t, unidade u "
                + "WHERE cpf = '" + cpf + "' AND t.id = a.tipopessoa AND u.id = a.unidade;";
        ResultSet rs = select(SQL);
        try {
            while (rs.next()) {
                av.setAgencia(rs.getString("agencia"));
                av.setBairro(rs.getString("bairro"));
                av.setBanco(rs.getString("banco"));
                av.setCep(rs.getString("cep"));
                av.setConta(rs.getString("conta"));
                av.setCpf(cpf);
                av.setEmail(rs.getString("email"));
                av.setEndereco(rs.getString("endereco"));
                av.setEstado(rs.getString("estado"));
                av.setMunicipio(rs.getString("municipio"));
                av.setNome(rs.getString("nome"));
                av.setSexo(rs.getString("sexo"));
                av.setSiape(rs.getInt("siape"));
                av.setTelefone(rs.getString("telefone"));
                av.setTipoPessoaCod(rs.getInt("tipopessoa"));
                av.setTipoPessoaNome(rs.getString("tipopessoanome"));
                av.setTipoconta(rs.getString("tipoconta"));
                av.setTitulacaoNome(rs.getString("titulacao"));
                av.setUnidadeCod(rs.getInt("unidade"));
                av.setFormacao(rs.getString("formacao"));
                av.setUnidadeNome(rs.getString("unidadenome"));
                av.setAcompanhante(rs.getInt("acompanhante") > 0);
                
                av.setRg(rs.getString("rg"));
                av.setObservacoes(rs.getString("observacoes"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(BancoDeDados.class.getName()).log(Level.SEVERE, null, ex);
        }
        av = recuperarAreasQueAvalia(av);
        return av;
    }

    public ArrayList<avaliador> recuperarTodosAvaliadores(String ev) {
        ArrayList<avaliador> avaliadores = new ArrayList<>();

        avaliador av;
        String SQL = "SELECT a.*, t.nome AS tipopessoanome, u.nome AS unidadenome "
                + "FROM avaliador a, tipopessoa t, unidade u "
                + "WHERE t.id = a.tipopessoa AND u.id = a.unidade ORDER BY a.nome;";
        ResultSet rs = select(SQL);
        try {
            while (rs.next()) {
                av = new avaliador();
                av.setAgencia(rs.getString("agencia"));
                av.setAcompanhante(rs.getInt("acompanhante") > 0);
                av.setBairro(rs.getString("bairro"));
                av.setFormacao(rs.getString("formacao"));
                av.setBanco(rs.getString("banco"));
                av.setCep(rs.getString("cep"));
                av.setConta(rs.getString("conta"));
                av.setCpf(rs.getString("cpf"));
                av.setEmail(rs.getString("email"));
                av.setEndereco(rs.getString("endereco"));
                av.setEstado(rs.getString("estado"));
                av.setMunicipio(rs.getString("municipio"));
                av.setNome(rs.getString("nome"));
                av.setSexo(rs.getString("sexo"));
                av.setSiape(rs.getInt("siape"));
                av.setTelefone(rs.getString("telefone"));
                av.setTipoPessoaCod(rs.getInt("tipopessoa"));
                av.setTipoPessoaNome(rs.getString("tipopessoanome"));
                av.setTipoconta(rs.getString("tipoconta"));
                av.setTitulacaoNome(rs.getString("titulacao"));
                av.setUnidadeCod(rs.getInt("unidade"));
                av.setUnidadeNome(rs.getString("unidadenome"));
                av.setHorasCert(rs.getInt("horas"));
                avaliadores.add(av);
            }
        } catch (SQLException ex) {
            Logger.getLogger(BancoDeDados.class.getName()).log(Level.SEVERE, null, ex);
        }
        //evento e = BancoDeDados.comando().getEventoAtual();
        for (int a = 0; a < avaliadores.size(); a++) {
            avaliadores.set(a, recuperarAreasQueAvalia(avaliadores.get(a)));
            avaliadores.get(a).setAvaliacoesPossiveis(recuperarAvaliacoes(avaliadores.get(a).getCpf(), 1, -1, false, false, ev));
        }

        return avaliadores;
    }

    public ArrayList<avaliador> recuperarAvaliadoresPossiveisParaArea(int area, int centr, int apresentacao,
            boolean somenteAcompanhante, String evt) {
        ArrayList<avaliador> avs = new ArrayList<>();
        String SQL = "SELECT DISTINCT a.*, t.nome AS tipopessoanome, u.nome AS unidadenome "
                + "FROM avaliador a, tipopessoa t, unidade u";

        if (area > -1) {
            SQL = SQL + ", avalia av";
        }

        if (apresentacao > -1) {
            SQL = SQL + ", apresentador ap, apresentacao apr";
        }

        SQL = SQL + " WHERE";

        if (somenteAcompanhante) {
            SQL = SQL + " a.acompanhante>0 AND";
        }

        SQL = SQL + " a.tipopessoa = t.id AND a.unidade = u.id"; //identifica tipopessoa, unidade do avaliador

        if (area > -1) {
            SQL = SQL + " AND av.avaliador = a.cpf AND av.area = " + area;   //grande area ou area tematica bate 
        }

        if (apresentacao > -1) {
            if (centr == 0) {
                SQL = SQL + " AND a.unidade = ap.unidade AND apr.apresentador = ap.cpf AND apr.id = " + apresentacao; // SE não centralizado, avaliador de mesma unidade da apresentacao
            } else {
                SQL = SQL + " AND a.unidade <> ap.unidade AND apr.apresentador = ap.cpf AND apr.id = " + apresentacao; // se centralizado, avaliador deve ser de outra unidade
            }
        }

        SQL = SQL + " ORDER BY a.nome;";
        // System.out.println(SQL);

        ResultSet rs = select(SQL);
        try {
            avaliador av;
            while (rs.next()) {
                av = new avaliador();

                av.setCpf(rs.getString("cpf"));
                av.setSexo(rs.getString("sexo"));
                av.setSiape(rs.getInt("siape"));
                av.setEndereco(rs.getString("endereco"));
                av.setMunicipio(rs.getString("municipio"));
                av.setEstado(rs.getString("estado"));
                av.setBairro(rs.getString("bairro"));
                av.setCep(rs.getString("cep"));
                av.setBanco(rs.getString("banco"));
                av.setAgencia(rs.getString("agencia"));
                av.setConta(rs.getString("conta"));
                av.setTipoconta(rs.getString("tipoconta"));
                av.setNome(rs.getString("nome"));
                av.setEmail(rs.getString("email"));
                av.setTelefone(rs.getString("telefone"));
                av.setTipoPessoaCod(rs.getInt("tipopessoa"));
                av.setTipoPessoaNome(rs.getString("tipopessoanome"));
                av.setTitulacaoNome(rs.getString("titulacao"));
                av.setUnidadeCod(rs.getInt("unidade"));
                av.setFormacao(rs.getString("formacao"));
                av.setUnidadeNome(rs.getString("unidadenome"));
                av.setAcompanhante((rs.getInt("acompanhante") > 0));
                avs.add(av);
            }

        } catch (SQLException ex) {
            Logger.getLogger(BancoDeDados.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (area == -1) {

            for (avaliador a : avs) {
                a.setAreaNome(recuperarAreasQueAvalia(a).getAreaNome());
            }

        }

        for (avaliador a : avs) {
            a.setAvaliacoesPossiveis(BancoDeDados.comando().recuperarAvaliacoes(a.getCpf(), centr, apresentacao, false, true, evt));
        }

        return avs;
    }

    public avaliador recuperarAreasQueAvalia(avaliador av) {
        String SQL = "SELECT DISTINCT area.id, area.nome "
                + "FROM area, avalia, avaliador "
                + "WHERE avalia.area = area.id AND avalia.avaliador = '" + av.getCpf() + "';";
        ResultSet rs = select(SQL);
        try {
            while (rs.next()) {
                av.addExcluirArea(rs.getString("nome"), rs.getInt("id"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(BancoDeDados.class.getName()).log(Level.SEVERE, null, ex);
        }
        return av;
    }

    public boolean atualizarAreasQueAvalia(avaliador novo) {
        boolean a, b;
        //excluir areas que nao avaliara mais
        a = excluirAreasQueAvalia(novo.getExcluirAreaCod(), novo.getCpf());
        if (!a) {
            System.out.println("Falha ao tentar excluir areas a serem avaliadas");
        }

        //incluir  areas que avaliará
        b = inserirAreasQueAvalia(novo.getAreaCod(), novo.getCpf());
        if (!b) {
            System.out.println("Falha ao tentar incluir novas areas a serem avaliadas");
        }

        return a && b;
    }

    public boolean excluirAreasQueAvalia(ArrayList<Integer> areas, String cpf) {
        System.out.println(areas.size() + " a avaliar para excluir...");
        String SQL;
        for (int a = 0; a < areas.size(); a++) {
            SQL = "DELETE FROM avalia WHERE avaliador = '" + cpf + "' AND area = " + areas.get(a) + ";";
            if (updateOUinsert(SQL) < 1) {
                System.out.println("Falha para excluir registro de que avaliador avalia area.");
                return false;
            }
        }

        return true;

    }

    public ArrayList<coordenador> recuperaLacosCoordenador(String cpf) {
        ArrayList<coordenador> c = new ArrayList<>();
        ResultSet rx = select("SELECT c.*, u.nome AS unity, u.limiteapresentacoesselecionadas AS las, u.limitecomunicacaooralselecionadas AS lcos,"
                + " u.limiteiftech, u.limiterobotica, a.nome "
                + "FROM coordenador c, avaliador a, unidade u "
                + "WHERE c.avaliador = '" + cpf + "' AND c.avaliador = a.cpf AND a.unidade = u.id;");
        coordenador c1;
        try {
            while (rx.next()) {
                c1 = new coordenador();
                c1.setAvaliador(rx.getString("avaliador"));
                c1.setAvaliadorNome(rx.getString("a.nome"));
                c1.setCentralizado(rx.getInt("centralizado"));
                c1.setUnidadeCodigo(rx.getInt("unidade"));
                c1.setUnidadeNome(rx.getString("unity"));
                c1.setLimiteiftech(rx.getInt("limiteiftech"));
                c1.setLimiterobotica(rx.getInt("limiterobotica"));
                c1.setLimitecomunicacaooralselecionadas(rx.getInt("lcos"));
                c1.setLimiteApresentacoesSelecionadas(rx.getInt("las"));
                c.add(c1);
                System.out.println("Result " + c.size());
            }
        } catch (SQLException ex) {
            Logger.getLogger(BancoDeDados.class.getName()).log(Level.SEVERE, null, ex);
        }
        return c;
    }

    public coordenador recuperaCoordenadorUnidade(unidade u) {
        coordenador c1 = new coordenador();
        c1.setAvaliador("-");
        c1.setUnidadeCodigo(u.getId());
        c1.setAvaliadorNome("-");

        if (this.selectCount("SELECT count(avaliador) FROM coordenador WHERE unidade =" + u.getId() + ";") == 0) {
            return c1;
        }

        ResultSet rx = select("SELECT c.*, u.nome AS unity, u.limiteapresentacoesselecionadas AS las, u.limitecomunicacaooralselecionadas AS lcos,"
                + " u.limiteiftech, u.limiterobotica, a.nome as avnome "
                + "FROM coordenador c, avaliador a, unidade u "
                + "WHERE a.unidade = " + u.getId() + " AND c.avaliador = a.cpf AND a.unidade = u.id;");
        // coordenador c1;
        try {
            while (rx.next()) {
                //c1 = new coordenador();
                c1.setAvaliador(rx.getString("avaliador"));
                c1.setAvaliadorNome(rx.getString("avnome"));
                c1.setCentralizado(rx.getInt("centralizado"));
                c1.setUnidadeCodigo(rx.getInt("unidade"));
                c1.setUnidadeNome(rx.getString("unity"));
                c1.setLimiteiftech(rx.getInt("limiteiftech"));
                c1.setLimiterobotica(rx.getInt("limiterobotica"));
                c1.setLimitecomunicacaooralselecionadas(rx.getInt("lcos"));
                c1.setLimiteApresentacoesSelecionadas(rx.getInt("las"));
                //c.add(c1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(BancoDeDados.class.getName()).log(Level.SEVERE, null, ex);
        }
        return c1;
    }

    //(avaliadores.get(a).getCpf(), 1, -1, false,false,e.getId()));
    public ArrayList<avaliacao> recuperarAvaliacoes(String avaliador, int centr, int apresentacao,
            boolean comOral, boolean todasComInativasTambem, String evento) {
        ArrayList<avaliacao> avs = new ArrayList<>();
        String SQL = "null nao definido";
        int id;
        int oral = 0;

        if (apresentacao < -1) {
            apresentacao = -apresentacao;
        }

        if (comOral) {
            oral = 1;
        }

        if (apresentacao > -1) {   //Para encontrar avalicoes de uma apresentacao especifica

            SQL = "SELECT av.*, v.nome as avnome, v.cpf as avcpf, u.nome as unome, ap.nome as apnome, v.email, apr.nome as aprnome, c.nome as cnome, u2.nome as u2nome "
                    + "FROM avaliacao av, avaliador v, unidade u, apresentacao ap, apresentador apr, categoria c, unidade u2 "
                    + "WHERE av.avaliador = v.cpf AND v.unidade = u.id AND av.apresentacao = ap.id AND ap.apresentador = apr.cpf AND ap.categoria = c.id AND apr.unidade = u2.id "
                    + "AND av.apresentacao = " + apresentacao + " AND av.centralizado = " + centr + " AND av.oral = " + oral + " AND ap.evento = '" + evento + "';";

        } else if (apresentacao < 0) { // caso não seja de apresentação especifica
            if (avaliador.length() < 3) // sem avaliador definido
            {
                SQL = "SELECT av.*, v.nome as avnome, v.cpf as avcpf, u.nome as unome, ap.nome as apnome, v.email, apr.nome as aprnome, c.nome as cnome, u2.nome as u2nome "
                        + "FROM avaliacao av, avaliador v, unidade u, apresentacao ap, apresentador apr, categoria c, unidade u2 "
                        + "WHERE av.avaliador = v.cpf AND v.unidade = u.id AND av.apresentacao = ap.id AND ap.apresentador = apr.cpf AND ap.categoria = c.id AND apr.unidade = u2.id "
                        + "AND av.centralizado = " + centr + " AND av.ativa=1 AND av.oral = " + oral + " AND ap.evento = '" + evento + "';";//todas as avaliações ativas
                if (todasComInativasTambem) {
                    SQL = "SELECT av.*, v.nome as avnome, v.cpf as avcpf, u.nome as unome, ap.nome as apnome, v.email, apr.nome as aprnome, c.nome as cnome, u2.nome as u2nome "
                            + "FROM avaliacao av, avaliador v, unidade u, apresentacao ap, apresentador apr, categoria c, unidade u2 "
                            + "WHERE av.avaliador = v.cpf AND v.unidade = u.id AND av.apresentacao = ap.id AND ap.apresentador = apr.cpf AND ap.categoria = c.id AND apr.unidade = u2.id "
                            + "AND av.centralizado = " + centr + " AND av.oral = " + oral + " AND ap.evento = '" + evento + "';";//todas as avaliações ativas
                }

            } else {// para determinado avaliador, todas as  apresentaçoes
                SQL = "SELECT av.*, v.nome as avnome, u.nome as unome, ap.nome as apnome, v.email, apr.nome as aprnome, c.nome as cnome, u2.nome as u2nome "
                        + "FROM avaliacao av, avaliador v, unidade u, apresentacao ap, apresentador apr, categoria c, unidade u2 "
                        + "WHERE av.avaliador = v.cpf AND v.unidade = u.id AND (av.apresentacao = ap.id) AND ap.apresentador = apr.cpf AND ap.categoria = c.id AND apr.unidade = u2.id "
                        + "AND av.avaliador = '" + avaliador + "' AND av.centralizado = " + centr + " AND av.oral = " + oral + " AND ap.evento = '" + evento + "';";
               // System.out.println(SQL);
            }
        }
        // System.out.println(SQL);
        ResultSet rs = select(SQL);
        try {
            while (rs.next()) {

                id = rs.getInt("apresentacao");
                boolean comOralIndividuoNoGrupo = id < 0;

                avaliacao av = new avaliacao();
                av.setApresentacao(id);
                if (comOral) {
                    av.setApresentacao(-apresentacao);
                }
                av.setApresentacaoNome(rs.getString("apnome"));
                if (comOral || comOralIndividuoNoGrupo) {
                    av.setApresentacaoNome("Com. Oral: " + rs.getString("apnome"));
                }
                av.setApresentadorNome(rs.getString("aprnome"));
                av.setCategoriaNome(rs.getString("cnome"));
                av.setAusente(rs.getInt("ausente") == 1);
                av.setExcluirInicial(rs.getInt("ativa") == 0);
                av.setAvaliadorNome(rs.getString("avnome"));
                av.setUnidadeAvaliadorNome(rs.getString("unome"));
                av.setAvaliadorEmail(rs.getString("email"));
                av.setUnidadeApresentadorNome(rs.getString("u2nome"));
                //av.setAtiva(rs.getInt("ativa")==1);
                av.setAvaliador(avaliador);

                if (avaliador.length() < 3) {
                    av.setAvaliador(rs.getString("avcpf"));
                }

                av.setCentralizado(centr);
                av.setOral(oral);
                //av.setExcluir(false);
                av.setNotaInicial(rs.getInt("nota"));
                av.setSalvoNoBD(true);
                avs.add(av);
                System.out.println("Recupero avaliacao num " + avs.size());
            }
        } catch (SQLException ex) {
            Logger.getLogger(BancoDeDados.class.getName()).log(Level.SEVERE, null, ex);
        }
        return avs;
    }

    public int getCentralizado() {
        return (int) centralizado;
    }

    public boolean excluirAvaliacao(avaliacao av) {
        String SQL = "DELETE FROM avaliacao "
                + "WHERE apresentacao = " + av.getApresentacao() + " AND "
                + "avaliador = '" + av.getAvaliador() + "' AND "
                + "centralizado = " + av.getCentralizado() + ";";
        int r = updateOUinsert(SQL);

        if (r > 0) {
            System.out.println("Exclusão de avaliação da apresentação " + av.getApresentacao() + " efetuada.");
            return true;
        } else {
            System.out.println("Exclusão de avaliação da apresentação " + av.getApresentacao() + " FALHOU!.");
            return false;
        }

    }

    public boolean incluirAvaliacao(avaliacao av) {
        String SQL = "INSERT INTO avaliacao(nota,avaliador,apresentacao,centralizado,ausente,ativa,oral) "
                + "VALUES(" + av.getNotaFinal() + ",'" + av.getAvaliador() + "',"
                + av.getApresentacao() + "," + av.getCentralizado() + "," + av.getAusente() + "," + av.getAtiva() + "," + av.getOral() + ");";
        int r = updateOUinsert(SQL);
        if (r > 0) {
            System.out.println("Inclusão de avaliação da apresentação " + av.getApresentacao() + " efetuada.");
            return true;
        } else {
            System.out.println("Inclusão de avaliação da apresentação " + av.getApresentacao() + " FALHOU!.");
            return false;
        }

    }

    public boolean atualizarAvaliacao(avaliacao av, String novoAvaliador) {
        String SQL = "UPDATE avaliacao SET nota = " + av.getNotaFinal() + ", ausente = " + av.getAusente() + ", "
                + "ativa = " + av.getAtiva() + ", avaliador = '" + novoAvaliador + "'"
                + " WHERE avaliador = '" + av.getAvaliador() + "' AND "
                + "apresentacao = " + av.getApresentacao() + " AND "
                + "centralizado = " + av.getCentralizado() + " AND oral = " + av.getOral() + ";";
        int r = updateOUinsert(SQL);
        if (r > 0) {
            System.out.println("Atualização de avaliação da apresentação " + av.getApresentacao() + " efetuada.");
            return true;
        } else {
            System.out.println("Atualização de avaliação da apresentação " + av.getApresentacao() + " FALHOU!.");
            return false;
        }
    }

    public ArrayList<rankingApresentacao> getRankingsApresentacoes(String evento, String eventoIssn, boolean selecionaComEvento,
            int centralizado, String unidadeNome, int unidadeCodigo, boolean selecionaComUnidade,
            String areaNome, int areaCodigo, boolean selecionaComArea, String subareaNome, int subareaCodigo, boolean selecionaComSubarea,
            String categoriaNome, int categoriaCodigo, boolean selecionaComModalidade, int oral, boolean resumo, String nivel) {

        ArrayList<categoria> categorias = getCategorias();

        ArrayList<rankingApresentacao> ra = new ArrayList<>();
        String SQL = "SELECT DISTINCT AVG(av.nota) AS media, COUNT(DISTINCT av.avaliador) AS navaliacoes, "
                + "aut.nome, aut.unidade, aut.estadodenaturalidade as tipocurso, "
                + "u.nome AS unnome, u.id AS unid, "
                + "ap.id AS aprid, ap.nome AS aprnome, ap.apresentador AS aprescpf, aut.nome AS apresnome, "
                + "sa.nome AS sanome, sa.id AS said, a.nome AS anome, a.id AS aid, av.oral, ap.categoria "
                + "FROM avaliacao av, apresentacao ap, apresentador aut, unidade u, subarea sa, area a "
                + "WHERE aut.unidade = u.id AND av.apresentacao = ap.id AND ap.apresentador = aut.cpf AND ap.subarea = sa.id AND sa.area = a.id "
                + "AND centralizado = " + centralizado + " AND ativa > 0 AND av.oral = " + oral + " ";
        if (selecionaComEvento) {
            SQL = SQL + "AND ap.evento = '" + eventoIssn + "' ";
        }
        if (selecionaComUnidade) {
            SQL = SQL + "AND aut.unidade = " + unidadeCodigo + " ";
        }
        if (selecionaComArea) {
            SQL = SQL + "AND sa.area = " + areaCodigo + " ";
        }
        if (selecionaComSubarea) {
            SQL = SQL + "AND ap.subarea = " + subareaCodigo + " ";
        }
        if (selecionaComModalidade) {
            SQL = SQL + "AND ap.categoria = " + categoriaCodigo + " ";
        }
        
        if(nivel.equals("Nivel Medio")) {
            SQL = SQL + "AND aut.estadodenaturalidade = 'Nivel Medio' ";
        } else if (nivel.equals("pos")) {
            SQL = SQL + "AND aut.estadodenaturalidade <> 'Nivel Medio' ";
        }

        /*if (resumo) {
            SQL = SQL + "GROUP BY ap.id ORDER BY categoria, area, AVG(nota) DESC;";
        } else {
            SQL = SQL + "GROUP BY ap.id ORDER BY AVG(nota) DESC;";
        }*/
        SQL = SQL + "GROUP BY ap.id ORDER BY AVG(nota) DESC;";
        
        int colocacao = 1;
        ResultSet r = select(SQL);
        rankingApresentacao aux;
        try {
            while (r.next()) {
                aux = new rankingApresentacao();

                aux.setApresentacaoID(r.getInt("aprid"), r.getInt("av.oral"));
                aux.setApresentacaoNome(r.getString("aprnome"));
                aux.setApresentadorCpf(r.getString("aprescpf"));
                aux.setApresentadorNome(r.getString("apresnome"));
                aux.setAreaID(r.getInt("aid"));
                aux.setAreaNome(r.getString("anome"));
                aux.setMedia(r.getInt("media"));
                aux.setNumeroAvaliacoes(r.getInt("navaliacoes"));
                aux.setSubAreaID(r.getInt("said"));
                aux.setSubAreaNome(r.getString("sanome"));
                aux.setUnidadeID(r.getInt("unid"));
                aux.setUnidadeNome(r.getString("unnome"));
                aux.setCategoriaId(r.getInt("categoria"));
                aux.setCategoriaNome(aux.getCategoriaId(), categorias);
                aux.setTipoCurso(r.getString("tipocurso"));

                aux.setColocacao(colocacao);
                colocacao++;

                ra.add(aux);

            }
        } catch (SQLException ex) {
            Logger.getLogger(BancoDeDados.class.getName()).log(Level.SEVERE, null, ex);
        }

        //parte com negativos
        /// parte final
        SQL = "Recuperou " + ra.size() + " posições em ranking. Selecionou usando: ";
        if (selecionaComEvento) {
            SQL = SQL + "Evento; ";
        }
        if (selecionaComModalidade) {
            SQL = SQL + "Modalidade; ";
        }
        if (selecionaComUnidade) {
            SQL = SQL + "Unidade; ";
        }
        if (selecionaComArea) {
            SQL = SQL + "Area; ";
        }
        if (selecionaComSubarea) {
            SQL = SQL + "Subarea; ";
        }

        if (centralizado > 0) {
            SQL = SQL + "Para o evento centralizado.";
        } else {
            SQL = SQL + "Para o evento nos campi.";
        }
        // System.out.println(SQL);

       /* if (resumo) {
            colocacao = 1;
            int maior;
            int indexMaior;
            ArrayList<Integer> foi = new ArrayList<>();

            while (colocacao <= ra.size()) { //enquanto ouvir objetos em RA...
                maior = -1;
                indexMaior = -1;

                for (int i = 0; i < ra.size(); i++) { //procura maior média
                    if(!foi.contains(i))
                    if (ra.get(i).getMedia() > maior) {
                        maior = ra.get(i).getMedia();
                        indexMaior = i;
                    }
                }//encontrou maior nota
                
                foi.add(indexMaior);
                ra.get(indexMaior).setColocacao(colocacao); //arruma colocacao
                colocacao++;
                
            }
            
            ArrayList<String> areaJaFoi = new ArrayList<>();
            int mostraIfTechs = 2;
            ArrayList<rankingApresentacao> RA2 = new ArrayList<>();
            //for(int x=1;x<=ra.size();x++)
                 for (int i = 0; i < ra.size(); i++)
                  //   if(ra.get(i).getColocacao()==x)
                     if (resumo && areaJaFoi.indexOf(ra.get(i).getAreaNome()) != -1) {
                //acrescenta somente se Iftech e dentro dos limites
                if(ra.get(i).getCategoriaId()==4  && mostraIfTechs>0){ //categoria 4 é IFTECH
                    areaJaFoi.add("IFtech"+mostraIfTechs);
                    mostraIfTechs--;
                    
                    RA2.add(ra.get(i));
                }
                

            } else {                
                    areaJaFoi.add(ra.get(i).getAreaNome());
                    RA2.add(ra.get(i));                         
             }
                         
            
            ra = RA2;
        }*/

        return ra;
    }

    public Connection getConnection() {
        return con;
    }

    boolean loginAvaliador(String text, String text0) {
        String SQL = "SELECT COUNT(*) AS conte FROM avaliador WHERE cpf = ? AND"
                + " senha = md5(?);";

        try {
            PreparedStatement stmt1 = con.prepareStatement(SQL);
            stmt1.setString(1, text);
            stmt1.setString(2, text0);

            ResultSet rs = stmt1.executeQuery();

            while (rs.next()) {
                return (rs.getInt("conte") > 0);
            }

        } catch (SQLException ex) {
            Logger.getLogger(BancoDeDados.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

        return false;
    }

    public boolean atualizarApresentacaoModoDeSelecao(apresentacao ap) {

        // atualizar "hospedagem", "transporte", "alimentacao", "selecionada" e "apresentadorEfetivo"/NYI
        String SQL = "UPDATE apresentacao SET "
                + "hospedagem = " + ap.getHospedagens() + ", transporte = " + ap.getTransportes() + ", "
                + "alimentacao = " + ap.getAlimentacoes() + ", selecionada = " + ap.getSelecionado()
                + ", apresentadorefetivo = " + ap.getApresentadorEfetivoID() + " WHERE id = " + ap.getId() + ";";

        return updateOUinsert(SQL) > 0;

    }

    public void relatorioUnidade(int codUnidade, int centralizado, relatorioUnidade relat, evento e) {

        // relatorio por bolsa
        String SQL = "SELECT COUNT(DISTINCT a.id) AS q, b.id AS bid, b.nome AS bnome"
                + " FROM apresentacao a, unidade u, bolsa b, apresentador apr, convenio c"
                + " WHERE apr.unidade = u.id AND a.apresentador = apr.cpf AND u.id =" + codUnidade
                + " AND c.bolsa = b.id AND c.apresentador = apr.cpf AND c.apresentacao = a.id AND a.evento='" + e.getId() + "'";
        if (centralizado == 1) {
            SQL = SQL + " AND a.selecionada = " + centralizado + "";
        }
        SQL = SQL + " GROUP BY b.id;";
        ResultSet rs = select(SQL);

        try {
            while (rs.next()) {
                relat.adicionarQuantidadePorBolsa(rs.getInt("q"), new bolsa(rs.getString("bnome"), rs.getInt("bid")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(BancoDeDados.class.getName()).log(Level.SEVERE, null, ex);
        }
        // relatorio por modalidade
        SQL = "SELECT COUNT(DISTINCT a.id) AS q, m.id AS mid, m.nome AS mnome"
                + " FROM apresentacao a, unidade u, modalidade m, apresentador apr"
                + " WHERE apr.unidade = u.id AND a.apresentador = apr.cpf AND u.id =" + codUnidade
                + " AND a.modalidade = m.id AND a.evento='" + e.getId() + "'";
        if (centralizado == 1) {
            SQL = SQL + " AND a.selecionada = " + centralizado + "";
        }
        SQL = SQL + " GROUP BY m.id;";
        ResultSet rs2 = select(SQL);

        try {
            while (rs2.next()) {
                relat.adicionarQuantidadePorModalidade(rs2.getInt("q"), new modalidade(rs2.getString("mnome"), rs2.getInt("mid")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(BancoDeDados.class.getName()).log(Level.SEVERE, null, ex);
        }
        // relatorio de materiais necessarios
        SQL = "SELECT a.materiais, a.id, a.nome"
                + " FROM apresentacao a, unidade u, modalidade m, apresentador apr"
                + " WHERE apr.unidade = u.id AND a.apresentador = apr.cpf AND u.id =" + codUnidade
                + " AND a.modalidade = m.id AND m.nome = 'Apresentacao cultural' AND a.evento='" + e.getId() + "'";
        if (centralizado == 1) {
            SQL = SQL + " AND a.selecionada = " + centralizado + "";
        }
        SQL = SQL + ";";
        ResultSet rs3 = select(SQL);

        try {
            while (rs3.next()) {
                relat.adicionarMateriaisNecessarios(rs3.getString("materiais"), rs3.getInt("id"), rs3.getString("nome"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(BancoDeDados.class.getName()).log(Level.SEVERE, null, ex);
        }
        // relatório por area de conhecimento
        SQL = "SELECT COUNT(DISTINCT a.id) AS q, ar.id AS mid, ar.nome AS mnome"
                + " FROM apresentacao a, unidade u, area ar, subarea sa, apresentador apr"
                + " WHERE apr.unidade = u.id AND a.apresentador = apr.cpf AND u.id =" + codUnidade
                + " AND a.subarea = sa.id AND sa.area = ar.id AND a.evento='" + e.getId() + "'";
        if (centralizado == 1) {
            SQL = SQL + " AND a.selecionada = " + centralizado + "";
        }
        SQL = SQL + " GROUP BY mid;";
        ResultSet rs4 = select(SQL);

        try {
            while (rs4.next()) {
                relat.adicionarQuantidadePorArea(rs4.getInt("q"), new area(rs4.getString("mnome"), rs4.getInt("mid")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(BancoDeDados.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void relatorioApresentacoesApresentador(relatorioApresentacoesPorApresentador raa, evento e) {
        ArrayList<apresentacao> retr = new ArrayList<>();
        String SQL = "SELECT a.id as apid, a.nome as apnome, m.nome as modal, m.id as modcod,"
                + " a.apresentador as apcpf, at.nome as atnome, a.apresentadorefetivo"
                + " FROM apresentacao a, evento e, modalidade m, apresentador at"
                + " WHERE e.issn = a.evento AND e.issn='" + e.getId() + "' AND a.modalidade = m.id AND a.apresentador = at.cpf";
        if (raa.getCentralizado() > 0) {
            SQL = SQL + " AND selecionada=1";
        }
        SQL = SQL + ";";
        ResultSet rs = select(SQL);
        apresentacao nova;
        try {
            while (rs.next()) {
                nova = new apresentacao();
                nova.setId(rs.getInt("apid"));
                nova.setApresentadorCpf(rs.getString("apcpf"));
                nova.setNometitulo(rs.getString("apnome"));
                nova.setModalidadeNome(rs.getString("modal"));
                nova.setModalidadeCod(rs.getInt("modcod"));
                nova.setApresentadorNome(rs.getString("atnome"));
                nova.setApresentadorEfetivoIDInicial(rs.getInt("apresentadorefetivo"));
                nova.descobrirCpfdoApresentadorEfetivoAPartirDoId();
                retr.add(nova);
            }
        } catch (SQLException ex) {
            Logger.getLogger(BancoDeDados.class.getName()).log(Level.SEVERE, null, ex);
        }

        for (apresentacao aprs : retr) {
            aprs.setAutores(recuperarAutoresApresentacao(aprs.getId()));
            raa.addApresentacao(aprs);
        }

    }

    public boolean setAutorComoAtivo(int ID) {

        String SQL = "UPDATE autor SET ativo=1 WHERE id =" + ID + ";";
        int x = updateOUinsert(SQL);

        if (x > 0) {
            System.out.println("Setou autor " + ID + " como ativo.");
            return true;
        } else {
            return false;
        }
    }

    public boolean setAutorComoInativo(int ID) {
        String SQL = "UPDATE autor SET ativo=0 WHERE id =" + ID + ";";
        int x = updateOUinsert(SQL);

        if (x > 0) {
            System.out.println("Setou autor " + ID + " como inativo.");
            return true;
        } else {
            return false;
        }

    }

    public boolean setApresentadorEfetivoApresentacao(int id, int codApresentador) {
        String s = "UPDATE apresentacao "
                + "SET apresentadorefetivo=" + codApresentador + " "
                + "WHERE id = " + id + ";";
        int x = updateOUinsert(s);

        if (x > 0) {
            System.out.println("Setou apresentador efetivo " + codApresentador + " em apr. ID " + id + "!");
            return true;
        } else {
            return false;
        }
    }

    ArrayList<autor> recuperarAutoresAtivosDoCentralizado(evento e) {
        ArrayList<autor> autores = new ArrayList<>();
        autor aux;
        String SQL = "SELECT DISTINCT a.nome, a.id, a.email, a.unidade, a.funcao,  a.cpf, a.nascimento, a.sexo, "
                + "u.nome AS nomeunidade, a.ativo as autativo, ap.id AS apid "
                + "FROM autor a, apresentacao ap, unidade u, evento e  "
                + "WHERE a.apresentacao = ap.id AND a.unidade = u.id AND a.ativo >0 AND ap.selecionada>0 "
                + "AND ap.evento = e.issn AND e.issn='" + e.getId() + "';";
        ResultSet rs = select(SQL);
        try {
            while (rs.next()) {
                aux = new autor();
                aux.setApresentacaoCod(rs.getInt("apid"));
                //aux.setBolsaid(rs.getInt("bolsa"));
                aux.setEmail(rs.getString("email"));
                //aux.setFuncaoCod(rs.getInt("funcao"));
                aux.setId(rs.getInt("id"));
                aux.setNome(rs.getString("nome"));
                aux.setUnidadecodigo(rs.getInt("unidade"));
                //aux.setBolsaNome(rs.getString("nomebolsa"));
                //aux.setFuncaoNome(rs.getString("nomefuncao"));
                aux.setUnidadeNome(rs.getString("nomeunidade"));
                aux.setCpf(rs.getString("cpf"));
                aux.setSexo(rs.getString("sexo"));
                aux.setNascimentoDate(rs.getString("nascimento"));
                aux.setAtivoInicial(rs.getInt("autativo"));
                autores.add(aux);
            }
        } catch (SQLException ex) {
            Logger.getLogger(BancoDeDados.class.getName()).log(Level.SEVERE, null, ex);
        }
        return autores;
    }

   public ArrayList<apresentador> recuperarApresentadoresEfetivosDoCentralizado(evento e) {
        ArrayList<apresentador> aps = new ArrayList<>();
        String SQL = "SELECT DISTINCT ap.cpf, ap.observacoes, ap.email, ap.telefone, ap.datadenascimento, ap.sexo, ap.necessidadeespecial, ap.nome AS apnome, ap.rg, ap.estadorg, ap.orgaoexpedidorrg, ap.estadodenaturalidade, "
                + "u.nome as unome, ap.tipopessoa, b.nome as bnome, b.id as bid"//, b.nome as nbolsa, t.nome as ntipop "
                + " FROM apresentador ap, apresentacao x, unidade u, evento e, bolsa b, tipopessoa t, convenio c"
                + " WHERE x.apresentadorefetivo = -1 AND ap.unidade = u.id AND c.bolsa = b.id AND c.apresentador = ap.cpf "
                + "AND x.selecionada>0 "//AND ap.bolsa = b.id AND ap.tipopessoa = t.id "
                + "AND x.evento = e.issn AND e.issn='" + e.getId() + "';";
        apresentador apr;
        ResultSet rs = select(SQL);
        
        
        try {
            while (rs.next()) {
                apr = new apresentador();
                apr.setUnidade(rs.getString("unome"));
                apr.setTipoPessoa(rs.getInt("tipopessoa"));
                apr.setBolsaCod(rs.getInt("bid"));
                apr.setBolsaNome(rs.getString("bnome"));
                //apr.setVinculo(rs.getString("ntipop"));
                // apr.setBolsaNome(rs.getString("nbolsa"));
                //  apr.setCodigoUnidade(rs.getInt("unidade"));   
                apr.setEmail(rs.getString("email"));
                apr.setTelefone(rs.getString("telefone"));
                apr.setCpf(rs.getString("cpf"));
                apr.setDatadenascimento(Arrumador.comando().DATEParaData(rs.getString("datadenascimento")));
                apr.setNome(rs.getString("apnome"));
                apr.setSexo(rs.getString("sexo"));
                apr.setNecessidadesespeciais(rs.getString("necessidadeespecial"));
                apr.setObservacoes(rs.getString("observacoes"));
                //ap.rg, ap.estadorg, ap.orgaoexpedidorrg
                apr.setRg(rs.getString("ap.rg"));
                apr.setOrgaoexpedidorrg(rs.getString("ap.estadorg"));
                apr.setEstadorg(rs.getString("ap.orgaoexpedidorrg"));
                apr.setEstadonaturalidade(rs.getString("ap.estadodenaturalidade"));
                
                aps.add(apr);

            }
        } catch (SQLException ex) {
            Logger.getLogger(BancoDeDados.class.getName()).log(Level.SEVERE, null, ex);
        }

        return aps;

    }

    public boolean tamanhoDoResumoOk(int c) {

        return (c >= minimoResumo && c <= maximoResumo);

    }

    public boolean versaoDifere(String versaoBD) {

        System.out.println("Comparando " + versaoBD + " com " + versao + "...");
        return !versaoBD.equals(versao);
    }

    public ArrayList<categoria> getCategorias() {
        ArrayList<categoria> cat = new ArrayList<>();
        String SQL = "SELECT * FROM categoria;";
        ResultSet r = select(SQL);
        categoria ax;

        try {
            while (r.next()) {

                ax = new categoria();
                ax.setId(r.getInt("id"));
                ax.setNome(r.getString("nome"));
                ax.setComResumo(r.getInt("comresumo") > 0);
                ax.setAreaTematica(r.getInt("areatematica") > 0);
                ax.setGrandeArea(r.getInt("grandearea") > 0);
                ax.setCultural(r.getInt("cultural") > 0);
                cat.add(ax);

            }
        } catch (SQLException ex) {
            Logger.getLogger(BancoDeDados.class.getName()).log(Level.SEVERE, null, ex);
        }

        ResultSet t = select("SELECT * FROM categoriamodalidade;");
        int categoria, modalidade;
        try {
            while (t.next()) {
                categoria = t.getInt("categoria");
                modalidade = t.getInt("modalidade");

                for (categoria c : cat) {
                    c.addModalidade(categoria, modalidade);
                }

            }
        } catch (SQLException ex) {
            Logger.getLogger(BancoDeDados.class.getName()).log(Level.SEVERE, null, ex);
        }

        return cat;
    }

    public ArrayList<avaliador> recuperarAvaliadoresDaUnidade(int unidade) {
        ArrayList<avaliador> avs = new ArrayList<>();
        String SQL = "SELECT DISTINCT a.*, u.nome as unid, "
                + " t.nome AS tipopessoanome "
                + "FROM avaliador a, tipopessoa t, unidade u, avalia av "
                + "WHERE t.id = a.tipopessoa AND u.id = a.unidade AND av.avaliador = a.cpf AND a.unidade = u.id";

        if (unidade > -1) {
            SQL = SQL + " AND u.id = " + unidade;
        }

        SQL = SQL + " ORDER BY a.nome;";
        // System.out.println(SQL);

        ResultSet rs = select(SQL);
        try {
            avaliador av;
            while (rs.next()) {
                av = new avaliador();

                av.setAcompanhante(rs.getInt("acompanhante") > 0);
                av.setAgencia(rs.getString("agencia"));
                av.setCpf(rs.getString("cpf"));
                av.setUnidadeNome(rs.getString("unid"));
                av.setSexo(rs.getString("sexo"));
                av.setNome(rs.getString("nome"));
                av.setEmail(rs.getString("email"));
                av.setTelefone(rs.getString("telefone"));
                av.setTipoPessoaCod(rs.getInt("tipopessoa"));
                av.setTipoPessoaNome(rs.getString("tipopessoanome"));
                av.setTitulacaoNome(rs.getString("titulacao"));
                av.setUnidadeCod(rs.getInt("unidade"));
                av.setFormacao(rs.getString("formacao"));
                av.setHorasCert(rs.getInt("horas"));
                //av.setUnidadeNome(rs.getString("unid"));
                avs.add(av);
                //System.out.println(av.getCpf()+"; ");
            }

        } catch (SQLException ex) {
            Logger.getLogger(BancoDeDados.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (unidade > -1) {
            for (avaliador a : avs) {
                a.setAreaNome(recuperarAreasQueAvalia(a).getAreaNome());
            }
        }

        return avs;
    }

    /**
     * @return the bloqueioEdicaoGeral
     */
    public boolean isBloqueioEdicaoGeralApresentacoes() {
        return bloqueioEdicaoGeral;
    }

    /**
     * @return the bloqueiAreaApresentador
     */
    public boolean isBloqueiAreaApresentador() {
        return bloqueiAreaApresentador;
    }

    public int atualizarStatusAcompanhanteAvaliador(String cpf, boolean acompanhante) {
        String SQL;
        if (acompanhante) {
            SQL = "UPDATE avaliador set acompanhante = 1 WHERE cpf = '" + cpf + "';";
        } else {
            SQL = "UPDATE avaliador set acompanhante = 0 WHERE cpf = '" + cpf + "';";
        }

        return updateOUinsert(SQL);
    }

    public int numeroAvaliacoesCadastradasAvaliador(String cpf, evento e) {
        int a = 0;
        ResultSet r = select("SELECT COUNT(a.apresentacao) AS c FROM avaliacao a, apresentacao apr, evento e "
                + "WHERE a.avaliador = '" + cpf + "' "
                + "AND a.apresentacao = apr.id AND apr.evento = e.issn AND e.issn='" + e.getId() + "';");
        try {
            while (r.next()) {
                a = r.getInt("c");
            }
        } catch (SQLException ex) {
            Logger.getLogger(BancoDeDados.class.getName()).log(Level.SEVERE, null, ex);
        }

        return a;
    }

    public void tirarPontosAutores() {
        ArrayList<String> nomes = new ArrayList<>();
        ArrayList<Integer> ids = new ArrayList<>();
        String aux;
        ResultSet e = select("SELECT nome, id FROM autor;");

        try {
            while (e.next()) {

                nomes.add(e.getString("nome"));
                ids.add(e.getInt("id"));

            }
            System.out.println(ids.size() + " autores!");

            for (int a = 0; a < ids.size(); a++) {

                if (nomes.get(a).substring((nomes.get(a).length() - 1), nomes.get(a).length()).equals(".")) {
                    aux = nomes.get(a).substring(0, nomes.get(a).length() - 1);
                    update("UPDATE autor set nome = '" + aux + "' WHERE id = " + ids.get(a) + ";");
                }

            }
            System.out.println("Processo finalizado! Retirou pontos de autores.");

        } catch (SQLException ex) {
            Logger.getLogger(BancoDeDados.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void tirarPontosApresentadores() {
        ArrayList<String> nomes = new ArrayList<>();
        ArrayList<String> ids = new ArrayList<>();
        String aux;
        ResultSet e = select("SELECT nome, cpf FROM apresentador;");

        try {
            while (e.next()) {

                nomes.add(e.getString("nome"));
                ids.add(e.getString("cpf"));

            }
            System.out.println(ids.size() + " apresentadores!");

            for (int a = 0; a < ids.size(); a++) {

                if (nomes.get(a).substring((nomes.get(a).length() - 1), nomes.get(a).length()).equals(".")) {
                    aux = nomes.get(a).substring(0, nomes.get(a).length() - 1);
                    update("UPDATE apresentador set nome = '" + aux + "' WHERE cpf = '" + ids.get(a) + "';");
                }

            }
            System.out.println("Processo finalizado! Retirou pontos de apresentadores.");

        } catch (SQLException ex) {
            Logger.getLogger(BancoDeDados.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
      public void contApresentadores() {
        ArrayList<String> nomes = new ArrayList<>();
        ArrayList<String> ids = new ArrayList<>();
        String aux;
        ResultSet e = select("SELECT nome, cpf FROM apresentador;");

        try {
            while (e.next()) {

                nomes.add(e.getString("nome"));
                ids.add(e.getString("cpf"));
                System.out.println(nomes.get(nomes.size()-1) + " " + ids.get(nomes.size()-1));

            }
            System.out.println("Existem  " + ids.size() + " apresentadores!");

        } catch (SQLException ex) {
            Logger.getLogger(BancoDeDados.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    

    public void tirarPontosAvaliadores() {
        ArrayList<String> nomes = new ArrayList<>();
        ArrayList<String> ids = new ArrayList<>();
        String aux;
        ResultSet e = select("SELECT nome, cpf FROM avaliador;");

        try {
            while (e.next()) {

                nomes.add(e.getString("nome"));
                ids.add(e.getString("cpf"));

            }
            System.out.println(ids.size() + " avaliadores!");

            for (int a = 0; a < ids.size(); a++) {

                if (nomes.get(a).substring((nomes.get(a).length() - 1), nomes.get(a).length()).equals(".")) {
                    aux = nomes.get(a).substring(0, nomes.get(a).length() - 1);
                    update("UPDATE avaliador set nome = '" + aux + "' WHERE cpf = '" + ids.get(a) + "';");
                }

            }
            System.out.println("Processo finalizado! Retirou pontos de avalidores.");

        } catch (SQLException ex) {
            Logger.getLogger(BancoDeDados.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void reprocessarTitulosApresentações(boolean pontoFinal) {
        ArrayList<String> nomes = new ArrayList<>();
        ArrayList<Integer> ids = new ArrayList<>();
        String aux;
        ResultSet e = select("SELECT nome, id FROM apresentacao;");

        try {
            while (e.next()) {

                nomes.add(e.getString("nome"));
                ids.add(e.getInt("id"));

            }
            System.out.println(ids.size() + " apresentações!");

            for (int a = 0; a < ids.size(); a++) {

                if (!nomes.get(a).substring((nomes.get(a).length() - 1), nomes.get(a).length()).equals(".")) //não termina com ponto
                {
                    aux = Arrumador.comando().paraMaisculaComecoPalavra(nomes.get(a), pontoFinal); //pode colocar ponto
                } else {
                    aux = Arrumador.comando().paraMaisculaComecoPalavra(nomes.get(a), false); //já termina com ponto, de qualquer forma
                }

                PreparedStatement st = con.prepareStatement("UPDATE apresentacao set nome = ? WHERE id = ?;");

                st.setString(1, aux);
                st.setInt(2, ids.get(a));

                st.execute();

            }
            System.out.println("Processo finalizado! Reprocessou nomes de apresentações.");

        } catch (SQLException ex) {
            Logger.getLogger(BancoDeDados.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void reprocessarApresentadores() {
        ArrayList<String> nomes = new ArrayList<>();
        ArrayList<String> cpf = new ArrayList<>();
        String aux;
        ResultSet e = select("SELECT nome, cpf FROM apresentador;");

        try {
            while (e.next()) {
                nomes.add(e.getString("nome"));
                cpf.add(e.getString("cpf"));
            }
            System.out.println(cpf.size() + " apresentadores!");

            for (int a = 0; a < cpf.size(); a++) {

                aux = Arrumador.comando().paraMaisculaComecoPalavra(nomes.get(a), false); //já termina com ponto, de qualquer forma

                PreparedStatement st = con.prepareStatement("UPDATE apresentador set nome = ? WHERE cpf = ?;");
                st.setString(1, aux);
                st.setString(2, cpf.get(a));
                st.execute();

            }
            System.out.println("Processo finalizado! Reprocessou nomes de apresentadores.");

        } catch (SQLException ex) {
            Logger.getLogger(BancoDeDados.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void reprocessarNomesAutoresEApresentadores() {
        reprocessarAutores();
        reprocessarApresentadores();
    }

    public void reprocessarAutores() {
        ArrayList<String> nomes = new ArrayList<>();
        ArrayList<Integer> ids = new ArrayList<>();
        String aux;
        ResultSet e = select("SELECT nome, id FROM autor;");

        try {
            while (e.next()) {
                nomes.add(e.getString("nome"));
                ids.add(e.getInt("id"));
            }
            System.out.println(ids.size() + " autores a processar!");

            for (int a = 0; a < ids.size(); a++) {

                aux = Arrumador.comando().paraMaisculaComecoPalavra(nomes.get(a), false); //já termina com ponto, de qualquer forma

                PreparedStatement st = con.prepareStatement("UPDATE autor set nome = ? WHERE id = ?;");
                st.setString(1, aux);
                st.setInt(2, ids.get(a));
                st.execute();

            }
            System.out.println("Processo finalizado! Reprocessou nomes de autores.");

        } catch (SQLException ex) {
            Logger.getLogger(BancoDeDados.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public boolean apresentouTrabalho(String cpf, String id) {

        String SQL = "SELECT count(id) FROM apresentacao WHERE apresentador = '" + cpf + "' AND evento = '" + id + "';";

        return (selectCount(SQL) > 0);

    }

    public void deletarApresentacaoCompletamente(int id) {

        String SQL = "DELETE FROM convenio WHERE apresentacao = " + id + ";";
        update(SQL);
        SQL = "DELETE FROM autor WHERE apresentacao = " + id + ";";
        update(SQL);
        SQL = "DELETE FROM avaliacao WHERE apresentacao = " + id + ";";
        update(SQL);
        SQL = "DELETE FROM apresentacao WHERE id = " + id + ";";

        if (update(SQL) > 0) {
            System.out.println("Apresentacao " + id + " foi aniquilada!");
        }

    }

    public boolean inserirOficinaSessaoMonitor(oficinaSessaoMonitor obj) {
        String SQL = "INSERT INTO oficinasessaopalestra ("
                + "tipo,nomeparticipantes,verbo,"
                + "trabalhooucampus, documento, evento) "
                + "VALUES(?,?,?,"
                + "?,?,?);";
        try {
            PreparedStatement stmt1 = con.prepareStatement(SQL);
            stmt1.setString(1, obj.getTipo());
            stmt1.setString(2, obj.getNomeParticipantes());
            stmt1.setString(3, obj.getVerbo());

            stmt1.setString(4, obj.getTrabalhoOuCampus());
            stmt1.setString(5, obj.getDocumento());
            stmt1.setString(6, obj.getEvento());

            stmt1.execute();

            return true;

        } catch (SQLException ex) {
            System.out.println("FALHA salvando " + obj.getNomeParticipantes());
            Logger.getLogger(BancoDeDados.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

    }

    public boolean atualizarOficinaSessaoMonitor(oficinaSessaoMonitor obj) {
        String SQL = "UPDATE oficinasessaopalestra "
                + "SET tipo = ?, nomeparticipantes = ?, verbo = ?,"
                + "trabalhooucampus = ?, documento = ? WHERE id = ?;";
        try {
            PreparedStatement stmt1 = con.prepareStatement(SQL);
            stmt1.setString(1, obj.getTipo());
            stmt1.setString(2, obj.getNomeParticipantes());
            stmt1.setString(3, obj.getVerbo());

            stmt1.setString(4, obj.getTrabalhoOuCampus());
            stmt1.setString(5, obj.getDocumento());
            stmt1.setInt(6, obj.getId());

            stmt1.execute();

            return true;

        } catch (SQLException ex) {
            System.out.println("FALHA atualizando " + obj.getId());
            Logger.getLogger(BancoDeDados.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

    }

    public ArrayList<oficinaSessaoMonitor> getArrayOficinaSessaoMonitor(String evento) {
        ArrayList<oficinaSessaoMonitor> ret = new ArrayList<>();

        ResultSet k = select("SELECT * FROM oficinasessaopalestra WHERE evento = '" + evento + "' ORDER by tipo, trabalhooucampus;");

        try {
            while (k.next()) {
                oficinaSessaoMonitor n = new oficinaSessaoMonitor();

                n.setTipo(k.getString("tipo"));
                n.setNomeParticipantes(k.getString("nomeparticipantes"));
                n.setVerbo(k.getString("verbo"));
                n.setTrabalhoOuCampus(k.getString("trabalhooucampus"));
                n.setEvento(evento);
                n.setDocumento(k.getString("documento"));
                n.setId(k.getInt("id"));

                ret.add(n);

            }
        } catch (SQLException ex) {
            Logger.getLogger(BancoDeDados.class.getName()).log(Level.SEVERE, null, ex);
        }

        return ret;
    }

    
    public int returnMax(String SQL) {
        int m = -1;

        ResultSet rst = select(SQL);

        try {
            while (rst.next()) {
                m = rst.getInt("max");
            }
        } catch (SQLException ex) {
            Logger.getLogger(BancoDeDados.class.getName()).log(Level.SEVERE, null, ex);
        }

        return m;
    }

    public boolean trabalhoOkParaAnaisCertificado(apresentacao ap, evento e) {
        if ( ap.getModalidadeCod() == 4 )
            return true;
        
        return selectCount("SELECT count(avaliador) FROM avaliacao "
                + "WHERE ausente < 1 AND apresentacao = " + ap.getId() + ";") > 0;
    }

    public ArrayList<avaliador> getAvaliadoresAusentes(evento event) {

        String SQL = "SELECT DISTINCT a.*, u.nome AS unid FROM avaliador a, unidade u, avaliacao avalia, apresentacao ap "
                + "WHERE u.id = a.unidade AND avalia.avaliador = a.cpf AND a.acompanhante=0 "
                + "AND avalia.apresentacao = ap.id AND ap.evento = '" + event.getId() + "' ORDER BY a.nome;";

        //avaliadores diferentes, que são da unidade u, não estão marcados como acompanhates/presentes
        //mas tem uma avaliação de apresentação do evento em questão
        ArrayList<avaliador> avs = new ArrayList<>();

        ResultSet rs = select(SQL);
        try {
            avaliador av;
            while (rs.next()) {
                av = new avaliador();

                av.setAcompanhante(false);
                av.setAgencia(rs.getString("agencia"));
                av.setCpf(rs.getString("cpf"));
                av.setUnidadeNome(rs.getString("unid"));
                av.setSexo(rs.getString("sexo"));
                av.setNome(rs.getString("nome"));
                av.setEmail(rs.getString("email"));
                av.setTelefone(rs.getString("telefone"));
                av.setTipoPessoaCod(rs.getInt("tipopessoa"));
                // av.setTipoPessoaNome(rs.getString("tipopessoanome"));
                av.setTitulacaoNome(rs.getString("titulacao"));
                av.setUnidadeCod(rs.getInt("unidade"));
                av.setFormacao(rs.getString("formacao"));
                av.setHorasCert(rs.getInt("horas"));
                av.setUnidadeNome(rs.getString("unid"));
                avs.add(av);
                //System.out.println(av.getCpf()+"; ");
            }

        } catch (SQLException ex) {
            Logger.getLogger(BancoDeDados.class.getName()).log(Level.SEVERE, null, ex);
        }

        return avs;
    }

    /**
     * @return the coordenadorCampusPodeExcluirApresentacao
     */
    public boolean isCoordenadorCampusPodeExcluirApresentacao() {
        return coordenadorCampusPodeExcluirApresentacao;
    }

    public String getEmailCoordenadorUnidade(int codUnid, boolean virgulaAntes) {
        String email = "";

        if (virgulaAntes) {
            email = ", ";
        }

        int cont = 0;

        String SQL = "SELECT av.email "
                + "FROM avaliador av, unidade u, coordenador c "
                + "WHERE u.id = " + codUnid + " AND c.unidade = u.id AND c.avaliador = av.cpf;";
        ResultSet tg = select(SQL);

        try {
            while (tg.next()) {
                if (cont == 0) {
                    email = email + tg.getString("av.email");
                } else {
                    email = email + ", " + tg.getString("av.email");
                }

                cont++;
            }
        } catch (SQLException ex) {
            Logger.getLogger(BancoDeDados.class.getName()).log(Level.SEVERE, null, ex);
        }

        return email;
    }

    public String getEmailCoordenadorApresentador(String cpf, boolean virgulaAntes) {
        String email = "";

        if (virgulaAntes) {
            email = ", ";
        }
        int cont = 0;

        String SQL = "SELECT av.email "
                + "FROM avaliador av, unidade u, coordenador c, apresentador apr "
                + "WHERE apr.cpf = '" + cpf + "' AND apr.unidade = u.id "
                + "AND c.unidade = u.id AND c.avaliador = av.cpf;";
        ResultSet tg = select(SQL);

        try {
            while (tg.next()) {
                if (cont == 0) {
                    email = email + tg.getString("av.email");
                } else {
                    email = email + ", " + tg.getString("av.email");
                }

                cont++;
            }
        } catch (SQLException ex) {
            Logger.getLogger(BancoDeDados.class.getName()).log(Level.SEVERE, null, ex);
        }

        return email;
    }

    public boolean unidadeDoApresentadorUsaCodigoParaCadastro(String cpf) {

        ResultSet asas = this.select("SELECT u.usacodigosparacadastro AS x FROM unidade u, apresentador a "
                + "WHERE a.unidade = u.id AND a.cpf = '" + cpf + "';");
        int x = 0;
        try {
            while (asas.next()) {

                x = asas.getInt("x");

            }
        } catch (SQLException ex) {
            Logger.getLogger(BancoDeDados.class.getName()).log(Level.SEVERE, null, ex);
        }

        return x > 0;
    }

    public boolean unidadeUsaCodigoParaCadastro(int u) {

        ResultSet asas = this.select("SELECT u.usacodigosparacadastro AS x FROM unidade u "
                + "WHERE u.id = " + u + ";");
        int x = 0;
        try {
            while (asas.next()) {

                x = asas.getInt("x");

            }
        } catch (SQLException ex) {
            Logger.getLogger(BancoDeDados.class.getName()).log(Level.SEVERE, null, ex);
        }

        return x > 0;
    }

    public boolean existeTicket(String cod, String tipo, int unid) {

        cod = cod.replace("'", ".");

        return this.selectCount("SELECT count(*) FROM ticket WHERE codigo = '" + cod + "' AND tipo = '" + tipo + "' AND unidade = " + unid + ";") > 0;
    }

    public boolean existeTicketTrabalho(String cod, String trabalho, String cpfApresentador) {

        cod = cod.replace("'", ".");

        return this.selectCount("SELECT count(t.codigo) FROM ticket t, unidade u, apresentador a "
                + "WHERE t.codigo = '" + cod + "' AND t.tipo = '" + trabalho + "' "
                + "AND t.unidade = u.id AND u.id = a.unidade AND a.cpf = '" + cpfApresentador + "';") > 0;
    }

    public ArrayList<ticket> getTickets(boolean t, ArrayList<coordenador> cords) {
        ArrayList<ticket> tcs = new ArrayList<>();
        String SQL;

        if (t) {
            SQL = "SELECT t.*, u.nome FROM ticket t, unidade u WHERE t.unidade = u.id ORDER BY t.tipo DESC, u.nome;";
        } else {
            SQL = "SELECT t.*, u.nome FROM ticket t, unidade u WHERE t.unidade = u.id AND u.id = " + cords.get(0).getUnidadeCodigo() + " ORDER BY t.tipo, u.nome;";
        }

        ResultSet asas = this.select(SQL);

        try {
            while (asas.next()) {

                tcs.add(new ticket(asas.getString("t.codigo"), asas.getString("t.tipo"), asas.getInt("t.unidade")));
                tcs.get(tcs.size() - 1).setUnidadeNome(asas.getString("u.nome"));

            }
        } catch (SQLException ex) {
            Logger.getLogger(BancoDeDados.class.getName()).log(Level.SEVERE, null, ex);
        }

        return tcs;
    }

    public boolean gerarTickets() {
        ArrayList<ticket> tcs = new ArrayList<>();

        ArrayList<unidade> unidades;
        unidades = this.getUnidades();
        int cont;

        for (unidade u : unidades) { //de unidade em unidade

            //verifica quantos tickets de trabalho já tem    
            cont = BancoDeDados.comando().selectCount("SELECT count(codigo) FROM ticket WHERE unidade = " + u.getId() + " AND tipo = 'trabalho';");

            //complementa o que falta 
            if (cont < u.getTotalTrabalhos()) {
                for (int a = 0; a < (u.getTotalTrabalhos() - cont); a++) {
                    tcs.add(new ticket("trabalho", u.getId()));
                }

                System.out.println("Criando " + (u.getTotalTrabalhos() - cont) + " tickets para unidade " + u.getNome());
            }

            //retira o que está sobrando
            if (cont > u.getTotalTrabalhos()) {
                excluirTickets(u.getId(), cont - u.getTotalTrabalhos());
            }

            //adiciona ticket para coordenador, se já não existir
            if (u.getId() != 22) {
                if (BancoDeDados.comando().selectCount("SELECT count(codigo) FROM ticket WHERE unidade = " + u.getId() + " AND tipo = 'coordenador';") < 1) {
                    tcs.add(new ticket("coordenador", u.getId()));
                }
            } else { //se for reitoria
                if (BancoDeDados.comando().selectCount("SELECT count(codigo) FROM ticket WHERE unidade = " + u.getId() + " AND tipo = 'coordenador';") < 6) {
                    tcs.add(new ticket("coordenador", u.getId()));
                    tcs.add(new ticket("coordenador", u.getId()));
                    tcs.add(new ticket("coordenador", u.getId()));
                    tcs.add(new ticket("coordenador", u.getId()));
                }
            }

        }

        for (ticket t : tcs) {
            t.salvarNoBD();
        }

        return true;
    }

    private void excluirTickets(int unidade, int quant) {

        ArrayList<String> cods = new ArrayList<>();

        ResultSet asas = this.select("SELECT codigo FROM ticket "
                + "WHERE unidade = " + unidade + " AND tipo = 'trabalho' limit " + quant + ";");
        int x = 0;
        try {
            while (asas.next()) {

                cods.add(asas.getString("codigo"));

            }

            asas.close();
        } catch (SQLException ex) {
            Logger.getLogger(BancoDeDados.class.getName()).log(Level.SEVERE, null, ex);
        }

        for (String s : cods) {
            BancoDeDados.comando().update("DELETE FROM ticket WHERE codigo = '" + s + "';");
        }

        System.out.println("Excluiu " + cods.size() + " códigos extras da unidade " + unidade);

    }

    /**
     * @return the usaCodigoDeCadastro
     */
    public boolean isUsaCodigoDeCadastro() {
        return usaCodigoDeCadastro;
    }

    /**
     * @param usaCodigoDeCadastro the usaCodigoDeCadastro to set
     */
    public void setUsaCodigoDeCadastro(boolean usaCodigoDeCadastro) {
        this.usaCodigoDeCadastro = usaCodigoDeCadastro;
    }

}
