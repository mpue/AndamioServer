<?xml version="1.0" ?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format"
	xmlns:xhtml="http://www.w3.org/1999/xhtml" exclude-result-prefixes="xhtml">
	<xsl:output method="html" version="1.0" standalone="yes"
		omit-xml-declaration="no" encoding="utf-8" media-type="text/xml"
		indent="yes" />

	<xsl:strip-space elements="*" />

	<xsl:template match="/">
	  <html>
	  <body>
	    <h2>Patient report for patient : <xsl:value-of select="patientReport/patient/randomNumber"/> </h2>
	    <table border="1" width="50%">
	      <tr bgcolor="#9acd32">
	        <th>Application date</th>
	        <th>Parameter</th>
	        <th>Value</th>
	      </tr>
	      <xsl:for-each select="patientReport/params">
	        <tr>
	          <td width="40%"><xsl:value-of select="applicationDate"/></td>
	          <td width="40%"><xsl:value-of select="parameter"/></td>
	          <td><xsl:value-of select="value"/> mg</td>
	        </tr>
	      </xsl:for-each>
	    </table>
	  </body>
	  </html>
	</xsl:template>


</xsl:stylesheet>
