<%--
  Created by IntelliJ IDEA.
  User: David
  Date: 10.01.2019
  Time: 21:52
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ page session="false" %>

<t:layout title="Persons Test" heading="Persons">
    <h2>
        Add a Person
    </h2>


    <form:form action="/person/add" method="post" modelAttribute="person">
        <table>
            <tr>
                <td>
                    <form:label path="name">
                        <spring:message text="Name"/>
                    </form:label>
                </td>
                <td>
                    <form:input path="name"/>
                </td>
            </tr>
            <tr>
                <td>
                    <form:label path="surname">
                        <spring:message text="Surname"/>
                    </form:label>
                </td>
                <td>
                    <form:input path="surname"/>
                </td>
            </tr>
            <tr>
                <td>
                    <form:label path="email">
                        <spring:message text="Email"/>
                    </form:label>
                </td>
                <td>
                    <form:input path="email"/>
                </td>
            </tr>
            <tr>
                <td>
                    <form:label path="phoneNumber">
                        <spring:message text="Phone Number"/>
                    </form:label>
                </td>
                <td>
                    <form:input path="phoneNumber"/>
                </td>
            </tr>
            <tr>
                <td>
                    <form:label path="password">
                        <spring:message text="Password"/>
                    </form:label>
                </td>
                <td>
                    <form:input path="password"/>
                </td>
            </tr>
            <tr>
                <td colspan="2">
                    <c:if test="${!empty person.name}">
                        <input type="submit"
                               value="<spring:message text="Edit Person"/>"/>
                    </c:if>
                    <c:if test="${empty person.name}">
                        <input type="submit"
                               value="<spring:message text="Add Person"/>"/>
                    </c:if>
                </td>
            </tr>
        </table>
    </form:form>
    <br>

    <h3>Persons List</h3>
    <c:if test="${!empty listPersons}">
        <table class="tg">
            <tr>
                <th width="80">ID</th>
                <th width="120">Name</th>
                <th width="120">Surname</th>
                <th width="120">Email</th>
                <th width="120">Phone number</th>
                <th width="60">Edit</th>
                <th width="60">Delete</th>
            </tr>
            <c:forEach items="${listPersons}" var="person">
                <tr>
                    <td>${person.id}</td>
                    <td>${person.name}</td>
                    <td>${person.surname}</td>
                    <td>${person.email}</td>
                    <td>${person.phoneNumber}</td>
                    <td><a href="<c:url value='/edit/${person.id}' />">Edit</a></td>
                    <td><a href="<c:url value='/remove/${person.id}' />">Delete</a></td>
                </tr>
            </c:forEach>
        </table>
    </c:if>
</t:layout>