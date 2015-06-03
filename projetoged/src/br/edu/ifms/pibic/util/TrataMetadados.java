package br.edu.ifms.pibic.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Classe responsavel pelo tratamento de datas
 * @author marilia
 *
 */
public class TrataMetadados {

	
	public String trataResumo(String texto){
		if(texto.length() > 400){
			return texto.substring(0, 200)+"...";
		}else{
			return"";
		}
	}
	
	public String[] dataDoDocumento(String data) {

		data = data.trim();
		// separa hora e data
		String listaDataDocumento[] = data.split(" ");
		// separa dia, mes e ano
		listaDataDocumento = listaDataDocumento[0].split("/");
		return listaDataDocumento;

	}

	public String[] dataDeHoje() {

		Date dataDeHoje = new Date();
		SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy");
		// separa dia, mes e ano
		String listaDataDeHoje[] = formatador.format(dataDeHoje).split("/");
		return listaDataDeHoje;

	}

	public String tempoDoDocumento(String data) {

		String tempoPostado;
		int tempo;

		// dados atuais
		String listaDataDeHoje[] = dataDeHoje();
		int anoAtual = Integer.parseInt(listaDataDeHoje[2]);
		int mesAtual = Integer.parseInt(listaDataDeHoje[1]);		
		int diaAtual = Integer.parseInt(listaDataDeHoje[0]);

		
		// dados do documento
		String listaDataDocumento[] = dataDoDocumento(data);
		// guarda cada um em um inteiro dia, mes e ano
		int anoDoc = Integer.parseInt(listaDataDocumento[2]);
		int mesDoc = Integer.parseInt(listaDataDocumento[1]);
		int diaDoc = Integer.parseInt(listaDataDocumento[0]);

		if (anoAtual > anoDoc) {
			tempo = anoAtual - anoDoc;
			
			if (tempo == 1) {
				tempoPostado = "Há "+tempo + " ano atrás.";
			} else {
				tempoPostado = "Há "+tempo + " anos atrás.";
			}

		} else if (mesAtual > mesDoc) {
			tempo = mesAtual - mesDoc;
			if (tempo == 1) {
				tempoPostado = "Há "+tempo + " mes atrás.";
			} else {
				tempoPostado = "Há "+tempo + " meses atrás.";
			}

		} else {
			tempo = diaAtual - diaDoc;			
			if(tempo == 0){
				tempoPostado = "Postado hoje.";
			}else if (tempo == 1) {
				tempoPostado = "Há "+tempo + " dia atrás.";
			} else {
				tempoPostado = "Há "+tempo + " dias atrás.";
			}
		}

		return tempoPostado;
	}

}
