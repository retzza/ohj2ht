package kasityologi;

import java.io.OutputStream;
import java.io.PrintStream;
import java.time.LocalDate;
import java.util.Comparator;

import fi.jyu.mit.ohj2.Mjonot;

/**
 * - tietää projektin kentät (nimi, tyyppi, kenelle, jne.) ja tarkistaa tietyn kentän oikeellisuuden
 * - osaa muuttaa 1|musta pipo|virkkaus|itselle|1.1.1253| |20| - merkkijonon projektin tiedoiksi
 * - osaa antaa merkkijonona i:nnen kentän tiedot 
 * - osaa laittaa merkkijonon i:nneksi kentäksi
 * 
 * @author Reetta Koskelo, reetta.m.koskelo@student.jyu.fi
 * @version Feb 24, 2020
 *
 */
public class Projekti implements Cloneable {
    
    private int             projektiId;
    private String          nimi            = "";
    private String          tyyppi          = "";
    private String          kenelle         = "";
    private LocalDate       aloituspaiva    = LocalDate.now(); // TODO: kuluva päivä oletukseksi
    private LocalDate       lopetuspaiva    = LocalDate.now(); 
    private double          valmiusaste     = 0;
    
    private static int      seuraavaId      = 1;
    
    
    /** 
     * Projektien vertailija 
     */ 
    public static class Vertailija implements Comparator<Projekti> { 
        private int k;  
         
        @SuppressWarnings("javadoc") 
        public Vertailija(int k) { 
            this.k = k; 
        } 
         
        @Override 
        public int compare(Projekti projekti1, Projekti projekti2) { 
            return projekti1.getAvain(k).compareToIgnoreCase(projekti2.getAvain(k)); 
        } 
    } 
    
    /** 
     * Antaa k:n kentän sisällön merkkijonona 
     * @param k monenenko kentän sisältö palautetaan 
     * @return kentän sisältö merkkijonona 
     */ 
    public String getAvain(int k) { 
        switch ( k ) { 
        case 0: return "" + projektiId; 
        case 1: return "" + nimi.toUpperCase(); 
        case 2: return "" + tyyppi;
        case 3: return "" + kenelle; 
        case 4: return "" + aloituspaiva; 
        case 5: return "" + lopetuspaiva; 
        case 6: return "" + String.format("%04", valmiusaste); 
        default: return "Äääliö"; 
        } 
    } 


    
    /**
     * Palauttaa jäsenen kenttien lukumäärän
     * @return kenttien lukumäärä
     */
    public int getKenttia() {
        return 7;
    }

    
    /**
     * Eka kenttä joka on mielekäs kysyttäväksi
     * @return eknn kentän indeksi
     */
    public int ekaKentta() {
        return 1;
    }


    
    /**
     * Asettaa tunnusnumeron ja samalla varmistaa että
     * seuraava numero on aina suurempi kuin tähän mennessä suurin.
     * @param nr asetettava tunnusnumero
     */
    private void setProjektiId(int nr) {
        projektiId = nr;
        if (seuraavaId >= seuraavaId) seuraavaId = projektiId + 1;
    }

    
    
    /**
     * Oletusmuodostaja
     */
    public Projekti() {
        // ei tarvihe
    }
    
    /**
     * Apumetodi, jolla arvotaan satunnainen kokonaisluku välille [ala,yla]
     * TODO: poista kun valmista!
     * @param ala arvonnan alaraja
     * @param yla arvonnan yläraja
     * @return satunnainen luku väliltä [ala,yla]
     */
    public static int rand(int ala, int yla) {
      double n = (yla-ala)*Math.random() + ala;
      return (int)Math.round(n);
    }

