INSERT IGNORE INTO `members` (email, status, created_at, created_by, updated_at, updated_by)
VALUES ('hblee@test.com', 'ACTIVATED', now(), 'system', now(), 'system');

INSERT IGNORE INTO `pg_accounts` (member_id, pg_account_id, pg_provider, status, created_at, created_by, updated_at, updated_by)
VALUES (1, 'test_pg_acc_1', 'TEST', 'ACTIVATED', now(), 'system', now(), 'system');

INSERT IGNORE INTO `members` (email, status, created_at, created_by, updated_at, updated_by)
VALUES ('ed@test.com', 'ACTIVATED', now(), 'system', now(), 'system');
