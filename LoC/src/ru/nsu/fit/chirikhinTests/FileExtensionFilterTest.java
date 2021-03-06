package ru.nsu.fit.chirikhinTests;

import org.junit.Test;
import ru.nsu.fit.chirikhin.FileExtensionFilter;

import java.io.File;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

public class FileExtensionFilterTest {
    public FileExtensionFilterTest() {

    }

    @Test
    public void testIsAppropriate() {
        File myFile = new File("hello");
        FileExtensionFilter filter = new FileExtensionFilter("txt");
        assertFalse (filter.isAppropriate(myFile));
        FileExtensionFilter filter1 = new FileExtensionFilter("");
        assertTrue (filter1.isAppropriate(myFile));

        File myFile1 = new File("hello.");
        filter = new FileExtensionFilter("txt");
        assertFalse (filter.isAppropriate(myFile1));
        filter1 = new FileExtensionFilter("");
        assertTrue (filter1.isAppropriate(myFile1));

        File myFile2 = new File("hello.txt");
        filter = new FileExtensionFilter("txt");
        assertTrue (filter.isAppropriate(myFile2));
        filter1 = new FileExtensionFilter("");
        assertFalse (filter1.isAppropriate(myFile2));
    }

    @Test
    public void getDescriptorForOutputTest() {
        FileExtensionFilter filter = new FileExtensionFilter("txt");
        assertTrue(filter.getDescriptionForOutput().equals("txt"));
    }


}
