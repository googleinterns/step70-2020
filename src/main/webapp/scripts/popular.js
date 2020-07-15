function loadPopular() {
  loadApi(function() {
    getTrendingFromYoutubeApi()
    .then((response) => {
      for(item of response.items) {
        // Ready in a later PR: display the item nicely on the page
        console.log(item);
      }
    })
    .catch((error) => {
      updateDom(error.message, 'popular-list-container');
    });
  });
}
