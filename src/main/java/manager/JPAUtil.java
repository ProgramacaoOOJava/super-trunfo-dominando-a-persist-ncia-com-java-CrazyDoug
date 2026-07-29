//Feito por Douglas Alves Costa
//Nivel Mestre

package manager;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * Classe utilitária responsável por gerenciar
 * o EntityManagerFactory.
 */
public final class JPAUtil {

    private static final String UNIDADE_PERSISTENCIA =
            "superTrunfoMestrePU";

    private static final EntityManagerFactory FACTORY =
            Persistence.createEntityManagerFactory(
                    UNIDADE_PERSISTENCIA
            );

    private JPAUtil() {
    }

    public static EntityManager criarEntityManager() {
        return FACTORY.createEntityManager();
    }

    public static void fechar() {
        if (FACTORY.isOpen()) {
            FACTORY.close();
        }
    }
}