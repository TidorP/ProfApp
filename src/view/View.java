//package view;
//
//import controller.ControllerStudent;
//import domain.Student;
//import javafx.scene.Node;
//import javafx.scene.control.*;
//import javafx.scene.control.cell.PropertyValueFactory;
//import javafx.scene.layout.BorderPane;
//import javafx.scene.layout.GridPane;
//import javafx.scene.layout.HBox;
//import javafx.scene.layout.StackPane;
//import validate.ValidationException;
//
//public class View {
//    private TableView<Student> tableView;
//    private TextField idText;
//    private TextField numeText;
//    private TextField grupaText;
//    private TextField emailText;
//    private TextField cadruText;
//
//    private ControllerStudent controller;
//
//    //StackPane
//    //GreedPane
//    //Box
//    public View(ControllerStudent controller){
//        tableView = new TableView<>();
//        this.controller = controller;
//    }
//
//    public BorderPane getView(){
//        BorderPane borderPane = new BorderPane();
//        borderPane.setLeft(createTable());
//        borderPane.setRight(createStudent());
//        return borderPane;
//    }
//
//    private void initTableView(){
//        tableView.setItems(controller.getList());
//        TableColumn<Student, Integer> idColumn = new TableColumn<>("Id");
//        TableColumn<Student, String> numeColumn = new TableColumn<>("Nume");
//        TableColumn<Student, Integer> grupaColumn = new TableColumn<>("Grupa");
//        TableColumn<Student, String> emailColumn = new TableColumn<>("Email");
//        TableColumn<Student, String> cadruColumn = new TableColumn<>("Cadru Didactic");
//
//        tableView.getColumns().addAll(idColumn, numeColumn, grupaColumn,emailColumn,cadruColumn);
//        idColumn.setCellValueFactory(new PropertyValueFactory<Student, Integer>("id"));
//        numeColumn.setCellValueFactory(new PropertyValueFactory<Student, String>("nume"));
//        grupaColumn.setCellValueFactory(new PropertyValueFactory<Student, Integer>("grupa"));
//        emailColumn.setCellValueFactory(new PropertyValueFactory<Student,String>("email"));
//        cadruColumn.setCellValueFactory(new PropertyValueFactory<Student,String>("cadruDidactic"));
//        tableView.getSelectionModel().selectedItemProperty().addListener((observer, oldData, newData)-> showDetails(newData));
//    }
//
//    private void showDetails(Student st){
//        if(st!=null){
//            idText.setText(Integer.toString(st.getId()));
//            numeText.setText(st.getNume());
//            grupaText.setText(Integer.toString(st.getGrupa()));
//            emailText.setText(st.getEmail());
//            cadruText.setText(st.getCadruDidactic());
//        }
//    }
//
//    private StackPane createTable(){
//        StackPane stackPane = new StackPane();
//        stackPane.getChildren().add(tableView);
//        initTableView();
//        return stackPane;
//    }
//
//    private GridPane createStudent(){
//        GridPane gridPane = new GridPane();
//
//        gridPane.add(new Label("Id"), 0, 0);
//        gridPane.add(idText = new TextField(), 1, 0);
//
//        gridPane.add(new Label("Nume"), 0, 1);
//        gridPane.add(numeText = new TextField(), 1, 1);
//
//        gridPane.add(new Label("Grupa"), 0, 2);
//        gridPane.add(grupaText = new TextField(), 1, 2);
//
//        gridPane.add(new Label("Email"), 0, 3);
//        gridPane.add(emailText = new TextField(), 1, 3);
//
//        gridPane.add(new Label("Cadru"), 0, 4);
//        gridPane.add(cadruText = new TextField(), 1, 4);
//
//        HBox buttonsBox = new HBox();
//        Button add = new Button("Add");
//        add.setOnAction(event -> {
//            this.addHandler();
//        });
//        buttonsBox.getChildren().add(add);
//        Button clearAll = new Button("Clear All");
//        clearAll.setOnAction(event -> {
//            this.clearAllHandler();
//        });
//        buttonsBox.getChildren().add(clearAll);
//
//        Button update = new Button("Update");
//        update.setOnAction(event -> {
//            this.updateHandler();
//        });
//        buttonsBox.getChildren().add(update);
//        Button delete = new Button("Delete");
//        delete.setOnAction(event -> {
//            this.deleteHandler();
//        });
//        buttonsBox.getChildren().add(delete);
//        gridPane.add(buttonsBox, 0, 4,2, 1);
//        return gridPane;
//    }
//
//    private void addHandler(){
//        try {
//            controller.addStudent(Integer.parseInt(idText.getText()),numeText.getText(),Integer.parseInt(grupaText.getText()),emailText.getText(),cadruText.getText());
//        } catch (ValidationException e) {
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setHeaderText("Student error");
//            alert.setContentText(e.getMessage());
//            alert.show();
//        }
//    }
//    private Node deleteHandler(){return null;}
//    private Node clearAllHandler(){return null;}
//    private Node updateHandler(){return null;}
//}
