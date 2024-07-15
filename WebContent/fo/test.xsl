<?xml version="1.0" ?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format"
	xmlns:xhtml="http://www.w3.org/1999/xhtml" exclude-result-prefixes="xhtml">
	<xsl:output method="html" version="1.0" standalone="yes"
		omit-xml-declaration="yes" encoding="utf-8" media-type="text/xml"
		indent="yes" />

	<xsl:strip-space elements="*" />

	<xsl:template name="common-atts">
	  <xsl:copy-of select="@id|@color|@height|@width|@xml:lang|@border"/>
	  <xsl:if test="@align"><xsl:attribute name="text-align"><xsl:value-of select="@align"/></xsl:attribute></xsl:if>
	  <xsl:if test="@nowrap"><xsl:attribute name="wrap-option">no-wrap</xsl:attribute></xsl:if>
	</xsl:template>
	
	<xsl:template match="page">
		<html>
			<head>
				<title>
				</title>
			</head>
			<body>
				<xsl:call-template name="common-atts"/>
				<xsl:apply-templates/>							
			</body>
		</html>
	</xsl:template>
	
	<xsl:template match="page/table">	
		<table>
			<xsl:call-template name="common-atts"/>
			<xsl:apply-templates/>			
		</table>	    
	</xsl:template>

	<xsl:template match="page/table/row">
		<tr>
			<xsl:call-template name="common-atts"/>		
			<xsl:apply-templates/>
		</tr>
	</xsl:template>

	<xsl:template match="page/table/row/column">
		<td>
			<xsl:call-template name="common-atts"/>		
			<xsl:apply-templates/>
		</td>
	</xsl:template>


</xsl:stylesheet>
