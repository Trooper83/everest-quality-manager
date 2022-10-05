<fieldset>
    <table>
        <thead>
        <tr>
            <th></th>
            <th>Action</th>
            <th>Result</th>
            <th></th>
        </tr>
        </thead>
        <tbody id="stepsTableContent">
        <g:each status="i" var="step" in="${entity.steps}">
            <tr>
                <td><g:hiddenField name="stepsIndex[${i}]" class="iHidden" /></td>
                <td><g:field type="text" name="steps[${i}].action" value="${step.action}" /></td>
                <td><g:field type="text" name="steps[${i}].result" value="${step.result}" /></td>
                <td>
                    <g:if test="${i == entity.steps.size() - 1}">
                        <input type="button" value="Remove" onclick="removeEntryRow(this, ${step.id})" />
                    </g:if>
                    <g:else>
                        <input type="button" value="Remove" style="display: none;" onclick="removeEntryRow(this, ${step.id})" />
                    </g:else>
                </td>
            </tr>
        </g:each>
        </tbody>
    </table>
    <input id="btnAddRow" type="button" value="Add" onclick="addEntryRow()" />
</fieldset>