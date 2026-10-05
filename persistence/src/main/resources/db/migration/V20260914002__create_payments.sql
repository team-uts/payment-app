CREATE TABLE IF NOT EXISTS `payments`
(
    id                      BIGINT UNSIGNED     NOT NULL AUTO_INCREMENT    PRIMARY  KEY,
    order_id                BIGINT UNSIGNED     NOT NULL COMMENT 'order ID',
    member_id               BIGINT UNSIGNED     NOT NULL COMMENT 'member ID',

    service_type            VARCHAR(32)         NOT NULL COMMENT 'app service type',
    status                  VARCHAR(32)         NOT NULL COMMENT 'payment status',

    pay_amount              DECIMAL(15, 2)      NOT NULL COMMENT 'actual payment amount through pg',
    point_amount            DECIMAL(15, 2)      NOT NULL COMMENT 'point payment amount used',
    currency                CHAR(3)             NOT NULL COMMENT 'currency type (AUD)',

    payment_method_id       BIGINT UNSIGNED     NULL     COMMENT 'stored payment method',
    payment_method_type     VARCHAR(32)         NOT NULL COMMENT 'payment method (CARD)',

    created_at              DATETIME(6)         NOT NULL,
    updated_at              DATETIME(6)         NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX ix_p_orderid ON payments (order_id);
CREATE INDEX ix_p_memberid ON payments (member_id);
