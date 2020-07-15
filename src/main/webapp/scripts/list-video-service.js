YOUTUBE_API_ERROR = new Error('Oops! We were not able to get the videos from Youtube')

function getTrendingFromYoutubeApi() {
  // Ready in a later PR: let user choose country, currently defaulting to US
  return gapi.client.youtube.videos.list(getPopularRequest('US'))
  .then((response) => {
    return response.result;
  })
  .catch((error) => {
    console.log('An error occurred when making API list request:', error);
    throw YOUTUBE_API_ERROR;
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
