<!doctype html>
<html lang="en" class="no-js">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <asset:link rel="shortcut icon" href="icons/favicon.png" type="image/x-icon"/>
    <title><g:message code='springSecurity.login.title'/></title>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>

    <asset:stylesheet src="application.css"/>
</head>

<body>
<div class="container height-100">
    <div class="row height-100 center">
        <div class="col-4">
        <form action="${postUrl ?: '/login/authenticate'}" method="POST" id="loginForm" autocomplete="off">
                <g:if test='${flash.message}'>
                    <div class="alert alert-danger" role="alert">${flash.message}</div>
                </g:if>
                <h2>Everest Quality Manager</h2>
                <div class="form-group mt-3">
                    <label for="email">Email address</label>
                    <input type="email" id="email" name="${usernameParameter ?: 'username'}" class="form-control"
                           placeholder="Email address" required="" autofocus="">
                </div>
                <div class="form-group mt-3">
                    <label for="password">Password</label>
                    <input type="password" id="password" name="${passwordParameter ?: 'password'}" class="form-control"
                           placeholder="Password" required="">
                </div>
                <div class="form-check mt-3">
                    <label>
                        <input class="form-check-input" type="checkbox" value="remember-me" name="${rememberMeParameter ?: 'remember-me'}"
                               id="remember_me" <g:if test='${hasCookie}'>checked="checked"</g:if>> Remember me
                    </label>
                </div>
                <button class="btn btn-primary btn-block mt-3" type="submit" id="submit">Sign in</button>
        </form>
        </div>
    </div>
    </div>
</div>
</div>
<div id="spinner" class="spinner" style="display:none;">
    <g:message code="spinner.alt" default="Loading&hellip;"/>
</div>

<asset:javascript src="application.js"/>
</body>
</html>