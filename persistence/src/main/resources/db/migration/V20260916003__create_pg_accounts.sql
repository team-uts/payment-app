CREATE TABLE if not exists `pg_accounts`
(
    id                      BIGINT UNSIGNED     NOT NULL AUTO_INCREMENT    PRIMARY  KEY,

    member_id               BIGINT UNSIGNED     NOT NULL COMMENT 'member ID',
    pg_account_id           VARCHAR(255)       NOT NULL COMMENT 'PG account ID (encryption)',

    pg_provider             VARCHAR(32)         NOT NULL COMMENT 'PG provider',

    status                  VARCHAR(32)         NOT NULL COMMENT 'ACTIVATED/DEACTIVATED',

    created_at              DATETIME(6)         NOT NULL,
    created_by              VARCHAR(30)         NOT NULL COMMENT 'creator',
    updated_at              DATETIME(6)         NULL,
    updated_by              VARCHAR(30)         NULL COMMENT 'updater'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE UNIQUE INDEX ux_pga_memberid ON pg_accounts (member_id);
