<ul class="list-group list-group-flush" id="steps">
    <g:each in="${entity.steps}">
        <li class="list-group-item border-0">
            <div class="row">
                <p class="col-4">${it.act}</p>
                <p class="col-4">${it.data}</p>
                <p class="col-4">${it.result}</p>
            </div>
        </li>
    </g:each>
</ul>