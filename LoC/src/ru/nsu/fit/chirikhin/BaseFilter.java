package ru.nsu.fit.chirikhin;

import java.io.File;

/**
 * @author Chirikhin Alexander
 */
public interface BaseFilter {
    boolean isAppropriate(File file);
    String getDescriptionForOutput();
}
