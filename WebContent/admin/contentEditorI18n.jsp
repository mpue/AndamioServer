 <%@page import="java.util.*" %>
	
<%  ResourceBundle editorBundle = ResourceBundle.getBundle("MessageResources",Locale.getDefault()); %>


<script type="text/javascript">		
    var dialogDeleteNodekey       = "<%= editorBundle.getString("dialog.message.deletenode") %>";
	var dialogDeleteNodeTitleKey  = "<%= editorBundle.getString("dialog.title.deletenode") %>";
	var dialogContinueKey         = "<%= editorBundle.getString("dialog.message.continue") %>";
	var dialogSavingContentKey    = "<%= editorBundle.getString("dialog.title.savingcontent") %>";
	var dialogWarningTitleKey     = "<%= editorBundle.getString("dialog.message.warning") %>";
	var descriptionKey            = "<%= editorBundle.getString("table.content.description") %>";
	var dialogParentKey           = "<%= editorBundle.getString("dialog.label.parent") %>";
	var dialogCommonKey           = "<%= editorBundle.getString("dialog.title.common") %>";
	var nameKey                   = "<%= editorBundle.getString("table.content.name") %>";
	var newContentKey             = "<%= editorBundle.getString("button.new.content") %>";
	var newPluginKey              = "<%= editorBundle.getString("button.new.plugin") %>";
	var newLinkKey                = "<%= editorBundle.getString("button.new.link") %>";
	var previewKey                = "<%= editorBundle.getString("button.preview") %>";
	var importContentsKey         = "<%= editorBundle.getString("button.contents.import") %>";
	var exportContentsKey         = "<%= editorBundle.getString("button.contents.export") %>";
	var contentNameKey            = "<%= editorBundle.getString("form.content.name") %>";
	var contentDescriptionKey     = "<%= editorBundle.getString("form.content.description") %>";
	var contentPagenameKey        = "<%= editorBundle.getString("form.content.pagename") %>";
	var contentPagetitleKey       = "<%= editorBundle.getString("form.content.pagetitle") %>";
	var linkNameKey				  = "<%= editorBundle.getString("form.link.name") %>";
	var linkUrlKey                = "<%= editorBundle.getString("form.link.url") %>";
	var targetNameKey             = "<%= editorBundle.getString("form.link.targetName") %>";
	var dialogSavingLinkKey       = "<%= editorBundle.getString("dialog.title.savingLink") %>";
	var configKey                 = "<%= editorBundle.getString("dialog.title.config") %>";
	var contentErrorValidationKey = "<%= editorBundle.getString("error.content.create.invalidform") %>";
	var pluginErrorValidationKey  = "<%= editorBundle.getString("error.content.create.invalidform") %>";	
	var messageSavedKey           = "<%= editorBundle.getString("message.saved") %>";
	var backgroundImageKey        = "<%= editorBundle.getString("form.plugin.backgroundimage") %>";
	var parameterKey              = "<%= editorBundle.getString("form.plugin.parameter") %>";
	var jumpToFirstChildKey       = "<%= editorBundle.getString("form.plugin.firstchild") %>";
</script>
