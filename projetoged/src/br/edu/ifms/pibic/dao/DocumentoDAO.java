package br.edu.ifms.pibic.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.query.dsl.QueryBuilder;
import br.edu.ifms.pibic.model.Documento;

public class DocumentoDAO extends JpaDAO<Documento> {

	/**
	 * Metodo usado para reindexar os documentos da base de dados
	 */
	public void reindexaBaseInteira() {
		em = criaEntityManager();
		try {
			em.createIndexer().startAndWait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		em.close();
	}

	/**
	 * Metodo para recuperar documentos indexados apartir de uma consulta
	 * @param consulta
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Documento> recuperaPorConteudo(String consulta) {
		List<Documento> documentos = new ArrayList<Documento>();
		try {
			em = criaEntityManager();
			QueryBuilder builder = em.getSearchFactory().buildQueryBuilder()
					.forEntity(Documento.class).get();
			org.apache.lucene.search.Query luceneQuery = builder.keyword()
					.onFields("conteudoTextual", "nomeArquivo")
					.matching(consulta).createQuery();
			FullTextQuery fullTextQuery = em.createFullTextQuery(luceneQuery);
			fullTextQuery.setProjection(FullTextQuery.SCORE, "hash",
					"nomeArquivo", "extensao", "numeroPaginas", "dataCriacao",
					"conteudoTextual");
			List<Object[]> documentosEncontrados = fullTextQuery
					.getResultList();
			return deArraysObjetosParaDocumentos(documentosEncontrados);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return documentos;
	}

	/**
	 * Metodo para transformar os objetos de um documento em um documento
	 * @param arraysDocumentos
	 * @return
	 */
	private List<Documento> deArraysObjetosParaDocumentos(
			List<Object[]> arraysDocumentos) {
		List<Documento> documentos = new ArrayList<Documento>();
		if (arraysDocumentos != null && arraysDocumentos.size() > 0) {
			for (Object[] arrayDocumento : arraysDocumentos) {
				Documento documento = new Documento();
				documento.setScore((Float) arrayDocumento[0]);
				documento.setHash(String.valueOf(arrayDocumento[1]));
				documento.setNomeArquivo(String.valueOf(arrayDocumento[2]));
				documento.setExtensao(String.valueOf(arrayDocumento[3]));
				documento.setNumeroPaginas((Integer) arrayDocumento[4]);
				documento.setDataCriacao((Date) arrayDocumento[5]);
				documento.setConteudoTextual(String.valueOf(arrayDocumento[6]));
				documentos.add(documento);
			}
		}
		return documentos;
	}
	
	public static void main(String[] args) {
		DocumentoDAO doc = new DocumentoDAO();
		List<Documento> documento = doc.recuperaPorConteudo("sistema");
		for(int i=0; i<documento.size(); i++){
			System.out.println(i+"- "+documento.get(i).getNomeArquivo());
		}
	}

}
