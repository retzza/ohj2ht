 
package fxKasityologi;
    
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import kasityologi.Kasityologi;

/**
 * @author Reetta Koskelo, reetta.m.koskelo@student.jyu.fi
 * @version Jan 15, 2020
 * 
 * Pääohjelma Käsityölogi-ohjelman käyttämiseksi
 */
public class KasityologiMain extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            final FXMLLoader ldr = new FXMLLoader(getClass().getResource("KasityologiGUIView.fxml"));
            final Pane root = (Pane)ldr.load();
            final KasityologiGUIController kasityologiCtrl = (KasityologiGUIController)ldr.getController();

            final Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("kasityologi.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.setTitle("Kasityologi");
            
            Kasityologi kasityologi = new Kasityologi(); 
            kasityologiCtrl.setKasityologi(kasityologi);
            
            // Platform.setImplicitExit(false); // tätä ei kai saa laittaa

            primaryStage.setOnCloseRequest((event) -> {
                    if ( !kasityologiCtrl.voikoSulkea() ) event.consume();
                });
            
            primaryStage.show();
            if ( !kasityologiCtrl.avaa() ) Platform.exit();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Käynnistetään käyttöliittymä 
     * @param args komentorivin parametrit
     */
    public static void main(String[] args) {
        launch(args);
    }
}