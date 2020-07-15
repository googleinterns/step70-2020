class MockVideoResponse {
  constructor() {
    this.result = 'result';
  }
}

QUnit.test('list-video-service - getPopularRequest', function(assert) {
  const regionCode = 'US';
  const result = getPopularRequest(regionCode);
  assert.equal(result.regionCode, regionCode, 'Expecting value US');
});

QUnit.test('list-video-service - API error', function(assert) {
  const done = assert.async();
  loadApi(function() {
    sinon.stub(gapi.client.youtube.videos, 'list');
    gapi.client.youtube.videos.list.returns(
      new Promise((resolve, reject) => {
        throw new Error('error message');
      })
    );
    getTrendingFromYoutubeApi().catch((err) => {
      assert.equal(err, YOUTUBE_API_ERROR, 'generic error is converted to specific error message');
      gapi.client.youtube.videos.list.restore();
      done();
    });
  });
});

QUnit.test('list-video-service - normal use', function(assert) {
  const done = assert.async();
  loadApi(function() {
    sinon.stub(gapi.client.youtube.videos, 'list');
    gapi.client.youtube.videos.list.returns(
      new Promise((resolve, reject) => {
        resolve(new MockVideoResponse());
      })
    );
    getTrendingFromYoutubeApi().then((response) => {
      assert.equal(response, 'result', 'expecting object.result = result');
      gapi.client.youtube.videos.list.restore();
      done();
    });
  });
});
