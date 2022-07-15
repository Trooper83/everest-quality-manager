<fieldset>
    <table>
        <thead>
        <tr>
            <th>Action</th>
            <th>Result</th>
        </tr>
        </thead>
        <tbody>
        <g:each in="${entity.steps}">
            <tr>
                <td>${it.action}</td>
                <td>${it.result}</td>
            </tr>
        </g:each>
        </tbody>
    </table>
</fieldset>