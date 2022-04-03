package com.everlution

class UrlMappings {

    static mappings = {
        "/projects"(controller: "project", action: "projects")
        group "/project/$projectId", {
            "/bugs"(controller: "bug", action: "bugs")
            "/home"(controller: "project", action: "home")
            group "/bug", {
                "/create"(controller:"bug", action:"create")
                "/edit/$id"(controller: "bug", action: "edit")
                "/show/$id"(controller: "bug", action: "show")
            }
        }
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }

        "/"(view:'/login/auth')
        "500"(view:'/error')
        "404"(view:'/notFound')
    }
}