    /**
     * Palauttaa k:tta projektin kenttää vastaavan kysymyksen
     * @param k kuinka monennen kentän kysymys palautetaan (0-alkuinen)
     * @return k:netta kenttää vastaava kysymys
     */
    public String getKysymys(int k) {
        switch ( k ) {
        case 0: return "projektin ID";
        case 1: return "nimi";
        case 2: return "tyyppi";
        case 3: return "kenelle";
        case 4: return "aloituspäivä";
        case 5: return "lopetuspäivä";
        case 6: return "valmiusaste";
        default: return "Ääliö";
        }
    }

    
    /**
     * Apumetodi, jolla saadaan täytettyä testiarvot projektille.
     * TODO: poista kun valmista!
     */
    public void taytaVillasukkaTiedoilla() {
        nimi = "villasukka " + rand(1000, 9999);
        tyyppi = "neulonta";
        kenelle = "itselle";
        valmiusaste = rand(0, 100);
    }
 

    

    /**
     * Tulostetaan projektin tiedot
     * @param os tietovirta johon tulostetaan
     */
    public void tulosta(OutputStream os) {
        tulosta(new PrintStream(os));
    }

    
    /**
     * Antaa projektille seuraavan projekti-id:n.
     * @return Projektin uusi id
     * @example
     * <pre name="test">
     *   Projekti sukka1 = new Projekti();
     *   sukka1.rekisteroi();
     *   Projekti sukka2 = new Projekti();
     *   sukka2.rekisteroi();
     *   int n1 = sukka1.getProjektiId();
     *   int n2 = sukka2.getProjektiId();
     *   n1 === n2-1;
     * </pre>
     */
    public int rekisteroi() {
        projektiId = seuraavaId;
        seuraavaId++;
        return projektiId;
    }

    /**
     * Hakee projektin id:n.
     * @return Projektin id
     */
    public int getProjektiId() {
        return projektiId;
    }
    
    /**
     * Hakee projektin nimen
     * @return Projektin nimen
     */
    public String getNimi() {
        return nimi;
    }
    

    /**
     * Hakee projektin tyypin
     * @return Projektin tyypin
     */
    public String getTyyppi() {
        return tyyppi;
    }
    
    /**
     * Hakee kenelle projekti on 
     * @return Kenelle projekti on
     */
    public String getKenelle() {
        return kenelle;
    }
    
    /**
     * Hakee projektin aloituspäivän
     * @return Projektin aloituspäivän
     */
    public LocalDate getAloituspv() {
        return aloituspaiva;
    }

    /**
     * Hakee projektin lopetuspäivän
     * @return Projektin lopetuspäivän
     */
    public LocalDate getLopetuspv() {
        return lopetuspaiva;
    }
    
    /**
     * Hakee projektin valmiusasteen
     * @return Projektin valmiusasteen
     */
    public double getValmiusaste() {
        return valmiusaste;
    }
    

    /**
     * Asettaa projektille nimen
     * @param s Asetettava nimi
     * @return Virheilmoitus, jos s on tyhjä
     */
    public String setNimi(String s) {
        if (s.equals("")) return "Nimi ei voi olla tyhjä!";
        nimi = s;
        return null;
    }


    /**
     * Asettaa projektille tyypin
     * @param s Asetettava tyyppi
     * @return null
     */
    public String setTyyppi(String s) {
        tyyppi = s;
        return null;
    }

    /**
     * Asettaa projektille tidoen kenelle se on 
     * @param s Asetettava kenelle 
     * @return null
     */
    public String setKenelle(String s) {
        kenelle = s;
        return null;
    }
    
    /**
     * Asettaa projektille aloituspäivän
     * @param l Asetettava aloituspäivä
     * @return null
     */
    public String setAloituspv(LocalDate l) {
        aloituspaiva = l;
        return null;
    }

    /**
     * Asettaa projektille lopetuspäivän
     * @param l Asetettava lopetuspäivä
     * @return null
     */
    public String setLopetuspv(LocalDate l) {
        lopetuspaiva = l;
        return null;
    }
    
    /**
     * Asettaa projektille valmisuasteen
     * @param d Asetettava valmiusaste
     * @return null
     */
    public String setValmiusaste(double d) {
        valmiusaste = d;
        return null;
    }
    
