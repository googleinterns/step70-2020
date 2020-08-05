import popular from '../popular.js';

const FAKE_ERROR = new Error('error message');

const mockGetTrendingResponse = {
  items: [{
    id: 'sampleID',
    statistics: { likeCount: 1},
    snippet: {
        title: 'Example Title',
        thumbnails: { medium: { url: 'sampleURL'}}
    }
  }
  ]
};

const mockGetRegionsResponse = {
  items: [
    { snippet: { gl: 'EG', name: 'Example'}}
  ]
}

QUnit.module("list videos", {
  afterEach: function() {
    sinon.restore();
  },
});

QUnit.test('loadPopular - normal use', function(assert) {
  const done = assert.async();
  document.getElementById('popular-list-container').innerHTML = '';
  const getTrendingFromYoutubeApi = sinon.fake.returns(
    Promise.resolve(mockGetTrendingResponse)
  );
  sinon.replace(popular, 'getTrendingFromYoutubeApi', getTrendingFromYoutubeApi);
  popular.loadPopular().then(() => {
    assert.dom('.card').exists({ count: 1 });
    done();
  });
});

QUnit.test('loadPopular - API error', function(assert) {
  const done = assert.async();
  document.getElementById('popular-list-container').innerHTML = '';
  const getTrendingFromYoutubeApi = sinon.fake.returns(
    Promise.reject(FAKE_ERROR)
  );
  sinon.replace(popular, 'getTrendingFromYoutubeApi', getTrendingFromYoutubeApi);
  popular.loadPopular().then(() => {
    assert.dom('#popular-list-container').hasText('An error occurred with YouTube');
    done();
  });
});

QUnit.test('region menu display',  function(assert) {
  const done = assert.async();
  const getRegions = sinon.fake.returns(
    Promise.resolve(mockGetRegionsResponse)
  );
  sinon.replace(popular, 'getRegions', getRegions);
  const loadApi = sinon.fake.returns(
    Promise.resolve()
  );
  popular.addRegionOptions().then(() => {
    assert.dom('option').hasText('Example');
    done();
  });
});
