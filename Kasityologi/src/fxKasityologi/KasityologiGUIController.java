package fxKasityologi;

import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;

import fi.jyu.mit.fxgui.ComboBoxChooser;
import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ListChooser;
import fi.jyu.mit.fxgui.ModalController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import kasityologi.Kasityologi;
import kasityologi.Materiaali;
import kasityologi.Projekti;
import kasityologi.SailoException;
import kasityologi.Tyovaline;

/**
 * @author Reetta Koskelo, reetta.m.koskelo@student.jyu.fi
 * @version Feb 5, 2020
 *
 */
public class KasityologiGUIController implements Initializable {

    @FXML
    private TextField hakuehto;

    @FXML
    private ComboBoxChooser<String> cbKentat;

    @FXML
    ListChooser<Projekti> chooserProjektit;

    @FXML
    private ScrollPane panelProjekti;

    @FXML
    private Label labelVirhe;

    @FXML
    private TextField editNimi;

    @FXML
    private TextField editTyyppi;

    @FXML
    private TextField editKenelle;

    @FXML
    private TextField editAloituspv;

    @FXML
    private TextField editLopetuspv;

    @FXML
    private Slider editValmiusaste;

    @FXML
    private ListView<Materiaali> listMateriaalit;

    @FXML
    private ListView<Tyovaline> listValineet;

    private String kasityologinnimi = "";

    @Override
    public void initialize(URL url, ResourceBundle bundle) {
        alusta();
    }


    @FXML
    private void handleHakuehto() {
        hae(0);
    }


    /**
      * Tietojen tallennus
     */
    @FXML
    void handleTallenna() {
        tallenna();
    }


    /**
     * Avaa uuden käsityölogin annetun nimen perusteella
     */
    @FXML
    private void handleAvaa() {
        avaa();
    }


    @FXML
    private void handleLopeta() {
        tallenna();
        Platform.exit();
    }


    /**
     * Luodaan uusi käsityöprojekti
     */
    @FXML
    private void handleUusiProjekti() {
        uusiProjekti();
    }


    @FXML
    private void handleMuokkaa() {
        muokkaa();
    }


    @FXML
    private void handlePoistaProjekti() {
        poistaProjekti();
    }
    
    @FXML
    private void handlePoistaMateriaali() {
        poistaMateriaali();
    }
    
    @FXML
    private void handlePoistaTyovaline() {
        poistaTyovaline();
    }


    @FXML
    private void handleApua() {
        Dialogs.showMessageDialog("Ei osata vielä antaa apua!");
    }


    @FXML
    private void handleTietoja() {
        ModalController.showModal(
                KasityologiGUIController.class.getResource("AboutGUIView.fxml"),
                "Tietoja", null, "");
    }


    @FXML
    private void handleLisatietoja() {
        lisatietoja();
    }

    // =================================================================================================================================
    // Tästä eteenpäin ei käyttöliittymään suoraan liittyvää koodia

    private Kasityologi kasityologi;
    private static Projekti projektiKohdalla;
    private String uusinimi = "";
    private TextField[] edits1;
    private Slider[] edits2;
    private static Projekti apuprojekti = new Projekti();

    private void alusta() {
        chooserProjektit.clear();
        chooserProjektit.addSelectionListener(e -> naytaProjekti());

        cbKentat.clear();
        for (int k = apuprojekti.ekaKentta(); k < apuprojekti.getKenttia(); k++)
            cbKentat.add(apuprojekti.getKysymys(k), null);
        cbKentat.getSelectionModel().select(0);

        panelProjekti.setFitToHeight(true);

        chooserProjektit.clear();
        chooserProjektit.addSelectionListener(e -> naytaProjekti());

        edits1 = new TextField[] { editNimi, editTyyppi, editKenelle, editAloituspv, editLopetuspv };
        edits2 = new Slider[] { editValmiusaste };

    }


    private void hae(int pnr) {
        int pnro = pnr; // jnro jäsenen numero, joka aktivoidaan haun jälkeen
        if (pnro <= 0) {
            Projekti kohdalla = projektiKohdalla;
            if (kohdalla != null)
                pnro = kohdalla.getProjektiId();
        }

        int k = cbKentat.getSelectionModel().getSelectedIndex()
                + apuprojekti.ekaKentta();
        String ehto = hakuehto.getText();
        if (ehto.indexOf('*') < 0)
            ehto = "*" + ehto + "*";

        chooserProjektit.clear();

        int index = 0;
        Collection<Projekti> projektit;
        projektit = kasityologi.etsi(ehto, k);
        int i = 0;
        for (Projekti projekti : projektit) {
            if (projekti.getProjektiId() == pnro)
                index = i;
            chooserProjektit.add(projekti.getNimi(), projekti);
            i++;
        }
        chooserProjektit.setSelectedIndex(index);
    }


    private void naytaProjekti() {
        projektiKohdalla = chooserProjektit.getSelectedObject();

        if (projektiKohdalla == null)
            return;

        ProjektiDialogGUIController.naytaProjektiPaaikkunassa(edits1, edits2, projektiKohdalla);
        naytaMateriaalit(projektiKohdalla);
        naytaTyovalineet(projektiKohdalla);
    }


    private void naytaMateriaalit(Projekti projekti) {
        listMateriaalit.getItems().clear();
        if (projekti == null)
            return;

        List<Materiaali> materiaalit = kasityologi.annaMateriaalit(projekti);
        if (materiaalit.size() == 0)
            return;
        for (Materiaali mat : materiaalit)
            naytaMateriaali(mat);
    }


    private void naytaMateriaali(Materiaali mat) {
        listMateriaalit.getItems().add(mat);
    }


    private void naytaTyovaline(Tyovaline val) {
        listValineet.getItems().add(val);
    }


