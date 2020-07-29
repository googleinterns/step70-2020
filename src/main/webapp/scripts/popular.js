import { getTrendingFromYoutubeApi } from './list-video-service.js';

function loadPopular() {
  return loadApi().then(() => popular.getTrendingFromYoutubeApi()).then((response) => {
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

let popular = { loadPopular, getTrendingFromYoutubeApi };

popular.loadPopular();
export default popular;
