package br.edu.ifms.pibic.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;

public class JPAUtil {
	private static final EntityManagerFactory emf = Persistence
			.createEntityManagerFactory("development");

	private JPAUtil() {
	}

	public static FullTextEntityManager getEntityManager() {
		EntityManager em = emf.createEntityManager();
		return Search.getFullTextEntityManager(em);
	}
}
