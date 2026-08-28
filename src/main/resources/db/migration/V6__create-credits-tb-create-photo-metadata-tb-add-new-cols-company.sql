CREATE TABLE IF NOT EXISTS company_credits (
    id BIGINT PRIMARY KEY,
    default_credits INTEGER,
    bought_credits INTEGER
);

CREATE TABLE IF NOT EXISTS company_photo_metadata (
    id BIGINT PRIMARY KEY,
    photo_url text,
    photo_name varchar(255),
    photo_size integer,
    photo_mime_type varchar(255)
    );


ALTER TABLE COMPANY ADD Column PUBLIC_ID UUID;
ALTER TABLE COMPANY ADD COLUMN TAX_ID varchar(255);
ALTER TABLE COMPANY ADD COLUMN PHONE_NUMBER varchar(255);
ALTER TABLE COMPANY ADD COLUMN EMAIL varchar(255);
ALTER TABLE COMPANY ADD CONSTRAINT credits_id FOREIGN KEY (id) REFERENCES company_credits (id) MATCH SIMPLE;
ALTER TABLE COMPANY ADD CONSTRAINT company_photo_id FOREIGN KEY (id) REFERENCES company_photo_metadata (id) MATCH SIMPLE;