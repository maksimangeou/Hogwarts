--liquibase farmatted sql

--changeset maximangeou: 1

CREATE INDEX student_name_index ON student (name);