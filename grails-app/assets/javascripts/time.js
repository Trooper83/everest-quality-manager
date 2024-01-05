
const options = {
    year: "numeric",
    month: "long",
    day: "numeric",
    timeZone: Intl.DateTimeFormat().resolvedOptions().timeZone
};

(() => {
    const created = document.querySelectorAll('[data-name=createdDateValue]');
    created.forEach((ele) => {
        const text = ele.innerText;
        ele.innerText = text.length > 0 ? getDateText(text) : '';
    });

    const updated = document.querySelectorAll('[data-name=updatedDateValue]');
    updated.forEach((ele) => {
        const text = ele.innerText;
        ele.innerText = text.length > 0 ? getDateText(text) : '';
    });

    const executed = document.querySelectorAll('[data-name=executedDateValue]');
    executed.forEach((ele) => {
        const text = ele.innerText;
        ele.innerText = text.length > 0 ? getDateText(text) : '';
    });
})();

function getDateText(date) {
    const d = new Date(date + 'Z');
    return d.toLocaleString('en-US', options);
}