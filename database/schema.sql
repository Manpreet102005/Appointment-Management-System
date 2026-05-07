CREATE DATABASE IF NOT EXISTS appointment_db;
USE appointment_db;

CREATE TABLE IF NOT EXISTS patients (
     id INT PRIMARY KEY AUTO_INCREMENT,
     patient_name VARCHAR(20) NOT NULL,
     phone_no VARCHAR(10) NOT NULL,
     gender CHAR(1) CHECK (gender IN ('M', 'F', 'O')) NOT NULL,
     blood_group VARCHAR(3) CHECK (blood_group IN ('A+', 'A-', 'B+', 'B-', 'AB+', 'AB-', 'O+', 'O-'))
) AUTO_INCREMENT=1;

CREATE TABLE IF NOT EXISTS doctors (
    doctor_id INT PRIMARY KEY AUTO_INCREMENT,
    doctor_name VARCHAR(20) NOT NULL ,
    specialisation VARCHAR(20)
) AUTO_INCREMENT=1;

CREATE TABLE IF NOT EXISTS appointments (
    appointment_id INT PRIMARY KEY AUTO_INCREMENT,
    doctor_id INT NOT NULL,
    patient_id INT NOT NULL,
    date_time DATETIME NOT NULL,
    status VARCHAR(9) CHECK (status IN ('BOOKED', 'CANCELLED')),
    FOREIGN KEY (patient_id) REFERENCES patients(id) ON DELETE CASCADE,
    FOREIGN KEY (doctor_id)  REFERENCES doctors(doctor_id) ON DELETE CASCADE
) AUTO_INCREMENT=1;

CREATE UNIQUE INDEX patients_idx on patients(id);
CREATE UNIQUE INDEX doctors_idx on doctors(doctor_id);
CREATE UNIQUE INDEX appointments_idx on appointments(appointment_id);
#Future Purpose
CREATE TABLE IF NOT EXISTS authorised_users (
    username VARCHAR(20) PRIMARY KEY,
    password VARCHAR(20) NOT NULL
) ;

