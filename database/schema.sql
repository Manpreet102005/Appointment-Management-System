CREATE DATABASE IF NOT EXISTS appointment_db;
USE appointment_db;

CREATE TABLE IF NOT EXISTS patient (
     id INT PRIMARY KEY AUTO_INCREMENT,
     fullname VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS doctor (
    doctor_id INT PRIMARY KEY AUTO_INCREMENT,
    specialisation VARCHAR(30)
);

CREATE TABLE IF NOT EXISTS appointment (
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
    password VARCHAR(20)
);