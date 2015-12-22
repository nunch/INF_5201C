-- phpMyAdmin SQL Dump
-- version 4.0.10deb1
-- http://www.phpmyadmin.net
--
-- Client: localhost
-- Généré le: Lun 19 Octobre 2015 à 15:13
-- Version du serveur: 5.5.44-0ubuntu0.14.04.1
-- Version de PHP: 5.5.9-1ubuntu4.13
drop table if EXISTS `article`;
drop table if EXISTS `provider`;

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+01:00";

CREATE TABLE IF NOT EXISTS `article` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `stock` int(10) NOT NULL,
  `barcode` varchar(45) NOT NULL,
  `stock` int(10) NOT NULL,
  `seuil` int(10) NOT NULL,
  `price` decimal(6,2) NOT NULL,
  `type` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

CREATE TABLE IF NOT EXISTS `provider` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `phone` varchar(10) NOT NULL,
  `barcode` varchar(45) NOT NULL,
  `adress` varchar(100) NOT NULL,
  `postalCode` varchar(10) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

