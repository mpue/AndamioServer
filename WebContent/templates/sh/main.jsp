<?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ taglib uri="/tags/pagetags" prefix="page" %>

<%@ page import="java.*" %>
<html:html xhtml="true">
 <head>
  <title><page:pagetitle/></title>
  <meta http-equiv="X-UA-Compatible" content="IE=9" />
  <meta property="og:image" content="http://www.sounds-hochladen.de/projects/sounds-hochladen/images/logoplay.png" />
  <meta property="og:site_name" content="<page:pagetitle/>" />

  <link rel="shortcut icon" href="favicon.ico" type="image/x-icon">
  <link rel="stylesheet" href="/<page:config property="localPath"/>templates/<page:config property="template"/>/css/template.css" />
  <link rel="stylesheet" href="/<page:config property="localPath"/>templates/<page:config property="template"/>/css/base/jquery-ui.css"/>
  <link rel="stylesheet" href="/<page:config property="localPath"/>templates/<page:config property="template"/>/css/jquery.rating.css"/>
  <script language="javascript" src="/<page:config property="localPath"/>templates/<page:config property="template"/>/js/jquery-1.9.0.js" type="text/javascript"></script>
  <script language="javascript" src="/<page:config property="localPath"/>templates/<page:config property="template"/>/js/jquery-ui-1.10.0.custom.min.js" type="text/javascript"></script>
  <script language="javascript" src="/<page:config property="localPath"/>templates/<page:config property="template"/>/js/jquery.rating.js" type="text/javascript"></script>
  <script language="javascript" src="/<page:config property="localPath"/>templates/<page:config property="template"/>/js/underscore.js" type="text/javascript"></script>
  <script language="javascript" src="/<page:config property="localPath"/>templates/<page:config property="template"/>/js/backbone.js" type="text/javascript"></script>
  <script language="javascript" src="/<page:config property="localPath"/>templates/<page:config property="template"/>/js/download.js" type="text/javascript"></script>
  <script language="javascript" src="/<page:config property="localPath"/>templates/<page:config property="template"/>/js/downloadManager.js" type="text/javascript"></script>
  <script language="javascript" src="/<page:config property="localPath"/>templates/<page:config property="template"/>/js/page.js" type="text/javascript"></script>
  <script>

	var page;
	var downloadManager;
	
	// position of the player dialog
	var playerX;
	var playerY;
  
	$(document).ready(function() {
		// create a new page object, maybe we should call it "PageManager" ;)
		page = new Page();
		// load the start page
	  	page.loadStartpage();
	  	downloadManager = new DownloadManager();

	    $(function() {
	        $("#playerDialog").dialog({
	            autoOpen : false,
	            show: 'fade', 
	            hide: 'drop', 
	            width : 500,
	            height : 270,
	            dialogClass : 'dropShadow',
	            // store position on drag 
	            drag: function (ev, ui) {
	            	playerX = ui.position.left;
	            	playerY = ui.position.top;                
				}             
	        });
	        $("#ratingDialog").dialog({
	            autoOpen : false,
	            show: 'fade', 
	            hide: 'drop', 
	            width : 500,
	            height : 270,
	            dialogClass : 'dropShadow'
	        });

	    });
    
	    // prevent the player dialog from being moved when the page scrolls
	    $(window).scroll(function () {
	    	$("#playerDialog").dialog("option","position",[playerX,playerY]);
	    	$("#ratingDialog").dialog("option","position","center");
	    }); 
    
	    $("#abortRating").button();
	    $("#abortRating").click(function(e) {
	    	$("#ratingDialog").dialog('close');
	    });
	    
	    $("#submitRating").button();

	    /*
	    	Submit the rating.
	    	fetch the value from the hidden rating id input field
	    	and the selected value from the radio group
	    */	    
	    $("#submitRating").click(function(e) {
	    	$("#ratingDialog").dialog('close');
	    	var id = $("#ratingId").val();
	    	var value = $('#ratingValue').val();
	    	downloadManager.submitRating(id,value);	    	
	    });
	    
	    // enable rating for all radio buttons of class star
	    // $(":radio.star").rating();
	    
	    $('.rateSound').rating({
	    	callback: function(value, link){
	    		$("#ratingValue").val(value);
	    	}
	    });
	});
  
  </script>
 </head>
 <body>

<div id="head-ui"></div>
<span id="logo"></span>
<span id="sound-search"><input id="search-input" type="text" value="Durchsuche sounds-hochladen.de" /><input id="search-button" type="button" value="" /></span>


<span id="login">
<input id="login-button" type="button" value="Benutzer-Login">
</span>

<!---<span id="lang">
          <select id="lang-select">
          <option value="Deutsch">Deutsch</option>
          <option value="English">English</option>
          </select>
