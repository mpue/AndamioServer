/*
	* author: Logan Cai
	* Email: cailongqun (at) yahoo.com.cn
	* Website: www.phpletter.com
	* Created At: 21/April/2007
	* Modified At: 21/April/2007
*/

/**
* add over class to the specific table
*/
function tableRuler(element)
{
	
    var rows = $(element);
	
    $(rows).each(function(){
        $(this).mouseover(function(){
            $(this).addClass('over');
        });
        $(this).mouseout(function(){
            $(this).removeClass('over');
        });
    });
}


		 function getIEVersion( ) 
		 {
    	var ua = navigator.userAgent;
   	 	var IEOffset = ua.indexOf("MSIE ");
   		 return parseFloat(ua.substring(IEOffset + 5, ua.indexOf(";", IEOffset)));
		}			
/**
*check all checkboxs and uncheck all checkbox
*/
function checkAll(selectAllText, unselectAllText)
{
	var checkbox = $('#tickAll');
	if($(checkbox).attr('class') == "check_all")
	{
		$(checkbox).attr('class', 'uncheck_all');
		$(checkbox).attr('title', unselectAllText);
		$("#fileList tr[@id^=row] input[@type=checkbox]").each(function(i){
														   			$(this).attr("checked", 'checked');												 
																	 })			

	}else
	{
		$(checkbox).attr('class', 'check_all');
		$(checkbox).attr('title', selectAllText);	
		$("#fileList input[@type=checkbox][@checked]").each(function(i){
																		 $(this).removeAttr("checked");
																	 
																	 })	
	}
		
}
/**
*	show up the selected document information
*/
function setDocInfo(type, rowNum)
{
	if(type=="folder")
	{
		$('#folderPath').text($('#folderPath'+rowNum).val());
		$('#folderFile').text($('#folderFile'+rowNum).val());
		$('#folderSubdir').text($('#folderSubdir'+rowNum).val());
		$('#folderCtime').text($('#folderCtime'+rowNum).val());
		$('#folderMtime').text($('#folderMtime'+rowNum).val());
		if($('#folderReadable' + rowNum).val() == '1')
		{
			$('#folderReadable').html("<span class=\"flagYes\">&nbsp;</span>")	
		}else
		{
			$('#folderReadable').html("<span class=\"flagNo\">&nbsp;</span>")
		}
		if($('#folderWritable' + rowNum).val() == '1')
		{
			$('#folderWritable').html("<span class=\"flagYes\">&nbsp;</span>")	
		}else
		{
			$('#folderWritable').html("<span class=\"flagNo\">&nbsp;</span>")
		}	
		$('#folderFieldSet').css('display', '');
		$('#fileFieldSet').css('display', 'none');	
		$('#preview').html(msgNotPreview);
	}else
	{
		
		$('#selectedFileRowNum').val(rowNum);
		$('#fileName').text($('#fileName'+rowNum).val());
		$('#fileSize').text($('#fileSize'+rowNum).val());
		$('#fileType').text($('#fileType'+rowNum).val());
		$('#fileCtime').text($('#fileCtime'+rowNum).val());
		$('#fileMtime').text($('#fileMtime'+rowNum).val());
		if($('#fileReadable' + rowNum).val() == '1')
		{
			$('#fileReadable').html("<span class=\"flagYes\">&nbsp;</span>")	
		}else
		{
			$('#fileReadable').html("<span class=\"flagNo\">&nbsp;</span>")
		}
		if($('#fileWritable' + rowNum).val() == '1')
		{
			$('#fileWritable').html("<span class=\"flagYes\">&nbsp;</span>")	
		}else
		{
			$('#fileWritable').html("<span class=\"flagNo\">&nbsp;</span>")
		}	
		$('#folderFieldSet').css('display', 'none');
		$('#fileFieldSet').css('display', '');
		
		if($('#filePreview' + rowNum).val() == '1')
		{
			$("#loading")
			   .ajaxStart(function(){
				   $(this).show();
			   })
			   .ajaxComplete(function(){
				   $(this).hide();
			   });	
			switch($('#fileType'+rowNum).val())
			{
				case "image":
				case "txt":
				case "video":
				$("#preview").load(urlPreview + "?path=" + $('#filePath' + rowNum).val(),
				  function() {
					  }
				);					
					break;	
				default:
				//do nothing
			}	
			
		}else
		{
			$('#preview').html(msgNotPreview);
		}
		
	}
	
}

