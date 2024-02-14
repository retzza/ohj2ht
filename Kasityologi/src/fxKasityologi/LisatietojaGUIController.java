package fxKasityologi;

import java.net.URL;
import java.util.ResourceBundle;

import fi.jyu.mit.fxgui.ComboBoxChooser;
import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import kasityologi.Kasityologi;
import kasityologi.Materiaali;
import kasityologi.Projekti;
import kasityologi.Tyovaline;

/**
 * @author Reetta Koskelo, reetta.m.koskelo@student.jyu.fi
 * @version Feb 7, 2020
 *
 */
public class LisatietojaGUIController
        implements ModalControllerInterface<Kasityologi>, Initializable {

    @FXML
    private Label labelVirhe;
    @FXML
    private TextField editMateriaali;

    @FXML
    private TextField editVari;

    @FXML
    private TextField editTyovaline;

    @FXML private ComboBoxChooser<String> cbMateriaali;
    
    @FXML private ComboBoxChooser<String> cbTyovaline;
    
    @FXML private ComboBoxChooser<String> cbVari;


    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        //
    }

    @FXML
    private void handleLisaa() {
        if (projektiKohdalla != null && editMateriaali.getText().equals("") && editVari.getText().equals("") && editTyovaline.getText().equals("")) {
            ModalController.closeStage(labelVirhe);
        }
        if (projektiKohdalla != null && editMateriaali.getText().equals("") && !editVari.getText().equals("")) {
           naytaVirhe("Materiaalin nimi ei saa olla tyhjä? Viesti peräisin LisätietojaGUIControllerista");
           return;
       }
       if (projektiKohdalla != null && !editMateriaali.getText().equals("") && editVari.getText().equals("")) {
           naytaVirhe("Materiaalin väri ei saa olla tyhjä? Viesti peräisin LisätietojaGUIControllerista");
           return;
       }
       if (!editMateriaali.getText().equals("") && !editVari.getText().equals("")) uusiMateriaali();
       if (!editTyovaline.getText().equals("")) uusiTyovaline();
       for (TextField edit : edits) edit.clear();
       // for (ComboBoxChooser<String> box : boxes) box.getSelectionModel().select(0);
    }


    @FXML
    private void handleValmis() {
        handleLisaa();
        theKasityologi = null;
        projektiKohdalla = null;
        ModalController.closeStage(labelVirhe);
    }


    @FXML
    private void handlePeruuta() {
        theKasityologi = null;
        projektiKohdalla = null;
        ModalController.closeStage(labelVirhe);
    }
    
    @FXML
    private void handleMateriaali() {
        editMateriaali.setText(cbMateriaali.getSelectedText());
    }

    @FXML
    private void handleVari() {
        editVari.setText(cbVari.getSelectedText());
    }

    @FXML
    private void handleTyovaline() {
        editTyovaline.setText(cbTyovaline.getSelectedText());
    }



    // ===============================================================

    private Kasityologi theKasityologi;
    private Projekti projektiKohdalla = KasityologiGUIController.getProjektiKohdalla();
    private Materiaali uusiMat = new Materiaali(projektiKohdalla.getProjektiId());
    private Tyovaline uusiVal = new Tyovaline(projektiKohdalla.getProjektiId());

    private TextField[] edits;

    private void alusta() {
        
        cbMateriaali.clear();
        cbVari.clear();
        
        for (Materiaali mat : theKasityologi.annaKaikkiMateriaalit()) {
            cbMateriaali.add(mat.getNimi(), null); 
            cbVari.add(mat.getVari(), null);
        }
        for (Tyovaline val : theKasityologi.annaKaikkiTyovalineet()) // annaKaikkiTyovalineet
            cbTyovaline.add(val.getNimi(), null);

        edits = new TextField[] { editMateriaali, editVari, editTyovaline };
        int i = 0;
        for (TextField edit : edits) {
            final int k = ++i;
            edit.setOnKeyReleased(
                    e -> kasitteleMuutos(k, (TextField) (e.getSource())));
        }
        

    }


    @Override
    public Kasityologi getResult() {
        return theKasityologi;
    }


    @Override
    public void handleShown() {
        editMateriaali.requestFocus();
    }


    @Override
    public void setDefault(Kasityologi oletus) {
        theKasityologi = oletus;
        alusta();
    }


    /**
     * Tekee uuden tyhjän materiaalin editointia varten
     */
    private void uusiMateriaali() {
        if (projektiKohdalla == null)
            return;
        uusiMat.setNimi(editMateriaali.getText());
        String virhe = uusiMat.setVari(editVari.getText());
        labelVirhe.setText(virhe);
        theKasityologi.lisaa(uusiMat);
    }


    /**
     * Tekee uuden tyhjän työvälineen editointia varten
     */
    private void uusiTyovaline() {

        if (projektiKohdalla == null)
            return;
        uusiVal.setNimi(editTyovaline.getText());
        theKasityologi.lisaa(uusiVal);
        // KasityologiGUIController.hae(projektiKohdalla.getProjektiId());
    }


    private void naytaVirhe(String virhe) {
        if (virhe == null || virhe.isEmpty()) {
            labelVirhe.setText("");
            labelVirhe.getStyleClass().removeAll("virhe");
            return;
        }
        labelVirhe.setText(virhe);
        labelVirhe.getStyleClass().add("virhe");
    }


    /**
     * Käsitellään projektiin tullut muutos
     * @param edit muuttunut kenttä
     */
    private void kasitteleMuutos(int k, TextField edit) {
        if (projektiKohdalla == null)
            return;
        String s = edit.getText();
        String virhe = null;
        switch (k) {
        case 0:
            virhe = uusiMat.setNimi(s);
            break;
        case 1:
            virhe = uusiMat.setVari(s);
            break;
        case 2:
            virhe = uusiVal.setNimi(s);
            break;
        default:
        }
        if (virhe == null) {
            Dialogs.setToolTipText(edit, "");
            edit.getStyleClass().removeAll("virhe");
            naytaVirhe(virhe);
        } else {
            Dialogs.setToolTipText(edit, virhe);
            edit.getStyleClass().add("virhe");
            naytaVirhe(virhe);
        }
    }


    /**
     * Luodaan projektin kysymisdialogi ja palautetaan sama tietue muutettuna tai null
     * TODO: korjattava toimimaan
     * @param modalityStage mille ollaan modaalisia, null = sovellukselle
     * @param oletus mitä dataan näytetään oletuksena
     * @return null jos painetaan Cancel, muuten täytetty tietue
     */
    public static Kasityologi kysyKasityologi(Stage modalityStage,
            Kasityologi oletus) {
        return ModalController
                .<Kasityologi, LisatietojaGUIController> showModal(
                        LisatietojaGUIController.class
                                .getResource("LisatietojaGUIView.fxml"),
                        "Käsityölogi", modalityStage, oletus, null);
    }

}
