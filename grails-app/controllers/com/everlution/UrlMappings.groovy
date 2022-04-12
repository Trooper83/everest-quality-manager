package com.everlution

class UrlMappings {

    static mappings = {
        "/projects"(controller: "project", action: "projects")
        group "/project/$projectId", {
            "/bugs"(controller: "bug", action: "bugs")
            "/home"(controller: "project", action: "home")
            "/releasePlans"(controller: "releasePlan", action: "releasePlans")
            "/scenarios"(controller: "scenario", action: "scenarios")
            "/testCases"(controller: "testCase", action: "testCases")
            "/testGroups"(controller: "testGroup", action: "testGroups")
            group "/bug", {
                "/create"(controller: "bug", action: "create")
                "/edit/$id"(controller: "bug", action: "edit")
                "/show/$id"(controller: "bug", action: "show")
            }
            group "/releasePlan", {
                "/create"(controller: "releasePlan", action: "create")
                "/edit/$id"(controller: "releasePlan", action: "edit")
                "/show/$id"(controller: "releasePlan", action: "show")
            }
            group "/scenario", {
                "/create"(controller: "scenario", action: "create")
                "/edit/$id"(controller: "scenario", action: "edit")
                "/show/$id"(controller: "scenario", action: "show")
            }
            group "/testCase", {
                "/create"(controller: "testCase", action: "create")
                "/edit/$id"(controller: "testCase", action: "edit")
                "/show/$id"(controller: "testCase", action: "show")
            }
            group "/testCycle", {
                "/create"(controller: "testCycle", action: "create")
                "/show/$id"(controller: "testCycle", action: "show")
            }
            group "/testIteration", {
                "/execute/$id"(controller: "testIteration", action: "execute")
                "/show/$id"(controller: "testIteration", action: "show")
            }
            group "/testGroup", {
                "/create"(controller: "testGroup", action: "create")
                "/edit/$id"(controller: "testGroup", action: "edit")
                "/show/$id"(controller: "testGroup", action: "show")
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
