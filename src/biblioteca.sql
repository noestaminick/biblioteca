CREATE DATABASE IF NOT EXISTS biblioteca;

USE biblioteca;

CREATE TABLE IF NOT EXISTS llibres (
    id_llibre INT AUTO_INCREMENT PRIMARY KEY,
    titol VARCHAR(255) NOT NULL,
    autor VARCHAR(255) NOT NULL,
    isbn VARCHAR(13) NOT NULL,
    editorial VARCHAR(255) NOT NULL,
    any_publicacio INT NOT NULL,
    categoria VARCHAR(255) NOT NULL,
    estat VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS usuaris (
    id_usuari INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(255) NOT NULL,
    cognoms VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    telefon VARCHAR(255) NOT NULL,
    rol VARCHAR(255) NOT NULL,
    data_registre DATE NOT NULL
);

CREATE TABLE IF NOT EXISTS prestecs (
    id_prestec INT AUTO_INCREMENT PRIMARY KEY,
    id_llibre INT,
    id_usuari INT,
    data_prestec DATE NOT NULL,
    data_retorn_prevista DATE NOT NULL,
    data_retorn_real DATE,
    estat VARCHAR(255) NOT NULL,
    FOREIGN KEY (id_llibre) REFERENCES llibres(id_llibre),
    FOREIGN KEY (id_usuari) REFERENCES usuaris(id_usuari)
);