CREATE TABLE if not exists `pg_external_requests`
(
    id                      BIGINT UNSIGNED     NOT NULL AUTO_INCREMENT    PRIMARY  KEY,

    pg_request_id           VARCHAR(255)        NOT NULL COMMENT 'PG request ID',
    member_id               BIGINT UNSIGNED     NOT NULL COMMENT 'member ID',

    pg_provider             VARCHAR(32)         NOT NULL COMMENT 'PG provider',
    request_type            VARCHAR(32)         NOT NULL COMMENT 'PAYMENT_METHOD/PAYMENT',
    ext_operation           VARCHAR(32)         NOT NULL COMMENT 'specific operation of PG provider',

    ext_provider_secret     VARCHAR(255)        NOT NULL COMMENT 'secret token for request (client_secret)',

    status                  VARCHAR(32)         NOT NULL COMMENT 'INIT/SUCCESS/PENDING/FAILED',

    created_at              DATETIME(6)         NOT NULL,
    created_by              VARCHAR(30)         NOT NULL COMMENT 'creator',
    updated_at              DATETIME(6)         NULL,
    updated_by              VARCHAR(30)         NULL COMMENT 'updater'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX ix_pger_memberid ON pg_external_requests (member_id);
