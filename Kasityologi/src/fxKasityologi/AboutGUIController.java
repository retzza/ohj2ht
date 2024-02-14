package fxKasityologi;

import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * @author Reetta Koskelo, reetta.m.koskelo@student.jyu.fi
 * @version Feb 7, 2020
 *
 */
public class AboutGUIController implements ModalControllerInterface<String> {
    
    @FXML private Label labelVirhe;

    @Override
    public String getResult() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void handleShown() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setDefault(String arg0) {
        // TODO Auto-generated method stub
        
    }
    
    @FXML private void handleOK() {
        ModalController.closeStage(labelVirhe);
    }  

}
