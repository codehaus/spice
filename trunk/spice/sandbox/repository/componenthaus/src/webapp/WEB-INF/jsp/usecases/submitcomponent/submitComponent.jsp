<%@ include file="/WEB-INF/jsp/includes.jsp" %>

<span class="description_text">
<form enctype="multipart/form-data" action="submitComponent.action" method="post">
    Upload a component:<br>
    <input type="file" name="submission" class="input" value=""><br>
    <input type="submit" value="Upload">
</form>

To upload a component, you need to do the following:

<ul>
  <li>Create a .CAR file.  There is an <a href="anttask.html" class="description_text">ant task</a> to do this and also a <a href="anttask.html" class="description_text">maven plug-in</a>, with instructions for both.
  <li>Once you have created the .CAR file, click Browse to locate it on your harddisk, then click Upload to commit it to the repository.
</ul>
</span>