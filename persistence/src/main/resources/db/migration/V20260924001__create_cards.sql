CREATE TABLE if not exists `cards`
(
    id                      BIGINT UNSIGNED     NOT NULL AUTO_INCREMENT    PRIMARY  KEY,

    member_id               BIGINT UNSIGNED     NOT NULL COMMENT 'member ID',
    pay_method_id           BIGINT UNSIGNED     NOT NULL COMMENT 'payment method ID',

    brand                   VARCHAR(32)         NULL COMMENT 'VISA/MASTERCARD',
    last_four               VARCHAR(4)          NULL COMMENT 'last 4 digits of the card',
    expiry_month            INT                 NULL COMMENT 'card expiry month',
    expiry_year             INT                 NULL COMMENT 'card expiry year',

    status                  VARCHAR(32)         NOT NULL COMMENT 'ACTIVE/INACTIVE/EXPIRED',

    created_at              DATETIME(6)         NOT NULL,
    created_by              VARCHAR(30)         NOT NULL COMMENT 'creator',
    updated_at              DATETIME(6)         NULL,
    updated_by              VARCHAR(30)         NULL COMMENT 'updater'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX ix_c_paymethodid ON cards (pay_method_id);
