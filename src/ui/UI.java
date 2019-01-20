package ui;

import domain.Student;
import domain.Tema;
import service.Service;
import validate.*;

import java.io.IOException;
import java.util.Scanner;

public class UI implements CrudUI
{
    private Service service;

    /**
     *
     * @param service -business
     */
    public UI(Service service)
    {
        this.service = service;
    }

    /**
     * the menu
     */
    private void showMenuStudent()
    {
        System.out.println("1.Find");
        System.out.println("2.Save");
        System.out.println("3.Delete");
        System.out.println("4.Update");
        System.out.println("5.Print all");
        System.out.println("6.Back");

        System.out.println();
    }

    /**
     *
     * @param command -the command, input from the user
     */
    private void executeMenuStudent(String command)
    {
        if(command.equals("1"))
        {
            findOneStudent();
        }
        else
        if(command.equals("2"))
        {
            saveStudent();
        }
        else
        if(command.equals("3"))
        {
            deleteStudent();
        }
        else
        if(command.equals("4"))
        {
            updateStudent();
        }
        else
        if(command.equals("5"))
        {
            printAllStudent();
        }
        else
        if(command.equals("6"))
        {
            return;
        }
    }

    /**
     * Homework Menu
     */
    private void showMenuTema()
    {
        System.out.println("1.Find");
        System.out.println("2.Save");
        System.out.println("3.Delete");
        System.out.println("4.Update");
        System.out.println("5.Prelungire");
        System.out.println("6.Print all");
        System.out.println("7.Back");

        System.out.println();
    }

