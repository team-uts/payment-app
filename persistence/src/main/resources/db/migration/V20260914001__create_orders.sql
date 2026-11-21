CREATE TABLE IF NOT EXISTS `orders`
(
    id                  BIGINT UNSIGNED     NOT NULL AUTO_INCREMENT    PRIMARY  KEY,
    ext_order_no        VARCHAR(64)         NOT NULL COMMENT 'external order number',
    service_type        VARCHAR(32)         NOT NULL COMMENT 'app service type',
    member_id           BIGINT UNSIGNED     NOT NULL COMMENT 'member ID',
    amount              DECIMAL(15, 2)      NOT NULL COMMENT 'order total totalAmount',
    currency            CHAR(3)             NOT NULL COMMENT 'currency type (AUD)',
    status              VARCHAR(32)         NOT NULL COMMENT 'order status',
    created_at          DATETIME(6)         NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at          DATETIME(6)         NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE UNIQUE INDEX ux_o_extorderno_servicetype ON orders (ext_order_no, service_type);
CREATE INDEX ix_o_memberid ON orders (member_id);
