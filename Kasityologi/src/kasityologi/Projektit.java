package kasityologi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import fi.jyu.mit.ohj2.WildChars;

/**
 * - pitää yllä varsinaista käsityölogia, eli osaa lisätä ja poistaa projektin                      
 * - lukee ja kirjoittaa projektit tiedostoon         
 * - osaa etsiä ja lajitella projekteja 
 * @author Reetta Koskelo, reetta.m.koskelo@student.jyu.fi
 * @version Feb 25, 2020
 *
 */
public class Projektit implements Iterable<Projekti> {

    private int lkm = 0;
    private final String tiedostonPerusNimi = "\\tiedot";
    private Projekti[] alkiot;
    private boolean muutettu = false;

    /**
     * Oletusmuodostaja
     */
    public Projektit() {
        alkiot = new Projekti[5];
    }
    


    /**
     * Lisää uuden projektin tietorakenteeseen.  Ottaa projektin omistukseensa.
     * @param projekti Lisättävän projektin viite.  Huom tietorakenne muuttuu omistajaksi
     * @example
     * <pre name="test">
     * #THROWS SailoException 
     * Projektit projektit = new Projektit();
     * Projekti sukka1 = new Projekti(), sukka2 = new Projekti();
     * projektit.getLkm() === 0;
     * projektit.lisaa(sukka1); projektit.getLkm() === 1;
     * projektit.lisaa(sukka2); projektit.getLkm() === 2;
     * projektit.lisaa(sukka1); projektit.getLkm() === 3;
     * projektit.anna(0) === sukka1;
     * projektit.anna(1) === sukka2;
     * projektit.anna(2) === sukka1;
     * projektit.anna(1) == sukka1 === false;
     * projektit.anna(1) == sukka2 === true;
     * projektit.anna(3) === sukka1; #THROWS IndexOutOfBoundsException 
     * projektit.lisaa(sukka1); projektit.getLkm() === 4;
     * projektit.lisaa(sukka1); projektit.getLkm() === 5;
     * projektit.lisaa(sukka1);  
     * </pre>
     */
    public void lisaa(Projekti projekti) {
        if (lkm >= alkiot.length) {
            Projekti[] uusiTaulukko = new Projekti[2 * alkiot.length];
            for (int i = 0; i < lkm; i++) {
                uusiTaulukko[i] = (this.get(i));
            }
            alkiot = uusiTaulukko;
        }
        alkiot[lkm++] = projekti;
        muutettu = true;
    }
    
    /** 
     * Korvaa projektin tietorakenteessa. Ottaa projektin omistukseensa. 
     * Etsitään samalla id:llä oleva projekti. Jos ei löydy, 
     * niin lisätään uutena projektina. 
     * @param projekti lisättävän projektin viite. Huom tietorakenne muuttuu omistajaksi 
     * @throws SailoException jos tietorakenne on jo täynnä 
     */ 
    public void korvaaTaiLisaa(Projekti projekti) throws SailoException {
        int id = projekti.getProjektiId();
        for (int i = 0; i < lkm; i++) {
            if ( alkiot[i].getProjektiId() == id ) {
                alkiot[i] = projekti;
                muutettu = true;
                return;
            }
        }
        lisaa(projekti);
    }


    /**
     * Paikassa i olevan alkion viite
     * @param i mistä paikasta
     * @return paikassa i oleva viite
     * @throws IndexOutOfBoundsException jos indeksi väärin
     */
    public Projekti get(int i) throws IndexOutOfBoundsException {
        if ((i < 0) || (lkm <= i))
            throw new IndexOutOfBoundsException("i = " + i);
        return alkiot[i];
    }


    /**
     * Palauttaa viitteen i:nnenteen projektiin.
     * @param i monennenko projektin viite halutaan
     * @return viite projektiin, jonka indeksi on i
     * @throws IndexOutOfBoundsException jos i ei ole sallitulla alueella  
     */
    public Projekti anna(int i) throws IndexOutOfBoundsException {
        if (i < 0 || lkm <= i)
            throw new IndexOutOfBoundsException("Laiton indeksi: " + i);
        return alkiot[i];
    }
    
    /** 
     * Poistaa projektin, jolla on valittu id
     * @param id poistettavan projektin id
     * @return 1 jos poistettiin, 0 jos ei löydy 
     */ 
    public int poista(int id) { 
        int ind = etsiId(id); 
        if (ind < 0) return 0; 
        lkm--; 
        for (int i = ind; i < lkm; i++) 
            alkiot[i] = alkiot[i + 1]; 
        alkiot[lkm] = null; 
        muutettu = true; 
        return 1; 
    } 
    
    /** 
     * Etsii projektin id:n perusteella 
     * @param id id, jonka mukaan etsitään 
     * @return löytyneen projektin indeksi tai -1 jos ei löydy 
     */ 
    public int etsiId(int id) { 
        for (int i = 0; i < lkm; i++) 
            if (id == alkiot[i].getProjektiId()) return i; 
        return -1; 
    } 




    /**
     * Palauttaa käsityölogin projektien lukumäärän
     * @return projektien lukumäärä
     */
    public int getLkm() {
        return lkm;
    }


