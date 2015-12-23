-- phpMyAdmin SQL Dump
-- version 4.0.10deb1
-- http://www.phpmyadmin.net
--
-- Client: localhost
-- Généré le: Lun 19 Octobre 2015 à 15:13
-- Version du serveur: 5.5.44-0ubuntu0.14.04.1
-- Version de PHP: 5.5.9-1ubuntu4.13

DROP DATABASE IF EXISTS `INF-5201C`;
CREATE DATABASE `INF-5201C`;
USE `INF-5201C`;

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+01:00";

CREATE TABLE IF NOT EXISTS `provider` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `phone` varchar(10) NOT NULL,
  `barcode` varchar(45) NOT NULL,
  `adress` varchar(100) NOT NULL,
  `postalCode` varchar(10) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

CREATE TABLE IF NOT EXISTS `article` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `stock` int(10) NOT NULL,
  `barcode` varchar(45) NOT NULL,
  `seuil` int(10) NOT NULL,
  `price` decimal(6,2) NOT NULL,
  `type` varchar(45) NOT NULL,
  `provider_id` int(10) unsigned NOT NULL,
  FOREIGN KEY (`provider_id`) REFERENCES `provider`(`id`),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

CREATE TABLE IF NOT EXISTS `client` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `firstname` varchar(45) NOT NULL,
  `lastname` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

CREATE TABLE IF NOT EXISTS `employee` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `firstname` varchar(45) NOT NULL,
  `lastname` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

CREATE TABLE IF NOT EXISTS `retailer` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `employee_id` int(10) unsigned NOT NULL,
  FOREIGN KEY(`employee_id`) REFERENCES `employee`(`id`),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

CREATE TABLE IF NOT EXISTS `cashier` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `employee_id` int(10) unsigned NOT NULL,
  FOREIGN KEY(`employee_id`) REFERENCES `employee`(`id`),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

CREATE TABLE IF NOT EXISTS `session` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `pseudo` varchar(45) NOT NULL,
  `password` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

CREATE TABLE IF NOT EXISTS `key` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `cashRegister_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

CREATE TABLE IF NOT EXISTS `cashRegister` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `session_id` int(10) unsigned,
  FOREIGN KEY (`session_id`) REFERENCES `session`(`id`),
  `key_id` int(10) unsigned,
  FOREIGN KEY (`key_id`) REFERENCES `key`(`id`),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

ALTER TABLE `key`
 add constraint chashRegisterfk FOREIGN KEY (`cashRegister_id`) REFERENCES `cashRegister`(`id`);

CREATE TABLE IF NOT EXISTS `payment` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `type` varchar(10) NOT NULL,
  `value` decimal(6,2) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

CREATE TABLE IF NOT EXISTS `command` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `provider_id` int(10) unsigned NOT NULL,
  FOREIGN KEY (`provider_id`) REFERENCES `provider`(`id`),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

CREATE TABLE IF NOT EXISTS `commandedArticles` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `command_id` int(10) unsigned NOT NULL,
  `article_id` int(10) unsigned NOT NULL,
  `quantity` int(10) unsigned NOT NULL,
  FOREIGN KEY (`command_id`) REFERENCES `command`(`id`),
  FOREIGN KEY (`article_id`) REFERENCES `article`(`id`),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

CREATE TABLE IF NOT EXISTS `retailerCommands` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `retailer_id` int(10) unsigned NOT NULL,
  `command_id` int(10) unsigned NOT NULL,
  FOREIGN KEY (`retailer_id`) REFERENCES `retailer`(`id`),
  FOREIGN KEY (`command_id`) REFERENCES `command`(`id`),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

CREATE TABLE IF NOT EXISTS `warehouseArticles` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `quantity` int(10) unsigned NOT NULL,
  `article_id` int(10) unsigned NOT NULL,
  FOREIGN KEY (`article_id`) REFERENCES `article`(`id`),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

CREATE TABLE IF NOT EXISTS `aisleArticles` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `quantity` int(10) unsigned NOT NULL,
  `article_id` int(10) unsigned NOT NULL,
  FOREIGN KEY (`article_id`) REFERENCES `article`(`id`),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

CREATE TABLE IF NOT EXISTS `transaction` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `payment_id` int(10) unsigned NOT NULL,
  FOREIGN KEY (`payment_id`) REFERENCES `payment`(`id`),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

CREATE TABLE IF NOT EXISTS `transactionArticles` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `quantity` int(10) unsigned NOT NULL,
  `article_id` int(10) unsigned NOT NULL,
  `transaction_id` int(10) unsigned NOT NULL,
  FOREIGN KEY (`article_id`) REFERENCES `article`(`id`),
  FOREIGN KEY (`transaction_id`) REFERENCES `transaction`(`id`),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;