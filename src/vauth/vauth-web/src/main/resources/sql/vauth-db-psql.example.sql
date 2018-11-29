-- -----------------------------------------------------
-- Schema vauth-db
-- -----------------------------------------------------
-- VAUTH database schema / Vilten's Identity Management
-- version: 1.2
-- since: 2017-07-20
-- created-by: vilten

DROP DATABASE IF EXISTS "vauth-db";
DROP USER IF EXISTS "vauth-user";

CREATE USER "vauth-user" WITH LOGIN NOSUPERUSER CREATEDB NOCREATEROLE INHERIT PASSWORD 'vauth123';
CREATE DATABASE "vauth-db" OWNER "vauth-user";

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- -----------------------------------------------------
-- Function created_update
-- -----------------------------------------------------
DROP FUNCTION IF EXISTS created_update();

CREATE OR REPLACE FUNCTION created_update() RETURNS TRIGGER AS $$
BEGIN
    NEW.CREATED = NOW();
    RETURN NEW;
END;
$$ language 'plpgsql';

-- -----------------------------------------------------
-- Table vauth_application
-- -----------------------------------------------------
DROP TABLE IF EXISTS vauth_application;

CREATE TABLE IF NOT EXISTS vauth_application
(
  APPLICATION_ID BIGSERIAL NOT NULL PRIMARY KEY,
  EXTERNAL_ID VARCHAR(45) NOT NULL,
  CREATED TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  ENABLED BOOLEAN NOT NULL,
  CLIENT_ID VARCHAR(32) NOT NULL DEFAULT '',
  CLIENT_SECRET VARCHAR(32) NOT NULL DEFAULT ''
);

CREATE UNIQUE INDEX EXTERNAL_ID_UNIQUE ON vauth_application USING btree (EXTERNAL_ID ASC);

CREATE UNIQUE INDEX CLIENT_ID_UNIQUE ON vauth_application USING btree (CLIENT_ID ASC);	

CREATE UNIQUE INDEX CLIENT_SECRET_UNIQUE ON vauth_application USING btree (CLIENT_SECRET ASC);	

CREATE TRIGGER APPLICATION_CREATED_UPDATE_trg BEFORE UPDATE ON vauth_application FOR EACH ROW EXECUTE PROCEDURE created_update();

-- -----------------------------------------------------
-- Table vauth_user
-- -----------------------------------------------------
DROP TABLE IF EXISTS vauth_user ;

CREATE TABLE IF NOT EXISTS vauth_user (
  USER_ID BIGSERIAL NOT NULL PRIMARY KEY,
  EXTERNAL_ID VARCHAR(100) UNIQUE NOT NULL,
  ENABLED BOOLEAN NOT NULL,
  CREATED TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PASSWORD VARCHAR(32) NOT NULL
);

CREATE UNIQUE INDEX USER_EXTERNAL_ID_UNIQUE ON vauth_user USING btree (EXTERNAL_ID ASC);

CREATE INDEX IDX_USER_ID ON vauth_user USING btree (EXTERNAL_ID ASC, USER_ID ASC);

CREATE TRIGGER USER_CREATED_UPDATE_trg BEFORE UPDATE ON vauth_user FOR EACH ROW EXECUTE PROCEDURE created_update();

-- -----------------------------------------------------
-- Table vauth_actcode
-- -----------------------------------------------------
DROP TABLE IF EXISTS vauth_actcode ;

CREATE TABLE IF NOT EXISTS vauth_actcode (
  ACTCODE_ID BIGSERIAL NOT NULL PRIMARY KEY,
  USER_ID BIGINT NOT NULL,
  ACTCODE VARCHAR(32) NOT NULL DEFAULT '',
  CREATED TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT FK_ACTCODE_USER
    FOREIGN KEY (USER_ID)
    REFERENCES vauth_user (USER_ID)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);

CREATE INDEX IDX_ACTCODE ON vauth_actcode USING btree (ACTCODE ASC);

CREATE UNIQUE INDEX USER_ID_UNIQUE ON vauth_actcode USING btree (USER_ID ASC);

CREATE TRIGGER ACTCODE_CREATED_UPDATE_trg BEFORE UPDATE ON vauth_actcode FOR EACH ROW EXECUTE PROCEDURE created_update();

-- -----------------------------------------------------
-- Table vauth_resetcode
-- -----------------------------------------------------
DROP TABLE IF EXISTS vauth_resetcode ;

CREATE TABLE IF NOT EXISTS vauth_resetcode (
  RESETCODE_ID BIGSERIAL NOT NULL PRIMARY KEY,
  USER_ID BIGINT NOT NULL,
  RESETCODE VARCHAR(32) NOT NULL DEFAULT '',
  CREATED TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT FK_RESETCODE_USER
    FOREIGN KEY (USER_ID)
    REFERENCES vauth_user (USER_ID)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);

CREATE INDEX IDX_RESETCODE ON vauth_resetcode USING btree (RESETCODE ASC);

CREATE UNIQUE INDEX USER_ID_UNIQUE ON vauth_resetcode USING btree (USER_ID ASC);

CREATE TRIGGER RESETCODE_CREATED_UPDATE_trg BEFORE UPDATE ON vauth_resetcode FOR EACH ROW EXECUTE PROCEDURE created_update();

-- -----------------------------------------------------
-- Table vauth_activatecode
-- -----------------------------------------------------
DROP TABLE IF EXISTS vauth_activatecode ;

