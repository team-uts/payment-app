CREATE TABLE IF NOT EXISTS `outbox_events`
(
    id                    BIGINT UNSIGNED     NOT NULL AUTO_INCREMENT   PRIMARY  KEY,
    event_type            VARCHAR(40)         NOT NULL COMMENT 'event type',
    payload               VARCHAR(1000)       NOT NULL COMMENT 'event payload',
    status                VARCHAR(20)         NOT NULL COMMENT 'event status',
    created_at            DATETIME(6)         NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at            DATETIME(6)         NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
