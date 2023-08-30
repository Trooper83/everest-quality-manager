  const endpoint = 'http://localhost:8080/project/1/step/getSteps?q=step';

  const searchInput = document.querySelector('#linkedSteps');
  const card = document.querySelector(".search");

  searchInput.addEventListener("change", displayMatchedResults);
  searchInput.addEventListener("keyup", displayMatchedResults);

  const suggestions = [];

  function displayMatchedResults() {

    fetch(endpoint, {
      method: "GET",
    })
  	.then((blobdata) => blobdata.json())
    .then((data) => suggestions.push(...data));

    const searchTerm = this.value;
    const htmlToDisplay = suggestions
        .map((step) => {
            const regex = RegExp(searchTerm, "gi");
            const stepName = step.name.replace(regex, `<strong>${this.value}</strong>`);
            const html = `<li class='search-item'><span class='name'>${stepName}</span></li>`;
            return html;
        }).join("");

    if (searchTerm === "") {
        card.innerHTML = "";
    } else {
        card.innerHTML = htmlToDisplay;
    }
    suggestions.length = 0;
  }