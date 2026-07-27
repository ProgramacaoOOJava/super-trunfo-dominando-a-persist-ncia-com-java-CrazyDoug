//Feito por Douglas Alves Costa
//Nivel Aventureiro

package supertrunfo;

import java.util.List;
import java.util.Scanner;

/**
 * Sistema Super Trunfo com DAO e JPA.
 */
public class SuperTrunfoJPA {

    private static final Scanner scanner =
            new Scanner(System.in);

    private static final AlunoDAO alunoDAO =
            new AlunoDAO();

    private static final int PONTUACAO_MAXIMA = 5;

    private static int pontuacao = 0;

    public static void main(String[] args) {
        System.out.println(
                "🃏 ======================================="
        );
        System.out.println(
                "     SUPER TRUNFO - DAO E JPA"
        );
        System.out.println(
                "     NÍVEL AVENTUREIRO"
        );
        System.out.println(
                "🃏 ======================================="
        );

        int opcao = 0;

        try {
            do {
                exibirMenu();

                opcao = lerInteiro(
                        "Escolha uma opção: "
                );

                processarOpcao(opcao);

                if (pontuacao >= PONTUACAO_MAXIMA) {
                    System.out.println(
                            "\n🏆 Você alcançou 5 pontos!"
                    );

                    System.out.println(
                            "O sistema será finalizado automaticamente."
                    );

                    opcao = 6;
                }

            } while (opcao != 6);

        } finally {
            scanner.close();
            JPAUtil.fechar();
        }

        System.out.println(
                "\n👋 Super Trunfo encerrado!"
        );

        System.out.println(
                "Pontuação final: "
                        + pontuacao
                        + "/"
                        + PONTUACAO_MAXIMA
        );
    }

    /**
     * Menu com exatamente seis opções.
     */
    private static void exibirMenu() {
        System.out.println(
                "\n🃏 === MENU PRINCIPAL ==="
        );

        System.out.println(
                "Pontuação atual: "
                        + pontuacao
                        + "/"
                        + PONTUACAO_MAXIMA
        );

        System.out.println("1 - Inserir aluno");
        System.out.println("2 - Remover aluno");
        System.out.println("3 - Alterar aluno");
        System.out.println("4 - Listar alunos");
        System.out.println("5 - Obter aluno");
        System.out.println("6 - Sair");
    }

    private static void processarOpcao(int opcao) {
        switch (opcao) {
            case 1:
                inserirAluno();
                break;

            case 2:
                removerAluno();
                break;

            case 3:
                alterarAluno();
                break;

            case 4:
                listarAlunos();
                break;

            case 5:
                obterAluno();
                break;

            case 6:
                System.out.println(
                        "\n👋 Saindo do sistema..."
                );
                break;

            default:
                System.out.println(
                        "❌ Opção inválida."
                );
        }
    }

    private static void inserirAluno() {
        System.out.println(
                "\n➕ === INSERIR ALUNO ==="
        );

        try {
            System.out.print("Matrícula: ");

            String matricula =
                    scanner.nextLine().trim();

            System.out.print("Nome: ");

            String nome =
                    scanner.nextLine().trim();

            int entrada =
                    lerInteiro("Ano de entrada: ");

            Aluno aluno =
                    new Aluno(
                            matricula,
                            nome,
                            entrada
                    );

            if (alunoDAO.inserir(aluno)) {
                System.out.println(
                        "✅ Aluno inserido com sucesso!"
                );

                adicionarPonto();
            }

        } catch (IllegalArgumentException erro) {
            System.out.println(
                    "❌ " + erro.getMessage()
            );
        }
    }

    private static void removerAluno() {
        System.out.println(
                "\n🗑️ === REMOVER ALUNO ==="
        );

        System.out.print("Digite a matrícula: ");

        String matricula =
                scanner.nextLine()
                        .trim()
                        .toUpperCase();

        if (alunoDAO.remover(matricula)) {
            System.out.println(
                    "✅ Aluno removido com sucesso!"
            );

            adicionarPonto();

        } else {
            System.out.println(
                    "⚠️ Aluno não encontrado."
            );
        }
    }

    private static void alterarAluno() {
        System.out.println(
                "\n✏️ === ALTERAR ALUNO ==="
        );

        System.out.print("Digite a matrícula: ");

        String matricula =
                scanner.nextLine()
                        .trim()
                        .toUpperCase();

        Aluno alunoExistente =
                alunoDAO.obter(matricula);

        if (alunoExistente == null) {
            System.out.println(
                    "⚠️ Aluno não encontrado."
            );

            return;
        }

        try {
            System.out.print("Novo nome: ");

            String novoNome =
                    scanner.nextLine().trim();

            int novaEntrada =
                    lerInteiro(
                            "Novo ano de entrada: "
                    );

            Aluno alunoAlterado =
                    new Aluno(
                            matricula,
                            novoNome,
                            novaEntrada
                    );

            if (
                    alunoDAO.alterar(
                            alunoAlterado,
                            matricula
                    )
            ) {
                System.out.println(
                        "✅ Aluno alterado com sucesso!"
                );

                adicionarPonto();
            }

        } catch (IllegalArgumentException erro) {
            System.out.println(
                    "❌ " + erro.getMessage()
            );
        }
    }

    private static void listarAlunos() {
        System.out.println(
                "\n📋 === LISTA DE ALUNOS ==="
        );

        try {
            List<Aluno> alunos =
                    alunoDAO.listarTodos();

            if (alunos.isEmpty()) {
                System.out.println(
                        "📭 Nenhum aluno cadastrado."
                );

            } else {
                for (Aluno aluno : alunos) {
                    aluno.exibirCarta();
                    System.out.println();
                }
            }

            System.out.println(
                    "Total de alunos: "
                            + alunos.size()
            );

            adicionarPonto();

        } catch (Exception erro) {
            System.out.println(
                    "❌ Erro ao listar alunos: "
                            + erro.getMessage()
            );
        }
    }

    private static void obterAluno() {
        System.out.println(
                "\n🔍 === OBTER ALUNO ==="
        );

        System.out.print("Digite a matrícula: ");

        String matricula =
                scanner.nextLine()
                        .trim()
                        .toUpperCase();

        Aluno aluno =
                alunoDAO.obter(matricula);

        if (aluno == null) {
            System.out.println(
                    "⚠️ Aluno não encontrado."
            );

            return;
        }

        System.out.println(
                "\n✅ Carta encontrada:"
        );

        aluno.exibirCarta();
        adicionarPonto();
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
                        "❌ Digite um número inteiro válido."
                );
            }
        }
    }

    private static void adicionarPonto() {
        pontuacao++;

        System.out.println(
                "⭐ Operação válida! +1 ponto."
        );

        System.out.println(
                "Pontuação: "
                        + pontuacao
                        + "/"
                        + PONTUACAO_MAXIMA
        );
    }
}