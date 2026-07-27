//Feito por Douglas Alves Costa
//Nivel Aventureiro

package supertrunfo;

/**
 * DAO específico para a entidade Aluno.
 */
public class AlunoDAO
        extends GenericDAO<Aluno, String> {

    public AlunoDAO() {
        super(Aluno.class);
    }
}