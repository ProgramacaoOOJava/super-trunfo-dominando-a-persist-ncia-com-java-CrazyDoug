//Feito por Douglas Alves Costa
//Nivel Mestre

package app;

import java.util.List;
import java.util.Random;
import java.util.Scanner;

import manager.AlunoManager;
import manager.JPAUtil;
import model.Aluno;

/**
 * Sistema principal do Super Trunfo.
 */
public class SistemaEscola {

    private static final Scanner scanner =
            new Scanner(System.in);

    private static final Random random =
            new Random();

    private static final AlunoManager alunoManager =
            new AlunoManager();

    private static int vitorias = 0;
    private static int derrotas = 0;
    private static int empates = 0;

    public static void main(String[] args) {
        System.out.println(
                "=========================================="
        );
        System.out.println(
                "       SUPER TRUNFO - NIVEL MESTRE"
        );
        System.out.println(
                "=========================================="
        );

        int opcao;

        try {
            do {
                exibirMenu();

                opcao = lerInteiro(
                        "Escolha uma opcao: "
                );

                processarOpcao(opcao);

                if (opcao != 7) {
                    aguardarEnter();
                }

            } while (opcao != 7);

        } finally {
            scanner.close();
            JPAUtil.fechar();
        }

        System.out.println(
                "\nSistema encerrado."
        );
    }

    /**
     * Menu com exatamente sete opções.
     */
    private static void exibirMenu() {
        exibirEstatisticas();

        System.out.println(
                "\n============= MENU ============="
        );
        System.out.println("1 - Inserir carta");
        System.out.println("2 - Remover carta");
        System.out.println("3 - Alterar carta");
        System.out.println("4 - Listar todas as cartas");
        System.out.println("5 - Iniciar mini-torneio");
        System.out.println("6 - Visualizar cartas lendarias");
        System.out.println("7 - Sair");
        System.out.println(
                "================================"
        );
    }

    private static void processarOpcao(int opcao) {
        switch (opcao) {
            case 1:
                inserirCarta();
                break;

            case 2:
                removerCarta();
                break;

            case 3:
                alterarCarta();
                break;

            case 4:
                listarCartas();
                break;

            case 5:
                iniciarTorneio();
                break;

            case 6:
                listarCartasLendarias();
                break;

            case 7:
                System.out.println(
                        "\nFinalizando o programa..."
                );
                break;

            default:
                System.out.println(
                        "\nOpcao invalida."
                );
        }
    }

    private static void inserirCarta() {
        System.out.println(
                "\n===== INSERIR CARTA ====="
        );

        try {
            System.out.print("Matricula: ");

            String matricula =
                    scanner.nextLine()
                            .trim()
                            .toUpperCase();

            System.out.print("Nome: ");

            String nome =
                    scanner.nextLine().trim();

            int entrada =
                    lerInteiro(
                            "Ano de entrada: "
                    );

            Aluno aluno =
                    new Aluno(
                            matricula,
                            nome,
                            entrada
                    );

            if (alunoManager.inserir(aluno)) {
                System.out.println(
                        "Carta inserida com sucesso."
                );
            } else {
                System.out.println(
                        "Nao foi possivel inserir a carta."
                );
            }

        } catch (IllegalArgumentException erro) {
            System.out.println(
                    "Dados invalidos: "
                            + erro.getMessage()
            );
        }
    }

    private static void removerCarta() {
        System.out.println(
                "\n===== REMOVER CARTA ====="
        );

        System.out.print("Matricula: ");

        String matricula =
                scanner.nextLine()
                        .trim()
                        .toUpperCase();

        if (alunoManager.remover(matricula)) {
            System.out.println(
                    "Carta removida com sucesso."
            );
        } else {
            System.out.println(
                    "Carta nao encontrada."
            );
        }
    }

