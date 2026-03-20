CREATE DATABASE IF NOT EXISTS appointment_db;
use appointment_db;

CREATE TABLE  IF NOT EXISTS person (
       id int primary key ,
       fullname varchar(20) not null
);

CREATE TABLE IF NOT EXISTS patient(
       id int primary key,
       fullname varchar(20) not null
);

CREATE TABLE IF NOT EXISTS doctor(
                       doctor_id int primary key ,
                       specialisation varchar(20)
);

CREATE TABLE IF NOT EXISTS appointment(
        appointment_id int primary key auto_increment,
        doctor_id int ,
        patient_id int not null ,
        date_time datetime ,
        status varchar(9) check (status in('BOOKED','CANCELLED')),
        foreign key (patient_id) references patient(id) on delete cascade ,
        foreign key (doctor_id) references doctor(doctor_id) on delete set null
);

CREATE TABLE IF NOT EXISTS authorised_users(
        username varchar(20) primary key ,
        password varchar(20)
);



