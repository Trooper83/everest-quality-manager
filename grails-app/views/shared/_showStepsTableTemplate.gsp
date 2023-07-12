<ul class="list-group list-group-flush" id="steps">
    <g:each in="${entity.steps}">
        <li class="list-group-item border-0">
            <div class="row">
                <p class="col-6">${it.action}</p>
                <p class="col-6">${it.result}</p>
            </div>
        </li>
    </g:each>
</ul>