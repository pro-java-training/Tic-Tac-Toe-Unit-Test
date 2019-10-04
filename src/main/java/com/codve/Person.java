package com.codve;

public class Person {

    private Db db;

    public Person() {
        this(new Db());
    }

    protected Person(Db db) {
        this.db = db;
    }

    public boolean isDeveloper() {
        return false;
    }

    public boolean save(String msg) {
        try {
            getDb().save(msg);
            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }

    public Db getDb() {
        return db;
    }

    public static class Db {
        public void save(String msg) {

        }
    }
}