CREATE TABLE IF NOT EXISTS vauth_activatecode (
  ACTIVATECODE_ID BIGSERIAL NOT NULL PRIMARY KEY,
  USER_ID BIGINT NOT NULL,
  ACTIVATECODE VARCHAR(32) NOT NULL DEFAULT '',
  CREATED TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT FK_ACTIVATECODE_USER
    FOREIGN KEY (USER_ID)
    REFERENCES vauth_user (USER_ID)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);

CREATE INDEX IDX_ACTIVATECODE ON vauth_activatecode USING btree (ACTIVATECODE ASC);

CREATE UNIQUE INDEX USER_ID_UNIQUE ON vauth_activatecode USING btree (USER_ID ASC);

CREATE TRIGGER ACTIVATECODE_CREATED_UPDATE_trg BEFORE UPDATE ON vauth_activatecode FOR EACH ROW EXECUTE PROCEDURE created_update();

-- -----------------------------------------------------
-- Table vauth_authcode
-- -----------------------------------------------------
DROP TABLE IF EXISTS vauth_authcode ;

CREATE TABLE IF NOT EXISTS vauth_authcode (
  AUTHCODE_ID BIGSERIAL NOT NULL PRIMARY KEY,
  APPLICATION_ID BIGINT NOT NULL,
  USER_ID BIGINT NOT NULL,
  AUTHCODE VARCHAR(32) NOT NULL,
  CREATED TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  REDIRECT_URL VARCHAR(5000) NOT NULL,
  IP VARCHAR(100) NOT NULL,
  USER_AGENT VARCHAR(200) NOT NULL,
  CONSTRAINT FK_AUTHCODE_APPLICATION
    FOREIGN KEY (APPLICATION_ID)
    REFERENCES vauth_application (APPLICATION_ID)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT FK_AUTHCODE_USER
    FOREIGN KEY (USER_ID)
    REFERENCES vauth_user (USER_ID)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);

CREATE INDEX IDX_AUTHCODE ON vauth_authcode USING btree (AUTHCODE ASC);

CREATE UNIQUE INDEX AUTHCODE_USER_ID_UNIQUE ON vauth_authcode USING btree (USER_ID ASC, APPLICATION_ID ASC, IP ASC, USER_AGENT ASC);

CREATE INDEX FK_AUTHCODE_APPLICATION_idx ON vauth_authcode USING btree (APPLICATION_ID ASC);

CREATE TRIGGER AUTHCODE_CREATED_UPDATE_trg BEFORE UPDATE ON vauth_authcode FOR EACH ROW EXECUTE PROCEDURE created_update();

-- -----------------------------------------------------
-- Table vauth_token
-- -----------------------------------------------------
DROP TABLE IF EXISTS vauth_token ;

CREATE TABLE IF NOT EXISTS vauth_token (
  TOKEN_ID BIGSERIAL NOT NULL PRIMARY KEY,
  TOKEN VARCHAR(32) NOT NULL,
  USER_ID BIGINT NOT NULL,
  APPLICATION_ID BIGINT NOT NULL,
  EXPIRATION_TOKEN VARCHAR(32) NOT NULL,
  IP VARCHAR(100) NOT NULL,
  USER_AGENT VARCHAR(200) NOT NULL,
  CREATED TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT FK_TOKEN_USER
    FOREIGN KEY (USER_ID)
    REFERENCES vauth_user (USER_ID)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT FK_TOKEN_APPLICATION
    FOREIGN KEY (APPLICATION_ID)
    REFERENCES vauth_application (APPLICATION_ID)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);

CREATE UNIQUE INDEX TOKEN_UNIQUE ON vauth_token USING btree (TOKEN ASC);

CREATE UNIQUE INDEX EXPIRATION_TOKEN_UNIQUE ON vauth_token USING btree (EXPIRATION_TOKEN ASC);

CREATE UNIQUE INDEX TOKEN_USER_ID_UNIQUE ON vauth_token USING btree (USER_ID ASC, APPLICATION_ID ASC, IP ASC, USER_AGENT ASC);

CREATE INDEX IDX_TOKEN ON vauth_token USING btree (TOKEN ASC, CREATED ASC, EXPIRATION_TOKEN ASC, USER_ID ASC, IP ASC, USER_AGENT ASC);

CREATE INDEX FK_TOKEN_APPLICATION_idx ON vauth_token USING btree (APPLICATION_ID ASC);

CREATE TRIGGER TOKEN_CREATED_UPDATE_trg BEFORE UPDATE ON vauth_token FOR EACH ROW EXECUTE PROCEDURE created_update();

-- -----------------------------------------------------
-- Table vauth_property
-- -----------------------------------------------------
DROP TABLE IF EXISTS vauth_property ;

CREATE TABLE IF NOT EXISTS vauth_property (
  PROPERTY_ID BIGSERIAL NOT NULL PRIMARY KEY,
  EXTERNAL_ID VARCHAR(50) NOT NULL,
  CREATED TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  TAG VARCHAR(32) NOT NULL DEFAULT 'common',
  DEFAULT_VALUE VARCHAR(5000) DEFAULT NULL
);

CREATE UNIQUE INDEX PROPERTY_EXTERNAL_ID_UNIQUE ON vauth_property USING btree (EXTERNAL_ID ASC);

CREATE TRIGGER PROPERTY_CREATED_UPDATE_trg BEFORE UPDATE ON vauth_property FOR EACH ROW EXECUTE PROCEDURE created_update();

