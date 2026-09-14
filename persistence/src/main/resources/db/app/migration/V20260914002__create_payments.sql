CREATE TABLE IF NOT EXISTS ecommerce.payments
(
    id                      BIGINT UNSIGNED     NOT NULL AUTO_INCREMENT    PRIMARY  KEY,
    order_id                BIGINT UNSIGNED     NOT NULL COMMENT 'order ID',
    member_id               BIGINT UNSIGNED     NOT NULL COMMENT 'member ID',
    status                  VARCHAR(32)         NOT NULL COMMENT 'payment status',

    currency                CHAR(3)             NOT NULL COMMENT 'currency type (AUD)',

    amount                  DECIMAL(15, 2)      NOT NULL COMMENT 'actual payment amount',

    pg_provider             VARCHAR(32)         NOT NULL COMMENT 'PG provider',
    pg_method               VARCHAR(32)         NOT NULL COMMENT 'payment method (CARD)',

    payment_method_id       BIGINT UNSIGNED     NULL     COMMENT 'stored payment method',

    created_at              DATETIME(6)         NOT NULL,
    updated_at              DATETIME(6)         NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX ix_payments_orderid ON ecommerce.payments (order_id);
CREATE INDEX ix_payments_memberid ON ecommerce.payments (member_id);
