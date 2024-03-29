package kasityologi.test;
// Generated by ComTest BEGIN
import static org.junit.Assert.assertEquals;

import org.junit.Test;

// Generated by ComTest END
import kasityologi.Tyovaline;

/**
 * Test class made by ComTest
 * @version 2020.04.01 20:47:34 // Generated by ComTest
 *
 */
@SuppressWarnings({ "all" })
public class TyovalineTest {


  // Generated by ComTest BEGIN
  /** testToString84 */
  @Test
  public void testToString84() {    // Tyovaline: 84
    Tyovaline tyovaline = new Tyovaline(); 
    tyovaline.parse("   2   |  virkkuukoukku  "); 
    assertEquals("From: Tyovaline line: 87", "2|virkkuukoukku", tyovaline.toString()); 
  } // Generated by ComTest END


  // Generated by ComTest BEGIN
  /** testParse113 */
  @Test
  public void testParse113() {    // Tyovaline: 113
    Tyovaline tyovaline = new Tyovaline(); 
    tyovaline.parse("   2   |  virkkuukoukku  "); 
    assertEquals("From: Tyovaline line: 116", 2, tyovaline.getProjektiId()); 
    assertEquals("From: Tyovaline line: 117", "2|virkkuukoukku", tyovaline.toString()); 
  } // Generated by ComTest END
}