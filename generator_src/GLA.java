import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class GLA {

	private static Map<String, String> regexi = new HashMap<>(); 	
	private static List<String> stanjaLAnalizatora = new LinkedList<>();
	private static List<String> tokeni = new LinkedList<>();
	
	public static void main(String[] args) {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
					
			procitajRegularneDefinicije(reader);
			procitajStanjaAnalizatora(reader);
			procitajTokene(reader);
			
			//ispis pocetnog stanja
			System.out.println(stanjaLAnalizatora.get(0));
			
			//pravila
			RegexParser regexParser = new RegexParser();
			String stanjeAnalizatora = "";
			String regex = "";
			StringBuilder akcije = new StringBuilder();
			for(String inputString = reader.readLine(); inputString != null; inputString = reader.readLine()) {
				//odredi stanje analizatora i regex string
				if(inputString.startsWith("<")) {
					int pocetakRegexa = inputString.indexOf(">") + 1;
					stanjeAnalizatora = inputString.substring(1, pocetakRegexa-1);
					regex = izbaciSveReference(inputString).substring(pocetakRegexa);
				//ne radi nista
				} else if (inputString.startsWith("{")) {
					continue;
				//kraj pravila : ispisi stanje, prijelaze automata i akcije
				} else if(inputString.startsWith("}")) {
					regexParser.pretvori(regex);
					System.out.println(stanjeAnalizatora
							+ "--" + regexParser.getPrijelazi()
							+ "--" + regexParser.getEpsilonPrijelazi()
							+ "--" + akcije.toString().substring(0, akcije.length()-2)); // -2 zbog micanja zareza i razmaka
					regexParser.reset();
					akcije = new StringBuilder();
				//dodaj akcije
				} else {
					if(inputString.equals("-")) inputString = "ODBACI";
					akcije.append(inputString + ", ");
				}
			}
			
			reader.close();			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * Procita sve definicije i poslozoi ih u mapz, pritom micuci sve reference
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
		int proslaVitica = -1;
		while(proslaVitica < regexString.length()) {
			int startReference = regexString.indexOf("{", proslaVitica+1);
			if(startReference == -1) {
				break; 		// nema niti jedna vitica -> nema referenci
			}
			proslaVitica = startReference;
			if(startReference > 0) {
				if(regexString.charAt(startReference-1) == '\\') {	//preskoci ako je vitica prefiksirana
					continue;
				}
			}
			
			int endReference = regexString.indexOf("}", startReference);

			String referencedRegex = regexString.substring(startReference, endReference+1);
			regexString = regexString.replace(referencedRegex, "(" + regexi.get(referencedRegex) + ")");
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
}
