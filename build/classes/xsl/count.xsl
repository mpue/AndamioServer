<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="html" indent="yes"/>
    <xsl:template match="/">
        <html>
            <head>
                <title>zählen</title>
            </head>
            <body>
                <h1>Zählen</h1>
                <xsl:apply-templates select="root"/>
                <h1>geht auch direkt
                <xsl:call-template name="ausgabe">
                    <xsl:with-param name="anzahl">3</xsl:with-param>
                </xsl:call-template>
                </h1>
            </body>
        </html>
    </xsl:template>
    <xsl:template match="element">
        <xsl:param name="zahl" select="."/>
        <h1>soviel mal zählen
            <xsl:value-of select="$zahl"/>
        <xsl:text> Los gehts : </xsl:text>
        <xsl:call-template name="ausgabe">
            <xsl:with-param name="anzahl" select="$zahl"/>
        </xsl:call-template>
        </h1>
    </xsl:template>
    <!-- eigentliche schleife -->
    <xsl:template name="ausgabe">
        <xsl:param name="anzahl"></xsl:param>
        <xsl:param name="z">0</xsl:param>

            <xsl:value-of select="$z"/>

        <xsl:if test="$z!=$anzahl">
        <xsl:text>,</xsl:text>
            <xsl:call-template name="ausgabe">
                <xsl:with-param name="anzahl" select="$anzahl"/>
                <xsl:with-param name="z" select="$z+1"/>
            </xsl:call-template>

        </xsl:if>
    </xsl:template>
</xsl:stylesheet>