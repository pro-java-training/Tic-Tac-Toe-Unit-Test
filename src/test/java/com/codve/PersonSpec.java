package com.codve;

import com.codve.Person.Db;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class PersonSpec {

    private Person person;
    private Db db;
    private String msg = "hello, world";

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void before() {
        db = mock(Db.class);
        person = spy(new Person(db));
    }


    @Test
    public void whenPersonSaveThenDBSave() {

        doReturn(db).when(person).getDb();
        person.save(msg);
        verify(db, times(1)).save(msg);
    }

    /**
     * 存储失败时返回 false
     */
    @Test
    public void givenExceptionWhenSaveThenReturnFalse() {
        doThrow(new RuntimeException("error"))
                .when(db)
                .save(any());
        doReturn(db).when(person).getDb();
        assertFalse(person.save(msg));
    }

    /**
     * 初始化 person 时数据库连接必须存在
     */
    @Test
    public void whenInitThenDbIsSet() {
        assertNotNull(person.getDb());
    }

}