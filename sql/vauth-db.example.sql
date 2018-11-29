-- -----------------------------------------------------
-- Schema vauth-db
-- -----------------------------------------------------
-- VAUTH database schema / Vilten's Identity Management
-- version: 1.1
-- since: 2017-03-16
-- created-by: vilten
SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

DROP SCHEMA IF EXISTS `vauth-db` ;

CREATE SCHEMA IF NOT EXISTS `vauth-db` DEFAULT CHARACTER SET utf8 ;

-- -----------------------------------------------------
-- Create user
-- -----------------------------------------------------
DROP USER IF EXISTS 'vauth-user'@'localhost';
CREATE USER 'vauth-user'@'localhost' IDENTIFIED BY 'vauth123';

GRANT ALL PRIVILEGES ON `vauth-db`.* TO 'vauth-user'@'localhost' WITH GRANT OPTION;

USE `vauth-db`;
-- -----------------------------------------------------
-- Table `vauth_application`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `vauth_application` ;

CREATE TABLE IF NOT EXISTS `vauth_application` (
  `APPLICATION_ID` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `EXTERNAL_ID` VARCHAR(45) NOT NULL,
  `CREATED` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `ENABLED` TINYINT(1) NOT NULL,
  `CLIENT_ID` VARCHAR(32) NOT NULL DEFAULT '',
  `CLIENT_SECRET` VARCHAR(32) NOT NULL DEFAULT '',
  PRIMARY KEY (`APPLICATION_ID`))
ENGINE = InnoDB;

CREATE UNIQUE INDEX `EXTERNAL_ID_UNIQUE` ON `vauth_application` (`EXTERNAL_ID` ASC);

CREATE UNIQUE INDEX `CLIENT_ID_UNIQUE` ON `vauth_application` (`CLIENT_ID` ASC);	

CREATE UNIQUE INDEX `CLIENT_SECRET_UNIQUE` ON `vauth_application` (`CLIENT_SECRET` ASC);	

-- -----------------------------------------------------
-- Table `vauth_user`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `vauth_user` ;

CREATE TABLE IF NOT EXISTS `vauth_user` (
  `USER_ID` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `EXTERNAL_ID` VARCHAR(100) UNIQUE NOT NULL,
  `ENABLED` TINYINT(1) NOT NULL,
  `CREATED` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `PASSWORD` VARCHAR(32) NOT NULL,
  PRIMARY KEY (`USER_ID`))
ENGINE = InnoDB;

CREATE UNIQUE INDEX `EXTERNAL_USER_ID_UNIQUE` ON `vauth_user` (`EXTERNAL_ID` ASC);

CREATE INDEX `IDX_USER_ID` ON `vauth_user` (`EXTERNAL_ID` ASC, `USER_ID` ASC);

-- -----------------------------------------------------
-- Table `vauth_actcode`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `vauth_actcode` ;

