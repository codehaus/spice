<?xml version="1.0"?>

<!--
stylesheet to convert naproject elements to project elements.
To get work around so that don't have to namespace all jelly
elements because project is one of jellys top level tags.
-->
<xsl:stylesheet
    version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:output method="xml" indent="yes"/>

    <!-- naproject to project -->
    <xsl:template match="naproject">
        <project>
            <xsl:apply-templates/>
        </project>
    </xsl:template>


    <xsl:template match="node()|@*" priority="-1">
        <xsl:copy>
            <xsl:apply-templates select="node()|@*"/>
        </xsl:copy>
    </xsl:template>

</xsl:stylesheet>
