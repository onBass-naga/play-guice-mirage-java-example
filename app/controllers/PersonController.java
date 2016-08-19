package controllers;

import dao.Person;
import org.springframework.transaction.annotation.Transactional;

import play.mvc.Controller;
import play.mvc.Result;
import service.PersonService;
import views.html.index;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public class PersonController extends Controller {

    public static Result index() {
        return ok(index.render("Your new application is ready."));
    }


    @Inject
    PersonService personService;

    @Transactional
    public Result add_member() {

        List<Person> persons = personService.addMember();

        return ok(index.render("New Member Id: " + persons.get(1).name));
    }
}
