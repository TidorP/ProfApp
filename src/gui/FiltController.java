package gui;

import domain.Nota;
import domain.Student;
import domain.StudentGrades;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Pair;
import service.Service;
import service.ServiceNote;
import sun.awt.datatransfer.DataTransferer;
import utils.Pairr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class FiltController {
    private ServiceNote service;
    private ObservableList<StudentGrades> model;
    @FXML
    private TableView<StudentGrades> tabel;
    @FXML
    private TableColumn<StudentGrades,String> grupaC;
    @FXML
    private TableColumn<StudentGrades,Integer> tema1C;
    @FXML
    private TableColumn<StudentGrades,Integer> tema2C;
    @FXML
    private TableColumn<StudentGrades,Integer> tema3C;
    @FXML
    private TableColumn<StudentGrades,Integer> tema4C;
    @FXML
    private TableColumn<StudentGrades,Integer> tema5C;
    @FXML
    private TableColumn<StudentGrades,Integer> tema6C;
    @FXML
    private TableColumn<StudentGrades,Integer> tema7C;
    @FXML
    private TableColumn<StudentGrades,Integer> tema8C;
    @FXML
    private TableColumn<StudentGrades,Integer> tema9C;
    @FXML
    private TableColumn<StudentGrades,Integer> tema10C;
    @FXML
    private TableColumn<StudentGrades,Integer> tema11C;
    @FXML
    private TableColumn<StudentGrades,Integer> tema12C;

    private Stage stage;

    public FiltController(){

    }

    public void setInfo(List<Student> sts, ServiceNote service, Stage stage) {
        this.service = service;
        this.stage = stage;

        List<StudentGrades> init_model=new ArrayList<>();
        for(Student st:sts)
        {
            List<Pair<Integer,Integer>> note=service.getNoteForStudent(st.getNume());
            Collections.sort(note, new Comparator<Pair<Integer,Integer>>() {
                @Override
                public int compare(Pair<Integer,Integer> h1, Pair<Integer, Integer> h2) {
                    if(h1.getValue()>h2.getValue())
                        return 1;
                    else
                        return -1;
                }
            });
            StudentGrades studentGrades=new StudentGrades(st.getNume());
            if(note.size()!=0) {
                studentGrades.setGradeT1(note.get(0).getKey());
                studentGrades.setGradeT2(note.get(1).getKey());
                studentGrades.setGradeT3(note.get(2).getKey());
                studentGrades.setGradeT4(note.get(3).getKey());
                studentGrades.setGradeT5(note.get(4).getKey());
                studentGrades.setGradeT6(note.get(5).getKey());
                studentGrades.setGradeT7(note.get(6).getKey());
                studentGrades.setGradeT8(note.get(7).getKey());
                studentGrades.setGradeT9(note.get(8).getKey());
                studentGrades.setGradeT10(note.get(9).getKey());
                studentGrades.setGradeT11(note.get(10).getKey());
                studentGrades.setGradeT12(note.get(11).getKey());
            }
            else
            {
                studentGrades.setGradeT1(1);
                studentGrades.setGradeT2(1);
                studentGrades.setGradeT3(1);
                studentGrades.setGradeT4(1);
                studentGrades.setGradeT5(1);
                studentGrades.setGradeT6(1);
                studentGrades.setGradeT7(1);
                studentGrades.setGradeT8(1);
                studentGrades.setGradeT9(1);
                studentGrades.setGradeT10(1);
                studentGrades.setGradeT11(1);
                studentGrades.setGradeT12(1);
            }
                init_model.add(studentGrades);

        }
        this.model =FXCollections.observableList(StreamSupport.stream(init_model.spliterator(), false).collect(Collectors.toList()));
        grupaC.setText(Integer.toString(sts.get(0).getGrupa()));
        loadDataHandler();
    }
    @FXML
    private void loadDataHandler(){

        grupaC.setCellValueFactory(new PropertyValueFactory<StudentGrades,String>("student"));
        tema1C.setCellValueFactory(new PropertyValueFactory<StudentGrades,Integer>("gradeT1"));
        tema2C.setCellValueFactory(new PropertyValueFactory<StudentGrades,Integer>("gradeT2"));
        tema3C.setCellValueFactory(new PropertyValueFactory<StudentGrades,Integer>("gradeT3"));
        tema4C.setCellValueFactory(new PropertyValueFactory<StudentGrades,Integer>("gradeT4"));
        tema5C.setCellValueFactory(new PropertyValueFactory<StudentGrades,Integer>("gradeT5"));
        tema6C.setCellValueFactory(new PropertyValueFactory<StudentGrades,Integer>("gradeT6"));
        tema7C.setCellValueFactory(new PropertyValueFactory<StudentGrades,Integer>("gradeT7"));
        tema8C.setCellValueFactory(new PropertyValueFactory<StudentGrades,Integer>("gradeT8"));
        tema9C.setCellValueFactory(new PropertyValueFactory<StudentGrades,Integer>("gradeT9"));
        tema10C.setCellValueFactory(new PropertyValueFactory<StudentGrades,Integer>("gradeT10"));
        tema11C.setCellValueFactory(new PropertyValueFactory<StudentGrades,Integer>("gradeT11"));
        tema12C.setCellValueFactory(new PropertyValueFactory<StudentGrades,Integer>("gradeT12"));
        tabel.setItems(this.model);
    }



}
