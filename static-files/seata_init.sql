CREATE DATABASE IF NOT EXISTS seata;

USE seata;

CREATE TABLE IF NOT EXISTS global_table (
    xid VARCHAR(128) NOT NULL,
    transaction_id BIGINT,
    status TINYINT NOT NULL,
    application_id VARCHAR(32),
    transaction_service_group VARCHAR(32),
    transaction_name VARCHAR(128),
    timeout INT,
    begin_time BIGINT,
    application_data VARCHAR(2000),
    gmt_create TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    gmt_modified TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    PRIMARY KEY (xid),
    KEY idx_gmt_modified_status (gmt_modified, status),
    KEY idx_transaction_id (transaction_id)
    );

CREATE TABLE IF NOT EXISTS branch_table (
                                            branch_id BIGINT NOT NULL,
                                            xid VARCHAR(128) NOT NULL,
    transaction_id BIGINT DEFAULT 0,
    resource_group_id VARCHAR(32) DEFAULT NULL,
    resource_id VARCHAR(256) DEFAULT NULL,
    branch_type VARCHAR(8),
    status TINYINT,
    client_id VARCHAR(64),
    application_data VARCHAR(2000),
    gmt_create TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    gmt_modified TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    PRIMARY KEY (branch_id),
    KEY idx_xid (xid)
    );

CREATE TABLE IF NOT EXISTS lock_table (
    row_key VARCHAR(128) NOT NULL,
    xid VARCHAR(96),
    transaction_id BIGINT,
    branch_id BIGINT NOT NULL,
    resource_id VARCHAR(256),
    table_name VARCHAR(32),
    pk VARCHAR(36),
    gmt_create TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    gmt_modified TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    PRIMARY KEY (row_key),
    KEY idx_branch_id (branch_id)
    );