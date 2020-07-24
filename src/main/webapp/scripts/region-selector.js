/**
 * Makes a call to the Youtube API to get the region list
 * @return {Promise} for API response results
 */
function getRegions() {
  return gapi.client.youtube.i18nRegions.list({
    'part': [
      'snippet'
    ]
  })
  .then((response) => response.result)
  .catch((error) => {
    console.error('An error occurred getting the region list:', error);
  });
}

export { getRegions };
