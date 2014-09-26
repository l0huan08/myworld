SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

DROP SCHEMA IF EXISTS `bank` ;
CREATE SCHEMA IF NOT EXISTS `bank` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
USE `bank` ;

-- -----------------------------------------------------
-- Table `bank`.`tbClient`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `bank`.`tbClient` ;

CREATE  TABLE IF NOT EXISTS `bank`.`tbClient` (
  `cid` INT NOT NULL AUTO_INCREMENT ,
  `fname` VARCHAR(45) NOT NULL ,
  `mname` VARCHAR(45) NULL ,
  `lname` VARCHAR(45) NOT NULL ,
  `gender` VARCHAR(6) NOT NULL ,
  `birthday` DATE NOT NULL ,
  `tel` VARCHAR(45) NOT NULL ,
  `add1` VARCHAR(45) NOT NULL ,
  `add2` VARCHAR(45) NULL ,
  `zip` VARCHAR(10) NOT NULL ,
  `email` VARCHAR(45) NOT NULL ,
  `username` VARCHAR(45) NOT NULL ,
  `pw` VARCHAR(45) NOT NULL ,
  PRIMARY KEY (`cid`) ,
  UNIQUE INDEX `username_UNIQUE` (`username` ASC) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `bank`.`tbAccountType`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `bank`.`tbAccountType` ;

CREATE  TABLE IF NOT EXISTS `bank`.`tbAccountType` (
  `typeid` INT NOT NULL AUTO_INCREMENT ,
  `typename` VARCHAR(45) NOT NULL ,
  PRIMARY KEY (`typeid`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `bank`.`tbAccount`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `bank`.`tbAccount` ;

CREATE  TABLE IF NOT EXISTS `bank`.`tbAccount` (
  `aid` INT NOT NULL AUTO_INCREMENT ,
  `cid` INT NOT NULL ,
  `typeid` INT NOT NULL ,
  `acnumber` VARCHAR(45) NOT NULL ,
  `balance` DECIMAL(20,2) NOT NULL ,
  `isactive` TINYINT(1) NOT NULL DEFAULT TRUE ,
  PRIMARY KEY (`aid`) ,
  UNIQUE INDEX `acnumber_UNIQUE` (`acnumber` ASC) ,
  INDEX `fk_tbAccount_tbClient_idx` (`cid` ASC) ,
  INDEX `fk_tbAccount_tbAccountType1_idx` (`typeid` ASC) ,
  CONSTRAINT `fk_tbAccount_tbClient`
    FOREIGN KEY (`cid` )
    REFERENCES `bank`.`tbClient` (`cid` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_tbAccount_tbAccountType1`
    FOREIGN KEY (`typeid` )
    REFERENCES `bank`.`tbAccountType` (`typeid` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `bank`.`tbTransactionType`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `bank`.`tbTransactionType` ;

CREATE  TABLE IF NOT EXISTS `bank`.`tbTransactionType` (
  `trtid` INT NOT NULL AUTO_INCREMENT ,
  `trtname` VARCHAR(45) NOT NULL ,
  PRIMARY KEY (`trtid`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `bank`.`tbTransaction`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `bank`.`tbTransaction` ;

CREATE  TABLE IF NOT EXISTS `bank`.`tbTransaction` (
  `trid` INT NOT NULL AUTO_INCREMENT ,
  `aid` INT NOT NULL ,
  `trtime` TIMESTAMP NOT NULL ,
  `trtype` INT NOT NULL ,
  `amount` DECIMAL(20,2) NOT NULL ,
  `description` VARCHAR(255) NULL ,
  PRIMARY KEY (`trid`) ,
  INDEX `fk_tbTransaction_tbAccount1_idx` (`aid` ASC) ,
  INDEX `fk_tbTransaction_tbTransactionType1_idx` (`trtype` ASC) ,
  CONSTRAINT `fk_tbTransaction_tbAccount1`
    FOREIGN KEY (`aid` )
    REFERENCES `bank`.`tbAccount` (`aid` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_tbTransaction_tbTransactionType1`
    FOREIGN KEY (`trtype` )
    REFERENCES `bank`.`tbTransactionType` (`trtid` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `bank`.`tbAdmin`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `bank`.`tbAdmin` ;

CREATE  TABLE IF NOT EXISTS `bank`.`tbAdmin` (
  `adminid` INT NOT NULL AUTO_INCREMENT ,
  `username` VARCHAR(45) NOT NULL ,
  `pw` VARCHAR(45) NOT NULL ,
  PRIMARY KEY (`adminid`) ,
  UNIQUE INDEX `username_UNIQUE` (`username` ASC) )
ENGINE = InnoDB;

USE `bank` ;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
