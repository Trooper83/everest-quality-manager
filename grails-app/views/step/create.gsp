<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'step.label', default: 'Step')}" />
    <title><g:message code="default.create.label" args="[entityName]" /></title>
</head>
<body>
<div class="container">
    <div class="row">
        <g:render template="/shared/sidebarTemplate" model="['name':project.name, 'code':project.code]"/>
        <main class="col-md-9 col-lg-10 ms-sm-auto mt-3">
            <g:render template="/shared/messagesTemplate" bean="${step}" var="entity"/>
            <g:form resource="${this.step}" method="POST" uri="/project/${project.id}/step/save" useToken="true">
                <div class="card mt-3">
                    <div class="card-header">
                        <h1>
                            <g:message code="default.create.label" args="[entityName]"/>
                        </h1>
                    </div>
                    <div class="card-body">
                        <g:hiddenField name="project" value="${project.id}"/>
                        <g:hiddenField name="isBuilderStep" value="true"/>
                        <div class="col-8 required mb-3">
                            <label class="form-label" for="name">Name</label>
                            <g:textField class="form-control" name="name" value="${step.name}" maxLength="255" autocomplete="off"></g:textField>
                        </div>
                        <div class="col-8 mb-3">
                            <label class="form-label" for="act">Action</label>
                            <g:textArea class="form-control" maxLength="500" name="act" value="${step.act}"/>
                        </div>
                        <div class="col-8 mb-3">
                            <label class="form-label" for="result">Result</label>
                            <g:textArea class="form-control" maxLength="500" name="result" value="${step.result}"/>
                        </div>
                        <div class="col-8 mb-3">
                            <label class="form-label" for="linkedSteps">Linked Steps</label>
                            <input class="form-control" id="linkedSteps" type="text" placeholder="Step Name" autocomplete="off"/>
                            <ul class="search"></ul>
                        </div>
                    </div>
                    <div class="card-footer">
                        <fieldset class="buttons">
                            <g:submitButton name="create" class="btn btn-primary" value="${message(code: 'default.button.create.label', default: 'Create')}" />
                        </fieldset>
                    </div>
                </div>
            </g:form>
        </main>
    </div>
</div>
<asset:javascript src="stepSuggestions.js"/>
<!--script>
const endpoint = 'http://localhost:8080/project/1/step/getSteps?q=step';
const suggestions = [];

function getResults() {
  fetch(endpoint, {
      method: "GET",
    })
  	.then((blobdata) => blobdata.json())
    .then((data) => suggestions.push(...data));
}


    function autocomplete(inp) {
    getResults();
    arr = suggestions;
  /*the autocomplete function takes two arguments,
  the text field element and an array of possible autocompleted values:*/
  let currentFocus;
  /*execute a function when someone writes in the text field:*/
  inp.addEventListener("input", function(e) {
      var a, b, i, val = this.value;
      /*close any already open lists of autocompleted values*/
      closeAllLists();
      if (!val) { return false;}
      currentFocus = -1;
      /*create a DIV element that will contain the items (values):*/
      a = document.createElement("DIV");
      a.setAttribute("id", this.id + "autocomplete-list");
      a.setAttribute("class", "autocomplete-items");
      /*append the DIV element as a child of the autocomplete container:*/
      this.parentNode.appendChild(a);
      /*for each item in the array...*/
      for (i = 0; i < arr.length; i++) {
        /*check if the item starts with the same letters as the text field value:*/
          /*create a DIV element for each matching element:*/
          b = document.createElement("DIV");
          /*make the matching letters bold:*/
          //b.innerHTML = "<strong>" + arr[i].substr(0, val.length) + "</strong>";
          //b.innerHTML += arr[i].substr(val.length);
          /*insert a input field that will hold the current array item's value:*/
          b.innerHTML += "<input type='hidden' value='" + arr[i] + "'>";
          /*execute a function when someone clicks on the item value (DIV element):*/
              b.addEventListener("click", function(e) {
              /*insert the value for the autocomplete text field:*/
              inp.value = this.getElementsByTagName("input")[0].value;
              /*close the list of autocompleted values,
              (or any other open lists of autocompleted values:*/
              closeAllLists();
          });
          a.appendChild(b);
      }
  });
  /*execute a function presses a key on the keyboard:*/
  inp.addEventListener("keydown", function(e) {
      var x = document.getElementById(this.id + "autocomplete-list");
      if (x) x = x.getElementsByTagName("div");
      if (e.keyCode == 40) {
        /*If the arrow DOWN key is pressed,
        increase the currentFocus variable:*/
        currentFocus++;
        /*and and make the current item more visible:*/
        addActive(x);
      } else if (e.keyCode == 38) { //up
        /*If the arrow UP key is pressed,
        decrease the currentFocus variable:*/
        currentFocus--;
        /*and and make the current item more visible:*/
        addActive(x);
      } else if (e.keyCode == 13) {
        /*If the ENTER key is pressed, prevent the form from being submitted,*/
        e.preventDefault();
        if (currentFocus > -1) {
          /*and simulate a click on the "active" item:*/
          if (x) x[currentFocus].click();
        }
      }
  });
  function addActive(x) {
    /*a function to classify an item as "active":*/
    if (!x) return false;
    /*start by removing the "active" class on all items:*/
    removeActive(x);
    if (currentFocus >= x.length) currentFocus = 0;
    if (currentFocus < 0) currentFocus = (x.length - 1);
    /*add class "autocomplete-active":*/
    x[currentFocus].classList.add("autocomplete-active");
  }
  function removeActive(x) {
    /*a function to remove the "active" class from all autocomplete items:*/
    for (var i = 0; i < x.length; i++) {
      x[i].classList.remove("autocomplete-active");
    }
  }
  function closeAllLists(elmnt) {
    /*close all autocomplete lists in the document,
    except the one passed as an argument:*/
    var x = document.getElementsByClassName("autocomplete-items");
    for (var i = 0; i < x.length; i++) {
      if (elmnt != x[i] && elmnt != inp) {
      x[i].parentNode.removeChild(x[i]);
    }
  }
}
/*execute a function when someone clicks in the document:*/
document.addEventListener("click", function (e) {
    closeAllLists(e.target);
});
}
</script>
<script>
autocomplete(document.getElementById("linkedSteps"));
</script-->
<!--script>
  let steps = new Bloodhound({
  datumTokenizer: Bloodhound.tokenizers.obj.whitespace('value'),
  queryTokenizer: Bloodhound.tokenizers.whitespace,
  remote: {
    url: 'http://localhost:8080/project/1/step/getSteps?q=%QUERY',
    wildcard: '%QUERY'
  }
});

$('#linkedSteps').typeahead(null, {
  name: 'steps-search',
  display: 'name',
  source: steps,
  minLength: 3,
  limit: 10,
});
</script-->
</body>
</html>