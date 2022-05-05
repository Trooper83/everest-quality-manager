package com.everlution

class UrlMappings {

    static mappings = {
        "/project/$projectId/$controller/$action?/$id?"()
        "/projects"(controller: "project", action: "projects")
        "/project/home/$projectId"(controller: "project", action: "home")
        "/project/edit/$projectId"(controller: "project", action: "edit")
        group "/project/$projectId", {
            "/bugs"(controller: "bug", action: "bugs")
            "/releasePlans"(controller: "releasePlan", action: "releasePlans")
            "/scenarios"(controller: "scenario", action: "scenarios")
            "/testCases"(controller: "testCase", action: "testCases")
            "/testGroups"(controller: "testGroup", action: "testGroups")
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
