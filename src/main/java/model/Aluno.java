//Feito por Douglas Alves Costa
//Nivel Mestre

package model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

/**
 * Entidade que representa um aluno como uma carta
 * do jogo Super Trunfo.
 */
@Entity
@Table(name = "alunos")
@NamedQueries({
        @NamedQuery(
                name = "Aluno.listarTodos",
                query = "SELECT a FROM Aluno a ORDER BY a.nome"
        ),
        @NamedQuery(
                name = "Aluno.listarLendarios",
                query = "SELECT a FROM Aluno a "
                        + "WHERE a.lendaria = true "
                        + "ORDER BY a.nome"
        ),
        @NamedQuery(
                name = "Aluno.contarTodos",
                query = "SELECT COUNT(a) FROM Aluno a"
        ),
        @NamedQuery(
                name = "Aluno.contarLendarios",
                query = "SELECT COUNT(a) FROM Aluno a "
                        + "WHERE a.lendaria = true"
        ),
        @NamedQuery(
                name = "Aluno.buscarPorNome",
                query = "SELECT a FROM Aluno a "
                        + "WHERE UPPER(a.nome) LIKE UPPER(:nome) "
                        + "ORDER BY a.nome"
        )
})
public class Aluno {

    @Id
    @Column(
            name = "matricula",
            nullable = false,
            unique = true,
            length = 30
    )
    private String matricula;

    @Column(
            name = "nome",
            nullable = false,
            length = 100
    )
    private String nome;

    @Column(
            name = "entrada",
            nullable = false
    )
    private int entrada;

    @Column(
            name = "lendaria",
            nullable = false
    )
    private boolean lendaria;

    /**
     * Construtor obrigatório para o JPA.
     */
    public Aluno() {
    }

    public Aluno(
            String matricula,
            String nome,
            int entrada
    ) {
        this(
                matricula,
                nome,
                entrada,
                false
        );
    }

    public Aluno(
            String matricula,
            String nome,
            int entrada,
            boolean lendaria
    ) {
        setMatricula(matricula);
        setNome(nome);
        setEntrada(entrada);
        this.lendaria = lendaria;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        if (
                matricula == null
                        || matricula.trim().isEmpty()
        ) {
            throw new IllegalArgumentException(
                    "A matricula nao pode estar vazia."
            );
        }

        this.matricula =
                matricula.trim().toUpperCase();
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        if (
                nome == null
                        || nome.trim().isEmpty()
        ) {
            throw new IllegalArgumentException(
                    "O nome nao pode estar vazio."
            );
        }

        this.nome = nome.trim();
    }

    public int getEntrada() {
        return entrada;
    }

    public void setEntrada(int entrada) {
        if (entrada < 1900 || entrada > 2030) {
            throw new IllegalArgumentException(
                    "O ano de entrada deve estar "
                            + "entre 1900 e 2030."
            );
        }

        this.entrada = entrada;
    }

    public boolean isLendaria() {
        return lendaria;
    }

    public void setLendaria(boolean lendaria) {
        this.lendaria = lendaria;
    }

    /**
     * Calcula a força da carta.
     *
     * Cartas lendárias recebem um bônus.
     */
    public int getForca() {
        int forcaBase = entrada - 1900;

        if (lendaria) {
            forcaBase += 100;
        }

        return forcaBase;
    }

    /**
     * Determina a raridade da carta.
     */
    public String getRaridade() {
        if (lendaria) {
            return "Lendaria";
        }

        if (
                matricula == null
                        || matricula.isBlank()
        ) {
            return "Indefinida";
        }

        char primeiraLetra =
                Character.toUpperCase(
                        matricula.charAt(0)
                );

        if (
                primeiraLetra >= 'A'
                        && primeiraLetra <= 'M'
        ) {
            return "Comum";
        }

        if (
                primeiraLetra >= 'N'
                        && primeiraLetra <= 'Z'
        ) {
            return "Rara";
        }

        return "Indefinida";
    }

    /**
     * Retorna um valor numérico para comparação
     * da raridade durante o torneio.
     */
    public int getValorRaridade() {
        if (lendaria) {
            return 3;
        }

        if ("Rara".equals(getRaridade())) {
            return 2;
        }

        if ("Comum".equals(getRaridade())) {
            return 1;
        }

        return 0;
    }

    /**
     * Exibe a carta com uma borda diferente
     * quando ela for lendária.
     */
    public void exibirCarta() {
        if (lendaria) {
            exibirCartaLendaria();
        } else {
            exibirCartaNormal();
        }
    }

    private void exibirCartaNormal() {
        System.out.println(
                "+----------------------------------------+"
        );
        System.out.println(
                "|          CARTA SUPER TRUNFO             |"
        );
        System.out.println(
                "+----------------------------------------+"
        );

        exibirInformacoes();

        System.out.println(
                "+----------------------------------------+"
        );
    }

    private void exibirCartaLendaria() {
        System.out.println(
                "##########################################"
        );
        System.out.println(
                "#          CARTA LENDARIA                #"
        );
        System.out.println(
                "##########################################"
        );

        System.out.printf(
                "# Matricula: %-27s #%n",
                matricula
        );

        System.out.printf(
                "# Nome:      %-27s #%n",
                nome
        );

        System.out.printf(
                "# Entrada:   %-27d #%n",
                entrada
        );

        System.out.printf(
                "# Forca:     %-27d #%n",
                getForca()
        );

        System.out.printf(
                "# Raridade:  %-27s #%n",
                getRaridade()
        );

        System.out.println(
                "##########################################"
        );
    }

    private void exibirInformacoes() {
        System.out.printf(
                "| Matricula: %-27s |%n",
                matricula
        );

        System.out.printf(
                "| Nome:      %-27s |%n",
                nome
        );

        System.out.printf(
                "| Entrada:   %-27d |%n",
                entrada
        );

        System.out.printf(
                "| Forca:     %-27d |%n",
                getForca()
        );

        System.out.printf(
                "| Raridade:  %-27s |%n",
                getRaridade()
        );
    }

    @Override
    public String toString() {
        return "Aluno{"
                + "matricula='" + matricula + '\''
                + ", nome='" + nome + '\''
                + ", entrada=" + entrada
                + ", forca=" + getForca()
                + ", raridade='" + getRaridade() + '\''
                + ", lendaria=" + lendaria
                + '}';
    }
}