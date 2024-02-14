package kasityologi;

import java.io.File;
import java.util.Collection;
import java.util.List;

/**
 * - huolehtii Projektit-, Työvalineet- ja Materiaalit-luokkien välisestä yhteistyöstä
 *   ja välittää näitä tietoja pyydettäessä              
 * - lukee ja kirjoittaa login tiedostoon pyytämällä apua avustajiltaan
 * @author Reetta Koskelo, reetta.m.koskelo@student.jyu.fi
 * @version Feb 25, 2020
 *
 */
public class Kasityologi {

    private final Projektit projektit = new Projektit();
    private final Tyovalineet tyovalineet = new Tyovalineet();
    private final Materiaalit materiaalit = new Materiaalit();

    /**
     * Alustetaan käsityölogin tiedot
     */
    public Kasityologi() {
        // ei tarvihe tehdä mittään
    }
    
    /**
     * Poistaa projekteista, materiaaleista ja työvälineistä projektin tiedot 
     * @param projekti projekti jokapoistetaan
     * @return montako projektia poistettiin
     */
    public int poista(Projekti projekti) {
        if ( projekti == null ) return 0;
        int ret = projektit.poista(projekti.getProjektiId()); 
        materiaalit.poistaProjektinMateriaalit(projekti.getProjektiId()); 
        tyovalineet.poistaProjektinTyovalineet(projekti.getProjektiId()); 
        return ret; 
    }
    
    /** 
     * Poistaa tämän työvälineen 
     * @param tyovaline poistettava työväline 
     */ 
    public void poistaTyovaline(Tyovaline tyovaline) { 
        tyovalineet.poista(tyovaline); 
    } 

    /** 
     * Poistaa tämän materiaalin
     * @param materiaali poistettava materiaali 
     */ 
    public void poistaMateriaali(Materiaali materiaali) { 
        materiaalit.poista(materiaali); 
    } 




    /**
     * Lisätään uusi projekti käsityölogiin
     * @param projekti lisättävä
     * @throws SailoException jos ei mahdu
     * @example
     * <pre name="test">
     * #THROWS SailoException
     * Kasityologi kasityologi = new Kasityologi();
     * Projekti sukka1 = new Projekti();
     * Projekti sukka2 = new Projekti();
     * sukka1.rekisteroi(); sukka2.rekisteroi();
     * kasityologi.getProjekteja() === 0;
     * kasityologi.lisaa(sukka1); kasityologi.getProjekteja() === 1;
     * kasityologi.lisaa(sukka2); kasityologi.getProjekteja() === 2;
     * kasityologi.lisaa(sukka1); kasityologi.getProjekteja() === 3;
     * kasityologi.annaProjekti(0) === sukka1;
     * kasityologi.annaProjekti(1) === sukka2;
     * kasityologi.annaProjekti(2) === sukka1;
     * kasityologi.annaProjekti(3) === sukka1; #THROWS IndexOutOfBoundsException 
     * kasityologi.lisaa(sukka1); kasityologi.getProjekteja() === 4;
     * kasityologi.lisaa(sukka1); kasityologi.getProjekteja() === 5;
     * kasityologi.lisaa(sukka1);
     * </pre>
     */
    public void lisaa(Projekti projekti) throws SailoException {
        projektit.lisaa(projekti);
    }
    
    /** 
     * Korvaa projektin tietorakenteessa. Ottaa projektin omistukseensa. 
     * Etsitään samalla id:llä oleva projekti. Jos ei löydy, 
     * niin lisätään uutena projektina. 
     * @param projekti lisättävän projektin viite. Huom tietorakenne muuttuu omistajaksi 
     * @throws SailoException jos tietorakenne on jo täynnä 
     */ 
    public void korvaaTaiLisaa(Projekti projekti) throws SailoException { 
        projektit.korvaaTaiLisaa(projekti); 
    } 


    /**
     * Lisätään uusi työväline käsityölogiin
     * @param val Lisättävä työvaline
     */
    public void lisaa(Tyovaline val) {
        tyovalineet.lisaa(val);
    }


    /**
     * Lisätään uusi materiaali käsityölogiin
     * @param mat Lisättävä materiaali
     */
    public void lisaa(Materiaali mat) {
        materiaalit.lisaa(mat);
    }
    
    
    /**
     * Etsii projekteja tietllä hakuehdolla
     * @param hakuehto Hakuehto jolla haetaan
     * @param k Monennenko kentän tietoja haetaan
     * @return Lista projekteista, jotka täyttävät hakuehdon
     */
    public Collection<Projekti> etsi(String hakuehto, int k) {
        return projektit.etsi(hakuehto, k);
    } 
    

