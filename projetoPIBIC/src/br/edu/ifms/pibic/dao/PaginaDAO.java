package br.edu.ifms.pibic.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import org.apache.lucene.document.CompressionTools;

import br.edu.ifms.pibic.model.Documento;
import br.edu.ifms.pibic.model.Pagina;

public class PaginaDAO extends JpaDAO<Pagina> {

	public Pagina recuperaPorDocumentoPagina(Documento documento,
			Integer numeroPagina) {
		try {
			em = criaEntityManager();
			Query consulta = em
					.createNamedQuery(Pagina.RECUPERA_POR_DOCUMENTO_PAGINA);
			consulta.setParameter("hash", documento.getHash());
			consulta.setParameter("numero", numeroPagina);
			Pagina pagina = (Pagina) consulta.getSingleResult();
			em.close();
			return pagina;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<Pagina> recuperaPaginasOrdenadas(Documento documento) {
		List<Pagina> paginas = new ArrayList<Pagina>();
		try {
			em = criaEntityManager();
			Query consulta = em
					.createNamedQuery(Pagina.RECUPERA_PAGINAS_ORDENADAS);
			consulta.setParameter("hash", documento.getHash());
			paginas = consulta.getResultList();
			paginas = descomprimir(paginas);
			em.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return paginas;
	}

	public boolean removePorDocumentoEPagina(Documento documento,
			Integer numeroPagina) {
		try {
			em = criaEntityManager();
			Query consulta = em
					.createNamedQuery(Pagina.REMOVE_POR_DOCUMENTO_E_PAGINA);
			consulta.setParameter("hash", documento.getHash());
			consulta.setParameter("numero", numeroPagina);
			EntityTransaction transacao = em.getTransaction();
			transacao.begin();
			consulta.executeUpdate();
			transacao.commit();
			em.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean removeTodasPaginas(Documento documento) {
		try {
			em = criaEntityManager();
			Query consulta = em.createNamedQuery(Pagina.REMOVE_TODAS_PAGINAS);
			consulta.setParameter("hash", documento.getHash());
			EntityTransaction transacao = em.getTransaction();
			transacao.begin();
			consulta.executeUpdate();
			transacao.commit();
			em.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	private List<Pagina> descomprimir(List<Pagina> paginas) {
		List<Pagina> paginasDescomprimidas = new ArrayList<Pagina>();
		if (paginas != null && paginas.size() > 0) {
			try {
				for (Pagina paginaComprimida : paginas) {
					byte[] bytesPagina = paginaComprimida.getBytesPagina();
					bytesPagina = CompressionTools.decompress(bytesPagina);
					Pagina pagina = new Pagina();
					pagina.setBytesPagina(bytesPagina);
					paginasDescomprimidas.add(pagina);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return paginasDescomprimidas;
	}
}
