package repository;

import domain.Nota;
import domain.Tema;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class NoteRepository {
    private List<String> files;
    private List<Nota> note;

    /**
     * we initialize the arrays
     */
    public NoteRepository(){
        this.files = new ArrayList<>();
        this.note=new ArrayList<>();
        try {
            readFromFile();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void readFromFile() throws FileNotFoundException {
        File file = new File("C:\\Users\\ptido\\IdeaProjects\\Lab3\\src\\repository\\Files");
        Scanner sc = new Scanner(file);
        while (sc.hasNextLine()) {
            String splited = sc.nextLine();
            addFile(splited);
        }
    }
    private void writeToFile()
    {
        try {
            PrintWriter pw = new PrintWriter(new File("C:\\Users\\ptido\\IdeaProjects\\Lab3\\src\\repository\\Files"));
            for (String t : getFiles()) {
                pw.println(t);
            }
            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @return files-all the file names
     */
    public List<String> getFiles() {
        return files;
    }

    /**
     *
     * @param fileName -a fileName
     * @return true if the filename is in there
     */
    public boolean findFile(String fileName)
    {
        return files.indexOf(fileName) >= 0;
    }

    /**
     *
     * @param file -add a file name
     */
    public void addFile(String file)
    {
        files.add(file);
        writeToFile();
    }

    /**
     *
     * @param n -add a mark
     */
    public void addNota(Nota n)
    {
        note.add(n);
    }
    public void deleteNota(Nota n)
    {
        note.remove(n);
    }

    /**
     *
     * @param studentId -a sudent Id
     * @param temaId -a homework Id
     * @return true if this pair is already here
     */
    public boolean findNota_forStudent(int studentId,int temaId)
    {
        for(Nota n: note)
            if(n.getStudentId()==studentId&&n.getTemaId()==temaId)
                return false;
        return true;

    }

    /**
     *
     * @return note- all marks
     */
    public List<Nota> getNote() {
        return note;
    }
}
