//package controller;
//
//import domain.Student;
//import javafx.collections.ObservableList;
//import service.Service;
//import utils.ChangeEventType;
//import utils.Observer;
//import utils.StudentEvent;
//import validate.ValidationException;
//
//public class ControllerStudent implements Observer<StudentEvent> {
//    private Service service;
//    private ObservableList<Student> observableList;
//
//    public ControllerStudent(Service service) {
//        this.service = service;
//        this.service.addObserver(this);
//    }
//
//    @Override
//    public void update(StudentEvent studentEvent) {
//        if(studentEvent.getType() == ChangeEventType.ADD){
//            observableList.add(studentEvent.getData());
//        }
//    }
//
//    public ObservableList getList(){
//        return observableList;
//    }
//
//    public void addStudent(int id, String nume, int grupa, String email, String cadruDidactic) throws ValidationException {
//        Student student = new Student(id, nume, grupa, email, cadruDidactic);
//        service.forControllerSaveS(student);
//    }
//    public void deleteStudent(int id)
//    {
//        service.deleteS(id);
//    }
//    public void updStudent(int id, String nume, int grupa, String email, String cadruDidactic) throws ValidationException {
//        Student student = new Student(id, nume, grupa, email, cadruDidactic);
//        service.forControllerUpdateS(student);
//    }
//}
//
//