    private static void alterarCarta() {
        System.out.println(
                "\n===== ALTERAR CARTA ====="
        );

        System.out.print("Matricula: ");

        String matricula =
                scanner.nextLine()
                        .trim()
                        .toUpperCase();

        Aluno alunoExistente =
                alunoManager.obter(matricula);

        if (alunoExistente == null) {
            System.out.println(
                    "Carta nao encontrada."
            );

            return;
        }

        alunoExistente.exibirCarta();

        try {
            System.out.print("Novo nome: ");

            String nome =
                    scanner.nextLine().trim();

            int entrada =
                    lerInteiro(
                            "Novo ano de entrada: "
                    );

            Aluno alunoAlterado =
                    new Aluno(
                            matricula,
                            nome,
                            entrada,
                            alunoExistente.isLendaria()
                    );

            if (
                    alunoManager.alterar(
                            alunoAlterado
                    )
            ) {
                System.out.println(
                        "Carta alterada com sucesso."
                );
            } else {
                System.out.println(
                        "Nao foi possivel alterar a carta."
                );
            }

        } catch (IllegalArgumentException erro) {
            System.out.println(
                    "Dados invalidos: "
                            + erro.getMessage()
            );
        }
    }

    private static void listarCartas() {
        System.out.println(
                "\n===== BARALHO COMPLETO ====="
        );

        List<Aluno> alunos =
                alunoManager.listarTodos();

        if (alunos.isEmpty()) {
            System.out.println(
                    "Nenhuma carta cadastrada."
            );

            return;
        }

        for (int i = 0; i < alunos.size(); i++) {
            System.out.println(
                    "\nCarta " + (i + 1)
            );

            alunos.get(i).exibirCarta();
        }

        System.out.println(
                "\nTotal de cartas: "
                        + alunos.size()
        );
    }

    private static void listarCartasLendarias() {
        System.out.println(
                "\n===== CARTAS LENDARIAS ====="
        );

        List<Aluno> lendarias =
                alunoManager.listarLendarios();

        if (lendarias.isEmpty()) {
            System.out.println(
                    "Nenhuma carta lendaria foi conquistada."
            );

            return;
        }

        for (Aluno aluno : lendarias) {
            aluno.exibirCarta();
            System.out.println();
        }

        System.out.println(
                "Total de cartas lendarias: "
                        + lendarias.size()
        );
    }

    private static void iniciarTorneio() {
        List<Aluno> cartas =
                alunoManager.listarTodos();

        if (cartas.size() < 2) {
            System.out.println(
                    "\nCadastre pelo menos duas cartas "
                            + "para iniciar o torneio."
            );

            return;
        }

        System.out.println(
                "\n===== MINI-TORNEIO ====="
        );

        exibirCartasParaEscolha(cartas);

        int indiceEscolhido =
                lerInteiro(
                        "Escolha o numero da sua carta: "
                ) - 1;

        if (
                indiceEscolhido < 0
                        || indiceEscolhido
                        >= cartas.size()
        ) {
            System.out.println(
                    "Carta escolhida invalida."
            );

            return;
        }

        Aluno cartaJogador =
                cartas.get(indiceEscolhido);

        Aluno cartaAdversaria =
                sortearAdversario(
                        cartas,
                        cartaJogador
                );

        System.out.println(
                "\nSua carta:"
        );
        cartaJogador.exibirCarta();

        System.out.println(
                "\nCarta adversaria:"
        );
        cartaAdversaria.exibirCarta();

        int atributo =
                escolherAtributo();

        int valorJogador =
                obterValorAtributo(
                        cartaJogador,
                        atributo
                );

        int valorAdversario =
                obterValorAtributo(
                        cartaAdversaria,
                        atributo
                );

        exibirValoresDaBatalha(
                cartaJogador,
                cartaAdversaria,
                atributo,
                valorJogador,
                valorAdversario
        );

        if (valorJogador > valorAdversario) {
            registrarVitoria();

        } else if (
                valorJogador < valorAdversario
        ) {
            derrotas++;

            System.out.println(
                    "\nA carta adversaria venceu."
            );

        } else {
            empates++;

            System.out.println(
                    "\nA batalha terminou empatada."
            );
        }
    }

    private static void exibirCartasParaEscolha(
            List<Aluno> cartas
    ) {
        for (int i = 0; i < cartas.size(); i++) {
            Aluno aluno = cartas.get(i);

            System.out.printf(
                    "%d - %s | Matricula: %s | "
                            + "Forca: %d | Raridade: %s%n",
                    i + 1,
                    aluno.getNome(),
                    aluno.getMatricula(),
                    aluno.getForca(),
                    aluno.getRaridade()
            );
        }
    }

