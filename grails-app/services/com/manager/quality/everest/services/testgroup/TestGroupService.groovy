package com.manager.quality.everest.services.testgroup

import com.manager.quality.everest.GroupWithPaginatedTests
import com.manager.quality.everest.SearchResult
import com.manager.quality.everest.domains.Project
import com.manager.quality.everest.domains.TestCase
import com.manager.quality.everest.domains.TestGroup
import com.manager.quality.everest.services.testcase.TestCaseService
import grails.gorm.services.Service
import grails.gorm.transactions.Transactional

@Service(TestGroup)
abstract class TestGroupService implements ITestGroupService {

    TestCaseService testCaseService

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
    SearchResult findAllByProject(Project project, Map params) {
        int c = TestGroup.countByProject(project)
        List groups = TestGroup.findAllByProject(project, params)
        return new SearchResult(groups, c)
    }

    /**
     * finds all groups in the project with a name
     * that contains the string
     * @param name - the string to search
     */
    @Transactional
    SearchResult findAllInProjectByName(Project project, String s, Map params) {
        int c = TestGroup.countByProjectAndNameIlike(project, "%${s}%")
        List groups = TestGroup.findAllByProjectAndNameIlike(project, "%${s}%", params)
        return new SearchResult(groups, c)
    }

    /**
     * gets all the test groups of the ids supplied
     */
    @Transactional
    List<TestGroup> getAll(List<Serializable> ids) {
        return TestGroup.getAll(ids)
    }

    /**
     * gets a test group with paginated test cases
     */
    @Transactional
    GroupWithPaginatedTests getWithPaginatedTests(Serializable id, Map params) {
        def group = get(id)
        List<TestCase> testList = []
        if (group) {
            def tests = testCaseService.getAllByGroup(id, params)
            testList.addAll(tests)
        }
        return new GroupWithPaginatedTests(group, testList)
    }
}
