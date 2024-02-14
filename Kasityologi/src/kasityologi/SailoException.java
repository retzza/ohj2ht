package kasityologi;

/**
 * Poikkeusluokka tietorakenteesta aiheutuville poikkeuksille.
 * @author Reetta Koskelo, reetta.m.koskelo@student.jyu.fi
 * @version 26.2.2020
 */
public class SailoException extends Exception {
    private static final long serialVersionUID = 1L;


    /**
     * Poikkeuksen muodostaja jolle tuodaan poikkeuksessa
     * käytettävä viesti
     * @param viesti Poikkeuksen viesti
     */
    public SailoException(String viesti) {
        super(viesti);
    }
    
}