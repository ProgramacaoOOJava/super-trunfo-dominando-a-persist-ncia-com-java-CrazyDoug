//Feito por Douglas Alves Costa
//Nivel Novato

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * Sistema Super Trunfo usando JDBC puro.
 *
 * Funcionalidades:
 * - Cadastro de cartas;
 * - Consulta de cartas;
 * - Atualização de cartas;
 * - Exclusão de cartas;
 * - Busca por matrícula;
 * - Sistema de batalha;
 * - Persistência com Apache Derby.
 */
public class SuperTrunfoJDBC {

    private static final String URL =
            "jdbc:derby:escola;create=true";

    private static final String USUARIO = "";
    private static final String SENHA = "";

    private static final Scanner scanner =
            new Scanner(System.in);

    private static final Random random =
            new Random();

    /**
     * Obtém uma conexão com o banco Apache Derby.
     */
    private static Connection getConnection()
            throws SQLException {

        return DriverManager.getConnection(
                URL,
                USUARIO,
                SENHA
        );
    }

    /**
     * Cria a tabela aluno caso ela ainda não exista.
     */
    public static void criarTabela() {
        String sql = """
                CREATE TABLE aluno (
                    matricula VARCHAR(20) PRIMARY KEY,
                    nome VARCHAR(100) NOT NULL,
                    entrada INTEGER NOT NULL
                )
                """;

        try (
                Connection conn = getConnection();
                Statement stmt = conn.createStatement()
        ) {
            stmt.executeUpdate(sql);

            System.out.println(
                    "✅ Tabela aluno criada com sucesso!"
            );

        } catch (SQLException erro) {
            /*
             * X0Y32 é o código utilizado pelo Derby
             * quando a tabela já existe.
             */
            if ("X0Y32".equals(erro.getSQLState())) {
                System.out.println(
                        "ℹ️ A tabela aluno já existe."
                );
            } else {
                System.err.println(
                        "❌ Erro ao criar tabela: "
                                + erro.getMessage()
                );
            }
        }
    }

    /**
     * Insere uma nova carta usando PreparedStatement.
     */
    public static boolean inserirAluno(Aluno aluno) {
        String sql = """
                INSERT INTO aluno
                (matricula, nome, entrada)
                VALUES (?, ?, ?)
                """;

        try (
                Connection conn = getConnection();
                PreparedStatement ps =
                        conn.prepareStatement(sql)
        ) {
            ps.setString(1, aluno.getMatricula());
            ps.setString(2, aluno.getNome());
            ps.setInt(3, aluno.getEntrada());

            int linhasAfetadas = ps.executeUpdate();

            if (linhasAfetadas > 0) {
                System.out.println(
                        "✅ Carta inserida: "
                                + aluno.getNome()
                );

                return true;
            }

        } catch (SQLException erro) {
            System.err.println(
                    "❌ Erro ao inserir aluno: "
                            + erro.getMessage()
            );
        }

        return false;
    }

    /**
     * Consulta todos os alunos usando Statement e ResultSet.
     */
    public static List<Aluno> consultarTodosAlunos() {
        List<Aluno> alunos = new ArrayList<>();

        String sql = """
                SELECT matricula, nome, entrada
                FROM aluno
                ORDER BY nome
                """;

        try (
                Connection conn = getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)
        ) {
            while (rs.next()) {
                Aluno aluno = new Aluno();

                aluno.setMatricula(
                        rs.getString("matricula")
                );

                aluno.setNome(
                        rs.getString("nome")
                );

                aluno.setEntrada(
                        rs.getInt("entrada")
                );

                alunos.add(aluno);
            }

        } catch (SQLException erro) {
            System.err.println(
                    "❌ Erro ao consultar alunos: "
                            + erro.getMessage()
            );
        }

