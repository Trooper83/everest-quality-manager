package com.everlution

interface IStepLinkService {

    void delete(Serializable id)

    List<StepLink> findAllByProjectAndChild(Project project, Step child)

    List<StepLink> findAllByProjectAndParent(Project project, Step parent)

    StepLink save(StepLink stepLink)

}