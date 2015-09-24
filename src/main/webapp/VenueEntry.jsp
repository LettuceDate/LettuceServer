<%--
  Created by IntelliJ IDEA.
  User: ultradad
  Date: 9/23/15
  Time: 4:25 PM
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreServiceFactory" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreService" %>
<%@ page import="com.lettucedate.core.ActivityType" %>
<%@ page import="java.util.List" %>

<%
    BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
%>

<html>
<head>
    <title>LettuceDate Venue Entry</title>
</head>
<body>
<form action="<%= blobstoreService.createUploadUrl("/api/v1/admin/addvenue") %>" method="post" enctype="multipart/form-data">


    venue name: <input type="text" name="venuename"><p/>
    description: <input type="text" name="venuedesc"><p/>
    image: <input type="file" name="file"><p/>
    <%
        List<ActivityType>  typeList = ActivityType.GetActivityTypes();

        for (ActivityType curType : typeList) {
            %>
        <input type="checkbox" name="<%=curType.id%>"><%=curType.typename%><br>
    <%
        }
    %>
    <input type="submit" value="Submit">
</form>

</body>
</html>