    /**
     * Haetaan kaikki projektin työvälineet
     * @param projekti Projekti, johon liittyviä työvälineitä haetaan
     * @return tietorakenne jossa viiteet löydetteyihin työvälineisiin
     * @example
     * <pre name="test">
     * #import java.util.*;
     * 
     *  Kasityologi kasityologi = new Kasityologi();
     *  Projekti villasukka1 = new Projekti(), villasukka2 = new Projekti(), villasukka3 = new Projekti();
     *  villasukka1.rekisteroi(); villasukka2.rekisteroi(); villasukka3.rekisteroi();
     *  int id1 = villasukka1.getProjektiId();
     *  int id2 = villasukka2.getProjektiId();
     *  Tyovaline neulepuikko11 = new Tyovaline(id1); kasityologi.lisaa(neulepuikko11);
     *  Tyovaline neulepuikko12 = new Tyovaline(id1); kasityologi.lisaa(neulepuikko12);
     *  Tyovaline neulepuikko21 = new Tyovaline(id2); kasityologi.lisaa(neulepuikko21);
     *  Tyovaline neulepuikko22 = new Tyovaline(id2); kasityologi.lisaa(neulepuikko22);
     *  Tyovaline neulepuikko23 = new Tyovaline(id2); kasityologi.lisaa(neulepuikko23);
     *  
     *  List<Tyovaline> loytyneet;
     *  loytyneet = kasityologi.annaTyovalineet(villasukka3);
     *  loytyneet.size() === 0; 
     *  loytyneet = kasityologi.annaTyovalineet(villasukka1);
     *  loytyneet.size() === 2; 
     *  loytyneet.get(0) == neulepuikko11 === true;
     *  loytyneet.get(1) == neulepuikko12 === true;
     *  loytyneet = kasityologi.annaTyovalineet(villasukka2);
     *  loytyneet.size() === 3; 
     *  loytyneet.get(0) == neulepuikko21 === true;
     * </pre> 
     */
    public List<Tyovaline> annaTyovalineet(Projekti projekti) {
        return tyovalineet.annaTyovalineet(projekti.getProjektiId());
    }

    /**
     * Palauttaa kaikki käsityöogiin tallennetut työvälineet projektista riippumatta
     * @return Kaikki käsityölogin työvälineet
     */
    public List<Tyovaline> annaKaikkiTyovalineet() {
        return tyovalineet.annaKaikkiTyovalineet();
    }


    /**
     * Haetaan kaikki projektin materiaalit
     * @param projekti Projekti, johon liittyviä työvälineitä haetaan
     * @return tietorakenne jossa viiteet löydetteyihin työvälineisiin
     * @example
     * <pre name="test">
     * #import java.util.*;
     * 
     *  Kasityologi kasityologi = new Kasityologi();
     *  Projekti kangas1 = new Projekti(), kangas2 = new Projekti(), kangas3 = new Projekti();
     *  kangas1.rekisteroi(); kangas2.rekisteroi(); kangas3.rekisteroi();
     *  int id1 = kangas1.getProjektiId();
     *  int id2 = kangas2.getProjektiId();
     *  Materiaali neulepuikko11 = new Materiaali(id1); kasityologi.lisaa(neulepuikko11);
     *  Materiaali neulepuikko12 = new Materiaali(id1); kasityologi.lisaa(neulepuikko12);
     *  Materiaali neulepuikko21 = new Materiaali(id2); kasityologi.lisaa(neulepuikko21);
     *  Materiaali neulepuikko22 = new Materiaali(id2); kasityologi.lisaa(neulepuikko22);
     *  Materiaali neulepuikko23 = new Materiaali(id2); kasityologi.lisaa(neulepuikko23);
     *  
     *  List<Materiaali> loytyneet;
     *  loytyneet = kasityologi.annaMateriaalit(kangas3);
     *  loytyneet.size() === 0; 
     *  loytyneet = kasityologi.annaMateriaalit(kangas1);
     *  loytyneet.size() === 2; 
     *  loytyneet.get(0) == neulepuikko11 === true;
     *  loytyneet.get(1) == neulepuikko12 === true;
     *  loytyneet = kasityologi.annaMateriaalit(kangas2);
     *  loytyneet.size() === 3; 
     *  loytyneet.get(0) == neulepuikko21 === true;
     * </pre> 
     */
    public List<Materiaali> annaMateriaalit(Projekti projekti) {
        return materiaalit.annaMateriaalit(projekti.getProjektiId());
    }
    
    /**
     * Palauttaa kaikki käsityöogiin tallennetut materiaalit projektista riippumatta
     * @return Kaikki käsityölogin materiaalit
     */
    public List<Materiaali> annaKaikkiMateriaalit() {
        return materiaalit.annaKaikkiMateriaalit();
    }


    /**
     * Palautaa käsityölogin projektien määrän
     * @return Montako projektia käsityölogissa on
     */
    public int getProjekteja() {
        return projektit.getLkm();
    }


    /**
     * Palauttaa i:nnen projektin
     * @param i monesko projekti palautetaan
     * @return viite i:nnenteen jäseneen
     * @throws IndexOutOfBoundsException jos i väärin
     */
    public Projekti annaProjekti(int i) throws IndexOutOfBoundsException {
        return projektit.anna(i);
    }


