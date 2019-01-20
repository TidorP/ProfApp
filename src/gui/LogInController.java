package gui;

import domain.Student;
import domain.Tema;
import javafx.animation.Animation;
import javafx.animation.TranslateTransition;
import javafx.beans.InvalidationListener;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import repository.StudentRepository;
import repository.TemaRepository;
import security.Enc;
import service.Service;
import service.ServiceNote;
import service.ServiceTema;
import validate.StudentValidator;
import validate.TemaValidator;
import validate.Validator;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

import static javafx.scene.media.MediaPlayer.INDEFINITE;

public class LogInController {
    private Enc security;
    private ServiceNote service;
    private Service serviceSt;
    private ServiceTema serviceTema;
    @FXML
    private ImageView imageView;
    @FXML
    private ImageView keyImg;
    @FXML
    private TextField usernameText;
    @FXML
    private TextField passwordText;
    @FXML
    private TextField adminCode;
    @FXML
    private Text hidingText;
    @FXML
    private Button buttonAdmin;
    @FXML
    private Button buttonRegister;
    @FXML
    private Button buttonGo;
    @FXML
    private AnchorPane anchor;
    @FXML
    private RadioButton radioB;
    @FXML
    private MediaView mv;

    private Stage thisStage;
    private Thread thread;
    private AudioClip audio;

    public void setInfo(String fName,ServiceNote service,Service serviceSt,ServiceTema serviceTema,Stage stage)
    {
        thisStage=stage;
        security=new Enc(fName);
        Image image = new Image("file:C:\\Users\\ptido\\IdeaProjects\\Lab3\\student's folders\\Untitled.png");
        imageView.setImage(image);
        Image imageKey = new Image("file:C:\\Users\\ptido\\IdeaProjects\\Lab3\\student's folders\\Key.png");
        keyImg.setImage(imageKey);
        this.service= service;
        this.serviceSt=serviceSt;
        this.serviceTema=serviceTema;
        hidingText.setVisible(false);
        hidingText.setFill(Color.RED);


        buttonGo.getStyleClass().add("buttonGoStyle");
        buttonGo.getStyleClass().add("buttonGoStyleH");
        buttonRegister.getStyleClass().add("buttonRegisterStyle");
        buttonRegister.getStyleClass().add("buttonRegisterStyleH");
        buttonAdmin.getStyleClass().add("buttonAdminStyle");



        Circle cir=new Circle();
        //cir.setFill(Color.AQUAMARINE);
        Image img=new Image("file:C:\\Users\\ptido\\IdeaProjects\\Lab3\\src\\gui\\Imgs\\lock2.png");
        cir.setFill(new ImagePattern(img));
        cir.setRadius(35);
        cir.setLayoutX(20);
        cir.setLayoutY(282);
        anchor.getChildren().add(cir);
        Circle cir2=new Circle();
        //cir2.setFill(Color.AQUAMARINE);
        cir2.setFill(new ImagePattern(img));
        cir2.setRadius(35);
        cir2.setLayoutX(820);
        cir2.setLayoutY(282);
        anchor.getChildren().add(cir2);

        TranslateTransition transition=new TranslateTransition();
        transition.setDuration(Duration.seconds(3));
        transition.setToX(810);//810
        transition.setToY(370);//370
        transition.setAutoReverse(true);
        transition.setCycleCount(Animation.INDEFINITE);
        transition.setNode(cir);

        TranslateTransition transition2=new TranslateTransition();
        transition2.setDuration(Duration.seconds(3));
        transition2.setToX(-800);
        transition2.setToY(375);
        transition2.setAutoReverse(true);
        transition2.setCycleCount(Animation.INDEFINITE);
        transition2.setNode(cir2);

        transition.play();
        transition2.play();
        playMusic();

        //setVideo();


    }
    private void playMusic()
    {
        final Task task = new Task() {

            @Override
            protected Object call() throws Exception {
                int s = INDEFINITE;
                audio = new AudioClip(getClass().getResource("Imgs/remix.mp3").toExternalForm());
                audio.setVolume(0.5f);
                audio.setCycleCount(100);
                audio.play();
                return null;
            }
        };
        thread = new Thread(task);
        thread.start();

    }
    @FXML
    private void stopMusicHandler()
    {
        if(radioB.isSelected()) {
            audio.stop();
            thread.interrupt();
        }
        else
            playMusic();
    }

