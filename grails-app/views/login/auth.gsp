<!doctype html>
<html lang="en" class="no-js">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title><g:message code='springSecurity.login.title'/></title>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <asset:link rel="icon" href="favicon.ico" type="image/x-ico"/>

    <asset:stylesheet src="application.css"/>
    <asset:stylesheet src="signin.css"/>
</head>

<body>
<div class="container">
        <form action="${postUrl ?: '/login/authenticate'}" method="POST" id="loginForm" class="form-signin" autocomplete="off">
            <g:if test='${flash.message}'>
                <div class="alert alert-danger" role="alert">${flash.message}</div>
            </g:if>
            <h2 class="form-signin-heading"><g:message code='springSecurity.login.header'/></h2>
            <label for="email" class="sr-only">Email address</label>
            <input type="email" id="email" name="${usernameParameter ?: 'username'}" class="form-control"
                   placeholder="Email address" required="" autofocus="">
            <label for="password" class="sr-only">Password</label>
            <input type="password" id="password" name="${passwordParameter ?: 'password'}" class="form-control"
                   placeholder="Password" required="">
            <div class="checkbox">
                <label>
                    <input type="checkbox" value="remember-me" name="${rememberMeParameter ?: 'remember-me'}"
                           id="remember_me" <g:if test='${hasCookie}'>checked="checked"</g:if>> Remember me
                </label>
            </div>
            <button class="btn btn-lg btn-primary btn-block" type="submit" id="submit">Sign in</button>
        </form>
    </div>
</div>
</div>
<div id="spinner" class="spinner" style="display:none;">
    <g:message code="spinner.alt" default="Loading&hellip;"/>
</div>

<asset:javascript src="application.js"/>
</body>
</html>