import domain.Nota;
import org.junit.Test;
import repository.NoteRepository;

import static org.junit.Assert.*;
public class TestNote {
    @Test
    public void testAddNota() {
        NoteRepository noteRepository=new NoteRepository();
        noteRepository.addNota(new Nota(1,1,10,4));
        assertEquals(noteRepository.getNote().get(0).getValoare(),10);

    }
    @Test
    public void testAddAndFindFile() {
        NoteRepository noteRepository=new NoteRepository();
        noteRepository.addFile("file1");
        noteRepository.addFile("file2");
        assertTrue(noteRepository.findFile("file1"));
        assertFalse(noteRepository.findFile("file3"));
    }
    @Test
    public void testfindNota() {
        NoteRepository noteRepository=new NoteRepository();
        noteRepository.addNota(new Nota(1,1,10,4));
        assertFalse(noteRepository.findNota_forStudent(1,1));
        assertTrue(noteRepository.findNota_forStudent(1,2));

    }
}