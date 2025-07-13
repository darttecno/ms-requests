--liquibase formatted sql

--changeset andres.sanchez:V1.1.1 splitStatements:false runOnChange:true

CREATE TABLE IF NOT EXISTS medications (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    atc_code VARCHAR(20),
    manufacturer VARCHAR(100),
    is_no_pos BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

--changeset andres.sanchez:insert-medications

INSERT INTO medications (name, description, atc_code, manufacturer, is_no_pos)
SELECT 'Paracetamol', 'Analgésico y antipirético de uso común.', 'N02BE01', 'Genfar', FALSE
    WHERE NOT EXISTS (SELECT 1 FROM medications WHERE name = 'Paracetamol');

INSERT INTO medications (name, description, atc_code, manufacturer, is_no_pos)
SELECT 'Amoxicilina', 'Antibiótico betalactámico para infecciones bacterianas.', 'J01CA04', 'Pfizer', FALSE
    WHERE NOT EXISTS (SELECT 1 FROM medications WHERE name = 'Amoxicilina');

INSERT INTO medications (name, description, atc_code, manufacturer, is_no_pos)
SELECT 'Metformina', 'Tratamiento para la diabetes tipo 2.', 'A10BA02', 'Tecnoquímicas', FALSE
    WHERE NOT EXISTS (SELECT 1 FROM medications WHERE name = 'Metformina');

INSERT INTO medications (name, description, atc_code, manufacturer, is_no_pos)
SELECT 'Omeprazol', 'Inhibidor de la bomba de protones para tratamiento gástrico.', 'A02BC01', 'La Santé', FALSE
    WHERE NOT EXISTS (SELECT 1 FROM medications WHERE name = 'Omeprazol');

INSERT INTO medications (name, description, atc_code, manufacturer, is_no_pos)
SELECT 'Tramadol', 'Analgésico opioide usado para dolor moderado a severo.', 'N02AX02', 'Siegfried Rhein', FALSE
    WHERE NOT EXISTS (SELECT 1 FROM medications WHERE name = 'Tramadol');

INSERT INTO medications (name, description, atc_code, manufacturer, is_no_pos)
SELECT 'Rituximab', 'Anticuerpo monoclonal para tratamiento de linfoma y artritis reumatoide.', 'L01XC02', 'Roche', TRUE
    WHERE NOT EXISTS (SELECT 1 FROM medications WHERE name = 'Rituximab');

INSERT INTO medications (name, description, atc_code, manufacturer, is_no_pos)
SELECT 'Adalimumab', 'Biológico antiinflamatorio para enfermedades autoinmunes.', 'L04AB04', 'AbbVie', TRUE
    WHERE NOT EXISTS (SELECT 1 FROM medications WHERE name = 'Adalimumab');

INSERT INTO medications (name, description, atc_code, manufacturer, is_no_pos)
SELECT 'Lenalidomida', 'Tratamiento para mieloma múltiple y síndromes mielodisplásicos.', 'L04AX04', 'Bristol Myers Squibb', TRUE
    WHERE NOT EXISTS (SELECT 1 FROM medications WHERE name = 'Lenalidomida');

INSERT INTO medications (name, description, atc_code, manufacturer, is_no_pos)
SELECT 'Lurasidona', 'Antipsicótico atípico para esquizofrenia y trastorno bipolar.', 'N05AE05', 'Sunovion', TRUE
    WHERE NOT EXISTS (SELECT 1 FROM medications WHERE name = 'Lurasidona');

INSERT INTO medications (name, description, atc_code, manufacturer, is_no_pos)
SELECT 'Eculizumab', 'Tratamiento para hemoglobinuria paroxística nocturna (HPN).', 'L04AA25', 'Alexion', TRUE
    WHERE NOT EXISTS (SELECT 1 FROM medications WHERE name = 'Eculizumab');

--rollback DELETE FROM medications;
--rollback DROP TABLE IF EXISTS medications;
