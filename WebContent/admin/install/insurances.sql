-- MySQL dump 10.11
--
-- Host: localhost    Database: io5Initials
-- ------------------------------------------------------
-- Server version	5.0.32-Debian_7etch12-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `INSURANCE`
--

DROP TABLE IF EXISTS `INSURANCE`;
CREATE TABLE `INSURANCE` (
  `INSURANCE_ID` bigint(20) NOT NULL,
  `NAME` varchar(255) default NULL,
  PRIMARY KEY  (`INSURANCE_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `INSURANCE`
--

LOCK TABLES `INSURANCE` WRITE;
/*!40000 ALTER TABLE `INSURANCE` DISABLE KEYS */;
INSERT INTO `INSURANCE` VALUES (1,'----------------------'),(2,'AOK Baden-W&uuml;rttemberg'),(3,'AOK Bayern'),(4,'AOK Berlin'),(5,'AOK Brandenburg'),(6,'AOK Bremen/Bremerhaven'),(7,'AOK Hessen'),(8,'AOK Mecklenburg-Vorpommern'),(9,'AOK Niedersachsen'),(10,'AOK Plus Sachsen &amp; Th&uuml;ringen'),(11,'AOK Rheinland/Hamburg'),(12,'AOK Rheinland-Pfalz'),(13,'AOK Saarland'),(14,'AOK Sachsen-Anhalt'),(15,'AOK Schleswig-Holstein'),(16,'AOK Westfalen-Lippe'),(17,'ATLAS BKK'),(18,'Audi BKK'),(19,'Bahn BKK'),(20,'Bank BKK'),(21,'BEK'),(22,'Bertelsmann BKK'),(23,'Berufsgenossenschaft'),(24,'BIG Direkt'),(25,'BKK 11 88 0'),(26,'BKK 24'),(27,'BKK A.T.U. - Die Pers&ouml;nliche Krankenkasse'),(28,'BKK Achenbach Buschh&uuml;tten'),(29,'BKK advita'),(30,'BKK Aesculap'),(31,'BKK Ahlmann'),(32,'BKK Akzo Nobel'),(33,'BKK ALP plus'),(34,'BKK Axel Springer Verlag'),(35,'BKK B. Braun'),(36,'BKK Basell'),(37,'BKK Beiersdorf'),(38,'BKK BJB'),(39,'BKK BMW'),(40,'BKK BPW Wiehl'),(41,'BKK Braun-Gillette'),(42,'BKK Demag Krauss-Maffei'),(43,'BKK Dematic'),(44,'BKK DER PARTNER'),(45,'BKK Deutsche Bank'),(46,'BKK Deutsche BP'),(47,'BKK Diakonie'),(48,'BKK Dr. Oetker'),(49,'BKK D&uuml;rkopp Adler'),(50,'BKK ENKA'),(51,'BKK Ernst &amp; Young'),(52,'BKK Essanelle'),(53,'BKK Euregio'),(54,'BKK EWE'),(55,'BKK exclusiv'),(56,'BKK Faber-Castell &amp; Partner'),(57,'BKK Fahr'),(58,'BKK firmus'),(59,'BKK Ford &amp; Rheinland'),(60,'BKK Freudenberg'),(61,'BKK FTE'),(62,'BKK f&uuml;r Heilberufe'),(63,'BKK futur'),(64,'BKK G &amp; V'),(65,'BKK Gesundheit'),(66,'BKK Gildemeister Seidensticker'),(67,'BKK Goetze &amp; Partner'),(68,'BKK Grillo-Werke'),(69,'BKK Groz-Beckert'),(70,'BKK Heimbach'),(71,'BKK Henschel Plus'),(72,'BKK Herford Minden Ravensberg'),(73,'BKK Herkules'),(74,'BKK Hoesch'),(75,'BKK IHV'),(76,'BKK Karl Mayer'),(77,'BKK Kassana'),(78,'BKK KBA'),(79,'BKK KEVAG'),(80,'BKK Krones'),(81,'BKK Linde'),(82,'BKK Mahle'),(83,'BKK MAN und MTU'),(84,'BKK MEDICUS'),(85,'BKK Melitta plus'),(86,'BKK MEM'),(87,'BKK Merck'),(88,'BKK Miele'),(89,'BKK Mobil Oil'),(90,'BKK MTU Friedrichshafen'),(91,'BKK N-ERGIE'),(92,'BKK Norddeutsche Affinerie'),(93,'BKK Ost-Hessen'),(94,'BKK Pfaff'),(95,'BKK Pfalz'),(96,'BKK Pfeifer &amp; Langen'),(97,'BKK Ph&ouml;nix'),(98,'BKK Publik'),(99,'BKK PWC'),(100,'BKK R+V'),(101,'BKK Rieker.Ricosta.Weisser'),(102,'BKK RWE'),(103,'BKK Salzgitter'),(104,'BKK Scheufelen'),(105,'BKK Schott-Rohrglas'),(106,'BKK Schwarzwald-Baar-Heuberg'),(107,'BKK Schwesternschaft vom BRK'),(108,'BKK S-H'),(109,'BKK SIEMAG'),(110,'BKK Stadt Augsburg'),(111,'BKK Technoform'),(112,'BKK Textilgruppe Hof'),(113,'BKK Th&uuml;ringer Energieversorgung'),(114,'BKK TUI'),(115,'BKK VBU'),(116,'BKK VDN'),(117,'BKK VerbundPlus'),(118,'BKK Victoria D.A.S.'),(119,'BKK VITAL'),(120,'BKK VOR ORT'),(121,'BKK Voralb'),(122,'BKK Werra-Meissner'),(123,'BKK Westfalen-Lippe'),(124,'BKK Wieland-Werke'),(125,'BKK Wirtschaft &amp; Finanzen'),(126,'BKK W&uuml;rth'),(127,'BKK ZF &amp; Partner'),(128,'Bosch BKK'),(129,'Brandenburgische BKK'),(130,'City BKK'),(131,'Daimler BKK'),(132,'DAK'),(133,'Debeka BKK'),(134,'Deutsche BKK'),(135,'Die Bergische KK'),(136,'Die Continentale BKK'),(137,'Dr&auml;ger &amp; Hanse BKK'),(138,'E.ON BKK'),(139,'Energie-BKK'),(140,'Esso BKK'),(141,'GBK K&ouml;ln'),(142,'GEK'),(143,'Hamburg-M&uuml;nchener'),(144,'Handelskrankenkasse Bremen'),(145,'Hanseatische Krankenkasse'),(146,'HEAG BKK'),(147,'HypoVereinsbank BKK'),(148,'IKK Baden-W&uuml;rttemberg und Hessen'),(149,'IKK Brandenburg und Berlin'),(150,'IKK gesund plus'),(151,'IKK Hamburg'),(152,'IKK Niedersachsen'),(153,'IKK Nord'),(154,'IKK Nordrhein'),(155,'IKK Sachsen'),(156,'IKK S&uuml;dwest'),(157,'IKK Th&uuml;ringen'),(158,'Inovita BKK'),(159,'KKH Allianz'),(160,'Knappschaft'),(161,'Krankenkasse Gartenbau'),(162,'ktpBKK'),(163,'LKK Baden-W&uuml;rttemberg'),(164,'LKK Franken und Oberbayern'),(165,'LKK Hessen/Rheinland-Pfalz/Saarland'),(166,'LKK Mittel- und Ostdeutschland'),(167,'LKK Niederbayern/Oberpfalz/Schwaben'),(168,'LKK Niedersachsen-Bremen'),(169,'LKK Nordrhein-Westfalen'),(170,'LKK Schleswig-Holstein/Hamburg'),(171,'mhplus BKK'),(172,'Neckermann BKK'),(173,'neue BKK'),(174,'Novitas BKK - Die Pr&auml;ventionskasse'),(175,'numIKK'),(176,'Postbeamtenkrankenkasse'),(177,'pronova BKK'),(178,'Rest'),(179,'Saint Gobain BKK'),(180,'Salus BKK'),(181,'SBK'),(182,'Schwenninger BKK'),(183,'Securvita BKK'),(184,'Shell BKK/LIFE'),(185,'Signal Iduna IKK'),(186,'SKD BKK'),(187,'Sozial&auml;mter'),(188,'Sprechstundenbedarf'),(189,'S&uuml;dzucker BKK'),(190,'TAUNUS BKK'),(191,'TK'),(192,'Vaillant BKK'),(193,'WMF BKK');
/*!40000 ALTER TABLE `INSURANCE` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2010-03-10  9:01:41
