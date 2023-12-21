DROP SCHEMA IF EXISTS db_employee_junit;
CREATE SCHEMA IF NOT EXISTS db_employee_junit;
USE db_employee_junit;

CREATE TABLE IF NOT EXISTS employee
(
	id         BIGINT AUTO_INCREMENT NOT NULL,
	first_name VARCHAR(255)          NOT NULL,
	last_name  VARCHAR(255)          NOT NULL,
	email      VARCHAR(255)          NOT NULL,
	CONSTRAINT pk_employee PRIMARY KEY (id)
);