    private void naytaTyovalineet(Projekti projekti) {
        listValineet.getItems().clear();
        if (projekti == null)
            return;

        List<Tyovaline> tyovalineet = kasityologi.annaTyovalineet(projekti);
        if (tyovalineet.size() == 0)
            return;
        for (Tyovaline val : tyovalineet)
            naytaTyovaline(val);
    }


    private void muokkaa() {
        if (projektiKohdalla == null)
            return;
        try {
            Projekti projekti;
            projekti = ProjektiDialogGUIController.kysyProjekti(null,
                    projektiKohdalla.clone());
            if (projekti == null)
                return;
            kasityologi.korvaaTaiLisaa(projekti);
            hae(projekti.getProjektiId());
        } catch (CloneNotSupportedException e) {
            //
        } catch (SailoException e) {
            Dialogs.showMessageDialog(e.getMessage());
        }
    }


    private void uusiProjekti() {
        try {
            Projekti uusi = new Projekti();
            uusi = ProjektiDialogGUIController.kysyProjekti(null, uusi);
            if (uusi == null)
                return;
            uusi.rekisteroi();
            kasityologi.lisaa(uusi);
            hae(uusi.getProjektiId());
        } catch (SailoException e) {
            Dialogs.showMessageDialog(
                    "Ongelmia uuden luomisessa " + e.getMessage());
            return;
        }
    }


    /**
     * Avaa materiaalien ja työvälinein lisäysikkunan
     */
    public void lisatietoja() {
        try {
            if (projektiKohdalla == null)
                return;

            ModalController.showModal(
                    LisatietojaGUIController.class
                            .getResource("LisatietojaGUIView.fxml"),
                    "Lisatiedot", null, kasityologi);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void setTitle(@SuppressWarnings("unused") String title) {
        // ModalController.getStage(hakuehto).setTitle(title);
    }


    /**
     * Alustaa kerhon lukemalla sen valitun nimisestä tiedostosta
     * @param nimi tiedosto josta kerhon tiedot luetaan
     */
    protected void lueTiedosto(String nimi) {
        kasityologinnimi = nimi;
        setTitle("Käsityölogi - " + kasityologinnimi);
        try {
            kasityologi.lueTiedosto(kasityologinnimi);
            hae(0);
        } catch (SailoException e) {
            Dialogs.showMessageDialog(e.getMessage());
        }
    }


    /**
     * Kysytään tiedoston nimi ja luetaan se
     * @return true jos onnistui, false jos ei
     */
    public boolean avaa() {
        uusinimi = KasityologinNimiGUIController.kysyNimi(null,
                kasityologinnimi);
        if (uusinimi == null)
            return false;
        lueTiedosto(uusinimi);
        return true;
    }


    /**
     * Tietojen tallennus
     */
    private void tallenna() {
        try {
            kasityologi.tallenna(uusinimi);
        } catch (SailoException e) {
            Dialogs.showMessageDialog(
                    "Tallennuksessa tuli virhe:" + e.getMessage());
        }
    }


    /**
     * Tarkistetaan onko tallennus tehty
     * @return true jos saa sulkea sovelluksen, false jos ei
     */
    public boolean voikoSulkea() {
        tallenna();
        return true;
    }


    /**
     * Asetetaan kontrollerin käsityologiviite
     * @param kasityologi Käsityölogi, johon viitataan
     */
    public void setKasityologi(Kasityologi kasityologi) {
        this.kasityologi = kasityologi;
    }

    

    /**
     * Poistetaan työvälinelistalta valitulla kohdalla oleva harrastus. 
     */
    private void poistaTyovaline() {
        int rivi = listValineet.getSelectionModel().getSelectedIndex();
        if ( rivi < 0 ) return;
        Tyovaline tyovaline = listValineet.getSelectionModel().getSelectedItem();
        if ( tyovaline == null ) return;
        kasityologi.poistaTyovaline(tyovaline);
        naytaTyovalineet(projektiKohdalla);
        int tyovalineita = listValineet.getItems().size(); 
        if ( rivi >= tyovalineita ) rivi = tyovalineita -1;
        listValineet.getFocusModel().focus(rivi);
        listValineet.getSelectionModel().select(rivi);
    }

    /**
     * Poistetaan materiaalilistasta valitulla kohdalla oleva harrastus. 
     */
    private void poistaMateriaali() {
        int rivi = listMateriaalit.getSelectionModel().getSelectedIndex();
        if ( rivi < 0 ) return;
        Materiaali materiaali = listMateriaalit.getSelectionModel().getSelectedItem();
        if ( materiaali == null ) return;
        kasityologi.poistaMateriaali(materiaali);
        naytaMateriaalit(projektiKohdalla);
        int materiaaleja = listMateriaalit.getItems().size(); 
        if ( rivi >= materiaaleja ) rivi = materiaaleja -1;
        listMateriaalit.getFocusModel().focus(rivi);
        listMateriaalit.getSelectionModel().select(rivi);
    }


    /*
     * Poistetaan listalta valittu projekti
     */
    private void poistaProjekti() {
        Projekti projekti = projektiKohdalla;
        if (projekti == null)
            return;
        if (!Dialogs.showQuestionDialog("Poisto",
                "Poistetaanko projekti: " + projekti.getNimi() + " ?", "Kyllä", "Ei"))
            return;
        kasityologi.poista(projekti);
        int index = chooserProjektit.getSelectedIndex();
        hae(0);
        chooserProjektit.setSelectedIndex(index);
    }


    /**
     * Palauttaa sen projektin, joka on valittuna
     * @return Valitun projektin
     */
    public static Projekti getProjektiKohdalla() {
        return projektiKohdalla;
    }

}