-- -----------------------------------------------------
-- Table vauth_uservalue
-- -----------------------------------------------------
DROP TABLE IF EXISTS vauth_uservalue ;

CREATE TABLE IF NOT EXISTS vauth_uservalue (
  ID BIGSERIAL NOT NULL PRIMARY KEY,
  USER_ID BIGINT NOT NULL,
  PROPERTY_ID BIGINT NOT NULL,
  USERVALUE VARCHAR(5000) NULL,
  CREATED TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT FK_USERVALUE_PROPERTY
    FOREIGN KEY (PROPERTY_ID)
    REFERENCES vauth_property (PROPERTY_ID)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT FK_USERVALUE_USER
    FOREIGN KEY (USER_ID)
    REFERENCES vauth_user (USER_ID)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);

CREATE UNIQUE INDEX UQ_USERVALUE ON vauth_uservalue USING btree (USER_ID ASC, PROPERTY_ID ASC);

CREATE INDEX IDX_USERVALUE_ID ON vauth_uservalue USING btree (USER_ID ASC);

CREATE INDEX FK_USERVALUE_PROPERTY_idx ON vauth_uservalue USING btree (PROPERTY_ID ASC);

CREATE TRIGGER USERVALUE_CREATED_UPDATE_trg BEFORE UPDATE ON vauth_uservalue FOR EACH ROW EXECUTE PROCEDURE created_update();

-- -----------------------------------------------------
-- Table vauth_role
-- -----------------------------------------------------
DROP TABLE IF EXISTS vauth_role ;

