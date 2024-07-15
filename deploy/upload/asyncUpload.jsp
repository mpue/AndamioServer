<?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ taglib uri="/tags/pagetags" prefix="page" %>
<%@ page import="org.pmedv.web.ServerUtil"%>

<%@ page import="java.*" %>
<html:html xhtml="true">

	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Upload</title>	
        <link rel="stylesheet" href="/<page:config property="localPath"/>upload/css/base/jquery-ui.css" />
		<link href="/<page:config property="localPath"/>templates/sh/css/template.css" rel="stylesheet" type="text/css" />
		<script type="text/javascript" src="/<page:config property="localPath"/>upload/js/asyncUpload.js"></script>
        <script src="/<page:config property="localPath"/>upload/js/jquery-1.9.0.js"></script>
        <script src="/<page:config property="localPath"/>upload/js/jquery-ui-1.10.0.custom.min.js"></script>
        <script>
	        $(document).ready(function() {
	            init();
	            
  	            $.getJSON("/andamio/UserDownloadAction.do?do=getCategories", function(result) {
	                var categories = $("#selectable");
	                $.each(result, function(item) {
	                	categories.append('<li class="ui-widget-content"'+' id="'+this.id+'">'+this.name+'</li>');
	                });
	            });  

				$(function() {
				    $( "#selectable" ).selectable({
				        stop: function() {
				            var result = $( "#select-result" ).empty();
				            $( ".ui-selected", this ).each(function() {
				                // var index = $( "#selectable li" ).index( this );
				                var index = $(this).attr('id');
				                result.append(index + " ");
				            });
				            $("#categories").val($("#select-result").text());
				        }
				    });
				    });	            
				});        
        </script>		
	</head>
	<body bgcolor="#000">
		<div id="uploadDialog" title="Please wait">
			<p>
			     Uploading file
			</p>
			<p>
			 <div id="progressbar" style="width:250px;height:20px"></div>
			</p>
		</div>
		<div id="validateDialog" title="Validation">
            <p class="ui-widget">
                You must provide file, name and description.</br>
            </p>
            <p align="center" >
                <button id="closeButton">Ok</button>
            </p>
        </div>
		
		
		<iframe frameborder="0" height="0" id="uploadFrameID" name="uploadFrame" scrolling="yes" width="0"></iframe> 
			<form name="FileUploadForm" method="post" action="/andamio/AsyncUpload" target="uploadFrame" onsubmit="submitUpload();"  enctype="multipart/form-data" class="FileUploadForm">
				<input type="hidden" name="directory" id="directory" value="upload/data/"/>
				<p>
				<h2 style="padding-left : 20px">Lade jetzt Deinen Sound hoch</h2>
				</p>
				<hr size="1"/>          		
          		<table class="upload" cellpadding="10">
          			<tr>
          				<td class="ui-widget">File</td>
          				<td><input type="file" id="uploadFile" name="uploadFile" class="textInput" size="50"/></td>          				
          			</tr>
          			<tr>
          			   <td class="ui-widget">&Ouml;ffentlich</td>          			   
          			   <td><input type="checkbox" id="publicfile" name="publicfile" class="textInput"/></td>
          			             			
          			</tr>   
                    <tr>
                       <td class="ui-widget">Download erlauben</td>                       
                       <td><input type="checkbox" id="downloadable" name="downloadable"/></td>                                            
                    </tr>   
                    <tr>
                       <td class="ui-widget">Autor</td>
                       <td><input name="author" id="author" size="65" class="textInput"/></td>                         
                    </tr>
                    <tr>
                       <td class="ui-widget">Lizenz</td>
                       <td><input name="license" id="license" size="65" class="textInput"/></td>                         
                    </tr>
          			<tr>
          			   <td class="ui-widget">Name</td>
          			   <td><input name="name" id="name" size="65" class="textInput"/></td>          			   
          			</tr>
                    <tr>                    
                       <td colspan="2" class="ui-widget">Kategorien wählen</td>
                    </tr>
                    <tr>
                       <td colspan="2">
							<ol id="selectable">
							</ol>                     
                       </td>                         
                    </tr>
                    <tr>
                       <td colspan="2" class="ui-widget">Beschreibung</td>
                    </tr>
                    <tr>
                       <td colspan="2"><textarea rows="3" cols="70" name="description" id="description" class="textInput"></textarea></td>                       
                    </tr>
                    <tr>
                       <td colspan="2" class="ui-widget">Tags</td>
                    </tr>
                    <tr>
                       <td colspan="2"><textarea rows="3" cols="70" name="tags" id="tags" class="textInput"></textarea></td>                       
                    </tr>
                    
                    <tr>
                        <td colspan="2" align="center"><button id="uploadButton">Jetzt hochladen!</button></td>
                    </tr>
          			
          		</table>
          		<input type="hidden" name="categories" id="categories" size="80" class="textInput"/>
			</form>
           
            <span style="display:none" id="select-result">none</span>
            
	</body>
</html:html>