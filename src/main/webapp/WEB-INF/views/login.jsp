<%--
  Created by IntelliJ IDEA.
  User: David
  Date: 20.02.2019
  Time: 9:05
  To change this template use File | Settings | File Templates.

  <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/> CSRF TOKEN
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page session="false" contentType="text/html; charset=UTF-8" %>



<!DOCTYPE html>
<html lang="cs">

<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Bootstrap Login &amp; Register Templates</title>

    <!-- CSS -->
    <link rel="stylesheet" href="http://fonts.googleapis.com/css?family=Roboto:400,100,300,500">
    <link rel="stylesheet" href="/webjars/bootstrap/4.3.0/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.7.2/css/all.css" integrity="sha384-fnmOCqbTlWIlj8LyTjo7mOUStjsKC4pOpQbqyi7RrhN7udi9RwhKkMHpvLbHG9Sr" crossorigin="anonymous">
    <link rel="stylesheet" href="/css/signin.css">

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
    <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->

    <!--Icon-->
    <link rel="apple-touch-icon" sizes="180x180" href="/img/icon/apple-touch-icon.png">
    <link rel="icon" type="image/png" sizes="32x32" href="/img/icon/favicon-32x32.png">
    <link rel="icon" type="image/png" sizes="16x16" href="/img/icon/favicon-16x16.png">
    <link rel="manifest" href="/img/icon/site.webmanifest">
    <link rel="mask-icon" href="/img/icon/safari-pinned-tab.svg" color="#5bbad5">
    <meta name="msapplication-TileColor" content="#9f00a7">
    <meta name="theme-color" content="#ffffff">

</head>

<body>

<!-- Top content -->
<div class="top-content">

    <div class="inner-bg">
        <div class="container">

            <div class="row text-center">
                <div class="col-md-8 col-md-offset-2 text" style="float: none; margin: 0 auto;">
                    <h1><strong>Přihlášení</strong> a registrace</h1>
                    <div class="description">
                        <p>
                            K přístupu do systému je nutné se nejdříve <strong>přihlásit</strong>.
                            Pokud zatím nemáte účet je možné se ihned <strong>zaregistrovat</strong>.
                        </p>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="col-md-5">

                    <div class="form-box">
                        <div class="form-top">
                            <div class="form-top-left">
                                <h3>Přihlášení</h3>
                                <p>Zadej login a heslo:</p>
                            </div>
                            <div class="form-top-right">
                                <i class="fa fa-key"></i>
                            </div>
                        </div>
                        <div class="form-bottom">
                            <form role="form" action="" method="post" class="login-form">
                                <div class="form-group">
                                    <label class="sr-only" for="form-username">Username</label>
                                    <input type="text" name="form-username" placeholder="Login..." class="form-username form-control" id="form-username">
                                </div>
                                <div class="form-group">
                                    <label class="sr-only" for="form-password">Password</label>
                                    <input type="password" name="form-password" placeholder="Heslo..." class="form-password form-control" id="form-password">
                                </div>
                                <button type="submit" class="btn btn-primary">Přihlásit se</button>
                            </form>
                        </div>
                    </div>

                </div>

                <div class="col-md-1 middle-border"></div>
                <div class="col-md-1"></div>

                <div class="col-md-5">

                    <div class="form-box">
                        <div class="form-top">
                            <div class="form-top-left">
                                <h3>Registrace</h3>
                                <p>Vyplň formulář k vytvoření nového uživatele:</p>
                            </div>
                            <div class="form-top-right">
                                <i class="fa fa-pencil-alt"></i>
                            </div>
                        </div>
                        <div class="form-bottom">
                            <form:form action="/user/add" method="post" modelAttribute="user">
                                <div class="form-group">
                                    <form:label path="name" class="sr-only">First name</form:label>
                                    <form:input path="name" type="text" placeholder="Jméno..." class="form-control" />
                                    <div class="invalid-feedback">
                                        Name Error.
                                    </div>
                                </div>
                                <div class="form-group">
                                    <form:label path="surname" class="sr-only">Last name</form:label>
                                    <form:input path="surname" type="text" placeholder="Příjmení..." class="form-control" />
                                </div>
                                <div class="form-group">
                                    <form:label path="email" class="sr-only">Email</form:label>
                                    <form:input path="email" type="text" placeholder="Email..." class="form-control" />
                                </div>
                                <div class="form-group">
                                    <form:label path="phoneNumber" class="sr-only">Telephone number</form:label>
                                    <form:input path="phoneNumber" type="text" placeholder="Telefonní číslo..." class="form-control" />
                                </div>
                                <br>
                                <div class="form-group">
                                    <form:label path="password" class="sr-only">Password</form:label>
                                    <form:input path="password" type="password" placeholder="Heslo..." class="form-control" />
                                </div>
                                <div class="form-group">
                                    <input type="password" placeholder="Heslo znovu..." class="form-email form-control" id="psw2" />
                                </div>
                                <button type="submit" class="btn btn-warning">Zaregistrovat se</button>
                            </form:form>
                        </div>
                    </div>

                </div>
            </div>

        </div>
    </div>

</div>


<!-- Javascript -->
<script src="/webjars/jquery/3.3.1/jquery.min.js"></script>
<script src="/webjars/bootstrap/4.3.0/js/bootstrap.min.js"></script>
<script src="/js/jquery.backstretch.min.js"></script>

<script>

    jQuery(document).ready(function() {

        /*
            Fullscreen background
        */
        $.backstretch("/img/truck_01.jpg");

        /*
            Login form validation
        */
        $('.login-form input[type="text"], .login-form input[type="password"], .login-form textarea').on('focus', function() {
            $(this).removeClass('input-error');
        });

        $('.login-form').on('submit', function(e) {

            $(this).find('input[type="text"], input[type="password"], textarea').each(function(){
                if( $(this).val() == "" ) {
                    e.preventDefault();
                    $(this).addClass('input-error');
                }
                else {
                    $(this).removeClass('input-error');
                }
            });

        });

        /*
            Registration form validation
        */
        $('.registration-form input[type="text"], .registration-form textarea').on('focus', function() {
            $(this).removeClass('input-error');
        });

        $('.registration-form').on('submit', function(e) {

            $(this).find('input[type="text"], textarea').each(function(){
                if( $(this).val() == "" ) {
                    e.preventDefault();
                    $(this).addClass('input-error');
                }
                else {
                    $(this).removeClass('input-error');
                }
            });

        });


    });
</script>


</body>

</html>


