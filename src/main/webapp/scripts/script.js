const API_KEY = '';
const YOUTUBE_DISCOVERY = 'https://www.googleapis.com/discovery/v1/apis/youtube/v3/rest';

/**
 * Loads any Google APIs listed in the discoveryDocs
 * @return {Promise} for completing the load
 */
function loadApi() {
  return new Promise((resolve, reject) => {
    gapi.load('client', () => {
      gapi.client.init({
        'apiKey': API_KEY,
        'discoveryDocs': [YOUTUBE_DISCOVERY]
      })
      .then(resolve)
      .catch((err) => {
        alert('Oops! An error occured with Youtube, please try again later.');
        console.error('Was not able to load Google API client:', err);
        reject();
      });
    });
  });
}

function updateDom(text, containerId) {
  const containerDOM = document.getElementById(containerId);
  containerDOM.innerText = text;
}

function getDomValue(domId) {
  return document.getElementById(domId).value;
}

function addOptionToSelectList(value, text, selectId) {
  const selectionDOM = document.getElementById(selectId);
  const option = document.createElement('option');
  option.value = value;
  option.innerText = text;
  selectionDOM.appendChild(option);
}
