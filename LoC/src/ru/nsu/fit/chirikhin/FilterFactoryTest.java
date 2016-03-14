package ru.nsu.fit.chirikhin;

import org.junit.Test;

public class FilterFactoryTest {
    public FilterFactoryTest() {

    }

    @Test(expected = IllegalArgumentException.class)
    public void createFileExtFilterNoArgs() {
        FilterFactory.createFilters(FilterIdentifier.fileExtensionFilter, new String[] {});
    }

    @Test(expected = IllegalArgumentException.class)
    public void createFileExtFilterManyArgs() {
        FilterFactory.createFilters(FilterIdentifier.fileExtensionFilter, new String[] {"me", "you"});
    }
}
