package com.everlution

import grails.gorm.services.Service
import grails.gorm.transactions.Transactional

@Service(TestGroup)
abstract class TestGroupService implements ITestGroupService {

    /**
     * removes all test cases from the group and
     * deletes the group
     * @param id
     */
    @Transactional
    void delete(Serializable id) {
        def group = get(id)
        if (group) {
            if (group.testCases) {
                def testCases = []
                testCases += group.testCases
                testCases.each {
                    group.removeFromTestCases(it)
                }
            }
            group.delete()
        }
    }

    /**
     * gets all groups in the domain with the associated project
     * @param projectId - id of the project
     * @return - list of all groups with the project
     */
    @Transactional
    List<TestGroup> findAllByProject(Project project) {
        return TestGroup.findAllByProject(project)
    }

    /**
     * gets all the test groups of the ids supplied
     */
    @Transactional
    List<TestGroup> getAll(List<Serializable> ids) {
        return TestGroup.getAll(ids)
    }
}
