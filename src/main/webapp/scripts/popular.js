import { getTrendingFromYoutubeApi } from './list-video-service.js';
import { getRegions } from './region-selector.js';

function loadPopular() {
  return popular.getTrendingFromYoutubeApi()
  .then((response) => {
    for(const item of response.items) {
      // Ready in a later PR: display the item nicely on the page
      const containerDOM = document.getElementById('popular-list-container');
      containerDOM.innerText += item;
    }
  })
  .catch((error) => {
    updateDom('An error occured with YouTube', 'popular-list-container');
    console.error(error);
  });
}

/**
 * returns a promise that the region selector is updated
 * with a list of regions from Youtube API
 * @param {function} makes API call for regions list
 * @return {Promise}
 */
function addRegionOptions() {
  return popular.getRegions()
  .then((response) => {
    for(const region of response.items) {
      addOptionToSelectList(region.snippet.gl, region.snippet.name, 'region-select');
    }
  });
}

let popular = { loadPopular, getTrendingFromYoutubeApi, addRegionOptions, getRegions };
loadApi().then(() => {
  popular.addRegionOptions();
  popular.loadPopular();
});

const regionSelectorElement = document.getElementById('region-select');
regionSelectorElement.onchange = function(){
  //clear page to display new region's videos
  const popularListElement = document.getElementById('popular-list-container');
  while(popularListElement.hasChildNodes()){
    popularListElement.removeChild(popularListElement.firstChild);
  }
  popular.loadPopular();
}

export default popular;

/*
loadApi(function(){
  getCountries().then((response) => {
  for(const num of response.items) {
    makeCountrySelect(num.snippet);
  }
});
});

function loadPopular() {
  const listDom = document.getElementById('popular-list-container');
  listDom.innerHTML = "";
  loadApi(function(){
    getPopular().then((response) => {
      const recommended = new RecommendedVideoDisplay(listDom);
      for(item of response.items) {
        const video = new Video(item);
        recommended.addVideoCard(video.createVideoCard(null));
      }
    });
  });
}

function getPopular() {
  // Ready in a later PR: let user choose country, currently defaulting to US
  const country = document.getElementById('country').value;
  return gapi.client.youtube.videos.list(getPopularRequest(country))
  .then(function(response) {
    return response.result;
  })
  .catch((err) => {
    alert('Oops! We were not able to get the videos from Youtube');
    console.log('An error occurred when making API list request:', err);
  })
}

function getCountries() {
  return gapi.client.youtube.i18nRegions.list({
    "part": [
      "snippet"
    ],
    "hl": "es_MX"
  })
  .then(function(response) {
    return response.result;
  });
}

function getPopularRequest(regionCode) {
  return {
      "part": [
        "snippet,contentDetails,statistics"
      ],
      "chart": "mostPopular",
      'maxResults': 12,
      "regionCode": regionCode
  };
}

function makeCountrySelect(snippet){
  const countrySelectDOM = document.getElementById('country');
  const option = document.createElement('option');
  option.value = snippet.gl;
  option.innerText = snippet.name;
  countrySelectDOM.appendChild(option);
}

*/
