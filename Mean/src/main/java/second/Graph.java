package second;

import java.util.ArrayList;
import java.util.Arrays;

public class Graph {
    private static ArrayList<String[]> links = new ArrayList<>(Arrays.asList(
            new String[] {"2", "4", "5"},
            new String[] {"1", "3"},
            new String[] {"2", "4"},
            new String[] {"1", "3", "5"},
            new String[] {"1", "4"}
    ));

    static public String[] getLinks(int i){
        return links.get(i - 1);
    }
}
