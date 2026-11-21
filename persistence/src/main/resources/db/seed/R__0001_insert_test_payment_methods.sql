INSERT IGNORE INTO `payment_methods` (member_id, pg_provider, method_type, provider_token, default_method, status, created_at, created_by, updated_at, updated_by)
VALUES (1, 'TEST', 'TEST', null, false, 'ACTIVE', now(), 'system', now(), 'system');
