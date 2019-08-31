package ClassesAuxiliares;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import objetosBase.apresentacao;
import objetosBase.apresentador;
import objetosBase.autor;

public final class Arrumador {
/////////////////////////////Definiçoes_do_Singleton////////////////////////////

    private ImageIcon icone = null;
    private apresentador user = new apresentador();

    private static Arrumador OBJETO;

    public static Arrumador comando() {
        if (OBJETO == null) {
            OBJETO = new Arrumador();
        }
        return OBJETO;
    }

////////////////////////////////////////////////////////////////////////////////
    /**
     * Para comparação de datas
     *
     * @param datax
     * @param datay
     * @return 1 if data x > data y 0 if data y > data x 2 if data x = data y -1
     * if data x || data y = invalidas
     *
     */
    public int dataXdepoisDataY(String datax, String datay) {

        try {
            int anox, anoy;
            anox = datax.charAt(6) * 1000 + datax.charAt(7) * 100 + datax.charAt(8) * 10 + datax.charAt(9);
            anoy = datay.charAt(6) * 1000 + datay.charAt(7) * 100 + datay.charAt(8) * 10 + datay.charAt(9);

            if (anox > anoy) {
                return 1;
            } else if (anoy > anox) {
                return 0;
            }

            int mesx = datax.charAt(3) * 10 + datax.charAt(4);
            int mesy = datay.charAt(3) * 10 + datay.charAt(4);

            if (mesx > mesy) {
                return 1;
            } else if (mesy > mesx) {
                return 0;
            }

            int diax = datax.charAt(0) * 10 + datax.charAt(1);
            int diay = datay.charAt(0) * 10 + datay.charAt(1);

            if (mesx > mesy) {
                return 1;
            } else if (mesy > mesx) {
                return 0;
            }

            if (diay < diax) {
                return 1;
            } else if (diay == diax) {
                return 2;
            }
            return 0;

        } catch (Exception e) {

            System.out.println("Formato inválido de data ");
            return -1;
        }

    }

    public ArrayList<String> getNiveisDeCurso() {
        ArrayList<String> x = new ArrayList<>();
        x.add("Nível Médio");
        x.add("Gradução");
        x.add("Especialização");
        x.add("Mestrado");
        x.add("Doutorado");
        
        return x;
       }
    
