function makeSelectList(value, text, selectId) {
  const selectionDOM = document.getElementById(selectId);
  const option = document.createElement('option');
  option.value = value;
  option.innerText = text;
  selectionDOM.appendChild(option);
}
