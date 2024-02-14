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

/**
 * - pitää yllä varsinaista työvälinerekisteriä, eli osaa lisätä ja poistaa työvälineen               
 * - lukee ja kirjoittaa työvälineet tiedostoon       
 * - osaa etsiä ja lajitella työvälineitä   
 * @author Reetta Koskelo, reetta.m.koskelo@student.jyu.fi
 * @version Feb 25, 2020
 *
 */
public class Tyovalineet implements Iterable<Tyovaline> {

    private boolean muutettu = false;
    private final String tiedostonPerusNimi = "\\valineet";

    /** Taulukko työvälineistä */
    private final Collection<Tyovaline> alkiot = new ArrayList<Tyovaline>();

    /**
     * Tyovalineiden alustaminen
     */
    public Tyovalineet() {
        // toistaiseksi ei tarvitse tehdä mitään
    }


    /**
     * Lisää uuden työvälineen tietorakenteeseen. Ottaa työvalineen omistukseensa.
     * @param val lisättävä työväline. Huom tietorakenne muuttuu omistajaksi
     */
    public void lisaa(Tyovaline val) {
        alkiot.add(val);
        muutettu = true;
    }
    
    /**
     * Poistaa valitun työvälineen
     * @param valine poistettava työväline
     * @return tosi jos löytyi poistettava tietue 
     */
    public boolean poista(Tyovaline valine) {
        boolean ret = alkiot.remove(valine);
        if (ret) muutettu = true;
        return ret;
    }

    
    /**
     * Poistaa kaikki tietyn tietyn projektin työvälineet
     * @param id viite siihen, mihin liittyvät tietueet poistetaan
     * @return montako poistettiin 
     */
    public int poistaProjektinTyovalineet(int id) {
        int n = 0;
        for (Iterator<Tyovaline> it = alkiot.iterator(); it.hasNext();) {
            Tyovaline val = it.next();
            if ( val.getProjektiId() == id ) {
                it.remove();
                n++;
            }
        }
        if (n > 0) muutettu = true;
        return n;
    }



    /**
     * Lukee työvälineet tiedostosta.
     * @param hakemisto tiedoston nimen alkuosa
     * @throws SailoException jos lukeminen epäonnistuu
     * 
     * @example
     * <pre name="test">
     * #THROWS SailoException 
     * #import java.io.File;
     *  Tyovalineet valineet = new Tyovalineet();
     *  Tyovaline neulepuikko21 = new Tyovaline(); neulepuikko21.taytaTyovalineTiedoilla(2);
     *  Tyovaline neulepuikko11 = new Tyovaline(); neulepuikko11.taytaTyovalineTiedoilla(1);
     *  Tyovaline neulepuikko22 = new Tyovaline(); neulepuikko22.taytaTyovalineTiedoilla(2); 
     *  Tyovaline neulepuikko12 = new Tyovaline(); neulepuikko12.taytaTyovalineTiedoilla(1); 
     *  Tyovaline neulepuikko23 = new Tyovaline(); neulepuikko23.taytaTyovalineTiedoilla(2); 
     *  String tiedNimi = "testineuleet";
     *  File ftied = new File(tiedNimi+".dat");
     *  ftied.delete();
     *  valineet.lueTiedostosta(tiedNimi); #THROWS SailoException
     *  valineet.lisaa(neulepuikko21);
     *  valineet.lisaa(neulepuikko11);
     *  valineet.lisaa(neulepuikko22);
     *  valineet.lisaa(neulepuikko12);
     *  valineet.lisaa(neulepuikko23);
     *  valineet.tallenna();
     *  valineet = new Tyovalineet();
     *  valineet.lueTiedostosta(tiedNimi);
     *  Iterator<Tyovaline> i = valineet.iterator();
     *  i.next().stringiksi() === neulepuikko21.stringiksi();
     *  i.next().stringiksi() === neulepuikko11.stringiksi();
     *  i.next().stringiksi() === neulepuikko22.stringiksi();
     *  i.next().stringiksi() === neulepuikko12.stringiksi();
     *  i.next().stringiksi() === neulepuikko23.stringiksi();
     *  i.hasNext() === false;
     *  valineet.lisaa(neulepuikko23);
     *  valineet.tallenna();
     *  ftied.delete() === true;
     *  File fbak = new File(tiedNimi+".bak");
     *  fbak.delete() === true;
     * </pre>
     */
    public void lueTiedostosta(String hakemisto) throws SailoException {
        try (BufferedReader fi = new BufferedReader(
                new FileReader(hakemisto + getDatNimi()))) {

            String rivi;
            while ((rivi = fi.readLine()) != null) {
                rivi = rivi.trim();
                if ("".equals(rivi) || rivi.charAt(0) == ';')
                    continue;
                Tyovaline val = new Tyovaline();
                val.parse(rivi); // voisi olla virhekäsittely
                lisaa(val);
            }
            muutettu = false;

        } catch (FileNotFoundException e) {
            throw new SailoException(
                    "Tiedosto " + hakemisto + " ei aukea");
        } catch (IOException e) {
            throw new SailoException(
                    "Ongelmia tiedoston kanssa: " + e.getMessage());
        }
    }


    /**
     * Luetaan aikaisemmin annetun nimisestä tiedostosta
     * @throws SailoException jos tulee poikkeus
     */
    public void lueTiedostosta() throws SailoException {
        lueTiedostosta(getTiedostonPerusNimi());
    }


