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
public class Materiaali {
    private int projektiId;
    private String nimi;
    private String vari;
    
    /** 
     * Materiaalien vertailija 
     */ 
    public static class Vertailija implements Comparator<Materiaali> { 
        private int k;  
         
        @SuppressWarnings("javadoc") 
        public Vertailija(int k) { 
            this.k = k; 
        } 
         
        @Override 
        public int compare(Materiaali valine1, Materiaali valine2) { 
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
        case 2: return "" + vari.toUpperCase();
        default: return "Äääliö"; 
        } 
    } 
    
    
    /**
     * Oletusmuodostaja
     */
    public Materiaali() {
        // Ei vielä tarvita
    }
    
    /**
     * Muodostaja materiaalille
     * @param projektiId Projekti, johon materiaali liittyy
     */
    public Materiaali(int projektiId) {
        this.projektiId = projektiId; 
    }
    

    
    
    
    
    /**
     * Apumetodi, jolla saadaan täytettyä testiarvot materiaalille.
     * Nimi arvotaan 10 työvälineen taulukosta, jotta kahdella harrastuksella ei olisi samoja tietoja.
     * Arvotaan samalla tavalla väri.
     * @param nro viite projektiin, johon liittyvästä työvälineestä on kyse
     */
    public void taytaMateriaaliTiedoilla(int nro) {
        String[] materiaalit = {"villalanka", "puuvillalanka", "ompelulanka", "trikoo", "sametti", "denim", "resori",
                "pellavalanka", "nappi", "vetoketju"};
        String[] varit = {"sininen", "punainen", "vihreä", "musta", "keltainen", "oranssi", "harmaa",
                "valkoinen", "pinkki", "ruskea"};
        projektiId = nro;
        nimi = materiaalit[Projekti.rand(0, 9)];
        vari = varit[Projekti.rand(0, 9)];
    }
    
    
    /**
     * Tulostetaan materiaalin tiedot
     * @param out tietovirta johon tulostetaan
     */
    public void tulosta(PrintStream out) {
        out.println(projektiId + " " + nimi + " - " + vari);
    }


    /**
     * Tulostetaan projektin tiedot
     * @param os tietovirta johon tulostetaan
     */
    public void tulosta(OutputStream os) {
        tulosta(new PrintStream(os));
    }



    /**
     * Palautetaan mihin projektiin materiaali kuuluu
     * @return projektin id
     */
    public int getProjektiId() {
        return projektiId;
    }
    
    /**
     * Nimen getteri
     * @return magteriaalin nimi
     */
    public String getNimi() {
        return nimi;
    }
    
    /**
     * Värin getteri
     * @return materiaalin väri
     */
    public String getVari() {
        return vari;
    }

    
    /**
     * Asettaa materiaalille nimen
     * @param s asetettava nimi
     * @return null
     */
    public String setNimi(String s) {
        nimi = s;
        return null;
    }
    
    /**
     * Asettaa materiaalille nimen
     * @param s asetettava väri
     * @return virheilmoitus, jos s on null
     */
    public String setVari(String s) {
        if (s.equals("")) return "Materiaalin väri ei saa olla tyhjä!";
        vari = s;
        return null;
    }
    
    /**
     * Palauttaa materiaalin tiedot merkkijonona, jonka voi tallentaa tiedostoon.
     * @return ateriaali tolppaeroteltuna merkkijonona 
     * @example
     * <pre name="test">
     *   Materiaali materiaali = new Materiaali();
     *   materiaali.parse("   2   |  villalanka  |   musta    ");
     *   materiaali.stringiksi()    === "2|villalanka|musta";
     * </pre>
     */
    @Override
    public String toString() {
        return nimi + " - " + vari;
    }
    
    @Override
    public boolean equals(Object obj) {
        if ( obj == null ) return false;
        return this.stringiksi().equals(((Materiaali) obj).stringiksi());
    }


    @Override
    public int hashCode() {
        return projektiId;
    }
    
    /**
     * toStringin uusi muoto tiedostoon tallentamista varten
     * @return materiaalin tiedot |-merkillä erotettuna
     */
    public String stringiksi() {
        return "" + getProjektiId() + "|" + nimi + "|" + vari;
    }



    /**
     * Selvitää työvälineen tiedot merkillä | erotellusta merkkijonosta.
     * @param rivi josta harrastuksen tiedot otetaan
     * @example
     * <pre name="test">
     *   Materiaali materiaali = new Materiaali();
     *   materiaali.parse("   2   |  villalanka  |   musta    ");
     *   materiaali.getProjektiId() === 2;
     *   materiaali.stringiksi()    === "2|villalanka|musta";
     * </pre>
     */
    public void parse(String rivi) {
        StringBuffer sb = new StringBuffer(rivi);
        projektiId = Mjonot.erota(sb, '|', getProjektiId());
        nimi = Mjonot.erota(sb, '|', nimi);
        vari = Mjonot.erota(sb, '|', vari);
    }



    /**
     * Testiohjelma Materiaalille.
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        Materiaali mat = new Materiaali();
        mat.taytaMateriaaliTiedoilla(2);
        mat.tulosta(System.out);
    }


    

     
}
