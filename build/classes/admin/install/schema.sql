-- MySQL dump 10.11
--
-- Host: localhost    Database: andamio
-- ------------------------------------------------------
-- Server version	5.0.51a-24+lenny4

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
-- Current Database: `andamio`
--

USE `andamio`;

--
-- Table structure for table `APPOINTMENT`
--

DROP TABLE IF EXISTS `APPOINTMENT`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `APPOINTMENT` (
  `APPOINTMENT_ID` bigint(20) NOT NULL,
  `SHORTDESCRIPTION` varchar(255) default NULL,
  `LONGDESCRIPTION` text,
  `START` datetime default NULL,
  `END` datetime default NULL,
  `CALENDAR_ID` bigint(20) NOT NULL,
  PRIMARY KEY  (`APPOINTMENT_ID`),
  KEY `FK2868EF7F22B9B82D` (`CALENDAR_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `ATTACHMENT`
--

DROP TABLE IF EXISTS `ATTACHMENT`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `ATTACHMENT` (
  `DOWNLOAD_ID` bigint(20) NOT NULL,
  `NAME` varchar(255) default NULL,
  `DESCRIPTION` text,
  `HITS` int(11) default NULL,
  `FILENAME` varchar(255) default NULL,
  `UPLOADTIME` datetime default NULL,
  `POSTING_ID` bigint(20) default NULL,
  PRIMARY KEY  (`DOWNLOAD_ID`),
  KEY `FKA7E14523FE733C5B` (`POSTING_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `ATTRIBUTE`
--

DROP TABLE IF EXISTS `ATTRIBUTE`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `ATTRIBUTE` (
  `ATTRIBUTE_ID` bigint(20) NOT NULL,
  `_KEY` varchar(255) default NULL,
  `_VALUE` varchar(255) default NULL,
  `DESCRIPTION` varchar(255) default NULL,
  `dataType` varchar(255) NOT NULL,
  `USER_ID` bigint(20) default NULL,
  PRIMARY KEY  (`ATTRIBUTE_ID`),
  KEY `FKA6DFBA7CFC30D2B7` (`USER_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `AVATAR`
--

DROP TABLE IF EXISTS `AVATAR`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `AVATAR` (
  `AVATAR_ID` bigint(20) NOT NULL,
  `DESCRIPTION` varchar(255) default NULL,
  `PATH` varchar(255) default NULL,
  `FILENAME` varchar(255) default NULL,
  `DEFAULTIMAGE` bit(1) default NULL,
  `USER_ID` bigint(20) NOT NULL,
  PRIMARY KEY  (`AVATAR_ID`),
  KEY `FK73C5B559FC30D2B7` (`USER_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `BIBLIO`
--

DROP TABLE IF EXISTS `BIBLIO`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `BIBLIO` (
  `ID` bigint(20) NOT NULL,
  `Identifier` varchar(255) default NULL,
  `BibliographyType` varchar(255) default NULL,
  `Title` varchar(255) default NULL,
  `Address` varchar(255) default NULL,
  `Annote` varchar(255) default NULL,
  `Author` varchar(255) default NULL,
  `Booktitle` varchar(255) default NULL,
  `Chapter` varchar(255) default NULL,
  `Edition` varchar(255) default NULL,
  `Editor` varchar(255) default NULL,
  `Howpublished` varchar(255) default NULL,
  `Institution` varchar(255) default NULL,
  `Journal` varchar(255) default NULL,
  `Month` varchar(255) default NULL,
  `Note` varchar(255) default NULL,
  `Number` int(11) default NULL,
  `Organizations` varchar(255) default NULL,
  `Pages` int(11) default NULL,
  `Publisher` varchar(255) default NULL,
  `School` varchar(255) default NULL,
  `Series` varchar(255) default NULL,
  `ReportType` varchar(255) default NULL,
  `Volume` varchar(255) default NULL,
  `URL` varchar(255) default NULL,
  `Year` varchar(255) default NULL,
  `Custom1` varchar(255) default NULL,
  `Custom2` varchar(255) default NULL,
  `Custom3` varchar(255) default NULL,
  `Custom4` varchar(255) default NULL,
  `Custom5` varchar(255) default NULL,
  `ISBN` varchar(255) default NULL,
  `CREATED` datetime default NULL,
  `LASTMODIFIED` datetime default NULL,
  `LASTMODIFIEDBY` varchar(255) default NULL,
  `SUBMITTEDBY` varchar(255) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `CALENDAR`
--

DROP TABLE IF EXISTS `CALENDAR`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `CALENDAR` (
  `CALENDAR_ID` bigint(20) NOT NULL,
  `NAME` varchar(255) default NULL,
  `DESCRIPTION` text,
  PRIMARY KEY  (`CALENDAR_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `CATEGORY`
--

DROP TABLE IF EXISTS `CATEGORY`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `CATEGORY` (
  `CATEGORY_ID` bigint(20) NOT NULL,
  `NAME` varchar(255) default NULL,
  `POSITION` int(11) default NULL,
  `DESCRIPTION` varchar(255) default NULL,
  `FORUM_ID` bigint(20) NOT NULL,
  PRIMARY KEY  (`CATEGORY_ID`),
  KEY `FK31A8ACFE410E003B` (`FORUM_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `CONFIG`
--

DROP TABLE IF EXISTS `CONFIG`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `CONFIG` (
  `CONFIG_ID` bigint(20) NOT NULL,
  `HOSTNAME` varchar(255) default NULL,
  `LOCALPATH` varchar(255) default NULL,
  `SMTPHOST` varchar(255) default NULL,
  `FROMADRESS` varchar(255) default NULL,
  `USERNAME` varchar(255) default NULL,
  `PASSWORD` varchar(255) default NULL,
  `IMPORTPATH` varchar(255) default NULL,
  `IMAGEPATH` varchar(255) default NULL,
  `DOWNLOADPATH` varchar(255) default NULL,
  `IMAGEURL` varchar(255) default NULL,
  `KEYWORDS` varchar(255) default NULL,
  `BASEPATH` varchar(255) default NULL,
  `GALLERYPATH` varchar(255) default NULL,
  `PRODUCTIMAGEPATH` varchar(255) default NULL,
  `SITENAME` varchar(255) default NULL,
  `TEMPLATE` varchar(255) default NULL,
  `STARTNODE` varchar(255) default NULL,
  `ADMINTEMPLATE` varchar(255) default NULL,
  `CONTENTPATH` varchar(255) default NULL,
  `MAXAVATARHEIGHT` int(11) default NULL,
  `MAXAVATARWIDTH` int(11) default NULL,
  `MAXIMAGEUPLOADSIZE` int(11) default NULL,
  `MAXFILEUPLOADSIZE` int(11) default NULL,
  `MAINTENANCE` bit(1) default NULL,
  PRIMARY KEY  (`CONFIG_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `CONTACT`
--

DROP TABLE IF EXISTS `CONTACT`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `CONTACT` (
  `CONTACT_ID` bigint(20) NOT NULL,
  `FIRSTNAME` varchar(255) default NULL,
  `LASTNAME` varchar(255) default NULL,
  `TITLE` varchar(255) default NULL,
  `COMPANY` varchar(255) default NULL,
  `PHONE` varchar(255) default NULL,
  `EMAIL` varchar(255) default NULL,
  `WEBSITE` varchar(255) default NULL,
  `ADRESS` varchar(255) default NULL,
  `CITY` varchar(255) default NULL,
  `SALUTATION` varchar(255) default NULL,
  `CREATED` datetime default NULL,
  `LASTCHANGE` datetime default NULL,
  `DATEOFBIRTH` datetime default NULL,
  `NOTE` text,
  PRIMARY KEY  (`CONTACT_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `CONTENT`
--

DROP TABLE IF EXISTS `CONTENT`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `CONTENT` (
  `CONTENT_ID` bigint(20) NOT NULL,
  `DESCRIPTION` varchar(255) default NULL,
  `CONTENTNAME` varchar(255) default NULL,
  `PAGENAME` varchar(255) default NULL,
  `METATAGS` longtext,
  `CONTENTSTRING` longtext,
  `LASTMODIFIED` datetime default NULL,
  `LASTMODIFIEDBY` varchar(255) default NULL,
  PRIMARY KEY  (`CONTENT_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `COUNTRY`
--

DROP TABLE IF EXISTS `COUNTRY`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `COUNTRY` (
  `COUNTRY_ID` bigint(20) NOT NULL,
  `NAME` varchar(255) default NULL,
  `CODE` varchar(255) default NULL,
  PRIMARY KEY  (`COUNTRY_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `DATES`
--

DROP TABLE IF EXISTS `DATES`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `DATES` (
  `DATE_ID` bigint(20) NOT NULL,
  `DATE` datetime default NULL,
  `EVENT_ID` bigint(20) default NULL,
  `PARTICIPANT_ID` bigint(20) default NULL,
  PRIMARY KEY  (`DATE_ID`),
  KEY `FK3DD0E65E75FC127` (`EVENT_ID`),
  KEY `FK3DD0E65FF1C18C7` (`PARTICIPANT_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `DOCTOR`
--

DROP TABLE IF EXISTS `DOCTOR`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `DOCTOR` (
  `DOCTOR_ID` bigint(20) NOT NULL,
  `FIRSTNAME` varchar(255) default NULL,
  `LASTNAME` varchar(255) default NULL,
  `LOGIN` varchar(255) default NULL,
  `TYPE` varchar(255) default NULL,
  `PARTICIPANT` bit(1) default NULL,
  PRIMARY KEY  (`DOCTOR_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `DOWNLOAD`
--

DROP TABLE IF EXISTS `DOWNLOAD`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `DOWNLOAD` (
  `DOWNLOAD_ID` bigint(20) NOT NULL,
  `NAME` varchar(255) default NULL,
  `DESCRIPTION` text,
  `RANKING` float default NULL,
  `HITS` int(11) default NULL,
  `PATH` varchar(255) default NULL,
  `FILENAME` varchar(255) default NULL,
  `UPLOADER` varchar(255) default NULL,
  `UPLOADTIME` datetime default NULL,
  PRIMARY KEY  (`DOWNLOAD_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `EVENT`
--

DROP TABLE IF EXISTS `EVENT`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `EVENT` (
  `EVENT_ID` bigint(20) NOT NULL,
  `TITLE` varchar(255) default NULL,
  `LOCATION` varchar(255) default NULL,
  `DESCRIPTION` varchar(255) default NULL,
  `USERNAME` varchar(255) default NULL,
  `SELECTEDDATE` datetime default NULL,
  PRIMARY KEY  (`EVENT_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `FAQ`
--

DROP TABLE IF EXISTS `FAQ`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `FAQ` (
  `FAQ_ID` bigint(20) NOT NULL,
  `STUDY_ID` varchar(255) default NULL,
  `QUESTION` text,
  `ANSWER` longtext,
  PRIMARY KEY  (`FAQ_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `FORUM`
--

DROP TABLE IF EXISTS `FORUM`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `FORUM` (
  `FORUM_ID` bigint(20) NOT NULL,
  `NAME` varchar(255) default NULL,
  `POSITION` int(11) default NULL,
  `DESCRIPTION` varchar(255) default NULL,
  `STATUS` int(11) default NULL,
  PRIMARY KEY  (`FORUM_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `GALLERY`
--

DROP TABLE IF EXISTS `GALLERY`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `GALLERY` (
  `GALLERY_ID` bigint(20) NOT NULL,
  `GALLERYNAME` varchar(255) default NULL,
  `DESCRIPTION` varchar(255) default NULL,
  `THUMBHEIGHT` int(11) default NULL,
  `THUMBWIDTH` int(11) default NULL,
  `NUMCOLUMNS` int(11) default NULL,
  `TEMPLATE` varchar(255) default NULL,
  `GALLERYTEXT` text,
  `RANKING` float default NULL,
  `USER_ID` bigint(20) default NULL,
  PRIMARY KEY  (`GALLERY_ID`),
  KEY `FK1F180332FC30D2B7` (`USER_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `IMAGE`
--

DROP TABLE IF EXISTS `IMAGE`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `IMAGE` (
  `GIMAGE_ID` bigint(20) NOT NULL,
  `DESCRIPTION` text,
  `RANKING` float default NULL,
  `PATH` varchar(255) default NULL,
  `NAME` varchar(255) default NULL,
  `FILENAME` varchar(255) default NULL,
  `LASTMODIFIED` bigint(20) default NULL,
  `GALLERY_ID` bigint(20) NOT NULL,
  PRIMARY KEY  (`GIMAGE_ID`),
  KEY `FK428B13B8E61D67D` (`GALLERY_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `INSURANCE`
--

DROP TABLE IF EXISTS `INSURANCE`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `INSURANCE` (
  `INSURANCE_ID` bigint(20) NOT NULL,
  `NAME` varchar(255) default NULL,
  PRIMARY KEY  (`INSURANCE_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `LOGENTRY`
--

DROP TABLE IF EXISTS `LOGENTRY`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `LOGENTRY` (
  `ENTRY_ID` bigint(20) NOT NULL,
  `USERNAME` varchar(255) default NULL,
  `ENTRYDATE` datetime default NULL,
  `ACTION` varchar(255) default NULL,
  PRIMARY KEY  (`ENTRY_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `MAIL_ACCOUNT`
--

DROP TABLE IF EXISTS `MAIL_ACCOUNT`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `MAIL_ACCOUNT` (
  `ACCOUNT_ID` bigint(20) NOT NULL,
  `NAME` varchar(255) default NULL,
  `TYPE` varchar(255) NOT NULL,
  `RECVHOST` varchar(255) default NULL,
  `SENDHOST` varchar(255) default NULL,
  `REMOTEUSER` varchar(255) default NULL,
  `REMOTEPASS` varchar(255) default NULL,
  `USER_ID` bigint(20) default NULL,
  PRIMARY KEY  (`ACCOUNT_ID`),
  KEY `FK3CF11385FC30D2B7` (`USER_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `MAIL_FOLDER`
--

DROP TABLE IF EXISTS `MAIL_FOLDER`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `MAIL_FOLDER` (
  `id` bigint(20) NOT NULL auto_increment,
  `NAME` varchar(255) default NULL,
  `FOLDER_ID` bigint(20) default NULL,
  `ACCOUNT_ID` bigint(20) default NULL,
  PRIMARY KEY  (`id`),
  KEY `FKB2C83F6367721C5` (`FOLDER_ID`),
  KEY `FKB2C83F67C6E512F` (`ACCOUNT_ID`)
) ENGINE=MyISAM AUTO_INCREMENT=25 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `MAIL_MESSAGE`
--

DROP TABLE IF EXISTS `MAIL_MESSAGE`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `MAIL_MESSAGE` (
  `MESSAGE_ID` bigint(20) NOT NULL,
  `SUBJECT` varchar(255) default NULL,
  `BODY` text,
  `SENDER` text,
  `RECEIVED` datetime default NULL,
  `SENT` datetime default NULL,
  `REPLYTO` bigint(20) default NULL,
  `STATUS` varchar(255) NOT NULL,
  `FOLDER_ID` bigint(20) default NULL,
  PRIMARY KEY  (`MESSAGE_ID`),
  KEY `FKBC08895F367721C5` (`FOLDER_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `MEETING`
--

DROP TABLE IF EXISTS `MEETING`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `MEETING` (
  `MEETING_ID` bigint(20) NOT NULL,
  `SHORTDESCRIPTION` varchar(255) default NULL,
  `LONGDESCRIPTION` text,
  `START` datetime default NULL,
  `END` datetime default NULL,
  PRIMARY KEY  (`MEETING_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `NODE`
--

DROP TABLE IF EXISTS `NODE`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `NODE` (
  `id` bigint(20) NOT NULL auto_increment,
  `NAME` varchar(255) default NULL,
  `DISPLAYNAME` varchar(255) default NULL,
  `NODEPOSITION` int(11) default NULL,
  `TYPE` int(11) default NULL,
  `IMAGE` varchar(255) default NULL,
  `PLUGINID` varchar(255) default NULL,
  `PLUGINPARAMS` text,
  `LINKURL` varchar(255) default NULL,
  `FIRSTCHILD` bit(1) default NULL,
  `EXPORTABLE` bit(1) default NULL,
  `NODE_ID` bigint(20) default NULL,
  `INHALT_ID` bigint(20) default NULL,
  `REL` varchar(255) default NULL,
  `TARGETNAME` varchar(255) default NULL,
  `TARGETTYPE` varchar(255) default NULL,
  PRIMARY KEY  (`id`),
  KEY `FK24A60282FA9357` (`NODE_ID`),
  KEY `FK24A6022062BF90` (`INHALT_ID`)
) ENGINE=MyISAM AUTO_INCREMENT=61 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `PARTICIPANT`
--

DROP TABLE IF EXISTS `PARTICIPANT`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `PARTICIPANT` (
  `PARTICIPANT_ID` bigint(20) NOT NULL,
  `firstname` varchar(255) default NULL,
  `lastname` varchar(255) default NULL,
  `EVENT_ID` bigint(20) default NULL,
  PRIMARY KEY  (`PARTICIPANT_ID`),
  KEY `FKAE118313E75FC127` (`EVENT_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `PARTICIPANT_ID`
--

DROP TABLE IF EXISTS `PARTICIPANT_ID`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `PARTICIPANT_ID` (
  `PARTICIPANT_ID` bigint(20) NOT NULL,
  `firstname` varchar(255) default NULL,
  `lastname` varchar(255) default NULL,
  `EVENT_ID` bigint(20) default NULL,
  PRIMARY KEY  (`PARTICIPANT_ID`),
  KEY `FK87E5ADC7E75FC127` (`EVENT_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `PATIENT`
--

DROP TABLE IF EXISTS `PATIENT`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `PATIENT` (
  `PATIENT_ID` bigint(20) NOT NULL,
  `BIRTHDATE` datetime default NULL,
  `RANDOMNUMBER` varchar(255) default NULL,
  `TREATMENTSTART` datetime default NULL,
  `TREATMENTEND` bit(1) default NULL,
  `LASTDOCUMENTATION` datetime default NULL,
  `CSIPARTICIPANT` bit(1) default NULL,
  `THERAPYCONFORM` bit(1) default NULL,
  `INSURANCE_ID` bigint(20) default NULL,
  `STARTDOSE` int(11) default NULL,
  `WEIGHT` float default NULL,
  `CURRENTDOSE` int(11) default NULL,
  `PROCESSED` bit(1) default NULL,
  `CENTER` varchar(255) default NULL,
  `REGISTERDATE` datetime default NULL,
  `PAID` bit(1) default NULL,
  `PAIDAMOUNT` float default NULL,
  `DOCTOR_ID` bigint(20) NOT NULL,
  PRIMARY KEY  (`PATIENT_ID`),
  KEY `FKFB9F76E512AD1842` (`DOCTOR_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `POSTING`
--

DROP TABLE IF EXISTS `POSTING`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `POSTING` (
  `POSTING_ID` bigint(20) NOT NULL,
  `TITLE` varchar(255) default NULL,
  `POSTINGTEXT` text,
  `POSTING_DATE` datetime default NULL,
  `POSTING_LASTCHANGE` datetime default NULL,
  `POSTING_LASTCHANGEBY` varchar(255) default NULL,
  `THREAD_ID` bigint(20) NOT NULL,
  `USER_ID` bigint(20) NOT NULL,
  PRIMARY KEY  (`POSTING_ID`),
  KEY `FK137A472220D45379` (`THREAD_ID`),
  KEY `FK137A4722FC30D2B7` (`USER_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `PROCESS`
--

DROP TABLE IF EXISTS `PROCESS`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `PROCESS` (
  `PROCESS_ID` bigint(20) NOT NULL,
  `NAME` varchar(255) default NULL,
  `TARGET` varchar(255) default NULL,
  `ICON` varchar(255) default NULL,
  `ACTIVE` bit(1) default NULL,
  `PROCPOSITION` int(11) default NULL,
  PRIMARY KEY  (`PROCESS_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `REVISION`
--

DROP TABLE IF EXISTS `REVISION`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `REVISION` (
  `REVISION_ID` bigint(20) NOT NULL,
  `CONTENTTEXT` text,
  `LASTMODIFIED` datetime default NULL,
  `AUTHOR` varchar(255) default NULL,
  `PUBLISHED` bit(1) default NULL,
  `CONTENT_ID` bigint(20) NOT NULL,
  PRIMARY KEY  (`REVISION_ID`),
  KEY `FK1F1AA7DB941E711D` (`CONTENT_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `SITEREQUEST`
--

DROP TABLE IF EXISTS `SITEREQUEST`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `SITEREQUEST` (
  `REQUEST_ID` bigint(20) NOT NULL,
  `USERNAME` varchar(255) default NULL,
  `CONTENTNAME` varchar(255) default NULL,
  `REQTIME` varchar(255) default NULL,
  `USERIP` varchar(255) default NULL,
  `USERAGENT` varchar(255) default NULL,
  `DOMAIN` varchar(255) default NULL,
  PRIMARY KEY  (`REQUEST_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `SYSUSER`
--

DROP TABLE IF EXISTS `SYSUSER`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `SYSUSER` (
  `USER_ID` bigint(20) NOT NULL,
  `NAME` varchar(255) default NULL,
  `FIRSTNAME` varchar(255) default NULL,
  `LASTNAME` varchar(255) default NULL,
  `TITLE` varchar(255) default NULL,
  `LAND` varchar(255) default NULL,
  `ORT` varchar(255) default NULL,
  `TELEFON` varchar(255) default NULL,
  `EMAIL` varchar(255) default NULL,
  `RANKING` int(11) default NULL,
  `PASSWORD` varchar(255) default NULL,
  `JOINDATE` datetime default NULL,
  `BIRTHDATE` datetime default NULL,
  `LASTACTIVITY` datetime default NULL,
  `ACTIVE` bit(1) default NULL,
  PRIMARY KEY  (`USER_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `TEMPLATE`
--

DROP TABLE IF EXISTS `TEMPLATE`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `TEMPLATE` (
  `TEMPLATE_ID` bigint(20) NOT NULL,
  `NAME` varchar(255) default NULL,
  `DESCRIPTION` text,
  `GALLERYTYPE` varchar(255) NOT NULL,
  `TABLESTYLE` varchar(255) default NULL,
  `DATASTYLE` varchar(255) default NULL,
  `ROWSTYLE` varchar(255) default NULL,
  `PLACEINDIV` bit(1) default NULL,
  `DIVSTYLE` varchar(255) default NULL,
  `HTML` text,
  `CSS` text,
  PRIMARY KEY  (`TEMPLATE_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `THREAD`
--

DROP TABLE IF EXISTS `THREAD`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `THREAD` (
  `THREAD_ID` bigint(20) NOT NULL,
  `NAME` varchar(255) default NULL,
  `THREAD_DATE` datetime default NULL,
  `LASTPOST` datetime default NULL,
  `CATEGORY_ID` bigint(20) NOT NULL,
  `USER_ID` bigint(20) NOT NULL,
  PRIMARY KEY  (`THREAD_ID`),
  KEY `FK9374020AFC30D2B7` (`USER_ID`),
  KEY `FK9374020AD0CEFB39` (`CATEGORY_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `USERCOMMENT`
--

DROP TABLE IF EXISTS `USERCOMMENT`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `USERCOMMENT` (
  `COMMENT_ID` bigint(20) NOT NULL,
  `TEXT` text,
  `AUTHOR` varchar(255) default NULL,
  `CREATED` datetime default NULL,
  `GIMAGE_ID` bigint(20) default NULL,
  `USER_ID` bigint(20) default NULL,
  PRIMARY KEY  (`COMMENT_ID`),
  KEY `FK43C2CC14FC30D2B7` (`USER_ID`),
  KEY `FK43C2CC14D66FC04` (`GIMAGE_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `USERGROUP`
--

DROP TABLE IF EXISTS `USERGROUP`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `USERGROUP` (
  `GROUP_ID` bigint(20) NOT NULL,
  `NAME` varchar(255) default NULL,
  `DESCRIPTION` text,
  PRIMARY KEY  (`GROUP_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `USERPROFILE`
--

DROP TABLE IF EXISTS `USERPROFILE`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `USERPROFILE` (
  `USER_ID` bigint(20) NOT NULL,
  `PAGECONTENT` text,
  PRIMARY KEY  (`USER_ID`),
  KEY `FKF8AB675EFC30D2B7` (`USER_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `link_calendar_meetings`
--

DROP TABLE IF EXISTS `link_calendar_meetings`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `link_calendar_meetings` (
  `CALENDAR_ID` bigint(20) NOT NULL,
  `MEETING_ID` bigint(20) NOT NULL,
  PRIMARY KEY  (`CALENDAR_ID`,`MEETING_ID`),
  KEY `FK8D28487422B9B82D` (`CALENDAR_ID`),
  KEY `FK8D284874DD8DA47` (`MEETING_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `link_forum_groups`
--

DROP TABLE IF EXISTS `link_forum_groups`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `link_forum_groups` (
  `FORUM_ID` bigint(20) NOT NULL,
  `GROUP_ID` bigint(20) NOT NULL,
  PRIMARY KEY  (`FORUM_ID`,`GROUP_ID`),
  KEY `FKD19272D71FD631B2` (`GROUP_ID`),
  KEY `FKD19272D7410E003B` (`FORUM_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `link_node_groups`
--

DROP TABLE IF EXISTS `link_node_groups`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `link_node_groups` (
  `NODE_ID` bigint(20) NOT NULL,
  `GROUP_ID` bigint(20) NOT NULL,
  PRIMARY KEY  (`NODE_ID`,`GROUP_ID`),
  KEY `FKC1E1A9EC82FA9357` (`NODE_ID`),
  KEY `FKC1E1A9EC1FD631B2` (`GROUP_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `link_process_groups`
--

DROP TABLE IF EXISTS `link_process_groups`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `link_process_groups` (
  `PROCESS_ID` bigint(20) NOT NULL,
  `GROUP_ID` bigint(20) NOT NULL,
  PRIMARY KEY  (`PROCESS_ID`,`GROUP_ID`),
  KEY `FK9C7DF8891FD631B2` (`GROUP_ID`),
  KEY `FK9C7DF8895259A55D` (`PROCESS_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `link_user_calendars`
--

DROP TABLE IF EXISTS `link_user_calendars`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `link_user_calendars` (
  `USER_ID` bigint(20) NOT NULL,
  `CALENDAR_ID` bigint(20) NOT NULL,
  PRIMARY KEY  (`USER_ID`,`CALENDAR_ID`),
  KEY `FKC100DC6622B9B82D` (`CALENDAR_ID`),
  KEY `FKC100DC66FC30D2B7` (`USER_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `link_user_countries`
--

DROP TABLE IF EXISTS `link_user_countries`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `link_user_countries` (
  `COUNTRY_ID` bigint(20) NOT NULL,
  `USER_ID` bigint(20) NOT NULL,
  PRIMARY KEY  (`USER_ID`,`COUNTRY_ID`),
  KEY `FK5B200A254814BFFD` (`COUNTRY_ID`),
  KEY `FK5B200A25FC30D2B7` (`USER_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `link_user_groups`
--

DROP TABLE IF EXISTS `link_user_groups`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `link_user_groups` (
  `USER_ID` bigint(20) NOT NULL,
  `GROUP_ID` bigint(20) NOT NULL,
  PRIMARY KEY  (`USER_ID`,`GROUP_ID`),
  KEY `FK7FFC0803FC30D2B7` (`USER_ID`),
  KEY `FK7FFC08031FD631B2` (`GROUP_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2011-05-06  6:29:28
