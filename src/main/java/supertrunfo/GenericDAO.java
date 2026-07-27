//Feito por Douglas Alves Costa
//Nivel Aventureiro

package supertrunfo;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.List;

/**
 * DAO genérico reutilizável para operações CRUD.
 *
 * @param <E> tipo da entidade
 * @param <K> tipo da chave primária
 */
public abstract class GenericDAO<E, K> {

    private final Class<E> tipoEntidade;

    protected GenericDAO(Class<E> tipoEntidade) {
        this.tipoEntidade = tipoEntidade;
    }

    public boolean inserir(E entidade) {
        EntityManager entityManager =
                JPAUtil.criarEntityManager();

        EntityTransaction transaction =
                entityManager.getTransaction();

        try {
            transaction.begin();
            entityManager.persist(entidade);
            transaction.commit();

            return true;

        } catch (Exception erro) {
            if (transaction.isActive()) {
                transaction.rollback();
            }

            System.err.println(
                    "❌ Erro ao inserir: "
                            + obterMensagemErro(erro)
            );

            return false;

        } finally {
            entityManager.close();
        }
    }

    public boolean remover(K chave) {
        EntityManager entityManager =
                JPAUtil.criarEntityManager();

        EntityTransaction transaction =
                entityManager.getTransaction();

        try {
            transaction.begin();

            E entidade =
                    entityManager.find(tipoEntidade, chave);

            if (entidade == null) {
                transaction.rollback();
                return false;
            }

            entityManager.remove(entidade);
            transaction.commit();

            return true;

        } catch (Exception erro) {
            if (transaction.isActive()) {
                transaction.rollback();
            }

            System.err.println(
                    "❌ Erro ao remover: "
                            + obterMensagemErro(erro)
            );

            return false;

        } finally {
            entityManager.close();
        }
    }

    public boolean alterar(E entidade, K chave) {
        EntityManager entityManager =
                JPAUtil.criarEntityManager();

        EntityTransaction transaction =
                entityManager.getTransaction();

        try {
            transaction.begin();

            E existente =
                    entityManager.find(tipoEntidade, chave);

            if (existente == null) {
                transaction.rollback();
                return false;
            }

            entityManager.merge(entidade);
            transaction.commit();

            return true;

        } catch (Exception erro) {
            if (transaction.isActive()) {
                transaction.rollback();
            }

            System.err.println(
                    "❌ Erro ao alterar: "
                            + obterMensagemErro(erro)
            );

            return false;

        } finally {
            entityManager.close();
        }
    }

    public List<E> listarTodos() {
        EntityManager entityManager =
                JPAUtil.criarEntityManager();

        try {
            String jpql =
                    "SELECT entidade FROM "
                            + tipoEntidade.getSimpleName()
                            + " entidade";

            return entityManager
                    .createQuery(jpql, tipoEntidade)
                    .getResultList();

        } finally {
            entityManager.close();
        }
    }

    public E obter(K chave) {
        EntityManager entityManager =
                JPAUtil.criarEntityManager();

        try {
            return entityManager.find(
                    tipoEntidade,
                    chave
            );

        } finally {
            entityManager.close();
        }
    }

    private String obterMensagemErro(Exception erro) {
        Throwable causa = erro;

        while (
                causa.getCause() != null
                        && causa.getCause() != causa
        ) {
            causa = causa.getCause();
        }

        return causa.getMessage();
    }
}