</span>--->
</span>



 <div id="left-ui">
 <a href="#" id="left-ui-button2"></a>
 
 <p class="p"><input id="icon-button" class="upload-button" type="button" value="Jetzt Hochladen" /></p>
 <p class="p"><input id="icon-button" class="suggest-button" type="button" value="Sound Vorschl&auml;ge" /></p>
 <p class="p"><input id="icon-button" class="rates-button" type="button" value="Beliebte Sounds" /></p>
 <p class="p"><input id="icon-button" class="latest-button" type="button" value="Neuste Sounds" /></p>
 <p class="p"><input id="icon-button" class="views-button" type="button" value="Oft Gespielt" /></p>
 <p class="p"><input id="icon-button" class="dlhits-button" type="button" value="Download Hits" /></p>
 <p class="p"><input id="icon-button" class="format-button" type="button" value="Nach Format" /></p>
 <p class="p"><input id="icon-button" class="categorie-button" type="button" value="Nach Kategorie" /></p>
 <p class="p"><input id="icon-button" class="autor-button" type="button" value="Nach Interpret" /></p>
 <p class="p"><input id="icon-button" class="search-button" type="button" value="Durchsuchen" /></p>

 <div id="left-ui-fun">
  Google Adds
 </div>

 </div><a href="#" id="left-ui-button"></a>

 <div id="main-window">
      <div id="main-content">
           <span id="main-content-title"></span>
           <hr>
           <div id="AUSTAUSCHER">
           </div>
			<div id="search" style="display:hidden">
		   		<table class="search" cellpadding="10">
		             <tr>
		                <td class="ui-widget">Autor</td>
		                <td><input name="searchAuthor" id="searchAuthor" size="65" class="textInput"/></td>                         
		             </tr>
		   			<tr>
		   			   <td class="ui-widget">Name</td>
		   			   <td><input name="searchName" id="searchName" size="65" class="textInput"/></td>          			   
		   			</tr>		             
		             <tr>
		                 <td colspan="2" align="center"><button id="searchButton">Suche starten!</button></td>
		             </tr>   			
		   		</table>
			</div>

      </div>

		<div id="main-content">
		Google Adds  Google Adds  Google Adds  Google Adds
		</div>


      <div id="main-content">
          <span id="main-content-title">Regisriere dich kostenlos und sichere dir viele Vorteile!</span>
           <hr>

         <p>Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.</p>

      </div>

		<div id="main-content"><center>
		<b>Unterst&uuml;tze sounds-hochladen.de</b>
		<p>F&uuml;ge Tools oder Banner zu deiner Webseite oder Homepage hinzu.<p>
		</center></div>
		
		<div id="main-content"><center><p><a href="http://www.files-hochladen.de" target="_blank"><font style="font-size:22px;font-color:#b03b27;" color="#b03b27"> XLS</font></a>&nbsp;<a href="http://www.files-hochladen.de" target="_blank"><font style="font-size:20px;font-color:#76a8d;" color="#76a8d"> MP3</font></a>&nbsp;<a href="http://www.files-hochladen.de" target="_blank"><font style="font-size:12px;font-color:#723e9c;" color="#723e9c"> M4A</font></a>&nbsp;<a href="http://www.files-hochladen.de" target="_blank"><font style="font-size:27px;font-color:#6fb57a;" color="#6fb57a"> Ohne Registrierung</font></a>&nbsp;<a href="http://www.files-hochladen.de" target="_blank"><font style="font-size:18px;font-color:#ce8c1f;" color="#ce8c1f"> PNG</font></a>&nbsp;<a href="http://www.files-hochladen.de" target="_blank"><font style="font-size:15px;font-color:#47da8c;" color="#47da8c"> JPEG</font></a>&nbsp;<a href="http://www.files-hochladen.de" target="_blank"><font style="font-size:13px;font-color:#32fe20;" color="#32fe20"> Musik</font></a>&nbsp;<a href="http://www.files-hochladen.de" target="_blank"><font style="font-size:18px;font-color:#f6be40;" color="#f6be40"> RM</font></a>&nbsp;<a href="http://www.files-hochladen.de" target="_blank"><font style="font-size:16px;font-color:#a218f9;" color="#a218f9"> Clips</font></a>&nbsp;<a href="http://www.files-hochladen.de" target="_blank"><font style="font-size:14px;font-color:#f6e56;" color="#f6e56"> XLA</font></a>&nbsp;<a href="http://www.files-hochladen.de" target="_blank"><font style="font-size:17px;font-color:#5a395d;" color="#5a395d"> DOC</font></a>&nbsp;<a href="http://www.files-hochladen.de" target="_blank"><font style="font-size:22px;font-color:#8e22c8;" color="#8e22c8"> RTF</font></a>&nbsp;<a href="http://www.files-hochladen.de" target="_blank"><font style="font-size:25px;font-color:#97220;" color="#97220"> WAV</font></a>&nbsp;<a href="http://www.files-hochladen.de" target="_blank"><font style="font-size:32px;font-color:#a9be79;" color="#a9be79"> Videos</font></a>&nbsp;<a href="http://www.files-hochladen.de" target="_blank"><font style="font-size:25px;font-color:#45df15;" color="#45df15"> ETX</font></a>&nbsp;<a href="http://www.files-hochladen.de" target="_blank"><font style="font-size:27px;font-color:#6e9d3;" color="#6e9d3"> OGG</font></a>&nbsp;<a href="http://www.files-hochladen.de" target="_blank"><font style="font-size:16px;font-color:#cae3c4;" color="#cae3c4"> TALK</font></a>&nbsp;<a href="http://www.files-hochladen.de" target="_blank"><font style="font-size:32px;font-color:#785b8b;" color="#785b8b"> HTML</font></a>&nbsp;<a href="http://www.files-hochladen.de" target="_blank"><font style="font-size:19px;font-color:#d4eca2;" color="#d4eca2"> AIF</font></a>&nbsp;<a href="http://www.files-hochladen.de" target="_blank"><font style="font-size:30px;font-color:#83872d;" color="#83872d"> AU</font></a>&nbsp;<a href="http://www.files-hochladen.de" target="_blank"><font style="font-size:19px;font-color:#8c72ec;" color="#8c72ec"> CSV</font></a>&nbsp;<a href="http://www.files-hochladen.de" target="_blank"><font style="font-size:23px;font-color:#7884b2;" color="#7884b2"> HTM</font></a>&nbsp;<a href="http://www.files-hochladen.de" target="_blank"><font style="font-size:16px;font-color:#b9b699;" color="#b9b699"> Dateien hochladen</font></a>&nbsp;<a href="http://www.files-hochladen.de" target="_blank"><font style="font-size:32px;font-color:#a25c11;" color="#a25c11"> Viele Formate</font></a>&nbsp;<a href="http://www.files-hochladen.de" target="_blank"><font style="font-size:19px;font-color:#89e26c;" color="#89e26c"> MOV</font></a>&nbsp;<a href="http://www.files-hochladen.de" target="_blank"><font style="font-size:14px;font-color:#a5acc;" color="#a5acc"> Dokumente</font></a>&nbsp;<a href="http://www.files-hochladen.de" target="_blank"><font style="font-size:27px;font-color:#e4f6f8;" color="#e4f6f8"> CSS</font></a>&nbsp;<a href="http://www.files-hochladen.de" target="_blank"><font style="font-size:23px;font-color:#736d84;" color="#736d84"> MP2</font></a>&nbsp;<a href="http://www.files-hochladen.de" target="_blank"><font style="font-size:25px;font-color:#8bdf88;" color="#8bdf88"> Movies</font></a>&nbsp;<a href="http://www.files-hochladen.de" target="_blank"><font style="font-size:31px;font-color:#7dee69;" color="#7dee69"> TSV</font></a>&nbsp;<a href="http://www.files-hochladen.de" target="_blank"><font style="font-size:13px;font-color:#8b8d4a;" color="#8b8d4a"> PDF</font></a>&nbsp;<a href="http://www.files-hochladen.de" target="_blank"><font style="font-size:20px;font-color:#42aa2a;" color="#42aa2a"> GIF</font></a>&nbsp;<a href="http://www.files-hochladen.de" target="_blank"><font style="font-size:14px;font-color:#efa559;" color="#efa559"> TXT</font></a>&nbsp;<a href="http://www.files-hochladen.de" target="_blank"><font style="font-size:27px;font-color:#f38433;" color="#f38433"> WMV</font></a>&nbsp;<a href="http://www.files-hochladen.de" target="_blank"><font style="font-size:24px;font-color:#3e0c9;" color="#3e0c9"> AVI</font></a>&nbsp;<a href="http://www.files-hochladen.de" target="_blank"><font style="font-size:19px;font-color:#e0458b;" color="#e0458b"> MKV</font></a>&nbsp;<a href="http://www.files-hochladen.de" target="_blank"><font style="font-size:27px;font-color:#f91b52;" color="#f91b52"> DOT</font></a>&nbsp;<a href="http://www.files-hochladen.de" target="_blank"><font style="font-size:32px;font-color:#d7194d;" color="#d7194d"> Sounds</font></a>&nbsp;<a href="http://www.files-hochladen.de" target="_blank"><font style="font-size:16px;font-color:#8cd47b;" color="#8cd47b"> RTX</font></a>&nbsp;<a href="http://www.files-hochladen.de" target="_blank"><font style="font-size:20px;font-color:#264df;" color="#264df"> MPEG</font></a>&nbsp;<a href="http://www.files-hochladen.de" target="_blank"><font style="font-size:27px;font-color:#d81e68;" color="#d81e68"> JPG</font></a>&nbsp;<a href="http://www.files-hochladen.de" target="_blank"><font style="font-size:30px;font-color:#68bce;" color="#68bce"> WMA</font></a>&nbsp;<a href="http://www.files-hochladen.de" target="_blank"><font style="font-size:17px;font-color:#bc9440;" color="#bc9440"> SPC</font></a>&nbsp;<a href="http://www.files-hochladen.de" target="_blank"><font style="font-size:31px;font-color:#8ea093;" color="#8ea093"> MID</font></a>&nbsp;<a href="http://www.files-hochladen.de" target="_blank"><font style="font-size:17px;font-color:#e2f76f;" color="#e2f76f"> Fotos</font></a>&nbsp;<a href="http://www.files-hochladen.de" target="_blank"><font style="font-size:16px;font-color:#7dc6e5;" color="#7dc6e5"> DIVX</font></a>&nbsp;<a href="http://www.files-hochladen.de" target="_blank"><font style="font-size:16px;font-color:#9d19ae;" color="#9d19ae"> BMP</font></a>&nbsp;<a href="http://www.files-hochladen.de" target="_blank"><font style="font-size:14px;font-color:#83a3df;" color="#83a3df"> Bilder</font></a>&nbsp;<a href="http://www.files-hochladen.de" target="_blank"><font style="font-size:32px;font-color:#43f923;" color="#43f923"> JPE</font></a>&nbsp;<a href="http://www.files-hochladen.de" target="_blank"><font style="font-size:30px;font-color:#38ef60;" color="#38ef60"> MPG</font></a>&nbsp;<a href="http://www.files-hochladen.de" target="_blank"><font style="font-size:16px;font-color:#d13a19;" color="#d13a19"> MP4</font></a>&nbsp;</p><p><a href="http://www.files-hochladen.de" target="_blank"><font class="normal-font">www.files-hochladen.de</font></a></p><p></p></center>
		</div>


		<div id="foot-ui">
           <div id="copyright">&copy; 2013 sounds-hochladen.de</div>
           <div id="powered">Powered by Andamio</div>
             <div id="imprint">
                <a href="legal" target="_blank">Rechtshinweise</a> | <a href="agb" target="_blank">Nutzungsbedingungen</a> |
                <a href="Datenschutz" target="_blank">Datenschutz</a> | <a href="Sicherheit" target="_blank">Sicherheit</a> | <a href="Urheberrecht" target="_blank">Urheberrecht</a>
                <a href="faq" target="_blank">FAQ</a> | <a href="impressum" target="_blank">Impressum</a> | <a href="shop" target="_blank">Shop</a> | <a href="sitemap" target="_blank">Sitemap</a> |
                <a href="antispam" target="_blank">AntiSpam</a> | <a href="Feedback" target="_blank">Feedback geben</a> | <a href="social" target="_blank">Sozial</a>
           </div>
		</div>

		<div id="foot-content"><center>Amazon Adds</center></div>
		</div>
		<div id="notclickable"></div>
		<div id="playerDialog" title="Music Player">
		    <p>
		     <div id="player"></div>
		    </p>
		</div>  
		<div id="ratingDialog" title="Sound bewerten">
			<table border="0" width="100%" height="100%">
				<tr>
					<td align="center" colspan="2">						
						Gib Deine Bewertung ab
					</td>
				</tr>
				<tr>
					<td>
						<div style="padding-left:30px">
							Bewertung
						</div>
					</td>
					<td align="center">
						<div style="padding-left:30px">
							<input name="rateSound" type="radio" class="rateSound" value="1"/>
							<input name="rateSound" type="radio" class="rateSound" value="2"/>
							<input name="rateSound" type="radio" class="rateSound" value="3"/>
							<input name="rateSound" type="radio" class="rateSound" value="4"/>
							<input name="rateSound" type="radio" class="rateSound" value="5"/>
						</div>		     					
					</td>
				</tr>				
				<tr>
					<td align="center">
						<button id="abortRating">Abbrechen</button>
					</td>
					<td align="center">
						<button id="submitRating">Bewertung abgeben</button>
					</td>					
				</tr>
			</table>
			<input type="hidden" id="ratingId" name="ratingId"></input>
			<input type="hidden" id="ratingValue" name="ratingValue"></input>				
		</div>  
 
 </body>
</html:html>
