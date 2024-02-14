package kasityologi;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Comparator;

import fi.jyu.mit.ohj2.Mjonot;

/**
 * - tietää työvälineiden kentät                      
 * - osaa tarkistaa tietyn kentän oikeellisuuden      
 * - osaa muuttaa 1|virkkuukoukku nro 3,5 -merkkijonon työvälineen tiedoiksi                            
 * - osaa antaa merkkijonona i:nnen kentän tiedot     
 * - osaa laittaa merkkijonon i:nneksi kentäksi
 * @author Reetta Koskelo, reetta.m.koskelo@student.jyu.fi
 * @version Feb 25, 2020
 *
 */
public class Tyovaline {
    private int projektiId;
    private String nimi;
    
    /** 
     * Työvälineiden vertailija 
     */ 
    public static class Vertailija implements Comparator<Tyovaline> { 
        private int k;  
         
        @SuppressWarnings("javadoc") 
        public Vertailija(int k) { 
            this.k = k; 
        } 
         
        @Override 
        public int compare(Tyovaline valine1, Tyovaline valine2) { 
            return valine1.getAvain(k).compareToIgnoreCase(valine2.getAvain(k)); 
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
        default: return "Äääliö"; 
        } 
    } 

    /**
     * Oletusmuodostaja
     */
    public Tyovaline() {
        // Ei vielä tarvita
    }


    /**
     * Muodostaja työvälineelle
     * @param projektiId Projekti, johon työväline liittyy
     */
    public Tyovaline(int projektiId) {
        this.projektiId = projektiId;
    }


    /**
     * Apumetodi, jolla saadaan täytettyä testiarvot työvälineelle.
     * Nimi arvotaan 10 työvälineen taulukosta, jotta kahdella harrastuksella ei olisi samoja tietoja.
     * @param nro viite projektiin, johon liittyvästä työvälineestä on kyse
     */
    public void taytaTyovalineTiedoilla(int nro) {
        String[] valineet = { "virkkuukoukku nro 3,5", "pienet sakset",
                "tylppä neula", "kierrepuikko nro 4", "pienet sakset",
                "terävä neula", "ompelukone", "Jersey-neula", "kangassakset",
                "sukkapuikko nro 3,5" };
        projektiId = nro;
        nimi = valineet[Projekti.rand(0, 9)];
    }


    /**
     * Tulostetaan työvälineen tiedot
     * @param out tietovirta johon tulostetaan
     */
    public void tulosta(PrintStream out) {
        out.println(projektiId + " " + nimi);
    }


    /**
     * Tulostetaan projektin tiedot
     * @param os tietovirta johon tulostetaan
     */
    public void tulosta(OutputStream os) {
        tulosta(new PrintStream(os));
    }


    /**
     * Palautetaan mihin projektiin tyovaline kuuluu
     * @return projektin id
     */
    public int getProjektiId() {
        return projektiId;
    }


    /**
     * Työvälineen nimen getter
     * @return Työvälineen nimi
     */
    public String getNimi() {
        return nimi;
    }


    /**
     * Asettaa työvälineelle nimen
     * @param s asetettava nimi
     * @return null
     */
    public String setNimi(String s) {
        nimi = s;
        return null;
    }


    /**
     * Palauttaa työvälineen tiedot merkkijonona, jonka voi tallentaa tiedostoon.
     * @return työväline tolppaeroteltuna merkkijonona 
     * @example
     * <pre name="test">
     *   Tyovaline tyovaline = new Tyovaline();
     *   tyovaline.parse("   2   |  virkkuukoukku  ");
     *   tyovaline.stringiksi()    === "2|virkkuukoukku";
     * </pre>
     */
    @Override
    public String toString() {
        return nimi;
    }


    /**
     * Apumetodi jotta saadaan toString toimimaan nätisti käyttöliittymässä
     * @return työvälineen nimi
     */
    public String stringiksi() {
        return "" + getProjektiId() + "|" + nimi;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        return this.stringiksi().equals(((Tyovaline) obj).stringiksi());
    }


    @Override
    public int hashCode() {
        return projektiId;
    }


    /**
     * Selvitää työvälineen tiedot merkillä | erotellusta merkkijonosta.
     * @param rivi josta harrastuksen tiedot otetaan
     * @example
     * <pre name="test">
     *   Tyovaline tyovaline = new Tyovaline();
     *   tyovaline.parse("   2   |  virkkuukoukku  ");
     *   tyovaline.getProjektiId() === 2;
     *   tyovaline.stringiksi()    === "2|virkkuukoukku";
     * </pre>
     */
    public void parse(String rivi) {
        StringBuffer sb = new StringBuffer(rivi);
        projektiId = Mjonot.erota(sb, '|', getProjektiId());
        nimi = Mjonot.erota(sb, '|', nimi);
    }


    /**
     * Testiohjelma Tyovalineelle.
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        Tyovaline val = new Tyovaline();
        val.taytaTyovalineTiedoilla(2);
        val.tulosta(System.out);
    }

}
