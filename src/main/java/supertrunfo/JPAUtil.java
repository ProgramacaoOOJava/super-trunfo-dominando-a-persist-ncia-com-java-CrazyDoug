//Feito por Douglas Alves Costa
//Nivel Aventureiro

package supertrunfo;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * Classe responsável pela criação e gerenciamento
 * do EntityManagerFactory.
 */
public final class JPAUtil {

    private static final EntityManagerFactory FACTORY =
            Persistence.createEntityManagerFactory(
                    "superTrunfoPU"
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