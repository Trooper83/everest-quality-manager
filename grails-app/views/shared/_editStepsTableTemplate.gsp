<fieldset>
    <table class="table">
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
                <td><input type="button" value="Remove" onclick="removeEntryRow(this, ${step.id})" /></td>
            </tr>
        </g:each>
        </tbody>
    </table>
    <input id="btnAddRow" type="button" value="Add" onclick="addEntryRow()" />
</fieldset>