package generator_src;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class GLA {

	private static Map<String, String> regexi = new HashMap<>(); 	
	private static List<String> stanjaLAnalizatora = new LinkedList<>();
	private static List<String> tokeni = new LinkedList<>();

	private static BufferedWriter writer;

	public static void main(String[] args) {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			writer = new BufferedWriter(new FileWriter("analizator/definicije.txt", true));
					
			procitajRegularneDefinicije(reader);
			
//			for(Map.Entry<String, String> entry : regexi.entrySet()) {
//				System.out.println(entry.getKey() + " = " + entry.getValue());
//			}
			
			procitajStanjaAnalizatora(reader);
			procitajTokene(reader);
			
			//ispis pocetnog stanja
			writer.append(stanjaLAnalizatora.get(0) + "\n");
			
			refaktorirajPravila(reader);
			
			reader.close();
			writer.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * Procita sve definicije i poslozoi ih u mapu, pritom micuci sve reference
	 */
	private static void procitajRegularneDefinicije(BufferedReader reader) throws Exception {
		String inputString = "";
		while(true) {
			reader.mark(10000);
			inputString = reader.readLine();
			if(inputString.startsWith("%X")) {
				reader.reset();
				break;
			}
			String[] regexDijelovi = inputString.split(Pattern.quote(" "));
			String regexIme = regexDijelovi[0];
			String regexString = regexDijelovi[1];
						
			regexString = izbaciSveReference(regexString);

			regexi.put(regexIme, regexString);
		}
	}

	private static String izbaciSveReference(String regexString) {
		Set<String> imenaRegexa = regexi.keySet();
		for(String imeRegexa : imenaRegexa) {
			if(regexString.contains(imeRegexa)) {
				regexString = regexString.replace(imeRegexa, "(" + regexi.get(imeRegexa) + ")");
			}
		}
		return regexString;
	}
	
	private static void procitajStanjaAnalizatora(BufferedReader reader) throws Exception {
		String inputString = reader.readLine();
		
		for(String stanje : inputString.split(Pattern.quote(" "))) {
			if(stanje.startsWith("S_")) {
				stanjaLAnalizatora.add(stanje);
			}
		}
	}

	private static void procitajTokene(BufferedReader reader) throws IOException {
		String inputString = reader.readLine();
		
		for(String token : inputString.split(Pattern.quote(" "))) {
			if(!token.startsWith("%L")) {
				tokeni.add(token);
			}
		}
	}
	
	private static void refaktorirajPravila(BufferedReader reader) throws IOException {
		RegexParser regexParser = new RegexParser();
		String stanjeAnalizatora = "";
		String regex = "";
		StringBuilder akcije = new StringBuilder();
		
		for(String inputString = reader.readLine(); inputString != null; inputString = reader.readLine()) {
			//odredi stanje analizatora i regex string
			if(inputString.startsWith("<")) {
				int pocetakRegexa = inputString.indexOf(">") + 1;
				stanjeAnalizatora = inputString.substring(1, pocetakRegexa-1);
				regex = izbaciSveReference(inputString.substring(pocetakRegexa));
			//ne radi nista
			} else if (inputString.startsWith("{")) {
				continue;
			//kraj pravila : ispisi stanje, prijelaze automata i akcije
			} else if(inputString.startsWith("}")) {
				regexParser.pretvori(regex);
				ispisiPravilo(stanjeAnalizatora, regexParser.getPrijelazi(), regexParser.getEpsilonPrijelazi(), akcije);
				regexParser.reset();
				akcije = new StringBuilder();
			//dodaj akcije
			} else {
				if(inputString.equals("-")) inputString = "ODBACI";
				akcije.append(inputString + ", ");
			}
		}
	}

	private static void ispisiPravilo(String stanjeAnalizatora, String prijelazi, String epsilonPrijelazi, StringBuilder akcije) throws IOException {
		writer.append(stanjeAnalizatora
				+ "\t" + prijelazi
				+ "\t" + epsilonPrijelazi
				+ "\t" + akcije.toString().substring(0, akcije.length()-2) + "\n"); // -2 zbog micanja zareza i razmaka
	}
}