        return alunos;
    }

    /**
     * Busca uma carta por matrícula.
     */
    public static Aluno buscarAluno(String matricula) {
        String sql = """
                SELECT matricula, nome, entrada
                FROM aluno
                WHERE matricula = ?
                """;

        try (
                Connection conn = getConnection();
                PreparedStatement ps =
                        conn.prepareStatement(sql)
        ) {
            ps.setString(1, matricula);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Aluno aluno = new Aluno();

                    aluno.setMatricula(
                            rs.getString("matricula")
                    );

                    aluno.setNome(
                            rs.getString("nome")
                    );

                    aluno.setEntrada(
                            rs.getInt("entrada")
                    );

                    return aluno;
                }
            }

        } catch (SQLException erro) {
            System.err.println(
                    "❌ Erro ao buscar aluno: "
                            + erro.getMessage()
            );
        }

        return null;
    }

    /**
     * Atualiza o nome e o ano de entrada de uma carta.
     */
    public static boolean atualizarAluno(Aluno aluno) {
        String sql = """
                UPDATE aluno
                SET nome = ?, entrada = ?
                WHERE matricula = ?
                """;

        try (
                Connection conn = getConnection();
                PreparedStatement ps =
                        conn.prepareStatement(sql)
        ) {
            ps.setString(1, aluno.getNome());
            ps.setInt(2, aluno.getEntrada());
            ps.setString(3, aluno.getMatricula());

            int linhasAfetadas = ps.executeUpdate();

            if (linhasAfetadas > 0) {
                System.out.println(
                        "✅ Carta atualizada com sucesso!"
                );

                return true;
            }

            System.out.println(
                    "⚠️ Nenhuma carta encontrada com essa matrícula."
            );

        } catch (SQLException erro) {
            System.err.println(
                    "❌ Erro ao atualizar aluno: "
                            + erro.getMessage()
            );
        }

        return false;
    }

    /**
     * Exclui uma carta pela matrícula.
     */
    public static boolean excluirAluno(String matricula) {
        String sql = """
                DELETE FROM aluno
                WHERE matricula = ?
                """;

        try (
                Connection conn = getConnection();
                PreparedStatement ps =
                        conn.prepareStatement(sql)
        ) {
            ps.setString(1, matricula);

            int linhasAfetadas = ps.executeUpdate();

            if (linhasAfetadas > 0) {
                System.out.println(
                        "✅ Carta removida com sucesso!"
                );

                return true;
            }

            System.out.println(
                    "⚠️ Nenhuma carta encontrada com essa matrícula."
            );

            return false;

        } catch (SQLException erro) {
            System.err.println(
                    "❌ Erro ao excluir aluno: "
                            + erro.getMessage()
            );

            return false;
        }
    }

    /**
     * Exibe todas as cartas cadastradas.
     */
    public static void exibirTodasCartas() {
        List<Aluno> alunos = consultarTodosAlunos();

        if (alunos.isEmpty()) {
            System.out.println(
                    "📭 Nenhuma carta encontrada no baralho."
            );

            return;
        }

        System.out.println(
                "\n🃏 === BARALHO SUPER TRUNFO ==="
        );

        System.out.printf(
                "Total de cartas: %d%n%n",
                alunos.size()
        );

        for (Aluno aluno : alunos) {
            aluno.exibirCarta();
            System.out.println();
        }
    }

    /**
     * Insere cinco cartas de exemplo.
     */
    public static void inserirDadosExemplo() {
        System.out.println(
                "\n🎲 Inserindo cartas de exemplo..."
        );

        Aluno[] exemplos = {
                new Aluno(
                        "A2020001",
                        "Ana Silva",
                        2020
                ),
                new Aluno(
                        "B2021002",
                        "Bruno Souza",
                        2021
                ),
                new Aluno(
                        "M2022003",
                        "Mariana Costa",
                        2022
                ),
                new Aluno(
                        "N2023004",
                        "Nicolas Rocha",
                        2023
                ),
                new Aluno(
                        "Z2024005",
                        "Zoe Martins",
                        2024
                )
        };

        int inseridos = 0;

        for (Aluno aluno : exemplos) {
            if (inserirAluno(aluno)) {
                inseridos++;
            }
        }

        System.out.printf(
                "✅ %d cartas inseridas com sucesso!%n",
                inseridos
        );
    }

    /**
     * Sorteia e realiza uma batalha entre duas cartas.
     */
    public static void batalharCartas() {
        List<Aluno> alunos = consultarTodosAlunos();

        if (alunos.size() < 2) {
            System.out.println(
                    "⚠️ É necessário ter pelo menos 2 cartas para batalhar!"
            );

            return;
        }

        System.out.println(
                "\n⚔️ === BATALHA SUPER TRUNFO ==="
        );

        Aluno carta1 =
                alunos.get(random.nextInt(alunos.size()));

        Aluno carta2;

        do {
            carta2 =
                    alunos.get(random.nextInt(alunos.size()));

        } while (
                carta1.getMatricula().equals(
                        carta2.getMatricula()
                )
        );

        System.out.println("\n🃏 Carta número 1:");
        carta1.exibirCarta();

        System.out.println("\n🃏 Carta número 2:");
        carta2.exibirCarta();

        System.out.println("\n⚔️ Resultado da batalha:");

        if (carta1.batalhar(carta2)) {
            System.out.println(
                    "🏆 " + carta1.getNome()
                            + " venceu com força "
                            + carta1.getForca() + "!"
            );

        } else if (carta2.batalhar(carta1)) {
            System.out.println(
                    "🏆 " + carta2.getNome()
                            + " venceu com força "
                            + carta2.getForca() + "!"
            );

        } else {
            System.out.println(
                    "🤝 A batalha terminou empatada!"
            );
        }
    }

    /**
     * Exibe o menu principal.
     */
    public static void exibirMenu() {
        System.out.println(
                "\n🃏 === SUPER TRUNFO - MENU PRINCIPAL ==="
        );
        System.out.println("1 - Exibir todas as cartas");
        System.out.println("2 - Inserir nova carta");
        System.out.println("3 - Buscar carta por matrícula");
        System.out.println("4 - Remover carta");
        System.out.println("5 - Batalhar cartas");
        System.out.println("6 - Inserir cartas de exemplo");
        System.out.println("7 - Atualizar carta");
        System.out.println("0 - Sair");
        System.out.print("Escolha uma opção: ");
    }

    /**
     * Processa a opção escolhida no menu.
     */
    public static void processarOpcao(int opcao) {
        switch (opcao) {
            case 1:
                exibirTodasCartas();
                break;

            case 2:
                System.out.println(
                        "\n➕ === INSERIR NOVA CARTA ==="
                );

                System.out.print("Digite a matrícula: ");
                String matricula =
                        scanner.nextLine().trim();

                System.out.print("Digite o nome: ");
                String nome =
                        scanner.nextLine().trim();

                int entrada =
                        lerNumeroInteiro(
                                "Digite o ano de entrada: "
                        );

                Aluno novoAluno =
                        new Aluno(
                                matricula,
                                nome,
                                entrada
                        );

                inserirAluno(novoAluno);
                break;

            case 3:
                System.out.println(
                        "\n🔍 === BUSCAR CARTA ==="
                );

                System.out.print("Digite a matrícula: ");

                String matriculaBusca =
                        scanner.nextLine().trim();

                Aluno encontrado =
                        buscarAluno(matriculaBusca);

                if (encontrado != null) {
                    System.out.println(
                            "\n✅ Carta encontrada:"
                    );

                    encontrado.exibirCarta();

                } else {
                    System.out.println(
                            "❌ Carta não encontrada!"
                    );
                }

                break;

            case 4:
                System.out.println(
                        "\n❌ === REMOVER CARTA ==="
                );

                System.out.print(
                        "Digite a matrícula da carta a ser removida: "
                );

                String matriculaRemover =
                        scanner.nextLine().trim();

                excluirAluno(matriculaRemover);
                break;

            case 5:
                batalharCartas();
                break;

            case 6:
                inserirDadosExemplo();
                break;

            case 7:
                System.out.println(
                        "\n✏️ === ATUALIZAR CARTA ==="
                );

                System.out.print(
                        "Digite a matrícula da carta: "
                );

                String matriculaAtualizar =
                        scanner.nextLine().trim();

                Aluno alunoAtual =
                        buscarAluno(matriculaAtualizar);

                if (alunoAtual == null) {
                    System.out.println(
                            "❌ Carta não encontrada!"
                    );

                    break;
                }

                System.out.print("Digite o novo nome: ");

                String novoNome =
                        scanner.nextLine().trim();

                int novaEntrada =
                        lerNumeroInteiro(
                                "Digite o novo ano de entrada: "
                        );

                alunoAtual.setNome(novoNome);
                alunoAtual.setEntrada(novaEntrada);

                atualizarAluno(alunoAtual);
                break;

            case 0:
                System.out.println(
                        "\n👋 Encerrando o Super Trunfo..."
                );
                break;

            default:
                System.out.println(
                        "❌ Opção inválida! Tente novamente."
                );
        }
    }

    /**
     * Lê um número inteiro sem encerrar o programa
     * quando o usuário digitar um valor inválido.
     */
    private static int lerNumeroInteiro(
            String mensagem
    ) {
        while (true) {
            System.out.print(mensagem);

            try {
                return Integer.parseInt(
                        scanner.nextLine().trim()
                );

            } catch (NumberFormatException erro) {
                System.out.println(
                        "❌ Digite um número inteiro válido."
                );
            }
        }
    }

    /**
     * Aguarda o usuário pressionar Enter.
     */
    private static void aguardarEnter() {
        System.out.println(
                "\nPressione Enter para continuar..."
        );

        scanner.nextLine();
    }

    /**
     * Método principal.
     */
    public static void main(String[] args) {
        System.out.println(
                "🃏 ==================================="
        );
        System.out.println(
                "   SUPER TRUNFO - CARTAS CLÁSSICAS"
        );
        System.out.println(
                "   Módulo 1 - Novato (JDBC Puro)"
        );
        System.out.println(
                "🃏 ==================================="
        );

        criarTabela();

        int opcao = -1;

        do {
            exibirMenu();

            try {
                opcao = Integer.parseInt(
                        scanner.nextLine().trim()
                );

                processarOpcao(opcao);

            } catch (NumberFormatException erro) {
                System.out.println(
                        "❌ Digite somente números nas opções do menu."
                );
            }

            if (opcao != 0) {
                aguardarEnter();
            }

        } while (opcao != 0);

        scanner.close();

        System.out.println(
                "Programa finalizado com sucesso!"
        );
    }
}