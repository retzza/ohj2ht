package kasityologi.test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

// Generated by ComTest BEGIN
import java.util.List;

import org.junit.Test;

// Generated by ComTest END
import kasityologi.Kasityologi;
import kasityologi.Materiaali;
import kasityologi.Projekti;
import kasityologi.SailoException;
import kasityologi.Tyovaline;

/**
 * Test class made by ComTest
 * @version 2020.03.12 16:07:41 // Generated by ComTest
 *
 */
@SuppressWarnings({ "all" })
public class KasityologiTest {



  // Generated by ComTest BEGIN
  /** 
   * testLisaa33 
   * @throws SailoException when error
   */
  @Test
  public void testLisaa33() throws SailoException {    // Kasityologi: 33
    Kasityologi kasityologi = new Kasityologi(); 
    Projekti sukka1 = new Projekti(); 
    Projekti sukka2 = new Projekti(); 
    sukka1.rekisteroi(); sukka2.rekisteroi(); 
    assertEquals("From: Kasityologi line: 39", 0, kasityologi.getProjekteja()); 
    kasityologi.lisaa(sukka1); assertEquals("From: Kasityologi line: 40", 1, kasityologi.getProjekteja()); 
    kasityologi.lisaa(sukka2); assertEquals("From: Kasityologi line: 41", 2, kasityologi.getProjekteja()); 
    kasityologi.lisaa(sukka1); assertEquals("From: Kasityologi line: 42", 3, kasityologi.getProjekteja()); 
    assertEquals("From: Kasityologi line: 43", sukka1, kasityologi.annaProjekti(0)); 
    assertEquals("From: Kasityologi line: 44", sukka2, kasityologi.annaProjekti(1)); 
    assertEquals("From: Kasityologi line: 45", sukka1, kasityologi.annaProjekti(2)); 
    try {
    assertEquals("From: Kasityologi line: 46", sukka1, kasityologi.annaProjekti(3)); 
    fail("Kasityologi: 46 Did not throw IndexOutOfBoundsException");
    } catch(IndexOutOfBoundsException _e_){ _e_.getMessage(); }
    kasityologi.lisaa(sukka1); assertEquals("From: Kasityologi line: 47", 4, kasityologi.getProjekteja()); 
    kasityologi.lisaa(sukka1); assertEquals("From: Kasityologi line: 48", 5, kasityologi.getProjekteja()); 
    kasityologi.lisaa(sukka1); 
  } // Generated by ComTest END


  // Generated by ComTest BEGIN
  /** testAnnaTyovalineet99 */
  @Test
  public void testAnnaTyovalineet99() {    // Kasityologi: 99
    Kasityologi kasityologi = new Kasityologi(); 
    Projekti villasukka1 = new Projekti(), villasukka2 = new Projekti(), villasukka3 = new Projekti(); 
    villasukka1.rekisteroi(); villasukka2.rekisteroi(); villasukka3.rekisteroi(); 
    int id1 = villasukka1.getProjektiId(); 
    int id2 = villasukka2.getProjektiId(); 
    Tyovaline neulepuikko11 = new Tyovaline(id1); kasityologi.lisaa(neulepuikko11); 
    Tyovaline neulepuikko12 = new Tyovaline(id1); kasityologi.lisaa(neulepuikko12); 
    Tyovaline neulepuikko21 = new Tyovaline(id2); kasityologi.lisaa(neulepuikko21); 
    Tyovaline neulepuikko22 = new Tyovaline(id2); kasityologi.lisaa(neulepuikko22); 
    Tyovaline neulepuikko23 = new Tyovaline(id2); kasityologi.lisaa(neulepuikko23); 
    List<Tyovaline> loytyneet; 
    loytyneet = kasityologi.annaTyovalineet(villasukka3); 
    assertEquals("From: Kasityologi line: 115", 0, loytyneet.size()); 
    loytyneet = kasityologi.annaTyovalineet(villasukka1); 
    assertEquals("From: Kasityologi line: 117", 2, loytyneet.size()); 
    assertEquals("From: Kasityologi line: 118", true, loytyneet.get(0) == neulepuikko11); 
    assertEquals("From: Kasityologi line: 119", true, loytyneet.get(1) == neulepuikko12); 
    loytyneet = kasityologi.annaTyovalineet(villasukka2); 
    assertEquals("From: Kasityologi line: 121", 3, loytyneet.size()); 
    assertEquals("From: Kasityologi line: 122", true, loytyneet.get(0) == neulepuikko21); 
  } // Generated by ComTest END


  // Generated by ComTest BEGIN
  /** testAnnaMateriaalit134 */
  @Test
  public void testAnnaMateriaalit134() {    // Kasityologi: 134
    Kasityologi kasityologi = new Kasityologi(); 
    Projekti kangas1 = new Projekti(), kangas2 = new Projekti(), kangas3 = new Projekti(); 
    kangas1.rekisteroi(); kangas2.rekisteroi(); kangas3.rekisteroi(); 
    int id1 = kangas1.getProjektiId(); 
    int id2 = kangas2.getProjektiId(); 
    Materiaali neulepuikko11 = new Materiaali(id1); kasityologi.lisaa(neulepuikko11); 
    Materiaali neulepuikko12 = new Materiaali(id1); kasityologi.lisaa(neulepuikko12); 
    Materiaali neulepuikko21 = new Materiaali(id2); kasityologi.lisaa(neulepuikko21); 
    Materiaali neulepuikko22 = new Materiaali(id2); kasityologi.lisaa(neulepuikko22); 
    Materiaali neulepuikko23 = new Materiaali(id2); kasityologi.lisaa(neulepuikko23); 
    List<Materiaali> loytyneet; 
    loytyneet = kasityologi.annaMateriaalit(kangas3); 
    assertEquals("From: Kasityologi line: 150", 0, loytyneet.size()); 
    loytyneet = kasityologi.annaMateriaalit(kangas1); 
    assertEquals("From: Kasityologi line: 152", 2, loytyneet.size()); 
    assertEquals("From: Kasityologi line: 153", true, loytyneet.get(0) == neulepuikko11); 
    assertEquals("From: Kasityologi line: 154", true, loytyneet.get(1) == neulepuikko12); 
    loytyneet = kasityologi.annaMateriaalit(kangas2); 
    assertEquals("From: Kasityologi line: 156", 3, loytyneet.size()); 
    assertEquals("From: Kasityologi line: 157", true, loytyneet.get(0) == neulepuikko21); 
  } // Generated by ComTest END
}