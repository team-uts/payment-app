CREATE TABLE IF NOT EXISTS `payment_transactions`
(
    id                      BIGINT UNSIGNED     NOT NULL AUTO_INCREMENT    PRIMARY  KEY,
    member_id               BIGINT UNSIGNED     NOT NULL COMMENT 'member ID',
    payment_id              BIGINT UNSIGNED     NOT NULL COMMENT 'payment ID',
    order_id                BIGINT UNSIGNED     NOT NULL COMMENT 'order ID',
    payment_method_id       BIGINT UNSIGNED     NOT NULL COMMENT 'payment method ID',
    payment_method_type     VARCHAR(32)         NOT NULL COMMENT 'payment method type (CARD/BANK_ACCOUNT/PAYPAL/APPLEPAY)',
    service_type            VARCHAR(32)         NOT NULL COMMENT 'app service type',
    amount                  DECIMAL(15, 2)      NOT NULL COMMENT 'payment transaction totalAmount',
    currency                CHAR(3)             NOT NULL COMMENT 'currency type (AUD)',
    pg_provider             VARCHAR(32)         NOT NULL COMMENT 'PG provider',
    pg_transaction_id       VARCHAR(64)         NULL     COMMENT 'PG transaction ID',
    pg_response_message     VARCHAR(255)        NULL     COMMENT 'PG response message',
    status                  VARCHAR(32)         NOT NULL COMMENT 'payment status',
    seq                     INT UNSIGNED        NOT NULL COMMENT 'payment transaction sequence',
    created_at              DATETIME(6)         NOT NULL,
    updated_at              DATETIME(6)         NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX ix_pt_memberid ON payment_transactions (member_id);
CREATE INDEX ix_pt_paymentid ON payment_transactions (payment_id);
CREATE INDEX ix_pt_orderid ON payment_transactions (order_id);