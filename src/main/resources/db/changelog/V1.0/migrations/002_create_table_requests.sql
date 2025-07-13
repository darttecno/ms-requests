--liquibase formatted sql

--changeset andres.sanchez:V1.1.2 splitStatements:false runOnChange:true

CREATE TABLE IF NOT EXISTS requests (
    id SERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    medication_id INT NOT NULL,
    quantity INT NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_medication FOREIGN KEY (medication_id)
    REFERENCES medications(id)
    ON DELETE RESTRICT
    ON UPDATE CASCADE
);

--rollback DELETE FROM requests;
--rollback DROP TABLE IF EXISTS requests;
