import { getRegions } from './region-selector.js';

/**
 * returns a promise that the region selector is updated
 * with a list of regions from Youtube API
 * @param {function} makes API call for regions list
 * @return {Promise}
 */
function addRegionOptions(getRegions) {
  return loadApi().then(getRegions)
  .then((response) => {
    for(const region of response.items) {
      addOptionToSelectList(region.snippet.gl, region.snippet.name, 'region-select');
    }
  });
}

export { addRegionOptions }
