package ru.nsu.fit.chirikhin;

import java.io.IOException;
import java.util.*;
import java.io.File;

public class ConfigParser implements Parser {

    private final Vector<FilterProperties> filters;

    private static final String extensionFilterIdentifier = "Ext";

    ConfigParser(String filePath) throws IOException, IllegalFormatException {

        filters = new Vector<>();

        File myFile = new File(filePath);
        try (Scanner myScanner = new Scanner(myFile)) {

            while (myScanner.hasNextLine()) {

                String line = myScanner.nextLine();
                try(Scanner strScanner = new Scanner(line)) {

                    String filterIdentifier;

                    if (strScanner.hasNext()) {
                        filterIdentifier = strScanner.next();
                    } else {
                        throw new IllegalArgumentException("Wrong format of input!");
                    }

                    switch (filterIdentifier) {
                        case extensionFilterIdentifier:

                            String filterParam;

                            try {
                                filterParam = strScanner.next();
                            } catch (NoSuchElementException e) {
                                throw new IllegalArgumentException("Invalid extension filter parameters!", e);
                            } catch (IllegalStateException e) {
                                throw new IllegalStateException("System Error!!! Can't read next token. The scanner is closed!", e);
                            }

                            if (!filters.contains(new FilterProperties(FilterIdentifier.fileExtensionFilter, new String[]{filterParam}))) {
                                filters.addElement(new FilterProperties(FilterIdentifier.fileExtensionFilter, new String[]{filterParam}));
                            } else {
                                throw new IllegalArgumentException("One filter was met twice!");
                            }

                            if (strScanner.hasNext()) {
                                throw new IllegalArgumentException("Too many arguments for Extension File Filter!");
                            }

                            break;

                        default:
                            throw new IllegalArgumentException("Filter with such identifier doesn't exist!");
                    }
                }

            }
        }
    }

    @Override
   public Vector<FilterProperties> getFiltersProperties() {
        return filters;
    }
}
