package service;

import dao.Person;
import dao.PersonDao;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by naga on 2016/08/17.
 */
public class PersonService {

    @Inject
    private PersonDao personDao;

    public List<Person> addMember() {
        personDao.insert(new Person("Tanaka"));
        personDao.insert(new Person("Suzuki"));

        return personDao.findAll();
    }
}
