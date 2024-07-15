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
<meta charset="utf-8" />
<title><page:pagetitle/></title>
<link rel="shortcut icon" href="/<page:config property="localPath"/>templates/<page:config property="template"/>/favicon.ico" type="image/x-icon" /> 
<link rel="stylesheet" href="/<page:config property="localPath"/>templates/<page:config property="template"/>/css/ui-darkness/jquery-ui-1.10.0.custom.min.css" />
<script src="/<page:config property="localPath"/>templates/<page:config property="template"/>/js/jquery-1.9.0.js"></script>
<script src="/<page:config property="localPath"/>templates/<page:config property="template"/>/js/jquery-ui-1.10.0.custom.min.js"></script>

<script>

    function submitComment() {
        $.ajax({
            url: '/<page:config property="localPath"/>/users/UserComment.do?do=addPageComment',
            type: "post",
            data: "content_id=<bean:write name="MainpageForm" property="content.id" filter="false"/>&comment="+$('#comment').val(),
            success: function(){
            	location.reload(true);
            },
            error:function(){
            	alert("Could not create comment.");
            }   
        }); 
    }

    function deleteComment(commentId) {
        $.ajax({
            url: '/<page:config property="localPath"/>/users/UserComment.do?do=deletePageComment',
            type: "post",
            data: "content_id=<bean:write name="MainpageForm" property="content.id" filter="false"/>&comment_id="+commentId,
            success: function(){
                location.reload(true);
            },
            error:function(){
                alert("Could not delete comment.");
            }   
        }); 
    }
    

/* 	function deleteComment(commentId) {
	
	    var url = '/<page:config property="localPath"/>/users/UserComment.do?do=deletePageComment';
	    
	    var oOptions = {
	        method : "post",
	        parameters : "content_id=<bean:write name="MainpageForm" property="content.id" filter="false"/>&comment_id="+commentId,
	        onSuccess : function(oXHR, oJson) {
	            location.reload(true);  
	        },
	        onFailure : function(oXHR, oJson) {
	            alert("Could not delete comment.");
	        }
	    };
	    
	    var oRequest = new Ajax.Request(url, oOptions);
	    
	}
 */	
	$.getDocHeight = function(){
	    return Math.max(
	        $(document).height(),
	        $(window).height(),
	        /* For opera: */
	        document.documentElement.clientHeight
	    );
	};

	$(window).resize(function () {
		$("#content").css({"height": $(window).height()-180   });
		$("#tabs-1").css({"height": $(window).height()-280   });
	});
	
	$(document).ready(function() { 
		
		$("#content").css({"height": $(window).height()-180   });
			    
		$(function() {
	        $("#accordion").accordion({
	            heightStyle : "content"	            
	        });
	    });
	    $(function() {
	        $("#tabs").tabs({
	            // heightStyle : "fill"
	        });
	    });
	    $(function() {
	        $("#dialog").dialog({
	            autoOpen : false,
	            show: 'fade', 
	            hide: 'drop' 
	        });
	    });

/* 	    $(function() {
	        $("#imprint").button().click(function(event) {
	            event.preventDefault();
	            $('#dialog').dialog('open');
	        });
	    }); */

	    $(function() {
	        $("#closeButton").button().click(function(event) {
	            event.preventDefault();
	            $('#dialog').dialog('close');
	        });
	    });

        $(function() {
            $("#addComment").button().click(function(event) {
                event.preventDefault();
                submitComment();
            });
        });	    

        $(function() {
            $("#deleteComment").button().click(function(event) {
                event.preventDefault();
                submitComment();
            });
        });   

        $(function() {
            $("#loginButton").button().click(function(event) {
                event.preventDefault();
                document.forms['LoginForm'].submit();        
            });
        });     
        
        $(function() {
            $("#logoutButton").button().click(function(event) {
                event.preventDefault();
                document.forms['LogoutForm'].submit();        
            });
        });     

        $(function() {
        	$( "#Navigation" ).menu();
      	});
        
	    $("#tabs-1").css({"height": $(window).height()-280   });
		
	}); 
</script>
<style type="text/css">

html {
	scrollbar-base-color: #222222;
	scrollbar-arrow-color: white;
	scrollbar-track-color: #333333;
	
	scrollbar-shadow-color: black;
	scrollbar-lightshadow-color: black;
	scrollbar-darkshadow-color: gray;
	
	scrollbar-highlight-color: white;
	scrollbar-3dlight-color: black;
}
body {
	color : #f19b14;
}
p{
    text-justify:inter-word;
}

.ui-widget {
	font-size: 14px;
}
.top {
    background-image: url("/<page:config property="localPath"/>templates/<page:config property="template"/>/images/orange.png");
    background-repeat : no-repeat;
    height:150px;
    padding: 0px;
    margin : 0px;
    border-bottom: 1px solid #f19b14;
}

.bottom {
    height:25px;
    padding: 0px;
    margin : 0px;
    border-top: 1px solid #f19b14;
}

.footer {
    font-family: sans-serif;
    text-align: center;
    color : #f19b14;
    font-size: 10px;
}

