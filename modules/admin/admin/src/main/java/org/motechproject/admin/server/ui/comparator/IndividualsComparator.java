package org.motechproject.admin.server.ui.comparator;

import java.util.Comparator;

public class IndividualsComparator implements Comparator<String> {

    private int getIndex(String module) {
        if ("admin.module".equals(module)) {
            return 1;
        } else if ("websecurity".equals(module)) {
            return 2;
        } else {
            return 0;
        }
    }

    @Override
    public int compare(String module1, String module2) {
        Integer index1 = getIndex(module1);
        Integer index2 = getIndex(module2);

        if (index1 == index2) { // both are 0
            return module1.compareTo(module2);
        } else {
            return index1.compareTo(index2);
        }
    }
}
