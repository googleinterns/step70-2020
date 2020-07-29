function getTrendingFromYoutubeApi() {
  // Ready in a later PR: let user choose country, currently defaulting to US
  return gapi.client.youtube.videos.list(getPopularRequest('US'))
  .then((response) => {
    return response.result;
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

export { getTrendingFromYoutubeApi }