    public void salvarTXT(String localNome, String texto) {

        FileWriter arquivo;

        try {
            arquivo = new FileWriter(new File(localNome + ".txt"));
            arquivo.write(texto);
            arquivo.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void criaDiretorio(String novoDiretorio) {
        String nomeDiretorio = null;
        // String separador = java.io.File.separator;   
        try {
            nomeDiretorio = novoDiretorio;
            if (!new File(nomeDiretorio).exists()) { // Verifica se o diretório existe.   
                (new File(nomeDiretorio)).mkdir();   // Cria o diretório   
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Err", "Erro ao criar o diretório" + ex.toString(), JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * @param data formato brasileiro
     * @return data formato americano
     */
    public String dataParaDATE(String data) {
        String DATE = "";
        char a[];
        a = new char[11];

        try {
            a[1] = data.charAt(6);
            a[2] = data.charAt(7);
            a[3] = data.charAt(8);
            a[4] = data.charAt(9);
            a[5] = '-';
            a[6] = data.charAt(3);
            a[7] = data.charAt(4);
            a[8] = '-';
            a[9] = data.charAt(0);
            a[10] = data.charAt(1);

            for (int aux = 1; aux < 11; aux++) {
                DATE = DATE + a[aux];
            }

            return DATE;
        } catch (Exception e) {
            return "0001-01-01";
        }
    }

    public ImageIcon createImageIcon(String path,
            String description) {
        java.net.URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL, description);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    public ImageIcon getIcone() {

        if (icone == null) {
            icone = Arrumador.comando().createImageIcon("icone.jpg", "a pretty but meaningless splat");
        }

        return icone;

    }

    public ArrayList<apresentacao> retornaApenasCulturaisSelecionadas(ArrayList<apresentacao> in) {
        ArrayList<apresentacao> out = new ArrayList<>();

        for (apresentacao a : in) {
            if (a.getSelecionado() == 1 && a.getModalidadeCod() == 4) {
                out.add(a);
            }
        }

        return out;
    }

    /**
     * Mudança de formato de datas
     *
     * @param DATE formato americano
     * @return data - formato brasileiro
     *
     */
    public String DATEParaData(String DATE) {
        String data = "";
        char a[];
        a = new char[11];
        //date = aaaa-mm-dd
        try {
            a[1] = DATE.charAt(8);
            a[2] = DATE.charAt(9);
            a[3] = '/';
            a[4] = DATE.charAt(5);
            a[5] = DATE.charAt(6);
            a[6] = '/';
            a[7] = DATE.charAt(0);
            a[8] = DATE.charAt(1);
            a[9] = DATE.charAt(2);
            a[10] = DATE.charAt(3);

            for (int aux = 1; aux < 11; aux++) {
                data = data + a[aux];
            }

            return data;
        } catch (Exception e) {
            return "01/01/0001";
        }

    }

    /**
     * Função para que comandos SQL possam ser salvos dentro do BD
     *
     * @param texto sql
     * @return texto sem virgulas, para poder
     */
    public String trocarVirgulas(String texto) {
        return texto.replace(",", ".");
    }

    /**
     *
     * @param valor double
     * @return string do valor com duas casas decimais
     */
    public String doubleParaString(double valor) {
        //Formata a string para mostrar apenas 2 casas decimais
        return String.format("%.2f", valor);
    }

    /**
     *
     * @param valor bigdecimal
     * @return string
     */
    public String doubleParaString(BigDecimal valor) {
        //Formata a string para mostrar apenas 2 casas decimais
        return String.format("%.2f", valor);
    }

    /**
     * Double para Bigdecimal, para salvar em BD
     *
     *
     *
     */
    public BigDecimal doubleParaBigDecimal(double valor) {
        //Formata o big decimal para ter apenas 2 casas decimais
        //É necessário pois evita erros de arredondamento
        return BigDecimal.valueOf(valor).setScale(2, 0);
    }

    /**
     * Pegar valor em horas de um timestamp
     *
     * @param horario
     * @return horas
     */
    public int pegarHorasDeStringTimeStamp(String horario) {
        //supomos que o formato da hora é 11:22:22
        String temp = new String(horario.substring(11, 13));
        return Integer.parseInt(temp);
    }

    /**
     * Pegar valor em horas de uma string
     *
     * @param horario
     * @return horas
     */
    public int pegarHorasDeStringTime(String horario) {
        //supomos que o formato da hora é 11:22:22
        String temp = new String(horario.substring(0, 2));
        return Integer.parseInt(temp);
    }

    /**
     *
     * @param string
     * @return valorBigdecimal
     */
    public BigDecimal stringParaBigDecimal(String valor) {
        return doubleParaBigDecimal(Double.parseDouble(trocarVirgulas(valor)));
    }

    // import java.util.regex.Matcher;
    //  import java.util.regex.Pattern;
    public boolean ehUmEmail(String hex) {

        Pattern pattern;
        Matcher matcher;

        String EMAIL_PATTERN
                = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        pattern = Pattern.compile(EMAIL_PATTERN);

        matcher = pattern.matcher(hex);
        return matcher.matches();

    }

    public boolean ehUmTelefone(String t) {
        return t.length() > 9;
    }

    /**
     *
     * @return dia atual
     */
    public java.sql.Date retornarDiaAtual() {
        java.util.Date dataT = new java.util.Date();
        java.sql.Date data = new java.sql.Date(dataT.getTime());
        return data;
    }

    /**
     * Verifica se uma string CPF é válida
     *
     * @param cpf
     * @return validade do cpf
     */
    public boolean validarCPF(String cpf) {
        cpf = cpf.replace(".", "");
        cpf = cpf.replace("-","");
        if (cpf.equals("00000000000")) {
            return false;
        }

        int dig[];
        dig = new int[11];
        //A String do cpf sempre vem assim: xxx.xxx.xxx-xx
        //por isso as posicoes estranhas do charAt
        try {
            dig[0] = Integer.parseInt(String.valueOf(cpf.charAt(0)));
            dig[1] = Integer.parseInt(String.valueOf(cpf.charAt(1)));
            dig[2] = Integer.parseInt(String.valueOf(cpf.charAt(2)));

            dig[3] = Integer.parseInt(String.valueOf(cpf.charAt(3)));
            dig[4] = Integer.parseInt(String.valueOf(cpf.charAt(4)));
            dig[5] = Integer.parseInt(String.valueOf(cpf.charAt(5)));

            dig[6] = Integer.parseInt(String.valueOf(cpf.charAt(6)));
            dig[7] = Integer.parseInt(String.valueOf(cpf.charAt(7)));
            dig[8] = Integer.parseInt(String.valueOf(cpf.charAt(8)));

            //primeiro digito verificador
            dig[9] = Integer.parseInt(String.valueOf(cpf.charAt(9)));
            //segundo digito verificador
            dig[10] = Integer.parseInt(String.valueOf(cpf.charAt(10)));

            //fazendo a verificacao do primeiro digito verificador
            int ver = 0;
            for (int cont = 0; cont < 9; cont++) {
                ver = ver + (dig[cont] * (10 - cont));
            }
            ver = ver % 11;
            if (ver < 2) {
                ver = 0;
            } else {
                ver = 11 - ver;
            }
            if (ver != dig[9]) {
                return false;
            }

            ver = 0;
            for (int cont = 0; cont < 10; cont++) {
                ver = ver + (dig[cont] * (11 - cont));
            }

            ver = ver % 11;
            if (ver < 2) {
                ver = 0;
            } else {
                ver = 11 - ver;
            }
            if (ver != dig[10]) {
                return false;
            }

        } catch (Exception e) {
            return false;
        }

        return true;
    }

    /**
     *
     * @return data de hoje
     */
    public java.sql.Timestamp retornarDiaAtualTimestamp() {
        java.util.Date dataT = new java.util.Date();
        java.sql.Timestamp data = new java.sql.Timestamp(dataT.getTime());
        return data;
    }

    /**
     *
     * @param tempo
     * @return
     */
    public String timestampParaString(java.sql.Timestamp tempo) {
        SimpleDateFormat sdf = new SimpleDateFormat("kk:mm 'de' dd/MM/yyyy");
        return sdf.format(tempo);
    }

    /**
     * Which Year is it?
     *
     * @return ano atual
     */
    public int getAnoAtual() {
        int ano = Calendar.getInstance().getWeekYear();
        return ano;
    }

    public void recuperarDadosUser(String cpf) {
        user = BancoDeDados.comando().getApresentador(cpf);
        System.out.println("Recuperou dados do usuário " + user.getNome());
    }

    public apresentador getUserApresentador() {
        return user;
    }

    private String maiuscula(String palavra, boolean primeira) {

        //palavra nao existe
        if (palavra.length() < 1) {
            return "";
        }

        //retirar espaços duplos ou triplos
        palavra = palavra.replace("  ", " ");
        palavra = palavra.replace("   ", " ");

        if (palavra.substring(0, 1).equals(" ") && palavra.length() > 1) {
            palavra = palavra.substring(1);
        }

        //palavra entre parenteses
        if (palavra.substring(0, 1).equals("(") && palavra.substring(palavra.length() - 1, palavra.length()).equals(")")) {
            return palavra.toUpperCase();
        }

        //palavra começa com parenteses ou aspas
        if ((palavra.substring(0, 1).equals("(") || palavra.substring(0, 1).equals("\"") || palavra.substring(0, 1).equals("“") || palavra.substring(0, 1).equals("'")) && palavra.length() > 3) {
            return palavra.substring(0, 2).toUpperCase() + palavra.substring(2).toLowerCase();
        }

        // palavra na lista de siglas
        if (palavra.toUpperCase().equals("IFPR") || palavra.toUpperCase().equals("IFPR:") || palavra.toUpperCase().equals("IFPR,") || palavra.toUpperCase().equals("IFPR.") || palavra.toUpperCase().equals("PR") || palavra.toUpperCase().equals("PIBIS") || palavra.toUpperCase().equals("PIBID")
                || palavra.toUpperCase().equals("IF") || palavra.toUpperCase().equals("MST") || palavra.toUpperCase().equals("MST:") || palavra.toUpperCase().equals("IF:") || palavra.toUpperCase().equals("HQ") || palavra.toUpperCase().equals("HQ:")
                || palavra.toUpperCase().equals("CIF") || palavra.toUpperCase().equals("3D") || palavra.toUpperCase().equals("COHPLI")
                || palavra.toUpperCase().equals("I") || palavra.toUpperCase().equals("LED") || palavra.toUpperCase().equals("II") || palavra.toUpperCase().equals("III") || palavra.toUpperCase().equals("IV")
                || palavra.toUpperCase().equals("V") || palavra.toUpperCase().equals("VI") || palavra.toUpperCase().equals("VII") || palavra.toUpperCase().equals("VIII") || palavra.toUpperCase().equals("IX") || palavra.toUpperCase().equals("X") || palavra.toUpperCase().equals("XI") || palavra.toUpperCase().equals("XII") || palavra.toUpperCase().equals("XIII") || palavra.toUpperCase().equals("XIV") || palavra.toUpperCase().equals("XV") || palavra.toUpperCase().equals("XVI") || palavra.toUpperCase().equals("XVII") || palavra.toUpperCase().equals("XVIII") || palavra.toUpperCase().equals("XIX") || palavra.toUpperCase().equals("XX") || palavra.toUpperCase().equals("XXI") || palavra.toUpperCase().equals("XXII")
                || palavra.toUpperCase().equals("(PHF):") || palavra.toUpperCase().equals("PID") || palavra.toUpperCase().equals("UV") || palavra.toUpperCase().equals("FEPIAC") || palavra.toUpperCase().equals("CTC") || palavra.toUpperCase().equals("SAPE") || palavra.toUpperCase().equals("FMEA") || palavra.toUpperCase().equals("FMEA:") || palavra.toUpperCase().equals("(PR)") || palavra.toUpperCase().equals("PR.") || palavra.toUpperCase().equals("-PR") || palavra.toUpperCase().equals("/PR") || palavra.toUpperCase().equals("-SC") || palavra.toUpperCase().equals("/SC") || palavra.toUpperCase().equals("-SP") || palavra.toUpperCase().equals("/SP") || palavra.toUpperCase().equals("SP")) {
            return palavra.toUpperCase();
        }

        //siglas diferenciadas
        if (palavra.toUpperCase().equals("PARANAGUÁ/PR") || palavra.toUpperCase().equals("PARANAGUÁ-PR") || palavra.toUpperCase().equals("PARANAGUA-PR") || palavra.toUpperCase().equals("PARANAGUA/PR")) {
            return "Paranaguá/PR";
        } else if (palavra.toUpperCase().equals("CURITIBA/PR") || palavra.toUpperCase().equals("CURITIBA-PR")) {
            return "Curitiba/PR";
        } else if (palavra.toUpperCase().equals("UMUARAMA/PR") || palavra.toUpperCase().equals("UMUARAMA-PR")) {
            return "Umuarama/PR";
        } else if (palavra.toUpperCase().equals("VITÓRIA/PR") || palavra.toUpperCase().equals("VITÓRIA-PR") || palavra.toUpperCase().equals("VITORIA-PR") || palavra.toUpperCase().equals("VITORIA/PR")) {
            return "Vitória/PR";
        } else if (palavra.toUpperCase().equals("UNIÃO/SC") || palavra.toUpperCase().equals("UNIÃO-SC") || palavra.toUpperCase().equals("UNIAO-SC") || palavra.toUpperCase().equals("UNIAO/SC")) {
            return "União/SC";
        } else if (palavra.toUpperCase().equals("JAGUARIAÍVA-PR") || palavra.toUpperCase().equals("JAGUARIAÍVA/PR") || palavra.toUpperCase().equals("JAGUARIAIVA-PR")) {
            return "Jaguariaíva/PR";
        } else if (palavra.toUpperCase().equals("LARGO-PR") || palavra.toUpperCase().equals("LARGO/PR")) {
            return "Largo/PR";
        } else if (palavra.toUpperCase().equals("COLOMBO-PR") || palavra.toUpperCase().equals("COLOMBO/PR") || palavra.toUpperCase().equals("COLOMBO/PARANÁ") || palavra.toUpperCase().equals("COLOMBO-PARANÁ")) {
            return "Colombo/PR";
        } else if (palavra.toUpperCase().equals("VIVIDA-PR") || palavra.toUpperCase().equals("VIVIDA/PR")) {
            return "Vivida/PR";
        } else if (palavra.toUpperCase().equals("IVAIPORÃ-PR") || palavra.toUpperCase().equals("IVAIPORÃ/PR") || palavra.toUpperCase().equals("IVAIPORA/PR") || palavra.toUpperCase().equals("IVAIPORA-PR")) {
            return "Ivaiporã/PR";
        } else if (palavra.toUpperCase().equals("PALMAS-PR") || palavra.toUpperCase().equals("PALMAS/PR")) {
            return "Palmas/PR";
        } else if (palavra.toUpperCase().equals("CASCAVEL-PR") || palavra.toUpperCase().equals("CASCAVEL/PR")) {
            return "Cascavel/PR";
        } else if (palavra.toUpperCase().equals("CASCAVEL-PR:") || palavra.toUpperCase().equals("CASCAVEL/PR:")) {
            return "Cascavel/PR:";
        } else if (palavra.toUpperCase().equals("IRATI-PR:") || palavra.toUpperCase().equals("IRATI/PR:")) {
            return "Irati/PR:";
        } else if (palavra.toUpperCase().equals("PITANGA-PR:") || palavra.toUpperCase().equals("PITANGA/PR:")) {
            return "Pitanga/PR:";
        } else if (palavra.toUpperCase().equals("CAPANEMA-PR:") || palavra.toUpperCase().equals("CAPANEMA/PR:")) {
            return "Capanema/PR:";
        } else if (palavra.toUpperCase().equals("IRATI-PR:") || palavra.toUpperCase().equals("IRATI/PR:")) {
            return "Irati/PR:";
        }

        if (palavra.length() > 2 || primeira || (palavra.length() == 2 && palavra.substring(1).equals("."))) {
            if (palavra.substring(0, 1).equals("\"")) //Com aspas no começo da palavra. Primeira letra maiuscula
            {
                return palavra.substring(0, 2).toUpperCase() + palavra.substring(2).toLowerCase();
            } else {
                return palavra.substring(0, 1).toUpperCase() + palavra.substring(1).toLowerCase(); //de qualquer forma, grande o bastante, ou primeira palavra. Fica Assim
            }
        } else {
            return palavra.toLowerCase(); //palavra muito curta e não se encaixa em nada
        }
    }

    public String paraMaisculaComecoPalavra(String s, boolean pontoNoFinal) {

        if (s.substring(s.length() - 1).equals(".") && !pontoNoFinal) {
            s = s.substring(0, s.length() - 1);
        }

        String[] words = s.trim().split(" ");
        String t = "";

        for (int a = 0; a < words.length; a++) {
            if (a == 0) {
                t = maiuscula(words[a], true);
            } else {
                t = t + " " + maiuscula(words[a], false);
            }
        }

        if (!t.substring(t.length() - 1, t.length()).equals(".") && !t.substring(t.length() - 1, t.length()).equals(";") && pontoNoFinal) {
            t = t + ".";
        } else if (t.substring(t.length() - 1, t.length()).equals(";")) //troca ponto e virgula por ponto
        {
            t = t.substring(0, t.length() - 1) + ".";
        }

        return t;
    }

    public String relatorioRapidoTrabalho(int id) {
        String r = "-=-=-=-=-=-=-=\n";

        apresentacao a = BancoDeDados.comando().recuperarApresentacao(id);

        String SQL = "SELECT id from apresentacao where apresentador = '" + a.getApresentadorCpf() + "';";

        ResultSet rs = BancoDeDados.comando().select(SQL);
        SQL = "";
        try {
            while (rs.next()) {
                SQL = SQL + rs.getInt("id") + ", ";
            }
        } catch (SQLException ex) {
            Logger.getLogger(Arrumador.class.getName()).log(Level.SEVERE, null, ex);
        }

        r = r + "ID " + id + " - " + a.getNometitulo() + "\n"
                + "Apresentador: " + a.getApresentadorNome() + " (" + a.getApresentadorCpf() + ", " + a.getBolsaApresentadorNome() + "; Apresenta " + SQL + ")\n Autores: ";

        for (autor at : a.getAutores()) {
            r = r + "\n" + at.getNome() + " ( " + at.getCpf() + "; " + at.getBolsaNome() + " )";
        }

        return r + "\n=-=-=-=-=-=-=-=-=-=";
    }

    public void mudarSenhaAvaliador(String avaliador, String senha) {

        String SQL = "UPDATE avaliador set senha = md5('" + senha + "') WHERE cpf = '" + avaliador + "';";
        BancoDeDados.comando().update(SQL);
        System.out.println("Atualizou senha de " + avaliador + " para " + senha);
    }

    public void colocarAcentosBD() {
    //unidades, categorias, modalidades (nao confundir um com outro), areas

        //areas
        BancoDeDados.comando().update("UPDATE area set nome = 'Ciências Exatas e da Terra' WHERE id = 1;");
        BancoDeDados.comando().update("UPDATE area set nome = 'Ciências Sociais Aplicadas' WHERE id = 2;");
        BancoDeDados.comando().update("UPDATE area set nome = 'Engenharias' WHERE id = 4;");
        BancoDeDados.comando().update("UPDATE area set nome = 'Linguística, Letras e Artes' WHERE id = 5;");
        BancoDeDados.comando().update("UPDATE area set nome = 'Ciências da Saúde' WHERE id = 6;");
        BancoDeDados.comando().update("UPDATE area set nome = 'Ciências Agrárias' WHERE id = 7;");
        BancoDeDados.comando().update("UPDATE area set nome = 'Ciências Humanas' WHERE id = 8;");
        BancoDeDados.comando().update("UPDATE area set nome = 'Ciências Biológicas' WHERE id = 9;");
        BancoDeDados.comando().update("UPDATE area set nome = 'Comunicação' WHERE id = 11;");
        BancoDeDados.comando().update("UPDATE area set nome = 'Cultura' WHERE id = 12;");
        BancoDeDados.comando().update("UPDATE area set nome = 'Direitos Humanos e Justiça' WHERE id = 13;");
        BancoDeDados.comando().update("UPDATE area set nome = 'Educação' WHERE id = 14;");
        BancoDeDados.comando().update("UPDATE area set nome = 'Meio Ambiente' WHERE id = 15;");
        BancoDeDados.comando().update("UPDATE area set nome = 'Saúde' WHERE id = 16;");
        BancoDeDados.comando().update("UPDATE area set nome = 'Tecnologia e Produção' WHERE id = 17;");
        BancoDeDados.comando().update("UPDATE area set nome = 'Trabalho' WHERE id = 18;");
        BancoDeDados.comando().update("UPDATE area set nome = 'Audiovisual' WHERE id = 21;");
        BancoDeDados.comando().update("UPDATE area set nome = 'Artes Visuais' WHERE id = 22;");
        BancoDeDados.comando().update("UPDATE area set nome = 'Literatura' WHERE id = 23;");
        BancoDeDados.comando().update("UPDATE area set nome = 'Música' WHERE id = 24;");
        BancoDeDados.comando().update("UPDATE area set nome = 'Teatro' WHERE id = 25;");
        BancoDeDados.comando().update("UPDATE area set nome = 'Dança' WHERE id = 26;");

        //unidades
        BancoDeDados.comando().update("UPDATE unidade SET nome='Assis Chateaubriand' WHERE id=1;");
        BancoDeDados.comando().update("UPDATE unidade SET nome='Campo Largo' WHERE id=2;");
        BancoDeDados.comando().update("UPDATE unidade SET nome='Cascavel' WHERE id=5;");
        BancoDeDados.comando().update("UPDATE unidade SET nome='Colombo' WHERE id=6;");
        BancoDeDados.comando().update("UPDATE unidade SET nome='Coronel Vivida' WHERE id=7;");
        BancoDeDados.comando().update("UPDATE unidade SET nome='Curitiba' WHERE id=8;");
        BancoDeDados.comando().update("UPDATE unidade SET nome='Foz do Iguaçu' WHERE id=9;");
        BancoDeDados.comando().update("UPDATE unidade SET nome='Irati' WHERE id=10;");
        BancoDeDados.comando().update("UPDATE unidade SET nome='Ivaiporã' WHERE id=11;");
        BancoDeDados.comando().update("UPDATE unidade SET nome='Jacarezinho' WHERE id=12;");
        BancoDeDados.comando().update("UPDATE unidade SET nome='Londrina' WHERE id=13;");
        BancoDeDados.comando().update("UPDATE unidade SET nome='Palmas' WHERE id=14;");
        BancoDeDados.comando().update("UPDATE unidade SET nome='Paranaguá' WHERE id=15;");
        BancoDeDados.comando().update("UPDATE unidade SET nome='Paranavaí' WHERE id=16;");
        BancoDeDados.comando().update("UPDATE unidade SET nome='Pinhais' WHERE id=17;");
        BancoDeDados.comando().update("UPDATE unidade SET nome='Pitanga' WHERE id=18;");
        BancoDeDados.comando().update("UPDATE unidade SET nome='Telêmaco Borba' WHERE id=19;");
        BancoDeDados.comando().update("UPDATE unidade SET nome='Umuarama' WHERE id=20;");
        BancoDeDados.comando().update("UPDATE unidade SET nome='União da Vitória' WHERE id=21;");
        BancoDeDados.comando().update("UPDATE unidade SET nome='Reitoria' WHERE id=22;");
        BancoDeDados.comando().update("UPDATE unidade SET nome='Astorga' WHERE id=23;");
        BancoDeDados.comando().update("UPDATE unidade SET nome='Barracão' WHERE id=24;");
        BancoDeDados.comando().update("UPDATE unidade SET nome='Capanema' WHERE id=25;");
        BancoDeDados.comando().update("UPDATE unidade SET nome='Goioerê' WHERE id=26;");
        BancoDeDados.comando().update("UPDATE unidade SET nome='Jaguariaiva' WHERE id=27;");

        //categorias
        BancoDeDados.comando().update("UPDATE categoria SET nome= 'Pesquisa/Inovação' WHERE id = 0;");
        BancoDeDados.comando().update("UPDATE categoria SET nome= 'Extensão' WHERE id = 1;");
        BancoDeDados.comando().update("UPDATE categoria SET nome= 'Ensino' WHERE id = 2;");
        BancoDeDados.comando().update("UPDATE categoria SET nome= 'Robótica' WHERE id = 5;");
        BancoDeDados.comando().update("UPDATE categoria SET nome= 'IFTECH' WHERE id = 4;");
        BancoDeDados.comando().update("UPDATE categoria SET nome= 'Apresentação Cultural' WHERE id = 6;");

        //modalidade
        BancoDeDados.comando().update("UPDATE modalidade SET nome= 'Pôster e Comunicação Oral' WHERE id = 2;");
        BancoDeDados.comando().update("UPDATE modalidade SET nome= 'Pôster' WHERE id = 1;");
        BancoDeDados.comando().update("UPDATE modalidade SET nome= 'Mostra IFTECH' WHERE id = 3;");
        BancoDeDados.comando().update("UPDATE modalidade SET nome= 'Mostra de Robótica' WHERE id = 5;");
        BancoDeDados.comando().update("UPDATE modalidade SET nome= 'Apresentação Cultural' WHERE id = 4;");

    }

}
