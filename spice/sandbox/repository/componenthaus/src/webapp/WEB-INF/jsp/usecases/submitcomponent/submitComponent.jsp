<form enctype="multipart/form-data" action="submitComponent.action" method="post">
    Upload component submission archive:<br>
    <input type="file" name="submission" value=""><br>
    <input type="submit" value="Upload">
</form>

Right, to submit a component, you need to do the following:

<ul>
  <li>Create a .car file.  There is an ant task to do this.  I have attached that in another jar.
  <li>Upload the .car here.  All going well, you'll have a submission.
</ul>