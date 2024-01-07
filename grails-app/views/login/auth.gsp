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
<section class="vh-100" style="background-color: #1d0b45;">
    <div class="container py-5 h-100">
        <div class="row d-flex justify-content-center align-items-center h-100">
            <div class="col-12 col-md-8 col-lg-6 col-xl-5">
                <div class="card shadow-2-strong" style="border-radius: 1rem;">
                    <div class="card-body p-5">

                        <h3 class="mb-5 text-center">Everest Quality Manager</h3>

                        <form action="${postUrl ?: '/login/authenticate'}" method="POST" id="loginForm" autocomplete="off">
                            <g:if test='${flash.message}'>
                                <div class="alert alert-danger" role="alert">${flash.message}</div>
                            </g:if>

                            <div class="form-group mt-3">
                                <label for="email">Email address</label>
                                <input type="email" id="email" name="${usernameParameter ?: 'username'}" class="form-control"
                                       required="" autofocus="">
                            </div>
                            <div class="form-group mt-3">
                                <label for="password">Password</label>
                                <input type="password" id="password" name="${passwordParameter ?: 'password'}" class="form-control"
                                       required="">
                            </div>
                            <div class="d-grid gap-2 col-6 mx-auto mt-4">
                                <button class="btn btn-primary" type="submit" id="submit">Login</button>
                            </div>
                        </form>

                    </div>
                </div>
            </div>
        </div>
    </div>
</section>
<asset:javascript src="application.js"/>
</body>
</html>