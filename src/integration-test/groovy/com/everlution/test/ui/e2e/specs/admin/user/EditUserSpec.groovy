package com.everlution.test.ui.e2e.specs.admin.user

import com.everlution.test.support.DataFactory
import com.everlution.test.ui.support.data.Credentials
import com.everlution.test.ui.support.data.SecurityRoles
import com.everlution.test.ui.support.data.UserStatuses
import com.everlution.test.ui.support.pages.admin.user.CreateUserPage
import com.everlution.test.ui.support.pages.admin.user.EditUserPage
import com.everlution.test.ui.support.pages.admin.user.SearchUserPage
import com.everlution.test.ui.support.pages.common.LoginPage
import geb.spock.GebSpec

class EditUserSpec extends GebSpec {

    void "user attributes are persisted and displayed on edit view"() {
        given:
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.APP_ADMIN.email, Credentials.APP_ADMIN.password)

        and:
        go "/user/create"
        def person = DataFactory.person()
        CreateUserPage createPage = browser.page(CreateUserPage)
        createPage.createPerson(person.email, person.password, [], [SecurityRoles.ROLE_BASIC.role])

        and:
        go "/user/search"
        def searchPage = browser.page(SearchUserPage)
        LinkedHashMap<String, String> map = []
        searchPage.search(person.email, map)
        searchPage.resultsTable.clickCell("Email", 0)

        when:
        EditUserPage page = browser.page(EditUserPage)
        def person1 = DataFactory.person()
        page.editPerson(person1.email, null,
                [UserStatuses.ACCOUNT_LOCKED.status, UserStatuses.ENABLED.status],
                [SecurityRoles.ROLE_APP_ADMIN.role])

        then:
        def edit = at EditUserPage
        edit.areRolesSelected([SecurityRoles.ROLE_BASIC.role, SecurityRoles.ROLE_APP_ADMIN.role])
        edit.areStatusesSelected([UserStatuses.ACCOUNT_LOCKED.status])
        !edit.areStatusesSelected([UserStatuses.ENABLED.status])
        edit.emailInput.text == person1.email
    }
}