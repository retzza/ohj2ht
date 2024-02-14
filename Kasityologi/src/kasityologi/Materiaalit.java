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
public class Materiaalit implements Iterable<Materiaali> {

    private boolean muutettu = false;
    private final String tiedostonPerusNimi = "\\materiaalit";

    /** Taulukko työvälineistä */
    private final Collection<Materiaali> alkiot = new ArrayList<Materiaali>();

    /**
     * Materiaalien alustaminen
     */
    public Materiaalit() {
        // toistaiseksi ei tarvitse tehdä mitään
    }


    /**
     * Lisää uuden materiaalin tietorakenteeseen. Ottaa materiaalin omistukseensa.
     * @param mat lisättävä materiaali. Huom tietorakenne muuttuu omistajaksi
     */
    public void lisaa(Materiaali mat) {
        alkiot.add(mat);
        muutettu = true;
    }
    
    /**
     * Poistaa valitun materiaalin
     * @param materiaali poistettava materiaali
     * @return tosi jos löytyi poistettava tietue 
     * <pre name="test">
     * #THROWS SailoException 
     * #import java.io.File;
     *  Materiaalit matskut = new Harrastukset();
     *  Materiaali kangas21 = new Materiaali(); kangas21.taytaMateriaaliTiedoilla(2);
     *  Materiaali kangas11 = new Materiaali(); kangas11.taytaMateriaaliTiedoilla(1);
     *  Materiaali kangas22 = new Materiaali(); kangas22.taytaMateriaaliTiedoilla(2); 
     *  Materiaali kangas12 = new Materiaali(); kangas12.taytaMateriaaliTiedoilla(1); 
     *  Materiaali kangas23 = new Materiaali(); kangas23.taytaMateriaaliTiedoilla(2); 
     *  matskut.lisaa(kangas21);
     *  matskut.lisaa(kangas11);
     *  matskut.lisaa(kangas22);
     *  matskut.lisaa(kangas12);
     *  matskut.poista(kangas23) === false ; matskut.getLkm() === 4;
     *  matskut.poista(kangas11) === true;   matskut.getLkm() === 3;
     *  List<Materiaali> h = matskut.annaMateriaalit(1);
     *  h.size() === 1; 
     *  h.get(0) === kangas12;
     * </pre>
     */

    public boolean poista(Materiaali materiaali) {
        boolean ret = alkiot.remove(materiaali);
        if (ret) muutettu = true;
        return ret;
    }

    
    /**
     * Poistaa kaikki tietyn tietyn projektin materiaalit
     * @param id viite siihen, mihin liittyvät tietueet poistetaan
     * @return montako poistettiin 
     */
    public int poistaProjektinMateriaalit(int id) {
        int n = 0;
        for (Iterator<Materiaali> it = alkiot.iterator(); it.hasNext();) {
            Materiaali mat = it.next();
            if ( mat.getProjektiId() == id ) {
                it.remove();
                n++;
            }
        }
        if (n > 0) muutettu = true;
        return n;
    }



