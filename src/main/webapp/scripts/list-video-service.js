function getTrendingFromYoutubeApi() {
  // Ready in a later PR: let user choose country, currently defaulting to US
  return gapi.client.youtube.videos.list(getPopularRequest(getDomValue('region-select')))
  .then((response) => {
    return response.result;
  })
}

function getPopularRequest(regionCode) {
  return {
    'part': [
      'snippet,statistics'
    ],
    'chart': 'mostPopular',
    'maxResults': 24,
    'regionCode': regionCode
  };
}

function getVideoFromYoutubeApi(id) {
  return gapi.client.youtube.videos.list({
      'part': ['snippet,contentDetails,statistics'],
      'id': [id]
  })
  .then(function(response) {
    return response.result;
  });
}

export { getTrendingFromYoutubeApi, getVideoFromYoutubeApi}