    /**
     * Adaugari de note
     */
    private void executeMenuNote()
    {
        System.out.println("Dam unui student o nota pentru o anumita tema..");
        Scanner keyboard = new Scanner(System.in);

        System.out.println("Student ID=");
        int id=keyboard.nextInt();

        System.out.println("Tema ID=");
        int temaId=keyboard.nextInt();

        System.out.println("Valoare=");
        int val=keyboard.nextInt();

        System.out.println("Saptamana=");
        int sapt=keyboard.nextInt();

        System.out.println("FeedBack=");
        String FeedBack;
        FeedBack=keyboard.next();

        try {
            int result=service.saveNota(id,temaId,val,sapt,FeedBack);
            if(result==-1)
                System.out.println("Un student poate sa obtina numai o singura nota pe o tema!!");
            else
                System.out.println("Nota maxima care se poate obtine este:  "+result);
        } catch (IOException | ValidationException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param command -input from the user
     */
    private void executeMenuTema(String command)
    {
        if(command.equals("1"))
        {
            findOneTema();
        }
        else
        if(command.equals("2"))
        {
            saveTema();
        }
        else
        if(command.equals("3"))
        {
            deleteTema();
        }
        else
        if(command.equals("4"))
        {
            updateTema();
        }
        else
        if(command.equals("5"))
        {
            prelungireTema();
        }
        else
        if(command.equals("6"))
        {
            printAllTema();
        }
        else
        if(command.equals("7"))
        {
            return;
        }
    }

    /**
     *
     * @return -the command from the user
     */
    private String getCommand()
    {
        Scanner keyboard = new Scanner(System.in);
        System.out.println("Command=");

        return keyboard.nextLine();
    }

    /**
     * show menu
     */
    public void showMenu()
    {
        System.out.println("1.Student");
        System.out.println("2.Tema");
        System.out.println("3.Note");
        System.out.println("0.Exit");

        System.out.println();
    }

    /**
     * find a student
     */
    private void findOneStudent()
    {
        System.out.println("Id=");
        Scanner keyboard = new Scanner(System.in);
        System.out.println(service.findOneS(keyboard.nextInt()));

        System.out.println();
    }

    /**
     * find a homework
     */
    private void findOneTema()
    {
        System.out.println("Id=");
        Scanner keyboard = new Scanner(System.in);
        System.out.println(service.findOneT(keyboard.nextInt()));

        System.out.println();
    }

    /**
     * save a student
     */
    private void saveStudent()
    {
        Scanner keyboard = new Scanner(System.in);

        System.out.print("Id=");
        int id=keyboard.nextInt();

        System.out.print("Nume=");
        String nume=keyboard.next();

        System.out.print("Grupa=");
        int grupa=keyboard.nextInt();

        System.out.print("Email=");
        String email=keyboard.next();

        System.out.print("Cadru didactic=");
        String cadru=keyboard.next();

        try
        {
            service.saveS(id,nume,grupa,email,cadru);
        }
        catch (ValidationException e)
        {
            e.printStackTrace();
        }

        System.out.println();
    }

    /**
     * save a homework
     */
    private void saveTema()
    {
        Scanner keyboard = new Scanner(System.in);

        System.out.println("Nr=");
        int id=keyboard.nextInt();

        System.out.println("Descriere=");
        String descriere=keyboard.next();

        System.out.println("Due=");
        int due=keyboard.nextInt();

        System.out.println("Given=");
        int given=keyboard.nextInt();

        try
        {
            service.saveT(id,descriere,due,given);
        }
        catch (ValidationException e)
        {
            e.printStackTrace();
        }

        System.out.println();
    }

    /**
     * delete a student
     */
    private void deleteStudent()
    {
        System.out.println("Id=");
        Scanner keyboard = new Scanner(System.in);
        service.deleteS(keyboard.nextInt());

        System.out.println();
    }

    /**
     * delete a homework
     */
    private void deleteTema()
    {
        System.out.println("Id=");
        Scanner keyboard = new Scanner(System.in);
        service.deleteT(keyboard.nextInt());

        System.out.println();
    }

    /**
     * update a Student
     */
    private void updateStudent()
    {
        Scanner keyboard = new Scanner(System.in);

        System.out.println("Id=");
        int id=keyboard.nextInt();

        System.out.println("Nume=");
        String nume=keyboard.next();

        System.out.println("Grupa=");
        int grupa=keyboard.nextInt();

        System.out.println("Email=");
        String email=keyboard.next();

        System.out.println("Cadru didactic=");
        String cadru=keyboard.next();

        try
        {
            service.updateS(id,nume,grupa,email,cadru);
        }
        catch (ValidationException e)
        {
            e.printStackTrace();
        }

        System.out.println();
    }

    /**
     * update a homework
     */
    private void updateTema()
    {
        Scanner keyboard = new Scanner(System.in);

        System.out.println("Nr=");
        int id=keyboard.nextInt();

        System.out.println("Descriere=");
        String descriere=keyboard.next();

        System.out.println("Due=");
        int due=keyboard.nextInt();

        System.out.println("Given=");
        int given=keyboard.nextInt();

        try
        {
            service.updateT(id,descriere,due,given);
        }
        catch (ValidationException e)
        {
            e.printStackTrace();
        }

        System.out.println();
    }

    /**
     * extend a due date for a homework
     */
    private void prelungireTema()
    {
        Scanner keyboard = new Scanner(System.in);

        System.out.println("Nr=");
        int id=keyboard.nextInt();

        System.out.println("Curent=");
        int current=keyboard.nextInt();

        service.prelungireTema(id);

        System.out.println();
    }

    /**
     * show all students
     */
    private void printAllStudent()
    {
        Iterable<Student> students=service.findAllS();
        for(Student st:students)
        {
            System.out.println(st);
        }

        System.out.println();
    }

    /**
     * show all homeworks
     */
    private void printAllTema()
    {
        Iterable<Tema> teme=service.findAllT();
        for(Tema tema:teme)
        {
            System.out.println(tema);
        }

        System.out.println();
    }

    /**
     * the run function
     */
    public void run()
    {
        String command="1";

        while(!command.equals("0"))
        {
            showMenu();

            command=getCommand();

            if(command.equals("1"))
            {
                showMenuStudent();
                command=getCommand();
                executeMenuStudent(command);
            }
            if(command.equals("3"))
            {
                //showMenuTema();
                //command=getCommand();
                executeMenuNote();
            }
            else
            if(command.equals("2"))
            {
                showMenuTema();
                command=getCommand();
                executeMenuTema(command);
            }

        }
    }
}

