CREATE DATABASE IF NOT EXISTS appointment_db;
USE appointment_db;

CREATE TABLE IF NOT EXISTS patients (
     id INT PRIMARY KEY AUTO_INCREMENT,
     patient_name VARCHAR(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS doctors (
    doctor_id INT PRIMARY KEY AUTO_INCREMENT,
    doctor_name VARCHAR(20) NOT NULL ,
    specialisation VARCHAR(20)
);

CREATE TABLE IF NOT EXISTS appointments (
    appointment_id INT PRIMARY KEY AUTO_INCREMENT,
    doctor_id INT NOT NULL,
    patient_id INT NOT NULL,
    date_time DATETIME,
    status VARCHAR(9) CHECK (status IN ('BOOKED', 'CANCELLED')),
    FOREIGN KEY (patient_id) REFERENCES patient(id) ON DELETE CASCADE,
    FOREIGN KEY (doctor_id)  REFERENCES doctor(doctor_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS authorised_users (
    username VARCHAR(20) PRIMARY KEY,
    password VARCHAR(20) NOT NULL
);