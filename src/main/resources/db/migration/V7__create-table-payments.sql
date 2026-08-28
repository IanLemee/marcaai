CREATE TABLE IF NOT EXISTS PAYMENTS(
    id BIGINT PRIMARY KEY,
    payment_date TIMESTAMP NOT NULL,
    payment_status varchar(255) NOT NULL,
    service_type varchar(255) NOT NULL,
    customer_id BIGINT references customer(id) NOT NULL,
    company_id BIGINT references company(id) NOT NULL,
    amount BIGINT NOT NULL,
--     payment_method varchar(255),
    payment_event_type varchar(255) NOT NULL,
    PAYMENT_ID varchar(255)
);