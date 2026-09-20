CREATE TABLE IF NOT EXISTS `members`
(
    id                      BIGINT UNSIGNED     NOT NULL AUTO_INCREMENT    PRIMARY  KEY,
    email                   VARCHAR(100)        NOT NULL COMMENT 'email',
    status                  VARCHAR(20)         NOT NULL COMMENT 'ACTIVATED/DEACTIVATED',
    created_at              DATETIME(6)         NOT NULL,
    created_by              VARCHAR(30)         NOT NULL COMMENT 'creator',
    updated_at              DATETIME(6)         NULL,
    updated_by              VARCHAR(30)         NULL COMMENT 'updater'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE UNIQUE INDEX ux_m_email ON members (email);