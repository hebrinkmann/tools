<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
	xmlns:fn="http://www.w3.org/2005/xpath-functions">
	<xsl:output method="text" encoding="ISO-8859-1"/>

	<xsl:template match="/">
		<xsl:text>Aufgabe; Storypoints; Version&#xa;</xsl:text>
		<xsl:for-each select="/map/node">
			<xsl:call-template name="node"/>
		</xsl:for-each>
	</xsl:template>

	<xsl:template name="node">
		<xsl:param name="indent"/>
		<xsl:variable name="newindent">
			<xsl:value-of select="$indent"/>
			<xsl:text>  </xsl:text>
		</xsl:variable>

		<xsl:for-each select="node">
			<xsl:variable name="complexity">
				<xsl:for-each select="node[starts-with(@TEXT, 'c:')]">
					<xsl:value-of select="substring-after(@TEXT, 'c:')"/>
				</xsl:for-each>
			</xsl:variable>
			<xsl:variable name="version">
				<xsl:for-each select="node[starts-with(@TEXT, 'v:')]">
					<xsl:value-of select="substring-after(@TEXT, 'v:')"/>
				</xsl:for-each>
			</xsl:variable>

			<xsl:choose>
				<xsl:when test="substring(@TEXT, string-length(@TEXT)) = '.'"/>
				<xsl:when test="substring(@TEXT, string-length(@TEXT)) = '?'"/>
				<xsl:otherwise>
					<xsl:value-of select="$indent"/>
					<xsl:value-of select="@TEXT"/>
					<xsl:text>; </xsl:text>
					<xsl:value-of select="$complexity"/>
					<xsl:text>; </xsl:text>
					<xsl:value-of select="$version"/>
					<xsl:text>&#xa;</xsl:text>

					<xsl:if test="string-length($complexity) = 0">
						<xsl:call-template name="node">
							<xsl:with-param name="indent" select="$newindent"/>
						</xsl:call-template>
					</xsl:if>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:for-each>
	</xsl:template>
</xsl:stylesheet>