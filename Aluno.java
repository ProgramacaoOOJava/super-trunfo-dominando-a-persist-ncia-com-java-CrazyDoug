//Feito por Douglas Alves Costa
//Nivel Novato

/**
 * Classe que representa um aluno no sistema Super Trunfo.
 *
 * Cada aluno representa uma carta com atributos únicos:
 * - Nome: nome do aluno;
 * - Força: ano de entrada;
 * - Raridade: primeira letra da matrícula.
 */
public class Aluno {

    private String matricula;
    private String nome;
    private int entrada;

    // Construtor padrão
    public Aluno() {
    }

    // Construtor com parâmetros
    public Aluno(String matricula, String nome, int entrada) {
        this.matricula = matricula;
        this.nome = nome;
        this.entrada = entrada;
    }

    // Getters e setters

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getEntrada() {
        return entrada;
    }

    public void setEntrada(int entrada) {
        this.entrada = entrada;
    }

    /**
     * Calcula a força da carta com base no ano de entrada.
     * Quanto mais recente for o ano, maior será a força.
     */
    public int getForca() {
        return entrada;
    }

    /**
     * Determina a raridade da carta pela primeira letra da matrícula.
     * A-M = Comum
     * N-Z = Rara
     */
    public String getRaridade() {
        if (matricula == null || matricula.trim().isEmpty()) {
            return "Indefinida";
        }

        char primeiraLetra =
                Character.toUpperCase(matricula.charAt(0));

        if (primeiraLetra >= 'A' && primeiraLetra <= 'M') {
            return "Comum";
        }

        if (primeiraLetra >= 'N' && primeiraLetra <= 'Z') {
            return "Rara";
        }

        return "Indefinida";
    }

    /**
     * Exibe a carta no estilo Super Trunfo.
     */
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

    /**
     * Compara duas cartas.
     * A carta com o ano de entrada mais recente vence.
     */
    public boolean batalhar(Aluno oponente) {
        return this.entrada > oponente.entrada;
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