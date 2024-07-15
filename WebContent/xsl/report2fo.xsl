<?xml version="1.0" ?>
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format"
	xmlns:xhtml="http://www.w3.org/1999/xhtml" exclude-result-prefixes="xhtml">
	<xsl:output method="xml" version="1.0" standalone="yes"
		omit-xml-declaration="yes" encoding="utf-8" media-type="text/xml"
		indent="yes" />

	<xsl:strip-space elements="*" />

	<xsl:template match="/">		
		<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
		  <fo:layout-master-set>
		    <fo:simple-page-master master-name="first"
		          margin-right="1.5cm"
		          margin-left="1.5cm"
		          margin-bottom="0.5cm"
		          margin-top="1cm"
		          page-width="29.7cm"
		          page-height="21.0cm">
		      <fo:region-body margin-top="1cm"/>
		      <fo:region-before extent="1cm"/>
		      <fo:region-after extent="1.5cm"/>
		    </fo:simple-page-master>
		  </fo:layout-master-set>
		
		  <fo:page-sequence master-reference="first">
		    <fo:static-content flow-name="xsl-region-before">
		      <fo:block line-height="14pt" font-size="10pt" text-align="end">AVANTI CSI</fo:block>
		    </fo:static-content>
		    <fo:static-content flow-name="xsl-region-after">
		      <fo:block line-height="14pt" font-size="10pt" text-align="center">Seite <fo:page-number/></fo:block>
		      <!--  
		      <fo:block line-height="14pt" font-size="10pt" text-align="begin">
		      	<fo:instream-foreign-object>  
					<svg xmlns="http://www.w3.org/2000/svg" xmlns:odm="http://product.corel.com/CGS/11/cddns/" xml:space="preserve" width="2.0in" height="1.0in" shape-rendering="geometricPrecision" text-rendering="geometricPrecision" image-rendering="optimizeQuality" fill-rule="evenodd"
					     viewBox="0 0 4724 1574">
					 <g id="Ebene 1">
					  <defs>
					   <linearGradient id="id0" gradientUnits="userSpaceOnUse" x1="1177" y1="478" x2="994" y2="1519">
					    <stop offset="0" stop-color="#B9BEDE"/>
					    <stop offset="0.6" stop-color="#5D82B6"/>
					    <stop offset="1" stop-color="#00458E"/>
					   </linearGradient>
					  </defs>
					  <path fill="url(#id0)" d="M1319 1028c-47,35 -92,68 -132,98 91,35 183,71 276,106 34,-35 70,-72 107,-110 -82,-30 -166,-62 -251,-94zm135 -96c-40,28 -78,55 -113,81 83,31 166,62 245,92l88 -91c-70,-26 -144,-54 -220,-82zm106 -74c-31,21 -60,41 -89,62 75,28 148,55 217,81l68 -71 -196 -72zm84 -57c-24,15 -47,31 -70,47l193 71 53 -54 -176 -64zm-624 114l-184 73c98,39 201,80 307,121l142 -94c-88,-33 -178,-67 -265,-100zm189 -75l-159 63c85,33 172,65 258,98 39,-26 79,-52 121,-79 -72,-27 -146,-54 -220,-82zm145 -58l-121 48c72,27 144,54 214,81 31,-20 62,-40 94,-60 -62,-23 -125,-46 -187,-69zm114 -46l-95 38c61,23 122,45 183,67 24,-15 49,-31 73,-46l-161 -59zm-732 71c-78,18 -154,35 -226,51 86,35 180,72 278,112l193 -70c-84,-31 -166,-63 -245,-93zm240 -56c-67,16 -135,32 -202,47 77,30 157,60 239,91l167 -60c-68,-26 -136,-52 -204,-78zm182 -42c-50,11 -100,23 -152,35 66,25 133,51 200,76l127 -46c-59,-22 -117,-44 -175,-65zm143 -35c-38,9 -78,19 -119,29 57,21 114,42 171,64l100 -37c-52,-19 -103,-38 -152,-56zm-1088 92l54 -8c59,24 126,52 198,82 73,-15 153,-30 235,-47 -65,-25 -128,-50 -187,-73l43 -6c58,23 120,46 184,71 69,-14 139,-28 211,-43 -56,-21 -111,-42 -164,-63l34 -5c53,20 107,40 161,61 52,-11 105,-22 157,-34 -49,-18 -97,-36 -144,-54l28 -4 140 53c42,-9 83,-18 123,-28 -42,-16 -84,-31 -123,-46l21 -3c39,14 80,29 121,45 26,-6 51,-12 77,-18l12 5 -74 18c48,18 97,37 148,55l63 -23 12 4 -60 25 158 58 48 -30 12 5 -46 30 173 64 35 -35 12 4 -32 36 116 42 -4 10 -120 -43 -49 55 143 52 -6 13 -146 -54 -65 73 177 65 -7 17 -182 -68 -82 93c77,29 151,56 220,82l-8 20c-72,-27 -148,-55 -227,-84 -37,42 -69,79 -98,113 90,34 181,68 271,102l-10 27c-93,-35 -187,-71 -281,-106l-66 76 -51 -20c23,-22 47,-47 73,-73 -95,-36 -189,-72 -281,-108 -32,24 -61,46 -88,65l-54 -21c29,-18 61,-39 95,-62 -109,-42 -215,-84 -316,-124l-125 50 -57 -22 133 -48c-102,-40 -198,-79 -286,-115l-165 38 -58 -23 175 -34c-74,-31 -142,-59 -201,-84z"/>
					  <path fill="#00458E" d="M1098 408l107 0 0 -86 -107 0 0 86zm0 38l0 291 107 0 0 -291 -107 0z"/>
					  <path fill="#00458E" d="M1609 737c27,0 105,-1 137,-34 31,-31 26,-89 26,-121l0 -80c0,-27 4,-85 -26,-114 -35,-33 -89,-33 -134,-33l-186 0c-46,0 -100,-1 -134,33 -34,33 -32,87 -32,114l0 80c0,25 1,90 32,121 32,33 101,34 137,34l180 0zm-157 -93c-19,0 -54,3 -71,-25 -4,-6 -5,-25 -5,-47l0 -52c0,-24 1,-41 5,-46 17,-29 52,-25 71,-25l129 0c19,0 54,-4 70,25 4,5 6,25 6,46l0 52c0,21 -2,41 -6,47 -16,28 -51,25 -70,25l-129 0z"/>
					  <path fill="#1F1A17" d="M1875 719l44 0 0 -300 211 300 31 0 207 -302 0 302 44 0 0 -344 -69 0 -199 287 -199 -287 -70 0 0 344z"/>
					  <path fill="#1F1A17" d="M2485 719l333 0 0 -38 -288 0 0 -122 275 0 0 -36 -275 0 0 -110 287 0 0 -38 -332 0 0 344z"/>
					  <path fill="#1F1A17" d="M2897 375l0 344 255 0c35,0 109,1 140,-55 25,-45 27,-192 0,-238 -29,-50 -93,-51 -139,-51l-256 0zm44 38l208 0c20,0 82,-5 105,34 18,31 19,166 0,198 -23,40 -75,36 -105,36l-208 0 0 -268z"/>
					  <path fill="#1F1A17" d="M3389 719l44 0 0 -344 -44 0 0 344z"/>
					  <path fill="#1F1A17" d="M3914 415c-25,-40 -76,-39 -103,-40l-144 0c-44,0 -88,-1 -117,28 -27,28 -25,75 -25,103l0 73c0,39 1,84 28,112 32,32 82,28 114,28l146 0c35,0 74,-2 98,-28 22,-24 19,-71 19,-92l-41 0c0,21 0,44 -7,57 -14,23 -44,25 -69,25l-135 0c-18,0 -71,3 -91,-18 -19,-18 -18,-50 -18,-87l0 -66c0,-23 -3,-59 17,-80 18,-19 53,-17 86,-17l146 0c22,0 41,2 54,14 13,15 13,52 13,61l38 0c1,-26 0,-58 -9,-73z"/>
					  <path fill="#1F1A17" d="M4050 391c-44,25 -41,73 -41,122l0 69c0,36 -2,83 28,114 27,26 85,23 104,23l167 0c31,0 83,-2 108,-26 26,-26 26,-86 26,-105l0 -83c0,-34 2,-72 -29,-103 -30,-30 -80,-27 -112,-27l-158 0c-35,0 -68,2 -93,16zm41 25c16,-4 31,-3 60,-3l140 0c20,0 71,-3 90,17 17,17 17,45 17,80l0 63c0,42 -1,72 -17,88 -21,21 -60,20 -89,20l-133 0c-26,0 -66,3 -87,-18 -21,-21 -20,-59 -20,-90l0 -75c0,-33 -4,-67 39,-82z"/>
					  <path fill="#1F1A17" d="M4029 941l23 0 20 -35 126 0 21 35 23 0 -95 -160 -27 0 -91 160zm105 -143l52 91 -105 0 53 -91z"/>
					  <path fill="#1F1A17" d="M4435 802c-11,-19 -37,-21 -50,-21l-73 0c-19,0 -40,0 -53,13 -13,14 -12,37 -12,49l0 34c0,15 -1,38 13,51 14,14 42,13 52,13l75 0c15,0 32,-1 44,-13 13,-14 12,-40 12,-52l0 -15 -103 0 0 17 81 0c0,13 0,26 -3,32 -9,14 -21,14 -36,14l-67 0c-10,0 -30,1 -39,-7 -9,-10 -9,-29 -9,-47l0 -27c0,-14 0,-28 8,-37 9,-8 30,-7 42,-7l72 0c11,0 21,1 26,7 7,7 7,18 7,24l19 0c0,-9 -1,-21 -6,-28z"/>
					 </g>
					</svg>
				</fo:instream-foreign-object>		      
		      </fo:block>
		       -->		      
		    </fo:static-content>
		
		    <fo:flow flow-name="xsl-region-body">
		
		   <fo:block space-before.optimum="30pt" space-after.optimum="15pt" font-size="18pt">
		  		Einzelpatienten - Listing   
		  </fo:block>
		  
		  <fo:table border-collapse="collapse" table-layout="fixed">
		  			
		  <fo:table-column column-width="6cm"/>
		  <fo:table-column column-width="8cm"/>
		  <fo:table-column column-width="8cm"/>
		  
		  <fo:table-body>
		
	      <fo:table-row background-color="#FFFFFF">	      
		    <fo:table-cell border-color="black" border-width="0.5pt" border-style="none">
		      <fo:block text-align="left">
		   			Pat.-Nr. XXXXXX 
		      </fo:block>
		    </fo:table-cell>
		    <fo:table-cell border-color="black" border-width="0.5pt" border-style="none">
		      <fo:block text-align="left">
		  			Zentrum : XXXXXX
		      </fo:block>
		    </fo:table-cell>
		    <fo:table-cell border-color="black" border-width="0.5pt" border-style="none">
		      <fo:block text-align="left">
		  			Gewicht :  <xsl:value-of select="patientReport/patient/weight"/> kg
		      </fo:block>
		    </fo:table-cell>	
		  </fo:table-row>

	      <fo:table-row background-color="#FFFFFF">
		    <fo:table-cell border-color="black" border-width="0.5pt" border-style="none">
		      <fo:block text-align="left">
		   			Gesamtdosis : <xsl:value-of select="patientReport/patient/currentDose"/> mg
		      </fo:block>
		    </fo:table-cell>
		    <fo:table-cell border-color="black" border-width="0.5pt" border-style="none">
		      <fo:block text-align="left">
		  			Kostenträger : <xsl:value-of select="patientReport/insurance/name"/>
		      </fo:block>
		    </fo:table-cell>
		    <fo:table-cell border-color="black" border-width="0.5pt" border-style="none">
				<fo:block text-align="left">
					<xsl:choose>
						<xsl:when test="patientReport/patient/therapyConform='false'">
							Therapie zulassungskonform : Nein
						</xsl:when>
						<xsl:otherwise>
							Therapie zulassungskonform : Ja
						</xsl:otherwise>
					</xsl:choose>
				</fo:block>
		    </fo:table-cell>	
		  </fo:table-row>
		
		</fo:table-body>
	    </fo:table>	
	    
  	    <fo:block space-before.optimum="0pt" space-after.optimum="10pt" font-size="18pt">
  	      <fo:leader leader-length="27cm" leader-pattern="rule" rule-thickness="1pt" color="black"/>  	       
	    </fo:block>


		<xsl:variable name="lastAnzahlMedications">0</xsl:variable>

		<xsl:for-each select="patientReport/cycles">
		
			<xsl:variable name="anzahlMedications" select="count(./medications)"/>
			 			
			<xsl:if test="$anzahlMedications &gt; $lastAnzahlMedications">
			
				<xsl:variable name="lastAnzahlMedications">
					<xsl:value-of select="$anzahlMedications"></xsl:value-of>					
				</xsl:variable>
			
			</xsl:if>
				
		</xsl:for-each>



		<xsl:variable name="lastAnzahlCombinations">0</xsl:variable>

		<xsl:for-each select="patientReport/cycles">
		
			<xsl:variable name="anzahlCombinations" select="count(./combinations)"/>
			
			<xsl:if test="$anzahlCombinations &gt; $lastAnzahlCombinations">
			
				<xsl:variable name="lastAnzahlCombinations">
					<xsl:value-of select="$anzahlCombinations"></xsl:value-of>
				</xsl:variable>
			
			</xsl:if>
				
		</xsl:for-each>



		<!-- 
	    <xsl:variable name="anzahlMedications">
	    	<xsl:value-of select="$lastAnzahlMedications"></xsl:value-of>
	    </xsl:variable>
		<xsl:variable name="anzahlCombinations">
			<xsl:value-of select="lastAnzahlCombinations"></xsl:value-of>
		</xsl:variable>
 		-->



	    <xsl:variable name="anzahlMedications">
	    	6
	    </xsl:variable>
		<xsl:variable name="anzahlCombinations">
			6
		</xsl:variable>


		<!--  
	    <xsl:variable name="anzahlMedications" select="count(//medications)" />
		<xsl:variable name="anzahlCombinations" select="count(//combinations)" />
 		-->
 		
		<xsl:variable name="anzahl">
			<xsl:choose>
				<xsl:when test="$anzahlMedications &gt; $anzahlCombinations">
					<xsl:value-of select="$anzahlMedications"/>
				</xsl:when>
				<xsl:when test="$anzahlMedications &lt; $anzahlCombinations">
					<xsl:value-of select="$anzahlCombinations"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="$anzahlMedications"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>

		<fo:table border-collapse="collapse" table-layout="fixed">
		    
		<fo:table-column column-width="1.5cm"/>		    
	    
         <xsl:call-template name="createColumns">
               <xsl:with-param name="anzahl" select="$anzahl"/>
         </xsl:call-template>
		
		<fo:table-column column-width="4cm"/>
		<fo:table-column column-width="5cm"/>
		
		<fo:table-body>
 
	      <fo:table-row background-color="#9acd32">
		    <fo:table-cell border-color="black" border-width="0.5pt" border-style="none">
		      <fo:block text-align="left">
		      	Zyklus
		      </fo:block>
		    </fo:table-cell>

	         <xsl:call-template name="createHeader">
	               <xsl:with-param name="anzahl" select="$anzahl"/>
	         </xsl:call-template>

		    <fo:table-cell border-color="black" border-width="0.5pt" border-style="none">
		      <fo:block text-align="left">
		      	Substanz /
		      </fo:block>
		      <fo:block text-align="left">
		      	Kombinationspartner
		      </fo:block>
		    </fo:table-cell>
			 
		    <fo:table-cell border-color="black" border-width="0.5pt" border-style="none">
		      <fo:block text-align="left">
		      	Therapieunterbechungen /
		      </fo:block>
		      <fo:block text-align="left">
		      	Dosisreduktion Gründe
		      </fo:block>		      
		    </fo:table-cell>
	 		
		  </fo:table-row>
 			
    	  
	      <xsl:for-each select="patientReport/cycles">
	      
	      	<xsl:if test="count(./medications) > 0">
	      
		      	  <fo:table-row background-color="#EEEEEE">
	
	
				    <fo:table-cell border-color="black" border-width="0.5pt" border-style="solid">
				      <fo:block text-align="left">
				      	<xsl:value-of select="number"/>
				      </fo:block>
				    </fo:table-cell>
	
		      
		      	  <xsl:for-each select="./medications">
			      	  
					    <fo:table-cell border-color="black" border-width="0.5pt" border-style="solid">
					      <fo:block text-align="left">
					      	<xsl:value-of select="applicationDate"/>
					      </fo:block>
					      <fo:block text-align="left">
					       	<xsl:if test="value > 0">
					      		<xsl:value-of select="value"/> mg
					      	</xsl:if>
					      </fo:block>
					    </fo:table-cell>
					
	 			  </xsl:for-each>
	 			  
				  
				   
				 <xsl:if test="($anzahl + 1 - count(./medications)) > 1">	        
			         <xsl:call-template name="createDummyCell">
			               <xsl:with-param name="anzahl"><xsl:value-of select="$anzahl - count(./medications)"/></xsl:with-param>
			         </xsl:call-template> 			  
			    </xsl:if>
			    
				<xsl:if test="($anzahl + 1 - count(./medications)) = 1">	        
				    <fo:table-cell border-color="black" border-width="0.5pt" border-style="solid">
				      <fo:block text-align="left">
				      </fo:block>
				    </fo:table-cell>
			    </xsl:if>
	 				
	 				
			    <fo:table-cell border-color="black" border-width="0.5pt" border-style="solid">
			      <fo:block text-align="left">		      
			      	<xsl:value-of select="./medications[1]/parameter"/> (S)
			      </fo:block>		      
			    </fo:table-cell>
			    
			    <fo:table-cell border-color="black" border-width="0.5pt" border-style="solid">
			      <fo:block text-align="left">		      
					keine
			      </fo:block>		      
			    </fo:table-cell>
	 			  
	    	    </fo:table-row>
    	    
    	    </xsl:if>
    	    
    	    <xsl:if test="count(./combinations) > 0">
    	    
	       	    <fo:table-row background-color="#EEEEEE">
	
				    <fo:table-cell border-color="black" border-width="0.5pt" border-style="solid">
				      <fo:block text-align="left">
				      	<xsl:value-of select="number"/>
				      </fo:block>
				    </fo:table-cell>
	    	    
		    	   	<xsl:for-each select="./combinations">
		    	   	
		    	   	    <fo:table-cell border-color="black" border-width="0.5pt" border-style="solid">
					      <fo:block text-align="left">
					      	<xsl:value-of select="applicationDate"/>
					      </fo:block>
					      <fo:block text-align="left">
					      	<xsl:if test="value > 0">
					      		<xsl:value-of select="value"/> mg
					      	</xsl:if>
					      </fo:block>
					    </fo:table-cell>
		    	   	
		    	   	
		    	   	</xsl:for-each>
		    	   	
					 <xsl:if test="($anzahl + 1 -count(./combinations)) > 1">	        
				         <xsl:call-template name="createDummyCell">
				               <xsl:with-param name="anzahl"><xsl:value-of select="$anzahl - count(./combinations)"/></xsl:with-param>
				         </xsl:call-template> 			  
				    </xsl:if>
				    
					<xsl:if test="($anzahl + 1 - count(./combinations)) = 1">	        
					    <fo:table-cell border-color="black" border-width="0.5pt" border-style="solid">
					      <fo:block text-align="left">
					      </fo:block>
					    </fo:table-cell>
				    </xsl:if>
		    	   	
				    <fo:table-cell border-color="black" border-width="0.5pt" border-style="solid">
				      <fo:block text-align="left">		      
				      	<xsl:value-of select="./combinations[1]/parameter"/> (K)
				      </fo:block>		      
				    </fo:table-cell>
				    
				    <fo:table-cell border-color="black" border-width="0.5pt" border-style="solid">
				      <fo:block text-align="left">		      
						keine
				      </fo:block>		      
				    </fo:table-cell>
			    
	 			  
	    	    </fo:table-row>
	    	   	
			</xsl:if>
			 			    
	      </xsl:for-each>

		</fo:table-body>
	    </fo:table>
	      
	    </fo:flow>
	  </fo:page-sequence>
	</fo:root>

	</xsl:template>

	<xsl:template name="createColumns">
		
		<xsl:param name="anzahl"></xsl:param>
	    <xsl:param name="z">0</xsl:param>
	    
		<fo:table-column column-width="2.4cm"/>
				   
		 <xsl:if test="$z!=$anzahl">	        
	        <xsl:call-template name="createColumns">
		        <xsl:with-param name="anzahl" select="$anzahl"/>
	        	<xsl:with-param name="z" select="$z+1"/>
	    	</xsl:call-template>	
	    </xsl:if>
	      
	</xsl:template>

	<xsl:template name="createDummyCell">
		
		<xsl:param name="anzahl"></xsl:param>
	    <xsl:param name="z">0</xsl:param>
	    
	    <fo:table-cell border-color="black" border-width="0.5pt" border-style="solid">
	      <fo:block text-align="left">
	      </fo:block>
	    </fo:table-cell>
				   
		 <xsl:if test="$z!=$anzahl">	        
	        <xsl:call-template name="createDummyCell">
		        <xsl:with-param name="anzahl" select="$anzahl"/>
	        	<xsl:with-param name="z" select="$z+1"/>
	    	</xsl:call-template>	
	    </xsl:if>
	      
	</xsl:template>


	<xsl:template name="createHeader">
		
		<xsl:param name="anzahl"></xsl:param>
	    <xsl:param name="z">0</xsl:param>
	    
		   <fo:table-cell border-color="black" border-width="0.5pt" border-style="none">
		     <fo:block text-align="left">
		     	<xsl:value-of select="$z+1"></xsl:value-of>. Gabe
		     </fo:block>
			  <fo:block text-align="left">		     	
		     	Dosis 
		     </fo:block>
		   </fo:table-cell>
				   
		 <xsl:if test="$z!=$anzahl">	        
	        <xsl:call-template name="createHeader">
		        <xsl:with-param name="anzahl" select="$anzahl"/>
	        	<xsl:with-param name="z" select="$z+1"/>
	    	</xsl:call-template>	
	    </xsl:if>
	      
	</xsl:template>

</xsl:stylesheet>
