package jcontainer.usecases.addcategory;

import com.interface21.web.servlet.ModelAndView;
import jcontainer.Repository;
import jcontainer.api.Category;
import jcontainer.common.PrevaylerFormController;
import jcontainer.common.RepositoryCommand;
import jcontainer.exceptions.ActorException;
import jcontainer.impl.JContainerFactory;
import org.prevayler.Prevayler;

import java.io.Serializable;

public class AddCategoryController extends PrevaylerFormController {

    public AddCategoryController() {
        setCommandClass(FormData.class);
    }

    protected ModelAndView onSubmit(Prevayler prevayler, JContainerFactory factory, Object o) throws Exception {
        final FormData formData = (FormData) o;
        prevayler.executeCommand(new AddCategoryCommand(factory.createCategory(formData.getCategoryName())));
        return null;
    }

    protected String formPurpose() {
        return "Add a component category to the repository";
    }

    private static class AddCategoryCommand extends RepositoryCommand {
        private final Category category;

        public AddCategoryCommand(Category category) {
            this.category = category;
        }

        protected Serializable execute(Repository repository) throws Exception {
            if ( repository.containsCategory(category.getName())) {
                throw new ActorException("Category " + category.getName() + " already exists in the repository");
            }
            repository.addCategory(category);
            return null;
        }
    }

    public static class FormData {
        private String categoryName = null;

        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }
    }
}