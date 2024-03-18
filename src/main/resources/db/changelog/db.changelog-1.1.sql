--liquibase formatted sql

--changeset kosvad9:1
alter table tasks
    rename column status to complete_status;