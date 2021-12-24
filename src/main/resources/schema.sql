-- noinspection SqlNoDataSourceInspectionForFile

DROP TABLE IF EXISTS tb_user;

CREATE TABLE tb_user
(
    username  VARCHAR(250) PRIMARY KEY,
    pw  VARCHAR(250) NOT NULL,
    first_name VARCHAR(250) NOT NULL,
    last_name  VARCHAR(250) NOT NULL,
    email     VARCHAR(250) NOT NULL
);