CREATE TABLE IF NOT EXISTS vauth_role (
  ROLE_ID BIGSERIAL NOT NULL PRIMARY KEY,
  EXTERNAL_ID VARCHAR(50) NOT NULL,
  CREATED TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE UNIQUE INDEX ROLE_EXTERNAL_ID_UNIQUE ON vauth_role USING btree (EXTERNAL_ID ASC);

CREATE INDEX IDX_ROLE_ID ON vauth_role USING btree (ROLE_ID ASC, EXTERNAL_ID ASC);

CREATE TRIGGER ROLE_CREATED_UPDATE_trg BEFORE UPDATE ON vauth_role FOR EACH ROW EXECUTE PROCEDURE created_update();

-- -----------------------------------------------------
-- Table vauth_group
-- -----------------------------------------------------
DROP TABLE IF EXISTS vauth_group ;

CREATE TABLE IF NOT EXISTS vauth_group (
  GROUP_ID BIGSERIAL NOT NULL PRIMARY KEY,
  EXTERNAL_ID VARCHAR(50) NOT NULL,
  ENABLED BOOLEAN NOT NULL,
  CREATED TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE UNIQUE INDEX GROUP_EXTERNAL_ID_UNIQUE ON vauth_group USING btree (EXTERNAL_ID ASC);

CREATE INDEX IDX_GROUP_ID ON vauth_group USING btree (GROUP_ID ASC, EXTERNAL_ID ASC);

CREATE TRIGGER GROUP_CREATED_UPDATE_trg BEFORE UPDATE ON vauth_group FOR EACH ROW EXECUTE PROCEDURE created_update();

-- -----------------------------------------------------
-- Table vauth_user2group
-- -----------------------------------------------------
DROP TABLE IF EXISTS vauth_user2group ;

CREATE TABLE IF NOT EXISTS vauth_user2group (
  ID BIGSERIAL NOT NULL PRIMARY KEY,
  USER_ID BIGINT NOT NULL,
  GROUP_ID BIGINT NOT NULL,
  CREATED TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT FK_User2Group_User
    FOREIGN KEY (USER_ID)
    REFERENCES vauth_user (USER_ID)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT FK_User2Group_Group
    FOREIGN KEY (GROUP_ID)
    REFERENCES vauth_group (GROUP_ID)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);

CREATE INDEX FK_User2Group_User_idx ON vauth_user2group USING btree (USER_ID ASC);

CREATE INDEX FK_User2Group_Group_idx ON vauth_user2group USING btree (GROUP_ID ASC);

CREATE UNIQUE INDEX UQ_USER2GROUP ON vauth_user2group USING btree (USER_ID ASC, GROUP_ID ASC);

CREATE INDEX IDX_USER2GROUP ON vauth_user2group USING btree (USER_ID ASC, GROUP_ID ASC);

CREATE TRIGGER USER2GROUP_CREATED_UPDATE_trg BEFORE UPDATE ON vauth_user2group FOR EACH ROW EXECUTE PROCEDURE created_update();

-- -----------------------------------------------------
-- Table vauth_user2role
-- -----------------------------------------------------
DROP TABLE IF EXISTS vauth_user2role ;

CREATE TABLE IF NOT EXISTS vauth_user2role (
  ID BIGSERIAL NOT NULL PRIMARY KEY,
  USER_ID BIGINT NOT NULL,
  ROLE_ID BIGINT NOT NULL,
  CREATED TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT FK_USER2ROLE_USER
    FOREIGN KEY (USER_ID)
    REFERENCES vauth_user (USER_ID)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT FK_USER2ROLE_ROLE
    FOREIGN KEY (ROLE_ID)
    REFERENCES vauth_role (ROLE_ID)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);

CREATE UNIQUE INDEX UQ_USER2ROLE ON vauth_user2role USING btree (USER_ID ASC, ROLE_ID ASC);

CREATE INDEX IDX_USER2ROLE ON vauth_user2role USING btree (USER_ID ASC, ROLE_ID ASC);

CREATE INDEX FK_USER2ROLE_ROLE_idx ON vauth_user2role USING btree (ROLE_ID ASC);

CREATE TRIGGER USER2ROLE_CREATED_UPDATE_trg BEFORE UPDATE ON vauth_user2role FOR EACH ROW EXECUTE PROCEDURE created_update();

-- -----------------------------------------------------
-- Table vauth_group2role
-- -----------------------------------------------------
DROP TABLE IF EXISTS vauth_group2role ;

CREATE TABLE IF NOT EXISTS vauth_group2role (
  ID BIGSERIAL NOT NULL PRIMARY KEY,
  GROUP_ID BIGINT NOT NULL,
  ROLE_ID BIGINT NOT NULL,
  CREATED TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT FK_GROUP2ROLE_GROUP
    FOREIGN KEY (GROUP_ID)
    REFERENCES vauth_group (GROUP_ID)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT FK_GROUP2ROLE_ROLE
    FOREIGN KEY (ROLE_ID)
    REFERENCES vauth_role (ROLE_ID)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);

CREATE UNIQUE INDEX UQ_GROUP2ROLE ON vauth_group2role USING btree (GROUP_ID ASC, ROLE_ID ASC);

CREATE INDEX IDX_GROUP2ROLE ON vauth_group2role USING btree (GROUP_ID ASC, ROLE_ID ASC);

CREATE INDEX FK_GROUP2ROLE_ROLE_idx ON vauth_group2role USING btree (ROLE_ID ASC);

CREATE TRIGGER GROUP2ROLE_CREATED_UPDATE_trg BEFORE UPDATE ON vauth_group2role FOR EACH ROW EXECUTE PROCEDURE created_update();













-- -----------------------------------------------------
-- Data for table vauth_application
-- -----------------------------------------------------
START TRANSACTION;
INSERT INTO vauth_application (EXTERNAL_ID, CREATED, ENABLED, CLIENT_ID, CLIENT_SECRET) VALUES ('vauth_rest_api', CURRENT_TIMESTAMP, true, MD5(UUID_GENERATE_V4()::VARCHAR), MD5(uuid_generate_v1()::varchar));
INSERT INTO vauth_application (EXTERNAL_ID, CREATED, ENABLED, CLIENT_ID, CLIENT_SECRET) VALUES ('vauth_web_ui', CURRENT_TIMESTAMP, true, MD5(UUID_GENERATE_V4()::VARCHAR), MD5(UUID_GENERATE_V4()::VARCHAR));
INSERT INTO vauth_application (EXTERNAL_ID, CREATED, ENABLED, CLIENT_ID, CLIENT_SECRET) VALUES ('vauth_notify_service', CURRENT_TIMESTAMP, true, MD5(UUID_GENERATE_V4()::VARCHAR), MD5(UUID_GENERATE_V4()::VARCHAR));

COMMIT;

-- -----------------------------------------------------
-- Data for table vauth_user
-- -----------------------------------------------------
START TRANSACTION;
INSERT INTO vauth_user (EXTERNAL_ID, ENABLED, CREATED, PASSWORD) VALUES ('admin', true, CURRENT_TIMESTAMP, 'd51ef89187525e3d8dda78f2e104d44a');
INSERT INTO vauth_user (EXTERNAL_ID, ENABLED, CREATED, PASSWORD) VALUES ('web_ui', true, CURRENT_TIMESTAMP, 'd51ef89187525e3d8dda78f2e104d44a');
INSERT INTO vauth_user (EXTERNAL_ID, ENABLED, CREATED, PASSWORD) VALUES ('notify_service', true, CURRENT_TIMESTAMP, 'd51ef89187525e3d8dda78f2e104d44a');
INSERT INTO vauth_user (EXTERNAL_ID, ENABLED, CREATED, PASSWORD) VALUES ('manager', true, CURRENT_TIMESTAMP, 'd51ef89187525e3d8dda78f2e104d44a');

COMMIT;

-- -----------------------------------------------------
-- Data for table vauth_property
-- -----------------------------------------------------
START TRANSACTION;
INSERT INTO vauth_property (EXTERNAL_ID, CREATED, TAG, DEFAULT_VALUE) VALUES ('account_locked', CURRENT_TIMESTAMP, 'common', 'true');
INSERT INTO vauth_property (EXTERNAL_ID, CREATED, TAG, DEFAULT_VALUE) VALUES ('active', CURRENT_TIMESTAMP, 'common', 'true');
INSERT INTO vauth_property (EXTERNAL_ID, CREATED, TAG, DEFAULT_VALUE) VALUES ('address', CURRENT_TIMESTAMP, 'common', 'MainStreet,Longon');
INSERT INTO vauth_property (EXTERNAL_ID, CREATED, TAG, DEFAULT_VALUE) VALUES ('address_street', CURRENT_TIMESTAMP, 'common', 'MainStreet');
INSERT INTO vauth_property (EXTERNAL_ID, CREATED, TAG, DEFAULT_VALUE) VALUES ('birth_date', CURRENT_TIMESTAMP, 'common', '');
INSERT INTO vauth_property (EXTERNAL_ID, CREATED, TAG, DEFAULT_VALUE) VALUES ('country', CURRENT_TIMESTAMP, 'common', 'SK');
INSERT INTO vauth_property (EXTERNAL_ID, CREATED, TAG, DEFAULT_VALUE) VALUES ('display_name', CURRENT_TIMESTAMP, 'common', 'UserName');
INSERT INTO vauth_property (EXTERNAL_ID, CREATED, TAG, DEFAULT_VALUE) VALUES ('email', CURRENT_TIMESTAMP, 'common', 'example@example.com');
INSERT INTO vauth_property (EXTERNAL_ID, CREATED, TAG, DEFAULT_VALUE) VALUES ('entitlements', CURRENT_TIMESTAMP, 'common', '');
INSERT INTO vauth_property (EXTERNAL_ID, CREATED, TAG, DEFAULT_VALUE) VALUES ('fax_number', CURRENT_TIMESTAMP, 'common', '+421901123456');
INSERT INTO vauth_property (EXTERNAL_ID, CREATED, TAG, DEFAULT_VALUE) VALUES ('first_name', CURRENT_TIMESTAMP, 'common', 'John');
INSERT INTO vauth_property (EXTERNAL_ID, CREATED, TAG, DEFAULT_VALUE) VALUES ('full_name', CURRENT_TIMESTAMP, 'common', 'John Smith');
INSERT INTO vauth_property (EXTERNAL_ID, CREATED, TAG, DEFAULT_VALUE) VALUES ('gender', CURRENT_TIMESTAMP, 'common', 'male');
INSERT INTO vauth_property (EXTERNAL_ID, CREATED, TAG, DEFAULT_VALUE) VALUES ('home_email', CURRENT_TIMESTAMP, 'common', 'home@example.com');
INSERT INTO vauth_property (EXTERNAL_ID, CREATED, TAG, DEFAULT_VALUE) VALUES ('home_number', CURRENT_TIMESTAMP, 'common', 'John');
INSERT INTO vauth_property (EXTERNAL_ID, CREATED, TAG, DEFAULT_VALUE) VALUES ('honoric_prefix', CURRENT_TIMESTAMP, 'common', null);
INSERT INTO vauth_property (EXTERNAL_ID, CREATED, TAG, DEFAULT_VALUE) VALUES ('honoric_suffix', CURRENT_TIMESTAMP, 'common', null);
INSERT INTO vauth_property (EXTERNAL_ID, CREATED, TAG, DEFAULT_VALUE) VALUES ('im', CURRENT_TIMESTAMP, 'common', null);
INSERT INTO vauth_property (EXTERNAL_ID, CREATED, TAG, DEFAULT_VALUE) VALUES ('last_login', CURRENT_TIMESTAMP, 'common', null);
INSERT INTO vauth_property (EXTERNAL_ID, CREATED, TAG, DEFAULT_VALUE) VALUES ('last_name', CURRENT_TIMESTAMP, 'common', 'Smith');
INSERT INTO vauth_property (EXTERNAL_ID, CREATED, TAG, DEFAULT_VALUE) VALUES ('last_password_update', CURRENT_TIMESTAMP, 'common', null);
INSERT INTO vauth_property (EXTERNAL_ID, CREATED, TAG, DEFAULT_VALUE) VALUES ('local', CURRENT_TIMESTAMP, 'common', null);
INSERT INTO vauth_property (EXTERNAL_ID, CREATED, TAG, DEFAULT_VALUE) VALUES ('locality', CURRENT_TIMESTAMP, 'common', null);
INSERT INTO vauth_property (EXTERNAL_ID, CREATED, TAG, DEFAULT_VALUE) VALUES ('location', CURRENT_TIMESTAMP, 'common', null);
INSERT INTO vauth_property (EXTERNAL_ID, CREATED, TAG, DEFAULT_VALUE) VALUES ('middle_name', CURRENT_TIMESTAMP, 'common', null);
INSERT INTO vauth_property (EXTERNAL_ID, CREATED, TAG, DEFAULT_VALUE) VALUES ('mobile', CURRENT_TIMESTAMP, 'common', null);
INSERT INTO vauth_property (EXTERNAL_ID, CREATED, TAG, DEFAULT_VALUE) VALUES ('nick_name', CURRENT_TIMESTAMP, 'common', null);
INSERT INTO vauth_property (EXTERNAL_ID, CREATED, TAG, DEFAULT_VALUE) VALUES ('organization', CURRENT_TIMESTAMP, 'common', 'vilten');
INSERT INTO vauth_property (EXTERNAL_ID, CREATED, TAG, DEFAULT_VALUE) VALUES ('other_email', CURRENT_TIMESTAMP, 'common', null);
INSERT INTO vauth_property (EXTERNAL_ID, CREATED, TAG, DEFAULT_VALUE) VALUES ('other_phone', CURRENT_TIMESTAMP, 'common', null);
INSERT INTO vauth_property (EXTERNAL_ID, CREATED, TAG, DEFAULT_VALUE) VALUES ('pager_number', CURRENT_TIMESTAMP, 'common', null);
INSERT INTO vauth_property (EXTERNAL_ID, CREATED, TAG, DEFAULT_VALUE) VALUES ('photo', CURRENT_TIMESTAMP, 'common', null);
INSERT INTO vauth_property (EXTERNAL_ID, CREATED, TAG, DEFAULT_VALUE) VALUES ('photo_thumbnail', CURRENT_TIMESTAMP, 'common', null);
INSERT INTO vauth_property (EXTERNAL_ID, CREATED, TAG, DEFAULT_VALUE) VALUES ('photo_url', CURRENT_TIMESTAMP, 'common', null);
INSERT INTO vauth_property (EXTERNAL_ID, CREATED, TAG, DEFAULT_VALUE) VALUES ('postal_code', CURRENT_TIMESTAMP, 'common', null);
INSERT INTO vauth_property (EXTERNAL_ID, CREATED, TAG, DEFAULT_VALUE) VALUES ('preferred_language', CURRENT_TIMESTAMP, 'common', 'sk');
INSERT INTO vauth_property (EXTERNAL_ID, CREATED, TAG, DEFAULT_VALUE) VALUES ('region', CURRENT_TIMESTAMP, 'common', null);
INSERT INTO vauth_property (EXTERNAL_ID, CREATED, TAG, DEFAULT_VALUE) VALUES ('state', CURRENT_TIMESTAMP, 'common', null);
INSERT INTO vauth_property (EXTERNAL_ID, CREATED, TAG, DEFAULT_VALUE) VALUES ('telephone', CURRENT_TIMESTAMP, 'common', null);
INSERT INTO vauth_property (EXTERNAL_ID, CREATED, TAG, DEFAULT_VALUE) VALUES ('time_zone', CURRENT_TIMESTAMP, 'common', 'Europe/Bratislava');
INSERT INTO vauth_property (EXTERNAL_ID, CREATED, TAG, DEFAULT_VALUE) VALUES ('title', CURRENT_TIMESTAMP, 'common', null);
INSERT INTO vauth_property (EXTERNAL_ID, CREATED, TAG, DEFAULT_VALUE) VALUES ('url', CURRENT_TIMESTAMP, 'common', null);
INSERT INTO vauth_property (EXTERNAL_ID, CREATED, TAG, DEFAULT_VALUE) VALUES ('user_id', CURRENT_TIMESTAMP, 'common', null);
INSERT INTO vauth_property (EXTERNAL_ID, CREATED, TAG, DEFAULT_VALUE) VALUES ('user_type', CURRENT_TIMESTAMP, 'common', null);
INSERT INTO vauth_property (EXTERNAL_ID, CREATED, TAG, DEFAULT_VALUE) VALUES ('verify_email', CURRENT_TIMESTAMP, 'common', null);
INSERT INTO vauth_property (EXTERNAL_ID, CREATED, TAG, DEFAULT_VALUE) VALUES ('work_email', CURRENT_TIMESTAMP, 'common', null);
INSERT INTO vauth_property (EXTERNAL_ID, CREATED, TAG, DEFAULT_VALUE) VALUES ('work_number', CURRENT_TIMESTAMP, 'common', null);

COMMIT;

-- -----------------------------------------------------
-- Data for table vauth_role
-- -----------------------------------------------------
START TRANSACTION;
INSERT INTO vauth_role (EXTERNAL_ID, CREATED) VALUES ('vauth_admin', CURRENT_TIMESTAMP);
INSERT INTO vauth_role (EXTERNAL_ID, CREATED) VALUES ('vauth_actcode_read', CURRENT_TIMESTAMP);
INSERT INTO vauth_role (EXTERNAL_ID, CREATED) VALUES ('vauth_actcode_write', CURRENT_TIMESTAMP);
INSERT INTO vauth_role (EXTERNAL_ID, CREATED) VALUES ('vauth_application_read', CURRENT_TIMESTAMP);
INSERT INTO vauth_role (EXTERNAL_ID, CREATED) VALUES ('vauth_application_write', CURRENT_TIMESTAMP);
INSERT INTO vauth_role (EXTERNAL_ID, CREATED) VALUES ('vauth_authcode_read', CURRENT_TIMESTAMP);
INSERT INTO vauth_role (EXTERNAL_ID, CREATED) VALUES ('vauth_authcode_write', CURRENT_TIMESTAMP);
INSERT INTO vauth_role (EXTERNAL_ID, CREATED) VALUES ('vauth_group_read', CURRENT_TIMESTAMP);
INSERT INTO vauth_role (EXTERNAL_ID, CREATED) VALUES ('vauth_group_write', CURRENT_TIMESTAMP);
INSERT INTO vauth_role (EXTERNAL_ID, CREATED) VALUES ('vauth_property_read', CURRENT_TIMESTAMP);
INSERT INTO vauth_role (EXTERNAL_ID, CREATED) VALUES ('vauth_property_write', CURRENT_TIMESTAMP);
INSERT INTO vauth_role (EXTERNAL_ID, CREATED) VALUES ('vauth_role_read', CURRENT_TIMESTAMP);
INSERT INTO vauth_role (EXTERNAL_ID, CREATED) VALUES ('vauth_role_write', CURRENT_TIMESTAMP);
INSERT INTO vauth_role (EXTERNAL_ID, CREATED) VALUES ('vauth_server_read', CURRENT_TIMESTAMP);
INSERT INTO vauth_role (EXTERNAL_ID, CREATED) VALUES ('vauth_server_write', CURRENT_TIMESTAMP);
INSERT INTO vauth_role (EXTERNAL_ID, CREATED) VALUES ('vauth_token_read', CURRENT_TIMESTAMP);
INSERT INTO vauth_role (EXTERNAL_ID, CREATED) VALUES ('vauth_token_write', CURRENT_TIMESTAMP);
INSERT INTO vauth_role (EXTERNAL_ID, CREATED) VALUES ('vauth_user_read', CURRENT_TIMESTAMP);
INSERT INTO vauth_role (EXTERNAL_ID, CREATED) VALUES ('vauth_user_write', CURRENT_TIMESTAMP);
INSERT INTO vauth_role (EXTERNAL_ID, CREATED) VALUES ('vauth_resetcode_read', CURRENT_TIMESTAMP);
INSERT INTO vauth_role (EXTERNAL_ID, CREATED) VALUES ('vauth_resetcode_write', CURRENT_TIMESTAMP);
INSERT INTO vauth_role (EXTERNAL_ID, CREATED) VALUES ('vauth_activatecode_read', CURRENT_TIMESTAMP);
INSERT INTO vauth_role (EXTERNAL_ID, CREATED) VALUES ('vauth_activatecode_write', CURRENT_TIMESTAMP);

COMMIT;

-- -----------------------------------------------------
-- Data for table vauth_user2role
-- -----------------------------------------------------
START TRANSACTION;
INSERT INTO vauth_user2role (USER_ID, ROLE_ID, CREATED) VALUES (1, 1, CURRENT_TIMESTAMP);
INSERT INTO vauth_user2role (USER_ID, ROLE_ID, CREATED) VALUES (2, 1, CURRENT_TIMESTAMP);

COMMIT;







-- -----------------------------------------------------
-- triggers
-- -----------------------------------------------------

DROP FUNCTION IF EXISTS client_update();

CREATE OR REPLACE FUNCTION client_update() RETURNS TRIGGER AS $$
BEGIN
    NEW.CLIENT_ID = MD5(UUID_GENERATE_V4()::VARCHAR);
    NEW.CLIENT_SECRET = MD5(UUID_GENERATE_V4()::VARCHAR);
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER vauth_application_BEFORE_INSERT BEFORE INSERT ON vauth_application FOR EACH ROW EXECUTE PROCEDURE client_update();


DROP FUNCTION IF EXISTS application_no_update();
DROP FUNCTION IF EXISTS application_no_delete();

CREATE OR REPLACE FUNCTION application_no_update() RETURNS TRIGGER AS $$
BEGIN
    IF ( OLD.APPLICATION_ID >= 1 AND OLD.APPLICATION_ID <= 2 ) THEN RAISE EXCEPTION 'Default values cannot update!';
    END IF;
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE OR REPLACE FUNCTION application_no_delete() RETURNS TRIGGER AS $$
BEGIN
    IF ( OLD.APPLICATION_ID >= 1 AND OLD.APPLICATION_ID <= 2 ) THEN RAISE EXCEPTION 'Default values cannot update!';
    END IF;
    RETURN OLD;
END;
$$ language 'plpgsql';

CREATE TRIGGER vauth_application_BEFORE_UPDATE BEFORE UPDATE ON vauth_application FOR EACH ROW EXECUTE PROCEDURE application_no_update();
CREATE TRIGGER vauth_application_BEFORE_DELETE BEFORE DELETE ON vauth_application FOR EACH ROW EXECUTE PROCEDURE application_no_delete();


DROP FUNCTION IF EXISTS admin_update();

CREATE OR REPLACE FUNCTION admin_update() RETURNS TRIGGER AS $$
BEGIN
    IF ( OLD.USER_ID >= 1 AND OLD.USER_ID <= 1 ) THEN
          NEW.EXTERNAL_ID = 'admin';
          NEW.ENABLED = true;
          NEW.PASSWORD = OLD.PASSWORD;
    END IF;
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER vauth_user_BEFORE_UPDATE BEFORE UPDATE ON vauth_user FOR EACH ROW EXECUTE PROCEDURE admin_update();


DROP FUNCTION IF EXISTS user_no_update();

CREATE OR REPLACE FUNCTION user_no_update() RETURNS TRIGGER AS $$
BEGIN
    IF ( OLD.USER_ID >= 1 AND OLD.USER_ID <= 2 ) THEN RAISE EXCEPTION 'Default values cannot update!';
    END IF;
    RETURN OLD;
END;
$$ language 'plpgsql';

CREATE TRIGGER vauth_user_BEFORE_DELETE BEFORE DELETE ON vauth_user FOR EACH ROW EXECUTE PROCEDURE user_no_update();


DROP FUNCTION IF EXISTS actcode_update();

CREATE OR REPLACE FUNCTION actcode_update() RETURNS TRIGGER AS $$
BEGIN
    NEW.ACTCODE = MD5(UUID_GENERATE_V4()::VARCHAR);
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER vauth_actcode_BEFORE_INSERT BEFORE INSERT ON vauth_actcode FOR EACH ROW EXECUTE PROCEDURE actcode_update();
CREATE TRIGGER vauth_actcode_BEFORE_UPDATE BEFORE UPDATE ON vauth_actcode FOR EACH ROW EXECUTE PROCEDURE actcode_update();


DROP FUNCTION IF EXISTS resetcode_update();

CREATE OR REPLACE FUNCTION resetcode_update() RETURNS TRIGGER AS $$
BEGIN
    NEW.RESETCODE = MD5(UUID_GENERATE_V4()::VARCHAR);
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER vauth_resetcode_BEFORE_INSERT BEFORE INSERT ON vauth_resetcode FOR EACH ROW EXECUTE PROCEDURE resetcode_update();
CREATE TRIGGER vauth_resetcode_BEFORE_UPDATE BEFORE UPDATE ON vauth_resetcode FOR EACH ROW EXECUTE PROCEDURE resetcode_update();


DROP FUNCTION IF EXISTS activatecode_update();

CREATE OR REPLACE FUNCTION activatecode_update() RETURNS TRIGGER AS $$
BEGIN
    NEW.ACTIVATECODE = MD5(UUID_GENERATE_V4()::VARCHAR);
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER vauth_activatecode_BEFORE_INSERT BEFORE INSERT ON vauth_activatecode FOR EACH ROW EXECUTE PROCEDURE activatecode_update();
CREATE TRIGGER vauth_activatecode_BEFORE_UPDATE BEFORE UPDATE ON vauth_activatecode FOR EACH ROW EXECUTE PROCEDURE activatecode_update();


DROP FUNCTION IF EXISTS authcode_update();

CREATE OR REPLACE FUNCTION authcode_update() RETURNS TRIGGER AS $$
BEGIN
    NEW.AUTHCODE = MD5(UUID_GENERATE_V4()::VARCHAR);
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER vauth_authcode_BEFORE_INSERT BEFORE INSERT ON vauth_authcode FOR EACH ROW EXECUTE PROCEDURE authcode_update();
CREATE TRIGGER vauth_authcode_BEFORE_UPDATE BEFORE UPDATE ON vauth_authcode FOR EACH ROW EXECUTE PROCEDURE authcode_update();


DROP FUNCTION IF EXISTS token_update();

CREATE OR REPLACE FUNCTION token_update() RETURNS TRIGGER AS $$
BEGIN
    NEW.TOKEN = MD5(UUID_GENERATE_V4()::VARCHAR);
    NEW.EXPIRATION_TOKEN = MD5(UUID_GENERATE_V4()::VARCHAR);
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER vauth_token_BEFORE_INSERT BEFORE INSERT ON vauth_token FOR EACH ROW EXECUTE PROCEDURE token_update();
CREATE TRIGGER vauth_token_BEFORE_UPDATE BEFORE UPDATE ON vauth_token FOR EACH ROW EXECUTE PROCEDURE token_update();


DROP FUNCTION IF EXISTS property_no_update();
DROP FUNCTION IF EXISTS property_no_delete();

CREATE OR REPLACE FUNCTION property_no_update() RETURNS TRIGGER AS $$
BEGIN
    IF ( OLD.PROPERTY_ID >= 1 AND OLD.PROPERTY_ID <= 47 ) THEN RAISE EXCEPTION 'Default values cannot update!';
    END IF;
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE OR REPLACE FUNCTION property_no_delete() RETURNS TRIGGER AS $$
BEGIN
    IF ( OLD.PROPERTY_ID >= 1 AND OLD.PROPERTY_ID <= 47 ) THEN RAISE EXCEPTION 'Default values cannot update!';
    END IF;
    RETURN OLD;
END;
$$ language 'plpgsql';

CREATE TRIGGER vauth_property_BEFORE_UPDATE BEFORE UPDATE ON vauth_property FOR EACH ROW EXECUTE PROCEDURE property_no_update();
CREATE TRIGGER vauth_property_BEFORE_DELETE BEFORE DELETE ON vauth_property FOR EACH ROW EXECUTE PROCEDURE property_no_delete();


DROP FUNCTION IF EXISTS role_no_update();
DROP FUNCTION IF EXISTS role_no_delete();

CREATE OR REPLACE FUNCTION role_no_update() RETURNS TRIGGER AS $$
BEGIN
    IF ( OLD.ROLE_ID >= 1 AND OLD.ROLE_ID <= 19 ) THEN RAISE EXCEPTION 'Default values cannot update!';
    END IF;
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE OR REPLACE FUNCTION role_no_delete() RETURNS TRIGGER AS $$
BEGIN
    IF ( OLD.ROLE_ID >= 1 AND OLD.ROLE_ID <= 19 ) THEN RAISE EXCEPTION 'Default values cannot update!';
    END IF;
    RETURN OLD;
END;
$$ language 'plpgsql';

CREATE TRIGGER vauth_role_BEFORE_UPDATE BEFORE UPDATE ON vauth_role FOR EACH ROW EXECUTE PROCEDURE role_no_update();
CREATE TRIGGER vauth_role_BEFORE_DELETE BEFORE DELETE ON vauth_role FOR EACH ROW EXECUTE PROCEDURE role_no_delete();


DROP FUNCTION IF EXISTS user2role_no_update();
DROP FUNCTION IF EXISTS user2role_no_delete();

CREATE OR REPLACE FUNCTION user2role_no_update() RETURNS TRIGGER AS $$
BEGIN
    IF ( OLD.ID >= 1 AND OLD.ID <= 1 ) THEN RAISE EXCEPTION 'Default values cannot update!';
    END IF;
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE OR REPLACE FUNCTION user2role_no_delete() RETURNS TRIGGER AS $$
BEGIN
    IF ( OLD.ID >= 1 AND OLD.ID <= 1 ) THEN RAISE EXCEPTION 'Default values cannot update!';
    END IF;
    RETURN OLD;
END;
$$ language 'plpgsql';

CREATE TRIGGER vauth_user2role_BEFORE_UPDATE BEFORE UPDATE ON vauth_user2role FOR EACH ROW EXECUTE PROCEDURE user2role_no_update();
CREATE TRIGGER vauth_user2role_BEFORE_DELETE BEFORE DELETE ON vauth_user2role FOR EACH ROW EXECUTE PROCEDURE user2role_no_delete();

