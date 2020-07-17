export { getRegions }

function getRegions() {
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