.title {
    font-size: 50pt;
    color: #fff;
    font-family: sans-serif;
    padding-left: 50px;
}
body {
    padding: 0px;
    margin: 0px;
    background-color: #000;
}

.scroll {
    overflow: auto;     
}

.textInput {
    background-color: #222;
    border : 1px solid #f19b14;
    color : #f19b14; 
    
}
</style>
</head>
<body>
    <table class="top" width="100%" cellspacing="0" cellpadding="0">
        <tr height="150px">
            <td>
                &nbsp;
            </td>
        </tr>
    </table>
	<div id="dialog" title="Contact">
		<p>
		  Matthias Püski</br>
		  Alte Sägemühle 5</br>
		  79117 Freiburg</br>
		  eMail : pueski(at)gmx(dot)de</br>
		  Internet : <a href="http://www.pueski.de" target="_blank">www.pueski.de</a></br>
		</p>
		<p align="center">
			<button id="closeButton">Ok</button>
		</p>
	</div>
    
	<table id="content" style="height:96%;border:0;width:100%;">
		<tr>
			<td width="20%" valign="top">
				<div id="accordion">
					<h3>Menu</h3>
					<div>
	                    <page:xmenu      
	                        startNode="Section 1"               
	                        forceLevelIndent="true"
	                        orientation="vertical"
	                        selected="<%= request.getParameter(\"nodeName\")%>"
	                        mode="website">
	                    </page:xmenu>										
					</div>
					<h3>Browse</h3>
					<div>
                        <page:xmenu                              
                            startNode="Section 2"               
                            forceLevelIndent="true"
                            orientation="vertical"                            
                            selected="<%= request.getParameter(\"nodeName\")%>"
                            mode="website">
                        </page:xmenu>                                       
					
					</div>
					<h3>News</h3>
					<div>
						<p>Nam enim risus, molestie et, porta ac, aliquam ac, risus.
							Quisque lobortis. Phasellus pellentesque purus in massa. Aenean
							in pede.</p>
						<img src="/andamio/content/flower.png" style="border: 1px solid #f19b14"/>							
							<p> Phasellus ac libero ac tellus pellentesque semper. Sed
							ac felis. Sed commodo, magna quis lacinia ornare, quam ante
							aliquam nisi, eu iaculis leo purus venenatis dui.</p>


					</div>
					<h3>My Orange</h3>
					<div>
	                    <logic:present name="permission" scope="session">
	                    <%@include file="logoutbox.jsp" %>                                                          
	                    </logic:present>
	                    <logic:notPresent name="permission" scope="session">
	                    <%@include file="loginbox.jsp" %>                               
	                    </logic:notPresent>					
                    </div>
				</div>
			</td>
			<td valign="top">
				<div id="tabs">
					<ul>
						<li><a href="#tabs-1"><bean:write name="MainpageForm" property="content.contentname" filter="false"/></a></li>
					</ul>
					<div id="tabs-1" class="scroll">
                        <bean:write name="MainpageForm" property="content.contentstring" filter="false"/>
                        <p>
                            &nbsp;
                        </p>
	                    <logic:equal value="true" name="MainpageForm" property="content.commentsAllowed">
	                        <logic:present name="permission" scope="session">
	                        <div class="comments">
	                            <div>
	                                <textarea class="textInput" rows="8" cols="150" id="comment" name="comment"></textarea>                                        
	                            </div>
	                            <p>
	                            &nbsp;
	                            </p>
	                            <div class="submitComment">
	                               <button id="addComment">Add comment</button>	                                                   
	                            </div>
	                        </div>
	                        </logic:present>                
	                        <div class="comments">
	                            <p>
	                                <b>Comments :</b>
	                            </p>
	                            <logic:notEmpty name="MainpageForm" property="content.userComments">                                    
	                                <logic:iterate id="comment" name="MainpageForm" property="content.userComments">
	                                    <p class="author"><bean:write name="comment" property="author"/> wrotes on <bean:write name="comment" property="humanReadableTime"/>
	                                        <logic:equal value="admin" name="login" scope="session">
	                                            <button class="ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only" role="button" onclick="deleteComment(<bean:write name="comment" property="id"/>);"/><span class="ui-button-text">Delete</span></button>
	                                        </logic:equal>                          
	                                    </p>
	                                    <p class="comment"><bean:write name="comment" property="text"/></p>                         
	                                </logic:iterate>
	                            </logic:notEmpty>
	                            <logic:empty name="MainpageForm" property="content.userComments">
	                                No comments found.
	                            </logic:empty>
	                        </div>
	                    </logic:equal>
                        
					</div>
				</div>
			</td>
		</tr>
	</table>
    <table class="bottom" width="100%" cellspacing="0" cellpadding="0">
        <tr height="25px">
            <td class="footer">
                <a id="imprint" href="#" onclick="$('#dialog').dialog('open');" style="margin : 5px;color : #f19b14">The orange (c) 2013 Matthias Pueski</a>
            </td>
        </tr>
    </table>


</body>
</html:html>