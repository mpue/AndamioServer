<?xml version="1.0" ?>
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format"
	xmlns:xhtml="http://www.w3.org/1999/xhtml" exclude-result-prefixes="xhtml">
	<xsl:output method="xml" version="1.0" standalone="yes"
		omit-xml-declaration="yes" encoding="utf-8" media-type="text/xml"
		indent="yes" />

	<xsl:strip-space elements="*" />

	<xsl:template match="/page">		
		<html>
			<xsl:template match="/head">
				<xsl:apply-templates/>
			</xsl:template>
		
			
			<body>
				<xsl:apply-templates/>
			</body>
		</html>		
			      
	</xsl:template>



</xsl:stylesheet>
