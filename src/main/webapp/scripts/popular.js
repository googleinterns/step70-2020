import { getTrendingFromYoutubeApi } from './list-video-service.js';
import { getRegions } from './region-selector.js';
import { VideoListDisplay } from './classes/recommended-video-display-class.js';
import { Video } from './classes/video-class.js';

function loadPopular() {
  return popular.getTrendingFromYoutubeApi()
  .then((response) => {
    const recommended = new VideoListDisplay('popular-list-container');
    for(const item of response.items) {
      const video = new Video(item, null);
      recommended.addVideo(video);
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
const regionSelectorElement = document.getElementById('region-select');
regionSelectorElement.onchange = function(){
  //clear page to display new region's videos
  const popularListElement = document.getElementById('popular-list-container');
  while(popularListElement.hasChildNodes()){
    popularListElement.removeChild(popularListElement.firstChild);
  }
  popular.loadPopular();
}

displayLoading('popular-list-container');
loadApi().then(() => {
  popular.addRegionOptions();
  popular.loadPopular();
});

export default popular;
