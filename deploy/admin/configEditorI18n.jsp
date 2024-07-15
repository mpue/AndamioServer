<%@page import="java.util.*" %>

<script type="text/javascript">	

	<%  ResourceBundle configBundle = ResourceBundle.getBundle("MessageResources",Locale.getDefault()); %>

    var sitenameKey        = "<%= configBundle.getString("form.config.sitename") %>";
    var admintemplateKey   = "<%= configBundle.getString("form.config.admintemplate") %>";
    var basepathKey        = "<%= configBundle.getString("form.config.basepath") %>";
    var usertemplateKey    = "<%= configBundle.getString("form.config.usertemplate") %>";
    var startnodeKey       = "<%= configBundle.getString("form.config.startnode") %>";
    var keywordKey         = "<%= configBundle.getString("form.config.keywords") %>";
    var hostnameKey        = "<%= configBundle.getString("form.config.hostname") %>";
    var maxuploadKey       = "<%= configBundle.getString("form.config.maxupload") %>";
    var contentpathKey     = "<%= configBundle.getString("form.config.contentpath") %>";
    var localpathKey       = "<%= configBundle.getString("form.config.localpath") %>";
    var downloadpathKey    = "<%= configBundle.getString("form.config.downloadpath") %>";
    var gallerypathKey     = "<%= configBundle.getString("form.config.gallerypath") %>";
    var imagepathKey       = "<%= configBundle.getString("form.config.imagepath") %>";
    var importpathKey      = "<%= configBundle.getString("form.config.importpath") %>";
    var productpathKey     = "<%= configBundle.getString("form.config.productimagepath") %>";
    var gallerypathKey     = "<%= configBundle.getString("form.config.gallerypath") %>";
    var maximageuploadKey  = "<%= configBundle.getString("form.config.maximageupload") %>";
    var maxAvatarHeightKey = "<%= configBundle.getString("form.config.maxAvatarHeight") %>";
    var maxAvatarWidthKey  = "<%= configBundle.getString("form.config.maxAvatarWidth") %>";
    var usernameKey        = "<%= configBundle.getString("form.config.username") %>";
    var passwordKey        = "<%= configBundle.getString("form.config.password") %>";
    var fromadressKey      = "<%= configBundle.getString("form.config.fromadress") %>";
    var smtphostKey        = "<%= configBundle.getString("form.config.smtphost") %>";
    var mainmenuKey        = "<%= configBundle.getString("button.mainmenu") %>";
    var saveKey            = "<%= configBundle.getString("button.save") %>";
    var changeWaitKey      = "<%= configBundle.getString("message.change.wait") %>";
    var changeSuccessKey   = "<%= configBundle.getString("message.change.success") %>";
    var changeFailKey      = "<%= configBundle.getString("message.change.fail") %>";
    var dialogSavedKey     = "<%= configBundle.getString("dialog.title.saved") %>";
    var dialogErrorKey     = "<%= configBundle.getString("dialog.title.error") %>";
    var dialogConfigKey    = "<%= configBundle.getString("dialog.title.config") %>";
    var titleGeneralKey    = "<%= configBundle.getString("form.config.title.general") %>";
    var titlePathsKey      = "<%= configBundle.getString("form.config.title.paths") %>";
    var titleImagesKey     = "<%= configBundle.getString("form.config.title.imageconfig") %>";
    var titleEmailKey      = "<%= configBundle.getString("form.config.title.mailconfig") %>";    
</script>
