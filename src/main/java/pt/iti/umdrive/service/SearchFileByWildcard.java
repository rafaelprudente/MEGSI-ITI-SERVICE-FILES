package pt.iti.umdrive.service;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class SearchFileByWildcard {
    static List<String> matchesList = new ArrayList<>();

    public static List<String> searchWithWc(Path rootDir, String pattern) {
        matchesList.clear();
        if (rootDir.toFile().list() != null) {
            Arrays.stream(rootDir.toFile().list()).filter(s -> s.matches(pattern)).forEach(s -> matchesList.add(s));
        }
        return matchesList;
    }
}
