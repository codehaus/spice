package jcontainer.api;

import jcontainer.api.Category;

public interface CategoryFactory {
    public Category createCategory(final String categoryName);
}
