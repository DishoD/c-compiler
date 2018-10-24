import java.util.LinkedList;
import java.util.List;

/**
 * Pretipkani pseudokod koji su nam dali za parsiranje regexa. 
 * 0 je uvijek pocetno stanje, 1 je uvijek prihvatljivo stanje
 * @author ivana
 *
 */
public class RegexParser {
	
	private int sekvencaStanja;
	private StringBuilder prijelazi;
	private StringBuilder epsilonPrijelazi;
	
	public RegexParser() {
		sekvencaStanja = 0;
		prijelazi = new StringBuilder();
		epsilonPrijelazi = new StringBuilder();
	}
	
	/**
	 * Vraca parser u pocetno stanje
	 */
	public void reset() {
		sekvencaStanja = 0;
		prijelazi = new StringBuilder();
		epsilonPrijelazi = new StringBuilder();
	}
	
	private int novoStanje() {
		return sekvencaStanja++;
	}

	private boolean jeOperator(String izraz, int indexOperatora) {
		int brojBackslasha = 0;
		while(indexOperatora-1 >= 0 ) {
			if(izraz.charAt(indexOperatora-1) == '\\') {
				brojBackslasha++;
				indexOperatora--;
			} else {
				break;
			}
		}
		return brojBackslasha%2 == 0 ? true : false;
	}
	
	/**
	 * parsira regex i stvara prijelaze
	 */
	public ParStanja pretvori(String izraz) {
		List<String> izbori = new LinkedList<>();
		int brojZagrada = 0;
		
		int pocetak = 0;		
		for(int i = 0; i < izraz.length(); i++) {
			if(izraz.charAt(i) == '(' & jeOperator(izraz, i)) {
				brojZagrada++;
			} else if(izraz.charAt(i) == ')' & jeOperator(izraz, i)) {
				brojZagrada--;
			} else if(brojZagrada == 0 & izraz.charAt(i) == '|' & jeOperator(izraz, i)) {
				izbori.add(izraz.substring(pocetak, i));
				pocetak = i+1;
			}
		}
		if(izbori.size()>0) {
			izbori.add(izraz.substring(pocetak, izraz.length()));
		}
		
		int lijevoStanje = novoStanje();
		int desnoStanje = novoStanje();
		
		if(izbori.size() > 0) {
			for(String izbor : izbori) {
				ParStanja privremeno = pretvori(izbor);
				dodajEpsilonPrijelaz(lijevoStanje, privremeno.lijevoStanje);
				dodajEpsilonPrijelaz(privremeno.desnoStanje, desnoStanje);
			}
			
		} else {
			boolean prefiksirano = false;
			int zadnjeStanje = lijevoStanje;
			for(int i = 0; i < izraz.length(); i++) {
				int a;
				int b;
				if(prefiksirano) {
					prefiksirano = false;
					char prijelazniZnak;
					switch (izraz.charAt(i)) {
						case 't':
							prijelazniZnak = '\t';
							break;
						case 'n':
							prijelazniZnak = '\n';
							break;
						case '_':
							prijelazniZnak = ' ';
							break;
						default:
							prijelazniZnak = izraz.charAt(i);
							break;
					}
					a = novoStanje();
					b = novoStanje();
					dodajPrijelaz(a, b, prijelazniZnak);
				} else {
					if(izraz.charAt(i) == '\\') {
						prefiksirano = true;
						continue;
					}
					if(izraz.charAt(i) != '(') {
						a = novoStanje();
						b = novoStanje();
						if(izraz.charAt(i) == '$') {
							dodajEpsilonPrijelaz(a, b);
						} else {
							dodajPrijelaz(a, b, izraz.charAt(i));
						}
					} else {
						int j = pronađiOdgovarajucuZagradu(izraz, i);
						ParStanja privremeno = pretvori(izraz.substring(i+1, j));
						a = privremeno.lijevoStanje;
						b = privremeno.desnoStanje;
						i = j;
					}
				}
				
				if(i+1 < izraz.length() ) {
					if(izraz.charAt(i+1) == '*') {
						int x = a;
						int y = b;
						a = novoStanje();
						b = novoStanje();
						dodajEpsilonPrijelaz(a, x);
						dodajEpsilonPrijelaz(y, b);
						dodajEpsilonPrijelaz(a, b);
						dodajEpsilonPrijelaz(y, x);
						i++;
					}
				}
				
				dodajEpsilonPrijelaz(zadnjeStanje, a);
				zadnjeStanje = b;
			}
			dodajEpsilonPrijelaz(zadnjeStanje, desnoStanje);
		}
		
		return new ParStanja(lijevoStanje, desnoStanje);
	}
	
	/**
	 * trazi zatvorenu zagradu koja odgovara otvorenoj zagradi (koja se nalazi na indexu i unutar izlaza)
	 */
	private int pronađiOdgovarajucuZagradu(String izraz, int i) {
		int brojacZagrada = 1;	//pocetna vrjednost je 1 jer je na mjestu i otvorena zagrada 
		for(int j = i+1; j < izraz.length(); j++) {
			if(izraz.charAt(j) == '(' & izraz.charAt(j-1) != '\\') { 	//ukoliko nađe neprefiksiranu otvorenu 
				brojacZagrada++;										//zagradu, povecaj brojac
			} else if (izraz.charAt(j) == ')' & izraz.charAt(j-1) != '\\') {	//ukoliko nađe neprefiksiranu
				brojacZagrada--; 												//zatvorenu zagradu, smanji brojac
				if(brojacZagrada == 0) { //pronađena odgovarajuca zagrada
					return j;
				}
			}
		}
		return izraz.length()-1; //ako su im datoteke dobro formatirane, nece se nikad dogoditi
	}

	private void dodajPrijelaz(int izvor, int odrediste, char prijelazniZnak) {
		String oznakaPrijelaznogZnaka;
		switch (prijelazniZnak) {
		case '\t':
			oznakaPrijelaznogZnaka = "\\t";
			break;
		case '\n':
			oznakaPrijelaznogZnaka = "\\n";
			break;
		case ' ':
			oznakaPrijelaznogZnaka = "\\_";
			break;
		default:
			oznakaPrijelaznogZnaka = "" + prijelazniZnak;
			break;
		}
		prijelazi.append("" + izvor + " " + odrediste + " " + oznakaPrijelaznogZnaka + " ");
		
	}

	private void dodajEpsilonPrijelaz(int izvor, int odrediste) {
		epsilonPrijelazi.append("" + izvor + " " + odrediste + " ");
	}
	
	public String getPrijelazi() {
		return prijelazi.toString().trim();
	}
	
	public String getEpsilonPrijelazi() {
		return epsilonPrijelazi.toString().trim();
	}
}
