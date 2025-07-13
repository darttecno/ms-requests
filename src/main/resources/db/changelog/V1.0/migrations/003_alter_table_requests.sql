--liquibase formatted sql

--changeset andres.sanchez:V1.1.3 splitStatements:false runOnChange:true

ALTER TABLE requests
    ADD COLUMN order_number VARCHAR(50),
    ADD COLUMN address TEXT,
    ADD COLUMN phone VARCHAR(20),
    ADD COLUMN email VARCHAR(100);

--rollback ALTER TABLE requests
--    DROP COLUMN email,
--    DROP COLUMN phone,
--    DROP COLUMN address,
--    DROP COLUMN order_number;