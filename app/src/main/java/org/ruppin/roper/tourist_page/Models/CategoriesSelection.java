package org.ruppin.roper.tourist_page.Models;

import java.util.List;

public class CategoriesSelection {
    List<String> selection;

    public CategoriesSelection(List<String> selection) {
        this.selection = selection;
    }

    public List<String> getSelection() {
        return selection;
    }

    public void setSelection(List<String> selection) {
        this.selection = selection;
    }
}
