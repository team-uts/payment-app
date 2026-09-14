CREATE TABLE IF NOT EXISTS ecommerce.orders
(
    id                  BIGINT UNSIGNED     NOT NULL AUTO_INCREMENT    PRIMARY  KEY,
    order_no            VARCHAR(64)         NOT NULL COMMENT 'display order number',
    service_type        VARCHAR(32)         NOT NULL COMMENT 'app service type',
    member_id           BIGINT UNSIGNED     NOT NULL COMMENT 'member ID',
    status              VARCHAR(32)         NOT NULL COMMENT 'order status',
    created_at          DATETIME(6)         NOT NULL,
    updated_at          DATETIME(6)         NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX ix_o_orderno ON ecommerce.orders (order_no);
CREATE INDEX ix_o_memberid ON ecommerce.orders (member_id);