    /**
     * Tallentaa kaikki tiedostot
     * @param hakemisto hakemisto jossa ollaan
     * @throws SailoException jos joku menee pieleen
     */
    public void tallenna(String hakemisto) throws SailoException {
        File f = new File(hakemisto);
        if(!f.exists()) f.mkdir();
        
        String virhe = "";
        try {
            projektit.tallenna(hakemisto);
        } catch (SailoException e) {
            virhe += e.getMessage();
        }
        try {
            tyovalineet.tallenna(hakemisto);
        } catch (SailoException e) {
            virhe += e.getMessage();
        }

        try {
            materiaalit.tallenna(hakemisto);
        } catch (SailoException e) {
            virhe += e.getMessage();
        }

        if (virhe.length() > 0)
            throw new SailoException(virhe);
    }


    /**
     * Luetaan tiedostot
     * @param hakemisto hakemisto jossa ollaan
     * @throws SailoException jos joku mene pieleen
     */
    public void lueTiedosto(String hakemisto) throws SailoException {
        projektit.lueTiedostosta(hakemisto);
        tyovalineet.lueTiedostosta(hakemisto);
        materiaalit.lueTiedostosta(hakemisto);
    }


    /**
     * Testiohjelma kasityologista
     * @param args ei käytössä
     */
    public static void main(String args[]) {
        Kasityologi kasityologi = new Kasityologi();

        try {
            // kasityologi.lueTiedostosta("kelmit");

            Projekti villasukka1 = new Projekti(), villasukka2 = new Projekti();
            villasukka1.rekisteroi();
            villasukka1.taytaVillasukkaTiedoilla();
            villasukka2.rekisteroi();
            villasukka2.taytaVillasukkaTiedoilla();

            kasityologi.lisaa(villasukka1);
            kasityologi.lisaa(villasukka2);
            int id1 = villasukka1.getProjektiId();
            int id2 = villasukka2.getProjektiId();
            Tyovaline neulepuikko11 = new Tyovaline(id1);
            neulepuikko11.taytaTyovalineTiedoilla(id1);
            kasityologi.lisaa(neulepuikko11);
            Tyovaline neulepuikko12 = new Tyovaline(id1);
            neulepuikko12.taytaTyovalineTiedoilla(id1);
            kasityologi.lisaa(neulepuikko12);
            Tyovaline neulepuikko21 = new Tyovaline(id2);
            neulepuikko21.taytaTyovalineTiedoilla(id2);
            kasityologi.lisaa(neulepuikko21);
            Tyovaline neulepuikko22 = new Tyovaline(id2);
            neulepuikko22.taytaTyovalineTiedoilla(id2);
            kasityologi.lisaa(neulepuikko22);
            Tyovaline neulepuikko23 = new Tyovaline(id2);
            neulepuikko23.taytaTyovalineTiedoilla(id2);
            kasityologi.lisaa(neulepuikko23);

            Materiaali villalanka11 = new Materiaali(id1);
            villalanka11.taytaMateriaaliTiedoilla(id1);
            kasityologi.lisaa(villalanka11);
            Materiaali villalanka12 = new Materiaali(id1);
            villalanka12.taytaMateriaaliTiedoilla(id1);
            kasityologi.lisaa(villalanka12);
            Materiaali villalanka21 = new Materiaali(id1);
            villalanka21.taytaMateriaaliTiedoilla(id2);
            kasityologi.lisaa(villalanka21);
            Materiaali villalanka22 = new Materiaali(id1);
            villalanka22.taytaMateriaaliTiedoilla(id2);
            kasityologi.lisaa(villalanka22);
            Materiaali villalanka23 = new Materiaali(id1);
            villalanka23.taytaMateriaaliTiedoilla(id2);
            kasityologi.lisaa(villalanka23);
            Materiaali villalanka24 = new Materiaali(id1);
            villalanka24.taytaMateriaaliTiedoilla(id2);
            kasityologi.lisaa(villalanka24);

            System.out.println(
                    "============= Käsityologin testi =================");

            for (int i = 0; i < kasityologi.getProjekteja(); i++) {
                Projekti projekti = kasityologi.annaProjekti(i);
                System.out.println("Projekti paikassa: " + i);
                projekti.tulosta(System.out);
                System.out.println("Projektin työvälineet: ");
                List<Tyovaline> loytyneet = kasityologi
                        .annaTyovalineet(projekti);
                for (Tyovaline tyovaline : loytyneet)
                    tyovaline.tulosta(System.out);
                System.out.println("Projektin materiaalit: ");
                List<Materiaali> loytyneet1 = kasityologi
                        .annaMateriaalit(projekti);
                for (Materiaali materiaali : loytyneet1)
                    materiaali.tulosta(System.out);
            }

        } catch (SailoException ex) {
            System.out.println(ex.getMessage());
        }
    }

}