function selectFile(msgNoFileSelected)
{
	var selectedFileRowNum = $('#selectedFileRowNum').val();
  if(selectedFileRowNum != '' && $('#row' + selectedFileRowNum))
  {
	  var win = tinyMCE.getWindowArg("window");
	  // insert information now
	  var url = $('#fileUrl'+selectedFileRowNum).val();
	 if(typeof(TinyMCE_convertURL) != "undefined")
	 {
	 	url = TinyMCE_convertURL(url, null, true);
	 }else
	 {
	 	url = tinyMCE.convertURL(url, null, true);
	 	
	 }
	  
	  win.document.getElementById(tinyMCE.getWindowArg("input")).value = url;
	  
	  //for image browsers
	  if(typeof(win.preloadImg) != "undefined")
	  {
	  	//priview image
	  	win.showPreviewImage(url, false );
	  } 	  

	  // close popup window
	  tinyMCEPopup.close();	  	
  }else
  {
  	alert(msgNoFileSelected);
  }
  

}



function cancelSelectFile()
{
  // close popup window
  tinyMCEPopup.close();		
}
function windowRefresh()
{
	document.location.reload();
}
/**
* selecte documents and fire an ajax call to delete them
*/
function deleteDocuments(msgNoDocSelected, msgUnableToDelete, msgWarning)
{
	var selectedDoc = $('#fileList input[@type=checkbox][@checked]');
	var hiddenSelectedDoc = $('#selectedDoc');
	var selectedOptions;
	var isSelected = false;
	var currentPath = $('#formDelete input[@name=currentFolderPath]').val();
	//remove all options
	$(hiddenSelectedDoc).removeOption(/./);
	$(selectedDoc).each(function(i){
										$(hiddenSelectedDoc).addOption($(this).val(), $(this).val(), true);
										isSelected = true;
										 })		
	if(!isSelected)
	{
		alert(msgNoDocSelected);
	}
	else
	{//remove them via ajax call
		if(window.confirm(msgWarning))
		{
			$("#loading")
			   .ajaxStart(function(){
				   $(this).show();
			   })
			   .ajaxComplete(function(){
				   $(this).hide();
			   });	
			var options = 
			{ 
				dataType: 'json',
				error: function (data, status, e) 
				{
					alert(e);
				},				
				success:   function(data) 
				{ 
					//remove those selected items
										if(data.error != '')
										{
											alert(data.error);
										}else
										{
											$(selectedDoc).each(function(i)
											{
												$(this).parent().parent().remove();
											 })												
										}
										
					


				} 
			}; 
			$('#formDelete').ajaxSubmit(options); 	
						 				
		}
	}
	
	return false;	
}



