CREATE TABLE if not exists `payment_methods`
(
    id                      BIGINT UNSIGNED     NOT NULL AUTO_INCREMENT    PRIMARY  KEY,

    member_id               BIGINT UNSIGNED     NOT NULL COMMENT 'member ID',

    pg_provider             VARCHAR(32)         NOT NULL COMMENT 'PG provider',
    method_type             VARCHAR(32)         NOT NULL COMMENT 'CARD/PAYPAL/APPLEPAY',

    provider_token          VARCHAR(256)        NULL COMMENT 'PG payment method token (encryption)',

    default_method          TINYINT(1)          NOT NULL COMMENT '1: default method, 0: not default',

    status                  VARCHAR(32)         NOT NULL COMMENT 'ACTIVE/INACTIVE/EXPIRED',

    created_at              DATETIME(6)         NOT NULL,
    created_by              VARCHAR(30)         NOT NULL COMMENT 'creator',
    updated_at              DATETIME(6)         NULL,
    updated_by              VARCHAR(30)         NULL COMMENT 'updater'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX ix_pm_memberid ON payment_methods (member_id);
