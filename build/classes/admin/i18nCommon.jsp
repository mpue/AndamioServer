 <%@page import="java.util.*" %>
	
<%  ResourceBundle myResourceBundle = ResourceBundle.getBundle("MessageResources",Locale.getDefault()); %>

<script type="text/javascript">		

    var mainmenuKey          = "<%= myResourceBundle.getString("button.mainmenu") %>";
    var commonKey            = "<%= myResourceBundle.getString("common") %>";
    var fromKey              = "<%= myResourceBundle.getString("from") %>";    
    var detailsKey           = "<%= myResourceBundle.getString("details") %>";
    var cancelKey            = "<%= myResourceBundle.getString("cancel") %>";            
    var errorKey             = "<%= myResourceBundle.getString("error") %>";    
    var saveKey              = "<%= myResourceBundle.getString("button.save") %>";
    var addGroupKey          = "<%= myResourceBundle.getString("button.addgroup") %>";
    var removeGroupKey       = "<%= myResourceBundle.getString("button.removegroup") %>";
    var recordsKey           = "<%= myResourceBundle.getString("records") %>";
    var changeWaitKey        = "<%= myResourceBundle.getString("message.change.wait") %>";
    var savedKey             = "<%= myResourceBundle.getString("saved") %>";
    var saveWaitKey          = "<%= myResourceBundle.getString("message.save.wait") %>";    
    var changeSuccessKey     = "<%= myResourceBundle.getString("message.change.success") %>";
    var changeFailKey        = "<%= myResourceBundle.getString("message.change.fail") %>";
    var dialogSavedKey       = "<%= myResourceBundle.getString("dialog.title.saved") %>";
    var dialogErrorKey       = "<%= myResourceBundle.getString("dialog.title.error") %>";
    var emptyMessageKey      = "<%= myResourceBundle.getString("message.nodata") %>";
    var groupRightsKey       = "<%= myResourceBundle.getString("grouprights") %>";
    var availableGroupsKey   = "<%= myResourceBundle.getString("availablegroups") %>";
    var messageLoadingKey    = "<%= myResourceBundle.getString("message.loading") %>";
    var editKey              = "<%= myResourceBundle.getString("edit") %>";
    var refreshKey           = "<%= myResourceBundle.getString("refresh") %>";
    var deleteKey            = "<%= myResourceBundle.getString("delete") %>";
    var moveUpKey            = "<%= myResourceBundle.getString("moveup") %>";
    var moveDownKey          = "<%= myResourceBundle.getString("movedown") %>";
    var groupsKey            = "<%= myResourceBundle.getString("groups") %>";
    var nodesKey             = "<%= myResourceBundle.getString("nodes") %>";
    var noContentSelectedKey = "<%= myResourceBundle.getString("message.nocontentselected") %>"; 
    var pleaseSelectNodeKey  = "<%= myResourceBundle.getString("message.pleaseselectnode") %>";
    var contentsKey          = "<%= myResourceBundle.getString("contents") %>";
    var helpKey              = "<%= myResourceBundle.getString("help") %>";
</script>
