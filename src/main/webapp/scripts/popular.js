import { getRegions } from './region-selector.js';

function addRegionOptions() {
  return loadApi().then(() => getRegions())
  .then((response) => {
    for(const region of response.items) {
      addOptionToSelectList(region.snippet.gl, region.snippet.name, 'region-select');
    }
  });
}

export { addRegionOptions }