function createFolder( msgNameFormat)
{
	
	var pattern=/^[A-Za-z0-9_ \-]+$/i;
	
	var folder = $('#new_folder');
	
	
	if(!pattern.test($(folder).val()))
	{
		alert(msgNameFormat);	
	}
	
	else
	{	

			$("#loading")
			   .ajaxStart(function(){
				   $(this).show();
			   })
			   .ajaxComplete(function(){
				   $(this).hide();
			   });	
			var options = 
			{ 
				dataType: 'json',
				error: function (data, status, e) 
				{

					alert(e);
				},				
				success:   function(data) 
				{ 
					//remove those selected items
										if(data.error != '')
										{
											alert(data.error);
										}else
										{
											//show up the new folder	
									var tbody = $('#fileList');
									var rows = $('#fileList tr');
									var newRowNum = $(rows).length;
									var cssRow = (newRowNum % 2?"even":"odd");
									var strCss = "left";
									var strDisabled = "";
									if(!data.is_writable)
									{
										strDisabled = " disabled";
										strCss = " leftDisabled";
									}									
									var folderRow = "";
									folderRow += '<tr class="' + cssRow + '" id="row'+ (newRowNum) +'" >';
									
									folderRow += '	<td onclick="setDocInfo(\'folder\', \''+ (newRowNum) +'\');"><input type="checkbox" name="check[]" id="check'+ (newRowNum) +'" value="' + data.name + '" ' + strDisabled +' />';
									folderRow += '		<input type="hidden" name="folderName'+ (newRowNum) +'" id="folderName'+ (newRowNum) +'" value="' + data.name + '" />';
									folderRow += '		<input type="hidden" name="folderPath'+ (newRowNum) +'" value="' + data.path + '" id="folderPath'+ (newRowNum) +'" />';
									folderRow += '		<input type="hidden" name="folderFile'+ (newRowNum) +'" value="' + data.file + '" id="folderFile'+ (newRowNum) +'" />';
									folderRow += '		<input type="hidden" name="folderSubdir'+ (newRowNum) +'" id="folderSubdir'+ (newRowNum) +'" value="' + data.subdir + '" />';
									folderRow += '		<input type="hidden" name="folderCtime'+ (newRowNum) +'" id="folderCtime'+ (newRowNum) +'" value="' + data.ctime + '" />';
									folderRow += '		<input type="hidden" name="folderMtime'+ (newRowNum) +'" id="folderMtime'+ (newRowNum) +'" value="' + data.mtime + '" />';
									folderRow += '		<input type="hidden" name="fileReadable'+ (newRowNum) +'" id="folderReadable'+ (newRowNum) +'" value="' + data.is_readable + '" />';
									folderRow += '		<input type="hidden" name="folderWritable'+ (newRowNum) +'" id="folderWritable'+ (newRowNum) +'" value="' + data.is_writable + '" />';
									folderRow += '		<input type="hidden" name="itemType'+ (newRowNum) +'" id="itemType'+ (newRowNum) +'" value="folder" />';
									folderRow += '	</td>';
									folderRow += '	<td><a href="' + data.url + '" title="' + data.tip + '"><span class="folderEmpty">&nbsp;</span></a></td>';
									folderRow += '	<td class="' + strCss +'" id="' + data.path + '">' + data.name + '</td>';
									folderRow += '	<td >&nbsp;</td>';
									folderRow += '	<td>' + data.mtime + '</td>';
									folderRow += '</tr>';	
									$(folderRow).appendTo('#fileList');
									tableRuler('#row'+ newRowNum);
									
									 $('#row'+ newRowNum +  ' td.left').editable("ajax_save_image_name.php", 
									 { 
												 submit    : 'Save',
												 width	   : '150',
												 height    : '14',
												 loadtype  : 'POST',
												 event	   :  'dblclick',
												 indicator : "<img src='images/loading.gif'>",
												 tooltip   : data.tipedit
									 });		 									
										}
										
					


				} 
			}; 
			$('#formNewFolder').ajaxSubmit(options); 	
						 				
				
	}
	return false;
	
}