    /**
     * Lukee materiaalit tiedostosta.
     * @param hakemisto tiedoston nimen alkuosa
     * @throws SailoException jos lukeminen epäonnistuu
     */
    public void lueTiedostosta(String hakemisto) throws SailoException {
        try (BufferedReader fi = new BufferedReader(
                new FileReader(hakemisto + getDatNimi()))) {

            String rivi;
            while ((rivi = fi.readLine()) != null) {
                rivi = rivi.trim();
                if ("".equals(rivi) || rivi.charAt(0) == ';')
                    continue;
                Materiaali mat = new Materiaali();
                mat.parse(rivi); // voisi olla virhekäsittely
                lisaa(mat);
            }
            muutettu = false;

        } catch (FileNotFoundException e) {
            throw new SailoException("Tiedosto " + hakemisto + " ei aukea");
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
     * Tallentaa projektit tiedostoon.  
     * TODO Kesken.
     * @param hakemisto hakemisto jossa ollaan
     * @throws SailoException jos talletus epäonnistuu
     */
    public void tallenna(String hakemisto) throws SailoException {
        if (!muutettu)
            return;
            
        File fbak = new File(hakemisto + getBakNimi());
        File ftied = new File(hakemisto + getDatNimi());
        fbak.delete(); // if ... System.err.println("Ei voi tuhota");
        ftied.renameTo(fbak); // if ... System.err.println("Ei voi nimetä");

        try (PrintWriter fo = new PrintWriter(
                new FileWriter(ftied.getCanonicalPath()))) {
            for (Materiaali mat : this) {
                fo.println(mat.stringiksi());
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
     * Palauttaa käsityölogin materiaalien lukumäärän
     * @return Materiaalien lukumäärä
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
    public Iterator<Materiaali> iterator() {
        return alkiot.iterator();
    }


    /**
     * Haetaan kaikki projektin materiaalit
     * @param id Sen projektin id, jolle materiaaleja haetaan
     * @return Tietorakenne, jossa on viiteet löydetteyihin materiaaleihin
     * @example
     * <pre name="test">
     * #import java.util.*;
     * 
     *  Materiaalit materiaalit = new Materiaalit();
     *  Materiaali kangas21 = new Materiaali(2); materiaalit.lisaa(kangas21);
     *  Materiaali kangas11 = new Materiaali(1); materiaalit.lisaa(kangas11);
     *  Materiaali kangas22 = new Materiaali(2); materiaalit.lisaa(kangas22);
     *  Materiaali kangas12 = new Materiaali(1); materiaalit.lisaa(kangas12);
     *  Materiaali kangas23 = new Materiaali(2); materiaalit.lisaa(kangas23);
     *  Materiaali kangas51 = new Materiaali(5); materiaalit.lisaa(kangas51);
     *  
     *  List<Materiaali> loytyneet;
     *  loytyneet = materiaalit.annaMateriaalit(3);
     *  loytyneet.size() === 0; 
     *  loytyneet = materiaalit.annaMateriaalit(1);
     *  loytyneet.size() === 2; 
     *  loytyneet.get(0) == kangas11 === true;
     *  loytyneet.get(1) == kangas12 === true;
     *  loytyneet = materiaalit.annaMateriaalit(5);
     *  loytyneet.size() === 1; 
     *  loytyneet.get(0) == kangas51 === true;
     * </pre> 
     */
    public List<Materiaali> annaMateriaalit(int id) {
        List<Materiaali> loydetyt = new ArrayList<Materiaali>();
        for (Materiaali mat : alkiot)
            if (mat.getProjektiId() == id)
                loydetyt.add(mat);
        return loydetyt;
    }

    /**
     * Palauttaa kaikki käsityöogiin tallennetut materiaalit projektista riippumatta
     * @return Kaikki käsityölogin materiaalit
     */
    public List<Materiaali> annaKaikkiMateriaalit() {
        List<Materiaali> loydetyt = new ArrayList<Materiaali>();
        for (Materiaali mat : alkiot) {
            loydetyt.add(mat);
        }
        Collections.sort(loydetyt, new Materiaali.Vertailija(1)); 
        return loydetyt;
    }


    /**
     * Testiohjelma työvälineille
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        Materiaalit materiaalit = new Materiaalit();
        Materiaali kangas1 = new Materiaali();
        kangas1.taytaMateriaaliTiedoilla(2);
        Materiaali kangas2 = new Materiaali();
        kangas2.taytaMateriaaliTiedoilla(1);
        Materiaali kangas3 = new Materiaali();
        kangas3.taytaMateriaaliTiedoilla(2);
        Materiaali kangas4 = new Materiaali();
        kangas4.taytaMateriaaliTiedoilla(2);

        materiaalit.lisaa(kangas1);
        materiaalit.lisaa(kangas2);
        materiaalit.lisaa(kangas3);
        materiaalit.lisaa(kangas2);
        materiaalit.lisaa(kangas4);

        System.out.println("============= Materiaalit testi =================");

        List<Materiaali> materiaalit2 = materiaalit.annaMateriaalit(2);

        for (Materiaali mat : materiaalit2) {
            System.out.print(mat.getProjektiId() + " ");
            mat.tulosta(System.out);
        }
    }

}
