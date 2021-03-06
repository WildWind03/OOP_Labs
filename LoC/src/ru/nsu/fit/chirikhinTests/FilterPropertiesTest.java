package ru.nsu.fit.chirikhinTests;

import org.junit.Test;
import ru.nsu.fit.chirikhin.FilterIdentifier;
import ru.nsu.fit.chirikhin.FilterProperties;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;


public class FilterPropertiesTest {
    public FilterPropertiesTest() {

    }

    @Test
    public void equalTest() {
        FilterProperties prop = new FilterProperties(FilterIdentifier.fileExtensionFilter, new String[] {"yes", "no"});
        assertTrue(prop.equals(new FilterProperties(FilterIdentifier.fileExtensionFilter, new String[] {"yes", "no"})));
        assertFalse(prop.equals(new FilterProperties(FilterIdentifier.fileExtensionFilter, new String[] {"yes", "yes"})));
        assertFalse(prop.equals(new FilterProperties(FilterIdentifier.fileExtensionFilter, new String[] {"yes", "no", "hi"})));
        assertFalse(prop.equals(new FilterProperties(FilterIdentifier.fileExtensionFilter, new String[] {"yes"})));
    }
}