    /**
     * Tallennetaan projektitiedosto
     * @param hakemisto Hakemisto jossa ollaan
     * @throws SailoException jos joku menee pieleen
     */
    public void tallenna(String hakemisto) throws SailoException {
        if (!muutettu)
            return;

        File ftied = new File(hakemisto + getDatNimi());
        try (PrintWriter fo = new PrintWriter(
                new FileWriter(ftied.getCanonicalPath()))) {
            fo.println("Himoneulojan neuleet");
            for (int i = 0; i < getLkm(); i++) {
                Projekti projekti = anna(i);
                fo.println(projekti.toString());
            }
        } catch (FileNotFoundException ex) {
            throw new SailoException(
                    "Tiedosto " + ftied.getName() + " ei aukea");
        } catch (IOException ex) {
            throw new SailoException("Tiedoston " + ftied.getName()
                    + " kirjoittamisessa ongelmia");
        }

    }


    private String getDatNimi() {
        return tiedostonPerusNimi + ".dat";
    }


    /**
     * Luetaan projektitiedosto
     * @param hakemisto Tiedosto jota luetaan
     * @throws SailoException jos vikaa
     */
    public void lueTiedostosta(String hakemisto) throws SailoException {
        File ftied = new File(hakemisto + tiedostonPerusNimi + ".dat");
        try (BufferedReader fi = new BufferedReader(
                new FileReader(hakemisto + tiedostonPerusNimi + ".dat"))) {
            String kokoNimi = fi.readLine();
            if (kokoNimi == null)
                throw new SailoException("Käsityölogin nimi puuttuu");
            String rivi;
            while (true) {
                rivi = fi.readLine();
                if (rivi == null)
                    break;
                rivi = rivi.trim();
                if ("".equals(rivi) || rivi.charAt(0) == ';')
                    continue;
                Projekti projekti = new Projekti();
                projekti.parse(rivi);
                lisaa(projekti);
            }
        } catch (FileNotFoundException e) {
            throw new SailoException(
                    "Tiedosto " + ftied.getName() + " ei aukea");
        } catch (IOException e) {
            throw new SailoException(
                    "Tiedosto " + ftied.getName() + " ei aukea");
        }

    }
    
    /**
     * @author Reetta Koskelo
     * @version Apr 18, 2020
     * Luokka Projektien iteroimiseksi
     */
    public class ProjektitIterator implements Iterator<Projekti> {
        private int kohdalla = 0;


        /**
         * Onko olemassa vielä seuraavaa projektia
         * @see java.util.Iterator#hasNext()
         * @return true jos on vielä projekteja
         */
        @Override
        public boolean hasNext() {
            return kohdalla < getLkm();
        }


        /**
         * Annetaan seuraava projekti
         * @return seuraava projekti
         * @throws NoSuchElementException jos seuraava alkiota ei enää ole
         * @see java.util.Iterator#next()
         */
        @Override
        public Projekti next() throws NoSuchElementException {
            if ( !hasNext() ) throw new NoSuchElementException("Ei oo");
            return anna(kohdalla++);
        }


        /**
         * Tuhoamista ei ole toteutettu
         * @throws UnsupportedOperationException aina
         * @see java.util.Iterator#remove()
         */
        @Override
        public void remove() throws UnsupportedOperationException {
            throw new UnsupportedOperationException("Me ei poisteta");
        }
    }
    
    /**
     * Palautetaan iteraattori projekteihin.
     * @return jäsen iteraattori
     */
    @Override
    public Iterator<Projekti> iterator() {
        return new ProjektitIterator();
    }
    
    /**
     * Palauttaa "taulukossa" hakuehtoon vastaavien projektien viitteet 
     * @param hakuehto hakuehto 
     * @param k etsittävän kentän indeksi  
     * @return tietorakenteen löytyneistä projekteista 
     */
    public Collection<Projekti> etsi(String hakuehto, int k) { 
        String ehto = "*"; 
        if ( hakuehto != null && hakuehto.length() > 0 ) ehto = hakuehto; 
        int hk = k; 
        if ( hk < 0 ) hk = 0; // jotta etsii id:n mukaan 
        List<Projekti> loytyneet = new ArrayList<Projekti>(); 
        for (Projekti projekti : this) { 
            if (WildChars.onkoSamat(projekti.anna(hk), ehto)) loytyneet.add(projekti);   
        } 
        Collections.sort(loytyneet, new Projekti.Vertailija(hk)); 
        return loytyneet; 
    }





    /**
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        Projektit Projektit = new Projektit();
        Projekti sukka = new Projekti(), sukka2 = new Projekti();
        sukka.rekisteroi();
        sukka.taytaVillasukkaTiedoilla();
        sukka2.rekisteroi();
        sukka2.taytaVillasukkaTiedoilla();

        Projektit.lisaa(sukka);
        Projektit.lisaa(sukka2);

        System.out.println("============= Projektit-testi =================");

        for (int i = 0; i < Projektit.getLkm(); i++) {
            Projekti Projekti = Projektit.anna(i);
            System.out.println("Projekti nro: " + i);
            Projekti.tulosta(System.out);
        }

    }



}
