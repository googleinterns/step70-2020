const API_KEY = '';

function updateDom(text, containerId) {
  const containerDOM = document.getElementById(containerId);
  containerDOM.innerText = text;
}

function getDomValue(domId) {
  return document.getElementById(domId).value;
}

function makeSelectList(value, text, selectId) {
  const selectionDOM = document.getElementById(selectId);
  const option = document.createElement('option');
  option.value = value;
  option.innerText = text;
  selectionDOM.appendChild(option);
}
