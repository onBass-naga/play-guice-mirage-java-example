package dao;

import jp.sf.amateras.mirage.SqlManager;
import jp.sf.amateras.mirage.SqlResource;
import jp.sf.amateras.mirage.StringSqlResource;

import javax.inject.Inject;
import java.util.List;

public class PersonDao {

    @Inject
    private SqlManager sqlManager;

    public Person insert(Person person) {
        sqlManager.insertEntity(person);
        return person;
    }

    public List<Person> findAll() {
        SqlResource sql = new StringSqlResource("select * from person");
        return sqlManager.getResultList(Person.class, sql);
    }
}