    /**
     * Tallentaa tyovalineet tiedostoon.
     * @param hakemisto hakemisto jossa ollaan
     * @throws SailoException jos talletus epäonnistuu
     */
    public void tallenna(String hakemisto) throws SailoException {
        if (!muutettu)
            return;

        File fbak = new File(hakemisto + tiedostonPerusNimi + ".dat");
        File ftied = new File(hakemisto + tiedostonPerusNimi + ".dat");
        fbak.delete(); // if ... System.err.println("Ei voi tuhota");
        ftied.renameTo(fbak); // if ... System.err.println("Ei voi nimetä");

        try (PrintWriter fo = new PrintWriter(
                new FileWriter(ftied.getCanonicalPath()))) {
            for (Tyovaline val : this) {
                fo.println(val.stringiksi());
            }
        } catch (FileNotFoundException ex) {
            throw new SailoException(
                    "Tiedosto " + ftied.getName() + " ei aukea");
        } catch (IOException ex) {
            throw new SailoException("Tiedoston " + ftied.getName()
                    + " kirjoittamisessa ongelmia");
        }

        muutettu = false;
    }


    /**
     * Palauttaa käsityölogin työvälineiden lukumäärän
     * @return Työvälineiden lukumäärä
     */
    public int getLkm() {
        return alkiot.size();
    }



    /**
     * Palauttaa tiedoston nimen, jota käytetään tallennukseen
     * @return tallennustiedoston nimi
     */
    public String getTiedostonPerusNimi() {
        return tiedostonPerusNimi;
    }


    /**
     * Palauttaa tiedoston nimen, jota käytetään tallennukseen
     * @return tallennustiedoston nimi
     */
    public String getDatNimi() {
        return tiedostonPerusNimi + ".dat";
    }


    /**
     * Palauttaa varakopiotiedoston nimen
     * @return varakopiotiedoston nimi
     */
    public String getBakNimi() {
        return tiedostonPerusNimi + ".bak";
    }


    @Override
    public Iterator<Tyovaline> iterator() {
        return alkiot.iterator();
    }


    /**
     * Haetaan kaikki projektin työvälineet
     * @param id Sen projektin id, jolle työvälineitä haetaan
     * @return Tietorakenne, jossa on viiteet löydetteyihin työvälineisiin
     * @example
     * <pre name="test">
     * #import java.util.*;
     * 
     *  Tyovalineet valineet = new Tyovalineet();
     *  Tyovaline neulepuikko21 = new Tyovaline(2); valineet.lisaa(neulepuikko21);
     *  Tyovaline neulepuikko11 = new Tyovaline(1); valineet.lisaa(neulepuikko11);
     *  Tyovaline neulepuikko22 = new Tyovaline(2); valineet.lisaa(neulepuikko22);
     *  Tyovaline neulepuikko12 = new Tyovaline(1); valineet.lisaa(neulepuikko12);
     *  Tyovaline neulepuikko23 = new Tyovaline(2); valineet.lisaa(neulepuikko23);
     *  Tyovaline neulepuikko51 = new Tyovaline(5); valineet.lisaa(neulepuikko51);
     *  
     *  List<Tyovaline> loytyneet;
     *  loytyneet = valineet.annaTyovalineet(3);
     *  loytyneet.size() === 0; 
     *  loytyneet = valineet.annaTyovalineet(1);
     *  loytyneet.size() === 2; 
     *  loytyneet.get(0) == neulepuikko11 === true;
     *  loytyneet.get(1) == neulepuikko12 === true;
     *  loytyneet = valineet.annaTyovalineet(5);
     *  loytyneet.size() === 1; 
     *  loytyneet.get(0) == neulepuikko51 === true;
     * </pre> 
     */
    public List<Tyovaline> annaTyovalineet(int id) {
        List<Tyovaline> loydetyt = new ArrayList<Tyovaline>();
        for (Tyovaline val : alkiot)
            if (val.getProjektiId() == id)
                loydetyt.add(val);
        return loydetyt;
    }

    /**
     * Palauttaa kaikki käsityöogiin tallennetut työvälineet projektista riippumatta
     * @return Kaikki käsityölogin työvälineet
     */
    public List<Tyovaline> annaKaikkiTyovalineet() {
        List<Tyovaline> loydetyt = new ArrayList<Tyovaline>();
        for (Tyovaline val : alkiot) 
                loydetyt.add(val);
        Collections.sort(loydetyt, new Tyovaline.Vertailija(1)); 
        return loydetyt;
    }


    /**
     * Testiohjelma työvälineille
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        Tyovalineet valineet = new Tyovalineet();
        Tyovaline neulepuikko1 = new Tyovaline();
        neulepuikko1.taytaTyovalineTiedoilla(2);
        Tyovaline neulepuikko2 = new Tyovaline();
        neulepuikko2.taytaTyovalineTiedoilla(1);
        Tyovaline neulepuikko3 = new Tyovaline();
        neulepuikko3.taytaTyovalineTiedoilla(2);
        Tyovaline neulepuikko4 = new Tyovaline();
        neulepuikko4.taytaTyovalineTiedoilla(2);

        valineet.lisaa(neulepuikko1);
        valineet.lisaa(neulepuikko2);
        valineet.lisaa(neulepuikko3);
        valineet.lisaa(neulepuikko2);
        valineet.lisaa(neulepuikko4);

        System.out.println("============= Tyovalineet testi =================");

        List<Tyovaline> tyovalineet2 = valineet.annaTyovalineet(2);

        for (Tyovaline val : tyovalineet2) {
            System.out.print(val.getProjektiId() + " ");
            val.tulosta(System.out);
        }
    }

}
