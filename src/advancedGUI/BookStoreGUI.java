package advancedGUI;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.*;
import java.util.*;

public class BookStoreGUI extends Application {

    private ListView<Book> availableBooksListView;
    private ListView<Book> shoppingCartListView;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        // Create Book lists
        availableBooksListView = new ListView<>();
        shoppingCartListView = new ListView<>();

        // Create File menu
        Menu fileMenu = new Menu("File");
        MenuItem loadBooksMenuItem = new MenuItem("Load Books");
        loadBooksMenuItem.setOnAction(e -> loadBooks(primaryStage));
        MenuItem exitMenuItem = new MenuItem("Exit");
        exitMenuItem.setOnAction(e -> System.exit(0));
        fileMenu.getItems().addAll(loadBooksMenuItem, exitMenuItem);

        // Create Shopping menu
        Menu shoppingMenu = new Menu("Shopping");
        MenuItem addMenuItem = new MenuItem("Add to Cart");
        addMenuItem.setOnAction(e -> addToCart());
        MenuItem removeMenuItem = new MenuItem("Remove from Cart");
        removeMenuItem.setOnAction(e -> removeFromCart());
        MenuItem clearMenuItem = new MenuItem("Clear Cart");
        clearMenuItem.setOnAction(e -> clearCart());
        MenuItem checkOutMenuItem = new MenuItem("Check Out");
        checkOutMenuItem.setOnAction(e -> checkOut(primaryStage));
        shoppingMenu.getItems().addAll(addMenuItem, removeMenuItem, clearMenuItem, checkOutMenuItem);

        // Create MenusBar and add menus
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(fileMenu, shoppingMenu);
        
        // Create labels
        Label welcomelbl = new Label("Welcome to the PFW Online Book Store!");
        welcomelbl.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        VBox topvbox = new VBox();
        topvbox.getChildren().addAll(menuBar, welcomelbl);
        Label lblBooks = new Label("Available Books");
        Label lblCart = new Label("Shopping Cart");
        VBox leftvbox = new VBox();
        leftvbox.getChildren().addAll(lblBooks, availableBooksListView);
        VBox rightvbox = new VBox();
        rightvbox.getChildren().addAll(lblCart, shoppingCartListView);
        
        // Create BorderPane and add Book lists and MenuBar
        BorderPane borderPane = new BorderPane();
        borderPane.setLeft(leftvbox);
        borderPane.setRight(rightvbox);
        borderPane.setTop(topvbox);

        // Set scene and show stage
        Scene scene = new Scene(borderPane, 500, 400);
        primaryStage.setTitle("Book Store Shopping Cart");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Tried to apply css styling but I'm not sure that I'm doing it correctly
        scene.getStylesheets().add("style.css");

    }

    private void loadBooks(Stage stage) {
        // Show file chooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Book List File");
        File initialDirectory = new File(System.getProperty("user.dir"));
        fileChooser.setInitialDirectory(initialDirectory);
        File selectedFile = fileChooser.showOpenDialog(stage);

        // Read file and add Books to availableBooksListView
        if (selectedFile != null) {
            try {
                Scanner scanner = new Scanner(selectedFile);
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    String[] parts = line.split(", ");
                    String title = parts[0];
                    double price = Double.parseDouble(parts[1]);
                    Book book = new Book(title, price);
                    availableBooksListView.getItems().add(book);
                }
                scanner.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void addToCart() {
        Book selectedBook = availableBooksListView.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            shoppingCartListView.getItems().add(selectedBook);
        }
    }

    private void removeFromCart() 
    {
        Book selectedBook = shoppingCartListView.getSelectionModel().getSelectedItem();
        if (selectedBook != null) 
        {
            shoppingCartListView.getItems().remove(selectedBook);
        }
    }

    private void clearCart() {
        shoppingCartListView.getItems().clear();
    }
    private void checkOut(Stage stage) {
    	double subTotal = 0;
    	double tax;
        double totalPrice;
        for (Book book : shoppingCartListView.getItems()) {
            subTotal += book.getPrice();
        }
        tax = subTotal*0.07;
        totalPrice = subTotal+tax;
        Label lblsubTotal = new Label(String.format("Subtotal: $%.2f", subTotal));
        Label lbltax = new Label(String.format("Tax: $%.2f", tax));
        Label lbltotal = new Label(String.format("Total: $%.2f", totalPrice));
        
        Button okButton = new Button("OK");
        okButton.setOnAction(e -> System.exit(0));
        
        VBox vbox = new VBox(4, lblsubTotal, lbltax, lbltotal, okButton);
        vbox.setPrefWidth(250);
        vbox.setPrefHeight(100);
        
        Scene scene = new Scene(vbox);
        stage.setTitle("Checkout");
        stage.setScene(scene);
        stage.show();

        shoppingCartListView.getItems().clear();
    }
}
