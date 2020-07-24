function getRegions() {
  return gapi.client.youtube.i18nRegions.list({
    'part': [
      'snippet'
    ]
  })
  .then((response) => {
    return response.result;
  })
  .catch((error) => {
    console.error('An error occurred getting the region list:', error);
  });
}

export { getRegions };
