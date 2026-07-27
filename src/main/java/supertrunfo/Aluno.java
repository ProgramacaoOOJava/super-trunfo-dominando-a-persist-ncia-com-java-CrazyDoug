//Feito por Douglas Alves Costa
//Nivel Aventureiro

package supertrunfo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entidade que representa uma carta de aluno.
 */
@Entity
@Table(name = "alunos")
public class Aluno {

    @Id
    @Column(
            name = "matricula",
            nullable = false,
            unique = true,
            length = 20
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
        setMatricula(matricula);
        setNome(nome);
        setEntrada(entrada);
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        if (matricula == null || matricula.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "A matrícula não pode estar vazia."
            );
        }

        this.matricula = matricula.trim().toUpperCase();
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "O nome não pode estar vazio."
            );
        }

        this.nome = nome.trim();
    }

    public int getEntrada() {
        return entrada;
    }

    public void setEntrada(int entrada) {
        if (entrada < 1900 || entrada > 2100) {
            throw new IllegalArgumentException(
                    "O ano de entrada deve estar entre 1900 e 2100."
            );
        }

        this.entrada = entrada;
    }

    public int getForca() {
        return entrada;
    }

    public String getRaridade() {
        if (matricula == null || matricula.isBlank()) {
            return "Indefinida";
        }

        char primeiraLetra =
                Character.toUpperCase(matricula.charAt(0));

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

    public void exibirCarta() {
        System.out.println("+--------------------------------------+");
        System.out.println("|          CARTA SUPER TRUNFO           |");
        System.out.println("+--------------------------------------+");

        System.out.printf(
                "| Matrícula: %-26s |%n",
                matricula
        );

        System.out.printf(
                "| Nome:      %-26s |%n",
                nome
        );

        System.out.printf(
                "| Entrada:   %-26d |%n",
                entrada
        );

        System.out.printf(
                "| Força:     %-26d |%n",
                getForca()
        );

        System.out.printf(
                "| Raridade:  %-26s |%n",
                getRaridade()
        );

        System.out.println("+--------------------------------------+");
    }

    @Override
    public String toString() {
        return "Matrícula: " + matricula
                + "\nNome: " + nome
                + "\nAno de Entrada: " + entrada
                + "\nForça: " + getForca()
                + "\nRaridade: " + getRaridade();
    }
}