function uploadFile(msgNameFormat, msgFileEmpty)
{

	var pattern=/^.*[\/|\\][A-Za-z0-9_\-. ]+\.[A-Za-z0-9]+$/i;
	
	var file = $('#new_file');
	if ($(file).val() == "")
	{
		alert(msgFileEmpty);
	}
	/*
	else if(!pattern.test($(file).val()))
	{
		alert(msgNameFormat+':'+$(file).val());	
	}
	*/
	else
	{
		

			$("#loading")
			   .ajaxStart(function(){
				   $(this).show();
			   })
			   .ajaxComplete(function(){
				   $(this).hide();
			   });	
			var options = 
			{ 
				dataType: 'json',
				error: function (data, status, e) 
				{
					alert(e);
				},				
				success: function(data) 
				{ 
					//remove those selected items
										if(typeof(data.error) == 'undefined')
										{
											alert('Unexpected information ');
										}
										else if(data.error != '')
										{
											
											alert(data.error);
										}else
										{
											
											//show up the new folder	
												var tbody = $('#fileList');
												var rows = $('#fileList tr');
												var cssRow = ($(rows).length) % 2?"even":"odd";
												var newRowNum = $(rows).length;
												var fileRow = "";
												var strCss = "left";
												var strDisabled = "";
												if(!data.is_writable)
												{
													strDisabled = " disabled";
													strCss = " leftDisabled";
												}
												
								fileRow +='<tr class="' + cssRow +'" id="row' + newRowNum + '"  >';
								fileRow +='<td onclick="setDocInfo(\''+ data.type + '\', \'' + newRowNum + '\');"><input type="checkbox" name="check[]" id="check' + newRowNum + '" value="'+ data.name + '"' + strDisabled + ' />';
								fileRow +='		<input type="hidden" name="fileName' + newRowNum + '" value="'+ data.name + '" id="fileName' + newRowNum + '" />';
								fileRow +='		<input type="hidden" name="fileSize' + newRowNum + '" value="'+ data.size + '" id="fileSize' + newRowNum + '" />'
								fileRow +='		<input type="hidden" name="fileType' + newRowNum + '" value="'+ data.fileType + '" id="fileType' + newRowNum + '" />';
								fileRow +='		<input type="hidden" name="fileCtime' + newRowNum + '" id="fileCtime' + newRowNum + '" value="'+ data.ctime + '" />';
								fileRow +='		<input type="hidden" name="fileMtime' + newRowNum + '" id="fileMtime' + newRowNum + '" value="'+ data.mtime + '" />';
								fileRow +='		<input type="hidden" name="fileReadable' + newRowNum + '" id="fileReadable' + newRowNum + '" value="'+ data.is_writable + '" />';
								fileRow +='		<input type="hidden" name="fileWritable' + newRowNum + '" id="fileWritable' + newRowNum + '" value="'+ data.is_readable + '" />';
								fileRow +='			<input type="hidden" name="filePreview' + newRowNum + '" id="filePreview' + newRowNum + '" value="'+ data.preview + '" />';
								fileRow +='			<input type="hidden" name="filePath' + newRowNum + '" id="filePath' + newRowNum + '" value="'+ data.path + '" />	';							
								fileRow +='<input type="hidden" name="fileUrl' + newRowNum + '" id="fileUrl' + newRowNum + '" value="' + data.url + '" />';
								fileRow +='	</td>';
								fileRow +='	<td><a href="'+ data.path + '" target="_blank"><span class="'+ data.cssClass + '">&nbsp;</span></a></td>';
								fileRow +='	<td class="' + strCss +'" id="'+ data.path + '">'+ data.name + '</td>';
								fileRow +='	<td >'+ data.size + '</td>';
								fileRow +='	<td>'+ data.mtime + '</td>';
								fileRow +='</tr>	';		
												$(fileRow).appendTo('#fileList');
												tableRuler('#row'+ newRowNum);
												//$('#row'+ newRowNum + ":first-child").click();
												 $('#row'+ newRowNum +  ' td.left').editable("ajax_save_image_name.php", 
												 { 
															 submit    : 'Save',
															 width	   : '150',
															 height    : '14',
															 loadtype  : 'POST',
															 event	   :  'dblclick',
															 indicator : "<img src='images/loading.gif'>",
															 tooltip   : data.tipedit
												 });		 									
										}
										
					


				} 
			}; 
			$('#formFile').ajaxSubmit(options); 	
						 				
				
	}
	return false;	
	
}