CREATE TABLE IF NOT EXISTS `vauth_actcode` (
  `ACTCODE_ID` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `USER_ID` BIGINT(20) NOT NULL,
  `ACTCODE` VARCHAR(32) NOT NULL DEFAULT '',
  `CREATED` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`ACTCODE_ID`),
  CONSTRAINT `FK_ACTCODE_USER`
    FOREIGN KEY (`USER_ID`)
    REFERENCES `vauth_user` (`USER_ID`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;

CREATE INDEX `IDX_ACTCODE` ON `vauth_actcode` (`ACTCODE` ASC);

CREATE UNIQUE INDEX `USER_ID_UNIQUE` ON `vauth_actcode` (`USER_ID` ASC);

-- -----------------------------------------------------
-- Table `vauth_resetcode`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `vauth_resetcode` ;

CREATE TABLE IF NOT EXISTS `vauth_resetcode` (
  `RESETCODE_ID` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `USER_ID` BIGINT(20) NOT NULL,
  `RESETCODE` VARCHAR(32) NOT NULL DEFAULT '',
  `CREATED` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`RESETCODE_ID`),
  CONSTRAINT `FK_RESETCODE_USER`
    FOREIGN KEY (`USER_ID`)
    REFERENCES `vauth_user` (`USER_ID`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;

CREATE INDEX `IDX_RESETCODE` ON `vauth_resetcode` (`RESETCODE` ASC);

CREATE UNIQUE INDEX `USER_ID_UNIQUE` ON `vauth_resetcode` (`USER_ID` ASC);

-- -----------------------------------------------------
-- Table `vauth_activatecode`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `vauth_activatecode` ;

CREATE TABLE IF NOT EXISTS `vauth_activatecode` (
  `ACTIVATECODE_ID` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `USER_ID` BIGINT(20) NOT NULL,
  `ACTIVATECODE` VARCHAR(32) NOT NULL DEFAULT '',
  `CREATED` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`ACTIVATECODE_ID`),
  CONSTRAINT `FK_ACTIVATECODE_USER`
    FOREIGN KEY (`USER_ID`)
    REFERENCES `vauth_user` (`USER_ID`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;

CREATE INDEX `IDX_ACTIVATECODE` ON `vauth_activatecode` (`ACTIVATECODE` ASC);

CREATE UNIQUE INDEX `USER_ID_UNIQUE` ON `vauth_activatecode` (`USER_ID` ASC);

-- -----------------------------------------------------
-- Table `vauth_nfccode`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `vauth_nfccode`;

CREATE TABLE IF NOT EXISTS `vauth_nfccode` (
  `NFCCODE_ID` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `USER_ID` BIGINT(20) NOT NULL,
  `APPLICATION_ID` BIGINT(20) NOT NULL,
  `NFCCODE` VARCHAR(32) NOT NULL DEFAULT '',
  `CREATED` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`NFCCODE_ID`),
  CONSTRAINT `FK_NFCCODE_APPLICATION`
    FOREIGN KEY (`APPLICATION_ID`)
    REFERENCES `vauth_application` (`APPLICATION_ID`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `FK_NFCCODE_USER`
    FOREIGN KEY (`USER_ID`)
    REFERENCES `vauth_user` (`USER_ID`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;

CREATE INDEX `IDX_NFCCODE` ON `vauth_nfccode` (`NFCCODE` ASC);

CREATE UNIQUE INDEX `NFC_USER_ID_UNIQUE` ON `vauth_nfccode` (`USER_ID` ASC, `APPLICATION_ID` ASC);

-- -----------------------------------------------------
-- Table `vauth_authcode`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `vauth_authcode` ;

CREATE TABLE IF NOT EXISTS `vauth_authcode` (
  `AUTHCODE_ID` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `APPLICATION_ID` BIGINT(20) NOT NULL,
  `USER_ID` BIGINT(20) NOT NULL,
  `AUTHCODE` VARCHAR(32) NOT NULL,
  `CREATED` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `REDIRECT_URL` VARCHAR(5000) NOT NULL,
  `IP` VARCHAR(100) NOT NULL,
  `USER_AGENT` VARCHAR(200) NOT NULL,
  PRIMARY KEY (`AUTHCODE_ID`),
  CONSTRAINT `FK_AUTHCODE_APPLICATION`
    FOREIGN KEY (`APPLICATION_ID`)
    REFERENCES `vauth_application` (`APPLICATION_ID`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `FK_AUTHCODE_USER`
    FOREIGN KEY (`USER_ID`)
    REFERENCES `vauth_user` (`USER_ID`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;

CREATE INDEX `IDX_AUTHCODE` ON `vauth_authcode` (`AUTHCODE` ASC);

CREATE UNIQUE INDEX `USER_ID_UNIQUE` ON `vauth_authcode` (`USER_ID` ASC, `APPLICATION_ID` ASC, `IP` ASC, `USER_AGENT` ASC);

CREATE INDEX `FK_AUTHCODE_APPLICATION_idx` ON `vauth_authcode` (`APPLICATION_ID` ASC);

-- -----------------------------------------------------
-- Table `vauth_token`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `vauth_token` ;

CREATE TABLE IF NOT EXISTS `vauth_token` (
  `TOKEN_ID` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `TOKEN` VARCHAR(32) NOT NULL,
  `USER_ID` BIGINT(20) NOT NULL,
  `APPLICATION_ID` BIGINT(20) NOT NULL,
  `EXPIRATION_TOKEN` VARCHAR(32) NOT NULL,
  `IP` VARCHAR(100) NOT NULL,
  `USER_AGENT` VARCHAR(200) NOT NULL,
  `CREATED` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`TOKEN_ID`),
  CONSTRAINT `FK_TOKEN_USER`
    FOREIGN KEY (`USER_ID`)
    REFERENCES `vauth_user` (`USER_ID`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `FK_TOKEN_APPLICATION`
    FOREIGN KEY (`APPLICATION_ID`)
    REFERENCES `vauth_application` (`APPLICATION_ID`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;

CREATE UNIQUE INDEX `TOKEN_UNIQUE` ON `vauth_token` (`TOKEN` ASC);

CREATE UNIQUE INDEX `EXPIRATION_TOKEN_UNIQUE` ON `vauth_token` (`EXPIRATION_TOKEN` ASC);

CREATE UNIQUE INDEX `USER_ID_UNIQUE` ON `vauth_token` (`USER_ID` ASC, `APPLICATION_ID` ASC, `IP` ASC, `USER_AGENT` ASC);

CREATE INDEX `IDX_TOKEN` ON `vauth_token` (`TOKEN` ASC, `CREATED` ASC, `EXPIRATION_TOKEN` ASC, `USER_ID` ASC, `IP` ASC, `USER_AGENT` ASC);

CREATE INDEX `FK_TOKEN_APPLICATION_idx` ON `vauth_token` (`APPLICATION_ID` ASC);

-- -----------------------------------------------------
-- Table `vauth_property`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `vauth_property` ;

CREATE TABLE IF NOT EXISTS `vauth_property` (
  `PROPERTY_ID` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `EXTERNAL_ID` VARCHAR(50) NOT NULL,
  `CREATED` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `TAG` VARCHAR(32) NOT NULL DEFAULT 'common',
  `DEFAULT_VALUE` VARCHAR(5000) DEFAULT NULL,
  PRIMARY KEY (`PROPERTY_ID`))
ENGINE = InnoDB;

CREATE UNIQUE INDEX `EXTERNAL_ID_UNIQUE` ON `vauth_property` (`EXTERNAL_ID` ASC);

-- -----------------------------------------------------
-- Table `vauth_uservalue`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `vauth_uservalue` ;

CREATE TABLE IF NOT EXISTS `vauth_uservalue` (
  `ID` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `USER_ID` BIGINT(20) NOT NULL,
  `PROPERTY_ID` BIGINT(20) NOT NULL,
  `USERVALUE` VARCHAR(5000) NULL,
  `CREATED` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`ID`),
  CONSTRAINT `FK_USERVALUE_PROPERTY`
    FOREIGN KEY (`PROPERTY_ID`)
    REFERENCES `vauth_property` (`PROPERTY_ID`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `FK_USERVALUE_USER`
    FOREIGN KEY (`USER_ID`)
    REFERENCES `vauth_user` (`USER_ID`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;

CREATE UNIQUE INDEX `UQ_USERVALUE` ON `vauth_uservalue` (`USER_ID` ASC, `PROPERTY_ID` ASC);

CREATE INDEX `IDX_USERVALUE_ID` ON `vauth_uservalue` (`USER_ID` ASC);

CREATE INDEX `FK_USERVALUE_PROPERTY_idx` ON `vauth_uservalue` (`PROPERTY_ID` ASC);

-- -----------------------------------------------------
-- Table `vauth_role`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `vauth_role` ;

CREATE TABLE IF NOT EXISTS `vauth_role` (
  `ROLE_ID` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `EXTERNAL_ID` VARCHAR(50) NOT NULL,
  `CREATED` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`ROLE_ID`))
ENGINE = InnoDB;

CREATE UNIQUE INDEX `EXTERNAL_ID_UNIQUE` ON `vauth_role` (`EXTERNAL_ID` ASC);

CREATE INDEX `IDX_ROLE_ID` ON `vauth_role` (`ROLE_ID` ASC, `EXTERNAL_ID` ASC);

-- -----------------------------------------------------
-- Table `vauth_group`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `vauth_group` ;

CREATE TABLE IF NOT EXISTS `vauth_group` (
  `GROUP_ID` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `EXTERNAL_ID` VARCHAR(50) NOT NULL,
  `ENABLED` TINYINT(1) NOT NULL,
  `CREATED` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`GROUP_ID`))
ENGINE = InnoDB;


CREATE UNIQUE INDEX `EXTERNAL_ID_UNIQUE` ON `vauth_group` (`EXTERNAL_ID` ASC);

CREATE INDEX `IDX_GROUP_ID` ON `vauth_group` (`GROUP_ID` ASC, `EXTERNAL_ID` ASC);

-- -----------------------------------------------------
-- Table `vauth_user2group`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `vauth_user2group` ;

CREATE TABLE IF NOT EXISTS `vauth_user2group` (
  `ID` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `USER_ID` BIGINT(20) NOT NULL,
  `GROUP_ID` BIGINT(20) NOT NULL,
  `CREATED` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`ID`),
  CONSTRAINT `FK_User2Group_User`
    FOREIGN KEY (`USER_ID`)
    REFERENCES `vauth_user` (`USER_ID`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `FK_User2Group_Group`
    FOREIGN KEY (`GROUP_ID`)
    REFERENCES `vauth_group` (`GROUP_ID`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;

CREATE INDEX `FK_User2Group_User_idx` ON `vauth_user2group` (`USER_ID` ASC);

CREATE INDEX `FK_User2Group_Group_idx` ON `vauth_user2group` (`GROUP_ID` ASC);

CREATE UNIQUE INDEX `UQ_USER2GROUP` ON `vauth_user2group` (`USER_ID` ASC, `GROUP_ID` ASC);

CREATE INDEX `IDX_USER2GROUP` ON `vauth_user2group` (`USER_ID` ASC, `GROUP_ID` ASC);

-- -----------------------------------------------------
-- Table `vauth_user2role`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `vauth_user2role` ;

CREATE TABLE IF NOT EXISTS `vauth_user2role` (
  `ID` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `USER_ID` BIGINT(20) NOT NULL,
  `ROLE_ID` BIGINT(20) NOT NULL,
  `CREATED` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`ID`),
  CONSTRAINT `FK_USER2ROLE_USER`
    FOREIGN KEY (`USER_ID`)
    REFERENCES `vauth_user` (`USER_ID`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `FK_USER2ROLE_ROLE`
    FOREIGN KEY (`ROLE_ID`)
    REFERENCES `vauth_role` (`ROLE_ID`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;

CREATE UNIQUE INDEX `UQ_USER2ROLE` ON `vauth_user2role` (`USER_ID` ASC, `ROLE_ID` ASC);

CREATE INDEX `IDX_USER2ROLE` ON `vauth_user2role` (`USER_ID` ASC, `ROLE_ID` ASC);

CREATE INDEX `FK_USER2ROLE_ROLE_idx` ON `vauth_user2role` (`ROLE_ID` ASC);

-- -----------------------------------------------------
-- Table `vauth_group2role`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `vauth_group2role` ;

CREATE TABLE IF NOT EXISTS `vauth_group2role` (
  `ID` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `GROUP_ID` BIGINT(20) NOT NULL,
  `ROLE_ID` BIGINT(20) NOT NULL,
  `CREATED` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`ID`),
  CONSTRAINT `FK_GROUP2ROLE_GROUP`
    FOREIGN KEY (`GROUP_ID`)
    REFERENCES `vauth_group` (`GROUP_ID`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `FK_GROUP2ROLE_ROLE`
    FOREIGN KEY (`ROLE_ID`)
    REFERENCES `vauth_role` (`ROLE_ID`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;

CREATE UNIQUE INDEX `UQ_GROUP2ROLE` ON `vauth_group2role` (`GROUP_ID` ASC, `ROLE_ID` ASC);

CREATE INDEX `IDX_GROUP2ROLE` ON `vauth_group2role` (`GROUP_ID` ASC, `ROLE_ID` ASC);

CREATE INDEX `FK_GROUP2ROLE_ROLE_idx` ON `vauth_group2role` (`ROLE_ID` ASC);


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;













-- -----------------------------------------------------
-- Data for table `vauth_application`
-- -----------------------------------------------------
START TRANSACTION;
USE `vauth-db`;
INSERT INTO `vauth_application` (`APPLICATION_ID`, `EXTERNAL_ID`, `CREATED`, `ENABLED`, `CLIENT_ID`, `CLIENT_SECRET`) VALUES (1, 'vauth_rest_api', CURRENT_TIMESTAMP, true, MD5(UUID()), MD5(UUID()));
INSERT INTO `vauth_application` (`APPLICATION_ID`, `EXTERNAL_ID`, `CREATED`, `ENABLED`, `CLIENT_ID`, `CLIENT_SECRET`) VALUES (2, 'vauth_web_ui', CURRENT_TIMESTAMP, true, MD5(UUID()), MD5(UUID()));
INSERT INTO `vauth_application` (`APPLICATION_ID`, `EXTERNAL_ID`, `CREATED`, `ENABLED`, `CLIENT_ID`, `CLIENT_SECRET`) VALUES (3, 'vauth_notify_service', CURRENT_TIMESTAMP, true, MD5(UUID()), MD5(UUID()));

COMMIT;

-- -----------------------------------------------------
-- Data for table `vauth_user`
-- -----------------------------------------------------
START TRANSACTION;
USE `vauth-db`;
INSERT INTO `vauth_user` (`USER_ID`, `EXTERNAL_ID`, `ENABLED`, `CREATED`, `PASSWORD`) VALUES (1, 'admin', true, CURRENT_TIMESTAMP, 'd51ef89187525e3d8dda78f2e104d44a');
INSERT INTO `vauth_user` (`USER_ID`, `EXTERNAL_ID`, `ENABLED`, `CREATED`, `PASSWORD`) VALUES (2, 'web_ui', true, CURRENT_TIMESTAMP, 'd51ef89187525e3d8dda78f2e104d44a');
INSERT INTO `vauth_user` (`USER_ID`, `EXTERNAL_ID`, `ENABLED`, `CREATED`, `PASSWORD`) VALUES (3, 'notify_service', true, CURRENT_TIMESTAMP, 'd51ef89187525e3d8dda78f2e104d44a');
INSERT INTO `vauth_user` (`USER_ID`, `EXTERNAL_ID`, `ENABLED`, `CREATED`, `PASSWORD`) VALUES (4, 'manager', true, CURRENT_TIMESTAMP, 'd51ef89187525e3d8dda78f2e104d44a');

COMMIT;

-- -----------------------------------------------------
-- Data for table `vauth_property`
-- -----------------------------------------------------
START TRANSACTION;
USE `vauth-db`;
INSERT INTO `vauth_property` (`PROPERTY_ID`, `EXTERNAL_ID`, `CREATED`, `TAG`, `DEFAULT_VALUE`) VALUES (1, 'account_locked', CURRENT_TIMESTAMP, 'common', 'true');
INSERT INTO `vauth_property` (`PROPERTY_ID`, `EXTERNAL_ID`, `CREATED`, `TAG`, `DEFAULT_VALUE`) VALUES (2, 'active', CURRENT_TIMESTAMP, 'common', 'true');
INSERT INTO `vauth_property` (`PROPERTY_ID`, `EXTERNAL_ID`, `CREATED`, `TAG`, `DEFAULT_VALUE`) VALUES (3, 'address', CURRENT_TIMESTAMP, 'common', 'MainStreet,Longon');
INSERT INTO `vauth_property` (`PROPERTY_ID`, `EXTERNAL_ID`, `CREATED`, `TAG`, `DEFAULT_VALUE`) VALUES (4, 'address_street', CURRENT_TIMESTAMP, 'common', 'MainStreet');
INSERT INTO `vauth_property` (`PROPERTY_ID`, `EXTERNAL_ID`, `CREATED`, `TAG`, `DEFAULT_VALUE`) VALUES (5, 'birth_date', CURRENT_TIMESTAMP, 'common', '');
INSERT INTO `vauth_property` (`PROPERTY_ID`, `EXTERNAL_ID`, `CREATED`, `TAG`, `DEFAULT_VALUE`) VALUES (6, 'country', CURRENT_TIMESTAMP, 'common', 'SK');
INSERT INTO `vauth_property` (`PROPERTY_ID`, `EXTERNAL_ID`, `CREATED`, `TAG`, `DEFAULT_VALUE`) VALUES (7, 'display_name', CURRENT_TIMESTAMP, 'common', 'UserName');
INSERT INTO `vauth_property` (`PROPERTY_ID`, `EXTERNAL_ID`, `CREATED`, `TAG`, `DEFAULT_VALUE`) VALUES (8, 'email', CURRENT_TIMESTAMP, 'common', 'example@example.com');
INSERT INTO `vauth_property` (`PROPERTY_ID`, `EXTERNAL_ID`, `CREATED`, `TAG`, `DEFAULT_VALUE`) VALUES (9, 'entitlements', CURRENT_TIMESTAMP, 'common', '');
INSERT INTO `vauth_property` (`PROPERTY_ID`, `EXTERNAL_ID`, `CREATED`, `TAG`, `DEFAULT_VALUE`) VALUES (10, 'fax_number', CURRENT_TIMESTAMP, 'common', '+421901123456');
INSERT INTO `vauth_property` (`PROPERTY_ID`, `EXTERNAL_ID`, `CREATED`, `TAG`, `DEFAULT_VALUE`) VALUES (11, 'first_name', CURRENT_TIMESTAMP, 'common', 'John');
INSERT INTO `vauth_property` (`PROPERTY_ID`, `EXTERNAL_ID`, `CREATED`, `TAG`, `DEFAULT_VALUE`) VALUES (12, 'full_name', CURRENT_TIMESTAMP, 'common', 'John Smith');
INSERT INTO `vauth_property` (`PROPERTY_ID`, `EXTERNAL_ID`, `CREATED`, `TAG`, `DEFAULT_VALUE`) VALUES (13, 'gender', CURRENT_TIMESTAMP, 'common', 'male');
INSERT INTO `vauth_property` (`PROPERTY_ID`, `EXTERNAL_ID`, `CREATED`, `TAG`, `DEFAULT_VALUE`) VALUES (14, 'home_email', CURRENT_TIMESTAMP, 'common', 'home@example.com');
INSERT INTO `vauth_property` (`PROPERTY_ID`, `EXTERNAL_ID`, `CREATED`, `TAG`, `DEFAULT_VALUE`) VALUES (15, 'home_number', CURRENT_TIMESTAMP, 'common', 'John');
INSERT INTO `vauth_property` (`PROPERTY_ID`, `EXTERNAL_ID`, `CREATED`, `TAG`, `DEFAULT_VALUE`) VALUES (16, 'honoric_prefix', CURRENT_TIMESTAMP, 'common', null);
INSERT INTO `vauth_property` (`PROPERTY_ID`, `EXTERNAL_ID`, `CREATED`, `TAG`, `DEFAULT_VALUE`) VALUES (17, 'honoric_suffix', CURRENT_TIMESTAMP, 'common', null);
INSERT INTO `vauth_property` (`PROPERTY_ID`, `EXTERNAL_ID`, `CREATED`, `TAG`, `DEFAULT_VALUE`) VALUES (18, 'im', CURRENT_TIMESTAMP, 'common', null);
INSERT INTO `vauth_property` (`PROPERTY_ID`, `EXTERNAL_ID`, `CREATED`, `TAG`, `DEFAULT_VALUE`) VALUES (19, 'last_login', CURRENT_TIMESTAMP, 'common', null);
INSERT INTO `vauth_property` (`PROPERTY_ID`, `EXTERNAL_ID`, `CREATED`, `TAG`, `DEFAULT_VALUE`) VALUES (20, 'last_name', CURRENT_TIMESTAMP, 'common', 'Smith');
INSERT INTO `vauth_property` (`PROPERTY_ID`, `EXTERNAL_ID`, `CREATED`, `TAG`, `DEFAULT_VALUE`) VALUES (21, 'last_password_update', CURRENT_TIMESTAMP, 'common', null);
INSERT INTO `vauth_property` (`PROPERTY_ID`, `EXTERNAL_ID`, `CREATED`, `TAG`, `DEFAULT_VALUE`) VALUES (22, 'local', CURRENT_TIMESTAMP, 'common', null);
INSERT INTO `vauth_property` (`PROPERTY_ID`, `EXTERNAL_ID`, `CREATED`, `TAG`, `DEFAULT_VALUE`) VALUES (23, 'locality', CURRENT_TIMESTAMP, 'common', null);
INSERT INTO `vauth_property` (`PROPERTY_ID`, `EXTERNAL_ID`, `CREATED`, `TAG`, `DEFAULT_VALUE`) VALUES (24, 'location', CURRENT_TIMESTAMP, 'common', null);
INSERT INTO `vauth_property` (`PROPERTY_ID`, `EXTERNAL_ID`, `CREATED`, `TAG`, `DEFAULT_VALUE`) VALUES (25, 'middle_name', CURRENT_TIMESTAMP, 'common', null);
INSERT INTO `vauth_property` (`PROPERTY_ID`, `EXTERNAL_ID`, `CREATED`, `TAG`, `DEFAULT_VALUE`) VALUES (26, 'mobile', CURRENT_TIMESTAMP, 'common', null);
INSERT INTO `vauth_property` (`PROPERTY_ID`, `EXTERNAL_ID`, `CREATED`, `TAG`, `DEFAULT_VALUE`) VALUES (27, 'nick_name', CURRENT_TIMESTAMP, 'common', null);
INSERT INTO `vauth_property` (`PROPERTY_ID`, `EXTERNAL_ID`, `CREATED`, `TAG`, `DEFAULT_VALUE`) VALUES (28, 'organization', CURRENT_TIMESTAMP, 'common', 'vilten');
INSERT INTO `vauth_property` (`PROPERTY_ID`, `EXTERNAL_ID`, `CREATED`, `TAG`, `DEFAULT_VALUE`) VALUES (29, 'other_email', CURRENT_TIMESTAMP, 'common', null);
INSERT INTO `vauth_property` (`PROPERTY_ID`, `EXTERNAL_ID`, `CREATED`, `TAG`, `DEFAULT_VALUE`) VALUES (30, 'other_phone', CURRENT_TIMESTAMP, 'common', null);
INSERT INTO `vauth_property` (`PROPERTY_ID`, `EXTERNAL_ID`, `CREATED`, `TAG`, `DEFAULT_VALUE`) VALUES (31, 'pager_number', CURRENT_TIMESTAMP, 'common', null);
INSERT INTO `vauth_property` (`PROPERTY_ID`, `EXTERNAL_ID`, `CREATED`, `TAG`, `DEFAULT_VALUE`) VALUES (32, 'photo', CURRENT_TIMESTAMP, 'common', null);
INSERT INTO `vauth_property` (`PROPERTY_ID`, `EXTERNAL_ID`, `CREATED`, `TAG`, `DEFAULT_VALUE`) VALUES (33, 'photo_thumbnail', CURRENT_TIMESTAMP, 'common', null);
INSERT INTO `vauth_property` (`PROPERTY_ID`, `EXTERNAL_ID`, `CREATED`, `TAG`, `DEFAULT_VALUE`) VALUES (34, 'photo_url', CURRENT_TIMESTAMP, 'common', null);
INSERT INTO `vauth_property` (`PROPERTY_ID`, `EXTERNAL_ID`, `CREATED`, `TAG`, `DEFAULT_VALUE`) VALUES (35, 'postal_code', CURRENT_TIMESTAMP, 'common', null);
INSERT INTO `vauth_property` (`PROPERTY_ID`, `EXTERNAL_ID`, `CREATED`, `TAG`, `DEFAULT_VALUE`) VALUES (36, 'preferred_language', CURRENT_TIMESTAMP, 'common', 'sk');
INSERT INTO `vauth_property` (`PROPERTY_ID`, `EXTERNAL_ID`, `CREATED`, `TAG`, `DEFAULT_VALUE`) VALUES (37, 'region', CURRENT_TIMESTAMP, 'common', null);
INSERT INTO `vauth_property` (`PROPERTY_ID`, `EXTERNAL_ID`, `CREATED`, `TAG`, `DEFAULT_VALUE`) VALUES (38, 'state', CURRENT_TIMESTAMP, 'common', null);
INSERT INTO `vauth_property` (`PROPERTY_ID`, `EXTERNAL_ID`, `CREATED`, `TAG`, `DEFAULT_VALUE`) VALUES (39, 'telephone', CURRENT_TIMESTAMP, 'common', null);
INSERT INTO `vauth_property` (`PROPERTY_ID`, `EXTERNAL_ID`, `CREATED`, `TAG`, `DEFAULT_VALUE`) VALUES (40, 'time_zone', CURRENT_TIMESTAMP, 'common', 'Europe/Bratislava');
INSERT INTO `vauth_property` (`PROPERTY_ID`, `EXTERNAL_ID`, `CREATED`, `TAG`, `DEFAULT_VALUE`) VALUES (41, 'title', CURRENT_TIMESTAMP, 'common', null);
INSERT INTO `vauth_property` (`PROPERTY_ID`, `EXTERNAL_ID`, `CREATED`, `TAG`, `DEFAULT_VALUE`) VALUES (42, 'url', CURRENT_TIMESTAMP, 'common', null);
INSERT INTO `vauth_property` (`PROPERTY_ID`, `EXTERNAL_ID`, `CREATED`, `TAG`, `DEFAULT_VALUE`) VALUES (43, 'user_id', CURRENT_TIMESTAMP, 'common', null);
INSERT INTO `vauth_property` (`PROPERTY_ID`, `EXTERNAL_ID`, `CREATED`, `TAG`, `DEFAULT_VALUE`) VALUES (44, 'user_type', CURRENT_TIMESTAMP, 'common', null);
INSERT INTO `vauth_property` (`PROPERTY_ID`, `EXTERNAL_ID`, `CREATED`, `TAG`, `DEFAULT_VALUE`) VALUES (45, 'verify_email', CURRENT_TIMESTAMP, 'common', null);
INSERT INTO `vauth_property` (`PROPERTY_ID`, `EXTERNAL_ID`, `CREATED`, `TAG`, `DEFAULT_VALUE`) VALUES (46, 'work_email', CURRENT_TIMESTAMP, 'common', null);
INSERT INTO `vauth_property` (`PROPERTY_ID`, `EXTERNAL_ID`, `CREATED`, `TAG`, `DEFAULT_VALUE`) VALUES (47, 'work_number', CURRENT_TIMESTAMP, 'common', null);

COMMIT;

-- -----------------------------------------------------
-- Data for table `vauth_role`
-- -----------------------------------------------------
START TRANSACTION;
USE `vauth-db`;
INSERT INTO `vauth_role` (`ROLE_ID`, `EXTERNAL_ID`, `CREATED`) VALUES (1, 'vauth_admin', CURRENT_TIMESTAMP);
INSERT INTO `vauth_role` (`ROLE_ID`, `EXTERNAL_ID`, `CREATED`) VALUES (2, 'vauth_actcode_read', CURRENT_TIMESTAMP);
INSERT INTO `vauth_role` (`ROLE_ID`, `EXTERNAL_ID`, `CREATED`) VALUES (3, 'vauth_actcode_write', CURRENT_TIMESTAMP);
INSERT INTO `vauth_role` (`ROLE_ID`, `EXTERNAL_ID`, `CREATED`) VALUES (4, 'vauth_application_read', CURRENT_TIMESTAMP);
INSERT INTO `vauth_role` (`ROLE_ID`, `EXTERNAL_ID`, `CREATED`) VALUES (5, 'vauth_application_write', CURRENT_TIMESTAMP);
INSERT INTO `vauth_role` (`ROLE_ID`, `EXTERNAL_ID`, `CREATED`) VALUES (6, 'vauth_authcode_read', CURRENT_TIMESTAMP);
INSERT INTO `vauth_role` (`ROLE_ID`, `EXTERNAL_ID`, `CREATED`) VALUES (7, 'vauth_authcode_write', CURRENT_TIMESTAMP);
INSERT INTO `vauth_role` (`ROLE_ID`, `EXTERNAL_ID`, `CREATED`) VALUES (8, 'vauth_group_read', CURRENT_TIMESTAMP);
INSERT INTO `vauth_role` (`ROLE_ID`, `EXTERNAL_ID`, `CREATED`) VALUES (9, 'vauth_group_write', CURRENT_TIMESTAMP);
INSERT INTO `vauth_role` (`ROLE_ID`, `EXTERNAL_ID`, `CREATED`) VALUES (10, 'vauth_property_read', CURRENT_TIMESTAMP);
INSERT INTO `vauth_role` (`ROLE_ID`, `EXTERNAL_ID`, `CREATED`) VALUES (11, 'vauth_property_write', CURRENT_TIMESTAMP);
INSERT INTO `vauth_role` (`ROLE_ID`, `EXTERNAL_ID`, `CREATED`) VALUES (12, 'vauth_role_read', CURRENT_TIMESTAMP);
INSERT INTO `vauth_role` (`ROLE_ID`, `EXTERNAL_ID`, `CREATED`) VALUES (13, 'vauth_role_write', CURRENT_TIMESTAMP);
INSERT INTO `vauth_role` (`ROLE_ID`, `EXTERNAL_ID`, `CREATED`) VALUES (14, 'vauth_server_read', CURRENT_TIMESTAMP);
INSERT INTO `vauth_role` (`ROLE_ID`, `EXTERNAL_ID`, `CREATED`) VALUES (15, 'vauth_server_write', CURRENT_TIMESTAMP);
INSERT INTO `vauth_role` (`ROLE_ID`, `EXTERNAL_ID`, `CREATED`) VALUES (16, 'vauth_token_read', CURRENT_TIMESTAMP);
INSERT INTO `vauth_role` (`ROLE_ID`, `EXTERNAL_ID`, `CREATED`) VALUES (17, 'vauth_token_write', CURRENT_TIMESTAMP);
INSERT INTO `vauth_role` (`ROLE_ID`, `EXTERNAL_ID`, `CREATED`) VALUES (18, 'vauth_user_read', CURRENT_TIMESTAMP);
INSERT INTO `vauth_role` (`ROLE_ID`, `EXTERNAL_ID`, `CREATED`) VALUES (19, 'vauth_user_write', CURRENT_TIMESTAMP);
INSERT INTO `vauth_role` (`ROLE_ID`, `EXTERNAL_ID`, `CREATED`) VALUES (20, 'vauth_resetcode_read', CURRENT_TIMESTAMP);
INSERT INTO `vauth_role` (`ROLE_ID`, `EXTERNAL_ID`, `CREATED`) VALUES (21, 'vauth_resetcode_write', CURRENT_TIMESTAMP);
INSERT INTO `vauth_role` (`ROLE_ID`, `EXTERNAL_ID`, `CREATED`) VALUES (22, 'vauth_activatecode_read', CURRENT_TIMESTAMP);
INSERT INTO `vauth_role` (`ROLE_ID`, `EXTERNAL_ID`, `CREATED`) VALUES (23, 'vauth_activatecode_write', CURRENT_TIMESTAMP);
INSERT INTO `vauth_role` (`ROLE_ID`, `EXTERNAL_ID`, `CREATED`) VALUES (24, 'vauth_nfccode_read', CURRENT_TIMESTAMP);
INSERT INTO `vauth_role` (`ROLE_ID`, `EXTERNAL_ID`, `CREATED`) VALUES (25, 'vauth_nfccode_write', CURRENT_TIMESTAMP);
INSERT INTO `vauth_role` (`ROLE_ID`, `EXTERNAL_ID`, `CREATED`) VALUES (26, 'vauth_nfc_authorization', CURRENT_TIMESTAMP);

COMMIT;

-- -----------------------------------------------------
-- Data for table `vauth_user2role`
-- -----------------------------------------------------
START TRANSACTION;
USE `vauth-db`;
INSERT INTO `vauth_user2role` (`ID`, `USER_ID`, `ROLE_ID`, `CREATED`) VALUES (1, 1, 1, CURRENT_TIMESTAMP);
INSERT INTO `vauth_user2role` (`ID`, `USER_ID`, `ROLE_ID`, `CREATED`) VALUES (2, 2, 1, CURRENT_TIMESTAMP);

COMMIT;







-- -----------------------------------------------------
-- triggers
-- -----------------------------------------------------

DELIMITER $$

USE `vauth-db`$$
DROP TRIGGER IF EXISTS `vauth_application_BEFORE_INSERT` $$
USE `vauth-db`$$
CREATE DEFINER = CURRENT_USER TRIGGER `vauth-db`.`vauth_application_BEFORE_INSERT` BEFORE INSERT ON `vauth_application` FOR EACH ROW
BEGIN
	SET NEW.CLIENT_ID = MD5(UUID());
	SET NEW.CLIENT_SECRET = MD5(UUID());
END$$


USE `vauth-db`$$
DROP TRIGGER IF EXISTS `vauth_application_BEFORE_UPDATE` $$
USE `vauth-db`$$
CREATE DEFINER = CURRENT_USER TRIGGER `vauth-db`.`vauth_application_BEFORE_UPDATE` BEFORE UPDATE ON `vauth_application` FOR EACH ROW
BEGIN
  IF ( OLD.APPLICATION_ID >= 1 AND OLD.APPLICATION_ID <= 2 ) THEN CALL default_values_error;
  END IF;
END$$


USE `vauth-db`$$
DROP TRIGGER IF EXISTS `vauth_application_BEFORE_DELETE` $$
USE `vauth-db`$$
CREATE DEFINER = CURRENT_USER TRIGGER `vauth-db`.`vauth_application_BEFORE_DELETE` BEFORE DELETE ON `vauth_application` FOR EACH ROW
BEGIN
  IF ( OLD.APPLICATION_ID >= 1 AND OLD.APPLICATION_ID <= 2 ) THEN CALL default_values_error;
  END IF;
END$$


USE `vauth-db`$$
DROP TRIGGER IF EXISTS `vauth_user_BEFORE_UPDATE` $$
USE `vauth-db`$$
CREATE DEFINER = CURRENT_USER TRIGGER `vauth-db`.`vauth_user_BEFORE_UPDATE` BEFORE UPDATE ON `vauth_user` FOR EACH ROW
BEGIN
  IF ( OLD.USER_ID >= 1 AND OLD.USER_ID <= 1 ) THEN
  	SET NEW.EXTERNAL_ID = 'admin';
  	SET NEW.ENABLED = true;
        SET NEW.PASSWORD = OLD.PASSWORD;
  END IF;
END$$


USE `vauth-db`$$
DROP TRIGGER IF EXISTS `vauth_user_BEFORE_DELETE` $$
USE `vauth-db`$$
CREATE DEFINER = CURRENT_USER TRIGGER `vauth-db`.`vauth_user_BEFORE_DELETE` BEFORE DELETE ON `vauth_user` FOR EACH ROW
BEGIN
  IF ( OLD.USER_ID >= 1 AND OLD.USER_ID <= 2 ) THEN CALL default_values_error;
  END IF;
END$$

USE `vauth-db`$$
DROP TRIGGER IF EXISTS `vauth_actcode_BEFORE_INSERT` $$
USE `vauth-db`$$
CREATE DEFINER = CURRENT_USER TRIGGER `vauth-db`.`vauth_actcode_BEFORE_INSERT` BEFORE INSERT ON `vauth_actcode` FOR EACH ROW
BEGIN
	SET NEW.ACTCODE = MD5(UUID());
END$$


USE `vauth-db`$$
DROP TRIGGER IF EXISTS `vauth_actcode_BEFORE_UPDATE` $$
USE `vauth-db`$$
CREATE DEFINER = CURRENT_USER TRIGGER `vauth-db`.`vauth_actcode_BEFORE_UPDATE` BEFORE UPDATE ON `vauth_actcode` FOR EACH ROW
BEGIN
  SET NEW.ACTCODE = MD5(UUID());
END$$

USE `vauth-db`$$
DROP TRIGGER IF EXISTS `vauth_nfccode_BEFORE_INSERT` $$
USE `vauth-db`$$
CREATE DEFINER = CURRENT_USER TRIGGER `vauth-db`.`vauth_nfccode_BEFORE_INSERT` BEFORE INSERT ON `vauth_nfccode` FOR EACH ROW
BEGIN
	SET NEW.NFCCODE = MD5(UUID());
END$$


USE `vauth-db`$$
DROP TRIGGER IF EXISTS `vauth_nfccode_BEFORE_UPDATE` $$
USE `vauth-db`$$
CREATE DEFINER = CURRENT_USER TRIGGER `vauth-db`.`vauth_nfccode_BEFORE_UPDATE` BEFORE UPDATE ON `vauth_nfccode` FOR EACH ROW
BEGIN
  SET NEW.NFCCODE = MD5(UUID());
END$$

USE `vauth-db`$$
DROP TRIGGER IF EXISTS `vauth_resetcode_BEFORE_INSERT` $$
USE `vauth-db`$$
CREATE DEFINER = CURRENT_USER TRIGGER `vauth-db`.`vauth_resetcode_BEFORE_INSERT` BEFORE INSERT ON `vauth_resetcode` FOR EACH ROW
BEGIN
	SET NEW.RESETCODE = MD5(UUID());
END$$


USE `vauth-db`$$
DROP TRIGGER IF EXISTS `vauth_resetcode_BEFORE_UPDATE` $$
USE `vauth-db`$$
CREATE DEFINER = CURRENT_USER TRIGGER `vauth-db`.`vauth_resetcode_BEFORE_UPDATE` BEFORE UPDATE ON `vauth_resetcode` FOR EACH ROW
BEGIN
  SET NEW.RESETCODE = MD5(UUID());
END$$

USE `vauth-db`$$
DROP TRIGGER IF EXISTS `vauth_activatecode_BEFORE_INSERT` $$
USE `vauth-db`$$
CREATE DEFINER = CURRENT_USER TRIGGER `vauth-db`.`vauth_activatecode_BEFORE_INSERT` BEFORE INSERT ON `vauth_activatecode` FOR EACH ROW
BEGIN
	SET NEW.ACTIVATECODE = MD5(UUID());
END$$


USE `vauth-db`$$
DROP TRIGGER IF EXISTS `vauth_activatecode_BEFORE_UPDATE` $$
USE `vauth-db`$$
CREATE DEFINER = CURRENT_USER TRIGGER `vauth-db`.`vauth_activatecode_BEFORE_UPDATE` BEFORE UPDATE ON `vauth_activatecode` FOR EACH ROW
BEGIN
  SET NEW.ACTIVATECODE = MD5(UUID());
END$$

USE `vauth-db`$$
DROP TRIGGER IF EXISTS `vauth_authcode_BEFORE_INSERT` $$
USE `vauth-db`$$
CREATE DEFINER = CURRENT_USER TRIGGER `vauth-db`.`vauth_authcode_BEFORE_INSERT` BEFORE INSERT ON `vauth_authcode` FOR EACH ROW
BEGIN
	SET NEW.AUTHCODE = MD5(UUID());
END$$


USE `vauth-db`$$
DROP TRIGGER IF EXISTS `vauth_authcode_BEFORE_UPDATE` $$
USE `vauth-db`$$
CREATE DEFINER = CURRENT_USER TRIGGER `vauth-db`.`vauth_authcode_BEFORE_UPDATE` BEFORE UPDATE ON `vauth_authcode` FOR EACH ROW
BEGIN
	SET NEW.AUTHCODE = MD5(UUID());
END$$

USE `vauth-db`$$
DROP TRIGGER IF EXISTS `vauth_token_BEFORE_INSERT` $$
USE `vauth-db`$$
CREATE DEFINER = CURRENT_USER TRIGGER `vauth-db`.`vauth_token_BEFORE_INSERT` BEFORE INSERT ON `vauth_token` FOR EACH ROW
BEGIN
	SET NEW.TOKEN = MD5(UUID());
	SET NEW.EXPIRATION_TOKEN = MD5(UUID());
END$$

USE `vauth-db`$$
DROP TRIGGER IF EXISTS `vauth_token_BEFORE_UPDATE` $$
USE `vauth-db`$$
CREATE DEFINER = CURRENT_USER TRIGGER `vauth-db`.`vauth_token_BEFORE_UPDATE` BEFORE UPDATE ON `vauth_token` FOR EACH ROW
BEGIN
	SET NEW.TOKEN = MD5(UUID());
	SET NEW.EXPIRATION_TOKEN = MD5(UUID());
END$$

USE `vauth-db`$$
DROP TRIGGER IF EXISTS `vauth_property_BEFORE_UPDATE` $$
USE `vauth-db`$$
CREATE DEFINER = CURRENT_USER TRIGGER `vauth-db`.`vauth_property_BEFORE_UPDATE` BEFORE UPDATE ON `vauth_property` FOR EACH ROW
BEGIN
  IF ( OLD.PROPERTY_ID >= 1 AND OLD.PROPERTY_ID <= 47 ) THEN CALL default_values_error;
  END IF;
END$$


USE `vauth-db`$$
DROP TRIGGER IF EXISTS `vauth_property_BEFORE_DELETE` $$
USE `vauth-db`$$
CREATE DEFINER = CURRENT_USER TRIGGER `vauth-db`.`vauth_property_BEFORE_DELETE` BEFORE DELETE ON `vauth_property` FOR EACH ROW
BEGIN
  IF ( OLD.PROPERTY_ID >= 1 AND OLD.PROPERTY_ID <= 47 ) THEN CALL default_values_error;
  END IF;
END$$

USE `vauth-db`$$
DROP TRIGGER IF EXISTS `vauth_role_BEFORE_UPDATE` $$
USE `vauth-db`$$
CREATE DEFINER = CURRENT_USER TRIGGER `vauth-db`.`vauth_role_BEFORE_UPDATE` BEFORE UPDATE ON `vauth_role` FOR EACH ROW
BEGIN
  IF ( OLD.ROLE_ID >= 1 AND OLD.ROLE_ID <= 26 ) THEN CALL default_values_error;
  END IF;
END$$


USE `vauth-db`$$
DROP TRIGGER IF EXISTS `vauth_role_BEFORE_DELETE` $$
USE `vauth-db`$$
CREATE DEFINER = CURRENT_USER TRIGGER `vauth-db`.`vauth_role_BEFORE_DELETE` BEFORE DELETE ON `vauth_role` FOR EACH ROW
BEGIN
  IF ( OLD.ROLE_ID >= 1 AND OLD.ROLE_ID <= 26 ) THEN CALL default_values_error;
  END IF;
END$$

USE `vauth-db`$$
DROP TRIGGER IF EXISTS `vauth_user2role_BEFORE_UPDATE` $$
USE `vauth-db`$$
CREATE DEFINER = CURRENT_USER TRIGGER `vauth-db`.`vauth_user2role_BEFORE_UPDATE` BEFORE UPDATE ON `vauth_user2role` FOR EACH ROW
BEGIN
  IF ( OLD.ID >= 1 AND OLD.ID <= 1 ) THEN CALL default_values_error;
  END IF;
END$$


USE `vauth-db`$$
DROP TRIGGER IF EXISTS `vauth_user2role_BEFORE_DELETE` $$
USE `vauth-db`$$
CREATE DEFINER = CURRENT_USER TRIGGER `vauth-db`.`vauth_user2role_BEFORE_DELETE` BEFORE DELETE ON `vauth_user2role` FOR EACH ROW
BEGIN
  IF ( OLD.ID >= 1 AND OLD.ID <= 1 ) THEN CALL default_values_error;
  END IF;
END$$



DELIMITER ;
