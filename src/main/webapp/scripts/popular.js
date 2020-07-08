function loadPopular() {
  loadApi(function(){
    getPopular().then((response) => {
      for(item of response.items) {
        // Ready in a later PR: display the item nicely on the page
        console.log(item);
      }
    });
  });
}

function getPopular() {
  // Ready in a later PR: let user choose country, currently defaulting to US
  return gapi.client.youtube.videos.list(getPopularRequest('US'))
  .then(function(response) {
    return response.result;
  })
  .catch((err) => {
    updateDom('Oops! We were not able to get the videos from Youtube', 'popular-list-container');
    console.log('An error occurred when making API list request:', err);
  })
}

function getPopularRequest(regionCode) {
  return {
    'part': [
      'snippet,contentDetails,statistics'
    ],
    'chart': 'mostPopular',
    'maxResults': 12,
    'regionCode': regionCode
  };
}
