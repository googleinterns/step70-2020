const API_KEY = 'AIzaSyAr7MKPZKUCQ9G-N00I44EJqvsYjE3jAq';

function updateDom(text, containerId) {
  const containerDOM = document.getElementById(containerId);
  containerDOM.innerText = text;
}

function getDomValue(domId) {
  return document.getElementById(domId).value;
}
