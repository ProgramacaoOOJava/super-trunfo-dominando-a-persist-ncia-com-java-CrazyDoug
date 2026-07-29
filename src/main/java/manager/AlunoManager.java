//Feito por Douglas Alves Costa
//Nivel Mestre

package manager;

import java.util.Collections;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import model.Aluno;

/**
 * Classe responsável pelas operações de persistência
 * relacionadas aos alunos.
 */
public class AlunoManager {

    /**
     * Insere uma nova carta no banco de dados.
     */
    public boolean inserir(Aluno aluno) {
        EntityManager entityManager =
                JPAUtil.criarEntityManager();

        EntityTransaction transaction =
                entityManager.getTransaction();

        try {
            transaction.begin();

            entityManager.persist(aluno);

            transaction.commit();

            return true;

        } catch (Exception erro) {
            realizarRollback(transaction);

            System.out.println(
                    "Erro ao inserir aluno: "
                            + obterMensagemErro(erro)
            );

            return false;

        } finally {
            entityManager.close();
        }
    }

    /**
     * Atualiza uma carta existente.
     */
    public boolean alterar(Aluno aluno) {
        EntityManager entityManager =
                JPAUtil.criarEntityManager();

        EntityTransaction transaction =
                entityManager.getTransaction();

        try {
            transaction.begin();

            Aluno existente =
                    entityManager.find(
                            Aluno.class,
                            aluno.getMatricula()
                    );

            if (existente == null) {
                transaction.rollback();
                return false;
            }

            existente.setNome(aluno.getNome());
            existente.setEntrada(aluno.getEntrada());
            existente.setLendaria(
                    aluno.isLendaria()
            );

            transaction.commit();

            return true;

        } catch (Exception erro) {
            realizarRollback(transaction);

            System.out.println(
                    "Erro ao alterar aluno: "
                            + obterMensagemErro(erro)
            );

            return false;

        } finally {
            entityManager.close();
        }
    }

    /**
     * Remove uma carta pela matrícula.
     */
    public boolean remover(String matricula) {
        EntityManager entityManager =
                JPAUtil.criarEntityManager();

        EntityTransaction transaction =
                entityManager.getTransaction();

        try {
            transaction.begin();

            Aluno aluno =
                    entityManager.find(
                            Aluno.class,
                            matricula
                    );

            if (aluno == null) {
                transaction.rollback();
                return false;
            }

            entityManager.remove(aluno);

            transaction.commit();

            return true;

        } catch (Exception erro) {
            realizarRollback(transaction);

            System.out.println(
                    "Erro ao remover aluno: "
                            + obterMensagemErro(erro)
            );

            return false;

        } finally {
            entityManager.close();
        }
    }

    /**
     * Busca uma carta pela matrícula.
     */
    public Aluno obter(String matricula) {
        EntityManager entityManager =
                JPAUtil.criarEntityManager();

        try {
            return entityManager.find(
                    Aluno.class,
                    matricula
            );

        } finally {
            entityManager.close();
        }
    }

    /**
     * Lista todas as cartas usando NamedQuery.
     */
    public List<Aluno> listarTodos() {
        EntityManager entityManager =
                JPAUtil.criarEntityManager();

        try {
            return entityManager
                    .createNamedQuery(
                            "Aluno.listarTodos",
                            Aluno.class
                    )
                    .getResultList();

        } catch (Exception erro) {
            System.out.println(
                    "Erro ao listar alunos: "
                            + obterMensagemErro(erro)
            );

            return Collections.emptyList();

        } finally {
            entityManager.close();
        }
    }

    /**
     * Lista apenas as cartas lendárias.
     */
    public List<Aluno> listarLendarios() {
        EntityManager entityManager =
                JPAUtil.criarEntityManager();

        try {
            return entityManager
                    .createNamedQuery(
                            "Aluno.listarLendarios",
                            Aluno.class
                    )
                    .getResultList();

        } catch (Exception erro) {
            System.out.println(
                    "Erro ao listar cartas lendarias: "
                            + obterMensagemErro(erro)
            );

            return Collections.emptyList();

        } finally {
            entityManager.close();
        }
    }

    /**
     * Busca alunos pelo nome.
     */
    public List<Aluno> buscarPorNome(String nome) {
        EntityManager entityManager =
                JPAUtil.criarEntityManager();

        try {
            return entityManager
                    .createNamedQuery(
                            "Aluno.buscarPorNome",
                            Aluno.class
                    )
                    .setParameter(
                            "nome",
                            "%" + nome + "%"
                    )
                    .getResultList();

        } finally {
            entityManager.close();
        }
    }

    /**
     * Conta todas as cartas do baralho.
     */
    public long contarTodos() {
        EntityManager entityManager =
                JPAUtil.criarEntityManager();

        try {
            return entityManager
                    .createNamedQuery(
                            "Aluno.contarTodos",
                            Long.class
                    )
                    .getSingleResult();

        } finally {
            entityManager.close();
        }
    }

    /**
     * Conta as cartas lendárias.
     */
    public long contarLendarios() {
        EntityManager entityManager =
                JPAUtil.criarEntityManager();

        try {
            return entityManager
                    .createNamedQuery(
                            "Aluno.contarLendarios",
                            Long.class
                    )
                    .getSingleResult();

        } finally {
            entityManager.close();
        }
    }

    private void realizarRollback(
            EntityTransaction transaction
    ) {
        if (
                transaction != null
                        && transaction.isActive()
        ) {
            transaction.rollback();
        }
    }

    private String obterMensagemErro(
            Exception erro
    ) {
        Throwable causa = erro;

        while (
                causa.getCause() != null
                        && causa.getCause() != causa
        ) {
            causa = causa.getCause();
        }

        if (causa.getMessage() == null) {
            return causa.getClass().getSimpleName();
        }

        return causa.getMessage();
    }
}