CREATE TABLE IF NOT EXISTS `orders`
(
    id                  BIGINT UNSIGNED     NOT NULL AUTO_INCREMENT    PRIMARY  KEY,
    ext_order_no        VARCHAR(64)         NOT NULL COMMENT 'display order number',
    service_type        VARCHAR(32)         NOT NULL COMMENT 'app service type',
    member_id           BIGINT UNSIGNED     NOT NULL COMMENT 'member ID',
    amount              DECIMAL(15, 2)      NOT NULL COMMENT 'order total totalAmount',
    currency            CHAR(3)             NOT NULL COMMENT 'currency type (AUD)',
    status              VARCHAR(32)         NOT NULL COMMENT 'order status',
    created_at          DATETIME(6)         NOT NULL,
    updated_at          DATETIME(6)         NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX ix_o_extorderno ON orders (ext_order_no);
CREATE INDEX ix_o_memberid ON orders (member_id);