    private static Aluno sortearAdversario(
            List<Aluno> cartas,
            Aluno cartaJogador
    ) {
        Aluno adversario;

        do {
            adversario =
                    cartas.get(
                            random.nextInt(
                                    cartas.size()
                            )
                    );

        } while (
                adversario.getMatricula().equals(
                        cartaJogador.getMatricula()
                )
        );

        return adversario;
    }

    private static int escolherAtributo() {
        while (true) {
            System.out.println(
                    "\nEscolha o atributo:"
            );
            System.out.println("1 - Forca");
            System.out.println("2 - Ano de entrada");
            System.out.println("3 - Raridade");

            int atributo =
                    lerInteiro(
                            "Atributo: "
                    );

            if (
                    atributo >= 1
                            && atributo <= 3
            ) {
                return atributo;
            }

            System.out.println(
                    "Atributo invalido."
            );
        }
    }

    private static int obterValorAtributo(
            Aluno aluno,
            int atributo
    ) {
        return switch (atributo) {
            case 1 -> aluno.getForca();
            case 2 -> aluno.getEntrada();
            case 3 -> aluno.getValorRaridade();
            default -> 0;
        };
    }

    private static void exibirValoresDaBatalha(
            Aluno jogador,
            Aluno adversario,
            int atributo,
            int valorJogador,
            int valorAdversario
    ) {
        String nomeAtributo =
                switch (atributo) {
                    case 1 -> "Forca";
                    case 2 -> "Ano de entrada";
                    case 3 -> "Raridade";
                    default -> "Desconhecido";
                };

        System.out.println(
                "\nAtributo escolhido: "
                        + nomeAtributo
        );

        System.out.println(
                jogador.getNome()
                        + ": "
                        + valorJogador
        );

        System.out.println(
                adversario.getNome()
                        + ": "
                        + valorAdversario
        );
    }

    private static void registrarVitoria() {
        vitorias++;

        System.out.println(
                "\nVoce venceu a batalha."
        );

        System.out.println(
                "Uma carta lendaria sera adicionada "
                        + "ao seu baralho."
        );

        gerarCartaLendaria();
    }

    private static void gerarCartaLendaria() {
        String matricula =
                gerarMatriculaLendaria();

        String[] nomesLendarios = {
                "Guardiao Supremo",
                "Mestre do Conhecimento",
                "Lenda Academica",
                "Sabio do Futuro",
                "Campeao da Escola"
        };

        String nome =
                nomesLendarios[
                        random.nextInt(
                                nomesLendarios.length
                        )
                ];

        Aluno cartaLendaria =
                new Aluno(
                        matricula,
                        nome,
                        2030,
                        true
                );

        if (
                alunoManager.inserir(
                        cartaLendaria
                )
        ) {
            System.out.println(
                    "\nCarta lendaria conquistada:"
            );

            cartaLendaria.exibirCarta();

        } else {
            System.out.println(
                    "Nao foi possivel salvar "
                            + "a carta lendaria."
            );
        }
    }

    private static String gerarMatriculaLendaria() {
        String matricula;

        do {
            int numero =
                    random.nextInt(
                            900000
                    ) + 100000;

            matricula =
                    "L2030" + numero;

        } while (
                alunoManager.obter(matricula) != null
        );

        return matricula;
    }

    private static void exibirEstatisticas() {
        long totalCartas =
                alunoManager.contarTodos();

        long totalLendarias =
                alunoManager.contarLendarios();

        System.out.println(
                "\n========== ESTATISTICAS =========="
        );

        System.out.println(
                "Cartas cadastradas: "
                        + totalCartas
        );

        System.out.println(
                "Cartas lendarias: "
                        + totalLendarias
        );

        System.out.println(
                "Vitorias nesta execucao: "
                        + vitorias
        );

        System.out.println(
                "Derrotas nesta execucao: "
                        + derrotas
        );

        System.out.println(
                "Empates nesta execucao: "
                        + empates
        );

        System.out.println(
                "=================================="
        );
    }

    private static int lerInteiro(
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
                        "Digite um numero inteiro valido."
                );
            }
        }
    }

    private static void aguardarEnter() {
        System.out.println(
                "\nPressione Enter para continuar..."
        );

        scanner.nextLine();
    }
}