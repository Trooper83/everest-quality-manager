package com.everlution.services.project

import com.everlution.SearchResult
import com.everlution.controllers.command.RemovedItems
import com.everlution.domains.Project
import com.everlution.services.area.AreaService
import com.everlution.services.environment.EnvironmentService
import com.everlution.services.platform.PlatformService
import grails.gorm.services.Service
import grails.gorm.transactions.Transactional

@Service(Project)
abstract class ProjectService implements IProjectService {

    AreaService areaService
    EnvironmentService environmentService
    PlatformService platformService

    /**
     * finds all projects with a name that contains the string
     * @param name - the string to search
     */
    @Transactional
    SearchResult findAllByNameIlike(String s, Map params) {
        List projects = Project.findAllByNameIlike("%${s}%", params)
        int c = Project.countByNameIlike("%${s}%")
        return new SearchResult(projects, c)
    }

    /**
     * save an updated project, deletes any removed areas
     * @param project - the project to update
     * @param removedItems - ids of the items to remove
     * @return - the updated project
     */
    @Transactional
    Project saveUpdate(Project project, RemovedItems removedItems) {
        for(id in removedItems.areaIds) {
            def area = areaService.get(id)
            project.removeFromAreas(area)
        }
        for(id in removedItems.environmentIds) {
            def env = environmentService.get(id)
            project.removeFromEnvironments(env)
        }
        for(id in removedItems.platformIds) {
            def platform = platformService.get(id)
            project.removeFromPlatforms(platform) //TODO: test me
        }
        return save(project)
    }
}