    /** 
     * Antaa k:nnen kentän sisällön merkkijonona 
     * @param k monennenko kentän sisältö palautetaan 
     * @return kentän sisältö merkkijonona 
     */ 
    public String anna(int k) {
        switch ( k ) {
        case 0: return "" + projektiId;
        case 1: return "" + nimi;
        case 2: return "" + tyyppi;
        case 3: return "" + kenelle;
        case 4: return "" + aloituspaiva;
        case 5: return "" + lopetuspaiva;
        case 6: return "" + valmiusaste;
        default: return "Äääliö";
        }
    }


    
    /**
     * Palauttaa projektin tiedot merkkijonona, jonka voi tallentaa tiedostoon.
     * @return projekti tolppaeroteltuna merkkijonona 
     * @example
     * <pre name="test">
     *   Projekti projekti = new Projekti();
     *   projekti.parse("   3  |  villasukka   | neulonta");
     *   projekti.toString().startsWith("3|villasukka|neulonta|") === true; // on enemmäkin kuin 3 kenttää, siksi loppu |
     * </pre>  
     */
    @Override
    public String toString() {
        return "" +
                getProjektiId() + "|" +
                nimi + "|" +
                tyyppi + "|" +
                kenelle + "|" +
                aloituspaiva + "|" +
                lopetuspaiva + "|" +
                valmiusaste;
    }
    
    /**
     * Selvitää projektin tiedot merkillä | erotellusta merkkijonosta
     * Pitää huolen että seuraavaId on suurempi kuin tuleva projektiId.
     * @param rivi josta jäsenen tiedot otetaan
     * 
     * @example
     * <pre name="test">
     *   Projekti projekti = new Projekti();
     *   projekti.parse("   3  |  villasukka   | neulonta");
     *   projekti.getProjektiId() === 3;
     *   projekti.toString().startsWith("3|villasukka|neulonta|") === true; // on enemmäkin kuin 3 kenttää, siksi loppu |
     *
     *   projekti.rekisteroi();
     *   int n = projekti.getProjektiId();
     *   projekti.parse(""+(n+20));       // Otetaan merkkijonosta vain id
     *   projekti.rekisteroi();           // ja tarkistetaan että seuraavalla kertaa tulee yhtä isompi
     *   projekti.getProjektiId() === n+20+1;
     *     
     * </pre>
     */
    public void parse(String rivi) {
        StringBuffer sb = new StringBuffer(rivi);
        setProjektiId(Mjonot.erota(sb, '|', getProjektiId()));
        nimi = Mjonot.erota(sb, '|', nimi);
        tyyppi = Mjonot.erota(sb, '|', tyyppi);
        kenelle = Mjonot.erota(sb, '|', kenelle);
        String aloituspv = Mjonot.erota(sb, '|', null);
        aloituspaiva = LocalDate.parse(aloituspv);
        String lopetuspv = Mjonot.erota(sb, '|', null);
        lopetuspaiva = LocalDate.parse(lopetuspv);
        valmiusaste = Mjonot.erota(sb, '|', valmiusaste);
    }

    /**
     * Tehdään identtinen klooni projektista
     * @return Object kloonattu projekti
     * @example
     * <pre name="test">
     * #THROWS CloneNotSupportedException 
     *   Projekti projekti = new Projekti();
     *   projekti.parse("   3  |  villasukka   | neulonta");
     *   Projekti kopio = projekti.clone();
     *   kopio.toString() === projekti.toString();
     *   projekti.parse("   4  |  lapanen   | virkkaus");
     *   kopio.toString().equals(projekti.toString()) === false;
     * </pre>
     */
    @Override
    public Projekti clone() throws CloneNotSupportedException {
        return (Projekti) super.clone();
    }


    
    /**
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        Projekti sukka = new Projekti();
        Projekti sukka2 = new Projekti();
        
        sukka.rekisteroi();
        sukka2.rekisteroi();

        sukka.tulosta(System.out);
        sukka.taytaVillasukkaTiedoilla(); 
        sukka.tulosta(System.out);
        
        sukka2.tulosta(System.out);
        sukka2.taytaVillasukkaTiedoilla(); 
        sukka2.tulosta(System.out);
    
    }




    
    


    
}
