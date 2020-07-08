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
      alert('Oops! An error occured with Youtube, please try again later.');
      console.log('Was not able to load Google API client:', err);
    });
  });
}
