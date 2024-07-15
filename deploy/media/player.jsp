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
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Player</title>	
 
        <link href="css/blue.monday/jplayer.blue.monday.css" rel="stylesheet" type="text/css" /> 
         
        <script type="text/javascript" src="js/jquery-1.9.0.js"></script>    
        <script type="text/javascript" src="js/jquery.jplayer.min.js"></script>
        <script type="text/javascript">
	        $(document).ready(function() {	        
	        <%
	        if (null != request.getParameter("autoplay") && request.getParameter("autoplay").equalsIgnoreCase("true")) {
	        
	        %>
	            $("#jquery_jplayer_1").jPlayer({
	                ready: function(event) {
	                    $(this).jPlayer("setMedia", {
	                        mp3: "<%= request.getParameter("play") %>",                       
	                    }).jPlayer("play");
	                },
	                swfPath: "js",
	                supplied: "mp3"
	            });
	        <%
	        }
	        else {
	        %>
		        $("#jquery_jplayer_1").jPlayer({
		            ready: function(event) {
		                $(this).jPlayer("setMedia", {
		                    mp3: "<%= request.getParameter("play") %>",                       
		                });
		            },
		            swfPath: "js",
		            supplied: "mp3"
		        });	        
	        <%
	        }
	        %>
	        });
	        window.oncontextmenu = function(){return false;}
	        
        </script>        
    </head>
    <body>
        <div id="jquery_jplayer_1" class="jp-jplayer"></div>
        
        <div id="jp_container_1" class="jp-audio">
            <div class="jp-type-single">
                <div class="jp-gui jp-interface">
                    <ul class="jp-controls">
                        
                        <!-- comment out any of the following <li>s to remove these buttons -->
                        
                        <li><a href="javascript:;" class="jp-play" tabindex="1">play</a></li>
                        <li><a href="javascript:;" class="jp-pause" tabindex="1">pause</a></li>
                        <li><a href="javascript:;" class="jp-stop" tabindex="1">stop</a></li>
                        <li><a href="javascript:;" class="jp-mute" tabindex="1" title="mute">mute</a></li>
                        <li><a href="javascript:;" class="jp-unmute" tabindex="1" title="unmute">unmute</a></li>
                        <li><a href="javascript:;" class="jp-volume-max" tabindex="1" title="max volume">max volume</a></li>
                    </ul>
                    
                    <div class="jp-progress">
                        <div class="jp-seek-bar">
                            <div class="jp-play-bar"></div>
                        </div>
                    </div>
                    <div class="jp-volume-bar">
                        <div class="jp-volume-bar-value"></div>
                    </div>
                    <div class="jp-current-time"></div>
                    <div class="jp-duration"></div>                   
                </div>
                <div class="jp-title">
                    <ul>
                        <li><%= request.getParameter("caption") %></li>
                    </ul>
                </div>
                <div class="jp-no-solution">
                    <span>Update Required</span>
                    To play the media you will need to either update your browser to a recent version or update your <a href="http://get.adobe.com/flashplayer/" target="_blank">Flash plugin</a>.
                </div>
            </div>
        </div>     
    </body>
</html:html>