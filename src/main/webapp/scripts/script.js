const API_KEY = '';
const YOUTUBE_DISCOVERY = 'https://www.googleapis.com/discovery/v1/apis/youtube/v3/rest';

function loadApi(callback) {
  gapi.load('client', function() {
    gapi.client.init({
      'apiKey': API_KEY,
      'discoveryDocs': [YOUTUBE_DISCOVERY]
    })
    .then(function() {
      callback();
    })
    .catch((err) => {
      updateDom('Oops! An error occured with Youtube, please try again later.', 'popular-list-container');
      console.log('Was not able to load Google API client:', err);
    });
  });
}

function updateDom(text, containerId) {
  const containerDOM = document.getElementById(containerId);
  containerDOM.innerText = text;
}
