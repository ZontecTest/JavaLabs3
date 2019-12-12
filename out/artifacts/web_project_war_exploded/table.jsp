
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" href="css\\tableStyle.css">
</head>
<body>
<h1>Parser type: ${parserType}</h1>
<h1>${xmlStatus}</h1>
<table border="2px">
    <tr class="firstTableLine">
        <th>uId</th>
        <th>first name</th>
        <th>second name</th>
        <th>middle name</th>
        <th>count</th>
    </tr>
    <c:forEach var="person" items="${persons}">
        <tr class="personItem">
            <td> <c:out value="${person.getUid()}"/>        </td>
            <td> <c:out value="${person.getFirstName()}"/>  </td>
            <td> <c:out value="${person.getSecondName()}"/> </td>
            <td> <c:out value="${person.getMiddleName()}"/> </td>
            <td> <c:out value="${person.getCount()}"/>      </td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
