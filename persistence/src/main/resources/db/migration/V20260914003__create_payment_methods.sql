CREATE TABLE if not exists `payment_methods`
(
    id                      BIGINT UNSIGNED     NOT NULL AUTO_INCREMENT    PRIMARY  KEY,

    member_id               BIGINT UNSIGNED     NOT NULL COMMENT 'member ID',

    pg_provider             VARCHAR(32)         NOT NULL COMMENT 'PG provider',
    method_type             VARCHAR(32)         NOT NULL COMMENT 'CARD/PAYPAL/WALLET',

    provider_token          VARCHAR(256)        NULL COMMENT 'PG payment method token',

    card_brand              VARCHAR(32)         NULL COMMENT 'VISA/MASTERCARD/AMEX',
    card_last_num           VARCHAR(4)          NULL COMMENT 'last 4 digits of the card',
    card_expiry_month       TINYINT             NULL COMMENT 'card expiry month',
    card_expiry_year        SMALLINT            NULL COMMENT 'card expiry year',

    is_default              TINYINT(1)          NOT NULL COMMENT '1: default method, 0: not default',

    status                  VARCHAR(32)         NOT NULL COMMENT 'ACTIVE/INACTIVE/EXPIRED',

    created_at              DATETIME(6)         NOT NULL,
    updated_at              DATETIME(6)         NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX ix_pm_memberid ON payment_methods (member_id);
