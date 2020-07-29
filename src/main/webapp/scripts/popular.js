import { getTrendingFromYoutubeApi } from './list-video-service.js';
import { getRegions } from './region-selector.js';

function loadPopular() {
  return loadApi().then(popular.getTrendingFromYoutubeApi)
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
  return loadApi().then(popular.getRegions)
  .then((response) => {
    for(const region of response.items) {
      addOptionToSelectList(region.snippet.gl, region.snippet.name, 'region-select');
    }
  });
}

let popular = { loadPopular, getTrendingFromYoutubeApi, addRegionOptions, getRegions };
popular.addRegionOptions();
popular.loadPopular();

export default popular;
