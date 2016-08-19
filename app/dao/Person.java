package dao;

import jp.sf.amateras.mirage.annotation.PrimaryKey;
import jp.sf.amateras.mirage.annotation.Table;

@Table(name="person")
public class Person {

    @PrimaryKey(generationType = PrimaryKey.GenerationType.IDENTITY)
    public Long id;

    public String name;

    public Person(String name) {
        this.name = name;
    }
}
