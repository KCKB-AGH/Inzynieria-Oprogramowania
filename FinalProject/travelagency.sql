-- phpMyAdmin SQL Dump
-- version 4.2.7.1
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Czas generowania: 25 Sty 2015, 14:27
-- Wersja serwera: 5.6.20
-- Wersja PHP: 5.5.15

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Baza danych: `travelagency`
--

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `attraction`
--

CREATE TABLE IF NOT EXISTS `attraction` (
`id` int(10) NOT NULL,
  `name` varchar(100) COLLATE utf8_polish_ci NOT NULL,
  `price` double NOT NULL
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_polish_ci AUTO_INCREMENT=2 ;

--
-- Zrzut danych tabeli `attraction`
--

INSERT INTO `attraction` (`id`, `name`, `price`) VALUES
(1, 'Zwiedzanie Muzeum Figur Woskowych', 120);

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `history`
--

CREATE TABLE IF NOT EXISTS `history` (
`id` int(10) NOT NULL,
  `idPerson` int(10) NOT NULL,
  `idTravel` int(10) NOT NULL,
  `idHotel` int(10) NOT NULL,
  `idTransport` int(10) NOT NULL,
  `idAttraction` int(10) NOT NULL,
  `startDate` date DEFAULT NULL,
  `endDate` date DEFAULT NULL,
  `name` varchar(50) COLLATE utf8_polish_ci NOT NULL,
  `city` varchar(50) COLLATE utf8_polish_ci NOT NULL,
  `price` double NOT NULL
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_polish_ci AUTO_INCREMENT=4 ;

--
-- Zrzut danych tabeli `history`
--

INSERT INTO `history` (`id`, `idPerson`, `idTravel`, `idHotel`, `idTransport`, `idAttraction`, `startDate`, `endDate`, `name`, `city`, `price`) VALUES
(1, 4, 1, 1, 1, 1, '2014-12-02', '2014-12-12', 'Wycieczka', 'Kraków', 500),
(2, 4, 2, 3, 4, 1, NULL, NULL, 'Wycieczka do Krakowa', 'Kraków', 590.1600000000001),
(3, 4, 2, 3, 3, 1, NULL, NULL, 'Wycieczka do Krakowa', 'Kraków', 464.76);

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `hotel`
--

CREATE TABLE IF NOT EXISTS `hotel` (
`id` int(10) NOT NULL,
  `name` varchar(50) COLLATE utf8_polish_ci NOT NULL,
  `city` varchar(50) COLLATE utf8_polish_ci NOT NULL,
  `price` double NOT NULL
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_polish_ci AUTO_INCREMENT=4 ;

--
-- Zrzut danych tabeli `hotel`
--

INSERT INTO `hotel` (`id`, `name`, `city`, `price`) VALUES
(1, 'Hilton', 'Warszawa', 500.55),
(2, 'Sobieski', 'Kraków', 677.93),
(3, 'Polonia', 'Kraków', 375);

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `person`
--

CREATE TABLE IF NOT EXISTS `person` (
`id` int(10) unsigned NOT NULL,
  `login` varchar(50) COLLATE utf8_polish_ci NOT NULL,
  `password` varchar(50) COLLATE utf8_polish_ci NOT NULL,
  `firstName` varchar(50) COLLATE utf8_polish_ci NOT NULL,
  `lastName` varchar(50) COLLATE utf8_polish_ci NOT NULL,
  `email` varchar(50) COLLATE utf8_polish_ci NOT NULL,
  `seller` tinyint(1) NOT NULL
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_polish_ci AUTO_INCREMENT=5 ;

--
-- Zrzut danych tabeli `person`
--

INSERT INTO `person` (`id`, `login`, `password`, `firstName`, `lastName`, `email`, `seller`) VALUES
(4, 'a', 'a', 'Konrad', 'Boguń', 'konrad127@gmail.com', 1);

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `transport`
--

CREATE TABLE IF NOT EXISTS `transport` (
`id` int(10) NOT NULL,
  `name` varchar(50) COLLATE utf8_polish_ci NOT NULL,
  `price` double NOT NULL
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_polish_ci AUTO_INCREMENT=5 ;

--
-- Zrzut danych tabeli `transport`
--

INSERT INTO `transport` (`id`, `name`, `price`) VALUES
(1, 'Autobus', 1.25),
(2, 'Pociąg', 2.53),
(3, 'Samochód', 3.69),
(4, 'Samolot', 15.96);

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `travel`
--

CREATE TABLE IF NOT EXISTS `travel` (
`id` int(10) NOT NULL,
  `name` varchar(50) COLLATE utf8_polish_ci NOT NULL,
  `overview` varchar(250) COLLATE utf8_polish_ci NOT NULL,
  `distance` int(11) NOT NULL,
  `city` varchar(50) COLLATE utf8_polish_ci NOT NULL,
  `price` double NOT NULL
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_polish_ci AUTO_INCREMENT=3 ;

--
-- Zrzut danych tabeli `travel`
--

INSERT INTO `travel` (`id`, `name`, `overview`, `distance`, `city`, `price`) VALUES
(1, 'Last Minute', 'qwerty', 300, 'Warszawa', 1563),
(2, 'Wycieczka do Krakowa', 'aisjdlaksjdaksdkamsd', 20, 'Kraków', 270.96);

--
-- Indeksy dla zrzutów tabel
--

--
-- Indexes for table `attraction`
--
ALTER TABLE `attraction`
 ADD PRIMARY KEY (`id`);

--
-- Indexes for table `history`
--
ALTER TABLE `history`
 ADD PRIMARY KEY (`id`);

--
-- Indexes for table `hotel`
--
ALTER TABLE `hotel`
 ADD PRIMARY KEY (`id`);

--
-- Indexes for table `person`
--
ALTER TABLE `person`
 ADD PRIMARY KEY (`id`);

--
-- Indexes for table `transport`
--
ALTER TABLE `transport`
 ADD PRIMARY KEY (`id`);

--
-- Indexes for table `travel`
--
ALTER TABLE `travel`
 ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT dla tabeli `attraction`
--
ALTER TABLE `attraction`
MODIFY `id` int(10) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=2;
--
-- AUTO_INCREMENT dla tabeli `history`
--
ALTER TABLE `history`
MODIFY `id` int(10) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=4;
--
-- AUTO_INCREMENT dla tabeli `hotel`
--
ALTER TABLE `hotel`
MODIFY `id` int(10) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=4;
--
-- AUTO_INCREMENT dla tabeli `person`
--
ALTER TABLE `person`
MODIFY `id` int(10) unsigned NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=5;
--
-- AUTO_INCREMENT dla tabeli `transport`
--
ALTER TABLE `transport`
MODIFY `id` int(10) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=5;
--
-- AUTO_INCREMENT dla tabeli `travel`
--
ALTER TABLE `travel`
MODIFY `id` int(10) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=3;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