    @FXML
    public void loginHandler()
    {
        if(security.check(usernameText.getText(),passwordText.getText())) {

            if(usernameText.getText().contains("Prof")) {
                thisStage.close();
                try {
                    showProfMenu();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else
            {
                setVideo();
                //showStudentMenu();
            }
        }
        else
            hidingText.setVisible(true);

    }
    @FXML
    public void registerHandler()
    {
        try {
            Stage stage = new Stage();
            stage.setTitle("Register");

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(ControllerStudents.class.getResource("addRegister.fxml"));

            Parent root = loader.load();
            stage.setScene(new Scene(root));
            AddRegisterController ctrl = loader.getController();

            ctrl.setInfo(service,stage,security);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void showProfMenu() throws IOException {
        Stage primaryStage=new Stage();
        //Parent root = FXMLLoader.load(getClass().getResource("gui/mainApplicationStudents.fxml"));
        FXMLLoader loader= new FXMLLoader();
        loader.setLocation(Main.class.getResource("mainApp.fxml"));

        Parent root = loader.load();

        ControllerMain ctrl = loader.getController();
        ctrl.setImg(service,serviceSt,serviceTema);
        primaryStage.setTitle("Main Menu");

        Scene scene = new Scene(root);
        root.setId("profPane");
        scene.getStylesheets().addAll(this.getClass().getResource("profStyle.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    private void setVideo()
    {
        //anchor.setMinWidth(1000);
        //final MediaPlayer videoPlayer = new MediaPlayer(
        //    new Media(new File("file:C:\\Users\\ptido\\IdeaProjects\\Lab3\\src\\gui\\Imgs\\Merged_one_two.mp4").toURI().toString()));

        MediaPlayer videoPlayer = new MediaPlayer( new Media(getClass().getResource("Imgs/Merged2.mp4").toExternalForm()));
        //MediaView mediaView = new MediaView(player);
        MediaView mv = new MediaView(videoPlayer);
        DoubleProperty mvw = mv.fitWidthProperty();
        DoubleProperty mvh = mv.fitHeightProperty();
        //mvw.bind(Bindings.selectDouble(mv.sceneProperty(), "width"));
        //mvh.bind(Bindings.selectDouble(mv.sceneProperty(), "height"));
        InvalidationListener resizeMediaView = observable -> {
            mv.setFitWidth(anchor.getWidth());
            mv.setFitHeight(anchor.getHeight());
            //mv.setPreserveRatio(true);

            // After setting a big fit width and height, the layout bounds match the video size. Not sure why and this feels fragile.
            //Bounds actualVideoSize = mv.getLayoutBounds();
            //mv.setX((mv.getFitWidth() - actualVideoSize.getWidth()) /2);
            //mv.setY((mv.getFitHeight() - actualVideoSize.getHeight())/2);
        };

        anchor.heightProperty().addListener(resizeMediaView);
        anchor.widthProperty().addListener(resizeMediaView);


//        thisStage.close();
//        Stage stageV=new Stage();
//        StackPane root = new StackPane();
//        root.getChildren().add(mv);
//        //anchor.getChildren().add(mv);
//
//        Scene scene = new Scene(root, 1280, 738);
//
//        stageV.setScene(scene);
//        stageV.show();

        videoPlayer.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                thisStage.close();
                try {
                    showStudentMenu();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        videoPlayer.setOnReady(new Runnable() {
            @Override
            public void run() {

                videoPlayer.play();
            }
        });
        videoPlayer.setOnReady(()->thisStage.sizeToScene());
        anchor.getChildren().add(mv);
        videoPlayer.play();

    }
    @FXML
    public void showStudentMenu() throws IOException {
        Stage primaryStage=new Stage();
        //Parent root = FXMLLoader.load(getClass().getResource("gui/mainApplicationStudents.fxml"));
        FXMLLoader loader= new FXMLLoader();
        loader.setLocation(Main.class.getResource("studentsApp.fxml"));

        Parent root = loader.load();

        StudentsAppController ctrl = loader.getController();
        ctrl.setService(service,usernameText.getText(),primaryStage);
        primaryStage.setTitle("Main Menu Students");

        Scene scene = new Scene(root);
        root.setId("stPane");
        scene.getStylesheets().addAll(this.getClass().getResource("mainStyleSt.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public void showAdminMenu() throws IOException {
        Stage primaryStage=new Stage();
        //Parent root = FXMLLoader.load(getClass().getResource("gui/mainApplicationStudents.fxml"));
        FXMLLoader loader= new FXMLLoader();
        loader.setLocation(Main.class.getResource("adminView.fxml"));

        Parent root = loader.load();

        AdminViewController ctrl = loader.getController();
        ctrl.setInfo(service,primaryStage,security);
        primaryStage.setTitle("Menu Admin");

        Scene scene = new Scene(root);
        root.setId("adminPane");
        scene.getStylesheets().addAll(this.getClass().getResource("adminStyle.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    @FXML
    public void registerAdmin()
    {
        if(security.check("A",adminCode.getText())) {
            thisStage.close();
            try {
                showAdminMenu();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }



}
