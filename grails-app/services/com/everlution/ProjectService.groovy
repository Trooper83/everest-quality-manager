package com.everlution

import com.everlution.command.RemovedItems
import grails.gorm.services.Service
import grails.gorm.transactions.Transactional

@Service(Project)
abstract class ProjectService implements IProjectService {

    AreaService areaService
    EnvironmentService environmentService

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
        return save(project)
    }
}
