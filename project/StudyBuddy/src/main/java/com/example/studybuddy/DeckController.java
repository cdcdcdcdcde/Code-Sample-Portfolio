package com.example.studybuddy;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;


public class DeckController extends SceneController{
    private Stage primaryStage;
    private Stage stage;
    private Scene scene;
    private DataHandlingCard ds = DataHandlingCard.getInstance();
    private ArrayList<Deck> decks;
    private Deck selectedDeck;
    @FXML
    private Label label;
    @FXML
    private ListView<String> deckList;
    public void initialize() {
        this.decks = this.ds.getDecks();

        if (this.decks != null && !this.decks.isEmpty()) {
            System.out.println("Decks from file " + decks);
            ArrayList<String> titles = new ArrayList<String>();
            for (Deck deck : this.decks) {
                titles.add(deck.getTitleOfDeck());
            }
            ObservableList<String> observableList = FXCollections.observableList(titles);
            deckList.getItems().setAll(observableList);
        } else {
            System.out.println("No decks found");
            deckList.setPlaceholder(new Label("Add a deck to start learning"));
        }

        attachEventHandlers();
    }
    public void attachEventHandlers() {
        deckList.setOnMouseClicked(event -> {
            if (this.decks != null) {
                String title = deckList.getSelectionModel().getSelectedItem();
                this.selectedDeck = this.decks.stream().filter(deck -> title.equals(deck.getTitleOfDeck())).findAny()
                        .orElse(null);
            } else {
                System.out.println("No Decks Available");
            }
        });
    }
    public void addDeck(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("addEditDeck.fxml"));
            Parent root = loader.load();
            AddEditDeckController addEditDeckController = loader.getController();
            Stage modal = new Stage();
            modal.setTitle("Add Deck");
            modal.initOwner(primaryStage);
            modal.initModality(Modality.APPLICATION_MODAL);
            modal.setScene(new Scene(root));
            addEditDeckController.initModal(modal);
            modal.showAndWait();
            initialize();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void onEditDeck(ActionEvent event) {
        if (this.selectedDeck != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("addEditDeck.fxml"));
                Parent root = loader.load();
                AddEditDeckController addEditDeckController = loader.getController();
                Stage modal = new Stage();
                modal.setTitle("Edit Deck");
                modal.initOwner(primaryStage);
                modal.initModality(Modality.APPLICATION_MODAL);
                modal.setScene(new Scene(root));
                addEditDeckController.initModal(modal, this.selectedDeck);
                modal.showAndWait();
                initialize();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void onDeleteDeck(ActionEvent event) {
        if (this.selectedDeck != null) {
            this.decks.remove(this.selectedDeck);
            this.ds.setDecks(this.decks);
            initialize();

        } else {
            System.out.println("No Deck Selected");
        }

    }
    public void onShowDeck(ActionEvent event) {
        if (this.selectedDeck != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("player.fxml"));
            try {
                Parent root = loader.load();
                PlayerController controller = loader.getController();
                controller.initPlayer(selectedDeck);
                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("No selected deck Available");
        }
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

}
