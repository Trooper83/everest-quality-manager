
const options = {
    year: "numeric",
    month: "long",
    day: "numeric",
    timeZone: Intl.DateTimeFormat().resolvedOptions().timeZone
};

(() => {
    const created = document.querySelectorAll('[data-name=createdDateValue]');
    created.forEach((ele) => {
      ele.innerText = getDateText(ele.innerText);
    });

    const updated = document.querySelectorAll('[data-name=updatedDateValue]');
    updated.forEach((ele) => {
        ele.innerText = getDateText(ele.innerText);
    });

    const executed = document.querySelectorAll('[data-name=executedDateValue]');
    executed.forEach((ele) => {
        ele.innerText = getDateText(ele.innerText);
    });
})();

function getDateText(date) {
    const d = new Date(date + 'Z');
    return d.toLocaleString('en-US', options);
}