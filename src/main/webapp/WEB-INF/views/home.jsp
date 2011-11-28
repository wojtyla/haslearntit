<%@ page contentType="text/html;charset=UTF-8" language="java" session="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<html>
<head><title>Agile Development Day 2011</title></head>
<body>
<h1>Welcome to Agile Development Day 2011 application stub!</h1>
Just to see if jenkins automatic deployment really works...

<p>Write performance: <c:out value="${writeSpeed}"/> elements/sec</p>
<p>Read performance: <c:out value="${readSpeed}"/> elements/sec</p>

<hr />
<!-- TODO: MZA: Should be moved to the footer -->
<p><c:out value="${appInfoText}"/></p>
</body>
</html>