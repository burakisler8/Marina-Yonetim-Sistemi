-- phpMyAdmin SQL Dump
-- version 5.1.0
-- https://www.phpmyadmin.net/
--
-- Anamakine: 127.0.0.1
-- Üretim Zamanı: 26 Ara 2023, 10:10:44
-- Sunucu sürümü: 10.4.19-MariaDB
-- PHP Sürümü: 8.0.6

CREATE DATABASE IF NOT EXISTS `yatlar` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `yatlar`;
SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;



CREATE TABLE `kullanicilar` (
  `kullaniciadi` varchar(50) NOT NULL,
  `sifre` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



INSERT INTO `kullanicilar` (`kullaniciadi`, `sifre`) VALUES
('admin', 'admin123');


CREATE TABLE `yatlar` (
  `ruhsatnum` varchar(20) NOT NULL,
  `uzunluk` varchar(10) NOT NULL,
  `ad` varchar(150) NOT NULL,
  `telefon` varchar(30) NOT NULL,
  `girisSaati` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



INSERT INTO `yatlar` (`ruhsatnum`, `uzunluk`, `ad`, `telefon`, `girisSaati`) VALUES
('01FFT234', '19.', 'Aleyna Çelik', '+905436780912', '2023-12-09'),
('34ZAP944', '24.', 'Ahmet Akarca', '+905455673210', '2023-03-05'),
('35JKL012', '36.', 'Süleyman Demir', '+905444146953', '2023-04-08'),
('545', '5454', 'dsfsdfs', '54541', '2023-12-26'),
('GH789', '14.', 'Bill Guorgini', '+01674587628', '2023-04-07'),
('MNO345', '42', 'John Affrold', '+464718293', '2023-09-03'),
('PR678', '8', 'Ludgozky Malentin', '+901234567895', '2023-10-04'),
('ST901', '14', 'Ludmilla Kratiskov', '+7288790543', '2023-12-24'),
('ZR3788', '9', 'Adina Lorens', '+451234567891', '2023-02-01'),
('ZR4725', '24', 'Igor', '+365848754', '2023-12-26');


ALTER TABLE `kullanicilar`
  ADD PRIMARY KEY (`kullaniciadi`);


ALTER TABLE `yatlar`
  ADD PRIMARY KEY (`ruhsatnum`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
