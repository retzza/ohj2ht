package fxKasityologi;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import kasityologi.Projekti;

/**
 * @author Reetta Koskelo, reetta.m.koskelo@student.jyu.fi
 * @version Feb 7, 2020
 *
 */
public class ProjektiDialogGUIController
        implements ModalControllerInterface<Projekti>, Initializable {

    @FXML
    private Label labelVirhe;

    @FXML
    private TextField editNimi;

    @FXML
    private TextField editTyyppi;

    @FXML
    private TextField editKenelle;

    @FXML
    private DatePicker editAloituspv;

    @FXML
    private DatePicker editLopetuspv;

    @FXML
    private Slider editValmiusaste;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        alusta();
    }




    @FXML
    private void handleOK() {
        if (projektiKohdalla != null
                && projektiKohdalla.getNimi().trim().equals("")) {
            naytaVirhe("Nimi ei saa olla tyhjä");
            return;
        }
        ModalController.closeStage(labelVirhe);
    }


    @FXML
    private void handlePeruuta() {
        projektiKohdalla = null;
        ModalController.closeStage(labelVirhe);
    }

    // ===================================================================
    // Tämän jälkeen ei suoraan käyttöliittymään liittyviä juttuja

    private Projekti projektiKohdalla;

    private TextField[] edits1;
    private DatePicker[] edits2;
    private Slider[] edits3;

    private void alusta() {
        edits1 = new TextField[] { editNimi, editTyyppi, editKenelle };
        int i = 0;
        for (TextField edit : edits1) {
            final int k = ++i;
            edit.setOnKeyReleased(e -> kasitteleMuutosProjektiin(k,
                    (TextField) (e.getSource())));
        }
        edits2 = new DatePicker[] { editAloituspv, editLopetuspv };
        for (DatePicker edit : edits2) {
            final int k = ++i;
            /*
            edit.setOnMouseClicked(e -> kasitteleMuutosProjektiin(k, (Slider) (e.getSource())));
            edit.setOnMouseDragEntered(e -> kasitteleMuutosProjektiin(k, (Slider) (e.getSource())));
            edit.setOnMouseDragExited(e -> kasitteleMuutosProjektiin(k, (Slider) (e.getSource())));
            edit.setOnMouseDragged(e -> kasitteleMuutosProjektiin(k, (Slider) (e.getSource())));
            edit.setOnMouseDragOver(e -> kasitteleMuutosProjektiin(k, (Slider) (e.getSource())));
            edit.setOnMouseDragReleased(e -> kasitteleMuutosProjektiin(k, (Slider) (e.getSource())));
            edit.setOnMouseEntered(e -> kasitteleMuutosProjektiin(k, (Slider) (e.getSource())));
            edit.setOnMouseExited(e -> kasitteleMuutosProjektiin(k, (Slider) (e.getSource())));
            */
            edit.setOnAction(e -> kasitteleMuutosProjektiin(k, (DatePicker) (e.getSource())));
       }
        edits3 = new Slider[] { editValmiusaste };
        for (Slider edit : edits3) {
            final int k = ++i;
            edit.setOnMouseReleased(e -> kasitteleMuutosProjektiin(k,
                    (Slider) (e.getSource())));
        }
    }


    @Override
    public Projekti getResult() {
        return projektiKohdalla;
    }


    @Override
    public void setDefault(Projekti oletus) {
        projektiKohdalla = oletus;
        naytaProjektiDialogissa(projektiKohdalla);
    }


    @Override
    public void handleShown() {
        editNimi.requestFocus();
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
    private void kasitteleMuutosProjektiin(int k, TextField edit) {
        if (projektiKohdalla == null)
            return;
        String s = edit.getText();
        String virhe = null;
        switch (k) {
        case 1:
            virhe = projektiKohdalla.setNimi(s);
            break;
        case 2:
            virhe = projektiKohdalla.setTyyppi(s);
            break;
        case 3:
            virhe = projektiKohdalla.setKenelle(s);
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
     * Käsitellään projektiin tullut muutos
     * @param k kentän indeksi
     * @param edit muuttunut kenttä
     */
    protected void kasitteleMuutosProjektiin(int k, DatePicker edit) {
        if (projektiKohdalla == null)
            return;
        LocalDate l = edit.getValue();
        String virhe = null;
        switch (k) {
        case 4:
            virhe = projektiKohdalla.setAloituspv(l);
            break;
        case 5:
            virhe = projektiKohdalla.setLopetuspv(l);
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
     * Käsitellään projektiin tullut muutos
     * @param k kentän indeksi
     * @param edit muuttunut kenttä
     */
    protected void kasitteleMuutosProjektiin(int k, Slider edit) {
        if (projektiKohdalla == null)
            return;
        double d = edit.getValue();
        String virhe = null;
        switch (k) {
        case 6:
            virhe = projektiKohdalla.setValmiusaste(d);
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
    * Näytetään projektin tiedot TextField- ja ProgressBar-komponentteihin
    * @param edits1 taulukko jossa tekstikenttiä
    * @param edits2 taulukko jossa Slidereitä
    * @param projekti näytettävä projekti
    */
    public static void naytaProjektiPaaikkunassa(TextField[] edits1,
            Slider[] edits2, Projekti projekti) {
        if (projekti == null)
            return;
        edits1[0].setText(projekti.getNimi());
        edits1[1].setText(projekti.getTyyppi());
        edits1[2].setText(projekti.getKenelle());
        edits1[3].setText(projekti.getAloituspv().toString());
        edits1[4].setText(projekti.getLopetuspv().toString());
        edits2[0].setValue(projekti.getValmiusaste());
    }

    /**
    * Näytetään projektin tiedot TextField-, DatePicker- ja Slider-komponentteihin
    * @param edits1 taulukko jossa tekstikenttiä
    * @param edits2 taulukko jossa DatePickereitä
     * @param edits3 taulukko jossa Slidereitä
    * @param projekti näytettävä projekti
    */
    public static void naytaProjektiDialogissa(TextField[] edits1,
            DatePicker[] edits2, Slider[] edits3, Projekti projekti) {
        if (projekti == null)
            return;
        edits1[0].setText(projekti.getNimi());
        edits1[1].setText(projekti.getTyyppi());
        edits1[2].setText(projekti.getKenelle());
        edits2[0].setValue(projekti.getAloituspv());
        edits2[1].setValue(projekti.getLopetuspv());
        edits3[0].setValue(projekti.getValmiusaste());
    }


    /**
    * Näytetään projektin tiedot TextField-, DatePicker- ja Slider-komponentteihin
     * @param projekti näytettävä projekti
     */

    public void naytaProjektiDialogissa(Projekti projekti) {
        naytaProjektiDialogissa(edits1, edits2, edits3, projekti);
    }

    /**
    * Näytetään projektin tiedot TextField- Slider-komponentteihin
     * @param projekti näytettävä projekti
     */

    public void naytaProjektiPaaikkunassa(Projekti projekti) {
        naytaProjektiPaaikkunassa(edits1, edits3, projekti);
    }


    /**
      * Luodaan projektin kysymisdialogi ja palautetaan sama tietue muutettuna tai null
      * TODO: korjattava toimimaan
      * @param modalityStage mille ollaan modaalisia, null = sovellukselle
      * @param oletus mitä dataan näytetään oletuksena
      * @return null jos painetaan Cancel, muuten täytetty tietue
      */
    public static Projekti kysyProjekti(Stage modalityStage, Projekti oletus) {
        return ModalController
                .<Projekti, ProjektiDialogGUIController> showModal(
                        ProjektiDialogGUIController.class
                                .getResource("ProjektiDialogGUIView.fxml"),
                        "Käsityölogi", modalityStage, oletus, null);
    }

}
