package com.example.diego.testecinq.dao.sqlite;

public final class UserContract {

    public UserContract() {
    }

    public static class UserEntry{

        public static final String TABLE_NAME = "users";
        public static final String ID = "id";
        public static final String NOME = "nome";
        public static final String EMAIL = "email";
        public static final String SENHA = "senha";
    }
}
